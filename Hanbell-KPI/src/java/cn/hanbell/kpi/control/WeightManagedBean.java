/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.ejb.ShoppingManufacturerBean;
import cn.hanbell.kpi.ejb.ShoppingMenuWeightBean;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.entity.ShoppingMenuWeight;
import cn.hanbell.kpi.lazy.MailSettingModel;
import cn.hanbell.kpi.lazy.ShoppingMenuWeightModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "weightManagedBean")
@SessionScoped
public class WeightManagedBean extends SuperSingleBean<ShoppingMenuWeight> {

    @EJB
    protected ShoppingMenuWeightBean shoppingMenuWeightBean;
    @EJB
    protected ShoppingManufacturerBean shoppingManufacturerBean;
    @EJB
    protected ShoppingAccomuntBean shoppingAccomuntBean;
    private String queryFacno;
    private String queryItnbr;
    private Date queryDate;

    private String inputFacno;
    private String inputItnbr;
    private String inputItdsc;
    private BigDecimal inputWeight;
    private String isIn;

    public WeightManagedBean() {
        super(ShoppingMenuWeight.class);
    }

    @Override
    public void init() {
        model = new ShoppingMenuWeightModel(shoppingMenuWeightBean);
        queryFacno = "C";
        query();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryFacno != null && !"".equals(queryFacno)) {
                model.getFilterFields().put("facno", queryFacno);
            }
            if (queryItnbr != null && !"".equals(queryItnbr)) {
                model.getFilterFields().put("itnbr", queryItnbr);
            }
        }
    }

    @Override
    public void persist() {
        ShoppingMenuWeight entity = new ShoppingMenuWeight();
        entity.setFacno(this.inputFacno);
        entity.setItnbr(this.inputItnbr);
        entity.setWeight(this.inputWeight);
        try {
            shoppingMenuWeightBean.persist(entity);
            showInfoMsg("Info", "添加成功");
            this.inputFacno = "C";
            this.inputFacno = "";
            this.inputItnbr = "";
            this.inputWeight = BigDecimal.ZERO;
            init();
        } catch (Exception ex) {
            showInfoMsg("Warn", "添加失败" + ex);
        }
    }

    @Override
    public void print() throws Exception {
        entityList = shoppingMenuWeightBean.findByFilters(model.getFilterFields(), model.getSortFields());
        if (entityList == null || entityList.isEmpty()) {
            showInfoMsg("Warn", "暂无数据");
        }
        fileName = "采购中心物料重量" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
        String fileFullName = reportOutputPath + fileName;
        HSSFWorkbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        //创建内容
        Sheet sheet = wb.createSheet("采购中心物料重量");
        Cell cell;
        Row row;
        row = sheet.createRow(0);
        row.createCell(0).setCellValue("公司别");
        row.createCell(1).setCellValue("品号");
        row.createCell(2).setCellValue("品名");
        row.createCell(3).setCellValue("重量");
        row.createCell(4).setCellValue("是否纳入采购重量计算");
        int i = 1;
        for (ShoppingMenuWeight e : entityList) {
            row = sheet.createRow(i);
            row.createCell(0).setCellValue(e.getFacno());
            row.createCell(1).setCellValue(e.getItnbr());
            row.createCell(2).setCellValue(e.getItdsc());
            row.createCell(3).setCellValue(e.getWeight().doubleValue());
            if (e.getIsIn()) {
                row.createCell(4).setCellValue("是");
            } else {
                row.createCell(4).setCellValue("否");
            }
            i++;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileFullName);
            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            showInfoMsg("Warn", "导出失败" + ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                showInfoMsg("Warn", "导出失败" + ex);

            }
        }
    }

    public StringBuffer getWhereVdrnos(String facno, String materialTypeName) {
        StringBuffer sql = new StringBuffer("");
        try {
            List<ShoppingManufacturer> list = shoppingManufacturerBean.findByMaterialTypeName(facno, materialTypeName);
            if (list == null || list.isEmpty()) {
                return sql;
            }
            sql.append(" in (");
            for (ShoppingManufacturer m : list) {
                sql.append("'").append(m.getVdrno()).append("',");
            }
            sql.replace(sql.length() - 1, sql.length(), "").append(")");
            return sql;
        } catch (Exception e) {
            throw e;
        }
    }

    //根据公司别导出需要配置的铸件品号重量
    public void export() throws Exception {
        //获取所有重量品号
        String shbFhszj = " in ('SZJ00065','SZJ00067')";
        String twFhszj = " in ('1139')";
        fileName = "采购中心物料重量" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
        List<Object[]> shbList = new ArrayList<>();
//                shoppingAccomuntBean.getWeightDate1("C", this.queryDate, getWhereVdrnos("C", "'铸件'").toString(), "");
        //上海汉钟已作为进口列入。需手动加入上海汉钟厂商
        StringBuffer sb = getWhereVdrnos("A", "'鑄件'");
        List<Object[]> thbList = new ArrayList<>();
//                shoppingAccomuntBean.getWeightDate1("A", this.queryDate, sb.substring(0, sb.length() - 1).concat(",'86005')"), "");
        String fileFullName = reportOutputPath + fileName;
        OutputStream os = null;
        os = new FileOutputStream(fileFullName);
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            CreationHelper createHelper = wb.getCreationHelper();
            CellStyle cellStyle = wb.createCellStyle();
            //创建内容
            Sheet sheet = wb.createSheet("重量");
            Cell cell;
            Row row;
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("公司别");
            row.createCell(1).setCellValue("品号");
            row.createCell(2).setCellValue("品名");
            row.createCell(3).setCellValue("重量");
            row.createCell(4).setCellValue("是否纳入采购重量计算");
            int i = 1;
            shbList.addAll(thbList);
            for (Object[] obj : shbList) {
                ShoppingMenuWeight weight = shoppingMenuWeightBean.findByItnbr((String) obj[1]);
                if (weight == null) {
                    row = sheet.createRow(i);
                    row.createCell(0).setCellValue((String) obj[0]);
                    row.createCell(1).setCellValue((String) obj[1]);
                    row.createCell(2).setCellValue((String) obj[2]);
                    row.createCell(4).setCellValue("是");
                    i++;
                }
            }

            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            ex.printStackTrace();
            showInfoMsg("Warn", "导出失败" + ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                showInfoMsg("Warn", "导出失败" + ex);

            }
        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void delete() {
        shoppingMenuWeightBean.delete(this.currentEntity);
    }

    public void handleFileUploadWhenNew(FileUploadEvent event) {
        UploadedFile file1 = event.getFile();
        Integer a = 0;
        InputStream inputStream = null;
        if (file1 != null) {
            try {
                inputStream = file1.getInputstream();
                upload(event);
                Workbook excel = WorkbookFactory.create(inputStream);
                Sheet sheet = excel.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    String facno = cellToVlaue(row.getCell(0));
                    String itnbr = cellToVlaue(row.getCell(1));
                    ShoppingMenuWeight k = shoppingMenuWeightBean.findByItnbr(itnbr);
                    if (k != null) {
                        shoppingMenuWeightBean.delete(k);
                    }
                    ShoppingMenuWeight entity = new ShoppingMenuWeight();
                    entity.setFacno(facno);
                    entity.setItnbr(itnbr);
                    entity.setItdsc(cellToVlaue(row.getCell(2)));
                    entity.setWeight(BigDecimal.valueOf(Double.valueOf(cellToVlaue(row.getCell(3)))));
                    if ("是".equals(cellToVlaue(row.getCell(4)))) {
                        if ("0.0".equals(entity.getWeight().toString())) {
                            throw new Exception("列表中存在纳入重量且重量为0的数据，请检查！");
                        }
                        entity.setIsIn(true);
                    } else {
                        entity.setIsIn(false);
                        entity.setWeight(BigDecimal.ZERO);
                    }
                    shoppingMenuWeightBean.persist(entity);
                }
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传成功"));
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "上传失败" + ex.getMessage()));

                ex.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        init();
    }

    public boolean upload(FileUploadEvent event) throws FileNotFoundException, IOException {
        OutputStream output = null;
        InputStream input = null;
        UploadedFile file1 = event.getFile();
        try {
            input = file1.getInputstream();
            String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("WEB-INF");
            String filePath = new String(finalFilePath.substring(1, index));
            StringBuffer pathString = new StringBuffer(filePath.concat("rpt/"));
            pathString.append(String.valueOf(new Date().getTime()));
            pathString.append(".xls");
            String path = pathString.substring(0, pathString.indexOf("KPI")).concat("FileUploadServer/resources").concat(String.valueOf(new Date().getTime()));
            StringBuffer url = new StringBuffer(pathString.substring(0, pathString.indexOf("KPI")));
            url.append("FileUploadServer/resources/").append(String.valueOf(new Date().getTime())).append(".xls");
            File dest = new File(url.toString());
            byte[] buf = new byte[1024];
            int bytesRead;
            output = new FileOutputStream(dest);
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            input.close();
            output.close();
        }
        return true;
    }

    public String cellToVlaue(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        switch (type) {
            case 0:
                double d = cell.getNumericCellValue();
                //整数去掉小数点
                if (d == (int) d) {
                    return String.valueOf((int) d);
                }
                return String.valueOf(cell.getNumericCellValue());
            case 1:
                return cell.getStringCellValue();
            case 2:
                return cell.getCellFormula();
            case 3:
                return "0";
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 5:
                return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

    public String getQueryFacno() {
        return queryFacno;
    }

    public void setQueryFacno(String queryFacno) {
        this.queryFacno = queryFacno;
    }

    public String getQueryItnbr() {
        return queryItnbr;
    }

    public void setQueryItnbr(String queryItnbr) {
        this.queryItnbr = queryItnbr;
    }

    public String getInputFacno() {
        return inputFacno;
    }

    public void setInputFacno(String inputFacno) {
        this.inputFacno = inputFacno;
    }

    public String getInputItnbr() {
        return inputItnbr;
    }

    public void setInputItnbr(String inputItnbr) {
        this.inputItnbr = inputItnbr;
    }

    public BigDecimal getInputWeight() {
        return inputWeight;
    }

    public void setInputWeight(BigDecimal inputWeight) {
        this.inputWeight = inputWeight;
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public String getInputItdsc() {
        return inputItdsc;
    }

    public void setInputItdsc(String inputItdsc) {
        this.inputItdsc = inputItdsc;
    }

    public String getIsIn() {
        return isIn;
    }

    public void setIsIn(String isIn) {
        this.isIn = isIn;
    }

}
