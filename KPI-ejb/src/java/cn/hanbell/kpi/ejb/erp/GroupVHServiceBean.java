/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.erp;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.erp.BscGroupShipment;
import com.lightshell.comm.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class GroupVHServiceBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, Object> queryParams = new LinkedHashMap<>();

    public GroupVHServiceBean() {
    }

    public LinkedHashMap<String, Object> getQueryParams() {
        return this.queryParams;
    }

    public void updataActualValue(int y, int m, Date d) {
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='O' ");
        List<BscGroupShipment> resultData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        List<BscGroupShipment> tempData;
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='R' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        queryParams.clear();
        queryParams.put("facno", "V");
        queryParams.put("hmark1", " ='A' ");
        tempData = getServiceValue(y, m, d, Calendar.MONTH, getQueryParams());
        if (tempData != null && !tempData.isEmpty()) {
            for (BscGroupShipment b : tempData) {
                if (resultData.contains(b)) {
                    BscGroupShipment a = resultData.get(resultData.indexOf(b));
                    a.setShpnum(BigDecimal.ZERO);
                    a.setShpamts(a.getShpamts().add(b.getShpamts()));
                    a.setOrdnum(BigDecimal.ZERO);
                    a.setOrdamts(BigDecimal.ZERO);
                } else {
                    resultData.add(b);
                }
            }
        }
        if (resultData != null) {
            erpEJB.setCompany("C");
            erpEJB.getEntityManager().createNativeQuery("delete from bsc_groupshipment where shptype = '21' and facno='V' and year(soday)=" + y + " and month(soday) = " + m).executeUpdate();
            for (BscGroupShipment e : resultData) {
                erpEJB.getEntityManager().persist(e);
            }
        }
    }

    //服务金额
    protected List<BscGroupShipment> getServiceValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String hmark1 = map.get("hmark1") != null ? map.get("hmark1").toString() : "";
        StringBuilder sb = new StringBuilder();
        List<BscGroupShipment> data = new ArrayList<>();
        BigDecimal sqty = BigDecimal.ZERO;
        sb.append(" select soday,isnull(cast(sum(num) as decimal(16,2)),0) as num from(  ");
        sb.append(" select h.shpdate as soday,isnull(sum((d.shpamts * h.ratio)),0) as num ");
        sb.append(" from [vnerp1].[dbo].cdrhmas e,[vnerp1].[dbo].cdrdta d  inner join [vnerp1].[dbo].cdrhad h on  d.facno=h.facno  and d.shpno=h.shpno   ");
        sb.append(" where h.facno = '${facno}'    and h.houtsta <> 'W' and e.cdrno=d.cdrno and e.hmark2='FW'  ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        sb.append(" and year(h.shpdate)=${y} and month(h.shpdate)=${m} ");
        sb.append(" group by h.shpdate ");
        sb.append(" UNION  all ");
        sb.append(" select  h.bakdate as soday,isnull(sum((d.bakamts * h.ratio)*(-1)),0) as num ");
        sb.append(" from  [vnerp1].[dbo].cdrhmas e,[vnerp1].[dbo].cdrbhad h right join [vnerp1].[dbo].cdrbdta d on h.bakno=d.bakno  ");
        sb.append(" where h.facno = '${facno}'   and h.baksta <> 'W' and e.cdrno=d.cdrno AND e.hmark2='FW' ");
        if (!"".equals(hmark1)) {
            sb.append(" and e.hmark1 ").append(hmark1);
        }
        sb.append(" and year(h.bakdate)=${y}  and month(h.bakdate)=${m} group by h.bakdate ");
        sb.append(" ) as a GROUP BY soday ");
        String sql = sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d));
        erpEJB.setCompany(facno);
        Query query1 = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List shpResult = query1.getResultList();
            Date shpdate;
            String protype, protypeno, shptype;
            if (hmark1.contains("O")) {
                protype = "其他代理品";
                protypeno = "O";
                shptype = "21";
            } else if (hmark1.contains("R") && !hmark1.contains("DR")) {
                protype = "R/CDU/Dorin服务收费";
                protypeno = "SR";
                shptype = "21";
            } else if (hmark1.contains("A")) {
                protype = "空压机服务收费";
                protypeno = "SA";
                shptype = "21";
            } else {
                protype = "";
                protypeno = "";
                shptype = "";
            }
            for (int i = 0; i < shpResult.size(); i++) {
                Object o[] = (Object[]) shpResult.get(i);
                shpdate = BaseLib.getDate("yyyy-MM-dd", o[0].toString());
                sqty = BigDecimal.valueOf(Double.valueOf(o[1].toString()));
                BscGroupShipment e = new BscGroupShipment("V", shpdate, protype, protypeno, shptype);
                e.setShpnum(BigDecimal.ZERO);
                e.setShpamts(sqty);
                data.add(e);
            }
        } catch (Exception ex) {
            Logger.getLogger(GroupVHServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return data;
    }

}
