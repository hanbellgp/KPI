/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C2082 柯茂机体部分
 */
public class ProductionPlanCompleteKMAH extends ProductionPlanComplete{

    public ProductionPlanCompleteKMAH() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        queryParams.put("stats", "2");
        queryParams.put("linecode", " IN('AT','ORC','RT','A8','A9') ");
        queryParams.put("typecode", " ='01' ");
        queryParams.put("prosscode", " IN ('RT07') ");
        queryParams.put("wrcode", " IN ('WL1') ");
        queryParams.put("itcls", " in ('3J76','3J79','3J80') ");
    }
}
