/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.ScorecardBean;
import cn.hanbell.kpi.ejb.ScorecardContentBean;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardContent;
import cn.hanbell.kpi.lazy.ScorecardContentModel;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.BaseLib;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "scorecardDetailPrintManagedBean")
@javax.faces.bean.ViewScoped
public class ScorecardDetailPrintManagedBean extends SuperSingleBean<ScorecardContent> {

    @EJB
    private ScorecardBean scorecardBean;
    @EJB
    private ScorecardContentBean scorecardContentBean;
    protected Calendar c;
    private Scorecard scorecard;

    private Date printDate;

    public ScorecardDetailPrintManagedBean() {
        super(ScorecardContent.class);
        c = Calendar.getInstance();
    }

    @Override
    public void init() {
        c.setTime(userManagedBean.getBaseDate());
        superEJB = scorecardContentBean;
        model = new ScorecardContentModel(scorecardContentBean, this.userManagedBean);
        model.getSortFields().put("seq", "ASC");
        model.getSortFields().put("deptno", "ASC");
        model.getFilterFields().put("parent.seq", c.get(Calendar.YEAR));
        this.printDate = c.getTime();
        super.init();
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
        init();
        super.construct();
    }

    @Override
    public void print() throws Exception {
         Calendar cal = Calendar.getInstance();
         cal.setTime(this.printDate);
        List<Scorecard> list = scorecardBean.findByStatusAndYear("V",cal.get(Calendar.YEAR));

        if (list == null || list.isEmpty()) {
            showMsg(FacesMessage.SEVERITY_WARN, "Warn", "没有可打印数据");
            return;
        }
        //先删除output的所有文件
        File output = new File(this.reportOutputPath);
        File[] xlscs = output.listFiles();
        for (int i = 0; i < xlscs.length; i++) {
            if (xlscs[i].isFile()) {
                xlscs[i].delete();
            }
        }
        for (Scorecard s : list) {
            HashMap<String, Object> reportParams = new HashMap<>();
            reportParams.put("company", userManagedBean.getCurrentCompany().getName());
            reportParams.put("companyFullName", userManagedBean.getCurrentCompany().getFullname());
            reportParams.put("JNDIName", "java:global/KPI/KPI-ejb/ScorecardBean!cn.hanbell.kpi.ejb.ScorecardBean");
            reportParams.put("seq", s.getSeq());
            reportParams.put("deptname", s.getDeptname());
            reportParams.put("id", s.getId());
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
            this.fileName = s.getName() + BaseLib.formatDate("yyyyMMddHHmmss", this.getDate()) + "." + "xls";
            String reportName = "";
            if (userManagedBean.getQ() == 1) {
                reportName = reportPath + "scorecarddetail1.rptdesign";
            } else if (userManagedBean.getQ() == 2) {
                reportName = reportPath + "scorecarddetail2.rptdesign";
            } else if (userManagedBean.getQ() == 3) {
                reportName = reportPath + "scorecarddetail3.rptdesign";
            } else if (userManagedBean.getQ() == 4) {
                reportName = reportPath + "scorecarddetail4.rptdesign";
            }
            String outputName = reportOutputPath + this.fileName;
            this.reportViewPath = reportViewContext + this.fileName;
            try {
                reportClassLoader = Class.forName("cn.hanbell.kpi.rpt.ScorecardReport").getClassLoader();
                // 初始配置
                this.reportInitAndConfig();
                // 生成报表
                this.reportRunAndOutput(reportName, reportParams, outputName, "xls", null);
            } catch (Exception ex) {
                throw ex;
            }

        }
        String filename = BaseLib.formatDate("yyyyMMddHHmmss", new Date()) + ".zip";
        FileOutputStream fos1 = new FileOutputStream(new File(reportOutputPath.replace("output/", "") + filename));
        File files = new File(reportOutputPath);
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(fos1);
            compress(files, zos, files.getName(), true);
            this.reportViewPath = reportViewContext.replace("output/", "") + filename;
            this.preview();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void compress(File sourceFile, ZipOutputStream zos, String name,
            boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[2 * 1024];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }

    public Scorecard getScorecard() {
        return scorecard;
    }

    public void setScorecard(Scorecard scorecard) {
        this.scorecard = scorecard;
    }

    public Date getPrintDate() {
        return printDate;
    }

    public void setPrintDate(Date printDate) {
        this.printDate = printDate;
    }

}
