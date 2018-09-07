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
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author C1879 R冷媒装配同期增长率
 */
public class AssemblyGrowthRateR extends Productivity {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public AssemblyGrowthRateR() {
        super();
        queryParams.put("formid", "R-R冷媒装配");
        queryParams.put("deptno", "1W000");
    }

    //得到Other1（总投入工时（分钟））与Other2的值（产出工时（分钟））、同期值
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        mon = indicatorBean.getIndicatorColumn("N", m);
        Field f;
        BigDecimal v1;
        Double a1, a2, a3;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o2 = i.getOther2Indicator();
        //同期值
        IndicatorDetail benchmark = i.getBenchmarkIndicator();
        try {
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

            f = benchmark.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a3 = Double.valueOf(f.get(benchmark).toString());

            //增长率公式
            if (a3 == 0.00 || a1 == 0.00) {
                v1 = BigDecimal.valueOf(0);
            } else {
                v1 = BigDecimal.valueOf((a2 / a1 * 100 - a3) / a3 * 100).divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
            }

            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
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
