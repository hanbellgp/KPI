/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author C1879
 */
public class FreeServiceOuterYS1H extends FreeServiceERP {

    public FreeServiceOuterYS1H() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) FreeServiceOuterYSerp1H.class.newInstance();
            BigDecimal ev = erp.getValue(y, m, d, type, erp.getQueryParams());

            Actual oa = (Actual) FreeServiceOuterYSoa1H.class.newInstance();
            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
            return ev.add(ov);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuterYS1H.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}

class FreeServiceOuterYSerp1H extends FreeServiceOuterYSerp {

    public FreeServiceOuterYSerp1H() {
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

class FreeServiceOuterYSoa1H extends FreeServiceOuterYSoa {

    public FreeServiceOuterYSoa1H() {
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
