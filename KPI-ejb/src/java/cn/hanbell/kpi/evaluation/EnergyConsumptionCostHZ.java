/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author C1879
 */
public class EnergyConsumptionCostHZ extends EnergyConsumptionCost {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public EnergyConsumptionCostHZ() {
        super();
        queryParams.put("formid0", "K-总厂能耗");
        queryParams.put("formid1", "K-枫泾一厂能耗");
        queryParams.put("deptno", "1W000");
    }

    //获得总厂电费
    public BigDecimal getHZ0(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid0").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getActualIndicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            v1 = BigDecimal.valueOf(Double.valueOf(f.get(o1).toString()));
            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProcessQuantityHFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    //获得枫泾一厂电费
    public BigDecimal getHZ1(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid1").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getActualIndicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            v1 = BigDecimal.valueOf(Double.valueOf(f.get(o1).toString()));
            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ProcessQuantityHFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

    //得到总厂与枫泾一厂的电费
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal v1 = BigDecimal.ZERO;
        try {
            return v1.add(getHZ0(y, m, d, type, map)).add(getHZ1(y, m, d, type, map));
        } catch (Exception ex) {
            Logger.getLogger(ProcessQuantityHFX.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
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
