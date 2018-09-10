/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.rpt;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.ejb.ClientNowAndPastBean;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.entity.ClientTable;
import cn.hanbell.kpi.entity.IndicatorChart;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C1879
 */
@ManagedBean(name = "shipmentTopManagedBean")
@ViewScoped
public class ShipmentTopManagedBean implements Serializable {

    @EJB
    private ClientNowAndPastBean clientrank;

    @EJB
    protected IndicatorChartBean indicatorChartBean;

    @ManagedProperty(value = "#{userManagedBean}")
    protected UserManagedBean userManagedBean;

    private Integer year;
    private Integer month;
    private LinkedHashMap<String, String> map;
    private List<ClientTable> clientlist;

    FacesContext fc;
    ExternalContext ec;
    protected IndicatorChart indicatorChart;
    private boolean checkbox;

    public ShipmentTopManagedBean() {
    }

    public int findyear() {
        Calendar c = Calendar.getInstance();
        c.setTime(userManagedBean.getBaseDate());
        return c.get(Calendar.YEAR);
    }

    public int findmonth() {
        Calendar c = Calendar.getInstance();
        c.setTime(userManagedBean.getBaseDate());
        return c.get(Calendar.MONTH) + 1;
    }

    public void initial() {
        setClientlist(new ArrayList<>());
        getMap().put("title", "");
        getMap().put("futitle", "");
        getMap().put("year", "");
        getMap().put("style", "");
    }

    @PostConstruct
    public void construct() {
        setCheckbox(true);
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
        }
        year = findyear();
        month = findmonth();

        map = new LinkedHashMap<>();
        setClientlist(new ArrayList<>());
    }

    public void refreshxhtml() {
        year = findyear();
        month = findmonth();
        setCheckbox(true);
        initial();
    }

    public boolean createtitle() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int m = cal.get(Calendar.MONTH) + 1;
        int y = cal.get(Calendar.YEAR);
        if (getYear() == null || getYear() > y || getMonth() > 12 || getMonth() < 1) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "输入的年份或月份值有误请重新填写！"));
            return false;
        } else {
            getMap().put("year", getYear() + "年");
            if (isCheckbox()) {
                getMap().put("title", getMonth() + "月");
                getMap().put("futitle", "当月");
            } else if (getYear() == findyear()) {
                if (getMonth() > 1) {
                    if (getMonth() > findmonth()) {
                        getMap().put("title", "1月至" + findmonth() + "月累计");
                    } else {
                        getMap().put("title", "1月至" + getMonth() + "月累计");
                    }
                } else {
                    getMap().put("title", "1月");
                }
                getMap().put("futitle", "本年");
            } else {
                if (getMonth() > 1) {
                    getMap().put("title", "1月至" + getMonth() + "月累计");
                } else {
                    getMap().put("title", "1月");
                }
                getMap().put("futitle", "本年");
            }
            return true;
        }

    }

    public void finddeptno() {
        String deptno = indicatorChart.getDeptno();
        switch (deptno) {
            case "1F000":
                getMap().put("facno", "C,C4,N,G,J");
                getMap().put("deptnoname", "制冷产品部");
                getMap().put("daname", "制冷产品");
                getMap().put("n_code_DA", "= 'R'");
                getMap().put("depno", " IN ('1B000','1C000','1B000','1D000','1B000','1E000','1V000') ");
                getMap().put("ogdkid", " IN ('RL01') ");
                break;
            case "1Q000":
                getMap().put("facno", "C");
                getMap().put("deptnoname", "空压机组产品部");
                getMap().put("daname", "空压机组");
                getMap().put("n_code_DA", "= 'AA'");
                getMap().put("depno", " IN ('1Q000','1Q100') ");
                getMap().put("ogdkid", " IN ('RL01','RL03') ");
                break;
            case "1G100":
                getMap().put("facno", "C");
                getMap().put("deptnoname", "空压机体营销一课");
                getMap().put("daname", "A机体");
                getMap().put("n_code_DA", "= 'AH'");
                getMap().put("n_code_DC", " LIKE 'AJ%'");
                getMap().put("depno", " IN ('1G110','1T100') ");
                getMap().put("ogdkid", " IN ('RL01','RL03') ");
                break;
            case "1G500":
                getMap().put("facno", "C");
                getMap().put("deptnoname", "空压机体营销二课");
                getMap().put("daname", "SDS无油");
                getMap().put("n_code_DA", "= 'AH'");
                getMap().put("n_code_DC", " = 'SDS' ");
                getMap().put("depno", " IN ('1G500') ");
                getMap().put("ogdkid", " IN ('RL01','RL03') ");
                break;
            case "1H000":
                getMap().put("facno", "C");
                getMap().put("deptnoname", "真空产品部");
                getMap().put("daname", "真空泵");
                getMap().put("n_code_DA", "= 'P'");
                break;
            case "1U000":
                getMap().put("facno", "C");
                getMap().put("deptnoname", "涡旋产品部");
                getMap().put("daname", "涡旋");
                getMap().put("n_code_DA", "= 'S'");
                break;
            case "5B000":
                getMap().put("facno", "K");
                getMap().put("deptnoname", "再生能源部");
                getMap().put("daname", "再生能源");
                getMap().put("n_code_DA", "= 'OH'");
                break;
            case "5A000":
                getMap().put("facno", "K");
                getMap().put("deptnoname", "制冷机组产品部");
                getMap().put("daname", "RT制冷");
                getMap().put("n_code_DA", "= 'RT'");
                break;
            case "50000":
                getMap().put("facno", "K");
                getMap().put("deptnoname", "柯茂");
                getMap().put("daname", "离心机");
                getMap().put("n_code_DA", " In('RT','OH') ");
                break;
            default:
                getMap().put("facno", "");
                getMap().put("decode", "");
                getMap().put("deptnoname", "");
                getMap().put("daname", "");
                getMap().put("n_code_DA", "");
                getMap().put("n_code_DC", "");

        }
    }

    public void findclient() {
        initial();
        try {
            if (createtitle()) {
                finddeptno();
                int m;
                int y = getYear();
                if (isCheckbox()) {
                    //指定年份月份
                    m = getMonth();
                    getMap().put("style", "nowmonth");
                } else {
                    if (y != findyear()) {
                        m = getMonth();
                    } else {
                        //当前年1月至登陆下方日期月份
                        if (getMonth() > findmonth()) {
                            m = findmonth();
                        } else {
                            m = getMonth();
                        }
                    }
                }
                String deptno = indicatorChart.getDeptno();
                List<ClientTable> list= clientrank.getNowClient(y, m, getMap());
                if (list.size() > 0) {
                    setClientlist(list);
                } else {
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "无法查询到该日期的数据，请重新查询！"));
                }
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.toString()));
        }
    }

    /**
     * @return the year
     */
    public Integer getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * @return the month
     */
    public Integer getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(Integer month) {
        this.month = month;
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
     * @return the clientlist
     */
    public List<ClientTable> getClientlist() {
        return clientlist;
    }

    /**
     * @param clientlist the clientlist to set
     */
    public void setClientlist(List<ClientTable> clientlist) {
        this.clientlist = clientlist;
    }

    /**
     * @return the userManagedBean
     */
    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    /**
     * @param userManagedBean the userManagedBean to set
     */
    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    /**
     * @return the checkbox
     */
    public boolean isCheckbox() {
        return checkbox;
    }

    /**
     * @param checkbox the checkbox to set
     */
    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

}
