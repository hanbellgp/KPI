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
 * @author C1749 机组试车缺点数
 */
public class QRATrialRunAdverseAA1T1 extends QRATrialRunAdverseAA1 {

    public QRATrialRunAdverseAA1T1() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        return super.getValue(y, m, d, type, map).multiply(BigDecimal.valueOf(100)); //To change body of generated methods, choose Tools | Templates.
    }

}
