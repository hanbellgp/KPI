/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class ShipmentQuantityR1B4 extends ShipmentQuantity {

    public ShipmentQuantityR1B4() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("deptno", " in ('1B000','1F700','1F800') ");
        //queryParams.put("decode", "1");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " ='HD' ");
        queryParams.put("n_code_DC", " ='Z' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
