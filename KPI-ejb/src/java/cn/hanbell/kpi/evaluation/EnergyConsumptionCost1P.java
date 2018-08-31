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
public class EnergyConsumptionCost1P extends EnergyConsumptionCost {

    public EnergyConsumptionCost1P() {
        super();
        queryParams.put("facno", "C");
        //加持项
        queryParams.put("additem", " in ('机体装配总柜','2#机体测试500KW','空压机组测试总柜','空压机组测试总柜') ");
        //减持项
        queryParams.put("subtractitem", " in ('机体装配总柜','2楼机体测试（总）') ");
        //其他条件
        queryParams.put("condition", " * 0.7 ");
        //其他加持项
        queryParams.put("otheritem", " in ('2#屋顶动力') ");

    }

}
