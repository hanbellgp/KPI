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
public class ProductionPlanOrderAJZ extends ProductionPlanOrder {

    public ProductionPlanOrderAJZ() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='AA' ");
        queryParams.put("itcls", " in ('3576','3579','3580','4052','3676','3679','3680') and itnbr <> '35302-H5233-08' ");

    }
}
