/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 采购中心采购金额，每个月初更新上月
 *
 * @author C2082
 */
public class ShoppingCenterAmount implements Actual {

    protected SuperEJBForERP superEJB;
    protected SuperEJBForKPI superEJBForKPI = lookupSuperEJBForKPI();
    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public ShoppingCenterAmount() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {

        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String prono = map.get("prono") != null ? map.get("prono").toString() : "";
        String vdrnoSql = map.get("vdrno") != null ? map.get("vdrno").toString() : "";
        //获取采购中心所有厂商
        try {
            StringBuffer vdrno =null;
            if (vdrnoSql != null && !"".equals(vdrnoSql)) {
                 vdrno=new StringBuffer();
                Query query1 = superEJBForKPI.getEntityManager().createNativeQuery(vdrnoSql);
                List<String> vdrnos = query1.getResultList();
                for (String o : vdrnos) {
                    vdrno.append("'").append(o).append("',");
                }
            }
            StringBuffer sb = new StringBuffer();
            sb.append(" SELECT sum(acpamt) as cp_acpamt ");
            sb.append(" FROM apmpyh ,purvdr ,purhad ");
            sb.append(" WHERE apmpyh.vdrno = purvdr.vdrno  and  purhad.facno = apmpyh.facno  and  purhad.prono = apmpyh.prono ");
            sb.append(" and  purhad.pono = apmpyh.pono  and  apmpyh.pyhkind = '1' ");
            sb.append(" AND apmpyh.facno =  '${facno}'  and apmpyh.prono ='${prono} '");
            if (vdrno!=null&&!"".equals(vdrno)) {
                sb.append(" and apmpyh.vdrno in (").append(vdrno.substring(0, vdrno.length() - 1)).append(")");
            }
            sb.append(" and year(apmpyh.trdat) = ${y} and month(apmpyh.trdat)= ${m} ");
            String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno).replace("${prono}", prono);
            superEJB.setCompany(facno);
            Query query = superEJB.getEntityManager().createNativeQuery(sql);

            Object o1 = query.getSingleResult();
            BigDecimal result = (BigDecimal) o1;
            if (result == null) {
                return BigDecimal.ZERO;
            }
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return BigDecimal.ZERO;
        }
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
