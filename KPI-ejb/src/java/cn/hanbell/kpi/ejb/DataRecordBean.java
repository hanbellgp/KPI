/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.DataRecord;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class DataRecordBean extends SuperEJBForKPI<DataRecord> {

    private final DecimalFormat df;

    public DataRecordBean() {
        super(DataRecord.class);
        this.df = new DecimalFormat("#,##0.00");
    }

    public List<DataRecord> findByYeaAndMon(DataRecord dr) {
        Query query = getEntityManager().createNamedQuery("DataRecord.findByYeaAndMon");
        query.setParameter("facno", dr.getFacno());
        query.setParameter("type", dr.getType());
        query.setParameter("yea", dr.getYea());
        query.setParameter("mon", dr.getMon());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<DataRecord> findDataRecords(String facno, String type, int yea, int mon) {
        Query query = getEntityManager().createNamedQuery("DataRecord.findDataRecords");
        query.setParameter("facno", facno);
        query.setParameter("type", type);
        query.setParameter("yea", yea);
        query.setParameter("mon", mon);
        query.setParameter("whethershow", true);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    //现金流量表
    public LinkedHashMap<String, String[]> getCashFlowMap(String facno, String type, int yea, int mon) {
        LinkedHashMap<String, String[]> map = null;
        List<DataRecord> nowlist = findDataRecords(facno, type, yea, mon);
        List<DataRecord> lastlist = findDataRecords(facno, type, yea - 1, mon);
        DecimalFormat dfpercent = new DecimalFormat("0.##％");
        try {
            if (nowlist != null && !nowlist.isEmpty()) {
                map = new LinkedHashMap<>();
                for (DataRecord now : nowlist) {
                    String[] arr = new String[7];
                    arr[0] = now.getDataRecordAssisted().getAdjitemname();
                    arr[1] = String.valueOf(now.getDataRecordAssisted().getShowno());
                    arr[2] = df.format(now.getAdjamt().divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                    for (DataRecord last : lastlist) {
                        if (now.getItemname().equals(last.getItemname())) {
                            arr[3] = df.format(last.getAdjamt().divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                            arr[4] = df.format(now.getAdjamt().subtract(last.getAdjamt()).divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                            if (last.getAdjamt().compareTo(BigDecimal.ZERO) != 0) {
                                arr[5] = dfpercent.format(now.getAdjamt().subtract(last.getAdjamt()).divide(last.getAdjamt().abs(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100d)));
                            } else {
                                if (now.getAdjamt().compareTo(BigDecimal.ZERO) == 0) {
                                    arr[5] = dfpercent.format(0);
                                } else {
                                    arr[5] = dfpercent.format(100);
                                }
                            }
                        }
                    }
                    arr[6] = now.getDataRecordAssisted().getHighlight() ? "true" : "false";
                    map.put(String.valueOf(now.getDataRecordAssisted().getShowno()), arr);
                }
            }
            return map;
        } catch (Exception e) {
        }
        return null;
    }

}
