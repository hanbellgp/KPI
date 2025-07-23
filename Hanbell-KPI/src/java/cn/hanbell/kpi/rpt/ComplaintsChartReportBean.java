/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.math.BigDecimal;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "complaintsChartReportBean")
@ViewScoped
public class ComplaintsChartReportBean extends BscChartManagedBean {

    public ComplaintsChartReportBean() {
    }

    @Override
    public void init() {
        monthchecked = false;
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
        }

        ChartSeries b = new ChartSeries();
        b.setLabel("基准");
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
        
        
        
         
        otherChartModel = new LineChartModel();
        ChartSeries lyactualAH = new ChartSeries();
        lyactualAH.setLabel("去年同期实际值（机体）");
        switch (getIndicator().getFormkind()) {
            case "M":
                lyactualAH.set("M01", getIndicator().getBenchmarkIndicator().getN01().doubleValue());
                lyactualAH.set("M02", getIndicator().getBenchmarkIndicator().getN02().doubleValue());
                lyactualAH.set("M03", getIndicator().getBenchmarkIndicator().getN03().doubleValue());
                lyactualAH.set("M04", getIndicator().getBenchmarkIndicator().getN04().doubleValue());
                lyactualAH.set("M05", getIndicator().getBenchmarkIndicator().getN05().doubleValue());
                lyactualAH.set("M06", getIndicator().getBenchmarkIndicator().getN06().doubleValue());
                lyactualAH.set("M07", getIndicator().getBenchmarkIndicator().getN07().doubleValue());
                lyactualAH.set("M08", getIndicator().getBenchmarkIndicator().getN08().doubleValue());
                lyactualAH.set("M09", getIndicator().getBenchmarkIndicator().getN09().doubleValue());
                lyactualAH.set("M10", getIndicator().getBenchmarkIndicator().getN10().doubleValue());
                lyactualAH.set("M11", getIndicator().getBenchmarkIndicator().getN11().doubleValue());
                lyactualAH.set("M12", getIndicator().getBenchmarkIndicator().getN12().doubleValue());
                break;
            case "Q":
                lyactualAH.set("Q1", getIndicator().getBenchmarkIndicator().getNq1().doubleValue());
                lyactualAH.set("Q2", getIndicator().getBenchmarkIndicator().getNq2().doubleValue());
                lyactualAH.set("Q3", getIndicator().getBenchmarkIndicator().getNq3().doubleValue());
                lyactualAH.set("Q4", getIndicator().getBenchmarkIndicator().getNq4().doubleValue());
                break;
        }

        ChartSeries nowactualAH = new ChartSeries();
        nowactualAH.setLabel("当年实际值（机体）");
        switch (getIndicator().getFormkind()) {
            case "M":
                nowactualAH.set("M01", getIndicator().getActualIndicator().getN01().doubleValue());
                nowactualAH.set("M02", getIndicator().getActualIndicator().getN02().doubleValue());
                nowactualAH.set("M03", getIndicator().getActualIndicator().getN03().doubleValue());
                nowactualAH.set("M04", getIndicator().getActualIndicator().getN04().doubleValue());
                nowactualAH.set("M05", getIndicator().getActualIndicator().getN05().doubleValue());
                nowactualAH.set("M06", getIndicator().getActualIndicator().getN06().doubleValue());
                nowactualAH.set("M07", getIndicator().getActualIndicator().getN07().doubleValue());
                nowactualAH.set("M08", getIndicator().getActualIndicator().getN08().doubleValue());
                nowactualAH.set("M09", getIndicator().getActualIndicator().getN09().doubleValue());
                nowactualAH.set("M10", getIndicator().getActualIndicator().getN10().doubleValue());
                nowactualAH.set("M11", getIndicator().getActualIndicator().getN11().doubleValue());
                nowactualAH.set("M12", getIndicator().getActualIndicator().getN12().doubleValue());
                break;
            case "Q":
                nowactualAH.set("Q1", getIndicator().getActualIndicator().getNq1().doubleValue());
                nowactualAH.set("Q2", getIndicator().getActualIndicator().getNq2().doubleValue());
                nowactualAH.set("Q3", getIndicator().getActualIndicator().getNq3().doubleValue());
                nowactualAH.set("Q4", getIndicator().getActualIndicator().getNq4().doubleValue());
                break;
        }
        
        ChartSeries lyactualAA = new ChartSeries();
        lyactualAA.setLabel("去年同期实际值（机组）");
        switch (getIndicator().getFormkind()) {
            case "M":
                lyactualAA.set("M01", getIndicator().getOther1Indicator().getN01().doubleValue());
                lyactualAA.set("M02", getIndicator().getOther1Indicator().getN02().doubleValue());
                lyactualAA.set("M03", getIndicator().getOther1Indicator().getN03().doubleValue());
                lyactualAA.set("M04", getIndicator().getOther1Indicator().getN04().doubleValue());
                lyactualAA.set("M05", getIndicator().getOther1Indicator().getN05().doubleValue());
                lyactualAA.set("M06", getIndicator().getOther1Indicator().getN06().doubleValue());
                lyactualAA.set("M07", getIndicator().getOther1Indicator().getN07().doubleValue());
                lyactualAA.set("M08", getIndicator().getOther1Indicator().getN08().doubleValue());
                lyactualAA.set("M09", getIndicator().getOther1Indicator().getN09().doubleValue());
                lyactualAA.set("M10", getIndicator().getOther1Indicator().getN10().doubleValue());
                lyactualAA.set("M11", getIndicator().getOther1Indicator().getN11().doubleValue());
                lyactualAA.set("M12", getIndicator().getOther1Indicator().getN12().doubleValue());
                break;
            case "Q":
                lyactualAA.set("Q1", getIndicator().getOther1Indicator().getNq1().doubleValue());
                lyactualAA.set("Q2", getIndicator().getOther1Indicator().getNq2().doubleValue());
                lyactualAA.set("Q3", getIndicator().getOther1Indicator().getNq3().doubleValue());
                lyactualAA.set("Q4", getIndicator().getOther1Indicator().getNq4().doubleValue());
                break;
        }
        
        ChartSeries nowactualAA = new ChartSeries();
        nowactualAA.setLabel("当年实际值（机组）");
        switch (getIndicator().getFormkind()) {
            case "M":
                nowactualAA.set("M01", getIndicator().getOther2Indicator().getN01().doubleValue());
                nowactualAA.set("M02", getIndicator().getOther2Indicator().getN02().doubleValue());
                nowactualAA.set("M03", getIndicator().getOther2Indicator().getN03().doubleValue());
                nowactualAA.set("M04", getIndicator().getOther2Indicator().getN04().doubleValue());
                nowactualAA.set("M05", getIndicator().getOther2Indicator().getN05().doubleValue());
                nowactualAA.set("M06", getIndicator().getOther2Indicator().getN06().doubleValue());
                nowactualAA.set("M07", getIndicator().getOther2Indicator().getN07().doubleValue());
                nowactualAA.set("M08", getIndicator().getOther2Indicator().getN08().doubleValue());
                nowactualAA.set("M09", getIndicator().getOther2Indicator().getN09().doubleValue());
                nowactualAA.set("M10", getIndicator().getOther2Indicator().getN10().doubleValue());
                nowactualAA.set("M11", getIndicator().getOther2Indicator().getN11().doubleValue());
                nowactualAA.set("M12", getIndicator().getOther2Indicator().getN12().doubleValue());
                break;
            case "Q":
                nowactualAA.set("Q1", getIndicator().getOther2Indicator().getNq1().doubleValue());
                nowactualAA.set("Q2", getIndicator().getOther2Indicator().getNq2().doubleValue());
                nowactualAA.set("Q3", getIndicator().getOther2Indicator().getNq3().doubleValue());
                nowactualAA.set("Q4", getIndicator().getOther2Indicator().getNq4().doubleValue());
                break;
        }
       

        getotherChartModel().addSeries(lyactualAH);//他项1
        getotherChartModel().addSeries(nowactualAH);//他项2
        getotherChartModel().addSeries(lyactualAA);//他项3
        getotherChartModel().addSeries(nowactualAA);//他项4
        getotherChartModel().setTitle(getIndicator().getName());
        getotherChartModel().setLegendPosition("e");
        getotherChartModel().setShowPointLabels(true);
        getotherChartModel().setBreakOnNull(true);
        
        
          
        AllChartModel = new LineChartModel();
        ChartSeries lyactualAHAA = new ChartSeries();
        lyactualAHAA.setLabel("去年同期实际值（机体+机组）");
        switch (getIndicator().getFormkind()) {
            case "M":
                lyactualAHAA.set("M01", getIndicator().getOther3Indicator().getN01().doubleValue());
                lyactualAHAA.set("M02", getIndicator().getOther3Indicator().getN02().doubleValue());
                lyactualAHAA.set("M03", getIndicator().getOther3Indicator().getN03().doubleValue());
                lyactualAHAA.set("M04", getIndicator().getOther3Indicator().getN04().doubleValue());
                lyactualAHAA.set("M05", getIndicator().getOther3Indicator().getN05().doubleValue());
                lyactualAHAA.set("M06", getIndicator().getOther3Indicator().getN06().doubleValue());
                lyactualAHAA.set("M07", getIndicator().getOther3Indicator().getN07().doubleValue());
                lyactualAHAA.set("M08", getIndicator().getOther3Indicator().getN08().doubleValue());
                lyactualAHAA.set("M09", getIndicator().getOther3Indicator().getN09().doubleValue());
                lyactualAHAA.set("M10", getIndicator().getOther3Indicator().getN10().doubleValue());
                lyactualAHAA.set("M11", getIndicator().getOther3Indicator().getN11().doubleValue());
                lyactualAHAA.set("M12", getIndicator().getOther3Indicator().getN12().doubleValue());
                break;
            case "Q":
                lyactualAHAA.set("Q1", getIndicator().getOther3Indicator().getNq1().doubleValue());
                lyactualAHAA.set("Q2", getIndicator().getOther3Indicator().getNq2().doubleValue());
                lyactualAHAA.set("Q3", getIndicator().getOther3Indicator().getNq3().doubleValue());
                lyactualAHAA.set("Q4", getIndicator().getOther3Indicator().getNq4().doubleValue());
                break;
        }

        ChartSeries nowactualAHAA = new ChartSeries();
        nowactualAHAA.setLabel("当年实际值（机体+机组）");
        switch (getIndicator().getFormkind()) {
            case "M":
                nowactualAHAA.set("M01", getIndicator().getOther4Indicator().getN01().doubleValue());
                nowactualAHAA.set("M02", getIndicator().getOther4Indicator().getN02().doubleValue());
                nowactualAHAA.set("M03", getIndicator().getOther4Indicator().getN03().doubleValue());
                nowactualAHAA.set("M04", getIndicator().getOther4Indicator().getN04().doubleValue());
                nowactualAHAA.set("M05", getIndicator().getOther4Indicator().getN05().doubleValue());
                nowactualAHAA.set("M06", getIndicator().getOther4Indicator().getN06().doubleValue());
                nowactualAHAA.set("M07", getIndicator().getOther4Indicator().getN07().doubleValue());
                nowactualAHAA.set("M08", getIndicator().getOther4Indicator().getN08().doubleValue());
                nowactualAHAA.set("M09", getIndicator().getOther4Indicator().getN09().doubleValue());
                nowactualAHAA.set("M10", getIndicator().getOther4Indicator().getN10().doubleValue());
                nowactualAHAA.set("M11", getIndicator().getOther4Indicator().getN11().doubleValue());
                nowactualAHAA.set("M12", getIndicator().getOther4Indicator().getN12().doubleValue());
                break;
            case "Q":
                nowactualAHAA.set("Q1", getIndicator().getOther4Indicator().getNq1().doubleValue());
                nowactualAHAA.set("Q2", getIndicator().getOther4Indicator().getNq2().doubleValue());
                nowactualAHAA.set("Q3", getIndicator().getOther4Indicator().getNq3().doubleValue());
                nowactualAHAA.set("Q4", getIndicator().getOther4Indicator().getNq4().doubleValue());
                break;
        }
        
        getallChartModel().addSeries(lyactualAHAA);//他项1
        getallChartModel().addSeries(nowactualAHAA);//他项2
        getallChartModel().setTitle(getIndicator().getName());
        getallChartModel().setLegendPosition("e");
        getallChartModel().setShowPointLabels(true);
        getallChartModel().setBreakOnNull(true);
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
}
