/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.PolicyBean;
import cn.hanbell.kpi.ejb.PolicyDetailBean;
import cn.hanbell.kpi.entity.Category;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.PolicyDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.tms.Project;
import cn.hanbell.kpi.lazy.PolicyDetailModel;
import cn.hanbell.kpi.lazy.PolicyModel;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "policySheetReportBean")
@ViewScoped
public class PolicySheetReportBean extends SuperQueryBean<PolicyDetail> {

    @EJB
    private PolicyDetailBean policyDetailBean;
    @EJB
    private PolicyBean policyBean;

    private String queryTimeInterval;
    private String queryGenre;
    private String displaydiv1;
    private String displaydiv2;
    private Policy policy;
    private List<PolicyDetail> detaillist;
    private String summary;
    private String factor;
    private String countermeasure;
    protected final DecimalFormat floatFormat1;
    protected final DecimalFormat floatFormat2;
    protected final DecimalFormat floatFormat3;
    protected final DecimalFormat floatFormat4;

    public PolicySheetReportBean() {
        super(PolicyDetail.class);
        floatFormat1 = new DecimalFormat("#,##0");
        floatFormat2 = new DecimalFormat("#,##0.00");
        floatFormat3 = new DecimalFormat("#,##0.00##");
        floatFormat4 = new DecimalFormat("#,##0.####");
    }

    @Override
    public void construct() {
        fc = FacesContext.getCurrentInstance();
        ec = fc.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        policy = policyBean.findById(Integer.valueOf(id));
        if (policy == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        } else {
            for (RoleGrantModule m : userManagedBean.getRoleGrantDeptList()) {
                if (m.getDeptno().equals(policy.getDeptno())) {
                    deny = false;
                }
            }
        }
        model = new PolicyDetailModel(policyDetailBean);
        model.getFilterFields().put("pid", Integer.valueOf(id));
        init();
    }

    @Override
    public void init() {
        this.queryGenre = "C";
        if (this.userManagedBean.getM() == 10) {
            //次年度全年目标
            this.queryTimeInterval = "D";
        } else if (this.userManagedBean.getM() == 6) {
            //Q2;上半年报告
            this.queryTimeInterval = "B";
        } else if (this.userManagedBean.getM() == 12) {
            //Q4&全年报告   
            this.queryTimeInterval = "C";
        } else {
            this.queryTimeInterval = "A";
        }
        btnquery();
    }

    public void btnquery() {
        if (!model.getFilterFields().containsKey("genre")) {
            model.getFilterFields().put("genre", this.queryGenre);
        } else {
            model.getFilterFields().remove("genre");
            model.getFilterFields().put("genre", this.queryGenre);
        }
        boolean isAula = true;
        if ("D".equals(this.queryTimeInterval)) {
            Policy p = policyBean.findByCompanyNameAndYear(policy.getCompany(), policy.getName(), this.userManagedBean.getY() + 1);
            if (p != null) {
                model.getFilterFields().remove("pid");
                model.getFilterFields().put("pid", p.getId());
            } else {
                this.showErrorMsg("Error", "没有次年数据");
                isAula = false;
            }
        }
        this.detaillist = this.policyDetailBean.findByFilters(model.getFilterFields());
        if (isAula && detaillist.size() > 0) {
            displaydiv1 = "none";
            displaydiv2 = "block";
        } else {
            this.detaillist.clear();
            displaydiv1 = "block";
            displaydiv2 = "none";
        }
        this.summary = "";
        this.factor = "";
        this.countermeasure = "";
        this.factor = "";
        this.countermeasure = "";
        this.currentEntity = null;
    }

    public void onRowSelect(SelectEvent event) {
        PolicyDetail pd = (PolicyDetail) event.getObject();
        switch (this.queryTimeInterval) {
            case "A":
                this.summary = pd.getFyaction();
                break;
            case "B":
                this.factor = pd.getHyreason1();
                this.countermeasure = pd.getHycountermeasure1();
                break;
            case "C":
                this.factor = pd.getFyreason1();
                this.countermeasure = pd.getFycountermeasure1();
                break;
        }
    }

    public boolean isRed(BigDecimal value) {
        if (value == null) {
            return false;
        }
        if (value.compareTo(BigDecimal.valueOf(100)) == -1) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param type
     * @param unit
     * @param value
     * @return
     */
    public String formatValue(String calculationtype, String unit, String value) {
        //天,台数取整，    万元两位  %按照有效数字判断4位还是2位
        StringBuffer sb = new StringBuffer();
        sb.setLength(0);
        if (value == null || "".equals(value)) {
            return unit;
        }
        if ("B".equals(calculationtype)) {
            switch (unit) {
                case "天":
                case "台":
                    //整数
                    sb.append(floatFormat1.format(Double.valueOf(value)));
                    break;
                case "万":
                case "万元":
                    //两位小数
                    sb.append(floatFormat2.format(Double.valueOf(value)));
                    break;
                case "%":
                    //两位小数
                    sb.append(floatFormat3.format(Double.valueOf(value)));
                    break;
                default:
                    //整数或者两位小数或者四位小数
                    sb.append(floatFormat4.format(Double.valueOf(value)));
                    break;
            }
            return sb.append(unit).toString();
        } else {
            sb.append(value);
            return sb.toString();
        }
    }

    public void setCurrentEntity(PolicyDetail t) {
        this.currentEntity = t;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getFactor() {
        return factor;
    }

    public void setFactor(String factor) {
        this.factor = factor;
    }

    public String getCountermeasure() {
        return countermeasure;
    }

    public void setCountermeasure(String countermeasure) {
        this.countermeasure = countermeasure;
    }

    public List<PolicyDetail> getDetaillist() {
        return detaillist;
    }

    public String getQueryTimeInterval() {
        return queryTimeInterval;
    }

    public void setQueryTimeInterval(String queryTimeInterval) {
        this.queryTimeInterval = queryTimeInterval;
    }

    public String getQueryGenre() {
        return queryGenre;
    }

    public void setQueryGenre(String queryGenre) {
        this.queryGenre = queryGenre;
    }

    public void setDetaillist(List<PolicyDetail> detaillist) {
        this.detaillist = detaillist;
    }

    public String getDisplaydiv1() {
        return displaydiv1;
    }

    public void setDisplaydiv1(String displaydiv1) {
        this.displaydiv1 = displaydiv1;
    }

    public String getDisplaydiv2() {
        return displaydiv2;
    }

    public void setDisplaydiv2(String displaydiv2) {
        this.displaydiv2 = displaydiv2;
    }

}
