/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn.hanbell.kpi.mail;

import cn.hanbell.kpi.comm.MailNotification;
import cn.hanbell.kpi.ejb.erp.BscGroupVBHSaleOrderBean;
import cn.hanbell.kpi.ejb.erp.BscGroupVBHShipmentBean;
import cn.hanbell.kpi.ejb.erp.BscGroupVHSaleOrderBean;
import cn.hanbell.kpi.ejb.erp.BscGroupVHShipmentBean;
import cn.hanbell.kpi.entity.Indicator;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class BscGroupVNShipmentMailBean extends MailNotification {

    @EJB
    private BscGroupVHShipmentBean bscGroupVHShipmentBean;
    @EJB
    private BscGroupVHSaleOrderBean bscGroupVHSaleOrderBean;

    @EJB
    private BscGroupVBHShipmentBean bscGroupVBHShipmentBean;
    @EJB
    private BscGroupVBHSaleOrderBean bscGroupVBHSaleOrderBean;

    public BscGroupVNShipmentMailBean() {

    }

    @Override
    public void init() {
        this.mailSetting = mailSettingBean.findByMailClazz(this.getClass().getName());
        super.init();
    }

    @Override
    protected String getHtmlTable(List<Indicator> indicatorList, int y, int m, Date d, boolean needsum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getHtmlTableRow(Indicator indicator, int y, int m, Date d) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected String getMailBody() {
        try {
            //隆安厂出货
            bscGroupVHShipmentBean.updataShpimentActualValue(y, m, d);
            log4j.info("End Execute Job updataShpimentActualValue");
            //隆安厂订单
            bscGroupVHSaleOrderBean.updataSaleOrderActualValue(y, m, d);
            log4j.info("End Execute Job updataSaleOrderActualValue");
            //隆安厂服务
            bscGroupVHShipmentBean.updataServerActualValue(y, m, d);
            log4j.info("End Execute Job updataServerActualValue");

            //北宁厂出货
            bscGroupVBHShipmentBean.updataShpimentActualValue(y, m, d);
            log4j.info("End Execute Job updataShpimentActualValue");
            //北宁厂订单
            bscGroupVBHSaleOrderBean.updataSaleOrderActualValue(y, m, d);
            log4j.info("End Execute Job updataSaleOrderActualValue");
            //北宁厂服务
            bscGroupVBHShipmentBean.updataServerActualValue(y, m, d);
            log4j.info("End Execute Job updataServerActualValue");

            return "越南数据更新集团报表数据成功";
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.toString();
        }
    }

}
