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
public class EmployeeShipmentQuantityAA extends EmployeeShipmentQuantity {

    public EmployeeShipmentQuantityAA() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_DC", " <>'SDS' ");
        queryParams.put("n_code_DD", " IN ('00') ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String userid = map.get("userid") != null ? map.get("userid").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        BigDecimal shpqy1 = BigDecimal.ZERO;
        BigDecimal bshpqy1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(d.shpqy1),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno  and h.houtsta<>'W'  ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
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
        sb.append("select isnull(sum(d.bshpqy1),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W'  ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
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
            shpqy1 = (BigDecimal) o1;
            bshpqy1 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shpqy1.subtract(bshpqy1);
    }

}
