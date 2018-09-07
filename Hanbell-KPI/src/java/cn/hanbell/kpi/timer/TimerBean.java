/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.timer;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.JobScheduleBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.JobSchedule;
import cn.hanbell.kpi.entity.MailSetting;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C0160
 */
@Singleton
@Startup
public class TimerBean {

    @EJB
    private IndicatorBean indicatorBean;
    @EJB
    private JobScheduleBean jobScheduleBean;
    @EJB
    private MailSettingBean mailSettingBean;

    @Resource
    TimerService timerService;

    private final Logger log4j = LogManager.getLogger();

    public TimerBean() {

    }

    @PostConstruct
    public void construct() {
        List<JobSchedule> jobs = jobScheduleBean.findByStatus("V");
        if (jobs != null && !jobs.isEmpty()) {
            ScheduleExpression se;
            for (JobSchedule j : jobs) {
                se = new ScheduleExpression();
                if (j.getSec() != null && !"".equals(j.getSec())) {
                    se.second(j.getSec());
                }
                if (j.getMin() != null && !"".equals(j.getMin())) {
                    se.minute(j.getMin());
                }
                if (j.getHr() != null && !"".equals(j.getHr())) {
                    se.hour(j.getHr());
                }
                if (j.getDayOfWeek() != null && !"".equals(j.getDayOfWeek())) {
                    se.dayOfWeek(j.getDayOfWeek());
                }
                if (j.getDayOfMonth() != null && !"".equals(j.getDayOfMonth())) {
                    se.dayOfMonth(j.getDayOfMonth());
                }
                if (j.getM() != null && !"".equals(j.getM()) && !"*".equals(j.getM())) {
                    se.month(j.getM());
                }
                if (j.getY() != null && !"".equals(j.getY()) && !"*".equals(j.getY())) {
                    se.year(j.getY());
                }
                timerService.createCalendarTimer(se, new TimerConfig(j.getFormid(), false));
            }
        }
    }

    @PreDestroy
    public void destroy() {
        for (Timer timer : timerService.getTimers()) {
            log4j.info("Stopping timer:" + timer.getInfo());
            timer.cancel();
        }
    }

    @Timeout
    public void jobScheduler(Timer timer) {
        log4j.info("Begin Execute Job Schedule " + timer.getInfo());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        List<Indicator> indicatorList = indicatorBean.findByJobScheduleAndStatus(timer.getInfo().toString(), "V");
        if (indicatorList != null && !indicatorList.isEmpty()) {
            for (Indicator e : indicatorList) {
                if (e.getActualInterface() != null && !"".equals(e.getActualInterface())) {
                    try {
                        indicatorBean.updateActual(e.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), Calendar.MONTH);
                        log4j.info(String.format("成功执行%s:更新指标%s实际值:Id:%d", "updateIndicatorActualValue", e.getName(), e.getId()));
                    } catch (Exception ex) {
                        log4j.error(String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()), ex);
                    }
                    try {
                        indicatorBean.updatePerformance(e);
                        indicatorBean.update(e);
                        log4j.info(String.format("成功执行%s:更新指标%s达成率:Id:%d", "updateIndicatorActualValue", e.getName(), e.getId()));
                    } catch (Exception ex) {
                        log4j.error(String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()), ex);
                    }
                }
            }
        }
        //部门指标来源产品指标，所以先算产品指标
        log4j.info("updateIndicatorActualValue开始产品指标堆叠计算");
        indicatorList = indicatorBean.findRootByAssignedAndJobSchedule("C", "P", c.get(Calendar.YEAR), timer.getInfo().toString());
        if (indicatorList != null && !indicatorList.isEmpty()) {
            for (Indicator e : indicatorList) {
                try {
                    updateActual(e, c.get(Calendar.MONTH) + 1);
                    log4j.info(String.format("成功执行%s:更新指标%s达成率:Id:%d", "updateIndicatorActualValue", e.getName(), e.getId()));
                } catch (Exception ex) {
                    log4j.error(String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()), ex);
                }
            }
        }
        log4j.info("updateIndicatorActualValue开始部门指标堆叠计算");
        indicatorList = indicatorBean.findRootByAssignedAndJobSchedule("C", "D", c.get(Calendar.YEAR), timer.getInfo().toString());
        if (indicatorList != null && !indicatorList.isEmpty()) {
            for (Indicator e : indicatorList) {
                try {
                    updateActual(e, c.get(Calendar.MONTH) + 1);
                    log4j.info(String.format("成功执行%s:更新指标%s达成率:Id:%d", "updateIndicatorActualValue", e.getName(), e.getId()));
                } catch (Exception ex) {
                    log4j.error(String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()), ex);
                }
            }
        }
        log4j.info("End Execute Job Schedule " + timer.getInfo());
    }

    @Schedule(minute = "3", hour = "10", dayOfWeek = "Tue,Wed,Thu,Fri,Sat", persistent = false)
    public void sendKPIReport() {
        String reportName = "";
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        List<MailSetting> mailSettingList = mailSettingBean.findByStatus("V");
        try {
            for (MailSetting ms : mailSettingList) {
                reportName = ms.getName();
                MailNotification mn = getMailNotificationBean(ms.getMailEJB());
                if (mn != null) {
                    mn.init();
                    mn.setD(c.getTime());
                    mn.setMailContent();
                    mn.setMailSubject();
                    mn.notify(new MailNotify());
                    log4j.info(String.format("成功执行%s:发送报表%s", "sendKPIReport", reportName));
                } else {
                    log4j.info(String.format("执行%s:发送报表%s失败,找不到MailBean", "sendKPIReport", reportName));
                }
            }
        } catch (Exception ex) {
            log4j.error(String.format("执行%s:发送报表%s时异常", "sendKPIReport", reportName), ex);
        }
    }

    private MailNotification getMailNotificationBean(String JNDIName) {
        InitialContext c;
        try {
            c = new InitialContext();
            Object objRef = c.lookup(JNDIName);
            return (MailNotification) objRef;
        } catch (NamingException ex) {
            java.util.logging.Logger.getLogger(TimerBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private void updateActual(Indicator entity, int m) {
        //递归更新某个月份的实际值,不调用ActualInterface计算方法
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateActual(d, m);
                }
            }//先计算子项值
            indicatorBean.updateActual(entity, m);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        } else {
            indicatorBean.updateActual(entity, m);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
        }
    }

}
