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
public class ShipmentAmountAJB extends ShipmentAmount {

    public ShipmentAmountAJB() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1G000','1G100' ");
        queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " = 'AJB' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
    
}
