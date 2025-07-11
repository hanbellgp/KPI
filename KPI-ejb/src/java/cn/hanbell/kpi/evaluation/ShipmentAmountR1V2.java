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
public class ShipmentAmountR1V2 extends ShipmentAmount {

    public ShipmentAmountR1V2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1V000' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='CQ' ");
        queryParams.put("n_code_DC", " ='H' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

  @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            BigDecimal temp1, temp2;
   
        //SHB ERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "C4");
        //GZ ERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //SHB + GZ
         Field f;
            String mon;
            Double a2, a4;
            mon = getIndicatorBean().getIndicatorColumn("N", m);
           String deptno = queryParams.get("deptno") != null ? queryParams.get("deptno").toString() : "";
            Indicator indicator = getIndicatorBean().findByFormidYearAndDeptno("R-重庆热泵R均价", y, "1F000");

            // //分公司卖出后新增到华东的台数
            IndicatorDetail o2 = indicator.getOther2Indicator();
            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

            //华东卖出后新增到分公司的台数
            IndicatorDetail o4 = indicator.getOther4Indicator();
            f = o4.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a4 = Double.valueOf(f.get(o4).toString());

             //华东销售台数=华东销售台数+分公司卖出后新增到华东-华东卖给分公司的台数
        return temp1.add(temp2).add(BigDecimal.valueOf(a2 - a4));
         } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ShipmentQuantityR1D1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}
