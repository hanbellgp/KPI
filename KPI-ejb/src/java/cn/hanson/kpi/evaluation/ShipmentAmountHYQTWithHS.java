/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class ShipmentAmountHYQTWithHS extends ShipmentAmount {

    public ShipmentAmountHYQTWithHS() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("protype", " in ('QT')");
    }

}
