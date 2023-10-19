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
public class ShoppingCenterMaterailAmount3HY extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount3HY() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("type", " ='轴承'");
    }
}
