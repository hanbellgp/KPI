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
public class ShipmentAmountAA91 extends ShipmentAmount9 {

    public ShipmentAmountAA91() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("ogdkid", "RL01");
        queryParams.put("n_code_DA", "='AA' ");
        queryParams.put("n_code_DC", " <> 'SDS' ");
        queryParams.put("n_code_CD", "  LIKE 'WX%' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        BigDecimal temp1, temp2,temp3,temp4,temp5;
        //SHB ERP
        temp1 = super.getValue(y, m, d, type, map);
        //GZ ERP
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "G");
        temp2 = super.getValue(y, m, d, type, queryParams);
        //JN ERP
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "J");
        temp3 = super.getValue(y, m, d, type, queryParams);
        //NJ ERP
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "N");
        temp4 = super.getValue(y, m, d, type, queryParams);
        //CQ ERP
        queryParams.remove("facno");
        queryParams.remove("n_code_CD");
        queryParams.put("facno", "C4");
        temp5 = super.getValue(y, m, d, type, queryParams);
        
        //SHB + GZ + JN + NJ + CQ
        return temp1.add(temp2).add(temp3).add(temp4).add(temp5);
    }

}
