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
public class ShoppingCenterMaterailAmount8HY extends ShoppingCenterMaterailAmount {

    public ShoppingCenterMaterailAmount8HY() {
        super();
        queryParams.put("facno", "Y");
        queryParams.put("type", " ='刀具'");
    }
}
