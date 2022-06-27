/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ClientRanking;
import cn.hanbell.kpi.entity.SalesTable;
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
public class SalesTableBean extends SuperEJBForKPI<SalesTable> {

    @EJB
    private SalesTableUpdateBean salesTableUpdateBean;

    private final DecimalFormat df;
    private final DecimalFormat dmf;

    public SalesTableBean() {
        super(SalesTable.class);
        this.df = new DecimalFormat("#");
        this.dmf = new DecimalFormat("#0.00");
    }

    public boolean querySalesTableIsExist(int y, int m, String daString, String typeString) {
        String da = daString == null ? "" : daString;
        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT * FROM SalesTable  WHERE type='${type}' and year(cdrdate)=${y} and month(cdrdate)=${m} ");
        if (!"".equals(da) && !"外销零件".equals(da)) {
            sb.append(" and n_code_DA = '").append(da).append("'");
            sb.append(" AND hmark1<>'WXLJ' ");
        } else if ("外销零件".equals(da)) {
            sb.append(" and n_code_CD LIKE 'WX%' AND hmark1='WXLJ' ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", typeString);
        Query query = getEntityManager().createNativeQuery(sql);
        try {
            if (!query.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.querySalesTableIsExist()" + e);
        }
        return false;
    }

    private void deleteSales(int y, int m, String daString, String typeString) {
        String da = daString == null ? "" : daString;
        StringBuilder sb = new StringBuilder();
        sb.append(" delete from SalesTable  WHERE type='${type}' and year(cdrdate)=${y} and month(cdrdate)=${m}  ");
        if (!"".equals(da) && !"外销零件".equals(da)) {
            sb.append(" and n_code_DA = '").append(da).append("'");
            sb.append(" AND hmark1<>'WXLJ' ");
        } else if ("外销零件".equals(da)) {
            sb.append(" and n_code_CD LIKE 'WX%' AND hmark1='WXLJ'");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", typeString);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            int count = query.executeUpdate();
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.deleteSales()受影响行数：" + count);
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.deleteSales()" + e);
        }

    }

//    /**
//     * 增加删除逻辑--RT新统计逻辑需要删除上海柯茂销售给上海汉钟RT部分
//     *
//     * @param y
//     * @param m
//     */
//    private void deleteShbRT(int y, int m) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(" delete from SalesTable  WHERE  cusno='KSH00004' and n_code_DC='RT' AND  year(cdrdate)=${y} and month(cdrdate)=${m}  ");
//        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));
//        try {
//            Query query = getEntityManager().createNativeQuery(sql);
//            int count = query.executeUpdate();
//            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.deleteShbRT()受影响行数：" + count);
//        } catch (Exception e) {
//            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.deleteShbRT()" + e);
//        }
//        
//    }
    //更新到KPI
    public boolean updateSalesTable(int y, int m, String daString, String typeString) {
        String da = daString == null ? "" : daString;
        List<SalesTable> SalesTableList = new ArrayList<>();
        if ("Shipment".equals(typeString)) {
            SalesTableList = salesTableUpdateBean.getShipmentListSum(y, m, da, typeString);
        }
        if ("SalesOrder".equals(typeString)) {
            SalesTableList = salesTableUpdateBean.getSalesOrderListSum(y, m, da, typeString);
        }
        if ("ServiceAmount".equals(typeString)) {
            SalesTableList = salesTableUpdateBean.getServiceAmountListSum(y, m, da, typeString);
        }
        try {
            if (SalesTableList != null && !SalesTableList.isEmpty()) {
                if (querySalesTableIsExist(y, m, da, typeString)) {
                    deleteSales(y, m, da, typeString);
                }
                for (SalesTable salesTable : SalesTableList) {
                    salesTable.setStatus("N");
                    super.persist(salesTable);
                }
//                //调用RT删除逻辑
//                deleteShbRT(y, m);
                return true;
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.updateSalesTable()" + e.toString());
        }
        return false;
    }

    //获得总台数
    protected Double getSumQuantity(int y, int m, LinkedHashMap<String, String> map, String type, Boolean monthchecked) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD") : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT sum(quantity) FROM SalesTable where type='${type}' ");
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND n_code_CD ").append(n_code_CD);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (monthchecked) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            return Double.parseDouble(o1.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    //获得总金额
    protected Double getSumAmount(int y, int m, LinkedHashMap<String, String> map, String type, Boolean monthchecked) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD") : "";

        StringBuilder sb = new StringBuilder();
        sb.append(" SELECT sum(amount) FROM SalesTable where type='${type}' ");
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND n_code_CD ").append(n_code_CD);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (monthchecked) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            Query query = getEntityManager().createNativeQuery(sql);
            Object o1 = query.getSingleResult();
            return Double.parseDouble(o1.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    // 返回当前ClientRanking集合getNowClient
    protected List<ClientRanking> getNowClient(int y, int m, LinkedHashMap<String, String> map, String type, Boolean monthchecked, Boolean aggregatechecked, String rowsPerPage) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD") : "";

        StringBuilder sb = new StringBuilder();
        if (aggregatechecked) {
            sb.append(" Select parentcusno,parentcusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        }
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND n_code_CD ").append(n_code_CD);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (monthchecked) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        if (aggregatechecked) {
            sb.append(" GROUP BY parentcusno ORDER BY sum(amount) desc");
        } else {
            sb.append(" GROUP BY cusno ORDER BY sum(amount) desc");
        }
        String sqlsize = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type).replace("${rowsPerPage}", rowsPerPage);

        if (!"0".equals(rowsPerPage)) {
            sb.append("  LIMIT ${rowsPerPage} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type).replace("${rowsPerPage}", rowsPerPage);
        try {
            ClientRanking ct;
            List<ClientRanking> list = new ArrayList<>();
            Query query1 = getEntityManager().createNativeQuery(sqlsize);
            int size = query1.getResultList().size();
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ct = new ClientRanking();
                    Object[] row = (Object[]) result.get(i);
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setNowrank(String.valueOf(i + 1));
                    ct.setNowshpqy1(row[2].toString());
                    ct.setNowshpamts(row[3].toString());
                    list.add(ct);
                }
                if (!"0".equals(rowsPerPage) && size > Integer.parseInt(rowsPerPage)) {
                    ct = new ClientRanking();
                    ct.setCusna("其他");
                    ct.setNowshpqy1("0");
                    ct.setNowshpamts("0");
                    list.add(ct);
                }
                ct = new ClientRanking();
                ct.setCusna("总计");
                ct.setNowshpqy1(String.valueOf(getSumQuantity(y, m, map, type, monthchecked)));
                ct.setNowshpamts(String.valueOf(getSumAmount(y, m, map, type, monthchecked)));
                list.add(ct);
            }
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClient()" + e);
        }
        return null;
    }

    // 返回当前ClientRanking集合getNowClientByCodeDA
    protected List<ClientRanking> getNowClientByCodeDA(int y, int m, LinkedHashMap<String, String> map, String type, Boolean monthchecked, Boolean aggregatechecked, String rowsPerPage) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD") : "";

        StringBuilder sb = new StringBuilder();
        if (aggregatechecked) {
            sb.append(" Select parentcusno,parentcusna,n_code_DA,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,n_code_DA,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        }
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND n_code_CD ").append(n_code_CD);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (monthchecked) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        if (aggregatechecked) {
            sb.append(" GROUP BY parentcusno,n_code_DA ORDER BY sum(amount) desc");
        } else {
            sb.append(" GROUP BY cusno,n_code_DA ORDER BY sum(amount) desc");
        }
        String sqlsize = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type).replace("${rowsPerPage}", rowsPerPage);

        if (!"0".equals(rowsPerPage)) {
            sb.append("  LIMIT ${rowsPerPage} ");
        }

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type).replace("${rowsPerPage}", rowsPerPage);
        try {
            ClientRanking ct;
            List<ClientRanking> list = new ArrayList<>();
            Query query1 = getEntityManager().createNativeQuery(sqlsize);
            int size = query1.getResultList().size();
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ct = new ClientRanking();
                    Object[] row = (Object[]) result.get(i);
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setN_code_DA(row[2].toString());
                    ct.setNowrank(String.valueOf(i + 1));
                    ct.setNowshpqy1(row[3].toString());
                    ct.setNowshpamts(row[4].toString());
                    list.add(ct);
                }
                if (!"0".equals(rowsPerPage) && size > Integer.parseInt(rowsPerPage)) {
                    ct = new ClientRanking();
                    ct.setCusna("其他");
                    ct.setNowshpqy1("0");
                    ct.setN_code_DA("");
                    ct.setNowshpamts("0");
                    list.add(ct);
                }
                ct = new ClientRanking();
                ct.setCusna("总计");
                ct.setN_code_DA("");
                ct.setNowshpqy1(String.valueOf(getSumQuantity(y, m, map, type, monthchecked)));
                ct.setNowshpamts(String.valueOf(getSumAmount(y, m, map, type, monthchecked)));
                list.add(ct);
            }
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.getNowClientByCodeDA()" + e.toString());
        }
        return null;
    }

    // 返回同期ClientRanking集合
    protected List<ClientRanking> getPastClient(int y, int m, LinkedHashMap<String, String> map, String type, Boolean monthchecked, Boolean aggregatechecked) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD") : "";

        StringBuilder sb = new StringBuilder();
        if (aggregatechecked) {
            sb.append(" Select parentcusno,parentcusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        }
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND n_code_CD ").append(n_code_CD);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (monthchecked) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        if (aggregatechecked) {
            sb.append(" GROUP BY parentcusno ORDER BY sum(amount) desc ");
        } else {
            sb.append(" GROUP BY cusno ORDER BY sum(amount) desc");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            ClientRanking ct;
            List<ClientRanking> list = new ArrayList<>();
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ct = new ClientRanking();
                    Object[] row = (Object[]) result.get(i);
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setPastrank(String.valueOf(i + 1));
                    ct.setPastshpqy1(row[2].toString());
                    ct.setPastshpamts(row[3].toString());
                    list.add(ct);
                }
                ct = new ClientRanking();
                ct.setCusna("总计");
                ct.setPastshpqy1(String.valueOf(getSumQuantity(y, m, map, type, monthchecked)));
                ct.setPastshpamts(String.valueOf(getSumAmount(y, m, map, type, monthchecked)));
                list.add(ct);
            }
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientNowAndPastBean.getClient()" + e);
        }
        return null;
    }

    // 返回同期ClientRanking集合getPastClientByCodeDA
    protected List<ClientRanking> getPastClientByCodeDA(int y, int m, LinkedHashMap<String, String> map, String type, Boolean monthchecked, Boolean aggregatechecked) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_CD = map.get("n_code_CD") != null ? map.get("n_code_CD") : "";
        StringBuilder sb = new StringBuilder();
        if (aggregatechecked) {
            sb.append(" Select parentcusno,parentcusna,n_code_DA,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,n_code_DA,sum(quantity),sum(amount) FROM  SalesTable where type='${type}' ");
        }
        sb.append(" AND n_code_DA ").append(n_code_DA);
        if (!"".equals(n_code_DC)) {
            sb.append(" AND n_code_DC ").append(n_code_DC);
        }
        if (!"".equals(n_code_CD)) {
            sb.append(" AND n_code_CD ").append(n_code_CD);
        }
        sb.append(" AND year(cdrdate) = ${y} ");
        if (monthchecked) {
            sb.append(" and month(cdrdate) = ${m} ");
        } else {
            sb.append(" and month(cdrdate) BETWEEN 1 AND ${m} ");
        }
        if (aggregatechecked) {
            sb.append(" GROUP BY parentcusno,n_code_DA ORDER BY sum(amount) desc ");
        } else {
            sb.append(" GROUP BY cusno,n_code_DA ORDER BY sum(amount) desc");
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type);
        try {
            ClientRanking ct;
            List<ClientRanking> list = new ArrayList<>();
            Query query = getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (result != null && !result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    ct = new ClientRanking();
                    Object[] row = (Object[]) result.get(i);
                    ct.setCusno(row[0].toString());
                    ct.setCusna(row[1].toString());
                    ct.setN_code_DA(row[2].toString());
                    ct.setPastrank(String.valueOf(i + 1));
                    ct.setPastshpqy1(row[3].toString());
                    ct.setPastshpamts(row[4].toString());
                    list.add(ct);
                }
                ct = new ClientRanking();
                ct.setCusna("总计");
                ct.setN_code_DA("");
                ct.setPastshpqy1(String.valueOf(getSumQuantity(y, m, map, type, monthchecked)));
                ct.setPastshpamts(String.valueOf(getSumAmount(y, m, map, type, monthchecked)));
                list.add(ct);
            }
            return list;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.getPastClientByCodeDA()" + e.toString());
        }
        return null;
    }

    /**
     *
     * @param y 查询年
     * @param m 查询月
     * @param map 查询参数
     * @param monthchecked 是为月查询 否为年查询
     * @param aggregatechecked 是否客户整合
     * @param rowsPerPage 显示行数
     * @return
     */
    public List<ClientRanking> getClientList(int y, int m, LinkedHashMap<String, String> map, Boolean monthchecked, Boolean aggregatechecked, String rowsPerPage) {
        String type = "Shipment";
        List<ClientRanking> list = new ArrayList<>();
        //得到已经有排名的list
        //NowClient
        List<ClientRanking> nowList = getNowClient(y, m, map, type, monthchecked, aggregatechecked, rowsPerPage);
        //PastClient
        List<ClientRanking> pastList = getPastClient(y - 1, m, map, type, monthchecked, aggregatechecked);
        //UltClient
        List<ClientRanking> ultList;
        if (monthchecked) {
            ultList = getPastClient(m == 1 ? y - 1 : y, m == 1 ? 12 : m - 1, map, type, monthchecked, aggregatechecked);
        } else {
            ultList = null;
        }
        try {
            //判断是否有其他项
            Boolean other = false;
            ClientRanking cr;
            if (nowList != null && !nowList.isEmpty()) {
                for (ClientRanking now : nowList) {
                    cr = new ClientRanking();
                    cr.setCusno(now.getCusno());
                    cr.setCusna(now.getCusna());
                    cr.setNowrank(now.getNowrank());
                    cr.setNowshpqy1(now.getNowshpqy1());
                    cr.setNowshpamts(now.getNowshpamts());
                    cr.setPastshpqy1("0");
                    cr.setPastshpamts("0");
                    cr.setUltshpqy1("0");
                    cr.setUltshpamts("0");
                    //找到同期数据
                    if (pastList != null && !pastList.isEmpty()) {
                        for (ClientRanking past : pastList) {
                            if (now.getCusna().equals(past.getCusna())) {
                                cr.setPastrank(past.getPastrank());
                                cr.setPastshpqy1(past.getPastshpqy1());
                                cr.setPastshpamts(past.getPastshpamts());
                                break;
                            }
                        }
                    }
                    if (ultList != null && !ultList.isEmpty()) {
                        for (ClientRanking ult : ultList) {
                            if (now.getCusna().equals(ult.getCusna())) {
                                cr.setUltshpqy1(ult.getPastshpqy1());
                                cr.setUltshpamts(ult.getPastshpamts());
                                break;
                            }
                        }
                    }
                    if ("其他".equals(now.getCusna())) {
                        other = true;
                    }
                    list.add(cr);
                }
                //计算其他项
                Double topNowshpqy1 = 0.0;
                Double topPastshpqy1 = 0.0;
                Double topUltshpqy1 = 0.0;

                Double topNowshpamts = 0.0;
                Double topPastshpamts = 0.0;
                Double topUltshpamts = 0.0;
                if (other) {
                    for (ClientRanking ranking : list) {
                        if (!"总计".equals(ranking.getCusna())) {
                            topNowshpqy1 += Double.parseDouble(ranking.getNowshpqy1());
                            topPastshpqy1 += Double.parseDouble(ranking.getPastshpqy1());
                            topUltshpqy1 += Double.parseDouble(ranking.getUltshpqy1());

                            topNowshpamts += Double.parseDouble(ranking.getNowshpamts());
                            topPastshpamts += Double.parseDouble(ranking.getPastshpamts());
                            topUltshpamts += Double.parseDouble(ranking.getUltshpamts());
                        }
                        if ("总计".equals(ranking.getCusna())) {
                            topNowshpqy1 = Double.parseDouble(ranking.getNowshpqy1()) - topNowshpqy1;
                            topPastshpqy1 = Double.parseDouble(ranking.getPastshpqy1()) - topPastshpqy1;
                            topUltshpqy1 = Double.parseDouble(ranking.getUltshpqy1()) - topUltshpqy1;

                            topNowshpamts = Double.parseDouble(ranking.getNowshpamts()) - topNowshpamts;
                            topPastshpamts = Double.parseDouble(ranking.getPastshpamts()) - topPastshpamts;
                            topUltshpamts = Double.parseDouble(ranking.getUltshpamts()) - topUltshpamts;
                        }
                    }
                }
                for (ClientRanking ranking : list) {
                    if ("其他".equals(ranking.getCusna())) {
                        ranking.setNowshpqy1(topNowshpqy1.toString());
                        ranking.setNowshpamts(topNowshpamts.toString());
                        ranking.setPastshpqy1(topPastshpqy1.toString());
                        ranking.setPastshpamts(topPastshpamts.toString());
                        ranking.setUltshpqy1(topUltshpqy1.toString());
                        ranking.setUltshpamts(topUltshpamts.toString());
                    }
                    if ("总计".equals(ranking.getCusna())) {

                    }
                    ranking.setDifferencevalue(CRdifferencevalue(ranking.getNowshpamts(), ranking.getPastshpamts()));
                    ranking.setGrowthrate(CRrate(ranking.getNowshpamts(), ranking.getPastshpamts()));
                    ranking.setShpqy1growthrate(CRrate(ranking.getNowshpqy1(), ranking.getPastshpqy1()));
                    if (Double.parseDouble(ranking.getGrowthrate()) < 0) {
                        ranking.setPaststyle("red");
                    }
                    ranking.setShpqy1chainrate(CRrate(ranking.getNowshpqy1(), ranking.getUltshpqy1()));
                    ranking.setShpamtschainrate(CRrate(ranking.getNowshpamts(), ranking.getUltshpamts()));
                    if (Double.parseDouble(ranking.getShpamtschainrate()) < 0) {
                        ranking.setUltstyle("red");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.getClientList()" + e.toString());
        }
        return list;
    }

    /**
     *
     * @param y 查询年
     * @param m 查询月
     * @param map 查询参数
     * @param monthchecked 是为月查询 否为年查询
     * @param aggregatechecked 是否客户整合
     * @param rowsPerPage 显示行数
     * @return
     */
    public List<ClientRanking> getClientListByCodeDA(int y, int m, LinkedHashMap<String, String> map, Boolean monthchecked, Boolean aggregatechecked, String rowsPerPage) {
        String type = "Shipment";
        List<ClientRanking> list = new ArrayList<>();
        //得到已经有排名的list
        //NowClient
        List<ClientRanking> nowList = getNowClientByCodeDA(y, m, map, type, monthchecked, aggregatechecked, rowsPerPage);
        //PastClient
        List<ClientRanking> pastList = getPastClientByCodeDA(y - 1, m, map, type, monthchecked, aggregatechecked);
        //UltClient
        List<ClientRanking> ultList;
        if (monthchecked) {
            ultList = getPastClientByCodeDA(m == 1 ? y - 1 : y, m == 1 ? 12 : m - 1, map, type, monthchecked, aggregatechecked);
        } else {
            ultList = null;
        }
        try {
            //判断是否有其他项
            Boolean other = false;
            ClientRanking cr;
            if (nowList != null && !nowList.isEmpty()) {
                for (ClientRanking now : nowList) {
                    cr = new ClientRanking();
                    cr.setCusno(now.getCusno());
                    cr.setCusna(now.getCusna());
                    cr.setN_code_DA(now.getN_code_DA());
                    cr.setNowrank(now.getNowrank());
                    cr.setNowshpqy1(now.getNowshpqy1());
                    cr.setNowshpamts(now.getNowshpamts());
                    cr.setPastshpqy1("0");
                    cr.setPastshpamts("0");
                    cr.setUltshpqy1("0");
                    cr.setUltshpamts("0");
                    //找到同期数据
                    if (pastList != null && !pastList.isEmpty()) {
                        for (ClientRanking past : pastList) {
                            if (now.getCusna().equals(past.getCusna()) && now.getN_code_DA().equals(past.getN_code_DA())) {
                                cr.setPastrank(past.getPastrank());
                                cr.setPastshpqy1(past.getPastshpqy1());
                                cr.setPastshpamts(past.getPastshpamts());
                                break;
                            }
                        }
                    }
                    if (ultList != null && !ultList.isEmpty()) {
                        for (ClientRanking ult : ultList) {
                            if (now.getCusna().equals(ult.getCusna()) && now.getN_code_DA().equals(ult.getN_code_DA())) {
                                cr.setUltshpqy1(ult.getPastshpqy1());
                                cr.setUltshpamts(ult.getPastshpamts());
                                break;
                            }
                        }
                    }
                    if ("其他".equals(now.getCusna())) {
                        other = true;
                    }
                    list.add(cr);
                }
                //计算其他项
                Double topNowshpqy1 = 0.0;
                Double topPastshpqy1 = 0.0;
                Double topUltshpqy1 = 0.0;

                Double topNowshpamts = 0.0;
                Double topPastshpamts = 0.0;
                Double topUltshpamts = 0.0;
                if (other) {
                    for (ClientRanking ranking : list) {
                        if (!"总计".equals(ranking.getCusna())) {
                            topNowshpqy1 += Double.parseDouble(ranking.getNowshpqy1());
                            topPastshpqy1 += Double.parseDouble(ranking.getPastshpqy1());
                            topUltshpqy1 += Double.parseDouble(ranking.getUltshpqy1());

                            topNowshpamts += Double.parseDouble(ranking.getNowshpamts());
                            topPastshpamts += Double.parseDouble(ranking.getPastshpamts());
                            topUltshpamts += Double.parseDouble(ranking.getUltshpamts());
                        }
                        if ("总计".equals(ranking.getCusna())) {
                            topNowshpqy1 = Double.parseDouble(ranking.getNowshpqy1()) - topNowshpqy1;
                            topPastshpqy1 = Double.parseDouble(ranking.getPastshpqy1()) - topPastshpqy1;
                            topUltshpqy1 = Double.parseDouble(ranking.getUltshpqy1()) - topUltshpqy1;

                            topNowshpamts = Double.parseDouble(ranking.getNowshpamts()) - topNowshpamts;
                            topPastshpamts = Double.parseDouble(ranking.getPastshpamts()) - topPastshpamts;
                            topUltshpamts = Double.parseDouble(ranking.getUltshpamts()) - topUltshpamts;
                        }
                    }
                }
                for (ClientRanking ranking : list) {
                    if ("其他".equals(ranking.getCusna())) {
                        ranking.setNowshpqy1(topNowshpqy1.toString());
                        ranking.setNowshpamts(topNowshpamts.toString());
                        ranking.setPastshpqy1(topPastshpqy1.toString());
                        ranking.setPastshpamts(topPastshpamts.toString());
                        ranking.setUltshpqy1(topUltshpqy1.toString());
                        ranking.setUltshpamts(topUltshpamts.toString());
                        ranking.setN_code_DA("");
                    }
                    if ("总计".equals(ranking.getCusna())) {
                        ranking.setN_code_DA("");
                    }
                    ranking.setDifferencevalue(CRdifferencevalue(ranking.getNowshpamts(), ranking.getPastshpamts()));
                    ranking.setGrowthrate(CRrate(ranking.getNowshpamts(), ranking.getPastshpamts()));
                    ranking.setShpqy1growthrate(CRrate(ranking.getNowshpqy1(), ranking.getPastshpqy1()));
                    if (Double.parseDouble(ranking.getGrowthrate()) < 0) {
                        ranking.setPaststyle("red");
                    }
                    ranking.setShpqy1chainrate(CRrate(ranking.getNowshpqy1(), ranking.getUltshpqy1()));
                    ranking.setShpamtschainrate(CRrate(ranking.getNowshpamts(), ranking.getUltshpamts()));
                    if (Double.parseDouble(ranking.getShpamtschainrate()) < 0) {
                        ranking.setUltstyle("red");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.getClientListByCodeDA()" + e.toString());
        }
        return list;
    }

    // 同比差异值 = 本年累计金额 - 去年同期累计金额
    protected String CRdifferencevalue(String now, String past) {
        return df.format(Double.parseDouble(now) - Double.parseDouble(past));
    }

    // 同比成长率 = (本年累计 - 去年同期累计)/去年同期累计*100
    // 环比增长率 = (本月 - 上月)/上月*100
    protected String CRrate(String now, String past) {
        if (Double.parseDouble(past) <= 0) {
            if (Double.parseDouble(now) <= 0) {
                return df.format(0);
            } else {
                return df.format(100);
            }
        } else {
            if (Double.parseDouble(now) < 0) {
                return df.format(-100);
                
            } else {
                return dmf.format((Double.parseDouble(now) - Double.parseDouble(past)) / Double.parseDouble(past) * 100);
            }
        }
    }

}
