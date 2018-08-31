/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 一厂管理
 */
public class EnergyConsumptionkWh2E extends EnergyConsumptionkWh {

    public EnergyConsumptionkWh2E() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-A3-1 1#车间动力','DP-A6-5 50KW1#门卫','DP-A6-6 10KW2#门卫') ");
        //其他条件
        queryParams.put("condition", " * 0.3 ");
        //其他加持项
        queryParams.put("otheritem", " in('DP-B5-2 1#楼屋顶') ");

    }
}