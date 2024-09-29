/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.PersonDeptPercentageBean;
import cn.hanbell.kpi.ejb.PersonScorecardBean;
import cn.hanbell.kpi.ejb.PersonScorecardDetailBean;
import cn.hanbell.kpi.ejb.PersonScorecardWayBean;
import cn.hanbell.kpi.ejb.PersonSetBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.entity.PersonScorecard;
import cn.hanbell.kpi.entity.PersonScorecardDetail;
import cn.hanbell.kpi.entity.PersonScorecardWay;
import cn.hanbell.kpi.entity.PersonSet;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.lazy.PersonSetModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import com.lightshell.comm.BaseLib;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
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
@ManagedBean(name = "personSetManagedBean")
@SessionScoped
public class PersonSetManagedBean extends SuperSingleBean<PersonSet> {

    public PersonSetManagedBean() {
        super(PersonSet.class);
    }
    @EJB
    public PersonSetBean personSetBean;
    @EJB
    public SystemUserBean systemUserBean;
    @EJB
    public DepartmentBean departmentBean;
    @EJB
    public ScorecardBean scorecardBean;
    @EJB
    public PersonScorecardBean personScorecardBean;
    @EJB
    public PersonScorecardDetailBean personScorecardDetailBean;
    @EJB
    public PersonDeptPercentageBean personDeptPercentageBean;
    @EJB
    public PersonScorecardWayBean personScorecardWayBean;
    
    
    @EJB
    private Agent1000002Bean agent1000002Bean;
    private String facno;
    public List<String> facnos;
    public List<String> parentDeptnos;
    public List<SystemUser> updateUsers;
    private List<Department> childDepts;
    private List<PersonScorecardWay> methods;

    private Date loginDate;
    private String queryUserId;
    private String queryUserName;
    private String queryDeptNo;
    private String queryDeptName;
    private String queryOfficialRank;
    private String queryJobCategory;
    private String queryDuties;
    private String queryAdministrative;
    private String queryStatus;
    private int importYear;

    @Override
    public void init() {
        facnos = new ArrayList<String>();
        updateUsers = new ArrayList<SystemUser>();
        facno = userManagedBean.getCompany();
        importYear = userManagedBean.getY();
        this.queryAdministrative = "ALL";
        this.queryStatus = "N";
        switch (facno) {
            case "C":
                facnos.add("C");
                facnos.add("K");
                facnos.add("E");
                break;
            case "H":
                facnos.add("H");
                facnos.add("Y");
                break;
        }
        loginDate = userManagedBean.getBaseDate();
        this.superEJB = personSetBean;
        this.model = new PersonSetModel(superEJB);


        methods=personScorecardWayBean.findAll();
       
        this.query();
        super.init();
    }

    @Override
    public void query() {
        super.query();
        if (model != null) {
            model.getFilterFields().clear();
            if (!facnos.isEmpty()) {
                model.getFilterFields().put("facno IN  ", facnos);
            }
            if (!"".equals(queryUserId) && queryUserId != null) {
                model.getFilterFields().put("userid", queryUserId);
            }
            if (!"".equals(queryUserName) && queryUserName != null) {
                model.getFilterFields().put("username", queryUserName);
            }
            if (!"".equals(queryDeptNo) && queryDeptNo != null) {
                model.getFilterFields().put("deptno", queryDeptNo);
            }
            if (!"".equals(queryDeptName) && queryDeptName != null) {
                model.getFilterFields().put("deptname", queryDeptName);
            }
            if (!"".equals(queryOfficialRank) && queryOfficialRank != null) {
                model.getFilterFields().put("officialrank", queryOfficialRank);
            }
            if (!"".equals(queryJobCategory) && queryJobCategory != null) {
                model.getFilterFields().put("jobcategory", queryJobCategory);
            }
            if (!"".equals(queryDuties) && queryDuties != null) {
                model.getFilterFields().put("duties", queryDuties);
            }
            if ("true".equals(queryAdministrative)) {
                model.getFilterFields().put("isadministrative", true);
            }
            if ("false".equals(queryAdministrative)) {
                model.getFilterFields().put("isadministrative", false);
            }
            if (!"ALL".equals(queryStatus) && queryStatus != null) {
                model.getFilterFields().put("status", queryStatus);
            }
        }
        model.getSortFields().put("userid", "ASC");
    }

    @Override
    public String edit(String path) {
        if ("personsetEdit".equals(path)) {
            if (this.entityList.size() != 1) {
                this.showErrorMsg("Error", "请选择一项");
                return "";
            }
            this.currentEntity = this.entityList.get(0);
        }
        return super.edit(path); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String view(String path) {
        if ("personsetView".equals(path)) {
            if (this.entityList.size() != 1) {
                this.showErrorMsg("Error", "请选择一项");
                return "";
            }
            this.currentEntity = this.entityList.get(0);
        }
        return super.view(path); //To change body of generated methods, choose Tools | Templates.
    }

    public void hrPrint() throws Exception {
        List<PersonSet> personList = getPerson();
        if (personList == null || personList.isEmpty()) {
            return;
        }
        OutputStream os = null;
        try {
            HSSFWorkbook wb=this.personToWorkbook(personList);
            fileName = "HR考核人员" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
            String fileFullName = reportOutputPath + fileName;
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

    @Override
    public void print() throws Exception {
         List<PersonSet> personList = personSetBean.findAll();
        if (personList == null || personList.isEmpty()) {
            return;
        }
        OutputStream os = null;
        try {
            HSSFWorkbook wb=this.personToWorkbook(personList);
            fileName = "考核人员" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
            String fileFullName = reportOutputPath + fileName;
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
    
    

    public HSSFWorkbook personToWorkbook(List<PersonSet> personList) {
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
        Sheet sheet = wb.createSheet("sheet1");
        Cell cell;
        Row row;
        row = sheet.createRow(0);
        cell = row.createCell(0);
        cell.setCellValue("公司别");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(1);
        cell.setCellValue("工号");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(2);
        cell.setCellValue("姓名");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(3);
        cell.setCellValue("部门");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(4);
        cell.setCellValue("部门名称");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(5);
        cell.setCellValue("员工性质");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(6);
        cell.setCellValue("职等");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(7);
        cell.setCellValue("职等名称");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(8);
        cell.setCellValue("岗位类别");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(9);
        cell.setCellValue("职务");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(10);
        cell.setCellValue("是否行政职");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(11);
        cell.setCellValue("行政职系数");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(12);
        cell.setCellValue("是否超过100分");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(13);
        cell.setCellValue("计算方式");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(14);
        cell.setCellValue("课级考核表");
        cell.setCellStyle(cellStyle);
        cell = row.createCell(15);
        cell.setCellValue("部级考核表");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(16);
        cell.setCellValue("奖金发放比率");
        cell.setCellStyle(cellStyle);

        cell = row.createCell(17);
        cell.setCellValue("在职状态");
        cell.setCellStyle(cellStyle);
        
          int i = 1;
            for (PersonSet p : personList) {
                row = sheet.createRow(i);

                cell = row.createCell(0);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getFacno());

                cell = row.createCell(1);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getUserid());

                cell = row.createCell(2);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getUsername());

                cell = row.createCell(3);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getDeptno());

                cell = row.createCell(4);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getDeptname());

                cell = row.createCell(5);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getType() != null ? p.getType() : "");

                cell = row.createCell(6);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getOfficialrank() != null ? p.getOfficialrank() : "");

                cell = row.createCell(7);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getOfficialrankdesc() != null ? p.getOfficialrankdesc() : "");

                cell = row.createCell(8);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getJobcategory() != null ? p.getJobcategory() : "");

                cell = row.createCell(9);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getDuties() != null ? p.getDuties() : "");

                cell = row.createCell(10);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getIsadministrative() != null && p.getIsadministrative() ? "T" : "F");
                
                  cell = row.createCell(11);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getCoefficient());

                cell = row.createCell(12);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getIshundred()!= null && p.getIshundred() ? "T" : "F");

                cell = row.createCell(13);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getAssessmentmethod() != null ? p.getAssessmentmethod() : "");

                cell = row.createCell(14);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getClassscorecard() != null ? p.getClassscorecard() : "");

                cell = row.createCell(15);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getDepartmentscorecard() != null ? p.getDepartmentscorecard() : "");

                cell = row.createCell(16);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getPercentage() != null ? p.getPercentage().doubleValue() : 0.0);

                cell = row.createCell(17);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(p.getStatus() != null ? p.getStatus() : "");

                i++;
            }
        return wb;
    }

    @Override
    protected boolean doBeforeUpdate() throws Exception {
        return super.doBeforeUpdate() && this.doBeforeMerge(this.currentEntity);
    }

    public boolean doBeforeMerge(PersonSet personset) {
        if(personset.getAssessmentmethod()==null || "".equals(personset.getAssessmentmethod()==null)){
              this.showErrorMsg("Error", String.format("%s考核方式不能为空!!", personset.getUsername()));
              return false;
        }
        List<String> list= methods.stream().map(e -> e.getFormid()).collect(Collectors.toList());
        if(!list.contains(personset.getAssessmentmethod())){
              this.showErrorMsg("Error", String.format("%s考核方式代号不合理!!",personset.getUsername()));
              return false;
        }
        if ("I".equals(personset.getAssessmentmethod())) {
            if (!"幕僚".equals(personset.getType()) && !"营销".equals(personset.getType())&& !"技术".equals(personset.getType())) {
                this.showErrorMsg("Error", String.format("%s的考核类型错误，I只能用于幕僚员工!!", personset.getUsername()));
                return false;
            }
        }
        if ("J".equals(personset.getAssessmentmethod())) {
            if (!"现场".equals(personset.getType())) {
                this.showErrorMsg("Error", String.format("%s的考核类型错误，J只能用于现场员工!!", personset.getUsername()));
                return false;
            }
        }
        if ("K".equals(personset.getAssessmentmethod())) {
            if (personset.getClassscorecard() == null || "".equals(personset.getClassscorecard())
                    || personset.getDepartmentscorecard() == null || "".equals(personset.getDepartmentscorecard())) {
                this.showErrorMsg("Error", String.format("%s的课级考核表和部级考核表不能为空",personset.getUsername()));
                return false;
            }
            if (personset.getIsadministrative()) {
                this.showErrorMsg("Error",String.format("%的是否行政值错误！",personset.getUsername()));
                return false;
            }
        }
        if ("L".equals(personset.getAssessmentmethod())) {
             if (personset.getClassscorecard() == null || "".equals(personset.getClassscorecard())
                    || personset.getDepartmentscorecard() == null || "".equals(personset.getDepartmentscorecard())) {
                this.showErrorMsg("Error", String.format("%s的课级考核表和部级考核表不能为空",personset.getUsername()));
                return false;
            }
            if (!personset.getIsadministrative()) {
                this.showErrorMsg("Error",String.format("%的是否行政值错误！",personset.getUsername()));
                return false;
            }
        }
        if ("M".equals(personset.getAssessmentmethod())) {
            if (personset.getDepartmentscorecard() == null || "".equals(personset.getDepartmentscorecard())) {
                this.showErrorMsg("Error", String.format("%s的部级考核表不能为空",personset.getUsername()));
                return false;
            }
            if (personset.getIsadministrative()) {
                this.showErrorMsg("Error", String.format("%的是否行政值错误！",personset.getUsername()));
                return false;
            }
        }

        if ("N".equals(personset.getAssessmentmethod())) {
            if (personset.getDepartmentscorecard() == null || "".equals(personset.getDepartmentscorecard())) {
                this.showErrorMsg("Error",String.format("%s的部级考核表不能为空",personset.getUsername()));
                return false;
            }
            if (!personset.getIsadministrative()) {
                this.showErrorMsg("Error", String.format("%的是否行政值错误！",personset.getUsername()));
                return false;
            }
        }
        return true;
    }

    public void createSubjectiveAssessment() {
        List<PersonScorecardDetail> addDetail = null;
        for (PersonSet p : this.entityList) {
            if (p.getAssessmentmethod() == null) {
                this.showErrorMsg("Error", p.getUsername() + "没有计算方式，请检查！");
                return;
            }
            if (p.getAssessmentmethod() == null || "".equals(p.getAssessmentmethod())) {
                this.showErrorMsg("Error", p.getUsername() + "没有计算方式，请检查！");
            }
            PersonScorecard sc = personScorecardBean.findByUseridAndYear(p.getUserid(), this.userManagedBean.getY());
            if (sc == null) {
                sc = new PersonScorecard(p.getUserid(), p.getUsername(), this.userManagedBean.getY());
                sc.setStatus(String.valueOf(this.userManagedBean.getQ()));
                personScorecardBean.persist(sc);
                sc = personScorecardBean.findByUseridAndYear(p.getUserid(), this.userManagedBean.getY());
            } else {
                sc.setStatus(String.valueOf(this.userManagedBean.getQ()));
                personScorecardBean.update(sc);
                List<PersonScorecardDetail> detailList = personScorecardDetailBean.findByPidAndQuarterAndType(sc.getId(), this.userManagedBean.getQ(), "S");
                if (detailList != null) {
                    personScorecardDetailBean.delete(detailList);
                }
            }
            addDetail = personScorecardDetailBean.getSubjectiveAssessmentDetail(sc.getId(), p.getAssessmentmethod(), this.userManagedBean.getQ());
            for (PersonScorecardDetail entity : addDetail) {
                personScorecardDetailBean.persist(entity);
            }
        }
        this.showInfoMsg("Info", "添加成功");
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        List<PersonSet> addlist = personSetBean.findAll();
        personSetBean.delete(addlist);
        addlist.clear();

        int a = 0;
        UploadedFile file = event.getFile();
        if (file != null) {
            try {
                InputStream is = file.getInputstream();
                Workbook excel = WorkbookFactory.create(is);
                Sheet sheet = excel.getSheetAt(0);
                Row row;
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    a = i;
                    row = sheet.getRow(i);
                    PersonSet p = new PersonSet(cellToVlaue(row.getCell(0)), cellToVlaue(row.getCell(1)), cellToVlaue(row.getCell(2)),
                            cellToVlaue(row.getCell(3)), cellToVlaue(row.getCell(4)), cellToVlaue(row.getCell(6)),
                            cellToVlaue(row.getCell(7)), cellToVlaue(row.getCell(8)), cellToVlaue(row.getCell(5)),
                            cellToVlaue(row.getCell(9)), cellToVlaue(row.getCell(17)), userManagedBean.getCurrentUser().getUsername());
                    p.setIsadministrative("T".equals(cellToVlaue(row.getCell(10))) ? true : false);
                    p.setCoefficient(Double.valueOf(cellToVlaue(row.getCell(11))));
                    p.setIshundred("T".equals(cellToVlaue(row.getCell(12))) ? true : false);
                    p.setAssessmentmethod(cellToVlaue(row.getCell(13)));
                    p.setClassscorecard(cellToVlaue(row.getCell(14)));
                    p.setDepartmentscorecard(cellToVlaue(row.getCell(15)));
                    p.setPercentage(new BigDecimal(cellToVlaue(row.getCell(16))).multiply(BigDecimal.TEN).multiply(BigDecimal.TEN));
                    addlist.add(p);
                    if (!doBeforeMerge(p)) {
                        return;
                    }
                }
                a = 0;
                //导入数据
                if (!addlist.isEmpty()) {
                    for (PersonSet p : addlist) {
                        personSetBean.persist(p);
                    }
                }
                this.showInfoMsg("Info", "导入成功");
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMsg("Error", "导入失败,找不到文件或格式错误----" + ex.toString());
                showErrorMsg("Error", "第" + (a+1) + "行附近栏位发生错误");
            }
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
                //时间格式
                short format = cell.getCellStyle().getDataFormat();
                if (DateUtil.isCellDateFormatted(cell)) {
                    if (format == 31) {
                        Date date = DateUtil.getJavaDate(d);
                        return BaseLib.formatDate("MM/dd", date);
                    } else {
                        throw new Exception("时间格式异常，请使用XXXX年XX月XX日格式！");
                    }
                }
                //整数去掉小数点
                if (d == (int) d) {
                    return String.valueOf((int) d);
                }
                return String.valueOf(cell.getNumericCellValue());
            case 1:
                return cell.getStringCellValue();
            case 2:
                return cell.getCellFormula();
            case 3:
                return "0";
            case 4:
                return String.valueOf(cell.getBooleanCellValue());
            case 5:
                return String.valueOf(cell.getErrorCellValue());
        }
        return "";
    }

    public List<PersonSet> getPerson() {
        List<PersonSet> personList = new ArrayList<>();

        for (String d : facnos) {
            updateUsers.clear();
            Department department = departmentBean.findByConpanyAndPid(d, 1);
            if (department == null) {
                this.showErrorMsg("Error", d + "未找到对应部门编号");
            }
            addUser(department);
            if (!updateUsers.isEmpty()) {
                //把需要更新，新增的人员放入对应集合中
                for (SystemUser user : updateUsers) {
                    PersonSet person = new PersonSet(d, user.getUserid(), user.getUsername(), user.getDeptno(), user.getDept().getDept(),
                            user.getLevelId(), user.getDecisionLevelName(), user.getPosition(), user.getType(), user.getJob(), user.getStatus(), userManagedBean.getCurrentUser().getUsername());
                    person.setCoefficient(1.0);
                    personList.add(person);

                }
            }
        }

        //透过考核表部门先给人员加入课级考核表，部级考核表。
        Map<String, Object> filters = new HashMap<>();
        filters.put("company IN  ", facnos);
        filters.put("seq", this.userManagedBean.getY());
        filters.put("template", false);
        filters.put("lvl", "D");
        List<Scorecard> deptScorecards = this.scorecardBean.findByFilters(filters);
        //部级数据
        for (Scorecard entity : deptScorecards) {
            updateUsers.clear();
            Department dept = departmentBean.findByDeptno(entity.getDeptno());
            addUser(dept);
            for (PersonSet sc : personList) {
                for (SystemUser su : updateUsers) {
                    if (sc.getUserid().equals(su.getUserid())) {
                        sc.setDepartmentscorecard(entity.getName());
                        break;
                    }
                }
            }
        }
        filters.remove("lvl");
        filters.put("lvl", "S");
        List<Scorecard> classScorecards = this.scorecardBean.findByFilters(filters);
        for (Scorecard entity : classScorecards) {
            updateUsers.clear();
            Department dept = departmentBean.findByDeptno(entity.getDeptno());
            addUser(dept);
            for (PersonSet sc : personList) {
                for (SystemUser su : updateUsers) {
                    if (sc.getUserid().equals(su.getUserid())) {
                        sc.setClassscorecard(entity.getName());
                        break;
                    }
                }
            }
        }
        /**
         * 设置考核内容 J AB通用 70%客观+30%主管 K 课级非行政 (50%课级考核+50%部级考核)60%+主管考评40% L 课级行政
         * (50%课级考核+50%部级考核)80%+主管考评20% M 部级非行政 60%部级考核+主管考评40% N 部级行政
         * 80%部级考核+主管考评20% O 不考核
         */
        //保存人员
        String regex = "[ABCDE]|(A-)|(A\\+)|(B-)|(B\\+)";
        for (PersonSet person : personList) {
            //离职人员不考核
            if (person.getStatus() == null || "X".equals(person.getStatus())) {
                person.setAssessmentmethod("O");
                continue;
            }
            //CA临时工，劳务工，门卫，兴塔门卫等不考核
            if (person.getUserid() == null || person.getUserid().startsWith("CA") || person.getUserid().startsWith("CL") || person.getUserid().startsWith("CM") || person.getUserid().startsWith("M")
                    || person.getUserid().startsWith("CG001")|| person.getUserid().startsWith("P032")
                    ) {
                person.setAssessmentmethod("O");
                continue;
            }
            if ("".equals(person.getOfficialrank()) || person.getOfficialrank() == null) {
                showErrorMsg("Error", person.getUsername() + "没有职等！");
                return null;
            }
            if (!person.getOfficialrank().matches(regex)) {
                showErrorMsg("Error", person.getUsername() + "职等不符合要求，请检查！");
                return null;
            }
            String assessmentlevel = person.getAssessmentlevel();
            person.setIsadministrative(person.getOfficialrank(), person.getOfficialrankdesc());

            if ("A".equals(assessmentlevel) || "B".equals(assessmentlevel)) {
                if (person.getType() == null) {
                    person.setAssessmentmethod("");
                    continue;
                }
                switch (person.getType()) {
                    case "技术":
                    case "幕僚":
                    case "营销":
                        person.setAssessmentmethod("I");
                        break;
                    case "现场":
                        person.setAssessmentmethod("J");
                        break;
                    default:
                        person.setAssessmentmethod("");
                        break;

                }
                continue;
            }
            if ("C".equals(assessmentlevel)) {
                if (person.getDepartmentscorecard() != null && !"".equals(person.getDepartmentscorecard()) && person.getClassscorecard() != null && !"".equals(person.getClassscorecard())) {
                    if (person.getIsadministrative()) {
                        person.setAssessmentmethod("L");
                    } else {
                        person.setAssessmentmethod("K");
                    }
                } else if (person.getDepartmentscorecard() != null && !"".equals(person.getDepartmentscorecard())) {
                    if (person.getIsadministrative()) {
                        person.setAssessmentmethod("N");
                    } else {
                        person.setAssessmentmethod("M");
                    }
                }
                continue;
            }
            if ("D".equals(assessmentlevel)) {
                if (person.getDepartmentscorecard() != null && !"".equals(person.getDepartmentscorecard()) && person.getClassscorecard() != null && !"".equals(person.getClassscorecard())) {
                    if (person.getIsadministrative()) {
                        person.setAssessmentmethod("L");
                    } else {
                        person.setAssessmentmethod("K");
                    }
                } else if (person.getDepartmentscorecard() != null && !"".equals(person.getDepartmentscorecard())) {
                    if (person.getIsadministrative()) {
                        person.setAssessmentmethod("N");
                    } else {
                        person.setAssessmentmethod("M");
                    }
                }
                continue;
            }
            if(person.getUserid().equals("C0003")){
                    System.out.print(fc);
            }
            if ("E".equals(assessmentlevel) && (person.getOfficialrankdesc().contains("董事长级") || person.getOfficialrankdesc().contains("副总经理级") 
                    || person.getOfficialrankdesc().contains("副董级")|| person.getOfficialrankdesc().contains("总经理级"))) {
                person.setAssessmentmethod("O");
                continue;
            } else if ("E".equals(assessmentlevel)) {
                if (person.getDepartmentscorecard() != null && !"".equals(person.getDepartmentscorecard()) && person.getClassscorecard() != null && !"".equals(person.getClassscorecard())) {
                    if (person.getIsadministrative()) {
                        person.setAssessmentmethod("L");
                    } else {
                        person.setAssessmentmethod("K");
                    }
                } else if (person.getDepartmentscorecard() != null && !"".equals(person.getDepartmentscorecard())) {
                    if (person.getIsadministrative()) {
                        person.setAssessmentmethod("N");
                    } else {
                        person.setAssessmentmethod("M");
                    }
                }
                continue;
            }
        }
        return personList;
    }

    /**
     *
     * @param personScorecard 人员分数
     * @param isMandatoryScore AB需要使用使用强制分数，C及C以上直接用绩效考核分数
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public BigDecimal getScoreCardScore(PersonScorecard personScorecard, boolean isMandatoryScore) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field f = null;
        Method method = null;
        BigDecimal a = BigDecimal.ZERO;
        BigDecimal b = BigDecimal.ZERO;
        if (personScorecard.getPersonset().getClassscorecard() != null && !"".equals(personScorecard.getPersonset().getClassscorecard())) {
            Scorecard entity = scorecardBean.findByNameAndSeqAndCompany(personScorecard.getPersonset().getClassscorecard(), this.userManagedBean.getY(), this.userManagedBean.getCompany());
            if (isMandatoryScore) {
                f = entity.getClass().getDeclaredField(scorecardBean.getColumn("lq", this.userManagedBean.getQ()));
            } else {
                f = entity.getClass().getDeclaredField(scorecardBean.getColumn("sq", this.userManagedBean.getQ()));
            }
            f.setAccessible(true);
            a = ((BigDecimal) f.get(entity));
        }
        if (personScorecard.getPersonset().getDepartmentscorecard() != null && !"".equals(personScorecard.getPersonset().getDepartmentscorecard())) {
            Scorecard entity = scorecardBean.findByNameAndSeqAndCompany(personScorecard.getPersonset().getDepartmentscorecard(), this.userManagedBean.getY(), this.userManagedBean.getCompany());
            if (isMandatoryScore) {
                f = entity.getClass().getDeclaredField(scorecardBean.getColumn("lq", this.userManagedBean.getQ()));
            } else {
                f = entity.getClass().getDeclaredField(scorecardBean.getColumn("sq", this.userManagedBean.getQ()));
            }
            f.setAccessible(true);
            b = ((BigDecimal) f.get(entity));
        }
        if (personScorecard.getPersonset().getDepartmentscorecard() != null && !"".equals(personScorecard.getPersonset().getDepartmentscorecard())
                && personScorecard.getPersonset().getClassscorecard() != null && !"".equals(personScorecard.getPersonset().getClassscorecard())) {
            return a.add(b).divide(BigDecimal.valueOf(2), 2, BigDecimal.ROUND_HALF_UP);
        } else {
            return a.add(b);
        }
    }

    private void addUser(Department dept) {
        if (dept != null) {
            childDepts = departmentBean.findByPId(dept.getId());
            if (childDepts != null && !childDepts.isEmpty()) {
                for (Department e : childDepts) {
                    if (e.getStatus().equals("X")) {
                        // 已停用部门无需载入
                        continue;
                    }
                    addUser(e);
                }
            }
            updateUsers.addAll(systemUserBean.findByDeptnoAndOnJob(dept.getDeptno()));
        }

    }

    public String getFacno() {
        return facno;
    }

    public void setFacno(String facno) {
        this.facno = facno;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
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

    public String getQueryOfficialRank() {
        return queryOfficialRank;
    }

    public void setQueryOfficialRank(String queryOfficialRank) {
        this.queryOfficialRank = queryOfficialRank;
    }

    public String getQueryJobCategory() {
        return queryJobCategory;
    }

    public void setQueryJobCategory(String queryJobCategory) {
        this.queryJobCategory = queryJobCategory;
    }

    public String getQueryDuties() {
        return queryDuties;
    }

    public void setQueryDuties(String queryDuties) {
        this.queryDuties = queryDuties;
    }

    public String getQueryAdministrative() {
        return queryAdministrative;
    }

    public void setQueryAdministrative(String queryAdministrative) {
        this.queryAdministrative = queryAdministrative;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public int getImportYear() {
        return importYear;
    }

    public void setImportYear(int importYear) {
        this.importYear = importYear;
    }

    public List<PersonScorecardWay> getMethods() {
        return methods;
    }

    public void setMothods(List<PersonScorecardWay> methods) {
        this.methods = methods;
    }

}
