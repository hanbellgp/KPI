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
public class SalesOrderQuantityKHM1 extends SalesOrderQuantity{

    public SalesOrderQuantityKHM1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5C000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='RT' ");
//        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='HM' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
