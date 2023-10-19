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
public class ShoppingCenterMaterailAmount1ZCM extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount1ZCM() {
        super();
        queryParams.put("facno", "E");
        queryParams.put("type", " ='铸加'");
    }
}
