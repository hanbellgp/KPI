/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.SalesTableBean;
import cn.hanbell.kpi.entity.ClientRanking;
import cn.hanbell.kpi.entity.Indicator;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class RClientRankingFileMailBean extends MailNotification {

    @EJB
    protected SalesTableBean salesTableBean;
    private List<ClientRanking> list;
    Workbook workbook = null;
    private final String PAGESIZE = "10";
    private LinkedHashMap<String, String> map;

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) throws Exception {
        return "";
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return "";
    }

    @Override
    protected String getMailBody() throws Exception {
        attachments.clear();
        String finalFilePath = "";
        map = new LinkedHashMap<>();
        try {
            StringBuffer title = new StringBuffer("\r\n");
            if (m == 1) {
                title.append(y).append("年1月份");
            } else {
                title.append(y).append("年1-").append(m).append("月份");
            }
//            判断月份

            finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            //测试路径
//            int index = finalFilePath.indexOf("dist/gfdeploy/KPI");
//            InputStream is = new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI/web/rpt/R冷媒目标与实际达成率模板.xlsx");
            //正式路径
            int index = finalFilePath.indexOf("KPI-ejb");
            InputStream is = new FileInputStream(finalFilePath.substring(1, index) + "Hanbell-KPI_war/rpt/R冷媒各单位客户排名前10.xlsx");
            workbook = WorkbookFactory.create(is);
            Cell cell;
            map.put("n_code_DA", "= 'R'");

            //制冷
            Sheet sheet1 = workbook.getSheetAt(0);

            int sheetMergeCount = sheet1.getNumMergedRegions();
            cell = sheet1.getRow(0).getCell(3);
            cell.getStringCellValue();
            cell.setCellValue("R 压缩机客户销售统计表（制冷）" + title);
            map.put("n_code_CD", "= 'HD'");
            map.put("n_code_DC", " in ('R','H')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setRHCellValue(sheet1, list);
            map.remove("n_code_DC");
            map.put("n_code_DC", " in ('L')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setLCellValue(sheet1, list);

            //南京
            Sheet sheet2 = workbook.getSheetAt(1);
            cell = sheet2.getRow(0).getCell(3);
            cell.setCellValue("R 压缩机客户销售统计表（南京）" + title);
            map.remove("n_code_CD");
            map.remove("n_code_DC");
            map.put("n_code_CD", "= 'NJ'");
            map.put("n_code_DC", " in ('R','H')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setRHCellValue(sheet2, list);
            map.remove("n_code_DC");
            map.put("n_code_DC", " in ('L')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setLCellValue(sheet2, list);

            //济南
            Sheet sheet3 = workbook.getSheetAt(2);
            cell = sheet3.getRow(0).getCell(3);
            cell.setCellValue("R 压缩机客户销售统计表（济南）" + title);
            map.remove("n_code_CD");
            map.remove("n_code_DC");
            map.put("n_code_CD", "= 'JN'");
            map.put("n_code_DC", " in ('R','H')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setRHCellValue(sheet3, list);
            map.remove("n_code_DC");
            map.put("n_code_DC", " in ('L')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setLCellValue(sheet3, list);

            //广州
            Sheet sheet4 = workbook.getSheetAt(3);
            cell = sheet4.getRow(0).getCell(3);
            cell.setCellValue("R 压缩机客户销售统计表（广州）" + title);
            map.remove("n_code_CD");
            map.remove("n_code_DC");
            map.put("n_code_CD", "= 'GZ'");
            map.put("n_code_DC", " in ('R','H')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setRHCellValue(sheet4, list);
            map.remove("n_code_DC");
            map.put("n_code_DC", " in ('L')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setLCellValue(sheet4, list);

            //重庆
            Sheet sheet5 = workbook.getSheetAt(4);
            cell = sheet5.getRow(0).getCell(3);
            cell.setCellValue("R 压缩机客户销售统计表（重庆）" + title);
            map.remove("n_code_CD");
            map.remove("n_code_DC");
            map.put("n_code_CD", "= 'CQ'");
            map.put("n_code_DC", " in ('R','H')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setRHCellValue(sheet5, list);
            map.remove("n_code_DC");
            map.put("n_code_DC", " in ('L')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setLCellValue(sheet5, list);

            //外销
            Sheet sheet6 = workbook.getSheetAt(5);
            cell = sheet6.getRow(0).getCell(3);
            cell.setCellValue("R 压缩机客户销售统计表（外销）" + title);
            map.remove("n_code_CD");
            map.remove("n_code_DC");
            map.put("n_code_CD", " like  '%WX%'");
            map.put("n_code_DC", " in ('R','H')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setRHCellValue(sheet6, list);
            map.remove("n_code_DC");
            map.put("n_code_DC", " in ('L')");
            list = salesTableBean.getClientList(y, m, map, false, true, this.PAGESIZE);
            this.setLCellValue(sheet6, list);

            //更新页面公式
            sheet1.setForceFormulaRecalculation(true);
            sheet2.setForceFormulaRecalculation(true);
            sheet3.setForceFormulaRecalculation(true);
            sheet4.setForceFormulaRecalculation(true);
            sheet5.setForceFormulaRecalculation(true);
            sheet6.setForceFormulaRecalculation(true);

            String path = "../" + String.format("%d年%d月%s", y, m, mailSubject) + ".xlsx";//新建文件保存路径
            FileOutputStream out = null;
            File file = new File(path);
            //如果文件存在,则删除已有的文件,重新创建一份新的
            try {
                if (file.exists() && file.isFile()) {
                    file.delete();
                    file = new File(path);
                }
                //写入新File
                out = new FileOutputStream(file);
                workbook.write(out);
            } catch (IOException e) {
                log4j.error(e.getMessage());
            } finally {
                if (null != out) {
                    out.flush();
                    out.close();
                }
            }
            //添加到邮件发送
            addAttachments(file);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString() + "Path:" + finalFilePath;
        }
        return BaseLib.formatDate("yyyy-MM-dd HH:mm", new Date());
    }

    public void setRHCellValue(Sheet sheet, List<ClientRanking> list) {
        Row row;
        Cell cell;
        int i = 6;
        for (ClientRanking entity : list) {
            row = sheet.getRow(i);

            if ("总计".equals(entity.getCusna())) {
                Row rowzj = sheet.getRow(17);
                rowzj.getCell(3).setCellValue(Double.valueOf(entity.getNowshpqy1()));
                rowzj.getCell(4).setCellValue(Double.valueOf(entity.getNowshpamts()));
                rowzj.getCell(6).setCellValue(Double.valueOf(entity.getPastshpqy1()));
                rowzj.getCell(7).setCellValue(Double.valueOf(entity.getPastshpamts()));
            } else if (!"其他".equals(entity.getCusna())) {
                row.getCell(2).setCellValue(entity.getCusna());
                row.getCell(3).setCellValue(Double.valueOf(entity.getNowshpqy1()));
                row.getCell(4).setCellValue(Double.valueOf(entity.getNowshpamts()));
                row.getCell(6).setCellValue(Double.valueOf(entity.getPastshpqy1()));
                row.getCell(7).setCellValue(Double.valueOf(entity.getPastshpamts()));
            }
            i++;
        }

    }

    public void setLCellValue(Sheet sheet, List<ClientRanking> list) {
        Row row;
        Cell cell;
        int i = 6;
        for (ClientRanking entity : list) {
            row = sheet.getRow(i);
            if ("总计".equals(entity.getCusna())) {
                Row rowzj = sheet.getRow(17);
                rowzj.getCell(12).setCellValue(Double.valueOf(entity.getNowshpqy1()));
                rowzj.getCell(13).setCellValue(Double.valueOf(entity.getNowshpamts()));
                rowzj.getCell(15).setCellValue(Double.valueOf(entity.getPastshpqy1()));
                rowzj.getCell(16).setCellValue(Double.valueOf(entity.getPastshpamts()));
            } else if (!"其他".equals(entity.getCusna())) {
                row.getCell(11).setCellValue(entity.getCusna());
                row.getCell(12).setCellValue(Double.valueOf(entity.getNowshpqy1()));
                row.getCell(13).setCellValue(Double.valueOf(entity.getNowshpamts()));
                row.getCell(15).setCellValue(Double.valueOf(entity.getPastshpqy1()));
                row.getCell(16).setCellValue(Double.valueOf(entity.getPastshpamts()));
                i++;
            }
        }
    }
}
