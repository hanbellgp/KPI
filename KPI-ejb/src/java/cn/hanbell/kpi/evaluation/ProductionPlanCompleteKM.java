/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 柯茂机组部分
 *
 */
public class ProductionPlanCompleteKM extends ProductionPlanComplete {

    public ProductionPlanCompleteKM() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " IN('AT','ORC','RT','A8','A9') ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " IN ('LX08','KQ10','OR08') ");
        queryParams.put("wrcode", " IN ('ZP01','ZP02','ZP03','WL2','ZS1') ");
        queryParams.put("itcls", " in ('3H76','3H79','3H80','3W80','3W76','3W79') ");
    }
}
