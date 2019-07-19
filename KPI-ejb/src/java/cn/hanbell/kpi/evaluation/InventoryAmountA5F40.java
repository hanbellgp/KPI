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
public class InventoryAmountA5F40 extends InventoryAmountA5{
    public InventoryAmountA5F40(){
        super();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        queryParams.put("genreno", "A5");
        queryParams.put("genzls", "F40");
        
    }
    
}
