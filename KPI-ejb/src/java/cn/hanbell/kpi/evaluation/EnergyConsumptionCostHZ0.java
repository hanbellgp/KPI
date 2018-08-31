/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 汉钟总厂
 */
public class EnergyConsumptionCostHZ0 extends EnergyConsumptionCost {

    public EnergyConsumptionCostHZ0() {
        super();
        queryParams.put("facno", "C");
        //加持项 查询全部
        queryParams.put("additem", "all");
    }

}