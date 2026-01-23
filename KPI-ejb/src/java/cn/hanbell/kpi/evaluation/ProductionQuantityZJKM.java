/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749 浙江柯茂
 */
public class ProductionQuantityZJKM extends ProductionQuantity {

    public ProductionQuantityZJKM() {
        super();
        queryParams.put("facno", "E");
        queryParams.put("prono", "1");
        //顾迪华2019年6月19日提出更改生产线别01 改 AT\ORC\RT
        queryParams.put("linecode", " IN('AT','ORC','RT','A9') ");
        queryParams.put("typecode", "= '01' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " in ('3W76','3W80','3W79','3Y76','3Y79','3Y80','3X76','3X79','3X80')");
        //#ITCLS CHANGE TODO #
        queryParams.put("itnbrf", " and itnbrf not in (select itnbr from invmas where itcls in ('3J76','3J79','3J80')) ");
        
    }

}
