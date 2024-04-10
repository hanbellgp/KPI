/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "personscorecarddetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonScorecardDetail.findAll", query = "SELECT p FROM PersonScorecardDetail p"),
    @NamedQuery(name = "PersonScorecardDetail.findById", query = "SELECT p FROM PersonScorecardDetail p WHERE p.id = :id"),
    @NamedQuery(name = "PersonScorecardDetail.findByPId", query = "SELECT p FROM PersonScorecardDetail p WHERE p.pid = :pid"),
    @NamedQuery(name = "PersonScorecardDetail.findBySeq", query = "SELECT p FROM PersonScorecardDetail p WHERE p.seq = :seq"),
    @NamedQuery(name = "PersonScorecardDetail.findByQuarter", query = "SELECT p FROM PersonScorecardDetail p WHERE p.quarter = :quarter"),
    @NamedQuery(name = "PersonScorecardDetail.findByType", query = "SELECT p FROM PersonScorecardDetail p WHERE p.type = :type"),
    @NamedQuery(name = "PersonScorecardDetail.findByTarget", query = "SELECT p FROM PersonScorecardDetail p WHERE p.target = :target"),
    @NamedQuery(name = "PersonScorecardDetail.findByActuality", query = "SELECT p FROM PersonScorecardDetail p WHERE p.actuality = :actuality"),
    @NamedQuery(name = "PersonScorecardDetail.findByPIdAndQuarteAndType", query = "SELECT p FROM PersonScorecardDetail p WHERE p.pid = :pid and p.quarter = :quarter and p.type = :type"),
    @NamedQuery(name = "PersonScorecardDetail.findByRatio", query = "SELECT p FROM PersonScorecardDetail p WHERE p.ratio = :ratio")})
public class PersonScorecardDetail extends SuperDetailEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "quarter")
    private Integer quarter;
    @Size(max = 5)
    @Column(name = "type")
    private String type;
    @Size(max = 500)
    @Column(name = "name")
    private String name;
    @Size(max = 500)
    @Column(name = "target")
    private String target;
    @Size(max = 500)
    @Column(name = "actuality")
    private String actuality;
    @Size(max = 500)
    @Column(name = "standard")
    private String standard;
    @Column(name = "score")
    private BigDecimal score;
    @Column(name = "ratio")
    private BigDecimal ratio;

    public PersonScorecardDetail() {
    }

    public PersonScorecardDetail(int pid, int seq, String type, Integer quarter, String name, BigDecimal score, BigDecimal ratio) {
        this.pid = pid;
        this.seq = seq;
        this.type = type;
        this.quarter = quarter;
        this.name = name;
        this.score = score;
        this.ratio = ratio;
    }

    public PersonScorecardDetail(Integer id) {
        this.id = id;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getActuality() {
        return actuality;
    }

    public void setActuality(String actuality) {
        this.actuality = actuality;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
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
        if (!(object instanceof PersonScorecardDetail)) {
            return false;
        }
        PersonScorecardDetail other = (PersonScorecardDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PersonScorecardDetail[ id=" + id + " ]";
    }

}
