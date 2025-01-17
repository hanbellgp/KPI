/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.EmployeeMail;
import cn.hanbell.kpi.entity.Indicator;
import java.math.BigDecimal;
import java.util.ArrayList;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class AAEmployeeMailBean extends EmployeeMail {

    public AAEmployeeMailBean() {

    }

    @Override
    public void init() {
        sumList = new ArrayList<>();
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    public String getMailBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getQuantityTable());
        sb.append("<div class=\"tableTitle\">单位：元</div>");
        sb.append(getAmountTable());

        return sb.toString();
    }

    protected String getQuantityTable() {
        sumList.clear();
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\" width=\"8%\" >区域</th><th rowspan=\"2\" colspan=\"1\" width=\"8%\" >业务员</th>");
            sb.append("<th rowspan=\"1\" colspan=\"2\">本日</th><th rowspan=\"1\" colspan=\"2\">本月</th><th rowspan=\"1\" colspan=\"3\">年累计</th>");
            sb.append("</tr>");
            sb.append("<tr><th colspan=\"1\" width=\"10%\">订单台数</th><th colspan=\"1\" width=\"10%\">出货台数</th><th colspan=\"1\" width=\"10%\">订单台数</th><th colspan=\"1\" width=\"10%\">出货台数</th>");
            sb.append("<th colspan=\"1\" width=\"10%\">出货台数</th><th colspan=\"1\" width=\"10%\">目标台数</th><th colspan=\"1\">目标台数达成率</th>");
            sb.append("</tr>");

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组营销一课个人销售台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "营销一课"));
            } else {
                sb.append("<tr style=\"text-align:center;\" ><td  colspan=\"9\">A机组营销一课个人销售台数设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组营销二课个人销售台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "营销二课"));
            } else {
                sb.append("<tr><td style=\"text-align:center;\"  colspan=\"9\">A机组营销二课个人销售台数设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组外销个人销售台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "外销"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"9\">A机组外销个人销售台数数设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组张文君SDS个人销售台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, false, "SDS"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"9\">A机组张文君SDS个人销售台数设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组张文君AA个人销售台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, false, "AA"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"9\">A机组张文君AA个人销售台数设定错误</td></tr>");
            }
//            this.indicators.clear();
//            this.indicators = indicatorBean.findByCategoryAndYear("A机组营销高专个人销售台数", y);
//            indicatorBean.getEntityManager().clear();
//            if (indicators != null && !indicators.isEmpty()) {
//                sb.append(getHtmlTable(this.indicators, y, m, d, false, "营销高专"));
//            } else {
//                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"9\">A机组营销高专个人销售台数设定错误</td></tr>");
//            }
            if (sumList != null) {
                total = getSumValue(sumList);
                total.setUsername("");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append("<tr style=\"color:Red;font-weight:bold;\" >");
                sb.append("<td>").append("合计").append("</td>");
                sb.append(getHtmlTableRow(total, y, m, d));
                sb.append("</tr>");
            }
            sb.append("</table></div>");
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }

    protected String getAmountTable() {
        sumList.clear();
        sum1 = BigDecimal.ZERO;
        sum2 = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        Indicator total;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\" width=\"8%\">区域</th><th rowspan=\"2\" colspan=\"1\" width=\"8%\">业务员</th>");
            sb.append("<th rowspan=\"1\" colspan=\"2\" >本日</th><th rowspan=\"1\" colspan=\"2\" >本月</th><th rowspan=\"1\" colspan=\"3\" width=\"10%\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年累计应收账款</th>");
            sb.append("</tr>");
            sb.append("<tr><th colspan=\"1\" width=\"10%\">订单金额</th><th colspan=\"1\" width=\"10%\">出货金额</th><th colspan=\"1\" width=\"10%\">订单金额</th>");
            sb.append("<th colspan=\"1\" width=\"10%\">出货金额</th><th colspan=\"1\" width=\"10%\">出货金额</th><th colspan=\"1\" width=\"10%\">目标金额</th><th colspan=\"1\" width=\"10%\" >目标金额达成率</th>");
            sb.append("</tr>");
            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组营销一课个人销售金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "营销一课"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"10\">A机组营销一课个人销售金额设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组营销二课个人销售金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "营销二课"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"10\">A机组营销二课个人销售金额设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组外销个人销售金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "外销"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"10\">A机组外销个人销售金额设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组张文君SDS个人销售金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, false, "SDS"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"10\">A机组张文君SDS个人销售金额设定错误</td></tr>");
            }

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("A机组张文君AA个人销售金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                sb.append(getHtmlTable(this.indicators, y, m, d, false, "AA"));
            } else {
                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"10\">A机组张文君AA个人销售金额设定错误</td></tr>");
            }

//            this.indicators.clear();
//            this.indicators = indicatorBean.findByCategoryAndYear("A机组营销高专个人销售金额", y);
//            indicatorBean.getEntityManager().clear();
//            if (indicators != null && !indicators.isEmpty()) {
//                sb.append(getHtmlTable(this.indicators, y, m, d, false, "营销高专"));
//            } else {
//                sb.append("<tr><td  style=\"text-align:center;\"  colspan=\"10\">A机组营销高专个人销售金额设定错误</td></tr>");
//            }
            if (sumList != null) {
                total = getSumValue(sumList);
                total.setUsername("");
                getData().put("sum1", sum1);
                getData().put("sum2", sum2);
                sb.append("<tr  style=\"color:Red;font-weight:bold;\">");
                sb.append("<td>").append("合计").append("</td>");
                sb.append(getHtmlTableRow(total, y, m, d));
                sb.append("</tr>");
            }
            sb.append("</table></div>");
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        StringBuilder sb = new StringBuilder();
        sb.append("</div>");//对应Head中的div.content
        sb.append("<div class=\"divFoot\">此报表由系统自动发送,请不要直接回复</div>");
        sb.append("<div class=\"divFoot\">报表管理员</div>");
        sb.append("</div></body></html>");
        return sb.toString();
    }
}
