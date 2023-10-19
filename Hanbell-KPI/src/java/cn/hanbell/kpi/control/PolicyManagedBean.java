/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.ejb.PolicyBean;
import cn.hanbell.kpi.ejb.PolicyContentBean;
import cn.hanbell.kpi.ejb.tms.ProjectBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.PolicyContent;
import cn.hanbell.kpi.lazy.PolicyContentModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "policyManagedBean")
@ViewScoped
public class PolicyManagedBean extends SuperSingleBean<PolicyContent> {

    @EJB
    private PolicyContentBean policyContentBean;

    @EJB
    private PolicyBean policyBean;

    @EJB
    private IndicatorBean indicatorBean;

    @EJB
    private ProjectBean projectBean;

    private List<PolicyContent> policyDetail;
    private PolicyContent selectPolicyDetail;
    private Policy policy;
    protected Calendar c;
    private boolean isImport;

    public PolicyManagedBean() {
        super(PolicyContent.class);
        c = Calendar.getInstance();
    }

    @Override
    public void construct() {
        if (fc == null) {
            fc = FacesContext.getCurrentInstance();
        }
        if (ec == null) {
            ec = fc.getExternalContext();
        }
        HttpServletRequest request = (HttpServletRequest) ec.getRequest();
        String id = request.getParameter("id");
        if (id == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        policy = policyBean.findById(Integer.valueOf(id));
        if (policy == null) {
            fc.getApplication().getNavigationHandler().handleNavigation(fc, null, "error");
        }
        init();
        super.construct();
    }

    @Override
    public void init() {
        isImport = true;
        c.setTime(userManagedBean.getBaseDate());
        this.superEJB = policyBean;
        model = new PolicyContentModel(policyContentBean, this.userManagedBean);
        model.getSortFields().put("seq", "ASC");
        model.getFilterFields().put("parent.year", c.get(Calendar.YEAR));
        if (this.getPolicy() != null && this.getPolicy().getId() != null) {
            model.getFilterFields().put("pid", getPolicy().getId());
        }
        super.init();
        policyDetail = policyContentBean.findByFilters(model.getFilterFields(), model.getSortFields());
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        return true;
    }

    @Override
    public void update() {
        if (this.selectPolicyDetail != null) {
            try {
                if (this.doBeforeUpdate()) {
                    this.getSuperEJB().update(this.selectPolicyDetail);
                    this.doAfterUpdate();
                    this.showInfoMsg("Info", "更新成功!");
                } else {
                    this.showWarnMsg("Warn", "更新前检查失败!");
                }
            } catch (Exception var2) {
                this.showErrorMsg("Error", var2.toString());
            }
        } else {
            this.showWarnMsg("Warn", "没有可更新数据!");
        }

    }

    //更新实际与达成
    public void calcItemScore() {
        try {
            if (selectPolicyDetail != null) {
                //更新KPI数据
                String col = policyContentBean.getColumn("q", userManagedBean.getQ());
                String target, actual, projectSeq;
                BigDecimal value;
                if (selectPolicyDetail.getFromkpi() != null && !"".equals(selectPolicyDetail.getFromkpi()) && selectPolicyDetail.getFromkpiname() != null && !"".equals(selectPolicyDetail.getFromkpiname())) {
                    Indicator i = indicatorBean.findByIdAndYear(Integer.valueOf(selectPolicyDetail.getFromkpi()), userManagedBean.getY());;
                    if (i != null) {
                        switch (userManagedBean.getQ()) {
                            case 1:
                                selectPolicyDetail.setAq1(i.getActualIndicator().getNq1().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq1(i.getPerformanceIndicator().getNq1());
                                break;
                            case 2:
                                selectPolicyDetail.setAq2(i.getActualIndicator().getNq2().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq2(i.getPerformanceIndicator().getNq2());
                                selectPolicyDetail.setAhy(i.getActualIndicator().getNh1().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPhy(i.getPerformanceIndicator().getNh1());
                                break;
                            case 3:
                                selectPolicyDetail.setAq3(i.getActualIndicator().getNq3().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq3(i.getPerformanceIndicator().getNq3());
                                break;
                            case 4:
                                selectPolicyDetail.setAq4(i.getActualIndicator().getNq4().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPq4(i.getPerformanceIndicator().getNq4());
                                selectPolicyDetail.setAfy(i.getActualIndicator().getNfy().divide(selectPolicyDetail.getIndicatorrate(), 2, BigDecimal.ROUND_HALF_UP).toString());
                                selectPolicyDetail.setPfy(i.getPerformanceIndicator().getNfy());
                                break;
                        }
                        showInfoMsg("Info", "更新实际值成功");
                    } else {
                        showErrorMsg("Error", "找不到相关指标,更新失败");
                    }
                    return;
                }
                //更新PLM数据
                if (selectPolicyDetail.getFromplm() != null && !"".equals(selectPolicyDetail.getFromplm())) {
                    projectSeq = projectBean.findByProjectSeq(selectPolicyDetail.getFromplm());
                    if (projectSeq == null || "".equals(projectSeq)) {
                        showErrorMsg("Error", "请确认PLM是否有进度");
                        return;
                    }
                    if (!"B".equals(selectPolicyDetail.getCalculationtype())) {
                        showErrorMsg("Error", "文字类型不能使用plm更新，请调整");
                        return;
                    }
                    switch (col) {
                        case "q1":
                            selectPolicyDetail.setAq1(projectSeq);
                            target = selectPolicyDetail.getTq1();
                            actual = selectPolicyDetail.getAq1();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq1(value);
                            break;
                        case "q2":
                            selectPolicyDetail.setAq2(projectSeq);
                            //Q2
                            target = selectPolicyDetail.getTq2();
                            actual = selectPolicyDetail.getAq2();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq2(value);
                            //上半年
                            selectPolicyDetail.setAhy(projectSeq);
                            target = selectPolicyDetail.getThy();
                            actual = selectPolicyDetail.getAhy();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPhy(value);
                            break;
                        case "q3":
                            selectPolicyDetail.setAq3(projectSeq);
                            target = selectPolicyDetail.getTq3();
                            actual = selectPolicyDetail.getAq3();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq3(value);
                            break;
                        case "q4":
                            selectPolicyDetail.setAq4(projectSeq);
                            target = selectPolicyDetail.getTq4();
                            actual = selectPolicyDetail.getAq4();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPq4(value);

                            selectPolicyDetail.setAfy(projectSeq);
                            target = selectPolicyDetail.getTfy();
                            actual = selectPolicyDetail.getAfy();
                            value = calculateScore(target, actual);
                            selectPolicyDetail.setPfy(value);
                            break;
                    }
                    return;
                }
                //数字型更新达成率
                if (!selectPolicyDetail.getCalculationtype().equals("B")) {
                    showErrorMsg("Warn", "数值型才能按计算公式更新");
                    return;
                }
                //A: 实际/目标
                switch (col) {
                    case "q1":
                        target = selectPolicyDetail.getTq1();
                        actual = selectPolicyDetail.getAq1();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq1(value);
                        break;
                    case "q2":
                        //Q2
                        target = selectPolicyDetail.getTq2();
                        actual = selectPolicyDetail.getAq2();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq2(value);
                        //上半年
                        target = selectPolicyDetail.getThy();
                        actual = selectPolicyDetail.getAhy();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPhy(value);
                        break;
                    case "q3":
                        target = selectPolicyDetail.getTq3();
                        actual = selectPolicyDetail.getAq3();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq3(value);
                        break;
                    case "q4":
                        target = selectPolicyDetail.getTq4();
                        actual = selectPolicyDetail.getAq4();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPq4(value);

                        target = selectPolicyDetail.getTfy();
                        actual = selectPolicyDetail.getAfy();
                        value = calculatePerformance(target, actual);
                        selectPolicyDetail.setPfy(value);
                        break;
                }

                //B: 目标/实际
            }
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMsg("Error", "更新失败！！");
            return;
        }
    }

    public BigDecimal calculatePerformance(String target, String actual) {
        //实际/目标
        BigDecimal t = BigDecimal.valueOf(Double.valueOf(target));
        BigDecimal a = BigDecimal.valueOf(Double.valueOf(actual));
        if (selectPolicyDetail.getPerformancecalculation().equals("A")) {
            return a.multiply(new BigDecimal(100)).divide(t, 2, RoundingMode.HALF_UP);
        } else {
            return t.multiply(new BigDecimal(100)).divide(a, 2, RoundingMode.HALF_UP);
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
//                str1 = target.substring(target.indexOf("#") + 1, target.indexOf("%"));
//                str2 = acutal.substring(acutal.indexOf("#") + 1, acutal.indexOf("%"));
            //判断截取出来的数据是否为数字

            if (target.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$") && acutal.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$")) {
                Double t = Double.valueOf(target);
                Double a = Double.valueOf(acutal);
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

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        UploadedFile file = event.getFile();
        if (file != null) {
            int index = 0;
            try {
                InputStream is = file.getInputstream();
                Workbook excel = WorkbookFactory.create(is);
                Sheet sheet = excel.getSheetAt(0);
                Row row;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    index = i;
                    row = sheet.getRow(i);
                    PolicyContent pc = this.policyContentBean.findByPidAndSeq(this.policy.getId(), (int) Math.round(BaseLib.convertExcelCell(Double.class, row.getCell(0))));
                    if (pc != null) {
                        if (!BaseLib.convertExcelCell(String.class, row.getCell(2)).equals(pc.getSeqname() + pc.getName())) {
                            throw new Exception(String.format("%s没有此明细", BaseLib.convertExcelCell(String.class, row.getCell(2))));
                        }
                        switch (this.userManagedBean.getQ()) {
                            case 1:
                                pc.setAq1(BaseLib.convertExcelCell(String.class, row.getCell(5)));
                                if ("B".equals(pc.getCalculationtype())) {
                                    BigDecimal target = pc.getTq1() == null ? BigDecimal.ZERO : new BigDecimal(pc.getTq1());
                                    BigDecimal actual = BaseLib.convertExcelCell(Double.class, row.getCell(5)) == null ? BigDecimal.ZERO : BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(5)));
                                    pc.setPq1(this.policyContentBean.getPerformance(pc.getPerformancecalculation(), target, actual));
                                } else {
                                    pc.setPq1(BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(6)) * 100));
                                }
                                pc.setQ1reason1(BaseLib.convertExcelCell(String.class, row.getCell(7)));
                                pc.setQ1countermeasure1(BaseLib.convertExcelCell(String.class, row.getCell(8)));
                                break;
                            case 2:
                                pc.setAq2(BaseLib.convertExcelCell(String.class, row.getCell(5)));
                                if ("B".equals(pc.getCalculationtype())) {
                                    BigDecimal target = pc.getTq2() == null ? BigDecimal.ZERO : new BigDecimal(pc.getTq2());
                                    BigDecimal actual = BaseLib.convertExcelCell(Double.class, row.getCell(5)) == null ? BigDecimal.ZERO : BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(5)));
                                    pc.setPq2(this.policyContentBean.getPerformance(pc.getPerformancecalculation(), target, actual));
                                } else {
                                    pc.setPq2(BaseLib.convertExcelCell(Double.class, row.getCell(6))==null ? null :BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(6)) * 100));
                                }
                                pc.setAhy(BaseLib.convertExcelCell(String.class, row.getCell(9)));
                                if ("B".equals(pc.getCalculationtype())) {
                                    BigDecimal target = pc.getThy() == null ? BigDecimal.ZERO : new BigDecimal(pc.getThy());
                                    BigDecimal actual = BaseLib.convertExcelCell(Double.class, row.getCell(9)) == null ? BigDecimal.ZERO : BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(9)));
                                    pc.setPhy(this.policyContentBean.getPerformance(pc.getPerformancecalculation(), target, actual));
                                } else {
                                    pc.setPhy(BaseLib.convertExcelCell(Double.class, row.getCell(10))==null?null :BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(10)) * 100));
                                }
                                pc.setHyreason1(BaseLib.convertExcelCell(String.class, row.getCell(11)));
                                pc.setHycountermeasure1(BaseLib.convertExcelCell(String.class, row.getCell(12)));
                                break;
                            case 3:
                                pc.setAq3(BaseLib.convertExcelCell(String.class, row.getCell(5)));
                                if ("B".equals(pc.getCalculationtype())) {
                                    BigDecimal target = pc.getTq3() == null ? BigDecimal.ZERO : new BigDecimal(pc.getTq3());
                                    BigDecimal actual = BaseLib.convertExcelCell(Double.class, row.getCell(5)) == null ? BigDecimal.ZERO : BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(5)));
                                    pc.setPq3(this.policyContentBean.getPerformance(pc.getPerformancecalculation(), target, actual));
                                } else {
                                    pc.setPq3(BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(6)) * 100));
                                }
                                pc.setQ3reason1(BaseLib.convertExcelCell(String.class, row.getCell(7)));
                                pc.setQ3countermeasure1(BaseLib.convertExcelCell(String.class, row.getCell(8)));
                                break;
                            case 4:
                                pc.setAq4(BaseLib.convertExcelCell(String.class, row.getCell(5)));
                                if ("B".equals(pc.getCalculationtype())) {
                                    BigDecimal target = pc.getTq4() == null ? BigDecimal.ZERO : new BigDecimal(pc.getTq4());
                                    BigDecimal actual = BaseLib.convertExcelCell(Double.class, row.getCell(5)) == null ? BigDecimal.ZERO : BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(5)));
                                    pc.setPq4(this.policyContentBean.getPerformance(pc.getPerformancecalculation(), target, actual));
                                } else {
                                    pc.setPq4(BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(6)) * 100));
                                }
                                pc.setAfy(BaseLib.convertExcelCell(String.class, row.getCell(9)));
                                if ("B".equals(pc.getCalculationtype())) {
                                    BigDecimal target = pc.getTfy() == null ? BigDecimal.ZERO : new BigDecimal(pc.getTfy());
                                    BigDecimal actual = BaseLib.convertExcelCell(Double.class, row.getCell(9)) == null ? BigDecimal.ZERO : BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(9)));
                                    pc.setPfy(this.policyContentBean.getPerformance(pc.getPerformancecalculation(), target, actual));
                                } else {
                                    pc.setPfy(BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(10)) * 100));
                                }
                                pc.setFyreason1(BaseLib.convertExcelCell(String.class, row.getCell(11)));
                                pc.setFycountermeasure1(BaseLib.convertExcelCell(String.class, row.getCell(12)));
                                break;
                        }
                        this.policyContentBean.update(pc);
                    } else {
                        throw new Exception("没有序号为" + BaseLib.convertExcelCell(String.class, row.getCell(2)) + "的数据");
                    }
                }
                this.showInfoMsg("Info", "导入成功");
            } catch (Exception e) {
                e.printStackTrace();
                this.showErrorMsg("Error", String.format("第%d行数据发生错误，%s", index + 1, e.getMessage()));
            }
        }
    }

    @Override
    public void print() throws Exception {
        isImport = false;
        OutputStream os = null;
        InputStream is = null;

        try {
            String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("WEB-INF");
            String filePath = new String(finalFilePath.substring(1, index));
            String pathString = new String(filePath.concat("rpt/"));
            File file = new File(pathString, "方针实际数据更新模板.xlsx");
            is = new FileInputStream(file);
            this.fileName = this.policy.getName() + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xlsx";
            Workbook wb = WorkbookFactory.create(is);
            CreationHelper createHelper = wb.getCreationHelper();
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

            CellStyle percentStyle = wb.createCellStyle();
            percentStyle.setBorderRight(CellStyle.BORDER_THIN);
            percentStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            percentStyle.setBorderLeft(CellStyle.BORDER_THIN);
            percentStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            percentStyle.setBorderTop(CellStyle.BORDER_THIN);
            percentStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            percentStyle.setBorderBottom(CellStyle.BORDER_THIN);
            percentStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            percentStyle.setDataFormat(wb.createDataFormat().getFormat("0.00%"));
            Sheet sheet = wb.getSheetAt(0);
            Cell cell;
            Row row;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("序号");
            cell = row.createCell(1);
            cell.setCellValue("目标类别");

            cell = row.createCell(2);
            cell.setCellValue("方针内容");
            switch (this.userManagedBean.getQ()) {
                case 1:
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q1基准");
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q1目标");
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q1实际");
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q1达成");
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("原因");
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("对策");
                    break;
                case 2:
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q2基准");
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q2目标");
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q2实际");
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q2达成");

                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("上半年基准");
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("上半年目标");
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("上半年实际");
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("上半年达成");
                    cell = row.createCell(11);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("原因");
                    cell = row.createCell(12);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("对策");
                    break;
                case 3:
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q3基准");
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q3目标");
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q3实际");
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q3达成");
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("原因");
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("对策");
                    break;
                case 4:
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q4基准");
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q4目标");
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("Q4实际");
                    cell.setCellStyle(cellStyle);
                    cell = row.createCell(6);
                    cell.setCellValue("Q4达成");

                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("全年基准");
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("全年目标");
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("全年实际");
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("全年达成");

                    cell = row.createCell(11);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("全年原因");
                    cell = row.createCell(12);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue("全年对策");
                    break;
            }
            int i = 1;
            for (PolicyContent pd : this.policyDetail) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(pd.getSeq());
                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(pd.getPerspective() + pd.getObjective());
                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(pd.getSeqname() + pd.getName());
                switch (this.userManagedBean.getQ()) {
                    case 1:
                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getBq1());
                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getTq1());
                        cell = row.createCell(5);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getAq1());
                        cell = row.createCell(6);
                        cell.setCellStyle(percentStyle);
                        cell.setCellValue(pd.getPq1() == null ? 0.00 : pd.getPq1().divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        cell = row.createCell(7);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getQ1reason1());
                        cell = row.createCell(8);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getQ1countermeasure1());
                        break;
                    case 2:
                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getBq2());
                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getTq2());
                        cell = row.createCell(5);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getAq2());
                        cell = row.createCell(6);
                        cell.setCellStyle(percentStyle);
                        cell.setCellValue(pd.getPq2() == null ? 0.00 : pd.getPq2().divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 2, BigDecimal.ROUND_HALF_UP).doubleValue());

                        cell = row.createCell(7);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getBhy());
                        cell = row.createCell(8);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getThy());
                        cell = row.createCell(9);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getAhy());
                        cell = row.createCell(10);
                        cell.setCellStyle(percentStyle);
                        cell.setCellValue(pd.getPhy() == null ? 0.00 : pd.getPhy().divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        cell = row.createCell(11);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getHyreason1());
                        cell = row.createCell(12);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getHycountermeasure1());
                        break;
                    case 3:
                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getBq3());
                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getTq3());
                        cell = row.createCell(5);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getAq3());
                        cell = row.createCell(6);
                        cell.setCellStyle(percentStyle);
                        cell.setCellValue(pd.getPq3() == null ? 0.00 : pd.getPq3().divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        cell = row.createCell(7);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getQ3reason1());
                        cell = row.createCell(8);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getQ3countermeasure1());
                        break;
                    case 4:
                        cell = row.createCell(3);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getBq4());
                        cell = row.createCell(4);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getTq4());
                        cell = row.createCell(5);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getAq4());
                        cell = row.createCell(6);
                        cell.setCellStyle(percentStyle);
                        cell.setCellValue(pd.getPq4() == null ? 0.00 : pd.getPq4().divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 2, BigDecimal.ROUND_HALF_UP).doubleValue());

                        cell = row.createCell(7);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getBfy());
                        cell = row.createCell(8);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getTfy());
                        cell = row.createCell(9);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getAfy());
                        cell = row.createCell(10);
                        cell.setCellStyle(percentStyle);
                        cell.setCellValue(pd.getPfy() == null ? 0.00 : pd.getPfy().divide(BigDecimal.TEN.multiply(BigDecimal.TEN), 2, BigDecimal.ROUND_HALF_UP).doubleValue());
                        cell = row.createCell(11);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getFyreason1());
                        cell = row.createCell(12);
                        cell.setCellStyle(cellStyle);
                        cell.setCellValue(pd.getFycountermeasure1());
                        break;
                }
                i++;
            }

            os = new FileOutputStream(this.reportOutputPath + this.fileName);
            wb.write(os);
            this.reportViewPath = this.reportViewContext + this.fileName;
            this.preview();
        } catch (Exception ex) {
            ex.printStackTrace();
            showErrorMsg("Error", ex.getMessage());
        } finally {
            try {
                if (null != os) {
                    os.flush();
                    os.close();
                }
            } catch (IOException ex) {
                showErrorMsg("Error", ex.getMessage());
            }
        }
    }

    public boolean isDouble(String value) {
        isImport = false;
        try {
            Double.valueOf(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<PolicyContent> getPolicyDetail() {
        return policyDetail;
    }

    public void setPolicyDetail(List<PolicyContent> policyDetail) {
        this.policyDetail = policyDetail;
    }

    public PolicyContent getSelectPolicyDetail() {
        return selectPolicyDetail;
    }

    public void setSelectPolicyDetail(PolicyContent selectPolicyDetail) {
        this.selectPolicyDetail = selectPolicyDetail;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public boolean isIsImport() {
        return isImport;
    }

    public void setIsImport(boolean isImport) {
        this.isImport = isImport;
    }

}
