/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForEEP;
import java.util.LinkedHashMap;
import javax.naming.InitialContext;

/**
 *
 * @author C1879
 */
public abstract class EnergyConsumption implements Actual {

    protected SuperEJBForEEP superEEP;

    protected LinkedHashMap<String, Object> queryParams;

    public EnergyConsumption() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForEEP setEJB() {
        return superEEP;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEEP = (SuperEJBForEEP) objRef;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

}
