/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class ShipmentAmountAJ1 extends ShipmentAmount {

    public ShipmentAmountAJ1() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1G000','1G100' ");
        // queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " not in ('WX','WXTW','WXOZ','WXHG','GZ') ");
        queryParams.put("n_code_DC", " NOT LIKE 'SAM%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
