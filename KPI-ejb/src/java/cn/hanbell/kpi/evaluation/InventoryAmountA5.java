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
public class InventoryAmountA5 extends Inventory{
    public InventoryAmountA5(){
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String genzls = map.get("genzls") != null ? map.get("genzls").toString() : "";
        String genreno = map.get("genreno") != null ? map.get("genreno").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT sum(isnull(amount,0)) FROM invamount   ");
        sb.append(" WHERE facno = '${facno}' AND prono = '${prono}' ");
        sb.append(" AND trtype = 'ZC' AND genre NOT LIKE  '%,%' ");
        sb.append(" AND wareh IN (SELECT d.wareh FROM invindexdta d  ");
        sb.append(" INNER JOIN invindexhad h ON d.facno = h.facno AND d.prono = h.prono AND d.indno = h.indno WHERE 1=1  ");
        if(!"".equals(genreno)){
            sb.append(" AND h.genreno ='").append(genreno).append("'");
        }
        if(!"".equals(genzls)){
            sb.append(" AND h.genzls ='").append(genzls).append("'");
        }
        sb.append("  ) ");
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
