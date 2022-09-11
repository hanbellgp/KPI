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
 * @author C1879
 */
public class SalesOrderQuantityVN extends SalesOrderQuantity {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal cdrqy1 = BigDecimal.ZERO;
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : ""; //机型别
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : ""; //服务OR整机

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(d.cdrqy1),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and d.drecsta not in ('98','99') ");
        sb.append(" and h.cusno not in ('SSD00328') and  h.facno='${facno}' ");
        sb.append(" and ( d.itnbr in (select itnbr from invmas where itcls in('RC2','AA','3176','3576','3579','3580','3679','3705','3707','3716','3733','3735','3738','3766','3776','3780','3801','3802','3806','3833','3835','3838','3866','3879','3880','3A76','3A79','CDU','7110')) )");
        if (!"".equals(hmark1)) {
            sb.append(" and h.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and h.hmark2 ").append(hmark2);
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                sb.append(" and h.recdate<= '${d}' ");
                break;
            case 5:
                sb.append(" and h.recdate= '${d}' ");
                break;
            default:
                sb.append(" and h.recdate<= '${d}' ");
        }

        String cdrdmas = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            cdrqy1 = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cdrqy1;
    }

}
