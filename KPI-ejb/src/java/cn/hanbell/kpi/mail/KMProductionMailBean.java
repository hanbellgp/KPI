/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.BscProductiontMail;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class KMProductionMailBean extends BscProductiontMail {

    public KMProductionMailBean() {
        
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }
    
    @Override
    protected String getMailBody() {
        indicator = indicatorBean.findByFormidYearAndDeptno("Q-柯茂机组每日生产", y, "1N000");
        if (indicator == null) {
            throw new UnsupportedOperationException("Not found 每日生产");
        }
        indicators.clear();
        indicators.add(indicator);
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">离心机组生产计划 单位：").append(indicator.getUnit()).append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        
        indicator = indicatorBean.findByFormidYearAndDeptno("Q-柯茂机体每日生产", y, "1N000");
        if (indicator == null) {
            throw new UnsupportedOperationException("Not found 每日生产");
        }
        indicators.clear();
        indicators.add(indicator);
        sb.append("<div class=\"tableTitle\">离心机体生产计划 单位：").append(indicator.getUnit()).append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        return sb.toString();
    }

    
}
