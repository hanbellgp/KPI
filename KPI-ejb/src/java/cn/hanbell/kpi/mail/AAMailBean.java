/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.entity.Indicator;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class AAMailBean extends MailNotification {

    public AAMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getServiceTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机组每日出货台数", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机组每日出货台数设定错误";
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机组每日出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机组每日出货金额设定错误";
        }
    }

    protected String getServiceTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机组收费服务金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            return getHtmlTable(this.indicators, y, m, d, false);
        } else {
            return "A机组收费服务金额设定错误";
        }
    }

}
