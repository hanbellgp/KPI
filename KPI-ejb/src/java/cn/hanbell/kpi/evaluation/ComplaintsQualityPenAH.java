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
public class ComplaintsQualityPenAH extends ComplaintsQualityPen {

    public ComplaintsQualityPenAH() {
        super();
        queryParams.put("BQ197", "%AH%");
        queryParams.put("BQ003"," in ('AJT') ");
        queryParams.put("BQ134", " in ('YX','-1') ");
        queryParams.put("BQ110"," in ('Y') ");
    }
}