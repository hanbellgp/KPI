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
public class ServiceAmountSF1H extends ServiceAmount {

    public ServiceAmountSF1H() {
        super();
        queryParams.put("deptno", "like '1H%'");
        queryParams.put("status", "Y");
    }

}
