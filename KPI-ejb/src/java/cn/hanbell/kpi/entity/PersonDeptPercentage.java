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
@Table(name = "persondeptpercentage")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonDeptPercentage.findAll", query = "SELECT p FROM PersonDeptPercentage p"),
    @NamedQuery(name = "PersonDeptPercentage.findById", query = "SELECT p FROM PersonDeptPercentage p WHERE p.id = :id"),
    @NamedQuery(name = "PersonDeptPercentage.findByYear", query = "SELECT p FROM PersonDeptPercentage p WHERE p.year = :year"),
    @NamedQuery(name = "PersonDeptPercentage.findByDeptno", query = "SELECT p FROM PersonDeptPercentage p WHERE p.deptno = :deptno"),
    @NamedQuery(name = "PersonDeptPercentage.findByDeptname", query = "SELECT p FROM PersonDeptPercentage p WHERE p.deptname = :deptname"),
    @NamedQuery(name = "PersonDeptPercentage.findByPercentage", query = "SELECT p FROM PersonDeptPercentage p WHERE p.percentage = :percentage"),
    @NamedQuery(name = "PersonDeptPercentage.findByStatus", query = "SELECT p FROM PersonDeptPercentage p WHERE p.status = :status"),
    @NamedQuery(name = "PersonDeptPercentage.findByYearAndDeptno", query = "SELECT p FROM PersonDeptPercentage p WHERE p.deptno = :deptno and p.year = :year")})
public class PersonDeptPercentage extends SuperEntity {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Column(name = "year")
    private int year;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "deptname")
    private String deptname;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "percentage")
    private BigDecimal percentage;

    public PersonDeptPercentage() {
    }

    public PersonDeptPercentage(Integer id) {
        this.id = id;
    }

    public PersonDeptPercentage(Integer id, int year, String deptno, String deptname, String status) {
        this.id = id;
        this.year = year;
        this.deptno = deptno;
        this.deptname = deptname;
        this.status = status;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
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
        if (!(object instanceof PersonDeptPercentage)) {
            return false;
        }
        PersonDeptPercentage other = (PersonDeptPercentage) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PersonDeptPercentage[ id=" + id + " ]";
    }

}
