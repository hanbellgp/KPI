/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 *
 * @author C0160
 */
public class MailNotify {

    protected String smtpHost = "172.16.10.18";
    protected int smptPort = 25;
    protected HtmlEmail email;

    protected MailNotification notification;

    public MailNotify() {

    }

    protected void init() {
        if (email == null) {
            email = new HtmlEmail();
            email.setHostName(smtpHost);
            email.setSmtpPort(smptPort);
        }
    }

    public void send() {
        try {
            init();
            email.setFrom(notification.mailFrom, "sys", "utf8");
            email.setSubject(notification.mailSubject);
            email.setHtmlMsg(notification.mailContent);
            email.setCharset("utf8");
            if (notification.getTo() != null && !notification.to.isEmpty()) {
                for (String t : notification.getTo()) {
                    email.addTo(t);
                }
            }
            if (notification.getCc() != null && !notification.cc.isEmpty()) {
                for (String c : notification.getCc()) {
                    email.addCc(c);
                }
            }
            if (notification.getBcc() != null && !notification.bcc.isEmpty()) {
                for (String b : notification.getBcc()) {
                    email.addBcc(b);
                }
            }
            if (notification.getAttachments() != null) {
                for (File f : notification.getAttachments()) {
                    email.attach(f);
                }
            }
            email.send();
        } catch (EmailException ex) {
            Logger.getLogger(MailNotify.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendMail(MailNotification notification) {
        this.notification = notification;
        send();
    }

}
