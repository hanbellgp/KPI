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
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
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
public class EquipmentPlanImplementationFileMailBean extends MailNotification {

    @EJB
    protected WorkshopEquipmentBean workShopEquipmentBean;

    @EJB
    protected EquipmentAnalyResultBean equipmentAnalyResultBean;

    public EquipmentPlanImplementationFileMailBean() {

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
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\" >附件是自主实施报表，请查收。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">谢谢!");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">此邮件是系统自动发送，请不要回复。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">报表责任人：周翔天。");
        sb.append("<div class=\"tableTitle\" style=\"margin-top: 30px\">");
        return sb.toString();
    }

    @Override
    protected String getMailFooter() {
        return "";
    }

    /**
     * 设置表头名称字段
     */
    private String[] getInventoryTitle2() {
        return new String[]{"所属部门", "设备编号", "设备名称", "MES编号", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
    }

    /**
     * 设置单元格宽度
     */
    private int[] getInventoryWidth2() {
        return new int[]{15, 20, 20, 20, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5,};
    }

    @Override
    protected String getMailBody() throws ParseException {
        attachments.clear();
        String fileName = "计划保全实施表" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";

        String finalFilePath = "";
        finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        int index = finalFilePath.indexOf("KPI-ejb");
        String deptName = "方型加工课";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        Date thisDate = new Date();//获取当前时间
        String strDate = sdf.format(thisDate);
        // Date d = sdf.parse(strDate);//将String格式转为日期格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(thisDate);
        cal.add(Calendar.DATE, -1);
        int hor = thisDate.getHours();//当前时间为几时
        try {
            String fileFullName = "";
            HSSFWorkbook workbook = new HSSFWorkbook();
            //获得表格样式
            Map<String, CellStyle> style = createStyles(workbook);
            // 生成一个表格
            HSSFSheet sheet1 = workbook.createSheet("Sheet1");
            // 设置表格宽度
            int[] wt1 = getInventoryWidth2();
            for (int i = 0; i < wt1.length; i++) {
                sheet1.setColumnWidth(i, wt1[i] * 256);
            }
          
          
            //创建标题行
            Row row;
            Row row1;
            Row row2;
            //表格一
            String[] title1 = getInventoryTitle2();
            row = sheet1.createRow(0);
            row.setHeight((short) 900);
            row1 = sheet1.createRow(1);
            row1.setHeight((short) 800);
            row2 = sheet1.createRow(2);
            row2.setHeight((short) 800);
            for (int i = 0; i < title1.length; i++) {
                Cell cell = row2.createCell(i);
                cell.setCellStyle(style.get("head"));
                cell.setCellValue(title1[i]);
            }
            List<?> itemList = equipmentAnalyResultBean.getImplementationYear(y+"");
            List<Object[]> list = (List<Object[]>) itemList;
            Map<String, List<Object[]>> map = new HashMap<>();
            list.forEach(result -> {
                if (map.containsKey(result[0].toString())) {//判断是否已存在对应键号
                    map.get(result[0].toString()).add(result);//直接在对应的map中添加数据
                } else {//map中不存在，新建key，用来存放数据
                    List<Object[]> tmpList = new ArrayList<>();
                    tmpList.add(result);
                    map.put(result[0].toString(), tmpList);//新增一个键号
                }
            });
            if (list == null || list.isEmpty()) {
                return null;
            }
            sheet1.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));
            Cell cellTitle = row.createCell(0);
            cellTitle.setCellStyle(style.get("title"));
            cellTitle.setCellValue("计划保全实施表");
            sheet1.addMergedRegion(new CellRangeAddress(1, 1, 0, 15));
            Cell cellTime = row1.createCell(0);
            cellTime.setCellStyle(style.get("date"));
            cellTime.setCellValue(y + "-" + m);
            int j = 3;
            int item = 0;
            for (Map.Entry<String, List<Object[]>> entry : map.entrySet()) {
                List<Object[]> list2 = entry.getValue();//取出对应的值
                if (item == 0) {
                    sheet1.addMergedRegion(new CellRangeAddress(j, j + (list2.size() * 2) - 1, 0, 0));
                } else {
                    sheet1.addMergedRegion(new CellRangeAddress(item, item + (list2.size() * 2) - 1, 0, 0));
                }
                item = j + (list2.size() * 2);
                for (Object[] eq : list2) {
                    sheet1.addMergedRegion(new CellRangeAddress(j, j + 1, 1, 1));
                    sheet1.addMergedRegion(new CellRangeAddress(j, j + 1, 2, 2));
                    sheet1.addMergedRegion(new CellRangeAddress(j, j + 1, 3, 3));
                    row = sheet1.createRow(j);
                    Cell cell0 = row.createCell(0);
                    cell0.setCellStyle(style.get("cell"));
                    cell0.setCellValue(entry.getKey());//将部门赋值
                    j = j + 2;
                    for (int i = 1; i <= 12; i++) {
                        cell0 = row.createCell(i + 3);
                        cell0.setCellStyle(style.get("cell"));
                        if (eq[i * 2 + 2] != null) {
                            cell0.setCellValue(Integer.parseInt(eq[i * 2 + 2].toString()));
                        }
                    }
                    for (int i = 1; i <= 3; i++) {
                        cell0 = row.createCell(i);
                        cell0.setCellStyle(style.get("cell"));
                        if (eq[i] != null) {
                            cell0.setCellValue(eq[i].toString());
                        }
                    }
                    row = sheet1.createRow(j - 1);

                    for (int i = 1; i <= 12; i++) {
                        Cell cell1 = row.createCell(i + 3);
                        cell1.setCellStyle(style.get("cell"));
                        if (eq[i * 2 + 2] != null) {
                            cell1.setCellValue(Integer.parseInt(eq[i * 2 + 3].toString()));
                        }
                    }
                    for (int i = 0; i <= 3; i++) {
                        Cell cell1 = row.createCell(i);
                        cell1.setCellStyle(style.get("cell"));
                    }
                }
            }
            String path = "../" + strDate + "计划点检实施表.xls";//新建文件保存路径
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

        } catch (IOException | NumberFormatException e) {
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
