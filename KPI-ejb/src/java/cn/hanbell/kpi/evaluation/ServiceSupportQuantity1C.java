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
public class ServiceSupportQuantity1C extends ServiceSupportQuantity {

    public ServiceSupportQuantity1C() {
        super();
        queryParams.put("deptno", "like '1C%'");
        queryParams.put("status", "");
    }

}
