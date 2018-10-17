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
public class FreeServiceOuter1C extends FreeServiceOuterERP {

    public FreeServiceOuter1C() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        try {
            Actual erp = (Actual) FreeserveOuterERP1C.class.newInstance();
            //SHB ERP
            BigDecimal ev1 = erp.getValue(y, m, d, type, erp.getQueryParams());
            erp.getQueryParams().remove("facno");
            erp.getQueryParams().put("facno", "J");
            //JN ERP
            BigDecimal ev2 = erp.getValue(y, m, d, type, erp.getQueryParams());

            Actual oa = (Actual) FreeserveOuterOA1C.class.newInstance();
            BigDecimal ov = oa.getValue(y, m, d, type, oa.getQueryParams());
            
            return ev1.add(ev2).add(ov);
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(FreeServiceOuter1B.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }

}

class FreeserveOuterERP1C extends FreeServiceOuterERP {

    public FreeserveOuterERP1C() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='HB' ");
        queryParams.put("hmark2", " ='R' ");
        queryParams.put("depno", " like '1C%' ");
    }
}

class FreeserveOuterOA1C extends FreeServiceOuterOA {

    public FreeserveOuterOA1C() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("depno", " like '1C%' ");
    }

}