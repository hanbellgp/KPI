/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "policy")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Policy.findAll", query = "SELECT p FROM Policy p"),
    @NamedQuery(name = "Policy.findById", query = "SELECT p FROM Policy p WHERE p.id = :id"),
    @NamedQuery(name = "Policy.findByCompany", query = "SELECT p FROM Policy p WHERE p.company = :company"),
    @NamedQuery(name = "Policy.findByName", query = "SELECT p FROM Policy p WHERE p.name = :name"),
    @NamedQuery(name = "Policy.findByVision", query = "SELECT p FROM Policy p WHERE p.vision = :vision"),
    @NamedQuery(name = "Policy.findByPolicydescript", query = "SELECT p FROM Policy p WHERE p.policydescript = :policydescript"),
    @NamedQuery(name = "Policy.findByDeptno", query = "SELECT p FROM Policy p WHERE p.deptno = :deptno"),
    @NamedQuery(name = "Policy.findByDeptna", query = "SELECT p FROM Policy p WHERE p.deptna = :deptna"),
    @NamedQuery(name = "Policy.findByUserno", query = "SELECT p FROM Policy p WHERE p.userno = :userno"),
    @NamedQuery(name = "Policy.findByUserna", query = "SELECT p FROM Policy p WHERE p.userna = :userna"),
    @NamedQuery(name = "Policy.findByApi", query = "SELECT p FROM Policy p WHERE p.api = :api"),
    @NamedQuery(name = "Policy.findByMenudeptno", query = "SELECT p FROM Policy p WHERE p.menudeptno = :menudeptno"),
    @NamedQuery(name = "Policy.findByCp", query = "SELECT p FROM Policy p WHERE p.cp = :cp"),
    @NamedQuery(name = "Policy.findByCo", query = "SELECT p FROM Policy p WHERE p.co = :co"),
    @NamedQuery(name = "Policy.findByQp", query = "SELECT p FROM Policy p WHERE p.qp = :qp"),
    @NamedQuery(name = "Policy.findByQo", query = "SELECT p FROM Policy p WHERE p.qo = :qo"),
    @NamedQuery(name = "Policy.findByDp", query = "SELECT p FROM Policy p WHERE p.dp = :dp"),
    @NamedQuery(name = "Policy.findByDo1", query = "SELECT p FROM Policy p WHERE p.do1 = :do1"),
    @NamedQuery(name = "Policy.findByPp", query = "SELECT p FROM Policy p WHERE p.pp = :pp"),
    @NamedQuery(name = "Policy.findByPo", query = "SELECT p FROM Policy p WHERE p.po = :po"),
    @NamedQuery(name = "Policy.findByYear", query = "SELECT p FROM Policy p WHERE p.year = :year"),
    @NamedQuery(name = "Policy.findByStatus", query = "SELECT p FROM Policy p WHERE p.status = :status"),
    @NamedQuery(name = "Policy.findByCreator", query = "SELECT p FROM Policy p WHERE p.creator = :creator"),
    @NamedQuery(name = "Policy.findByCredate", query = "SELECT p FROM Policy p WHERE p.credate = :credate"),
    @NamedQuery(name = "Policy.findByOptuser", query = "SELECT p FROM Policy p WHERE p.optuser = :optuser"),
    @NamedQuery(name = "Policy.findByOptdate", query = "SELECT p FROM Policy p WHERE p.optdate = :optdate"),
    @NamedQuery(name = "Policy.findByCfmuser", query = "SELECT p FROM Policy p WHERE p.cfmuser = :cfmuser"),
    @NamedQuery(name = "Policy.findByCfmdate", query = "SELECT p FROM Policy p WHERE p.cfmdate = :cfmdate"),
    @NamedQuery(name = "Policy.findByCompanyNameAndYear", query = "SELECT p FROM Policy p WHERE p.company = :company and p.name = :name and p.year = :year"),
 @NamedQuery(name = "Policy.findByCompanyMenuAndYear", query = "SELECT p FROM Policy p WHERE p.company = :company and p.deptno = :deptno and p.year = :year")})
public class Policy  extends SuperEntity implements Comparable<Policy> {

    private static final long serialVersionUID = 1L;

    @Size(max = 10)
    @Column(name = "company")
    private String company;
    @Size(max = 20)
    @Column(name = "name")
    private String name;
    @Size(max = 200)
    @Column(name = "vision")
    private String vision;
    @Size(max = 200)
    @Column(name = "policydescript")
    private String policydescript;
    @Size(max = 10)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 20)
    @Column(name = "deptna")
    private String deptna;
    @Size(max = 10)
    @Column(name = "userno")
    private String userno;
    @Size(max = 10)
    @Column(name = "userna")
    private String userna;
    @Size(max = 50)
    @Column(name = "api")
    private String api;
    @Size(max = 10)
    @Column(name = "menudeptno")
    private String menudeptno;
    @Size(max = 200)
    @Column(name = "cp")
    private String cp;
    @Size(max = 200)
    @Column(name = "co")
    private String co;
    @Size(max = 200)
    @Column(name = "qp")
    private String qp;
    @Size(max = 200)
    @Column(name = "qo")
    private String qo;
    @Size(max = 200)
    @Column(name = "dp")
    private String dp;
    @Size(max = 200)
    @Column(name = "do")
    private String do1;
    @Size(max = 200)
    @Column(name = "pp")
    private String pp;
    @Size(max = 200)
    @Column(name = "po")
    private String po;
    @Column(name = "year")
    private Integer year;

    public Policy() {
    }

    public Policy(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVision() {
        return vision;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public String getPolicydescript() {
        return policydescript;
    }

    public void setPolicydescript(String policydescript) {
        this.policydescript = policydescript;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getDeptna() {
        return deptna;
    }

    public void setDeptna(String deptna) {
        this.deptna = deptna;
    }

    public String getUserno() {
        return userno;
    }

    public void setUserno(String userno) {
        this.userno = userno;
    }

    public String getUserna() {
        return userna;
    }

    public void setUserna(String userna) {
        this.userna = userna;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMenudeptno() {
        return menudeptno;
    }

    public void setMenudeptno(String menudeptno) {
        this.menudeptno = menudeptno;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getCo() {
        return co;
    }

    public void setCo(String co) {
        this.co = co;
    }

    public String getQp() {
        return qp;
    }

    public void setQp(String qp) {
        this.qp = qp;
    }

    public String getQo() {
        return qo;
    }

    public void setQo(String qo) {
        this.qo = qo;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getDo1() {
        return do1;
    }

    public void setDo1(String do1) {
        this.do1 = do1;
    }

    public String getPp() {
        return pp;
    }

    public void setPp(String pp) {
        this.pp = pp;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Policy)) {
            return false;
        }
        Policy other = (Policy) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Policy[ id=" + id + " ]";
    }
 

    @Override
    public int compareTo(Policy o) {
         if (this.getDeptno().compareTo(o.getDeptno()) < 0) {
            return -1;
        } else {
            return 1;
        }
    }

}
