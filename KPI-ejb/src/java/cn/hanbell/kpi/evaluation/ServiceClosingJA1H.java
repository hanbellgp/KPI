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
public class ServiceClosingJA1H extends ServiceClosing {

    public ServiceClosingJA1H() {
        super();
        queryParams.put("deptno", "1H");
        queryParams.put("status", "JA");
    }

}
