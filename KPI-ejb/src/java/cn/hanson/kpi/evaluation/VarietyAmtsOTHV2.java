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
public class VarietyAmtsOTHV2 extends ShipmentPredictAmts {

    public VarietyAmtsOTHV2() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("variety", "OTH");
    }

}