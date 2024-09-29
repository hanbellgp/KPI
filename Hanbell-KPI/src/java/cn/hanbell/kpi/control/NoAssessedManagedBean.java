/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.PersonScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.entity.PersonScorecard;
import cn.hanbell.kpi.entity.PersonScorecardDetail;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.lazy.PersonScorecardModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "noAssessedManagedBean")
@SessionScoped
public class NoAssessedManagedBean extends SuperMultiBean<PersonScorecard, PersonScorecardDetail> {

    private int quater;
    private int year;
    private String queryUserId;
    private String queryUserName;
    private String queryDeptNo;
    private String queryDeptName;
    private String queryQ1status;
    private String queryQ2status;
    private String queryQ3status;
    private String queryQ4status;

    public NoAssessedManagedBean() {
        super(PersonScorecard.class, PersonScorecardDetail.class);
    }

    @EJB
    private PersonScorecardBean personScorecardBean;

    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private SystemUserBean systemUserBean;

    @EJB
    private Agent1000002Bean agent1000002Bean;

    @Override
    public void init() {
        superEJB = personScorecardBean;
        year = this.userManagedBean.getY();
        quater = this.userManagedBean.getQ();
        model = new PersonScorecardModel(personScorecardBean);
        model.getSortFields().put("userid", "ASC");
        queryQ1status = "All";
        queryQ2status = "All";
        queryQ3status = "All";
        queryQ4status = "All";
        model.getFilterFields().put("status", String.valueOf(this.userManagedBean.getQ()));
        super.init();
        this.query();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
             model.getFilterFields().put("status", String.valueOf(this.userManagedBean.getQ()));
            if (queryUserId != null && !"".equals(queryUserId)) {
                model.getFilterFields().put("userid", queryUserId);
            }
            if (queryUserName != null && !"".equals(queryUserName)) {
                model.getFilterFields().put("username", queryUserName);
            }
            if (queryDeptNo != null && !"".equals(queryDeptNo)) {
                model.getFilterFields().put("personset.deptno", queryDeptNo);
            }
            if (queryDeptName != null && !"".equals(queryDeptName)) {
                model.getFilterFields().put("personset.deptname", queryDeptName);
            }
            if (queryQ1status != null && !"All".equals(queryQ1status)) {
                model.getFilterFields().put("status1", queryQ1status);
            }
            if (queryQ2status != null && !"All".equals(queryQ2status)) {
                model.getFilterFields().put("status2", queryQ2status);
            }
            if (queryQ3status != null && !"All".equals(queryQ3status)) {
                model.getFilterFields().put("status3", queryQ3status);
            }
            if (queryQ4status != null && !"All".equals(queryQ4status)) {
                model.getFilterFields().put("status4", queryQ4status);
            }
        }
    }

    @Override
    public void reset() {
        queryQ1status = "All";
        queryQ2status = "All";
        queryQ3status = "All";
        queryQ4status = "All";
        queryUserId = "";
        queryUserName = "";
        queryDeptNo = "";
        queryDeptName = "";
        this.query();
    }

    /**
     * 计算CDE的主管考核
     */
    public void calculate() {
        try {
            List<PersonScorecard> list = this.superEJB.findByFilters(model.getFilterFields());
            //获取个人主管考评分数，BSC分数。并按照计算类型获取百分比计算
            Field f = null;
            Method method = null;
            BigDecimal a = BigDecimal.ZERO;
            BigDecimal b = BigDecimal.ZERO;
            BigDecimal sum = BigDecimal.ZERO;
            PersonScorecard e=null; 
            for (PersonScorecard sc : list) {
                a = BigDecimal.ZERO;
                b = BigDecimal.ZERO;
                //获取课级考核表分数
                if("C2632".equals(sc.getUserid())){
            }
                if (sc.getPersonset().getClassscorecard() != null && !"".equals(sc.getPersonset().getClassscorecard())) {
                    Scorecard entity = scorecardBean.findByNameAndSeqAndCompany(sc.getPersonset().getClassscorecard(), year, this.userManagedBean.getCompany());
                    //I,J对应AB职等，需要取考核表强制分布分数
                    if ("I".equals(sc.getPersonset().getAssessmentmethod()) || "J".equals(sc.getPersonset().getAssessmentmethod())) {
                        f = entity.getClass().getDeclaredField(scorecardBean.getColumn("lq", this.quater));
                    } else {
                        //取考核表分数
                        f = entity.getClass().getDeclaredField(scorecardBean.getColumn("sq", this.quater));
                    }
                    f.setAccessible(true);
                    a = ((BigDecimal) f.get(entity));
                }
                if (sc.getPersonset().getDepartmentscorecard() != null && !"".equals(sc.getPersonset().getDepartmentscorecard())) {
                    Scorecard entity = scorecardBean.findByNameAndSeqAndCompany(sc.getPersonset().getDepartmentscorecard(), this.userManagedBean.getY(), this.userManagedBean.getCompany());
                    //I,J对应AB职等，需要取考核表强制分布分数
                    if ("I".equals(sc.getPersonset().getAssessmentmethod()) || "J".equals(sc.getPersonset().getAssessmentmethod())) {
                        f = entity.getClass().getDeclaredField(scorecardBean.getColumn("lq", this.quater));
                    } else {
                        //取考核表分数
                        f = entity.getClass().getDeclaredField(scorecardBean.getColumn("sq", this.quater));
                    }
                    f.setAccessible(true);
                    b = ((BigDecimal) f.get(entity));
                }
                
                //A,B职等并且课级考核表不为空的情况下取课级考核表分数  
                if (sc.getPersonset().getClassscorecard() != null && !"".equals(sc.getPersonset().getClassscorecard()) 
                    &&  ("I".equals(sc.getPersonset().getAssessmentmethod()) || "J".equals(sc.getPersonset().getAssessmentmethod()))) {
                    sum=a;
                   
                } else {
                    sum =b;
                }
                switch (this.quater) {
                    case 1:
                        sc.setScorecard1(sum);
                        sc.setScorecardratio1(sc.getPersonset().getPersonscorecardway().getScoreratio());
                        sc.setScorecardpro1(sc.getScorecard1().multiply(sc.getScorecardratio1()));
                        break;
                    case 2:
                        sc.setScorecard2(sum);
                        sc.setScorecardratio2(sc.getPersonset().getPersonscorecardway().getScoreratio());
                        sc.setScorecardpro2(sc.getScorecard2().multiply(sc.getScorecardratio2()));
                        break;
                    case 3:
                        sc.setScorecard3(sum);
                        sc.setScorecardratio3(sc.getPersonset().getPersonscorecardway().getScoreratio());
                        sc.setScorecardpro3(sc.getScorecard3().multiply(sc.getScorecardratio3()));
                        break;
                    case 4:
                        sc.setScorecard4(sum);
                        sc.setScorecardratio4(sc.getPersonset().getPersonscorecardway().getScoreratio());
                        sc.setScorecardpro4(sc.getScorecard1().multiply(sc.getScorecardratio4()));
                        break;
                }
            }
            this.superEJB.update(list);
            this.showInfoMsg("Info", "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            this.showErrorMsg("Errpr", "更新失败");
        }
    }

    public void sendmsg() {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("status" + quater, "N");
            List<PersonScorecard> personScorecards = this.superEJB.findByFilters(map);
            Map<String, List<String>> users = new HashMap<String, List<String>>();
            SystemUser user = null;
            for (PersonScorecard entity : personScorecards) {
                BigDecimal porobjquarter = (BigDecimal) personScorecardBean.getScore(entity, "objectivepro", quater);
                BigDecimal porsubquarter = (BigDecimal) personScorecardBean.getScore(entity, "subjectivitypro", quater);
                if ("I".equals(entity.getPersonset().getAssessmentmethod()) || "J".equals(entity.getPersonset().getAssessmentmethod())) {
                    if (porobjquarter.compareTo(BigDecimal.ZERO) == 0 || porsubquarter.compareTo(BigDecimal.ZERO) == 0) {
                        user = this.systemUserBean.findByUserId(entity.getUserid());
                        if (users.containsKey(user.getManagerId())) {
                            users.get(user.getManagerId()).add(entity.getPersonset().getUsername());
                        } else {
                            List<String> list = new ArrayList<String>();
                            list.add(entity.getPersonset().getUsername());
                            users.put(user.getManagerId(), list);
                        }
                    }
                } else if ("K".equals(entity.getPersonset().getAssessmentmethod()) || "L".equals(entity.getPersonset().getAssessmentmethod())
                        || "M".equals(entity.getPersonset().getAssessmentmethod()) || "N".equals(entity.getPersonset().getAssessmentmethod())) {
                    if (porsubquarter.compareTo(BigDecimal.ZERO) == 0) {
                        user = this.systemUserBean.findByUserId(entity.getUserid());
                        if (users.containsKey(user.getManagerId())) {
                            users.get(user.getManagerId()).add(entity.getPersonset().getUsername());
                        } else {
                            List<String> list = new ArrayList<String>();
                            list.add(entity.getPersonset().getUsername());
                            users.put(user.getManagerId(), list);
                        }
                    }
                }
            }
            Set<String> manageds = users.keySet();
            StringBuilder msg = new StringBuilder();
            agent1000002Bean.initConfiguration();
            for (String employeeid : manageds) {
                msg.setLength(0);
                msg.append("### 个人绩效考核:").append(BaseLib.formatDate("yyyy/MM/dd hh:mm", new Date()));
                msg.append("\\n").append("");
                msg.append("\\n").append(">**未考核人员通知**");
                msg.append("\\n").append(">未考核人员：<font color=\"info\">");
                for (String username : users.get(employeeid)) {
                    msg.append(username).append(";");
                }
                msg.append("</font>");
                msg.append("\\n").append("");
                msg.append("\\n").append("><font color=\"warning\">尊敬的主管～贵单位以上人员尚未完成考核，烦请尽快处理，谢谢配合！</font>");
                if (!"C0002".equals(employeeid)) {
                     agent1000002Bean.sendMsgToUser(employeeid, "markdown", msg.toString());
                }
            }
              this.showInfoMsg("Info", "发送成功");
        } catch (Exception e) {
            e.printStackTrace();
            this.showErrorMsg("Errpr", "发送失败");
        }
    }

    @Override
    public void print() throws Exception {
        OutputStream os = null;
        InputStream is = null;
        try {
            List<PersonScorecard> list = this.superEJB.findByFilters(this.model.getFilterFields());
            if (list == null || list.isEmpty()) {
                return;
            }
            String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("WEB-INF");
            String filePath = new String(finalFilePath.substring(1, index));
            String pathString = new String(filePath.concat("rpt/"));
            File file = new File(pathString, "绩效考核未考核人员模版.xlsx");
            is = new FileInputStream(file);
            Workbook wb = WorkbookFactory.create(is);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Sheet sheet = wb.getSheetAt(0);
            wb.setSheetName(0, sdf.format(new Date()) + "考核人员");
            this.fileName = "绩效考核未考核人员" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xlsx";
            String fileFullName = reportOutputPath + fileName;
            Row row = null;
            int i = 2;
            CellStyle cellStyle = wb.createCellStyle();
            cellStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

            CellStyle cellRedStyle = wb.createCellStyle();;
            cellRedStyle.setBorderRight(CellStyle.BORDER_THIN);
            cellRedStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            cellRedStyle.setBorderLeft(CellStyle.BORDER_THIN);
            cellRedStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            cellRedStyle.setBorderTop(CellStyle.BORDER_THIN);
            cellRedStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
            cellRedStyle.setBorderBottom(CellStyle.BORDER_THIN);
            cellRedStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            Font font = wb.createFont();
            font.setColor(IndexedColors.RED.getIndex());
            cellRedStyle.setFont(font);
            for (PersonScorecard entity : list) {
                row = sheet.createRow(i);
                Cell cell = row.createCell(0);
                cell.setCellValue(entity.getUserid());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(1);
                cell.setCellValue(entity.getUsername());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(2);
                cell.setCellValue(entity.getPersonset().getAssessmentlevel());
                cell.setCellStyle(cellStyle);

                cell = row.createCell(3);
                cell.setCellValue(entity.getSubjectivitypro1().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(4);
                cell.setCellValue(entity.getObjectivepro1().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(5);
                cell.setCellValue("Y".equals(entity.getStatus1()) ? "√" : "X");
                cell.setCellStyle("Y".equals(entity.getStatus1()) ? cellStyle : cellRedStyle);

                cell = row.createCell(6);
                cell.setCellValue(entity.getSubjectivitypro2().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(7);
                cell.setCellValue(entity.getObjectivepro2().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(8);
                cell.setCellValue("Y".equals(entity.getStatus2()) ? "√" : "X");
                cell.setCellStyle("Y".equals(entity.getStatus2()) ? cellStyle : cellRedStyle);

                cell = row.createCell(9);
                cell.setCellValue(entity.getSubjectivitypro3().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(10);
                cell.setCellValue(entity.getObjectivepro3().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(11);
                cell.setCellValue("Y".equals(entity.getStatus3()) ? "√" : "X");
                cell.setCellStyle("Y".equals(entity.getStatus3()) ? cellStyle : cellRedStyle);

                cell = row.createCell(12);
                cell.setCellValue(entity.getSubjectivitypro4().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(13);
                cell.setCellValue(entity.getObjectivepro4().doubleValue());
                cell.setCellStyle(cellStyle);
                cell = row.createCell(14);
                cell.setCellValue("Y".equals(entity.getStatus4()) ? "√" : "X");
                cell.setCellStyle("Y".equals(entity.getStatus4()) ? cellStyle : cellRedStyle);
                i++;
            }
            os = new FileOutputStream(fileFullName);
            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
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
    
    
    public void printSalary() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //先根据人员调整考核表强制分配后的分数
        Map<String, Object> filters = new HashMap<>();
        List<String> methods = new ArrayList<>();
        methods.add("I");
        methods.add("J");
        methods.add("K");
        methods.add("L");
        methods.add("M");
        methods.add("N");
        filters.put("personset.assessmentmethod IN ", methods);
        filters.put("year", this.userManagedBean.getY());
        List<PersonScorecard> list = personScorecardBean.findByFilters(filters);
        Field f;
        Method method = null;
        OutputStream os = null;
        try {
            fileName = "奖金明细" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
            String fileFullName = reportOutputPath + fileName;
            HSSFWorkbook wb = new HSSFWorkbook();
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
            //创建内容
            Sheet sheet = wb.createSheet(String.format("%d$d个人奖金明细", this.userManagedBean.getY(), this.userManagedBean.getM()));
            Cell cell;
            Row row;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("工号");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue("姓名");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue("部门");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue("课别");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue("职等");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue("行政职");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue("行政职系数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue("岗位类别");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(8);
            cell.setCellValue("个人&主管考评分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(9);
            cell.setCellValue("单位季度考核分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(10);
            cell.setCellValue("个人综合绩效考核分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(11);
            cell.setCellValue("奖金发放标准分值");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(12);
            cell.setCellValue("分值对应元/分");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(13);
            cell.setCellValue("单位发放比率");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(14);
            cell.setCellValue("发放奖金元/月");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(15);
            cell.setCellValue("备注");
            cell.setCellStyle(cellStyle);
            int i = 1;
            BigDecimal proScore, bscScore, c;
            for (PersonScorecard sc : list) {

                row = sheet.createRow(i);

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getUserid());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getUsername());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getPersonset().getDeptname());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                //cell.setCellValue(sc.getDeptClass());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getPersonset().getOfficialrank());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getPersonset().getIsadministrative());

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getPersonset().getCoefficient() != null ? sc.getPersonset().getCoefficient() : 0.0);

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getPersonset().getJobcategory());

              
                BigDecimal performancepro = (BigDecimal) personScorecardBean.getScore(sc, "performancepro",quater);
                    cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(performancepro.doubleValue());
                
                BigDecimal scorecardpro = (BigDecimal) personScorecardBean.getScore(sc, "scorecardpro", quater);
                         cell = row.createCell(9);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(scorecardpro.doubleValue());
               
                
                cell = row.createCell(10);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(performancepro.add(scorecardpro).doubleValue());

                cell = row.createCell(11);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(personScorecardBean.getStandardScore(sc.getPersonset().getAssessmentlevel(),
                        performancepro.add(scorecardpro).doubleValue()));

                cell = row.createCell(12);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(personScorecardBean.getAmountOfScore(sc.getPersonset().getAssessmentlevel(),
                        sc.getPersonset().getCoefficient() > 0));

                cell = row.createCell(13);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getPersonset().getPercentage().divide(BigDecimal.TEN).divide(BigDecimal.TEN).doubleValue());

                cell = row.createCell(14);
                cell.setCellStyle(cellStyle);
                double a1 = new BigDecimal(BaseLib.convertExcelCell(Double.class, row.getCell(6))).compareTo(BigDecimal.ZERO) > 0 ? BaseLib.convertExcelCell(Double.class, row.getCell(6)) : 1.00;
                double a2 = BaseLib.convertExcelCell(Double.class, row.getCell(11)) == null ? 0.00 : BaseLib.convertExcelCell(Double.class, row.getCell(11));
                double a3 = BaseLib.convertExcelCell(Double.class, row.getCell(12)) == null ? 0.00 : BaseLib.convertExcelCell(Double.class, row.getCell(12));
                cell.setCellValue(a1 * a2 * a3);
                cell = row.createCell(15);
                cell.setCellStyle(cellStyle);
                i++;
            }
            os = new FileOutputStream(fileFullName);
            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
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

    public void printScore() {
        Map<String, Object> filters = new HashMap<>();
        List<String> methods = new ArrayList<>();
        methods.add("I");
        methods.add("J");
        methods.add("K");
        methods.add("L");
        methods.add("M");
        methods.add("N");
        filters.put("personset.assessmentmethod IN ", methods);
        filters.put("year", this.userManagedBean.getY());
        List<PersonScorecard> list = personScorecardBean.findByFilters(filters);
        Field f;
        Method method = null;
        OutputStream os = null;
        try {
            fileName = "Q" + this.userManagedBean.getQ() + "考核分数" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
            String fileFullName = reportOutputPath + fileName;
            HSSFWorkbook wb = new HSSFWorkbook();
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
            //创建内容
            Sheet sheet = wb.createSheet(String.format("%d$d个人奖金明细", this.userManagedBean.getY(), this.userManagedBean.getM()));
            Cell cell;
            Row row;
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("工号");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(1);
            cell.setCellValue("姓名");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(2);
            cell.setCellValue("主观考核分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(3);
            cell.setCellValue("主观考核比率");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(4);
            cell.setCellValue("主观分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(5);
            cell.setCellValue("客观考核分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(6);
            cell.setCellValue("客观考核比率");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(7);
            cell.setCellValue("客观分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(8);
            cell.setCellValue("合计分数");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(9);
            cell.setCellValue("计算奖金折算的比率");
            cell.setCellStyle(cellStyle);

            cell = row.createCell(10);
            cell.setCellValue("折算分数");
            cell.setCellStyle(cellStyle);

            int i = 1;
            BigDecimal proScore, bscScore, c;
            for (PersonScorecard sc : list) {
                row = sheet.createRow(i);
                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getUserid());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(sc.getUsername());

                if (this.userManagedBean.getQ() == 1) {
                    cell = row.createCell(2);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivity1() == null ? 0.0 : sc.getSubjectivity1().doubleValue());
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivityratio1() == null ? 0.0 : sc.getSubjectivityratio1().doubleValue());
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivitypro1() == null ? 0.0 : sc.getSubjectivitypro1().doubleValue());
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjective1() == null ? 0.0 : sc.getObjective1().doubleValue());
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectiveratio1() == null ? 0.0 : sc.getObjectiveratio1().doubleValue());
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectivepro1() == null ? 0.0 : sc.getObjectivepro1().doubleValue());
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformance1() == null ? 0.0 : sc.getPerformance1().doubleValue());
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformanceratio1() == null ? 0.0 : sc.getPerformanceratio1().doubleValue());
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformancepro1() == null ? 0.0 : sc.getPerformancepro1().doubleValue());
                } else if (this.userManagedBean.getQ() == 2) {
                    cell = row.createCell(2);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivity2() == null ? 0.0 : sc.getSubjectivity2().doubleValue());
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivityratio2() == null ? 0.0 : sc.getSubjectivityratio2().doubleValue());
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivitypro2() == null ? 0.0 : sc.getSubjectivitypro2().doubleValue());
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjective2() == null ? 0.0 : sc.getObjective2().doubleValue());
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectiveratio2() == null ? 0.0 : sc.getObjectiveratio2().doubleValue());
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectivepro2() == null ? 0.0 : sc.getObjectivepro2().doubleValue());
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformance2() == null ? 0.0 : sc.getPerformance2().doubleValue());
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformanceratio2() == null ? 0.0 : sc.getPerformanceratio2().doubleValue());
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformancepro2() == null ? 0.0 : sc.getPerformancepro2().doubleValue());
                } else if (this.userManagedBean.getQ() == 3) {
                    cell = row.createCell(2);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivity3() == null ? 0.0 : sc.getSubjectivity3().doubleValue());
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivityratio3() == null ? 0.0 : sc.getSubjectivityratio3().doubleValue());
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivitypro3() == null ? 0.0 : sc.getSubjectivitypro3().doubleValue());
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjective3() == null ? 0.0 : sc.getObjective3().doubleValue());
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectiveratio3() == null ? 0.0 : sc.getObjectiveratio3().doubleValue());
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectivepro3() == null ? 0.0 : sc.getObjectivepro3().doubleValue());
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformance3() == null ? 0.0 : sc.getPerformance3().doubleValue());
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformanceratio3() == null ? 0.0 : sc.getPerformanceratio3().doubleValue());
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformancepro3() == null ? 0.0 : sc.getPerformancepro3().doubleValue());
                } else if (this.userManagedBean.getQ() == 4) {
                    cell = row.createCell(2);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivity4() == null ? 0.0 : sc.getSubjectivity4().doubleValue());
                    cell = row.createCell(3);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivityratio4() == null ? 0.0 : sc.getSubjectivityratio4().doubleValue());
                    cell = row.createCell(4);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getSubjectivitypro4() == null ? 0.0 : sc.getSubjectivitypro4().doubleValue());
                    cell = row.createCell(5);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjective4() == null ? 0.0 : sc.getObjective4().doubleValue());
                    cell = row.createCell(6);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectiveratio4() == null ? 0.0 : sc.getObjectiveratio4().doubleValue());
                    cell = row.createCell(7);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getObjectivepro4() == null ? 0.0 : sc.getObjectivepro4().doubleValue());
                    cell = row.createCell(8);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformance4() == null ? 0.0 : sc.getPerformance4().doubleValue());
                    cell = row.createCell(9);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformanceratio4() == null ? 0.0 : sc.getPerformanceratio4().doubleValue());
                    cell = row.createCell(10);
                    cell.setCellStyle(cellStyle);
                    cell.setCellValue(sc.getPerformancepro4() == null ? 0.0 : sc.getPerformancepro4().doubleValue());
                }

                i++;
            }
            os = new FileOutputStream(fileFullName);
            wb.write(os);
            this.reportViewPath = reportViewContext + fileName;
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

    
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuater() {
        return quater;
    }

    public void setQuater(int quater) {
        this.quater = quater;
    }

    public String getQueryUserId() {
        return queryUserId;
    }

    public void setQueryUserId(String queryUserId) {
        this.queryUserId = queryUserId;
    }

    public String getQueryUserName() {
        return queryUserName;
    }

    public void setQueryUserName(String queryUserName) {
        this.queryUserName = queryUserName;
    }

    public String getQueryDeptNo() {
        return queryDeptNo;
    }

    public void setQueryDeptNo(String queryDeptNo) {
        this.queryDeptNo = queryDeptNo;
    }

    public String getQueryDeptName() {
        return queryDeptName;
    }

    public void setQueryDeptName(String queryDeptName) {
        this.queryDeptName = queryDeptName;
    }

    public String getQueryQ1status() {
        return queryQ1status;
    }

    public void setQueryQ1status(String queryQ1status) {
        this.queryQ1status = queryQ1status;
    }

    public String getQueryQ2status() {
        return queryQ2status;
    }

    public void setQueryQ2status(String queryQ2status) {
        this.queryQ2status = queryQ2status;
    }

    public String getQueryQ3status() {
        return queryQ3status;
    }

    public void setQueryQ3status(String queryQ3status) {
        this.queryQ3status = queryQ3status;
    }

    public String getQueryQ4status() {
        return queryQ4status;
    }

    public void setQueryQ4status(String queryQ4status) {
        this.queryQ4status = queryQ4status;
    }

    public PersonScorecardBean getPersonScorecardBean() {
        return personScorecardBean;
    }

    public void setPersonScorecardBean(PersonScorecardBean personScorecardBean) {
        this.personScorecardBean = personScorecardBean;
    }

}
