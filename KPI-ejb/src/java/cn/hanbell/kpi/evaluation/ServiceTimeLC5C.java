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
public class ServiceTimeLC5C extends ServiceTime {

    public ServiceTimeLC5C() {
        super();
        queryParams.put("deptno", "like '5C%'");
        queryParams.put("status", "LC");
    }

}
