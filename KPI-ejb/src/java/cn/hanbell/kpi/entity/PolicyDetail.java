/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "policydetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PolicyDetail.findAll", query = "SELECT p FROM PolicyDetail p"),
    @NamedQuery(name = "PolicyDetail.findById", query = "SELECT p FROM PolicyDetail p WHERE p.id = :id"),
    @NamedQuery(name = "PolicyDetail.findByPId", query = "SELECT p FROM PolicyDetail p WHERE p.pid = :pid"),
    @NamedQuery(name = "PolicyDetail.findBySeq", query = "SELECT p FROM PolicyDetail p WHERE p.seq = :seq"),
    @NamedQuery(name = "PolicyDetail.findByName", query = "SELECT p FROM PolicyDetail p WHERE p.name = :name"),
    @NamedQuery(name = "PolicyDetail.findBySeqname", query = "SELECT p FROM PolicyDetail p WHERE p.seqname = :seqname"),
    @NamedQuery(name = "PolicyDetail.findByPerspective", query = "SELECT p FROM PolicyDetail p WHERE p.perspective = :perspective"),
    @NamedQuery(name = "PolicyDetail.findByObjective", query = "SELECT p FROM PolicyDetail p WHERE p.objective = :objective"),
    @NamedQuery(name = "PolicyDetail.findByType", query = "SELECT p FROM PolicyDetail p WHERE p.type = :type"),
    @NamedQuery(name = "PolicyDetail.findByGenre", query = "SELECT p FROM PolicyDetail p WHERE p.genre = :genre"),
    @NamedQuery(name = "PolicyDetail.findByBq1", query = "SELECT p FROM PolicyDetail p WHERE p.bq1 = :bq1"),
    @NamedQuery(name = "PolicyDetail.findByBq2", query = "SELECT p FROM PolicyDetail p WHERE p.bq2 = :bq2"),
    @NamedQuery(name = "PolicyDetail.findByBq3", query = "SELECT p FROM PolicyDetail p WHERE p.bq3 = :bq3"),
    @NamedQuery(name = "PolicyDetail.findByBq4", query = "SELECT p FROM PolicyDetail p WHERE p.bq4 = :bq4"),
    @NamedQuery(name = "PolicyDetail.findByBhy", query = "SELECT p FROM PolicyDetail p WHERE p.bhy = :bhy"),
    @NamedQuery(name = "PolicyDetail.findByBfy", query = "SELECT p FROM PolicyDetail p WHERE p.bfy = :bfy"),
    @NamedQuery(name = "PolicyDetail.findByTq1", query = "SELECT p FROM PolicyDetail p WHERE p.tq1 = :tq1"),
    @NamedQuery(name = "PolicyDetail.findByTq2", query = "SELECT p FROM PolicyDetail p WHERE p.tq2 = :tq2"),
    @NamedQuery(name = "PolicyDetail.findByTq3", query = "SELECT p FROM PolicyDetail p WHERE p.tq3 = :tq3"),
    @NamedQuery(name = "PolicyDetail.findByTq4", query = "SELECT p FROM PolicyDetail p WHERE p.tq4 = :tq4"),
    @NamedQuery(name = "PolicyDetail.findByThy", query = "SELECT p FROM PolicyDetail p WHERE p.thy = :thy"),
    @NamedQuery(name = "PolicyDetail.findByTfy", query = "SELECT p FROM PolicyDetail p WHERE p.tfy = :tfy"),
    @NamedQuery(name = "PolicyDetail.findByAq1", query = "SELECT p FROM PolicyDetail p WHERE p.aq1 = :aq1"),
    @NamedQuery(name = "PolicyDetail.findByAq2", query = "SELECT p FROM PolicyDetail p WHERE p.aq2 = :aq2"),
    @NamedQuery(name = "PolicyDetail.findByAq3", query = "SELECT p FROM PolicyDetail p WHERE p.aq3 = :aq3"),
    @NamedQuery(name = "PolicyDetail.findByAq4", query = "SELECT p FROM PolicyDetail p WHERE p.aq4 = :aq4"),
    @NamedQuery(name = "PolicyDetail.findByAhy", query = "SELECT p FROM PolicyDetail p WHERE p.ahy = :ahy"),
    @NamedQuery(name = "PolicyDetail.findByAfy", query = "SELECT p FROM PolicyDetail p WHERE p.afy = :afy"),
    @NamedQuery(name = "PolicyDetail.findByPq1", query = "SELECT p FROM PolicyDetail p WHERE p.pq1 = :pq1"),
    @NamedQuery(name = "PolicyDetail.findByPq2", query = "SELECT p FROM PolicyDetail p WHERE p.pq2 = :pq2"),
    @NamedQuery(name = "PolicyDetail.findByPq3", query = "SELECT p FROM PolicyDetail p WHERE p.pq3 = :pq3"),
    @NamedQuery(name = "PolicyDetail.findByPq4", query = "SELECT p FROM PolicyDetail p WHERE p.pq4 = :pq4"),
    @NamedQuery(name = "PolicyDetail.findByPhy", query = "SELECT p FROM PolicyDetail p WHERE p.phy = :phy"),
    @NamedQuery(name = "PolicyDetail.findByPfy", query = "SELECT p FROM PolicyDetail p WHERE p.pfy = :pfy"),
    @NamedQuery(name = "PolicyDetail.findByUnit", query = "SELECT p FROM PolicyDetail p WHERE p.unit = :unit"),
    @NamedQuery(name = "PolicyDetail.findByCalculationtype", query = "SELECT p FROM PolicyDetail p WHERE p.calculationtype = :calculationtype"),
    @NamedQuery(name = "PolicyDetail.findByPerformancecalculation", query = "SELECT p FROM PolicyDetail p WHERE p.performancecalculation = :performancecalculation"),
    @NamedQuery(name = "PolicyDetail.findByFromkpi", query = "SELECT p FROM PolicyDetail p WHERE p.fromkpi = :fromkpi"),
    @NamedQuery(name = "PolicyDetail.findByFromkpiname", query = "SELECT p FROM PolicyDetail p WHERE p.fromkpiname = :fromkpiname"),
    @NamedQuery(name = "PolicyDetail.findByIndicatorrate", query = "SELECT p FROM PolicyDetail p WHERE p.indicatorrate = :indicatorrate"),
    @NamedQuery(name = "PolicyDetail.findByFromplm", query = "SELECT p FROM PolicyDetail p WHERE p.fromplm = :fromplm"),
    @NamedQuery(name = "PolicyDetail.findByStatus", query = "SELECT p FROM PolicyDetail p WHERE p.status = :status"),
    @NamedQuery(name = "PolicyDetail.findByCreator", query = "SELECT p FROM PolicyDetail p WHERE p.creator = :creator"),
    @NamedQuery(name = "PolicyDetail.findByCredate", query = "SELECT p FROM PolicyDetail p WHERE p.credate = :credate"),
    @NamedQuery(name = "PolicyDetail.findByOptuser", query = "SELECT p FROM PolicyDetail p WHERE p.optuser = :optuser"),
    @NamedQuery(name = "PolicyDetail.findByOptdate", query = "SELECT p FROM PolicyDetail p WHERE p.optdate = :optdate"),
    @NamedQuery(name = "PolicyDetail.findByCfmuser", query = "SELECT p FROM PolicyDetail p WHERE p.cfmuser = :cfmuser"),
    @NamedQuery(name = "PolicyDetail.findByCfmdate", query = "SELECT p FROM PolicyDetail p WHERE p.cfmdate = :cfmdate")})
public class PolicyDetail extends SuperDetailEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private Policy parent;
    
    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "seqname")
    private String seqname;
    @Size(max = 50)
    @Column(name = "perspective")
    private String perspective;
    @Size(max = 50)
    @Column(name = "objective")
    private String objective;
    @Size(max = 50)
    @Column(name = "type")
    private String type;
    @Size(max = 5)
    @Column(name = "genre")
    private String genre;
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
    @Column(name = "bhy")
    private String bhy;
    @Size(max = 200)
    @Column(name = "bfy")
    private String bfy;
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
    @Column(name = "thy")
    private String thy;
    @Size(max = 200)
    @Column(name = "tfy")
    private String tfy;
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
    @Column(name = "ahy")
    private String ahy;
    @Size(max = 200)
    @Column(name = "afy")
    private String afy;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "pq1")
    private BigDecimal pq1;
    @Column(name = "pq2")
    private BigDecimal pq2;
    @Column(name = "pq3")
    private BigDecimal pq3;
    @Column(name = "pq4")
    private BigDecimal pq4;
    @Column(name = "phy")
    private BigDecimal phy;
    @Column(name = "pfy")
    private BigDecimal pfy;
    @Size(max = 50)
    @Column(name = "unit")
    private String unit;
    @Size(max = 50)
    @Column(name = "calculationtype")
    private String calculationtype;
    @Size(max = 50)
    @Column(name = "performancecalculation")
    private String performancecalculation;
    @Size(max = 50)
    @Column(name = "fromkpi")
    private String fromkpi;
    @Size(max = 20)
    @Column(name = "fromkpiname")
    private String fromkpiname;
    @Column(name = "indicatorrate")
    private BigDecimal indicatorrate;
    @Size(max = 50)
    @Column(name = "fromplm")
    private String fromplm;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1reason1")
    private String q1reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1countermeasure1")
    private String q1countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2reason1")
    private String q2reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2countermeasure1")
    private String q2countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3reason1")
    private String q3reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3countermeasure1")
    private String q3countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4reason1")
    private String q4reason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4countermeasure1")
    private String q4countermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hyreason1")
    private String hyreason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "hycountermeasure1")
    private String hycountermeasure1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fyaction")
    private String fyaction;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fyreason1")
    private String fyreason1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fycountermeasure1")
    private String fycountermeasure1;
    @Size(max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    @Column(name = "credate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credate;
    @Size(max = 20)
    @Column(name = "optuser")
    private String optuser;
    @Column(name = "optdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optdate;
    @Size(max = 20)
    @Column(name = "cfmuser")
    private String cfmuser;
    @Column(name = "cfmdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cfmdate;

    public PolicyDetail() {
    }

    public PolicyDetail(Integer id) {
        this.id = id;
    }

    public PolicyDetail(Integer id, int seq) {
        this.id = id;
        this.seq = seq;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeqname() {
        return seqname;
    }

    public void setSeqname(String seqname) {
        this.seqname = seqname;
    }

    public String getPerspective() {
        return perspective;
    }

    public void setPerspective(String perspective) {
        this.perspective = perspective;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
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

    public String getBhy() {
        return bhy;
    }

    public void setBhy(String bhy) {
        this.bhy = bhy;
    }

    public String getBfy() {
        return bfy;
    }

    public void setBfy(String bfy) {
        this.bfy = bfy;
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

    public String getThy() {
        return thy;
    }

    public void setThy(String thy) {
        this.thy = thy;
    }

    public String getTfy() {
        return tfy;
    }

    public void setTfy(String tfy) {
        this.tfy = tfy;
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

    public String getAhy() {
        return ahy;
    }

    public void setAhy(String ahy) {
        this.ahy = ahy;
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

    public BigDecimal getPhy() {
        return phy;
    }

    public void setPhy(BigDecimal phy) {
        this.phy = phy;
    }

    public BigDecimal getPfy() {
        return pfy;
    }

    public void setPfy(BigDecimal pfy) {
        this.pfy = pfy;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCalculationtype() {
        return calculationtype;
    }

    public void setCalculationtype(String calculationtype) {
        this.calculationtype = calculationtype;
    }

    public String getPerformancecalculation() {
        return performancecalculation;
    }

    public void setPerformancecalculation(String performancecalculation) {
        this.performancecalculation = performancecalculation;
    }

    public String getFromkpi() {
        return fromkpi;
    }

    public void setFromkpi(String fromkpi) {
        this.fromkpi = fromkpi;
    }

    public String getFromkpiname() {
        return fromkpiname;
    }

    public void setFromkpiname(String fromkpiname) {
        this.fromkpiname = fromkpiname;
    }

    public BigDecimal getIndicatorrate() {
        return indicatorrate;
    }

    public void setIndicatorrate(BigDecimal indicatorrate) {
        this.indicatorrate = indicatorrate;
    }

    public String getFromplm() {
        return fromplm;
    }

    public void setFromplm(String fromplm) {
        this.fromplm = fromplm;
    }

    public String getQ1reason1() {
        return q1reason1;
    }

    public void setQ1reason1(String q1reason1) {
        this.q1reason1 = q1reason1;
    }

    public String getQ1countermeasure1() {
        return q1countermeasure1;
    }

    public void setQ1countermeasure1(String q1countermeasure1) {
        this.q1countermeasure1 = q1countermeasure1;
    }

    public String getQ2reason1() {
        return q2reason1;
    }

    public void setQ2reason1(String q2reason1) {
        this.q2reason1 = q2reason1;
    }

    public String getQ2countermeasure1() {
        return q2countermeasure1;
    }

    public void setQ2countermeasure1(String q2countermeasure1) {
        this.q2countermeasure1 = q2countermeasure1;
    }

    public String getQ3reason1() {
        return q3reason1;
    }

    public void setQ3reason1(String q3reason1) {
        this.q3reason1 = q3reason1;
    }

    public String getQ3countermeasure1() {
        return q3countermeasure1;
    }

    public void setQ3countermeasure1(String q3countermeasure1) {
        this.q3countermeasure1 = q3countermeasure1;
    }

    public String getQ4reason1() {
        return q4reason1;
    }

    public void setQ4reason1(String q4reason1) {
        this.q4reason1 = q4reason1;
    }

    public String getQ4countermeasure1() {
        return q4countermeasure1;
    }

    public void setQ4countermeasure1(String q4countermeasure1) {
        this.q4countermeasure1 = q4countermeasure1;
    }

    public String getHyreason1() {
        return hyreason1;
    }

    public void setHyreason1(String hyreason1) {
        this.hyreason1 = hyreason1;
    }

    public String getHycountermeasure1() {
        return hycountermeasure1;
    }

    public void setHycountermeasure1(String hycountermeasure1) {
        this.hycountermeasure1 = hycountermeasure1;
    }

    public String getFyaction() {
        return fyaction;
    }

    public void setFyaction(String fyaction) {
        this.fyaction = fyaction;
    }

    public String getFyreason1() {
        return fyreason1;
    }

    public void setFyreason1(String fyreason1) {
        this.fyreason1 = fyreason1;
    }

    public String getFycountermeasure1() {
        return fycountermeasure1;
    }

    public void setFycountermeasure1(String fycountermeasure1) {
        this.fycountermeasure1 = fycountermeasure1;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PolicyDetail)) {
            return false;
        }
        PolicyDetail other = (PolicyDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PolicyDetail[ id=" + id + " ]";
    }
    
}
