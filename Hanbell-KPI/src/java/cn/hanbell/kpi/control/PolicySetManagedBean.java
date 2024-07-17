/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.PolicyBean;
import cn.hanbell.kpi.ejb.PolicyDetailBean;
import cn.hanbell.kpi.ejb.RoleBean;
import cn.hanbell.kpi.ejb.RoleDetailBean;
import cn.hanbell.kpi.ejb.RoleGrantModuleBean;
import cn.hanbell.kpi.ejb.tms.ProjectBean;
import cn.hanbell.kpi.entity.ExchangeRate;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.PolicyDetail;
import cn.hanbell.kpi.entity.Role;
import cn.hanbell.kpi.entity.RoleDetail;
import cn.hanbell.kpi.entity.RoleGrantModule;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.entity.tms.Project;
import cn.hanbell.kpi.lazy.PolicyModel;
import cn.hanbell.kpi.lazy.ScorecardModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.jexl3.JexlException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "policySetManagedBean")
@javax.faces.bean.SessionScoped
public class PolicySetManagedBean extends SuperMultiBean<Policy, PolicyDetail> {

    @EJB
    private PolicyBean policyBean;
    @EJB
    private PolicyDetailBean policyDetailBean;
    protected Calendar c;
    private List<String> deptList = null;
    @EJB
    private RoleBean systemRoleBean;
    @EJB
    private RoleGrantModuleBean roleGrantModuleBean;

    @EJB
    private RoleDetailBean systemRoleDetailBean;
    @EJB
    private SystemUserBean systemUserBean;
    @EJB
    private ProjectBean projectBean;
    @EJB
    private IndicatorBean indicatorBean;
    private PolicyDetail beforeDetailEntity;
    //主页面查询参数
    protected int queryYear;
    protected String queryName;
    protected String queryDeptname;
    protected String queryState;
    private boolean isRateRequired;

    public PolicySetManagedBean() {
        super(Policy.class, PolicyDetail.class);
        c = Calendar.getInstance();
    }

    @Override
    public void create() {
        super.create();
        //this.newEntity.set(userManagedBean.getCompany());
        this.newEntity.setYear(queryYear);
        this.newEntity.setApi("policy");
    }

    @Override
    public void init() {
        superEJB = policyBean;
        detailEJB = policyDetailBean;
        model = new PolicyModel(policyBean, this.userManagedBean);
        deptList = findDeptListByUserId(userManagedBean.getUserid());
        model.getFilterFields().put("deptno IN ", deptList);
        model.getSortFields().put("year", "DESC");
        c.setTime(userManagedBean.getBaseDate());
        queryYear = c.get(Calendar.YEAR);
        super.init();
    }

    //数据按照初始化
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

    //文件导入
    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            DecimalFormat dmf = new DecimalFormat("#");
            List<ExchangeRate> addlist = new ArrayList<>();
            String a = "";
            String b = "";
//        super.handleFileUploadWhenNew(event);
            this.file = event.getFile();
            this.fileName = this.file.getFileName();
            super.upload();
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.setCharacterEncoding("UTF-8");
            if (this.fileName != null) {
                InputStream is = new FileInputStream(getAppResPath() + "/" + fileName);
                Workbook excel = WorkbookFactory.create(is);
                Sheet sheet = excel.getSheetAt(0);
                int rowNumbers = sheet.getLastRowNum();
                //第一，二，三行分别为标题，愿景和方针。不需要循环写入.从模板第6行开始写入
                Row row;
                newEntity.setName(sheet.getRow(0).getCell(0).getStringCellValue());
                newEntity.setVision(sheet.getRow(1).getCell(0).getStringCellValue());
                newEntity.setPolicydescript(sheet.getRow(2).getCell(0).getStringCellValue());
                newEntity.setCompany(userManagedBean.getCompany());
                SystemUser user = systemUserBean.findByUserId(userManagedBean.getUserid());
                List<String> faects = new ArrayList<String>();
                addedDetailList.clear();
                int seq = 0;
                for (int i = 6; i <= rowNumbers; i++) {
                    row = sheet.getRow(i);
                    String perspective = row.getCell(0).getStringCellValue();
                    if (perspective.startsWith("C") && !faects.contains("C")) {
                        faects.add("C");
                        newEntity.setCp(perspective);
                        newEntity.setCo(row.getCell(1).getStringCellValue());
                        continue;
                    }
                    if (perspective.startsWith("Q") && !faects.contains("Q")) {
                        faects.add("Q");
                        newEntity.setQp(perspective);
                        newEntity.setQo(row.getCell(1).getStringCellValue());
                        continue;
                    }
                    if (perspective.startsWith("D") && !faects.contains("D")) {
                        faects.add("D");
                        newEntity.setDp(perspective);
                        newEntity.setDo1(row.getCell(1).getStringCellValue());
                        continue;
                    }
                    if (perspective.startsWith("P") && !faects.contains("P")) {
                        faects.add("P");
                        newEntity.setPp(perspective);
                        newEntity.setPo(row.getCell(1).getStringCellValue());
                        continue;
                    }
                    PolicyDetail p = this.getPolicyDetailByExcelRow(row);
                    p.setGenre(faects.get(faects.size() - 1));
                    seq++;
                    p.setSeq(seq);
                    addedDetailList.add(p);
                }
            }
            //将导入文件删除掉
            File file = new File(getAppResPath() + "/" + fileName);
            if (file.isFile()) {
                file.delete();
            }
            showInfoMsg("Info", "导入成功");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("Error", e.getMessage());
        }
    }

    //参考KPI绑定
    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        //只有数字类型才能绑定KPI
        if (event.getObject() != null && currentDetail != null) {
            if ("B".equals(this.currentDetail.getCalculationtype())) {
                Indicator e = (Indicator) event.getObject();
                currentDetail.setFromkpi(String.valueOf(e.getId()));
                currentDetail.setFromkpiname(e.getName());
                currentDetail.setBq1(e.getBenchmarkIndicator().getNq1().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setBq2(e.getBenchmarkIndicator().getNq2().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setBhy(e.getBenchmarkIndicator().getNh1().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setBq3(e.getBenchmarkIndicator().getNq3().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setBq4(e.getBenchmarkIndicator().getNq4().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setBfy(e.getBenchmarkIndicator().getNfy().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                // 从指标代入目标
                currentDetail.setTq1(e.getTargetIndicator().getNq1().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setTq2(e.getTargetIndicator().getNq2().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setThy(e.getTargetIndicator().getNh1().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setTq3(e.getTargetIndicator().getNq3().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setTq4(e.getTargetIndicator().getNq4().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                currentDetail.setTfy(e.getTargetIndicator().getNfy().divide(currentDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
            } else {
                this.showErrorMsg("Errpe", "数字类型才能绑定KPI");
            }
        }
    }

    //参考PLM绑定
    public void handleDialogReturnDeptWhenProjectNew(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            Project p = (Project) o;
            //PLM必须是文本类型
            if ("A".equals(this.currentDetail.getCalculationtype())) {
                currentDetail.setFromplm(String.valueOf(p.getProjectSeq()));
                currentDetail.setFromplmname(String.valueOf(p.getProjectName()));
            } else {
                this.showErrorMsg("Errpe", "文本类型才能绑定PLM");
            }
        }
    }

    @Override
    public void openDialog(String view) {
        if ("indicatorSelect".equals(view)) {
            if (!"B".equals(currentDetail.getCalculationtype())) {
                showErrorMsg("Error", "只有数字格式才能绑定KPI");
                return;
            }
            if (currentDetail.getIndicatorrate() == null) {
                showErrorMsg("Error", "请先设置参考KPI比率");
                return;
            }
        }
        super.openDialog(view);
    }

    @Override
    public void persist() {
        try {
            Policy policy = this.policyBean.findByCompanyNameAndYear(newEntity.getCompany(), newEntity.getName(), newEntity.getYear());
            if (policy != null) {
                if ("V".equals(policy.getStatus())) {
                    throw new Exception("此方针已被锁定，不可导入，请取消审核后导入。");
                }
                this.policyBean.delete(policy);
                this.setDeletedDetailList(this.policyDetailBean.findByPId(policy.getId()));//加入删除集合。新增前会自动删除明细
            }
            super.persist();
        } catch (Exception e) {
            showErrorMsg("Error", e.getMessage());
        }
    }

    public PolicyDetail getPolicyDetailByExcelRow(Row row) throws Exception {
        try {
            PolicyDetail d = new PolicyDetail();
            d.setPerspective(cellToVlaue(row.getCell(0)));
            d.setObjective(cellToVlaue(row.getCell(1)));
            d.setSeqname(cellToVlaue(row.getCell(2)));
            d.setName(cellToVlaue(row.getCell(3)));
            d.setCalculationtype(cellToVlaue(row.getCell(4)));
            d.setPerformancecalculation(cellToVlaue(row.getCell(5)));
            d.setUnit(cellToVlaue(row.getCell(6)));
            d.setType(cellToVlaue(row.getCell(7)));
            d.setIndicatorrate(BigDecimal.ONE);
            if ("".equals(d.getName())) {
                throw new Exception("第" + (row.getRowNum() + 1) + "行中指标内容为空，需移除该行内容");
            }
            if ("".equals(d.getCalculationtype()) || (!"A".equals(d.getCalculationtype()) && !"B".equals(d.getCalculationtype()))) {
                throw new Exception("第" + (row.getRowNum() + 1) + "行中类型不能为空，且必须是文本或数字");
            }
            if ("B".equals(d.getCalculationtype()) && !"A".equals(d.getPerformancecalculation()) && !"B".equals(d.getPerformancecalculation())) {
                throw new Exception("第" + (row.getRowNum() + 1) + "行中类型是数字时，必须填入计算方式。");
            }
            if ("A".equals(d.getCalculationtype()) && !"".equals(d.getPerformancecalculation())) {
                throw new Exception("第" + (row.getRowNum() + 1) + "行中类型是文字时，计算方式为空。");
            }
            switch (d.getCalculationtype()) {
                case "A"://文字
                    d.setBq1(cellToVlaue(row.getCell(8)));
                    d.setTq1(cellToVlaue(row.getCell(9)));
//                    d.setAq1(cellToVlaue(row.getCell(10)));
//                    d.setPq1(new BigDecimal(cellToVlaue(row.getCell(11))).multiply(BigDecimal.valueOf(100)));
                    d.setBq2(cellToVlaue(row.getCell(12)));
                    d.setTq2(cellToVlaue(row.getCell(13)));
                    d.setBhy(cellToVlaue(row.getCell(16)));
                    d.setThy(cellToVlaue(row.getCell(17)));
                    d.setBq3(cellToVlaue(row.getCell(20)));
                    d.setTq3(cellToVlaue(row.getCell(21)));
                    d.setBq4(cellToVlaue(row.getCell(24)));
                    d.setTq4(cellToVlaue(row.getCell(25)));
                    d.setBfy(cellToVlaue(row.getCell(28)));
                    d.setTfy(cellToVlaue(row.getCell(29)));
                    break;
                case "B"://数字
                    BigDecimal b;

                    b = new BigDecimal(cellToVlaue(row.getCell(8)) != "" ? cellToVlaue(row.getCell(8)) : "0");
                    d.setBq1(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(9)) != "" ? cellToVlaue(row.getCell(9)) : "0");
                    d.setTq1(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

//                    b = new BigDecimal(cellToVlaue(row.getCell(10)) != "" ? cellToVlaue(row.getCell(10)) : "0");
//                    d.setAq1(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
//
//                    d.setPq1(new BigDecimal(cellToVlaue(row.getCell(11))).multiply(BigDecimal.valueOf(100)));

                    b = new BigDecimal(cellToVlaue(row.getCell(12)) != "" ? cellToVlaue(row.getCell(12)) : "0");
                    d.setBq2(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(13)) != "" ? cellToVlaue(row.getCell(13)) : "0");
                    d.setTq2(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(16)) != "" ? cellToVlaue(row.getCell(16)) : "0");
                    d.setBhy(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(17)) != "" ? cellToVlaue(row.getCell(17)) : "0");
                    d.setThy(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(20)) != "" ? cellToVlaue(row.getCell(20)) : "0");
                    d.setBq3(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(21)) != "" ? cellToVlaue(row.getCell(21)) : "0");
                    d.setTq3(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(24)) != "" ? cellToVlaue(row.getCell(24)) : "0");
                    d.setBq4(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(25)) != "" ? cellToVlaue(row.getCell(25)) : "0");
                    d.setTq4(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(28)) != "" ? cellToVlaue(row.getCell(28)) : "0");
                    d.setBfy(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());

                    b = new BigDecimal(cellToVlaue(row.getCell(29)) != "" ? cellToVlaue(row.getCell(29)) : "0");
                    d.setTfy(b.setScale(3, BigDecimal.ROUND_HALF_UP).toString());
                    break;
            }

            return d;
        } catch (java.lang.IllegalStateException e) {
            e.printStackTrace();
            throw new IllegalStateException("第" + (row.getRowNum() + 1) + "类型与基准或目标的内容类型不一致。请调整！");
        } catch (NumberFormatException e) {
            e.printStackTrace();
            throw new NumberFormatException("第" + (row.getRowNum() + 1) + "内容转换失败，请检查。");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("第" + (row.getRowNum() + 1) + "发生错误。" + e.getMessage());
        }
    }

    public String cellToVlaue(Cell cell) throws Exception {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        switch (type) {
            case 0:
                double d = cell.getNumericCellValue();
                //整数去掉小数点
                if (d == (int) d) {
                    return String.valueOf((int) d);
                }
                return String.valueOf(cell.getNumericCellValue());
            case 1:
                return cell.getStringCellValue();
            case 2:
                throw new Exception("存在公式，请调整");
            case 3:
                return "";
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 5:
                return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

    //部门绑定
    @Override
    public void handleDialogReturnWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Department e = (Department) event.getObject();
            this.newEntity.setDeptno(e.getDeptno());
            this.newEntity.setDeptna(e.getDept());
            this.newEntity.setMenudeptno(e.getDeptno());
        }
    }

    //负责人员绑定
    public void handleDialogReturnUserWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            this.newEntity.setUserno(user.getUserid());
            this.newEntity.setUserna(user.getUsername());
        }
    }

    public int getQueryYear() {
        return queryYear;
    }

    public void setSeq() {
        if (this.currentDetail != null) {
            PolicyDetail p = this.currentDetail;
            detailList.sort((PolicyDetail o1, PolicyDetail o2) -> {
                if (o1.getSeq() > o2.getSeq()) {
                    return 1;
                } else {
                    return -1;
                }
            });
            for (int i = 0; i < detailList.size(); i++) {
                this.currentDetail = detailList.get(i);
                this.currentDetail.setSeq(i + 1);
                this.doConfirmDetail();
            }
            this.setCurrentDetail(detailList.get(detailList.indexOf(p)));
        }
    }

    @Override
    public void doConfirmDetail() {
        if (this.newDetail != null && this.newDetail.equals(this.currentDetail)) {
            if (!this.addedDetailList.contains(this.newDetail)) {
                this.addedDetailList.add(this.newDetail);
                this.setNewDetail((PolicyDetail) null);
                this.setCurrentDetail((PolicyDetail) null);
            }
        } else if (this.currentDetail != null && !this.addedDetailList.contains(this.currentDetail)) {
            if (this.editedDetailList.contains(this.currentDetail)) {
                this.editedDetailList.set(this.editedDetailList.indexOf(this.currentDetail), this.currentDetail);
            } else {
                this.editedDetailList.add(this.currentDetail);
            }
            this.setCurrentDetail((PolicyDetail) null);
        }
    }

    public boolean verifyCalculationtype() {
        if ("B".equals(this.currentDetail.getCalculationtype())) {
            try {
                if (this.currentDetail.getAq1() == null || "".equals(this.currentDetail.getAq1())) {
                    this.currentDetail.setAq1("0");
                }
                Double.parseDouble(this.currentDetail.getAq1());

                if (this.currentDetail.getAq2() == null || "".equals(this.currentDetail.getAq2())) {
                    this.currentDetail.setAq2("0");
                }
                Double.parseDouble(this.currentDetail.getAq2());

                if (this.currentDetail.getAq3() == null || "".equals(this.currentDetail.getAq3())) {
                    this.currentDetail.setAq3("0");
                }
                Double.parseDouble(this.currentDetail.getAq3());

                if (this.currentDetail.getAq4() == null || "".equals(this.currentDetail.getAq4())) {
                    this.currentDetail.setAq4("0");
                }
                Double.parseDouble(this.currentDetail.getAq4());

                if (this.currentDetail.getAhy() == null || "".equals(this.currentDetail.getAq1())) {
                    this.currentDetail.setAhy("0");
                }
                Double.parseDouble(this.currentDetail.getAhy());

                if (this.currentDetail.getAfy() == null || "".equals(this.currentDetail.getAfy())) {
                    this.currentDetail.setAfy("0");
                }
                Double.parseDouble(this.currentDetail.getAfy());

                if (this.currentDetail.getBq1() == null || "".equals(this.currentDetail.getBq1())) {
                    this.currentDetail.setBq1("0");
                }
                Double.parseDouble(this.currentDetail.getBq1());

                if (this.currentDetail.getBq2() == null || "".equals(this.currentDetail.getBq2())) {
                    this.currentDetail.setBq2("0");
                }
                Double.parseDouble(this.currentDetail.getBq2());

                if (this.currentDetail.getBq3() == null || "".equals(this.currentDetail.getBq3())) {
                    this.currentDetail.setBq3("0");
                }
                Double.parseDouble(this.currentDetail.getBq3());

                if (this.currentDetail.getBq4() == null || "".equals(this.currentDetail.getBq4())) {
                    this.currentDetail.setBq4("0");
                }
                Double.parseDouble(this.currentDetail.getBq4());

                if (this.currentDetail.getBhy() == null || "".equals(this.currentDetail.getBhy())) {
                    this.currentDetail.setBhy("0");
                }
                Double.parseDouble(this.currentDetail.getBhy());

                if (this.currentDetail.getBfy() == null || "".equals(this.currentDetail.getBfy())) {
                    this.currentDetail.setBfy("0");
                }
                Double.parseDouble(this.currentDetail.getBfy());

                if (this.currentDetail.getTq1() == null || "".equals(this.currentDetail.getTq1())) {
                    this.currentDetail.setTq1("0");
                }
                Double.parseDouble(this.currentDetail.getTq1());

                if (this.currentDetail.getTq2() == null || "".equals(this.currentDetail.getTq2())) {
                    this.currentDetail.setTq2("0");
                }
                Double.parseDouble(this.currentDetail.getTq2());

                if (this.currentDetail.getTq3() == null || "".equals(this.currentDetail.getTq3())) {
                    this.currentDetail.setTq3("0");
                }
                Double.parseDouble(this.currentDetail.getTq3());

                if (this.currentDetail.getTq4() == null || "".equals(this.currentDetail.getTq4())) {
                    this.currentDetail.setTq4("0");
                }
                Double.parseDouble(this.currentDetail.getTq4());

                if (this.currentDetail.getThy() == null || "".equals(this.currentDetail.getThy())) {
                    this.currentDetail.setThy("0");
                }
                Double.parseDouble(this.currentDetail.getThy());

                if (this.currentDetail.getTfy() == null || "".equals(this.currentDetail.getTfy())) {
                    this.currentDetail.setTfy("0");
                }
                Double.parseDouble(this.currentDetail.getTfy());
            } catch (Exception e) {
                this.currentDetail.setCalculationtype("A");
                this.showErrorMsg("Error", "修改失败，基准，目标或实际存在文本类型。");
            }
        }
        return false;
    }

    @Override
    public void createDetail() {
        if (this.getNewDetail() == null) {
            try {
                this.newDetail = (PolicyDetail) this.detailClass.newInstance();
                if (this.currentDetail != null) {
                    this.newDetail.setSeq(this.currentDetail.getSeq() + 1);
                } else {
                    this.newDetail.setSeq(this.getMaxSeq(this.detailList));
                }
                //先调整添加后受影响的数据序号，再把新的数据放入集合中
                int i = this.newDetail.getSeq();
                for (PolicyDetail pc : this.detailList) {
                    if (pc.getSeq() >= this.newDetail.getSeq()) {
                        i++;
                        this.currentDetail = pc;
                        this.currentDetail.setSeq(i);
                        this.doConfirmDetail();
                    }
                }
                this.detailList.add(this.newDetail);
                detailList.sort((PolicyDetail o1, PolicyDetail o2) -> {
                    if (o1.getSeq() > o2.getSeq()) {
                        return 1;
                    } else {
                        return -1;
                    }
                });
            } catch (IllegalAccessException | InstantiationException var2) {
                Logger.getLogger(PolicyDetail.class.getName()).log(Level.SEVERE, (String) null, var2);
                this.showErrorMsg("Error", var2.getMessage());
            }
        }
        this.setCurrentDetail(this.newDetail);
    }

    public String edit(String path) {
        //防止未按保存，或者刷新以后直接进入编辑。。。清除调整的数组，currentDetail和newDateil
        if ("policysetEdit".equals(path)) {
            if (this.addedDetailList != null && !this.addedDetailList.isEmpty()) {
                this.addedDetailList.clear();
            }

            if (this.editedDetailList != null && !this.editedDetailList.isEmpty()) {
                this.editedDetailList.clear();
            }

            if (this.deletedDetailList != null && !this.deletedDetailList.isEmpty()) {
                this.deletedDetailList.clear();
            }
            this.setCurrentDetail(null);
            this.setNewDetail(null);
        }
        if (this.currentEntity != null) {
            return path;
        } else {
            this.showWarnMsg("Warn", "没有选择编辑数据!");
            return "";
        }
    }

    public void updateActual() {
        String col = policyBean.getColumn("q", userManagedBean.getQ());
        //PLM明细
        if (currentDetail.getFromplm() != null && !currentDetail.getFromplm().equals("")) {
            updateScoreByPLMProject();
            return;
        }
        //KPI明细        
        if (currentDetail.getFromkpi() != null && currentDetail.getFromkpi() != null) {
            Indicator i = indicatorBean.findById(currentDetail.getParent().getYear());
            if (i != null) {
                switch (userManagedBean.getQ()) {
                    case 1:
                        currentDetail.setAq1(i.getActualIndicator().getNq1().toString());
                        break;
                    case 2:
                        currentDetail.setAq2(i.getActualIndicator().getNq2().toString());
                        currentDetail.setAhy(i.getActualIndicator().getNh1().toString());
                        break;
                    case 3:
                        currentDetail.setAq3(i.getActualIndicator().getNq3().toString());
                        break;
                    case 4:
                        currentDetail.setAq4(i.getActualIndicator().getNq4().toString());
                        currentDetail.setAfy(i.getActualIndicator().getNfy().toString());
                        break;
                }
            }
            calcItemScore();
            return;
        }
        //数字格式明细
        calcItemScore();
        return;
    }

    public void calcItemScore() {
        if (currentDetail != null) {
            try {
                if (currentDetail.getParent().getFreezeDate() != null
                        && currentDetail.getParent().getFreezeDate().after(userManagedBean.getBaseDate())) {
                    showErrorMsg("Error", "资料已冻结,不可更新");
                    return;
                }
                String col = policyBean.getColumn("q", userManagedBean.getQ());
                String target, actual;
                BigDecimal value;
                //PLM为文本格式，计算达成做单独处理
                if (this.currentDetail.getFromplm() != null && !"".equals(this.currentDetail.getFromplm())) {
                    switch (col) {
                        case "q1":
                            target = currentDetail.getTq1();
                            actual = currentDetail.getAq1();
                            value = calculateScore(target, actual);
                            currentDetail.setPq1(value);
                            break;
                        case "q2":
                            target = currentDetail.getTq2();
                            actual = currentDetail.getAq2();
                            value = calculateScore(target, actual);
                            currentDetail.setPq2(value);

                            target = currentDetail.getThy();
                            actual = currentDetail.getAhy();
                            value = calculateScore(target, actual);
                            currentDetail.setPhy(value);
                            break;
                        case "q3":
                            target = currentDetail.getTq2();
                            actual = currentDetail.getAq2();
                            value = calculateScore(target, actual);
                            currentDetail.setPq2(value);
                            break;
                        case "q4":
                            target = currentDetail.getTq4();
                            actual = currentDetail.getAq4();
                            value = calculateScore(target, actual);
                            currentDetail.setPq4(value);

                            target = currentDetail.getTfy();
                            actual = currentDetail.getAfy();
                            value = calculateScore(target, actual);
                            currentDetail.setPfy(value);
                            break;
                    }
                    return;
                }
                if (!currentDetail.getCalculationtype().equals("B")) {
                    showWarnMsg("Warn", "数值型才能更新");
                    return;
                }

                showInfoMsg("Info", "更新部门分数成功");
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    // 关联PLM的更新
    public void updateScoreByPLMProject() throws StringIndexOutOfBoundsException, NumberFormatException {
        try {
            String target, actual, projectSeq;
            BigDecimal value;
            String col = policyBean.getColumn("q", userManagedBean.getQ());
            // 找到PLM的数据
            projectSeq = projectBean.findByProjectSeq(currentDetail.getFromplm());
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
                    break;
                case "q2":
                    currentDetail.setAq2("#" + projectSeq + "%#" + ";" + currentDetail.getAq2());
                    //Q2
                    target = currentDetail.getTq2();
                    actual = currentDetail.getAq2();
                    value = calculateScore(target, actual);
                    currentDetail.setPq2(value);
                    //上半年
                    currentDetail.setAhy("#" + projectSeq + "%#" + ";" + currentDetail.getAhy());
                    target = currentDetail.getThy();
                    actual = currentDetail.getAhy();
                    value = calculateScore(target, actual);
                    currentDetail.setPhy(value);
                    break;
                case "q3":
                    currentDetail.setAq3("#" + projectSeq + "%#" + ";" + currentDetail.getAq3());
                    target = currentDetail.getTq3();
                    actual = currentDetail.getAq3();
                    value = calculateScore(target, actual);
                    currentDetail.setPq3(value);
                    break;
                case "q4":
                    //Q4
                    currentDetail.setAq4("#" + projectSeq + "%#" + ";" + currentDetail.getAq4());
                    target = currentDetail.getTq4();
                    actual = currentDetail.getAq4();
                    value = calculateScore(target, actual);
                    currentDetail.setPq4(value);
                    //全年
                    currentDetail.setAfy("#" + projectSeq + "%#" + ";" + currentDetail.getAfy());
                    target = currentDetail.getTfy();
                    actual = currentDetail.getAfy();
                    value = calculateScore(target, actual);
                    currentDetail.setPfy(value);
                    break;
            }
            policyDetailBean.update(currentDetail);
            showInfoMsg("Info", "更新成功！");
        } catch (StringIndexOutOfBoundsException | NumberFormatException ex) {
            throw new NumberFormatException("无法解析时间，请检查！");
        }
    }

    /**
     * @desc 截取字符的数字计算得分、达成率
     * @param target
     * @param acutal
     * @return value
     */
    public BigDecimal calculateScore(String target, String acutal) throws StringIndexOutOfBoundsException {
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
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getQueryDeptname() {
        return queryDeptname;
    }

    public void setQueryDeptname(String queryDeptname) {
        this.queryDeptname = queryDeptname;
    }

    public String getQueryState() {
        return queryState;
    }

    public void setQueryState(String queryState) {
        this.queryState = queryState;
    }

    public boolean isIsRateRequired() {
        return isRateRequired;
    }

    public void setIsRateRequired(boolean isRateRequired) {
        this.isRateRequired = isRateRequired;
    }

    public void setBeforeDetailEntity() {
        this.beforeDetailEntity = this.currentDetail;
    }
}
