/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.MailSetting;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C0160
 */
public abstract class MailNotification {

    @EJB
    protected MailSettingBean mailSettingBean;

    @EJB
    protected IndicatorBean indicatorBean;

    protected MailNotify mail;

    protected String mailFrom = "noreply@hanbell.com.cn";
    protected String mailContent;
    protected String mailSubject;

    protected String company;

    protected MailSetting mailSetting;
    protected List<String> to;
    protected List<String> cc;
    protected List<String> bcc;
    protected List<File> attachments;

    protected String css = "<style type='text/css'>body{font-size:14px;}div.content{margin:auto;text-align:center;}div.tbl{margin-bottom:20px;}table{margin:auto;border-spacing:0px;border:1px solid #A2C0DA;}th,td{padding:5px;border-collapse:collapse;text-align:left;}th{background:#B0D0FC;border:1px solid #000000;text-align:center;font-weight:bold;}td{background:#D3E5FD;border:1px solid #000000;text-align:right;}.title{font-size:14px;font-weight:bold;}.foot{font-size:14px;color:Red;}.divFoot{text-align:right;height:20px;width:100%;}div.tableTitle{float:left;font-size:14px;font-weight:bold;text-align:left;}</style>";

    protected List<Indicator> indicators;
    protected HashMap<String, BigDecimal> data;

    protected Calendar c;
    protected int y;
    protected int m;
    protected Date d;

    protected DecimalFormat decimalFormat;

    protected Actual updateActual;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.kpi");

    public MailNotification() {
        this.c = Calendar.getInstance();
        this.to = new ArrayList();
        this.cc = new ArrayList();
        this.bcc = new ArrayList();
        this.attachments = new ArrayList();
        this.indicators = new ArrayList();
        this.data = new HashMap<>();
        this.decimalFormat = new DecimalFormat("#,###");
    }

    public void init() {
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
        this.d = c.getTime();
        if (this.indicators != null) {
            this.indicators.clear();
        }
        this.to = mailSettingBean.findRecipientTo(mailSetting.getFormid());
        this.cc = mailSettingBean.findRecipientCc(mailSetting.getFormid());
        this.bcc = mailSettingBean.findRecipientBcc(mailSetting.getFormid());
        this.mailSubject = mailSetting.getName();
        this.attachments.clear();
    }

    public void addAttachments(File f) {
        this.attachments.add(f);
    }

    public void addIndicators(Indicator i) {
        this.indicators.add(i);
    }

    protected abstract String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) throws Exception;

    protected abstract String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception;

    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    protected abstract String getMailBody() throws Exception;

    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");//对应body中的div.content
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }

    public void setMailContent() throws Exception {
        mailContent = getMailHead() + getMailBody() + getMailFooter();
        if(this.mailContent.contains("Exception")){
            throw new Exception(getMailHead()+"有异常，请查核");
        }
    }

    public void setMailContent(String ex) {
        mailContent = getMailHead() + ex + getMailFooter();
    }

    public void setMailSubject() {
        this.mailSubject = this.mailSubject + BaseLib.formatDate("yyyy/MM/dd", d);
    }

    public BigDecimal getPercent(BigDecimal up, BigDecimal down, int scale) {
        return up.divide(down, scale, RoundingMode.HALF_UP);
    }

    public void notify(MailNotify mail) {
        this.mail = mail;
        this.mail.sendMail(this);
    }

    public String percentFormat(double value) {
        return percentFormat(value, 2);
    }

    public String percentFormat(double value, int scale) {
        BigDecimal b = new BigDecimal(value);
        return percentFormat(b, scale);
    }

    public String percentFormat(BigDecimal value) {
        return percentFormat(value, 2);
    }

    public String percentFormat(BigDecimal value, int scale) {
        return String.format("%s%%", value.setScale(scale, RoundingMode.HALF_UP).toString());
    }

    public String percentFormat(Object o) {
        return percentFormat(o, 2);
    }

    public String percentFormat(Object o, int scale) {
        double value = Double.valueOf(o.toString());
        return percentFormat(value, scale);
    }

    public void setDecimalFormat(String format) {
        this.decimalFormat.applyPattern(format);
    }

    public void sumAdditionalData(String k, BigDecimal value) {
        try {
            getData().merge(k, value, (K, V) -> {
                return K.add(V);
            });
        } catch (Exception ex) {

        }
    }

    /**
     * @return the to
     */
    public List<String> getTo() {
        return to;
    }

    /**
     * @return the cc
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * @return the bcc
     */
    public List<String> getBcc() {
        return bcc;
    }

    /**
     * @return the attachments
     */
    public List<File> getAttachments() {
        return attachments;
    }

    /**
     * @return the indicator
     */
    public List<Indicator> getIndicators() {
        return indicators;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @return the m
     */
    public int getM() {
        return m;
    }

    /**
     * @return the d
     */
    public Date getD() {
        return d;
    }

    /**
     * @param d the d to set
     */
    public void setD(Date d) {
        this.d = d;
        this.c.setTime(d);
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
    }

    /**
     * @return the data
     */
    public HashMap<String, BigDecimal> getData() {
        return data;
    }

}
