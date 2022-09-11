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
public class ShipmentAmountRVN extends ShipmentAmountVN {

    public ShipmentAmountRVN() {
        super();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='R'");
        queryParams.put("hmark2", " ='ZJ'");
    }
}
