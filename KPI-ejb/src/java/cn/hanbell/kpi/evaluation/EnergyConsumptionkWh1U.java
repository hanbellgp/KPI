/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 涡旋
 */
public class EnergyConsumptionkWh1U extends EnergyConsumptionkWh {

    public EnergyConsumptionkWh1U() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-A3-1 1#车间动力','DP-A5-1 30KW1#车间照明') ");
        //其他条件
        queryParams.put("condition", " * 0.7 ");
        //其他加持项
        queryParams.put("otheritem", " in('DP-B5-2 1#楼屋顶') ");

    }
}
