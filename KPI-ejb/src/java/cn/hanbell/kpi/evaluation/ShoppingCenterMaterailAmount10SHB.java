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
public class ShoppingCenterMaterailAmount10SHB extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount10SHB() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("type", " ='接线盖板'");
    }
}
