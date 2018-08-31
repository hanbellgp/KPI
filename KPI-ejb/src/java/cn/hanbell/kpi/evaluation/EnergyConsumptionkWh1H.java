/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 真空部
 */
public class EnergyConsumptionkWh1H extends EnergyConsumptionkWh {

    public EnergyConsumptionkWh1H() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('真空生产测试区','真空研发测试区','DP-A6-1 45KW2#车间照明') ");

    }
}
