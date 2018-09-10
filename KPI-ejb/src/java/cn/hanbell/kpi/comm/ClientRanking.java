/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import cn.hanbell.kpi.entity.ClientTable;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.Query;

/**
 *
 * @author C1879
 */
public class ClientRanking {

    @EJB
    protected SuperEJBForERP erpEJB;

    protected final DecimalFormat df;

    public ClientRanking() {
        this.df = new DecimalFormat("#,###.##");
    }

    //出货台数SQL
    protected String getQuantitySql(int y, int m, String facno, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
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
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                if (!"".equals(n_code_DC)) {
                    sb.append(" and d.n_code_DC ").append(n_code_DC);
                }
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
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
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017 && !"".equals(n_code_DC)) {
                if (!"".equals(n_code_DC)) {
                    sb.append(" and d.n_code_DC ").append(n_code_DC);
                }
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c  where z.cusno=c.cusno ");

        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    //出货金额SQL
    protected String getAmountSql(int y, int m, String facno, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid") : "";
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
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                if (!"".equals(n_code_DC)) {
                    sb.append(" and d.n_code_DC ").append(n_code_DC);
                }
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
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
        //当查询年限超过2017年则按产品别查询，否则按部门区分
        if (!"".equals(n_code_DC)) {
            if (y > 2017) {
                if (!"".equals(n_code_DC)) {
                    sb.append(" and d.n_code_DC ").append(n_code_DC);
                }
            } else {
                if (!"".equals(depno)) {
                    sb.append(" and h.depno ").append(depno);
                }
            }
        }
        //机体机组算入折让
        if (n_code_DA.equals("= 'AA'") || n_code_DA.equals("= 'AH'") || n_code_DA.equals("= 'R'")) {
            sb.append(" group by  h.cusno ");
            sb.append(" union all ");
            //加扣款
            sb.append(" SELECT h.cusno AS 'cusno', ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS num FROM armpmm h,armacq d,cdrdta s ");
            sb.append(" WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno AND d.shpno=s.shpno AND d.shpseq = s.trseq  AND h.facno='${facno}' and s.n_code_DD ='00' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and s.n_code_DA ").append(n_code_DA);
            }
            //当查询年限超过2017年则按产品别查询，否则按部门区分
            if (!"".equals(n_code_DC)) {
                if (y > 2017) {
                    if (!"".equals(n_code_DC)) {
                        sb.append(" and s.n_code_DC ").append(n_code_DC);
                    }
                } else {
                    if (!"".equals(depno)) {
                        sb.append(" and d.depno ").append(depno);
                    }
                }
            }
            sb.append(" and year(h.trdat) = ${y}  ");
            if (style.equals("nowmonth")) {
                sb.append(" and month(h.trdat) = ${m} ");
            } else {
                sb.append(" and month(h.trdat) BETWEEN 1 AND ${m} ");
            }
            sb.append(" group by  h.cusno ");
            sb.append(" union all ");
            //--折让
            sb.append(" SELECT d.ivocus AS 'cusno',ISNULL(sum(d.recamt),0) AS num FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
            sb.append("  AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno='6001' AND h.facno='${facno}' and h.n_code_DD ='00' ");
            if (!"".equals(ogdkid)) {
                sb.append(" AND h.ogdkid ").append(ogdkid);
            }
            if (!"".equals(n_code_DA)) {
                sb.append(" and h.n_code_DA ").append(n_code_DA);
            }
            //当查询年限超过2017年则按产品别查询，否则按部门区分
            if (!"".equals(n_code_DC)) {
                if (y > 2017) {
                    if (!"".equals(n_code_DC)) {
                        sb.append(" and h.n_code_DC ").append(n_code_DC);
                    }
                } else {
                    if (!"".equals(depno)) {
                        sb.append(" and h.depno ").append(depno);
                    }
                }
            }
            sb.append(" and year(h.recdate) = ${y}  ");
            if (style.equals("nowmonth")) {
                sb.append(" and month(h.recdate) = ${m} ");
            } else {
                sb.append(" and month(h.recdate) BETWEEN 1 AND ${m} ");
            }
            sb.append(" GROUP BY d.ivocus ");
            sb.append(" union all ");
            //它项金额,关联部门 发票
            sb.append(" SELECT h.cusno AS 'cusno', ISNULL(sum(h.shpamt),0) AS  num FROM armbil h WHERE h.rkd='RQ11' AND h.facno='${facno}' ");
            sb.append("  AND h.depno ").append(depno);
            sb.append(" and year(h.bildat) = ${y}  ");
            if (style.equals("nowmonth")) {
                sb.append(" and month(h.bildat) = ${m} ");
            } else {
                sb.append(" and month(h.bildat) BETWEEN 1 AND ${m} ");
            }
        }
        sb.append(" group by  h.cusno ) ");
        sb.append(" x  group by x.cusno ) ");
        sb.append(" z,cdrcus c where z.cusno=c.cusno ");
        return sb.toString().replace("${facno}", facno).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
    }

    /**
     *
     * @param y
     * @param m
     * @param arr1
     * @param map
     * @return 返回ClientTable集合
     */
    protected List getClient(int y, int m, String arr1, LinkedHashMap<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  a.cusno as 'cusno',a.cusna as 'cusna',a.shpqy1 as 'shpqy1',b.shpamts as 'shpamts' from ( ");
        sb.append(getQuantitySql((y), m, arr1, map));
        sb.append(" ) a,( ");
        sb.append(getAmountSql((y), m, arr1, map));
        sb.append(" ) b where a.cusno=b.cusno  and a.cusna  =b.cusna ORDER BY shpamts DESC ");
        erpEJB.setCompany(arr1);
        Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString());
        List result = query.getResultList();
        return result;
    }

    /**
     *
     * @param a
     * @return 得到台数无小数点
     */
    protected String RTshpqy1(String a) {
        return String.valueOf(Double.valueOf(a).intValue());
    }

    /**
     *
     * @param a
     * @return 得到台数无小数点
     */
    protected String RTshpqy1(Double a) {
        return String.valueOf(a.intValue());
    }

    /**
     *
     * @param a
     * @param b
     * @return //得到合计台数
     */
    protected String RTshpqy1(String a, String b) {
        return String.valueOf(Double.valueOf(a).intValue() + Double.valueOf(a).intValue());
    }

    /**
     *
     * @param a
     * @param b
     * @return //得到合计金额
     */
    protected String RTshpamts(String a, String b) {
        return String.valueOf(Double.valueOf(a) + Double.valueOf(b));
    }

    /**
     *
     * @param a
     * @param b
     * @return 同比差异值 = 本年累计金额 - 去年同期累计金额
     */
    protected String RTdifferencevalue(String a, String b) {
        return df.format(Double.parseDouble(a) - Double.parseDouble(b));
    }

    /**
     *
     * @param a
     * @param b
     * @return 同比成长率 = (本年累计金额 - 去年同期累计金额)/去年同期累计金额*100
     */
    protected String RTgrowthrate(String a, String b) {
        return df.format((Double.parseDouble(a) - Double.parseDouble(b)) / Double.parseDouble(b) * 100);
    }

    /**
     *
     * @param list
     * @param type now or past
     * @return 返回按金额排序的的list
     */
    protected List<ClientTable> RTlist(List<ClientTable> list, String type) {
        if (list != null && !list.isEmpty()) {
            ClientTable ct = new ClientTable();
            int min;
            for (int i = 0; i < list.size() - 1; i++) {
                min = i;
                for (int j = i + 1; j < list.size(); j++) {
                    if (Double.valueOf(list.get(j).getPastshpamts()) > Double.valueOf(list.get(min).getPastshpamts())) {
                        min = j;
                        ct = list.get(i);
                        list.set(i, list.get(min));
                        list.set(min, ct);
                    }
                }
            }
            int sumshpqy1 = 0;
            Double sumshpamts = 0.0;
            //Past 去年同期排名 反之 now
            if (type.equals("past")) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setPastrank(String.valueOf(i + 1));
                    sumshpqy1 += Integer.parseInt(list.get(i).getPastshpqy1());
                    sumshpamts += Double.parseDouble(list.get(i).getPastshpamts());
                }
                ct.setCusna("总计");
                ct.setPastshpqy1(String.valueOf(sumshpqy1));
                ct.setPastshpamts(String.valueOf(sumshpamts));
            } else {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setNowrank(String.valueOf(i + 1));
                    sumshpqy1 += Integer.parseInt(list.get(i).getPastshpqy1());
                    sumshpamts += Double.parseDouble(list.get(i).getPastshpamts());
                }
                ct.setCusna("总计");
                ct.setNowshpqy1(String.valueOf(sumshpqy1));
                ct.setNowshpamts(String.valueOf(sumshpamts));
            }
            list.add(ct);
            for (int i = 0; i < list.size(); i++) {
                ClientTable get = list.get(i);
                System.out.println("客户代号——" + get.getCusno() + "——客户姓名——" + get.getCusna() + "——当前排名——" + get.getNowrank() + "——当前台数——" + get.getNowshpqy1() + "——当前金额——" + get.getNowshpamts());
                System.out.println("——去年排名——" + get.getNowrank() + "——去年台数——" + get.getNowshpqy1() + "——去年金额——" + get.getNowshpamts());
            }
        }
        return list;
    }

}
