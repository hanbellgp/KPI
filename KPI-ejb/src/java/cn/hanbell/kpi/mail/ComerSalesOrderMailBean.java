/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SalesOrderMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.evaluation.SalesOrderAmount;
import cn.hanbell.kpi.evaluation.SalesOrderQuantity;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ComerSalesOrderMailBean extends SalesOrderMail {

    protected List<Indicator> sumList;
    protected BigDecimal sum1 = BigDecimal.ZERO;
    protected BigDecimal sum2 = BigDecimal.ZERO;
    protected BigDecimal sumC1 = BigDecimal.ZERO;
    protected BigDecimal sumC2 = BigDecimal.ZERO;
    //上海柯茂每月实际
    protected BigDecimal actualNum1 = BigDecimal.ZERO;
    //浙江柯茂每月实际
    protected BigDecimal actualNum2 = BigDecimal.ZERO;

    public ComerSalesOrderMailBean() {
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
        sb.append(getComerQuantityTable());
        sb.append("<div class=\"tableTitle\"  style=\"margin-top: 30px\" >单位：万元</div>");
        sb.append(getComerAmountTable());
        sb.append("<div class=\"tableTitle\">单位：台</div>");
        sb.append(getZJComerQuantityTable());
        sb.append("<div class=\"tableTitle\"  style=\"margin-top: 30px\" >单位：万元</div>");
        sb.append(getZJComerAmountTable());
        return sb.toString();
    }

    protected String getComerQuantityTable() {
        StringBuilder sb = new StringBuilder();
        String deptno = "5C000";
        Indicator indicator;
        Indicator total;
        try {

            this.indicators.clear();
            this.indicators = indicatorBean.findByCategoryAndYear("涡轮每日订单台数", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                salesOrder = new SalesOrderQuantity();
                sb.append(getHtmlTable(this.indicators, y, m, d, true, "涡轮合计"));
            } else {
                sb.append("涡轮每日订单台数-内销设定错误");
            }
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getZJComerQuantityTable() {
        StringBuilder sb = new StringBuilder();
        try {

            Indicator indicator;
            Indicator total;
            //再生能源部分
            this.indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("再生订单台数", y);
            indicatorBean.getEntityManager().clear();
            total = indicatorBean.getSumValue(indicators);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("再生能源合计");
                total.setRate(BigDecimal.valueOf(10000));
                getData().put("sumC1", sumC1);
                getData().put("sumC2", sumC2);
                getHtmlTable_OH(indicators, y, m, d, true, "");
                actualNum1 = actualNum1.add(getData().get("actualNum1"));
                actualNum2 = actualNum2.add(getData().get("actualNum2"));
                getHtmlTableRow_OH(total, y, m, d);
            }
            indicators.add(total);
            sb.append(getHtmlTable_OH(indicators, y, m, d, false, ""));
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getComerAmountTable() {
        StringBuilder sb = new StringBuilder();
        String deptno = "5C000";
        Indicator indicator;
        Indicator total;
        try {
            //涡轮产品部分
            this.indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("涡轮每日订单金额", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                for (Indicator i : indicators) {
                    indicatorBean.divideByRate(i, 2);
                }
                salesOrder = new SalesOrderAmount();
                sb.append(getHtmlTable(this.indicators, y, m, d, true));
            } else {
                sb.append("涡轮每日订单金额设定错误");
            }

        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getZJComerAmountTable() {
        StringBuilder sb = new StringBuilder();
        String deptno = "5C000";
        Indicator indicator;
        Indicator total;
        try {
            //再生能源部分
            this.indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("再生订单金额", y);
            indicatorBean.getEntityManager().clear();
            for (Indicator i : indicators) {
                indicatorBean.divideByRate(i, 2);
            }
            total = getSumIndicator();
            total = indicatorBean.getSumValue(indicators);
            if (total != null) {
                indicatorBean.updatePerformance(total);
                total.setName("再生能源合计");
                getData().put("sumC1", sumC1);
                getData().put("sumC2", sumC2);
                getHtmlTable_OH(indicators, y, m, d, false, "");
                actualNum1 = actualNum1.add(getData().get("actualNum1"));
                actualNum2 = actualNum2.add(getData().get("actualNum2"));
                getHtmlTableRow_OH(total, y, m, d);
            }
            indicators.add(total);
            sb.append(getHtmlTable_OH(indicators, y, m, d, false, ""));
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum, String name) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\" ><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\" width=\"15%\" >产品别</th><th rowspan=\"2\" colspan=\"1\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"5\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th></tr>");
            sb.append("<tr><th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                sumIndicator.setName(name);
                if (sumIndicator != null) {
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow(sumIndicator, y, m, d));
                }
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    //20200817 再生能源的报表重新修改格式
    protected String getHtmlTable_OH(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum, String name) {
        getData().clear();
        getData().put("sum1", BigDecimal.ZERO);
        getData().put("sum2", BigDecimal.ZERO);
        getData().put("sumC1", BigDecimal.ZERO);
        getData().put("sumC2", BigDecimal.ZERO);
        getData().put("actualNum1", BigDecimal.ZERO);
        getData().put("actualNum2", BigDecimal.ZERO);
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th rowspan=\"2\" colspan=\"1\"  width=\"15%\" >产品别</th><th rowspan=\"1\" colspan=\"2\">本日</th>");
            sb.append("<th rowspan=\"1\" colspan=\"6\">本月</th><th rowspan=\"1\" colspan=\"5\">年累计</th>");
            sb.append("<th rowspan=\"2\" colspan=\"1\">年度目标</th><th rowspan=\"2\" colspan=\"1\">年度达成率</th><th rowspan=\"2\" colspan=\"1\">订单未交</th></tr>");
            sb.append("<tr><th colspan=\"1\">上海柯茂</th><th colspan=\"1\">浙江柯茂</th><th colspan=\"1\">上海柯茂实际</th><th colspan=\"1\">浙江柯茂实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("<th colspan=\"1\">实际</th><th colspan=\"1\">目标</th><th colspan=\"1\">达成率</th><th colspan=\"1\">去年同期</th><th colspan=\"1\">成长率</th>");
            sb.append("</tr>");
            for (Indicator i : indicatorList) {
                sb.append(getHtmlTableRow_OH(i, y, m, d));
            }
            if (needsum) {
                sumIndicator = indicatorBean.getSumValue(indicators);
                sumIndicator.setName(name);
                if (sumIndicator != null) {
                    indicatorBean.updatePerformance(sumIndicator);
                    sb.append(getHtmlTableRow_OH(sumIndicator, y, m, d));
                }
            }
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected String getHtmlTableRow_OH(Indicator indicator, int y, int m, Date d) throws Exception {
        //获取需要取值栏位
        String col = indicatorBean.getIndicatorColumn(indicator.getFormtype(), m);
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = indicator.getActualIndicator();
        IndicatorDetail b = indicator.getBenchmarkIndicator();
        IndicatorDetail p = indicator.getPerformanceIndicator();
        IndicatorDetail t = indicator.getTargetIndicator();
        IndicatorDetail other1 = indicator.getOther1Indicator();
        IndicatorDetail other2 = indicator.getOther2Indicator();
        Field f;
        try {
            BigDecimal num1, num2, numC1, numC2;
            BigDecimal actualNum1 = BigDecimal.ZERO;
            BigDecimal actualNum2 = BigDecimal.ZERO;
            if (indicator.getId() != -1) {
                if (indicator.getActualInterface() != null && indicator.getActualEJB() != null) {
                    //未交订单
                    Actual actualInterface = (Actual) Class.forName(indicator.getActualInterface()).newInstance();
                    actualInterface.setEJB(indicator.getActualEJB());
                    if (salesOrder != null) {
                        salesOrder.setEJB(indicator.getActualEJB());
                        num2 = salesOrder.getNotDelivery(d, actualInterface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                    } else {
                        num2 = BigDecimal.ZERO;
                    }
                } else {
                    num2 = BigDecimal.ZERO;
                }
                //本日上海柯茂出货
                if (indicator.getOther1Interface() != null && indicator.getOther1EJB() != null) {
                    Actual other1Interface = (Actual) Class.forName(indicator.getOther1Interface()).newInstance();
                    other1Interface.setEJB(indicator.getOther1EJB());
                    numC1 = other1Interface.getValue(y, m, d, Calendar.DATE, other1Interface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                } else {
                    numC1 = BigDecimal.ZERO;
                }
                //本日浙江柯茂出货
                if (indicator.getOther2Interface() != null && indicator.getOther2EJB() != null) {
                    Actual other2Interface = (Actual) Class.forName(indicator.getOther2Interface()).newInstance();
                    other2Interface.setEJB(indicator.getOther2EJB());
                    numC2 = other2Interface.getValue(y, m, d, Calendar.DATE, other2Interface.getQueryParams()).divide(indicator.getRate(), 2, RoundingMode.HALF_UP);
                } else {
                    numC2 = BigDecimal.ZERO;
                }
            } else {
                num2 = BigDecimal.ZERO;
                numC1 = BigDecimal.ZERO;
                numC2 = BigDecimal.ZERO;
            }
            if (indicator.getId() != -1) {
                //订单未交合计
                sumAdditionalData("sum2", num2);
                //上海柯茂每日合计
                sumAdditionalData("sumC1", numC1);
                //浙江柯茂每日合计
                sumAdditionalData("sumC2", numC2);
            }
            sb.append("<tr>");
            sb.append("<td>").append(indicator.getName()).append("</td>");
            //上海柯茂每日
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? numC1 : getData().get("sumC1"))).append("</td>");
            //浙江柯茂每日
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? numC2 : getData().get("sumC2"))).append("</td>");
            //当月
            //上海柯茂实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            if (other1 != null) {
                actualNum1 = BigDecimal.valueOf(Double.parseDouble(f.get(other1).toString()));
            }
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? actualNum1 : getData().get("actualNum1"))).append("</td>");
            //浙江柯茂实际
            f = a.getClass().getDeclaredField(col);
            f.setAccessible(true);
            if (other2 != null) {
                actualNum2 = BigDecimal.valueOf(Double.parseDouble(f.get(other2).toString()));
            }
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? actualNum2 : getData().get("actualNum2"))).append("</td>");
            //算每月的合计
            sumAdditionalData("actualNum1", actualNum1);
            //浙江柯茂每日合计
            sumAdditionalData("actualNum2", actualNum2);
            //目标
            f = t.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //达成
            f = p.getClass().getDeclaredField(col);
            f.setAccessible(true);
            sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
            //同期
            f = b.getClass().getDeclaredField(col);
            f.setAccessible(true);
            //sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
            //改成按天折算
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getValueOfDays(BigDecimal.valueOf(Double.valueOf(f.get(b).toString())), d, 0))).append("</td>");
            //成长
            //sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m))).append("</td>");
            //改成按天折算
            sb.append("<td>").append(percentFormat(indicatorBean.getGrowth(a, b, m, d, 0))).append("</td>");
            //累计
            //实际
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(a, m))).append("</td>");
            //目标
            //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m))).append("</td>");
            //改成按天折算
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(t, m, d))).append("</td>");
            //达成
            //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, t, m))).append("</td>");
            //改成按天折算
            sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedPerformance(a, false, t, true, m, d))).append("</td>");
            //同期
            //sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m))).append("</td>");
            //改成按天折算
            sb.append("<td>").append(decimalFormat.format(indicatorBean.getAccumulatedValue(b, m, d))).append("</td>");
            //成长
            //sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m))).append("</td>");
            //改成按天折算
            sb.append("<td>").append(percentFormat(indicatorBean.getAccumulatedGrowth(a, b, m, d))).append("</td>");
            //年度目标
            f = t.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
            //年度达成
            f = p.getClass().getDeclaredField("nfy");
            f.setAccessible(true);
            sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
            sb.append("<td>").append(decimalFormat.format(indicator.getId() != -1 ? num2 : getData().get("sum2"))).append("</td>");
            sb.append("</tr>");
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }
}
