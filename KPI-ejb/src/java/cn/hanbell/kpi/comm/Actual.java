/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import com.lightshell.comm.SuperEJB;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 *
 * @author C0160
 */
public interface Actual {

    public void setEJB(String JNDIName) throws Exception;

    public LinkedHashMap<String, Object> getQueryParams();

    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map);

}
