/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @author C0160
 */
@Entity
@Table(name = "scorecard")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Scorecard.findAll", query = "SELECT s FROM Scorecard s"),
    @NamedQuery(name = "Scorecard.findById", query = "SELECT s FROM Scorecard s WHERE s.id = :id"),
    @NamedQuery(name = "Scorecard.findByCompany", query = "SELECT s FROM Scorecard s WHERE s.company = :company"),
    @NamedQuery(name = "Scorecard.findByCompanyMenuAndYear", query = "SELECT s FROM Scorecard s WHERE s.company = :company AND s.menu = :menu AND s.seq = :seq ORDER BY s.sortid,s.deptno"),
    @NamedQuery(name = "Scorecard.findByNameAndSeq", query = "SELECT s FROM Scorecard s WHERE s.name = :name AND s.seq = :seq"),
    @NamedQuery(name = "Scorecard.findByPId", query = "SELECT s FROM Scorecard s WHERE s.pid = :pid"),
    @NamedQuery(name = "Scorecard.findBySeq", query = "SELECT s FROM Scorecard s WHERE s.seq = :seq"),
    @NamedQuery(name = "Scorecard.findByDeptno", query = "SELECT s FROM Scorecard s WHERE s.deptno = :deptno"),
    @NamedQuery(name = "Scorecard.findByDeptname", query = "SELECT s FROM Scorecard s WHERE s.deptname = :deptname"),
    @NamedQuery(name = "Scorecard.findByUserid", query = "SELECT s FROM Scorecard s WHERE s.userid = :userid"),
    @NamedQuery(name = "Scorecard.findByUsername", query = "SELECT s FROM Scorecard s WHERE s.username = :username"),
    @NamedQuery(name = "Scorecard.findByMenuAndYear", query = "SELECT s FROM Scorecard s WHERE s.menu = :menu AND s.seq = :seq ORDER BY s.sortid,s.deptno"),
    @NamedQuery(name = "Scorecard.findByTemplate", query = "SELECT s FROM Scorecard s WHERE s.template = :template"),
    @NamedQuery(name = "Scorecard.findByTemplateId", query = "SELECT s FROM Scorecard s WHERE s.templateId = :templateId"),
    @NamedQuery(name = "Scorecard.findByStatusAndYear", query = "SELECT s FROM Scorecard s WHERE s.status = :status  AND s.seq = :seq"),
    @NamedQuery(name = "Scorecard.findByRowCount", query = "SELECT COUNT(s) FROM Scorecard s WHERE s.company = :company AND s.seq = :seq"),
    @NamedQuery(name = "Scorecard.findByCompanyAndSeq", query = "SELECT s FROM Scorecard s WHERE s.company = :company AND s.seq = :seq"),
    @NamedQuery(name = "Scorecard.findByCompanyAndSeqAndIsbsc", query = "SELECT s FROM Scorecard s WHERE s.company = :company AND s.seq = :seq AND s.isbsc = :isbsc")})
public class Scorecard extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "company")
    private String company;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "name")
    private String name;
    @Size(max = 400)
    @Column(name = "descript")
    private String descript;
    @Column(name = "pid")
    private Integer pid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "seq")
    private int seq;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "deptno")
    private String deptno;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 10)
    @Column(name = "userid")
    private String userid;
    @Size(max = 20)
    @Column(name = "username")
    private String username;
    @Size(max = 2)
    @Column(name = "lvl")
    private String lvl;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sortid")
    private int sortid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "template")
    private boolean template;
    @Size(max = 200)
    @Column(name = "templateId")
    private String templateId;
    @Column(name = "sq1")
    private BigDecimal sq1;
    @Column(name = "sq2")
    private BigDecimal sq2;
    @Column(name = "sq3")
    private BigDecimal sq3;
    @Column(name = "sq4")
    private BigDecimal sq4;
    @Column(name = "sh1")
    private BigDecimal sh1;
    @Column(name = "sh2")
    private BigDecimal sh2;
    @Column(name = "sfy")
    private BigDecimal sfy;
    @Column(name = "freezeDate")
    @Temporal(TemporalType.DATE)
    private Date freezeDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "api")
    private String api;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "menu")
    private String menu;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    @Basic(optional = false)
    @Column(name = "isbsc")
    private boolean isbsc;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "oastatus1")
    private String oastatus1;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "oapsn1")
    private String oapsn1;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "oastatus2")
    private String oastatus2;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "oapsn2")
    private String oapsn2;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "oastatus3")
    private String oastatus3;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "oapsn3")
    private String oapsn3;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "oastatus4")
    private String oastatus4;

    @Basic(optional = false)
    @Size(min = 1, max = 50)
    @Column(name = "oapsn4")
    private String oapsn4;

    public Scorecard() {
        this.sortid = 0;
        this.template = false;
        this.sq1 = BigDecimal.ZERO;
        this.sq2 = BigDecimal.ZERO;
        this.sq3 = BigDecimal.ZERO;
        this.sq4 = BigDecimal.ZERO;
        this.sh1 = BigDecimal.ZERO;
        this.sh2 = BigDecimal.ZERO;
        this.sfy = BigDecimal.ZERO;
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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
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

    /**
     * @return the lvl
     */
    public String getLvl() {
        return lvl;
    }

    /**
     * @param lvl the lvl to set
     */
    public void setLvl(String lvl) {
        this.lvl = lvl;
    }

    public int getSortid() {
        return sortid;
    }

    public void setSortid(int sortid) {
        this.sortid = sortid;
    }

    public boolean getTemplate() {
        return template;
    }

    public void setTemplate(boolean template) {
        this.template = template;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the sq1
     */
    public BigDecimal getSq1() {
        return sq1;
    }

    /**
     * @param sq1 the sq1 to set
     */
    public void setSq1(BigDecimal sq1) {
        this.sq1 = sq1;
    }

    /**
     * @return the sq2
     */
    public BigDecimal getSq2() {
        return sq2;
    }

    /**
     * @param sq2 the sq2 to set
     */
    public void setSq2(BigDecimal sq2) {
        this.sq2 = sq2;
    }

    /**
     * @return the sq3
     */
    public BigDecimal getSq3() {
        return sq3;
    }

    /**
     * @param sq3 the sq3 to set
     */
    public void setSq3(BigDecimal sq3) {
        this.sq3 = sq3;
    }

    /**
     * @return the sq4
     */
    public BigDecimal getSq4() {
        return sq4;
    }

    /**
     * @param sq4 the sq4 to set
     */
    public void setSq4(BigDecimal sq4) {
        this.sq4 = sq4;
    }

    /**
     * @return the sh1
     */
    public BigDecimal getSh1() {
        return sh1;
    }

    /**
     * @param sh1 the sh1 to set
     */
    public void setSh1(BigDecimal sh1) {
        this.sh1 = sh1;
    }

    /**
     * @return the sh2
     */
    public BigDecimal getSh2() {
        return sh2;
    }

    /**
     * @param sh2 the sh2 to set
     */
    public void setSh2(BigDecimal sh2) {
        this.sh2 = sh2;
    }

    /**
     * @return the sfy
     */
    public BigDecimal getSfy() {
        return sfy;
    }

    /**
     * @param sfy the sfy to set
     */
    public void setSfy(BigDecimal sfy) {
        this.sfy = sfy;
    }

    /**
     * @return the freezeDate
     */
    public Date getFreezeDate() {
        return freezeDate;
    }

    /**
     * @param freezeDate the freezeDate to set
     */
    public void setFreezeDate(Date freezeDate) {
        this.freezeDate = freezeDate;
    }

    /**
     * @return the api
     */
    public String getApi() {
        return api;
    }

    /**
     * @param api the api to set
     */
    public void setApi(String api) {
        this.api = api;
    }

    /**
     * @return the menu
     */
    public String getMenu() {
        return menu;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(String menu) {
        this.menu = menu;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public boolean isIsbsc() {
        return isbsc;
    }

    public void setIsbsc(boolean isbsc) {
        this.isbsc = isbsc;
    }

    public String getOastatus1() {
        return oastatus1;
    }

    public void setOastatus1(String oastatus1) {
        this.oastatus1 = oastatus1;
    }

    public String getOapsn1() {
        return oapsn1;
    }

    public void setOapsn1(String oapsn1) {
        this.oapsn1 = oapsn1;
    }

    public String getOastatus2() {
        return oastatus2;
    }

    public void setOastatus2(String oastatus2) {
        this.oastatus2 = oastatus2;
    }

    public String getOapsn2() {
        return oapsn2;
    }

    public void setOapsn2(String oapsn2) {
        this.oapsn2 = oapsn2;
    }

    public String getOastatus3() {
        return oastatus3;
    }

    public void setOastatus3(String oastatus3) {
        this.oastatus3 = oastatus3;
    }

    public String getOapsn3() {
        return oapsn3;
    }

    public void setOapsn3(String oapsn3) {
        this.oapsn3 = oapsn3;
    }

    public String getOastatus4() {
        return oastatus4;
    }

    public void setOastatus4(String oastatus4) {
        this.oastatus4 = oastatus4;
    }

    public String getOapsn4() {
        return oapsn4;
    }

    public void setOapsn4(String oapsn4) {
        this.oapsn4 = oapsn4;
    }

    public String getLvlValue() {
        switch (this.lvl) {
            case "D":
                return "部级";
            case "S":
                return "课级";
            default:
                return "";
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
        if (!(object instanceof Scorecard)) {
            return false;
        }
        Scorecard other = (Scorecard) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return Objects.equals(this.company, other.company) && Objects.equals(this.name, other.name) && Objects.equals(this.deptno, other.deptno);
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.Scorecard[ id=" + id + " ]";
    }

}
