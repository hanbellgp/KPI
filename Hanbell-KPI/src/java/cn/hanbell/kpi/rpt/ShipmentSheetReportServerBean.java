/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscSheetManagedBean;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "ShipmentSheetReportServerBean")
@ViewScoped
public class ShipmentSheetReportServerBean extends BscSheetManagedBean {

    /**
     * Creates a new instance of ShipmentReportBean
     */
    public ShipmentSheetReportServerBean() {

    }

    @Override
    public void init() {
        //初始化 默认为只显示当月
        monthchecked = true;
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        String mon;
        Field f;
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (indicatorChart == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            for (RoleGrantModule m : userManagedBean.getRoleGrantDeptList()) {
                if (m.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        indicator = indicatorBean.findByFormidYearAndDeptno(indicatorChart.getFormid(), getY(), indicatorChart.getDeptno());
        if (indicator == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorList.clear();
        indicatorDetailList.clear();

        if (indicator.isAssigned()) {
            indicatorList = indicatorBean.findByPId(indicator.getId());
        } else {
            //找到指标相关子阶
            indicatorSetList = indicatorSetBean.findByPId(indicator.getId());
            if (indicatorSetList == null || indicatorSetList.isEmpty()) {
                fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
            }
            for (IndicatorSet is : indicatorSetList) {
                indicator = indicatorBean.findByFormidYearAndDeptno(is.getFormid(), getY(), is.getDeptno());
                if (indicator != null) {
                    indicatorList.add(indicator);
                }
            }
        }
        //指标排序
        indicatorList.sort((Indicator o1, Indicator o2) -> {
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });

        for (Indicator e : indicatorList) {
            //按换算率计算结果
            indicatorBean.divideByRate(e, 2);
        }

        //计算产品合计
        sumIndicator = indicatorBean.getSumValue(indicatorList);
        //合计本月达成
        indicatorBean.updatePerformance(sumIndicator);

        sumActualAccumulated = new IndicatorDetail();
        sumActualAccumulated.setParent(sumIndicator);
        sumActualAccumulated.setType("A");

        sumBenchmarkAccumulated = new IndicatorDetail();
        sumBenchmarkAccumulated.setParent(sumIndicator);
        sumBenchmarkAccumulated.setType("B");

        sumTargetAccumulated = new IndicatorDetail();
        sumTargetAccumulated.setParent(sumIndicator);
        sumTargetAccumulated.setType("T");

        sumAP = new IndicatorDetail();
        sumAP.setParent(sumIndicator);
        sumAP.setType("P");

        sumBG = new IndicatorDetail();
        sumBG.setParent(sumIndicator);
        sumBG.setType("P");

        sumAG = new IndicatorDetail();
        sumAG.setParent(sumIndicator);
        sumAG.setType("P");

        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        //计算每个指标的累计
        for (Indicator e : indicatorList) {

            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            benchmarkAccumulated = new IndicatorDetail();
            benchmarkAccumulated.setParent(e);
            benchmarkAccumulated.setType("B");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");

            //计算累计
            try {
                for (int i = getM(); i > 0; i--) {
                    //顺序计算的话会导致累计值重复累加
                    //实际值累计
                    v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                    setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(actualAccumulated, v);
                    //同期值累计
                    v = indicatorBean.getAccumulatedValue(e.getBenchmarkIndicator(), i);
                    setMethod = getBenchmarkAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(benchmarkAccumulated, v);
                    //目标值累计
                    v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                    setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(targetAccumulated, v);
                    //累计达成
                    v = indicatorBean.getAccumulatedPerformance(e.getActualIndicator(), e.getTargetIndicator(), i);
                    setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AP, v);
                    //同比成长率
                    v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                    setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(BG, v);
                    //累计成长率
                    v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                    setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                    setMethod.invoke(AG, v);
                }
                //按当前月份累计值重设全年累计
                f = actualAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

                f = benchmarkAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                benchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(benchmarkAccumulated).toString())));

                f = targetAccumulated.getClass().getDeclaredField(mon);
                f.setAccessible(true);
                targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));
                //M表示月份型
                indicatorBean.addValue(sumActualAccumulated, actualAccumulated, "M");
                indicatorBean.addValue(sumBenchmarkAccumulated, benchmarkAccumulated, "M");
                indicatorBean.addValue(sumTargetAccumulated, targetAccumulated, "M");

            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
                log4j.error("bscReportManagedBean", ex);
            }
            e.getTargetIndicator().setType("目标");
            indicatorDetailList.add(e.getTargetIndicator());
            e.getActualIndicator().setType("实际");
            indicatorDetailList.add(e.getActualIndicator());
            e.getPerformanceIndicator().setType("本月达成");
            indicatorDetailList.add(e.getPerformanceIndicator());
            targetAccumulated.setType("目标累计");
            indicatorDetailList.add(targetAccumulated);
            actualAccumulated.setType("实际累计");
            indicatorDetailList.add(actualAccumulated);
            AP.setType("累计达成");
            indicatorDetailList.add(AP);
            e.getBenchmarkIndicator().setType("去年同期");
            indicatorDetailList.add(e.getBenchmarkIndicator());
            benchmarkAccumulated.setType("去年累计");
            indicatorDetailList.add(benchmarkAccumulated);
            BG.setType("同比成长");
            indicatorDetailList.add(BG);
            AG.setType("累计成长");
            indicatorDetailList.add(AG);

        }
    }
}

