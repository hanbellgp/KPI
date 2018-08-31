/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 管理部
 */
public class EnergyConsumptionkWh1L extends EnergyConsumptionkWh {

    public EnergyConsumptionkWh1L() {
        super();
        queryParams.put("facno", "C");
        //其他条件
        queryParams.put("condition", " * 0.3 ");
        //其他加持项
        queryParams.put("otheritem", " in('2#屋顶动力') ");

    }
}