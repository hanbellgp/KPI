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
public class InventoryAmountA4E10 extends InventoryAmountA4{
    public InventoryAmountA4E10(){
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("trtype", "CA");
        queryParams.put("deptno", "1F");
    
    }
    
}
