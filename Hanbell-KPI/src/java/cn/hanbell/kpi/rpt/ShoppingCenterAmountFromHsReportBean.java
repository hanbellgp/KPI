/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.ejb.ShoppingManufacturerBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.web.BscChartManagedBean;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "shoppingCenterAmountFromHsReportBean")
@ViewScoped
public class ShoppingCenterAmountFromHsReportBean extends FinancingFreeServiceReportBean {

    protected final DecimalFormat floatFormat;
    private List<Object[]> list;
    private List<Object[]> weightList;
    protected LinkedHashMap<String, String> statusMap;
    private Date btnDate;
    private int y;
    private int m;
    @EJB
    private ShoppingAccomuntBean shoppingAccomuntBean;

    @EJB
    private ShoppingManufacturerBean shoppingManufacturerBean;

    public ShoppingCenterAmountFromHsReportBean() {
        this.floatFormat = new DecimalFormat("#,###.00");
    }

    @PostConstruct
    @Override
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        y = Integer.valueOf(BaseLib.formatDate("yyyy", getUserManagedBean().getBaseDate()));
        m = Integer.valueOf(BaseLib.formatDate("MM", getUserManagedBean().getBaseDate()));
        indicatorChart = indicatorChartBean.findById(Integer.valueOf(id));
        if (getIndicatorChart() == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            indicator = indicatorBean.findByFormidYearAndDeptno(indicatorChart.getFormid(), this.getY(), indicatorChart.getDeptno());
            for (RoleGrantModule m1 : userManagedBean.getRoleGrantDeptList()) {
                if (m1.getDeptno().equals(indicatorChart.getPid())) {
                    deny = false;
                }
            }
        }
        list = new ArrayList<>();
        weightList = new ArrayList<>();
        statusMap = new LinkedHashMap<>();
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        btnDate = settlementDate().getTime();
        statusMap.put("title", BaseLib.formatDate("yyyy", btnDate));

    }

    public void query() {
        try {
            //汉声铸件采购金额
            String shbFhszj = " in ('SZJ00065','SZJ00067')";
            String twFhszj = " in ('1139')";
            String scmFhszj = " in ('KZJ00053')";
            String zcmFhszj = " in ('EZJ00053')";
            Object[] shb = shoppingAccomuntBean.getShbDate("SHB", "C", btnDate, shbFhszj, "");
            Object[] thb = shoppingAccomuntBean.getShbDate("THB", "A", btnDate, twFhszj, "");
            Object[] scm = shoppingAccomuntBean.getShbDate("SCOMER", "K", btnDate, scmFhszj, "");
            Object[] zcm = shoppingAccomuntBean.getShbDate("ZCOMER", "E", btnDate, zcmFhszj, "");
            Object[] cm = new Object[15];
            cm[0] = "CM";
            for (int i = 1; i <= 13; i++) {
                cm[i] = ((BigDecimal) scm[i]).add((BigDecimal) zcm[i]);
            }

            Object[] sum = new Object[15];
            sum[0] = "集团内合计";
            for (int i = 1; i <= 13; i++) {
                sum[i] = ((BigDecimal) cm[i]).add((BigDecimal) shb[i]).add((BigDecimal) thb[i]);
            }
//            新增其中卓准部分
            //调整占比
            list.clear();
            list.add(shb);
            list.add(shoppingAccomuntBean.getCommodityCirculation("其中商流：", "H", btnDate));
            list.add(thb);
            list.add(cm);
            list.add(sum);

            for (Object[] o : list) {
                o[14] = ((BigDecimal) o[13]).multiply(BigDecimal.valueOf(100)).divide((BigDecimal) list.get(list.size() - 1)[13], 2).toString().concat("%");
            }
            //铸件重量;

            Object[] shbweigth = shoppingAccomuntBean.getGroupWeightDate("总重", "SHB", "C", btnDate, getWhereVdrnos("C", "'铸件'").toString(), ShoppingAccomuntBean.SHB_ITCLS_ZHUJIA + "/" + ShoppingAccomuntBean.SHB_ITCLS_ZHUANZI);
            Object[] shbgornhsweigth = shoppingAccomuntBean.getGroupWeightDate("汉声", "SHB", "C", btnDate, shbFhszj, ShoppingAccomuntBean.SHB_ITCLS_ZHUJIA + "/" + ShoppingAccomuntBean.SHB_ITCLS_ZHUANZI);
            //上海汉钟已作为进口列入。需手动加入上海汉钟厂商
            StringBuffer sb = getWhereVdrnos("A", "'鑄件'");
            Object[] thbweigth = shoppingAccomuntBean.getGroupWeightDate("总重", "THB", "A", btnDate, sb.substring(0, sb.length() - 1).concat(",'86005')"), "");
            Object[] thbgornhsweigth = shoppingAccomuntBean.getGroupWeightDate("汉声", "THB", "A", btnDate, twFhszj, "");
            weightList.clear();
            weightList.add(shbweigth);
            weightList.add(shbgornhsweigth);
            Object[] zhanbi1 = new Object[16];
            zhanbi1[0] = "占比";
            zhanbi1[1] = "SHB";
            for (int i = 2; i <= Integer.valueOf(BaseLib.formatDate("MM", btnDate)) + 1; i++) {
                zhanbi1[i] = ((BigDecimal) shbgornhsweigth[i]).multiply(new BigDecimal(100)).divide((BigDecimal) shbweigth[i], 0, BigDecimal.ROUND_HALF_UP).toString().concat("%");
            }
            zhanbi1[14] = ((BigDecimal) shbgornhsweigth[14]).multiply(new BigDecimal(100)).divide((BigDecimal) shbweigth[14], 0, BigDecimal.ROUND_HALF_UP).toString().concat("%");
            weightList.add(zhanbi1);
            weightList.add(thbweigth);
            weightList.add(thbgornhsweigth);
            Object[] zhanbi2 = new Object[16];
            zhanbi2[0] = "占比";
            zhanbi2[1] = "THB";
            for (int i = 2; i <= Integer.valueOf(BaseLib.formatDate("MM", btnDate)) + 1; i++) {
                zhanbi2[i] = ((BigDecimal) thbgornhsweigth[i]).multiply(new BigDecimal(100)).divide((BigDecimal) thbweigth[i], 0, BigDecimal.ROUND_HALF_UP).toString().concat("%");
            }
            zhanbi2[14] = ((BigDecimal) thbgornhsweigth[14]).multiply(new BigDecimal(100)).divide((BigDecimal) thbweigth[14], 0, BigDecimal.ROUND_HALF_UP).toString().concat("%");
            weightList.add(zhanbi2);

            Object[] sumall1 = new Object[16];
            sumall1[0] = "总重";
            sumall1[1] = "SHB+THB";
            for (int i = 2; i <= Integer.valueOf(BaseLib.formatDate("MM", btnDate)) + 1; i++) {
                sumall1[i] = ((BigDecimal) shbweigth[i]).add((BigDecimal) thbweigth[i]);
            }
            sumall1[14] = ((BigDecimal) shbweigth[14]).add((BigDecimal) thbweigth[14]);
            weightList.add(sumall1);

            Object[] sumall2 = new Object[16];
            sumall2[0] = "汉声";
            sumall2[1] = "SHB+THB";
            for (int i = 2; i <= Integer.valueOf(BaseLib.formatDate("MM", btnDate)) + 1; i++) {
                sumall2[i] = ((BigDecimal) thbgornhsweigth[i]).add((BigDecimal) shbgornhsweigth[i]);
            }
            sumall2[14] = ((BigDecimal) thbgornhsweigth[14]).add((BigDecimal) shbgornhsweigth[14]);
            weightList.add(sumall2);

            Object[] zhanbi3 = new Object[16];
            zhanbi3[0] = "占比";
            zhanbi3[1] = "SHB+THB";
            for (int i = 2; i <= Integer.valueOf(BaseLib.formatDate("MM", btnDate)) + 1; i++) {
                zhanbi3[i] = ((BigDecimal) sumall2[i]).multiply(new BigDecimal(100)).divide((BigDecimal) sumall1[i], 0, BigDecimal.ROUND_HALF_UP).toString().concat("%");
            }
            zhanbi3[14] = ((BigDecimal) sumall2[14]).multiply(new BigDecimal(100)).divide((BigDecimal) sumall1[14], 0, BigDecimal.ROUND_HALF_UP).toString().concat("%");
            weightList.add(zhanbi3);
            statusMap.put("displaydiv1", "none");
            statusMap.put("displaydiv2", "block");

            //根据指标ID加载指标说明、指标分析
            analysisList = indicatorAnalysisBean.findByPIdAndMonth(indicator.getId(), this.m);//指标分析
            if (analysisList != null) {
                this.analysisCount = analysisList.size();
            }
            summaryList = indicatorSummaryBean.findByPIdAndMonth(indicator.getId(), this.m);//指标说明
            if (summaryList != null) {
                this.summaryCount = summaryList.size();
            }
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage((String) null, new FacesMessage(FacesMessage.SEVERITY_INFO, "error", e.getMessage()));
        }

    }

    public void reset() {
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        list.clear();
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
        return null;
//        return percentFormat(value1.multiply(BigDecimal.valueOf(100)).divide(value2, i, BigDecimal.ROUND_HALF_UP), 2);
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

    public String doubleformat(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        } else {
            value = value.divide(new BigDecimal(10000), 2);
            return floatFormat.format(value);
        }
    }

    public String weightdoubleformat(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        } else {
            value = value.divide(new BigDecimal(1000), 2);
            return floatFormat.format(value);
        }
    }

    public boolean visible(int m) {
        if (m == this.m) {
            return true;
        } else {
            return false;
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

    public LinkedHashMap<String, String> getStatusMap() {
        return statusMap;
    }

    public void setStatusMap(LinkedHashMap<String, String> statusMap) {
        this.statusMap = statusMap;
    }

    public List<Object[]> getWeightList() {
        return weightList;
    }

    public void setWeightList(List<Object[]> weightList) {
        this.weightList = weightList;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

}
