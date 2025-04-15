/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanbell.kpi.timer;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.JobScheduleBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.ejb.SalesTableBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.JobSchedule;
import cn.hanbell.kpi.entity.MailSetting;
import cn.hanbell.util.BaseLib;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
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
import vn.hanbell.kpi.ejb.SalesTableVNBean;

/**
 *
 * @author C0160
 */
@Singleton
@Startup
public class TimerBean {

    @EJB
    private SystemUserBean systemuserBean;
    @EJB
    private IndicatorBean indicatorBean;
    @EJB
    private JobScheduleBean jobScheduleBean;
    @EJB
    private MailSettingBean mailSettingBean;
    @EJB
    private SalesTableBean salesTableBean;

    @EJB
    private SalesTableVNBean salesTablevnBean;
    @Resource
    TimerService timerService;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

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
        String reportName = "";
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);
        // KPI指标更新
        List<Indicator> indicatorList = indicatorBean.findByJobScheduleAndStatus(timer.getInfo().toString(), "V");
        if (indicatorList != null && !indicatorList.isEmpty()) {
            log4j.info("Begin Execute KPI Update Job Schedule " + timer.getInfo());
            for (Indicator e : indicatorList) {
                if ((e.getActualInterface() != null && !"".equals(e.getActualInterface())) || (e.getHasOther() > 0)) {
                    // 设计初衷有了实际接口才有其他接口,没有实际接口属于分配或者归集指标,这个规则已被打破
                    // 有实际接口或其他接口才需要按接口方式计算
                    try {
                        if ("D".equals(e.getFormkind().trim())) {
                            indicatorBean.updateActual(e.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                                    c.getTime(), Calendar.DAY_OF_MONTH);
                        } else {
                            indicatorBean.updateActual(e.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                                    c.getTime(), Calendar.MONTH);
                        }
                        log4j.info(String.format("成功执行%s:更新指标%s实际值:Id:%d", "updateIndicatorActualValue", e.getName(),
                                e.getId()));
                    } catch (Exception ex) {
                        log4j.error(
                                String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()),
                                ex);
                    }
                    try {
                        indicatorBean.updatePerformance(e);
                        Indicator newEntity=indicatorBean.findById(e.getId());
                        indicatorBean.update(newEntity);
                        log4j.info(String.format("成功执行%s:更新指标%s达成率:Id:%d。达成率是:%s", "updateIndicatorActualValue", e.getName(),
                                e.getId(),newEntity.getPerformanceIndicator().getN01()));
                    } catch (Exception ex) {
                        log4j.error(
                                String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()),
                                ex);
                    }
                } else {
                    // 独立归集指标计算
                    if (e.getPid() == 0) {
                        updateActual(e, c.get(Calendar.MONTH) + 1);
                    }
                }
            }
            // 部门指标来源产品指标，所以先算产品指标
            log4j.info("updateIndicatorActualValue开始产品指标堆叠计算");
            indicatorList = indicatorBean.findRootByAssignedObjtypeAndJobSchedule("P", c.get(Calendar.YEAR),
                    timer.getInfo().toString());
            if (indicatorList != null && !indicatorList.isEmpty()) {
                for (Indicator e : indicatorList) {
                    try {
                        updateActual(e, c.get(Calendar.MONTH) + 1);
                        log4j.info(String.format("成功执行%s:更新指标%s达成率:Id:%d", "updateIndicatorActualValue", e.getName(),
                                e.getId()));
                    } catch (Exception ex) {
                        log4j.error(
                                String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()),
                                ex);
                    }
                }
            }
            log4j.info("updateIndicatorActualValue开始部门指标堆叠计算");
            indicatorList = indicatorBean.findRootByAssignedObjtypeAndJobSchedule("D", c.get(Calendar.YEAR),
                    timer.getInfo().toString());
            if (indicatorList != null && !indicatorList.isEmpty()) {
                for (Indicator e : indicatorList) {
                    try {
                        updateActual(e, c.get(Calendar.MONTH) + 1);
                        log4j.info(String.format("成功执行%s:更新指标%s达成率:Id:%d", "updateIndicatorActualValue", e.getName(),
                                e.getId()));
                    } catch (Exception ex) {
                        log4j.error(
                                String.format("执行%s:更新指标%s:Id:%d时异常", "updateIndicatorActualValue", e.getName(), e.getId()),
                                ex);
                    }
                }
            }
            log4j.info("End Execute KPI Update Job Schedule " + timer.getInfo());
        }
        // KPI报表发送
        List<MailSetting> mailSettingList = mailSettingBean.findByJobScheduleAndStatus(timer.getInfo().toString(), "V");
        if (mailSettingList != null && !mailSettingList.isEmpty()) {
            log4j.info("Begin Execute Send KPI Report Job Schedule " + timer.getInfo());
            for (MailSetting ms : mailSettingList) {
                try {
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
                } catch (Exception ex) {
                    try {

                        List<String> list = systemuserBean.tryFindExceptionInformUsers();
                        StringBuffer users = new StringBuffer();
                        MailNotification mn = getMailNotificationBean(ms.getMailEJB());
                        if (list != null && !list.isEmpty()) {
                            mn.getTo().clear();
                            mn.getCc().clear();
                            for (String s : list) {
                                users.append(s).append("|");
                                mn.getTo().add(s + "@hanbell.com.cn");
                            }

                            // 企业微信推送
                            indicatorBean.sendMsgString(users.substring(0, users.length() - 1),
                                    "【KPI出货报表 At " + BaseLib.formatDate("yyyy/MM/dd HH:mm:ss", new Date()) + "】" + reportName
                                    + "发生异常，具体内容已通过邮件发送!");
                            // 邮件推送
                            reportName = ms.getName();
                            mn.setMailSubject();
                            mn.setMailContent(reportName + "异常：" + getExceptionInfo(ex) + "\n请管理员及时处理！！！");
                            mn.notify(new MailNotify());
                        }
                    } catch (Exception e) {
                        log4j.info("End Exception Send KPI Report Job Schedule " + e.toString());
                    }
                }
                log4j.info("End Execute Send KPI Report Job Schedule " + timer.getInfo());
            }
        }
    }

    private MailNotification getMailNotificationBean(String JNDIName) {
        InitialContext c;
        try {
            c = new InitialContext();
            Object objRef = c.lookup(JNDIName);
            return (MailNotification) objRef;
        } catch (NamingException ex) {
            log4j.error("getMailNotificationBean", ex);
        }
        return null;
    }

    private void updateActual(Indicator entity, int m) {
        // 递归更新某个月份的实际值,不调用ActualInterface计算方法
        if (entity.isAssigned()) {
            List<Indicator> details = indicatorBean.findByPId(entity.getId());
            if (details != null && !details.isEmpty()) {
                for (Indicator d : details) {
                    updateActual(d, m);
                }
            } // 先计算子项值
            indicatorBean.updateActual(entity, m);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
            log4j.info(
                    String.format("成功执行%s:堆叠指标%s:Id:%d", "updateIndicatorActualValue", entity.getName(), entity.getId()));
        } else {
            indicatorBean.updateActual(entity, m);
            indicatorBean.updatePerformance(entity);
            indicatorBean.update(entity);
            log4j.info(
                    String.format("成功执行%s:堆叠指标%s:Id:%d", "updateIndicatorActualValue", entity.getName(), entity.getId()));
        }
    }

    @Schedule(minute = "30", hour = "5", dayOfMonth = "1", persistent = false)
    public void updateKPISalesTable() {
        try {
            log4j.info("Begin Execute Job updateKPISalesTable");
            Calendar now = Calendar.getInstance();
            int year = now.get(Calendar.YEAR);
            int month = (now.get(Calendar.MONTH) + 1);
            // 本月更新上个月
            int y, m;
            if (month == 1) {
                m = 12;
                y = year - 1;
            } else {
                m = month - 1;
                y = year;
            }
            if (salesTableBean.updateSalesTable(y, m, "", "Shipment")
                    && salesTableBean.updateSalesTable(y, m, "", "SalesOrder")
                    && salesTableBean.updateSalesTable(y, m, "", "ServiceAmount")) {

            }

            if (salesTablevnBean.updateSalesTable(y, m, "", "Shipment")
                    && salesTablevnBean.updateSalesTable(y, m, "", "SalesOrder")
                    && salesTablevnBean.updateSalesTable(y, m, "", "ServiceAmount")) {
            }
            log4j.info("End Execute Job updateKPISalesTable");
        } catch (Exception e) {
            log4j.error(String.format("出货、订单、收费服务历史表归档更新异常", "updateKPISalesTable"), e.toString());
        }
    }

    @Schedule(minute = "30", hour = "2", persistent = false)
    public void updateKPISalesTableDays() {
        try {
            log4j.info("Begin Execute Job updateKPISalesTableDays");
            Calendar now = Calendar.getInstance();
            int y = now.get(Calendar.YEAR);
            int m = (now.get(Calendar.MONTH) + 1);
            if (salesTableBean.updateSalesTable(y, m, "", "Shipment")
                    && salesTablevnBean.updateSalesTable(y, m, "", "Shipment")) {
                log4j.info("End Execute Job updateKPISalesTableDays");
            }
        } catch (Exception e) {
            log4j.error(String.format("当月出货资料归档更新异常", "updateKPISalesTableDays"), e.toString());
        }
    }
    
    


    public String getExceptionInfo(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(out);
        ex.printStackTrace(pout);
        String ret = new String(out.toByteArray());
        pout.close();
        try {
            out.close();
        } catch (Exception e) {
        }
        return ret;
    }
}
