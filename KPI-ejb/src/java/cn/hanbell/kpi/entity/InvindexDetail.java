/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "invindexdetail")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InvindexDetail.findAll", query = "SELECT i FROM InvindexDetail i"),
    @NamedQuery(name = "InvindexDetail.findById", query = "SELECT i FROM InvindexDetail i WHERE i.id = :id"),
    @NamedQuery(name = "InvindexDetail.findByPId", query = "SELECT i FROM InvindexDetail i WHERE i.pid = :pid"),
    @NamedQuery(name = "InvindexDetail.findBySeq", query = "SELECT i FROM InvindexDetail i WHERE i.seq = :seq"),
    @NamedQuery(name = "InvindexDetail.findByIndno", query = "SELECT i FROM InvindexDetail i WHERE i.indno = :indno"),
    @NamedQuery(name = "InvindexDetail.findByGenerno", query = "SELECT i FROM InvindexDetail i WHERE i.generno = :generno"),
    @NamedQuery(name = "InvindexDetail.findByWhdsc", query = "SELECT i FROM InvindexDetail i WHERE i.whdsc = :whdsc"),
    @NamedQuery(name = "InvindexDetail.findByWareh", query = "SELECT i FROM InvindexDetail i WHERE i.wareh = :wareh")})
public class InvindexDetail extends SuperDetailEntity {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5)
    @Column(name = "indno")
    private String indno;
    @Size(max = 20)
    @Column(name = "generno")
    private String generno;
    @Size(max = 20)
    @Column(name = "whdsc")
    private String whdsc;
    @Size(max = 20)
    @Column(name = "wareh")
    private String wareh;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sort")
    private int sort;

    public InvindexDetail() {
    }

    public InvindexDetail(Integer id) {
        this.id = id;
    }

    public InvindexDetail(Integer id, int seq, String indno) {
        this.id = id;
        this.seq = seq;
        this.indno = indno;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getWhdsc() {
        return whdsc;
    }

    public void setWhdsc(String whdsc) {
        this.whdsc = whdsc;
    }

    public String getWareh() {
        return wareh;
    }

    public void setWareh(String wareh) {
        this.wareh = wareh;
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
        if (!(object instanceof InvindexDetail)) {
            return false;
        }
        InvindexDetail other = (InvindexDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.InvindexDetail[ id=" + id + " ]";
    }
    
}
