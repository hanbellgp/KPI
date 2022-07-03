/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanson.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.ProcessStepBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.ProcessStep;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ProcessingProductionValueMailBean extends MailNotification {

    @EJB
    private ProcessStepBean processStepBean;

    protected Indicator sumIndicator;
    protected List<Indicator> sumIndicatorList;

    protected BigDecimal sumPlan;
    protected BigDecimal sumStandard;
    protected BigDecimal sumActual;
    protected BigDecimal sumQuantity;
    protected BigDecimal sumStandardValue;
    protected BigDecimal sumActualValue;
    protected BigDecimal sumMonthPlan;
    protected BigDecimal sumMonthStandard;
    protected BigDecimal sumMonthActual;
    protected BigDecimal sumMonthQuantity;
    protected BigDecimal sumMonthStandardValue;
    protected BigDecimal sumMonthActualValue;

    protected Map<String, CellStyle> cellStyles;
    String reportPath = null;
    String reportOutputPath = null;

    // 异常数据
    protected List<ProcessStep> abnormalList = new ArrayList<>();

    public ProcessingProductionValueMailBean() {
        this.sumIndicatorList = new ArrayList();
    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        if (sumIndicatorList != null) {
            this.sumIndicatorList.clear();
        }
        super.init();
    }

    protected IndicatorDaily findIndicatorDaily(IndicatorDetail entity, int m) {
        return indicatorBean.findIndicatorDailyByPIdDateAndType(entity.getId(), entity.getSeq(), m, entity.getType());
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum)
        throws Exception {
        return "";
    }

    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, Sheet sheet) throws Exception {
        getData().clear();
        getData().put("sumPlan", BigDecimal.ZERO);
        getData().put("sumStandard", BigDecimal.ZERO);
        getData().put("sumActual", BigDecimal.ZERO);
        getData().put("sumQuantity", BigDecimal.ZERO);
        getData().put("sumStandardValue", BigDecimal.ZERO);
        getData().put("sumActualValue", BigDecimal.ZERO);
        getData().put("sumMonthPlan", BigDecimal.ZERO);
        getData().put("sumMonthStandard", BigDecimal.ZERO);
        getData().put("sumMonthActual", BigDecimal.ZERO);
        getData().put("sumMonthQuantity", BigDecimal.ZERO);
        getData().put("sumMonthStandardValue", BigDecimal.ZERO);
        getData().put("sumMonthActualValue", BigDecimal.ZERO);
        int i = sheet.getLastRowNum() + 1;
        StringBuilder sb = new StringBuilder();
        Row row;
        Cell cell;
        for (Indicator ind : indicators) {
            company = ind.getCompany();
            List<ProcessStep> processStepList =
                processStepBean.findByEndTimeAndEquipment(company, d, Calendar.DAY_OF_MONTH, ind.getProduct());
            if (processStepList != null && !processStepList.isEmpty()) {
                for (ProcessStep ps : processStepList) {
                    // 异常数据
                    if (ps.getStandardMachineTime().compareTo(BigDecimal.ZERO) == 0
                        || ps.getTotalMachineTime().compareTo(BigDecimal.ZERO) == 0
                        || ps.getProcessingPrice().compareTo(BigDecimal.ZERO) == 0) {
                        ps.setProduct(ind.getProduct());
                        abnormalList.add(ps);
                    }
                    row = sheet.createRow(i);
                    cell = row.createCell(0);
                    cell.setCellValue(ind.getCategory());
                    cell = row.createCell(1);
                    cell.setCellValue(ps.getEquipment());
                    cell = row.createCell(2);
                    cell.setCellValue(ps.getManno());
                    cell = row.createCell(3);
                    cell.setCellType(0);
                    cell.setCellStyle(getCellStyle("date"));
                    cell.setCellValue(ps.getFormdate());
                    cell = row.createCell(4);
                    cell.setCellValue(ps.getItemno());
                    cell = row.createCell(5);
                    cell.setCellValue(ps.getComponent());
                    cell = row.createCell(6);
                    cell.setCellValue(ps.getStep());
                    cell = row.createCell(7);
                    cell.setCellType(0);
                    cell.setCellStyle(getCellStyle("time"));
                    cell.setCellValue(ps.getStartTime());
                    cell = row.createCell(8);
                    cell.setCellType(0);
                    cell.setCellStyle(getCellStyle("time"));
                    cell.setCellValue(ps.getEndTime());
                    cell = row.createCell(9);
                    cell.setCellType(0);
                    cell.setCellValue(ps.getProcessingTime().doubleValue());
                    cell = row.createCell(10);
                    cell.setCellType(0);
                    cell.setCellValue(ps.getStandardMachineTime().multiply(ps.getQty()).doubleValue());
                    cell = row.createCell(11);
                    cell.setCellType(0);
                    cell.setCellValue(ps.getStandCost().doubleValue());
                    BigDecimal value = ps.getStandardMachineTime().multiply(ps.getQty()).multiply(ps.getStandCost());
                    cell = row.createCell(12);
                    cell.setCellType(0);
                    cell.setCellValue(value.doubleValue());
                    cell = row.createCell(13);
                    cell.setCellType(0);
                    cell.setCellValue(ps.getQty().doubleValue());
                    cell = row.createCell(14);
                    cell.setCellValue(ps.getUserid());
                    cell = row.createCell(15);
                    cell.setCellValue(ps.getUser());
                    i++;
                }
            }
            try {
                sb.append(getHtmlTableRow(ind, y, m, d, ""));
            } catch (Exception ex) {
                return ex.toString();
            }
        }
        for (int k = 0; k < 16; k++) {
            sheet.autoSizeColumn(k, true);
        }
        return sb.toString();
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return getHtmlTableRow(indicator, y, m, d, "");
    }

    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d, String sumStyle) throws Exception {
        // 获取需要取值栏位
        String col, mon;
        StringBuilder sb = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        IndicatorDetail f, b, t, a, o5;
        IndicatorDaily fd, bd, td, ad, o5d;
        Field field;
        mon = indicatorBean.getIndicatorColumn("N", m);
        col = indicatorBean.getIndicatorColumn("D", day);
        String value;
        if (indicator.getId() != -1) {
            // 实际产值
            f = indicator.getForecastIndicator();
            fd = findIndicatorDaily(f, m);
            // 标准工时
            b = indicator.getBenchmarkIndicator();
            bd = findIndicatorDaily(b, m);
            // 计划工时
            t = indicator.getTargetIndicator();
            td = findIndicatorDaily(t, m);
            // 报工工时
            a = indicator.getActualIndicator();
            ad = findIndicatorDaily(a, m);
            // 报工数量
            o5 = indicator.getOther5Indicator();
            o5d = findIndicatorDaily(o5, m);
            try {
                BigDecimal standardCost, planTime, standardTime, actualTime, quantity, standardValue, actualValue,
                    monthPlan, monthStandard, monthActual, monthQuantity, monthStandardValue, monthActualValue;
                sb.append("<tr>");
                sb.append("<td>").append("").append("</td>");
                sb.append("<td>").append(indicator.getName()).append("</td>");
                // 分钟产值
                standardCost = indicator.getRate();
                sb.append("<td>").append(standardCost).append("</td>");
                // 计划工时
                field = td.getClass().getDeclaredField(col);
                field.setAccessible(true);
                planTime = BigDecimal.valueOf(Double.parseDouble(field.get(td).toString()));
                value = decimalFormat.format(planTime);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 标准工时
                field = bd.getClass().getDeclaredField(col);
                field.setAccessible(true);
                standardTime = BigDecimal.valueOf(Double.parseDouble(field.get(bd).toString()));
                value = decimalFormat.format(standardTime);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 今日报工
                field = ad.getClass().getDeclaredField(col);
                field.setAccessible(true);
                actualTime = BigDecimal.valueOf(Double.parseDouble(field.get(ad).toString()));
                value = decimalFormat.format(actualTime);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 报工数量
                field = o5d.getClass().getDeclaredField(col);
                field.setAccessible(true);
                quantity = BigDecimal.valueOf(Double.parseDouble(field.get(o5d).toString()));
                value = decimalFormat.format(quantity);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 标准产值
                standardValue = standardCost.multiply(standardTime);
                value = decimalFormat.format(standardValue);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 实际产值
                field = fd.getClass().getDeclaredField(col);
                field.setAccessible(true);
                actualValue = BigDecimal.valueOf(Double.parseDouble(field.get(fd).toString()));
                value = decimalFormat.format(actualValue);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 本月计划
                field = t.getClass().getDeclaredField(mon);
                field.setAccessible(true);
                monthPlan = BigDecimal.valueOf(Double.parseDouble(field.get(t).toString()));
                value = decimalFormat.format(monthPlan);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 本月标准
                field = b.getClass().getDeclaredField(mon);
                field.setAccessible(true);
                monthStandard = BigDecimal.valueOf(Double.parseDouble(field.get(b).toString()));
                value = decimalFormat.format(monthStandard);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 本月报工
                field = a.getClass().getDeclaredField(mon);
                field.setAccessible(true);
                monthActual = BigDecimal.valueOf(Double.parseDouble(field.get(a).toString()));
                value = decimalFormat.format(monthActual);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 作业效率
                if (monthPlan.compareTo(BigDecimal.ZERO) > 0) {
                    value = percentFormat(
                        monthStandard.divide(monthPlan, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)));
                } else {
                    value = "100%";
                }
                sb.append("<td>").append(value).append("</td>");
                // 本月数量
                field = o5.getClass().getDeclaredField(mon);
                field.setAccessible(true);
                monthQuantity = BigDecimal.valueOf(Double.parseDouble(field.get(o5).toString()));
                value = decimalFormat.format(monthQuantity);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 本月标准产值
                monthStandardValue = standardCost.multiply(monthStandard);
                value = decimalFormat.format(monthStandardValue);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                // 本月实际产值
                field = f.getClass().getDeclaredField(mon);
                field.setAccessible(true);
                monthActualValue = BigDecimal.valueOf(Double.parseDouble(field.get(f).toString()));
                value = decimalFormat.format(monthActualValue);
                sb.append("<td>").append(value.equals("0") ? "-" : value).append("</td>");
                sb.append("</tr>");
                if (indicator.getId() != -1) {
                    sumAdditionalData("sumPlan", planTime);
                    sumAdditionalData("sumStandard", standardTime);
                    sumAdditionalData("sumActual", actualTime);
                    sumAdditionalData("sumQuantity", quantity);
                    sumAdditionalData("sumStandardValue", standardValue);
                    sumAdditionalData("sumActualValue", actualValue);
                    sumAdditionalData("sumMonthPlan", monthPlan);
                    sumAdditionalData("sumMonthStandard", monthStandard);
                    sumAdditionalData("sumMonthActual", monthActual);
                    sumAdditionalData("sumMonthQuantity", monthQuantity);
                    sumAdditionalData("sumMonthStandardValue", monthStandardValue);
                    sumAdditionalData("sumMonthActualValue", monthActualValue);
                }
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                throw ex;
            }
        } else {
            sb.append("<tr>");
            sb.append("<td style=${style}>").append(indicator.getCategory()).append("</td>");
            sb.append("<td style=${style}>").append("</td>");
            // 分钟产值
            sb.append("<td style=${style}>").append("</td>");
            // 今日计划
            value = decimalFormat.format(getData().get("sumPlan"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 今日标准
            value = decimalFormat.format(getData().get("sumStandard"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 今日报工
            value = decimalFormat.format(getData().get("sumActual"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 今日数量
            value = decimalFormat.format(getData().get("sumQuantity"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 标准产值
            value = decimalFormat.format(getData().get("sumStandardValue"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 实际产值
            value = decimalFormat.format(getData().get("sumActualValue"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 本月计划
            value = decimalFormat.format(getData().get("sumMonthPlan"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 本月标准
            value = decimalFormat.format(getData().get("sumMonthStandard"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 本月报工
            value = decimalFormat.format(getData().get("sumMonthActual"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 作业效率
            if (getData().get("sumMonthPlan").compareTo(BigDecimal.ZERO) > 0) {
                value = percentFormat(getData().get("sumMonthStandard")
                    .divide(getData().get("sumMonthPlan"), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100L)));
            } else {
                value = "100%";
            }
            sb.append("<td style=${style}>").append(value).append("</td>");
            // 本月数量
            value = decimalFormat.format(getData().get("sumMonthQuantity"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 本月产值
            value = decimalFormat.format(getData().get("sumMonthStandardValue"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            // 本月增值
            value = decimalFormat.format(getData().get("sumMonthActualValue"));
            sb.append("<td style=${style}>").append(value.equals("0") ? "-" : value).append("</td>");
            sb.append("</tr>");
        }
        if (sumStyle != null && !"".equals(sumStyle) && indicator.getId() == -1) {
            return sb.toString().replace("${style}", sumStyle);
        } else {
            return sb.toString().replace("${style}", "");
        }
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanson</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">浙江汉声精密机械有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:")
            .append(BaseLib.formatDate("yyyy-MM-dd", d)).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    protected String getMailBody() throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">分钟产值单位：元/分钟，产值单位：元</div>");
        sb.append(getProcessingProductionValue());
        if (abnormalList.size() > 0) {
            getAbnormalProcessingProductionValue();
        }
        sb.append("<div class=\"tableTitle\">分钟产值: 每个机台每分钟产出价值</div>");
        sb.append("<div class=\"tableTitle\">计划工时: 每个机台 1320分钟/天</div>");
        sb.append("<div class=\"tableTitle\">标准工时: 每个机台今日加工完成工件在ERP中标准机器工时 X 报工完成数量的合计</div>");
        sb.append("<div class=\"tableTitle\">报工工时: 每个机台今日加工完成工件在MES中报工开始时间 - 报工完成时间的合计</div>");
        sb.append("<div class=\"tableTitle\">标准产值: 标准工时 X 分钟产值 </div>");
        sb.append("<div class=\"tableTitle\">实际产值: 每个机台今日加工完成工件的加工价值的合计</div>");
        sb.append("<div class=\"tableTitle\">本月计划: 本月每个机台每日计划工时的合计</div>");
        sb.append("<div class=\"tableTitle\">本月标准: 本月每个机台每日标准工时的合计</div>");
        sb.append("<div class=\"tableTitle\">本月报工: 本月每个机台每日报工工时的合计</div>");
        sb.append("<div class=\"tableTitle\">作业效率: 本月标准 / 本月计划 X 100%</div>");
        sb.append("<div class=\"tableTitle\">月标准产值: 本月标准 X 分钟产值 </div>");
        sb.append("<div class=\"tableTitle\">月实际产值: 每日实际产值合计</div>");
        sb.append(
            "<div class=\"tableTitle\">类别说明：HMC-卧加 VMC-立加 BMC-镗铣 GMC-龙门 HNL-卧车 VNL-立车 CMM-铣床 NHP-刨床 NMD-铣打 TPM-良勇专机</div>");
        sb.append("<div class=\"tableTitle\"><span style=\"color:red\">注：标准工时和加工报价是否及时更新会影响统计结果</span></div>");
        return sb.toString();
    }

    protected String getProcessingProductionValue() {
        sumPlan = BigDecimal.ZERO;
        sumStandard = BigDecimal.ZERO;
        sumActual = BigDecimal.ZERO;
        sumQuantity = BigDecimal.ZERO;
        sumStandardValue = BigDecimal.ZERO;
        sumActualValue = BigDecimal.ZERO;
        sumMonthPlan = BigDecimal.ZERO;
        sumMonthStandard = BigDecimal.ZERO;
        sumMonthActual = BigDecimal.ZERO;
        sumMonthQuantity = BigDecimal.ZERO;
        sumMonthStandardValue = BigDecimal.ZERO;
        sumMonthActualValue = BigDecimal.ZERO;
        sumIndicatorList.clear();
        StringBuilder sb = new StringBuilder();
        abnormalList.clear();
        try {
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<tr>");
            sb.append(
                "<th>类别</th><th>机台</th><th>分钟产值</th><th>计划工时</th><th>标准工时</th><th>报工工时</th><th>报工数量</th><th>标准产值</th><th>实际产值</th>");
            sb.append("<th>本月计划</th><th>本月标准</th><th>本月报工</th><th>作业效率</th><th>本月数量</th><th>月标准产值</th><th>月实际产值</th>");
            String detail, category;
            // 报表路径
            if (reportPath == null || reportOutputPath == null) {
                reportPath = this.getClass().getClassLoader().getResource("../Hanbell-KPI_war/rpt").getPath();
                reportOutputPath =
                    this.getClass().getClassLoader().getResource("../Hanbell-KPI_war/rpt/output").getPath();
            }
            String fullFileName = reportOutputPath + mailSubject + BaseLib.formatDate("yyyy-MM-dd", d) + ".xlsx";
            File file = new File(fullFileName);
            FileOutputStream fos = new FileOutputStream(file);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("每日加工报工明细");
            Row row;
            Cell cell;
            // 初始样式
            initCellStyles(workbook);
            // 表格标题
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("类别");
            cell = row.createCell(1);
            cell.setCellValue("设备编号");
            cell = row.createCell(2);
            cell.setCellValue("制令编号");
            cell = row.createCell(3);
            cell.setCellValue("制令日期");
            cell = row.createCell(4);
            cell.setCellValue("品号");
            cell = row.createCell(5);
            cell.setCellValue("工件编号");
            cell = row.createCell(6);
            cell.setCellValue("加工工序");
            cell = row.createCell(7);
            cell.setCellValue("报工开始");
            cell = row.createCell(8);
            cell.setCellValue("报工完成");
            cell = row.createCell(9);
            cell.setCellValue("实际工时");
            cell = row.createCell(10);
            cell.setCellValue("标准工时");
            cell = row.createCell(11);
            cell.setCellValue("标准成本");
            cell = row.createCell(12);
            cell.setCellValue("标准产值");
            cell = row.createCell(13);
            cell.setCellValue("当日数量");
            cell = row.createCell(14);
            cell.setCellValue("报工工号");
            cell = row.createCell(15);
            cell.setCellValue("报工人员");
            // 获取资料
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-HMC", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-VNL", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-BMC", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-GMC", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-HNL", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-VMC", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-CMM", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-NHP", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-NMD", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-NCG", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-TPM", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            indicators.clear();
            indicators = indicatorBean.findByCategoryAndYear("HS-NDB", y);
            indicatorBean.getEntityManager().clear();
            if (indicators != null && !indicators.isEmpty()) {
                indicators.sort((Indicator i1, Indicator i2) -> {
                    if (i1.getProduct().compareTo(i2.getProduct()) < 1) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                detail = getHtmlTable(indicators, y, m, d, sheet);
                sumIndicator = indicatorBean.getSumValue(indicators);
                category = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(category).append(detail);
                setSumValue(sumIndicator);
            }
            if (sumIndicatorList != null && !sumIndicatorList.isEmpty()) {
                sumIndicator = indicatorBean.getSumValue(sumIndicatorList);
                sumIndicator.setCategory("合计");
                sumIndicator.setName("合计");
                getData().clear();
                getData().put("sumPlan", sumPlan);
                getData().put("sumStandard", sumStandard);
                getData().put("sumActual", sumActual);
                getData().put("sumQuantity", sumQuantity);
                getData().put("sumStandardValue", sumStandardValue);
                getData().put("sumActualValue", sumActualValue);
                getData().put("sumMonthPlan", sumMonthPlan);
                getData().put("sumMonthStandard", sumMonthStandard);
                getData().put("sumMonthActual", sumMonthActual);
                getData().put("sumMonthQuantity", sumMonthQuantity);
                getData().put("sumMonthStandardValue", sumMonthStandardValue);
                getData().put("sumMonthActualValue", sumMonthActualValue);
                detail = getHtmlTableRow(sumIndicator, y, m, d, "'background-color:#ff8e67';");
                sb.append(detail);
            }
            List employeeProductionValue = processStepBean.getEmployeeOperationTime("H", d, Calendar.MONTH, null);
            if (employeeProductionValue != null && !employeeProductionValue.isEmpty()) {
                int i = 0;
                sheet = workbook.createSheet("每月员工产值排名");
                row = sheet.createRow(0);
                cell = row.createCell(0);
                cell.setCellValue("工号");
                cell = row.createCell(1);
                cell.setCellValue("姓名");
                cell = row.createCell(2);
                cell.setCellValue("设备编号");
                cell = row.createCell(3);
                cell.setCellValue("标准工时");
                cell = row.createCell(4);
                cell.setCellValue("报工工时");
                cell = row.createCell(5);
                cell.setCellValue("标准产值");
                for (Object data : employeeProductionValue) {
                    i++;
                    Object[] e = (Object[])data;
                    row = sheet.createRow(i);
                    cell = row.createCell(0);
                    cell.setCellValue(String.valueOf(e[0]));
                    cell = row.createCell(1);
                    cell.setCellValue(String.valueOf(e[1]));
                    cell = row.createCell(2);
                    cell.setCellValue(String.valueOf(e[2]));
                    cell = row.createCell(3);
                    cell.setCellType(0);
                    cell.setCellStyle(getCellStyle("n"));
                    cell.setCellValue(Double.parseDouble(String.valueOf(e[3])));
                    cell = row.createCell(4);
                    cell.setCellType(0);
                    cell.setCellStyle(getCellStyle("n"));
                    cell.setCellValue(Double.parseDouble(String.valueOf(e[4])));
                    cell = row.createCell(5);
                    cell.setCellType(0);
                    cell.setCellStyle(getCellStyle("n"));
                    cell.setCellValue(Double.parseDouble(String.valueOf(e[5])));
                }
                for (int k = 0; k < 6; k++) {
                    sheet.autoSizeColumn(k, true);
                }
            }
            try {
                file.deleteOnExit();
                workbook.write(fos);
            } catch (Exception ex) {
                log4j.error(ex);
            } finally {
                if (null != fos) {
                    fos.flush();
                    fos.close();
                }
            }
            addAttachments(file);
            sb.append("</table></div>");
        } catch (Exception ex) {
            return ex.toString();
        }
        return sb.toString();
    }

    protected void initCellStyles(Workbook wb) {
        if (cellStyles == null) {
            cellStyles = new LinkedHashMap<>();
        } else {
            cellStyles.clear();
        }
        // 文本
        CellStyle stringStyle = wb.createCellStyle();
        stringStyle.setWrapText(true);// 设置自动换行
        cellStyles.put("string", stringStyle);
        cellStyles.put("s", stringStyle);
        // 数字
        CellStyle numberStyle = wb.createCellStyle();
        numberStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        cellStyles.put("number", numberStyle);
        cellStyles.put("n", numberStyle);
        // 日期
        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        dateStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd"));
        cellStyles.put("date", dateStyle);
        cellStyles.put("d", dateStyle);
        // 时间
        CellStyle timeStyle = wb.createCellStyle();
        timeStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        timeStyle.setDataFormat(wb.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss"));
        cellStyles.put("time", timeStyle);
        cellStyles.put("t", timeStyle);
    }

    protected CellStyle getCellStyle(String type) {
        return cellStyles.get(type);
    }

    protected void setSumValue(Indicator indicator) {
        sumIndicatorList.add(indicator);
        sumPlan = sumPlan.add(getData().get("sumPlan"));
        sumStandard = sumStandard.add(getData().get("sumStandard"));
        sumActual = sumActual.add(getData().get("sumActual"));
        sumQuantity = sumQuantity.add(getData().get("sumQuantity"));
        sumStandardValue = sumStandardValue.add(getData().get("sumStandardValue"));
        sumActualValue = sumActualValue.add(getData().get("sumActualValue"));
        sumMonthPlan = sumMonthPlan.add(getData().get("sumMonthPlan"));
        sumMonthStandard = sumMonthStandard.add(getData().get("sumMonthStandard"));
        sumMonthActual = sumMonthActual.add(getData().get("sumMonthActual"));
        sumMonthQuantity = sumMonthQuantity.add(getData().get("sumMonthQuantity"));
        sumMonthStandardValue = sumMonthStandardValue.add(getData().get("sumMonthStandardValue"));
        sumMonthActualValue = sumMonthActualValue.add(getData().get("sumMonthActualValue"));
    }

    // 异常数据报表
    protected void getAbnormalProcessingProductionValue() {
        try {
            // 报表路径
            if (reportPath == null || reportOutputPath == null) {
                reportPath = this.getClass().getClassLoader().getResource("../Hanbell-KPI_war/rpt").getPath();
                reportOutputPath =
                    this.getClass().getClassLoader().getResource("../Hanbell-KPI_war/rpt/output").getPath();
            }
            String fullFileName = reportOutputPath + "异常数据" + BaseLib.formatDate("yyyy-MM-dd", d) + ".xlsx";
            File file = new File(fullFileName);
            FileOutputStream fos = new FileOutputStream(file);
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("异常数据明细");
            Row row;
            Cell cell;
            // 初始样式
            initCellStyles(workbook);
            // 表格标题
            String[] title = {"类别", "设备编号", "制令编号", "制令日期", "品号", "工件编号", "加工工序", "报工开始", "报工完成", "实际工时", "标准工时",
                "标准成本", "标准产值", "当日数量", "报工工号", "报工人员"};
            row = sheet.createRow(0);
            for (int i = 0; i < title.length; i++) {
                cell = row.createCell(i);
                cell.setCellValue(title[i]);
            }
            int i = 1;
            for (ProcessStep ps : abnormalList) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellValue(ps.getProduct());
                cell = row.createCell(1);
                cell.setCellValue(ps.getEquipment());
                cell = row.createCell(2);
                cell.setCellValue(ps.getManno());
                cell = row.createCell(3);
                cell.setCellType(0);
                cell.setCellStyle(getCellStyle("date"));
                cell.setCellValue(ps.getFormdate());
                cell = row.createCell(4);
                cell.setCellValue(ps.getItemno());
                cell = row.createCell(5);
                cell.setCellValue(ps.getComponent());
                cell = row.createCell(6);
                cell.setCellValue(ps.getStep());
                cell = row.createCell(7);
                cell.setCellType(0);
                cell.setCellStyle(getCellStyle("time"));
                cell.setCellValue(ps.getStartTime());
                cell = row.createCell(8);
                cell.setCellType(0);
                cell.setCellStyle(getCellStyle("time"));
                cell.setCellValue(ps.getEndTime());
                cell = row.createCell(9);
                cell.setCellType(0);
                cell.setCellValue(ps.getProcessingTime().doubleValue());
                cell = row.createCell(10);
                cell.setCellType(0);
                cell.setCellValue(ps.getStandardMachineTime().multiply(ps.getQty()).doubleValue());
                cell = row.createCell(11);
                cell.setCellType(0);
                cell.setCellValue(ps.getStandCost().doubleValue());
                BigDecimal value = ps.getStandardMachineTime().multiply(ps.getQty()).multiply(ps.getStandCost());
                cell = row.createCell(12);
                cell.setCellType(0);
                cell.setCellValue(value.doubleValue());
                cell = row.createCell(13);
                cell.setCellType(0);
                cell.setCellValue(ps.getQty().doubleValue());
                cell = row.createCell(14);
                cell.setCellValue(ps.getUserid());
                cell = row.createCell(15);
                cell.setCellValue(ps.getUser());
                i++;
            }
            try {
                file.deleteOnExit();
                workbook.write(fos);
            } catch (Exception ex) {
                log4j.error(ex);
            } finally {
                if (null != fos) {
                    fos.flush();
                    fos.close();
                }
            }
            addAttachments(file);
        } catch (Exception ex) {
            log4j.error(ex);
        }
    }

}
