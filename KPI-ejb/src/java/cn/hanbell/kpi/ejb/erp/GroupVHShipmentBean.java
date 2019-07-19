/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.erp;


import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.erp.BscGroupShipment;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class GroupVHShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public GroupVHShipmentBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataActualValue(int y, int m, Date d) {
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='R' ");
        List<BscGroupShipment> resultData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='L' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='DR' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='CDU' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='A' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='SDS' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='P' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(a.getShpnum().add(b.getShpnum()));
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(a.getOrdnum().add(b.getOrdnum()));
                    a.setOrdamts(a.getOrdamts().add(b.getOrdamts()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where shptype <> '21' and facno='V' and year(soday)=" + y + " and month(soday) = " + m).executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //出货
    protected List<BscGroupShipment> getShipment(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        List<BscGroupShipment> temp;
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
//        sb.append(" select h.shpdate,isnull(sum(d.shpqy1),0) from  cdrhmas e,cdrdta d inner join cdrhad h on  d.facno=h.facno  and d.shpno=h.shpno  ");
//        sb.append(" where h.houtsta <> 'W'  and e.cdrno=d.cdrno    and  e.hmark2='ZJ' ");
//        sb.append(" and h.facno='${facno}' ");
//        if (!"".equals(hmark1)) {
//            sb.append(" and e.hmark1 ").append(hmark1);
//        }
//        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate<='${d}' group by h.shpdate ");
//        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
//                .replace("${facno}", facno);
//
//        sb.setLength(0);
//        sb.append(" select h.bakdate,-isnull(sum(d.bshpqy1),0) from cdrhmas e,cdrbhad h right join cdrbdta d on h.bakno=d.bakno ");
//        sb.append(" where h.baksta <> 'W'  and e.cdrno=d.cdrno and  e.hmark2='ZJ' ");
//        sb.append(" and h.facno='${facno}' ");
//        if (!"".equals(hmark1)) {
//            sb.append(" and e.hmark1 ").append(hmark1);
//        }
//        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} and h.bakdate<='${d}' group by h.bakdate ");
        sb.append(" select a.soday,sum(num) from ( ");
        sb.append(" select h.facno,h.shpdate as soday,e.hmark1,sum(d.shpqy1) as num  from  cdrhmas e,cdrdta d inner join cdrhad h on  d.facno=h.facno  and d.shpno=h.shpno ");
        sb.append(" where h.houtsta <> 'W'  and e.cdrno=d.cdrno    and  e.hmark2='ZJ' ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate<='${d}' ");
        sb.append(" GROUP BY h.facno,h.shpdate,e.hmark1 ");
        sb.append(" union all  ");
        sb.append(" select h.facno,h.bakdate as soday,e.hmark1, -sum(d.bshpqy1) as num from cdrhmas e,cdrbhad h right join cdrbdta d on h.bakno=d.bakno ");
        sb.append(" where h.baksta <> 'W'  and e.cdrno=d.cdrno and  e.hmark2='ZJ' ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} and h.bakdate<='${d}' ");
        sb.append(" group by h.facno,h.bakdate,e.hmark1 ");
        sb.append(" ) a group by a.soday ");
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        try {
            List shpResult = query1.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if (hmark1.contains("R") && !hmark1.contains("DR")) {
                protype = "压缩机R系列";
                protypeno = "R";
                shptype = "1";
            } else if (hmark1.contains("L")) {
                protype = "压缩机L系列";
                protypeno = "L";
                shptype = "1";
            } else if (hmark1.contains("DR")) {
                protype = "压缩机DORIN";
                protypeno = "DR";
                shptype = "1";
            } 
            //20190702 取消CDU产品别归到代理品
//            else if (hmark1.contains("CDU")) {
//                protype = "机组CDU";
//                protypeno = "CDU";
//                shptype = "2";
//            }
            else if (hmark1.contains("A")) {
                protype = "空压机组A系列";
                protypeno = "A";
                shptype = "2";
            } else if (hmark1.contains("SDS")) {
                protype = "空压机SDS";
                protypeno = "SDS";
                shptype = "2";
            } else if (hmark1.contains("P")) {
                protype = "真空泵";
                protypeno = "P";
                shptype = "2";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                qty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("V", shpdate, protype, protypeno, shptype);
                e.setShpnum(qty);
                e.setShpamts(BigDecimal.ZERO);
                data.add(e);
            }
//            for (int i = 0; i < bakResult.size(); i++) {
//                Object o[] = (Object[]) bakResult.get(i);
//                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
//                qty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
//                BscGroupShipment e = new BscGroupShipment("V", shpdate, protype, protypeno, shptype);
//                e.setShpnum(qty);
//                e.setShpamts(BigDecimal.ZERO);
//                if (data.contains(e)) {
//                    BscGroupShipment entity = data.get(data.indexOf(e));
//                    entity.setShpnum(entity.getShpnum().add(qty));
//                } else {
//                    data.add(e);
//                }
//            }
            //订单
            temp = getSalesOrder(y, m, d, Calendar.MONTH, getQueryParams());
            if (temp != null && !temp.isEmpty()) {
                for (BscGroupShipment c : temp) {
                    if (data.contains(c)) {
                        BscGroupShipment a = data.get(data.indexOf(c));
                        a.setOrdnum(a.getOrdnum().add(c.getOrdnum()));
                        a.setOrdamts(a.getOrdamts().add(c.getOrdamts()));
                    } else {
                        data.add(c);
                    }
                }
            }
            //出貨金額
            if (!data.isEmpty()) {
                for (BscGroupShipment e : data) {
                    amts = getShipmentAmount(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (amts != null) {
                        e.setShpamts(amts);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //出货金额
    public BigDecimal getShipmentAmount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        BigDecimal shp1 = BigDecimal.ZERO;
        BigDecimal bshp1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select  isnull(sum((d.shpamts*h.ratio)),0) from cdrhmas e,cdrdta d  inner join cdrhad h on  d.facno=h.facno  and d.shpno=h.shpno ");
        sb.append(" where  e.cdrno=d.cdrno and h.cusno not in ('SSD00328') and h.houtsta <> 'W' AND e.hmark2='ZJ' ");
        sb.append(" and h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
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
        sb.append("select isnull(sum((d.bakamts*h.ratio)),0) from cdrbhad h right join cdrbdta d on h.bakno=d.bakno ");
        sb.append(" where h.cusno not  in ('SSD00328')  and h.baksta <> 'W' AND h.hmark2='ZJ '   ");
        sb.append(" and h.facno='${facno}' ");
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
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = erpEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shp1 = (BigDecimal) o1;
            bshp1 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(GroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shp1.subtract(bshp1);
    }

    //订单台数
    protected List<BscGroupShipment> getSalesOrder(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select h.recdate,isnull(sum(d.cdrqy1),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and d.drecsta not in ('98','99') ");
        sb.append(" and h.cusno not in ('SSD00328') and  h.facno='${facno}' AND h.hmark2='ZJ '  ");
        if (!"".equals(hmark1)) {
            sb.append(" and h.hmark1 ").append(hmark1);
        }
        sb.append(" and ( d.itnbr in (select itnbr from invmas where itcls in('RC2','AA','3176','3576','3579','3580','3679','3705','3707','3716','3733','3735','3738','3766','3776','3780','3801','3802','3806','3833','3835','3838','3866','3879','3880','3A76','3A79','CDU','7110')) )");
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} and h.recdate<='${d}' group by h.recdate  ");
        String cdrdmas = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            List cdrResult = query.getResultList();
            Date recdate;
            String protype, protypeno, shptype;
            if (hmark1.contains("R") && !hmark1.contains("DR")) {
                protype = "压缩机R系列";
                protypeno = "R";
                shptype = "1";
            } else if (hmark1.contains("L")) {
                protype = "压缩机L系列";
                protypeno = "L";
                shptype = "1";
            } else if (hmark1.contains("DR")) {
                protype = "压缩机DORIN";
                protypeno = "DR";
                shptype = "1";
            } else if (hmark1.contains("CDU")) {
                protype = "机组CDU";
                protypeno = "CDU"; 
                shptype = "2";
            } else if (hmark1.contains("A")) {
                protype = "空压机组A系列";
                protypeno = "A";
                shptype = "2";
            } else if (hmark1.contains("SDS")) {
                protype = "空压机SDS";
                protypeno = "SDS";
                shptype = "2";
            } else if (hmark1.contains("P")) {
                protype = "真空泵";
                protypeno = "P";
                shptype = "2";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < cdrResult.size(); i++) {
                Object o[] = (Object[]) cdrResult.get(i);
                recdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                qty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("V", recdate, protype, protypeno, shptype);
                e.setOrdnum(qty);
                e.setOrdamts(BigDecimal.ZERO);
                data.add(e);
            }
            if (!data.isEmpty()) {
                for (BscGroupShipment e : data) {
                    amts = getSalesOrderAmount(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (amts != null) {
                        e.setOrdamts(amts);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(GroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //订单金额
    protected BigDecimal getSalesOrderAmount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT  isnull(convert(decimal(16,2),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and d.drecsta not in ('98','99') ");
        sb.append(" and h.cusno not in ('SSD00328') and  h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and h.hmark1 ").append(hmark1);
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

        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            result = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(GroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

}
