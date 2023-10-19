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
public class ShipmentAmountP7 extends ShipmentAmount9 {

    public ShipmentAmountP7() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='P' ");
       queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DD", " ='03' ");
    }
}
