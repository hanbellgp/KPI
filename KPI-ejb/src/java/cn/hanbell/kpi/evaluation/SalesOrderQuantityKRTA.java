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
public class SalesOrderQuantityKRTA extends SalesOrderQuantity{

    public SalesOrderQuantityKRTA() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5C000' ");
        queryParams.put("n_code_DA", " ='RT' ");
        queryParams.put("n_code_DC", " ='RTA' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
