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
public class SalesOrderAmountR1T3 extends SalesOrderAmount {

    public SalesOrderAmountR1T3() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000','1T100' ");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DC", " ='L' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}