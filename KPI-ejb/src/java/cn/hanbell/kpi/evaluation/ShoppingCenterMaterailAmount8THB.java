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
public class ShoppingCenterMaterailAmount8THB extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount8THB() {
        super();
        queryParams.put("facno", "A");
        queryParams.put("type", " ='刀具'");
    }
}
