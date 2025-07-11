/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.SalesTableBean;
import cn.hanbell.kpi.ejb.SalesTableUpdateBean;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.SalesTable;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import vn.hanbell.kpi.ejb.SalesTableVNBean;
import vn.hanbell.kpi.ejb.SalesTableVNUpdateBean;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "salesTableReportBean")
@ViewScoped
public class SalesTableReportBean implements Serializable {

    @EJB
    private SalesTableBean salesTableBean;
    @EJB
    protected SalesTableUpdateBean salesTableUpdateBean;

    @EJB
    private SalesTableVNBean salesTableVNBean;
    @EJB
    protected SalesTableVNUpdateBean salesTableVNUpdateBean;

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    protected Date btnDate;
    protected String da;
    protected String type;
    protected String display;
    protected String status1;
    protected String status2;
    protected int quantitySum;
    protected String amountSum;

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;

    public SalesTableReportBean() {

    }

    public Calendar getQueryDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(userManagedBean.getBaseDate());
        return c;
    }

    public Calendar getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getBtnDate());
        return c;
    }

    @PostConstruct
    public void construct() {
        initial();
    }

    public void initial() {
        quantitySum = 0;
        amountSum = "0";
        display = "none";
        da = "";
        status1 = "";
        status2 = "";
        btnDate = getQueryDate().getTime();
    }

    public void query() {
        display = "none";
        status1 = "";
        status2 = "";
        quantitySum = 0;
        amountSum = "0";
        if (btnDate.after(new Date())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过当前日期！"));
        } else {
            List<SalesTable> list = new ArrayList();
            try {
                if ("VNLA".equals(da) || "VNBN".equals(da)) {
                    queryVHB(); //查越南
                } else {
                    querySHB();//查上海汉钟
                }
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
            }
        }
    }

    public void querySHB() {
        List<SalesTable> list = new ArrayList();
        if ("Shipment".equals(type)) {
            list = salesTableUpdateBean.getShipmentListSum(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), (da == null ? "" : da), type);
        }
        if ("SalesOrder".equals(type)) {
            list = salesTableUpdateBean.getSalesOrderListSum(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), (da == null ? "" : da), type);
        }
        if ("ServiceAmount".equals(type)) {
            list = salesTableUpdateBean.getServiceAmountListSum(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), (da == null ? "" : da), type);
        }
        if (list != null && !list.isEmpty()) {
            if (salesTableBean.querySalesTableIsExist(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), da, type)) {
                status1 = new SimpleDateFormat("yyyy年MM月").format(getDate().getTime()) + (da != null ? da : "所有部门") + " (" + type + ") 数据已存在，如点击更新则会重新覆盖该时间数据！！！慎重";
            } else {
                status2 = new SimpleDateFormat("yyyy年MM月").format(getDate().getTime()) + (da != null ? da : "所有部门") + " (" + type + ")为新数据，请及时更新";
            }
            Double aaDouble = 0.0;
            for (SalesTable salesTable : list) {
                quantitySum += salesTable.getQuantity().doubleValue();
                aaDouble += salesTable.getAmount().doubleValue();
            }
            String bbString = new DecimalFormat("#,##0.00").format(aaDouble);
            amountSum = bbString;
            display = "block";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该数据！"));
        }
    }

    public void queryVHB() {
        List<SalesTable> list = new ArrayList();
        if ("Shipment".equals(type)) {
            list = salesTableVNUpdateBean.getShipmentListSum(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), (da == null ? "" : da), type);
        }
        if ("SalesOrder".equals(type)) {
            list = salesTableVNUpdateBean.getSalesOrderListSum(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), (da == null ? "" : da), type);
        }
        if ("ServiceAmount".equals(type)) {
            list = salesTableVNUpdateBean.getServiceAmountListSum(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), (da == null ? "" : da), type);
        }
        if (list != null && !list.isEmpty()) {
            if (salesTableVNBean.querySalesTableIsExist(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), da, type)) {
                status1 = new SimpleDateFormat("yyyy年MM月").format(getDate().getTime()) + (da != null ? da : "所有部门") + " (" + type + ") 数据已存在，如点击更新则会重新覆盖该时间数据！！！慎重";
            } else {
                status2 = new SimpleDateFormat("yyyy年MM月").format(getDate().getTime()) + (da != null ? da : "所有部门") + " (" + type + ")为新数据，请及时更新";
            }
            Double aaDouble = 0.0;
            for (SalesTable salesTable : list) {
                quantitySum += salesTable.getQuantity().doubleValue();
                aaDouble += salesTable.getAmount().doubleValue();
            }
            String bbString = new DecimalFormat("#,##0.00").format(aaDouble);
            amountSum = bbString;
            display = "block";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该数据！"));
        }
    }

    public void updateSales() {
        boolean a = true;
        if ("VNLA".equals(da) || "VNBN".equals(da)) {
            a = salesTableVNBean.updateSalesTable(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), da, type); //越南
        } else {
            a = salesTableBean.updateSalesTable(getDate().get(Calendar.YEAR), (getDate().get(Calendar.MONTH) + 1), da, type);//上海汉钟
        }
        if (a) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Info", "数据更新成功！"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "数据更新异常！"));
        }
        display = "none";
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

    /**
     * @return the da
     */
    public String getDa() {
        return da;
    }

    /**
     * @param da the da to set
     */
    public void setDa(String da) {
        this.da = da;
    }

    /**
     * @return the display
     */
    public String getDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * @return the quantitySum
     */
    public int getQuantitySum() {
        return quantitySum;
    }

    /**
     * @param quantitySum the quantitySum to set
     */
    public void setQuantitySum(int quantitySum) {
        this.quantitySum = quantitySum;
    }

    /**
     * @return the amountSum
     */
    public String getAmountSum() {
        return amountSum;
    }

    /**
     * @param amountSum the amountSum to set
     */
    public void setAmountSum(String amountSum) {
        this.amountSum = amountSum;
    }

    /**
     * @return the userManagedBean
     */
    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    /**
     * @param userManagedBean the userManagedBean to set
     */
    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    /**
     * @return the status1
     */
    public String getStatus1() {
        return status1;
    }

    /**
     * @param status1 the status1 to set
     */
    public void setStatus1(String status1) {
        this.status1 = status1;
    }

    /**
     * @return the status2
     */
    public String getStatus2() {
        return status2;
    }

    /**
     * @param status2 the status2 to set
     */
    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

}
