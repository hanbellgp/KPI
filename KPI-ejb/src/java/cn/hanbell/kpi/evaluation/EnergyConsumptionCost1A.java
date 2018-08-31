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
public class EnergyConsumptionCost1A extends EnergyConsumptionCost {

    public EnergyConsumptionCost1A() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-A3-2 2#车间动力') ");
        //减持项
        queryParams.put("subtractitem", " in('真空生产测试区','真空研发测试区') ");

    }
}
