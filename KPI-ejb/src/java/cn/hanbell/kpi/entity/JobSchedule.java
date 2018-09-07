/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import com.lightshell.comm.FormEntity;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author C0160
 */
@Entity
@Table(name = "jobschedule")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobSchedule.findAll", query = "SELECT j FROM JobSchedule j")
    , @NamedQuery(name = "JobSchedule.findById", query = "SELECT j FROM JobSchedule j WHERE j.id = :id")
    , @NamedQuery(name = "JobSchedule.findByFormid", query = "SELECT j FROM JobSchedule j WHERE j.formid = :formid")
    , @NamedQuery(name = "JobSchedule.findByFormkind", query = "SELECT j FROM JobSchedule j WHERE j.formkind = :formkind")
    , @NamedQuery(name = "JobSchedule.findByDescription", query = "SELECT j FROM JobSchedule j WHERE j.description = :description")
    , @NamedQuery(name = "JobSchedule.findByStatus", query = "SELECT j FROM JobSchedule j WHERE j.status = :status")})
public class JobSchedule extends FormEntity {

    @Size(max = 10)
    @Column(name = "formkind")
    private String formkind;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "description")
    private String description;
    @Size(max = 45)
    @Column(name = "sec")
    private String sec;
    @Size(max = 45)
    @Column(name = "min")
    private String min;
    @Size(max = 45)
    @Column(name = "hr")
    private String hr;
    @Size(max = 45)
    @Column(name = "dayOfMonth")
    private String dayOfMonth;
    @Column(name = "dayOfWeek")
    private String dayOfWeek;
    @Size(max = 45)
    @Column(name = "m")
    private String m;
    @Size(max = 45)
    @Size(max = 45)
    @Column(name = "y")
    private String y;
    @Size(max = 45)
    @Column(name = "start")
    private String start;
    @Size(max = 45)
    @Column(name = "end")
    private String end;
    @Size(max = 200)
    @Column(name = "remark")
    private String remark;

    public JobSchedule() {
    }

    public String getFormkind() {
        return formkind;
    }

    public void setFormkind(String formkind) {
        this.formkind = formkind;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getHr() {
        return hr;
    }

    public void setHr(String hr) {
        this.hr = hr;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        if (!(object instanceof JobSchedule)) {
            return false;
        }
        JobSchedule other = (JobSchedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cn.hanbell.kpi.entity.JobSchedule[ id=" + id + " ]";
    }

}
