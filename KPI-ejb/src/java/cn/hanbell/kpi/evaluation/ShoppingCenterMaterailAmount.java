/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.util.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C2082
 */
public class ShoppingCenterMaterailAmount implements Actual {

    protected SuperEJBForKPI superEJBForKPI = lookupSuperEJBForKPI();
    protected SuperEJBForERP superEJB;

    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public ShoppingCenterMaterailAmount() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String matype = map.get("type") != null ? map.get("type").toString() : "";
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select IFNULL(sum(acpamt),0)");
            sb.append("from shoppingtable a  where  a.facno='");
            sb.append(facno).append("' and iscenter=1 and type ").append(matype).append(" and yearmon='").append(BaseLib.formatDate("yyyyMM", d)).append("'");
            Query query = superEJBForKPI.getEntityManager().createNativeQuery(sb.toString());
            return (BigDecimal) query.getSingleResult();
        } catch (Exception ex) {
            return BigDecimal.ZERO;
        }
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }

    private SuperEJBForKPI lookupSuperEJBForKPI() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForKPI) c.lookup("java:global/KPI/KPI-ejb/IndicatorBean!cn.hanbell.kpi.ejb.IndicatorBean");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }
}
