/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

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
@Table(name = "indexwareh")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "IndexWareh.findAll", query = "SELECT i FROM IndexWareh i"),
    @NamedQuery(name = "IndexWareh.findById", query = "SELECT i FROM IndexWareh i WHERE i.id = :id"),
    @NamedQuery(name = "IndexWareh.findByWareh", query = "SELECT i FROM IndexWareh i WHERE i.wareh = :wareh"),
    @NamedQuery(name = "IndexWareh.findByWhdsc", query = "SELECT i FROM IndexWareh i WHERE i.whdsc = :whdsc"),
    @NamedQuery(name = "IndexWareh.findByGenerno", query = "SELECT i FROM IndexWareh i WHERE i.generno = :generno order by i.sort asc"),
    @NamedQuery(name = "IndexWareh.findByStatus", query = "SELECT i FROM IndexWareh i WHERE i.status = :status"),
    @NamedQuery(name = "IndexWareh.findByCreator", query = "SELECT i FROM IndexWareh i WHERE i.creator = :creator"),
    @NamedQuery(name = "IndexWareh.findByCredate", query = "SELECT i FROM IndexWareh i WHERE i.credate = :credate"),
    @NamedQuery(name = "IndexWareh.findByOptuser", query = "SELECT i FROM IndexWareh i WHERE i.optuser = :optuser"),
    @NamedQuery(name = "IndexWareh.findByOptdate", query = "SELECT i FROM IndexWareh i WHERE i.optdate = :optdate"),
    @NamedQuery(name = "IndexWareh.findByCfmuser", query = "SELECT i FROM IndexWareh i WHERE i.cfmuser = :cfmuser"),
    @NamedQuery(name = "IndexWareh.findByCfmdate", query = "SELECT i FROM IndexWareh i WHERE i.cfmdate = :cfmdate")})
public class IndexWareh extends SuperEntity {

    @Size(max = 20)
    @Column(name = "wareh")
    private String wareh;
    @Size(max = 20)
    @Column(name = "whdsc")
    private String whdsc;
    @Size(max = 20)
    @Column(name = "generno")
    private String generno;
        @Basic(optional = false)
    @NotNull
    @Column(name = "sort")
    private int sort;

    public IndexWareh() {
        this.status="N";
    }

    public IndexWareh(Integer id) {
        this.id = id;
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

    public String getGenerno() {
        return generno;
    }

    public void setGenerno(String generno) {
        this.generno = generno;
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
        if (!(object instanceof IndexWareh)) {
            return false;
        }
        IndexWareh other = (IndexWareh) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.IndexWareh[ id=" + id + " ]";
    }
    
}
