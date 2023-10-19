/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.ejb.ShoppingTableBean;
import cn.hanbell.kpi.entity.ShoppingTable;
import cn.hanbell.kpi.lazy.ShoppingTableModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import cn.hanbell.util.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "shoppingAmountManagedBean")
@ViewScoped
public class ShoppingAmountManagedBean extends SuperSingleBean<ShoppingTable> {

    @EJB
    private ShoppingTableBean shoppingTableBean;
    @EJB
    private ShoppingAccomuntBean shoppoingAccoumuntBean;
    protected Date btnDate;

    public ShoppingAmountManagedBean() {
        super(ShoppingTable.class);
    }

    @Override
    public void init() {
        btnDate = userManagedBean.getBaseDate();
        model = new ShoppingTableModel(shoppingTableBean);
    }

    @Override
    public void construct() {
        if (fc == null) {
            fc = FacesContext.getCurrentInstance();
        }
        if (ec == null) {
            ec = fc.getExternalContext();
        }
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        init();
        super.construct();
    }

    public void download() {
        try {
            shoppingTableBean.deleteByYearmon(BaseLib.formatDate("yyyyMM", btnDate));
            List<Object[]> shbList1 = shoppoingAccoumuntBean.getShbDateDetail("C", this.btnDate, "", "");
            persist(shbList1);
            List hsList1 = shoppoingAccoumuntBean.getShbDateDetail("H", this.btnDate, "", "");
            persist(hsList1);
            List scmList1 = shoppoingAccoumuntBean.getShbDateDetail("K", this.btnDate, "", "");
            persist(scmList1);
            List zcmList1 = shoppoingAccoumuntBean.getShbDateDetail("E", this.btnDate, "", "");
            persist(zcmList1);
            List hyList1 = shoppoingAccoumuntBean.getShbDateDetail("Y", this.btnDate, "", "");
            persist(hyList1);
            this.showInfoMsg("信息", "下载成功！");
        } catch (Exception e) {
            e.printStackTrace();
            this.showInfoMsg("信息", "下载失败！");
        }
    }

    public void persist(List<Object[]> list) {
        for (Object[] o : list) {
            ShoppingTable sb = new ShoppingTable();
            sb.setYearmon(BaseLib.formatDate("yyyyMM", btnDate));
            sb.setFacno((String) o[0]);
            sb.setVdrno((String) o[1]);
            sb.setVdrna((String) o[2]);
            sb.setItnbr((String) o[3]);
            sb.setItdsc((String) o[4]);
            sb.setAcpamt((BigDecimal) o[5]);
            sb.setPayqty((BigDecimal) o[6]);
            sb.setUserno((String) o[7]);
            sb.setItcls((String) o[8]);
            if ((String) o[6] != null && !"".equals((String) o[6])) {
                sb.setIscenter(Boolean.TRUE);
            } else {
                sb.setIscenter(false);
            }
            shoppingTableBean.persist(sb);
        }
    }

    public void update() {
        try {
            shoppingTableBean.updateSHBType(btnDate);
            shoppingTableBean.updateHSType(btnDate);
            shoppingTableBean.updateHYType(btnDate);
            shoppingTableBean.updateComerType(btnDate);
            shoppingTableBean.updateZJComerType(btnDate);
            shoppingTableBean.updateTHBType(btnDate);
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新1功！"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新失败！"));
            ex.printStackTrace();
            Logger.getLogger(ShoppingAmountManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 导出明细报表
     *
     * @throws Exception
     */
    public void print() throws Exception {

        fileName = "采购中心各分公司明细" + com.lightshell.comm.BaseLib.formatDate("yyyyMMddHHmmss", com.lightshell.comm.BaseLib.getDate()) + ".xls";
        String fileFullName = reportOutputPath + fileName;
        HSSFWorkbook wb = new HSSFWorkbook();
        CreationHelper createHelper = wb.getCreationHelper();
        CellStyle cellStyle = wb.createCellStyle();
        Map map = new LinkedHashMap<String, List<Object[]>>();
        map.put("上海汉钟采购明细", shoppingTableBean.findByFacnoAndYearmon("C", BaseLib.formatDate("yyyyMM", btnDate)));
        map.put("上海柯茂采购明细", shoppingTableBean.findByFacnoAndYearmon("K", BaseLib.formatDate("yyyyMM", btnDate)));
        map.put("浙江柯茂采购明细", shoppingTableBean.findByFacnoAndYearmon("E", BaseLib.formatDate("yyyyMM", btnDate)));
        map.put("浙江汉声采购明细", shoppingTableBean.findByFacnoAndYearmon("H", BaseLib.formatDate("yyyyMM", btnDate)));
        map.put("安徽汉扬采购明细", shoppingTableBean.findByFacnoAndYearmon("Y", BaseLib.formatDate("yyyyMM", btnDate)));
        map.put("台湾汉钟采购明细", shoppingTableBean.findByFacnoAndYearmon("A", BaseLib.formatDate("yyyyMM", btnDate)));
        Iterator iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();

            //创建内容
            Sheet sheet = wb.createSheet((String) entry.getKey());
            Cell cell;
            Row row;
            row = sheet.createRow(0);
            row.createCell(0).setCellValue("验收单号");
            row.createCell(1).setCellValue("采购单号");
            row.createCell(2).setCellValue("公司别");
            row.createCell(3).setCellValue("厂商编号");
            row.createCell(4).setCellValue("厂商名称");
            row.createCell(5).setCellValue("品号");
            row.createCell(6).setCellValue("品名");
            row.createCell(7).setCellValue("金额");
            row.createCell(8).setCellValue("品号大类");
            row.createCell(9).setCellValue("物料分类");
            int i = 1;
            List<Object[]> val = (List<Object[]>) entry.getValue();
            for (Object[] e : val) {
                row = sheet.createRow(i);
                row.createCell(0).setCellValue((String) e[9]);
                row.createCell(1).setCellValue((String) e[10]);
                row.createCell(2).setCellValue((String) e[0]);
                row.createCell(3).setCellValue((String) e[1]);
                row.createCell(4).setCellValue((String) e[2]);
                row.createCell(5).setCellValue((String) e[3]);
                row.createCell(6).setCellValue((String) e[4]);
                row.createCell(7).setCellValue(((BigDecimal) e[5]).doubleValue());
                row.createCell(8).setCellValue((String) e[6]);
                row.createCell(9).setCellValue((String) e[8]);
                i++;
            }
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

    /**
     * @return the btnDate
     */
    public Date getBtnDate() {
        return btnDate;
    }

    /**
     * @param btnDate the btnDate to set
     */
    public void setBtnDate(Date btnDate) {
        this.btnDate = btnDate;
    }
}
