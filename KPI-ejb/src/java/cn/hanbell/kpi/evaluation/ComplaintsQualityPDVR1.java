/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

/**
 *
 * @author C0160
 */
public class ComplaintsQualityPDVR1 extends ComplaintsQuality {
    //真空干泵年移动平均台数接口
    public ComplaintsQualityPDVR1() {
        super();
        queryParams.put("facno", "C");
        //queryParams.put("deptno", " '1H000','1H100' ");
        queryParams.put("n_code_DA", " ='P' ");
        queryParams.put("n_code_DC", " ='DVR' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

}