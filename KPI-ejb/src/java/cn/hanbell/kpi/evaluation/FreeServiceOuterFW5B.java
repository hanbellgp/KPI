/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

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
public class FreeServiceOuterFW5B extends FreeServiceOuterFW {

    public FreeServiceOuterFW5B() {
        super();
        queryParams.put("facno", "K");
//        queryParams.put("hmark1", "='HD' ");
        queryParams.put("hmark2", " ='OH' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal o1 = BigDecimal.ZERO;
        BigDecimal o2 = BigDecimal.ZERO;
        BigDecimal o3 = BigDecimal.ZERO;
        BigDecimal ZJComer = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        //领料
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE  facno ='K' AND t.prono='1' and  t.trtype='IAP' AND depno<>'K0000009' AND t.iocode='2' ");
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String iap2sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //退料
        sb.append(" select isnull(-sum(t.tramt),0) FROM invtrnh t WHERE facno ='K' AND t.prono='1' and  t.trtype='IAP' AND depno<>'K0000009' AND t.iocode='1' ");
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String iap1sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(iap2sql);
        Query query2 = superEJB.getEntityManager().createNativeQuery(iap1sql);
        try {
            Object p1 = query1.getSingleResult();
            Object p2 = query2.getSingleResult();

            o1 = this.getSuperValue(y, m, d, type, map);
            o2 = (BigDecimal) p1;
            o3 = (BigDecimal) p2;
            queryParams.remove("facno");
            queryParams.put("facno", "E");
            //ZJComer
            ZJComer = this.getSuperValue(y, m, d, type, queryParams);
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o1.add(o2).subtract(o3).add(ZJComer);
    }

    public BigDecimal getSuperValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        String hmark2 = map.get("hmark2") != null ? map.get("hmark2").toString() : "";

        //领料INV310
        BigDecimal iaf = BigDecimal.ZERO;
        //退料
        BigDecimal iag = BigDecimal.ZERO;
        //借出无法归还部分
        BigDecimal z08 = BigDecimal.ZERO;

        //领料SQL
        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE  facno ='${facno}' AND t.prono='1' and  t.trtype='IAF' AND t.rescode IN ('1001','1003','1013') ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String iafsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //退料SQL
        sb.append(" select isnull(-sum(t.tramt),0) FROM invtrnh t WHERE facno ='${facno}' AND t.prono='1' and  t.trtype='IAG' AND t.rescode IN ('1002','1004','1014') ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String iagsql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        //借出无法归还部分SQL
        sb.append(" select isnull(sum(t.tramt),0) FROM invtrnh t WHERE   facno ='${facno}' AND t.prono='1' and  t.trtype='IAB'  AND t.rescode IN ('Z08') ");
        if (!"".equals(hmark1)) {
            sb.append(" and t.hmark1 ").append(hmark1);
        }
        if (!"".equals(hmark2)) {
            sb.append(" and t.hmark2 ").append(hmark2);
        }
        sb.append(" AND year(t.trdate) = ${y} and month(t.trdate)= ${m} ");

        String z08sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(iafsql);
        Query query2 = superEJB.getEntityManager().createNativeQuery(iagsql);
        Query query3 = superEJB.getEntityManager().createNativeQuery(z08sql);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            Object o3 = query3.getSingleResult();
            iaf = (BigDecimal) o1;
            iag = (BigDecimal) o2;
            z08 = (BigDecimal) o3;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return iaf.add(iag).add(z08);
    }

}
