/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class InventoryAmountA4 extends Inventory {

    public InventoryAmountA4() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String trtype = map.get("trtype") != null ? map.get("trtype").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT ifnull(sum(amount+amamount),0) FROM inventoryproduct   ");
        sb.append(" WHERE facno = '${facno}' ");
        sb.append(" AND  wareh = 'JCZC' ");
        if (!"".equals(trtype)) {
            sb.append(" AND trtype = '").append(trtype).append("'");
        }
        if (!"".equals(deptno)) {
            sb.append(" AND deptno LIKE '").append(deptno).append("%'");
        }
        if (!"".equals(genre)) {
            sb.append(" AND genre ").append(genre);
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}",
                facno);
        Query query = superEJBForKPI.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA4.getValue)()异常", ex.toString());
        }
        return result;
    }

    @Override
    public List<String> getWarehs(LinkedHashMap<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
