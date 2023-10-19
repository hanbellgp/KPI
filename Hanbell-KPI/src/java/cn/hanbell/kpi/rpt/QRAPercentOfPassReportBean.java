/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.IndicatorSet;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C1749
 */
@ManagedBean(name = "qraPercentOfPassReportBean")
@ViewScoped
public class QRAPercentOfPassReportBean extends BscChartManagedBean {

    protected IndicatorDetail jlScore;//进料分数
    protected IndicatorDetail trScore;//试车分数
    protected IndicatorDetail bsScore;//三次元分数
    protected IndicatorDetail olScore;//上线分数
    protected IndicatorDetail tzScore;//涂装分数
    protected IndicatorDetail chScore;//出货分数
    protected BigDecimal mbTotal;//目标分数的合计 计算2、4、6、8、10、12月份合计的数据
    protected IndicatorDetail sumAG;//合计
    protected IndicatorDetail sumPerformance;//实际合格率（合计）

    protected IndicatorDetail other1Score;//关联指标other1分数
    protected IndicatorDetail other2Score;//关联指标other2分数
    protected IndicatorDetail other3Score;//关联指标other3分数

    protected IndicatorDetail other1Actual;//关联指标other1实际合格率
    protected IndicatorDetail other2Actual;//关联指标other2实际合格率
    protected IndicatorDetail other3Actual;//关联指标other3实际合格率

    protected IndicatorDetail other1Target;//关联指标other1目标合格率
    protected IndicatorDetail other2Target;//关联指标other2目标合格率
    protected IndicatorDetail other3Target;//关联指标other3目标合格率

    protected List<Indicator> indicatorList;
    protected List<IndicatorSet> indicatorSetList;
    protected List<IndicatorDetail> indicatorDetailList;

    protected final DecimalFormat floatFormat;

    protected boolean hasOther;

    public QRAPercentOfPassReportBean() {
        this.floatFormat = new DecimalFormat("##.00");
    }

    @Override
    public void init() {
        super.init();
        indicatorList = indicatorBean.findByCategoryAndYear("品质水准", y);
        if (indicatorList.isEmpty()) {
            showInfoMsg("Info", "没有数据可展示");
        }
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

    @Override
    public String doubleformat(BigDecimal value, int i) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "";
        } else {
            return floatFormat.format(value);
        }
    }

    public String percentFormat(String type, BigDecimal value, int i) {
        switch (type) {
            case "实际分数":
                return format(value, i);
            default:
                if (value == null) {
                    return "";
                } else if (i <= m) {
                    return format(value);
                } else if (value.compareTo(BigDecimal.ZERO) == 0) {
                    return "";
                } else {
                    return format(value);
                }
        }
    }

    public String floatFormat(String type, BigDecimal value, int i) {
        switch (type) {
            case "实际合格率":
                return format(value, i);
            default:
                if (value == null) {
                    return "0.00";
                } else if (i <= m) {
                    return floatFormat.format(value);
                } else if (value.compareTo(BigDecimal.ZERO) == 0) {
                    return "0.00";
                } else {
                    return floatFormat.format(value);
                }
        }
    }

    public IndicatorDetail getJlScore() {
        return jlScore;
    }

    public void setJlScore(IndicatorDetail jlScore) {
        this.jlScore = jlScore;
    }

    public IndicatorDetail getTrScore() {
        return trScore;
    }

    public void setTrScore(IndicatorDetail trScore) {
        this.trScore = trScore;
    }

    public IndicatorDetail getBsScore() {
        return bsScore;
    }

    public void setBsScore(IndicatorDetail bsScore) {
        this.bsScore = bsScore;
    }

    public IndicatorDetail getOlScore() {
        return olScore;
    }

    public void setOlScore(IndicatorDetail olScore) {
        this.olScore = olScore;
    }

    public IndicatorDetail getTzScore() {
        return tzScore;
    }

    public void setTzScore(IndicatorDetail tzScore) {
        this.tzScore = tzScore;
    }

    public IndicatorDetail getChScore() {
        return chScore;
    }

    public void setChScore(IndicatorDetail chScore) {
        this.chScore = chScore;
    }

    public IndicatorDetail getSumAG() {
        return sumAG;
    }

    public void setSumAG(IndicatorDetail sumAG) {
        this.sumAG = sumAG;
    }

    public IndicatorDetail getOther1Score() {
        return other1Score;
    }

    public void setOther1Score(IndicatorDetail other1Score) {
        this.other1Score = other1Score;
    }

    public IndicatorDetail getOther2Score() {
        return other2Score;
    }

    public void setOther2Score(IndicatorDetail other2Score) {
        this.other2Score = other2Score;
    }

    public IndicatorDetail getOther3Score() {
        return other3Score;
    }

    public void setOther3Score(IndicatorDetail other3Score) {
        this.other3Score = other3Score;
    }

    public List<Indicator> getIndicatorList() {
        return indicatorList;
    }

    public void setIndicatorList(List<Indicator> indicatorList) {
        this.indicatorList = indicatorList;
    }

    public List<IndicatorSet> getIndicatorSetList() {
        return indicatorSetList;
    }

    public void setIndicatorSetList(List<IndicatorSet> indicatorSetList) {
        this.indicatorSetList = indicatorSetList;
    }

    public List<IndicatorDetail> getIndicatorDetailList() {
        return indicatorDetailList;
    }

    public void setIndicatorDetailList(List<IndicatorDetail> indicatorDetailList) {
        this.indicatorDetailList = indicatorDetailList;
    }

    public IndicatorDetail getOther1Actual() {
        return other1Actual;
    }

    public void setOther1Actual(IndicatorDetail other1Actual) {
        this.other1Actual = other1Actual;
    }

    public IndicatorDetail getOther2Actual() {
        return other2Actual;
    }

    public void setOther2Actual(IndicatorDetail other2Actual) {
        this.other2Actual = other2Actual;
    }

    public IndicatorDetail getOther3Actual() {
        return other3Actual;
    }

    public void setOther3Actual(IndicatorDetail other3Actual) {
        this.other3Actual = other3Actual;
    }

    public IndicatorDetail getOther1Target() {
        return other1Target;
    }

    public void setOther1Target(IndicatorDetail other1Target) {
        this.other1Target = other1Target;
    }

    public IndicatorDetail getOther2Target() {
        return other2Target;
    }

    public void setOther2Target(IndicatorDetail other2Target) {
        this.other2Target = other2Target;
    }

    public IndicatorDetail getOther3Target() {
        return other3Target;
    }

    public void setOther3Target(IndicatorDetail other3Target) {
        this.other3Target = other3Target;
    }

    public boolean isHasOther() {
        return hasOther;
    }

    public void setHasOther(boolean hasOther) {
        this.hasOther = hasOther;
    }

    public BigDecimal getMbTotal() {
        return mbTotal;
    }

    public void setMbTotal(BigDecimal mbTotal) {
        this.mbTotal = mbTotal;
    }

    public IndicatorDetail getSumPerformance() {
        return sumPerformance;
    }

    public void setSumPerformance(IndicatorDetail sumPerformance) {
        this.sumPerformance = sumPerformance;
    }

}
