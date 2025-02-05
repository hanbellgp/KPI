/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.SalesTableBean;
import cn.hanbell.kpi.entity.ClientRanking;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.web.BscQueryTableManageBean;
import java.io.Serializable;
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
@ManagedBean(name = "auditClientRankingReportBean")
@ViewScoped
public class AuditClientRankingReportBean extends BscQueryTableManageBean implements Serializable {

    @EJB
    protected SalesTableBean salesTableBean;
    @EJB
    protected IndicatorChartBean indicatorChartBean;

    protected LinkedHashMap<String, String> map;
    protected List<ClientRanking> list;

    protected Date querydate;
    protected boolean monthchecked;
    protected boolean aggregatechecked;
    protected String rowsPerPage;

    protected String deptno;
    protected int year;
    protected int month;
    protected final DecimalFormat df;

    public AuditClientRankingReportBean() {
        this.df = new DecimalFormat("#,###");
    }

    public Calendar getCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }

    public String dfShpqy1(String a) {
        return df.format(Double.parseDouble(a));
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
        //根据备注取对应部门代号
        deptno = indicatorChart.getRemark().trim();
        map = new LinkedHashMap<>();
        reset();
    }

    public void reset() {
        querydate = userManagedBean.getBaseDate();
        monthchecked = true;
        aggregatechecked = false;
        rowsPerPage = "30";
        map.clear();
        year = getCalendar(querydate).get(Calendar.YEAR);
        month = getCalendar(querydate).get(Calendar.MONTH) + 1;
        map.put("title", year + "年" + month + "月");
        map.put("nowtitle", "当月");
        map.put("ulttitle", "上月");
        list = new ArrayList<>();
        finddeptno();
    }

    public void finddeptno() {
        switch (deptno) {
            case "1F000":
                map.put("deptnoname", "制冷产品部");
                map.put("daname", "制冷产品");
                map.put("n_code_DA", "= 'R'");
                break;
            case "1F330":
                map.put("deptnoname", "制冷产品部");
                map.put("daname", "制冷冷冻");
                map.put("n_code_DA", "= 'R'");
                map.put("n_code_DC", "= 'L'");
                break;
            case "1F310":
                map.put("deptnoname", "制冷产品部");
                map.put("daname", "空调热泵");
                map.put("n_code_DA", "= 'R'");
                map.put("n_code_DC", " IN ('R','H') ");
                break;
            case "1F340":
                map.put("deptnoname", "制冷产品部");
                map.put("daname", "离心机");
                map.put("n_code_DA", "= 'R'");
                map.put("n_code_DC", " IN ('RT') ");
                break;
            case "1Q000":
                map.put("deptnoname", "空压机组产品部");
                map.put("daname", "空压机组");
                map.put("n_code_DA", "= 'AA'");
                break;
            case "1Q000-AA":
                map.put("deptnoname", "空压机组产品部");
                map.put("daname", "空压机组");
                map.put("n_code_DA", "= 'AA'");
                map.put("n_code_DC", " <>'SDS' ");
                break;
            case "1Q000-SDS":
                map.put("deptnoname", "空压机组产品部");
                map.put("daname", "SDS");
                map.put("n_code_DA", "= 'AA'");
                map.put("n_code_DC", " = 'SDS' ");
                break;
              case "1G000":
                map.put("deptnoname", "空压机体营销一课");
                map.put("daname", "空压机体");
                map.put("n_code_DA", "= 'AH'");
                break;
                
            case "1G000-AH":
                map.put("deptnoname", "空压机体营销一课");
                map.put("daname", "A机体");
                map.put("n_code_DA", "= 'AH'");
                map.put("n_code_DC", " LIKE 'A%'");
                break;
            case "1G000-S":
                map.put("deptnoname", "空压机体涡旋");
                map.put("daname", "涡旋");
                map.put("n_code_DA", " in ('AH','S') ");
                map.put("n_code_DC", " in ('SAM-3HP','SAM-5HP','SAM-7HP','SAM-10HP','SF','SC') ");
                break;
            case "1H000":
                map.put("deptnoname", "真空产品部");
                map.put("daname", "真空泵");
                map.put("n_code_DA", "= 'P'");
                break;
            case "5B000":
            case "8A000":
                map.put("deptnoname", "再生能源部");
                map.put("daname", "再生能源");
                map.put("n_code_DA", "= 'OH'");
                break;
            case "5C000":
                map.put("deptnoname", "涡轮产品部");
                map.put("daname", "涡轮产品");
                map.put("n_code_DA", "= 'RT'");
                break;
            case "50000":
                map.put("deptnoname", "柯茂");
                map.put("daname", "柯茂");
                map.put("n_code_DA", " In('RT','OH') ");
                break;
            case "11000":
                map.put("deptnoname", "汉钟");
                map.put("daname", "汉钟");
                map.put("n_code_DA", " Not In('RT','OH') ");
                break;
            case "10000": 
                map.put("deptnoname", "汉钟+浙江柯茂+上海柯茂");
                map.put("daname", "汉钟+浙江柯茂+上海柯茂");
                map.put("n_code_DA", "  In('RT','OH','AA','AH','P','R') ");
                break;
            default:
                map.put("deptnoname", "");
                map.put("daname", "");
                map.put("n_code_DA", "");
                map.put("n_code_DC", "");
        }
    }

    public void query() {
        year = getCalendar(querydate).get(Calendar.YEAR);
        month = getCalendar(querydate).get(Calendar.MONTH) + 1;
        try {
            Boolean aa = true;
            if (querydate.compareTo(new Date()) == 1) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "查询时间不可超过当前日期"));
                aa = false;
            }
            if (aa) {
                //显示标题
                if (monthchecked) {
                    map.put("title", year + "年" + month + "月");
                    map.put("ulttitle", month == 1 ? (year - 1) + "年12月" : "上月");
                    map.put("nowtitle", "当月");
                } else {
                    if (month > 1) {
                        map.put("title", year + "年1月至" + month + "月累计");
                    } else {
                        map.put("title", "1月");
                    }
                    map.put("nowtitle", "本年");
                }
                list = salesTableBean.getClientList(year, month, map, monthchecked, aggregatechecked, rowsPerPage);
                if (list.size() <= 0) {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
                } else {
                    super.getRemarkOne(indicatorChart, year, month);
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
        }
    }

    /**
     * @return the clientlist
     */
    public List<ClientRanking> getList() {
        return list;
    }

    /**
     * @param list the list to set
     */
    public void setList(List<ClientRanking> list) {
        this.list = list;
    }

    /**
     * @param analysisCount the analysisCount to set
     */
    public void setAnalysisCount(int analysisCount) {
        this.analysisCount = analysisCount;
    }

    /**
     * @param summaryCount the summaryCount to set
     */
    public void setSummaryCount(int summaryCount) {
        this.summaryCount = summaryCount;
    }

    /**
     * @return the querydate
     */
    public Date getQuerydate() {
        return querydate;
    }

    /**
     * @param querydate the querydate to set
     */
    public void setQuerydate(Date querydate) {
        this.querydate = querydate;
    }

    /**
     * @return the monthchecked
     */
    public boolean isMonthchecked() {
        return monthchecked;
    }

    /**
     * @param monthchecked the monthchecked to set
     */
    public void setMonthchecked(boolean monthchecked) {
        this.monthchecked = monthchecked;
    }

    /**
     * @return the aggregatechecked
     */
    public boolean isAggregatechecked() {
        return aggregatechecked;
    }

    /**
     * @param aggregatechecked the aggregatechecked to set
     */
    public void setAggregatechecked(boolean aggregatechecked) {
        this.aggregatechecked = aggregatechecked;
    }

    /**
     * @return the rowsPerPage
     */
    public String getRowsPerPage() {
        return rowsPerPage;
    }

    /**
     * @param rowsPerPage the rowsPerPage to set
     */
    public void setRowsPerPage(String rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    /**
     * @return the map
     */
    public LinkedHashMap<String, String> getMap() {
        return map;
    }

    /**
     * @param map the map to set
     */
    public void setMap(LinkedHashMap<String, String> map) {
        this.map = map;
    }

    /**
     * @return the deptno
     */
    public String getDeptno() {
        return deptno;
    }

}
