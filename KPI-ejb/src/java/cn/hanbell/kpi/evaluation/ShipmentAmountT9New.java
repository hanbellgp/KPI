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
public class ShipmentAmountT9New extends ShipmentAmount {

    public ShipmentAmountT9New() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("decode", "2");
        queryParams.put("n_code_CD", " LIKE 'WX%' ");
        queryParams.put("n_code_DD", " ='01' ");//00是整机-01是零件-02是后处理
    }

//    //互工数据
//    public BigDecimal getQTValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
//        //获得查询参数
//        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
//        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
//        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
//
//        BigDecimal shp1 = BigDecimal.ZERO;
//        BigDecimal bshp1 = BigDecimal.ZERO;
//        StringBuilder sb = new StringBuilder();
//        sb.append("select isnull(sum((d.shpamts * h.ratio)/(h.taxrate + 1)),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W'");
//        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and h.depno in ('1T100','1A000') ");
//        sb.append(" and h.facno='${facno}' ");
//        if (!"".equals(decode)) {
//            sb.append(" and h.decode ='").append(decode).append("' ");
//        }
//        sb.append(" and d.n_code_DA ='QT'");
//        if (!"".equals(n_code_CD)) {
//            sb.append(" and d.n_code_CD ").append(n_code_CD);
//        }
//        sb.append(" and d.n_code_DD IN ('00','01') ");
//        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
//        switch (type) {
//            case 2:
//                //月
//                sb.append(" and h.shpdate<= '${d}' ");
//                break;
//            case 5:
//                //日
//                sb.append(" and h.shpdate= '${d}' ");
//                break;
//            default:
//                sb.append(" and h.shpdate<= '${d}' ");
//        }
//        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
//                .replace("${facno}", facno);
//
//        sb.setLength(0);
//        sb.append("select isnull(sum((d.bakamts * h.ratio)/(h.taxrate + 1)),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
//        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and h.depno in ('1T100','1A000') ");
//        sb.append(" and h.facno='${facno}' ");
//        if (!"".equals(decode)) {
//            sb.append(" and h.decode ='").append(decode).append("' ");
//        }
//        sb.append(" and d.n_code_DA ='QT'");
//        if (!"".equals(n_code_CD)) {
//            sb.append(" and d.n_code_CD ").append(n_code_CD);
//        }
//        sb.append(" and d.n_code_DD IN ('00','01') ");
//        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
//        switch (type) {
//            case 2:
//                //月
//                sb.append(" and h.bakdate<= '${d}' ");
//                break;
//            case 5:
//                //日
//                sb.append(" and h.bakdate= '${d}' ");
//                break;
//            default:
//                sb.append(" and h.bakdate<= '${d}' ");
//        }
//        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
//                .replace("${facno}", facno);
//
//        superEJB.setCompany(facno);
//        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
//        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
//        try {
//            Object o1 = query1.getSingleResult();
//            Object o2 = query2.getSingleResult();
//            shp1 = (BigDecimal) o1;
//            bshp1 = (BigDecimal) o2;
//        } catch (Exception ex) {
//            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return shp1.subtract(bshp1);
//    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        BigDecimal shpamts = BigDecimal.ZERO;
        BigDecimal bshpamts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum((d.shpamts * h.ratio)/(h.taxrate + 1)),0) from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and h.facno='${facno}' ");
//        if (!"".equals(decode)) {
//            sb.append(" and h.decode ='").append(decode).append("' ");
//        }
        sb.append(" and d.n_code_DA <> 'QT' ");
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
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
        sb.append("select isnull(sum((d.bakamts * h.ratio)/(h.taxrate + 1)),0) from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and h.facno='${facno}' ");
//        if (!"".equals(decode)) {
//            sb.append(" and h.decode ='").append(decode).append("' ");
//        }
        sb.append(" and d.n_code_DA <> 'QT' ");
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
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
            shpamts = (BigDecimal) o1;
            bshpamts = (BigDecimal) o2;
            //qtshpamts = getQTValue(y, m, d, type, map);
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shpamts.subtract(bshpamts);
    }

}
