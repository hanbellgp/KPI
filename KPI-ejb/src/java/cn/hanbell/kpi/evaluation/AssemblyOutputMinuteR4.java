/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * R冷媒装配4号车间产出工时
 */
public class AssemblyOutputMinuteR4 extends ProductivityOutputMinute {

    public AssemblyOutputMinuteR4() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*制作流程号
        queryParams.put("prosscode", " IN ('RZ01','RZ02','RZ03','RZ05','RZ06','FRZ07') ");
        //* 生产线别
        queryParams.put("linecode", " IN ('RC') ");
        //状态码 1未确认 2 确认 3作废
        queryParams.put("stats", " IN ('2') ");
    }
}
