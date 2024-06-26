/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "personscorecardway")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonScorecardWay.findAll", query = "SELECT p FROM PersonScorecardWay p"),
    @NamedQuery(name = "PersonScorecardWay.findById", query = "SELECT p FROM PersonScorecardWay p WHERE p.id = :id"),
    @NamedQuery(name = "PersonScorecardWay.findByFormid", query = "SELECT p FROM PersonScorecardWay p WHERE p.formid = :formid"),
    @NamedQuery(name = "PersonScorecardWay.findByFormname", query = "SELECT p FROM PersonScorecardWay p WHERE p.formname = :formname")})
public class PersonScorecardWay extends SuperEntity {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2)
    @Column(name = "formid")
    private String formid;
    @Size(max = 50)
    @Column(name = "formname")
    private String formname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "scoresubjectivityratio")
    private BigDecimal scoresubjectivityratio;
    @Column(name = "scoreobjectiveratio")
    private BigDecimal scoreobjectiveratio;
    @Column(name = "performanceratio")
    private BigDecimal performanceratio;
    @Column(name = "scoreratio")
    private BigDecimal scoreratio;
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

    public PersonScorecardWay() {
    }

    public PersonScorecardWay(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getFormname() {
        return formname;
    }

    public void setFormname(String formname) {
        this.formname = formname;
    }

    public BigDecimal getScoresubjectivityratio() {
        return scoresubjectivityratio;
    }

    public void setScoresubjectivityratio(BigDecimal scoresubjectivityratio) {
        this.scoresubjectivityratio = scoresubjectivityratio;
    }

    public BigDecimal getScoreobjectiveratio() {
        return scoreobjectiveratio;
    }

    public void setScoreobjectiveratio(BigDecimal scoreobjectiveratio) {
        this.scoreobjectiveratio = scoreobjectiveratio;
    }

    public BigDecimal getPerformanceratio() {
        return performanceratio;
    }

    public void setPerformanceratio(BigDecimal performanceratio) {
        this.performanceratio = performanceratio;
    }

    public BigDecimal getScoreratio() {
        return scoreratio;
    }

    public void setScoreratio(BigDecimal scoreratio) {
        this.scoreratio = scoreratio;
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
        if (!(object instanceof PersonScorecardWay)) {
            return false;
        }
        PersonScorecardWay other = (PersonScorecardWay) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PersonScorecardWay[ id=" + id + " ]";
    }
    
}
