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
public class ShipmentAmountKHG1 extends ShipmentAmount {

    public ShipmentAmountKHG1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5B000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DC", " ='HG' ");
        queryParams.put("n_code_DD", "  IN ('00','02') ");
    }

}
