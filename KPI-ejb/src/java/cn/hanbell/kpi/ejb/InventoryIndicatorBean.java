/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.InventoryIndicator;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.MathContext;
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
public class InventoryIndicatorBean implements Serializable {

    @EJB
    protected IndicatorBean indicatorBean;

    protected List<Indicator> indicators;
    protected Calendar c;
    protected int y;
    protected int m;
    protected Date d;
    protected final DecimalFormat doubleFormat;
    protected Logger log4j = LogManager.getLogger();

    public InventoryIndicatorBean() {
        this.c = Calendar.getInstance();
        this.doubleFormat = new DecimalFormat("###,###.##");
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

    // 获取总表
    public List<InventoryIndicator> getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum)
            throws Exception {
        List<InventoryIndicator> InventoryAmtList = new ArrayList<>();
        try {
            // 添加指标
            for (Indicator i : indicatorList) {
                InventoryAmtList.addAll(getHtmlTableRow(i, y, m, d, false));
            }
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return InventoryAmtList;
    }

    // 获取各目标表的行
    protected List<InventoryIndicator> getHtmlTableRow(Indicator indicator, int y, int m, Date d, boolean sumType)
            throws Exception {
        List<InventoryIndicator> inventoryList = new ArrayList<>();
        // 实例化对象
        InventoryIndicator ita = new InventoryIndicator();
        IndicatorDetail a = indicator.getActualIndicator();// 实际
        IndicatorDetail b = indicator.getBenchmarkIndicator();// 去年同期
        IndicatorDetail t = indicator.getTargetIndicator();// 目标

        String mon;
        Field f;
        try {
            // 总经理方针目标
            ita.setId(indicator.getCategory());

            // 责任单位
            ita.setDeptName(indicator.getDeptname());

            // 分类
            ita.setCategory(indicator.getDescript());

            // 当季目标
            mon = this.getIndicatorColumn("N", m);
            f = t.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal tarAmt = BigDecimal.valueOf(Double.valueOf(f.get(t).toString()));
            ita.setTarget(tarAmt);

            // 当月库存
            mon = this.getIndicatorColumn("N", m);
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal actAmt = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
            ita.setActual(actAmt);

            // 与目标差异 = 当月 - 目标
            ita.setTargetThan(actAmt.subtract(tarAmt));

            // 上月库存
            BigDecimal upactAmt = BigDecimal.ZERO;
            if (m == 1) {
                Indicator lastindicator = indicatorBean.findByFormidYearAndDeptno(indicator.getFormid(), y - 1, indicator.getDeptno());
                this.divideByRateOther(lastindicator, 2);
                IndicatorDetail upa = lastindicator.getActualIndicator();//去年的实际值
                mon = this.getIndicatorColumn("N", 12);
                f = upa.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                upactAmt = BigDecimal.valueOf(Double.valueOf(f.get(upa).toString()));
                ita.setUpactual(upactAmt);
            } else {
                mon = this.getIndicatorColumn("N", m - 1);
                f = a.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                upactAmt = BigDecimal.valueOf(Double.valueOf(f.get(a).toString()));
                ita.setUpactual(upactAmt);
            }
            // 与上月差异 当月 - 上月
            ita.setUpactualThan(actAmt.subtract(upactAmt));
            //同期金额
            mon = this.getIndicatorColumn("N", m);
            f = a.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            BigDecimal benchmarkAmt = BigDecimal.valueOf(Double.valueOf(f.get(b).toString()));
            ita.setBenchmark(benchmarkAmt);
            //与同期比
            ita.setBenchmarkThan(actAmt.subtract(benchmarkAmt));
            inventoryList.add(ita);
        } catch (SecurityException | IllegalArgumentException ex) {
            throw new Exception(ex);
        }
        return inventoryList;
    }

    public List<InventoryIndicator> getInventoryIndicatorResultList(int y, int m,String facno) {
        List<InventoryIndicator> InventoryAmtList = new ArrayList<>();
        String category = "";
        if(!facno.isEmpty() && facno.equals("K")){
            category = "柯茂";
        }
        try {
            // 生产目标
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear(category+"生产目标", y);
            indicatorBean.getEntityManager().clear();
            // 指标排序
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
            // 营业目标
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear(category+"营业目标", y);
            indicatorBean.getEntityManager().clear();
            // 指标排序
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

            // 服务目标
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear(category+"服务目标", y);
            indicatorBean.getEntityManager().clear();
            // 指标排序
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
            // 借出归还
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear(category+"借出未归", y);
            indicatorBean.getEntityManager().clear();
            // 指标排序
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
            // 其他
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear(category+"其他目标", y);
            indicatorBean.getEntityManager().clear();
            // 指标排序
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

            // 最后的总合计
            if (this.indicators.size() > 0) {
                this.indicators.clear();
            }
            indicators = indicatorBean.findByCategoryAndYear(category+"库存金额总计", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    this.divideByRateOther(i, 2);
                }
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

    // 换算率
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

}
