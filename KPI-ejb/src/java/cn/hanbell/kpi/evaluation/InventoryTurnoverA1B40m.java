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
// 在制品周转天数
//本月周转天数 = 今年到本月的天数 / (本月销售成本/((本月库存金额+上月库存金额)/2))
public class InventoryTurnoverA1B40m extends InventoryTurnoverA1 {

    public InventoryTurnoverA1B40m() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal sell, result;
        try {
            sell = getMonthSellingValue(y, m, d, type, map);
            queryParams.clear();
            queryParams.put("formid", "在制品库存金额");
            queryParams.put("deptno", "1P000");
            result = getMonthValue(y, m, d, type, queryParams, sell);
            return result;
        } catch (Exception ex) {
            log4j.error(ex);
        }
        return BigDecimal.ZERO;
    }
}
