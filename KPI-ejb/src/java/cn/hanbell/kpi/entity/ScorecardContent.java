/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "scorecarddetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScorecardContent.findAll", query = "SELECT s FROM ScorecardContent s"),
    @NamedQuery(name = "ScorecardContent.findById", query = "SELECT s FROM ScorecardContent s WHERE s.id = :id"),
    @NamedQuery(name = "ScorecardContent.findByPId", query = "SELECT s FROM ScorecardContent s WHERE s.pid = :pid ORDER BY s.seq"),
    @NamedQuery(name = "ScorecardContent.findByContent", query = "SELECT s FROM ScorecardContent s WHERE s.content = :content"),
    @NamedQuery(name = "ScorecardContent.findByType", query = "SELECT s FROM ScorecardContent s WHERE s.type = :type"),
    @NamedQuery(name = "ScorecardContent.findByKind", query = "SELECT s FROM ScorecardContent s WHERE s.kind = :kind"),
    @NamedQuery(name = "ScorecardContent.findByCategoryId", query = "SELECT s FROM ScorecardContent s WHERE s.categoryId = :categoryId"),
    @NamedQuery(name = "ScorecardContent.findByCategory", query = "SELECT s FROM ScorecardContent s WHERE s.category = :category"),
    @NamedQuery(name = "ScorecardContent.findByStatus", query = "SELECT s FROM ScorecardContent s WHERE s.status = :status")})
public class ScorecardContent extends SuperEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private Scorecard parent;

    @JoinColumn(name = "selfScoreId", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation selfScore;
    @JoinColumn(name = "deptScoreId", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation deptScore;
    @JoinColumn(name = "generalScoreId", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation generalScore;
    @JoinColumn(name = "otherScoreId", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation otherScore;
    @JoinColumn(name = "causeScore1Id", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation causeScore1;
    @JoinColumn(name = "summaryScore1Id", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation summaryScore1;
    @JoinColumn(name = "causeScore2Id", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation causeScore2;
    @JoinColumn(name = "summaryScore2Id", referencedColumnName = "id")
    @OneToOne
    private ScorecardExplanation summaryScore2;

    @Basic(optional = false)
    @NotNull
    @Column(name = "pid")
    private int pid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seq")
    private int seq;

    @Size(max = 45)
    @Column(name = "content")
    private String content;
    @Size(max = 10)
    @Column(name = "type")
    private String type;
    @Size(max = 10)
    @Column(name = "kind")
    private String kind;
    @Basic(optional = false)
    @NotNull
    @Column(name = "weight")
    private BigDecimal weight;
    @Column(name = "categoryId")
    private Integer categoryId;
    @Size(max = 45)
    @Column(name = "category")
    private String category;
    @Column(name = "sortid")
    private Integer sortid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "valuemode")
    private String valueMode;
    @Size(max = 10)
    @Column(name = "symbol")
    private String symbol;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "minNum")
    private BigDecimal minNum;
    @Column(name = "maxNum")
    private BigDecimal maxNum;
    @Column(name = "limited")
    private Boolean limited;
    @Size(max = 20)
    @Column(name = "indicator")
    private String indicator;
    @Size(max = 45)
    @Column(name = "projectName")
    private String projectName;
    @Size(max = 10)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 10)
    @Column(name = "userid")
    private String userid;
    @Size(max = 20)
    @Column(name = "username")
    private String username;
    @Size(max = 200)
    @Column(name = "tq1")
    private String tq1;
    @Size(max = 200)
    @Column(name = "tq2")
    private String tq2;
    @Size(max = 200)
    @Column(name = "tq3")
    private String tq3;
    @Size(max = 200)
    @Column(name = "tq4")
    private String tq4;
    @Size(max = 200)
    @Column(name = "th1")
    private String th1;
    @Size(max = 200)
    @Column(name = "th2")
    private String th2;
    @Size(max = 200)
    @Column(name = "tfy")
    private String tfy;
    @Size(max = 200)
    @Column(name = "bq1")
    private String bq1;
    @Size(max = 200)
    @Column(name = "bq2")
    private String bq2;
    @Size(max = 200)
    @Column(name = "bq3")
    private String bq3;
    @Size(max = 200)
    @Column(name = "bq4")
    private String bq4;
    @Size(max = 200)
    @Column(name = "bh1")
    private String bh1;
    @Size(max = 200)
    @Column(name = "bh2")
    private String bh2;
    @Size(max = 200)
    @Column(name = "bfy")
    private String bfy;
    @Size(max = 200)
    @Column(name = "fq1")
    private String fq1;
    @Size(max = 200)
    @Column(name = "fq2")
    private String fq2;
    @Size(max = 200)
    @Column(name = "fq3")
    private String fq3;
    @Size(max = 200)
    @Column(name = "fq4")
    private String fq4;
    @Size(max = 200)
    @Column(name = "fh1")
    private String fh1;
    @Size(max = 200)
    @Column(name = "fh2")
    private String fh2;
    @Size(max = 200)
    @Column(name = "ffy")
    private String ffy;
    @Size(max = 200)
    @Column(name = "aq1")
    private String aq1;
    @Size(max = 200)
    @Column(name = "aq2")
    private String aq2;
    @Size(max = 200)
    @Column(name = "aq3")
    private String aq3;
    @Size(max = 200)
    @Column(name = "aq4")
    private String aq4;
    @Size(max = 200)
    @Column(name = "ah1")
    private String ah1;
    @Size(max = 200)
    @Column(name = "ah2")
    private String ah2;
    @Size(max = 200)
    @Column(name = "afy")
    private String afy;
    @Column(name = "pq1")
    private BigDecimal pq1;
    @Column(name = "pq2")
    private BigDecimal pq2;
    @Column(name = "pq3")
    private BigDecimal pq3;
    @Column(name = "pq4")
    private BigDecimal pq4;
    @Column(name = "ph1")
    private BigDecimal ph1;
    @Column(name = "ph2")
    private BigDecimal ph2;
    @Column(name = "pfy")
    private BigDecimal pfy;
    @Column(name = "sq1")
    private BigDecimal sq1;
    @Column(name = "sq2")
    private BigDecimal sq2;
    @Column(name = "sq3")
    private BigDecimal sq3;
    @Column(name = "sq4")
    private BigDecimal sq4;
    @Column(name = "sh1")
    private BigDecimal sh1;
    @Column(name = "sh2")
    private BigDecimal sh2;
    @Column(name = "sfy")
    private BigDecimal sfy;
    @Size(max = 800)
    @Column(name = "performanceJexl")
    private String performanceJexl;
    @Size(max = 200)
    @Column(name = "performanceInterface")
    private String performanceInterface;
    @Size(max = 800)
    @Column(name = "scoreJexl")
    private String scoreJexl;
    @Size(max = 200)
    @Column(name = "scoreInterface")
    private String scoreInterface;
    @Column(name = "cq1")
    private BigDecimal cq1;
    @Column(name = "cq2")
    private BigDecimal cq2;
    @Column(name = "cq3")
    private BigDecimal cq3;
    @Column(name = "cq4")
    private BigDecimal cq4;
    @Column(name = "ch1")
    private BigDecimal ch1;
    @Column(name = "ch2")
    private BigDecimal ch2;
    @Column(name = "cfy")
    private BigDecimal cfy;
    @Size(max = 10)
    @Column(name = "freeze")
    private String freeze;
    @Column(name = "freezeDate")
    @Temporal(TemporalType.DATE)
    private Date freezeDate;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;
    @Size(max = 45)
    @Column(name = "projectSeq")
    private String projectSeq;
    public ScorecardContent() {
        this.sortid = 0;
        this.status = "N";
    }

    /**
     * @return the parent
     */
    public Scorecard getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Scorecard parent) {
        this.parent = parent;
    }

    /**
     * @return the selfScore
     */
    public ScorecardExplanation getSelfScore() {
        return selfScore;
    }

    /**
     * @param selfScore the selfScore to set
     */
    public void setSelfScore(ScorecardExplanation selfScore) {
        this.selfScore = selfScore;
    }

    /**
     * @return the deptScore
     */
    public ScorecardExplanation getDeptScore() {
        return deptScore;
    }

    /**
     * @param deptScore the deptScore to set
     */
    public void setDeptScore(ScorecardExplanation deptScore) {
        this.deptScore = deptScore;
    }

    /**
     * @return the generalScore
     */
    public ScorecardExplanation getGeneralScore() {
        return generalScore;
    }

    /**
     * @param generalScore the generalScore to set
     */
    public void setGeneralScore(ScorecardExplanation generalScore) {
        this.generalScore = generalScore;
    }

    /**
     * @return the otherScore
     */
    public ScorecardExplanation getOtherScore() {
        return otherScore;
    }

    /**
     * @param otherScore the otherScore to set
     */
    public void setOtherScore(ScorecardExplanation otherScore) {
        this.otherScore = otherScore;
    }

    /**
     * @return the pid
     */
    public int getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(int pid) {
        this.pid = pid;
    }

    /**
     * @return the seq
     */
    public int getSeq() {
        return seq;
    }

    /**
     * @param seq the seq to set
     */
    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getSortid() {
        return sortid;
    }

    public void setSortid(Integer sortid) {
        this.sortid = sortid;
    }

    public String getValueMode() {
        return valueMode;
    }

    public void setValueMode(String valueMode) {
        this.valueMode = valueMode;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getMinNum() {
        return minNum;
    }

    public void setMinNum(BigDecimal minNum) {
        this.minNum = minNum;
    }

    public BigDecimal getMaxNum() {
        return maxNum;
    }

    public void setMaxNum(BigDecimal maxNum) {
        this.maxNum = maxNum;
    }

    public Boolean getLimited() {
        return limited;
    }

    public void setLimited(Boolean limited) {
        this.limited = limited;
    }

    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
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

    public String getTq1() {
        return tq1;
    }

    public void setTq1(String tq1) {
        this.tq1 = tq1;
    }

    public String getTq2() {
        return tq2;
    }

    public void setTq2(String tq2) {
        this.tq2 = tq2;
    }

    public String getTq3() {
        return tq3;
    }

    public void setTq3(String tq3) {
        this.tq3 = tq3;
    }

    public String getTq4() {
        return tq4;
    }

    public void setTq4(String tq4) {
        this.tq4 = tq4;
    }

    public String getTh1() {
        return th1;
    }

    public void setTh1(String th1) {
        this.th1 = th1;
    }

    public String getTh2() {
        return th2;
    }

    public void setTh2(String th2) {
        this.th2 = th2;
    }

    public String getTfy() {
        return tfy;
    }

    public void setTfy(String tfy) {
        this.tfy = tfy;
    }

    public String getBq1() {
        return bq1;
    }

    public void setBq1(String bq1) {
        this.bq1 = bq1;
    }

    public String getBq2() {
        return bq2;
    }

    public void setBq2(String bq2) {
        this.bq2 = bq2;
    }

    public String getBq3() {
        return bq3;
    }

    public void setBq3(String bq3) {
        this.bq3 = bq3;
    }

    public String getBq4() {
        return bq4;
    }

    public void setBq4(String bq4) {
        this.bq4 = bq4;
    }

    public String getBh1() {
        return bh1;
    }

    public void setBh1(String bh1) {
        this.bh1 = bh1;
    }

    public String getBh2() {
        return bh2;
    }

    public void setBh2(String bh2) {
        this.bh2 = bh2;
    }

    public String getBfy() {
        return bfy;
    }

    public void setBfy(String bfy) {
        this.bfy = bfy;
    }

    public String getFq1() {
        return fq1;
    }

    public void setFq1(String fq1) {
        this.fq1 = fq1;
    }

    public String getFq2() {
        return fq2;
    }

    public void setFq2(String fq2) {
        this.fq2 = fq2;
    }

    public String getFq3() {
        return fq3;
    }

    public void setFq3(String fq3) {
        this.fq3 = fq3;
    }

    public String getFq4() {
        return fq4;
    }

    public void setFq4(String fq4) {
        this.fq4 = fq4;
    }

    public String getFh1() {
        return fh1;
    }

    public void setFh1(String fh1) {
        this.fh1 = fh1;
    }

    public String getFh2() {
        return fh2;
    }

    public void setFh2(String fh2) {
        this.fh2 = fh2;
    }

    public String getFfy() {
        return ffy;
    }

    public void setFfy(String ffy) {
        this.ffy = ffy;
    }

    public String getAq1() {
        return aq1;
    }

    public void setAq1(String aq1) {
        this.aq1 = aq1;
    }

    public String getAq2() {
        return aq2;
    }

    public void setAq2(String aq2) {
        this.aq2 = aq2;
    }

    public String getAq3() {
        return aq3;
    }

    public void setAq3(String aq3) {
        this.aq3 = aq3;
    }

    public String getAq4() {
        return aq4;
    }

    public void setAq4(String aq4) {
        this.aq4 = aq4;
    }

    public String getAh1() {
        return ah1;
    }

    public void setAh1(String ah1) {
        this.ah1 = ah1;
    }

    public String getAh2() {
        return ah2;
    }

    public void setAh2(String ah2) {
        this.ah2 = ah2;
    }

    public String getAfy() {
        return afy;
    }

    public void setAfy(String afy) {
        this.afy = afy;
    }

    public BigDecimal getPq1() {
        return pq1;
    }

    public void setPq1(BigDecimal pq1) {
        this.pq1 = pq1;
    }

    public BigDecimal getPq2() {
        return pq2;
    }

    public void setPq2(BigDecimal pq2) {
        this.pq2 = pq2;
    }

    public BigDecimal getPq3() {
        return pq3;
    }

    public void setPq3(BigDecimal pq3) {
        this.pq3 = pq3;
    }

    public BigDecimal getPq4() {
        return pq4;
    }

    public void setPq4(BigDecimal pq4) {
        this.pq4 = pq4;
    }

    public BigDecimal getPh1() {
        return ph1;
    }

    public void setPh1(BigDecimal ph1) {
        this.ph1 = ph1;
    }

    public BigDecimal getPh2() {
        return ph2;
    }

    public void setPh2(BigDecimal ph2) {
        this.ph2 = ph2;
    }

    public BigDecimal getPfy() {
        return pfy;
    }

    public void setPfy(BigDecimal pfy) {
        this.pfy = pfy;
    }

    public BigDecimal getSq1() {
        return sq1;
    }

    public void setSq1(BigDecimal sq1) {
        this.sq1 = sq1;
    }

    public BigDecimal getSq2() {
        return sq2;
    }

    public void setSq2(BigDecimal sq2) {
        this.sq2 = sq2;
    }

    public BigDecimal getSq3() {
        return sq3;
    }

    public void setSq3(BigDecimal sq3) {
        this.sq3 = sq3;
    }

    public BigDecimal getSq4() {
        return sq4;
    }

    public void setSq4(BigDecimal sq4) {
        this.sq4 = sq4;
    }

    public BigDecimal getSh1() {
        return sh1;
    }

    public void setSh1(BigDecimal sh1) {
        this.sh1 = sh1;
    }

    public BigDecimal getSh2() {
        return sh2;
    }

    public void setSh2(BigDecimal sh2) {
        this.sh2 = sh2;
    }

    public BigDecimal getSfy() {
        return sfy;
    }

    public void setSfy(BigDecimal sfy) {
        this.sfy = sfy;
    }

    public String getPerformanceJexl() {
        return performanceJexl;
    }

    public void setPerformanceJexl(String performanceJexl) {
        this.performanceJexl = performanceJexl;
    }

    public String getPerformanceInterface() {
        return performanceInterface;
    }

    public void setPerformanceInterface(String performanceInterface) {
        this.performanceInterface = performanceInterface;
    }

    public String getScoreJexl() {
        return scoreJexl;
    }

    public void setScoreJexl(String scoreJexl) {
        this.scoreJexl = scoreJexl;
    }

    public String getScoreInterface() {
        return scoreInterface;
    }

    public void setScoreInterface(String scoreInterface) {
        this.scoreInterface = scoreInterface;
    }

    /**
     * @return the cq1
     */
    public BigDecimal getCq1() {
        return cq1;
    }

    /**
     * @param cq1 the cq1 to set
     */
    public void setCq1(BigDecimal cq1) {
        this.cq1 = cq1;
    }

    /**
     * @return the cq2
     */
    public BigDecimal getCq2() {
        return cq2;
    }

    /**
     * @param cq2 the cq2 to set
     */
    public void setCq2(BigDecimal cq2) {
        this.cq2 = cq2;
    }

    /**
     * @return the cq3
     */
    public BigDecimal getCq3() {
        return cq3;
    }

    /**
     * @param cq3 the cq3 to set
     */
    public void setCq3(BigDecimal cq3) {
        this.cq3 = cq3;
    }

    /**
     * @return the cq4
     */
    public BigDecimal getCq4() {
        return cq4;
    }

    /**
     * @param cq4 the cq4 to set
     */
    public void setCq4(BigDecimal cq4) {
        this.cq4 = cq4;
    }

    /**
     * @return the ch1
     */
    public BigDecimal getCh1() {
        return ch1;
    }

    /**
     * @param ch1 the ch1 to set
     */
    public void setCh1(BigDecimal ch1) {
        this.ch1 = ch1;
    }

    /**
     * @return the ch2
     */
    public BigDecimal getCh2() {
        return ch2;
    }

    /**
     * @param ch2 the ch2 to set
     */
    public void setCh2(BigDecimal ch2) {
        this.ch2 = ch2;
    }

    /**
     * @return the cfy
     */
    public BigDecimal getCfy() {
        return cfy;
    }

    /**
     * @param cfy the cfy to set
     */
    public void setCfy(BigDecimal cfy) {
        this.cfy = cfy;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    /**
     * @return the freezeDate
     */
    public Date getFreezeDate() {
        return freezeDate;
    }

    /**
     * @param freezeDate the freezeDate to set
     */
    public void setFreezeDate(Date freezeDate) {
        this.freezeDate = freezeDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    public String getOptuser() {
        return optuser;
    }

    public void setOptuser(String optuser) {
        this.optuser = optuser;
    }

    public Date getOptdate() {
        return optdate;
    }

    public void setOptdate(Date optdate) {
        this.optdate = optdate;
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

    public ScorecardExplanation getCauseScore1() {
        return causeScore1;
    }

    public void setCauseScore1(ScorecardExplanation causeScore1) {
        this.causeScore1 = causeScore1;
    }

    public ScorecardExplanation getSummaryScore1() {
        return summaryScore1;
    }

    public void setSummaryScore1(ScorecardExplanation summaryScore1) {
        this.summaryScore1 = summaryScore1;
    }

    public ScorecardExplanation getCauseScore2() {
        return causeScore2;
    }

    public void setCauseScore2(ScorecardExplanation causeScore2) {
        this.causeScore2 = causeScore2;
    }

    public ScorecardExplanation getSummaryScore2() {
        return summaryScore2;
    }

    public void setSummaryScore2(ScorecardExplanation summaryScore2) {
        this.summaryScore2 = summaryScore2;
    }

    public String getProjectSeq() {
        return projectSeq;
    }

    public void setProjectSeq(String projectSeq) {
        this.projectSeq = projectSeq;
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
        if (!(object instanceof ScorecardContent)) {
            return false;
        }
        ScorecardContent other = (ScorecardContent) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return Objects.equals(this.getPid(), other.getPid()) && Objects.equals(this.getSeq(), other.getSeq());
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ScorecardContent[ id=" + id + " ]";
    }

}
