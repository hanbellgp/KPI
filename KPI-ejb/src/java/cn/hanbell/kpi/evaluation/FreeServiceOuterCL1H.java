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
public class FreeServiceOuterCL1H extends FreeServiceOuterCL {

    public FreeServiceOuterCL1H() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '1H%' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2;
        temp1 = super.getValue(y, m, d, type, map);
        queryParams.remove("depno");
        queryParams.put("depno", " like 'XA%' ");
        temp2 = super.getValue(y, m, d, type, queryParams);
        //银川+真空
        return temp1.add(temp2);
    }
}
