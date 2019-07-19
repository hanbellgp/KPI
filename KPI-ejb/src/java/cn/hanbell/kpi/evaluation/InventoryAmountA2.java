/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1749 营业性质
 */
public class InventoryAmountA2 extends Inventory {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();
    protected BigDecimal fgsValue = BigDecimal.ZERO;//汉钟分公司库 的数据
    protected BigDecimal fgsZjValue = BigDecimal.ZERO;//分公司整机部分的数据

    public InventoryAmountA2() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String genreno = map.get("genreno") != null ? map.get("genreno").toString() : "";//大类编号
        String genzls = map.get("genzls") != null ? map.get("genzls").toString() : "";//种类编号
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";//产品别
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        //公用物料部分
        sb.append(" SELECT isnull(sum(m.amount),0) FROM invamount m  ");
        sb.append(" WHERE m.facno = '${facno}' AND m.prono = '${prono}' ");
        sb.append(" AND m.trtype = 'ZC' AND m.genre NOT LIKE  '%,%' ");
        sb.append(" AND wareh IN (SELECT d.wareh FROM invindexdta d  ");
        sb.append(" INNER JOIN invindexhad h ON d.facno = h.facno AND d.prono = h.prono AND d.indno = h.indno where 1=1 ");
        sb.append(" AND h.genzls = 'C10' ");//待指标数据都归档完成之前用这个逻辑（ERP需维护每个指标对应的库别）
//        if (!"".equals(genzls)) {
//            sb.append(" AND h.genzls = '").append(genzls).append("'");
//        }
        if (!"".equals(genreno)) {
            sb.append(" AND h.genreno = '").append(genreno).append("'");
        }
        sb.append(" )");
        if (!"".equals(genre)) {
            sb.append(" AND m.genre ").append(genre);
        }
        sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getValue()异常", ex.toString());
        }
        return result;
    }

    //汉钟分公司库的数据 
    public BigDecimal getFgsValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String genreno = map.get("genreno") != null ? map.get("genreno").toString() : "";
        String genzls = map.get("genzls") != null ? map.get("genzls").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT isnull(sum(m.amount),0) FROM invamount m  ");
        sb.append(" WHERE m.facno = '${facno}' AND m.prono = '${prono}' ");
        sb.append(" AND m.trtype = 'ZC' AND m.genre NOT LIKE  '%,%' ");
        sb.append(" AND m.wareh IN (SELECT d.wareh FROM invindexdta d  ");
        sb.append(" INNER JOIN invindexhad h ON d.facno = h.facno AND d.prono = h.prono AND d.indno = h.indno where 1=1 ");
        if (!"".equals(genreno)) {
            sb.append(" AND h.genreno = '").append(genreno).append("'");
        }
        if (!"".equals(genzls)) {
            sb.append(" AND h.genzls ='").append(genzls).append("'");
        }
        sb.append(" )");
        if (!"".equals(genre)) {
            sb.append(" AND m.genre ").append(genre);
        }
        sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getFgsValue()异常", ex.toString());
        }
        return result;
    }

    //分公司整机数据 只有分公司数据才有自己本身整机的数据
    public BigDecimal getFgsZjValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String genreno = map.get("genreno") != null ? map.get("genreno").toString() : "";
        String genzls = map.get("genzls") != null ? map.get("genzls").toString() : "";
        String genre = map.get("genre") != null ? map.get("genre").toString() : "";
        StringBuilder sb = new StringBuilder();
        BigDecimal result = BigDecimal.ZERO;
        sb.append(" SELECT isnull(sum(m.amount),0) FROM invamount m   ");
        sb.append(" LEFT JOIN invwh w ON m.facno = w.facno AND m.prono = w.prono AND m.wareh = w.wareh ");
        sb.append(" where 1=1  ");
        if (!"".equals(genzls)) {
            switch (genzls) {
                case "C12":
                    sb.append(" AND m.facno = 'N' ");
                    break;
                case "C13":
                    sb.append(" AND m.facno = 'G' ");
                    break;
                case "C14":
                    sb.append(" AND m.facno = 'C4' ");
                    break;
                case "C15":
                    sb.append(" AND m.facno = 'J' ");
                    break;
                default:
                    sb.append(" ");
            }
        }
        sb.append(" AND m.prono = '${prono}' ");
        sb.append(" AND m.trtype = 'ZC' ");
        sb.append(" AND m.itclscode = '1' ");//itclscode='1'为整机物料
        sb.append(" AND w.costyn = 'Y'  ");
        sb.append(" AND m.yearmon =  '").append(y).append(getMon(m)).append("'");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);
        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            result = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2--getFgsZjValue()异常", ex.toString());
        }
        return result;
    }
    
    
    //获取生产性物料的库存金额
    public BigDecimal getProductValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map){
        BigDecimal result = BigDecimal.ZERO;
        return result;
    }
    
    //获取生产在制的库存金额
    public BigDecimal getgetProductZZValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map){
        BigDecimal result = BigDecimal.ZERO;
        return result;
    }
    

    private IndicatorBean lookupIndicatorBeanBean() {
        try {
            Context c = new InitialContext();
            return (IndicatorBean) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

}
