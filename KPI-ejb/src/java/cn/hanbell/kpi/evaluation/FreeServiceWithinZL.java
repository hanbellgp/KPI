/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879
 * //维修成本（厂内）制令领退料 
 */
public class FreeServiceWithinZL extends FreeServiceERP{

    public FreeServiceWithinZL() {
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        //产品类别
        String remark1 = map.get("remark1") != null ? map.get("remark1").toString() : "";
        //领料
        BigDecimal man430 = BigDecimal.ZERO;
        //退料
        BigDecimal man530 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(sum(cost),0) FROM ( ");
        sb.append(" SELECT  a.facno,a.prono,a.pisno,a.typecode,a.kfdh,sum(ttmatm) as cost,a.remark1 FROM ");
        sb.append(" (SELECT manpih.facno,manpih.prono,manpih.pisno,manpih.trtype,manpih.iocode,manpih.reascode,manpih.issdepno,manpih.itnbrf ");
        sb.append(" ,manpid.seqnr,manmas.typecode,manmas.kfdh,manmas.remark1 FROM manpid left outer join invmas s on s.itnbr=manpid.altitnbr, ");
        sb.append(" manpih left outer join invmas t on t.itnbr=manpih.itnbrf left outer join manmas on manmas.facno=manpih.facno and  manmas.prono=manpih.prono and manmas.manno=manpih.manno ");
        sb.append(" where  (manpih.facno= manpid.facno) and (manpih.prono = manpid.prono) and (manpih.pisno = manpid.pisno)   AND (manpih.issstatus = 'C') ");
        sb.append(" and manpid.altitnbr <> '31088-GBR6254-FW' and manpih.facno = '${facno}' and manpih.prono='1' and manpih.reascode in('1003','1004')");
        sb.append(" and manpid.wareh not in ('EKF03','EKF01','EZK01','EZK03','EAKF03') and manmas.typecode in ('02','05') ");
        if (!"".equals(remark1)) {
            sb.append(" AND  manmas.remark1 ").append(remark1);
        }
        sb.append(" and year(manpih.issdate) = ${y} and month(manpih.issdate)= ${m} ");
        sb.append(" ) as a LEFT JOIN invtrnh b on a.facno=b.facno and a.trtype=b.trtype and a.pisno=b.trno and a.seqnr=b.trseq ");
        sb.append(" WHERE b.facno='${facno}' AND b.prono='1' GROUP BY  a.facno,a.prono,a.pisno,a.typecode,a.kfdh,a.remark1) as c ");
        String sqlman430 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        sb.setLength(0);
        sb.append(" SELECT isnull(-sum(cost),0) FROM ( ");
        sb.append(" SELECT a.facno,a.prono,a.manno,a.typecode,a.kfdh,sum(ttmatm) as cost,a.remark1 FROM ");
        sb.append(" (SELECT manreh.facno, manreh.prono,manreh.manno,manreh.itnbrf,manreh.retdepno, manreh.trtype,manreh.retno,manreh.iocode,manred.seqnr, ");
        sb.append(" manmas.typecode,manmas.kfdh ,manmas.remark1 FROM  manred left outer join invmas s on s.itnbr=manred.altitnbr, ");
        sb.append(" manreh left outer join invmas t on t.itnbr=manreh.itnbrf ");
        sb.append(" left outer join manmas on manmas.facno=manreh.facno and  manmas.prono=manreh.prono and manmas.manno=manreh.manno ");
        sb.append(" WHERE ( manreh.facno = manred.facno ) and( manreh.prono = manred.prono ) and( manreh.retno = manred.retno ) and  ( manreh.issstatus = 'C') ");
        sb.append(" and manred.altitnbr <>'31088-GBR6254-FW' and manreh.facno='${facno}' and manreh.prono='1' and manred.reascode in('1003','1004')");
        sb.append(" and manred.wareh not in ('EKF03','EKF01','EZK01','EZK03','EAKF03') and manmas.typecode in ('02','05') ");
        if (!"".equals(remark1)) {
            sb.append(" AND  manmas.remark1 ").append(remark1);
        }
        sb.append(" and year(manreh.retdate) = ${y} and month(manreh.retdate)= ${m} ");
        sb.append(" )as a left join invtrnh b on a.facno=b.facno and a.trtype=b.trtype and a.retno=b.trno and a.seqnr=b.trseq ");
        sb.append("  WHERE b.facno='${facno}' AND b.prono='1' group by a.facno,a.prono,a.manno,a.typecode,a.kfdh,a.remark1) as c ");
        String sqlman530 = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(sqlman430);
        Query query2 = superEJB.getEntityManager().createNativeQuery(sqlman530);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            man430 = (BigDecimal) o1;
            man530 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return man430.add(man530);
    }
    
    
    
}
