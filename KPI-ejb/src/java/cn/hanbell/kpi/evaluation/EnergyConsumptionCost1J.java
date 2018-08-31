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
public class EnergyConsumptionCost1J extends EnergyConsumptionCost {

    public EnergyConsumptionCost1J() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-A4-1 3#车间动力') ");

    }

}
