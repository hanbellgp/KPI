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
public class FreeServiceWithinKS1H extends FreeServiceWithinKS{

    public FreeServiceWithinKS1H() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "<>'CK' ");
        queryParams.put("hmark2", " ='P' ");
    }   
}
