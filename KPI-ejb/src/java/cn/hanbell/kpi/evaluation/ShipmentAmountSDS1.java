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
public class ShipmentAmountSDS1 extends ShipmentAmount {

    public ShipmentAmountSDS1() {
        super();
        queryParams.put("facno", "C");
        //queryParams.put("decode", "1");
        queryParams.put("deptno", " '1Q000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " ='SDS'");
        queryParams.put("n_code_DD", " in ('00','02') ");
    }

    @Override
    public BigDecimal getARM270Value(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return BigDecimal.ZERO;
    }
    
}
