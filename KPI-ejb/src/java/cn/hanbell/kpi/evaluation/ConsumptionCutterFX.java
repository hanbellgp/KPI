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
 * @author C1879 得出实绩值
 */
public class ConsumptionCutterFX extends ConsumptionCutter {

    IndicatorBean indicatorBean = lookupIndicatorBeanBean();

    public ConsumptionCutterFX() {
        super();
        queryParams.put("formid", "A-方型件刀具耗用");
        queryParams.put("deptno", "1P100");
    }

    //得到Other1值与Other4的值相加
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Double a1, a4;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getOther1Indicator();
        IndicatorDetail o4 = i.getOther4Indicator();
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            f = o4.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a4 = Double.valueOf(f.get(o4).toString());

            if (a1 == 0.00) {
                v1 = BigDecimal.valueOf(0);
            } else {
                v1 = BigDecimal.valueOf(a4 / a1).divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
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