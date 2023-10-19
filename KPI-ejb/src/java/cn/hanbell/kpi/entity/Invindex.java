/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.BaseEntity;
import com.lightshell.comm.SuperEntity;
import java.io.Serializable;
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
@Table(name = "invindex")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Invindex.findAll", query = "SELECT i FROM Invindex i"),
    @NamedQuery(name = "Invindex.findById", query = "SELECT i FROM Invindex i WHERE i.id = :id"),
    @NamedQuery(name = "Invindex.findByIndno", query = "SELECT i FROM Invindex i WHERE i.indno = :indno"),
    @NamedQuery(name = "Invindex.findByGenerno", query = "SELECT i FROM Invindex i WHERE i.generno = :generno"),
    @NamedQuery(name = "Invindex.findByGenerna", query = "SELECT i FROM Invindex i WHERE i.generna = :generna"),
    @NamedQuery(name = "Invindex.findByGenzls", query = "SELECT i FROM Invindex i WHERE i.genzls = :genzls"),
    @NamedQuery(name = "Invindex.findByFormid", query = "SELECT i FROM Invindex i WHERE i.formid = :formid"),
    @NamedQuery(name = "Invindex.findByRemark", query = "SELECT i FROM Invindex i WHERE i.remark = :remark"),
    @NamedQuery(name = "Invindex.findByStatus", query = "SELECT i FROM Invindex i WHERE i.status = :status"),
    @NamedQuery(name = "Invindex.findByCreator", query = "SELECT i FROM Invindex i WHERE i.creator = :creator"),
    @NamedQuery(name = "Invindex.findByCredate", query = "SELECT i FROM Invindex i WHERE i.credate = :credate"),
    @NamedQuery(name = "Invindex.findByOptuser", query = "SELECT i FROM Invindex i WHERE i.optuser = :optuser"),
    @NamedQuery(name = "Invindex.findByOptdate", query = "SELECT i FROM Invindex i WHERE i.optdate = :optdate"),
    @NamedQuery(name = "Invindex.findByCfmuser", query = "SELECT i FROM Invindex i WHERE i.cfmuser = :cfmuser"),
    @NamedQuery(name = "Invindex.findByCfmdate", query = "SELECT i FROM Invindex i WHERE i.cfmdate = :cfmdate"),
    @NamedQuery(name = "Invindex.findByGenernoAndFormid", query = "SELECT i FROM Invindex i WHERE  i.formid = :formid and i.generno = :generno")})
public class Invindex extends SuperEntity {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "facno")
    private String facno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "indno")
    private String indno;
    @Size(max = 20)
    @Column(name = "generno")
    private String generno;
    @Size(max = 20)
    @Column(name = "generna")
    private String generna;
    @Size(max = 20)
    @Column(name = "genzls")
    private String genzls;
    @Size(max = 200)
    @Column(name = "formid")
    private String formid;
    @Size(max = 20)
    @Column(name = "remark")
    private String remark;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sort")
    private int sort;

    public Invindex() {
    }

    public Invindex(Integer id) {
        this.id = id;
    }

    public Invindex(Integer id, String indno) {
        this.id = id;
        this.indno = indno;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public String getIndno() {
        return indno;
    }

    public void setIndno(String indno) {
        this.indno = indno;
    }

    public String getGenerno() {
        return generno;
    }

    public void setGenerno(String generno) {
        this.generno = generno;
    }

    public String getGenerna() {
        return generna;
    }

    public void setGenerna(String generna) {
        this.generna = generna;
    }

    public String getGenzls() {
        return genzls;
    }

    public void setGenzls(String genzls) {
        this.genzls = genzls;
    }

    public String getFormid() {
        return formid;
    }

    public void setFormid(String formid) {
        this.formid = formid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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
        if (!(object instanceof Invindex)) {
            return false;
        }
        Invindex other = (Invindex) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Invindex[ id=" + id + " ]";
    }

}
