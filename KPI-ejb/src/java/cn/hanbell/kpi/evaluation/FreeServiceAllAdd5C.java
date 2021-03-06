/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1879
 */
public class FreeServiceAllAdd5C extends FreeServiceAllAdd {

    public FreeServiceAllAdd5C() {
        super();
        queryParams.put("OuterFormid", "A-涡轮服务成本");
        queryParams.put("deptno", "5C000");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        Double a1;
        Indicator i1 = indicatorBean.findByFormidYearAndDeptno(map.get("OuterFormid").toString(), y, map.get("deptno").toString());

        try {
            IndicatorDetail o1 = i1.getOther5Indicator();

            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            v1 = BigDecimal.valueOf(a1);

            return v1;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            indicatorBean.getLog4j().error(ex);
        }
        return BigDecimal.ZERO;

    }

}
