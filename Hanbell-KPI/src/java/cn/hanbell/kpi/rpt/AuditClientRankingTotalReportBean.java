/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "auditClientRankingTotalReportBean")
@ViewScoped
public class AuditClientRankingTotalReportBean extends AuditClientRankingReportBean implements Serializable {

    public AuditClientRankingTotalReportBean() {
        super();
    }
   
    @Override
    public void query() {
        year = getCalendar(querydate).get(Calendar.YEAR);
        month = getCalendar(querydate).get(Calendar.MONTH) + 1;
        try {
            Boolean aa = true;
            if (querydate.compareTo(new Date()) == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "查询时间不可超过当前日期"));
                aa = false;
            }
            if (aa) {
                //显示标题
                if (monthchecked) {
                    map.put("title", year + "年" + month + "月");
                    map.put("ulttitle", month == 1 ? (year - 1) + "年12月" : "上月");
                    map.put("nowtitle", "当月");
                } else {
                    if (month > 1) {
                        map.put("title", year + "年1月至" + month + "月累计");
                    } else {
                        map.put("title", "1月");
                    }
                    map.put("nowtitle", "本年");
                }
                list = salesTableBean.getClientList(year, month, map, monthchecked, aggregatechecked, rowsPerPage);
                if (list.size() <= 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
                } else {
                    super.getRemarkOne(indicatorChart, year, month);
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
        }
    }

}
