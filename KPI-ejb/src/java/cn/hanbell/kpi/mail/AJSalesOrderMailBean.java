/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.SalesOrderMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class AJSalesOrderMailBean extends SalesOrderMail {

    public AJSalesOrderMailBean() {
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">按机型统计 单位：台</div>");
        sb.append(getQuantityTableDomesticSale());

        sb.append("<div class=\"tableTitle\">按机型统计 单位：万元</div>");
        sb.append(getAmountTableDomesticSale());

        sb.append("<div class=\"tableTitle\">按区域统计 单位：台</div>");
        sb.append(getQuantityNXTable());

        sb.append("<div class=\"tableTitle\">按区域统计 单位：万元</div>");
        sb.append(getAmountNXTable());

        sb.append("<div class=\"tableTitle\">外销统计 单位：台</div>");
        sb.append(getQuantityWXTable());

        sb.append("<div class=\"tableTitle\">外销统计 单位：万元</div>");
        sb.append(getAmountWXTable());

        sb.append("<div class=\"tableTitle\">涡旋统计 单位：台</div>");
        sb.append(getQuantityTable_SAMAll());

        sb.append("<div class=\"tableTitle\">涡旋统计 单位：万元</div>");
        sb.append(getAmountTable_SAMAll());

        return sb.toString();
    }

    protected String getQuantityTableDomesticSale() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体订单台数-按机型", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单台数-按机型设定错误";
        }
    }

    protected String getAmountTableDomesticSale() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体订单金额-按机型", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单金额-按机型设定错误";
        }
    }

    protected String getQuantityNXTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体订单台数-内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单台数-内销设定错误";
        }
    }

    protected String getAmountNXTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体订单金额-内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单金额-内销设定错误";
        }
    }

    protected String getQuantityWXTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体订单台数-外销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单台数-外销设定错误";
        }
    }

    protected String getAmountWXTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体订单金额-外销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体订单金额-外销设定错误";
        }
    }

    protected String getQuantityTable_SAMAll() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日订单台数-涡旋", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日订单台数-涡旋设定错误";
        }
    }

    protected String getAmountTable_SAMAll() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日订单金额-涡旋", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日订单金额-涡旋设定错误";
        }
    }
}
