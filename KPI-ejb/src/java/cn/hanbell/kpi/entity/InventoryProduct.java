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
 * @author C1749
 */
@Entity
@Table(name = "inventoryproduct")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventoryProduct.findAll", query = "SELECT i FROM InventoryProduct i"),
    @NamedQuery(name = "InventoryProduct.findById", query = "SELECT i FROM InventoryProduct i WHERE i.id = :id"),
    @NamedQuery(name = "InventoryProduct.findByFacno", query = "SELECT i FROM InventoryProduct i WHERE i.facno = :facno"),
    @NamedQuery(name = "InventoryProduct.findByYearmon", query = "SELECT i FROM InventoryProduct i WHERE i.yearmon = :yearmon"),
    @NamedQuery(name = "InventoryProduct.findByWareh", query = "SELECT i FROM InventoryProduct i WHERE i.wareh = :wareh"),
    @NamedQuery(name = "InventoryProduct.findByWhdsc", query = "SELECT i FROM InventoryProduct i WHERE i.whdsc = :whdsc"),
    @NamedQuery(name = "InventoryProduct.findByGenre", query = "SELECT i FROM InventoryProduct i WHERE i.genre = :genre"),
    @NamedQuery(name = "InventoryProduct.findByTrtype", query = "SELECT i FROM InventoryProduct i WHERE i.trtype = :trtype"),
    @NamedQuery(name = "InventoryProduct.findByDeptno", query = "SELECT i FROM InventoryProduct i WHERE i.deptno = :deptno"),
    @NamedQuery(name = "InventoryProduct.findByItclscode", query = "SELECT i FROM InventoryProduct i WHERE i.itclscode = :itclscode"),
    @NamedQuery(name = "InventoryProduct.findByCategories", query = "SELECT i FROM InventoryProduct i WHERE i.categories = :categories"),
    @NamedQuery(name = "InventoryProduct.findByIndicatorno", query = "SELECT i FROM InventoryProduct i WHERE i.indicatorno = :indicatorno"),
    @NamedQuery(name = "InventoryProduct.findByAmount", query = "SELECT i FROM InventoryProduct i WHERE i.amount = :amount"),
    @NamedQuery(name = "InventoryProduct.findByAmamount", query = "SELECT i FROM InventoryProduct i WHERE i.amamount = :amamount"),
    @NamedQuery(name = "InventoryProduct.findByStatus", query = "SELECT i FROM InventoryProduct i WHERE i.status = :status"),
    @NamedQuery(name = "InventoryProduct.findByCreator", query = "SELECT i FROM InventoryProduct i WHERE i.creator = :creator"),
    @NamedQuery(name = "InventoryProduct.findByCredate", query = "SELECT i FROM InventoryProduct i WHERE i.credate = :credate"),
    @NamedQuery(name = "InventoryProduct.findByOptuser", query = "SELECT i FROM InventoryProduct i WHERE i.optuser = :optuser"),
    @NamedQuery(name = "InventoryProduct.findByOptdate", query = "SELECT i FROM InventoryProduct i WHERE i.optdate = :optdate"),
    @NamedQuery(name = "InventoryProduct.findByCfmuser", query = "SELECT i FROM InventoryProduct i WHERE i.cfmuser = :cfmuser"),
    @NamedQuery(name = "InventoryProduct.findByCfmdate", query = "SELECT i FROM InventoryProduct i WHERE i.cfmdate = :cfmdate"),
    @NamedQuery(name = "InventoryProduct.findByUnique", query = "SELECT i FROM InventoryProduct i WHERE i.yearmon = :yearmon "
            + " and i.whdsc = :whdsc and i.genre = :genre and i.trtype = :trtype and i.itclscode = :itclscode and i.categories = :categories"),
    @NamedQuery(name = "InventoryProduct.findYearmon", query = "SELECT i FROM InventoryProduct i WHERE i.yearmon = :yearmon"),
    @NamedQuery(name = "InventoryProduct.findByFacnoAndYearmon", query = "SELECT i FROM InventoryProduct i WHERE i.facno = :facno and i.yearmon = :yearmon"),
    @NamedQuery(name = "InventoryProduct.findByEditRow", query = "SELECT i FROM InventoryProduct i WHERE i.facno = :facno and i.yearmon = :yearmon"
            + " and i.wareh = :wareh and i.itclscode = :itclscode and i.genre = :genre"),
    @NamedQuery(name = "InventoryProduct.findByFacnoAndYearmonAndCategories", query = "SELECT i FROM InventoryProduct i WHERE i.facno = :facno and i.yearmon = :yearmon and i.categories = :categories")})
public class InventoryProduct extends SuperEntity implements Cloneable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull()
    @Size(min = 1, max = 6)
    @Column(name = "yearmon")
    private String yearmon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "wareh")
    private String wareh;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "whdsc")
    private String whdsc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "genre")
    private String genre;
    @Size(max = 45)
    @Column(name = "trtype")
    private String trtype;
    @Size(max = 45)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 2)
    @Column(name = "itclscode")
    private String itclscode;
    @Size(max = 45)
    @Column(name = "categories")
    private String categories;
    @Size(max = 45)
    @Column(name = "indicatorno")
    private String indicatorno;
    @Column(name = "editamount")
    private BigDecimal editamount;
    @Size(max = 2)
    @Column(name = "status")
    private String status;
    @Size(max = 20)
    @Column(name = "creator")
    private String creator;
    @Size(max = 45)
    @Column(name = "optuser")
    private String optuser;
    @Size(max = 45)
    @Column(name = "cfmuser")
    private String cfmuser;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    // @Max(value=?) @Min(value=?)//if you know range of your decimal fields
    // consider using these annotations to enforce field validation
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "amamount")
    private BigDecimal amamount;
    @Column(name = "credate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credate;
    @Column(name = "optdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date optdate;
    @Column(name = "cfmdate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cfmdate;

    public InventoryProduct() {
    }

    public InventoryProduct(Integer id) {
        this.id = id;
    }

    public InventoryProduct(Integer id, String facno, String yearmon, String wareh, String whdsc, String genre) {
        this.id = id;
        this.facno = facno;
        this.yearmon = yearmon;
        this.wareh = wareh;
        this.whdsc = whdsc;
        this.genre = genre;
        this.amount = BigDecimal.ZERO;
        this.amamount = BigDecimal.ZERO;
        this.editamount = BigDecimal.ZERO;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Double getDoubleAmount() {
        return this.amamount.add(this.amount).doubleValue();
    }

    public BigDecimal getAmamount() {
        return amamount;
    }

    public void setAmamount(BigDecimal amamount) {
        this.amamount = amamount;
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
        if (!(object instanceof InventoryProduct)) {
            return false;
        }
        InventoryProduct other = (InventoryProduct) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.InventoryProduct[ id=" + id + " ]";
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getYearmon() {
        return yearmon;
    }

    public void setYearmon(String yearmon) {
        this.yearmon = yearmon;
    }

    public String getWareh() {
        return wareh;
    }

    public void setWareh(String wareh) {
        this.wareh = wareh;
    }

    public String getWhdsc() {
        return whdsc;
    }

    public void setWhdsc(String whdsc) {
        this.whdsc = whdsc;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTrtype() {
        return trtype;
    }

    public void setTrtype(String trtype) {
        this.trtype = trtype;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getItclscode() {
        return itclscode;
    }

    public void setItclscode(String itclscode) {
        this.itclscode = itclscode;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getIndicatorno() {
        return indicatorno;
    }

    public void setIndicatorno(String indicatorno) {
        this.indicatorno = indicatorno;
    }

    public BigDecimal getEditamount() {
        return editamount;
    }

    public void setEditamount(BigDecimal editamount) {
        this.editamount = editamount;
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

    public Double getSumAmount() {
        return this.getAmamount().doubleValue()+this.getAmount().doubleValue();
    }
}
