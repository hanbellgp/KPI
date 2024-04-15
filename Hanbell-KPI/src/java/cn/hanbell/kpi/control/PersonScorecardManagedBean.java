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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
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
            scoreMap.put("sq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "S"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "S")));
            scoreMap.put("aq1", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 1, "O"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "O")));

            scoreMap.put("sq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "S"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "S")));
            scoreMap.put("aq2", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 2, "O"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "O")));

            scoreMap.put("sq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "S"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "S")));
            scoreMap.put("aq3", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 3, "O"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "O")));

            scoreMap.put("sq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "S"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "S")));
            scoreMap.put("aq4", new ScorecardDetailEntity(personScorecardDetailBean.findByPidAndQuarterAndType(currentEntity.getId(), 4, "O"), getScoreRatio(currentEntity.getPersonset().getAssessmentmethod(), "O")));

            this.currentEntity = this.entityList.get(0);
            this.editedDetailList.clear();
            this.addedDetailList.clear();
            this.deletedDetailList.clear();
        }
        return super.edit(path);
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
        return super.view(path);
    }

    @Override
    public void update() {
        try {
            ScorecardDetailEntity entity = this.scoreMap.get(this.tabid);
            for (PersonScorecardDetail detail : entity.getDetail()) {
                if (detail.getScore().compareTo(detail.getRatio().multiply(BigDecimal.valueOf(100))) == 1
                        && !"采购员".equals(this.currentEntity.getPersonset().getDuties())
                        && !"客服员".equals(this.currentEntity.getPersonset().getDuties())
                        && !"销售跟单员".equals(this.currentEntity.getPersonset().getDuties())
                        && !"生管员".equals(this.currentEntity.getPersonset().getDuties())) {
                    throw new Exception("分数必须小于或等于比重，请调整！");
                }
            };

            if (detailEdited != null && !detailEdited.values().isEmpty()) {
                detailEdited.remove(personScorecardDetailBean);
                detailEdited.put(personScorecardDetailBean, entity.getDetail());
            }

            agent1000002Bean.initConfiguration();
            StringBuilder msg = new StringBuilder();
            msg.append("### 个人绩效考核:").append(BaseLib.formatDate("yyyy/MM/dd hh:mm", new Date()));
            BigDecimal porobjquarter = (BigDecimal) personScorecardBean.getScore(this.currentEntity, "porobjquarter", this.userManagedBean.getQ());
            BigDecimal porsubquarter = (BigDecimal) personScorecardBean.getScore(this.currentEntity, "porsubquarter", this.userManagedBean.getQ());
            BigDecimal subquarter = (BigDecimal) personScorecardBean.getScore(this.currentEntity, "subquarter", this.userManagedBean.getQ());
            String msgstatus = (String) personScorecardBean.getScore(this.currentEntity, "msgstatus", this.userManagedBean.getQ());

            msg.append("\\n").append("");
            msg.append("\\n").append(">**考核详情**");
            msg.append("\\n").append(">考核人：").append(this.currentEntity.getUsername()).append("(").append(this.currentEntity.getUserid()).append(")");
            msg.append("\\n").append(">部门名称：<font color=\"warning\">").append(this.currentEntity.getPersonset().getDeptname()).append("</font>");
            msg.append("\\n").append(">岗位：<font color=\"info\">").append(this.currentEntity.getPersonset().getDuties()).append("</font>");
            msg.append("\\n").append(">职等：<font color=\"info\">").append(this.currentEntity.getPersonset().getOfficialrank()).append("</font>");
            if (currentEntity.getPersonset().getAssessmentmethod().equals("I") || currentEntity.getPersonset().getAssessmentmethod().equals("J")) {
                if (porobjquarter.compareTo(BigDecimal.ZERO) != 0 && porsubquarter.compareTo(BigDecimal.ZERO) != 0 && !"V".equals(msgstatus)) {
                    msg.append("\\n").append(">主观分数：<font color=\"comment\">").append(porsubquarter).append("</font>");
                    msg.append("\\n").append(">客观分数：<font color=\"comment\">").append(porobjquarter).append("</font>");
                    msg.append("\\n").append(">合计分数：<font color=\"comment\">").append(porsubquarter.add(porobjquarter)).append("</font>");
                    msg.append("\\n").append("");
                    msg.append("\\n").append("><font color=\"warning\">若您对考核结果存在任何异议或疑问，请及时前往系统进行调整与反馈:</font>");
                    //发送消息
                    SystemUser u1 = this.systemUserBean.findByUserId(this.currentEntity.getUserid());
                    SystemUser u2 = this.systemUserBean.findByUserId(u1.getManagerId());
                    System.out.println(u2.getManagerId());
                    if (!"C0002".equals(u2.getManagerId())) {
                        agent1000002Bean.sendMsgToUser(u2.getManagerId(), "markdown", msg.toString());
                    }
                    currentEntity.setMsgstatus("V", this.userManagedBean.getQ());
                }
            } else if (!currentEntity.getPersonset().getOfficialrank().equals("E")) {
                if (subquarter.compareTo(BigDecimal.ZERO) != 0 && !"V".equals(msgstatus)) {
                    msg.append("\\n").append(">主观分数：<font color=\"comment\">").append(subquarter).append("</font>");
                    msg.append("\\n").append("");
                    msg.append("\\n").append("><font color=\"warning\">若您对考核结果存在任何异议或疑问，请及时前往系统进行调整与反馈:</font>");
                    //发送消息
                    SystemUser u1 = this.systemUserBean.findByUserId(this.currentEntity.getUserid());
                    SystemUser u2 = this.systemUserBean.findByUserId(u1.getManagerId());
                    System.out.println(u2.getManagerId());
                    if (!"C0002".equals(u2.getManagerId())) {
                        agent1000002Bean.sendMsgToUser(u2.getManagerId(), "markdown", msg.toString());
                    }
                    currentEntity.setMsgstatus("V", this.userManagedBean.getQ());
                }

            }
            super.update();
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
                this.currentEntity.setSubquarter1(b);
                this.currentEntity.setPorsubquarter1(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq1":
                this.currentEntity.setObjquarter1(b);
                this.currentEntity.setPorobjquarter1(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq2":
                this.currentEntity.setSubquarter2(b);
                this.currentEntity.setPorsubquarter2(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq2":
                this.currentEntity.setObjquarter2(b);
                this.currentEntity.setPorobjquarter2(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq3":
                this.currentEntity.setSubquarter3(b);
                this.currentEntity.setPorsubquarter3(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq3":
                this.currentEntity.setObjquarter3(b);
                this.currentEntity.setPorobjquarter3(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq4":
                this.currentEntity.setSubquarter4(b);
                this.currentEntity.setPorsubquarter4(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq4":
                this.currentEntity.setObjquarter4(b);
                this.currentEntity.setPorobjquarter4(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
        }

    }

    public void setQuarter() {
        BigDecimal b = BigDecimal.ZERO;
        ScorecardDetailEntity entity = this.scoreMap.get(this.tabid);
        for (PersonScorecardDetail bean : entity.getDetail()) {
            b = b.add(bean.getScore());
        }
        switch (this.tabid) {
            case "sq1":
                this.currentEntity.setSubquarter1(b);
                this.currentEntity.setPorsubquarter1(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq1":
                this.currentEntity.setObjquarter1(b);
                this.currentEntity.setPorobjquarter1(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq2":
                this.currentEntity.setSubquarter2(b);
                this.currentEntity.setPorsubquarter2(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq2":
                this.currentEntity.setObjquarter2(b);
                this.currentEntity.setPorobjquarter2(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq3":
                this.currentEntity.setSubquarter3(b);
                this.currentEntity.setPorsubquarter3(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq3":
                this.currentEntity.setObjquarter3(b);
                this.currentEntity.setPorobjquarter3(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "sq4":
                this.currentEntity.setSubquarter4(b);
                this.currentEntity.setPorsubquarter4(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
            case "aq4":
                this.currentEntity.setObjquarter4(b);
                this.currentEntity.setPorobjquarter4(b.multiply(BigDecimal.valueOf(entity.getQuarter())));
                break;
        }
    }

    public double getScoreRatio(String assessmentmethod, String type) {
        switch (assessmentmethod) {
            case "I":
                if (type.equals("S")) {
                    return 0.4;
                } else if (type.equals("O")) {
                    return 0.6;
                }
                break;
            case "J":
                if (type.startsWith("S")) {
                    return 0.3;
                } else if (type.startsWith("O")) {
                    return 0.7;
                }
                break;
            case "K":
                if (type.startsWith("S")) {
                    return 0.4;
                } else if (type.startsWith("O")) {
                    return 0.6;
                }
                break;
            case "L":
                if (type.startsWith("S")) {
                    return 0.2;
                } else if (type.startsWith("O")) {
                    return 0.8;
                }
                break;
            case "M":
                if (type.startsWith("S")) {
                    return 0.4;
                } else if (type.startsWith("O")) {
                    return 0.6;
                }
                break;
            case "N":
                if (type.startsWith("S")) {
                    return 0.2;
                } else if (type.startsWith("O")) {
                    return 0.8;
                }
                break;
        }
        return 0.0;
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
