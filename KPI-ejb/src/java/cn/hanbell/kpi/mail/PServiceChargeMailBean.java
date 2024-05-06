/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.BscSheetMail;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
public class PServiceChargeMailBean extends BscSheetMail {

    public PServiceChargeMailBean() {

    }

    protected String servicecss = "<style type='text/css'>body{font-size:14px;font-weight:bold;}div.content{margin:auto;text-align:center;}div.tbl{margin-bottom:20px;}table{margin:auto;border-spacing:0px;border:1px solid #A2C0DA;}th,td{padding:5px;border-collapse:collapse;text-align:left;}th{border:1px solid #000000;text-align:center;font-weight:bold;}td{border:1px solid #000000;text-align:right;}.title{font-size:14px;font-weight:bold;}.foot{font-size:14px;color:Red;}.divFoot{text-align:right;font-weight:bold;height:20px;width:100%;}.divFoot1{text-align:left;height:20px;width:100%;font-weight:bold;}div.tableTitle{float:left;font-size:14px;font-weight:bold;text-align:left;}</style>";

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(servicecss);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">上海汉钟精机股份有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", getD())).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailBody() {
        indicators.clear();
        indicators = indicatorBean.findByCategoryAndYear("P收费服务金额", y);
        indicatorBean.getEntityManager().clear();
        //指标排序
        indicators.sort((Indicator o1, Indicator o2) -> {
            
            if (o1.getSortid() > o2.getSortid()) {
                return 1;
            } else {
                return -1;
            }
        });
          for (Indicator e : indicators) {
            //按换算率计算结果
            indicatorBean.divideByRate(e, 2);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\"><h4 style=\"color:red\">单位：万元</h4>").append("</div>");
        sb.append(getHtmlTable(indicators, y, m, d, true));
        return sb.toString();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        getData().clear();
        StringBuilder sb = new StringBuilder();
        //计算产品合计
        sumIndicator = indicatorBean.getSumValue(indicatorList);
        //合计本月达成
        indicatorBean.updatePerformance(sumIndicator);
        int size = 0;
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr><th width=\"6%\">项目</th><th width=\"8%\" >统计项</th><th>01月</th><th>02月</th><th>03月</th><th>04月</th><th>05月</th><th>06月</th><th>07月</th><th>08月</th>");
            sb.append("<th>09月</th><th>10月</th><th>11月</th><th>12月</th><th>合计</th></tr>");
            for (Indicator i : indicatorList) {
                size++;
                if (size % 2 != 0) {
                    sb.append(getHtmlTableRow(i, y, m, d, "#D3D7D4"));
                } else {
                    sb.append(getHtmlTableRow(i, y, m, d, "#FFFFFF"));
                }
            }
            sb.append(getHtmlTableRow(sumIndicator, y, m, d, "#FFFFFF"));
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator e, int y, int m, Date d) throws Exception {
        return super.getHtmlTableRow(e, y, m, d); //To change body of generated methods, choose Tools | Templates.
    }

    protected String getHtmlTableRow(Indicator e, int y, int m, Date d, String color) throws Exception {
        //获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();
        IndicatorDetail a = e.getActualIndicator();
        IndicatorDetail b = e.getBenchmarkIndicator();
        IndicatorDetail p = e.getPerformanceIndicator();
        IndicatorDetail t = e.getTargetIndicator();
        Field f;
        mon = indicatorBean.getIndicatorColumn("N", getM());
        BigDecimal v;
        Method setMethod;
        try {
            actualAccumulated = new IndicatorDetail();
            actualAccumulated.setParent(e);
            actualAccumulated.setType("A");

            benchmarkAccumulated = new IndicatorDetail();
            benchmarkAccumulated.setParent(e);
            benchmarkAccumulated.setType("B");

            targetAccumulated = new IndicatorDetail();
            targetAccumulated.setParent(e);
            targetAccumulated.setType("T");

            AP = new IndicatorDetail();
            AP.setParent(e);
            AP.setType("P");

            BG = new IndicatorDetail();
            BG.setParent(e);
            BG.setType("P");

            AG = new IndicatorDetail();
            AG.setParent(e);
            AG.setType("P");

            for (int i = getM(); i > 0; i--) {
                //顺序计算的话会导致累计值重复累加
                //实际值累计
                v = indicatorBean.getAccumulatedValue(e.getActualIndicator(), i);
                setMethod = actualAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(actualAccumulated, v);
                //同期值累计
                v = indicatorBean.getAccumulatedValue(e.getBenchmarkIndicator(), i);
                setMethod = benchmarkAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(benchmarkAccumulated, v);
                //目标值累计
                v = indicatorBean.getAccumulatedValue(e.getTargetIndicator(), i);
                setMethod = targetAccumulated.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(targetAccumulated, v);
                //累计达成
                v = indicatorBean.getAccumulatedPerformance(e.getActualIndicator(), e.getTargetIndicator(), i);
                setMethod = AP.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AP, v);
                //同比成长率
                v = indicatorBean.getGrowth(e.getActualIndicator(), e.getBenchmarkIndicator(), i);
                setMethod = BG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(BG, v);
                //累计成长率
                v = indicatorBean.getGrowth(actualAccumulated, benchmarkAccumulated, i);
                setMethod = AG.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(AG, v);
            }
            //按当前月份累计值重设全年累计
            f = actualAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            actualAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(actualAccumulated).toString())));

            f = benchmarkAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            benchmarkAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(benchmarkAccumulated).toString())));

            f = targetAccumulated.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            targetAccumulated.setNfy(BigDecimal.valueOf(Double.valueOf(f.get(targetAccumulated).toString())));

            sb.append("<tr style=\"background:").append(color).append(";\"><td  rowspan=\"10\" colspan=\"1\" style=\"text-align: center;\">").append(e.getName().replace("服务收费", "")).append("</td>");
            sb.append("<td  style=\"text-align: right;\">目标</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = t.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(t))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(t)).equals("0") ? "" : decimalFormat.format(f.get(t))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(t))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(t.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\">");
            sb.append("<td  style=\"text-align: right;\">实际</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = a.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(a))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(a)).equals("0") ? "" : decimalFormat.format(f.get(a))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(a))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(a.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">本月达成</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = p.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(p))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(p)).equals("0.00%") ? "" : percentFormat(f.get(p))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(p))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(p.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">目标累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = targetAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(targetAccumulated)).equals("0") ? "" : decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(targetAccumulated))).append("</td>");
                }
            }
            sb.append("<td></td>");
            sb.append("</tr>");

            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">实际累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = actualAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(actualAccumulated)).equals("0") ? "" : decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(actualAccumulated))).append("</td>");
                }
            }
            sb.append("<td></td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">累计达成</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = AP.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(AP))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(AP)).equals("0.00%") ? "" : percentFormat(f.get(AP))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(AP))).append("</td>");
                }
            }
            sb.append("<td></td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\">");
            sb.append("<td  style=\"text-align: right;\">去年同期</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = b.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(b))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(b)).equals("0") ? "" : decimalFormat.format(f.get(b))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(b))).append("</td>");
                }
            }
            sb.append("<td>").append(decimalFormat.format(b.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">去年累计</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = benchmarkAccumulated.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(decimalFormat.format(f.get(benchmarkAccumulated)).equals("0") ? "" : decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                } else {
                    sb.append("<td>").append(decimalFormat.format(f.get(benchmarkAccumulated))).append("</td>");
                }
            }
            sb.append("<td></td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">同比成长</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = BG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(BG))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(BG)).equals("0.00%") ? "" : percentFormat(f.get(BG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(BG))).append("</td>");
                }
            }
            sb.append("<td>").append(percentFormat(BG.getNfy())).append("</td>");
            sb.append("</tr>");
            sb.append("<tr style=\"background:").append(color).append(";\"><td style=\"text-align: right;\">累计成长</td>");
            for (int i = 1; i < 13; i++) {
                col = indicatorBean.getIndicatorColumn(e.getFormtype(), i);
                f = AG.getClass().getDeclaredField(col);
                f.setAccessible(true);
                if (i == m) {
                    sb.append("<td style=\"color:red\">").append(percentFormat(f.get(AG))).append("</td>");
                } else if (i > m) {
                    sb.append("<td>").append(percentFormat(f.get(AG)).equals("0.00%") ? "" : percentFormat(f.get(AG))).append("</td>");
                } else {
                    sb.append("<td>").append(percentFormat(f.get(AG))).append("</td>");
                }
            }
            sb.append("<td></td>");
            sb.append("</tr>");

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            throw new Exception(ex);
        }
        return sb.toString();
    }

    @Override
    public void setD(Date d) {
        this.d = d;
        c.setTime(d);
        this.y = c.get(Calendar.YEAR);
        this.m = c.get(Calendar.MONTH) + 1;
    }

}
