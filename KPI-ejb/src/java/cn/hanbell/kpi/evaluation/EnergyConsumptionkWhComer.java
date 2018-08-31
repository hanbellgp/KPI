/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * 柯茂
 */
public class EnergyConsumptionkWhComer extends EnergyConsumptionkWh {

    public EnergyConsumptionkWhComer() {
        super();
        queryParams.put("facno", "E");
        //加持项
        queryParams.put("additem", " in('DP-C3 离心机组测试','DP-C4 标准电压测试(现场) 测试辅助设备','DP-A6-3 20KW4#车间照明') ");

    }
}
