/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C2082
 */
public class ShoppingCenterMaterailAmount15THB extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount15THB() {
        super();
        queryParams.put("facno", "A");
        queryParams.put("type", " ='变频器'");
    }
}
