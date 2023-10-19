/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.ShipmentMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class AJShipmentMailBean extends ShipmentMail {

    public AJShipmentMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">按机型统计 单位：台</div>");
        sb.append(getQuantityTableDomesticMarket());

        sb.append("<div class=\"tableTitle\">按机型统计 单位：万元</div>");
        sb.append(getAmountTableDomesticMarket());

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

        sb.append("<div class=\"tableTitle\">机体收费统计 单位：万元</div>");
        sb.append(getServiceTable());

        sb.append("<div class=\"tableTitle\">涡旋收费统计 单位：万元</div>");
        sb.append(getServiceTable_SAM());

        sb.append("<div class=\"tableTitle\">机体调拨统计 单位：台</div>");
        sb.append(getTransferTable());
        return sb.toString();
    }

    protected String getQuantityTableDomesticMarket() throws Exception {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数-按机型", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货台数-按机型设定错误";
        }
    }

    protected String getAmountTableDomesticMarket() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额-按机型", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额-按机型设定错误";
        }
    }

    protected String getQuantityNXTable() throws Exception {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数-内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货台数-内销设定错误";
        }
    }

    protected String getAmountNXTable() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额-内销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额-内设定错误";
        }
    }

    protected String getQuantityWXTable() throws Exception {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数-外销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货台数-外销设定错误";
        }
    }

    protected String getAmountWXTable() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额-外销", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额-外销设定错误";
        }
    }
    
 
    protected String getQuantityTable_SAMAll() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货台数-涡旋", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货台数-涡旋设定错误";
        }
    }

    protected String getAmountTable_SAMAll() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体每日出货金额-涡旋", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "A机体每日出货金额-涡旋设定错误";
        }
    }

     protected String getServiceTable() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体收费服务", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = null;
            setDecimalFormat("#,###.00");
            String t = getHtmlTable(this.indicators, y, m, d, true);
            setDecimalFormat("#,###");
            return t;
        } else {
            return "A机体收费服务设定错误";
        }
    }
    
    protected String getServiceTable_SAM() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("涡旋收费服务", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = null;
            setDecimalFormat("#,###.00");
            String t = getHtmlTable(this.indicators, y, m, d, true);
            setDecimalFormat("#,###");
            return t;
        } else {
            return "涡旋收费服务设定错误";
        }
    }

    protected String getTransferTable() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("A机体厂内调拨", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            return getHtmlTable(this.indicators, y, m, d, false);
        } else {
            return "A机体每日出货厂内调拨设定错误";
        }
    }

}
