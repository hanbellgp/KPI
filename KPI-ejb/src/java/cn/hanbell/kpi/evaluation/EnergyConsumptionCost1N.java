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
public class EnergyConsumptionCost1N extends EnergyConsumptionCost {

    public EnergyConsumptionCost1N() {
        super();
        queryParams.put("facno", "C");
        //加持项
        queryParams.put("additem", " in('自动仓储','3#车间总柜') ");
        //减持项
        queryParams.put("subtractitem", " in('3#冷媒测试小系统','3#冷媒测试周边测试','3#冷媒测试大系统1','3#冷媒测试大系统2') ");

    }

}
