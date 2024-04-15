/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import cn.hanbell.eap.entity.Department;
import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C2082
 */
@Entity
@Table(name = "personscorecard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonScorecard.findAll", query = "SELECT p FROM PersonScorecard p"),
    @NamedQuery(name = "PersonScorecard.findById", query = "SELECT p FROM PersonScorecard p WHERE p.id = :id"),
    @NamedQuery(name = "PersonScorecard.findByUserid", query = "SELECT p FROM PersonScorecard p WHERE p.userid = :userid"),
    @NamedQuery(name = "PersonScorecard.findByUsername", query = "SELECT p FROM PersonScorecard p WHERE p.username = :username"),
    @NamedQuery(name = "PersonScorecard.findByYear", query = "SELECT p FROM PersonScorecard p WHERE p.year = :year"),
    @NamedQuery(name = "PersonScorecard.findByOfficiallevel", query = "SELECT p FROM PersonScorecard p WHERE p.officiallevel = :officiallevel"),
    @NamedQuery(name = "PersonScorecard.findByUseridAndYear", query = "SELECT p FROM PersonScorecard p WHERE  p.userid = :userid and p.year = :year"),
    @NamedQuery(name = "PersonScorecard.findByStatus", query = "SELECT p FROM PersonScorecard p WHERE p.status = :status")})
public class PersonScorecard extends SuperEntity {

    @JoinColumn(name = "userid", referencedColumnName = "userid", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private PersonSet personset;

    private static final long serialVersionUID = 1L;
    @Size(max = 20)
    @Column(name = "userid")
    private String userid;
    @Size(max = 50)
    @Column(name = "username")
    private String username;
    @Column(name = "year")
    private Integer year;
    @Size(max = 50)
    @Column(name = "officiallevel")
    private String officiallevel;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "subquarter1")
    private BigDecimal subquarter1;
    @Column(name = "objquarter1")
    private BigDecimal objquarter1;
    @Column(name = "porobjquarter1")
    private BigDecimal porobjquarter1;
    @Column(name = "porsubquarter1")
    private BigDecimal porsubquarter1;
    @Column(name = "subquarter2")
    private BigDecimal subquarter2;
    @Column(name = "objquarter2")
    private BigDecimal objquarter2;
    @Column(name = "porobjquarter2")
    private BigDecimal porobjquarter2;
    @Column(name = "porsubquarter2")
    private BigDecimal porsubquarter2;
    @Column(name = "subquarter3")
    private BigDecimal subquarter3;
    @Column(name = "objquarter3")
    private BigDecimal objquarter3;
    @Column(name = "porobjquarter3")
    private BigDecimal porobjquarter3;
    @Column(name = "porsubquarter3")
    private BigDecimal porsubquarter3;
    @Column(name = "subquarter4")
    private BigDecimal subquarter4;
    @Column(name = "objquarter4")
    private BigDecimal objquarter4;
    @Column(name = "porobjquarter4")
    private BigDecimal porobjquarter4;
    @Column(name = "porsubquarter4")
    private BigDecimal porsubquarter4;
    @Transient
    private String deptClass;

    @Size(max = 5)
    @Column(name = "msgstatus1")
    private String msgstatus1;
    @Size(max = 5)
    @Column(name = "msgstatus2")
    private String msgstatus2;
    @Size(max = 5)
    @Column(name = "msgstatus3")
    private String msgstatus3;
    @Size(max = 5)
    @Column(name = "msgstatus4")
    private String msgstatus4;

    public PersonScorecard() {
    }

    public PersonScorecard(String userid, String username, Integer year) {
        this.userid = userid;
        this.username = username;
        this.year = year;
        this.subquarter1 = BigDecimal.ZERO;
        this.objquarter1 = BigDecimal.ZERO;
        this.porsubquarter1 = BigDecimal.ZERO;
        this.porobjquarter1 = BigDecimal.ZERO;

        this.subquarter2 = BigDecimal.ZERO;
        this.objquarter2 = BigDecimal.ZERO;
        this.porsubquarter2 = BigDecimal.ZERO;
        this.porobjquarter2 = BigDecimal.ZERO;

        this.subquarter3 = BigDecimal.ZERO;
        this.objquarter3 = BigDecimal.ZERO;
        this.porsubquarter3 = BigDecimal.ZERO;
        this.porobjquarter3 = BigDecimal.ZERO;

        this.subquarter4 = BigDecimal.ZERO;
        this.objquarter4 = BigDecimal.ZERO;
        this.porsubquarter4 = BigDecimal.ZERO;
        this.porobjquarter4 = BigDecimal.ZERO;
    }

    public PersonSet getPersonset() {
        return personset;
    }

    public void setPersonset(PersonSet personset) {
        this.personset = personset;
    }

    public PersonScorecard(Integer id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getOfficiallevel() {
        return officiallevel;
    }

    public void setOfficiallevel(String officiallevel) {
        this.officiallevel = officiallevel;
    }

    public BigDecimal getSubquarter1() {
        return subquarter1;
    }

    public void setSubquarter1(BigDecimal subquarter1) {
        this.subquarter1 = subquarter1;
    }

    public BigDecimal getObjquarter1() {
        return objquarter1;
    }

    public void setObjquarter1(BigDecimal objquarter1) {
        this.objquarter1 = objquarter1;
    }

    public BigDecimal getPorobjquarter1() {
        return porobjquarter1;
    }

    public void setPorobjquarter1(BigDecimal porobjquarter1) {
        this.porobjquarter1 = porobjquarter1;
    }

    public BigDecimal getPorsubquarter1() {
        return porsubquarter1;
    }

    public void setPorsubquarter1(BigDecimal porsubquarter1) {
        this.porsubquarter1 = porsubquarter1;
    }

    public BigDecimal getSubquarter2() {
        return subquarter2;
    }

    public void setSubquarter2(BigDecimal subquarter2) {
        this.subquarter2 = subquarter2;
    }

    public BigDecimal getObjquarter2() {
        return objquarter2;
    }

    public void setObjquarter2(BigDecimal objquarter2) {
        this.objquarter2 = objquarter2;
    }

    public BigDecimal getPorobjquarter2() {
        return porobjquarter2;
    }

    public void setPorobjquarter2(BigDecimal porobjquarter2) {
        this.porobjquarter2 = porobjquarter2;
    }

    public BigDecimal getPorsubquarter2() {
        return porsubquarter2;
    }

    public void setPorsubquarter2(BigDecimal porsubquarter2) {
        this.porsubquarter2 = porsubquarter2;
    }

    public BigDecimal getSubquarter3() {
        return subquarter3;
    }

    public void setSubquarter3(BigDecimal subquarter3) {
        this.subquarter3 = subquarter3;
    }

    public BigDecimal getObjquarter3() {
        return objquarter3;
    }

    public void setObjquarter3(BigDecimal objquarter3) {
        this.objquarter3 = objquarter3;
    }

    public BigDecimal getPorobjquarter3() {
        return porobjquarter3;
    }

    public void setPorobjquarter3(BigDecimal porobjquarter3) {
        this.porobjquarter3 = porobjquarter3;
    }

    public BigDecimal getPorsubquarter3() {
        return porsubquarter3;
    }

    public void setPorsubquarter3(BigDecimal porsubquarter3) {
        this.porsubquarter3 = porsubquarter3;
    }

    public BigDecimal getSubquarter4() {
        return subquarter4;
    }

    public void setSubquarter4(BigDecimal subquarter4) {
        this.subquarter4 = subquarter4;
    }

    public BigDecimal getObjquarter4() {
        return objquarter4;
    }

    public void setObjquarter4(BigDecimal objquarter4) {
        this.objquarter4 = objquarter4;
    }

    public BigDecimal getPorobjquarter4() {
        return porobjquarter4;
    }

    public void setPorobjquarter4(BigDecimal porobjquarter4) {
        this.porobjquarter4 = porobjquarter4;
    }

    public BigDecimal getPorsubquarter4() {
        return porsubquarter4;
    }

    public void setPorsubquarter4(BigDecimal porsubquarter4) {
        this.porsubquarter4 = porsubquarter4;
    }

    public String getDeptClass() {
        return deptClass;
    }

    public void setDeptClass(String deptClass) {
        this.deptClass = deptClass;
    }

    public String getMsgstatus1() {
        return msgstatus1;
    }

    public void setMsgstatus1(String msgstatus1) {
        this.msgstatus1 = msgstatus1;
    }

    public String getMsgstatus2() {
        return msgstatus2;
    }

    public void setMsgstatus2(String msgstatus2) {
        this.msgstatus2 = msgstatus2;
    }

    public String getMsgstatus3() {
        return msgstatus3;
    }

    public void setMsgstatus3(String msgstatus3) {
        this.msgstatus3 = msgstatus3;
    }

    public String getMsgstatus4() {
        return msgstatus4;
    }

    public void setMsgstatus4(String msgstatus4) {
        this.msgstatus4 = msgstatus4;
    }

    public void setMsgstatus(String msgstatus, int q) {
        if (q == 1) {
            this.msgstatus1 = msgstatus;
        } else if (q == 2) {
            this.msgstatus2 = msgstatus;
        } else if (q == 3) {
            this.msgstatus3 = msgstatus;
        } else if (q == 4) {
            this.msgstatus4 = msgstatus;
        }
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
        if (!(object instanceof PersonScorecard)) {
            return false;
        }
        PersonScorecard other = (PersonScorecard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PersonScorecard[ id=" + id + " ]";
    }

}
