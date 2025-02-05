/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1879
 */
public class SalesOrderQuantityKCLZjComer extends SalesOrderQuantity {

    public SalesOrderQuantityKCLZjComer() {
        super();
        queryParams.put("facno", "E");
        queryParams.put("deptno", " '5B000' ");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", " ='OH' ");
        queryParams.put("n_code_DC", " ='CL' ");
        queryParams.put("n_code_DD", " ='00' ");
    }



}
