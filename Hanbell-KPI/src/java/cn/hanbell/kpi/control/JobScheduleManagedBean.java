/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.JobScheduleBean;
import cn.hanbell.kpi.entity.JobSchedule;
import cn.hanbell.kpi.lazy.JobScheduleModel;
import cn.hanbell.kpi.web.FormSingleBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "jobScheduleManagedBean")
@SessionScoped
public class JobScheduleManagedBean extends FormSingleBean<JobSchedule> {

    @EJB
    private JobScheduleBean jobScheduleBean;

    public JobScheduleManagedBean() {
        super(JobSchedule.class);
    }

    @Override
    public void create() {
        super.create();
        newEntity.setFormdate(getDate());
        setCurrentEntity(newEntity);
    }

    @Override
    public void init() {
        superEJB = jobScheduleBean;
        model = new JobScheduleModel(jobScheduleBean);
        super.init();
    }

}
