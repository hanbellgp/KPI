/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscQueryTableManageBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
@ManagedBean(name = "financingFreeServiceReportBean")
@ViewScoped
public class FinancingFreeServiceReportBean extends BscQueryTableManageBean implements Serializable {

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    protected int y;
    protected int m;
    protected Date btndate;
    protected String facno;

    protected final DecimalFormat decimalFormat;

    protected List<Indicator> firstList;
    protected List<Indicator> secondList;
    protected List<Indicator> thirdlyList;
    protected int colspan;

    protected Indicator sumIndicator;

    public FinancingFreeServiceReportBean() {
        firstList = new ArrayList<>();
        secondList = new ArrayList<>();
        thirdlyList = new ArrayList<>();
        this.decimalFormat = new DecimalFormat("#,##0.00");
    }

    public Calendar settlementDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(getUserManagedBean().getBaseDate());
        return c;
    }

    public Calendar getDate() {
        Calendar c = Calendar.getInstance();
        c.setTime(btndate);
        return c;
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
        if (getIndicatorChart() == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            for (RoleGrantModule m1 : userManagedBean.getRoleGrantDeptList()) {
                if (m1.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        btndate = settlementDate().getTime();
        btnquery();
    }

    public void btnreset() {;
        btndate = settlementDate().getTime();
        btnquery();
    }

    public void btnquery() {
        firstList.clear();
        secondList.clear();
        m = getDate().get(Calendar.MONTH) + 1;
        y = getDate().get(Calendar.YEAR);
        boolean aa = true;
        if (getBtndate().after(settlementDate().getTime())) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "日期选择不能超过系统结算日期！"));
            aa = false;
        }
        if (m > 3 && m <= 6) {
            colspan = 1;
        } else if (m > 6 && m <= 9) {
            colspan = 2;
        } else if (m > 9 && m <= 12) {
            colspan = 3;
        }
        //分割第一个为免费服务金额 第二个为资金回收
        String[] arr = indicatorChart.getRemark().split(",");
        //免费服务金额
        indicator = indicatorBean.findByFormidYearAndDeptno(arr[0], y, indicatorChart.getDeptno());
        if (indicator == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        if (indicator.isAssigned() && aa) {
            firstList = indicatorBean.findByPId(indicator.getId());
            //排序
            firstList.sort((Indicator o1, Indicator o2) -> {
                if (o1.getSortid() < o2.getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            //删除
            firstList.removeIf(x -> !x.getProduct().contains("汉钟"));
            sumIndicator = indicatorBean.getSumValue(getFirstList());
            sumIndicator.setParent(indicator);
            sumIndicator.setDescript("合计");
            sumIndicator.setDeptname("");
            sumIndicator.setUsername("");
            firstList.add(sumIndicator);
            for (Indicator e : firstList) {
                //按换算率计算结果
                indicatorBean.divideByRate(e, 2);
            }
            try {
                for (Indicator fw : firstList) {
                    IndicatorDetailToZero(fw.getActualIndicator(), m);
                    IndicatorDetailToZero(fw.getBenchmarkIndicator(), m);
                }
            } catch (Exception e) {
                System.out.println("cn.hanbell.kpi.rpt.FreeServiceChartReportBean.init()" + e.toString());
            }
            //资金回收率
            indicator = new Indicator();
            if (arr.length == 2) {
                indicator = indicatorBean.findByFormidYearAndDeptno(arr[1], y, indicatorChart.getPid());
            }
            if (indicator != null) {
                secondList = indicatorBean.findByPId(indicator.getId());
                //排序
                secondList.sort((Indicator o1, Indicator o2) -> {
                    if (o1.getSortid() < o2.getSortid()) {
                        return -1;
                    } else {
                        return 1;
                    }
                });
                //根据指标ID加载指标说明、指标分析
                getRemarkOne(indicatorChart, y, m);
            }
        }
    }

    public BigDecimal contrastValue(BigDecimal now, BigDecimal past) {
        return now.subtract(past);
    }

    public BigDecimal contrastValue(IndicatorDetail now, IndicatorDetail past) {
        return getValue(now).subtract(getValue(past));
    }

    public void IndicatorDetailToZero(IndicatorDetail entity, int m) throws InvocationTargetException, NoSuchMethodException {
        for (int i = m + 1; i <= 12; i++) {
            try {
                Method setMethod = entity.getClass().getDeclaredMethod("set" + indicatorBean.getIndicatorColumn("N", i).toUpperCase(), BigDecimal.class);
                setMethod.invoke(entity, BigDecimal.ZERO);
            } catch (SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            }
        }
    }

    public BigDecimal getValue(IndicatorDetail entity) {
        String mon;
        BigDecimal total = BigDecimal.ZERO;
        Field f;
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = entity.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            total = BigDecimal.valueOf(Double.valueOf(f.get(entity).toString()));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            total = BigDecimal.ZERO;
        }
        return total;
    }

    public String getColor(Indicator a) {
        if (a == null) {
            return "";
        }
        BigDecimal value = contrastValue(getValue(a.getActualIndicator()), getValue(a.getBenchmarkIndicator()));
        if (a.getProduct().contains("应付")) {
            return getColor(value, "");
        } else {
            return getColor(value, "fw");
        }
    }

    public String getColor(BigDecimal value, String taye) {
        if ("fw".equals(taye)) {
            if (value.compareTo(BigDecimal.ZERO) == 1) {
                return "color:red";
            }
        } else {
            if (value.compareTo(BigDecimal.ZERO) == -1) {
                return "color:red";
            }
        }
        return "";
    }

    public String getBackgroundColor(String value) {
        switch (value) {
            case "合计":
            case "总产品":
            case "营业周期":
            case "现金周期":
                return "background-color:#dbdbdb";
            default:
                return "";
        }
    }

    public String format(BigDecimal value) {
        return decimalFormat.format(value); //To change body of generated methods, choose Tools | Templates.
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
     * @return the y
     */
    public int getY() {
        return y;
    }

    public int getM() {
        return m;
    }

    /**
     * @return the firstList
     */
    public List<Indicator> getFirstList() {
        return firstList;
    }

    /**
     * @param firstList the firstList to set
     */
    public void setFirstList(List<Indicator> firstList) {
        this.firstList = firstList;
    }

    /**
     * @return the secondList
     */
    public List<Indicator> getSecondList() {
        return secondList;
    }

    /**
     * @param secondList the secondList to set
     */
    public void setSecondList(List<Indicator> secondList) {
        this.secondList = secondList;
    }

    /**
     * @return the thirdlyList
     */
    public List<Indicator> getThirdlyList() {
        return thirdlyList;
    }

    /**
     * @param thirdlyList the thirdlyList to set
     */
    public void setThirdlyList(List<Indicator> thirdlyList) {
        this.thirdlyList = thirdlyList;
    }

    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

}
