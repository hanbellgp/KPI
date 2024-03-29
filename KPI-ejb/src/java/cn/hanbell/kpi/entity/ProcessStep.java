/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.FormEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "processstep")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "ProcessStep.findAll", query = "SELECT p FROM ProcessStep p"),
    @NamedQuery(name = "ProcessStep.findById", query = "SELECT p FROM ProcessStep p WHERE p.id = :id"),
    @NamedQuery(name = "ProcessStep.findByItemno", query = "SELECT p FROM ProcessStep p WHERE p.itemno = :itemno"),
    @NamedQuery(name = "ProcessStep.findByManno", query = "SELECT p FROM ProcessStep p WHERE p.manno = :manno"),
    @NamedQuery(name = "ProcessStep.findByComponent",
        query = "SELECT p FROM ProcessStep p WHERE p.component = :component"),
    @NamedQuery(name = "ProcessStep.findByEquipment",
        query = "SELECT p FROM ProcessStep p WHERE p.equipment = :equipment"),
    @NamedQuery(name = "ProcessStep.findByStep", query = "SELECT p FROM ProcessStep p WHERE p.step = :step"),
    @NamedQuery(name = "ProcessStep.findByStatus", query = "SELECT p FROM ProcessStep p WHERE p.status = :status")})
public class ProcessStep extends FormEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "manno")
    private String manno;
    @Size(max = 45)
    @Column(name = "component")
    private String component;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "itemno")
    private String itemno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "step")
    private String step;
    @Size(max = 45)
    @Column(name = "stepName")
    private String stepName;
    @Column(name = "stepSeq")
    private Integer stepSeq;
    @Size(max = 45)
    @Column(name = "equipment")
    private String equipment;
    @Column(name = "startTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Column(name = "endTime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "processingTime")
    private BigDecimal processingTime;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "qty")
    private BigDecimal qty;
    @Size(max = 45)
    @Column(name = "rule")
    private String rule;
    @Size(max = 20)
    @Column(name = "userid")
    private String userid;
    @Size(max = 45)
    @Column(name = "user")
    private String user;
    @Column(name = "standardMachineTime")
    private BigDecimal standardMachineTime;
    @Column(name = "standardLaborTime")
    private BigDecimal standardLaborTime;
    @Column(name = "totalMachineTime")
    private BigDecimal totalMachineTime;
    @Column(name = "totalLaborTime")
    private BigDecimal totalLaborTime;
    @Column(name = "standardMachineCost")
    private BigDecimal standardMachineCost;
    @Column(name = "standardLaborCost")
    private BigDecimal standardLaborCost;
    @Column(name = "materialCost")
    private BigDecimal materialCost;
    @Column(name = "machineCost")
    private BigDecimal machineCost;
    @Column(name = "laborCost")
    private BigDecimal laborCost;
    @Column(name = "manufacturingExpenses")
    private BigDecimal manufacturingExpenses;
    @Column(name = "standCost")
    private BigDecimal standCost;
    @Column(name = "materialPrice")
    private BigDecimal materialPrice;
    @Column(name = "processingPrice")
    private BigDecimal processingPrice;
    @Column(name = "materialAmount")
    private BigDecimal materialAmount;
    @Column(name = "processingAmount")
    private BigDecimal processingAmount;

    @Transient
    private String product;
    
    public ProcessStep() {
        this.totalMachineTime = BigDecimal.ZERO;
        this.totalLaborTime = BigDecimal.ZERO;
        this.standardMachineCost = BigDecimal.ZERO;
        this.standardLaborCost = BigDecimal.ZERO;
        this.materialCost = BigDecimal.ZERO;
        this.machineCost = BigDecimal.ZERO;
        this.laborCost = BigDecimal.ZERO;
        this.manufacturingExpenses = BigDecimal.ZERO;
        this.standCost = BigDecimal.ZERO;
        this.materialPrice = BigDecimal.ZERO;
        this.processingPrice = BigDecimal.ZERO;
        this.materialAmount = BigDecimal.ZERO;
        this.processingAmount = BigDecimal.ZERO;
        this.status = "N";
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
    public String getManno() {
        return manno;
    }

    public void setManno(String manno) {
        this.manno = manno;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Integer getStepSeq() {
        return stepSeq;
    }

    public void setStepSeq(Integer stepSeq) {
        this.stepSeq = stepSeq;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(BigDecimal processingTime) {
        this.processingTime = processingTime;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public BigDecimal getStandardMachineTime() {
        return standardMachineTime;
    }

    public void setStandardMachineTime(BigDecimal standardMachineTime) {
        this.standardMachineTime = standardMachineTime;
    }

    public BigDecimal getStandardLaborTime() {
        return standardLaborTime;
    }

    public void setStandardLaborTime(BigDecimal standardLaborTime) {
        this.standardLaborTime = standardLaborTime;
    }

    /**
     * @return the totalMachineTime
     */
    public BigDecimal getTotalMachineTime() {
        return totalMachineTime;
    }

    /**
     * @param totalMachineTime
     *                             the totalMachineTime to set
     */
    public void setTotalMachineTime(BigDecimal totalMachineTime) {
        this.totalMachineTime = totalMachineTime;
    }

    /**
     * @return the totalLaborTime
     */
    public BigDecimal getTotalLaborTime() {
        return totalLaborTime;
    }

    /**
     * @param totalLaborTime
     *                           the totalLaborTime to set
     */
    public void setTotalLaborTime(BigDecimal totalLaborTime) {
        this.totalLaborTime = totalLaborTime;
    }

    public BigDecimal getStandardMachineCost() {
        return standardMachineCost;
    }

    public void setStandardMachineCost(BigDecimal standardMachineCost) {
        this.standardMachineCost = standardMachineCost;
    }

    public BigDecimal getStandardLaborCost() {
        return standardLaborCost;
    }

    public void setStandardLaborCost(BigDecimal standardLaborCost) {
        this.standardLaborCost = standardLaborCost;
    }

    public BigDecimal getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(BigDecimal materialCost) {
        this.materialCost = materialCost;
    }

    public BigDecimal getMachineCost() {
        return machineCost;
    }

    public void setMachineCost(BigDecimal machineCost) {
        this.machineCost = machineCost;
    }

    public BigDecimal getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(BigDecimal laborCost) {
        this.laborCost = laborCost;
    }

    public BigDecimal getManufacturingExpenses() {
        return manufacturingExpenses;
    }

    public void setManufacturingExpenses(BigDecimal manufacturingExpenses) {
        this.manufacturingExpenses = manufacturingExpenses;
    }

    public BigDecimal getStandCost() {
        return standCost;
    }

    public void setStandCost(BigDecimal standCost) {
        this.standCost = standCost;
    }

    /**
     * @return the materialPrice
     */
    public BigDecimal getMaterialPrice() {
        return materialPrice;
    }

    /**
     * @param materialPrice
     *                          the materialPrice to set
     */
    public void setMaterialPrice(BigDecimal materialPrice) {
        this.materialPrice = materialPrice;
    }

    /**
     * @return the processingPrice
     */
    public BigDecimal getProcessingPrice() {
        return processingPrice;
    }

    /**
     * @param processingPrice
     *                            the processingPrice to set
     */
    public void setProcessingPrice(BigDecimal processingPrice) {
        this.processingPrice = processingPrice;
    }

    /**
     * @return the materialAmount
     */
    public BigDecimal getMaterialAmount() {
        return materialAmount;
    }

    /**
     * @param materialAmount
     *                           the materialAmount to set
     */
    public void setMaterialAmount(BigDecimal materialAmount) {
        this.materialAmount = materialAmount;
    }

    /**
     * @return the processingAmount
     */
    public BigDecimal getProcessingAmount() {
        return processingAmount;
    }

    /**
     * @param processingAmount
     *                             the processingAmount to set
     */
    public void setProcessingAmount(BigDecimal processingAmount) {
        this.processingAmount = processingAmount;
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
        if (!(object instanceof ProcessStep)) {
            return false;
        }
        ProcessStep other = (ProcessStep)object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ProcessStep[ id=" + id + " ]";
    }

}
