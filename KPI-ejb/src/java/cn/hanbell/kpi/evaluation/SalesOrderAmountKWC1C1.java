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
public class SalesOrderAmountKWC1C1 extends SalesOrderAmount {

    public SalesOrderAmountKWC1C1() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("deptno", " '5B000'");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='OH' ");
//        queryParams.put("n_code_CD", " NOT LIKE 'WX%' ");
        queryParams.put("n_code_DC", " ='WC' ");
        queryParams.put("n_code_DD", " In ('00','02') ");
    }
}
