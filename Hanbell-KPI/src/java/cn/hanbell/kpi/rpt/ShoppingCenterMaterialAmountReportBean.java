/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.ejb.ShoppingManufacturerBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.util.BaseLib;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "shoppingCenterMaterialAmountReportBean")
@ViewScoped
public class ShoppingCenterMaterialAmountReportBean extends FinancingFreeServiceReportBean {

    protected final DecimalFormat percentFormat;
    protected final DecimalFormat decFormat;
    private List<Indicator> indicatorList;
    private List<Indicator> firstList;
    private Date btndate;
    private double monthSum;
    private double yearSum;
    private List<Object[]> shbList;
    private List<Object[]> thbList;
    private List<Object[]> hsList;
    private List<Object[]> scmList;
    private List<Object[]> zcmList;
    private List<Object[]> hyList;

    @EJB
    private ShoppingAccomuntBean shoppoingAccoumuntBean;

    @EJB
    private ShoppingManufacturerBean shoppingManufacturerBean;
    @EJB
    private IndicatorDetailBean indicatorDetailBean;

    public ShoppingCenterMaterialAmountReportBean() {
        super();
        this.percentFormat = new DecimalFormat("#0.00％");
        this.decFormat = new DecimalFormat("#,##0");
    }

    @PostConstruct
    @Override
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (getIndicatorChart() == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            for (RoleGrantModule m1 : userManagedBean.getRoleGrantDeptList()) {
                if (m1.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        BigDecimal a =new BigDecimal(0);
//        a.doubleValue()
        btndate = settlementDate().getTime();

        btnquery();
    }

    @Override
    public void btnquery() {
        try {
            m = btndate.getMonth() + 1;
            y = Integer.valueOf(BaseLib.formatDate("yyyy", btndate));
            indicatorList = indicatorBean.findByCategoryAndYear("物料采购金额", y);
            Field f;
            String mon = indicatorBean.getIndicatorColumn("N", m);
            Double a1 = 0.0, a2 = 0.0, a3 = 0.0, a4 = 0.0, a5 = 0.0, a6 = 0.0, a7 = 0.0, b1 = 0.0;
            Collections.sort(indicatorList, new Comparator<Indicator>() {
                @Override
                public int compare(Indicator o1, Indicator o2) {
                    return o2.getActualIndicator().getNfy().compareTo(o1.getActualIndicator().getNfy());//倒序
                }

            });
            BigDecimal a=BigDecimal.ZERO;
            for (Indicator indicator : indicatorList) {
                indicator.setCategory(m + "月");
                indicator.setCategoryId(m);
                //计算合计
                IndicatorDetail o1 = indicator.getOther1Indicator();
                f = o1.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a1 += Double.valueOf(f.get(o1).toString());

                IndicatorDetail o2 = indicator.getOther2Indicator();
                f = o2.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a2 += Double.valueOf(f.get(o2).toString());

                IndicatorDetail o3 = indicator.getOther3Indicator();
                f = o3.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a3 += Double.valueOf(f.get(o3).toString());

                IndicatorDetail o4 = indicator.getOther4Indicator();
                f = o4.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a4 += Double.valueOf(f.get(o4).toString());

                IndicatorDetail o5 = indicator.getOther5Indicator();
                f = o5.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a5 += Double.valueOf(f.get(o5).toString());

                IndicatorDetail o6 = indicator.getOther6Indicator();
                f = o6.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                a6 += Double.valueOf(f.get(o6).toString());

                a7 += indicator.getActualIndicator().getNfy().doubleValue();
                IndicatorDetail bench = indicator.getBenchmarkIndicator();
                f = bench.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                b1 += Double.valueOf(f.get(bench).toString());
            }
            //实际合计
            Indicator sumIndicator = new Indicator();
            sumIndicator.setDescript("合计");
            sumIndicator.setRate(new BigDecimal(10000));
            IndicatorDetail authDetail = new IndicatorDetail();
            f = authDetail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(authDetail, BigDecimal.valueOf(a1 + a2 + a3+a4+a5+a6));

            //上海汉钟合计
            IndicatorDetail other1Detail = new IndicatorDetail();
            f = other1Detail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(other1Detail, BigDecimal.valueOf(a1));
            //浙江汉声合计
            IndicatorDetail other2Detail = new IndicatorDetail();
            f = other2Detail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(other2Detail, BigDecimal.valueOf(a2));
            //台湾汉钟合计
            IndicatorDetail other3Detail = new IndicatorDetail();
            f = other3Detail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(other3Detail, BigDecimal.valueOf(a3));

            //上海柯茂合计
            IndicatorDetail other4Detail = new IndicatorDetail();
            f = other4Detail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(other4Detail, BigDecimal.valueOf(a4));
            //浙江柯茂合计
            IndicatorDetail other5Detail = new IndicatorDetail();
            f = other5Detail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(other5Detail, BigDecimal.valueOf(a5));
            //安徽汉扬合计
            IndicatorDetail other6Detail = new IndicatorDetail();
            f = other6Detail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(other6Detail, BigDecimal.valueOf(a6));
            //基准值合计
            IndicatorDetail benchDetail = new IndicatorDetail();
            f = benchDetail.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            f.set(benchDetail, BigDecimal.valueOf(b1));
            //实际合计
            authDetail.setNfy(BigDecimal.valueOf( a7));
            sumIndicator.setActualIndicator(authDetail);
            sumIndicator.setOther1Indicator(other1Detail);
            sumIndicator.setOther2Indicator(other2Detail);
            sumIndicator.setOther3Indicator(other3Detail);
            sumIndicator.setOther4Indicator(other4Detail);
            sumIndicator.setOther5Indicator(other5Detail);
            sumIndicator.setOther6Indicator(other6Detail);
            sumIndicator.setBenchmarkIndicator(benchDetail);

            indicatorList.add(sumIndicator);
            monthSum = a1 + a2 + a3 + a4 + a5 + a6;
            yearSum = a7;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public double getYearMonth(Indicator indicator, int m) {
        return 0.0;
    }

    public String percentValue(double a, double b) {
        if (b == 0.0) {
            return "0.00%";
        }
        return percentFormat.format(a * 100 / b);
    }

    public String format(BigDecimal value, BigDecimal rate) {
        if (BigDecimal.ZERO.equals(value)) {
            return "0";
        }
        return decFormat.format(value.divide(rate));
    }

    public List<Indicator> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    public List<Indicator> getFirstList() {
        return firstList;
    }

    public void setFirstList(List<Indicator> firstList) {
        this.firstList = firstList;
    }

    public Date getBtndate() {
        return btndate;
    }

    public void setBtndate(Date btndate) {
        this.btndate = btndate;
    }

    public double getMonthSum() {
        return monthSum;
    }

    public void setMonthSum(double monthSum) {
        this.monthSum = monthSum;
    }

    public double getYearSum() {
        return yearSum;
    }

    public void setYearSum(double yearSum) {
        this.yearSum = yearSum;
    }

}
