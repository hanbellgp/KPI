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
public class BscGroupSHSaleOrderBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    private List<BscGroupShipment> bsList;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupSHSaleOrderBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    //订单
    public void updataSaleOrderActualValue(int y, int m, Date d) {

        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
        List<BscGroupShipment> resultData = getSalesOrderAmount(y, m, d, y, getQueryParams());

        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "G");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
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
        queryParams.clear();
        queryParams.put("facno", "J");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
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
        queryParams.clear();
        queryParams.put("facno", "N");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
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
        queryParams.clear();
        queryParams.put("facno", "C4");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " <>'RT' ");
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
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
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
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "= 'P'");
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
        //涡旋机
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " IN ('SAM-5HP','SAM-7HP') ");
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
        //202204 拆分A机体。此处排除了（除涡旋机），c
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AH' ");
        queryParams.put("n_code_DC", " NOT IN ('SAM-5HP','SAM-7HP') ");
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
        
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='OH' ");
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
        queryParams.clear();
        queryParams.put("facno", "E");
        queryParams.put("n_code_DA", " ='OH' ");
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
        queryParams.clear();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DA", " ='RT' ");
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
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where protypeno <> 'Z' and facno='S' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'SalesOrder' ").executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //202204 拆分
    protected List<BscGroupShipment> getSalesOrderAmount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select h.recdate,isnull(convert(decimal(16,2),sum(case h.tax when '1' then (d.tramts*h.ratio) else (d.tramts*h.ratio)/(h.taxrate+1) end)),0) as tramt  ");
        sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
        sb.append(" and d.drecsta not in ('98','99','10') ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        //20220604涡旋机金额也要加上后处理设备
        if (n_code_DA.contains("AA") || n_code_DA.contains("RT") || n_code_DA.contains("OH")||("AH".equals(n_code_DA)&&" IN ('SAM-5HP','SAM-7HP') ".equals(n_code_CD))) {
            sb.append(" and d.n_code_DD in ('00','02') ");
        } else {
            sb.append(" and d.n_code_DD in ('00') ");
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} and h.recdate<='${d}' group by h.recdate");
        String cdrSql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query cdrQuery = erpEJB.getEntityManager().createNativeQuery(cdrSql);
        try {
            List cdrResult = cdrQuery.getResultList();
            Date recdate;
            String protype, protypeno, shptype;
            if (n_code_DA.contains("R") && !n_code_DA.contains("RT")) {
                protype = "R机体";
                protypeno = "R";
                shptype = "1";
            } else if (n_code_DA.contains("AA")) {
                protype = "A机组";
                protypeno = "A";
                shptype = "2";
            } else if (n_code_DA.contains("AH")&& " IN ('SAM-5HP','SAM-7HP') ".equals(n_code_DC)) {
                 protype = "无油机组";
                protypeno = "S";
                shptype = "1";
            }else if(n_code_DA.contains("AH")){
                      protype = "A机体";
                protypeno = "A";
                shptype = "1";
            }else if (n_code_DA.contains("P")) {
                protype = "真空泵";
                protypeno = "P";
                shptype = "2";
            } else if (n_code_DA.contains("OH")) {
                protype = "低环温热泵";
                protypeno = "OH";
                shptype = "2";
            } else if (n_code_DA.contains("RT")) {
                protype = "离心机体";
                protypeno = "RT";
                shptype = "2";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < cdrResult.size(); i++) {
                Object o[] = (Object[]) cdrResult.get(i);
                recdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                amts = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("S", recdate, "SalesOrder", protype, protypeno, shptype);
                e.setQuantity(BigDecimal.ZERO);
                e.setAmount(amts);
                data.add(e);
            }
            if (!data.isEmpty()) {
                if (map.get("n_code_DD") != null) {
                    map.remove("n_code_DD");
                    map.put("n_code_DD", " IN ('00') ");
                } else {
                    map.put("n_code_DD", " IN ('00') ");
                }
                for (BscGroupShipment e : data) {
                    qty = getSalesOrder(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (qty != null) {
                        e.setQuantity(qty);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupSHSaleOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //订单台数
    protected BigDecimal getSalesOrder(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" select isnull(sum(d.cdrqy1),0) as num ");
        sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
        sb.append(" and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
        sb.append(" and d.drecsta not in ('98','99','10') ");
        if (n_code_DA.contains("AA")) {
            sb.append(" and left(d.itnbr,1)='3' ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        sb.append(" and year(h.recdate) = ${y} and month(h.recdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.recdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.recdate= '${d}' ");
                break;
            default:
                sb.append(" and h.recdate<= '${d}' ");
        }
        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query1.getSingleResult();
            result = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(BscGroupSHSaleOrderBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
}

