/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.PolicyBean;
import cn.hanbell.kpi.ejb.PolicyDetailBean;
import cn.hanbell.kpi.entity.Category;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.PolicyDetail;
import cn.hanbell.kpi.entity.tms.Project;
import cn.hanbell.kpi.lazy.PolicyDetailModel;
import cn.hanbell.kpi.lazy.PolicyModel;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
    private Policy policy;
    private TreeNode rootNode;
    private TreeNode selectedNode;
    private Calendar c;
    List<GroupRow> groupRows;
    private List<PolicyDetail> cDetail; //C成本构面
    private List<PolicyDetail> qDetail; //Q顾客/品质构面
    private List<PolicyDetail> dDetail; //D流程/交期构面
    private List<PolicyDetail> pDetail; //P学习成长/人员构面

    private String summary;
    private String factor;
    private String countermeasure;

    public PolicySheetReportBean() {
        super(PolicyDetail.class);
    }

    @Override
    public void init() {
        int i = 0;
        superEJB = policyDetailBean;
        model = new PolicyDetailModel(policyDetailBean, userManagedBean);
        params = ec.getRequestParameterValuesMap();
        if (params != null) {
            if (params.containsKey("id")) {
                i = Integer.parseInt(params.get("id")[0]);
            }
        }
        if (i > 0) {
            model.getFilterFields().put("pid", i);
            policy = policyBean.findById(i);
            groupRows = new ArrayList<>();
            model.getFilterFields().put("genre", "C");
            cDetail = policyDetailBean.findByFilters(model.getFilterFields());
            groupRows.add(new GroupRow("C成本构面", cDetail));
            model.getFilterFields().remove("genre");
            model.getFilterFields().put("genre", "Q");
            qDetail = policyDetailBean.findByFilters(model.getFilterFields());
            groupRows.add(new GroupRow("Q顾客/品质构面", qDetail));
            model.getFilterFields().remove("genre");
            model.getFilterFields().put("genre", "D");
            dDetail = policyDetailBean.findByFilters(model.getFilterFields());
            groupRows.add(new GroupRow("D流程/交期构面", dDetail));
            model.getFilterFields().remove("genre");
            model.getFilterFields().put("genre", "P");
            pDetail = policyDetailBean.findByFilters(model.getFilterFields());
            groupRows.add(new GroupRow("P学习成长/人员构面", pDetail));
            if (cDetail != null && !cDetail.isEmpty()) {
                this.setCurrentEntity((PolicyDetail) cDetail.get(0));
            } else {
                this.setCurrentEntity(this.getNewEntity());
            }
             deny = false;
        }
    }

    public int compareTo(int a, int b) {
        if (a > b) {
            return 1;
        } else if (a == b) {
            return 0;
        } else {
            return -1;
        }
    }

    public void onRowSelect(SelectEvent event) {
        PolicyDetail pd = (PolicyDetail) event.getObject();
        switch (this.userManagedBean.getM()) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
                this.factor = pd.getHyreason1();
                this.countermeasure = pd.getHycountermeasure1();
                break;
            case 7:
            case 8:
            case 9:
            case 11:
            case 12:
                this.factor = pd.getFyreason1();
                this.countermeasure = pd.getFycountermeasure1();
                break;
            case 10:
                this.summary = pd.getFyaction();
                break;
        }
    }

    public boolean isRed(BigDecimal value){
         if(value==null){
             return false;
         }
        if(value.compareTo(BigDecimal.valueOf(100))==-1){
            return true;
        }
       
        return false;
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

    public TreeNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(TreeNode rootNode) {
        this.rootNode = rootNode;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public List<GroupRow> getGroupRows() {
        return groupRows;
    }

    public void setGroupRows(List<GroupRow> groupRows) {
        this.groupRows = groupRows;
    }

    public List<PolicyDetail> getcDetail() {
        return cDetail;
    }

    public void setcDetail(List<PolicyDetail> cDetail) {
        this.cDetail = cDetail;
    }

    public List<PolicyDetail> getqDetail() {
        return qDetail;
    }

    public void setqDetail(List<PolicyDetail> qDetail) {
        this.qDetail = qDetail;
    }

    public List<PolicyDetail> getdDetail() {
        return dDetail;
    }

    public void setdDetail(List<PolicyDetail> dDetail) {
        this.dDetail = dDetail;
    }

    public List<PolicyDetail> getpDetail() {
        return pDetail;
    }

    public void setpDetail(List<PolicyDetail> pDetail) {
        this.pDetail = pDetail;
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

    public class GroupRow {

        private String name;
        private List<PolicyDetail> details;

        public GroupRow(String name, List<PolicyDetail> details) {
            this.name = name;
            this.details = details;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<PolicyDetail> getDetails() {
            return details;
        }

        public void setDetails(List<PolicyDetail> details) {
            this.details = details;
        }

    }

}
