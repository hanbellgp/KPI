/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C2082
 */
public class ShipmentAmountP91X extends ShipmentAmount9{

    public ShipmentAmountP91X() {
        super();
        queryParams.put("facno", "F");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='P' ");
    }

}
