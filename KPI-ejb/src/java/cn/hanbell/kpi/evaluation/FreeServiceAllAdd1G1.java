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
public class FreeServiceAllAdd1G1 extends FreeServiceAllAdd {

    //机体广州综合成本 厂内+厂外
    public FreeServiceAllAdd1G1() {
        super();
        queryParams.put("OuterFormid", "A-空压机体广州服务成本");
        queryParams.put("deptno", "1G100");
        queryParams.put("WithinFormid", "A-空压机体广州维修成本");
    }

}
