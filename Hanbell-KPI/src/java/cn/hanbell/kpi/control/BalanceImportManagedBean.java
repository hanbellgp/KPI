/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.BalancerecordBean;
import cn.hanbell.kpi.entity.Balancerecord;
import cn.hanbell.kpi.entity.DataRecord;
import cn.hanbell.kpi.entity.DataRecordAssisted;
import cn.hanbell.kpi.lazy.BalancerecordModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "balanceImportManagedBean")
@SessionScoped
public class BalanceImportManagedBean extends SuperSingleBean<Balancerecord> {

    protected Date queryDate;
    protected Date createDate;
    protected String queryCompany;
    protected String createCompany;

    @EJB
    private BalancerecordBean balancerecordBean;

    public BalanceImportManagedBean() {
        super(Balancerecord.class);
    }

    public Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    @Override
    public void update() {
        currentEntity.setOptuser(getUserManagedBean().getCurrentUser().getUsername());
        currentEntity.setOptdateToNow();
        balancerecordBean.update(currentEntity);
        super.update();
    }

    @Override
    public void init() {
        superEJB = balancerecordBean;
        model = new BalancerecordModel(balancerecordBean);
        model.getFilterFields().clear();
        model.getFilterFields().put("facno =", "C");
        if (queryDate != null && !"".equals(queryDate)) {
            model.getFilterFields().put("year =", getCalendar(queryDate).get(Calendar.YEAR));
        }
        if (queryDate != null && !"".equals(queryDate)) {
            model.getFilterFields().put("mon =", getCalendar(queryDate).get((Calendar.MONTH)) + 1);
        }

        queryCompany = "C";
        createCompany = "C";
        this.queryDate = userManagedBean.getBaseDate();
        createDate = userManagedBean.getBaseDate();
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryCompany != null && !"".equals(queryCompany)) {
                model.getFilterFields().put("facno =", queryCompany);
            }
            if (queryDate != null && !"".equals(queryDate)) {
                model.getFilterFields().put("year =", getCalendar(queryDate).get(Calendar.YEAR));
            }
            if (queryDate != null && !"".equals(queryDate)) {
                model.getFilterFields().put("mon =", getCalendar(queryDate).get((Calendar.MONTH)) + 1);
            }

        }
    }

    @Override
    public void reset() {
        queryDate = userManagedBean.getBaseDate();
        queryName = null;
        queryCompany = "C";
        createCompany = "C";
        createDate = userManagedBean.getBaseDate();
        super.reset();
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        List<Balancerecord> addlist = new ArrayList<>();
        String a = "";
        UploadedFile file = event.getFile();
        if (file != null) {
            try {
                InputStream is = file.getInputstream();
                Workbook excel = WorkbookFactory.create(is);
                Sheet sheet = excel.getSheetAt(0);
                int itemno = 1;
                int y = getCalendar(createDate).get(Calendar.YEAR);
                int m = getCalendar(createDate).get(Calendar.MONTH) + 1;
//                Cell cell;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {

                    a = (i + 1) + "";
                    newEntity = new Balancerecord();
                    newEntity.setStatus("N");
                    newEntity.setCreator(getUserManagedBean().getCurrentUser().getUsername());
                    newEntity.setCredateToNow();
                    newEntity.setFacno(createCompany);
                    newEntity.setType("balance");
                    newEntity.setItemno(itemno);
                    newEntity.setItemname(sheet.getRow(i).getCell(0).getStringCellValue());
                    newEntity.setYear(y);
                    newEntity.setMon(m);
                    Cell cell1 = sheet.getRow(i).getCell(1);
                    Cell cell2 = sheet.getRow(i).getCell(2);
                    //数字
                    if (cell1.getCellType() == Cell.CELL_TYPE_NUMERIC && cell2.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        newEntity.setYearmon(BigDecimal.valueOf(cell1.getNumericCellValue()));
                        newEntity.setMonthmon(BigDecimal.valueOf(cell2.getNumericCellValue()));
                        newEntity.setDifference(newEntity.getYearmon().subtract(newEntity.getMonthmon()));
                        if (newEntity.getMonthmon().doubleValue() == 0.0) {
                            newEntity.setScale(BigDecimal.ZERO);
                        } else {
                            newEntity.setScale(newEntity.getDifference().divide(newEntity.getMonthmon().abs(), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN));
                        }
                    } else {
                        newEntity.setYearmon(BigDecimal.ZERO);
                        newEntity.setMonthmon(BigDecimal.ZERO);
                        newEntity.setDifference(BigDecimal.ZERO);
                        newEntity.setScale(BigDecimal.ZERO);
                    }
                    itemno++;
                    addlist.add(newEntity);
                }
                //导入数据
                if (!addlist.isEmpty()) {
                    List<Balancerecord> deletelist = balancerecordBean.findByYeaAndMon(newEntity);
                    if (!deletelist.isEmpty()) {
                        balancerecordBean.delete(deletelist);
                    }
                    for (Balancerecord dr : addlist) {
                        balancerecordBean.persist(dr);
                    }
                    showInfoMsg("Info", "数据导入成功");
                } else {
                    showErrorMsg("Error", "数据导入失败");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMsg("Error", "导入失败,找不到文件或格式错误----" + ex.toString());
            }
        }
    }

    public Date getQueryDate() {
        return queryDate;
    }

    public void setQueryDate(Date queryDate) {
        this.queryDate = queryDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getQueryCompany() {
        return queryCompany;
    }

    public void setQueryCompany(String queryCompany) {
        this.queryCompany = queryCompany;
    }

    public String getCreateCompany() {
        return createCompany;
    }

    public void setCreateCompany(String createCompany) {
        this.createCompany = createCompany;
    }

    public BalancerecordBean getBalancerecordBean() {
        return balancerecordBean;
    }

    public void setBalancerecordBean(BalancerecordBean balancerecordBean) {
        this.balancerecordBean = balancerecordBean;
    }

}
