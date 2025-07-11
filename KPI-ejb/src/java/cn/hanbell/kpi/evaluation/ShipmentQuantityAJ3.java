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
 * @author C0160
 */
public class ShipmentQuantityAJ3 extends ShipmentQuantity {

    public ShipmentQuantityAJ3() {
        super();
        // A机体总部内销出货台数
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", "='AH' ");
        queryParams.put("n_code_CD", " in ('GZ')");
        queryParams.put("n_code_DC", " NOT LIKE 'SAM%' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}
