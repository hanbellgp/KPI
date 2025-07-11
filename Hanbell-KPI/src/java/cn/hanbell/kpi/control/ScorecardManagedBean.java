/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.efgp.WorkFlowBean;
import cn.hanbell.kpi.efgp.model.HKGL076Model;
import cn.hanbell.kpi.efgp.model.HKGL076Q1DetailModel;
import cn.hanbell.kpi.efgp.model.HKGL076Q2DetailModel;
import cn.hanbell.kpi.efgp.model.HKGL076Q3DetailModel;
import cn.hanbell.kpi.efgp.model.HKGL076Q4DetailModel;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.PolicyDetailBean;
import cn.hanbell.kpi.ejb.ScorecardAuditorBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardContentBean;
import cn.hanbell.kpi.ejb.ScorecardDetailBean;
import cn.hanbell.kpi.ejb.ScorecardGrantBean;
import cn.hanbell.kpi.ejb.tms.ProjectBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.PolicyDetail;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardAuditor;
import cn.hanbell.kpi.entity.ScorecardContent;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.entity.ScorecardGrant;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.ibm.icu.math.MathContext;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.jexl3.JexlException;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scorecardManagedBean")
@ViewScoped
public class ScorecardManagedBean extends SuperSingleBean<ScorecardContent> {

    @EJB
    private IndicatorBean indicatorBean;
    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private ScorecardContentBean scorecardContentBean;
    @EJB
    private ScorecardGrantBean scorecardGrantBean;
    @EJB
    private ScorecardAuditorBean scorecardAuditorBean;
    @EJB
    private ScorecardDetailBean scorecardDetailBean;
    @EJB
    SystemUserBean systemUserBean;
    @EJB
    WorkFlowBean workFlowBean;
    @EJB
    private ProjectBean projectBean;
    @EJB
    private PolicyDetailBean policyDetailBean;

    protected ScorecardGrant scorecardGrant;

    protected Calendar c;
    private Scorecard scorecard;
    private boolean freezed;
    protected LinkedHashMap<String, String> auditorMap;
    protected List<ScorecardAuditor> scorecardAuditorList;

    @EJB
    SuperEJBForERP SuperEJBForERP;

    public ScorecardManagedBean() {
        super(ScorecardContent.class);
        c = Calendar.getInstance();
    }

    public void calcScore() {
        if (scorecard != null) {
            if (scorecard.getFreezeDate() != null && scorecard.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            String col;
            BigDecimal value;
            col = scorecardBean.getColumn("sq", userManagedBean.getQ());
            List<ScorecardContent> data = scorecardContentBean.findByPId(scorecard.getId());
            try {
                value = scorecardBean.getContentScores(data, col);
                switch (userManagedBean.getQ()) {
                    case 1:
                        scorecard.setSq1(value);
                        break;
                    case 2:
                        scorecard.setSq2(value);
                        value = scorecardBean.getContentScores(data, "sh1");
                        scorecard.setSh1(value);
                        break;
                    case 3:
                        scorecard.setSq3(value);
                        break;
                    case 4:
                        scorecard.setSq4(value);
                        value = scorecardBean.getContentScores(data, "sh2");
                        scorecard.setSh2(value);
                        value = scorecardBean.getContentScores(data, "sfy");
                        scorecard.setSfy(value);
                        break;
                }
                scorecardBean.update(scorecard);
                showInfoMsg("Info", "更新成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    public void updateActual() {
        String col = scorecardBean.getColumn("q", userManagedBean.getQ());
        //优先处理方针数据
        if (this.currentEntity.getPolicySeq() != null && !"".equals(this.currentEntity.getPolicySeq())) {
            PolicyDetail policyDetail = policyDetailBean.findById(Integer.valueOf(this.currentEntity.getPolicySeq()));
            if (policyDetail != null) {
                if ("A".equals(policyDetail.getCalculationtype())) {
                    //文字类型 
                    switch (col) {
                        case "q1":
                            this.currentEntity.setAq1(policyDetail.getAq1());
                            this.currentEntity.setPq1(policyDetail.getPq1());
                            this.currentEntity.getDeptScore().setSq1(policyDetail.getPq1());
                            this.currentEntity.getGeneralScore().setSq1(policyDetail.getPq1());
                            break;
                        case "q2":
                            this.currentEntity.setAq2(policyDetail.getAq2());
                            this.currentEntity.setPq2(policyDetail.getPq2());
                            this.currentEntity.getDeptScore().setSq2(policyDetail.getPq2());
                            this.currentEntity.getGeneralScore().setSq2(policyDetail.getPq2());

                            this.currentEntity.setAh1(policyDetail.getAhy());
                            this.currentEntity.setPh1(policyDetail.getPhy());
                            this.currentEntity.getDeptScore().setSh1(policyDetail.getPhy());
                            this.currentEntity.getGeneralScore().setSh1(policyDetail.getPhy());
                            break;
                        case "q3":
                            this.currentEntity.setAq3(policyDetail.getAq3());
                            this.currentEntity.setPq3(policyDetail.getPq3());
                            this.currentEntity.getDeptScore().setSq3(policyDetail.getPq3());
                            this.currentEntity.getGeneralScore().setSq3(policyDetail.getPq3());
                            break;
                        case "q4":
                            this.currentEntity.setAq4(policyDetail.getAq4());
                            this.currentEntity.setPq4(policyDetail.getPq4());
                            this.currentEntity.getDeptScore().setSq4(policyDetail.getPq4());
                            this.currentEntity.getGeneralScore().setSq4(policyDetail.getPq4());

                            this.currentEntity.setAfy(policyDetail.getAfy());
                            this.currentEntity.setPfy(policyDetail.getPfy());
                            this.currentEntity.getDeptScore().setSfy(policyDetail.getPfy());
                            this.currentEntity.getGeneralScore().setSfy(policyDetail.getPfy());
                            break;
                    }
                } else if ("B".equals(policyDetail.getCalculationtype())) {
                    //数字类型
                    switch (col) {
                        case "q1":
                            this.currentEntity.setAq1(policyDetail.getAq1());
                            break;
                        case "q2":
                            this.currentEntity.setAq2(policyDetail.getAq2());
                            this.currentEntity.setAh1(policyDetail.getAhy());
                            break;
                        case "q3":
                            this.currentEntity.setAq3(policyDetail.getAq3());
                            break;
                        case "q4":
                            this.currentEntity.setAq4(policyDetail.getAq4());
                            this.currentEntity.setAfy(policyDetail.getAfy());
                            break;
                    }
                    calcItemScore();
                }
            }
            return;
        }
        //PLM明细
        if (currentEntity.getProjectSeq() != null && !currentEntity.getType().equals("N")) {
            updateScoreByPLMProject();
            return;
        }
        //KPI明细        
        if (currentEntity.getIndicator() != null && currentEntity.getIndicator() != null) {
            Indicator i = indicatorBean.findByFormidYearAndDeptno(currentEntity.getIndicator(),
                    currentEntity.getParent().getSeq(), currentEntity.getDeptno());
            if (i != null) {
                switch (userManagedBean.getQ()) {
                    case 1:
                        currentEntity.setAq1(i.getActualIndicator().getNq1().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        break;
                    case 2:
                        currentEntity.setAq2(i.getActualIndicator().getNq2().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        currentEntity.setAh1(i.getActualIndicator().getNh1().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        break;
                    case 3:
                        currentEntity.setAq3(i.getActualIndicator().getNq3().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        break;
                    case 4:
                        currentEntity.setAq4(i.getActualIndicator().getNq4().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        currentEntity.setAh2(i.getActualIndicator().getNh2().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        currentEntity.setAfy(i.getActualIndicator().getNfy().divide(currentEntity.getRate()).setScale(2,MathContext.ROUND_HALF_UP).toString());
                        break;
                }
            }
            calcItemScore();
            return;
        }
        //数字格式明细
        calcItemScore();
        return;
    }

    public void calcItemScore() {
        if (currentEntity != null) {
            try {
                if (currentEntity.getFreezeDate() != null
                        && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
                    showErrorMsg("Error", "资料已冻结,不可更新");
                    return;
                }
                String col = scorecardBean.getColumn("q", userManagedBean.getQ());
                String target, actual;
                BigDecimal value;
                //PLM为文本格式，计算达成做单独处理
                if (this.currentEntity.getProjectSeq() != null && !"".equals(this.currentEntity.getProjectSeq())) {
                    switch (col) {
                        case "q1":
                            target = currentEntity.getTq1();
                            actual = currentEntity.getAq1();
                            value = calculateScore(target, actual);
                            String    str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            String    str2 = actual.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            double a = Double.valueOf(str1);
                            double b = Double.valueOf(str2);
                            if (a == 0.0) { //如果目标为0。则
                                if (b == 0) { //
                                    currentEntity.setPq1(new BigDecimal(100));
                                } else {
                                    currentEntity.setPq1(currentEntity.getMaxNum());
                                }
                            } else {
                                currentEntity.setPq1(value);
                            }

                            currentEntity.getDeptScore().setSq1(value);
                            currentEntity.getGeneralScore().setSq1(value);
                            break;
                        case "q2":
                            target = currentEntity.getTq2();
                            actual = currentEntity.getAq2();
                            value = calculateScore(target, actual);
                            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            str2 = actual.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            a = Double.valueOf(str1);
                            b = Double.valueOf(str2);
                            if (a == 0.0) { //如果目标为0。则
                                if (b == 0) { //
                                    currentEntity.setPq2(new BigDecimal(100));
                                } else {
                                    currentEntity.setPq2(currentEntity.getMaxNum());
                                }
                            } else {
                                currentEntity.setPq2(value);
                            }
                            currentEntity.getDeptScore().setSq2(value);
                            currentEntity.getGeneralScore().setSq2(value);

                            target = currentEntity.getTh1();
                            actual = currentEntity.getAh1();
                            value = calculateScore(target, actual);
                            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            str2 = actual.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            a = Double.valueOf(str1);
                            b = Double.valueOf(str2);
                            if (a == 0.0) { //如果目标为0。则
                                if (b == 0) { //
                                    currentEntity.setPh1(new BigDecimal(100));
                                } else {
                                    currentEntity.setPh1(currentEntity.getMaxNum());
                                }
                            } else {
                                currentEntity.setPh1(value);
                            }
                            currentEntity.getDeptScore().setSh1(value);
                            currentEntity.getGeneralScore().setSh1(value);
                            break;
                        case "q3":
                            target = currentEntity.getTq3();
                            actual = currentEntity.getAq3();
                            value = calculateScore(target, actual);
                            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            str2 = actual.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            a = Double.valueOf(str1);
                            b = Double.valueOf(str2);
                            if (a == 0.0) { //如果目标为0。则
                                if (b == 0) { //
                                    currentEntity.setPq3(new BigDecimal(100));
                                } else {
                                    currentEntity.setPq3(currentEntity.getMaxNum());
                                }
                            } else {
                                currentEntity.setPq3(value);
                            }
                            currentEntity.getDeptScore().setSq3(value);
                            currentEntity.getGeneralScore().setSq3(value);
                            break;
                        case "q4":
                            target = currentEntity.getTq4();
                            actual = currentEntity.getAq4();
                            value = calculateScore(target, actual);
                            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            str2 = actual.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            a = Double.valueOf(str1);
                            b = Double.valueOf(str2);
                            if (a == 0.0) { //如果目标为0。则
                                if (b == 0) { //
                                    currentEntity.setPq4(new BigDecimal(100));
                                } else {
                                    currentEntity.setPq4(currentEntity.getMaxNum());
                                }
                            } else {
                                currentEntity.setPq4(value);
                            }
                            currentEntity.getDeptScore().setSq4(value);
                            currentEntity.getGeneralScore().setSq4(value);

                            target = currentEntity.getTfy();
                            actual = currentEntity.getAfy();
                            value = calculateScore(target, actual);
                            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            str2 = actual.substring(target.indexOf("#") + 1, target.indexOf("%"));
                            a = Double.valueOf(str1);
                            b = Double.valueOf(str2);
                            if (a == 0.0) { //如果目标为0。则
                                if (b == 0) { //
                                    currentEntity.setPfy(new BigDecimal(100));
                                } else {
                                    currentEntity.setPfy(currentEntity.getMaxNum());
                                }
                            } else {
                                currentEntity.setPfy(value);
                            }
                            currentEntity.getDeptScore().setSfy(value);
                            currentEntity.getGeneralScore().setSfy(value);
                            break;
                    }
                    return;
                }
                if (!currentEntity.getType().equals("N")) {
                    showWarnMsg("Warn", "数值型才能更新");
                    return;
                }
                if (currentEntity.getScoreJexl() != null && !"".equals(currentEntity.getScoreJexl())) {
                    // 计算达成
                    scorecardBean.setPerf(currentEntity, col);
                    if (currentEntity.getScoreJexl().contains("object.c${n}")) {
                        //通过系数计算得分
                        scorecardBean.setContentScoreByCoefficient(currentEntity, col);
                    } else {
                        // 计算得分
                        scorecardBean.setContentScore(currentEntity, col);
                    }
                    // 上半年
                    if (userManagedBean.getQ() == 2) {
                        col = scorecardBean.getColumn("h", 1);
                        scorecardBean.setPerf(currentEntity, col);
                        if (currentEntity.getScoreJexl().contains("object.c${n}")) {
                            //通过系数计算得分
                            scorecardBean.setContentScoreByCoefficient(currentEntity, col);
                        } else {
                            // 计算得分
                            scorecardBean.setContentScore(currentEntity, col);
                        }
                    } else if (userManagedBean.getQ() == 4) {
                        // 全年
                        scorecardBean.setPerf(currentEntity, "fy");
                        if (currentEntity.getScoreJexl().contains("object.c${n}")) {
                            //通过系数计算得分
                            scorecardBean.setContentScoreByCoefficient(currentEntity, "fy");
                        } else {
                            // 计算得分
                            scorecardBean.setContentScore(currentEntity,  "fy");
                        }
                    }
                }
                switch (userManagedBean.getQ()) {
                    case 1:
                        if (currentEntity.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                        }
                        break;
                    case 2:
                        if (currentEntity.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                        }
                        if (currentEntity.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                        }
                        break;
                    case 3:
                        if (currentEntity.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                        }
                        break;
                    case 4:
                        if (currentEntity.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                        }
                        if (currentEntity.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                        }
                        if (currentEntity.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                            currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                        } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                        }
                        break;
                }
                showInfoMsg("Info", "更新部门分数成功");
            } catch (JexlException ex) {
                ex.printStackTrace();
                showErrorMsg("Error", ex.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    // 关联PLM的更新
    public void updateScoreByPLMProject() throws StringIndexOutOfBoundsException, NumberFormatException {
        try {
            String target, actual, projectSeq;
            BigDecimal value;
            String col = scorecardBean.getColumn("q", userManagedBean.getQ());
            // 找到PLM的数据
            projectSeq = projectBean.findByProjectSeq(currentEntity.getProjectSeq());
            if (projectSeq == null || "".equals(projectSeq)) {
                showErrorMsg("Error", "请确认PLM是否有进度");
                return;
            }
            // 选择季度更新
            switch (col) {
                case "q1":
                    currentEntity.setAq1("#" + projectSeq + "%#" + ";" + currentEntity.getAq1());
                    target = currentEntity.getTq1();
                    actual = currentEntity.getAq1();
                    value = calculateScore(target, actual);
                    currentEntity.setPq1(value);
                    currentEntity.getDeptScore().setSq1(value);
                    currentEntity.getGeneralScore().setSq1(value);
                    break;
                case "q2":
                    currentEntity.setAq2("#" + projectSeq + "%#" + ";" + currentEntity.getAq2());
                    //Q2
                    target = currentEntity.getTq2();
                    actual = currentEntity.getAq2();
                    value = calculateScore(target, actual);
                    currentEntity.setPq2(value);
                    currentEntity.getDeptScore().setSq2(value);
                    currentEntity.getGeneralScore().setSq2(value);
                    //上半年
                    currentEntity.setAh1("#" + projectSeq + "%#" + ";" + currentEntity.getAh1());
                    target = currentEntity.getTh1();
                    actual = currentEntity.getAh1();
                    value = calculateScore(target, actual);
                    currentEntity.setPh1(value);
                    currentEntity.getDeptScore().setSh1(value);
                    currentEntity.getGeneralScore().setSh1(value);
                    break;
                case "q3":
                    currentEntity.setAq3("#" + projectSeq + "%#" + ";" + currentEntity.getAq3());
                    target = currentEntity.getTq3();
                    actual = currentEntity.getAq3();
                    value = calculateScore(target, actual);
                    currentEntity.setPq3(value);
                    currentEntity.getDeptScore().setSq3(value);
                    currentEntity.getGeneralScore().setSq3(value);
                    break;
                case "q4":
                    //Q4
                    currentEntity.setAq4("#" + projectSeq + "%#" + ";" + currentEntity.getAq4());
                    target = currentEntity.getTq4();
                    actual = currentEntity.getAq4();
                    value = calculateScore(target, actual);
                    currentEntity.setPq4(value);
                    currentEntity.getDeptScore().setSq4(value);
                    currentEntity.getGeneralScore().setSq4(value);
                    //全年
                    currentEntity.setAfy("#" + projectSeq + "%#" + ";" + currentEntity.getAfy());
                    target = currentEntity.getTfy();
                    actual = currentEntity.getAfy();
                    value = calculateScore(target, actual);
                    currentEntity.setPfy(value);
                    currentEntity.getDeptScore().setSfy(value);
                    currentEntity.getGeneralScore().setSfy(value);
                    break;
            }
            scorecardContentBean.update(currentEntity);
            showInfoMsg("Info", "更新成功！");
        } catch (StringIndexOutOfBoundsException | NumberFormatException ex) {
            throw new NumberFormatException("无法解析时间，请检查！");
        }
    }

    /**
     * @desc 截取字符的数字计算得分、达成率
     * @param target
     * @param acutal
     * @return value
     */
    public BigDecimal calculateScore(String target, String acutal) throws StringIndexOutOfBoundsException {
        BigDecimal value = BigDecimal.ZERO;
        String str1, str2;
        // 先判断有值
        if ((!"".equals(target) || target != null) && (!"".equals(acutal) || acutal != null)) {
            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
            str2 = acutal.substring(acutal.indexOf("#") + 1, acutal.indexOf("%"));
            //判断截取出来的数据是否为数字
            if (str1.matches("[0-9]*") && str2.matches("[0-9]*")) {
                Double t = Double.valueOf(str1);
                Double a = Double.valueOf(str2);
                // 分母不为零
                if (t > 0.00001) {
                    // 达成率、得分
                    value = BigDecimal.valueOf(a / t * 100);
                }
            } else {
                showErrorMsg("Error", "基准目标值格式不正确！！");
                return BigDecimal.ZERO;
            }
        }
        BigDecimal a = value.setScale(2, BigDecimal.ROUND_HALF_UP);
        if (a.compareTo(new BigDecimal(120)) >= 0) {
            return currentEntity.getMaxNum();
        } else if (a.compareTo(new BigDecimal(60)) <= 0) {
            return currentEntity.getMinNum();
        }
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        if (currentEntity != null) {
            if (currentEntity.getFreezeDate() != null
                    && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return false;
            }
            if (currentEntity.getType().equals("N")) {
                if (currentEntity.getAq1() != null && !currentEntity.getAq1().contains(".")) {
                    currentEntity.setAq1(currentEntity.getAq1().concat(".00"));
                }
                if (currentEntity.getAq2() != null && !currentEntity.getAq2().contains(".")) {
                    currentEntity.setAq2(currentEntity.getAq2().concat(".00"));
                }
                if (currentEntity.getAh1() != null && !currentEntity.getAh1().contains(".")) {
                    currentEntity.setAh1(currentEntity.getAh1().concat(".00"));
                }
                if (currentEntity.getAq3() != null && !currentEntity.getAq3().contains(".")) {
                    currentEntity.setAq3(currentEntity.getAq3().concat(".00"));
                }
                if (currentEntity.getAq4() != null && !currentEntity.getAq4().contains(".")) {
                    currentEntity.setAq4(currentEntity.getAq4().concat(".00"));
                }
                if (currentEntity.getAh2() != null && !currentEntity.getAh2().contains(".")) {
                    currentEntity.setAh2(currentEntity.getAh2().concat(".00"));
                }
                if (currentEntity.getAfy() != null && !currentEntity.getAfy().contains(".")) {
                    currentEntity.setAfy(currentEntity.getAfy().concat(".00"));
                }
            }
            switch (userManagedBean.getQ()) {
                case 1:
                    currentEntity.setSq1(currentEntity.getDeptScore().getSq1());
                    if (currentEntity.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq1(currentEntity.getGeneralScore().getSq1());
                    }
                    break;
                case 2:
                    currentEntity.setSq2(currentEntity.getDeptScore().getSq2());
                    if (currentEntity.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq2(currentEntity.getGeneralScore().getSq2());
                    }
                    currentEntity.setSh1(currentEntity.getDeptScore().getSh1());
                    if (currentEntity.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSh1(currentEntity.getGeneralScore().getSh1());
                    }
                    break;
                case 3:
                    currentEntity.setSq3(currentEntity.getDeptScore().getSq3());
                    if (currentEntity.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq3(currentEntity.getGeneralScore().getSq3());
                    }
                    break;
                case 4:
                    currentEntity.setSq4(currentEntity.getDeptScore().getSq4());
                    if (currentEntity.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSq4(currentEntity.getGeneralScore().getSq4());
                    }
                    currentEntity.setSh2(currentEntity.getDeptScore().getSh2());
                    if (currentEntity.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSh2(currentEntity.getGeneralScore().getSh2());
                    }
                    currentEntity.setSfy(currentEntity.getDeptScore().getSfy());
                    if (currentEntity.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                        currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                    } else if (currentEntity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                        currentEntity.setSfy(currentEntity.getGeneralScore().getSfy());
                    }
                    break;
            }

        }
        return super.doBeforeUpdate();
    }

    @Override
    public void init() {
        c.setTime(userManagedBean.getBaseDate());
        superEJB = scorecardContentBean;
        model = new ScorecardContentModel(scorecardContentBean, this.userManagedBean);
        model.getSortFields().put("seq", "ASC");
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("parent.seq", c.get(Calendar.YEAR));
        if (this.getScorecard() != null && this.getScorecard().getId() != null) {
            model.getFilterFields().put("pid", getScorecard().getId());
        }
        scorecardAuditorList = new ArrayList<>();
        scorecardAuditorList = scorecardAuditorBean.findByPidAndQuarter(scorecard.getId(), userManagedBean.getY(),
                userManagedBean.getQ());
        auditorMap = new LinkedHashMap<>();
        if (!scorecardAuditorList.isEmpty()) {
            for (ScorecardAuditor sa : scorecardAuditorList) {
                auditorMap.put(sa.getAuditorId(), sa.getAuditorName() + "Q" + userManagedBean.getQ() + "已审核");
            }
        } else {
            auditorMap.put("no", "没有人审核哟");
        }

        super.init();
    }

    @Override
    public void construct() {
        if (fc == null) {
            fc = FacesContext.getCurrentInstance();
        }
        if (ec == null) {
            ec = fc.getExternalContext();
        }
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        scorecard = scorecardBean.findById(Integer.valueOf(id));
        if (scorecard == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            this.freezed = scorecard.getFreezeDate() != null
                    && scorecard.getFreezeDate().after(userManagedBean.getBaseDate());
        }
        init();
        super.construct();
    }

    @Override
    public void verify() {
        int quality = userManagedBean.getQ();
        if (quality == 1) {
            if ("Y".equals(scorecard.getOastatus1())) {
                showInfoMsg("Info", "该考核项已抛转至OA不能重复抛转。");
                return;
            }
        } else if (quality == 2) {
            if ("Y".equals(scorecard.getOastatus2())) {
                showInfoMsg("Info", "该考核项已抛转至OA不能重复抛转。");
                return;
            }
        } else if (quality == 3) {
            if ("Y".equals(scorecard.getOastatus3())) {
                showInfoMsg("Info", "该考核项已抛转至OA不能重复抛转。");
                return;
            }
        } else if (quality == 4) {
            if ("Y".equals(scorecard.getOastatus4())) {
                showInfoMsg("Info", "该考核项已抛转至OA不能重复抛转。");
                return;
            }
        }
        int length;
        HKGL076Model m = null;
        HKGL076Q1DetailModel d1;
        HKGL076Q2DetailModel d2;
        HKGL076Q3DetailModel d3;
        HKGL076Q4DetailModel d4;
        List<HKGL076Q1DetailModel> detailList1 = new ArrayList<>();
        List<HKGL076Q2DetailModel> detailList2 = new ArrayList<>();
        List<HKGL076Q3DetailModel> detailList3 = new ArrayList<>();
        List<HKGL076Q4DetailModel> detailList4 = new ArrayList<>();
        LinkedHashMap<String, List<?>> details = new LinkedHashMap<>();
//        details.put("Detail", detailList);
        try {
            List<ScorecardDetail> list = scorecardBean.getDetail(this.scorecard.getId());
            m = new HKGL076Model(this.scorecard.getName(), getValue(this.scorecard.getSeq()), "Q" + getValue(userManagedBean.getQ()), this.scorecard.getDeptno(),
                    userManagedBean.getUserid(), this.scorecard.getLvlValue(), userManagedBean.getCurrentUser().getDeptno(), this.scorecard.getLvl(), this.scorecard.getId().toString());
            m.setCreateDate(BaseLib.getDate());
            int i = 1;
            switch (userManagedBean.getQ()) {
                case 1://第一季度
                    for (ScorecardDetail scorecardDetail : list) {
                        d1 = new HKGL076Q1DetailModel();
                        if (scorecardDetail.getIndicator() != null && !"".equals(scorecardDetail.getIndicator())) {
                            //数据由产品指标生成，OA中不能修改
                            d1.setChange1("N");
                        } else {
                            d1.setChange1("Y");
                        }
                        d1.setSeq(getValue(i));
                        i++;
                        d1.setContent1(getValue(scorecardDetail.getContent()));
                        d1.setProportion1(getValue(scorecardDetail.getWeight()));//比重
                        d1.setBenchmarkYear1(getValue(scorecardDetail.getBfy()));//全年基准
                        d1.setTargetYear1(getValue(scorecardDetail.getTfy()));//全年目标
                        d1.setBenchmarkQ1(getValue(scorecardDetail.getBq1()));
                        d1.setTargetQ1(getValue(scorecardDetail.getTq1()));
                        d1.setActualValueQ1(getValue(scorecardDetail.getAq1()));
                        d1.setAchievementRateQ1("".equals(getValue(scorecardDetail.getPq1())) ? "0" : getValue(scorecardDetail.getPq1()));
                        length = this.getExplationsLength(scorecardDetail.getDeptScore().getQ1());
                        if (length > 200) {
                            throw new Exception("部门说明超过200字。");
                        }
                        d1.setExplanation1(getValue(scorecardDetail.getDeptScore().getQ1()));
                        d1.setScoreQ1("".equals(getValue(scorecardDetail.getDeptScore().getSq1())) ? "0" : getValue(scorecardDetail.getDeptScore().getSq1()));
                        detailList1.add(d1);
                    }
                    details.put("detailQ1", detailList1);
                    break;

                case 2://第二季度
                    for (ScorecardDetail scorecardDetail : list) {
                        d2 = new HKGL076Q2DetailModel();
                        if (scorecardDetail.getIndicator() != null && !"".equals(scorecardDetail.getIndicator())) {
                            //数据由产品指标生成，OA中不能修改
                            d2.setChange2("N");
                        } else {
                            d2.setChange2("Y");
                        }
                        d2.setSeq(getValue(i));
                        i++;
                        d2.setContent2(getValue(scorecardDetail.getContent()));
                        d2.setProportion2(getValue(scorecardDetail.getWeight()));//比重
                        d2.setBenchmarkYear2(getValue(scorecardDetail.getBfy()));//全年基准
                        d2.setTargetYear2(getValue(scorecardDetail.getTfy()));//全年目标
                        d2.setBenchmarkQ2(getValue(scorecardDetail.getBq2()));
                        d2.setTargetQ2(getValue(scorecardDetail.getTq2()));
                        d2.setActualValueQ2(getValue(scorecardDetail.getAq2()));
                        d2.setAchievementRateQ2("".equals(getValue(scorecardDetail.getPq2())) ? "0" : getValue(scorecardDetail.getPq2()));
                        length = this.getExplationsLength(scorecardDetail.getDeptScore().getQ2());
                        if (length > 200) {
                            throw new Exception("部门说明超过200字。");
                        }
                        d2.setExplanation2(getValue(scorecardDetail.getDeptScore().getQ2()));
                        d2.setScoreQ2("".equals(getValue(scorecardDetail.getDeptScore().getSq2())) ? "0" : getValue(scorecardDetail.getDeptScore().getSq2()));
                        d2.setBenchmarkHalfYear(getValue(scorecardDetail.getBh1()));
                        d2.setTargetHalfYear(getValue(scorecardDetail.getTh1()));
                        d2.setActualValueHalfYear(getValue(scorecardDetail.getAh1()));
                        d2.setAchievementRateHalfYear("".equals(getValue(scorecardDetail.getPh1())) ? "0" : getValue(scorecardDetail.getPh1()));
                        d2.setScoreHalfYear("".equals(getValue(scorecardDetail.getDeptScore().getSh1())) ? "0" : getValue(scorecardDetail.getDeptScore().getSh1()));
                        detailList2.add(d2);
                    }
                    details.put("detailQ2", detailList2);
                    break;

                case 3://第三季度
                    for (ScorecardDetail scorecardDetail : list) {
                        d3 = new HKGL076Q3DetailModel();
                        if (scorecardDetail.getIndicator() != null && !"".equals(scorecardDetail.getIndicator())) {
                            //数据由产品指标生成，OA中不能修改
                            d3.setChange3("N");
                        } else {
                            d3.setChange3("Y");
                        }
                        d3.setSeq(getValue(i));
                        i++;
                        d3.setContent3(getValue(scorecardDetail.getContent()));
                        d3.setProportion3(getValue(scorecardDetail.getWeight()));//比重
                        d3.setBenchmarkYear3(getValue(scorecardDetail.getBfy()));//全年基准
                        d3.setTargetYear3(getValue(scorecardDetail.getTfy()));//全年目标
                        d3.setBenchmarkQ3(getValue(scorecardDetail.getBq3()));
                        d3.setTargetQ3(getValue(scorecardDetail.getTq3()));
                        d3.setActualValueQ3(getValue(scorecardDetail.getAq3()));
                        d3.setAchievementRateQ3("".equals(getValue(scorecardDetail.getPq3())) ? "0" : getValue(scorecardDetail.getPq3()));
                        length = this.getExplationsLength(scorecardDetail.getDeptScore().getQ3());
                        if (length > 200) {
                            throw new Exception("部门说明超过200字。");
                        }
                        d3.setExplanation3(getValue(scorecardDetail.getDeptScore().getQ3()));
                        d3.setScoreQ3("".equals(getValue(scorecardDetail.getDeptScore().getSq3())) ? "0" : getValue(scorecardDetail.getDeptScore().getSq3()));
                        detailList3.add(d3);
                    }
                    details.put("detailQ3", detailList3);
                    break;

                case 4://第四季度
                    for (ScorecardDetail scorecardDetail : list) {
                        d4 = new HKGL076Q4DetailModel();
                        if (scorecardDetail.getIndicator() != null && !"".equals(scorecardDetail.getIndicator())) {
                            //数据由产品指标生成，OA中不能修改
                            d4.setChange4("N");
                        } else {
                            d4.setChange4("Y");
                        }
                        d4.setSeq(getValue(i));
                        i++;
                        d4.setContent4(scorecardDetail.getContent());
                        d4.setProportion4(getValue(scorecardDetail.getWeight()));//比重
                        d4.setBenchmarkYear4(getValue(scorecardDetail.getBfy()));//全年基准
                        d4.setTargetYear4(getValue(scorecardDetail.getTfy()));//全年目标
                        d4.setBenchmarkQ4(getValue(scorecardDetail.getBq4()));
                        d4.setTargetQ4(getValue(scorecardDetail.getTq4()));
                        d4.setActualValueQ4(getValue(scorecardDetail.getAq4()));
                        d4.setAchievementRateQ4("".equals(getValue(scorecardDetail.getPq4())) ? "0" : getValue(scorecardDetail.getPq4()));
                        length = this.getExplationsLength(scorecardDetail.getDeptScore().getQ4());
                        if (length > 200) {
                            throw new Exception("部门说明超过200字。");
                        }
                        d4.setExplanation4(getValue(scorecardDetail.getDeptScore().getQ4()));
                        d4.setScoreQ4("".equals(getValue(scorecardDetail.getDeptScore().getSq4())) ? "0" : getValue(scorecardDetail.getDeptScore().getSq4()));
                        d4.setActualValueYear(getValue(scorecardDetail.getAfy()));
                        d4.setAchievementRateYear("".equals(getValue(scorecardDetail.getPfy())) ? "0" : getValue(scorecardDetail.getPfy()));
                        d4.setScoreYear("".equals(getValue(scorecardDetail.getDeptScore().getSfy())) ? "0" : getValue(scorecardDetail.getDeptScore().getSfy()));
                        detailList4.add(d4);
                    }
                    details.put("detailQ4", detailList4);
                    break;

            }
            workFlowBean.initUserInfo(userManagedBean.getUserid());
            String formInstance = workFlowBean.buildXmlForEFGP("HK_GL076", m, details);
            String subject = "Q" + userManagedBean.getQ() + this.scorecard.getName();
            String msg = workFlowBean.invokeProcess(workFlowBean.HOST_ADD, workFlowBean.HOST_PORT, "PKG_HK_GL076", formInstance, subject);
            String[] rm = msg.split("\\$");
            if (rm.length == 2 && rm[1] != null) {
                boolean isSuccess = true;
                if (quality == 1) {
                    scorecard.setOastatus1("Y");
                    scorecard.setOapsn1(rm[1]);
                } else if (quality == 2) {
                    scorecard.setOastatus2("Y");
                    scorecard.setOapsn2(rm[1]);
                } else if (quality == 3) {
                    scorecard.setOastatus3("Y");
                    scorecard.setOapsn3(rm[1]);
                } else if (quality == 4) {
                    scorecard.setOastatus4("Y");
                    scorecard.setOapsn4(rm[1]);
                }
                scorecardBean.update(scorecard);
                showInfoMsg("Info", "抛转成功");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showInfoMsg("error", "抛转失败" + ex.getMessage());
        }
    }

    public String getValue(Object value) throws Exception {
        String v = String.valueOf(value);

        if (!v.contains("&") && !v.contains("<") && !v.contains(">")) {
            return this.scorecardDetailBean.formatOAEnt(value);
        } else {
            throw new Exception("文本中含有&，<,>,等特殊字符。");
        }
    }

    public int getExplationsLength(String value) {
        if (value != null && !"".equals(value)) {
            Matcher m = Pattern.compile("(?m)^.*$").matcher(value);
            StringBuffer resultValue = new StringBuffer();

            while (m.find()) {
                resultValue.append(m.group().trim());
            }

            return resultValue.length();
        } else {
            return 0;
        }
    }

    public void handleDialogReturnWhenSelect(SelectEvent event) {
        // 考核表修改权限设定
        if (scorecard.getId() != null) {
            scorecardGrant = scorecardGrantBean.findByCompanyAndScorecardidAndContentidAndSeq(
                    userManagedBean.getCompany(), scorecard.getId(), currentEntity.getId(), c.get(Calendar.YEAR));
            if (scorecardGrant == null) {
                scorecardGrant = new ScorecardGrant();
                scorecardGrant.setBenchmark(false);
                scorecardGrant.setTarget(false);
                scorecardGrant.setActual(false);
                scorecardGrant.setPerformance(false);
                scorecardGrant.setDeptscore(false);
                scorecardGrant.setGeneralscore(false);
            }
        }
    }

    @Override
    public void print() throws Exception {
        if (scorecard == null) {
            showMsg(FacesMessage.SEVERITY_WARN, "Warn", "没有可打印数据");
            return;
        }
        HashMap<String, Object> reportParams = new HashMap<>();
        reportParams.put("company", userManagedBean.getCurrentCompany().getName());
        reportParams.put("companyFullName", userManagedBean.getCurrentCompany().getFullname());
        reportParams.put("JNDIName", "java:global/KPI/KPI-ejb/ScorecardBean!cn.hanbell.kpi.ejb.ScorecardBean");
        reportParams.put("seq", scorecard.getSeq());
        reportParams.put("deptname", scorecard.getDeptname());
        reportParams.put("id", scorecard.getId());
        reportParams.put("season", userManagedBean.getQ());
        if (!this.model.getFilterFields().isEmpty()) {
            reportParams.put("filterFields", BaseLib.convertMapToStringWithClass(this.model.getFilterFields()));
        } else {
            reportParams.put("filterFields", "");
        }
        if (!this.model.getSortFields().isEmpty()) {
            reportParams.put("sortFields", BaseLib.convertMapToString(this.model.getSortFields()));
        } else {
            reportParams.put("sortFields", "");
        }
        this.fileName = "scorecard" + BaseLib.formatDate("yyyyMMddHHss", this.getDate()) + "." + "xls";

        String reportName = "";
        if (userManagedBean.getQ() == 1) {
            reportName = reportPath + "scorecarddetail1.rptdesign";
        } else if (userManagedBean.getQ() == 2) {
            reportName = reportPath + "scorecarddetail2.rptdesign";
        } else if (userManagedBean.getQ() == 3) {
            reportName = reportPath + "scorecarddetail3.rptdesign";
        } else if (userManagedBean.getQ() == 4) {
            reportName = reportPath + "scorecarddetail4.rptdesign";
        }
        String outputName = reportOutputPath + this.fileName;
        this.reportViewPath = reportViewContext + this.fileName;
        try {
            reportClassLoader = Class.forName("cn.hanbell.kpi.rpt.ScorecardReport").getClassLoader();
            // 初始配置
            this.reportInitAndConfig();
            // 生成报表
            this.reportRunAndOutput(reportName, reportParams, outputName, "xls", null);
            // 预览报表
            this.preview();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public boolean isReadonly(int q) {
        if (currentEntity.getFreezeDate() != null
                && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
            return true;
        }
        if ((this.currentEntity.getIndicator() != null && !"".equals(this.currentEntity.getIndicator()))
                || (this.currentEntity.getProjectName() != null && !"".equals(this.currentEntity.getProjectName()))
                || (this.currentEntity.getPolicyName() != null && !"".equals(this.currentEntity.getPolicyName()))) {
            return true;
        }
        return q != this.userManagedBean.getQ();
    }

    /**
     * @return the scorecard
     */
    public Scorecard getScorecard() {
        return scorecard;
    }

    /**
     * @return the freezed
     */
    public boolean isFreezed() {
        return freezed;
    }

    public ScorecardGrant getScorecardGrant() {
        return scorecardGrant;
    }

    public void setScorecardGrant(ScorecardGrant scorecardGrant) {
        this.scorecardGrant = scorecardGrant;
    }

    public LinkedHashMap<String, String> getAuditorMap() {
        return auditorMap;
    }

    public void setAuditorMap(LinkedHashMap<String, String> auditorMap) {
        this.auditorMap = auditorMap;
    }

}
