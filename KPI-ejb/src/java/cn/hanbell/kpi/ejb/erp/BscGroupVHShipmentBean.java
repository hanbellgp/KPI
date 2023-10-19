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
public class BscGroupVHShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupVHShipmentBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataShpimentActualValue(int y, int m, Date d) {
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
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='O' ");
        tempData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    //沈里达提出代理品不计算台数，实际越南打单为ZJ，故在此处调整
                    b.setQuantity(BigDecimal.ZERO);
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
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
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
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
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
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
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
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
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
                    a.setQuantity(a.getQuantity().add(b.getQuantity()));
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where facno='V' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'Shipment'").executeUpdate();
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
            } //20190702 取消CDU产品别归到代理品
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
            } else if (hmark1.contains("O")) {
                protype = "代理品";
                protypeno = "O";
                shptype = "21";
            }else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                qty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("V", shpdate, "Shipment", protype, protypeno, shptype);
                e.setQuantity(qty);
                e.setAmount(BigDecimal.ZERO);
                data.add(e);
            }
            //出貨金額
            if (!data.isEmpty()) {
                for (BscGroupShipment e : data) {
                    amts = getShipmentAmount(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (amts != null) {
                        e.setAmount(amts);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
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
        sb.append(" where h.cusno not  in ('SSD00328')  and h.baksta <> 'W' AND h.hmark2='ZJ'   ");
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
            Logger.getLogger(BscGroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shp1.subtract(bshp1);
    }

    //服务金额更新到中间档
    public void updataServerActualValue(int y, int m, Date d) {
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " in ('R','P','L','DR')");
        List<BscGroupShipment> resultData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='A'");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='M' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setQuantity(BigDecimal.ZERO);
                    a.setAmount(a.getAmount().add(b.getAmount()));
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where facno='V' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'ServiceAmount' ").executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //服务金额明细
    protected List<BscGroupShipment> getServiceValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        StringBuilder sb = new StringBuilder();
        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal sqty = BigDecimal.ZERO;
        sb.append(" select soday,isnull(cast(sum(num) as decimal(16,2)),0) as num from(  ");
        sb.append(" select h.shpdate as soday,isnull(sum((d.shpamts * h.ratio)),0) as num ");
        sb.append(" from cdrhmas e,cdrdta d  inner join cdrhad h on  d.facno=h.facno  and d.shpno=h.shpno   ");
        sb.append(" where h.facno = '${facno}' and h.houtsta <> 'W' and e.cdrno=d.cdrno and e.hmark2='FW'  ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        sb.append(" and year(h.shpdate)=${y} and month(h.shpdate)=${m} ");
        sb.append(" group by h.shpdate ");
        sb.append(" UNION  all ");
        sb.append(" select  h.bakdate as soday,isnull(sum((d.bakamts * h.ratio)*(-1)),0) as num ");
        sb.append(" from  cdrhmas e,cdrbhad h right join cdrbdta d on h.bakno=d.bakno  ");
        sb.append(" where h.facno = '${facno}'   and h.baksta <> 'W' and e.cdrno=d.cdrno AND e.hmark2='FW' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        sb.append(" and year(h.bakdate)=${y}  and month(h.bakdate)=${m} group by h.bakdate ");
        sb.append(" ) as a GROUP BY soday ");
        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List shpResult = query1.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if (hmark1.contains("R") || hmark1.contains("P") || hmark1.contains("L") || hmark1.contains("DR")) {
                protype = "越南R&P&L出货收费服务";
                protypeno = "R";
                shptype = "21";
            } else if (hmark1.contains("M")) {
                protype = "越南M出货收费服务";
                protypeno = "M";
                shptype = "21";
            } else if (hmark1.contains("A")) {
                protype = "空压机服务收费";
                protypeno = "A";
                shptype = "21";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                sqty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("V", shpdate, "ServiceAmount", protype, protypeno, shptype);
                e.setQuantity(BigDecimal.ZERO);
                e.setAmount(sqty);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupVHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
