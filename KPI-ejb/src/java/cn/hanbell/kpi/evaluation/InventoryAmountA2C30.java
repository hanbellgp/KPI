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
public class InventoryAmountA2C30 extends InventoryAmountA2 {

    public InventoryAmountA2C30() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("categories", "A2");
        queryParams.put("genre", "='AD'");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal scValue = getProductValue(y, m, d, type, map);// 生产性的AD
        BigDecimal sczzValue = getgetProductZZValue(y, m, d, type, map);// 生产在制的AD
        result = super.getValue(y, m, d, type, map).add(scValue).add(sczzValue);
        return result;
    }

}
