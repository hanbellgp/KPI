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
public class ShipmentAmountR1V3 extends ShipmentAmount {

    public ShipmentAmountR1V3() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1V000' ");
        queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='CQ' ");
        queryParams.put("n_code_DC", " ='L' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHB ERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "C4");
        //CQ ERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //SHB + CQ
        return temp1.add(temp2);
    }

}
