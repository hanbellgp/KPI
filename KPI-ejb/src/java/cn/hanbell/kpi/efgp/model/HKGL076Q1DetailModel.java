/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.efgp.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@XmlRootElement
public class HKGL076Q1DetailModel {

    private String seq;
    private String content1;
    private String proportion1;
    private String benchmarkYear1;
    private String targetYear1;
    private String benchmarkQ1;
    private String targetQ1;
    private String actualValueQ1;
    private String achievementRateQ1;
    private String explanation1;
    private String scoreQ1;
    private String  change1;

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    
    public String getContent1() {
        return content1;
    }

    public void setContent1(String content1) {
        this.content1 = content1;
    }

    public String getProportion1() {
        return proportion1;
    }

    public void setProportion1(String proportion1) {
        this.proportion1 = proportion1;
    }

    public String getBenchmarkYear1() {
        return benchmarkYear1;
    }

    public void setBenchmarkYear1(String benchmarkYear1) {
        this.benchmarkYear1 = benchmarkYear1;
    }

    public String getTargetYear1() {
        return targetYear1;
    }

    public void setTargetYear1(String targetYear1) {
        this.targetYear1 = targetYear1;
    }

    public String getBenchmarkQ1() {
        return benchmarkQ1;
    }

    public void setBenchmarkQ1(String benchmarkQ1) {
        this.benchmarkQ1 = benchmarkQ1;
    }

    public String getTargetQ1() {
        return targetQ1;
    }

    public void setTargetQ1(String targetQ1) {
        this.targetQ1 = targetQ1;
    }

    public String getActualValueQ1() {
        return actualValueQ1;
    }

    public void setActualValueQ1(String actualValueQ1) {
        this.actualValueQ1 = actualValueQ1;
    }

    public String getAchievementRateQ1() {
        return achievementRateQ1;
    }

    public void setAchievementRateQ1(String achievementRateQ1) {
        this.achievementRateQ1 = achievementRateQ1;
    }

    public String getExplanation1() {
        return explanation1;
    }

    public void setExplanation1(String explanation1) {
        this.explanation1 = explanation1;
    }

    public String getScoreQ1() {
        return scoreQ1;
    }

    public void setScoreQ1(String scoreQ1) {
        this.scoreQ1 = scoreQ1;
    }

    public String getChange1() {
        return change1;
    }

    public void setChange1(String change1) {
        this.change1 = change1;
    }

}
