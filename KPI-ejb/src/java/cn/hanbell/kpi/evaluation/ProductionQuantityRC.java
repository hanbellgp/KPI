/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1879
 */
public class ProductionQuantityRC extends ProductionQuantity {

    /**
     * R冷媒
     */
    public ProductionQuantityRC() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = 'RC' ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " IN ('3176','3179','3180','3276','3279','3280','3083','4079')");

    }
}
