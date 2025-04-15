/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
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
public class BscGroupHSShipmentBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public BscGroupHSShipmentBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataShpimentActualValue(int y, int m, Date d) throws Exception {
        try {
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " ='QT' ");
            queryParams.put("cusno", " ='HSH00003' ");
            List<BscGroupShipment> resultData = getShipment(y, m, d, Calendar.MONTH, getQueryParams());

            List<BscGroupShipment> tempData;
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " ='QT' ");
            queryParams.put("cusno", " ='HTW00001' ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " ='QT' ");
            queryParams.put("cusno", "  not in ('HSH00003','HTW00001') ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " ='HT' ");
            queryParams.put("cusno", " ='HSH00003' ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " ='HT' ");
            queryParams.put("cusno", " ='HTW00001' ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " ='HT' ");
            queryParams.put("cusno", "  not in ('HSH00003','HTW00001') ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " not in ('HT','QT') ");
            queryParams.put("cusno", " ='HSH00003' ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " not in ('HT','QT') ");
            queryParams.put("cusno", " ='HTW00001' ");
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
            queryParams.clear();
            queryParams.put("facno", "H");
            queryParams.put("spdsc", " not in ('HT','QT') ");
            queryParams.put("cusno", "  not in ('HSH00003','HTW00001') ");
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
                erpEJB.getEntityManager()
                    .createNativeQuery("delete from bsc_groupshipment where  facno='H' and year(soday)=" + y
                        + " and month(soday) = " + m + " and type = 'Shipment'")
                    .executeUpdate();
                for (BscGroupShipment e : resultData) {
                    erpEJB.getEntityManager().persist(e);
                }
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    // 出货
    // 2022/5/23 没有单位换算的出货数量直接是0，HS/HY互售原料不计重量
    // d.shpqy1 d.bshpqy1 => 0
    protected List<BscGroupShipment> getShipment(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String spdsc = map.get("spdsc") != null ? map.get("spdsc").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";
        List<BscGroupShipment> data = new ArrayList<>();
        List<BscGroupShipment> temp;
        BigDecimal num = BigDecimal.ZERO;
        BigDecimal amts = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.soday,isnull(sum(a.num),0),isnull(sum(a.shpamts),0) from ( ");
        sb.append(" select h.shpdate as soday, ");
        sb.append(
            " cast(isnull(case substring(s.judco,1,1)+s.fvco when '4F' then d.shpqy1*s.rate2  else 0 end,0) as decimal(12,2)) as num,");
        sb.append(
            " cast((case when h.coin<>'RMB' then d.shpamts*h.ratio else (case when h.tax = '1' then d.shpamts*h.ratio else d.shpamts*h.ratio/(h.taxrate+1) end) end) as decimal(12,2)) as shpamts ");
        sb.append(" from cdrdta d,cdrhad h,invmas s ");
        sb.append(" where h.shpno=d.shpno and d.itnbr=s.itnbr  and h.houtsta not in ('W','N') ");
        if (!"".equals(spdsc)) {
            sb.append(" and substring(s.spdsc,1,2) ").append(spdsc);
        }
        if (!"".equals(cusno)) {
            sb.append(" AND h.cusno ").append(cusno);
        }
        sb.append("  and h.trtype in ('L1A','S1A')");
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate<='${d}' ");
        sb.append(" UNION ALL ");
        // 销退
        sb.append(" SELECT  h.bakdate as soday, ");
        sb.append(
            " -1*cast(isnull(case substring(s.judco,1,1)+s.fvco when '4F' then d.bshpqy1*s.rate2  else 0 end,0) as decimal(12,2)) as num, ");
        sb.append(
            " -1*cast((case when h.coin<>'RMB' then d.bakamts*h.ratio else (case when h.tax = '1' then d.bakamts*h.ratio else d.bakamts*h.ratio/(h.taxrate+1) end) end) as decimal(12,2)) as shpamts ");
        sb.append(" from cdrbdta d,cdrbhad h,invmas s");
        sb.append(" where h.bakno=d.bakno and d.itnbr=s.itnbr and h.baksta not in ('W','N') and h.owarehyn='Y' ");
        if (!"".equals(spdsc)) {
            sb.append(" and substring(s.spdsc,1,2) ").append(spdsc);
        }
        if (!"".equals(cusno)) {
            sb.append(" AND h.cusno ").append(cusno);
        }
        sb.append(" and h.trtype in ('L2A','L2B','S2A','S2B')");
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} and h.bakdate<='${d}' ");
        // 质量扣款
        sb.append(" UNION ALL ");
        sb.append(" select  a.bildat as soday, ");
        sb.append(
            " -1*cast( (case substring(s.judco,1,1)+s.fvco when '4F' then d.bshpqy1*s.rate2  else 0 end)  as decimal(12,2)) as num, ");
        sb.append(
            " -1*cast((case when a.taxkd = '1' then a.losamts else a.losamts/(1+a.taxrate) end) as decimal(12,2)) as shpamts  from armblos a,cdrbdta d,invmas s ");
        sb.append(" where a.facno=d.facno  and a.bakno=d.bakno and a.trseq=d.trseq and s.itnbr=d.itnbr  ");
        if (!"".equals(spdsc)) {
            sb.append(" and substring(s.spdsc,1,2) ").append(spdsc);
        }
        if (!"".equals(cusno)) {
            sb.append(" AND a.ivocus ").append(cusno);
        }
        sb.append(" and year(a.bildat) = ${y} and month(a.bildat) = ${m} and a.bildat<='${d}' ");
        sb.append(" ) as a GROUP BY a.soday ");
        String cdrqty = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m))
            .replace("${facno}", facno).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query shpQuery = erpEJB.getEntityManager().createNativeQuery(cdrqty);
        try {
            List shpResult = shpQuery.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if ("='QT'".equals(spdsc.trim())) {
                protype = "球铁";
                protypeno = "BI";
                if (null == cusno.trim()) {
                    shptype = "";
                } else {
                    switch (cusno.trim()) {
                        case "='HSH00003'":
                            shptype = "S";
                            break;
                        case "='HTW00001'":
                            shptype = "T";
                            break;
                        default:
                            shptype = "5";
                            break;
                    }
                }
            } else if ("='HT'".equals(spdsc.trim())) {
                protype = "灰铁";
                protypeno = "GI";
                if (null == cusno.trim()) {
                    shptype = "";
                } else {
                    switch (cusno.trim()) {
                        case "='HSH00003'":
                            shptype = "S";
                            break;
                        case "='HTW00001'":
                            shptype = "T";
                            break;
                        default:
                            shptype = "5";
                            break;
                    }
                }
            } else if (spdsc.contains("not")) {
                protype = "其他";
                protypeno = "OTH";
                if (null == cusno.trim()) {
                    shptype = "";
                } else {
                    switch (cusno.trim()) {
                        case "='HSH00003'":
                            shptype = "S";
                            break;
                        case "='HTW00001'":
                            shptype = "T";
                            break;
                        default:
                            shptype = "5";
                            break;
                    }
                }
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[])shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                num = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                amts = BigDecimal.valueOf(Double.valueOf(o[2].toString()));
                BscGroupShipment e = new BscGroupShipment("H", shpdate, "Shipment", protype, protypeno, shptype);
                e.setQuantity(num);
                e.setAmount(amts);
                e.setCusno(shptype);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(BscGroupHSShipmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
