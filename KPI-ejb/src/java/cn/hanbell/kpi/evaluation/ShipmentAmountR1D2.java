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
public class ShipmentAmountR1D2 extends ShipmentAmount {

    public ShipmentAmountR1D2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1D000','1D100' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='GZ' ");
        queryParams.put("n_code_DC", " ='H' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        Field f;
        String mon;
        Double a;
        //SHB ERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "G");
        //GZ ERP
        temp2 = super.getValue(y, m, d, type, queryParams);

        //KPI中卖到各个分公司中的数据,由制冷维护金额
        Indicator indicator = getIndicatorBean().findByFormidYearAndDeptno("R-广州R均价", y, "1F000");
        try {
            IndicatorDetail o = indicator.getOther2Indicator();
            mon = getIndicatorBean().getIndicatorColumn("N", m);
            f = o.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a = Double.valueOf(f.get(o).toString());
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            a = 0.0;
            Logger.getLogger(ShipmentAmountR1E2.class.getName()).log(Level.SEVERE, null, ex);
        }

        //SHB + GZ +   产品后续由分公司卖出的数据（广州R销售均价）
        return temp1.add(temp2).add(BigDecimal.valueOf(a));
    }
}
