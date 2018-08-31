/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 枫泾一厂
 */
public class EnergyConsumptionCostHZ1 extends EnergyConsumptionCost {

    public EnergyConsumptionCostHZ1() {
        super();
        queryParams.put("facno", "E");
        //加持项 查询全部
        queryParams.put("additem", "all");
    }

}