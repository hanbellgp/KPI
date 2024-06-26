/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.PersonScorecardBean;
import cn.hanbell.kpi.ejb.PersonScorecardDetailBean;
import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.entity.PersonScorecard;
import cn.hanbell.kpi.entity.PersonScorecardDetail;
import cn.hanbell.kpi.lazy.PersonScorecardModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "personScorecardManagedBean")
@javax.faces.bean.SessionScoped
public class PersonScorecardManagedBean extends SuperMultiBean<PersonScorecard, PersonScorecardDetail> {

    private int queryYear;
    private int queryQuarter;
    private String queryFacno;
    private String isShowAll;
    private String queryUserId;
    private String queryUserName;
    private String queryDeptNo;
    private String queryDeptName;
    private Map<String, ScorecardDetailEntity> scoreMap;
    private String tabid;
    @EJB
    private PersonScorecardBean personScorecardBean;
    @EJB
    private PersonScorecardDetailBean personScorecardDetailBean;
    @EJB
    private SystemUserBean systemUserBean;
    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private Agent1000002Bean agent1000002Bean;

    public PersonScorecardManagedBean() {
        super(PersonScorecard.class, PersonScorecardDetail.class);
    }

    @Override
    public void init() {
        superEJB = personScorecardBean;
        detailEJB = personScorecardDetailBean;
        model = new PersonScorecardModel(personScorecardBean);
        scoreMap = new HashMap<>();
        model.getSortFields().put("userid", "ASC");
        queryYear = this.userManagedBean.getY();
        queryQuarter = this.userManagedBean.getQ();
        isShowAll = "F";
        super.init();
        this.query();
    }

    @Override
    public void query() {
        if (model != null) {
            List<String> list = new ArrayList<>();
            model.getFilterFields().clear();
            if (queryYear != 0) {
                model.getFilterFields().put("year", queryYear);
            }
            if ("F".equals(isShowAll)) {
                List<SystemUser> users = systemUserBean.findByManagerIdAndOnJob(this.userManagedBean.getCurrentUser().getUserid());
                list = users.stream().map(e -> new String(e.getUserid())).collect(Collectors.toList());

            } else {
                list = this.getAllChildrenIds(this.userManagedBean.getCurrentUser().getUserid(), systemUserBean.findByStatus("N"));
                model.getFilterFields().put("userid IN ", list);
            }
            list.add(this.userManagedBean.getUserid());
            if (!list.isEmpty()) {
                model.getFilterFields().put("userid IN ", list);
            }
            if (queryUserId != null && !"".equals(queryUserId)) {
                model.getFilterFields().put("userid", queryUserId);
            }
            if (queryUserName != null && !"".equals(queryUserName)) {
                model.getFilterFields().put("username", queryUserName);
            }
            if (list == null) {
                model.getFilterFields().put("status", "");
            }
        }
    }

    @Override
    public void reset() {
        this.isShowAll = "F";
        this.queryUserId = "";
        this.queryUserName = "";
        this.queryDeptNo = "";
        this.queryDeptName = "";
        this.query();
    }

    private List<String> getAllChildrenIds(String parentId, List<SystemUser> allData) {
        ArrayList<String> childrenSiteIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(parentId) && null != allData && allData.size() > 0) {
            try {
                for (SystemUser item : allData) {
                    if (parentId.equals(item.getManagerId())) {

                        //添加子级节点
                        childrenSiteIds.add(item.getUserid());
                        //递归获取深层节点
                        childrenSiteIds.addAll(getAllChildrenIds(item.getUserid(), allData));
                    }
                }
            } catch (Exception exception) {

            }
        }
        return childrenSiteIds;
    }

    @Override
    public void handleFileUploadWhenNew(FileUploadEvent event) {
        int a = 0;
        UploadedFile file = event.getFile();
        if (file != null) {
            try {
                InputStream is = file.getInputstream();
                Workbook excel = WorkbookFactory.create(is);
                List<PersonScorecardDetail> list = new ArrayList<PersonScorecardDetail>();
                for (PersonScorecard p : this.entityList) {
                    if (!p.getPersonset().getAssessmentmethod().equals("I") && !p.getPersonset().getAssessmentmethod().equals("J")) {
                        throw new Exception("C，D，E职等不使用客观考核内容!");
                    }
                    Sheet sheet = excel.getSheet("Q1");
                    //防止导入之前的数据。
                    if (sheet != null && 1 < queryQuarter) {
                        throw new Exception("Q1考核数据已锁定，无法重复导入。请删除Q1页签");
                    }
                    if (sheet != null) {
                        list.addAll(getDataBySheet(sheet, p));
                    }
                    sheet = excel.getSheet("Q2");
                    if (sheet != null && 2 < queryQuarter) {
                        throw new Exception("Q2考核数据已锁定，无法重复导入。请删除Q2页签");
                    }
                    if (sheet != null) {
                        list.addAll(getDataBySheet(sheet, p));
                    }
                    sheet = excel.getSheet("Q3");
                    if (sheet != null && 3 < queryQuarter) {
                        throw new Exception("Q3考核数据已锁定，无法重复导入。请删除Q3页签");
                    }
                    if (sheet != null) {
                        list.addAll(getDataBySheet(sheet, p));
                    }
                    sheet = excel.getSheet("Q4");
                    if (sheet != null && 4 < queryQuarter) {
                        throw new Exception("Q4考核数据已锁定，无法重复导入。请删除Q4页签");
                    }
                    if (sheet != null) {
                        list.addAll(getDataBySheet(sheet, p));
                    }
                }
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0 && (list.get(i).getName() == null || "".equals(list.get(i).getName()))) {
                        list.get(i).setName(list.get(i - 1).getName());
                    }
                    personScorecardDetailBean.persist(list.get(i));
                }
                showInfoMsg("Info", "导入成功");
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMsg("Error", String.format("导入失败,%s", ex.getMessage()));
            }
        }
    }

    @Override
    public void print() throws Exception {
        InputStream is = null;
        try {
            String finalFilePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
            int index = finalFilePath.indexOf("WEB-INF");
            String filePath = new String(finalFilePath.substring(1, index));
            String pathString = new String(filePath.concat("rpt/"));
            File file = new File(pathString, "个人工作评分表模板.xlsx");
            is = new FileInputStream(file);
            this.fileName = "个人工作评分表模板" + BaseLib.formatDate("yyyyMMddHHmmss", BaseLib.getDate()) + ".xls";
            Workbook wb = WorkbookFactory.create(is);
            FileOutputStream os = null;
            try {
                os = new FileOutputStream(this.reportOutputPath + this.fileName);
                wb.write(os);
                this.reportViewPath = this.reportViewContext + this.fileName;
                this.preview();
            } catch (Exception var38) {
                var38.printStackTrace();
            } finally {
                try {
                    if (null != os) {
                        os.flush();
                        os.close();
                    }
                } catch (IOException var37) {
                    var37.printStackTrace();
                }
            }
        } catch (FileNotFoundException var40) {
            var40.printStackTrace();
        } catch (Exception var42) {
            var42.printStackTrace();
        }
    }

    public List<PersonScorecardDetail> getDataBySheet(Sheet sheet, PersonScorecard p) throws Exception {
        Row row;
        int quarter = Integer.valueOf(sheet.getSheetName().substring(1));
        List<PersonScorecardDetail> list = personScorecardDetailBean.findByPidAndQuarterAndType(p.getId(), quarter, "O");
        if (list != null && !list.isEmpty()) {
            personScorecardDetailBean.delete(list);
        }
        list.clear();
        if (sheet == null) {
            return list;
        }
        BigDecimal r = BigDecimal.ZERO;
        for (int i = 5; i <= sheet.getLastRowNum(); i++) {
            try {
                row = sheet.getRow(i);
                String name = BaseLib.convertExcelCell(String.class, row.getCell(1));
                BigDecimal ratio = BigDecimal.valueOf(BaseLib.convertExcelCell(Double.class, row.getCell(4)));
                r = r.add(ratio);
                PersonScorecardDetail sc = new PersonScorecardDetail(p.getId(), i - 4, "O", quarter, name, BigDecimal.ZERO, ratio);
                sc.setTarget(BaseLib.convertExcelCell(String.class, row.getCell(2)));
                sc.setStandard(BaseLib.convertExcelCell(String.class, row.getCell(3)));
                list.add(sc);
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception(String.format("第%d行文本内容解析失败，请查看", i));
            }
        }
        if (BigDecimal.ONE.compareTo(r) != 0) {
            throw new Exception("系数相加不为100%，请检查");
        }
        return list;
    }

    @Override
    public String edit(String path) {
        if ("personscorecardEdit".equals(path)) {
            if (this.entityList.size() != 1) {
                this.showErrorMsg("Error", "请选择一项");
                return "";
            }
            tabid = "sq1";
            currentEntity = this.entityList.get(0);
            if (this.currentEntity.getUserid().equals(this.userManagedBean.getUserid())) {
                this.showErrorMsg("Error", "您无编辑权限！");
                return "";
            }
            scoreMap.put("sq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            scoreMap.put("sq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            scoreMap.put("sq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            scoreMap.put("sq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            this.currentEntity = this.entityList.get(0);
            this.editedDetailList.clear();
            this.addedDetailList.clear();
            this.deletedDetailList.clear();
        }
        return super.edit(path);
    }

    public String returnPage(String path) {
        this.entityList.clear();
        return super.edit(path);
    }

    public void navigate(AjaxBehaviorEvent event) {
        try {
            if (this.entityList.size() != 1) {
                this.showErrorMsg("Error", "请选择一项");
                return;
            }
            tabid = "sq1";
            currentEntity = this.entityList.get(0);
            if (this.currentEntity.getUserid().equals(this.userManagedBean.getUserid())) {
                this.showErrorMsg("Error", "您无编辑权限！");
                return;
            }

            scoreMap.put("sq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            scoreMap.put("sq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            scoreMap.put("sq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            scoreMap.put("sq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
            scoreMap.put("aq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

            this.currentEntity = this.entityList.get(0);
            this.editedDetailList.clear();
            this.addedDetailList.clear();
            this.deletedDetailList.clear();
            FacesContext.getCurrentInstance().getExternalContext().redirect("personscorecardEdit.xhtml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String view(String path) {
        if ("personscorecardView".equals(path)) {
            if (this.entityList.size() != 1) {
                this.showErrorMsg("Error", "请选择一项");
                return "";
            }
        }
        tabid = "sq1";
        this.currentEntity = this.entityList.get(0);
        scoreMap.put("sq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
        scoreMap.put("aq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

        scoreMap.put("sq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
        scoreMap.put("aq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

        scoreMap.put("sq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
        scoreMap.put("aq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

        scoreMap.put("sq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "S"), currentEntity.getPersonset().getPersonscorecardway().getScoresubjectivityratio().doubleValue()));
        scoreMap.put("aq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "O"), currentEntity.getPersonset().getPersonscorecardway().getScoreobjectiveratio().doubleValue()));

        return super.view(path);
    }

    @Override
    public void update() {
        try {
            ScorecardDetailEntity entity = this.scoreMap.get(this.tabid);
            for (PersonScorecardDetail detail : entity.getDetail()) {
                if (detail.getScore().compareTo(detail.getRatio().multiply(BigDecimal.valueOf(100))) == 1
                        && !this.currentEntity.getPersonset().getIshundred()) {
                    throw new Exception("分数必须小于或等于比重，请调整！");
                }
                if (detail.getHunscore().compareTo(new BigDecimal(20)) < 1) {
                    throw new Exception("单项分数满分100分,请调整!");
                }
            };
            switch (this.tabid) {
                case "sq1":
                case "aq1":
                    this.currentEntity.setPerformance1(this.currentEntity.getSubjectivitypro1().add(this.currentEntity.getObjectivepro1()));
                    this.currentEntity.setPerformanceratio1(this.currentEntity.getPersonset().getPersonscorecardway().getPerformanceratio());
                    this.currentEntity.setPerformancepro1(currentEntity.getPerformance1().multiply(currentEntity.getPerformanceratio1()));
                    System.out.print(this.currentEntity.getPerformancepro1());
                    if (this.currentEntity.getPersonset().getAssessmentmethod().equals("I") || this.currentEntity.getPersonset().getAssessmentmethod().equals("J")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro1()) == -1 && BigDecimal.ZERO.compareTo(this.currentEntity.getObjectivepro1()) == -1) {
                            this.currentEntity.setStatus1("Y");
                        }
                    } else if (!this.currentEntity.getPersonset().getAssessmentmethod().equals("O")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro1()) == -1) {
                            this.currentEntity.setStatus1("Y");
                        }
                    }
                    break;
                case "sq2":
                case "aq2":
                    this.currentEntity.setPerformance2(this.currentEntity.getSubjectivitypro2().add(this.currentEntity.getObjectivepro2()));
                    this.currentEntity.setPerformanceratio2(this.currentEntity.getPersonset().getPersonscorecardway().getPerformanceratio());
                    this.currentEntity.setPerformancepro2(currentEntity.getPerformance2().multiply(currentEntity.getPerformanceratio2()));
                    if (this.currentEntity.getPersonset().getAssessmentmethod().equals("I") || this.currentEntity.getPersonset().getAssessmentmethod().equals("J")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro2()) == -1 && BigDecimal.ZERO.compareTo(this.currentEntity.getObjectivepro2()) == -1) {
                            this.currentEntity.setStatus2("Y");
                        }
                    } else if (!this.currentEntity.getPersonset().getAssessmentmethod().equals("O")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro2()) == -1) {
                            this.currentEntity.setStatus2("Y");
                        }
                    }
                    break;
                case "sq3":
                case "aq3":
                    this.currentEntity.setPerformance3(this.currentEntity.getSubjectivitypro3().add(this.currentEntity.getObjectivepro3()));
                    this.currentEntity.setPerformanceratio3(this.currentEntity.getPersonset().getPersonscorecardway().getPerformanceratio());
                    this.currentEntity.setPerformancepro3(currentEntity.getPerformance3().multiply(currentEntity.getPerformanceratio3()));
                    if (this.currentEntity.getPersonset().getAssessmentmethod().equals("I") || this.currentEntity.getPersonset().getAssessmentmethod().equals("J")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro3()) == -1 && BigDecimal.ZERO.compareTo(this.currentEntity.getObjectivepro3()) == -1) {
                            this.currentEntity.setStatus3("Y");
                        }
                    } else if (!this.currentEntity.getPersonset().getAssessmentmethod().equals("O")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro3()) == -1) {
                            this.currentEntity.setStatus3("Y");
                        }
                    }
                    break;
                case "sq4":
                case "aq4":
                    this.currentEntity.setPerformance4(this.currentEntity.getSubjectivitypro4().add(this.currentEntity.getObjectivepro4()));
                    this.currentEntity.setPerformanceratio4(this.currentEntity.getPersonset().getPersonscorecardway().getPerformanceratio());
                    this.currentEntity.setPerformancepro4(currentEntity.getPerformance4().multiply(currentEntity.getPerformanceratio4()));
                    if (this.currentEntity.getPersonset().getAssessmentmethod().equals("I") || this.currentEntity.getPersonset().getAssessmentmethod().equals("J")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro4()) == -1 && BigDecimal.ZERO.compareTo(this.currentEntity.getObjectivepro4()) == -1) {
                            this.currentEntity.setStatus4("Y");
                        }
                    } else if (!this.currentEntity.getPersonset().getAssessmentmethod().equals("O")) {
                        if (BigDecimal.ZERO.compareTo(this.currentEntity.getSubjectivitypro4()) == -1) {
                            this.currentEntity.setStatus4("Y");
                        }
                    }
                    break;
            }

            if (detailEdited != null && !detailEdited.values().isEmpty()) {
                detailEdited.remove(personScorecardDetailBean);
                detailEdited.put(personScorecardDetailBean, entity.getDetail());
            }

            super.update();
            this.personScorecardBean.sendmsg(this.currentEntity, this.queryQuarter);
            this.showInfoMsg("Info", "保存成功");

        } catch (Exception e) {
            e.printStackTrace();
            this.showInfoMsg("Info", String.format("更新失败，%s", e.getMessage()));
        }
    }

    public void setScore(AjaxBehaviorEvent value) {
        UIComponent component = value.getComponent();
        DataTable table = (DataTable) component.getParent().getParent();
        this.currentDetail = (PersonScorecardDetail) table.getRowData(); // 这里就是当前行的数据
        BigDecimal b = BigDecimal.ZERO;
        ScorecardDetailEntity entity = this.scoreMap.get(this.tabid);
        for (PersonScorecardDetail bean : entity.getDetail()) {
            if (this.currentDetail.getId() == bean.getId()) {
                bean.setScore(this.currentDetail.getHunscore().multiply(this.currentDetail.getRatio()));
            }
            b = b.add(bean.getScore());
        }
        switch (this.tabid) {
            case "sq1":
                this.currentEntity.setSubjectivity1(b);
                this.currentEntity.setSubjectivityratio1(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setSubjectivitypro1(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq1":
                this.currentEntity.setObjective1(b);
                this.currentEntity.setObjectiveratio1(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setObjectivepro1(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq2":
                this.currentEntity.setSubjectivity2(b);
                this.currentEntity.setSubjectivityratio2(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setSubjectivitypro2(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq2":
                this.currentEntity.setObjective2(b);
                this.currentEntity.setObjectiveratio2(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setObjectivepro2(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq3":
                this.currentEntity.setSubjectivity3(b);
                this.currentEntity.setSubjectivityratio3(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setSubjectivitypro3(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq3":
                this.currentEntity.setObjective3(b);
                this.currentEntity.setObjectiveratio3(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setObjectivepro3(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq4":
                this.currentEntity.setSubjectivity4(b);
                this.currentEntity.setSubjectivityratio4(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setSubjectivitypro4(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq4":
                this.currentEntity.setObjective4(b);
                this.currentEntity.setObjectiveratio4(new BigDecimal(this.scoreMap.get(tabid).getQuarter()));
                this.currentEntity.setObjectivepro4(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
        }

    }

    public void clearlist() {
        this.entityList.clear();
    }

    public Map<String, ScorecardDetailEntity> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<String, ScorecardDetailEntity> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public void tabchange(TabChangeEvent event) {
        this.tabid = event.getTab().getId();
    }

    public int getQueryYear() {
        return queryYear;
    }

    public void setQueryYear(int queryYear) {
        this.queryYear = queryYear;
    }

    public int getQueryQuarter() {
        return queryQuarter;
    }

    public void setQueryQuarter(int queryQuarter) {
        this.queryQuarter = queryQuarter;
    }

    public String getQueryFacno() {
        return queryFacno;
    }

    public void setQueryFacno(String queryFacno) {
        this.queryFacno = queryFacno;
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

    public String getIsShowAll() {
        return isShowAll;
    }

    public void setIsShowAll(String isShowAll) {
        this.isShowAll = isShowAll;
    }

    public String getTabid() {
        return tabid;
    }

    public void setTabid(String tabid) {
        this.tabid = tabid;
    }

    public class ScorecardDetailEntity {

        private List<PersonScorecardDetail> detail;
        private double quarter;

        public List<PersonScorecardDetail> getDetail() {
            return detail;
        }

        public void setDetail(List<PersonScorecardDetail> detail) {
            this.detail = detail;
        }

        public double getQuarter() {
            return quarter;
        }

        public void setQuarter(double quarter) {
            this.quarter = quarter;
        }

        public ScorecardDetailEntity(List<PersonScorecardDetail> detail, double quarter) {
            this.detail = detail;
            this.quarter = quarter;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + Objects.hashCode(this.detail);
            hash = 37 * hash + (int) (Double.doubleToLongBits(this.quarter) ^ (Double.doubleToLongBits(this.quarter) >>> 32));
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ScorecardDetailEntity other = (ScorecardDetailEntity) obj;
            if (!Objects.equals(this.detail, other.detail)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "ScorecardDetailEntity{" + "detail=" + detail + ", quarter=" + quarter + '}';
        }

    }
}
