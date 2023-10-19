/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.evaluation;

import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.IndicatorDaily;
import cn.hanbell.kpi.entity.IndicatorDetail;
import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class ProductionPlanShipmentR extends ProductionPlanShipment {

    public ProductionPlanShipmentR() {
        super();
        queryParams.put("facno", "C");
        queryParams.put("n_code_DA", " ='R' ");
        queryParams.put("n_code_DD", " ='00' ");
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
         BigDecimal value = BigDecimal.ZERO;
        try {
            String facno = map.get("facno") != null ? map.get("facno").toString() : "";
            String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
            String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
            String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
            String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";
            String id = map.get("id") != null ? map.get("id").toString() : "";

            StringBuilder sb = new StringBuilder();
            sb.append(" select sum(a.totshpqy)");
            sb.append("  from (");
            //上海汉钟
            sb.append(" select isnull(sum(d.shpqy1),0) as totshpqy from cdrhad h, cdrdta d ");
            sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='${facno}' and d.issevdta='N'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            if (!"".equals(n_code_DD)) {
                sb.append(" and d.n_code_DD ").append(n_code_DD);
            }
            if (!"".equals(itcls)) {
                sb.append(" and d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
            }
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
            switch (type) {
                case 2:
                    //月
                    sb.append(" and h.shpdate<= '${d}' ");
                    break;
                case 5:
                    //日
                    sb.append(" and h.shpdate= '${d}' ");
                    break;
                default:
                    sb.append(" and h.shpdate<= '${d}' ");
            }

            sb.append(" union all ");
            //重庆ERP
            sb.append(" select isnull(sum(d.shpqy1),0) as totshpqy from cqerp.dbo.cdrhad h, cqerp.dbo.cdrdta d ");
            sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='C4' and d.issevdta='N'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            if (!"".equals(n_code_DD)) {
                sb.append(" and d.n_code_DD ").append(n_code_DD);
            }
            if (!"".equals(itcls)) {
                sb.append(" and d.itnbr in(select itnbr from cqerp.dbo.invmas where itcls ").append(itcls).append(") ");
            }
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
            switch (type) {
                case 2:
                    //月
                    sb.append(" and h.shpdate<= '${d}' ");
                    break;
                case 5:
                    //日
                    sb.append(" and h.shpdate= '${d}' ");
                    break;
                default:
                    sb.append(" and h.shpdate<= '${d}' ");
            }
            sb.append(" union all ");
            //广州ERP
            sb.append(" select isnull(sum(d.shpqy1),0) as totshpqy from gzerp.dbo.cdrhad h, gzerp.dbo.cdrdta d ");
            sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='G' and d.issevdta='N'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            if (!"".equals(n_code_DD)) {
                sb.append(" and d.n_code_DD ").append(n_code_DD);
            }
            if (!"".equals(itcls)) {
                sb.append(" and d.itnbr in(select itnbr from gzerp.dbo.invmas where itcls ").append(itcls).append(") ");
            }
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
            switch (type) {
                case 2:
                    //月
                    sb.append(" and h.shpdate<= '${d}' ");
                    break;
                case 5:
                    //日
                    sb.append(" and h.shpdate= '${d}' ");
                    break;
                default:
                    sb.append(" and h.shpdate<= '${d}' ");
            }
            sb.append(" union all ");
            //济南ERP
            sb.append(" select isnull(sum(d.shpqy1),0) as totshpqy from jnerp.dbo.cdrhad h, jnerp.dbo.cdrdta d ");
            sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='J' and d.issevdta='N'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            if (!"".equals(n_code_DD)) {
                sb.append(" and d.n_code_DD ").append(n_code_DD);
            }
            if (!"".equals(itcls)) {
                sb.append(" and d.itnbr in(select itnbr from jnerp.dbo.invmas where itcls ").append(itcls).append(") ");
            }
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
            switch (type) {
                case 2:
                    //月
                    sb.append(" and h.shpdate<= '${d}' ");
                    break;
                case 5:
                    //日
                    sb.append(" and h.shpdate= '${d}' ");
                    break;
                default:
                    sb.append(" and h.shpdate<= '${d}' ");
            }
            sb.append(" union all ");
            //南京ERP
            sb.append(" select isnull(sum(d.shpqy1),0) as totshpqy from njerp.dbo.cdrhad h, njerp.dbo.cdrdta d ");
            sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='N' and d.issevdta='N'");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            if (!"".equals(n_code_DD)) {
                sb.append(" and d.n_code_DD ").append(n_code_DD);
            }
            if (!"".equals(itcls)) {
                sb.append(" and d.itnbr in(select itnbr from njerp.dbo.invmas where itcls ").append(itcls).append(") ");
            }
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
            switch (type) {
                case 2:
                    //月
                    sb.append(" and h.shpdate<= '${d}' ");
                    break;
                case 5:
                    //日
                    sb.append(" and h.shpdate= '${d}' ");
                    break;
                default:
                    sb.append(" and h.shpdate<= '${d}' ");
            }
            sb.append(") a");
            String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                    .replace("${facno}", facno);

            superEJB.setCompany(facno);
            Query query = superEJB.getEntityManager().createNativeQuery(sql);

            Object o = query.getSingleResult();
            value = (BigDecimal) o;
            //更新往日出货
            if (type == 5) {
                List list = getLastValue(y, m, d, map);
                if (list != null && !list.isEmpty()) {
                    if (!"".equals(id)) {
                        Indicator entity = indicatorBean.findById(Integer.valueOf(id));
                        if (entity != null && entity.getOther3Indicator() != null) {
                            IndicatorDetail salesOrder = entity.getOther3Indicator();
                            IndicatorDaily daily = indicatorDailyBean.findByPIdDateAndType(salesOrder.getId(), salesOrder.getSeq(), m, salesOrder.getType());
                            daily.clearDate();
                            for (int i = 0; i < list.size(); i++) {
                                Object[] row = (Object[]) list.get(i);
                                updateValue(Integer.valueOf(row[0].toString()), BigDecimal.valueOf(Double.valueOf(row[1].toString())), daily);
                            }
                            indicatorDailyBean.update(daily);
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return value;
    }

    @Override
    public List getLastValue(int y, int m, Date d, LinkedHashMap<String, Object> map) {

        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String itcls = map.get("itcls") != null ? map.get("itcls").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA").toString() : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC").toString() : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD").toString() : "";

        BigDecimal value = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        sb.append(" select a.day,sum(a.totshpqy)");
        sb.append(" from (");
        //上海汉钟ERP
        sb.append(" select day(h.shpdate) as day,isnull(sum(d.shpqy1),0) as totshpqy from cdrhad h, cdrdta d ");
        sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='${facno}' and d.issevdta='N' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate< '${d}'  GROUP BY day(h.shpdate)");

        //重庆ERP
        sb.append("union all");
        sb.append(" select day(h.shpdate)as day,isnull(sum(d.shpqy1),0) as totshpqy from cqerp.dbo.cdrhad h, cqerp.dbo.cdrdta d ");
        sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='C4' and d.issevdta='N'");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from cqerp.dbo.invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate< '${d}'  GROUP BY day(h.shpdate)");

        //广州ERP
        sb.append("union all");
        sb.append(" select day(h.shpdate)as day,isnull(sum(d.shpqy1),0) as totshpqy from gzerp.dbo.cdrhad h, gzerp.dbo.cdrdta d ");
        sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='G' and d.issevdta='N'");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from gzerp.dbo.invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate< '${d}'  GROUP BY day(h.shpdate)");

        //济南ERP
        sb.append("union all");
        sb.append(" select day(h.shpdate)as day,isnull(sum(d.shpqy1),0) as totshpqy from jnerp.dbo.cdrhad h, jnerp.dbo.cdrdta d ");
        sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='J' and d.issevdta='N'");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from jnerp.dbo.invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate< '${d}'  GROUP BY day(h.shpdate)");

        //南京ERP
        sb.append("union all");
        sb.append(" select day(h.shpdate)as day,isnull(sum(d.shpqy1),0) as totshpqy from njerp.dbo.cdrhad h, njerp.dbo.cdrdta d ");
        sb.append(" where  h.houtsta<>'W' and h.shpno=d.shpno and  h.facno=d.facno  and h.facno='N' and d.issevdta='N' ");
        sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" and d.n_code_DD ").append(n_code_DD);
        }
        if (!"".equals(itcls)) {
            sb.append(" and d.itnbr in(select itnbr from njerp.dbo.invmas where itcls ").append(itcls).append(") ");
        }
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} and h.shpdate< '${d}'  GROUP BY day(h.shpdate)");
        sb.append(") a group by a.day");
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${d}", BaseLib.formatDate("yyyyMMdd", d))
                .replace("${facno}", facno);

        superEJB.setCompany(facno);
        Query query = superEJB.getEntityManager().createNativeQuery(sql);
        try {
            List list = query.getResultList();
            return list;
        } catch (Exception e) {
            Logger.getLogger(Shipment.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
