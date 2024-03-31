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
public class SalesOrderQuantityR1T5 extends SalesOrderQuantity{

    public SalesOrderQuantityR1T5() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T000','1T100' ");
        queryParams.put("decode", "2");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", "  LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='Z' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
