/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C1749 实际值 金额
 */
public class VarietyAmtsJTV1 extends ShipmentAmount {

    public VarietyAmtsJTV1() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("variety", "JT");
    }

}
