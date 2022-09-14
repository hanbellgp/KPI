/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.web.BscChartManagedBean;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "shoppingCenterAmountReportBean")
@ViewScoped
public class ShoppingCenterAmountReportBean extends BscChartManagedBean {

    protected final DecimalFormat floatFormat;
    private List<Object[]> list;
    private BigDecimal sum1X000Money;
    private BigDecimal sumAllMoney;
    private Date btnDate;
    @EJB
    private ShoppingAccomuntBean shoppingAccomuntBean;

    public ShoppingCenterAmountReportBean() {
        this.floatFormat = new DecimalFormat("#,###.##");
    }

    @Override
    public void init() {
        super.init();
        btnDate = userManagedBean.getBaseDate();
        list = shoppingAccomuntBean.getList(btnDate);
        Calendar c = Calendar.getInstance();
        c.setTime(btnDate);

    }

    public void query() {
        try {
            list = shoppingAccomuntBean.getList(btnDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        list.clear();
    }

    public String percentFormat(BigDecimal value1, BigDecimal value2, int i) {
        if (value1 == null || value1 == BigDecimal.ZERO) {
            return "0.00%";
        }
        if (value2 == null || value2 == BigDecimal.ZERO) {
            return "0.00%";
        }
        if (i == 0 || value2 == BigDecimal.ZERO) {
            return "0.00%";
        }
        return percentFormat(value1.multiply(BigDecimal.valueOf(100)).divide(value2, i, BigDecimal.ROUND_HALF_UP), 2);
    }

    public double getMonthSUM(IndicatorDetail indicatorDetail) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        Field f;
        double a1;
        String mon = "";
        mon = indicatorBean.getIndicatorColumn("N", m);
        f = indicatorDetail.getClass().getDeclaredField(mon);
        f.setAccessible(true);
        a1 = Double.valueOf(f.get(indicatorDetail).toString());
        return a1;
    }

    @Override
    public String doubleformat(BigDecimal value, int scale) {
        BigDecimal sc = new BigDecimal(scale);
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        } else {
            value = value.divide(sc);
            return floatFormat.format(value);
        }
    }

    public List<Object[]> getList() {
        return list;
    }

    public void setList(List<Object[]> list) {
        this.list = list;
    }

    public Date getBtnDate() {
        return btnDate;
    }

    public void setBtnDate(Date btnDate) {
        this.btnDate = btnDate;
    }

}
