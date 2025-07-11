/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "personset")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonSet.findAll", query = "SELECT p FROM PersonSet p"),
    @NamedQuery(name = "PersonSet.findById", query = "SELECT p FROM PersonSet p WHERE p.id = :id"),
    @NamedQuery(name = "PersonSet.findByUserid", query = "SELECT p FROM PersonSet p WHERE p.userid = :userid"),
    @NamedQuery(name = "PersonSet.findByUsername", query = "SELECT p FROM PersonSet p WHERE p.username = :username"),
    @NamedQuery(name = "PersonSet.findByOfficialrank", query = "SELECT p FROM PersonSet p WHERE p.officialrank = :officialrank"),
    @NamedQuery(name = "PersonSet.findByJobcategory", query = "SELECT p FROM PersonSet p WHERE p.jobcategory = :jobcategory"),
    @NamedQuery(name = "PersonSet.findByDuties", query = "SELECT p FROM PersonSet p WHERE p.duties = :duties"),
    @NamedQuery(name = "PersonSet.findByIsadministrative", query = "SELECT p FROM PersonSet p WHERE p.isadministrative = :isadministrative"),
    @NamedQuery(name = "PersonSet.findByCoefficient", query = "SELECT p FROM PersonSet p WHERE p.coefficient = :coefficient"),
    @NamedQuery(name = "PersonSet.findByDepartmentscorecard", query = "SELECT p FROM PersonSet p WHERE p.departmentscorecard = :departmentscorecard"),
    @NamedQuery(name = "PersonSet.findByClassscorecard", query = "SELECT p FROM PersonSet p WHERE p.classscorecard = :classscorecard"),
    @NamedQuery(name = "PersonSet.findByStatus", query = "SELECT p FROM PersonSet p WHERE p.status = :status"),
    @NamedQuery(name = "PersonSet.findByCreator", query = "SELECT p FROM PersonSet p WHERE p.creator = :creator"),
    @NamedQuery(name = "PersonSet.findByCredate", query = "SELECT p FROM PersonSet p WHERE p.credate = :credate"),
    @NamedQuery(name = "PersonSet.findByOptuser", query = "SELECT p FROM PersonSet p WHERE p.optuser = :optuser"),
    @NamedQuery(name = "PersonSet.findByOptdate", query = "SELECT p FROM PersonSet p WHERE p.optdate = :optdate"),
    @NamedQuery(name = "PersonSet.findByCfmuser", query = "SELECT p FROM PersonSet p WHERE p.cfmuser = :cfmuser"),
    @NamedQuery(name = "PersonSet.findByCfmdate", query = "SELECT p FROM PersonSet p WHERE p.cfmdate = :cfmdate")})
public class PersonSet extends SuperEntity {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "userid")
    private String userid;
    @Size(max = 50)
    @Column(name = "facno")
    private String facno;
    @Size(max = 50)
    @Column(name = "username")
    private String username;

    @Size(max = 50)
    @Column(name = "deptno")
    private String deptno;
    @Size(max = 50)
    @Column(name = "deptname")
    private String deptname;
    @Size(max = 50)
    @Column(name = "type")
    private String type;
    @Size(max = 50)
    @Column(name = "officialrank")
    private String officialrank;
    @Size(max = 60)
    @Column(name = "officialrankdesc")
    private String officialrankdesc;
    @Size(max = 50)
    @Column(name = "jobcategory")
    private String jobcategory;
    @Size(max = 50)
    @Column(name = "duties")
    private String duties;
    @Size(max = 50)
    @Column(name = "assessmentmethod")
    private String assessmentmethod;
    @Column(name = "isadministrative")
    private Boolean isadministrative;
    @Column(name = "coefficient")
    private Double coefficient;
    @Size(max = 50)
    @Column(name = "departmentscorecard")
    private String departmentscorecard;
    @Size(max = 50)
    @Column(name = "classscorecard")
    private String classscorecard;
    @Column(name = "percentage")
    private BigDecimal percentage;
    
    @Column(name = "minscore")
    private BigDecimal minscore;
    @Column(name = "maxscore")
    private BigDecimal maxscore;


    
    @JoinColumn(name = "assessmentmethod", referencedColumnName = "formid", updatable = false, insertable = false)
    @ManyToOne(optional = true)
    private PersonScorecardWay personscorecardway;

    public PersonSet() {
    }

    public PersonSet(Integer id) {
        this.id = id;
    }

    public PersonSet(Integer id, String userid, String status) {
        this.id = id;
        this.userid = userid;
        this.status = status;
    }

    public PersonSet(String facno, String userid, String username, String deptno, String deptname, String officialrank, String officialrankdesc, String jobcategory, String type, String duties, String status, String creator) {
        this.facno = facno;
        this.username = username;
        this.userid = userid;
        this.deptno = deptno;
        this.deptname = deptname;
        this.officialrank = officialrank;
        this.officialrankdesc = officialrankdesc;
        this.jobcategory = jobcategory;
        this.type = type;
        this.duties = duties;
        this.status = status;
        this.creator = creator;
        this.credate = new Date();
        this.coefficient=1.0;
    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOfficialrank() {
        return officialrank;
    }

    public void setOfficialrank(String officialrank) {
        this.officialrank = officialrank;
    }

    public String getOfficialrankdesc() {
        return officialrankdesc;
    }

    public void setOfficialrankdesc(String officialrankdesc) {
        this.officialrankdesc = officialrankdesc;
    }

    public String getJobcategory() {
        return jobcategory;
    }

    public void setJobcategory(String jobcategory) {
        this.jobcategory = jobcategory;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

    public String getAssessmentmethod() {
        return assessmentmethod;
    }

    public void setAssessmentmethod(String assessmentmethod) {
        this.assessmentmethod = assessmentmethod;
    }

    public Boolean getIsadministrative() {
        return isadministrative;
    }

    public void setIsadministrative(Boolean isadministrative) {
        this.isadministrative = isadministrative;
    }


    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public String getDepartmentscorecard() {
        return departmentscorecard;
    }

    public void setDepartmentscorecard(String departmentscorecard) {
        this.departmentscorecard = departmentscorecard;
    }

    public String getClassscorecard() {
        return classscorecard;
    }

    public void setClassscorecard(String classscorecard) {
        this.classscorecard = classscorecard;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public PersonScorecardWay getPersonscorecardway() {
        return personscorecardway;
    }

    public void setPersonscorecardway(PersonScorecardWay personscorecardway) {
        this.personscorecardway = personscorecardway;
    }

    public BigDecimal getMinscore() {
        return minscore;
    }

    public void setMinscore(BigDecimal minscore) {
        this.minscore = minscore;
    }

    public BigDecimal getMaxscore() {
        return maxscore;
    }

    public void setMaxscore(BigDecimal maxscore) {
        this.maxscore = maxscore;
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
        if (!(object instanceof PersonSet)) {
            return false;
        }
        PersonSet other = (PersonSet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String getAssessmentlevel() {
        String officialrank, officialrankdesc;
        if (this.officialrank.contains("A")) {
            officialrank = "A";
        } else if (this.officialrank.contains("B")) {
            officialrank = "B";
        }else{
            officialrank = this.officialrank;
        }
        switch (this.officialrankdesc) {
            case "课长级":
                officialrankdesc = "C";
                break;
            case "经理级":
                officialrankdesc = "D";
                break;
            case "协理级":
            case "副总经理级":
            case "总经理级":
            case "副董级":
            case "董事长级":
                officialrankdesc = "E";
                break;
            default:
                officialrankdesc = "A";
        }
        //处理B职等课长级等问题
        if (officialrank.compareTo(officialrankdesc) > 0) {
            return officialrank;
        } else {
            return officialrankdesc;
        }
    }

    public void setIsadministrative(String officialrank, String officialrankdesc) {
        if (officialrank.contains("A")) {
            officialrank = "A";
        } else if (officialrank.contains("B")) {
            officialrank = "B";
        }
        switch (officialrankdesc) {
            case "组长级":
                officialrankdesc = "B";
                break;
            case "课长级":
                officialrankdesc = "C";
                break;
            case "经理级":
                officialrankdesc = "D";
                break;
            case "协理级":
            case "副总经理级":
            case "总经理级":
            case "副董级":
            case "董事长级":
                officialrankdesc = "E";
                break;
            default:
                this.setIsadministrative(false);
        }
        if (officialrank.compareTo(officialrankdesc) == 0) {
            this.setIsadministrative(true);
        } else if (officialrank.compareTo(officialrankdesc) == -1) {
            this.setIsadministrative(true);
        } else {
            this.setIsadministrative(false);
        }
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.PersonSet[ id=" + id + " ]";
    }

}
