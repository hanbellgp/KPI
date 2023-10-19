/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
public class ProcurementBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    private final DecimalFormat df;
    private final DecimalFormat dfpercent;
    private final LinkedHashMap<String, String[]> map;

    public ProcurementBean() {
        this.df = new DecimalFormat("#,###");
        this.dfpercent = new DecimalFormat("0.00％");
        map = new LinkedHashMap<>();
    }

    private int findYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    private int findMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    /**
     *
     * @param date
     * @param status 状态 month月销售额 year 年销售额
     * @return
     */
    private Double getSaleValue(Date date, String status) {
        StringBuilder sb = new StringBuilder();

        sb.append(" select isnull(sum(p.q),0)  from( ");
        sb.append(" select year(cdrhad.shpdate) year,month(cdrhad.shpdate) month,isnull(sum((cdrdta.shpamts * cdrhad.ratio)/(cdrhad.taxrate + 1)),0) as q  FROM cdrdta,cdrhad ");
        sb.append(" WHERE  cdrhad.facno = cdrdta.facno and   cdrhad.shpno = cdrdta.shpno   and cdrhad.houtsta='Y' ");
        sb.append(" group by year(cdrhad.shpdate) ,month(cdrhad.shpdate) ");
        sb.append(" union all ");
        sb.append(" select year(armrech.recdate) year,month(armrech.recdate) month,isnull(sum(armrec.recamts),0) as q from armrech,armrec ");
        sb.append(" where armrech.facno=armrec.facno and armrech.recno=armrec.recno and armrech.recstat='1' and armrech.kind='4' ");
        sb.append(" and armrec.recseq=2 group by year(armrech.recdate),month(armrech.recdate) ");
        sb.append(" ) p  where  p.year = ${y} ");
        //月销售额
        if (status.equals("month")) {
            sb.append(" and p.month = ${m} ");
        }
        //年销售额
        if (status.equals("year")) {
            sb.append(" and p.month <= ${m} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(findYear(date))).replace("${m}", String.valueOf(findMonth(date)));

        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            Object o = query.getSingleResult();
            return Double.parseDouble(o.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 订购项
     *
     * @param date 查询日期
     * @param type purhad表头条件 判断是否包含
     * @param vdrnoH purhad表头条件
     * @param itnbrD purdta表身条件
     * @return 返回行的四个金额 上月、本月、本年累计、去年同期
     */
    private List getPurchaseList(Date date, String type, String vdrnoH, String itnbrD) {
        StringBuilder sb = new StringBuilder();

//        //上月
//        sb.append(" select isnull(sum(d.tramts*ratio/(taxrate+1)),0) from  purhad h,purdta d where h.pono=d.pono and h.prono=d.prono and h.facno=d.facno and hposta<>'W' ");
//        if (!itnbrD.equals("")) {
//            sb.append(" AND d.itnbr in(").append(itnbrD).append(")");
//        }
//        if (!vdrnoH.equals("")) {
//            sb.append(" AND h.vdrno ${type} in (").append(vdrnoH).append(")");
//        }
//        //生产
//        if (type.equals("SC")) {
//            sb.append(" and  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        }
//        sb.append(" AND year(h.podate) =").append(findMonth(date) == 1 ? findYear(date) - 1 : findYear(date));
//        sb.append(" AND month(h.podate) =").append(findMonth(date) == 1 ? 12 : findMonth(date) - 1);
//        sb.append(" UNION  ALL ");
//        //本月
//        sb.append(" select isnull(sum(d.tramts*ratio/(taxrate+1)),0) from  purhad h,purdta d where h.pono=d.pono and h.prono=d.prono and h.facno=d.facno and hposta<>'W' ");
//        if (!itnbrD.equals("")) {
//            sb.append(" AND d.itnbr in(").append(itnbrD).append(")");
//        }
//        if (!vdrnoH.equals("")) {
//            sb.append(" AND h.vdrno ${type} in (").append(vdrnoH).append(")");
//        }
//        //生产
//        if (type.equals("SC")) {
//            sb.append(" and  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        }
//        sb.append(" AND year(h.podate) =").append(findYear(date));
//        sb.append(" AND month(h.podate) =").append(findMonth(date));
//        sb.append(" UNION  ALL ");
//        //本年累计
//        sb.append(" select isnull(sum(d.tramts*ratio/(taxrate+1)),0) from  purhad h,purdta d where h.pono=d.pono and h.prono=d.prono and h.facno=d.facno and hposta<>'W' ");
//        if (!itnbrD.equals("")) {
//            sb.append(" AND d.itnbr in(").append(itnbrD).append(")");
//        }
//        if (!vdrnoH.equals("")) {
//            sb.append(" AND h.vdrno ${type} in (").append(vdrnoH).append(")");
//        }
//        //生产
//        if (type.equals("SC")) {
//            sb.append(" and  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        }
//        sb.append(" AND year(h.podate) =").append(findYear(date));
//        sb.append(" AND month(h.podate) <=").append(findMonth(date));
//        sb.append(" UNION  ALL ");
//        //去年同期累计
//        sb.append(" select isnull(sum(d.tramts*ratio/(taxrate+1)),0) from  purhad h,purdta d where h.pono=d.pono and h.prono=d.prono and h.facno=d.facno and hposta<>'W' ");
//        if (!itnbrD.equals("")) {
//            sb.append(" AND d.itnbr in(").append(itnbrD).append(")");
//        }
//        if (!vdrnoH.equals("")) {
//            sb.append(" AND h.vdrno ${type} in (").append(vdrnoH).append(")");
//        }
//        //生产
//        if (type.equals("SC")) {
//            sb.append(" and  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        }
//        sb.append(" AND year(h.podate) =").append(findYear(date) - 1);
//        sb.append(" AND month(h.podate) <=").append(findMonth(date));
        sb.append(" select isnull(sum(case when year(h.podate)= ").append(findMonth(date) == 1 ? findYear(date) - 1 : findYear(date));
        sb.append(" and month(h.podate)= ").append(findMonth(date) == 1 ? 12 : findMonth(date) - 1);
        sb.append(" then (case when d.dposta <> '98' then d.tramts*h.ratio/(h.taxrate+1) else (d.okqy1*d.unpris)*h.ratio/(h.taxrate+1) end)  ELSE 0 END),0) as '上月', ");
        sb.append(" isnull(sum(case when year(h.podate) = ").append(findYear(date));
        sb.append(" and month(h.podate)= ").append(findMonth(date));
        sb.append(" then (case when d.dposta <> '98' then d.tramts*h.ratio/(h.taxrate+1) else (d.okqy1*d.unpris)*h.ratio/(h.taxrate+1) end)  ELSE 0 END),0) as '本月', ");
        sb.append(" isnull(sum(case when year(h.podate) = ").append(findYear(date));
        sb.append(" AND month(h.podate) <=").append(findMonth(date));
        sb.append(" then (case when d.dposta <> '98' then d.tramts*h.ratio/(h.taxrate+1) else (d.okqy1*d.unpris)*h.ratio/(h.taxrate+1) end)  ELSE 0 END),0) as '年累计', ");
        sb.append(" isnull(sum(case when year(h.podate) = ").append(findYear(date) - 1);
        sb.append(" AND month(h.podate) <=").append(findMonth(date));
        sb.append(" then (case when d.dposta <> '98' then d.tramts*h.ratio/(h.taxrate+1) else (d.okqy1*d.unpris)*h.ratio/(h.taxrate+1) end) ELSE 0 END),0) as '去年同期累计' ");
        sb.append(" from  purhad h,purdta d where h.pono=d.pono and h.prono=d.prono and h.facno=d.facno and hposta<>'W' ");
        if (!itnbrD.equals("")) {
            sb.append(" AND d.itnbr in(").append(itnbrD).append(")");
        }
        if (!vdrnoH.equals("")) {
            sb.append(" AND h.vdrno ${type} in (").append(vdrnoH).append(")");
        }
        //生产
        if (type.equals("SC")) {
            sb.append(" and  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
        }
        //2019年5月7日添加此逻辑 原因：因税改—— 3月部分采购单据在4月初重新打单，此部分不应该计入整年或4月份统计中
        sb.append(" AND  (d.pono>'PC119042743' or  d.pono <'PC119040001') ");

        sb.append(" AND year(h.podate) BETWEEN ").append(findYear(date) - 1).append(" and ").append(findYear(date));

        String sql = sb.toString().replace("${type}", type);
        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            Object[] row = (Object[]) result.get(0);
            List aaList = new ArrayList();
            aaList.add(row[0]);
            aaList.add(row[1]);
            aaList.add(row[2]);
            aaList.add(row[3]);
            return aaList;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ProcurementBean.getPurchaseList()" + e.toString());
        }
        return null;

    }

    /**
     * 入库项
     *
     * @param date 查询日期
     * @param type purhad表头条件 判断是否包含
     * @param vdrnoH purhad表头条件
     * @param itnbrD purdta表身条件
     * @return 返回行的四个金额 上月、本月、本年累计、去年同期
     */
    private List getEnterWarehouseList(Date date, String type, String vdrno, String itnbr) {
        StringBuilder sb = new StringBuilder();

//        //上月
//        sb.append(" SELECT  isnull(sum(acpamt),0) from apmpyh  where pyhkind = '1' ");
//        if (!itnbr.equals("")) {
//            sb.append(" AND itnbr in(").append(itnbr).append(")");
//        }
//        if (!vdrno.equals("")) {
//            sb.append(" AND vdrno ${type} in (").append(vdrno).append(")");
//        }
//        sb.append(" AND year(trdat) =").append(findMonth(date) == 1 ? findYear(date) - 1 : findYear(date));
//        sb.append(" AND month(trdat) =").append(findMonth(date) == 1 ? 12 : findMonth(date) - 1);
//        sb.append(" UNION  ALL ");
//        //本月
//        sb.append(" SELECT  isnull(sum(acpamt),0) from apmpyh  where pyhkind = '1' ");
//        if (!itnbr.equals("")) {
//            sb.append(" AND itnbr in(").append(itnbr).append(")");
//        }
//        if (!vdrno.equals("")) {
//            sb.append(" AND vdrno ${type} in (").append(vdrno).append(")");
//        }
//        sb.append(" AND year(trdat) =").append(findYear(date));
//        sb.append(" AND month(trdat) =").append(findMonth(date));
//        sb.append(" UNION  ALL ");
//        //本年累计
//        sb.append(" SELECT isnull(sum(acpamt),0) from apmpyh  where pyhkind = '1' ");
//        if (!itnbr.equals("")) {
//            sb.append(" AND itnbr in(").append(itnbr).append(")");
//        }
//        if (!vdrno.equals("")) {
//            sb.append(" AND vdrno ${type} in (").append(vdrno).append(")");
//        }
//        sb.append(" AND year(trdat) =").append(findYear(date));
//        sb.append(" AND month(trdat) <=").append(findMonth(date));
//        sb.append(" UNION  ALL ");
//        //去年同期累计
//        sb.append(" SELECT  isnull(sum(acpamt),0) from apmpyh  where pyhkind = '1' ");
//        if (!itnbr.equals("")) {
//            sb.append(" AND itnbr in(").append(itnbr).append(")");
//        }
//        if (!vdrno.equals("")) {
//            sb.append(" AND vdrno ${type} in (").append(vdrno).append(")");
//        }
//        sb.append(" AND year(trdat) =").append(findYear(date) - 1);
//        sb.append(" AND month(trdat) <=").append(findMonth(date));
        sb.append(" select isnull(sum(case when year(trdat) = ").append(findMonth(date) == 1 ? findYear(date) - 1 : findYear(date));
        sb.append(" and month(trdat)= ").append(findMonth(date) == 1 ? 12 : findMonth(date) - 1);
        sb.append(" then acpamt ELSE 0 END),0) as '上月', ");
        sb.append(" isnull(sum(case when year(trdat) = ").append(findYear(date));
        sb.append(" and month(trdat)= ").append(findMonth(date));
        sb.append(" then acpamt ELSE 0 END),0) as '本月', ");
        sb.append(" isnull(sum(case when year(trdat) = ").append(findYear(date));
        sb.append(" AND month(trdat) <=").append(findMonth(date));
        sb.append(" then acpamt ELSE 0 END),0) as '年累计', ");
        sb.append(" isnull(sum(case when year(trdat) = ").append(findYear(date) - 1);
        sb.append(" AND month(trdat) <=").append(findMonth(date));
        sb.append(" then acpamt ELSE 0 END),0) as '去年同期累计' ");
        sb.append(" from apmpyh  where facno='C' and prono='1' AND  pyhkind = '1' ");
        if (!itnbr.equals("")) {
            sb.append(" AND itnbr in(").append(itnbr).append(")");
        }
        if (!vdrno.equals("")) {
            sb.append(" AND vdrno ${type} in (").append(vdrno).append(")");
        }
        sb.append(" AND year(trdat) BETWEEN ").append(findYear(date) - 1).append(" and ").append(findYear(date));

        String sql = sb.toString().replace("${type}", type);

        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            Object[] row = (Object[]) result.get(0);
            List aaList = new ArrayList();
            aaList.add(row[0]);
            aaList.add(row[1]);
            aaList.add(row[2]);
            aaList.add(row[3]);
            return aaList;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ProcurementBean.getEnterWarehouseList()" + e.toString());
        }
        return null;
    }

    /**
     * 入库非生产性项
     *
     * @param date 查询日期
     * @return 返回行的四个金额 上月、本月、本年累计、去年同期
     */
    private List getEnterWarehouseList(Date date) {
        StringBuilder sb = new StringBuilder();

//        //上月
//        sb.append(" select isnull(sum(d.acpamt),0) from  purhad h,apmpyh  d where h.pono=d.pono  AND  d.pyhkind = '1' ");
//        sb.append(" AND  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        sb.append(" AND year(d.trdat) =").append(findMonth(date) == 1 ? findYear(date) - 1 : findYear(date));
//        sb.append(" AND month(d.trdat) =").append(findMonth(date) == 1 ? 12 : findMonth(date) - 1);
//        sb.append(" UNION  ALL ");
//        //本月
//        sb.append(" select isnull(sum(d.acpamt),0) from  purhad h,apmpyh d where h.pono=d.pono  AND  d.pyhkind = '1' ");
//        sb.append(" AND  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        sb.append(" AND year(d.trdat) =").append(findYear(date));
//        sb.append(" AND month(d.trdat) =").append(findMonth(date));
//        sb.append(" UNION  ALL ");
//        //本年累计
//        sb.append(" select isnull(sum(d.acpamt),0) from  purhad h,apmpyh d where h.pono=d.pono AND  d.pyhkind = '1' ");
//        sb.append(" AND  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        sb.append(" AND year(d.trdat) =").append(findYear(date));
//        sb.append(" AND month(d.trdat) <=").append(findMonth(date));
//        sb.append(" UNION  ALL ");
//        //去年同期累计
//        sb.append(" select isnull(sum(d.acpamt),0) from  purhad h,apmpyh d where h.pono=d.pono AND  d.pyhkind = '1' ");
//        sb.append(" AND  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
//        sb.append(" AND year(d.trdat) =").append(findYear(date) - 1);
//        sb.append(" AND month(d.trdat) <=").append(findMonth(date));
        sb.append(" select isnull(sum(case when year(trdat) = ").append(findMonth(date) == 1 ? findYear(date) - 1 : findYear(date));
        sb.append(" and month(trdat)= ").append(findMonth(date) == 1 ? 12 : findMonth(date) - 1);
        sb.append(" then acpamt ELSE 0 END),0) as '上月', ");
        sb.append(" isnull(sum(case when year(trdat) = ").append(findYear(date));
        sb.append(" and month(trdat)= ").append(findMonth(date));
        sb.append(" then acpamt ELSE 0 END),0) as '本月', ");
        sb.append(" isnull(sum(case when year(trdat) = ").append(findYear(date));
        sb.append(" AND month(d.trdat) <=").append(findMonth(date));
        sb.append(" then acpamt ELSE 0 END),0) as '年累计', ");
        sb.append(" isnull(sum(case when year(trdat) = ").append(findYear(date) - 1);
        sb.append(" AND month(d.trdat) <=").append(findMonth(date));
        sb.append(" then acpamt ELSE 0 END),0) as '去年同期累计' ");
        sb.append(" from  purhad h,apmpyh d where h.facno=d.facno  AND h.prono=d.prono and   h.pono=d.pono  AND  d.pyhkind = '1' ");
        sb.append(" AND  h.hmark1 in ('DJ','FW','FY','GD','GJ','HC','LY','SBBY','SBWX','YF','ZJ') ");
        sb.append(" AND year(d.trdat) BETWEEN ").append(findYear(date) - 1).append(" and ").append(findYear(date));

        erpEJB.setCompany("C");
        Query query = erpEJB.getEntityManager().createNativeQuery(sb.toString());
        try {
            List result = query.getResultList();
            Object[] row = (Object[]) result.get(0);
            List aaList = new ArrayList();
            aaList.add(row[0]);
            aaList.add(row[1]);
            aaList.add(row[2]);
            aaList.add(row[3]);
            return aaList;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ProcurementBean.getEnterWarehouseList()" + e.toString());
        }
        return null;
    }

    /**
     * 其他项与总计项计算 并加入map中
     *
     * @param other 需要算入的ROW
     * @param summation 合计项
     * @param summation Row名称
     * @throws Exception
     */
    private void addRowForMap(List other, List summation, String name, Double monthDouble, Double yearDouble) throws Exception {
        if ((other != null && !other.isEmpty()) || (summation != null && !summation.isEmpty())) {
            String[] arr = new String[9];
            //上月值
            arr[0] = df.format(other.get(0));
            //本月值
            arr[1] = df.format(other.get(1));
            //本月百分比
            Double other1 = Double.parseDouble(other.get(1).toString());
            Double sum1 = Double.parseDouble(summation.get(1).toString());
            if (sum1 != 0) {
                arr[2] = dfpercent.format(other1 / sum1 * 100);
            } else {
                if (other1 == 0) {
                    arr[2] = dfpercent.format(0);
                } else {
                    arr[2] = dfpercent.format(100);
                }
            }
            //本年累计
            arr[3] = df.format(other.get(2));
            //本年百分比
            Double other2 = Double.parseDouble(other.get(2).toString());
            Double sum2 = Double.parseDouble(summation.get(2).toString());
            if (sum2 != 0) {
                arr[4] = dfpercent.format(other2 / sum2 * 100);
            } else {
                if (other2 == 0) {
                    arr[4] = dfpercent.format(0);
                } else {
                    arr[4] = dfpercent.format(100);
                }
            }
            //去年同期
            arr[5] = df.format(other.get(3));
            //增减百分比
            Double other3 = Double.parseDouble(other.get(3).toString());
            if (other3 != 0) {
                arr[6] = dfpercent.format((other2 - other3) / other3 * 100);
            } else {
                if (other2 == 0) {
                    arr[6] = dfpercent.format(0);
                } else {
                    arr[6] = dfpercent.format(100);
                }
            }
            if (name.equals("订购非生产性") || name.equals("订购生产性") || name.equals("入库非生产性") || name.equals("入库生产性")) {
                if (monthDouble != 0) {
                    arr[7] = dfpercent.format(other1 / monthDouble * 100);
                } else {
                    if (other1 == 0) {
                        arr[7] = dfpercent.format(0);
                    } else {
                        arr[7] = dfpercent.format(100);
                    }
                }
                if (yearDouble != 0) {
                    arr[8] = dfpercent.format(other2 / yearDouble * 100);
                } else {
                    if (other2 == 0) {
                        arr[8] = dfpercent.format(0);
                    } else {
                        arr[8] = dfpercent.format(100);
                    }
                }
            }
            map.put(name, arr);
        }
    }

    public LinkedHashMap<String, String[]> getMapTable(Date date) {
        String jk1, jk2, jksum, itnbrDj, itnbrZc, itnbrZj, itnbrSgp, itnbrCjjg;
        //2019年5月7日 陆夏玲要求 进口1加入厂商 'STW00035'，2023年1月4号进口1加入STW00045
        jk1 = "'STW00007','STW00015','SXG00006','SXG00003','SXG00005','SXG00007','SXG00001','SXG00004','STW00033','STW00035','STW00045'";
        //2019年12月31日 陆夏玲要求加入STW00004  （从2020年1月开始润滑油原来贸易商CPA ENGINEERING SERVICES LTD.（STW00002）现变更抬头为FOU TAI ENTERPRISE CO.,LTD.（STW00004））
        //2021年12月13日 陆夏玲要求加入SDA00005
        jk2 = "'STW00010','STW00001','STW00002','STW00004','STW00013','SDM00001','STW00028','STW00029','STW00020','SDQ00002','STW00009','SDQ00004','SXG00011','SXG00009','STW00030','SXG00013','STW00024','STW00036','SDF00001','SDA00005'";
        jksum = jk1 + "," + jk2;
        //2019年2月26日 陆夏玲要求加入'4304'，
        //2021年12月13日 陆夏玲要求加入'4504','3703'
        //#ITCLS CHANGE TODO #
        itnbrDj = " select itnbr from invmas where itcls in('3104','4503','4703','3504','4304','4504','3703') ";
        //#ITCLS CHANGE TODO #
        itnbrZc = " select itnbr from invmas where itcls in('4009') ";
        //2019年2月26日 陆夏玲要求加入'1401'，删除'1E01','1E02','1K02'，
        //2022/02/05，件号转换后加入1C02
        //#ITCLS CHANGE TODO #
        itnbrZj = " select itnbr from invmas where itcls in('1101','1102','1201','1202','1401','1402','1801','1802','1C02')";
        //2019年2月26日 陆夏玲要求去除5063（研发大类），新增4020
        //2021年12月13日 陆夏玲要求加入 4520	A风扇/联轴器/弹性体
         //2022/02/05，件号转换后加入移除4020，添加'4522','4523','5068'
        //#ITCLS CHANGE TODO #
        itnbrSgp = " select itnbr from invmas where itcls in('4010','4046','4047','4048','4049','5050','4052','4151','5061','5062','5064','5065','4079','4507','4520','4522','4523','5068')";
        //2019年2月26日 陆夏玲要求新增2401,3401，去除3016（9QC零件）
        //#ITCLS CHANGE TODO #
        itnbrCjjg = " select itnbr from invmas where itcls in('2012','2013','2015','2101','2102','2201','2202','2402','2801','2802','3012','3013','3015','3101','3102','3201','3202','3402','3801','3802','2401','3401','3C02')";
        try {
            //销售额
            Double monthDouble = getSaleValue(date, "month");
            Double yearDouble = getSaleValue(date, "year");

            if (monthDouble != null || yearDouble != null) {
                String[] aa = new String[2];
                aa[0] = df.format(monthDouble / 10000);
                aa[1] = df.format(yearDouble / 10000);
                map.put("销售额", aa);
            }
            // 订购-------------------------------------------------------------
            List summation = getPurchaseList(date, "", "", "");
//
            List dgjk1 = getPurchaseList(date, "", jk1, "");
            addRowForMap(dgjk1, summation, "订购进口1", monthDouble, yearDouble);

            List dgjk2 = getPurchaseList(date, "", jk2, "");
            addRowForMap(dgjk2, summation, "订购进口2", monthDouble, yearDouble);

            List dgdj = getPurchaseList(date, "not", jksum, itnbrDj);
            addRowForMap(dgdj, summation, "订购电机", monthDouble, yearDouble);

            List dgzc = getPurchaseList(date, "not", jksum, itnbrZc);
            addRowForMap(dgzc, summation, "订购轴承", monthDouble, yearDouble);

            List dgzj = getPurchaseList(date, "not", jksum, itnbrZj);
            addRowForMap(dgzj, summation, "订购铸件", monthDouble, yearDouble);

            List dgsgp = getPurchaseList(date, "not", jksum, itnbrSgp);
            addRowForMap(dgsgp, summation, "订购市购品", monthDouble, yearDouble);

            List dgcjjg = getPurchaseList(date, "not", jksum, itnbrCjjg);
            addRowForMap(dgcjjg, summation, "订购粗精加工", monthDouble, yearDouble);

            addRowForMap(getWgpList(summation, dgjk1, dgjk2, dgdj, dgzc, dgzj, dgsgp, dgcjjg), summation, "订购外购品", monthDouble, yearDouble);
            addRowForMap(summation, summation, "订购合计", monthDouble, yearDouble);

            List dgfscx = getPurchaseList(date, "SC", "", "");
            addRowForMap(dgfscx, summation, "订购非生产性", monthDouble, yearDouble);
            addRowForMap(getScxList(summation, dgfscx), summation, "订购生产性", monthDouble, yearDouble);
//            入库-------------------------------------------------------------
            List rksummation = getEnterWarehouseList(date, "", "", "");

            List rkjk1 = getEnterWarehouseList(date, "", jk1, "");
            addRowForMap(rkjk1, rksummation, "入库进口1", monthDouble, yearDouble);

            List rkjk2 = getEnterWarehouseList(date, "", jk2, "");
            addRowForMap(rkjk2, rksummation, "入库进口2", monthDouble, yearDouble);

            List rkdj = getEnterWarehouseList(date, "not", jksum, itnbrDj);
            addRowForMap(rkdj, rksummation, "入库电机", monthDouble, yearDouble);

            List rkzc = getEnterWarehouseList(date, "not", jksum, itnbrZc);
            addRowForMap(rkzc, rksummation, "入库轴承", monthDouble, yearDouble);

            List rkzj = getEnterWarehouseList(date, "not", jksum, itnbrZj);
            addRowForMap(rkzj, rksummation, "入库铸件", monthDouble, yearDouble);

            List rksgp = getEnterWarehouseList(date, "not", jksum, itnbrSgp);
            addRowForMap(rksgp, rksummation, "入库市购品", monthDouble, yearDouble);

            List rkcjjg = getEnterWarehouseList(date, "not", jksum, itnbrCjjg);
            addRowForMap(rkcjjg, rksummation, "入库粗精加工", monthDouble, yearDouble);

            addRowForMap(getWgpList(rksummation, rkjk1, rkjk2, rkdj, rkzc, rkzj, rksgp, rkcjjg), rksummation, "入库外购品", monthDouble, yearDouble);
            addRowForMap(rksummation, rksummation, "入库合计", monthDouble, yearDouble);

            List rkfscx = getEnterWarehouseList(date);
            addRowForMap(rkfscx, rksummation, "入库非生产性", monthDouble, yearDouble);
            addRowForMap(getScxList(rksummation, rkfscx), rksummation, "入库生产性", monthDouble, yearDouble);
            return map;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ProcurementBean.getMapTable()" + e.toString());
        }
        return null;
    }

    //外购品=总计-其他7项
    private List getWgpList(List summation, List dgjk1, List dgjk2, List dgdj, List dgzc, List dgzj, List dgsgp, List dgcjjg) {
        List wglist = new ArrayList();
        Double a0 = Double.parseDouble(summation.get(0).toString()) - Double.parseDouble(dgjk1.get(0).toString())
                - Double.parseDouble(dgjk2.get(0).toString()) - Double.parseDouble(dgdj.get(0).toString()) - Double.parseDouble(dgzc.get(0).toString())
                - Double.parseDouble(dgzj.get(0).toString()) - Double.parseDouble(dgsgp.get(0).toString()) - Double.parseDouble(dgcjjg.get(0).toString());
        Double a1 = Double.parseDouble(summation.get(1).toString()) - Double.parseDouble(dgjk1.get(1).toString())
                - Double.parseDouble(dgjk2.get(1).toString()) - Double.parseDouble(dgdj.get(1).toString()) - Double.parseDouble(dgzc.get(1).toString())
                - Double.parseDouble(dgzj.get(1).toString()) - Double.parseDouble(dgsgp.get(1).toString()) - Double.parseDouble(dgcjjg.get(1).toString());
        Double a2 = Double.parseDouble(summation.get(2).toString()) - Double.parseDouble(dgjk1.get(2).toString())
                - Double.parseDouble(dgjk2.get(2).toString()) - Double.parseDouble(dgdj.get(2).toString()) - Double.parseDouble(dgzc.get(2).toString())
                - Double.parseDouble(dgzj.get(2).toString()) - Double.parseDouble(dgsgp.get(2).toString()) - Double.parseDouble(dgcjjg.get(2).toString());
        Double a3 = Double.parseDouble(summation.get(3).toString()) - Double.parseDouble(dgjk1.get(3).toString())
                - Double.parseDouble(dgjk2.get(3).toString()) - Double.parseDouble(dgdj.get(3).toString()) - Double.parseDouble(dgzc.get(3).toString())
                - Double.parseDouble(dgzj.get(3).toString()) - Double.parseDouble(dgsgp.get(3).toString()) - Double.parseDouble(dgcjjg.get(3).toString());
        wglist.add(a0);
        wglist.add(a1);
        wglist.add(a2);
        wglist.add(a3);
        return wglist;
    }

    //生产性=总计-非生产性
    private List getScxList(List summation, List dgfscx) {
        List sclist = new ArrayList();
        Double a0 = Double.parseDouble(summation.get(0).toString()) - Double.parseDouble(dgfscx.get(0).toString());
        Double a1 = Double.parseDouble(summation.get(1).toString()) - Double.parseDouble(dgfscx.get(1).toString());
        Double a2 = Double.parseDouble(summation.get(2).toString()) - Double.parseDouble(dgfscx.get(2).toString());
        Double a3 = Double.parseDouble(summation.get(3).toString()) - Double.parseDouble(dgfscx.get(3).toString());
        sclist.add(a0);
        sclist.add(a1);
        sclist.add(a2);
        sclist.add(a3);
        return sclist;
    }
}
