/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.EmployeeMail;
import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.MailSetting;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C2472
 */
@Stateless
@LocalBean
public class AAEmployeeOtherMailBean extends EmployeeMail {
    
 
    @Override
    public void init() {
        sumList = new ArrayList<>();
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        this.mailSetting = new MailSetting();
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
        this.d = c.getTime();
        if (this.indicators != null) {
            this.indicators.clear();
        }
        this.getTo().clear();
        this.getCc().clear();
        this.getBcc().clear();
        this.attachments.clear();
    }

    @Override
    protected String getMailBody() throws Exception {
        List<Indicator> quantityIndicators = indicatorBean.findByCategoryAndYear("A机组营销一课个人销售台数", y);
        List<Indicator> amountIndicators = indicatorBean.findByCategoryAndYear("A机组营销一课个人销售金额", y);
         StringBuilder content = new StringBuilder();
        for (int i = 0; i < quantityIndicators.size(); i++) {
                 content = new StringBuilder();
            for (int j = 0; j < amountIndicators.size(); j++) {
                if (quantityIndicators.get(i).getUserid().equals(amountIndicators.get(j).getUserid())) {
                    content.append(getOtherMailString(quantityIndicators.get(i), amountIndicators.get(j)));
                    break;
                }
            }
            //amountIndicator.stream().filter(predicate);
            //发送邮箱
            if (this != null) {
                this.init();
                this.setD(d);
                this.setMailContent(content.toString());
                this.getTo().add("C2472@hanbell.com.cn");
                  this.mailSubject = quantityIndicators.get(i).getUsername()+"销售业绩表";
                this.notify(new MailNotify());
                }
             }
        
          quantityIndicators = indicatorBean.findByCategoryAndYear("A机组营销二课个人销售台数", y);
         amountIndicators = indicatorBean.findByCategoryAndYear("A机组营销二课个人销售金额", y);
         content = new StringBuilder();
        for (int i = 0; i < quantityIndicators.size(); i++) {
                 content = new StringBuilder();
            for (int j = 0; j < amountIndicators.size(); j++) {
                if (quantityIndicators.get(i).getUserid().equals(amountIndicators.get(j).getUserid())) {
                    content.append(getOtherMailString(quantityIndicators.get(i), amountIndicators.get(j)));
                    break;
                }
            }
            //amountIndicator.stream().filter(predicate);
            //发送邮箱
            if (this != null) {
                this.init();
                this.setD(d);
                this.setMailContent(content.toString());
                this.getTo().add("C2472@hanbell.com.cn");
                  this.mailSubject = quantityIndicators.get(i).getUsername()+"销售业绩表";
                this.notify(new MailNotify());
                }
             }
        
          quantityIndicators = indicatorBean.findByCategoryAndYear("A机组营销三课个人销售台数", y);
         amountIndicators = indicatorBean.findByCategoryAndYear("A机组营销三课个人销售金额", y);
         content = new StringBuilder();
        for (int i = 0; i < quantityIndicators.size(); i++) {
                 content = new StringBuilder();
            for (int j = 0; j < amountIndicators.size(); j++) {
                if (quantityIndicators.get(i).getUserid().equals(amountIndicators.get(j).getUserid())) {
                    content.append(getOtherMailString(quantityIndicators.get(i), amountIndicators.get(j)));
                    break;
                }
            }
            //amountIndicator.stream().filter(predicate);
            //发送邮箱
            if (this != null) {
                this.init();
                this.setD(d);
                this.setMailContent(content.toString());
                this.getTo().add("C2472@hanbell.com.cn");
                  this.mailSubject = quantityIndicators.get(i).getUsername()+"销售业绩表";
                this.notify(new MailNotify());
                }
             }
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        this.to = mailSettingBean.findRecipientTo(mailSetting.getFormid());
        this.cc = mailSettingBean.findRecipientCc(mailSetting.getFormid());
        this.bcc = mailSettingBean.findRecipientBcc(mailSetting.getFormid());
        this.mailSubject = mailSetting.getName();
        return "发送成功";
    }
     @Override
     protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        return sb.toString();
    }

   
 @Override
    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");//对应body中的div.content
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }
 @Override
    public void setMailContent() throws Exception {
        mailContent = getMailHead() + getMailBody() + getMailFooter();
        if(this.mailContent.contains("Exception")){
            throw new Exception(getMailHead()+"有异常，请查核");
        }
    }


    /**
     * *
     *
     * @param quantityIndicator 台数指标
     * @param AmountIndicator 金额指标
     * @return
     */
    public StringBuilder getOtherMailString(Indicator quantityIndicator, Indicator amountIndicator) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(quantityIndicator.getUsername()).append("销售业绩报表</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr>");
        sb.append("<th rowspan=\"2\" colspan=\"1\" width=\"8%\" >业务员</th>");
        sb.append("<th rowspan=\"1\" colspan=\"2\">本日</th><th rowspan=\"1\" colspan=\"2\">本月</th><th rowspan=\"1\" colspan=\"3\">年累计</th>");
        sb.append("</tr>");
        sb.append("<tr><th colspan=\"1\" width=\"10%\">订单台数</th><th colspan=\"1\" width=\"10%\">出货台数</th><th colspan=\"1\" width=\"10%\">订单台数</th><th colspan=\"1\" width=\"10%\">出货台数</th>");
        sb.append("<th colspan=\"1\" width=\"10%\">出货台数</th><th colspan=\"1\" width=\"10%\">目标台数</th><th colspan=\"1\">目标台数达成率</th>");
        sb.append("</tr>");
        sb.append("<tr>");
        sb.append(getHtmlTableRow(quantityIndicator, y, m, d));
        sb.append("</tr>");
        sb.append("</table>");
        sb.append("</div>");
        sb.append("<br>");
        sb.append("<div class=\"tableTitle\">单位：金额</div>");
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr>");
        sb.append("<th rowspan=\"2\" colspan=\"1\" width=\"8%\">业务员</th>");
        sb.append("<th rowspan=\"1\" colspan=\"2\" >本日</th><th rowspan=\"1\" colspan=\"2\" >本月</th><th rowspan=\"1\" colspan=\"3\" width=\"10%\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年累计应收账款</th>");
        sb.append("</tr>");
        sb.append("<tr><th colspan=\"1\" width=\"10%\">订单金额</th><th colspan=\"1\" width=\"10%\">出货金额</th><th colspan=\"1\" width=\"10%\">订单金额</th>");
        sb.append("<th colspan=\"1\" width=\"10%\">出货金额</th><th colspan=\"1\" width=\"10%\">出货金额</th><th colspan=\"1\" width=\"10%\">目标金额</th><th colspan=\"1\" width=\"10%\" >目标金额达成率</th>");
        sb.append("</tr>");
        sb.append(getHtmlTableRow(amountIndicator, y, m, d));
        sb.append("</table>");
        return sb;
    }
}
