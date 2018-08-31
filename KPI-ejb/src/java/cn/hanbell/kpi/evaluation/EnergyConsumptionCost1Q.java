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
public class EnergyConsumptionCost1Q extends EnergyConsumptionCost {

    public EnergyConsumptionCost1Q() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-A4-2 5#车间动力','DP-A6-4 45KW5#车间照明','DP-B6-1 涂装线冰水机') ");

    }

}
