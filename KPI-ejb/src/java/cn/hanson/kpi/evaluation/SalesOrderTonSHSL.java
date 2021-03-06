/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 上海三菱订单数量
 *
 * @author C0160
 */
public class SalesOrderTonSHSL extends SalesOrderTon {

    public SalesOrderTonSHSL() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("cusno", " ='HSH00005' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal quantity1, quantity2 = BigDecimal.ZERO;
        quantity1 = super.getValue(y, m, d, type, map);
        return quantity1.add(quantity2);
    }

}
