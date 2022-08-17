/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "scorecardauditor")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScorecardAuditor.findAll", query = "SELECT s FROM ScorecardAuditor s"),
    @NamedQuery(name = "ScorecardAuditor.findById", query = "SELECT s FROM ScorecardAuditor s WHERE s.id = :id"),
    @NamedQuery(name = "ScorecardAuditor.findByPId", query = "SELECT s FROM ScorecardAuditor s WHERE s.pid = :pid ORDER BY s.credate ASC"),
    @NamedQuery(name = "ScorecardAuditor.findByAuditorId", query = "SELECT s FROM ScorecardAuditor s WHERE s.auditorId = :auditorId"),
    @NamedQuery(name = "ScorecardAuditor.findByAuditorName", query = "SELECT s FROM ScorecardAuditor s WHERE s.auditorName = :auditorName"),
    @NamedQuery(name = "ScorecardAuditor.findByRemark", query = "SELECT s FROM ScorecardAuditor s WHERE s.remark = :remark"),
    @NamedQuery(name = "ScorecardAuditor.findByStatus", query = "SELECT s FROM ScorecardAuditor s WHERE s.status = :status"),
    @NamedQuery(name = "ScorecardAuditor.findByCreator", query = "SELECT s FROM ScorecardAuditor s WHERE s.creator = :creator"),
    @NamedQuery(name = "ScorecardAuditor.findByCredate", query = "SELECT s FROM ScorecardAuditor s WHERE s.credate = :credate"),
    @NamedQuery(name = "ScorecardAuditor.findByOptuser", query = "SELECT s FROM ScorecardAuditor s WHERE s.optuser = :optuser"),
    @NamedQuery(name = "ScorecardAuditor.findByOptdate", query = "SELECT s FROM ScorecardAuditor s WHERE s.optdate = :optdate"),
    @NamedQuery(name = "ScorecardAuditor.findByCfmuser", query = "SELECT s FROM ScorecardAuditor s WHERE s.cfmuser = :cfmuser"),
    @NamedQuery(name = "ScorecardAuditor.findByCfmdate", query = "SELECT s FROM ScorecardAuditor s WHERE s.cfmdate = :cfmdate"),
    @NamedQuery(name = "ScorecardAuditor.findByPidAndAuditorId", query = "SELECT s FROM ScorecardAuditor s WHERE s.pid = :pid AND s.auditorId = :auditorId"),
    @NamedQuery(name = "ScorecardAuditor.findByPidAndSeqAndQuarter", query = "SELECT s FROM ScorecardAuditor s WHERE s.pid = :pid AND s.seq = :seq AND s.quarter =:quarter ORDER BY s.credate ASC")})
public class ScorecardAuditor extends SuperEntity {

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private Scorecard parent;

    @Column(name = "pid")
    private Integer pid;
    @Size(max = 20)
    @Column(name = "auditorId")
    private String auditorId;
    @Size(max = 20)
    @Column(name = "auditorName")
    private String auditorName;
    @Column(name = "seq")
    private Integer seq;
    @Column(name = "quarter")
    private Integer quarter;
    @Size(max = 45)
    @Column(name = "remark")
    private String remark;

    public ScorecardAuditor() {
    }

    public ScorecardAuditor(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getAuditorId() {
        return auditorId;
    }

    public void setAuditorId(String auditorId) {
        this.auditorId = auditorId;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public Integer getQuarter() {
        return quarter;
    }

    public void setQuarter(Integer quarter) {
        this.quarter = quarter;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
    
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Scorecard getParent() {
        return parent;
    }

    public void setParent(Scorecard parent) {
        this.parent = parent;
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
        if (!(object instanceof ScorecardAuditor)) {
            return false;
        }
        ScorecardAuditor other = (ScorecardAuditor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ScorecardAuditor[ id=" + id + " ]";
    }

}
