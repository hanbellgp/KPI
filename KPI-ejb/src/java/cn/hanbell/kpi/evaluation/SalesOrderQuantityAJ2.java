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
public class SalesOrderQuantityAJ2 extends SalesOrderQuantity{

    public SalesOrderQuantityAJ2() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T100' ");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " LIKE 'WX%' ");
        queryParams.put("n_code_DC", " LIKE 'AJ%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}