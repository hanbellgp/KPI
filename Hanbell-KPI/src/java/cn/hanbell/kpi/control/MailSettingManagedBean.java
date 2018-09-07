/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.comm.MailNotify;
import cn.hanbell.kpi.ejb.MailRecipientBean;
import cn.hanbell.kpi.ejb.MailSettingBean;
import cn.hanbell.kpi.entity.MailRecipient;
import cn.hanbell.kpi.entity.MailSetting;
import cn.hanbell.kpi.lazy.MailSettingModel;
import cn.hanbell.kpi.web.FormMultiBean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.naming.InitialContext;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "mailSettingManagedBean")
@SessionScoped
public class MailSettingManagedBean extends FormMultiBean<MailSetting, MailRecipient> {

    @EJB
    private MailSettingBean mailSettingBean;

    @EJB
    private MailRecipientBean mailRecipientBean;

    protected MailNotification mailBean;

    private String recipient;

    public MailSettingManagedBean() {
        super(MailSetting.class, MailRecipient.class);
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
        newEntity.setFormdate(getDate());
        setCurrentEntity(newEntity);
    }

    @Override
    public void createDetail() {
        super.createDetail();
        newDetail.setKind("to");
    }

    @Override
    public void handleDialogReturnWhenDetailEdit(SelectEvent event) {
        if (event.getObject() != null && currentDetail != null) {
            Object o = event.getObject();
            SystemUser user = (SystemUser) o;
            currentDetail.setUserid(user.getUserid());
            currentDetail.setUsername(user.getUsername());
            currentDetail.setMailadd(user.getEmail());
            if (currentDetail.getMailadd() == null) {
                currentDetail.setMailadd(user.getUserid() + "@hanbell.com.cn");
            }
        }
    }

    @Override
    public void init() {
        superEJB = mailSettingBean;
        detailEJB = mailRecipientBean;
        model = new MailSettingModel(mailSettingBean);
        super.init();
    }

    public void sendMail() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                setMailBean(currentEntity.getMailEJB());
                if (mailBean != null) {
                    mailBean.init();
                    mailBean.setD(queryDateBegin);
                    mailBean.setMailContent();
                    mailBean.setMailSubject();
                    mailBean.notify(new MailNotify());
                    showInfoMsg("Info", "报表邮件发送成功");
                } else {
                    showErrorMsg("Error", "MailEJB设置错误");
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(MailSettingManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                showErrorMsg("Error", ex.toString());
            } catch (Exception ex) {
                Logger.getLogger(MailSettingManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    public void testMail() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                setMailBean(currentEntity.getMailEJB());
                if (mailBean != null && recipient != null && !"".equals(recipient)) {
                    mailBean.init();
                    //清除设定的收件人,重设为测试人员
                    mailBean.getTo().clear();
                    mailBean.getCc().clear();
                    mailBean.getBcc().clear();
                    if (recipient.contains("@")) {
                        mailBean.getTo().add(recipient);
                    } else {
                        mailBean.getTo().add(recipient + "@hanbell.com.cn");
                    }
                    mailBean.setD(queryDateBegin);
                    mailBean.setMailContent();
                    mailBean.setMailSubject();
                    mailBean.notify(new MailNotify());
                    showInfoMsg("Info", "报表邮件发送成功");
                } else {
                    showErrorMsg("Error", "MailEJB或测试人员设置错误");
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(MailSettingManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                showErrorMsg("Error", ex.toString());
            } catch (Exception ex) {
                Logger.getLogger(MailSettingManagedBean.class.getName()).log(Level.SEVERE, null, ex);
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    public void setMailBean(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        mailBean = (MailNotification) objRef;
    }

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

}
