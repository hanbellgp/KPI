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
public class ProductionQuantityPR600 extends ProductionQuantity {

    /**
     * PR600机体
     */
    public ProductionQuantityPR600() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " = 'VP2' ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //品号大类
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", "  IN ('3476','3479','3480') ");
        
    }
}
