/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "indicator")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndicatorAssignment.findAll", query = "SELECT i FROM IndicatorAssignment i")
    ,
    @NamedQuery(name = "IndicatorAssignment.findById", query = "SELECT i FROM IndicatorAssignment i WHERE i.id = :id")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByCompany", query = "SELECT i FROM IndicatorAssignment i WHERE i.company = :company")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByFormtype", query = "SELECT i FROM IndicatorAssignment i WHERE i.formtype = :formtype")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByFormkind", query = "SELECT i FROM IndicatorAssignment i WHERE i.formkind = :formkind")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByPId", query = "SELECT i FROM IndicatorAssignment i WHERE i.pid = :pid")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByPIdAndSeq", query = "SELECT i FROM IndicatorAssignment i WHERE i.pid = :pid AND i.seq = :seq")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByDeptno", query = "SELECT i FROM IndicatorAssignment i WHERE i.deptno = :deptno")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByDeptname", query = "SELECT i FROM IndicatorAssignment i WHERE i.deptname = :deptname")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByUserid", query = "SELECT i FROM IndicatorAssignment i WHERE i.userid = :userid")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByUsername", query = "SELECT i FROM IndicatorAssignment i WHERE i.username = :username")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByRemark", query = "SELECT i FROM IndicatorAssignment i WHERE i.remark = :remark")
    ,
    @NamedQuery(name = "IndicatorAssignment.findByStatus", query = "SELECT i FROM IndicatorAssignment i WHERE i.status = :status")})
public class IndicatorAssignment extends SuperDetailEntity {

    @JoinColumn(name = "formid", referencedColumnName = "formid", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private IndicatorDefine indicatorDefine;

    @JoinColumn(name = "pid", referencedColumnName = "id", updatable = false, insertable = false)
    @ManyToOne(optional = false)
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

    @JoinColumn(name = "other1", referencedColumnName = "id")
    @OneToOne(optional = true)
    private IndicatorDetail other1Indicator;
    @JoinColumn(name = "other2", referencedColumnName = "id")
    @OneToOne(optional = true)
    private IndicatorDetail other2Indicator;
    @JoinColumn(name = "other3", referencedColumnName = "id")
    @OneToOne(optional = true)
    private IndicatorDetail other3Indicator;
    @JoinColumn(name = "other4", referencedColumnName = "id")
    @OneToOne(optional = true)
    private IndicatorDetail other4Indicator;
    @JoinColumn(name = "other5", referencedColumnName = "id")
    @OneToOne(optional = true)
    private IndicatorDetail other5Indicator;
    @JoinColumn(name = "other6", referencedColumnName = "id")
    @OneToOne(optional = true)
    private IndicatorDetail other6Indicator;

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

    @Column(name = "productId")
    private int productId;
    @Size(max = 20)
    @Column(name = "product")
    private String product;
    @Column(name = "categoryId")
    private int categoryId;
    @Size(max = 20)
    @Column(name = "category")
    private String category;

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
    @Column(name = "minNum")
    private BigDecimal minNum;
    @Column(name = "maxNum")
    private BigDecimal maxNum;
    @Column(name = "limited")
    private boolean limited;
    @Column(name = "assigned")
    private boolean assigned;
    @Column(name = "freezeDate")
    @Temporal(TemporalType.DATE)
    private Date freezeDate;

    @Basic(optional = false)
    @NotNull
    @Column(name = "hasOther")
    private int hasOther;

    @Size(max = 45)
    @Column(name = "api")
    private String api;
    @Size(max = 100)
    @Column(name = "actualInterface")
    private String actualInterface;
    @Size(max = 100)
    @Column(name = "actualEJB")
    private String actualEJB;
    @Size(max = 100)
    @Column(name = "performanceInterface")
    private String performanceInterface;
    @Size(max = 100)
    @Column(name = "performanceEJB")
    private String performanceEJB;
    @Size(max = 100)
    @Column(name = "other1Interface")
    private String other1Interface;
    @Size(max = 100)
    @Column(name = "other1EJB")
    private String other1EJB;
    @Size(max = 45)
    @Column(name = "other1Label")
    private String other1Label;
    @Size(max = 100)
    @Column(name = "other2Interface")
    private String other2Interface;
    @Size(max = 100)
    @Column(name = "other2EJB")
    private String other2EJB;
    @Size(max = 45)
    @Column(name = "other2Label")
    private String other2Label;
    @Size(max = 100)
    @Column(name = "other3Interface")
    private String other3Interface;
    @Size(max = 100)
    @Column(name = "other3EJB")
    private String other3EJB;
    @Size(max = 45)
    @Column(name = "other3Label")
    private String other3Label;
    @Size(max = 100)
    @Column(name = "other4Interface")
    private String other4Interface;
    @Size(max = 100)
    @Column(name = "other4EJB")
    private String other4EJB;
    @Size(max = 45)
    @Column(name = "other4Label")
    private String other4Label;
    @Size(max = 100)
    @Column(name = "other5Interface")
    private String other5Interface;
    @Size(max = 100)
    @Column(name = "other5EJB")
    private String other5EJB;
    @Size(max = 45)
    @Column(name = "other5Label")
    private String other5Label;
    @Size(max = 100)
    @Column(name = "other6Interface")
    private String other6Interface;
    @Size(max = 100)
    @Column(name = "other6EJB")
    private String other6EJB;
    @Size(max = 45)
    @Column(name = "other6Label")
    private String other6Label;
    @Size(max = 20)
    @Column(name = "jobSchedule")
    private String jobSchedule;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "status")
    private String status;

    public IndicatorAssignment() {
        this.rate = BigDecimal.ONE;
        this.minNum = BigDecimal.ZERO;
        this.maxNum = BigDecimal.ZERO;
        this.limited = false;
        this.assigned = false;
        this.hasOther = 0;
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
     * @return the productId
     */
    public int getProductId() {
        return productId;
    }

    /**
     * @param productId the productId to set
     */
    public void setProductId(int productId) {
        this.productId = productId;
    }

    /**
     * @return the product
     */
    public String getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(String product) {
        this.product = product;
    }

    /**
     * @return the categoryId
     */
    public int getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId the categoryId to set
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
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

    /**
     * @return the hasOther
     */
    public int getHasOther() {
        return hasOther;
    }

    /**
     * @param hasOther the hasOther to set
     */
    public void setHasOther(int hasOther) {
        this.hasOther = hasOther;
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

    /**
     * @return the actualInterface
     */
    public String getActualInterface() {
        return actualInterface;
    }

    /**
     * @param actualInterface the actualInterface to set
     */
    public void setActualInterface(String actualInterface) {
        this.actualInterface = actualInterface;
    }

    /**
     * @return the actualEJB
     */
    public String getActualEJB() {
        return actualEJB;
    }

    /**
     * @param actualEJB the actualEJB to set
     */
    public void setActualEJB(String actualEJB) {
        this.actualEJB = actualEJB;
    }

    /**
     * @return the performanceInterface
     */
    public String getPerformanceInterface() {
        return performanceInterface;
    }

    /**
     * @param performanceInterface the performanceInterface to set
     */
    public void setPerformanceInterface(String performanceInterface) {
        this.performanceInterface = performanceInterface;
    }

    /**
     * @return the performanceEJB
     */
    public String getPerformanceEJB() {
        return performanceEJB;
    }

    /**
     * @param performanceEJB the performanceEJB to set
     */
    public void setPerformanceEJB(String performanceEJB) {
        this.performanceEJB = performanceEJB;
    }

    /**
     * @return the other1Interface
     */
    public String getOther1Interface() {
        return other1Interface;
    }

    /**
     * @param other1Interface the other1Interface to set
     */
    public void setOther1Interface(String other1Interface) {
        this.other1Interface = other1Interface;
    }

    /**
     * @return the other1EJB
     */
    public String getOther1EJB() {
        return other1EJB;
    }

    /**
     * @param other1EJB the other1EJB to set
     */
    public void setOther1EJB(String other1EJB) {
        this.other1EJB = other1EJB;
    }

    /**
     * @return the other1Label
     */
    public String getOther1Label() {
        return other1Label;
    }

    /**
     * @param other1Label the other1Label to set
     */
    public void setOther1Label(String other1Label) {
        this.other1Label = other1Label;
    }

    /**
     * @return the other2Interface
     */
    public String getOther2Interface() {
        return other2Interface;
    }

    /**
     * @param other2Interface the other2Interface to set
     */
    public void setOther2Interface(String other2Interface) {
        this.other2Interface = other2Interface;
    }

    /**
     * @return the other2EJB
     */
    public String getOther2EJB() {
        return other2EJB;
    }

    /**
     * @param other2EJB the other2EJB to set
     */
    public void setOther2EJB(String other2EJB) {
        this.other2EJB = other2EJB;
    }

    /**
     * @return the other2Label
     */
    public String getOther2Label() {
        return other2Label;
    }

    /**
     * @param other2Label the other2Label to set
     */
    public void setOther2Label(String other2Label) {
        this.other2Label = other2Label;
    }

    /**
     * @return the other3Interface
     */
    public String getOther3Interface() {
        return other3Interface;
    }

    /**
     * @param other3Interface the other3Interface to set
     */
    public void setOther3Interface(String other3Interface) {
        this.other3Interface = other3Interface;
    }

    /**
     * @return the other3EJB
     */
    public String getOther3EJB() {
        return other3EJB;
    }

    /**
     * @param other3EJB the other3EJB to set
     */
    public void setOther3EJB(String other3EJB) {
        this.other3EJB = other3EJB;
    }

    /**
     * @return the other3Label
     */
    public String getOther3Label() {
        return other3Label;
    }

    /**
     * @param other3Label the other3Label to set
     */
    public void setOther3Label(String other3Label) {
        this.other3Label = other3Label;
    }

    /**
     * @return the other4Interface
     */
    public String getOther4Interface() {
        return other4Interface;
    }

    /**
     * @param other4Interface the other4Interface to set
     */
    public void setOther4Interface(String other4Interface) {
        this.other4Interface = other4Interface;
    }

    /**
     * @return the other4EJB
     */
    public String getOther4EJB() {
        return other4EJB;
    }

    /**
     * @param other4EJB the other4EJB to set
     */
    public void setOther4EJB(String other4EJB) {
        this.other4EJB = other4EJB;
    }

    /**
     * @return the other4Label
     */
    public String getOther4Label() {
        return other4Label;
    }

    /**
     * @param other4Label the other4Label to set
     */
    public void setOther4Label(String other4Label) {
        this.other4Label = other4Label;
    }

    /**
     * @return the other5Interface
     */
    public String getOther5Interface() {
        return other5Interface;
    }

    /**
     * @param other5Interface the other5Interface to set
     */
    public void setOther5Interface(String other5Interface) {
        this.other5Interface = other5Interface;
    }

    /**
     * @return the other5EJB
     */
    public String getOther5EJB() {
        return other5EJB;
    }

    /**
     * @param other5EJB the other5EJB to set
     */
    public void setOther5EJB(String other5EJB) {
        this.other5EJB = other5EJB;
    }

    /**
     * @return the other5Label
     */
    public String getOther5Label() {
        return other5Label;
    }

    /**
     * @param other5Label the other5Label to set
     */
    public void setOther5Label(String other5Label) {
        this.other5Label = other5Label;
    }

    /**
     * @return the other6Interface
     */
    public String getOther6Interface() {
        return other6Interface;
    }

    /**
     * @param other6Interface the other6Interface to set
     */
    public void setOther6Interface(String other6Interface) {
        this.other6Interface = other6Interface;
    }

    /**
     * @return the other6EJB
     */
    public String getOther6EJB() {
        return other6EJB;
    }

    /**
     * @param other6EJB the other6EJB to set
     */
    public void setOther6EJB(String other6EJB) {
        this.other6EJB = other6EJB;
    }

    /**
     * @return the other6Label
     */
    public String getOther6Label() {
        return other6Label;
    }

    /**
     * @param other6Label the other6Label to set
     */
    public void setOther6Label(String other6Label) {
        this.other6Label = other6Label;
    }

    /**
     * @return the jobSchedule
     */
    public String getJobSchedule() {
        return jobSchedule;
    }

    /**
     * @param jobSchedule the jobSchedule to set
     */
    public void setJobSchedule(String jobSchedule) {
        this.jobSchedule = jobSchedule;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the minNum
     */
    public BigDecimal getMinNum() {
        return minNum;
    }

    /**
     * @param minNum the minNum to set
     */
    public void setMinNum(BigDecimal minNum) {
        this.minNum = minNum;
    }

    /**
     * @return the maxNum
     */
    public BigDecimal getMaxNum() {
        return maxNum;
    }

    /**
     * @param maxNum the maxNum to set
     */
    public void setMaxNum(BigDecimal maxNum) {
        this.maxNum = maxNum;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof IndicatorAssignment)) {
            return false;
        }
        IndicatorAssignment other = (IndicatorAssignment) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        if (Objects.equals(this.pid, other.pid)) {
            return Objects.equals(this.formid, other.formid) && Objects.equals(this.deptno, other.deptno);
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

    /**
     * @return the other1Indicator
     */
    public IndicatorDetail getOther1Indicator() {
        return other1Indicator;
    }

    /**
     * @param other1Indicator the other1Indicator to set
     */
    public void setOther1Indicator(IndicatorDetail other1Indicator) {
        this.other1Indicator = other1Indicator;
    }

    /**
     * @return the other2Indicator
     */
    public IndicatorDetail getOther2Indicator() {
        return other2Indicator;
    }

    /**
     * @param other2Indicator the other2Indicator to set
     */
    public void setOther2Indicator(IndicatorDetail other2Indicator) {
        this.other2Indicator = other2Indicator;
    }

    /**
     * @return the other3Indicator
     */
    public IndicatorDetail getOther3Indicator() {
        return other3Indicator;
    }

    /**
     * @param other3Indicator the other3Indicator to set
     */
    public void setOther3Indicator(IndicatorDetail other3Indicator) {
        this.other3Indicator = other3Indicator;
    }

    /**
     * @return the other4Indicator
     */
    public IndicatorDetail getOther4Indicator() {
        return other4Indicator;
    }

    /**
     * @param other4Indicator the other4Indicator to set
     */
    public void setOther4Indicator(IndicatorDetail other4Indicator) {
        this.other4Indicator = other4Indicator;
    }

    /**
     * @return the other5Indicator
     */
    public IndicatorDetail getOther5Indicator() {
        return other5Indicator;
    }

    /**
     * @param other5Indicator the other5Indicator to set
     */
    public void setOther5Indicator(IndicatorDetail other5Indicator) {
        this.other5Indicator = other5Indicator;
    }

    /**
     * @return the other6Indicator
     */
    public IndicatorDetail getOther6Indicator() {
        return other6Indicator;
    }

    /**
     * @param other6Indicator the other6Indicator to set
     */
    public void setOther6Indicator(IndicatorDetail other6Indicator) {
        this.other6Indicator = other6Indicator;
    }

}
