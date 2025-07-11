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
public class ShipmentQuantityAJ8 extends ShipmentQuantity {

    public ShipmentQuantityAJ8() {
        super();
        // A机体总部外销出货台数
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " ='WXYD' ");
        queryParams.put("n_code_DC", " NOT LIKE 'SAM%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }
}
