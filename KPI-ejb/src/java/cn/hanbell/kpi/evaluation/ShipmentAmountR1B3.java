/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.ejb.IndicatorBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
public class ShipmentAmountR1B3 extends ShipmentAmount {
IndicatorBean indicatorBean = getIndicatorBean();
    public ShipmentAmountR1B3() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1B000','1B100' ");
        //queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='HD' ");
        queryParams.put("n_code_DC", " ='L' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("ogdkid", "RL01");
    }
 @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Field f;
            String mon;
            Double a1, a2;
            mon = indicatorBean.getIndicatorColumn("N", m);
            //华东销售金额
            BigDecimal saleCount1B = super.getValue(y, m, d, type, queryParams);
            String deptno = queryParams.get("deptno") != null ? queryParams.get("deptno").toString() : "";
            Indicator indicator = indicatorBean.findByFormidYearAndDeptno("R-华东冷冻R均价", y, "1F000");

            //分公司卖出后新增到华东的金额
            IndicatorDetail o1 = indicator.getOther2Indicator();
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());

            //华东卖出后新增到分公司的金额
            IndicatorDetail o2 = indicator.getOther4Indicator();
            f = o2.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a2 = Double.valueOf(f.get(o2).toString());

            //华东销售金额=华东销售金额+分公司卖出后新增到华东-华东卖给分公司的金额
            return saleCount1B.add(BigDecimal.valueOf(a1 - a2));
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(ShipmentQuantityR1B2.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }
}
