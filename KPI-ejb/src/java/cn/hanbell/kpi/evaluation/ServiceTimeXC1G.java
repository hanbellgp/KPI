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
public class ServiceTimeXC1G extends ServiceTime {

    public ServiceTimeXC1G() {
        super();
        queryParams.put("deptno", "like '1G%'");
        queryParams.put("status", "");
    }

}
