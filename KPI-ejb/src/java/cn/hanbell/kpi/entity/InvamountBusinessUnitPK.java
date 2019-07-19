/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author C1749
 */
@Embeddable
public class InvamountBusinessUnitPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "prono")
    private String prono;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "creyear")
    private String creyear;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "wareh")
    private String wareh;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "whdsc")
    private String whdsc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "categories")
    private String categories;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "genre")
    private String genre;

    public InvamountBusinessUnitPK() {
    }

    public InvamountBusinessUnitPK(String whdsc){
        this.whdsc = whdsc;
    }
    public InvamountBusinessUnitPK(String facno, String prono, String creyear, String wareh, String whdsc, String categories, String genre) {
        this.facno = facno;
        this.prono = prono;
        this.creyear = creyear;
        this.wareh = wareh;
        this.whdsc = whdsc;
        this.categories = categories;
        this.genre = genre;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getProno() {
        return prono;
    }

    public void setProno(String prono) {
        this.prono = prono;
    }

    public String getCreyear() {
        return creyear;
    }

    public void setCreyear(String creyear) {
        this.creyear = creyear;
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

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (facno != null ? facno.hashCode() : 0);
        hash += (prono != null ? prono.hashCode() : 0);
        hash += (creyear != null ? creyear.hashCode() : 0);
        hash += (wareh != null ? wareh.hashCode() : 0);
        hash += (whdsc != null ? whdsc.hashCode() : 0);
        hash += (categories != null ? categories.hashCode() : 0);
        hash += (genre != null ? genre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof InvamountBusinessUnitPK)) {
            return false;
        }
        InvamountBusinessUnitPK other = (InvamountBusinessUnitPK) object;
        if ((this.facno == null && other.facno != null) || (this.facno != null && !this.facno.equals(other.facno))) {
            return false;
        }
        if ((this.prono == null && other.prono != null) || (this.prono != null && !this.prono.equals(other.prono))) {
            return false;
        }
        if ((this.creyear == null && other.creyear != null) || (this.creyear != null && !this.creyear.equals(other.creyear))) {
            return false;
        }
        if ((this.wareh == null && other.wareh != null) || (this.wareh != null && !this.wareh.equals(other.wareh))) {
            return false;
        }
        if ((this.whdsc == null && other.whdsc != null) || (this.whdsc != null && !this.whdsc.equals(other.whdsc))) {
            return false;
        }
        if ((this.categories == null && other.categories != null) || (this.categories != null && !this.categories.equals(other.categories))) {
            return false;
        }
        if ((this.genre == null && other.genre != null) || (this.genre != null && !this.genre.equals(other.genre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.InvamountBusinessUnitPK[ facno=" + facno + ", prono=" + prono + ", creyear=" + creyear + ", wareh=" + wareh + ", whdsc=" + whdsc + ", categories=" + categories + ", genre=" + genre + " ]";
    }
    
}
