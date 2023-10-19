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
public class ShoppingCenterMaterailAmount12SCM extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount12SCM() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("type", " ='模具'");
    }
}
