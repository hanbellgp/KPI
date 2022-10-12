/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.ejb.CompanyGrantBean;
import cn.hanbell.kpi.entity.CompanyGrant;
import cn.hanbell.eap.ejb.CompanyBean;
import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.Company;
import cn.hanbell.eap.entity.SystemGrantPrg;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.entity.RoleGrantModule;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "userManagedBean")
@SessionScoped
public class UserManagedBean implements Serializable {

    @EJB
    private CompanyBean companyBean;

    @EJB
    private SystemUserBean systemUserBean;

    @EJB
    private CompanyGrantBean companyGrantBean;

    private Company currentCompany;
    private SystemUser currentUser;
    private boolean status;
    private Calendar c;
    private Date baseDate;
    private int m;
    private int q;
    private int y;

    private String company;
    private String userid;
    private String mobile;
    private String email;
    private String pwd;
    private String newpwd;
    private String secpwd;

    private List<SystemGrantPrg> systemGrantPrgList;
    private List<RoleGrantModule> roleGrantDeptList;
    private List<Company> companyList;
    private String token;

    public UserManagedBean() {
        status = false;
        c = Calendar.getInstance();
    }

    @PostConstruct
    public void construct() {
        companyList = companyBean.findBySystemName("KPI");
        c.setTime(BaseLib.getDate());
        c.add(Calendar.DATE, 0 - c.get(Calendar.DATE));
        setBaseDate(c.getTime());
    }

    /**
     * 获取当前访问URL （含协议、域名、端口号[忽略80端口]、项目名）
     *
     * @param url
     * @param param
     * @return: String
     */
    // 新增一个接口
    public String sendPostLogin(String url, String param) throws IOException {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest)fc.getExternalContext().getRequest();
        // unicorn-sso
        if (request.getParameter("unicorn-sso") != null) {
            String sessionId = request.getParameter("unicorn-sso");
            // session验证地址
            String unicornEapUrl = fc.getExternalContext().getInitParameter("unicorn.sso.url");
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(unicornEapUrl + sessionId);
            try {
                CloseableHttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity httpEntity = response.getEntity();
                    JSONObject jo = new JSONObject(EntityUtils.toString(httpEntity, "UTF-8"));
                    if (jo.has("code") && jo.getString("code").equals("200") && jo.has("object")) {
                        userid = jo.getJSONObject("object").getString("username");
                        currentUser = systemUserBean.findByUserId(userid);
                        if (currentUser != null) {
                            company = currentUser.getDept().getCompany();
                            if (company == null || "".equals(company)) {
                                company = this.currentUser.getUserid().substring(0, 1);
                            }
                            // 此处加入公司授权检查
                            status = setCompanyInfo(company, userid);
                            if (!status) {
                                return "";
                            }
                            updateLoginTime();
                            status = true;
                            FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
                            return "";
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.print(ex);
            }
        }
        token = request.getQueryString();
        if (token == null || token.equals("")) {
            return "";
        }
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("accept", "*/*");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(token);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        } // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        String userNo = "";

        JSONObject jsonObject = new JSONObject(result);
        int resultTemp = jsonObject.getInt("result");// 获取成功状态
        if (resultTemp == 1) {// 成功则获取员工id
            JSONObject data = jsonObject.getJSONObject("data");
            userid = data.getString("userNo");
            SystemUser u = null;
            u = systemUserBean.findByUserId(userid);
            company = "C";
            if (u != null) {
                if ("Admin".equals(u.getUserid())) {
                    currentCompany = companyBean.findByCompany(company);
                    if (currentCompany == null) {
                        FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "请维护公司信息"));
                    }
                } else {
                    // 此处加入公司授权检查
                    CompanyGrant cg = companyGrantBean.findByCompanyAndUserid(company, userid);
                    if (cg == null) {
                        FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "您无权访问此公司别,请联系管理员"));
                        status = false;
                        return "";
                    }
                    currentCompany = companyBean.findByCompany(company);
                    if (currentCompany == null) {
                        FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "请维护公司信息"));
                        status = false;
                        return "";
                    }
                }
                currentUser = u;
                status = true;
                mobile = u.getUserid();
                updateLoginTime();

            }
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "您无权免密登入,请联系管理员"));
            status = false;
            return "login";
        }
        token = "";
        FacesContext.getCurrentInstance().getExternalContext().redirect("home.xhtml");
        return "home";
    }

    public boolean checkUser() {
        return true;
    }

    public SystemUser findById(int id) {
        return systemUserBean.findById(id);
    }

    public int getQuarter(int m) {
        switch (m) {
            case 1:
            case 2:
            case 3:
                return 1;
            case 4:
            case 5:
            case 6:
                return 2;
            case 7:
            case 8:
            case 9:
                return 3;
            case 10:
            case 11:
            case 12:
                return 4;
            default:
                return 0;
        }
    }

    public String login() {
        if (getUserid().length() == 0 || getPwd().length() == 0) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "请输入用户名和密码"));
            return "";
        }
        secpwd = BaseLib.securityMD5(getPwd());
        try {
            SystemUser u = null;
            if ("Admin".equals(userid)) {
                u = systemUserBean.findByUserIdAndPwd(userid, secpwd);
                if (u == null) {
                    FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "用户名或密码错误"));
                    status = false;
                    return "";
                }
            } else if (cn.hanbell.util.BaseLib.ADAuth("172.16.10.6:389", userid + "@hanbell.com.cn", pwd)) {
                u = systemUserBean.findByUserId(getUserid());
            }
            if (u != null) {
                if ("Admin".equals(u.getUserid())) {
                    currentCompany = companyBean.findByCompany(company);
                    if (currentCompany == null) {
                        FacesContext.getCurrentInstance().addMessage(null,
                            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "请维护公司信息"));
                    }
                } else {
                    // 此处加入公司授权检查
                    status = setCompanyInfo(company, userid);
                    if (!status) {
                        return "";
                    }
                }
                currentUser = u;
                status = true;
                mobile = u.getUserid();
                updateLoginTime();
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "用户名或密码错误"));
                status = false;
                return "";
            }
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "用户名或密码不正确！"));
            status = false;
            return "";
        }
        return "home";
    }

    private Boolean setCompanyInfo(String company, String userid) {
        CompanyGrant cg = companyGrantBean.findByCompanyAndUserid(company, userid);
        if (cg == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "您无权访问此公司别"));
            return false;
        }
        currentCompany = companyBean.findByCompany(company);
        if (currentCompany == null) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "请维护公司信息"));
            return false;
        }
        return true;
    }

    public String logout() {
        if (status) {
            currentUser = null;
            status = false;
            HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return "login";
        } else {
            return "home";
        }
    }

    public void update() {
        if (currentUser != null) {
            if (mobile != null && !mobile.equals("") && !mobile.equals(currentUser.getUserid())) {
                currentUser.setUserid(mobile);
            }
            if (email != null && !email.equals("") && !email.equals(currentUser.getEmail())) {
                currentUser.setEmail(email);
            }
            try {
                systemUserBean.update(currentUser);
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新成功"));
            } catch (Exception e) {
                FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "更新失败！"));
            }
        }
    }

    public void updateLoginTime() {
        if (currentUser != null) {
            currentUser.setLastlogin(currentUser.getNewestlogin());
            currentUser.setNewestlogin(BaseLib.getDate());
            update();
        }
    }

    public void updatePwd() {
        secpwd = BaseLib.securityMD5(getPwd());
        SystemUser u = systemUserBean.findByUserIdAndPwd(getUserid(), getSecpwd());
        if (u != null) {
            secpwd = BaseLib.securityMD5(newpwd);
            currentUser.setPassword(secpwd);
            update();
            pwd = "";
            newpwd = "";
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "更新密码成功"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Warn", "原密码错误"));
        }
    }

    /**
     * @return the currentCompany
     */
    public Company getCurrentCompany() {
        return currentCompany;
    }

    /**
     * @return the currentUser
     */
    public SystemUser getCurrentUser() {
        return currentUser;
    }

    public boolean getStatus() {
        return status;
    }

    /**
     * @return the baseDate
     */
    public Date getBaseDate() {
        return baseDate;
    }

    /**
     * @param baseDate the baseDate to set
     */
    public void setBaseDate(Date baseDate) {
        this.baseDate = baseDate;
        c.setTime(baseDate);
        m = c.get(Calendar.MONTH) + 1;
        q = getQuarter(m);
        y = c.get(Calendar.YEAR);
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid the userid to set
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /**
     * @return the pwd
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * @param pwd the pwd to set
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * @return the newpwd
     */
    public String getNewpwd() {
        return newpwd;
    }

    /**
     * @param newpwd the newpwd to set
     */
    public void setNewpwd(String newpwd) {
        this.newpwd = newpwd;
    }

    /**
     * @return the secpwd
     */
    public String getSecpwd() {
        return secpwd;
    }

    /**
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the systemGrantPrgList
     */
    public List<SystemGrantPrg> getSystemGrantPrgList() {
        return systemGrantPrgList;
    }

    /**
     * @param systemGrantPrgList the systemGrantPrgList to set
     */
    public void setSystemGrantPrgList(List<SystemGrantPrg> systemGrantPrgList) {
        this.systemGrantPrgList = systemGrantPrgList;
    }

    /**
     * @return the companyList
     */
    public List<Company> getCompanyList() {
        return companyList;
    }

    /**
     * @return the roleGrantDeptList
     */
    public List<RoleGrantModule> getRoleGrantDeptList() {
        return roleGrantDeptList;
    }

    /**
     * @param roleGrantDeptList the roleGrantDeptList to set
     */
    public void setRoleGrantDeptList(List<RoleGrantModule> roleGrantDeptList) {
        this.roleGrantDeptList = roleGrantDeptList;
    }

    /**
     * @return the m
     */
    public int getM() {
        return m;
    }

    /**
     * @return the q
     */
    public int getQ() {
        return q;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
