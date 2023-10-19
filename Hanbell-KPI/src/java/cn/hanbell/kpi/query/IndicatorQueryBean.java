/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.lazy.IndicatorModel;
import cn.hanbell.kpi.web.SuperQueryBean;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "indicatorQueryBean")
@ViewScoped
public class IndicatorQueryBean extends SuperQueryBean<Indicator> {

    @EJB
    private IndicatorBean indicatorBean;

    private String queryDeptno;
    private String queryDeptname;
    List<String> types;
    public IndicatorQueryBean() {
        super(Indicator.class);
    }

    @Override
    public void init() {
        superEJB = indicatorBean;
        model = new IndicatorModel(indicatorBean, userManagedBean);
        model.getSortFields().put("sortid", "ASC");
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("seq", userManagedBean.getY());
        types=new ArrayList<String>();
        types.add("D");
        types.add("P");
        model.getFilterFields().put("objtype IN ", types);
        params = ec.getRequestParameterValuesMap();
        if (params != null) {
            if (params.containsKey("deptno")) {
                queryDeptno = params.get("deptno")[0];
            }
        }
        if (queryDeptno != null && !"".equals(queryDeptno)) {
            model.getFilterFields().put("deptno", queryDeptno);
        }
        super.init();
    }

    @Override
    public void query() {
        model.getFilterFields().clear();
        if (this.queryName != null && !"".equals(this.queryName)) {
            model.getFilterFields().put("name", this.queryName);
        }
        if (this.queryFormId != null && !"".equals(this.queryFormId)) {
            model.getFilterFields().put("formid", this.queryFormId);
        }
        if (this.queryDeptno != null && !"".equals(this.queryDeptno)) {
            model.getFilterFields().put("deptno", this.queryDeptno);
        }
        if (this.queryDeptname != null && !"".equals(this.queryDeptname)) {
            model.getFilterFields().put("deptname", this.queryDeptname);
        }
        model.getFilterFields().put("seq", userManagedBean.getY());
        model.getFilterFields().put("objtype IN ", types);
    }

    @Override
    public void reset() {
        super.reset();
        model.getFilterFields().put("seq", userManagedBean.getY());
        model.getFilterFields().put("objtype IN ", types);
    }

    /**
     * @return the queryDeptno
     */
    public String getQueryDeptno() {
        return queryDeptno;
    }

    /**
     * @param queryDeptno the queryDeptno to set
     */
    public void setQueryDeptno(String queryDeptno) {
        this.queryDeptno = queryDeptno;
    }

    /**
     * @return the queryDeptname
     */
    public String getQueryDeptname() {
        return queryDeptname;
    }

    /**
     * @param queryDeptname the queryDeptname to set
     */
    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

}
