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
public class ServiceMaintainTimeAH extends ServiceMaintainTime {

    public ServiceMaintainTimeAH() {
        super();
        queryParams.put("CProductType", "in('AH机体','AA机组')");
    }
}
