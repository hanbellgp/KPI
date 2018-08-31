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
public class EnergyConsumptionCost1F extends EnergyConsumptionCost {

    public EnergyConsumptionCost1F() {
        super();
        queryParams.put("facno", "C");
         //加持项
        queryParams.put("additem", " in('4#车间总柜','4#冷媒测试2','4#冷媒测试1#','4#特殊电压测试','4# R134a测试','3#冷媒测试小系统','3#冷媒测试大系统','3#冷媒测试大系统2','服务厂房总柜') ");
        
    }

}
