/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 技术中心
 */
public class EnergyConsumptionkWh1J extends EnergyConsumptionkWh {

    public EnergyConsumptionkWh1J() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-A4-1 3#车间动力') ");


    }
}

