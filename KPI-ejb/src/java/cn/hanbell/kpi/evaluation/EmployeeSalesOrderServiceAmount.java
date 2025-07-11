/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.crm.DSALPBean;
import com.lightshell.comm.BaseLib;
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
 * @author C1879
 */
public class EmployeeSalesOrderServiceAmount extends SalesOrder {

    DSALPBean dsalpBean = lookupDsalpBean();

    public EmployeeSalesOrderServiceAmount() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String userid = map.get("userid") != null ? map.get("userid").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        BigDecimal tram = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT  isnull(convert(decimal(16,2),sum((d.tramts*h.ratio)/(h.taxrate+1))),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno");
        sb.append(" WHERE  isnull(h.hmark2,'') in('FW','WX','LJ') AND h.hrecsta <> 'W' AND h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" AND  h.facno='${facno}' ");
        sb.append(" and d.drecsta not in ('98','99','10') ");
        if (!"".equals(userid)) {
            sb.append(" and h.mancode ='").append(userid).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
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

        String cdrdmas = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            tram = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tram;
    }

    @Override
    public BigDecimal getNotDelivery(Date d, LinkedHashMap<String, Object> map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private DSALPBean lookupDsalpBean() {
        try {
            Context c = new InitialContext();
            return (DSALPBean) c.lookup("java:global/KPI/KPI-ejb/DSALPBean!cn.hanbell.kpi.ejb.crm.DSALPBean");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
