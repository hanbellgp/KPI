/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.BalancerecordBean;
import cn.hanbell.kpi.ejb.DataRecordBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscQueryTableManageBean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "balanceSheet2ReportBean")
@ViewScoped
public class BalanceSheet2ReportBean extends BscQueryTableManageBean implements Serializable {

    @EJB
    protected BalancerecordBean balancerecordBean;

    @EJB
    protected IndicatorChartBean indicatorChartBean;
    protected final DecimalFormat floatFormat;
    protected final DecimalFormat centFormat;
    protected Date btndate;
//    protected LinkedHashMap<String, String[]> map;
    protected List<Object[]> list;
    protected LinkedHashMap<String, String> statusMap;
    protected String facno;
    protected boolean showchecked;

    public BalanceSheet2ReportBean() {
        this.floatFormat = new DecimalFormat("#,##0.00");
        this.centFormat = new DecimalFormat("#.##");
    }

    public Calendar settlementDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getUserManagedBean().getBaseDate());
        return c;
    }

    public Calendar getdate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getBtndate());
        return c;
    }

    public String format(String title, BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }else if(title.contains("率")){
             return centFormat.format(value)+"%";
        } else {
            return floatFormat.format(value);
        }
    }

    @PostConstruct
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (indicatorChart == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            for (RoleGrantModule m : userManagedBean.getRoleGrantDeptList()) {
                if (m.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        setList(new ArrayList());
        statusMap = new LinkedHashMap<>();
        if (indicatorChart.getRemark().contains("汉钟")) {
            statusMap.put("title", "上海汉钟精机股份有限公司");
            facno = "C";
        }
        if (indicatorChart.getRemark().contains("上海柯茂")) {
            statusMap.put("title", "上海柯茂机械有限公司");
            facno = "K";
        }
        if (indicatorChart.getRemark().contains("浙江柯茂")) {
            statusMap.put("title", "浙江柯茂节能环保工程设备有限公司");
            facno = "E";
        }
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        setBtndate(settlementDate().getTime());
    }

    public void btnreset() {
        setBtndate(settlementDate().getTime());
    }

    public void btnquery() {
        try {
            statusMap.put("displaydiv1", "block");
            statusMap.put("displaydiv2", "none");
            boolean aa = true;
            if (getBtndate().after(settlementDate().getTime())) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
                aa = false;
            }
            int y = getdate().get(Calendar.YEAR);
            int m = getdate().get(Calendar.MONTH) + 1;
            if (aa) {
                list = this.balancerecordBean.findByYeaAndMonAndWhethershow(y, m, showchecked);
                if (list != null && !list.isEmpty()) {
                    statusMap.put("displaydiv1", "none");
                    statusMap.put("displaydiv2", "block");
                    statusMap.put("th1title", "期末余额");
                    statusMap.put("th2title", "期初余额");
                    super.getRemarkOne(indicatorChart, getdate().get(Calendar.YEAR), getdate().get(Calendar.MONTH) + 1);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
                }
            }
        } catch (Exception e) {

        }
    }

    private String getTitle(int y, int m) {
        return y + "年 1~" + m + " 月";
    }

    /**
     * @return the btndate
     */
    public Date getBtndate() {
        return btndate;
    }

    /**
     * @param btndate the btndate to set
     */
    public void setBtndate(Date btndate) {
        this.btndate = btndate;
    }

    /**
     * @return the statusMap
     */
    public LinkedHashMap<String, String> getStatusMap() {
        return statusMap;
    }

    /**
     * @param statusMap the statusMap to set
     */
    public void setStatusMap(LinkedHashMap<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    public BalancerecordBean getBalancerecordBean() {
        return balancerecordBean;
    }

    public void setBalancerecordBean(BalancerecordBean balancerecordBean) {
        this.balancerecordBean = balancerecordBean;
    }

    public List<Object[]> getList() {
        return list;
    }

    public void setList(List<Object[]> list) {
        this.list = list;
    }

    public boolean isShowchecked() {
        return showchecked;
    }

    public void setShowchecked(boolean showchecked) {
        this.showchecked = showchecked;
    }

}
