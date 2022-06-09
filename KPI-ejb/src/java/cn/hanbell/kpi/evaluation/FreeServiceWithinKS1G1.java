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
public class FreeServiceWithinKS1G1 extends FreeServiceWithinKS{

    public FreeServiceWithinKS1G1() {
        //广州机体维修领退料
        super();
        queryParams.put("facno", "C");
        queryParams.put("hmark1", "  in ('HN') ");
        queryParams.put("hmark2", "='AH' ");
    }   
}
