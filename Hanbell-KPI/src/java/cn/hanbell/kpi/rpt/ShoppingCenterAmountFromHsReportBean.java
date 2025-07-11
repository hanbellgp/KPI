/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.ShoppingAccomuntBean;
import cn.hanbell.kpi.ejb.ShoppingManufacturerBean;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.model.ShoppingHSDataModel;
import cn.hanbell.kpi.model.ShoppingHSPercentModel;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "shoppingCenterAmountFromHsReportBean")
@ViewScoped
public class ShoppingCenterAmountFromHsReportBean extends FinancingFreeServiceReportBean {

    protected final DecimalFormat floatFormat;
    private List<Serializable> list1;
    private List<Serializable> list2;
    private List<Serializable> list3;
    protected LinkedHashMap<String, String> statusMap;
    private Date btnDate;
    private int y;
    private int m;
    @EJB
    private ShoppingAccomuntBean shoppingAccomuntBean;

    @EJB
    private ShoppingManufacturerBean shoppingManufacturerBean;

    public ShoppingCenterAmountFromHsReportBean() {
        this.floatFormat = new DecimalFormat("#,###");
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
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();
        list3 = new ArrayList<>();
        statusMap = new LinkedHashMap<>();
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        btnDate = settlementDate().getTime();
        statusMap.put("title", BaseLib.formatDate("yyyy", btnDate));

    }

    public void query() {
        try {
            list1.clear();
            list2.clear();
            list3.clear();
            //上海汉钟金额部分
            ShoppingHSDataModel shbsalary1 = shoppingAccomuntBean.getSalary1("C", this.y, false);
            ShoppingHSDataModel shbsalary2 = shoppingAccomuntBean.getSalary2("C", this.y, false);
            ShoppingHSDataModel shbsalary = mergeListDecimal(shbsalary1, shbsalary2);
            ShoppingHSDataModel shbforhssalary1 = shoppingAccomuntBean.getSalary1("C", this.y, true);
            ShoppingHSDataModel shbforhssalary2 = shoppingAccomuntBean.getSalary2("C", this.y, true);
            ShoppingHSDataModel shbforhssalary = mergeListDecimal(shbforhssalary1, shbforhssalary2);
            ShoppingHSPercentModel percentshbsalary = percentListDecimal(shbsalary, shbforhssalary);
            shbsalary.setName("总金额(SHB)");
            shbforhssalary.setName("汉声金额(SHB)");
            percentshbsalary.setName("占比(SHB)");
            list1.add(shbsalary);
            list1.add(shbforhssalary);
            list1.add(percentshbsalary);
            //台湾汉钟金额部分
            ShoppingHSDataModel thbsalary1 = shoppingAccomuntBean.getSalary1("A", this.y, false);
            ShoppingHSDataModel thbsalary2 = shoppingAccomuntBean.getSalary2("A", this.y, false);
            ShoppingHSDataModel thbsalary = mergeListDecimal(thbsalary1, thbsalary2);
            ShoppingHSDataModel thbforhssalary1 = shoppingAccomuntBean.getSalary1("A", this.y, true);
            ShoppingHSDataModel thbforhssalary2 = shoppingAccomuntBean.getSalary2("A", this.y, true);
            ShoppingHSDataModel thbforhssalary = mergeListDecimal(thbforhssalary1, thbforhssalary2);
            ShoppingHSPercentModel percentthbsalary = percentListDecimal(thbsalary, thbforhssalary);
            thbsalary.setName("总金额(THB)");
            thbforhssalary.setName("汉声金额(THB)");
            percentthbsalary.setName("占比(THB)");
            list1.add(thbsalary);
            list1.add(thbforhssalary);
            list1.add(percentthbsalary);
            //合计金额
            ShoppingHSDataModel sumsalary = mergeListDecimal(shbsalary, thbsalary);
            ShoppingHSDataModel sumforhssalary = mergeListDecimal(shbforhssalary, thbforhssalary);
            ShoppingHSPercentModel percentsumsalary = percentListDecimal(sumsalary, sumforhssalary);
            sumsalary.setName("总金额(SHB+THB)");
            sumforhssalary.setName("汉声金额(SHB+THB)");
            percentsumsalary.setName("占比(SHB+THB)");
            list1.add(sumsalary);
            list1.add(sumforhssalary);
            list1.add(percentsumsalary);

            //region
            //上海汉钟重量
            ShoppingHSDataModel shbweight = shoppingAccomuntBean.getWeight("C", this.y, false);
            ShoppingHSDataModel shbforhsweight = shoppingAccomuntBean.getWeight("C", this.y, true);
            ShoppingHSPercentModel percentshbweight = percentListDecimal(shbweight, shbforhsweight);
            shbweight.setName("总重(SHB)");
            shbforhsweight.setName("汉声重量(SHB)");
            percentshbweight.setName("占比(SHB)");
            list2.add(shbweight);
            list2.add(shbforhsweight);
            list2.add(percentshbweight);

            //台湾汉钟重量
            ShoppingHSDataModel thbweight = shoppingAccomuntBean.getWeight("A", this.y, false);
            ShoppingHSDataModel thbforhsweight = shoppingAccomuntBean.getWeight("A", this.y, true);
            ShoppingHSPercentModel percentthbweight = percentListDecimal(thbweight, thbforhsweight);
            thbweight.setName("总重(THB)");
            thbforhsweight.setName("汉声重量(THB)");
            percentthbweight.setName("占比(THB)");
            list2.add(thbweight);
            list2.add(thbforhsweight);
            list2.add(percentthbweight);

            //合计重量
            ShoppingHSDataModel sumweight = this.mergeListDecimal(shbweight, thbweight);
            ShoppingHSDataModel sumforhsweight = this.mergeListDecimal(shbforhsweight, thbforhsweight);
            ShoppingHSPercentModel percentsumweight = percentListDecimal(sumweight, sumforhsweight);
            sumweight.setName("总重(SHB+THB)");
            sumforhsweight.setName("汉声重量(SHB+THB)");
            percentsumweight.setName("占比(SHB+THB)");
            list2.add(sumweight);
            list2.add(sumforhsweight);
            list2.add(percentsumweight);
            //endregion

            //上海汉钟加工件金额
            ShoppingHSDataModel shbsalary3 = shoppingAccomuntBean.getSalary3("C", this.y, false);
            ShoppingHSDataModel shbsalary4 = shoppingAccomuntBean.getSalary4("C", this.y, false);
            ShoppingHSDataModel shbforhssalary3 = shoppingAccomuntBean.getSalary3("C", this.y, true);
            ShoppingHSDataModel shbforhssalary4 = shoppingAccomuntBean.getSalary4("C", this.y, true);

            ShoppingHSDataModel shbsumtg = this.mergeListDecimal(shbsalary3, shbsalary4);
            ShoppingHSDataModel shbsumforhstg = this.mergeListDecimal(shbforhssalary3, shbforhssalary4);
            ShoppingHSPercentModel percentshbsalary3 = percentListDecimal(shbsumtg, shbsumforhstg);
            shbsumtg.setName("总金额(SHB)");
            shbsumforhstg.setName("汉声金额(SHB)");
            percentshbsalary3.setName("占比(SHB)");
            list3.add(shbsumtg);
            list3.add(shbsumforhstg);
            list3.add(percentshbsalary3);

            //台湾汉钟加工件金额
            ShoppingHSDataModel thbsalary3 = shoppingAccomuntBean.getSalary3("A", this.y, false);
            ShoppingHSDataModel thbsalary4 = shoppingAccomuntBean.getSalary4("A", this.y, false);
            ShoppingHSDataModel thbforhssalary3 = shoppingAccomuntBean.getSalary3("A", this.y, true);
            ShoppingHSDataModel thbforhssalary4 = shoppingAccomuntBean.getSalary4("A", this.y, true);
            
            ShoppingHSDataModel thbsumtg = this.mergeListDecimal(thbsalary3, thbsalary4);
            ShoppingHSDataModel thbsumforhstg = this.mergeListDecimal(thbforhssalary3, thbforhssalary4);
            
            ShoppingHSPercentModel percentthbsalary3 = percentListDecimal(thbsumtg, thbsumforhstg);
            thbsumtg.setName("总金额(THB)");
            thbsumforhstg.setName("汉声金额(THB)");
            percentthbsalary3.setName("占比(THB)");
            list3.add(thbsumtg);
            list3.add(thbsumforhstg);
            list3.add(percentthbsalary3);

            ShoppingHSDataModel sunsalary3 = this.mergeListDecimal(shbsumtg, thbsumtg);
            ShoppingHSDataModel sunforhssalary3 = this.mergeListDecimal(shbforhssalary3, thbforhssalary3);
            ShoppingHSPercentModel percentsumsalary3 = percentListDecimal(shbsumforhstg, thbsumforhstg);
            sunsalary3.setName("总金额(SHB+THB)");
            sunforhssalary3.setName("汉声金额(SHB+THB)");
            percentsumsalary3.setName("占比(SHB+THB)");
            list3.add(sunsalary3);
            list3.add(sunforhssalary3);
            list3.add(percentsumsalary3);

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

    public ShoppingHSDataModel mergeListDecimal(ShoppingHSDataModel b1, ShoppingHSDataModel b2) throws Exception {
        ShoppingHSDataModel result = new ShoppingHSDataModel();
        result.setM1(b1.getM1().add(b2.getM1()));
        result.setM2(b1.getM2().add(b2.getM2()));
        result.setM3(b1.getM3().add(b2.getM3()));
        result.setM4(b1.getM4().add(b2.getM4()));
        result.setM5(b1.getM5().add(b2.getM5()));
        result.setM6(b1.getM6().add(b2.getM6()));
        result.setM7(b1.getM7().add(b2.getM7()));
        result.setM8(b1.getM8().add(b2.getM8()));
        result.setM9(b1.getM9().add(b2.getM9()));
        result.setM10(b1.getM10().add(b2.getM10()));
        result.setM11(b1.getM11().add(b2.getM11()));
        result.setM12(b1.getM12().add(b2.getM12()));
        result.setSum(b1.getSum().add(b2.getSum()));
        return result;
    }

    public ShoppingHSPercentModel percentListDecimal(ShoppingHSDataModel b1, ShoppingHSDataModel b2) throws Exception {
        ShoppingHSPercentModel mode = new ShoppingHSPercentModel();
        mode.setM1(b1.getM1().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM1().divide(b1.getM1(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM2(b1.getM2().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM2().divide(b1.getM2(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM3(b1.getM3().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM3().divide(b1.getM3(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM4(b1.getM4().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM4().divide(b1.getM4(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM5(b1.getM5().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM5().divide(b1.getM5(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM6(b1.getM6().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM6().divide(b1.getM6(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM7(b1.getM7().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM7().divide(b1.getM7(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM8(b1.getM8().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM8().divide(b1.getM8(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM9(b1.getM9().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM9().divide(b1.getM9(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM10(b1.getM10().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM10().divide(b1.getM10(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM11(b1.getM11().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM11().divide(b1.getM11(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setM12(b1.getM12().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getM12().divide(b1.getM12(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        mode.setSum(b1.getSum().compareTo(BigDecimal.ZERO) == 0 ? "" : b2.getSum().divide(b1.getSum(), 2, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100)).setScale(0).toString() + "%");
        return mode;
    }

    public void reset() {
        statusMap.put("displaydiv1", "block");
        statusMap.put("displaydiv2", "none");
        list1.clear();
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

    public StringBuffer getWhereItlcs(String itcls) {
        StringBuffer sql = new StringBuffer("");
        try {
            StringTokenizer stzj = new StringTokenizer(itcls, "/");
            sql.append(" in (");
            while (stzj.hasMoreTokens()) {
                sql.append("'").append(stzj.nextToken()).append("',");
            }
            return sql.delete(sql.length() - 1, sql.length()).append(")");
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
            value = value.divide(new BigDecimal(10000), 0);
            return floatFormat.format(value);
        }
    }

    public String weightdoubleformat(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        } else {
            value = value.divide(new BigDecimal(1000), 0);
            return floatFormat.format(value);
        }
    }

    public String salarydoubleformat(BigDecimal value) {
        if (value == null || value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        } else {
            value = value.divide(new BigDecimal(10000), 0);
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

    public List<Serializable> getList1() {
        return list1;
    }

    public void setList1(List<Serializable> list1) {
        this.list1 = list1;
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

    public List<Serializable> getList2() {
        return list2;
    }

    public void setList2(List<Serializable> list2) {
        this.list2 = list2;
    }

    public List<Serializable> getList3() {
        return list3;   
    }

    public void setList3(List<Serializable> list3) {
        this.list3 = list3;
    }

}
