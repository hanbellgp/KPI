/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C1749
 */
@Entity
@Table(name = "invamountbusinessunit")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvamountBusinessUnit.findAll", query = "SELECT i FROM InvamountBusinessUnit i")
    , @NamedQuery(name = "InvamountBusinessUnit.findByFacno", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.facno = :facno")
    , @NamedQuery(name = "InvamountBusinessUnit.findByProno", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.prono = :prono")
    , @NamedQuery(name = "InvamountBusinessUnit.findByCreyear", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.creyear = :creyear")
    , @NamedQuery(name = "InvamountBusinessUnit.findByWareh", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.wareh = :wareh")
    , @NamedQuery(name = "InvamountBusinessUnit.findByWhdsc", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.whdsc = :whdsc")
    , @NamedQuery(name = "InvamountBusinessUnit.findByCategories", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.categories = :categories")
    , @NamedQuery(name = "InvamountBusinessUnit.findByGenre", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.genre = :genre")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN01", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n01 = :n01")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN02", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n02 = :n02")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN03", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n03 = :n03")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN04", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n04 = :n04")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN05", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n05 = :n05")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN06", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n06 = :n06")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN07", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n07 = :n07")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN08", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n08 = :n08")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN09", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n09 = :n09")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN10", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n10 = :n10")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN11", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n11 = :n11")
    , @NamedQuery(name = "InvamountBusinessUnit.findByN12", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.n12 = :n12")
    , @NamedQuery(name = "InvamountBusinessUnit.findByDifference", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.difference = :difference")
    , @NamedQuery(name = "InvamountBusinessUnit.findByProportion", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.proportion = :proportion")
    , @NamedQuery(name = "InvamountBusinessUnit.findByPk", query = "SELECT i FROM InvamountBusinessUnit i WHERE i.invamountBusinessUnitPK.facno = :facno and i.invamountBusinessUnitPK.prono = :prono and i.invamountBusinessUnitPK.creyear = :creyear and "
            + "i.invamountBusinessUnitPK.wareh = :wareh and i.invamountBusinessUnitPK.whdsc = :whdsc and i.invamountBusinessUnitPK.categories = :categories and i.invamountBusinessUnitPK.genre = :genre")})
public class InvamountBusinessUnit implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected InvamountBusinessUnitPK invamountBusinessUnitPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "n01")
    private BigDecimal n01;
    @Column(name = "n02")
    private BigDecimal n02;
    @Column(name = "n03")
    private BigDecimal n03;
    @Column(name = "n04")
    private BigDecimal n04;
    @Column(name = "n05")
    private BigDecimal n05;
    @Column(name = "n06")
    private BigDecimal n06;
    @Column(name = "n07")
    private BigDecimal n07;
    @Column(name = "n08")
    private BigDecimal n08;
    @Column(name = "n09")
    private BigDecimal n09;
    @Column(name = "n10")
    private BigDecimal n10;
    @Column(name = "n11")
    private BigDecimal n11;
    @Column(name = "n12")
    private BigDecimal n12;
    @Column(name = "difference")
    private BigDecimal difference;
    @Column(name = "proportion")
    private BigDecimal proportion;

    public InvamountBusinessUnit() {
        
    }

    public InvamountBusinessUnit(InvamountBusinessUnitPK invamountBusinessUnitPK) {
        this.invamountBusinessUnitPK = invamountBusinessUnitPK;
    }

    public InvamountBusinessUnit(String facno, String prono, String creyear, String wareh, String whdsc, String categories, String genre) {
        this.invamountBusinessUnitPK = new InvamountBusinessUnitPK(facno, prono, creyear, wareh, whdsc, categories, genre);
    }

    public InvamountBusinessUnitPK getInvamountBusinessUnitPK() {
        return invamountBusinessUnitPK;
    }

    public void setInvamountBusinessUnitPK(InvamountBusinessUnitPK invamountBusinessUnitPK) {
        this.invamountBusinessUnitPK = invamountBusinessUnitPK;
    }

    public BigDecimal getN01() {
        return n01;
    }

    public void setN01(BigDecimal n01) {
        this.n01 = n01;
    }

    public BigDecimal getN02() {
        return n02;
    }

    public void setN02(BigDecimal n02) {
        this.n02 = n02;
    }

    public BigDecimal getN03() {
        return n03;
    }

    public void setN03(BigDecimal n03) {
        this.n03 = n03;
    }

    public BigDecimal getN04() {
        return n04;
    }

    public void setN04(BigDecimal n04) {
        this.n04 = n04;
    }

    public BigDecimal getN05() {
        return n05;
    }

    public void setN05(BigDecimal n05) {
        this.n05 = n05;
    }

    public BigDecimal getN06() {
        return n06;
    }

    public void setN06(BigDecimal n06) {
        this.n06 = n06;
    }

    public BigDecimal getN07() {
        return n07;
    }

    public void setN07(BigDecimal n07) {
        this.n07 = n07;
    }

    public BigDecimal getN08() {
        return n08;
    }

    public void setN08(BigDecimal n08) {
        this.n08 = n08;
    }

    public BigDecimal getN09() {
        return n09;
    }

    public void setN09(BigDecimal n09) {
        this.n09 = n09;
    }

    public BigDecimal getN10() {
        return n10;
    }

    public void setN10(BigDecimal n10) {
        this.n10 = n10;
    }

    public BigDecimal getN11() {
        return n11;
    }

    public void setN11(BigDecimal n11) {
        this.n11 = n11;
    }

    public BigDecimal getN12() {
        return n12;
    }

    public void setN12(BigDecimal n12) {
        this.n12 = n12;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getProportion() {
        return proportion;
    }

    public void setProportion(BigDecimal proportion) {
        this.proportion = proportion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (invamountBusinessUnitPK != null ? invamountBusinessUnitPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvamountBusinessUnit)) {
            return false;
        }
        InvamountBusinessUnit other = (InvamountBusinessUnit) object;
        if ((this.invamountBusinessUnitPK == null && other.invamountBusinessUnitPK != null) || (this.invamountBusinessUnitPK != null && !this.invamountBusinessUnitPK.equals(other.invamountBusinessUnitPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.InvamountBusinessUnit[ invamountBusinessUnitPK=" + invamountBusinessUnitPK + " ]";
    }
    
}
