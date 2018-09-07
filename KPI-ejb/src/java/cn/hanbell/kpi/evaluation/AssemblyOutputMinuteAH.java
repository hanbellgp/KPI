/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * A机体专装配产出工时
 */
public class AssemblyOutputMinuteAH extends ProductivityOutputMinute{

    public AssemblyOutputMinuteAH() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*制作流程号
        queryParams.put("prosscode", " IN ('AA01','AA02','AA03','AH03','AA04','AA05','AA06','AA07','AA08','FAA09') ");
        //* 生产线别
        queryParams.put("linecode", " IN ('AH') ");
        //状态码 1未确认 2 确认 3作废
        queryParams.put("stats", " IN ('2') ");
    }
}