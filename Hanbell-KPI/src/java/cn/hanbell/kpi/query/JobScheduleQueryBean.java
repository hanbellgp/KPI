/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

import cn.hanbell.kpi.ejb.JobScheduleBean;
import cn.hanbell.kpi.entity.JobSchedule;
import cn.hanbell.kpi.lazy.JobScheduleModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "jobScheduleQueryBean")
@ViewScoped
public class JobScheduleQueryBean extends SuperQueryBean<JobSchedule> {

    @EJB
    private JobScheduleBean jobScheduleBean;

    public JobScheduleQueryBean() {
        super(JobSchedule.class);
    }

    @Override
    public void init() {
        superEJB = jobScheduleBean;
        model = new JobScheduleModel(jobScheduleBean);
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryFormId != null) {
                model.getFilterFields().put("formid", queryFormId);
            }
            if (queryName != null && !"".equals(queryName)) {
                model.getFilterFields().put("description", queryName);
            }
        }
    }

}
