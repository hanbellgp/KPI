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
public class BscGroupSHShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    private List<BscGroupShipment> bsList;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupSHShipmentBean() {
    }

    public List findNeedRow(int y, int m, Date d) {
        erpEJB.setCompany("C");
        StringBuilder sb = new StringBuilder();
        //View的形式
        sb.append(" SELECT facno,soday,protypeno,shptype,cusno,isnull(sum(shpnum),0) as shpnum,isnull(sum(shpamts),0) as shpamts,isnull(sum(ordnum),0) as ordnum,isnull(sum(ordamts),0) as ordamts FROM v_bsc_groupshipment b ");
        sb.append(" where year(soday) = ${y} and month(soday) = ${m} and soday <= '${d}' ");
        sb.append(" GROUP BY facno,soday,protypeno,shptype,cusno ");
        //不要View的形式
//        sb.append(" SELECT facno, soday, protypeno, shptype, isnull(cusno,'') cusno, sum(shpnum) shpnum, sum(shpamts) shpamts, sum(ordnum) ordnum, sum(ordamts) ordamts  ");
//        sb.append(" FROM ( ");
//        sb.append(" SELECT facno, soday, protype, protypeno, shptype, cusno, quantity shpnum, amount shpamts, 0 as ordnum, 0 as ordamts ");
//        sb.append(" FROM bsc_groupshipment ");
//        sb.append(" WHERE type='Shipment' OR type='ServiceAmount' ");
//        sb.append(" UNION ");
//        sb.append(" SELECT facno, soday, protype, protypeno, shptype, cusno, 0 as shpnum, 0 as shpamts, quantity ordnum, amount ordamts ");
//        sb.append(" FROM bsc_groupshipment ");
//        sb.append(" WHERE type='SalesOrder' ");
//        sb.append(" ) a  ");
//        sb.append(" where year(soday) = ${y} and month(soday) = ${m} and soday <= '${d}' ");
//        sb.append(" GROUP BY facno, soday, protypeno, shptype, cusno  ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List resultList = query.getResultList();
            return resultList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    //出货
    public void updataShpimentActualValue(int y, int m, Date d) throws Exception {
        try {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", " ='R' ");
            queryParams.put("n_code_DC", " <>'RT' ");
            queryParams.put("depno", " IN ('1B000','1B100' ,'1C000','1D000','1E000','1V000') ");
            //queryParams.put("n_code_DD", " in ('00','02') ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            List<BscGroupShipment> resultData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());

            List<BscGroupShipment> tempData;
            queryParams.clear();
            queryParams.put("facno", "G");
            queryParams.put("n_code_DA", " ='R' ");
            queryParams.put("n_code_DC", " <>'RT' ");
            queryParams.put("depno", " IN ('1B000','1B100','1C000','1D000','1E000','1V000') ");
            //queryParams.put("n_code_DD", " in ('00','02') ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN ('1B000','1C000','1D000','1E000','1V000') ");
            //queryParams.put("n_code_DD", " in ('00','02') ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN ('1B000','1C000','1D000','1E000','1V000') ");
            //queryParams.put("n_code_DD", " in ('00','02') ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN ('1B000','1C000','1D000','1E000','1V000') ");
            //queryParams.put("n_code_DD", " in ('00','02') ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN ('1Q000','1Q100') ");
            queryParams.put("n_code_DA", " ='AA' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN ('1H000','1H100') ");
            queryParams.put("n_code_DA", "= 'P'");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            //A机体-非涡旋部分
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN('1G100','1G110') ");
            queryParams.put("n_code_DA", " ='AH' ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            queryParams.put("n_code_DC", " NOT  IN ('SAM-5HP','SAM-7HP') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            //涡旋部分
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN('1G100','1G110') ");
            queryParams.put("n_code_DA", " ='AH' ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            queryParams.put("n_code_DC", " IN ('SAM-5HP','SAM-7HP') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
            if (tempData != null && !tempData.isEmpty()) {
                for (BscGroupShipment b : tempData) {
                    if (resultData.contains(b)) {
                        System.out.print("----" + b);
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
            queryParams.put("depno", " IN('5B000') ");
            queryParams.put("n_code_DA", " ='OH' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN('8A000') ");
            queryParams.put("n_code_DA", " ='OH' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
            queryParams.put("depno", " IN('5C000','5A000') ");
            queryParams.put("n_code_DA", " ='RT' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            tempData = getShipmentAmount(y, m, d, Calendar.MONTH, getQueryParams());
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
                erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where protypeno <> 'Z' and facno='S' and year(soday)=" + y + " and month(soday) = " + m + " and type = 'Shipment'").executeUpdate();
                for (BscGroupShipment e : resultData) {
                    erpEJB.getEntityManager().persist(e);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    //出货金额
    protected List<BscGroupShipment> getShipmentAmount(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String depno = map.get("depno") != null ? map.get("depno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        List<BscGroupShipment> temp;
        BigDecimal qty = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT a.cdrdate,isnull(sum(a.amount),0) FROM ( ");
        //出货
        sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno, ");
        sb.append(" sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN shpqy1 when d.n_code_DA!='AA' THEN shpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD  ,mancode,hmark1,hmark2 ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno where h.facno='${facno}' and h.houtsta <> 'W'  ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate<='${d}' ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" UNION  ALL  ");
        //销退
        sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,  -sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN bshpqy1 when d.n_code_DA!='AA' THEN bshpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD  ,mancode,hmark1,hmark2 ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno  where h.baksta <> 'W'  and  h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} and h.bakdate<='${d}' ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        //如果算入后处理金额则单独处理，只算金额不算台数
        if (!"".equals(n_code_DD) && !n_code_DA.contains("AH")) {
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount, ");
            sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
            sb.append(" where h.facno='${facno}' and h.houtsta <> 'W' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ").append(n_code_DD);
            sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} and h.shpdate <= '${d}' ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
            sb.append(" ,mancode,hmark1,hmark2 from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
            sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ").append(n_code_DD);
            sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} and h.bakdate <= '${d}' ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        }
        //AH 中  AJ不算后处理  SDS算入后处理
        if (n_code_DA.contains("AH")) {
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount, ");
            sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno where h.facno='${facno}' and h.houtsta <> 'W' ");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            sb.append(" and d.n_code_DC='SDS' and d.n_code_DD = '02' ");
            sb.append(" AND year(h.shpdate)=${y} AND month(h.shpdate)=${m} and h.shpdate <= '${d}' ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
            sb.append("  ,mancode,hmark1,hmark2 from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
            sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N'");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            sb.append(" and d.n_code_DC='SDS' and d.n_code_DD = '02' ");
            sb.append(" AND year(h.bakdate)=${y} AND month(h.bakdate)=${m} and h.bakdate <= '${d}' ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2");
        }
        //getARM232Value 加扣款单独列出来 只算金额不算台数
        sb.append(" union all ");
        sb.append(" SELECT h.facno,itnbrcus,h.cusno,h.trdat AS cdrdate ,d.depno,0 AS quantity, ");
        sb.append(" ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS amount ");
        sb.append(" ,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode,'ARM232' as hmark1,'ARM232' as hmark2 ");
        sb.append(" FROM armpmm h,armacq d,cdrdta s,cdrhad c WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno ");
        sb.append(" AND d.shpno=s.shpno AND d.shpseq = s.trseq AND s.shpno=c.shpno ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and s.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and s.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND (s.n_code_DD  ='00'  or s.n_code_DD ").append(n_code_DD).append(" ) ");
        } else {
            sb.append("  AND s.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m} and h.trdat <= '${d}' ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.trdat,d.depno,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode ");

        //getARM423Value 折让
        sb.append(" union all ");
        sb.append(" SELECT h.facno,'ARM423' as itnbrcus,d.ivocus AS 'cusno',h.recdate AS cdrdate,h.depno,0 AS quantity, ");
        sb.append(" ISNULL(sum(d.recamt),0) AS amount ,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno IN ('6001','6002') ");
        sb.append(" AND h.ogdkid  ").append(ogdkid);
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and h.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND (h.n_code_DD  ='00'  or h.n_code_DD ").append(n_code_DD).append(" ) ");
        } else {
            sb.append("  AND h.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) =${m} and h.recdate <= '${d}' ");
        sb.append(" GROUP BY h.facno,d.ivocus,h.recdate,h.depno,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");

        //ARM235的金额部分
//        sb.append(" union all ");
//        sb.append(" select DISTINCT c.facno,'' as itnbrcus,c.cusno,c.shpdate as cdrdate, h.depno, 0 AS quantity ,c.apmamt as amount, ");
//        sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,h.mancode,'ARM235' as hmark1,'ARM235' as hmark2 from armicdh c ");
//        sb.append(" LEFT JOIN cdrdta d on h.facno = d.facno and h.shpno = d.shpno on c.facno = h.facno and c.shpno = h.shpno ");
//        sb.append(" where  h.houtsta<>'W' ");
//        if (!"".equals(n_code_DA)) {
//            sb.append(" and h.n_code_DA ").append(n_code_DA);
//        }
//        if (!"".equals(n_code_DC)) {
//            sb.append(" and h.n_code_DC ").append(n_code_DC);
//        }
//        sb.append(" and c.apmamt > 0 and convert(VARCHAR(6),c.shpdate,112) = convert(VARCHAR(6),h.shpdate,112) ");
//        sb.append(" and year(c.shpdate) = ${y} AND month(c.shpdate)= ${m} and c.shpdate <= '${d}' ");
        //getARM270Value 其它项金额 关联部门
        if (!"".equals(depno) && !"SDS".equals(n_code_DC)) {
            sb.append(" union all ");
            sb.append(" SELECT h.facno,'' as itnbrcus,h.cusno,h.bildat AS cdrdate ,h.depno,0 AS quantity,ISNULL(sum(h.shpamt),0) AS amount, ");
            sb.append(getDA(depno)).append(" as n_code_DA, ");
            sb.append(" '' as  n_code_CD,");
            sb.append(getDA(depno)).append(" as n_code_DC, ");
            sb.append(" '00' as  n_code_DD,mancode,'ARM270' as hmark1,'ARM270' as hmark2 FROM armbil h WHERE 1=1 ");
            sb.append(" and h.rkd='RQ11' ");
            sb.append(" AND h.depno ").append(depno);
            sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) =${m} and h.bildat <= '${d}' ");
            sb.append(" group by  h.facno,h.cusno,h.depno,h.bildat,mancode ");
            // 关于柯茂北京中矿之芦南芦北项目分期收款，期限为三年，每季度为1期，共12期（2018年4月至2021年3月），调整逻辑抓取金额/(1+7%*3)
            if (depno.contains("5B000")) {
                sb.append(" union all ");
                sb.append(" SELECT h.facno,'' as itnbrcus,h.cusno,h.bildat AS cdrdate ,h.depno,0 AS quantity,ISNULL(sum(h.shpamt) / 1.21,0) AS amount, ");
                sb.append(getDA(depno)).append(" as n_code_DA, ");
                sb.append(" '' as  n_code_CD,");
                sb.append(getDA(depno)).append(" as n_code_DC, ");
                sb.append(" '00' as  n_code_DD,mancode,'ARM270' as hmark1,'ARM270' as hmark2 FROM armbil h WHERE 1=1 ");
                sb.append(" and h.rkd in ('RQ51') ");
                sb.append(" AND h.depno ").append(depno);
                sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) =${m} and h.bildat <= '${d}' ");
                sb.append(" group by  h.facno,h.cusno,h.depno,h.bildat,mancode ");
            }
            sb.append(" ) a,cdrcus s ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");
            sb.append(" GROUP BY  a.cdrdate ");
        } else {
            sb.append(" ) a,cdrcus s ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");
            sb.append(" GROUP BY  a.cdrdate ");
        }

        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query shpQuery = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        try {
            List shpResult = shpQuery.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if (n_code_DA.contains("R") && !n_code_DA.contains("RT")) {
                protype = "R机体";
                protypeno = "R";
                shptype = "1";
            } else if (n_code_DA.contains("AH") && " IN ('SAM-5HP','SAM-7HP') ".equals(n_code_DC)) {
                protype = "无油机组";
                protypeno = "S";
                shptype = "1";
            } else if (n_code_DA.contains("AH")) {
                protype = "A机体";
                protypeno = "A";
                shptype = "1";
            } else if (n_code_DA.contains("AA")) {
                protype = "A机组";
                protypeno = "A";
                shptype = "2";
            } else if (n_code_DA.contains("P")) {
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
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                amts = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("S", shpdate, "Shipment", protype, protypeno, shptype);
                e.setQuantity(BigDecimal.ZERO);
                e.setAmount(amts);
                data.add(e);
            }
            //出貨台数
            if (!data.isEmpty()) {
                for (BscGroupShipment e : data) {
                    qty = getShipment(y, m, e.getBscGroupShipmentPK().getSoday(), Calendar.DATE, getQueryParams());
                    if (qty != null) {
                        e.setQuantity(qty);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupSHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

    //出货台数
    public BigDecimal getShipment(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        BigDecimal shp1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        //出货
        sb.append(" SELECT isnull(sum(quantity),0) FROM ( ");
        sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno, ");
        sb.append(" sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN shpqy1 when d.n_code_DA!='AA' THEN shpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD  ,mancode,hmark1,hmark2 ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno where h.facno='${facno}' and h.houtsta <> 'W'  ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' and d.n_code_DD ='00'  ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
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
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" UNION ALL ");
        //销退
        sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,  -sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN bshpqy1 when d.n_code_DA!='AA' THEN bshpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD  ,mancode,hmark1,hmark2 ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno  where h.baksta <> 'W'  ");
        sb.append(" and  h.facno='${facno}' and d.issevdta='N' and d.n_code_DD ='00' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
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
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" ) a,cdrcus s ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");

        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(cdrdta);
        try {
            Object o1 = query1.getSingleResult();
            shp1 = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(BscGroupSHShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shp1;
    }

    private String getDA(String depno) {
        String aa = "'ARM270'";
        if (depno.contains("1B")) {
            aa = "'R'";
        }
        if (depno.contains("1C")) {
            aa = "'R'";
        }
        if (depno.contains("1D")) {
            aa = "'R'";
        }
        if (depno.contains("1E")) {
            aa = "'R'";
        }
        if (depno.contains("1V")) {
            aa = "'R'";
        }
        if (depno.contains("1Q")) {
            aa = "'AA'";
        }
        if (depno.contains("1G")) {
            aa = "'AH'";
        }
        if (depno.contains("1H")) {
            aa = "'P'";
        }
        if (depno.contains("5A")) {
            aa = "'RT'";
        }
        if (depno.contains("5B") || depno.contains("8A")) {
            aa = "'OH'";
        }
        if (depno.contains("5C")) {
            aa = "'RT'";
        }
        return aa;
    }

}
