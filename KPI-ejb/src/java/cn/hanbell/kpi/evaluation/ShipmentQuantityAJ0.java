/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
public class ShipmentQuantityAJ0 extends ShipmentQuantity {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //o转出，理解为销货
        //i转入，理解为销退
        BigDecimal trnqyo = BigDecimal.ZERO;
        BigDecimal trnqyoh = BigDecimal.ZERO;
        BigDecimal trnqyi = BigDecimal.ZERO;
        BigDecimal trnqyih = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        //调出
        sb.append("SELECT ISNULL(sum(trnqy1),0) from invdta d,invhad h WHERE d.facno=h.facno AND d.trno = h.trno AND  d.trtype='IAC' AND d.wareh='W01' ");
        sb.append(" AND h.trno in (SELECT DISTINCT a.trno FROM invdta b,invhad a WHERE b.facno=a.facno AND b.trno = a.trno AND b.trtype='IAC' AND b.wareh  in('EM01','EW01') AND b.iocode='1' AND a.status<>'W' ");
        //#ITCLS CHANGE TODO #
        sb.append(" AND b.itcls IN ('3876','3879','3880','3886','3889','3890','3976','3979','3980') ");
        sb.append(" AND a.facno='C' AND year(a.trdate)=${y} and month(a.trdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and a.trdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and a.trdate= '${d}' ");
                break;
            default:
                sb.append(" and a.trdate<= '${d}' ");
        }
        sb.append(" )");
        sb.append(" AND h.facno='C' AND year(h.trdate)=${y} and month(h.trdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.trdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.trdate= '${d}' ");
                break;
            default:
                sb.append(" and h.trdate<= '${d}' ");
        }
        String sqlo = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        //历史
        String sqloh = sqlo.replace("invdta", "invdtah").replace("invhad", "invhadh");

        sb.setLength(0);
        //调入
        sb.append("SELECT ISNULL(sum(trnqy1),0) from invdta d,invhad h WHERE d.facno=h.facno AND d.trno = h.trno AND  d.trtype='IAC' AND d.wareh  in('EM01','EW01') ");
        sb.append(" AND h.trno in (SELECT DISTINCT a.trno FROM invdta b,invhad a WHERE b.facno=a.facno AND b.trno = a.trno AND b.trtype='IAC' AND b.wareh='W01' AND b.iocode='1' AND a.status<>'W' ");
        //#ITCLS CHANGE TODO #
        sb.append(" AND b.itcls IN ('3876','3879','3880','3886','3889','3890','3976','3979','3980') ");
        sb.append(" AND a.facno='C' AND year(a.trdate)=${y} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and month(a.trdate)= ${m} and a.trdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and month(a.trdate)= ${m} and a.trdate= '${d}' ");
                break;
            default:
                sb.append(" and month(a.trdate)= ${m} and a.trdate<= '${d}' ");
        }
        sb.append(" )");
        sb.append(" AND h.facno='C' AND year(h.trdate)=${y} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and month(h.trdate)= ${m} and h.trdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and month(h.trdate)= ${m} and h.trdate= '${d}' ");
                break;
            default:
                sb.append(" and month(h.trdate)= ${m} and h.trdate<= '${d}' ");
        }
        String sqli = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        //历史
        String sqlih = sqli.replace("invdta", "invdtah").replace("invhad", "invhadh");

        superEJB.setCompany("C");
        Query qo = superEJB.getEntityManager().createNativeQuery(sqlo);
        Query qoh = superEJB.getEntityManager().createNativeQuery(sqloh);
        Query qi = superEJB.getEntityManager().createNativeQuery(sqli);
        Query qih = superEJB.getEntityManager().createNativeQuery(sqlih);
        try {
            Object o1 = qo.getSingleResult();
            Object o2 = qoh.getSingleResult();
            Object i1 = qi.getSingleResult();
            Object i2 = qih.getSingleResult();
            trnqyo = (BigDecimal) o1;
            trnqyoh = (BigDecimal) o2;
            trnqyi = (BigDecimal) i1;
            trnqyih = (BigDecimal) i2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return trnqyo.add(trnqyoh).subtract(trnqyi).subtract(trnqyih);
    }

}
