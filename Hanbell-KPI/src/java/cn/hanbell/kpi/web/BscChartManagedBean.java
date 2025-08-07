/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.web;

import cn.hanbell.kpi.ejb.IndicatorAnalysisBean;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorSummaryBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorAnalysis;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSummary;
import cn.hanbell.kpi.entity.RoleGrantModule;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C0160
 */
public abstract class BscChartManagedBean extends SuperQueryBean<Indicator> {

    @EJB
    protected IndicatorBean indicatorBean;
    @EJB
    protected IndicatorChartBean indicatorChartBean;
    @EJB
    protected IndicatorAnalysisBean indicatorAnalysisBean;
    @EJB
    protected IndicatorSummaryBean indicatorSummaryBean;

    protected Indicator indicator;
    protected IndicatorChart indicatorChart;
    protected IndicatorDetail actualAccumulated;
    protected IndicatorDetail benchmarkAccumulated;
    protected IndicatorDetail targetAccumulated;
    protected IndicatorDetail AP;
    protected IndicatorDetail BG;
    protected IndicatorDetail AG;

    protected final DecimalFormat decimalFormat;
    protected final DecimalFormat percentFormat;
    protected LineChartModel chartModel;
    protected LineChartModel accumulatedChartModel;


    protected List<IndicatorAnalysis> analysisList;
    protected List<IndicatorSummary> summaryList;
    protected int analysisCount;
    protected int summaryCount;

    protected int y;
    protected int m;

    protected int scale;
    protected boolean monthchecked;

    public BscChartManagedBean() {
        super(Indicator.class);
        this.decimalFormat = new DecimalFormat("#,###");
        this.percentFormat = new DecimalFormat("#0.00％");
    }

    @PostConstruct
    @Override
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

    @Override
    public void init() {
        monthchecked = true;
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
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
        //按换算率计算结果
        indicatorBean.divideByRate(indicator, 2);

        actualAccumulated = new IndicatorDetail();
        actualAccumulated.setParent(indicator);
        actualAccumulated.setType("A");

        benchmarkAccumulated = new IndicatorDetail();
        benchmarkAccumulated.setParent(indicator);
        benchmarkAccumulated.setType("B");

        targetAccumulated = new IndicatorDetail();
        targetAccumulated.setParent(indicator);
        targetAccumulated.setType("T");

        AP = new IndicatorDetail();
        AP.setParent(indicator);
        AP.setType("P");

        BG = new IndicatorDetail();
        BG.setParent(indicator);
        BG.setType("P");

        AG = new IndicatorDetail();
        AG.setParent(indicator);
        AG.setType("P");

        try {
            for (int i = 1; i <= getM(); i++) {
                BigDecimal v;
                Method setMethod;
                //实际值累计
                v = indicatorBean.getAccumulatedValue(indicator.getActualIndicator(), i);
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(actualAccumulated, v);
                //同期值累计
                v = indicatorBean.getAccumulatedValue(indicator.getBenchmarkIndicator(), i);
                setMethod = getActualAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(benchmarkAccumulated, v);
                //目标值累计
                v = indicatorBean.getAccumulatedValue(indicator.getTargetIndicator(), i);
                setMethod = getTargetAccumulated().getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(targetAccumulated, v);
                //累计达成
                v = indicatorBean.getAccumulatedPerformance(indicator.getActualIndicator(), indicator.getTargetIndicator(), i);
                setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AP, v);
                //同比成长率
                v = indicatorBean.getGrowth(indicator.getActualIndicator(), indicator.getBenchmarkIndicator(), i);
                setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(BG, v);
                //累计成长率
                v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AG, v);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            log4j.error("bscReportManagedBean", ex);
        }

        chartModel = new LineChartModel();
        ChartSeries t = new ChartSeries();
        t.setLabel("目标");
        switch (getIndicator().getFormkind()) {
            case "M":
                t.set("M01", getIndicator().getTargetIndicator().getN01().doubleValue());
                t.set("M02", getIndicator().getTargetIndicator().getN02().doubleValue());
                t.set("M03", getIndicator().getTargetIndicator().getN03().doubleValue());
                t.set("M04", getIndicator().getTargetIndicator().getN04().doubleValue());
                t.set("M05", getIndicator().getTargetIndicator().getN05().doubleValue());
                t.set("M06", getIndicator().getTargetIndicator().getN06().doubleValue());
                t.set("M07", getIndicator().getTargetIndicator().getN07().doubleValue());
                t.set("M08", getIndicator().getTargetIndicator().getN08().doubleValue());
                t.set("M09", getIndicator().getTargetIndicator().getN09().doubleValue());
                t.set("M10", getIndicator().getTargetIndicator().getN10().doubleValue());
                t.set("M11", getIndicator().getTargetIndicator().getN11().doubleValue());
                t.set("M12", getIndicator().getTargetIndicator().getN12().doubleValue());
                break;
            case "Q":
                t.set("Q1", getIndicator().getTargetIndicator().getNq1().doubleValue());
                t.set("Q2", getIndicator().getTargetIndicator().getNq2().doubleValue());
                t.set("Q3", getIndicator().getTargetIndicator().getNq3().doubleValue());
                t.set("Q4", getIndicator().getTargetIndicator().getNq4().doubleValue());
                break;
        }

        ChartSeries a = new ChartSeries();
        a.setLabel("实际");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (getIndicator().getActualIndicator().getN01().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M01", getIndicator().getActualIndicator().getN01().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN02().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M02", getIndicator().getActualIndicator().getN02().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN03().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M03", getIndicator().getActualIndicator().getN03().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN04().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M04", getIndicator().getActualIndicator().getN04().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN05().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M05", getIndicator().getActualIndicator().getN05().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN06().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M06", getIndicator().getActualIndicator().getN06().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN07().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M07", getIndicator().getActualIndicator().getN07().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN08().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M08", getIndicator().getActualIndicator().getN08().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN09().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M09", getIndicator().getActualIndicator().getN09().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN10().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M10", getIndicator().getActualIndicator().getN10().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN11().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M11", getIndicator().getActualIndicator().getN11().doubleValue());
                }
                if (getIndicator().getActualIndicator().getN12().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("M12", getIndicator().getActualIndicator().getN12().doubleValue());
                }
                break;
            case "Q":
                if (getIndicator().getActualIndicator().getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q1", getIndicator().getActualIndicator().getNq1().doubleValue());
                }
                if (getIndicator().getActualIndicator().getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q2", getIndicator().getActualIndicator().getNq2().doubleValue());
                }
                if (getIndicator().getActualIndicator().getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q3", getIndicator().getActualIndicator().getNq3().doubleValue());
                }
                if (getIndicator().getActualIndicator().getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    a.set("Q4", getIndicator().getActualIndicator().getNq4().doubleValue());
                }
                break;
        }

        ChartSeries b = new ChartSeries();
        b.setLabel("同期");
        switch (getIndicator().getFormkind()) {
            case "M":
                b.set("M01", getIndicator().getBenchmarkIndicator().getN01().doubleValue());
                b.set("M02", getIndicator().getBenchmarkIndicator().getN02().doubleValue());
                b.set("M03", getIndicator().getBenchmarkIndicator().getN03().doubleValue());
                b.set("M04", getIndicator().getBenchmarkIndicator().getN04().doubleValue());
                b.set("M05", getIndicator().getBenchmarkIndicator().getN05().doubleValue());
                b.set("M06", getIndicator().getBenchmarkIndicator().getN06().doubleValue());
                b.set("M07", getIndicator().getBenchmarkIndicator().getN07().doubleValue());
                b.set("M08", getIndicator().getBenchmarkIndicator().getN08().doubleValue());
                b.set("M09", getIndicator().getBenchmarkIndicator().getN09().doubleValue());
                b.set("M10", getIndicator().getBenchmarkIndicator().getN10().doubleValue());
                b.set("M11", getIndicator().getBenchmarkIndicator().getN11().doubleValue());
                b.set("M12", getIndicator().getBenchmarkIndicator().getN12().doubleValue());
                break;
            case "Q":
                b.set("Q1", getIndicator().getBenchmarkIndicator().getNq1().doubleValue());
                b.set("Q2", getIndicator().getBenchmarkIndicator().getNq2().doubleValue());
                b.set("Q3", getIndicator().getBenchmarkIndicator().getNq3().doubleValue());
                b.set("Q4", getIndicator().getBenchmarkIndicator().getNq4().doubleValue());
                break;
        }

        getChartModel().addSeries(t);//目标
        getChartModel().addSeries(b);//同期
        getChartModel().addSeries(a);//实际
        getChartModel().setTitle(getIndicator().getName());
        getChartModel().setLegendPosition("e");
        getChartModel().setShowPointLabels(true);
        getChartModel().setBreakOnNull(true);

        //累计
        accumulatedChartModel = new LineChartModel();
        ChartSeries at = new ChartSeries();
        at.setLabel("目标累计");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (targetAccumulated.getN01().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M01", targetAccumulated.getN01().doubleValue());
                }
                if (targetAccumulated.getN02().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M02", targetAccumulated.getN02().doubleValue());
                }
                if (targetAccumulated.getN03().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M03", targetAccumulated.getN03().doubleValue());
                }
                if (targetAccumulated.getN04().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M04", targetAccumulated.getN04().doubleValue());
                }
                if (targetAccumulated.getN05().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M05", targetAccumulated.getN05().doubleValue());
                }
                if (targetAccumulated.getN06().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M06", targetAccumulated.getN06().doubleValue());
                }
                if (targetAccumulated.getN07().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M07", targetAccumulated.getN07().doubleValue());
                }
                if (targetAccumulated.getN08().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M08", targetAccumulated.getN08().doubleValue());
                }
                if (targetAccumulated.getN09().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M09", targetAccumulated.getN09().doubleValue());
                }
                if (targetAccumulated.getN10().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M10", targetAccumulated.getN10().doubleValue());
                }
                if (targetAccumulated.getN11().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M11", targetAccumulated.getN11().doubleValue());
                }
                if (targetAccumulated.getN12().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("M12", targetAccumulated.getN12().doubleValue());
                }
                break;
            case "Q":
                if (targetAccumulated.getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("Q1", targetAccumulated.getNq1().doubleValue());
                }
                if (targetAccumulated.getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("Q2", targetAccumulated.getNq2().doubleValue());
                }
                if (targetAccumulated.getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("Q3", targetAccumulated.getNq3().doubleValue());
                }
                if (targetAccumulated.getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    at.set("Q4", targetAccumulated.getNq4().doubleValue());
                }
                break;
        }

        ChartSeries aa = new ChartSeries();
        aa.setLabel("实际累计");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (actualAccumulated.getN01().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M01", actualAccumulated.getN01().doubleValue());
                }
                if (actualAccumulated.getN02().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M02", actualAccumulated.getN02().doubleValue());
                }
                if (actualAccumulated.getN03().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M03", actualAccumulated.getN03().doubleValue());
                }
                if (actualAccumulated.getN04().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M04", actualAccumulated.getN04().doubleValue());
                }
                if (actualAccumulated.getN05().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M05", actualAccumulated.getN05().doubleValue());
                }
                if (actualAccumulated.getN06().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M06", actualAccumulated.getN06().doubleValue());
                }
                if (actualAccumulated.getN07().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M07", actualAccumulated.getN07().doubleValue());
                }
                if (actualAccumulated.getN08().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M08", actualAccumulated.getN08().doubleValue());
                }
                if (actualAccumulated.getN09().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M09", actualAccumulated.getN09().doubleValue());
                }
                if (actualAccumulated.getN10().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M10", actualAccumulated.getN10().doubleValue());
                }
                if (actualAccumulated.getN11().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M11", actualAccumulated.getN11().doubleValue());
                }
                if (actualAccumulated.getN12().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("M12", actualAccumulated.getN12().doubleValue());
                }
                break;
            case "Q":
                if (actualAccumulated.getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("Q1", actualAccumulated.getNq1().doubleValue());
                }
                if (actualAccumulated.getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("Q2", actualAccumulated.getNq2().doubleValue());
                }
                if (actualAccumulated.getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("Q3", actualAccumulated.getNq3().doubleValue());
                }
                if (actualAccumulated.getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    aa.set("Q4", actualAccumulated.getNq4().doubleValue());
                }
                break;
        }

        ChartSeries ab = new ChartSeries();
        ab.setLabel("同期累计");
        switch (getIndicator().getFormkind()) {
            case "M":
                if (benchmarkAccumulated.getN01().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M01", benchmarkAccumulated.getN01().doubleValue());
                }
                if (benchmarkAccumulated.getN02().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M02", benchmarkAccumulated.getN02().doubleValue());
                }
                if (benchmarkAccumulated.getN03().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M03", benchmarkAccumulated.getN03().doubleValue());
                }
                if (benchmarkAccumulated.getN04().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M04", benchmarkAccumulated.getN04().doubleValue());
                }
                if (benchmarkAccumulated.getN05().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M05", benchmarkAccumulated.getN05().doubleValue());
                }
                if (benchmarkAccumulated.getN06().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M06", benchmarkAccumulated.getN06().doubleValue());
                }
                if (benchmarkAccumulated.getN07().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M07", benchmarkAccumulated.getN07().doubleValue());
                }
                if (benchmarkAccumulated.getN08().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M08", benchmarkAccumulated.getN08().doubleValue());
                }
                if (benchmarkAccumulated.getN09().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M09", benchmarkAccumulated.getN09().doubleValue());
                }
                if (benchmarkAccumulated.getN10().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M10", benchmarkAccumulated.getN10().doubleValue());
                }
                if (benchmarkAccumulated.getN11().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M11", benchmarkAccumulated.getN11().doubleValue());
                }
                if (benchmarkAccumulated.getN12().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("M12", benchmarkAccumulated.getN12().doubleValue());
                }
                break;
            case "Q":
                if (benchmarkAccumulated.getNq1().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("Q1", benchmarkAccumulated.getNq1().doubleValue());
                }
                if (benchmarkAccumulated.getNq2().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("Q2", benchmarkAccumulated.getNq2().doubleValue());
                }
                if (benchmarkAccumulated.getNq3().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("Q3", benchmarkAccumulated.getNq3().doubleValue());
                }
                if (benchmarkAccumulated.getNq4().compareTo(BigDecimal.ZERO) != 0) {
                    ab.set("Q4", benchmarkAccumulated.getNq4().doubleValue());
                }
                break;
        }

        accumulatedChartModel.addSeries(at);//累计目标
        accumulatedChartModel.addSeries(ab);//累计同期
        accumulatedChartModel.addSeries(aa);//累计实际
        accumulatedChartModel.setTitle(getIndicator().getName() + "累计");
        accumulatedChartModel.setLegendPosition("e");
        accumulatedChartModel.setShowPointLabels(true);
        accumulatedChartModel.setBreakOnNull(true);

        
        //根据指标ID加载指标说明、指标分析
        analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标分析
        if (analysisList != null) {
            this.analysisCount = analysisList.size();
        }
        summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.getM());//指标说明
        if (summaryList != null) {
            this.summaryCount = summaryList.size();
        }

    }
  

    public LineChartModel initLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        getChartModel().setSeriesColors("33FF66,FF6633,0000EE");//自定义颜色
        getChartModel().getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = getChartModel().getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");
        return getChartModel();
    }

    public LineChartModel initAccumulatedLineChartModel(String xTitle, String yTitle) {
        Axis yAxis;
        accumulatedChartModel.setSeriesColors("33FF66,FF6633,0000EE");//自定义颜色
        accumulatedChartModel.getAxes().put(AxisType.X, new CategoryAxis(xTitle));
        yAxis = accumulatedChartModel.getAxis(AxisType.Y);
        yAxis.setLabel(Objects.equals(getIndicator().getUnit(), "") ? yTitle : yTitle + "(" + getIndicator().getUnit() + ")");
        return accumulatedChartModel;
    }

    /**
     *
     * @param a
     * @param b
     * @return a/b
     */
    public BigDecimal getValueA(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) != 0) {
            return a.divide(b, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d));
        } else {
            if (a.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            } else {
                return BigDecimal.ZERO;
            }
        }
    }

    /**
     *
     * @param a
     * @param b
     * @param c
     * @return (a/b - c)/c
     */
    public BigDecimal getValueA(BigDecimal a, BigDecimal b, BigDecimal c) {
        BigDecimal d = getValueA(a, b);
        if (c.compareTo(BigDecimal.ZERO) != 0) {
            return d.divide(c, 4, RoundingMode.HALF_UP).subtract(BigDecimal.ONE).multiply(BigDecimal.valueOf(100d));
        } else {
            if (d.compareTo(BigDecimal.ZERO) != 0) {
                return BigDecimal.valueOf(100d);
            } else {
                return BigDecimal.ZERO;
            }
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

    public String doubleformat(BigDecimal value) {
        if (value == null) {
            return "";
        } else {
            return percentFormat.format(value);
        }
    }

    public String doubleformat(BigDecimal value, int i) {
        if (value == null) {
            return "";
        } else if (i <= m) {
            return percentFormat.format(value);
        } else if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return percentFormat.format(value);
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

    public boolean visible(int m) {
        if (m == this.m) {
            return true;
        } else {
            if (monthchecked == false) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    /**
     * @param indivatorDetail
     * @return 指标明细中的月平均值
     */
    public BigDecimal calculateYearMoveAVG(IndicatorDetail indivatorDetail) {
        Field f;
        Double a;
        BigDecimal result;
        result = new BigDecimal("0");
        int count = 0;
        try {
            for (int mon = 1; mon <= 12; mon++) {
                String month = indicatorBean.getIndicatorColumn("N", mon);
                f = indivatorDetail.getClass().getDeclaredField(month);
                f.setAccessible(true);
                a = Double.valueOf(f.get(indivatorDetail).toString());
                if (a != null && a != 0.0) {
                    count++;
                    result = result.add(BigDecimal.valueOf(a));
                }

            }
           BigDecimal bCount= BigDecimal.valueOf(count);
            return result.divide(bCount,2,BigDecimal.ROUND_HALF_UP);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
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
     * @return the benchmarkAccumulated
     */
    public IndicatorDetail getBenchmarkAccumulated() {
        return benchmarkAccumulated;
    }

    /**
     * @return the targetAccumulated
     */
    public IndicatorDetail getTargetAccumulated() {
        return targetAccumulated;
    }

    /**
     * @return the AP
     */
    public IndicatorDetail getAP() {
        return AP;
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
     * @return the chartModel
     */
    public LineChartModel getChartModel() {
        return chartModel;
    }
    

    /**
     * @return the accumulativeChartModel
     */
    public LineChartModel getAccumulatedChartModel() {
        return accumulatedChartModel;
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

    public boolean isMonthchecked() {
        return monthchecked;
    }

    public void setMonthchecked(boolean monthchecked) {
        this.monthchecked = monthchecked;
    }

}
