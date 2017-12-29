/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "indicator")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Indicator.findAll", query = "SELECT i FROM Indicator i"),
    @NamedQuery(name = "Indicator.findById", query = "SELECT i FROM Indicator i WHERE i.id = :id"),
    @NamedQuery(name = "Indicator.findByCompany", query = "SELECT i FROM Indicator i WHERE i.lvl = 0 AND i.company = :company ORDER BY i.seq DESC,i.sortid ASC"),
    @NamedQuery(name = "Indicator.findByFormidSeqAndDeptno", query = "SELECT i FROM Indicator i WHERE i.formid = :formid AND i.seq = :seq AND i.deptno=:deptno"),
    @NamedQuery(name = "Indicator.findByFormtype", query = "SELECT i FROM Indicator i WHERE i.formtype = :formtype"),
    @NamedQuery(name = "Indicator.findByFormkind", query = "SELECT i FROM Indicator i WHERE i.formkind = :formkind"),
    @NamedQuery(name = "Indicator.findByPId", query = "SELECT i FROM Indicator i WHERE i.pid = :pid ORDER BY i.seq DESC,i.sortid ASC,i.deptno ASC"),
    @NamedQuery(name = "Indicator.findByPIdAndSeq", query = "SELECT i FROM Indicator i WHERE i.pid = :pid AND i.seq = :seq"),
    @NamedQuery(name = "Indicator.findByPIdSeqAndDeptno", query = "SELECT i FROM Indicator i WHERE i.pid = :pid AND i.seq = :seq AND i.deptno=:deptno"),
    @NamedQuery(name = "Indicator.findByDeptno", query = "SELECT i FROM Indicator i WHERE i.deptno = :deptno"),
    @NamedQuery(name = "Indicator.findByDeptname", query = "SELECT i FROM Indicator i WHERE i.deptname = :deptname"),
    @NamedQuery(name = "Indicator.findByUserid", query = "SELECT i FROM Indicator i WHERE i.userid = :userid"),
    @NamedQuery(name = "Indicator.findByUsername", query = "SELECT i FROM Indicator i WHERE i.username = :username"),
    @NamedQuery(name = "Indicator.findByRemark", query = "SELECT i FROM Indicator i WHERE i.remark = :remark"),
    @NamedQuery(name = "Indicator.findByStatus", query = "SELECT i FROM Indicator i WHERE i.status = :status")})
public class Indicator extends SuperEntity {

    @JoinColumn(name = "formid", referencedColumnName = "formid", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private IndicatorDefine indicatorDefine;

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private Indicator parent;

    @JoinColumn(name = "actualId", referencedColumnName = "id")
    @OneToOne
    private IndicatorDetail actualIndicator;
    @JoinColumn(name = "benchmarkId", referencedColumnName = "id")
    @OneToOne
    private IndicatorDetail benchmarkIndicator;
    @JoinColumn(name = "forecastId", referencedColumnName = "id")
    @OneToOne
    private IndicatorDetail forecastIndicator;
    @JoinColumn(name = "performanceId", referencedColumnName = "id")
    @OneToOne
    private IndicatorDetail performanceIndicator;
    @JoinColumn(name = "targetId", referencedColumnName = "id")
    @OneToOne
    private IndicatorDetail targetIndicator;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Size(max = 20)
    @Column(name = "formid")
    private String formid;
    @Size(max = 100)
    @Column(name = "formtype")
    private String formtype;
    @Size(max = 10)
    @Column(name = "formkind")
    private String formkind;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 400)
    @Column(name = "descript")
    private String descript;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid")
    private int pid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seq")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "objtype")
    private String objtype;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 20)
    @Column(name = "userid")
    private String userid;
    @Size(max = 20)
    @Column(name = "username")
    private String username;
    @Column(name = "sortid")
    private int sortid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "lvl")
    private int lvl;
    @Basic(optional = false)
    @NotNull
    @Size(max = 10)
    @Column(name = "valuemode")
    private String valueMode;
    @NotNull
    @Size(max = 10)
    @Column(name = "perfcalc")
    private String perfCalc;
    @Size(max = 10)
    @Column(name = "symbol")
    private String symbol;
    @Size(max = 10)
    @Column(name = "unit")
    private String unit;
    @Column(name = "rate")
    private BigDecimal rate;
    @Column(name = "limited")
    private boolean limited;
    @Column(name = "assigned")
    private boolean assigned;
    @Size(max = 45)
    @Column(name = "api")
    private String api;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public Indicator() {
        this.pid = 0;
        this.symbol = "";
        this.limited = false;
        this.assigned = false;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the formid
     */
    public String getFormid() {
        return formid;
    }

    /**
     * @param formid the formid to set
     */
    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getFormtype() {
        return formtype;
    }

    public void setFormtype(String formtype) {
        this.formtype = formtype;
    }

    public String getFormkind() {
        return formkind;
    }

    public void setFormkind(String formkind) {
        this.formkind = formkind;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the descript
     */
    public String getDescript() {
        return descript;
    }

    /**
     * @param descript the descript to set
     */
    public void setDescript(String descript) {
        this.descript = descript;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    /**
     * @return the objtype
     */
    public String getObjtype() {
        return objtype;
    }

    /**
     * @param objtype the objtype to set
     */
    public void setObjtype(String objtype) {
        this.objtype = objtype;
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

    /**
     * @return the sortid
     */
    public int getSortid() {
        return sortid;
    }

    /**
     * @param sortid the sortid to set
     */
    public void setSortid(int sortid) {
        this.sortid = sortid;
    }

    /**
     * @return the lvl
     */
    public int getLvl() {
        return lvl;
    }

    /**
     * @param lvl the lvl to set
     */
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    /**
     * @return the valueMode
     */
    public String getValueMode() {
        return valueMode;
    }

    /**
     * @param valueMode the valueMode to set
     */
    public void setValueMode(String valueMode) {
        this.valueMode = valueMode;
    }

    /**
     * @return the perfCalc
     */
    public String getPerfCalc() {
        return perfCalc;
    }

    /**
     * @param perfCalc the perfCalc to set
     */
    public void setPerfCalc(String perfCalc) {
        this.perfCalc = perfCalc;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return the rate
     */
    public BigDecimal getRate() {
        return rate;
    }

    /**
     * @return the limited
     */
    public boolean isLimited() {
        return limited;
    }

    /**
     * @param unit the unit to set
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * @param rate the rate to set
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    /**
     * @param limited the limited to set
     */
    public void setLimited(boolean limited) {
        this.limited = limited;
    }

    /**
     * @return the assigned
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     * @param assigned the assigned to set
     */
    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }

    /**
     * @return the api
     */
    public String getApi() {
        return api;
    }

    /**
     * @param api the api to set
     */
    public void setApi(String api) {
        this.api = api;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof Indicator)) {
            return false;
        }
        Indicator other = (Indicator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Indicator[ id=" + id + " ]";
    }

    /**
     * @return the indicatorDefine
     */
    public IndicatorDefine getIndicatorDefine() {
        return indicatorDefine;
    }

    /**
     * @return the parent
     */
    public Indicator getParent() {
        return parent;
    }

    /**
     * @return the actualIndicator
     */
    public IndicatorDetail getActualIndicator() {
        return actualIndicator;
    }

    /**
     * @param actualIndicator the actualIndicator to set
     */
    public void setActualIndicator(IndicatorDetail actualIndicator) {
        this.actualIndicator = actualIndicator;
    }

    /**
     * @return the benchmarkIndicator
     */
    public IndicatorDetail getBenchmarkIndicator() {
        return benchmarkIndicator;
    }

    /**
     * @param benchmarkIndicator the benchmarkIndicator to set
     */
    public void setBenchmarkIndicator(IndicatorDetail benchmarkIndicator) {
        this.benchmarkIndicator = benchmarkIndicator;
    }

    /**
     * @return the forecastIndicator
     */
    public IndicatorDetail getForecastIndicator() {
        return forecastIndicator;
    }

    /**
     * @param forecastIndicator the forecastIndicator to set
     */
    public void setForecastIndicator(IndicatorDetail forecastIndicator) {
        this.forecastIndicator = forecastIndicator;
    }

    /**
     * @return the performanceIndicator
     */
    public IndicatorDetail getPerformanceIndicator() {
        return performanceIndicator;
    }

    /**
     * @param performanceIndicator the performanceIndicator to set
     */
    public void setPerformanceIndicator(IndicatorDetail performanceIndicator) {
        this.performanceIndicator = performanceIndicator;
    }

    /**
     * @return the targetIndicator
     */
    public IndicatorDetail getTargetIndicator() {
        return targetIndicator;
    }

    /**
     * @param targetIndicator the targetIndicator to set
     */
    public void setTargetIndicator(IndicatorDetail targetIndicator) {
        this.targetIndicator = targetIndicator;
    }

}
