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
public class SHBSalesOrderMailBean extends SalesOrderMail {

    protected List<Indicator> sumList;
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;

    public SHBSalesOrderMailBean() {
    }

    @Override
    public void init() {
        sumList = new ArrayList<>();
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getSHBAmountTable());
        sb.append("<div class=\"tableTitle\">单位：万元</div>");
        sb.append(getComerAmountTable());
        sb.append("<div class=\"tableTitle\">单位：百万越南盾</div>");
        sb.append(getVHBAmountTable());
        return sb.toString();
    }

    protected String getQuantityTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            salesOrder = new SalesOrderQuantity();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("R冷媒订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("R制冷订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体订单台数-按机型", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机体订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));
            
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体每日订单台数-涡旋", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("S涡旋订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机组订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机组订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("SDS无油订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("SDS无油订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("P订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("P真空订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("涡轮每日订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("涡轮订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("再生订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("再生订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("越南订单台数", y);
            indicatorBean.getEntityManager().clear();
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("越南订单台数");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getSHBAmountTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sumList.clear();
            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("R冷媒订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("R制冷订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体订单金额-按机型", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机体订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));
            
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机体每日订单金额-涡旋", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("S涡旋订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("A机组订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("A机组订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("SDS无油订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("SDS无油订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("P订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("P真空订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            total = indicatorBean.getSumValue(sumList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("合计");
                getData().put("sum1", sum1);
                sb.append(getHtmlTableRow(total, y, m, d));
            }

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getComerAmountTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");

            sum1 = BigDecimal.ZERO;
            sumList.clear();
            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("涡轮每日订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("涡轮订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("再生订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("再生订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));
            sumList.add(total);
            sum1 = sum1.add(getData().get("sum1"));

            total = indicatorBean.getSumValue(sumList);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("合计");
                getData().put("sum1", sum1);
                sb.append(getHtmlTableRow(total, y, m, d));
            }

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getVHBAmountTable() {
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\">产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            salesOrder = new SalesOrderAmount();

            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("越南订单金额", y);
            indicatorBean.getEntityManager().clear();
            indicators.stream().forEach((i) -> {
                indicatorBean.divideByRate(i, 2);
            });
            getHtmlTable(indicators, y, m, d, true);
            total = getSumIndicator();
            total.setName("越南订单金额");
            sb.append(getHtmlTableRow(total, y, m, d));

            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

}
