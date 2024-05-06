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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
public class SalesOrderQuantityAA extends SalesOrderQuantity {

    public SalesOrderQuantityAA() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String modelcode = map.get("modelcode") != null ? map.get("modelcode").toString() : "";
        BigDecimal cdrqy1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select isnull(sum(d.cdrqy1),0) from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno  ");
        sb.append(" LEFT JOIN cdritncusmap p on d.itnbrcus = p.itnbrcus ");
        sb.append(" where h.hrecsta <> 'W' and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        sb.append(" and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
        sb.append(" and d.drecsta not in ('98','99','10') ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        switch (modelcode) {
            case "QT":
                sb.append(" and p.modelcode = ").append("null");
                break;
            default:
                sb.append(" and p.modelcode ").append(modelcode);
                break;
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
            cdrqy1 = (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cdrqy1;

    }

    @Override
    public BigDecimal getNotDelivery(Date d, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String modelcode = map.get("modelcode") != null ? map.get("modelcode").toString() : "";

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(d.cdrqy1 - d.shpqy1),0) from cdrhmas h,cdrdmas d left join cdritncusmap p on d.itnbrcus = p.itnbrcus ");
        sb.append(" where h.facno=d.facno and h.cdrno=d.cdrno and h.hrecsta<>'W' ");
        //sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append(" and ((d.cdrqy1-d.shpqy1)>0) and d.drecsta<'95' and h.facno='${facno}' ");
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" and d.n_code_CD ").append(n_code_CD);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        if (!"".equals(modelcode)) {
            switch (modelcode) {
                case "QT":
                    sb.append(" and p.modelcode = ").append("null");
                    break;
                default:
                    sb.append(" and p.modelcode ").append(modelcode);
                    break;
            }
        }
        sb.append(" and h.recdate<= '${d}' ");

        String cdrdmas = sb.toString().replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(cdrdmas);
        try {
            Object o1 = query.getSingleResult();
            return (BigDecimal) o1;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}
