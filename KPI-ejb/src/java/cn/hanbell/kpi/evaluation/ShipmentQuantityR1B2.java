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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C0160
 */
public class ShipmentQuantityR1B2 extends ShipmentQuantity {

    public ShipmentQuantityR1B2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1B000' ");
        //queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='HD' ");
        queryParams.put("n_code_DC", " ='H' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Field f;
            String mon;
            Double a1, a2;
            mon = getIndicatorBean().getIndicatorColumn("N", m);
            //华东销售台数
            BigDecimal saleCount1B = super.getValue(y, m, d, type, queryParams);
            String deptno = queryParams.get("deptno") != null ? queryParams.get("deptno").toString() : "";
            Indicator indicator = getIndicatorBean().findByFormidYearAndDeptno("R-华东热泵R均价", y, "1F000");

            // //分公司卖出后新增到华东的台数
            IndicatorDetail o1 = indicator.getOther1Indicator();
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            //华东卖出后新增到分公司的台数
            IndicatorDetail o2 = indicator.getOther3Indicator();
            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

             //华东销售台数=华东销售台数+分公司卖出后新增到华东-华东卖给分公司的台数
            return saleCount1B.add(BigDecimal.valueOf(a1 - a2));
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ShipmentQuantityR1B2.class.getName()).log(Level.SEVERE, null, ex);
        }
  
        return BigDecimal.ZERO;
    }

}
