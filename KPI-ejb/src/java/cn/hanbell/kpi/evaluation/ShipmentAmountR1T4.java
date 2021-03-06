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
public class ShipmentAmountR1T4 extends ShipmentAmount {

    public ShipmentAmountR1T4() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000','1T100' ");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("n_code_CD", "  LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='RT' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getARM423Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }

}
