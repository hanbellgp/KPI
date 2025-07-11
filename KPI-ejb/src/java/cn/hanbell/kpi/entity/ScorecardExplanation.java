/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.SuperDetailEntity;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "scorecardexplanation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ScorecardExplanation.findAll", query = "SELECT s FROM ScorecardExplanation s"),
    @NamedQuery(name = "ScorecardExplanation.findById", query = "SELECT s FROM ScorecardExplanation s WHERE s.id = :id"),
    @NamedQuery(name = "ScorecardExplanation.findByPId", query = "SELECT s FROM ScorecardExplanation s WHERE s.pid = :pid ORDER BY s.seq"),
    @NamedQuery(name = "ScorecardExplanation.findBySeq", query = "SELECT s FROM ScorecardExplanation s WHERE s.seq = :seq"),
    @NamedQuery(name = "ScorecardExplanation.findByType", query = "SELECT s FROM ScorecardExplanation s WHERE s.type = :type")})
public class ScorecardExplanation extends SuperDetailEntity {

    @Size(max = 10)
    @Column(name = "type")
    private String type;
    @Size(max = 400)
    @Column(name = "title")
    private String title;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q1")
    private String q1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q2")
    private String q2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q3")
    private String q3;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "q4")
    private String q4;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "h1")
    private String h1;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "h2")
    private String h2;
    @Lob
    @Size(max = 2147483647)
    @Column(name = "fy")
    private String fy;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
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

    public ScorecardExplanation() {
        this.q1 = "";
        this.q2 = "";
        this.q3 = "";
        this.q4 = "";
        this.h1 = "";
        this.h2 = "";
        this.fy = "";
        this.sq1 = BigDecimal.ZERO;
        this.sq2 = BigDecimal.ZERO;
        this.sq3 = BigDecimal.ZERO;
        this.sq4 = BigDecimal.ZERO;
        this.sh1 = BigDecimal.ZERO;
        this.sh2 = BigDecimal.ZERO;
        this.sfy = BigDecimal.ZERO;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQ1() {
        return q1;
    }

    public void setQ1(String q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getQ3() {
        return q3;
    }

    public void setQ3(String q3) {
        this.q3 = q3;
    }

    public String getQ4() {
        return q4;
    }

    public void setQ4(String q4) {
        this.q4 = q4;
    }

    public String getH1() {
        return h1;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public String getH2() {
        return h2;
    }

    public void setH2(String h2) {
        this.h2 = h2;
    }

    public String getFy() {
        return fy;
    }

    public void setFy(String fy) {
        this.fy = fy;
    }

    public BigDecimal getSq1() {
        return sq1;
    }

    public void setSq1(BigDecimal sq1) {
        this.sq1 = sq1;
    }

    public BigDecimal getSq2() {
        return sq2;
    }

    public void setSq2(BigDecimal sq2) {
        this.sq2 = sq2;
    }

    public BigDecimal getSq3() {
        return sq3;
    }

    public void setSq3(BigDecimal sq3) {
        this.sq3 = sq3;
    }

    public BigDecimal getSq4() {
        return sq4;
    }

    public void setSq4(BigDecimal sq4) {
        this.sq4 = sq4;
    }

    public BigDecimal getSh1() {
        return sh1;
    }

    public void setSh1(BigDecimal sh1) {
        this.sh1 = sh1;
    }

    public BigDecimal getSh2() {
        return sh2;
    }

    public void setSh2(BigDecimal sh2) {
        this.sh2 = sh2;
    }

    public BigDecimal getSfy() {
        return sfy;
    }

    public void setSfy(BigDecimal sfy) {
        this.sfy = sfy;
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
        if (!(object instanceof ScorecardExplanation)) {
            return false;
        }
        ScorecardExplanation other = (ScorecardExplanation) object;
        if (this.id != null && other.id != null) {
            return Objects.equals(this.id, other.id);
        }
        return Objects.equals(this.pid, other.pid) && Objects.equals(this.seq, other.seq);
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.ScorecardExplanation[ id=" + id + " ]";
    }

}
