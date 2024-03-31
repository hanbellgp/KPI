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
public class SalesOrderAmountR1C2 extends SalesOrderAmount {

    public SalesOrderAmountR1C2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1C000','1C100' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='JN' ");
        queryParams.put("n_code_DC", " ='H' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal amount1, amount2;
        amount1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "J");
        amount2 = super.getValue(y, m, d, type, map);
        return amount1.add(amount2);
    }

}
