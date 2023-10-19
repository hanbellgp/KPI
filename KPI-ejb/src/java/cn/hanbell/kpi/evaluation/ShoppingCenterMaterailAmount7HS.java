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
public class ShoppingCenterMaterailAmount7HS extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount7HS() {
        super();
        queryParams.put("facno", "H");
        queryParams.put("type", " ='阀类'");
    }
}
