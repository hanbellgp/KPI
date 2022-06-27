/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.efgp;

import cn.hanbell.eap.ejb.DepartmentBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Department;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.util.BaseLib;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.DependsOn;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.client.Call;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
@DependsOn({"UsersBean", "FunctionsBean", "TitleBean"})
public class WorkFlowBean implements Serializable {

    //public final String HOST_ADD = "http://oa.hanbell.com.cn";
    public final String HOST_ADD = "http://172.16.10.157";

    public final String HOST_PORT = "8086";

    @EJB
    SystemUserBean systemUserBean;

    @EJB
    DepartmentBean departmentBean;

    protected SystemUser currentUser;
    protected Department currentDept;

    public WorkFlowBean() {
        super();
    }

    public void initUserInfo(String userid) {
        currentUser = systemUserBean.findByUserId(userid);
        if (currentUser != null) {
            currentDept = departmentBean.findByDeptno(currentUser.getDeptno());
        }
    }

    public String buildXmlForEFGP(String formName, Object master, LinkedHashMap<String, List<?>> details) {

        StringBuilder xmlBuilder = new StringBuilder();
        xmlBuilder.append("<").append(formName).append(">");
        buildXmlForEFGPMaster(xmlBuilder, formName, master);
        if (details != null && !details.isEmpty()) {
            for (Map.Entry<String, List<?>> e : details.entrySet()) {
                xmlBuilder.append(buildXmlForEFGPDetails(e.getKey(), e.getValue()));
            }
        }
        
        
        xmlBuilder.append("</").append(formName).append(">");
        return xmlBuilder.toString();

    }

    protected void buildXmlForEFGPMaster(StringBuilder builder, String formName, Object master) throws RuntimeException {

        Field[] fields = master.getClass().getDeclaredFields();//不含继承自超类的属性
        for (Field f : fields) {
            try {
                f.setAccessible(true);
                if ((f.getName().equals("creator") || f.getName().equals("empl") || f.getName().equals("emply") || f.getName().equals("employee") || f.getName().endsWith("user") || f.getName().endsWith("userno") || f.getName().endsWith("User") || f.getName().endsWith("Userno")) && (!f.getName().startsWith("hdn"))) {
                    if (f.get(master) != null && !"".equals(f.get(master))) {
                        SystemUser user = systemUserBean.findByUserId(f.get(master).toString());
                        if (user == null) {
                            throw new RuntimeException();
                        }
                        builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\" label=\"").append(user.getUsername()).append("\"");
//                        builder.append("  hidden=\"").append(user.getOid()).append("\" list_hidden=\"\"");
                        builder.append("  dataType=\"").append(f.getType().getName()).append("\">").append(f.get(master)).append("</").append(f.getName()).append(">");
                    } else {
                        builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\" label=\"\" hidden=\"\" list_hidden=\"\" dataType=\"").append(f.getType().getName()).append("\" />");
                    }
                } else if ((f.getName().equals("dept") || f.getName().equals("department") || f.getName().endsWith("dept") || f.getName().endsWith("deptno") || f.getName().endsWith("Dept") || f.getName().endsWith("Deptno") || f.getName().endsWith("depno")) && (!f.getName().startsWith("hdn"))) {
                    if (f.get(master) != null && !"".equals(f.get(master))) {
                        Department dept = departmentBean.findByDeptno(f.get(master).toString());
                        if (dept == null) {
                            throw new RuntimeException();
                        }
                        builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\" label=\"").append(dept.getDept()).append("\"");
//                        builder.append("  hidden=\"").append(dept.getOid()).append("\" list_hidden=\"\"");
                        builder.append("  dataType=\"").append(f.getType().getName()).append("\">").append(f.get(master)).append("</").append(f.getName()).append(">");
                    } else {
                        builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\" label=\"\" hidden=\"\" list_hidden=\"\" dataType=\"").append(f.getType().getName()).append("\" />");
                    }
                } else if (f.getType().getName().equals("java.util.Date")) {
                    if (f.get(master) != null && !"".equals(f.get(master))) {
                        builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\" list_hidden=\"\" dataType=\"").append(f.getType().getName()).append("\" >");
                        builder.append(BaseLib.formatDate("yyyy/MM/dd", (Date) f.get(master))).append("</").append(f.getName()).append(">");
                    } else {
                        builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\" list_hidden=\"\" dataType=\"").append(f.getType().getName()).append("\" />");
                    }
                } else {
                    builder.append("<").append(f.getName()).append("  id=\"").append(f.getName()).append("\"  dataType=\"").append(f.getType().getName()).append("\" perDataProId=\"\">");
                    builder.append(f.get(master)).append("</").append(f.getName()).append(">");
                  
                }
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
        }

    }

    protected String buildXmlForEFGPDetails(String detailName, List<?> detailList) {
        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append("<").append(detailName).append(" id=\"").append(detailName).append("\"><records>");
        if (detailList != null && !detailList.isEmpty()) {
            int i = 0;
            for (Object detail : detailList) {
                buildEFGPDetail(detailBuilder, detailName, detail, i);
                i++;
            }
        }
        detailBuilder.append("</records></").append(detailName).append(">");
        return detailBuilder.toString();

    }

    protected void buildEFGPDetail(StringBuilder builder, String detailName, Object detail, int index) {

        Field[] fields = detail.getClass().getDeclaredFields();
        builder.append("<record  id = \"");
        builder.append(detailName).append("_");
        builder.append(index).append("\">");
        for (Field f : fields) {
            f.setAccessible(true);
            try {
                builder.append("<item id=\"").append(f.getName()).append("\"");
                builder.append(" dataType=\"").append(f.getType().getName()).append("\" perDataProId=\"\">").append(f.get(detail)).append("</item>");
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        builder.append("</record>");
    }

    public String invokeProcess(String host, String port, String processId, String formFieldValue, String subject) throws Exception {
        if ((getCurrentUser() == null) || (getCurrentDept() == null)) {
//            log4j.error("用户或部门不存在");
            return "401$用户或部门不存在";
        }
        return invokeProcess(host, port, processId, getCurrentUser().getUserid(), getCurrentDept().getDeptno(), formFieldValue, subject);
    }

    public String invokeProcess(String host, String port, String processId, String userId, String orgUnitId, String formFieldValue, String subject) throws Exception {

        if ((getCurrentUser() == null) || (getCurrentDept() == null)) {
//            log4j.error("用户或部门不存在");
            return "401$用户或部门不存在";
        }

        Object[] params = null;
        Object object = null;

        String formOID = null;
        String serialNo = null;
        try {
            //建立一个WebServices调用连接
            Call call = BaseLib.getAXISCall(host, port, "/NaNaWeb/services/WorkflowService?wsdl");
            //查找表单FormOID，一个流程关联多个表单时，返回值用","分开
            call.setOperationName(new QName("WorkflowService", "findFormOIDsOfProcess"));
            //转入流程代号
            params = new Object[]{processId};
            //获取表单唯一代号
            object = call.invoke(params);
            formOID = object.toString();
            //发起一个流程
            call.setOperationName(new QName("WorkflowService", "invokeProcess"));
            params = new Object[]{processId, userId, orgUnitId, formOID, formFieldValue, subject};
            object = call.invoke(params);
            serialNo = object.toString();
            return "200$" + serialNo;
        } catch (ServiceException | RemoteException e) {
            e.printStackTrace();
//            throw new RuntimeException(e);
        } finally {
            currentUser = null;
            currentDept = null;
        }
        return "200$" + serialNo;
    }

    public SystemUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(SystemUser currentUser) {
        this.currentUser = currentUser;
    }

    public Department getCurrentDept() {
        return currentDept;
    }

    public void setCurrentDept(Department currentDept) {
        this.currentDept = currentDept;
    }

}
