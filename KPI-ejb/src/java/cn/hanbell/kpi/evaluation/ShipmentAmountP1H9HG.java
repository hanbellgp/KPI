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
public class ShipmentAmountP1H9HG extends ShipmentAmount9 {

    public ShipmentAmountP1H9HG() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='P' ");
//        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DD", "  in ('07') ");
    }

}
