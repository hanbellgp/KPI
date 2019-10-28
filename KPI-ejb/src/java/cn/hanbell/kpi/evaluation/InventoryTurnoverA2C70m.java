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
// 离心机体周转天数(含服务)
//本月周转天数 = 30 / (本月销售成本/((本月库存金额+上月库存金额)/2))
public class InventoryTurnoverA2C70m extends InventoryTurnoverA2 {

    public InventoryTurnoverA2C70m() {
        super();
        queryParams.put("mm", "y");
        queryParams.put("formid", "离心机体库存金额");
        queryParams.put("deptno", "1F000");
        queryParams.put("facno", "C");
        queryParams.put("n_code_da", "= 'R'");
        queryParams.put("n_code_dc", "= 'RT'");
        queryParams.put("n_code_dd", "in ('00','02','01')");
    }

}
