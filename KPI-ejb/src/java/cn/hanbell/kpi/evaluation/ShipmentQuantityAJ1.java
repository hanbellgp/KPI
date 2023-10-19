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
public class ShipmentQuantityAJ1 extends ShipmentQuantity {

    public ShipmentQuantityAJ1() {
        super();
        // A机体总部内销出货台数
        queryParams.put("facno", "C");
        // queryParams.put("decode", "1");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " not in ('WX','WXTW','WXOZ','WXHG','GZ') ");
        queryParams.put("n_code_DC", " NOT LIKE 'SAM%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
