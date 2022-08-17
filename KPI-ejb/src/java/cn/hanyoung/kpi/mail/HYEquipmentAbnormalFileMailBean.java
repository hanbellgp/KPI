/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanyoung.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.eam.WorkshopEquipmentBean;
import cn.hanbell.kpi.entity.Indicator;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author C2079 报修异常邮件推送
 */
@Stateless
@LocalBean
public class HYEquipmentAbnormalFileMailBean extends MailNotification {

    @EJB
    protected WorkshopEquipmentBean workShopEquipmentBean;

    public HYEquipmentAbnormalFileMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Hanbell</title>");
        sb.append(css);
        sb.append("</head><body><div style=\"margin: auto;text-align: center;\">");
        sb.append("<div style=\"width:100%\" class=\"title\">");
        sb.append("<div style=\"text-align:center;width:100%\">安徽汉扬精密机械有限公司</div>");
        sb.append("<div style=\"text-align:center;width:100%\">").append(mailSubject).append("</div>");
        sb.append("<div style=\"text-align:center;width:100%; color:Red;\">日期:").append(BaseLib.formatDate("yyyy-MM-dd", getD())).append("</div>");
        sb.append("</div>");
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    @Override
    protected String getMailBody() throws ParseException {
        StringBuilder sb = new StringBuilder();
//        sb.append(getHtmlTable(indicators, y, m, d, true));
        String finalFilePath = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date thisDate = new Date();//获取当前时间
        String strDate = sdf.format(thisDate);
        try {
            List<Object[]> equipmentAbnormalList = workShopEquipmentBean.getEquipmentRepairAbnormal("Y", strDate);//获取报修后未处理数据
            sb.append("<div class=\"tbl\"><table width=\"100%\">");
            sb.append("<th>报修单号</th><th>建单日期</th><th>故障发生时间</th><th>维修人员到达时间</th><th>资产编号</th><th>件号</th><th>设备名称</th><th>报修部门</th>");
            sb.append("<th>报修人</th><th>维修人</th><th>单据状态</th>");
            for (Object[] objects : equipmentAbnormalList) {
                sb.append("<tr>");
                for (int i = 0; i <= 10; i++) {
                    if (objects[i] != null) {
                        sb.append("<td>").append(objects[i].toString()).append("</td>");
                    } else {
                        sb.append("<td>").append("</td>");
                    }

                }
                sb.append("</tr>");
            }
            finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("KPI-ejb");
            InputStream is = new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI_war/rpt/报修未处理表模板.xls");
            Workbook workbook = WorkbookFactory.create(is);
            //获得表格样式
            Map<String, CellStyle> style = createStyles(workbook);
            Sheet sheet;
            sheet = workbook.getSheetAt(0);
            Row row;
            Row row1;
            row = sheet.createRow(0);
            row.setHeight((short) 900);
            row1 = sheet.createRow(14);
            row1.setHeight((short) 800);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 10));
            Cell cellTitle = row.createCell(0);
            cellTitle.setCellStyle(style.get("title"));
            cellTitle.setCellValue(strDate + "报修未及时处理表");

            int j = 2;
            for (Object[] eq : equipmentAbnormalList) {
                row = sheet.createRow(j);
                j++;
                row.setHeight((short) 400);
                for (int i = 0; i <= 10; i++) {
                    Cell cell0 = row.createCell(i);
                    cell0.setCellStyle(style.get("cell"));
                    if (eq[i] != null) {
                        cell0.setCellValue(eq[i].toString());
                    }

                }

            }
            String path = "../" + strDate + "报修未及时处理表" + ".xls";//新建文件保存路径
            FileOutputStream out = null;
            File file = new File(path);
            try {
                if (file.exists() && file.isFile()) {
                    file.delete();
                    file = new File(path);
                }
                //写入新File
                out = new FileOutputStream(file);
                workbook.write(out);

            } catch (Exception e) {
                log4j.error(e.getMessage());
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
            //添加到邮件发送
            addAttachments(file);
            sb.append("</table></div>");
        } catch (IOException | NumberFormatException | InvalidFormatException e) {
            return e.toString() + "Path:" + finalFilePath;
        }
        return sb.toString();
    }

    /**
     * 设置导出EXCEL表格样式
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new LinkedHashMap<>();
        // 文件头样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setWrapText(true);//设置自动换行
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());//单元格背景颜色
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setBorderRight(CellStyle.BORDER_THIN);
        headStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderTop(CellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        Font headFont = wb.createFont();
        headFont.setFontHeightInPoints((short) 11);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(headFont);
        styles.put("head", headStyle);

        // 正文样式
        CellStyle cellStyle = wb.createCellStyle();
        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);//设置自动换行
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());//单元格背景颜色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", cellStyle);

        CellStyle leftStyle = wb.createCellStyle();
        leftStyle.setWrapText(true);//设置自动换行
        leftStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        leftStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        leftStyle.setBorderLeft(CellStyle.BORDER_THIN);
        leftStyle.setBorderBottom(CellStyle.BORDER_THIN);
        leftStyle.setBorderRight(CellStyle.BORDER_THIN);
        styles.put("left", leftStyle);

        CellStyle rightStyle = wb.createCellStyle();
        rightStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        rightStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        styles.put("right", rightStyle);

        CellStyle titleStyle = wb.createCellStyle();
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font headFont2 = wb.createFont();
        headFont2.setFontHeightInPoints((short) 20);
        headFont2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        titleStyle.setFont(headFont2);
        styles.put("title", titleStyle);

        CellStyle dateStyle = wb.createCellStyle();
        dateStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        dateStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        Font dateFont = wb.createFont();
        dateFont.setFontHeightInPoints((short) 11);
        dateFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        dateStyle.setFont(headFont);
        styles.put("date", dateStyle);
        return styles;
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
//        StringBuilder sb = new StringBuilder();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date thisDate = new Date();//获取当前时间
//        String strDate = sdf.format(thisDate);
//        List<Object[]> equipmentAbnormalList = workShopEquipmentBean.getEquipmentRepairAbnormal("H", strDate);//获取报修后未处理数据
//        sb.append("<div class=\"tbl\"><table width=\"100%\">");
//        sb.append("<th>报修单号</th><th>建单日期</th><th>故障发生时间</th><th>维修人员到达时间</th><th>资产编号</th><th>件号</th><th>设备名称</th><th>报修部门</th>");
//        sb.append("<th>报修人</th><th>维修人</th><th>单据状态</th>");
//        for (Object[] objects : equipmentAbnormalList) {
//            sb.append("<tr>");
//            for (int i = 0; i <= 10; i++) {
//                if (objects[i] != null) {
//                    sb.append("<td>").append(objects[i].toString()).append("</td>");
//                } else {
//                    sb.append("<td>").append("</td>");
//                }
//
//            }
//            sb.append("</tr>");
//        }
//        sb.append("</table></div>");
//        return sb.toString();
return "";
        
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return "";
    }

}
