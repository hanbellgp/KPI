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
public class ShipmentAmountR1B5 extends ShipmentAmount {

    public ShipmentAmountR1B5() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1B000','1B100','1F700'");
        //queryParams.put("decode", "1");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='HD' ");
        queryParams.put("n_code_DC", " ='Z' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
