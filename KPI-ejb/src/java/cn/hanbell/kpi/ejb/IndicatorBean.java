/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.SuperEJB;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.commons.beanutils.BeanUtils;
import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.entity.IndicatorAssignment;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorSet;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class IndicatorBean extends SuperEJBForKPI<Indicator> {

    @EJB
    private IndicatorAssignmentBean indicatorAssignmentBean;

    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;

    @EJB
    private IndicatorDetailBean indicatorDetailBean;

    @EJB
    private IndicatorDailyBean indicatorDailyBean;

    @EJB
    private IndicatorSetBean indicatorSetBean;

    protected Actual actualInterface;
    protected Actual otherInterface;

    public IndicatorBean() {
        super(Indicator.class);
    }

    public void addValue(IndicatorDetail a, IndicatorDetail b, String formKind) {
        // 先算汇总字段再算每月字段,A和S类型会重算汇总
        switch (formKind) {
            case "M":
            case "D":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                a.setN01(a.getN01().add(b.getN01()));
                a.setN02(a.getN02().add(b.getN02()));
                a.setN03(a.getN03().add(b.getN03()));
                a.setN04(a.getN04().add(b.getN04()));
                a.setN05(a.getN05().add(b.getN05()));
                a.setN06(a.getN06().add(b.getN06()));
                a.setN07(a.getN07().add(b.getN07()));
                a.setN08(a.getN08().add(b.getN08()));
                a.setN09(a.getN09().add(b.getN09()));
                a.setN10(a.getN10().add(b.getN10()));
                a.setN11(a.getN11().add(b.getN11()));
                a.setN12(a.getN12().add(b.getN12()));
                break;
            case "Q":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                a.setNq4(a.getNq4().add(b.getNq4()));
                a.setNq3(a.getNq3().add(b.getNq3()));
                a.setNq2(a.getNq2().add(b.getNq2()));
                a.setNq1(a.getNq1().add(b.getNq1()));
                break;
            case "H":
                a.setNfy(a.getNfy().add(b.getNfy()));
                a.setNh2(a.getNh2().add(b.getNh2()));
                a.setNh1(a.getNh1().add(b.getNh1()));
                break;
            case "Y":
                a.setNfy(a.getNfy().add(b.getNfy()));
        }
    }

    @Override
    public void delete(Indicator entity, HashMap<SuperEJB, List<?>> detailDeleted) {
        super.delete(entity, detailDeleted);
        indicatorDetailBean.deleteByPId(entity.getId());
    }

    public void deleteByFormidYearAndDeptno(String formid, int y, String value) {
        Indicator i = findByFormidYearAndDeptno(formid, y, value);
        if (i != null) {
            indicatorDetailBean.deleteByPId(i.getId());
            indicatorDepartmentBean.deleteByPId(i.getId());
            delete(i);
        }
    }

    public void deleteByPIdAndYear(int pid, int y) {
        List<Indicator> data = findByPIdAndYear(pid, y);
        if (data != null) {
            for (Indicator i : data) {
                indicatorDetailBean.deleteByPId(i.getId());
                indicatorDepartmentBean.deleteByPId(i.getId());
                delete(i);
            }
        }
    }

    public void divideByRate(Indicator i, int scale) {
        divideByRate(i.getActualIndicator(), i.getRate(), scale);
        divideByRate(i.getBenchmarkIndicator(), i.getRate(), scale);
        divideByRate(i.getForecastIndicator(), i.getRate(), scale);
        divideByRate(i.getTargetIndicator(), i.getRate(), scale);
        if (i.getOther1Indicator() != null) {
            divideByRate(i.getOther1Indicator(), i.getRate(), scale);
        }
        if (i.getOther2Indicator() != null) {
            divideByRate(i.getOther2Indicator(), i.getRate(), scale);
        }

    }

    public void divideByRate(IndicatorDetail id, BigDecimal rate, int scale) {
        getEntityManager().clear();
        // 先算汇总字段再算每月字段,A和S类型会重算汇总
        id.setNfy(id.getNfy().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNh2(id.getNh2().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNh1(id.getNh1().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq4(id.getNq4().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq3(id.getNq3().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq2(id.getNq2().divide(rate, scale, RoundingMode.HALF_UP));
        id.setNq1(id.getNq1().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN01(id.getN01().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN02(id.getN02().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN03(id.getN03().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN04(id.getN04().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN05(id.getN05().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN06(id.getN06().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN07(id.getN07().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN08(id.getN08().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN09(id.getN09().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN10(id.getN10().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN11(id.getN11().divide(rate, scale, RoundingMode.HALF_UP));
        id.setN12(id.getN12().divide(rate, scale, RoundingMode.HALF_UP));
    }

    public List<Indicator> findByCategoryAndYear(String c, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByCategoryAndSeq");
        query.setParameter("category", c);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByDeptnoObjtypeAndYear(String d, String t, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByDeptnoObjtypeAndYear");
        query.setParameter("deptno", d);
        query.setParameter("objtype", t);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByFormidAndYear(String formid, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByFormidAndSeq");
        query.setParameter("formid", formid);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public Indicator findByFormidYearAndDeptno(String formid, int y, String value) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByFormidSeqAndDeptno");
        query.setParameter("formid", formid);
        query.setParameter("seq", y);
        query.setParameter("deptno", value);
        try {
            Object o = query.getSingleResult();
            return (Indicator)o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByJobScheduleAndStatus(String jobschedule, String status) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByJobScheduleAndStatus");
        query.setParameter("jobschedule", jobschedule);
        query.setParameter("status", status);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public Indicator findByIdAndYear(int id, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByIdAndSeq");
        query.setParameter("id", id);
        query.setParameter("seq", y);
        try {
            Object o = query.getSingleResult();
            return (Indicator)o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByObjtypeAndYear(String type, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByObjtypeAndSeq");
        query.setParameter("objtype", type);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByObjtypeYearAndStatus(String type, int y, String status) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByObjtypeSeqAndStatus");
        query.setParameter("objtype", type);
        query.setParameter("seq", y);
        query.setParameter("status", status);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    public List<Indicator> findByPId(Object value) {
        return super.findByPId(value);
    }

    public List<Indicator> findByPIdAndYear(int pid, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByPIdAndSeq");
        query.setParameter("pid", pid);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findByPIdAndSeqAndFormid(int pid, int y, String value) {
        Query query = getEntityManager().createNamedQuery("Indicator.findByPIdAndSeqAndFormid");
        query.setParameter("pid", pid);
        query.setParameter("seq", y);
        query.setParameter("formid", "%" + value + "%");
        try {
            return query.getResultList();
        } catch (Exception ex) {
            System.out.println("cn.hanbell.kpi.ejb.IndicatorBean.findByPIdAndSeqAndFormid()" + ex);
            return null;
        }
    }

    public IndicatorDaily findIndicatorDailyByPIdDateAndType(int pid, int seq, int m, String type) {
        return indicatorDailyBean.findByPIdDateAndType(pid, seq, m, type);
    }

    public List<IndicatorSet> findIndicatorSetByPId(int pid) {
        return indicatorSetBean.findByPId(pid);
    }

    public List<Indicator> findRootByCompany(String company, String objtype, int y) {
        Query query = getEntityManager().createNamedQuery("Indicator.findRootByCompany");
        query.setParameter("company", company);
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findBySeqObjtypeAndCompany(int y, String objtype, String company) {
        Query query = getEntityManager().createNamedQuery("Indicator.findBySeqObjtypeAndCompany");
        query.setParameter("seq", y);
        query.setParameter("objtype", objtype);
        query.setParameter("company", company);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findRootByAssignedObjtypeAndJobSchedule(String objtype, int y, String jobschedule) {
        Query query = getEntityManager().createNamedQuery("Indicator.findRootByAssignedObjtypeAndJobSchedule");
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        query.setParameter("jobschedule", jobschedule);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Indicator> findRootByAssignedAndJobSchedule(String company, String objtype, int y, String jobschedule) {
        Query query = getEntityManager().createNamedQuery("Indicator.findRootByAssignedAndJobSchedule");
        query.setParameter("company", company);
        query.setParameter("objtype", objtype);
        query.setParameter("seq", y);
        query.setParameter("jobschedule", jobschedule);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<String> findCategoryList(String company, int y, String actualInterface) {
        StringBuilder sb = new StringBuilder();
        sb.append(
            " SELECT category FROM indicator WHERE company ='${company}' AND   seq = ${y} AND   actualInterface = '${actualInterface}' GROUP BY category ");
        String sql = sb.toString().replace("${company}", company).replace("${y}", String.valueOf(y))
            .replace("${actualInterface}", actualInterface);
        Query query = getEntityManager().createNativeQuery(sql);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public BigDecimal getAccumulatedGrowth(IndicatorDetail a, IndicatorDetail b, int m) {
        return getAccumulatedGrowth(a, b, m, 2);
    }

    public BigDecimal getAccumulatedGrowth(IndicatorDetail a, IndicatorDetail b, int m, int scale) {
        BigDecimal na, nb;
        na = getAccumulatedValue(a, m);
        nb = getAccumulatedValue(b, m);
        // 计算
        if (nb.compareTo(BigDecimal.ZERO) != 0) {
            return na.divide(nb, scale + 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100d));
        } else {
            return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
        }
    }

    public BigDecimal getAccumulatedGrowth(IndicatorDetail a, IndicatorDetail b, int m, Date d) {
        return getAccumulatedGrowth(a, b, m, d, 2);
    }

    public BigDecimal getAccumulatedGrowth(IndicatorDetail a, IndicatorDetail b, int m, Date d, int scale) {
        BigDecimal na, nb;
        na = getAccumulatedValue(a, m);
        nb = getAccumulatedValue(b, m, d);
        // 计算
        if (nb.compareTo(BigDecimal.ZERO) != 0) {
            return na.divide(nb, scale + 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100d));
        } else {
            return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
        }
    }

    public BigDecimal getAccumulatedPerformance(IndicatorDetail a, IndicatorDetail b, int m) {
        return getAccumulatedPerformance(a, b, m, 2);
    }

    public BigDecimal getAccumulatedPerformance(IndicatorDetail a, IndicatorDetail b, int m, int scale) {
        BigDecimal na, nb;
        na = getAccumulatedValue(a, m);
        nb = getAccumulatedValue(b, m);
        // 计算
        if (nb.compareTo(BigDecimal.ZERO) != 0) {
            return na.divide(nb, scale + 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
        }
    }

    public BigDecimal getAccumulatedPerformance(IndicatorDetail a, boolean fa, IndicatorDetail b, boolean fb, int m,
        Date d) {
        return getAccumulatedPerformance(a, fa, b, fb, m, d, 2);
    }

    public BigDecimal getAccumulatedPerformance(IndicatorDetail a, boolean fa, IndicatorDetail b, boolean fb, int m,
        Date d, int scale) {
        // fa,fb = true 表示需要按天折算
        BigDecimal na, nb;
        if (fa) {
            na = getAccumulatedValue(a, m, d);
        } else {
            na = getAccumulatedValue(a, m);
        }
        if (fb) {
            nb = getAccumulatedValue(b, m, d);
        } else {
            nb = getAccumulatedValue(b, m);
        }
        // 计算
        if (nb.compareTo(BigDecimal.ZERO) != 0) {
            return na.divide(nb, scale + 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
        }
    }

    public BigDecimal getAccumulatedValue(IndicatorDetail entity, int m) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        for (int i = 1; i <= m; i++) {
            try {
                mon = this.getIndicatorColumn("N", i);
                f = entity.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                total = total.add(BigDecimal.valueOf(Double.valueOf(f.get(entity).toString())));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                log4j.error(ex);
                total = BigDecimal.ZERO;
            }
        }
        return total;
    }

    public BigDecimal getAccumulatedValue(IndicatorDaily entity, int d) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        for (int i = 1; i <= d; i++) {
            try {
                mon = this.getIndicatorColumn("D", i);
                f = entity.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                total = total.add(BigDecimal.valueOf(Double.valueOf(f.get(entity).toString())));
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                log4j.error(ex);
                total = BigDecimal.ZERO;
            }
        }
        return total;
    }

    public BigDecimal getAccumulatedValue(IndicatorDetail entity, int m, Date d) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        for (int i = 1; i <= m; i++) {
            try {
                mon = this.getIndicatorColumn("N", i);
                f = entity.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                if (i < m) {
                    total = total.add(BigDecimal.valueOf(Double.valueOf(f.get(entity).toString())));
                } else {
                    total =
                        total.add(getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(entity).toString())), d, 0));
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                log4j.error(ex);
                total = BigDecimal.ZERO;
            }
        }
        return total;
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, int m) {
        return getGrowth(a, b, m, 2);
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, int m, int scale) {
        String mon;
        BigDecimal na, nb;
        Field f;
        try {
            mon = this.getIndicatorColumn("N", m);
            // 实际值分子
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            na = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            // 比较值分母
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            nb = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            // 计算
            if (nb.compareTo(BigDecimal.ZERO) != 0) {
                return na.divide(nb, scale + 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                    .multiply(BigDecimal.valueOf(100d));
            } else {
                return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, int m, Date d) {
        return getGrowth(a, b, m, d, 2);
    }

    public BigDecimal getGrowth(IndicatorDetail a, IndicatorDetail b, int m, Date d, int scale) {
        String mon;
        BigDecimal na, nb;
        Field f;
        try {
            mon = this.getIndicatorColumn("N", m);
            // 实际值分子
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            na = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            // 比较值分母
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            nb = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            // 折算到天数
            nb = getValueOfDays(nb, d, scale);
            // 计算
            if (nb.compareTo(BigDecimal.ZERO) != 0) {
                return na.divide(nb, scale + 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                    .multiply(BigDecimal.valueOf(100d));
            } else {
                return BigDecimal.valueOf(na.compareTo(nb)).multiply(BigDecimal.valueOf(100d));
            }
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }

    public String getIndicatorColumn(String formtype, int m) {
        if (formtype.equals("N")) {
            return "n" + String.format("%02d", m);
        } else if (formtype.equals("D")) {
            return "d" + String.format("%02d", m);
        } else if (formtype.equals("NQ")) {
            return "nq" + String.valueOf(m);
        } else {
            return "";
        }
    }

    public String getIndicatorColumn(String formtype, String c) {
        if (formtype.equals("N")) {
            return "n" + c;
        } else if (formtype.equals("D")) {
            return "d" + c;
        } else {
            return "";
        }
    }

    public int getRowCount(int y, String company) {
        Query query = getEntityManager().createNamedQuery("Indicator.getRowCountBySeq");
        query.setParameter("seq", y);
        query.setParameter("company", company);
        if (query.getSingleResult() == null) {
            return -1;
        } else {
            return Integer.parseInt(query.getSingleResult().toString());
        }
    }

    public Indicator getSumValue(List<Indicator> indicators) {
        return getSumValue(indicators, true, false);
    }

    public Indicator getSumValue(List<Indicator> indicators, boolean sumOther, boolean updatePerf) {
        if (indicators.isEmpty()) {
            return null;
        }
        Indicator entity = null;
        IndicatorDetail a, b, f, t;
        IndicatorDetail sa, sb, sf, st, sp, so1, so2, so3, so4, so5, so6;
        try {
            entity = (Indicator)BeanUtils.cloneBean(indicators.get(0));
            entity.setId(-1);
            entity.setName("合计");
            entity.setFormid("合计");
            sa = new IndicatorDetail();
            sa.setParent(entity);
            sa.setType("A");
            sb = new IndicatorDetail();
            sb.setParent(entity);
            sb.setType("B");
            sf = new IndicatorDetail();
            sf.setParent(entity);
            sf.setType("F");
            st = new IndicatorDetail();
            st.setParent(entity);
            st.setType("T");
            sp = new IndicatorDetail();
            sp.setParent(entity);
            sp.setType("P");
            entity.setActualIndicator(sa);
            entity.setBenchmarkIndicator(sb);
            entity.setForecastIndicator(sf);
            entity.setTargetIndicator(st);
            entity.setPerformanceIndicator(sp);
            if (entity.getHasOther() > 0) {
                switch (entity.getHasOther()) {
                    case 1:
                        so1 = new IndicatorDetail();
                        so1.setParent(entity);
                        so1.setType("A");
                        entity.setOther1Indicator(so1);
                        break;
                    case 2:
                        so1 = new IndicatorDetail();
                        so1.setParent(entity);
                        so1.setType("A");
                        entity.setOther1Indicator(so1);
                        so2 = new IndicatorDetail();
                        so2.setParent(entity);
                        so2.setType("A");
                        entity.setOther2Indicator(so2);
                        break;
                    case 3:
                        so1 = new IndicatorDetail();
                        so1.setParent(entity);
                        so1.setType("A");
                        entity.setOther1Indicator(so1);
                        so2 = new IndicatorDetail();
                        so2.setParent(entity);
                        so2.setType("A");
                        entity.setOther2Indicator(so2);
                        so3 = new IndicatorDetail();
                        so3.setParent(entity);
                        so3.setType("A");
                        entity.setOther3Indicator(so3);
                        break;
                    case 4:
                        so1 = new IndicatorDetail();
                        so1.setParent(entity);
                        so1.setType("A");
                        entity.setOther1Indicator(so1);
                        so2 = new IndicatorDetail();
                        so2.setParent(entity);
                        so2.setType("A");
                        entity.setOther2Indicator(so2);
                        so3 = new IndicatorDetail();
                        so3.setParent(entity);
                        so3.setType("A");
                        entity.setOther3Indicator(so3);
                        so4 = new IndicatorDetail();
                        so4.setParent(entity);
                        so4.setType("A");
                        entity.setOther4Indicator(so4);
                        break;
                    case 5:
                        so1 = new IndicatorDetail();
                        so1.setParent(entity);
                        so1.setType("A");
                        entity.setOther1Indicator(so1);
                        so2 = new IndicatorDetail();
                        so2.setParent(entity);
                        so2.setType("A");
                        entity.setOther2Indicator(so2);
                        so3 = new IndicatorDetail();
                        so3.setParent(entity);
                        so3.setType("A");
                        entity.setOther3Indicator(so3);
                        so4 = new IndicatorDetail();
                        so4.setParent(entity);
                        so4.setType("A");
                        entity.setOther4Indicator(so4);
                        so5 = new IndicatorDetail();
                        so5.setParent(entity);
                        so5.setType("A");
                        entity.setOther5Indicator(so5);
                        break;
                    case 6:
                        so1 = new IndicatorDetail();
                        so1.setParent(entity);
                        so1.setType("A");
                        entity.setOther1Indicator(so1);
                        so2 = new IndicatorDetail();
                        so2.setParent(entity);
                        so2.setType("A");
                        entity.setOther2Indicator(so2);
                        so3 = new IndicatorDetail();
                        so3.setParent(entity);
                        so3.setType("A");
                        entity.setOther3Indicator(so3);
                        so4 = new IndicatorDetail();
                        so4.setParent(entity);
                        so4.setType("A");
                        entity.setOther4Indicator(so4);
                        so5 = new IndicatorDetail();
                        so5.setParent(entity);
                        so5.setType("A");
                        entity.setOther5Indicator(so5);
                        so6 = new IndicatorDetail();
                        so6.setParent(entity);
                        so6.setType("A");
                        entity.setOther6Indicator(so6);
                        break;
                    default:
                }
            }
            // 计算周期
            String formkind = entity.getFormkind();
            for (int i = 0; i < indicators.size(); i++) {
                a = indicators.get(i).getActualIndicator();
                b = indicators.get(i).getBenchmarkIndicator();
                f = indicators.get(i).getForecastIndicator();
                t = indicators.get(i).getTargetIndicator();
                addValue(entity.getActualIndicator(), a, formkind);
                addValue(entity.getBenchmarkIndicator(), b, formkind);
                addValue(entity.getForecastIndicator(), f, formkind);
                addValue(entity.getTargetIndicator(), t, formkind);
                if (sumOther && indicators.get(i).getHasOther() > 0) {
                    switch (entity.getHasOther()) {
                        case 1:
                            addValue(entity.getOther1Indicator(), indicators.get(i).getOther1Indicator(), formkind);
                            break;
                        case 2:
                            addValue(entity.getOther1Indicator(), indicators.get(i).getOther1Indicator(), formkind);
                            addValue(entity.getOther2Indicator(), indicators.get(i).getOther2Indicator(), formkind);
                            break;
                        case 3:
                            addValue(entity.getOther1Indicator(), indicators.get(i).getOther1Indicator(), formkind);
                            addValue(entity.getOther2Indicator(), indicators.get(i).getOther2Indicator(), formkind);
                            addValue(entity.getOther3Indicator(), indicators.get(i).getOther3Indicator(), formkind);
                            break;
                        case 4:
                            addValue(entity.getOther1Indicator(), indicators.get(i).getOther1Indicator(), formkind);
                            addValue(entity.getOther2Indicator(), indicators.get(i).getOther2Indicator(), formkind);
                            addValue(entity.getOther3Indicator(), indicators.get(i).getOther3Indicator(), formkind);
                            addValue(entity.getOther4Indicator(), indicators.get(i).getOther4Indicator(), formkind);
                            break;
                        case 5:
                            addValue(entity.getOther1Indicator(), indicators.get(i).getOther1Indicator(), formkind);
                            addValue(entity.getOther2Indicator(), indicators.get(i).getOther2Indicator(), formkind);
                            addValue(entity.getOther3Indicator(), indicators.get(i).getOther3Indicator(), formkind);
                            addValue(entity.getOther4Indicator(), indicators.get(i).getOther4Indicator(), formkind);
                            addValue(entity.getOther5Indicator(), indicators.get(i).getOther5Indicator(), formkind);
                            break;
                        case 6:
                            addValue(entity.getOther1Indicator(), indicators.get(i).getOther1Indicator(), formkind);
                            addValue(entity.getOther2Indicator(), indicators.get(i).getOther2Indicator(), formkind);
                            addValue(entity.getOther3Indicator(), indicators.get(i).getOther3Indicator(), formkind);
                            addValue(entity.getOther4Indicator(), indicators.get(i).getOther4Indicator(), formkind);
                            addValue(entity.getOther5Indicator(), indicators.get(i).getOther5Indicator(), formkind);
                            addValue(entity.getOther6Indicator(), indicators.get(i).getOther6Indicator(), formkind);
                            break;
                        default:
                    }
                }
            }
            if (updatePerf) {
                updatePerformance(entity);
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException
            | NoSuchMethodException ex) {
            log4j.error(ex);
        }
        return entity;
    }

    /**
     * 默认不处理其他值，不计算达成率
     *
     * @param minuend
     *                       被减数
     * @param subtrahend
     *                       减数
     * @return 新的指标对象
     */
    public Indicator getSubtractValue(Indicator minuend, Indicator subtrahend) {
        return getSubtractValue(minuend, subtrahend, false, false);
    }

    /**
     *
     * @param minuend
     *                       被减数
     * @param subtrahend
     *                       减数
     * @param subOther
     *                       是否处理其他值
     * @param updatePerf
     *                       是否计算达成率
     * @return 新的指标对象
     */
    public Indicator getSubtractValue(Indicator minuend, Indicator subtrahend, boolean subOther, boolean updatePerf) {
        if (!minuend.getFormtype().equals(subtrahend.getFormtype())
            || !minuend.getFormkind().equals(subtrahend.getFormkind())) {
            return null;
        }
        String formkind = minuend.getFormkind();
        Indicator entity = null;
        try {
            entity = (Indicator)BeanUtils.cloneBean(minuend);
            entity.setId(-1);
            subtractValue(entity.getActualIndicator(), subtrahend.getActualIndicator(), formkind);
            subtractValue(entity.getBenchmarkIndicator(), subtrahend.getBenchmarkIndicator(), formkind);
            subtractValue(entity.getForecastIndicator(), subtrahend.getForecastIndicator(), formkind);
            subtractValue(entity.getTargetIndicator(), subtrahend.getTargetIndicator(), formkind);
            if (subOther && minuend.getHasOther() == subtrahend.getHasOther()) {
                switch (entity.getHasOther()) {
                    case 1:
                        subtractValue(entity.getOther1Indicator(), subtrahend.getOther1Indicator(), formkind);
                        break;
                    case 2:
                        subtractValue(entity.getOther1Indicator(), subtrahend.getOther1Indicator(), formkind);
                        subtractValue(entity.getOther2Indicator(), subtrahend.getOther2Indicator(), formkind);
                        break;
                    case 3:
                        subtractValue(entity.getOther1Indicator(), subtrahend.getOther1Indicator(), formkind);
                        subtractValue(entity.getOther2Indicator(), subtrahend.getOther2Indicator(), formkind);
                        subtractValue(entity.getOther3Indicator(), subtrahend.getOther3Indicator(), formkind);
                        break;
                    case 4:
                        subtractValue(entity.getOther1Indicator(), subtrahend.getOther1Indicator(), formkind);
                        subtractValue(entity.getOther2Indicator(), subtrahend.getOther2Indicator(), formkind);
                        subtractValue(entity.getOther3Indicator(), subtrahend.getOther3Indicator(), formkind);
                        subtractValue(entity.getOther4Indicator(), subtrahend.getOther4Indicator(), formkind);
                        break;
                    case 5:
                        subtractValue(entity.getOther1Indicator(), subtrahend.getOther1Indicator(), formkind);
                        subtractValue(entity.getOther2Indicator(), subtrahend.getOther2Indicator(), formkind);
                        subtractValue(entity.getOther3Indicator(), subtrahend.getOther3Indicator(), formkind);
                        subtractValue(entity.getOther4Indicator(), subtrahend.getOther4Indicator(), formkind);
                        subtractValue(entity.getOther5Indicator(), subtrahend.getOther5Indicator(), formkind);
                        break;
                    case 6:
                        subtractValue(entity.getOther1Indicator(), subtrahend.getOther1Indicator(), formkind);
                        subtractValue(entity.getOther2Indicator(), subtrahend.getOther2Indicator(), formkind);
                        subtractValue(entity.getOther3Indicator(), subtrahend.getOther3Indicator(), formkind);
                        subtractValue(entity.getOther4Indicator(), subtrahend.getOther4Indicator(), formkind);
                        subtractValue(entity.getOther5Indicator(), subtrahend.getOther5Indicator(), formkind);
                        subtractValue(entity.getOther6Indicator(), subtrahend.getOther6Indicator(), formkind);
                        break;
                    default:
                }
            }
            if (updatePerf) {
                updatePerformance(entity);
            }
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException
            | NoSuchMethodException ex) {
            log4j.error(ex);
        }
        return entity;
    }

    public BigDecimal getValueOfDays(BigDecimal v, Date d, int scale) {
        int i, j;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        i = c.get(Calendar.DAY_OF_MONTH);
        c.add(Calendar.MONTH, 1);
        c.add(Calendar.DATE, 0 - c.get(Calendar.DATE));
        j = c.get(Calendar.DAY_OF_MONTH);
        return v.multiply(BigDecimal.valueOf(i)).divide(BigDecimal.valueOf(j), scale, RoundingMode.HALF_UP);
    }

    public String percentFormat(BigDecimal value) {
        return percentFormat(value, 2);
    }

    public String percentFormat(BigDecimal value, int scale) {
        return String.format("%s%%", value.setScale(scale, RoundingMode.HALF_UP).toString());
    }

    @Override
    public void persist(Indicator entity) {
        super.persist(entity);
        this.getEntityManager().flush();
        // 目标
        IndicatorDetail t = new IndicatorDetail();
        t.setPid(entity.getId());
        t.setSeq(1);
        t.setType("T");
        indicatorDetailBean.persist(t);
        indicatorDetailBean.getEntityManager().flush();
        entity.setTargetIndicator(t);
        // 基准
        IndicatorDetail b = new IndicatorDetail();
        b.setPid(entity.getId());
        b.setSeq(2);
        b.setType("B");
        indicatorDetailBean.persist(b);
        indicatorDetailBean.getEntityManager().flush();
        entity.setBenchmarkIndicator(b);
        // 预期
        IndicatorDetail f = new IndicatorDetail();
        f.setPid(entity.getId());
        f.setSeq(3);
        f.setType("F");
        indicatorDetailBean.persist(f);
        indicatorDetailBean.getEntityManager().flush();
        entity.setForecastIndicator(f);
        // 实际
        IndicatorDetail a = new IndicatorDetail();
        a.setPid(entity.getId());
        a.setSeq(4);
        a.setType("A");
        indicatorDetailBean.persist(a);
        indicatorDetailBean.getEntityManager().flush();
        entity.setActualIndicator(a);
        // 绩效
        IndicatorDetail p = new IndicatorDetail();
        p.setPid(entity.getId());
        p.setSeq(5);
        p.setType("P");
        indicatorDetailBean.persist(p);
        indicatorDetailBean.getEntityManager().flush();
        entity.setPerformanceIndicator(p);
        // 设置指标明细后保存
        update(entity);
        // 设置指标关联部门
        IndicatorDepartment dept = new IndicatorDepartment();
        dept.setPid(entity.getId());
        dept.setSeq(entity.getSeq());
        dept.setDeptno(entity.getDeptno());
        dept.setDeptname(entity.getDeptname());
        indicatorDepartmentBean.persist(dept);
    }

    public void persistIndicatorDaily(Indicator entity, IndicatorDetail detail) {
        IndicatorDaily table;
        if (entity != null) {
            for (int i = 1; i <= 12; i++) {
                table = new IndicatorDaily();
                table.setPid(detail.getId());
                table.setSeq(detail.getSeq());
                table.setMth(i);
                table.setType(detail.getType());
                indicatorDailyBean.persist(table);
            }
        }
    }

    public void subtractValue(IndicatorDetail a, IndicatorDetail b, String formKind) {
        // 先算汇总字段再算每月字段,A和S类型会重算汇总
        switch (formKind) {
            case "M":
            case "D":
                a.setNfy(a.getNfy().subtract(b.getNfy()));
                a.setNh2(a.getNh2().subtract(b.getNh2()));
                a.setNh1(a.getNh1().subtract(b.getNh1()));
                a.setNq4(a.getNq4().subtract(b.getNq4()));
                a.setNq3(a.getNq3().subtract(b.getNq3()));
                a.setNq2(a.getNq2().subtract(b.getNq2()));
                a.setNq1(a.getNq1().subtract(b.getNq1()));
                a.setN01(a.getN01().subtract(b.getN01()));
                a.setN02(a.getN02().subtract(b.getN02()));
                a.setN03(a.getN03().subtract(b.getN03()));
                a.setN04(a.getN04().subtract(b.getN04()));
                a.setN05(a.getN05().subtract(b.getN05()));
                a.setN06(a.getN06().subtract(b.getN06()));
                a.setN07(a.getN07().subtract(b.getN07()));
                a.setN08(a.getN08().subtract(b.getN08()));
                a.setN09(a.getN09().subtract(b.getN09()));
                a.setN10(a.getN10().subtract(b.getN10()));
                a.setN11(a.getN11().subtract(b.getN11()));
                a.setN12(a.getN12().subtract(b.getN12()));
                break;
            case "Q":
                a.setNfy(a.getNfy().subtract(b.getNfy()));
                a.setNh2(a.getNh2().subtract(b.getNh2()));
                a.setNh1(a.getNh1().subtract(b.getNh1()));
                a.setNq4(a.getNq4().subtract(b.getNq4()));
                a.setNq3(a.getNq3().subtract(b.getNq3()));
                a.setNq2(a.getNq2().subtract(b.getNq2()));
                a.setNq1(a.getNq1().subtract(b.getNq1()));
                break;
            case "H":
                a.setNfy(a.getNfy().subtract(b.getNfy()));
                a.setNh2(a.getNh2().subtract(b.getNh2()));
                a.setNh1(a.getNh1().subtract(b.getNh1()));
                break;
            case "Y":
                a.setNfy(a.getNfy().subtract(b.getNfy()));
        }
    }

    @Override
    public Indicator update(Indicator entity) {
        indicatorDetailBean.update(entity.getTargetIndicator());
        indicatorDetailBean.update(entity.getBenchmarkIndicator());
        indicatorDetailBean.update(entity.getForecastIndicator());
        indicatorDetailBean.update(entity.getActualIndicator());
        indicatorDetailBean.update(entity.getPerformanceIndicator());
        indicatorDetailBean.getEntityManager().flush();
        if ("D".equals(entity.getFormkind())) {
            // 检查是否有天指标
            if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getTargetIndicator().getId(),
                entity.getTargetIndicator().getSeq())) {
                persistIndicatorDaily(entity, entity.getTargetIndicator());
            }
            if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getBenchmarkIndicator().getId(),
                entity.getBenchmarkIndicator().getSeq())) {
                persistIndicatorDaily(entity, entity.getBenchmarkIndicator());
            }
            if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getForecastIndicator().getId(),
                entity.getForecastIndicator().getSeq())) {
                persistIndicatorDaily(entity, entity.getForecastIndicator());
            }
            if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getActualIndicator().getId(),
                entity.getActualIndicator().getSeq())) {
                persistIndicatorDaily(entity, entity.getActualIndicator());
            }
            if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getPerformanceIndicator().getId(),
                entity.getPerformanceIndicator().getSeq())) {
                persistIndicatorDaily(entity, entity.getPerformanceIndicator());
            }
        }
        if (entity.getHasOther() == 1) {
            if (entity.getOther1Indicator() == null) {
                IndicatorDetail o1 = new IndicatorDetail();
                o1.setPid(entity.getId());
                o1.setSeq(6);
                o1.setType("A");
                indicatorDetailBean.persist(o1);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther1Indicator(o1);
            }
            if ("D".equals(entity.getFormkind())) {
                // 检查是否有天指标
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther1Indicator().getId(),
                    entity.getOther1Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther1Indicator());
                }
            }
        }
        if (entity.getHasOther() == 2) {
            if (entity.getOther1Indicator() == null) {
                IndicatorDetail o1 = new IndicatorDetail();
                o1.setPid(entity.getId());
                o1.setSeq(6);
                o1.setType("A");
                indicatorDetailBean.persist(o1);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther1Indicator(o1);
            }
            if (entity.getOther2Indicator() == null) {
                IndicatorDetail o2 = new IndicatorDetail();
                o2.setPid(entity.getId());
                o2.setSeq(7);
                o2.setType("A");
                indicatorDetailBean.persist(o2);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther2Indicator(o2);
            }
            if ("D".equals(entity.getFormkind())) {
                // 检查是否有天指标
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther1Indicator().getId(),
                    entity.getOther1Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther1Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther2Indicator().getId(),
                    entity.getOther2Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther2Indicator());
                }
            }
        }
        if (entity.getHasOther() == 3) {
            if (entity.getOther1Indicator() == null) {
                IndicatorDetail o1 = new IndicatorDetail();
                o1.setPid(entity.getId());
                o1.setSeq(6);
                o1.setType("A");
                indicatorDetailBean.persist(o1);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther1Indicator(o1);
            }
            if (entity.getOther2Indicator() == null) {
                IndicatorDetail o2 = new IndicatorDetail();
                o2.setPid(entity.getId());
                o2.setSeq(7);
                o2.setType("A");
                indicatorDetailBean.persist(o2);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther2Indicator(o2);
            }
            if (entity.getOther3Indicator() == null) {
                IndicatorDetail o3 = new IndicatorDetail();
                o3.setPid(entity.getId());
                o3.setSeq(8);
                o3.setType("A");
                indicatorDetailBean.persist(o3);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther3Indicator(o3);
            }
            if ("D".equals(entity.getFormkind())) {
                // 检查是否有天指标
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther1Indicator().getId(),
                    entity.getOther1Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther1Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther2Indicator().getId(),
                    entity.getOther2Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther2Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther3Indicator().getId(),
                    entity.getOther3Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther3Indicator());
                }
            }
        }
        if (entity.getHasOther() == 4) {
            if (entity.getOther1Indicator() == null) {
                IndicatorDetail o1 = new IndicatorDetail();
                o1.setPid(entity.getId());
                o1.setSeq(6);
                o1.setType("A");
                indicatorDetailBean.persist(o1);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther1Indicator(o1);
            }
            if (entity.getOther2Indicator() == null) {
                IndicatorDetail o2 = new IndicatorDetail();
                o2.setPid(entity.getId());
                o2.setSeq(7);
                o2.setType("A");
                indicatorDetailBean.persist(o2);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther2Indicator(o2);
            }
            if (entity.getOther3Indicator() == null) {
                IndicatorDetail o3 = new IndicatorDetail();
                o3.setPid(entity.getId());
                o3.setSeq(8);
                o3.setType("A");
                indicatorDetailBean.persist(o3);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther3Indicator(o3);
            }
            if (entity.getOther4Indicator() == null) {
                IndicatorDetail o4 = new IndicatorDetail();
                o4.setPid(entity.getId());
                o4.setSeq(9);
                o4.setType("A");
                indicatorDetailBean.persist(o4);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther4Indicator(o4);
            }
            if ("D".equals(entity.getFormkind())) {
                // 检查是否有天指标
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther1Indicator().getId(),
                    entity.getOther1Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther1Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther2Indicator().getId(),
                    entity.getOther2Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther2Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther3Indicator().getId(),
                    entity.getOther3Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther3Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther4Indicator().getId(),
                    entity.getOther4Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther4Indicator());
                }
            }
        }
        if (entity.getHasOther() == 5) {
            if (entity.getOther1Indicator() == null) {
                IndicatorDetail o1 = new IndicatorDetail();
                o1.setPid(entity.getId());
                o1.setSeq(6);
                o1.setType("A");
                indicatorDetailBean.persist(o1);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther1Indicator(o1);
            }
            if (entity.getOther2Indicator() == null) {
                IndicatorDetail o2 = new IndicatorDetail();
                o2.setPid(entity.getId());
                o2.setSeq(7);
                o2.setType("A");
                indicatorDetailBean.persist(o2);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther2Indicator(o2);
            }
            if (entity.getOther3Indicator() == null) {
                IndicatorDetail o3 = new IndicatorDetail();
                o3.setPid(entity.getId());
                o3.setSeq(8);
                o3.setType("A");
                indicatorDetailBean.persist(o3);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther3Indicator(o3);
            }
            if (entity.getOther4Indicator() == null) {
                IndicatorDetail o4 = new IndicatorDetail();
                o4.setPid(entity.getId());
                o4.setSeq(9);
                o4.setType("A");
                indicatorDetailBean.persist(o4);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther4Indicator(o4);
            }
            if (entity.getOther5Indicator() == null) {
                IndicatorDetail o5 = new IndicatorDetail();
                o5.setPid(entity.getId());
                o5.setSeq(10);
                o5.setType("A");
                indicatorDetailBean.persist(o5);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther5Indicator(o5);
            }
            if ("D".equals(entity.getFormkind())) {
                // 检查是否有天指标
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther1Indicator().getId(),
                    entity.getOther1Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther1Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther2Indicator().getId(),
                    entity.getOther2Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther2Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther3Indicator().getId(),
                    entity.getOther3Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther3Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther4Indicator().getId(),
                    entity.getOther4Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther4Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther5Indicator().getId(),
                    entity.getOther5Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther5Indicator());
                }
            }
        }
        if (entity.getHasOther() == 6) {
            if (entity.getOther1Indicator() == null) {
                IndicatorDetail o1 = new IndicatorDetail();
                o1.setPid(entity.getId());
                o1.setSeq(6);
                o1.setType("A");
                indicatorDetailBean.persist(o1);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther1Indicator(o1);
            }
            if (entity.getOther2Indicator() == null) {
                IndicatorDetail o2 = new IndicatorDetail();
                o2.setPid(entity.getId());
                o2.setSeq(7);
                o2.setType("A");
                indicatorDetailBean.persist(o2);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther2Indicator(o2);
            }
            if (entity.getOther3Indicator() == null) {
                IndicatorDetail o3 = new IndicatorDetail();
                o3.setPid(entity.getId());
                o3.setSeq(8);
                o3.setType("A");
                indicatorDetailBean.persist(o3);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther3Indicator(o3);
            }
            if (entity.getOther4Indicator() == null) {
                IndicatorDetail o4 = new IndicatorDetail();
                o4.setPid(entity.getId());
                o4.setSeq(9);
                o4.setType("A");
                indicatorDetailBean.persist(o4);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther4Indicator(o4);
            }
            if (entity.getOther5Indicator() == null) {
                IndicatorDetail o5 = new IndicatorDetail();
                o5.setPid(entity.getId());
                o5.setSeq(10);
                o5.setType("A");
                indicatorDetailBean.persist(o5);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther5Indicator(o5);
            }
            if (entity.getOther6Indicator() == null) {
                IndicatorDetail o6 = new IndicatorDetail();
                o6.setPid(entity.getId());
                o6.setSeq(11);
                o6.setType("A");
                indicatorDetailBean.persist(o6);
                indicatorDetailBean.getEntityManager().flush();
                entity.setOther6Indicator(o6);
            }
            if ("D".equals(entity.getFormkind())) {
                // 检查是否有天指标
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther1Indicator().getId(),
                    entity.getOther1Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther1Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther2Indicator().getId(),
                    entity.getOther2Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther2Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther3Indicator().getId(),
                    entity.getOther3Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther3Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther4Indicator().getId(),
                    entity.getOther4Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther4Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther5Indicator().getId(),
                    entity.getOther5Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther5Indicator());
                }
                if (!indicatorDailyBean.queryIndicatorDailyIsExist(entity.getOther6Indicator().getId(),
                    entity.getOther6Indicator().getSeq())) {
                    persistIndicatorDaily(entity, entity.getOther6Indicator());
                }
            }
        }
        return super.update(entity);
    }

    public void updateActual(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null || "".equals(entity.getActualInterface())) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getActualIndicator().setNfy(si.getActualIndicator().getNfy());
                    entity.getActualIndicator().setNh2(si.getActualIndicator().getNh2());
                    entity.getActualIndicator().setNh1(si.getActualIndicator().getNh1());
                    entity.getActualIndicator().setNq1(si.getActualIndicator().getNq1());
                    entity.getActualIndicator().setNq2(si.getActualIndicator().getNq2());
                    entity.getActualIndicator().setNq3(si.getActualIndicator().getNq3());
                    entity.getActualIndicator().setNq4(si.getActualIndicator().getNq4());
                    entity.getActualIndicator().setN01(si.getActualIndicator().getN01());
                    entity.getActualIndicator().setN02(si.getActualIndicator().getN02());
                    entity.getActualIndicator().setN03(si.getActualIndicator().getN03());
                    entity.getActualIndicator().setN04(si.getActualIndicator().getN04());
                    entity.getActualIndicator().setN05(si.getActualIndicator().getN05());
                    entity.getActualIndicator().setN06(si.getActualIndicator().getN06());
                    entity.getActualIndicator().setN07(si.getActualIndicator().getN07());
                    entity.getActualIndicator().setN08(si.getActualIndicator().getN08());
                    entity.getActualIndicator().setN09(si.getActualIndicator().getN09());
                    entity.getActualIndicator().setN10(si.getActualIndicator().getN10());
                    entity.getActualIndicator().setN11(si.getActualIndicator().getN11());
                    entity.getActualIndicator().setN12(si.getActualIndicator().getN12());
                    break;
                case "Q":
                    entity.getActualIndicator().setNfy(si.getActualIndicator().getNfy());
                    entity.getActualIndicator().setNh2(si.getActualIndicator().getNh2());
                    entity.getActualIndicator().setNh1(si.getActualIndicator().getNh1());
                    entity.getActualIndicator().setNq1(si.getActualIndicator().getNq1());
                    entity.getActualIndicator().setNq2(si.getActualIndicator().getNq2());
                    entity.getActualIndicator().setNq3(si.getActualIndicator().getNq3());
                    entity.getActualIndicator().setNq4(si.getActualIndicator().getNq4());
                    break;
            }
        }
    }

    public void updateActual(Indicator entity, Object prop) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null || "".equals(entity.getActualInterface())) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            String col = "";
            Field f;
            Method setMethod;
            try {
                switch (entity.getFormtype() + entity.getFormkind()) {
                    case "NM":
                        col = this.getIndicatorColumn(entity.getFormtype(), Integer.valueOf(prop.toString()));
                        break;
                    case "NQ":
                        switch (prop.toString()) {
                            case "1":
                            case "2":
                            case "3":
                                col = this.getIndicatorColumn(entity.getFormtype(), "q1");
                                break;
                            case "4":
                            case "5":
                            case "6":
                                col = this.getIndicatorColumn(entity.getFormtype(), "q2");
                                break;
                            case "7":
                            case "8":
                            case "9":
                                col = this.getIndicatorColumn(entity.getFormtype(), "q3");
                                break;
                            case "10":
                            case "11":
                            case "12":
                                col = this.getIndicatorColumn(entity.getFormtype(), "q4");
                                break;
                            default:
                                col = this.getIndicatorColumn(entity.getFormtype(), prop.toString());
                        }
                }
                if (col != null && !"".equals(col)) {
                    f = si.getActualIndicator().getClass().getDeclaredField(col);
                    f.setAccessible(true);
                    setMethod = entity.getActualIndicator().getClass().getDeclaredMethod(
                        "set" + col.substring(0, 1).toUpperCase() + col.substring(1), BigDecimal.class);
                    setMethod.invoke(entity.getActualIndicator(),
                        BigDecimal.valueOf(Double.valueOf(f.get(si.getActualIndicator()).toString())));
                }
            } catch (NoSuchFieldException | SecurityException | NoSuchMethodException | IllegalArgumentException
                | IllegalAccessException | InvocationTargetException ex) {
                log4j.error(ex);
            }
        }
    }

    public Indicator updateActual(int id, int y, int m, Date d, int type) {
        Indicator entity = findById(id);
        String userid = entity.getUserid();
        IndicatorDetail detail;
        int uy, um;
        // 先计算Other
        if (entity != null && entity.getHasOther() > 0) {
            if (entity.getOther1Interface() != null && !"".equals(entity.getOther1Interface())) {
                IndicatorDetail o1 = entity.getOther1Indicator();
                try {
                    otherInterface = (Actual)Class.forName(entity.getOther1Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther1EJB());
                    otherInterface.getQueryParams().put("userid", userid);
                    otherInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(o1, uy, um, d, type, otherInterface);
                    } else {
                        na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    }
                    Method setMethod = o1.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(o1, na);
                    indicatorDetailBean.update(o1);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther2Interface() != null && !"".equals(entity.getOther2Interface())) {
                IndicatorDetail o2 = entity.getOther2Indicator();
                try {
                    otherInterface = (Actual)Class.forName(entity.getOther2Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther2EJB());
                    otherInterface.getQueryParams().put("userid", userid);
                    otherInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(o2, uy, um, d, type, otherInterface);
                    } else {
                        na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    }
                    Method setMethod = o2.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(o2, na);
                    indicatorDetailBean.update(o2);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther3Interface() != null && !"".equals(entity.getOther3Interface())) {
                IndicatorDetail o3 = entity.getOther3Indicator();
                try {
                    otherInterface = (Actual)Class.forName(entity.getOther3Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther3EJB());
                    otherInterface.getQueryParams().put("userid", userid);
                    otherInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(o3, uy, um, d, type, otherInterface);
                    } else {
                        na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    }
                    Method setMethod = o3.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(o3, na);
                    indicatorDetailBean.update(o3);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther4Interface() != null && !"".equals(entity.getOther4Interface())) {
                IndicatorDetail o4 = entity.getOther4Indicator();
                try {
                    otherInterface = (Actual)Class.forName(entity.getOther4Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther4EJB());
                    otherInterface.getQueryParams().put("userid", userid);
                    otherInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(o4, uy, um, d, type, otherInterface);
                    } else {
                        na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    }
                    Method setMethod = o4.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(o4, na);
                    indicatorDetailBean.update(o4);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther5Interface() != null && !"".equals(entity.getOther5Interface())) {
                IndicatorDetail o5 = entity.getOther5Indicator();
                try {
                    otherInterface = (Actual)Class.forName(entity.getOther5Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther5EJB());
                    otherInterface.getQueryParams().put("userid", userid);
                    otherInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(o5, uy, um, d, type, otherInterface);
                    } else {
                        na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    }
                    Method setMethod = o5.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(o5, na);
                    indicatorDetailBean.update(o5);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
            if (entity.getOther6Interface() != null && !"".equals(entity.getOther6Interface())) {
                IndicatorDetail o6 = entity.getOther6Indicator();
                try {
                    otherInterface = (Actual)Class.forName(entity.getOther6Interface()).newInstance();
                    uy = otherInterface.getUpdateYear(y, m);
                    um = otherInterface.getUpdateMonth(y, m);
                    otherInterface.setEJB(entity.getOther6EJB());
                    otherInterface.getQueryParams().put("userid", userid);
                    otherInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(o6, uy, um, d, type, otherInterface);
                    } else {
                        na = otherInterface.getValue(uy, um, d, type, otherInterface.getQueryParams());
                    }
                    Method setMethod = o6.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(o6, na);
                    indicatorDetailBean.update(o6);
                } catch (Exception ex) {
                    log4j.error(ex);
                }
            }
        }
        if ((entity != null) && (entity.getActualInterface() != null) && (!"".equals(entity.getActualInterface()))) {
            IndicatorDetail a = entity.getActualIndicator();
            try {
                actualInterface = (Actual)Class.forName(entity.getActualInterface()).newInstance();
                uy = actualInterface.getUpdateYear(y, m);
                um = actualInterface.getUpdateMonth(y, m);
                if (entity.getSeq() == uy) {
                    actualInterface.setEJB(entity.getActualEJB());
                    actualInterface.getQueryParams().put("userid", userid);
                    actualInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(a, uy, um, d, type, actualInterface);
                    } else {
                        na = actualInterface.getValue(uy, um, d, type, actualInterface.getQueryParams());
                    }
                    Method setMethod = a.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(a, na);
                    indicatorDetailBean.update(a);
                }
            } catch (Exception ex) {
                log4j.error(ex);
            }
        }
        return entity;
    }

    public Indicator updateActualManual(int id, int y, int m, Date d, int type) {
        Indicator entity = findById(id);
        String userid = entity.getUserid();
        int uy, um;
        if ((entity != null) && (entity.getActualInterface() != null) && !"".equals(entity.getActualInterface())) {
            IndicatorDetail a = entity.getActualIndicator();
            try {
                actualInterface = (Actual)Class.forName(entity.getActualInterface()).newInstance();
                uy = actualInterface.getUpdateYear(y, m);
                um = actualInterface.getUpdateMonth(y, m);
                if (entity.getSeq() == uy) {
                    actualInterface.setEJB(entity.getActualEJB());
                    actualInterface.getQueryParams().put("userid", userid);
                    actualInterface.getQueryParams().put("id", id);
                    BigDecimal na;
                    if ("D".equals(entity.getFormkind())) {
                        na = updateIndicatorDaily(a, uy, um, d, type, actualInterface);
                    } else {
                        na = actualInterface.getValue(uy, um, d, type, actualInterface.getQueryParams());
                    }
                    Method setMethod = a.getClass()
                        .getDeclaredMethod("set" + this.getIndicatorColumn("N", um).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(a, na);
                    indicatorDetailBean.update(a);
                }
            } catch (Exception ex) {
                log4j.error(ex);
            }
        }
        return entity;
    }

    public BigDecimal updateIndicatorDaily(IndicatorDaily daily) {
        indicatorDailyBean.update(daily);
        return daily.getTotal();
    }

    public BigDecimal updateIndicatorDaily(IndicatorDetail entity, int uy, int um, Date d, int type, Actual a1) {
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            int day = c.get(Calendar.DAY_OF_MONTH);
            BigDecimal value = a1.getValue(uy, um, d, type, a1.getQueryParams());
            IndicatorDaily daily =
                indicatorDailyBean.findByPIdDateAndType(entity.getId(), entity.getSeq(), um, entity.getType());
            if (daily != null) {
                Method setMethod = daily.getClass()
                    .getDeclaredMethod("set" + this.getIndicatorColumn("D", day).toUpperCase(), BigDecimal.class);
                setMethod.invoke(daily, value);
                indicatorDailyBean.update(daily);
                return daily.getTotal();
            }
        } catch (Exception ex) {
            log4j.error("updateActualOfIndicatorDaily" + ex);
        }
        return BigDecimal.ZERO;
    }

    public IndicatorDetail updateIndicatorDetail(IndicatorDetail id) {
        return indicatorDetailBean.update(id);
    }

    public void updateBenchmark(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null || "".equals(entity.getActualInterface())) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getBenchmarkIndicator().setNfy(si.getBenchmarkIndicator().getNfy());
                    entity.getBenchmarkIndicator().setNh2(si.getBenchmarkIndicator().getNh2());
                    entity.getBenchmarkIndicator().setNh1(si.getBenchmarkIndicator().getNh1());
                    entity.getBenchmarkIndicator().setNq1(si.getBenchmarkIndicator().getNq1());
                    entity.getBenchmarkIndicator().setNq2(si.getBenchmarkIndicator().getNq2());
                    entity.getBenchmarkIndicator().setNq3(si.getBenchmarkIndicator().getNq3());
                    entity.getBenchmarkIndicator().setNq4(si.getBenchmarkIndicator().getNq4());
                    entity.getBenchmarkIndicator().setN01(si.getBenchmarkIndicator().getN01());
                    entity.getBenchmarkIndicator().setN02(si.getBenchmarkIndicator().getN02());
                    entity.getBenchmarkIndicator().setN03(si.getBenchmarkIndicator().getN03());
                    entity.getBenchmarkIndicator().setN04(si.getBenchmarkIndicator().getN04());
                    entity.getBenchmarkIndicator().setN05(si.getBenchmarkIndicator().getN05());
                    entity.getBenchmarkIndicator().setN06(si.getBenchmarkIndicator().getN06());
                    entity.getBenchmarkIndicator().setN07(si.getBenchmarkIndicator().getN07());
                    entity.getBenchmarkIndicator().setN08(si.getBenchmarkIndicator().getN08());
                    entity.getBenchmarkIndicator().setN09(si.getBenchmarkIndicator().getN09());
                    entity.getBenchmarkIndicator().setN10(si.getBenchmarkIndicator().getN10());
                    entity.getBenchmarkIndicator().setN11(si.getBenchmarkIndicator().getN11());
                    entity.getBenchmarkIndicator().setN12(si.getBenchmarkIndicator().getN12());
                    break;
                case "Q":
                    entity.getBenchmarkIndicator().setNfy(si.getBenchmarkIndicator().getNfy());
                    entity.getBenchmarkIndicator().setNh2(si.getBenchmarkIndicator().getNh2());
                    entity.getBenchmarkIndicator().setNh1(si.getBenchmarkIndicator().getNh1());
                    entity.getBenchmarkIndicator().setNq1(si.getBenchmarkIndicator().getNq1());
                    entity.getBenchmarkIndicator().setNq2(si.getBenchmarkIndicator().getNq2());
                    entity.getBenchmarkIndicator().setNq3(si.getBenchmarkIndicator().getNq3());
                    entity.getBenchmarkIndicator().setNq4(si.getBenchmarkIndicator().getNq4());
                    break;
            }
        }
    }

    public void updatePerformance(Indicator entity) {
        if (entity != null) {
            switch (entity.getPerfCalc()) {
                case "AT":
                    if (entity.getTargetIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN01(entity.getActualIndicator().getN01()
                                .divide(entity.getTargetIndicator().getN01(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN01().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN01(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN01().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN01(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN01(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN02(entity.getActualIndicator().getN02()
                                .divide(entity.getTargetIndicator().getN02(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN02().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN02(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN02().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN02(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN02(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN03(entity.getActualIndicator().getN03()
                                .divide(entity.getTargetIndicator().getN03(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN03().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN03(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN03().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN03(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN03(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN04(entity.getActualIndicator().getN04()
                                .divide(entity.getTargetIndicator().getN04(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN04().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN04(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN04().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN04(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN04(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN05(entity.getActualIndicator().getN05()
                                .divide(entity.getTargetIndicator().getN05(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN05().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN05(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN05().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN05(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN05(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN06(entity.getActualIndicator().getN06()
                                .divide(entity.getTargetIndicator().getN06(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN06().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN06(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN05().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN05(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN06(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN07(entity.getActualIndicator().getN07()
                                .divide(entity.getTargetIndicator().getN07(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN07().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN07(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN07().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN07(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN07(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN08(entity.getActualIndicator().getN08()
                                .divide(entity.getTargetIndicator().getN08(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN08().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN08(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN08().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN08(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN08(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN09(entity.getActualIndicator().getN09()
                                .divide(entity.getTargetIndicator().getN09(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN09().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN09(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN09().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN09(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN09(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN10(entity.getActualIndicator().getN10()
                                .divide(entity.getTargetIndicator().getN10(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN10().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN10(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN10().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN10(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN10(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN11(entity.getActualIndicator().getN11()
                                .divide(entity.getTargetIndicator().getN11(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN11().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN11(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN11().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN11(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN11(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN12(entity.getActualIndicator().getN12()
                                .divide(entity.getTargetIndicator().getN12(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getN12().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setN12(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getN12().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setN12(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setN12(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq1(entity.getActualIndicator().getNq1()
                                .divide(entity.getTargetIndicator().getNq1(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNq1(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNq1(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNq1(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq2(entity.getActualIndicator().getNq2()
                                .divide(entity.getTargetIndicator().getNq2(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNq2(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNq2(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNq2(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq3(entity.getActualIndicator().getNq3()
                                .divide(entity.getTargetIndicator().getNq3(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNq3(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNq3(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNq3(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq4(entity.getActualIndicator().getNq4()
                                .divide(entity.getTargetIndicator().getNq4(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNq4(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNq4(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNq4(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNh1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNh1(entity.getActualIndicator().getNh1()
                                .divide(entity.getTargetIndicator().getNh1(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNh1().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNh1(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNh1().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNh1(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNh1(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNh2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNh2(entity.getActualIndicator().getNh2()
                                .divide(entity.getTargetIndicator().getNh2(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNh2().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNh2(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNh2().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNh2(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNh2(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getTargetIndicator().getNfy().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNfy(entity.getActualIndicator().getNfy()
                                .divide(entity.getTargetIndicator().getNfy(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getActualIndicator().getNfy().compareTo(BigDecimal.ZERO) > 0) {
                            entity.getPerformanceIndicator().setNfy(BigDecimal.valueOf(100d));
                        } else if (entity.getActualIndicator().getNfy().compareTo(BigDecimal.ZERO) < 0) {
                            entity.getPerformanceIndicator().setNfy(BigDecimal.valueOf(-100d));
                        } else {
                            entity.getPerformanceIndicator().setNfy(BigDecimal.valueOf(0d));
                        }
                    }
                    break;
                case "TA":
                    if (entity.getActualIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN01(entity.getTargetIndicator().getN01()
                                .divide(entity.getActualIndicator().getN01(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN01(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN01(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN02(entity.getTargetIndicator().getN02()
                                .divide(entity.getActualIndicator().getN02(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN02(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN02(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN03(entity.getTargetIndicator().getN03()
                                .divide(entity.getActualIndicator().getN03(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN03(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN03(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN04(entity.getTargetIndicator().getN04()
                                .divide(entity.getActualIndicator().getN04(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN04(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN04(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN05(entity.getTargetIndicator().getN05()
                                .divide(entity.getActualIndicator().getN05(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN05(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN05(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN06(entity.getTargetIndicator().getN06()
                                .divide(entity.getActualIndicator().getN06(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN06(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN06(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN07(entity.getTargetIndicator().getN07()
                                .divide(entity.getActualIndicator().getN07(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN07(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN07(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN08(entity.getTargetIndicator().getN08()
                                .divide(entity.getActualIndicator().getN08(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN08(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN08(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN09(entity.getTargetIndicator().getN09()
                                .divide(entity.getActualIndicator().getN09(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN09(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN09(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN10(entity.getTargetIndicator().getN10()
                                .divide(entity.getActualIndicator().getN10(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN10(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN10(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN11(entity.getTargetIndicator().getN11()
                                .divide(entity.getActualIndicator().getN11(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN11(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN11(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setN12(entity.getTargetIndicator().getN12()
                                .divide(entity.getActualIndicator().getN12(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setN12(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setN12(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq1(entity.getTargetIndicator().getNq1()
                                .divide(entity.getActualIndicator().getNq1(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNq1(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNq1(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq2(entity.getTargetIndicator().getNq2()
                                .divide(entity.getActualIndicator().getNq2(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNq2(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNq2(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq3(entity.getTargetIndicator().getNq3()
                                .divide(entity.getActualIndicator().getNq3(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNq3(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNq3(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNq4(entity.getTargetIndicator().getNq4()
                                .divide(entity.getActualIndicator().getNq4(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNq4(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNq4(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNh1().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNh1(entity.getTargetIndicator().getNh1()
                                .divide(entity.getActualIndicator().getNh1(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNh1().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNh1(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNh1(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNh2().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNh2(entity.getTargetIndicator().getNh2()
                                .divide(entity.getActualIndicator().getNh2(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNh2().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNh2(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNh2(BigDecimal.valueOf(0d));
                        }
                    }
                    if (entity.getActualIndicator().getNfy().compareTo(BigDecimal.ZERO) != 0) {
                        entity.getPerformanceIndicator()
                            .setNfy(entity.getTargetIndicator().getNfy()
                                .divide(entity.getActualIndicator().getNfy(), 4, RoundingMode.HALF_UP)
                                .multiply(BigDecimal.valueOf(100d)));
                    } else {
                        if (entity.getTargetIndicator().getNfy().compareTo(BigDecimal.ZERO) != 0) {
                            entity.getPerformanceIndicator().setNfy(BigDecimal.valueOf(100d));
                        } else {
                            entity.getPerformanceIndicator().setNfy(BigDecimal.valueOf(0d));
                        }
                    }
                    break;
            }
        }
    }

    public void updateTarget(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null || "".equals(entity.getActualInterface())) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getTargetIndicator().setNfy(si.getTargetIndicator().getNfy());
                    entity.getTargetIndicator().setNh2(si.getTargetIndicator().getNh2());
                    entity.getTargetIndicator().setNh1(si.getTargetIndicator().getNh1());
                    entity.getTargetIndicator().setNq1(si.getTargetIndicator().getNq1());
                    entity.getTargetIndicator().setNq2(si.getTargetIndicator().getNq2());
                    entity.getTargetIndicator().setNq3(si.getTargetIndicator().getNq3());
                    entity.getTargetIndicator().setNq4(si.getTargetIndicator().getNq4());
                    entity.getTargetIndicator().setN01(si.getTargetIndicator().getN01());
                    entity.getTargetIndicator().setN02(si.getTargetIndicator().getN02());
                    entity.getTargetIndicator().setN03(si.getTargetIndicator().getN03());
                    entity.getTargetIndicator().setN04(si.getTargetIndicator().getN04());
                    entity.getTargetIndicator().setN05(si.getTargetIndicator().getN05());
                    entity.getTargetIndicator().setN06(si.getTargetIndicator().getN06());
                    entity.getTargetIndicator().setN07(si.getTargetIndicator().getN07());
                    entity.getTargetIndicator().setN08(si.getTargetIndicator().getN08());
                    entity.getTargetIndicator().setN09(si.getTargetIndicator().getN09());
                    entity.getTargetIndicator().setN10(si.getTargetIndicator().getN10());
                    entity.getTargetIndicator().setN11(si.getTargetIndicator().getN11());
                    entity.getTargetIndicator().setN12(si.getTargetIndicator().getN12());
                    break;
                case "Q":
                    entity.getTargetIndicator().setNfy(si.getTargetIndicator().getNfy());
                    entity.getTargetIndicator().setNh2(si.getTargetIndicator().getNh2());
                    entity.getTargetIndicator().setNh1(si.getTargetIndicator().getNh1());
                    entity.getTargetIndicator().setNq1(si.getTargetIndicator().getNq1());
                    entity.getTargetIndicator().setNq2(si.getTargetIndicator().getNq2());
                    entity.getTargetIndicator().setNq3(si.getTargetIndicator().getNq3());
                    entity.getTargetIndicator().setNq4(si.getTargetIndicator().getNq4());
                    break;
            }
        }
    }

    public void updateForecast(Indicator entity) {
        List<Indicator> indicators = new ArrayList<>();
        if (entity.isAssigned()) {
            List<IndicatorAssignment> assList = indicatorAssignmentBean.findByPId(entity.getId());
            for (IndicatorAssignment ia : assList) {
                Indicator i = findByFormidYearAndDeptno(ia.getFormid(), entity.getSeq(), ia.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        } else if (entity.getActualInterface() == null || "".equals(entity.getActualInterface())) {
            List<IndicatorSet> setList = indicatorSetBean.findByPId(entity.getId());
            for (IndicatorSet is : setList) {
                Indicator i = findByFormidYearAndDeptno(is.getFormid(), entity.getSeq(), is.getDeptno());
                if (i != null) {
                    indicators.add(i);
                }
            }
        }
        indicatorAssignmentBean.getEntityManager().clear();
        indicatorSetBean.getEntityManager().clear();
        getEntityManager().clear();
        if (!indicators.isEmpty()) {
            Indicator si = getSumValue(indicators);
            switch (entity.getFormkind()) {
                case "M":
                    entity.getForecastIndicator().setNfy(si.getForecastIndicator().getNfy());
                    entity.getForecastIndicator().setNh2(si.getForecastIndicator().getNh2());
                    entity.getForecastIndicator().setNh1(si.getForecastIndicator().getNh1());
                    entity.getForecastIndicator().setNq1(si.getForecastIndicator().getNq1());
                    entity.getForecastIndicator().setNq2(si.getForecastIndicator().getNq2());
                    entity.getForecastIndicator().setNq3(si.getForecastIndicator().getNq3());
                    entity.getForecastIndicator().setNq4(si.getForecastIndicator().getNq4());
                    entity.getForecastIndicator().setN01(si.getForecastIndicator().getN01());
                    entity.getForecastIndicator().setN02(si.getForecastIndicator().getN02());
                    entity.getForecastIndicator().setN03(si.getForecastIndicator().getN03());
                    entity.getForecastIndicator().setN04(si.getForecastIndicator().getN04());
                    entity.getForecastIndicator().setN05(si.getForecastIndicator().getN05());
                    entity.getForecastIndicator().setN06(si.getForecastIndicator().getN06());
                    entity.getForecastIndicator().setN07(si.getForecastIndicator().getN07());
                    entity.getForecastIndicator().setN08(si.getForecastIndicator().getN08());
                    entity.getForecastIndicator().setN09(si.getForecastIndicator().getN09());
                    entity.getForecastIndicator().setN10(si.getForecastIndicator().getN10());
                    entity.getForecastIndicator().setN11(si.getForecastIndicator().getN11());
                    entity.getForecastIndicator().setN12(si.getForecastIndicator().getN12());
                    break;
                case "Q":
                    entity.getForecastIndicator().setNfy(si.getForecastIndicator().getNfy());
                    entity.getForecastIndicator().setNh2(si.getForecastIndicator().getNh2());
                    entity.getForecastIndicator().setNh1(si.getForecastIndicator().getNh1());
                    entity.getForecastIndicator().setNq1(si.getForecastIndicator().getNq1());
                    entity.getForecastIndicator().setNq2(si.getForecastIndicator().getNq2());
                    entity.getForecastIndicator().setNq3(si.getForecastIndicator().getNq3());
                    entity.getForecastIndicator().setNq4(si.getForecastIndicator().getNq4());
                    break;
            }
        }
    }

}
