/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.RoleBean;
import cn.hanbell.kpi.ejb.RoleDetailBean;
import cn.hanbell.kpi.ejb.RoleGrantModuleBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardDetailBean;
import cn.hanbell.kpi.ejb.tms.ProjectBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Role;
import cn.hanbell.kpi.entity.RoleDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.entity.tms.Project;
import cn.hanbell.kpi.lazy.ScorecardModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import com.lightshell.comm.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "scorecardSetManagedBean")
@javax.faces.bean.SessionScoped
public class ScorecardSetManagedBean extends SuperMultiBean<Scorecard, ScorecardDetail> {

    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private ScorecardDetailBean scorecardDetailBean;

    @EJB
    private RoleBean systemRoleBean;

    @EJB
    private RoleDetailBean systemRoleDetailBean;
    @EJB
    private RoleGrantModuleBean roleGrantModuleBean;

    @EJB
    private ProjectBean projectBean;

    protected Calendar c;
    private boolean freezed;
    private boolean isSort;

    protected String queryDeptno;
    protected String queryDeptname;
    protected String queryUserid;
    protected String queryUsername;
    protected int queryYear;

    protected boolean firstDetail;
    protected boolean lastDetail;

    protected List<String> paramDeptno = null;
    private List<String> deptList = null;
    private List<ScorecardDetail> notAchievedList;
    private String[] selectedQuarter;

    protected final Logger log4j = LogManager.getLogger("hanbell-KPI");

    public ScorecardSetManagedBean() {
        super(Scorecard.class, ScorecardDetail.class);
        c = Calendar.getInstance();
        openParams = new HashMap();
        openOptions = new HashMap();
    }

    /**
     * @param userid
     * @return
     * @description 只能查看自己部门的考核内容
     */
    public List<String> findDeptListByUserId(String userid) {
        List<RoleDetail> roleDetails = systemRoleDetailBean.findByUserId(userid);
        List<Role> roles = new ArrayList<>();
        if (!roleDetails.isEmpty()) {
            roleDetails.stream().map((rd) -> systemRoleBean.findById(rd.getPid())).filter((role) -> (role != null))
                    .forEachOrdered((role) -> {
                        roles.add(role);
                    });
        }
        List<RoleGrantModule> roleGrantModules = new ArrayList<>();
        roles.stream().map((role) -> roleGrantModuleBean.findByRoleId(role.getId()))
                .filter((roleGrantModuleList) -> (!roleGrantModuleList.isEmpty()))
                .forEachOrdered((roleGrantModuleList) -> {
                    roleGrantModules.addAll(roleGrantModuleList);
                });
        List<String> depts = new ArrayList<>();
        // 总经理室目前没有考核内容，需显示模板
        depts.add("10000");
        if (!roleGrantModules.isEmpty()) {
            roleGrantModules.stream().forEach((e) -> {
                depts.add(e.getDeptno());
            });
            return depts;
        }
        return depts;
    }

    @Override
    public void create() {
        super.create();
        this.newEntity.setCompany(userManagedBean.getCompany());
        this.newEntity.setSeq(queryYear);
        this.newEntity.setApi("scorecard");
    }

    public void calcScore() {
        if (currentEntity != null && currentEntity.getFreezeDate() != null
                && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate())) {
            showErrorMsg("Error", "资料已冻结,不可更新");
            return;
        }
        String col;
        BigDecimal value;
        col = scorecardBean.getColumn("sq", userManagedBean.getQ());
        List<ScorecardDetail> data = scorecardDetailBean.findByPId(currentEntity.getId());
        try {
            value = scorecardBean.getDetailScores(data, col);
            switch (userManagedBean.getQ()) {
                case 1:
                    currentEntity.setSq1(value);
                    break;
                case 2:
                    currentEntity.setSq2(value);
                    value = scorecardBean.getDetailScores(data, "sh1");
                    currentEntity.setSh1(value);
                    break;
                case 3:
                    currentEntity.setSq3(value);
                    break;
                case 4:
                    currentEntity.setSq4(value);
                    value = scorecardBean.getDetailScores(data, "sfy");
                    currentEntity.setSfy(value);
                    break;
            }
            scorecardBean.update(currentEntity);
            showInfoMsg("Info", "更新成功");
        } catch (Exception ex) {
            showErrorMsg("Error", ex.getMessage());
        }
    }

    public void calcItemScore() {
        if (currentDetail != null) {
            // 如果考核指标有PLM代号的就用PLM代过来的值计算
            if (currentDetail.getProjectSeq() != null && !currentDetail.getType().equals("N")) {
                updateScoreByPLMProject();
            }
            if (!currentDetail.getType().equals("N")) {
                showWarnMsg("Warn", "数值型才能更新");
                return;
            }
            if (currentDetail.getFreezeDate() != null
                    && currentDetail.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            String col = scorecardBean.getColumn("q", userManagedBean.getQ());
            try {
                if (currentDetail.getScoreJexl() != null && !"".equals(currentDetail.getScoreJexl())) {
                    // 计算达成
                    scorecardBean.setPerf(currentDetail, col);
                    // 计算得分
                    scorecardBean.setDetailScore(currentDetail, col);
                    // 上半年
                    if (userManagedBean.getQ() == 2) {
                        col = scorecardBean.getColumn("h", 1);
                        scorecardBean.setDetailScore(currentDetail, col);
                    } else if (userManagedBean.getQ() == 4) {
                        // 全年
                        scorecardBean.setDetailScore(currentDetail, "fy");
                    }
                }
                switch (userManagedBean.getQ()) {
                    case 1:
                        if (currentDetail.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                        }
                        break;
                    case 2:
                        if (currentDetail.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                        }
                        if (currentDetail.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                        }
                        break;
                    case 3:
                        if (currentDetail.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                        }
                        break;
                    case 4:
                        if (currentDetail.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                        }
                        if (currentDetail.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                        }
                        if (currentDetail.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                        }
                        break;
                }
                showInfoMsg("Info", "更新部门分数成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    /**
     * @desc 截取字符的数字计算得分、达成率
     * @param target
     * @param acutal
     * @return value
     */
    public BigDecimal calculateScore(String target, String acutal) {
        BigDecimal value = BigDecimal.ZERO;
        String str1, str2;
        // 先判断有值
        if ((!"".equals(target) || target != null) && (!"".equals(acutal) || acutal != null)) {
            str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
            str2 = acutal.substring(acutal.indexOf("#") + 1, acutal.indexOf("%"));
            //判断截取出来的数据是否为数字
            if (str1.matches("[0-9]*") && str2.matches("[0-9]*")) {
                Double t = Double.valueOf(str1);
                Double a = Double.valueOf(str2);
                // 分母不为零
                if (t > 0.00001) {
                    // 达成率、得分
                    value = BigDecimal.valueOf(a / t * 100);
                }
            } else {
                showErrorMsg("Error", "基准目标值格式不正确！！");
                return BigDecimal.ZERO;
            }
        }
        return value;
    }

    public String copyEntity(String path) {
        if (this.currentEntity != null && this.currentPrgGrant != null) {
            try {
                Scorecard entity = (Scorecard) BeanUtils.cloneBean(currentEntity);
                entity.setId(null);
                entity.setFreezeDate(null);
                entity.setSeq(entity.getSeq() + 1);
                entity.setCreator(this.userManagedBean.getCurrentUser().getUserid() + "-" + this.userManagedBean.getCurrentUser().getUsername());
                entity.setCredate(getDate());
                entity.setStatus("N");
                setNewEntity(entity);

                if (detailList != null && !detailList.isEmpty()) {
                    deletedDetailList.clear();
                    editedDetailList.clear();
                    addedDetailList.clear();
                    for (ScorecardDetail sd : detailList) {
                        ScorecardDetail detail = (ScorecardDetail) BeanUtils.cloneBean(sd);
                        detail.setId(null);
                        detail.setPid(0);
                        addedDetailList.add(detail);
                    }
                }
                persist();
                setCurrentEntity(entity);
                return path;
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
        return "";
    }

    @Override
    protected boolean doBeforeVerify() throws Exception {
        if (super.doBeforeVerify()) {
            // super.doBeforeVerify()会重设detailList
            if (!currentEntity.getTemplate()) {
                // 不是模板需要检查权重是否100
                BigDecimal weight = BigDecimal.ZERO;
                for (ScorecardDetail d : detailList) {
                    weight = weight.add(d.getWeight());
                }
                if (weight.compareTo(BigDecimal.valueOf(100)) != 0) {
                    showErrorMsg("Error", "权重合计需要等于100");
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void doConfirmDetail() {
        if (currentDetail.getFreezeDate() != null
                && currentDetail.getFreezeDate().after(userManagedBean.getBaseDate())) {
            showErrorMsg("Error", "资料已经冻结,不可更新");
             this.isSort=false;
            return;
        }
        for (ScorecardDetail detail : detailList) {
            if (!Objects.equals(currentDetail.getId(), detail.getId()) && currentDetail.getSeq() == detail.getSeq()) {
                showErrorMsg("Error", "明细序号重复,不可更新");
                 this.isSort=false;
                return;
            }
        }
        if(this.isSort){
            this.editedDetailList.clear();
            this.editedDetailList.addAll(detailList);
            super.doConfirmDetail();
            this.isSort=false;
            return ;
        }
        if (currentDetail.getGeneralScore() != null) {
            try {
                switch (userManagedBean.getQ()) {
                    case 1:
                        if (currentDetail.getGeneralScore().getSq1().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq1(currentDetail.getGeneralScore().getSq1());
                        }
                        break;
                    case 2:
                        if (currentDetail.getGeneralScore().getSq2().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq2(currentDetail.getGeneralScore().getSq2());
                        }
                        if (currentDetail.getGeneralScore().getSh1().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSh1(currentDetail.getGeneralScore().getSh1());
                        }
                        break;
                    case 3:
                        if (currentDetail.getGeneralScore().getSq3().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq3(currentDetail.getGeneralScore().getSq3());
                        }
                        break;
                    case 4:
                        if (currentDetail.getGeneralScore().getSq4().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSq4(currentDetail.getGeneralScore().getSq4());
                        }
                        if (currentDetail.getGeneralScore().getSh2().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSh2(currentDetail.getGeneralScore().getSh2());
                        }
                        if (currentDetail.getGeneralScore().getSfy().compareTo(BigDecimal.ZERO) != 0) {
                            currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                        } else if (currentDetail.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                            currentDetail.setSfy(currentDetail.getGeneralScore().getSfy());
                        }
                        break;
                }
            } catch (Exception ex) {
                showErrorMsg("Error", "更新异常");
            }
        }
        super.doConfirmDetail();
    }

    // 关联PLM的更新
    public void updateScoreByPLMProject() {
        try {
            String target, actual, projectSeq;
            BigDecimal value;
            String col = scorecardBean.getColumn("q", userManagedBean.getQ());
            // 找到PLM的数据
            projectSeq = projectBean.findByProjectSeq(currentDetail.getProjectSeq());
            if (projectSeq == null || "".equals(projectSeq)) {
                showErrorMsg("Error", "请确认PLM是否有进度");
                return;
            }
            // 选择季度更新
            switch (col) {
                case "q1":
                    currentDetail.setAq1("#" + projectSeq + "%#" + ";" + currentDetail.getAq1());
                    target = currentDetail.getTq1();
                    actual = currentDetail.getAq1();
                    value = calculateScore(target, actual);
                    currentDetail.setPq1(value);
                    currentDetail.getDeptScore().setSq1(value);
                    currentDetail.getGeneralScore().setSq1(value);
                    break;
                case "q2":
                    currentDetail.setAq2("#" + projectSeq + "%#" + ";" + currentDetail.getAq2());
                    //Q2
                    target = currentDetail.getTq2();
                    actual = currentDetail.getAq2();
                    value = calculateScore(target, actual);
                    currentDetail.setPq2(value);
                    currentDetail.getDeptScore().setSq2(value);
                    currentDetail.getGeneralScore().setSq2(value);
                    //上半年
                    currentDetail.setAh1("#" + projectSeq + "%#" + ";" + currentDetail.getAh1());
                    target = currentDetail.getTh1();
                    actual = currentDetail.getAh1();
                    value = calculateScore(target, actual);
                    currentDetail.setPh1(value);
                    currentDetail.getDeptScore().setSh1(value);
                    currentDetail.getGeneralScore().setSh1(value);
                    break;
                case "q3":
                    currentDetail.setAq3("#" + projectSeq + "%#" + ";" + currentDetail.getAq3());
                    target = currentDetail.getTq3();
                    actual = currentDetail.getAq3();
                    value = calculateScore(target, actual);
                    currentDetail.setPq3(value);
                    currentDetail.getDeptScore().setSq3(value);
                    currentDetail.getGeneralScore().setSq3(value);
                    break;
                case "q4":
                    //Q4
                    currentDetail.setAq4("#" + projectSeq + "%#" + ";" + currentDetail.getAq4());
                    target = currentDetail.getTq4();
                    actual = currentDetail.getAq4();
                    value = calculateScore(target, actual);
                    currentDetail.setPq4(value);
                    currentDetail.getDeptScore().setSq4(value);
                    currentDetail.getGeneralScore().setSq4(value);
                    //全年
                    currentDetail.setAfy("#" + projectSeq + "%#" + ";" + currentDetail.getAfy());
                    target = currentDetail.getTfy();
                    actual = currentDetail.getAfy();
                    value = calculateScore(target, actual);
                    currentDetail.setPfy(value);
                    currentDetail.getDeptScore().setSfy(value);
                    currentDetail.getGeneralScore().setSfy(value);
                    break;
            }
            scorecardDetailBean.update(currentDetail);
            showErrorMsg("Info", "更新成功！");
        } catch (NumberFormatException ex) {
            log4j.warn("updateScoreByPLMProject()方法异常-" + ex.toString());
        }

    }

    @Override
    public void handleDialogReturnWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Department e = (Department) event.getObject();
            newEntity.setDeptno(e.getDeptno());
            newEntity.setDeptname(e.getDept());
        }
    }

    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Department e = (Department) event.getObject();
            currentEntity.setDeptno(e.getDeptno());
            currentEntity.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnTemplateWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            Scorecard sc = (Scorecard) o;
            currentEntity.setTemplateId(sc.getName());
            currentEntity.setApi(sc.getApi());
            List<ScorecardDetail> templateDetail = scorecardDetailBean.findByPId(sc.getId());
            if (templateDetail != null && !templateDetail.isEmpty()) {
                for (ScorecardDetail d : templateDetail) {
                    this.createDetail();
                    this.currentDetail.setPid(currentEntity.getId());
                    this.currentDetail.setContent(d.getContent());
                    this.currentDetail.setWeight(d.getWeight());
                    this.currentDetail.setType(d.getType());
                    this.currentDetail.setKind(d.getKind());
                    this.currentDetail.setValueMode(d.getValueMode());
                    this.currentDetail.setUnit(d.getUnit());
                    this.currentDetail.setSymbol(d.getSymbol());
                    this.currentDetail.setMinNum(d.getMinNum());
                    this.currentDetail.setMaxNum(d.getMaxNum());
                    this.currentDetail.setPerformanceJexl(d.getPerformanceJexl());
                    this.currentDetail.setPerformanceInterface(d.getPerformanceInterface());
                    this.currentDetail.setScoreJexl(d.getScoreJexl());
                    this.currentDetail.setScoreInterface(d.getScoreInterface());
                    this.currentDetail.setBfy(d.getBfy());
                    this.currentDetail.setBh2(d.getBh2());
                    this.currentDetail.setBh1(d.getBh1());
                    this.currentDetail.setBq1(d.getBq1());
                    this.currentDetail.setBq2(d.getBq2());
                    this.currentDetail.setBq3(d.getBq3());
                    this.currentDetail.setBq4(d.getBq4());
                    this.currentDetail.setTfy(d.getTfy());
                    this.currentDetail.setTh2(d.getTh2());
                    this.currentDetail.setTh1(d.getTh1());
                    this.currentDetail.setTq1(d.getTq1());
                    this.currentDetail.setTq2(d.getTq2());
                    this.currentDetail.setTq3(d.getTq3());
                    this.currentDetail.setTq4(d.getTq4());
                    this.doConfirmDetail();
                }
            }
        }
    }

    public void handleDialogReturnUserWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            newEntity.setUserid(user.getUserid());
            newEntity.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentEntity.setUserid(user.getUserid());
            currentEntity.setUsername(user.getUsername());
        }
    }

    @Override
    public void handleDialogReturnWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Indicator e = (Indicator) event.getObject();
            currentDetail.setIndicator(e.getFormid());
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDeptname());
            currentDetail.setUserid(e.getUserid());
            currentDetail.setUsername(e.getUsername());
            // 从指标代入基准
            currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().toString());
            currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().toString());
            currentDetail.setBh1(e.getBenchmarkIndicator().getNh1().toString());
            currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().toString());
            currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().toString());
            currentDetail.setBh2(e.getBenchmarkIndicator().getNh2().toString());
            currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().toString());
            // 从指标代入目标
            currentDetail.setTq1(e.getTargetIndicator().getNq1().toString());
            currentDetail.setTq2(e.getTargetIndicator().getNq2().toString());
            currentDetail.setTh1(e.getTargetIndicator().getNh1().toString());
            currentDetail.setTq3(e.getTargetIndicator().getNq3().toString());
            currentDetail.setTq4(e.getTargetIndicator().getNq4().toString());
            currentDetail.setTh2(e.getTargetIndicator().getNh2().toString());
            currentDetail.setTfy(e.getTargetIndicator().getNfy().toString());
        }
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Indicator e = (Indicator) event.getObject();
            currentDetail.setIndicator(e.getFormid());
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDeptname());
            currentDetail.setUserid(e.getUserid());
            currentDetail.setUsername(e.getUsername());
            // 从指标代入基准
            currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().toString());
            currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().toString());
            currentDetail.setBh1(e.getBenchmarkIndicator().getNh1().toString());
            currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().toString());
            currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().toString());
            currentDetail.setBh2(e.getBenchmarkIndicator().getNh2().toString());
            currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().toString());
            // 从指标代入目标
            currentDetail.setTq1(e.getTargetIndicator().getNq1().toString());
            currentDetail.setTq2(e.getTargetIndicator().getNq2().toString());
            currentDetail.setTh1(e.getTargetIndicator().getNh1().toString());
            currentDetail.setTq3(e.getTargetIndicator().getNq3().toString());
            currentDetail.setTq4(e.getTargetIndicator().getNq4().toString());
            currentDetail.setTh2(e.getTargetIndicator().getNh2().toString());
            currentDetail.setTfy(e.getTargetIndicator().getNfy().toString());
        }
    }

    public void handleDialogReturnDeptWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Department e = (Department) event.getObject();
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnDeptWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            Department e = (Department) event.getObject();
            currentDetail.setDeptno(e.getDeptno());
            currentDetail.setDeptname(e.getDept());
        }
    }

    public void handleDialogReturnUserWhenDetailNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnUserWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
        }
    }

    public void handleDialogReturnDeptWhenProjectNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            Project p = (Project) o;
            currentDetail.setProjectSeq(String.valueOf(p.getProjectSeq()));
            currentDetail.setProjectName(p.getProjectName());
        }
    }

    public void handleDialogReturnDeptWhenProjectEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            Project p = (Project) o;
            currentDetail.setProjectSeq(String.valueOf(p.getProjectSeq()));
            currentDetail.setProjectName(p.getProjectName());
        }
    }

    @Override
    public void init() {
        superEJB = scorecardBean;
        detailEJB = scorecardDetailBean;
        model = new ScorecardModel(scorecardBean, this.userManagedBean);
        deptList = findDeptListByUserId(userManagedBean.getUserid());
        model.getFilterFields().put("deptno IN ", deptList);
        model.getSortFields().put("seq", "DESC");
        model.getSortFields().put("sortid", "ASC");
        model.getSortFields().put("deptno", "ASC");
        c.setTime(userManagedBean.getBaseDate());
        queryYear = c.get(Calendar.YEAR);
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            model.getFilterFields().put("deptno IN ", deptList);
            if (queryYear != 0) {
                model.getFilterFields().put("seq", queryYear);
            }
            if (queryName != null && !"".equals(queryName)) {
                model.getFilterFields().put("name", queryName);
            }
            if (queryDeptname != null && !"".equals(queryDeptname)) {
                model.getFilterFields().put("deptname", queryDeptname);
            }
            if (queryState != null && !"ALL".equals(queryState)) {
                model.getFilterFields().put("status", queryState);
            }
        }
    }

    public void moveDown() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i < detailList.size() - 1) {
                ScorecardDetail scd = detailList.get(i + 1);
                m = scd.getSeq();
                scd.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                scd.setSeq(n);
                currentDetail = scd;
                this.doConfirmDetail();
                // 重新排序
                detailList.sort((ScorecardDetail o1, ScorecardDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveDown");
            } else {
                showWarnMsg("Warn", "已是最后");
            }
        }
    }

    public void moveUp() {
        if (currentDetail != null) {
            int i, n, m;
            n = currentDetail.getSeq();
            i = detailList.indexOf(currentDetail);
            if (i > 0) {
                ScorecardDetail scd = detailList.get(i - 1);
                m = scd.getSeq();
                scd.setSeq(-1);
                currentDetail.setSeq(m);
                this.doConfirmDetail();
                scd.setSeq(n);
                currentDetail = scd;
                this.doConfirmDetail();
                // 重新排序
                detailList.sort((ScorecardDetail o1, ScorecardDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
                showInfoMsg("Info", "MoveUp");
            } else {
                showWarnMsg("Warn", "已是最前");
            }
        }
    }

    @Override
    public void openDialog(String view) {
        switch (view) {
            case "indicatorSelect":
                openParams.clear();
                if (paramDeptno == null) {
                    paramDeptno = new ArrayList<>();
                } else {
                    paramDeptno.clear();
                }
                paramDeptno.add(currentEntity.getDeptno());
                openParams.put("deptno", paramDeptno);
                openOptions.clear();
                openOptions.put("modal", true);
                openOptions.put("contentWidth", "900");
                super.openDialog(view, openOptions, openParams);
                break;
            default:
                super.openDialog(view);
        }
    }

    public void updateScorecardExplanationScore() {
        if (currentDetail != null) {
            if (currentDetail.getFreezeDate() != null
                    && currentDetail.getFreezeDate().after(userManagedBean.getBaseDate())) {
                showErrorMsg("Error", "资料已冻结,不可更新");
                return;
            }
            ScorecardDetail sd = scorecardDetailBean.findById(currentDetail.getId());
            try {
                if (sd != null) {
                    switch (userManagedBean.getQ()) {
                        case 1:
                            sd.getCauseScore1().setQ1(currentDetail.getCauseScore2().getQ1());
                            sd.getCauseScore2().setQ1(currentDetail.getCauseScore2().getQ1());
                            sd.getSummaryScore1().setQ1(currentDetail.getSummaryScore1().getQ1());
                            sd.getSummaryScore2().setQ1(currentDetail.getSummaryScore2().getQ1());
                            break;
                        case 2:
                            sd.getCauseScore1().setQ2(currentDetail.getSummaryScore1().getQ1());
                            sd.getCauseScore2().setQ2(currentDetail.getCauseScore2().getQ2());
                            sd.getSummaryScore1().setQ2(currentDetail.getSummaryScore1().getQ2());
                            sd.getSummaryScore2().setQ2(currentDetail.getSummaryScore2().getQ2());
                            break;
                        case 3:
                            sd.getCauseScore1().setQ3(currentDetail.getCauseScore1().getQ3());
                            sd.getCauseScore2().setQ3(currentDetail.getCauseScore2().getQ3());
                            sd.getSummaryScore1().setQ3(currentDetail.getSummaryScore1().getQ3());
                            sd.getSummaryScore2().setQ3(currentDetail.getSummaryScore2().getQ3());
                            break;
                        case 4:
                            sd.getCauseScore1().setQ4(currentDetail.getCauseScore1().getQ4());
                            sd.getCauseScore2().setQ4(currentDetail.getCauseScore2().getQ4());
                            sd.getSummaryScore1().setQ4(currentDetail.getSummaryScore1().getQ4());
                            sd.getSummaryScore2().setQ4(currentDetail.getSummaryScore2().getQ4());
                            break;
                    }
                    scorecardDetailBean.update(sd);
                }
                showInfoMsg("Info", "更新成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }

    }

    @Override
    public void print() throws Exception {
        if (currentPrgGrant != null && currentPrgGrant.getDoprt()) {
            HashMap<String, Object> reportParams = new HashMap<>();
            reportParams.put("company", userManagedBean.getCurrentCompany().getName());
            reportParams.put("companyFullName", userManagedBean.getCurrentCompany().getFullname());
            reportParams.put("JNDIName", this.currentPrgGrant.getSysprg().getRptjndi());
            reportParams.put("seq", currentEntity.getSeq());
            reportParams.put("deptname", currentEntity.getDeptname());
            reportParams.put("id", currentEntity.getId());
            reportParams.put("season", userManagedBean.getQ());
            if (!this.model.getFilterFields().isEmpty()) {
                reportParams.put("filterFields", BaseLib.convertMapToStringWithClass(this.model.getFilterFields()));
            } else {
                reportParams.put("filterFields", "");
            }
            if (!this.model.getSortFields().isEmpty()) {
                reportParams.put("sortFields", BaseLib.convertMapToString(this.model.getSortFields()));
            } else {
                reportParams.put("sortFields", "");
            }
            this.fileName = this.currentPrgGrant.getSysprg().getApi()
                    + BaseLib.formatDate("yyyyMMddHHss", this.getDate()) + "." + "xls";
            String reportName = reportPath + this.currentPrgGrant.getSysprg().getRptdesign();
            String outputName = reportOutputPath + this.fileName;
            this.reportViewPath = reportViewContext + this.fileName;
            try {
                if (this.currentPrgGrant.getSysprg().getRptclazz() != null) {
                    reportClassLoader = Class.forName(this.currentPrgGrant.getSysprg().getRptclazz()).getClassLoader();
                }
                // 初始配置
                this.reportInitAndConfig();
                // 生成报表
                this.reportRunAndOutput(reportName, reportParams, outputName, "xls", null);
                // 预览报表
                this.preview();
            } catch (Exception ex) {
                throw ex;
            }
        }
    }

    @Override
    public void setCurrentEntity(Scorecard currentEntity) {
        super.setCurrentEntity(currentEntity);
        if (currentEntity != null) {
            this.freezed = currentEntity.getFreezeDate() != null
                    && currentEntity.getFreezeDate().after(userManagedBean.getBaseDate());
        }
    }

    @Override
    public void setCurrentDetail(ScorecardDetail currentDetail) {
        super.setCurrentDetail(currentDetail);
        if (currentDetail != null) {
            int i = detailList.indexOf(currentDetail);
            if (i == 0) {
                this.firstDetail = true;
                this.lastDetail = false;
            } else if (i == detailList.size() - 1) {
                this.firstDetail = false;
                this.lastDetail = true;
            } else {
                this.firstDetail = false;
                this.lastDetail = false;
            }
        }
    }

    // 月结功能
    public void setFreezeDate() {
        if (currentEntity != null) {
            currentEntity.setFreezeDate(userManagedBean.getBaseDate());
            scorecardBean.update(currentEntity);
            if (detailList != null) {
                for (ScorecardDetail s : detailList) {
                    s.setFreezeDate(userManagedBean.getBaseDate());
                    scorecardDetailBean.update(s);
                }
                showInfoMsg("Info", "冻结时间成功");
            }
        } else {
            showErrorMsg("Error", "请选中一个指标");
        }
    }

    //修改明细排序
    public void editSort() {
        this.isSort=true;
    }

    @Override
    public String edit(String path) {
        //编辑页面时移除edit集合中的内容。关闭排序功能。
        if("scorecardsetEdit".equals(path)){
             this.isSort=false;
             this.editedDetailList.clear();
        }
        return super.edit(path);
    }

    /**
     * @description 查询不达标项
     */
    public void queryNotAchieved() {
        notAchievedList = new ArrayList<>();

    }

    /**
     * @description 导出不达标项
     */
    public void printNotAchieved() {
        notAchievedList = null;
        if (notAchievedList == null || notAchievedList.isEmpty()) {
            return;
        }
        // 设置报表名称
        fileName = "Q4" + "不达标项考核明细" + ".xls";
        String fileFullName = reportOutputPath + fileName;
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 获得表格样式
        Map<String, CellStyle> style = createStyles(workbook);
        // 生成一个表格
        HSSFSheet sheet1 = workbook.createSheet("不达标项考核明细");
        // 设置表格宽度
        int[] wt = {20, 5, 10, 10, 20, 5, 5, 5, 5, 5, 5, 20, 20, 20, 20, 20, 20};
        for (int i = 0; i < wt.length; i++) {
            sheet1.setColumnWidth(i, wt[i] * 256);
        }
        // 创建标题行
        Row row;
        // Sheet1
        String[] title = {"考核表", "季度", "部门代号", "部门简称", "考核内容", "Q1达成率", "Q2达成率", "上半年达成率", "Q3达成率", "Q4达成率", "全年达成率",
            "Q1阶段说明", "Q2阶段说明", "上半年阶段说明", "Q3阶段说明", "Q4阶段说明", "全年阶段说明"};
        row = sheet1.createRow(0);
        row.setHeight((short) 600);
        for (int i = 0; i < title.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellStyle(style.get("head"));
            cell.setCellValue(title[i]);
        }
        int j = 1;
        // 添加数据内容
        for (ScorecardDetail ip : notAchievedList) {
            row = sheet1.createRow(j);
            j++;
            row.setHeight((short) 400);

        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(fileFullName);
            workbook.write(os);
            this.reportViewPath = reportViewContext + fileName;
            this.preview();
        } catch (Exception ex) {
            log4j.error(ex);
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                log4j.error(ex.getMessage());
            }
        }

    }

    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new LinkedHashMap<>();

        // 文件头样式
        CellStyle headStyle = wb.createCellStyle();
        headStyle.setWrapText(true);// 设置自动换行
        headStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        headStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());// 单元格背景颜色
        headStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headStyle.setBorderRight(CellStyle.BORDER_THIN);
        headStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderLeft(CellStyle.BORDER_THIN);
        headStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderTop(CellStyle.BORDER_THIN);
        headStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        headStyle.setBorderBottom(CellStyle.BORDER_THIN);
        headStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        Font headFont = wb.createFont();
        headFont.setFontHeightInPoints((short) 12);
        headFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headStyle.setFont(headFont);
        styles.put("head", headStyle);

        // 正文样式
        CellStyle cellStyle = wb.createCellStyle();
        Font cellFont = wb.createFont();
        cellFont.setFontHeightInPoints((short) 10);
        cellStyle.setFont(cellFont);
        cellStyle.setWrapText(true);// 设置自动换行
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());// 单元格背景颜色
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderRight(CellStyle.BORDER_THIN);
        cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderTop(CellStyle.BORDER_THIN);
        cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
        cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", cellStyle);

        return styles;
    }

    /**
     * @return the freezed
     */
    public boolean isFreezed() {
        return freezed;
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

    /**
     * @return the queryUserid
     */
    public String getQueryUserid() {
        return queryUserid;
    }

    /**
     * @param queryUserid the queryUserid to set
     */
    public void setQueryUserid(String queryUserid) {
        this.queryUserid = queryUserid;
    }

    /**
     * @return the queryUsername
     */
    public String getQueryUsername() {
        return queryUsername;
    }

    /**
     * @param queryUsername the queryUsername to set
     */
    public void setQueryUsername(String queryUsername) {
        this.queryUsername = queryUsername;
    }

    /**
     * @return the queryYear
     */
    public int getQueryYear() {
        return queryYear;
    }

    /**
     * @param queryYear the queryYear to set
     */
    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

    /**
     * @return the currentDetailIndex
     */
    public boolean isFirstDetail() {
        return firstDetail;
    }

    /**
     * @return the lastDetail
     */
    public boolean isLastDetail() {
        return lastDetail;
    }

    public boolean isIsSort() {
        return isSort;
    }

    public void setIsSort(boolean isSort) {
        this.isSort = isSort;
    }

}
