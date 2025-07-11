/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.util.LinkedHashMap;

/**
 *
 * @author C0160
 */
public class ShipmentAmountAJ1T9 extends ShipmentAmountAJ1G9 {

    public ShipmentAmountAJ1T9() {
        queryParams = new LinkedHashMap<>();
        queryParams.put("facno", "C");
//        queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " like 'WX%'");
        queryParams.put("n_code_DC", " NOT LIKE 'SAM%' ");
        queryParams.put("n_code_DD", " ='07' ");
    }


}
