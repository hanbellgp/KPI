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
// 半成品
public class InventoryAmountA1B30 extends InventoryAmountA1 {

    public InventoryAmountA1B30() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("indicatorno", "B30");
    }

}
