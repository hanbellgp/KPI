/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.InvamountPolicyTarget;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InvamountPolicyTargetBean implements Serializable {

    @EJB
    protected IndicatorBean indicatorBean;

    protected List<Indicator> indicators;
    protected Calendar c;
    protected int y;
    protected int m;
    protected Date d;
    protected DecimalFormat decimalFormat;
    protected Logger log4j = LogManager.getLogger();

    public InvamountPolicyTargetBean() {
        this.c = Calendar.getInstance();
        this.decimalFormat = new DecimalFormat("#,###");
        indicators = new ArrayList<>();
    }

    public void init() {
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
        this.d = c.getTime();
        if (this.indicators != null) {
            this.indicators.clear();
        }
    }

    public void addIndicators(Indicator i) {
        this.indicators.add(i);
    }

    //获取总表
    public List<InvamountPolicyTarget> getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) throws Exception {
        List<InvamountPolicyTarget> InventoryAmtList = new ArrayList<>();
        try {
            //添加指标
            for (Indicator i : indicatorList) {
                InventoryAmtList.addAll(getHtmlTableRow(i, y, m, d, false));
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return InventoryAmtList;
    }

    //获取各目标表的行
    protected List<InvamountPolicyTarget> getHtmlTableRow(Indicator indicator, int y, int m, Date d, boolean sumType) throws Exception {
        List<InvamountPolicyTarget> InventoryList1 = new ArrayList<>();
        //实例化对象
        InvamountPolicyTarget ita = new InvamountPolicyTarget();
        IndicatorDetail a = indicator.getActualIndicator();//实际
        IndicatorDetail b = indicator.getBenchmarkIndicator();//去年同期
        IndicatorDetail t = indicator.getTargetIndicator();//目标

        String mon;
        Field f;
        try {
            //总经理方针目标
            ita.setId(indicator.getCategory());

            //责任单位
            ita.setDeptName(indicator.getDeptname());

            //分类
            ita.setClassify(indicator.getFormid());

            //责任人
            ita.setResponsible(indicator.getRemark());

            //当季目标
            mon = this.getIndicatorColumn("N", m);
            f = t.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal tarAmt = BigDecimal.valueOf(Double.valueOf(f.get(t).toString()));
            ita.setTarget(tarAmt);

            //当月库存
            mon = this.getIndicatorColumn("N", m);
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal actAmt = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            ita.setActual(actAmt);

            //与目标差异 = 当月 - 目标
            ita.setDifference1((actAmt.subtract(tarAmt)).divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP));

            //上月库存
            mon = this.getIndicatorColumn("N", m - 1);
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal upactAmt = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            ita.setUpactual(upactAmt);

            //与上月差异 当月 - 上月
            ita.setDifference2((actAmt.subtract(upactAmt)).divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP));

            //去年同期库存
            mon = this.getIndicatorColumn("N", m);
            f = b.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal benAmt = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            ita.setBenchmark(benAmt);

            //与去年同期差异  当月 - 去年同期
            ita.setDifference3((actAmt.subtract(benAmt)).divide(BigDecimal.valueOf(10000), 2, BigDecimal.ROUND_HALF_UP));

            InventoryList1.add(ita);
        } catch (SecurityException | IllegalArgumentException ex) {
            throw new Exception(ex);
        }
        return InventoryList1;
    }

    public List<InvamountPolicyTarget> getInvamountPolicyTargetResultList(int y, int m) {
        List<InvamountPolicyTarget> InventoryAmtList = new ArrayList<>();
        try {
            //生产目标
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("生产目标", y);
            indicatorBean.getEntityManager().clear();
            //指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                InventoryAmtList.addAll(getHtmlTable(this.indicators, y, m, d, true));
            }
            //营业目标
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("营业目标", y);
            indicatorBean.getEntityManager().clear();
            //指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                InventoryAmtList.addAll(getHtmlTable(this.indicators, y, m, d, true));
            }

            //服务目标
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("服务目标", y);
            indicatorBean.getEntityManager().clear();
            //指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                InventoryAmtList.addAll(getHtmlTable(this.indicators, y, m, d, true));
            }
            //借出归还
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("借出未归", y);
            indicatorBean.getEntityManager().clear();
            //指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                InventoryAmtList.addAll(getHtmlTable(this.indicators, y, m, d, true));
            }
            //其他
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("其他目标", y);
            indicatorBean.getEntityManager().clear();
            //指标排序
            indicators.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() > o2.getSortid()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
                InventoryAmtList.addAll(getHtmlTable(this.indicators, y, m, d, true));
            }

            //最后的总合计
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear("库存金额总计", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                };
                InventoryAmtList.addAll(getHtmlTable(this.indicators, y, m, d, true));
            }
            return InventoryAmtList;
        } catch (Exception ex) {
            ex.toString();
        }
        return InventoryAmtList;
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

    //换算率
    public void divideByRateOther(Indicator i, int scale) {
        indicatorBean.divideByRate(i.getActualIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getBenchmarkIndicator(), i.getRate(), scale);
        indicatorBean.divideByRate(i.getTargetIndicator(), i.getRate(), scale);
    }

    public List<Indicator> getIndicators() {
        return indicators;
    }

    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

    public DecimalFormat getDecimalFormat() {
        return decimalFormat;
    }

    public void setDecimalFormat(DecimalFormat decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

}
