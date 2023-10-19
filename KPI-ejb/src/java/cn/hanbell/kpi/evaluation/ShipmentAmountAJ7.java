/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C2082
 */
public class ShipmentAmountAJ7 extends ShipmentAmount {

    public ShipmentAmountAJ7() {
        super();
        // A机体总部外销出货台数
        queryParams.put("facno", "C");
        queryParams.put("deptno", " '1T100' ");
        // queryParams.put("decode", "2");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " like 'WX%' ");
        queryParams.put("n_code_DC", " NOT LIKE 'SAM%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
