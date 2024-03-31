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
 * 质量扣款
 */
public class FreeServiceARM1B extends FreeServiceERP{

    public FreeServiceARM1B() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "('RL01','RL03')");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_CD", " IN ('HD') ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {        
       return BigDecimal.ZERO;
    }
    
}
