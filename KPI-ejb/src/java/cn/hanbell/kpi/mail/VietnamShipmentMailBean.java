/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.ShipmentMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderAmountVN;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import cn.hanbell.kpi.evaluation.SalesOrderQuantityVN;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class VietnamShipmentMailBean extends ShipmentMail {

    public VietnamShipmentMailBean() {
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
        sb.append("<div class=\"tableTitle\">单位：百万 越南盾</div>");
        sb.append(getAmountTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        try {
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("越南出货台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                //越南明细中无归类栏位，不适用上海汉钟的分类方法。
                salesOrder = new SalesOrderQuantityVN();
                return getHtmlTable(this.indicators, y, m, d, true);
            } else {
                return "越南出货台数设定错误";
            }
        } catch (Exception ex) {
            return ex.toString();
        }

    }

    protected String getAmountTable() {
        try {
            indicators.clear();
            //越南的出货金额 = 整机出货金额 + 零件收费服务金额
            indicators = indicatorBean.findByCategoryAndYear("越南出货金额", y);
            indicators.addAll(indicatorBean.findByCategoryAndYear("越南出货收费服务金额", y));
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.stream().forEach((i) -> {
                    indicatorBean.divideByRate(i, 2);
                });
                //越南明细中无归类栏位，不适用上海汉钟的分类方法。
                salesOrder = new SalesOrderAmountVN();
                return getHtmlTable(this.indicators, y, m, d, true);
            } else {
                return "越南出货金额设定错误";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

}
