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
public class ShipmentAmountR1D5 extends ShipmentAmount {

    public ShipmentAmountR1D5() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1D000','1D100' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='GZ' ");
        queryParams.put("n_code_DC", " ='Z' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        //SHB ERP
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "G");
        //GZ ERP
        temp2 = super.getValue(y, m, d, type, queryParams);
        //SHB + GZ
        return temp1.add(temp2);
    }

}
