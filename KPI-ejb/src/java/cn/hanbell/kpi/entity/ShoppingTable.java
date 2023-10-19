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
 * @author C2082
 */
@Entity
@Table(name = "shoppingtable")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ShoppingTable.findAll", query = "SELECT s FROM ShoppingTable s"),
    @NamedQuery(name = "ShoppingTable.findById", query = "SELECT s FROM ShoppingTable s WHERE s.id = :id"),
    @NamedQuery(name = "ShoppingTable.findByFacno", query = "SELECT s FROM ShoppingTable s WHERE s.facno = :facno"),
    @NamedQuery(name = "ShoppingTable.findByYearmon", query = "SELECT s FROM ShoppingTable s WHERE s.yearmon = :yearmon"),
    @NamedQuery(name = "ShoppingTable.findByItnbr", query = "SELECT s FROM ShoppingTable s WHERE s.itnbr = :itnbr"),
    @NamedQuery(name = "ShoppingTable.findByItdsc", query = "SELECT s FROM ShoppingTable s WHERE s.itdsc = :itdsc"),
    @NamedQuery(name = "ShoppingTable.findByItcls", query = "SELECT s FROM ShoppingTable s WHERE s.itcls = :itcls"),
    @NamedQuery(name = "ShoppingTable.findByVdrno", query = "SELECT s FROM ShoppingTable s WHERE s.vdrno = :vdrno"),
    @NamedQuery(name = "ShoppingTable.findByVdrna", query = "SELECT s FROM ShoppingTable s WHERE s.vdrna = :vdrna"),
    @NamedQuery(name = "ShoppingTable.findByAcpamt", query = "SELECT s FROM ShoppingTable s WHERE s.acpamt = :acpamt"),
    @NamedQuery(name = "ShoppingTable.findByStatus", query = "SELECT s FROM ShoppingTable s WHERE s.status = :status"),
    @NamedQuery(name = "ShoppingTable.findByCreator", query = "SELECT s FROM ShoppingTable s WHERE s.creator = :creator"),
    @NamedQuery(name = "ShoppingTable.findByCredate", query = "SELECT s FROM ShoppingTable s WHERE s.credate = :credate"),
    @NamedQuery(name = "ShoppingTable.findByOptuser", query = "SELECT s FROM ShoppingTable s WHERE s.optuser = :optuser"),
    @NamedQuery(name = "ShoppingTable.findByOptdate", query = "SELECT s FROM ShoppingTable s WHERE s.optdate = :optdate"),
    @NamedQuery(name = "ShoppingTable.findByCfmuser", query = "SELECT s FROM ShoppingTable s WHERE s.cfmuser = :cfmuser"),
    @NamedQuery(name = "ShoppingTable.findByCfmdate", query = "SELECT s FROM ShoppingTable s WHERE s.cfmdate = :cfmdate")})
public class ShoppingTable extends SuperEntity {

    private static final long serialVersionUID = 1L;

    @Size(max = 50)
    @Column(name = "yearmon")
    private String yearmon;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "facno")
    private String facno;
    @Size(max = 50)
    @Column(name = "itnbr")
    private String itnbr;
    @Size(max = 50)
    @Column(name = "itdsc")
    private String itdsc;
    @Size(max = 50)
    @Column(name = "itcls")
    private String itcls;
    @Size(max = 50)
    @Column(name = "vdrno")
    private String vdrno;
    @Size(max = 20)
    @Column(name = "type")
    private String type;
    @Size(max = 50)
    @Column(name = "userno")
    private String userno;
    @Size(max = 50)
    @Column(name = "acpno")
    private String acpno;
    @Size(max = 50)
    @Column(name = "sponr")
    private String sponr;
    @Size(max = 50)
    @Column(name = "vdrna")
    private String vdrna;
    @Column(name = "iscenter")
    private Boolean iscenter;
    @Column(name = "acpamt")
    private BigDecimal acpamt;
    @Column(name = "payqty")
    private BigDecimal payqty;

    public ShoppingTable() {
    }

    public ShoppingTable(Integer id) {
        this.id = id;
    }

    public ShoppingTable(Integer id, String facno) {
        this.id = id;
        this.facno = facno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getItnbr() {
        return itnbr;
    }

    public void setItnbr(String itnbr) {
        this.itnbr = itnbr;
    }

    public String getItdsc() {
        return itdsc;
    }

    public void setItdsc(String itdsc) {
        this.itdsc = itdsc;
    }

    public String getItcls() {
        return itcls;
    }

    public void setItcls(String itcls) {
        this.itcls = itcls;
    }

    public String getVdrno() {
        return vdrno;
    }

    public void setVdrno(String vdrno) {
        this.vdrno = vdrno;
    }

    public String getVdrna() {
        return vdrna;
    }

    public void setVdrna(String vdrna) {
        this.vdrna = vdrna;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIscenter() {
        return iscenter;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public void setIscenter(Boolean iscenter) {
        this.iscenter = iscenter;
    }

    public BigDecimal getAcpamt() {
        return acpamt;
    }

    public void setAcpamt(BigDecimal acpamt) {
        this.acpamt = acpamt;
    }

    public BigDecimal getPayqty() {
        return payqty;
    }

    public void setPayqty(BigDecimal payqty) {
        this.payqty = payqty;
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

    public String getYearmon() {
        return yearmon;
    }

    public void setYearmon(String yearmon) {
        this.yearmon = yearmon;
    }

    public String getAcpno() {
        return acpno;
    }

    public void setAcpno(String acpno) {
        this.acpno = acpno;
    }

    public String getSponr() {
        return sponr;
    }

    public void setSponr(String sponr) {
        this.sponr = sponr;
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
        if (!(object instanceof ShoppingTable)) {
            return false;
        }
        ShoppingTable other = (ShoppingTable) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ShoppingTable[ id=" + id + " ]";
    }

}
