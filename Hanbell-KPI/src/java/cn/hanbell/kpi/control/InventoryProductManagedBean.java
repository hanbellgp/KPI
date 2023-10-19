/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndexWarehBean;
import cn.hanbell.kpi.ejb.IndicatorDetailBean;
import cn.hanbell.kpi.ejb.InventoryDepartmentBean;
import cn.hanbell.kpi.ejb.InventoryProductBean;
import cn.hanbell.kpi.ejb.InvindexBean;
import cn.hanbell.kpi.ejb.InvindexDetailBean;
import cn.hanbell.kpi.entity.IndexWareh;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.InventoryProduct;
import cn.hanbell.kpi.entity.Invindex;
import cn.hanbell.kpi.entity.InvindexDetail;
import cn.hanbell.kpi.lazy.InventoryProductModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

/**
 *
 * @author C1749
 */
@ManagedBean(name = "inventoryProductManagedBean")
@SessionScoped
public class InventoryProductManagedBean extends SuperSingleBean<InventoryProduct> {

    @EJB
    protected InventoryProductBean inventoryProductBean;

    @EJB
    private InventoryDepartmentBean inventoryDepartmentBean;

    @EJB
    private InvindexDetailBean invindexDetailBean;

    @EJB
    private InvindexBean invindexBean;

    @EJB
    private IndexWarehBean indexWarehBean;
    protected String facno;
    protected String queryYearmon;
    protected String queryWhdsc;
    protected String queryWareh;
    protected String queryGenre;
    protected String fileFullName;

    protected List<InventoryProduct> editInventoryProductList;
    protected List<InventoryProduct> inventoryProductList;

    protected final DecimalFormat doubleFormat;

    protected final Logger log4j = LogManager.getLogger("cn.hanbell.eap");

    public InventoryProductManagedBean() {
        super(InventoryProduct.class);
        this.doubleFormat = new DecimalFormat("###,###.##");
    }

    @Override
    protected boolean doBeforePersist() throws Exception {
        if (newEntity != null) {
            if (inventoryProductBean.queryInventoryProductIsExist(newEntity)) {
                showErrorMsg("Error", "添加失败,数据库已存在相同的数据，请在确认是否添加？？？");
                return false;
            }
        }
        return super.doBeforePersist(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean doAfterPersist() throws Exception {
        super.doAfterPersist(); // To change body of generated methods, choose Tools | Templates.
        create();
        return true;
    }

    @Override
    public void init() {
        this.facno = "C";
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        String y = String.valueOf(year);
        String m = String.valueOf(month);
        this.superEJB = inventoryProductBean;
        this.model = new InventoryProductModel(superEJB);
        model.getFilterFields().put("facno =", facno);
        queryYearmon = y.concat(month < 10 ? "0" + m : m);
        queryWhdsc = "";
        setEditInventoryProductList(new ArrayList<>());
        setInventoryProductList(new ArrayList<>());
        query();
        super.init();
    }

    @Override
    public void query() {
        super.query();
        if (model != null) {
            model.getFilterFields().clear();
            if (!"".equals(facno) && facno != null) {
                model.getFilterFields().put("facno", facno);
            }
            if (!"".equals(queryYearmon) && queryYearmon != null) {
                model.getFilterFields().put("yearmon", queryYearmon);
            }
            if (!"".equals(queryWareh) && queryWareh != null) {
                model.getFilterFields().put("wareh", queryWareh);
            }
            if (!"".equals(queryWhdsc) && queryWhdsc != null) {
                model.getFilterFields().put("whdsc", queryWhdsc);
            }
            if (!"".equals(queryGenre) && queryGenre != null) {
                model.getFilterFields().put("genre", queryGenre);
            }
        }
    }

    @Override
    public void reset() {
        super.reset();
        queryYearmon = "";
        queryWhdsc = "";
        queryWareh = "";
        queryGenre = "";
    }

    @Override
    public void update() {
        super.update();
        unverify();
    }

    public void queryEdit() {
        if (currentEntity != null) {
            List<InventoryProduct> list = inventoryProductBean.getEditList(currentEntity);
            if (!list.isEmpty()) {
                setEditInventoryProductList(list);
            } else {
                showErrorMsg("Error", "当前数据异常，请重试！！！");
            }
        }
    }

    public void updateEdit() {
        // 取到当前修改的集合
        List<InventoryProduct> list = getEditInventoryProductList();
        if (!list.isEmpty()) {
            for (InventoryProduct ip : list) {
                ip.setAmamount(ip.getEditamount() != null ? ip.getEditamount() : BigDecimal.ZERO);
                ip.setEditamount(BigDecimal.ZERO);
                ip.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
                ip.setOptdateToNow();
                inventoryProductBean.update(ip);
            }
            showErrorMsg("Info", "更新数据成功！！！");
        } else {
            showErrorMsg("Error", "更新数据失败，请重试！！！");
        }
    }

    @Override
    public void create() {
        if (newEntity != null) {
            newEntity.setAmamount(BigDecimal.ZERO);
            newEntity.setStatus("N");
            newEntity.setCreator(getUserManagedBean().getCurrentUser().getUsername());
            newEntity.setCredateToNow();
            if (queryYearmon.equals("")) {
                newEntity.setYearmon(queryYearmon);
            }
        }
        setCurrentEntity(newEntity);
        super.create(); // To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unverify() {
        if (null != getCurrentEntity()) {
            try {
                if (doBeforeUnverify()) {
                    newEntity.setStatus("N");// 简化查询条件,此处不再提供修改状态(M)
                    newEntity.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
                    newEntity.setOptdateToNow();
                    superEJB.unverify(currentEntity);
                    doAfterUnverify();
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(null, e.getMessage()));
            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "没有可更新数据!"));
        }
    }

    public void updateInventoryDepartment() {
        boolean flag = false;
        try {
            int y = Integer.parseInt(queryYearmon.substring(0, 4), 10);
            int m = Integer.parseInt(queryYearmon.substring(queryYearmon.length() - 2, queryYearmon.length()), 10);
            flag = inventoryDepartmentBean.updateInventoryDepartment(y, m, facno);
            if (flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "数据保存成功！"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据保存失败，请重试！！！"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateInventoryProduct() {
        boolean flag;
        try {
            int y = Integer.parseInt(queryYearmon.substring(0, 4), 10);
            int m = Integer.parseInt(queryYearmon.substring(queryYearmon.length() - 2, queryYearmon.length()), 10);
            flag = inventoryProductBean.updateInventoryProduct(y, m, facno);
            //这里需增加一个判断，确认当前公司当前月份有没有数据，如果有提示用户是否继续执行，如果没有直接更新
            if (flag) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "数据保存成功！"));
            } else {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据保存失败，请重试！！！"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //导出成报表
    @Override
    public void print() throws Exception {
        inventoryProductList = inventoryProductBean.getDataByFindByYearmon(queryYearmon);
        if (inventoryProductList == null || inventoryProductList.isEmpty()) {
            return;
        }
        //设置报表名称
        fileName = "库存金额" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
        fileFullName = reportOutputPath + fileName;
        HSSFWorkbook workbook = new HSSFWorkbook();
        //获得表格样式
        Map<String, CellStyle> style = createStyles(workbook);
        // 生成一个表格
        HSSFSheet sheet1 = workbook.createSheet("库存金额明细");
        HSSFSheet sheet2 = workbook.createSheet("生产目标统计表");
        HSSFSheet sheet3 = workbook.createSheet("营业目标统计表");
        HSSFSheet sheet4 = workbook.createSheet("服务目标统计表");
        HSSFSheet sheet5 = workbook.createSheet("借出未归统计表");
        HSSFSheet sheet6 = workbook.createSheet("其他目标统计表");
        // 表格宽度
        int[] wt = getInventoryProductWidth();
        //标题行
        String[] title = getInventoryProductTitle();

        Row row;
        row = sheet1.createRow(0);
        row.setHeight((short) 800);
        for (int i = 0; i < title.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style.get("head"));
            cell.setCellValue(title[i]);
        }
        for (int i = 0; i < wt.length; i++) {
            sheet1.setColumnWidth(i, wt[i] * 256);
        }
        int j = 1;
        for (InventoryProduct ip : inventoryProductList) {
            row = sheet1.createRow(j);
            j++;
            row.setHeight((short) 400);
            Cell cell0 = row.createCell(0);
            cell0.setCellStyle(style.get("cell"));
            cell0.setCellValue(ip.getId());
            Cell cell1 = row.createCell(1);
            cell1.setCellStyle(style.get("cell"));
            cell1.setCellValue(ip.getFacno());
            Cell cell2 = row.createCell(2);
            cell2.setCellStyle(style.get("cell"));
            cell2.setCellValue(ip.getYearmon());
            Cell cell3 = row.createCell(3);
            cell3.setCellStyle(style.get("cell"));
            cell3.setCellValue(ip.getWareh());
            Cell cell4 = row.createCell(4);
            cell4.setCellStyle(style.get("cell"));
            cell4.setCellValue(ip.getWhdsc());
            Cell cell5 = row.createCell(5);
            cell5.setCellStyle(style.get("cell"));
            cell5.setCellValue(ip.getGenre());
            Cell cell6 = row.createCell(6);
            cell6.setCellStyle(style.get("cell"));
            cell6.setCellValue(ip.getTrtype());
            Cell cell7 = row.createCell(7);
            cell7.setCellStyle(style.get("cell"));
            cell7.setCellValue(ip.getItclscode());
            Cell cell8 = row.createCell(8);
            cell8.setCellStyle(style.get("cell"));
            cell8.setCellValue(ip.getCategories() != null ? ip.getCategories() : "");
            Cell cell9 = row.createCell(9);
            cell9.setCellStyle(style.get("cell"));
            cell9.setCellValue(ip.getIndicatorno() != null ? ip.getIndicatorno() : "");
            Cell cell10 = row.createCell(10);
            cell10.setCellStyle(style.get("cell"));
            cell10.setCellValue(ip.getAmount().doubleValue());
            Cell cell11 = row.createCell(11);
            cell11.setCellStyle(style.get("cell"));
            cell11.setCellValue(ip.getAmamount().doubleValue());
        }
        setSheet(sheet2, style, "A1",invindexBean.getGengerna("A1"));
        setSheet(sheet3, style, "A2", invindexBean.getGengerna("A2"));
        setSheet(sheet4, style, "A3", invindexBean.getGengerna("A3"));
        setSheet(sheet5, style, "A4", invindexBean.getGengerna("A4"));
        setSheet(sheet6, style, "A5", invindexBean.getGengerna("A5"));

        OutputStream os = null;
        try {
            os = new FileOutputStream(fileFullName);
            workbook.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            log4j.error(ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                log4j.error(ex.getMessage());
            }
        }
    }

    public void setSheet(HSSFSheet sheet, Map<String, CellStyle> style, String generno, String generna) {
        List<IndexWareh> rows = indexWarehBean.findByGenerno(generno);
        List<Object[]> columns = invindexBean.getRemarkByGenerno(generno);
        List<InventoryProduct> list = inventoryProductBean.findByFacnoAndYearmonAndCategories(facno, this.queryYearmon, generna);
        if ("C".equals(facno)) {
            list.addAll(inventoryProductBean.findByFacnoAndYearmonAndCategories("G", this.queryYearmon, generna));
            list.addAll(inventoryProductBean.findByFacnoAndYearmonAndCategories("J", this.queryYearmon, generna));
            list.addAll(inventoryProductBean.findByFacnoAndYearmonAndCategories("N", this.queryYearmon, generna));
            list.addAll(inventoryProductBean.findByFacnoAndYearmonAndCategories("C4", this.queryYearmon, generna));
            list.addAll(inventoryProductBean.findByFacnoAndYearmonAndCategories("C5", this.queryYearmon, generna));
        }

        Row row;
        row = sheet.createRow(0);
        row.setHeight((short) 800);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style.get("head"));
        //设置列数据
        for (int i = 0; i < columns.size(); i++) {
            cell = row.createCell(i + 1);
            cell.setCellStyle(style.get("head"));
            cell.setCellValue(columns.get(i)[0].toString());
        }
        //设置行数据
        for (int i = 0; i < rows.size(); i++) {
            row = sheet.createRow(i + 1);
            row.setHeight((short) 500);
            cell = row.createCell(0);
            cell.setCellStyle(style.get("head"));
            cell.setCellValue(rows.get(i).getWhdsc());
        }

        for (int i = 0; i <=columns.size(); i++) {
            sheet.setColumnWidth(i, 5000);
        }
        for (int x = 0; x < rows.size(); x++) {
            row = sheet.getRow(x+1);
            String wareh=rows.get(x).getWareh();
            String whdsc=rows.get(x).getWhdsc();
            for (int y = 0; y < columns.size(); y++) {
                String indicatorno=columns.get(y)[0].toString();
                int i=0;
                for(InventoryProduct p:list){
                    if( p.getWareh().equals(wareh) && p.getIndicatorno().equals(indicatorno)){
                        i++;
                    }
                }
                Double sum = list.stream().filter(d ->  d.getWhdsc().equals(whdsc) &&d.getWareh().equals(wareh) && d.getIndicatorno().equals(indicatorno)).collect(Collectors.toList()).stream().mapToDouble(InventoryProduct::getSumAmount).sum();
                cell = row.createCell(y+1);
                cell.setCellStyle(style.get("cell"));
                cell.setCellValue(sum);
            }
        }
    }

    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new LinkedHashMap<>();

        // 文件头样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setWrapText(true);//设置自动换行
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//单元格背景颜色
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
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(headFont);
        styles.put("head", headStyle);

        // 正文样式
        CellStyle cellStyle = wb.createCellStyle();
        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);//设置自动换行
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

        return styles;
    }

    public String[] getInventoryProductTitle() {
        return new String[]{"编号", "公司别", "日期", "库号", "库名", "产品别", "类别", "物料归类码", "大类编号", "中类编号", "库存金额", "调差金额"};
    }

    private int[] getInventoryProductWidth() {
        return new int[]{15, 10, 15, 15, 15, 15, 10, 20, 10, 10, 20, 20};
    }

    public void updateHanbellIndicator() {
        int y = Integer.parseInt(queryYearmon.substring(0, 4), 10);
        int m = Integer.parseInt(queryYearmon.substring(queryYearmon.length() - 2, queryYearmon.length()), 10);
        try {
            model.getFilterFields().clear();
            model.getFilterFields().put("yearmon", this.queryYearmon);
            List<InventoryProduct> list = this.inventoryProductBean.findByFilters(model.getFilterFields());
            for (InventoryProduct entity : list) {
                entity.setCategories("");
                entity.setIndicatorno("");
            }
            List<InventoryProduct> CList = list.stream().filter(d -> "C".equals(d.getFacno())).collect(Collectors.toList());
            List<Object[]> A1List = invindexDetailBean.getWarehs("A1");
            List<Object[]> A2List = invindexDetailBean.getWarehs("A2");
            List<Object[]> A3List = invindexDetailBean.getWarehs("A3");
            List<Object[]> A4List = invindexDetailBean.getWarehs("A4");
            List<Object[]> A5List = invindexDetailBean.getWarehs("A5");
            for (InventoryProduct entity : CList) {
                for (Object[] o : A1List) {
                    if (entity.getWareh().equals(o[1]) && this.genzlsToList((String) o[0]).contains(entity.getGenre())) {
                        entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A1"): String.format("%s,%s", entity.getCategories(), invindexBean.getGengerna("A1")));
                        entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? (String) o[3] : String.format("%s,%s", entity.getIndicatorno(), (String) o[3]));
                    }
                }
                for (Object[] o : A2List) {
                    if (entity.getWareh().equals(o[1]) && this.genzlsToList((String) o[0]).contains(entity.getGenre())) {
                        entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ?invindexBean.getGengerna("A2"): String.format("%s,%s", entity.getCategories(), invindexBean.getGengerna("A2")));
                        entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? (String) o[3] : String.format("%s,%s", entity.getIndicatorno(), (String) o[3]));
                    }
                }
                for (Object[] o : A3List) {
                    if (entity.getWareh().equals(o[1])) {
                        entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A3"): String.format("%s,%s", entity.getCategories(),invindexBean.getGengerna("A3")));
                        entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? (String) o[3] : String.format("%s,%s", entity.getIndicatorno(), (String) o[3]));
                    }
                }
                for (Object[] o : A4List) {
                    if (entity.getWareh().equals(o[1]) && startWithDeptno((String) o[0], entity.getDeptno())) {
                        entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A4") : String.format("%s,%s", entity.getCategories(),invindexBean.getGengerna("A3")));
                        entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? (String) o[3] : String.format("%s,%s", entity.getIndicatorno(), (String) o[3]));
                    }
                }
                for (Object[] o : A5List) {
                    if (entity.getWareh().equals(o[1])) {
                        entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A5") : String.format("%s,%s",entity.getCategories(), invindexBean.getGengerna("A5")));
                        entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? (String) o[3] : String.format("%s,%s", entity.getIndicatorno(), (String) o[3]));
                    }
                }
            }
            this.inventoryProductBean.update(CList);

            List<InventoryProduct> GList = list.stream().filter(d -> !"C".equals(d.getFacno())).collect(Collectors.toList());
            for (InventoryProduct entity : list) {
                if ("J".equals(entity.getFacno())) {
                    entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A3") : String.format("%s,%s", entity.getCategories(),invindexBean.getGengerna("A3")));
                    entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? "济南服务库存金额" : String.format("%s,%s", entity.getIndicatorno(), "济南服务库存金额"));
                } else if ("G".equals(entity.getFacno())) {
                    entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A3") : String.format("%s,%s", entity.getCategories(), invindexBean.getGengerna("A3") ));
                    entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? "广州服务库存金额" : String.format("%s,%s", entity.getIndicatorno(), "广州服务库存金额"));
                } else if ("N".equals(entity.getFacno())) {
                    entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A3")  : String.format("%s,%s", entity.getCategories(),invindexBean.getGengerna("A3") ));
                    entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? "南京服务库存金额" : String.format("%s,%s", entity.getIndicatorno(), "南京服务库存金额"));
                } else if ("C4".equals(entity.getFacno())) {
                    entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A3") : String.format("%s,%s", entity.getCategories(), invindexBean.getGengerna("A3") ));
                    entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? "重庆服务库存金额" : String.format("%s,%s", entity.getIndicatorno(), "重庆服务库存金额"));
                } else if ("C5".equals(entity.getFacno())) {
                    entity.setCategories(entity.getCategories() == null || "".equals(entity.getCategories()) ? invindexBean.getGengerna("A3") : String.format("%s,%s", entity.getCategories(), invindexBean.getGengerna("A3") ));
                    entity.setIndicatorno(entity.getIndicatorno() == null || "".equals(entity.getIndicatorno()) ? "银川服务库存金额" : String.format("%s,%s", entity.getIndicatorno(), "银川服务库存金额"));
                }
            }
            this.inventoryProductBean.update(GList);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "数据更新成功，请导出月报表核查"));
        } catch (Exception ex) {
            ex.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "ERROR", ex.getMessage()));
        }
    }

    public boolean startWithDeptno(String deptnos, String deptno) {
        String args[] = deptnos.replace("，", ",").split(",");
        for (int i = 0; i < args.length; i++) {
            if (deptno.startsWith(args[i])) {
                return true;
            }
        }
        return false;
    }

    public List<String> genzlsToList(String genzls) {
        List<String> list = new ArrayList();
        if (genzls != null && !"".equals(genzls)) {
            genzls = genzls.replaceAll("，", ",");
            String[] st = genzls.split(",");
            for (int i = 0; i < st.length; i++) {
                list.add(st[i]);
            }
            return list;
        }
        return list;
    }

    public String getQueryYearmon() {
        return queryYearmon;
    }

    public void setQueryYearmon(String queryYearmon) {
        this.queryYearmon = queryYearmon;
    }

    public String getQueryWhdsc() {
        return queryWhdsc;
    }

    public void setQueryWhdsc(String queryWhdsc) {
        this.queryWhdsc = queryWhdsc;
    }

    public String getQueryWareh() {
        return queryWareh;
    }

    public void setQueryWareh(String queryWareh) {
        this.queryWareh = queryWareh;
    }

    public String getQueryGenre() {
        return queryGenre;
    }

    public void setQueryGenre(String queryGenre) {
        this.queryGenre = queryGenre;
    }

    public InventoryProduct getInventoryProduct() {
        return newEntity;
    }

    public void setExchangEntity(InventoryProduct newEntity) {
        this.newEntity = newEntity;
    }

    public List<InventoryProduct> getEditInventoryProductList() {
        return editInventoryProductList;
    }

    public void setEditInventoryProductList(List<InventoryProduct> editInventoryProductList) {
        this.editInventoryProductList = editInventoryProductList;
    }

    public String format(BigDecimal b) {
        return doubleFormat.format(b);
    }

    public List<InventoryProduct> getInventoryProductList() {
        return inventoryProductList;
    }

    public void setInventoryProductList(List<InventoryProduct> inventoryProductList) {
        this.inventoryProductList = inventoryProductList;
    }


    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

}
