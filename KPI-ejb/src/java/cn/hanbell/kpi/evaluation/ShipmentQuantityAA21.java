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
 * @author C0160
 */
public class ShipmentQuantityAA21 extends ShipmentQuantityAA {

    public ShipmentQuantityAA21() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000','1T100' ");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("n_code_CD", " ='WX' ");
        queryParams.put("n_code_DC", " <> 'SDS' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //2026-4-13 国际营销课卖机组'C2915','C2718' 算国际营销课业绩
        //出货=销售-退货
        //机体整机
 //获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String deptno = map.get("deptno") != null ? map.get("deptno").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
        String modelcode = map.get("modelcode") != null ? map.get("modelcode").toString() : "";

        BigDecimal shpqy1 = BigDecimal.ZERO;
        BigDecimal bshpqy1 = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append("select isnull(sum(d.shpqy1),0) from cdrhad h,cdrdta d,cdrdmas cd left join  cdritncusmap p on cd.itnbrcus = p.itnbrcus ");
        sb.append(" where h.facno=d.facno and h.shpno=d.shpno and d.facno = cd.facno and d.cdrno = cd.cdrno and d.ctrseq = cd.trseq and h.houtsta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append("  and d.issevdta='N' and h.facno='${facno}' ");
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
        sb.append(" and h.mancode  in('C2915','C2718')");
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.shpdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and h.shpdate<= '${d}' ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

         sb.setLength(0);
        sb.append("select isnull(sum(d.bshpqy1),0) from cdrbhad h,cdrbdta d,cdrdmas cd left join  cdritncusmap p on cd.itnbrcus = p.itnbrcus  ");
        sb.append(" where h.facno=d.facno and h.bakno=d.bakno and d.cdrno = cd.cdrno and d.ctrseq = cd.trseq and h.baksta<>'W' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') ");
        sb.append("  and d.issevdta='N' and h.facno='${facno}' ");
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
        sb.append(" and h.mancode  in('C2915','C2718')");
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        switch (type) {
            case 2:
                //月
                sb.append(" and h.bakdate<= '${d}' ");
                break;
            case 5:
                //日
                sb.append(" and h.bakdate= '${d}' ");
                break;
            default:
                sb.append(" and h.bakdate<= '${d}' ");
        }
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        try {
            Object o1 = query1.getSingleResult();
            Object o2 = query2.getSingleResult();
            shpqy1 = (BigDecimal) o1;
            bshpqy1 = (BigDecimal) o2;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return shpqy1.subtract(bshpqy1);
    }
   
}
