/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class VarietyAmtsMDHTV3 extends ShipmentDelyAmts {

    public VarietyAmtsMDHTV3() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("houtsta", "not in ('W')");
        queryParams.put("variety", "MDHT");
    }

}