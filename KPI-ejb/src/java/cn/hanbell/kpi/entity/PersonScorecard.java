/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "personscorecard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonScorecard.findAll", query = "SELECT p FROM PersonScorecard p"),
    @NamedQuery(name = "PersonScorecard.findById", query = "SELECT p FROM PersonScorecard p WHERE p.id = :id"),
    @NamedQuery(name = "PersonScorecard.findByUserid", query = "SELECT p FROM PersonScorecard p WHERE p.userid = :userid"),
    @NamedQuery(name = "PersonScorecard.findByUsername", query = "SELECT p FROM PersonScorecard p WHERE p.username = :username"),
    @NamedQuery(name = "PersonScorecard.findByYear", query = "SELECT p FROM PersonScorecard p WHERE p.year = :year"),
    @NamedQuery(name = "PersonScorecard.findByUseridAndYear", query = "SELECT p FROM PersonScorecard p WHERE p.year = :year and p.userid = :userid"),
    @NamedQuery(name = "PersonScorecard.findByOfficiallevel", query = "SELECT p FROM PersonScorecard p WHERE p.officiallevel = :officiallevel"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivity1", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivity1 = :subjectivity1"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivityratio1", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivityratio1 = :subjectivityratio1"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivitypro1", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivitypro1 = :subjectivitypro1"),
    @NamedQuery(name = "PersonScorecard.findByObjective1", query = "SELECT p FROM PersonScorecard p WHERE p.objective1 = :objective1"),
    @NamedQuery(name = "PersonScorecard.findByObjectiveratio1", query = "SELECT p FROM PersonScorecard p WHERE p.objectiveratio1 = :objectiveratio1"),
    @NamedQuery(name = "PersonScorecard.findByObjectivepro1", query = "SELECT p FROM PersonScorecard p WHERE p.objectivepro1 = :objectivepro1"),
    @NamedQuery(name = "PersonScorecard.findByPerformance1", query = "SELECT p FROM PersonScorecard p WHERE p.performance1 = :performance1"),
    @NamedQuery(name = "PersonScorecard.findByPerformanceratio1", query = "SELECT p FROM PersonScorecard p WHERE p.performanceratio1 = :performanceratio1"),
    @NamedQuery(name = "PersonScorecard.findByPerformancepro1", query = "SELECT p FROM PersonScorecard p WHERE p.performancepro1 = :performancepro1"),
    @NamedQuery(name = "PersonScorecard.findByScorecard1", query = "SELECT p FROM PersonScorecard p WHERE p.scorecard1 = :scorecard1"),
    @NamedQuery(name = "PersonScorecard.findByScorecardratio1", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardratio1 = :scorecardratio1"),
    @NamedQuery(name = "PersonScorecard.findByScorecardpro1", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardpro1 = :scorecardpro1"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivity2", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivity2 = :subjectivity2"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivityratio2", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivityratio2 = :subjectivityratio2"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivitypro2", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivitypro2 = :subjectivitypro2"),
    @NamedQuery(name = "PersonScorecard.findByObjective2", query = "SELECT p FROM PersonScorecard p WHERE p.objective2 = :objective2"),
    @NamedQuery(name = "PersonScorecard.findByObjectiveratio2", query = "SELECT p FROM PersonScorecard p WHERE p.objectiveratio2 = :objectiveratio2"),
    @NamedQuery(name = "PersonScorecard.findByObjectivepro2", query = "SELECT p FROM PersonScorecard p WHERE p.objectivepro2 = :objectivepro2"),
    @NamedQuery(name = "PersonScorecard.findByPerformance2", query = "SELECT p FROM PersonScorecard p WHERE p.performance2 = :performance2"),
    @NamedQuery(name = "PersonScorecard.findByPerformanceratio2", query = "SELECT p FROM PersonScorecard p WHERE p.performanceratio2 = :performanceratio2"),
    @NamedQuery(name = "PersonScorecard.findByPerformancepro2", query = "SELECT p FROM PersonScorecard p WHERE p.performancepro2 = :performancepro2"),
    @NamedQuery(name = "PersonScorecard.findByScorecard2", query = "SELECT p FROM PersonScorecard p WHERE p.scorecard2 = :scorecard2"),
    @NamedQuery(name = "PersonScorecard.findByScorecardratio2", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardratio2 = :scorecardratio2"),
    @NamedQuery(name = "PersonScorecard.findByScorecardpro2", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardpro2 = :scorecardpro2"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivity3", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivity3 = :subjectivity3"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivityratio3", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivityratio3 = :subjectivityratio3"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivitypro3", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivitypro3 = :subjectivitypro3"),
    @NamedQuery(name = "PersonScorecard.findByObjective3", query = "SELECT p FROM PersonScorecard p WHERE p.objective3 = :objective3"),
    @NamedQuery(name = "PersonScorecard.findByObjectiveratio3", query = "SELECT p FROM PersonScorecard p WHERE p.objectiveratio3 = :objectiveratio3"),
    @NamedQuery(name = "PersonScorecard.findByObjectivepro3", query = "SELECT p FROM PersonScorecard p WHERE p.objectivepro3 = :objectivepro3"),
    @NamedQuery(name = "PersonScorecard.findByPerformance3", query = "SELECT p FROM PersonScorecard p WHERE p.performance3 = :performance3"),
    @NamedQuery(name = "PersonScorecard.findByPerformanceratio3", query = "SELECT p FROM PersonScorecard p WHERE p.performanceratio3 = :performanceratio3"),
    @NamedQuery(name = "PersonScorecard.findByPerformancepro3", query = "SELECT p FROM PersonScorecard p WHERE p.performancepro3 = :performancepro3"),
    @NamedQuery(name = "PersonScorecard.findByScorecard3", query = "SELECT p FROM PersonScorecard p WHERE p.scorecard3 = :scorecard3"),
    @NamedQuery(name = "PersonScorecard.findByScorecardratio3", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardratio3 = :scorecardratio3"),
    @NamedQuery(name = "PersonScorecard.findByScorecardpro3", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardpro3 = :scorecardpro3"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivity4", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivity4 = :subjectivity4"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivityratio4", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivityratio4 = :subjectivityratio4"),
    @NamedQuery(name = "PersonScorecard.findBySubjectivitypro4", query = "SELECT p FROM PersonScorecard p WHERE p.subjectivitypro4 = :subjectivitypro4"),
    @NamedQuery(name = "PersonScorecard.findByObjective4", query = "SELECT p FROM PersonScorecard p WHERE p.objective4 = :objective4"),
    @NamedQuery(name = "PersonScorecard.findByObjectiveratio4", query = "SELECT p FROM PersonScorecard p WHERE p.objectiveratio4 = :objectiveratio4"),
    @NamedQuery(name = "PersonScorecard.findByObjectivepro4", query = "SELECT p FROM PersonScorecard p WHERE p.objectivepro4 = :objectivepro4"),
    @NamedQuery(name = "PersonScorecard.findByPerformance4", query = "SELECT p FROM PersonScorecard p WHERE p.performance4 = :performance4"),
    @NamedQuery(name = "PersonScorecard.findByPerformanceratio4", query = "SELECT p FROM PersonScorecard p WHERE p.performanceratio4 = :performanceratio4"),
    @NamedQuery(name = "PersonScorecard.findByPerformancepro4", query = "SELECT p FROM PersonScorecard p WHERE p.performancepro4 = :performancepro4"),
    @NamedQuery(name = "PersonScorecard.findByScorecard4", query = "SELECT p FROM PersonScorecard p WHERE p.scorecard4 = :scorecard4"),
    @NamedQuery(name = "PersonScorecard.findByScorecardratio4", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardratio4 = :scorecardratio4"),
    @NamedQuery(name = "PersonScorecard.findByScorecardpro4", query = "SELECT p FROM PersonScorecard p WHERE p.scorecardpro4 = :scorecardpro4"),
    @NamedQuery(name = "PersonScorecard.findByStatus1", query = "SELECT p FROM PersonScorecard p WHERE p.status1 = :status1"),
    @NamedQuery(name = "PersonScorecard.findByStatus2", query = "SELECT p FROM PersonScorecard p WHERE p.status2 = :status2"),
    @NamedQuery(name = "PersonScorecard.findByStatus3", query = "SELECT p FROM PersonScorecard p WHERE p.status3 = :status3"),
    @NamedQuery(name = "PersonScorecard.findByStatus4", query = "SELECT p FROM PersonScorecard p WHERE p.status4 = :status4"),
    @NamedQuery(name = "PersonScorecard.findByStatus", query = "SELECT p FROM PersonScorecard p WHERE p.status = :status"),
    @NamedQuery(name = "PersonScorecard.findByCreator", query = "SELECT p FROM PersonScorecard p WHERE p.creator = :creator"),
    @NamedQuery(name = "PersonScorecard.findByCredate", query = "SELECT p FROM PersonScorecard p WHERE p.credate = :credate"),
    @NamedQuery(name = "PersonScorecard.findByOptdate", query = "SELECT p FROM PersonScorecard p WHERE p.optdate = :optdate"),
    @NamedQuery(name = "PersonScorecard.findByOptuser", query = "SELECT p FROM PersonScorecard p WHERE p.optuser = :optuser"),
    @NamedQuery(name = "PersonScorecard.findByCfmuser", query = "SELECT p FROM PersonScorecard p WHERE p.cfmuser = :cfmuser"),
    @NamedQuery(name = "PersonScorecard.findByCfmdate", query = "SELECT p FROM PersonScorecard p WHERE p.cfmdate = :cfmdate")})
public class PersonScorecard extends SuperEntity {

    @Size(max = 20)
    @Column(name = "userid")
    private String userid;
    @Size(max = 50)
    @Column(name = "username")
    private String username;
    @Column(name = "year")
    private Integer year;
    @Size(max = 50)
    @Column(name = "officiallevel")
    private String officiallevel;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subjectivity1")
    private BigDecimal subjectivity1;
    @Column(name = "subjectivityratio1")
    private BigDecimal subjectivityratio1;
    @Column(name = "subjectivitypro1")
    private BigDecimal subjectivitypro1;
    @Column(name = "objective1")
    private BigDecimal objective1;
    @Column(name = "objectiveratio1")
    private BigDecimal objectiveratio1;
    @Column(name = "objectivepro1")
    private BigDecimal objectivepro1;
    @Column(name = "performance1")
    private BigDecimal performance1;
    @Column(name = "performanceratio1")
    private BigDecimal performanceratio1;
    @Column(name = "performancepro1")
    private BigDecimal performancepro1;
    @Column(name = "scorecard1")
    private BigDecimal scorecard1;
    @Column(name = "scorecardratio1")
    private BigDecimal scorecardratio1;
    @Column(name = "scorecardpro1")
    private BigDecimal scorecardpro1;
    @Column(name = "subjectivity2")
    private BigDecimal subjectivity2;
    @Column(name = "subjectivityratio2")
    private BigDecimal subjectivityratio2;
    @Column(name = "subjectivitypro2")
    private BigDecimal subjectivitypro2;
    @Column(name = "objective2")
    private BigDecimal objective2;
    @Column(name = "objectiveratio2")
    private BigDecimal objectiveratio2;
    @Column(name = "objectivepro2")
    private BigDecimal objectivepro2;
    @Column(name = "performance2")
    private BigDecimal performance2;
    @Column(name = "performanceratio2")
    private BigDecimal performanceratio2;
    @Column(name = "performancepro2")
    private BigDecimal performancepro2;
    @Column(name = "scorecard2")
    private BigDecimal scorecard2;
    @Column(name = "scorecardratio2")
    private BigDecimal scorecardratio2;
    @Column(name = "scorecardpro2")
    private BigDecimal scorecardpro2;
    @Column(name = "subjectivity3")
    private BigDecimal subjectivity3;
    @Column(name = "subjectivityratio3")
    private BigDecimal subjectivityratio3;
    @Column(name = "subjectivitypro3")
    private BigDecimal subjectivitypro3;
    @Column(name = "objective3")
    private BigDecimal objective3;
    @Column(name = "objectiveratio3")
    private BigDecimal objectiveratio3;
    @Column(name = "objectivepro3")
    private BigDecimal objectivepro3;
    @Column(name = "performance3")
    private BigDecimal performance3;
    @Column(name = "performanceratio3")
    private BigDecimal performanceratio3;
    @Column(name = "performancepro3")
    private BigDecimal performancepro3;
    @Column(name = "scorecard3")
    private BigDecimal scorecard3;
    @Column(name = "scorecardratio3")
    private BigDecimal scorecardratio3;
    @Column(name = "scorecardpro3")
    private BigDecimal scorecardpro3;
    @Column(name = "subjectivity4")
    private BigDecimal subjectivity4;
    @Column(name = "subjectivityratio4")
    private BigDecimal subjectivityratio4;
    @Column(name = "subjectivitypro4")
    private BigDecimal subjectivitypro4;
    @Column(name = "objective4")
    private BigDecimal objective4;
    @Column(name = "objectiveratio4")
    private BigDecimal objectiveratio4;
    @Column(name = "objectivepro4")
    private BigDecimal objectivepro4;
    @Column(name = "performance4")
    private BigDecimal performance4;
    @Column(name = "performanceratio4")
    private BigDecimal performanceratio4;
    @Column(name = "performancepro4")
    private BigDecimal performancepro4;
    @Column(name = "scorecard4")
    private BigDecimal scorecard4;
    @Column(name = "scorecardratio4")
    private BigDecimal scorecardratio4;
    @Column(name = "scorecardpro4")
    private BigDecimal scorecardpro4;
    @Size(max = 5)
    @Column(name = "status1")
    private String status1;
    @Size(max = 5)
    @Column(name = "status2")
    private String status2;
    @Size(max = 5)
    @Column(name = "status3")
    private String status3;
    @Size(max = 5)
    @Column(name = "status4")
    private String status4;

    @JoinColumn(name = "userid", referencedColumnName = "userid", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private PersonSet personset;

    public PersonScorecard(String userid, String username, Integer year) {
        this.userid = userid;
        this.username = username;
        this.year = year;
        this.subjectivity1 = BigDecimal.ZERO;
        this.subjectivityratio1 = BigDecimal.ZERO;
        this.subjectivitypro1 = BigDecimal.ZERO;
        this.objective1 = BigDecimal.ZERO;
        this.objectiveratio1 = BigDecimal.ZERO;
        this.objectivepro1 = BigDecimal.ZERO;
        this.performance1 = BigDecimal.ZERO;
        this.performanceratio1 = BigDecimal.ZERO;
        this.performancepro1 = BigDecimal.ZERO;
        this.scorecard1 = BigDecimal.ZERO;
        this.scorecardratio1 = BigDecimal.ZERO;
        this.scorecardpro1 = BigDecimal.ZERO;
        this.subjectivity2 = BigDecimal.ZERO;
        this.subjectivityratio2 = BigDecimal.ZERO;
        this.subjectivitypro2 = BigDecimal.ZERO;
        this.objective2 = BigDecimal.ZERO;
        this.objectiveratio2 = BigDecimal.ZERO;
        this.objectivepro2 = BigDecimal.ZERO;
        this.performance2 = BigDecimal.ZERO;
        this.performanceratio2 = BigDecimal.ZERO;
        this.performancepro2 = BigDecimal.ZERO;
        this.scorecard2 = BigDecimal.ZERO;
        this.scorecardratio2 = BigDecimal.ZERO;
        this.scorecardpro2 = BigDecimal.ZERO;
        this.subjectivity3 = BigDecimal.ZERO;
        this.subjectivityratio3 = BigDecimal.ZERO;
        this.subjectivitypro3 = BigDecimal.ZERO;
        this.objective3 = BigDecimal.ZERO;
        this.objectiveratio3 = BigDecimal.ZERO;
        this.objectivepro3 = BigDecimal.ZERO;
        this.performance3 = BigDecimal.ZERO;
        this.performanceratio3 = BigDecimal.ZERO;
        this.performancepro3 = BigDecimal.ZERO;
        this.scorecard3 = BigDecimal.ZERO;
        this.scorecardratio3 = BigDecimal.ZERO;
        this.scorecardpro3 = BigDecimal.ZERO;
        this.subjectivity4 = BigDecimal.ZERO;
        this.subjectivityratio4 = BigDecimal.ZERO;
        this.subjectivitypro4 = BigDecimal.ZERO;
        this.objective4 = BigDecimal.ZERO;
        this.objectiveratio4 = BigDecimal.ZERO;
        this.objectivepro4 = BigDecimal.ZERO;
        this.performance4 = BigDecimal.ZERO;
        this.performanceratio4 = BigDecimal.ZERO;
        this.performancepro4 = BigDecimal.ZERO;
        this.scorecard4 = BigDecimal.ZERO;
        this.scorecardratio4 = BigDecimal.ZERO;
        this.scorecardpro4 = BigDecimal.ZERO;
        this.status1 = "N";
        this.status2 = "N";
        this.status3 = "N";
        this.status4 = "N";
    }

    public PersonScorecard() {
    }

    public PersonScorecard(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getOfficiallevel() {
        return officiallevel;
    }

    public void setOfficiallevel(String officiallevel) {
        this.officiallevel = officiallevel;
    }

    public BigDecimal getSubjectivity1() {
        return subjectivity1;
    }

    public void setSubjectivity1(BigDecimal subjectivity1) {
        this.subjectivity1 = subjectivity1;
    }

    public BigDecimal getSubjectivityratio1() {
        return subjectivityratio1;
    }

    public void setSubjectivityratio1(BigDecimal subjectivityratio1) {
        this.subjectivityratio1 = subjectivityratio1;
    }

    public BigDecimal getSubjectivitypro1() {
        return subjectivitypro1;
    }

    public void setSubjectivitypro1(BigDecimal subjectivitypro1) {
        this.subjectivitypro1 = subjectivitypro1;
    }

    public BigDecimal getObjective1() {
        return objective1;
    }

    public void setObjective1(BigDecimal objective1) {
        this.objective1 = objective1;
    }

    public BigDecimal getObjectiveratio1() {
        return objectiveratio1;
    }

    public void setObjectiveratio1(BigDecimal objectiveratio1) {
        this.objectiveratio1 = objectiveratio1;
    }

    public BigDecimal getObjectivepro1() {
        return objectivepro1;
    }

    public void setObjectivepro1(BigDecimal objectivepro1) {
        this.objectivepro1 = objectivepro1;
    }

    public BigDecimal getPerformance1() {
        return performance1;
    }

    public void setPerformance1(BigDecimal performance1) {
        this.performance1 = performance1;
    }

    public BigDecimal getPerformanceratio1() {
        return performanceratio1;
    }

    public void setPerformanceratio1(BigDecimal performanceratio1) {
        this.performanceratio1 = performanceratio1;
    }

    public BigDecimal getPerformancepro1() {
        return performancepro1;
    }

    public void setPerformancepro1(BigDecimal performancepro1) {
        this.performancepro1 = performancepro1;
    }

    public BigDecimal getScorecard1() {
        return scorecard1;
    }

    public void setScorecard1(BigDecimal scorecard1) {
        this.scorecard1 = scorecard1;
    }

    public BigDecimal getScorecardratio1() {
        return scorecardratio1;
    }

    public void setScorecardratio1(BigDecimal scorecardratio1) {
        this.scorecardratio1 = scorecardratio1;
    }

    public BigDecimal getScorecardpro1() {
        return scorecardpro1;
    }

    public void setScorecardpro1(BigDecimal scorecardpro1) {
        this.scorecardpro1 = scorecardpro1;
    }

    public BigDecimal getSubjectivity2() {
        return subjectivity2;
    }

    public void setSubjectivity2(BigDecimal subjectivity2) {
        this.subjectivity2 = subjectivity2;
    }

    public BigDecimal getSubjectivityratio2() {
        return subjectivityratio2;
    }

    public void setSubjectivityratio2(BigDecimal subjectivityratio2) {
        this.subjectivityratio2 = subjectivityratio2;
    }

    public BigDecimal getSubjectivitypro2() {
        return subjectivitypro2;
    }

    public void setSubjectivitypro2(BigDecimal subjectivitypro2) {
        this.subjectivitypro2 = subjectivitypro2;
    }

    public BigDecimal getObjective2() {
        return objective2;
    }

    public void setObjective2(BigDecimal objective2) {
        this.objective2 = objective2;
    }

    public BigDecimal getObjectiveratio2() {
        return objectiveratio2;
    }

    public void setObjectiveratio2(BigDecimal objectiveratio2) {
        this.objectiveratio2 = objectiveratio2;
    }

    public BigDecimal getObjectivepro2() {
        return objectivepro2;
    }

    public void setObjectivepro2(BigDecimal objectivepro2) {
        this.objectivepro2 = objectivepro2;
    }

    public BigDecimal getPerformance2() {
        return performance2;
    }

    public void setPerformance2(BigDecimal performance2) {
        this.performance2 = performance2;
    }

    public BigDecimal getPerformanceratio2() {
        return performanceratio2;
    }

    public void setPerformanceratio2(BigDecimal performanceratio2) {
        this.performanceratio2 = performanceratio2;
    }

    public BigDecimal getPerformancepro2() {
        return performancepro2;
    }

    public void setPerformancepro2(BigDecimal performancepro2) {
        this.performancepro2 = performancepro2;
    }

    public BigDecimal getScorecard2() {
        return scorecard2;
    }

    public void setScorecard2(BigDecimal scorecard2) {
        this.scorecard2 = scorecard2;
    }

    public BigDecimal getScorecardratio2() {
        return scorecardratio2;
    }

    public void setScorecardratio2(BigDecimal scorecardratio2) {
        this.scorecardratio2 = scorecardratio2;
    }

    public BigDecimal getScorecardpro2() {
        return scorecardpro2;
    }

    public void setScorecardpro2(BigDecimal scorecardpro2) {
        this.scorecardpro2 = scorecardpro2;
    }

    public BigDecimal getSubjectivity3() {
        return subjectivity3;
    }

    public void setSubjectivity3(BigDecimal subjectivity3) {
        this.subjectivity3 = subjectivity3;
    }

    public BigDecimal getSubjectivityratio3() {
        return subjectivityratio3;
    }

    public void setSubjectivityratio3(BigDecimal subjectivityratio3) {
        this.subjectivityratio3 = subjectivityratio3;
    }

    public BigDecimal getSubjectivitypro3() {
        return subjectivitypro3;
    }

    public void setSubjectivitypro3(BigDecimal subjectivitypro3) {
        this.subjectivitypro3 = subjectivitypro3;
    }

    public BigDecimal getObjective3() {
        return objective3;
    }

    public void setObjective3(BigDecimal objective3) {
        this.objective3 = objective3;
    }

    public BigDecimal getObjectiveratio3() {
        return objectiveratio3;
    }

    public void setObjectiveratio3(BigDecimal objectiveratio3) {
        this.objectiveratio3 = objectiveratio3;
    }

    public BigDecimal getObjectivepro3() {
        return objectivepro3;
    }

    public void setObjectivepro3(BigDecimal objectivepro3) {
        this.objectivepro3 = objectivepro3;
    }

    public BigDecimal getPerformance3() {
        return performance3;
    }

    public void setPerformance3(BigDecimal performance3) {
        this.performance3 = performance3;
    }

    public BigDecimal getPerformanceratio3() {
        return performanceratio3;
    }

    public void setPerformanceratio3(BigDecimal performanceratio3) {
        this.performanceratio3 = performanceratio3;
    }

    public BigDecimal getPerformancepro3() {
        return performancepro3;
    }

    public void setPerformancepro3(BigDecimal performancepro3) {
        this.performancepro3 = performancepro3;
    }

    public BigDecimal getScorecard3() {
        return scorecard3;
    }

    public void setScorecard3(BigDecimal scorecard3) {
        this.scorecard3 = scorecard3;
    }

    public BigDecimal getScorecardratio3() {
        return scorecardratio3;
    }

    public void setScorecardratio3(BigDecimal scorecardratio3) {
        this.scorecardratio3 = scorecardratio3;
    }

    public BigDecimal getScorecardpro3() {
        return scorecardpro3;
    }

    public void setScorecardpro3(BigDecimal scorecardpro3) {
        this.scorecardpro3 = scorecardpro3;
    }

    public BigDecimal getSubjectivity4() {
        return subjectivity4;
    }

    public void setSubjectivity4(BigDecimal subjectivity4) {
        this.subjectivity4 = subjectivity4;
    }

    public BigDecimal getSubjectivityratio4() {
        return subjectivityratio4;
    }

    public void setSubjectivityratio4(BigDecimal subjectivityratio4) {
        this.subjectivityratio4 = subjectivityratio4;
    }

    public BigDecimal getSubjectivitypro4() {
        return subjectivitypro4;
    }

    public void setSubjectivitypro4(BigDecimal subjectivitypro4) {
        this.subjectivitypro4 = subjectivitypro4;
    }

    public BigDecimal getObjective4() {
        return objective4;
    }

    public void setObjective4(BigDecimal objective4) {
        this.objective4 = objective4;
    }

    public BigDecimal getObjectiveratio4() {
        return objectiveratio4;
    }

    public void setObjectiveratio4(BigDecimal objectiveratio4) {
        this.objectiveratio4 = objectiveratio4;
    }

    public BigDecimal getObjectivepro4() {
        return objectivepro4;
    }

    public void setObjectivepro4(BigDecimal objectivepro4) {
        this.objectivepro4 = objectivepro4;
    }

    public BigDecimal getPerformance4() {
        return performance4;
    }

    public void setPerformance4(BigDecimal performance4) {
        this.performance4 = performance4;
    }

    public BigDecimal getPerformanceratio4() {
        return performanceratio4;
    }

    public void setPerformanceratio4(BigDecimal performanceratio4) {
        this.performanceratio4 = performanceratio4;
    }

    public BigDecimal getPerformancepro4() {
        return performancepro4;
    }

    public void setPerformancepro4(BigDecimal performancepro4) {
        this.performancepro4 = performancepro4;
    }

    public BigDecimal getScorecard4() {
        return scorecard4;
    }

    public void setScorecard4(BigDecimal scorecard4) {
        this.scorecard4 = scorecard4;
    }

    public BigDecimal getScorecardratio4() {
        return scorecardratio4;
    }

    public void setScorecardratio4(BigDecimal scorecardratio4) {
        this.scorecardratio4 = scorecardratio4;
    }

    public BigDecimal getScorecardpro4() {
        return scorecardpro4;
    }

    public void setScorecardpro4(BigDecimal scorecardpro4) {
        this.scorecardpro4 = scorecardpro4;
    }

    public String getStatus1() {
        return status1;
    }

    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public String getStatus3() {
        return status3;
    }

    public void setStatus3(String status3) {
        this.status3 = status3;
    }

    public String getStatus4() {
        return status4;
    }

    public void setStatus4(String status4) {
        this.status4 = status4;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
    }

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public String getCfmuser() {
        return cfmuser;
    }

    public void setCfmuser(String cfmuser) {
        this.cfmuser = cfmuser;
    }

    public Date getCfmdate() {
        return cfmdate;
    }

    public void setCfmdate(Date cfmdate) {
        this.cfmdate = cfmdate;
    }

    public PersonSet getPersonset() {
        return personset;
    }

    public void setPersonset(PersonSet personset) {
        this.personset = personset;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonScorecard)) {
            return false;
        }
        PersonScorecard other = (PersonScorecard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PersonScorecard[ id=" + id + " ]";
    }

}
