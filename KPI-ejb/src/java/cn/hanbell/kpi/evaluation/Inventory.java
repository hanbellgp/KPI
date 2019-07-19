/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.comm.Actual;
import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.util.LinkedHashMap;
import javax.naming.InitialContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
public abstract class Inventory implements Actual {

    protected SuperEJBForERP superEJB;
    protected LinkedHashMap<String, Object> queryParams;
    protected final Logger log4j = LogManager.getLogger();

    public Inventory() {
        queryParams = new LinkedHashMap<>();
    }

    public SuperEJBForERP getEJB() {
        return superEJB;
    }

    @Override
    public void setEJB(String JNDIName) throws Exception {
        InitialContext c = new InitialContext();
        Object objRef = c.lookup(JNDIName);
        superEJB = (SuperEJBForERP) objRef;
    }

    @Override
    public LinkedHashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    @Override
    public int getUpdateMonth(int y, int m) {
        return m;
    }

    @Override
    public int getUpdateYear(int y, int m) {
        return y;
    }
    
    //取到月份小于10 前面自动添加为0
    public String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        } else {
            return String.valueOf(m);
        }
    }
}
