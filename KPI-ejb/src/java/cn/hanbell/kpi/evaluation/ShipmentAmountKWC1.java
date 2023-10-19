/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C0160
 */
public class ShipmentAmountKWC1 extends ShipmentAmount {

    public ShipmentAmountKWC1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5B000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='OH' ");
//        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='WC' ");
        queryParams.put("n_code_DD", "  IN ('00','02') ");
    }

    @Override
    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //ComerERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("deptno");
        queryParams.put("facno", "E");
        queryParams.put("deptno", " '8A000' ");
        //ZJComerERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //ComerERP + ZJComerERP
        return temp1.add(temp2);
    }

}
