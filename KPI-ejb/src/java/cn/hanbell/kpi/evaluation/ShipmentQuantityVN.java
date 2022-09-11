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
public class ShipmentQuantityVN extends ShipmentQuantity {

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : ""; //机型别
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : ""; //服务OR整机
        BigDecimal shpqy1 = BigDecimal.ZERO;
        BigDecimal bshpqy1 = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(d.shpqy1),0) from  cdrhmas e,cdrdta d inner join cdrhad h on  d.facno=h.facno  and d.shpno=h.shpno  ");
        sb.append(" where h.houtsta <> 'W'  and e.cdrno=d.cdrno   ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and e.hmark2 ").append(hmark2);
        }

        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and h.shpdate<= '${d}' ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        sb.setLength(0);
        sb.append(" select isnull(sum(d.bshpqy1),0) from cdrhmas e,cdrbhad h right join cdrbdta d on h.bakno=d.bakno ");
        sb.append(" where h.baksta <> 'W'  and e.cdrno=d.cdrno and  e.hmark2='ZJ' ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and e.hmark2 ").append(hmark2);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.bakdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.bakdate= '${d}' ");
                break;
            default:
                sb.append(" and h.bakdate<= '${d}' ");
        }
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shpqy1 = BigDecimal.valueOf(Double.valueOf(o1.toString()));
            bshpqy1 = BigDecimal.valueOf(Double.valueOf(o2.toString()));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shpqy1.subtract(bshpqy1);
    }

}
