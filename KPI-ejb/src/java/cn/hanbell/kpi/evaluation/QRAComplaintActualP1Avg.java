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
public class QRAComplaintActualP1Avg extends QRAShipmentAvg{

    public QRAComplaintActualP1Avg() {
        super();
        queryParams.put("n_code_DA", "P");
    }

    //真空总移动平均出货台数
    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map);
    }
    
}
