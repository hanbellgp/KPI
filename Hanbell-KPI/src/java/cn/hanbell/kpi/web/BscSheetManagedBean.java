/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.web;

import cn.hanbell.kpi.ejb.IndicatorAnalysisBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorSetBean;
import cn.hanbell.kpi.ejb.IndicatorSummaryBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.entity.IndicatorSummary;
import cn.hanbell.kpi.entity.RoleGrantModule;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C0160
 */
public abstract class BscSheetManagedBean extends SuperQueryBean<Indicator> {

    @EJB
    protected IndicatorBean indicatorBean;
    @EJB
    protected IndicatorChartBean indicatorChartBean;
    @EJB
    protected IndicatorAnalysisBean indicatorAnalysisBean;
    @EJB
    protected IndicatorSummaryBean indicatorSummaryBean;
    @EJB
    protected IndicatorSetBean indicatorSetBean;

    protected Indicator indicator;
    protected IndicatorChart indicatorChart;
    protected IndicatorDetail actualAccumulated;
    protected IndicatorDetail benchmarkAccumulated;
    protected IndicatorDetail targetAccumulated;
    protected IndicatorDetail AP;
    protected IndicatorDetail BG;
    protected IndicatorDetail AG;
    //合计
    protected Indicator sumIndicator;
    protected IndicatorDetail sumActualAccumulated;
    protected IndicatorDetail sumBenchmarkAccumulated;
    protected IndicatorDetail sumTargetAccumulated;
    protected IndicatorDetail sumAP;
    protected IndicatorDetail sumBG;
    protected IndicatorDetail sumAG;

    protected List<Indicator> indicatorList;
    protected List<IndicatorSet> indicatorSetList;

    protected List<IndicatorDetail> indicatorDetailList;

    protected final DecimalFormat decimalFormat;

    protected List<IndicatorAnalysis> analysisList;
    protected List<IndicatorSummary> summaryList;
    protected int analysisCount;
    protected int summaryCount;

    protected int y;
    protected int m;

    protected int scale;

    private IndicatorChart sheetChart;
    private boolean isExists;
    private String formidValue;
    protected boolean monthchecked;

    public BscSheetManagedBean() {
        super(Indicator.class);
        this.decimalFormat = new DecimalFormat("#,###");
        //初始化对象
        indicatorList = new ArrayList<>();
        indicatorDetailList = new ArrayList<>();
        formidValue = "";
        isExists = true;
    }

    @PostConstruct
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        if (!fc.isPostback()) {
            Calendar c = Calendar.getInstance();
            c.setTime(userManagedBean.getBaseDate());
            y = c.get(Calendar.YEAR);
            m = c.get(Calendar.MONTH) + 1;
            this.init();
        }
    }

    public boolean isExists(String formid, String deptno) {
        List<IndicatorChart> charts = null;
        //相同formid只查询一次
        if (!formidValue.equals(formid)) {
            formidValue = formid;
            charts = indicatorChartBean.findByFormidAndDeptno(formid, deptno);
            if (charts != null && !charts.isEmpty()) {
                sheetChart = charts.get(0);
                isExists = true;
                return true;
            }
            isExists = false;
            return false;
        }
        return isExists;
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
        //产品合计逻辑
        try {
            for (int i = getM(); i > 0; i--) {
                //合计累计达成
                v = indicatorBean.getAccumulatedPerformance(sumIndicator.getActualIndicator(), sumIndicator.getTargetIndicator(), i);
                setMethod = sumAP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAP, v);
                //合计同期成长
                v = indicatorBean.getGrowth(sumIndicator.getActualIndicator(), sumIndicator.getBenchmarkIndicator(), i);
                setMethod = sumBG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumBG, v);
                //合计累计成长
                v = indicatorBean.getGrowth(sumActualAccumulated, sumBenchmarkAccumulated, i);
                setMethod = sumAG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(sumAG, v);
            }
            //按当前月份累计值重设全年累计
            f = sumTargetAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumTargetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumTargetAccumulated).toString())));

            f = sumActualAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumActualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumActualAccumulated).toString())));

            f = sumBenchmarkAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            sumBenchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(sumBenchmarkAccumulated).toString())));

            sumIndicator.getTargetIndicator().setType("目标");
            indicatorDetailList.add(sumIndicator.getTargetIndicator());
            sumIndicator.getActualIndicator().setType("实际");
            indicatorDetailList.add(sumIndicator.getActualIndicator());
            sumIndicator.getPerformanceIndicator().setType("本月达成");
            indicatorDetailList.add(sumIndicator.getPerformanceIndicator());
            sumTargetAccumulated.setType("目标累计");
            indicatorDetailList.add(sumTargetAccumulated);
            sumActualAccumulated.setType("实际累计");
            indicatorDetailList.add(sumActualAccumulated);
            sumAP.setType("累计达成");
            indicatorDetailList.add(sumAP);
            sumIndicator.getBenchmarkIndicator().setType("去年同期");
            indicatorDetailList.add(sumIndicator.getBenchmarkIndicator());
            sumBenchmarkAccumulated.setType("同期累计");
            indicatorDetailList.add(sumBenchmarkAccumulated);
            sumBG.setType("同比成长");
            indicatorDetailList.add(sumBG);
            sumAG.setType("累计成长");
            indicatorDetailList.add(sumAG);

            //根据指标ID加载指标说明、指标分析
            analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
            if (analysisList != null) {
                this.analysisCount = analysisList.size();
            }
            summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
            if (summaryList != null) {
                this.summaryCount = summaryList.size();
            }

        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException ex) {
            log4j.error("bscReportManagedBean", ex);
        }
    }

    /**
     * @return the indicator
     */
    public Indicator getIndicator() {
        return indicator;
    }

    public String format(BigDecimal value) {
        if (value == null) {
            return "";
        } else {
            return decimalFormat.format(value);
        }
    }

    public String format(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return decimalFormat.format(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return decimalFormat.format(value);
        }
    }

    public String format(String type, BigDecimal value, int i) {
        switch (type) {
            case "P":
            case "达成":
            case "本月达成":
            case "当月达成":
            case "累计达成":
            case "同比成长":
            case "累计成长":
                return percentFormat(value, i);
            default:
                return format(value, i);
        }
    }

    public String percentFormat(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return indicatorBean.percentFormat(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return indicatorBean.percentFormat(value);
        }
    }
    
    public boolean visible(int m){
        if(m == this.m){
            return true;
        }else{
            if(monthchecked==false){
                return true;
            }else{
                return false;
            }
        }
    }

    /**
     * @return the indicatorChart
     */
    public IndicatorChart getIndicatorChart() {
        return indicatorChart;
    }

    /**
     * @return the actualAccumulated
     */
    public IndicatorDetail getActualAccumulated() {
        return actualAccumulated;
    }

    /**
     * @param actualAccumulated the actualAccumulated to set
     */
    public void setActualAccumulated(IndicatorDetail actualAccumulated) {
        this.actualAccumulated = actualAccumulated;
    }

    /**
     * @return the benchmarkAccumulated
     */
    public IndicatorDetail getBenchmarkAccumulated() {
        return benchmarkAccumulated;
    }

    /**
     * @param benchmarkAccumulated the benchmarkAccumulated to set
     */
    public void setBenchmarkAccumulated(IndicatorDetail benchmarkAccumulated) {
        this.benchmarkAccumulated = benchmarkAccumulated;
    }

    /**
     * @return the targetAccumulated
     */
    public IndicatorDetail getTargetAccumulated() {
        return targetAccumulated;
    }

    /**
     * @param targetAccumulated the targetAccumulated to set
     */
    public void setTargetAccumulated(IndicatorDetail targetAccumulated) {
        this.targetAccumulated = targetAccumulated;
    }

    /**
     * @return the BG
     */
    public IndicatorDetail getBG() {
        return BG;
    }

    /**
     * @return the AG
     */
    public IndicatorDetail getAG() {
        return AG;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the m
     */
    public int getM() {
        return m;
    }

    /**
     * @return the scale
     */
    public int getScale() {
        return scale;
    }

    /**
     * @param scale the scale to set
     */
    public void setScale(int scale) {
        this.scale = scale;
        if (scale == 2) {
            this.decimalFormat.applyPattern("###,##0.00");
            this.construct();
        } else {
            this.decimalFormat.applyPattern("###,###");
            this.construct();
        }
    }

    /**
     * @return the indicatorDetailList
     */
    public List<IndicatorDetail> getIndicatorDetailList() {
        return indicatorDetailList;
    }

    /**
     * @param indicatorDetailList the indicatorDetailList to set
     */
    public void setIndicatorDetailList(List<IndicatorDetail> indicatorDetailList) {
        this.indicatorDetailList = indicatorDetailList;
    }

    /**
     * @return the analysisList
     */
    public List<IndicatorAnalysis> getAnalysisList() {
        return analysisList;
    }

    /**
     * @return the summaryList
     */
    public List<IndicatorSummary> getSummaryList() {
        return summaryList;
    }

    /**
     * @return the analysisCount
     */
    public int getAnalysisCount() {
        return analysisCount;
    }

    /**
     * @return the summaryCount
     */
    public int getSummaryCount() {
        return summaryCount;
    }

    /**
     * @return the sheetChart
     */
    public IndicatorChart getSheetChart() {
        return sheetChart;
    }

    /**
     * @param sheetChart the sheetChart to set
     */
    public void setSheetChart(IndicatorChart sheetChart) {
        this.sheetChart = sheetChart;
    }

    /**
     * @return the isExists
     */
    public boolean isIsExists() {
        return isExists;
    }

    /**
     * @param isExists the isExists to set
     */
    public void setIsExists(boolean isExists) {
        this.isExists = isExists;
    }

    public boolean isMonthchecked() {
        return monthchecked;
    }

    public void setMonthchecked(boolean monthchecked) {
        this.monthchecked = monthchecked;
    }

}
