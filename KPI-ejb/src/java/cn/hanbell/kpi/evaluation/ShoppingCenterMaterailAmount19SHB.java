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
public class ShoppingCenterMaterailAmount19SHB extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount19SHB() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("type", " ='其他'");
    }
}
