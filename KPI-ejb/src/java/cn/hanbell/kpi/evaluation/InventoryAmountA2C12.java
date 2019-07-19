/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C1749
 */
public class InventoryAmountA2C12 extends InventoryAmountA2 {

    public InventoryAmountA2C12() {
        super();
        queryParams.put("formid", "制冷库存分摊比率");
        queryParams.put("deptno", "1F000");
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("genreno", "A2");
        queryParams.put("genzls", "C12");
        queryParams.put("genre", "in('R','RG','L')");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String mon;
        Field f;
        BigDecimal v1;
        BigDecimal result = BigDecimal.ZERO;
        Double a1;
        Indicator i = indicatorBean.findByFormidYearAndDeptno(map.get("formid").toString(), y, map.get("deptno").toString());
        IndicatorDetail o1 = i.getOther3Indicator();//南京分公司
        try {
            mon = indicatorBean.getIndicatorColumn("N", m);
            f = o1.getClass().getDeclaredField(mon);
            f.setAccessible(true);
            a1 = Double.valueOf(f.get(o1).toString());
            v1 = BigDecimal.valueOf(a1);
            result = super.getValue(y, m, d, type, map).multiply(v1).setScale(2, BigDecimal.ROUND_HALF_UP);
            fgsValue = getFgsValue(y, m, d, type, map).setScale(2, BigDecimal.ROUND_HALF_UP);
            fgsZjValue = getFgsZjValue(y, m, d, type, map).setScale(2, BigDecimal.ROUND_HALF_UP);;
            //分公司 = 公用分摊部分数据 + 汉钟分公司库的数据 + 分公司自己本身的整机部分数据
            result = result.add(fgsValue).add(fgsZjValue);
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryAmountA2C10--getValue()异常", ex.toString());
        }
        return result;
    }

}
