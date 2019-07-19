/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749 借员工（展示机）
 */
public class InventoryAmountA4F60 extends InventoryAmountA4{
    public InventoryAmountA4F60(){
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("trtype", "9E");
    
    }
    
}
