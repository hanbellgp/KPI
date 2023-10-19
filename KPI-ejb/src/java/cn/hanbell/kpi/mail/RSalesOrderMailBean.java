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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class RSalesOrderMailBean extends SalesOrderMail {

    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;

    public RSalesOrderMailBean() {
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
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable_DC());
        sb.append("<br/>");
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable_DC());
        return sb.toString();
    }

    protected String getQuantityTable() {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("R冷媒订单台数", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒订单台数设定错误";
        }
    }

    protected String getQuantityTable_DC() {
        StringBuilder sb = new StringBuilder();
        Indicator indicator;
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        try {
            List<Indicator> indicatorsR = new ArrayList<>();
            List<Indicator> indicatorsL = new ArrayList<>();
            List<Indicator> indicatorsH = new ArrayList<>();
            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            salesOrder = new SalesOrderQuantity();
            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("R冷媒订单台数", y);
            for (Indicator i : indicators) {
                switch (i.getProduct().trim()) {
                    case "空调":
                        indicatorsR.add(i);
                        break;
                    case "热泵":
                        indicatorsH.add(i);
                        break;
                    case "冷冻":
                        indicatorsL.add(i);
                        break;
                    default:
                }
            }
            indicators.clear();
            indicator = indicatorBean.getSumValue(indicatorsR);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("空调订单台数合计");
            getHtmlTable(indicatorsR, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicator = indicatorBean.getSumValue(indicatorsH);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("热泵订单台数合计");
            getHtmlTable(indicatorsH, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicator = indicatorBean.getSumValue(indicatorsL);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("冷冻订单台数合计");
            getHtmlTable(indicatorsL, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicator = indicatorBean.getSumValue(indicators);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("合计");
            getData().put("sum1", sum1);
            getData().put("sum2", sum2);
            sb.append(getHtmlTableRow(indicator, y, m, d));
            sb.append("</table></div>");
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                return sb.toString();
            } else {
                return "R冷媒订单台数设定错误";
            }
        } catch (Exception ex) {
            return ex.toString();
        }
    }

    protected String getAmountTable() {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("R冷媒订单金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒订单金额设定错误";
        }
    }

    protected String getAmountTable_DC() {
        StringBuilder sb = new StringBuilder();
        Indicator indicator;
        getData().clear();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        try {
            List<Indicator> indicatorsR = new ArrayList<>();
            List<Indicator> indicatorsL = new ArrayList<>();
            List<Indicator> indicatorsH = new ArrayList<>();
            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            this.indicators.clear();
            salesOrder = new SalesOrderAmount();
            indicators = indicatorBean.findByCategoryAndYear("R冷媒订单金额", y);
            for (Indicator i : indicators) {
                switch (i.getProduct().trim()) {
                    case "空调":
                        indicatorsR.add(i);
                        break;
                    case "热泵":
                        indicatorsH.add(i);
                        break;
                    case "冷冻":
                        indicatorsL.add(i);
                        break;
                    default:
                }
            }
            indicators.clear();
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsR) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsR);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("空调订单金额合计");

            getHtmlTable(indicatorsR, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsH) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsH);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("热泵订单金额合计");
            getHtmlTable(indicatorsH, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsL) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsL);
            indicatorBean.updatePerformance(indicator);
            indicatorBean.getEntityManager().clear();
            indicator.setName("冷冻订单金额合计");
            getHtmlTable(indicatorsL, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            indicator = indicatorBean.getSumValue(indicators);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("合计");
            getData().put("sum1", sum1);
            getData().put("sum2", sum2);
            sb.append(getHtmlTableRow(indicator, y, m, d));
            sb.append("</table></div>");
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                return sb.toString();
            } else {
                return "R冷媒订单金额设定错误";
            }
        } catch (Exception ex) {
            return "R冷媒订单金额设定错误";
        }
    }
}
