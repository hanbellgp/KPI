/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class InventoryAmountA4 extends Inventory{
    public InventoryAmountA4(){
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String trtype = map.get("trtype") != null ? map.get("trtype").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT sum(isnull(amount,0)) FROM invamount   ");
        sb.append(" WHERE facno = '${facno}' AND prono = '${prono}' ");
        sb.append(" AND  wareh = 'JCZC' AND trtype = 'CA' ");
        if(!"".equals(trtype)){
            sb.append(" AND trtype = '").append(trtype).append("'");
        }
        sb.append(" AND genre NOT LIKE  '%,%' ");
        if(!"".equals(deptno)){
            sb.append(" AND deptno LIKE '").append(deptno).append("%'");
        }
        sb.append(" AND yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA4--getValue)()异常", ex.toString());
        }
        return result;
    }
    
}
