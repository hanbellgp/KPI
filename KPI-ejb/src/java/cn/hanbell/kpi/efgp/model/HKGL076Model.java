/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.efgp.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@XmlRootElement
public class HKGL076Model {

    private String performanceName;
    private String assessmentYear;
    private String assessmentQuarter;
    private String assessmentDept;
    private String applyUser;
    private String assessmentLevel;
    private String applyDept;
    private String levelCode;
    private String scoreCord;
    private Date createDate;

    public HKGL076Model(String performanceName, String assessmentYear, String assessmentQuarter, String assessmentDept,
             String applyUser, String assessmentLevel, String applyDept, String levelCode, String scoreCord) {
        this.performanceName = performanceName;
        this.assessmentYear = assessmentYear;
        this.assessmentQuarter = assessmentQuarter;
        this.assessmentDept = assessmentDept;
        this.applyUser = applyUser;
        this.assessmentLevel = assessmentLevel;
        this.applyDept = applyDept;
        this.levelCode = levelCode;
        this.scoreCord = scoreCord;
    }

    public String getPerformanceName() {
        return performanceName;
    }

    public void setPerformanceName(String performanceName) {
        this.performanceName = performanceName;
    }

    public String getAssessmentYear() {
        return assessmentYear;
    }

    public void setAssessmentYear(String assessmentYear) {
        this.assessmentYear = assessmentYear;
    }

    public String getAssessmentQuarter() {
        return assessmentQuarter;
    }

    public void setAssessmentQuarter(String assessmentQuarter) {
        this.assessmentQuarter = assessmentQuarter;
    }

    public String getAssessmentDept() {
        return assessmentDept;
    }

    public void setAssessmentDept(String assessmentDept) {
        this.assessmentDept = assessmentDept;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getAssessmentLevel() {
        return assessmentLevel;
    }

    public void setAssessmentLevel(String assessmentLevel) {
        this.assessmentLevel = assessmentLevel;
    }

    public String getApplyDept() {
        return applyDept;
    }

    public void setApplyDept(String applyDept) {
        this.applyDept = applyDept;
    }

    public String getLevelCode() {
        return levelCode;
    }

    public void setLevelCode(String levelCode) {
        this.levelCode = levelCode;
    }

    public String getScoreCord() {
        return scoreCord;
    }

    public void setScoreCord(String scoreCord) {
        this.scoreCord = scoreCord;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
