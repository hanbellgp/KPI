/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 * A机组装配产出工时
 */
public class AssemblyOutputMinuteAJZ extends ProductivityOutputMinute{

    public AssemblyOutputMinuteAJZ() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*制作流程号
        queryParams.put("prosscode", " IN ('AH01','AH04','AH05','AH03','AH06','AH07') ");
        //* 生产线别
        queryParams.put("linecode", " IN ('AJZ') ");
        //状态码 1未确认 2 确认 3作废
        queryParams.put("stats", " IN ('2') ");
    }
    
    
}
