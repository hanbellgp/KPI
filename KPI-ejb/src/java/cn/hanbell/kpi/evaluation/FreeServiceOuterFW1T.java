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
public class FreeServiceOuterFW1T extends FreeServiceOuterFW {

    public FreeServiceOuterFW1T() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "='CK' ");
    }

}
