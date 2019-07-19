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
public class InventoryAmountA2C60 extends InventoryAmountA2{
    public InventoryAmountA2C60(){
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("genreno", "A2");
        queryParams.put("genzls", "C10");
        queryParams.put("genre", "='S'");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        fgsValue = getFgsValue(y, m, d, type, map).setScale(2, BigDecimal.ROUND_HALF_UP);
        return super.getValue(y, m, d, type, map).add(fgsValue); //To change body of generated methods, choose Tools | Templates.
    }
    
}
