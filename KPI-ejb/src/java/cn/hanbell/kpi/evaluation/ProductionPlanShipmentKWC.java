/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 */
public class ProductionPlanShipmentKWC extends ProductionPlanShipment {

    public ProductionPlanShipmentKWC() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("n_code_DC", " ='WC' ");
        queryParams.put("itcls", " IN('3W76','3W79','3W80') ");
    }
}
