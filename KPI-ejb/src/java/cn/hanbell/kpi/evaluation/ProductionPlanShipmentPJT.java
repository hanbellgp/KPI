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
public class ProductionPlanShipmentPJT extends ProductionPlanShipment {

    public ProductionPlanShipmentPJT() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DD", " ='00' ");
        queryParams.put("itcls", " in ('6053','3376','3476','3379','3380','3480') ");
    }
}
