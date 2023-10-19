/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemGrantModuleBean;
import cn.hanbell.eap.ejb.SystemGrantPrgBean;
import cn.hanbell.eap.ejb.SystemRoleBean;
import cn.hanbell.eap.ejb.SystemRoleDetailBean;
import cn.hanbell.eap.entity.SystemGrantModule;
import cn.hanbell.eap.entity.SystemGrantPrg;
import cn.hanbell.eap.entity.SystemRole;
import cn.hanbell.eap.entity.SystemRoleDetail;
import cn.hanbell.kpi.ejb.IndicatorChartBean;
import cn.hanbell.kpi.ejb.IndicatorDepartmentBean;
import cn.hanbell.kpi.ejb.PolicyBean;
import cn.hanbell.kpi.ejb.RoleDetailBean;
import cn.hanbell.kpi.ejb.RoleGrantModuleBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.entity.IndicatorChart;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.RoleDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.Scorecard;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author kevindong
 */
@ManagedBean(name = "menuManagedBean")
@SessionScoped
public class MenuManagedBean implements Serializable {

    @EJB
    private IndicatorChartBean indicatorChartBean;
    @EJB
    private IndicatorDepartmentBean indicatorDepartmentBean;
    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private PolicyBean policyBean;
    @EJB
    private RoleDetailBean roleDetailBean;
    @EJB
    private RoleGrantModuleBean roleGrantModuleBean;

    @EJB
    private SystemRoleDetailBean systemRoleDetailBean;
    @EJB
    private SystemGrantModuleBean systemGrantModuleBean;
    @EJB
    private SystemGrantPrgBean systemGrantPrgBean;
    @EJB
    private SystemRoleBean systemRoleBean;

    @ManagedProperty(value = "#{userManagedBean}")
    private UserManagedBean userManagedBean;

    private List<SystemGrantModule> userModuleGrantList;
    private List<SystemGrantModule> roleModuleGrantList;
    private List<SystemGrantModule> moduleGrantList;
    private List<SystemGrantPrg> userPrgGrantList;
    private List<SystemGrantPrg> rolePrgGrantList;
    private List<SystemGrantPrg> prgGrantList;
    private List<SystemRoleDetail> systemRoleList;

    private List<RoleGrantModule> roleGrantList;
    private List<RoleGrantModule> grantList;
    private List<RoleDetail> roleDetailList;

    private List<IndicatorChart> indicatorChartList;
    private List<IndicatorDepartment> indicatorDepartmentList;
    private List<Scorecard> scorecardList;
    private List<Policy> policyList;
    private MenuModel model;

    public MenuManagedBean() {
    }

    @PostConstruct
    public void init() {

        boolean flag;
        moduleGrantList = new ArrayList<>();
        prgGrantList = new ArrayList<>();
        // KPI授權列表
        grantList = new ArrayList<>();

        model = new DefaultMenuModel();

        if (getUserManagedBean() != null) {

            String company = userManagedBean.getCompany();
            int y = userManagedBean.getY();

            DefaultSubMenu kpimenu;
            DefaultSubMenu appmenu;
            DefaultSubMenu submenu;
            DefaultMenuItem menuitem;

            menuitem = new DefaultMenuItem("Home");
            menuitem.setId("menu_home");
            menuitem.setOutcome("home");
            menuitem.setIcon("dashboard");

            model.addElement(menuitem);

            appmenu = new DefaultSubMenu("应用");
            appmenu.setIcon("menu");
            // 将用户权限和角色权限合并后产生菜单,用户权限优先角色权限
            moduleGrantList.clear();
            userModuleGrantList
                    = systemGrantModuleBean.findBySystemNameAndUserId("KPI", userManagedBean.getCurrentUser().getId());
            userModuleGrantList.forEach((m) -> {
                moduleGrantList.add(m);
            });
            prgGrantList.clear();
            userPrgGrantList
                    = systemGrantPrgBean.findBySystemNameAndUserId("KPI", userManagedBean.getCurrentUser().getId());
            userPrgGrantList.forEach((p) -> {
                prgGrantList.add(p);
            });
            systemRoleList = systemRoleDetailBean.findByUserId(userManagedBean.getCurrentUser().getId());
            for (SystemRoleDetail r : systemRoleList) {
                roleModuleGrantList = systemGrantModuleBean.findBySystemNameAndRoleId("KPI", r.getPid());
                if (moduleGrantList.isEmpty()) {
                    moduleGrantList.addAll(roleModuleGrantList);
                } else {
                    for (SystemGrantModule m : roleModuleGrantList) {
                        flag = true;
                        for (SystemGrantModule e : moduleGrantList) {
                            if (e.getSystemModule().getId().compareTo(m.getSystemModule().getId()) == 0) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            moduleGrantList.add(m);
                        }
                    }
                }
                rolePrgGrantList = systemGrantPrgBean.findBySystemNameAndRoleId("KPI", r.getPid());
                if (prgGrantList.isEmpty()) {
                    prgGrantList.addAll(rolePrgGrantList);
                } else {
                    for (SystemGrantPrg p : rolePrgGrantList) {
                        flag = true;
                        for (SystemGrantPrg e : prgGrantList) {
                            if (e.getSysprg().getId().compareTo(p.getSysprg().getId()) == 0) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            prgGrantList.add(p);
                        }
                    }
                }
            }
            moduleGrantList.sort((SystemGrantModule o1, SystemGrantModule o2) -> {
                if (o1.getSystemModule().getSortid() < o2.getSystemModule().getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            prgGrantList.sort((SystemGrantPrg o1, SystemGrantPrg o2) -> {
                if (o1.getSysprg().getSortid() < o2.getSysprg().getSortid()) {
                    return -1;
                } else {
                    return 1;
                }
            });
            userManagedBean.setSystemGrantPrgList(prgGrantList);
            for (SystemGrantModule m : moduleGrantList) {
                submenu = new DefaultSubMenu(m.getSystemModule().getName());
                submenu.setIcon("menu");
                for (SystemGrantPrg p : prgGrantList) {
                    if (p.getPid() == m.getSystemModule().getId()) {
                        menuitem = new DefaultMenuItem(p.getSysprg().getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(p.getSysprg().getApi());
                        submenu.addElement(menuitem);
                    }
                }
                appmenu.addElement(submenu);
            }
            model.addElement(appmenu);

            roleDetailList = roleDetailBean.findByUserId(userManagedBean.getCurrentUser().getUserid());
            for (RoleDetail r : roleDetailList) {
                roleGrantList = roleGrantModuleBean.findByRoleId(r.getPid());
                if (grantList.isEmpty()) {
                    grantList.addAll(roleGrantList);
                } else {
                    for (RoleGrantModule m : roleGrantList) {
                        flag = true;
                        for (RoleGrantModule e : grantList) {
                            if (e.getDeptno().equals(m.getDeptno())) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            grantList.add(m);
                        }
                    }
                }
            } // 得到授權查看的部門列表
            grantList.sort((RoleGrantModule o1, RoleGrantModule o2) -> {
                if (o1.getDeptno().compareTo(o2.getDeptno()) < 1) {
                    return -1;
                } else {
                    return 1;
                }
            });
            userManagedBean.setRoleGrantDeptList(grantList);

            submenu = null;
            kpimenu = new DefaultSubMenu("部门KPI");
            kpimenu.setIcon("menu");
            for (RoleGrantModule r : grantList) {
                indicatorDepartmentList
                        = indicatorDepartmentBean.findByCompanyDeptnoTypeAndYear(company, r.getDeptno(), "D", y);
                if (indicatorDepartmentList != null && !indicatorDepartmentList.isEmpty()) {
                    submenu = new DefaultSubMenu(r.getDept());
                    submenu.setIcon("menu");
                    for (IndicatorDepartment i : indicatorDepartmentList) {
                        menuitem
                                = new DefaultMenuItem(String.valueOf(i.getParent().getSeq()) + i.getParent().getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(i.getParent().getApi());
                        menuitem.setParam("id", i.getParent().getId());
                        submenu.addElement(menuitem);
                    }
                    if (submenu != null) {
                        kpimenu.addElement(submenu);
                    }
                }
            }
            model.addElement(kpimenu);

            submenu = null;
            kpimenu = new DefaultSubMenu("产品KPI");
            kpimenu.setIcon("menu");
            for (RoleGrantModule r : grantList) {
                indicatorDepartmentList
                        = indicatorDepartmentBean.findByCompanyDeptnoTypeAndYear(company, r.getDeptno(), "P", y);
                if (indicatorDepartmentList != null && !indicatorDepartmentList.isEmpty()) {
                    submenu = new DefaultSubMenu(r.getDept());
                    submenu.setIcon("menu");
                    for (IndicatorDepartment i : indicatorDepartmentList) {
                        menuitem
                                = new DefaultMenuItem(String.valueOf(i.getParent().getSeq()) + i.getParent().getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(i.getParent().getApi());
                        menuitem.setParam("id", i.getParent().getId());
                        submenu.addElement(menuitem);
                    }
                    if (submenu != null) {
                        kpimenu.addElement(submenu);
                    }
                }
            }
            model.addElement(kpimenu);

            kpimenu = new DefaultSubMenu("绩效考核");
            kpimenu.setIcon("menu");
            for (RoleGrantModule r : grantList) {
                submenu = null;
                // scorecardList = scorecardBean.findByMenuAndYear(r.getDeptno(), y);
                scorecardList = scorecardBean.findByCompanyMenuAndYear(company, r.getDeptno(), y);
                if (scorecardList != null && !scorecardList.isEmpty()) {
                    if (submenu == null) {
                        submenu = new DefaultSubMenu(r.getDept());
                        submenu.setIcon("menu");
                    }
                    // 重新排序
                    scorecardList.sort((Scorecard o1, Scorecard o2) -> {
                        if (o1.getSortid() <= o2.getSortid() && o1.getDeptno().compareTo(o2.getDeptno()) < 0) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                    for (Scorecard i : scorecardList) {
                        // 考核模版不用显示
                        if (i.getTemplate()) {
                            continue;
                        }
                        menuitem = new DefaultMenuItem(i.getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(i.getApi());
                        menuitem.setParam("id", i.getId());
                        submenu.addElement(menuitem);
                    }
                }
                if (submenu != null) {
                    kpimenu.addElement(submenu);
                }
            }
            model.addElement(kpimenu);

            kpimenu = new DefaultSubMenu("方针展开");
            kpimenu.setIcon("menu");
            for (RoleGrantModule r : grantList) {
                submenu = null;
                // scorecardList = scorecardBean.findByMenuAndYear(r.getDeptno(), y);
                policyList = policyBean.findByCompanyMenuAndYear(company, r.getDeptno(), y);
                if (policyList != null && !policyList.isEmpty()) {
                    if (submenu == null) {
                        submenu = new DefaultSubMenu(r.getDept());
                        submenu.setIcon("menu");
                    }
                    // 重新排序
                    policyList.sort((Policy o1, Policy o2) -> {
                        if (o1.getDeptno().compareTo(o2.getDeptno()) < 0) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                    for (Policy i : policyList) {
                        menuitem = new DefaultMenuItem(i.getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(i.getApi());
                        menuitem.setParam("id", i.getId());
                        submenu.addElement(menuitem);
                    }
                }
                if (submenu != null) {
                    kpimenu.addElement(submenu);
                }
            }
            model.addElement(kpimenu);

            kpimenu = new DefaultSubMenu("经营汇报");
            kpimenu.setIcon("menu");
            for (RoleGrantModule r : grantList) {
                submenu = null;
                indicatorChartList = indicatorChartBean.findByCompanyAndPId(company, r.getDeptno());
                if (indicatorChartList != null && !indicatorChartList.isEmpty()) {
                    if (submenu == null) {
                        submenu = new DefaultSubMenu(r.getDept());
                        submenu.setIcon("menu");
                    }
                    // 按报表名称重新排序
                    indicatorChartList.sort((IndicatorChart o1, IndicatorChart o2) -> {
                        if (o1.getSortid() <= o2.getSortid()) {
                            return -1;
                        } else {
                            return 1;
                        }
                    });
                    for (IndicatorChart i : indicatorChartList) {
                        menuitem = new DefaultMenuItem(i.getName());
                        menuitem.setIcon("menu");
                        menuitem.setOutcome(i.getApi());
                        menuitem.setParam("id", i.getId());
                        submenu.addElement(menuitem);
                    }
                }
                if (submenu != null) {
                    kpimenu.addElement(submenu);
                }
            }
            model.addElement(kpimenu);

            kpimenu = new DefaultSubMenu("KPI汇报");
            kpimenu.setIcon("menu");
            Set<Scorecard> list = new TreeSet<Scorecard>((o1, o2) -> o2.compareTo(o1));
            for (RoleGrantModule r : grantList) {
                submenu = null;
                scorecardList = scorecardBean.findByCompanyAndMenuAndIsBscAndYear(company, r.getDeptno(), true, y);
                if (scorecardList != null && !scorecardList.isEmpty()) {
                    list.addAll(scorecardList);
                }
            }
            for (Scorecard s : list) {
                menuitem = new DefaultMenuItem(s.getName());
                menuitem.setIcon("menu");
                menuitem.setOutcome("scorecardbsc");
                menuitem.setParam("id", s.getId());
                kpimenu.addElement(menuitem);
            }
            model.addElement(kpimenu);

            kpimenu = new DefaultSubMenu("方针汇报");
            kpimenu.setIcon("menu");
            Set<Policy> list1 = new TreeSet<Policy>((o1, o2) -> o2.compareTo(o1));
            for (RoleGrantModule r : grantList) {
                submenu = null;
                System.out.println("---"+r.getDeptno());
                policyList = policyBean.findByDepeno(r.getDeptno());
                if (policyList != null && !policyList.isEmpty()) {
                    list1.addAll(policyList);
                }
            }
            for (Policy s : list1) {
                menuitem = new DefaultMenuItem(s.getName());
                menuitem.setIcon("menu");
                menuitem.setOutcome("policysheet");
                menuitem.setParam("id", s.getId());
                kpimenu.addElement(menuitem);
            }
            model.addElement(kpimenu);
        }
    }

    @PreDestroy
    public void destory() {
        if (indicatorChartList != null) {
            indicatorChartList.clear();
            indicatorChartList = null;
        }
        if (indicatorDepartmentList != null) {
            indicatorDepartmentList.clear();
            indicatorDepartmentList = null;
        }
        if (scorecardList != null) {
            scorecardList.clear();
            scorecardList = null;
        }
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
     * @return the model
     */
    public MenuModel getModel() {
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(MenuModel model) {
        this.model = model;
    }

}
