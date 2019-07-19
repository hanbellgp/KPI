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
 * @author C1749
 */
//半成品
public class InventoryAmountA1B30 extends InventoryAmountA1 {

    public InventoryAmountA1B30() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("genzls", "B30");

    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map); //To change body of generated methods, choose Tools | Templates.
    }
}
