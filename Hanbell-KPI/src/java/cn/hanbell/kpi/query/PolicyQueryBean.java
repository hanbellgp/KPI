/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

import cn.hanbell.kpi.ejb.PolicyDetailBean;
import cn.hanbell.kpi.entity.PolicyDetail;
import cn.hanbell.kpi.lazy.PolicyDetailModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import com.lightshell.comm.BaseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "policyQueryBean")
@ViewScoped
public class PolicyQueryBean extends SuperQueryBean<PolicyDetail> {

    @EJB
    private PolicyDetailBean policyDetailBean;

    private List<PolicyDetail> detailList;
    private List<PolicyDetail> selectDetail;
    private String queryPerspective;
    private String queryObjective;
    private String querySeqName;
    private String queryName;
    private String queryDeptno;
    private String queryDeptname;

    Map<String, Object> filterFields;
    Map<String, String> sortFields;

    public PolicyQueryBean() {
        super(PolicyDetail.class);
    }

    @Override
    public void init() {
        filterFields = new HashMap();
        sortFields = new HashMap();
        superEJB = policyDetailBean;
        sortFields.put("seq", "ASC");
        filterFields.put("parent.year", userManagedBean.getY());
        params = ec.getRequestParameterValuesMap();
        detailList = policyDetailBean.findByFilters(filterFields, sortFields);
    }

    @Override
    public void query() {
        filterFields.clear();
        if (this.queryPerspective != null && !"".equals(this.queryPerspective)) {
            filterFields.put("perspective", this.queryPerspective);
        }
        if (this.queryObjective != null && !"".equals(this.queryObjective)) {
            filterFields.put("objective", this.queryObjective);
        }
        if (this.querySeqName != null && !"".equals(this.querySeqName)) {
            filterFields.put("seqname", this.querySeqName);
        }
        if (this.queryName != null && !"".equals(this.queryName)) {
            filterFields.put("name.", this.queryName);
        }
        if (this.queryDeptno != null && !"".equals(this.queryDeptno)) {
            filterFields.put("parent.deptno", this.queryDeptno);
        }
        if (this.queryDeptname != null && !"".equals(this.queryDeptname)) {
            filterFields.put("parent.deptna", this.queryDeptname);
        }
        filterFields.put("parent.year", userManagedBean.getY());
        detailList = policyDetailBean.findByFilters(filterFields, sortFields);
    }
    
    @Override
    public void closeDialog() {
        if (this.selectDetail != null && !this.selectDetail.isEmpty()) {
            PrimeFaces.current().dialog().closeDynamic(this.selectDetail);
        } else {
            this.showWarnMsg("Warn", "没有选择数据!");
        }
    }


    

    public String getQueryPerspective() {
        return queryPerspective;
    }

    public void setQueryPerspective(String queryPerspective) {
        this.queryPerspective = queryPerspective;
    }

    public String getQueryObjective() {
        return queryObjective;
    }

    public void setQueryObjective(String queryObjective) {
        this.queryObjective = queryObjective;
    }

    public String getQuerySeqName() {
        return querySeqName;
    }

    public void setQuerySeqName(String querySeqName) {
        this.querySeqName = querySeqName;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryDeptno() {
        return queryDeptno;
    }

    public void setQueryDeptno(String queryDeptno) {
        this.queryDeptno = queryDeptno;
    }

    public String getQueryDeptname() {
        return queryDeptname;
    }

    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

    public List<PolicyDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<PolicyDetail> detailList) {
        this.detailList = detailList;
    }

    public List<PolicyDetail> getSelectDetail() {
        return selectDetail;
    }

    public void setSelectDetail(List<PolicyDetail> selectDetail) {
        this.selectDetail = selectDetail;
    }

}
