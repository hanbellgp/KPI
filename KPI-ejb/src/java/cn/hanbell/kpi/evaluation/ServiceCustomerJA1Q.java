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
public class ServiceCustomerJA1Q extends ServiceCustomer {

    public ServiceCustomerJA1Q() {
        super();
        queryParams.put("deptno", "like '1Q%'");
        queryParams.put("status", "JA");
    }

}
