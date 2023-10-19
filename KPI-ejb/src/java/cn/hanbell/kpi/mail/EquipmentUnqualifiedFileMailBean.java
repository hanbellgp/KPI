/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.eam.EquipmentAnalyResultBean;
import cn.hanbell.kpi.ejb.eam.WorkshopEquipmentBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResult;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * @author C2079 车间设备月报
 */
@Stateless
@LocalBean
public class EquipmentUnqualifiedFileMailBean extends MailNotification {

    @EJB
    protected WorkshopEquipmentBean workShopEquipmentBean;

    @EJB
    protected EquipmentAnalyResultBean equipmentAnalyResultBean;

    public EquipmentUnqualifiedFileMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getMailHead() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"tableTitle\">各位主管、各位同事：大家好！</div>");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\" >附件是不合格点检报表，请查收。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">谢谢!");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">此邮件是系统自动发送，请不要回复。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">报表责任人：许兵。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">");
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    @Override
    protected String getMailBody() throws ParseException {
        attachments.clear();
        String finalFilePath = "";
        String deptName = "方型加工课";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date thisDate = new Date();//获取当前时间
        String strDate = sdf.format(thisDate);
        // Date d = sdf.parse(strDate);//将String格式转为日期格式
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(thisDate);
//        cal.add(Calendar.DATE, -1);
//        int hor = thisDate.getHours();//当前时间为几时
//        if (hor > 12) {//当时间是下午时，推送方型的不合格数据
//            deptName = "方型加工课";
//        } else {
//            deptName = "圆型加工课";
//            strDate = sdf.format(cal.getTime());//推送圆型数据时推送前一天的数据
//        }
        String []  str =new String[2];
        str[0]="方型加工课";
        str[1]="圆型加工课";
        try {

            finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("KPI-ejb");
            InputStream is = new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI_war/rpt/不合格点检表模板.xls");
            Workbook workbook = WorkbookFactory.create(is);
            //获得表格样式
            Map<String, CellStyle> style = createStyles(workbook);
            Sheet sheet;
           for (int i = 0; i < str.length; i++) {
            List<Object[]> equipmentAnalyResultList = equipmentAnalyResultBean.getUnqualifiedEquipmentAnalyResult(str[i], strDate);
            sheet = workbook.getSheetAt(i);
            Row row;
            Row row1;
            row = sheet.createRow(0);
            row.setHeight((short) 900);
            row1 = sheet.createRow(14);
            row1.setHeight((short) 800);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            Cell cellTitle = row.createCell(0);
            cellTitle.setCellStyle(style.get("title"));
            cellTitle.setCellValue(strDate + "点检不合格表-----" + str[i]);
            Cell cellTime = row1.createCell(13);
            cellTime.setCellStyle(style.get("right"));
            cellTime.setCellValue("汉钟版");

            int j = 2;
            for (Object[] eq : equipmentAnalyResultList) {
                row = sheet.createRow(j);
                j++;
                row.setHeight((short) 400);
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(eq[0].toString());
                cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(1);
                cell0.setCellValue(eq[1].toString());
                cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(2);
                cell0.setCellValue(eq[2].toString());
                cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(3);
                if (eq[7]!=null) {
                    cell0.setCellValue(eq[7].toString()); 
                }
                cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(4);
                cell0.setCellValue(eq[3].toString());
                cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(5);
                if (eq[4] != null) {
                    cell0.setCellValue(eq[4].toString());
                }
                cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(6);
                if (eq[5] != null) {
                    cell0.setCellValue(eq[5].toString());
                }
                cell0.setCellStyle(style.get("cell"));
                 cell0.setCellStyle(style.get("cell"));
                cell0 = row.createCell(7);
                if (eq[6]!= null) {
                    cell0.setCellValue(eq[6].toString());
                }
                
                cell0.setCellStyle(style.get("cell"));
                 cell0 = row.createCell(8);
                if (eq[8]!= null) {
                    cell0.setCellValue(eq[8].toString());
                }
                cell0.setCellStyle(style.get("cell"));
              }

            }
            
            String path = "../" + strDate + "不合格点检单.xls";//新建文件保存路径
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

        } catch (IOException | NumberFormatException | InvalidFormatException e) {
            return e.toString() + "Path:" + finalFilePath;
        }
        return BaseLib.formatDate("yyyy-MM-dd HH:mm", new Date());
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
        return "";
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return "";
    }

}
