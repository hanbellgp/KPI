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
import javax.persistence.Query;

/**
 *
 * @author C1749 生产目标计算接口
 */
public class InventoryAmountA1 extends Inventory {

    public InventoryAmountA1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String genzls = map.get("genzls") != null ? map.get("genzls").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;

        if (!genzls.equals("")) {
            switch (genzls) {
                case "B20"://原材料 = 生产性类别（itclscode）不是2的 + 借出总仓借厂商部分 + 托工借厂商部分
                case "B30"://半成品 = 生产性类别（itclscode）是2的 + 借出总仓借厂商部分 + 托工借厂商部分
                    //原数据中 类别是2的部分
                    sb.append(" SELECT sum(b.num) FROM ( ");
                    sb.append(" SELECT sum(isnull(m.amount,0)) AS num FROM invamount m  ");
                    sb.append(" WHERE m.facno = '${facno}' AND m.prono ='${prono}' AND  ");
                    sb.append(" m.wareh IN (SELECT d.wareh FROM invindexdta d INNER JOIN invindexhad h ON d.facno = h.facno AND d.prono = h.prono AND d.indno = h.indno ");
                    sb.append(" WHERE   h.genreno = 'A1' AND h.genzls <> 'B50' ) ");
                    sb.append(" AND m.genre NOT LIKE '%,%' ");
                    sb.append(" AND m.trtype = 'ZC' ");
                    //确定当前指标是原材料或者半成品
                    if (!genzls.equals("") && genzls.contains("B20")) {
                        sb.append(" AND m.itclscode <> '2' ");
                    } else {
                        sb.append(" AND m.itclscode = '2' ");
                    }
                    sb.append(" AND m.genre NOT IN ('RT','P','AD') ");
                    sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
                    //借出总仓的借厂商部分
                    sb.append(" UNION all ");
                    sb.append(" SELECT sum(isnull(m.amount,0)) AS num FROM invamount m  ");
                    sb.append(" WHERE m.facno = '${facno}' AND m.prono ='${prono}' AND m.genre NOT LIKE '%,%'  ");
                    sb.append(" AND m.wareh = 'JCZC' AND m.trtype = 'PJ' AND m.whdsc  = '借厂商' AND m.genre NOT IN ('RT','P','AD')  ");
                    if (!genzls.equals("") && genzls.contains("B20")) {
                        sb.append(" AND m.itclscode <> '2' ");
                    } else {
                        sb.append(" AND m.itclscode = '2' ");
                    }
                    sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
                    //原数据中托工的借厂商部分
                    sb.append(" UNION all ");
                    sb.append(" SELECT sum(isnull(m.amount,0)) AS num FROM invamount m  ");
                    sb.append(" WHERE m.facno = '${facno}' AND m.prono = '${prono}' AND m.genre NOT LIKE '%,%' ");
                    sb.append(" AND m.trtype = 'ZC' AND m.whdsc  = '借厂商' AND m.genre NOT IN ('RT','P','AD') ");
                    if (!genzls.equals("") && genzls.contains("B20")) {
                        sb.append(" AND m.itclscode <> '2' ");
                    } else {
                        sb.append(" AND m.itclscode = '2' ");
                    }
                    sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
                    sb.append(" ) b ");
                    break;
                case "B40"://如果中类编号是B40 在制品
                    sb.append(" SELECT sum(isnull(amount,0)) AS num FROM invamount WHERE trtype = 'ZZ'   AND wareh = 'SCZZ'  ");
                    sb.append(" AND genre NOT IN ('RT','P','AD') ");
                    sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
                    break;
                case "B50"://如果中类编号是B50（加工刀片库存（含刀柄））就直接选择库号
                    sb.append(" SELECT sum(isnull(m.amount,0)) FROM invamount m  ");
                    sb.append(" WHERE m.facno = '${facno}' AND m.prono = '${prono}' ");
                    sb.append(" and m.wareh IN (SELECT d.wareh FROM invindexdta d INNER JOIN invindexhad h ON d.facno = h.facno AND d.prono = h.prono AND d.indno = h.indno ");
                    sb.append(" WHERE  h.genzls = 'B50' ");
                    sb.append(" AND h.genreno = 'A1' ) ");
                    sb.append(" AND m.genre NOT LIKE '%,%' ");
                    sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
                    break;
                default:
                    sb.append("");
            }

        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno).replace("${prono}", prono);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA1类异常", ex.toString());
        }
        return result;

    }

}
