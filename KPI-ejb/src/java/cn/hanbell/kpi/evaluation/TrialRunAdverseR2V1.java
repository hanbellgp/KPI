/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C1749
 */
public class TrialRunAdverseR2V1 extends TrialRunAdverseR {

    public TrialRunAdverseR2V1() {
        super();
        queryParams.put("STEPID", "冷媒");
        queryParams.put("typecode", "LB");
    }
}
