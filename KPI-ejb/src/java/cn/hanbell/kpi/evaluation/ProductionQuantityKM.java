/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879 柯茂机组部分
 */
public class ProductionQuantityKM extends ProductionQuantity {

    public ProductionQuantityKM() {
        super();
        queryParams.put("facno", "K");
        queryParams.put("prono", "1");
        //顾迪华2019年6月19日提出更改生产线别01 改 AT\ORC\RT
        queryParams.put("linecode", " IN('AT','ORC','RT') ");
        queryParams.put("typecode", "= '01' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " in ('3H76','3H79','3H80','3W80','3W76','3W79')");
        //#ITCLS CHANGE TODO #
//        queryParams.put("itnbrf", " and itnbrf not in (select itnbr from invmas where itcls in ('3J76','3J79','3J80')) ");
        
    }

}
