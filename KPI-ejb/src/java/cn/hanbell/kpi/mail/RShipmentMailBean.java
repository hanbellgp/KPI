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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class RShipmentMailBean extends ShipmentMail {

    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;

    public RShipmentMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable_DC());
        sb.append("</br>");
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getAmountTable_DC());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getServiceTable());
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");//对应Head中的div.content
        sb.append("<div style=\"text-align:left;width:100%;color:Red;\">华东销售实际值包括销售给柯茂的台数和金额</div>");
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }

    protected String getQuantityTable() throws Exception {
        this.indicators.clear();
        this.indicators = indicatorBean.findByCategoryAndYear("R冷媒出货台数", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            salesOrder = new SalesOrderQuantity();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒出货台数设定错误";
        }
    }

    protected String getQuantityTable_DC() {
        StringBuilder sb = new StringBuilder();
        Indicator indicator;
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        try {
            List<Indicator> indicatorsR = new ArrayList<>();
            List<Indicator> indicatorsL = new ArrayList<>();
            List<Indicator> indicatorsH = new ArrayList<>();
            List<Indicator> indicatorsZ = new ArrayList<>();
            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            salesOrder = new SalesOrderQuantity();
            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("R冷媒出货台数", y);
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
                    case "盐水":
                        indicatorsZ.add(i);
                        break;
                    default:
                }
            }
            indicators.clear();
            indicator = indicatorBean.getSumValue(indicatorsR);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("空调出货台数合计");
            getHtmlTable(indicatorsR, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicator = indicatorBean.getSumValue(indicatorsH);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("热泵出货台数合计");
            getHtmlTable(indicatorsH, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicator = indicatorBean.getSumValue(indicatorsL);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("冷冻出货台数合计");
            getHtmlTable(indicatorsL, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicator = indicatorBean.getSumValue(indicatorsZ);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("盐水出货台数合计");
            getHtmlTable(indicatorsZ, y, m, d, true);
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
                return "R冷媒出货台数设定错误";
            }
        } catch (Exception ex) {
            return ex.toString();
        }

    }

    protected String getAmountTable() throws Exception {
        this.indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("R冷媒出货金额", y);
        indicatorBean.getEntityManager().clear();
        if (indicators != null && !indicators.isEmpty()) {
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            salesOrder = new SalesOrderAmount();
            return getHtmlTable(this.indicators, y, m, d, true);
        } else {
            return "R冷媒出货金额设定错误";
        }
    }

    protected String getAmountTable_DC() {
        StringBuilder sb = new StringBuilder();
        Indicator indicator;
        getData().clear();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        try {
            List<Indicator> indicatorsR = new ArrayList<>();
            List<Indicator> indicatorsL = new ArrayList<>();
            List<Indicator> indicatorsH = new ArrayList<>();
            List<Indicator> indicatorsZ = new ArrayList<>();
            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            this.indicators.clear();
            salesOrder = new SalesOrderAmount();
            indicators = indicatorBean.findByCategoryAndYear("R冷媒出货金额", y);
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
                    case "盐水":
                        indicatorsZ.add(i);
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
            indicator.setName("空调出货金额合计");

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
            indicator.setName("热泵出货金额合计");
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
            indicator.setName("冷冻出货金额合计");
            getHtmlTable(indicatorsL, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsZ) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsZ);
            indicatorBean.updatePerformance(indicator);
            indicatorBean.getEntityManager().clear();
            indicator.setName("盐水出货金额合计");
            getHtmlTable(indicatorsZ, y, m, d, true);
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
                return "R冷媒出货金额设定错误";
            }
        } catch (Exception ex) {
            return "R冷媒出货金额设定错误";
        }

    }

    protected String getServiceTable() throws Exception {
        StringBuilder sb = new StringBuilder();
        Indicator indicator;
        getData().clear();
        sb.append("<div class=\"tbl\"><table width=\"100%\">");
        sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
        sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
        sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
        sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
        sb.append("</tr>");
        try {
            List<Indicator> indicatorsHD = new ArrayList<>();
            List<Indicator> indicatorsJN = new ArrayList<>();
            List<Indicator> indicatorsGZ = new ArrayList<>();
            List<Indicator> indicatorsNJ = new ArrayList<>();
            List<Indicator> indicatorsCQ = new ArrayList<>();

            sum1 = BigDecimal.ZERO;
            sum2 = BigDecimal.ZERO;
            this.indicators.clear();
            salesOrder = new SalesOrderAmount();
            indicators = indicatorBean.findByCategoryAndYear("R收费服务金额", y);
            for (Indicator i : indicators) {
                switch (i.getProduct().trim()) {
                      case "华东R收费服务":
                        indicatorsHD.add(i);
                        break;
                    case "济南R收费服务":
                        indicatorsJN.add(i);
                        break;
                    case "广州R收费服务":
                        indicatorsGZ.add(i);
                        break;
                    case "南京R收费服务":
                        indicatorsNJ.add(i);
                        break;
                    case "重庆R收费服务":
                        indicatorsCQ.add(i);
                        break;
                    default:
                }
            }
            indicators.clear();
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsHD) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsHD);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("制冷服务课服务收费");

            getHtmlTable(indicatorsHD, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsJN) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsJN);
            indicatorBean.updatePerformance(indicator);
            indicator.setName("济南服务收费");
            getHtmlTable(indicatorsJN, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsGZ) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsGZ);
            indicatorBean.updatePerformance(indicator);
            indicatorBean.getEntityManager().clear();
            indicator.setName("广州服务收费");
            getHtmlTable(indicatorsGZ, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);

            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsNJ) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsNJ);
            indicatorBean.updatePerformance(indicator);
            indicatorBean.getEntityManager().clear();
            indicator.setName("南京R收费服务");
            getHtmlTable(indicatorsNJ, y, m, d, true);
            sum1 = sum1.add(getData().get("sum1"));
            sum2 = sum2.add(getData().get("sum2"));
            sb.append(getHtmlTableRow(indicator, y, m, d));
            indicators.add(indicator);
            
           indicatorBean.getEntityManager().clear();
            for (Indicator i : indicatorsCQ) {
                indicatorBean.divideByRate(i, 2);
            }
            indicator = indicatorBean.getSumValue(indicatorsCQ);
            indicatorBean.updatePerformance(indicator);
            indicatorBean.getEntityManager().clear();
            indicator.setName("重庆R收费服务");
            getHtmlTable(indicatorsCQ, y, m, d, true);
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
                return "R冷媒出货金额设定错误";
            }
        } catch (Exception ex) {
            return "R冷媒出货金额设定错误";
        }
    }

}
