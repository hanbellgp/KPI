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
public class BscGroupVHSaleOrderBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupVHSaleOrderBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataSaleOrderActualValue(int y, int m, Date d) {
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='R' ");
        List<BscGroupShipment> resultData = getSalesOrderAmount(y, m, d, y, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.remove("hmark1");
        queryParams.put("hmark1", " ='L' ");
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
        tempData = getSalesOrderAmount(y, m, d, y, getQueryParams());
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
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where facno='V' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'SalesOrder'").executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //订单金额
    protected List<BscGroupShipment> getSalesOrderAmount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  h.recdate,isnull(convert(decimal(16,2),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and d.drecsta not in ('98','99') ");
        sb.append(" and h.cusno not in ('SSD00328') and  h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and h.hmark1 ").append(hmark1);
        }
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
            } ////20190702 取消CDU产品别归到代理品
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
            for (int i = 0; i < cdrResult.size(); i++) {
                Object o[] = (Object[]) cdrResult.get(i);
                recdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                amts = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("V", recdate, "SalesOrder", protype, protypeno, shptype);
                e.setQuantity(BigDecimal.ZERO);
                e.setAmount(amts);
                data.add(e);
            }
            if (!data.isEmpty()) {
                for (BscGroupShipment e : data) {
                    qty = getSalesOrder(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (qty != null) {
                        e.setQuantity(qty);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupVHSaleOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //订单台数
    protected BigDecimal getSalesOrder(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" select isnull(sum(d.cdrqy1),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and d.drecsta not in ('98','99') ");
        sb.append(" and h.cusno not in ('SSD00328') and  h.facno='${facno}' ");
        if (!"".equals(hmark1)) {
            sb.append(" and h.hmark1 ").append(hmark1);
        }
        sb.append(" and ( d.itnbr in (select itnbr from invmas where itcls in('RC2','AA','3176','3576','3579','3580','3679','3705','3707','3716','3733','3735','3738','3766','3776','3780','3801','3802','3806','3833','3835','3838','3866','3879','3880','3A76','3A79','CDU','7110')) )");
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
            Logger.getLogger(BscGroupVHSaleOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;

    }

}
