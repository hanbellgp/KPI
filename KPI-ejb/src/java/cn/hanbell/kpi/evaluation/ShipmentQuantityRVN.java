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
public class ShipmentQuantityRVN extends ShipmentQuantityVN {

    public ShipmentQuantityRVN() {
        super();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " in ('R','DR')");
        queryParams.put("hmark2", " ='ZJ'");
    }

}
