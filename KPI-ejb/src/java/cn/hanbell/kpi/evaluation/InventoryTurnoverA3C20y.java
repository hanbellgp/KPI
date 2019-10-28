/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
// 南京周转天数（服务部(冷媒、冷冻+空压机组+空压机体+涡旋）
//本月周转天数 = 今年截止到本月底的天数 / (本月销售成本/((本月库存金额+上月库存金额)/2))
public class InventoryTurnoverA3C20y extends InventoryTurnoverA3 {

    public InventoryTurnoverA3C20y() {
        super();
        queryParams.put("formid", "南京制冷库存金额");
        queryParams.put("deptno", "1E000");
        queryParams.put("issevdta", "'Y'");
        queryParams.put("n_code_cd", "in('NJ')");
        queryParams.put("n_code_dc", "not in ('RT','SDS')");
        queryParams.put("n_code_dd", "in ('01')");
    }

}
