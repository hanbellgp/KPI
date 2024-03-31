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
 * @author C1879
 */
public class SalesOrderQuantityR1V5 extends SalesOrderQuantity {

    public SalesOrderQuantityR1V5() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1V000' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='CQ' ");
        queryParams.put("n_code_DC", " ='Z' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal quantity1, quantity2;
        quantity1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "C4");
        quantity2 = super.getValue(y, m, d, type, map);
        return quantity1.add(quantity2);
    }

}
