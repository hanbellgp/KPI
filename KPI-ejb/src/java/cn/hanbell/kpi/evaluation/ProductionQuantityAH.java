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
public class ProductionQuantityAH extends ProductionQuantity {

    /**
     * A机体
     */
    public ProductionQuantityAH() {
        super();
        //*公司别
        queryParams.put("facno", "C");
        //*生产地
        queryParams.put("prono", "1");
        //*生产线别
        queryParams.put("linecode", " in ('AH','AH2') ");
        //制令等级
        queryParams.put("typecode", "= '01' ");
        //品号大类
        //#ITCLS CHANGE TODO #
        queryParams.put("itcls", " NOT IN ('3476','3479','3480') ");
        
    }
}
