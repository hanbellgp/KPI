/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.ClientTable;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class ClientNowAndPastBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    private final DecimalFormat df;

    public ClientNowAndPastBean() {
        this.df = new DecimalFormat("#,###.00");
    }

    public String getQuantitySql(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpqy1' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select h.cusno,sum(shpqy1) as num  from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.houtsta <> 'W' and year(h.shpdate) = ${y} and d.n_code_DD ='00' ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,-sum(bshpqy1) as num  from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.baksta <> 'W' and year(h.bakdate) = ${y} and  d.n_code_DD ='00' ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }        
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c  where z.cusno=c.cusno ");

        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    public String getAmountSql(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        String decode = map.get("decode") != null ? map.get("decode").toString() : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String style = map.get("style") != null ? map.get("style") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select  z.cusno as 'cusno' ,c.cusna as 'cusna' ,z.num  as 'shpamts' from ( ");
        sb.append(" select x.cusno,sum(num) as num  from ( ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),sum((d.shpamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.houtsta <> 'W'  and year(h.shpdate) = ${y} and d.n_code_DD ='00' ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.shpdate) = ${m} ");
        } else {
            sb.append(" and month(h.shpdate) BETWEEN 1 AND ${m} ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" group by  h.cusno ");
        sb.append(" union all ");
        sb.append(" select  h.cusno,isnull(convert(decimal(16,2),-sum((d.bakamts * h.ratio)/(h.taxrate + 1))),0) as num ");
        sb.append(" from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146') and d.issevdta='N' and h.facno='${facno}' ");
        sb.append(" and h.baksta <> 'W' and year(h.bakdate) = ${y} and d.n_code_DD ='00' ");
        if (style.equals("nowmonth")) {
            sb.append(" and month(h.bakdate)= ${m} ");
        } else {
            sb.append(" and month(h.bakdate) BETWEEN 1 AND ${m} ");
        }
        if (!"".equals(decode)) {
            sb.append(" and h.decode ='").append(decode).append("' ");
        }
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DC)) {
            sb.append(" and d.n_code_DC ").append(n_code_DC);
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c where z.cusno=c.cusno ");

        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    public List<ClientTable> getNowClient(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.cusno as 'cusno',a.cusna as 'cusna',a.shpqy1 as 'shpqy1',b.shpamts as 'shpamts' from ( ");
        sb.append(getQuantitySql(y, m, map));
        sb.append(" ) a,( ");
        sb.append(getAmountSql(y, m, map));
        sb.append(" ) b where a.cusno=b.cusno  and a.cusna  =b.cusna ORDER BY shpamts DESC ");

        List<ClientTable> nowlist = new ArrayList<>();
        List<ClientTable> pastlist = getPastClient(y, m, map);
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString()).setFirstResult(0).setMaxResults(20);
        try {
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                //通过客户编号找到去年同期值;
                boolean aa;
                double nowshpamts;
                double pastshpamts;
                for (int i = 0; i < result.size(); i++) {
                    aa = true;
                    Object[] row = (Object[]) result.get(i);
                    ClientTable ct = new ClientTable();
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setNowshpqy1(String.valueOf(Double.valueOf(row[2].toString()).intValue()));
                    ct.setNowshpamts(df.format(Double.parseDouble(row[3].toString())));
                    ct.setNowrank(String.valueOf(i + 1));
                    for (int j = 0; j < pastlist.size(); j++) {
                        if (row[0].toString().equals(pastlist.get(j).getCusno())) {
                            aa = false;
                            nowshpamts = Double.parseDouble(row[3].toString());
                            pastshpamts = Double.parseDouble(pastlist.get(j).getPastshpamts());
                            ct.setPastshpqy1(pastlist.get(j).getPastshpqy1());
                            ct.setPastshpamts(df.format(Double.parseDouble(pastlist.get(j).getPastshpamts())));
                            ct.setPastrank(pastlist.get(j).getPastrank());
                            ct.setDifferencevalue(df.format(nowshpamts - pastshpamts));
                            ct.setGrowthrate(String.valueOf(df.format((nowshpamts - pastshpamts) / pastshpamts * 100)) + "%");                            
                        }
                    }
                    if (aa) {
                        ct.setPastshpqy1("0");
                        ct.setPastshpamts("0");
                        ct.setGrowthrate("100%");
                        ct.setDifferencevalue(df.format(Double.parseDouble(row[3].toString())));
                    }
                    nowlist.add(ct);
                }

            }

            return nowlist;
        } catch (Exception e) {
            return null;
        }
    }

    public List<ClientTable> getPastClient(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        //查询去年同期值并赋予排名
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.cusno as 'cusno',a.cusna as 'cusna',a.shpqy1 as 'shpqy1',b.shpamts as 'shpamts' from ( ");
        sb.append(getQuantitySql((y - 1), m, map));
        sb.append(" ) a,( ");
        sb.append(getAmountSql((y - 1), m, map));
        sb.append(" ) b where a.cusno=b.cusno  and a.cusna  =b.cusna ORDER BY shpamts DESC ");
        List<ClientTable> list = new ArrayList<>();
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString());
        try {
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    Object[] row = (Object[]) result.get(i);
                    ClientTable ct = new ClientTable();
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setPastshpqy1(String.valueOf(Double.valueOf(row[2].toString()).intValue()));
                    ct.setPastshpamts(row[3].toString());
                    ct.setPastrank(String.valueOf(i + 1));
                    list.add(ct);
                }
            }
            return list;
        } catch (Exception e) {
            return null;
        }
    }
}
