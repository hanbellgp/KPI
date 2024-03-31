/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForEFGP;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public abstract class FreeServiceOuterOA implements Actual {

    protected SuperEJBForEFGP superEFGP = lookupSuperEJBForERP();

    protected LinkedHashMap<String, Object> queryParams;

    //服务成本（厂外）OA工作支援单（快递费和运费）
    protected BigDecimal workSF = BigDecimal.ZERO;
    //服务成本（厂外）退货通知单（吊装费、运费）
    protected BigDecimal returnSM = BigDecimal.ZERO;

    public FreeServiceOuterOA() {
        queryParams = new LinkedHashMap<>();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            workSF = getWorkSF(y, m, d, type, map);
            returnSM = getReturnSM(y, m, d, type, map);
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.evaluation.FreeserveOuter.getValue()" + e);
        }
        return workSF.add(returnSM);
    }

    //服务成本（厂外）OA工作支援单（快递费和运费）
    protected BigDecimal getWorkSF(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //申请部门
        String applydept = map.get("depno") != null ? map.get("depno").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(sum(a.total),0) FROM  ");
        sb.append(" ( SELECT  DISTINCT h5.* FROM  HK_FW005 h5 INNER JOIN  ProcessInstance pi on h5.processSerialNumber=pi.serialNumber ");
        sb.append(" INNER JOIN WorkItem wi on pi.contextOID=wi.contextOID  where ( h5.fwno <> '' and h5.fwno <> '-') and (h5.kfno <> '' and h5.kfno <> '-') ");
        sb.append(" AND pi.currentState =3 and h5.total <> 0 and h5.type in ('1','2','3') and h5.mftype <> '' ");
        if (!"".equals(applydept)) {
            sb.append("  and applydept ").append(applydept);
        }
        sb.append(" AND year(wi.completedTime) = ${y} and month(wi.completedTime)= ${m} ");
        sb.append(" and wi.completedTime=( select max(d.completedTime) from WorkItem d where wi.contextOID=d.contextOID ");
        sb.append("  AND year(d.completedTime) = ${y} and month(d.completedTime)>=${m} ) ");
        sb.append(" ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        Query query = superEFGP.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return BigDecimal.valueOf(Double.valueOf(o.toString()));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    //服务成本（厂外）退货通知单（吊装费、运费）
    protected BigDecimal getReturnSM(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //配合部门
        String supportdept = map.get("depno") != null ? map.get("depno").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT ISNULL(sum(a.yf),0) FROM  ");
        sb.append(" ( SELECT DISTINCT h6.* FROM HK_FW006 h6 INNER JOIN ProcessInstance pi on h6.processSerialNumber=pi.serialNumber ");
        sb.append(" INNER JOIN WorkItem wi on pi.contextOID= wi.contextOID WHERE ( h6.kfno <> '' and  h6.kfno <> '-') ");
        sb.append(" AND pi.currentState =3 and h6.yf>0 and h6.rettype = 2 and h6.returntype in ('2','4','8') ");
        if (!"".equals(supportdept)) {
            sb.append("  and h6.supportdept ").append(supportdept);
        }
        sb.append(" AND year(wi.completedTime) = ${y} and month(wi.completedTime)= ${m} "); 
        sb.append(" and wi.completedTime=( select max(d.completedTime) from WorkItem d where wi.contextOID=d.contextOID ");
        sb.append("  AND year(d.completedTime) = ${y} and month(d.completedTime)>=${m} ) ");
        sb.append(" ) as a ");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);
        Query query = superEFGP.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return BigDecimal.valueOf(Double.valueOf(o.toString()));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    protected SuperEJBForEFGP lookupSuperEJBForERP() {
        try {
            Context c = new InitialContext();
            return (SuperEJBForEFGP) c.lookup("java:global/KPI/KPI-ejb/SuperEJBForEFGP!cn.hanbell.kpi.comm.SuperEJBForEFGP");
        } catch (NamingException ne) {
            throw new RuntimeException(ne);
        }
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup("java:global/KPI/KPI-ejb/SuperEJBForEFGP!cn.hanbell.kpi.comm.SuperEJBForEFGP");
        superEFGP = (SuperEJBForEFGP) objRef;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        //每月3-5号自动更新上个月的数据
        int month;
        if (m == 1) {
            month = 12;
        } else {
            month = m - 1;
        }
        return month;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        //每月3-5号自动更新上个月的数据
        int year;
        if (m == 1) {
            year = y - 1;
        } else {
            year = y;
        }
        return year;
    }
}
