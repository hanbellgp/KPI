/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.InvamountBUBean;
import cn.hanbell.kpi.entity.Indicator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InvamountBUMailBean extends MailNotification {

    @EJB
    private InvamountBUBean invamountBUBean;

    public InvamountBUMailBean() {

    }
    
    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getMailBody() {
        StringBuilder sb = new StringBuilder();
        boolean flag;
        try {
            flag = invamountBUBean.updateInvamountBusinessUnit(y, m);
            if (flag) {
                sb.append("<div style=\"text-align:center;width:100%\">资料更新成功！</div>");
            } else {
                sb.append("<div style=\"text-align:center;width:100%\">资料更新失败，请重新手动执行！</div>");
            }
            sb.append(getMailFooter());
            return sb.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "资料写入KPI失败！";
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        return " ";
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        return " ";
    }

}
