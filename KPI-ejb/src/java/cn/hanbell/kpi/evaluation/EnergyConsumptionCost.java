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
 * @author C1879
 */
public class EnergyConsumptionCost extends EnergyConsumption {

    public BigDecimal addItem(int y, int m, Date d, int type, String description, String facno) {
        BigDecimal charge = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(SUM (KWH * UnitElectricCost ),0) FROM ElectricDayReport e INNER JOIN DayReportPara d ON e.TagID=d.ItemName ");
        sb.append(" WHERE  1=1 ");
        if (!"all".equals(description)) {
            sb.append(" and d.Description ").append(description);
        }
        sb.append(" and year(e.RecTime) = ${y} and month(e.RecTime)= ${m} ");
        switch (type) {
            case 2:
                sb.append(" and e.RecTime<= '${d}' ");
                break;
            case 5:
                sb.append(" and e.RecTime= '${d}' ");
                break;
            default:
                sb.append(" and e.RecTime<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        superEEP.setCompany(facno);
        Query query = superEEP.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            charge = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return charge;
    }
    //特殊情况项用电量的占比

    public BigDecimal otherItem(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String otheritem = map.get("otheritem") != null ? map.get("otheritem").toString() : "";
        String condition = map.get("condition") != null ? map.get("condition").toString() : "";
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal charge = BigDecimal.ZERO;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT isnull(SUM( KWH * UnitElectricCost),0) FROM ElectricDayReport e INNER JOIN DayReportPara d ON e.TagID=d.ItemName ");
        sb.append(" WHERE  d.Description ").append(otheritem);
        sb.append(" and year(e.RecTime) = ${y} and month(e.RecTime)= ${m} ");
        switch (type) {
            case 2:
                sb.append(" and e.RecTime<= '${d}' ");
                break;
            case 5:
                sb.append(" and e.RecTime= '${d}' ");
                break;
            default:
                sb.append(" and e.RecTime<= '${d}' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${condition}", condition);
        superEEP.setCompany(facno);
        Query query = superEEP.getEntityManager().createNativeQuery(sql);
        try {
            Object o1 = query.getSingleResult();
            charge = BigDecimal.valueOf(Double.parseDouble(o1.toString()));
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return charge;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        //获得查询参数
        String additem = map.get("additem") != null ? map.get("additem").toString() : "";
        String subtractitem = map.get("subtractitem") != null ? map.get("subtractitem").toString() : "";
        String otheritem = map.get("otheritem") != null ? map.get("otheritem").toString() : "";
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        BigDecimal kwhcharge = BigDecimal.ZERO;
        BigDecimal a1 = BigDecimal.ZERO;
        BigDecimal a2 = BigDecimal.ZERO;
        BigDecimal a3 = BigDecimal.ZERO;
        try {
            if (!"".equals(additem)) {
                a1 = addItem(y, m, d, type, additem, facno);
            }
            if (!"".equals(subtractitem)) {
                a2 = addItem(y, m, d, type, subtractitem, facno);
            }
            if (!"".equals(otheritem)) {
                a3 = otherItem(y, m, d, type, map);
            }
            kwhcharge = a1.subtract(a2).add(a3);
            return kwhcharge;
        } catch (Exception ex) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return kwhcharge;
    }

}
