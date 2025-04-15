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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
            sb.append(" Select parentcusno,parentcusna,sum(quantity),sum(amount),n_code_DA FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,sum(quantity),sum(amount),n_code_DA FROM  SalesTable where type='${type}' ");
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

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m)).replace("${type}", type).replace("${rowsPerPage}", rowsPerPage);
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
                    ct.setNowrank(String.valueOf(i + 1));
                    ct.setNowshpqy1(row[2].toString());
                    ct.setNowshpamts(row[3].toString());
                    ct.setN_code_DA(row[4].toString());
                    list.add(ct);
                }
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
            sb.append(" Select parentcusno,parentcusna,sum(quantity),sum(amount),n_code_DA FROM  SalesTable where type='${type}' ");
        } else {
            sb.append(" Select cusno,cusna,sum(quantity),sum(amount),n_code_DA FROM  SalesTable where type='${type}' ");
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
                    ct.setPastrank(String.valueOf(i + 1));
                    ct.setPastshpqy1(row[2].toString());
                    ct.setPastshpamts(row[3].toString());
                    ct.setN_code_DA(row[4].toString());
                    list.add(ct);
                }
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
        Set<ClientRanking> set = new HashSet<ClientRanking>();
        //得到已经有排名的list
        //NowClient
        List<ClientRanking> nowList = getNowClient(y, m, map, type, monthchecked, aggregatechecked, rowsPerPage);
        set.addAll(nowList.stream().map(source -> new ClientRanking(source.getCusno(), source.getCusna(), source.getN_code_DA())).collect(Collectors.toList()));

        //PastClient
        List<ClientRanking> pastList = getPastClient(y - 1, m, map, type, monthchecked, aggregatechecked);
        set.addAll(pastList.stream().map(source -> new ClientRanking(source.getCusno(), source.getCusna(), source.getN_code_DA())).collect(Collectors.toList()));
        //UltClient
        List<ClientRanking> ultList;
        if (monthchecked) {
            ultList = getPastClient(m == 1 ? y - 1 : y, m == 1 ? 12 : m - 1, map, type, monthchecked, aggregatechecked);
            set.addAll(ultList.stream().map(source -> new ClientRanking(source.getCusno(), source.getCusna(), source.getN_code_DA())).collect(Collectors.toList()));
        } else {
            set.addAll(pastList);
            ultList = null;
        }
        try {
            ClientRanking cr;
            //获取厂商并集
            for (ClientRanking rank : set) {
                rank.setNowshpqy1("0");
                rank.setNowshpamts("0");
                rank.setPastshpqy1("0");
                rank.setPastshpamts("0");
                rank.setUltshpqy1("0");
                rank.setUltshpamts("0");
                rank.setNowrank("9999");//方便后面排序，这里先设置到最大
                rank.setPastrank("9999");
                //当月数据
                List<ClientRanking> nowFilter = nowList.stream().filter(element -> element.getCusno().equals(rank.getCusno())
                        && element.getCusna().equals(rank.getCusna())&& element.getN_code_DA().equals(rank.getN_code_DA())).collect(Collectors.toList());
                if (nowFilter.size() > 0) {
                    rank.setNowrank(nowFilter.get(0).getNowrank());
                    rank.setNowshpqy1(nowFilter.get(0).getNowshpqy1());
                    rank.setNowshpamts(nowFilter.get(0).getNowshpamts());
                }

                //去年同期
                List<ClientRanking> pastFilter = pastList.stream().filter(element -> element.getCusno().equals(rank.getCusno())
                        && element.getCusna().equals(rank.getCusna()) && element.getN_code_DA().equals(rank.getN_code_DA()) ).collect(Collectors.toList());
                if (pastFilter.size() > 0) {
                    rank.setPastrank(pastFilter.get(0).getPastrank());
                    rank.setPastshpqy1(pastFilter.get(0).getPastshpqy1());
                    rank.setPastshpamts(pastFilter.get(0).getPastshpamts());
                }

                if (ultList != null) {
                    List<ClientRanking> ultFilter = ultList.stream().filter(element -> element.getCusno().equals(rank.getCusno())
                            && element.getCusna().equals(rank.getCusna())  && element.getN_code_DA().equals(rank.getN_code_DA())).collect(Collectors.toList());
                    if (ultFilter.size() > 0) {
                        rank.setUltshpqy1(ultFilter.get(0).getPastshpqy1());
                        rank.setUltshpamts(ultFilter.get(0).getPastshpamts());
                    }
                }
            }
            List<ClientRanking> list = new ArrayList<>(set);

            //list排序
            Collections.sort(list);


            //获取求和
            ClientRanking sumClientRanking = new ClientRanking();
            sumClientRanking.setCusna("总计");
            List<ClientRanking> result = new ArrayList<>();
            Double topNowshpqy1 = 0.0, topPastshpqy1 = 0.0, topUltshpqy1 = 0.0;
            Double topNowshpamts = 0.0, topPastshpamts = 0.0, topUltshpamts = 0.0;
            if (list.size() <= Double.parseDouble(rowsPerPage)) {//客户少于页面的行数
                result = new ArrayList<ClientRanking>(list);
                for (ClientRanking r : result) {
                    //计算合计
                    topNowshpqy1 += Double.parseDouble(r.getNowshpqy1());
                    topPastshpqy1 += Double.parseDouble(r.getPastshpqy1());
                    topUltshpqy1 += Double.parseDouble(r.getUltshpqy1());
                    topNowshpamts += Double.parseDouble(r.getNowshpamts());
                    topPastshpamts += Double.parseDouble(r.getPastshpamts());
                    topUltshpamts += Double.parseDouble(r.getUltshpamts());

                    if ("9999".equals(r.getNowrank())) {
                        r.setNowrank("");
                    }
                    if ("9999".equals(r.getPastrank())) {
                        r.setPastrank("");
                    }
                    //调整环比,同比,
                    r.setDifferencevalue(CRdifferencevalue(r.getNowshpamts(), r.getPastshpamts()));
                    r.setGrowthrate(CRrate(r.getNowshpamts(), r.getPastshpamts()));
                    r.setShpqy1growthrate(CRrate(r.getNowshpqy1(), r.getPastshpqy1()));
                    if (Double.parseDouble(r.getGrowthrate()) < 0) {
                        r.setPaststyle("red");
                    }
                    r.setShpqy1chainrate(CRrate(r.getNowshpqy1(), r.getUltshpqy1()));
                    r.setShpamtschainrate(CRrate(r.getNowshpamts(), r.getUltshpamts()));
                    if (Double.parseDouble(r.getShpamtschainrate()) < 0) {
                        r.setUltstyle("red");
                    }
                }
                

                    
                //合计
                sumClientRanking.setNowshpqy1(topNowshpqy1.toString());
                sumClientRanking.setPastshpqy1(topPastshpqy1.toString());
                sumClientRanking.setUltshpqy1(topUltshpqy1.toString());
                sumClientRanking.setNowshpamts(topNowshpamts.toString());
                sumClientRanking.setPastshpamts(topPastshpamts.toString());
                sumClientRanking.setUltshpamts(topUltshpamts.toString());

                sumClientRanking.setDifferencevalue(CRdifferencevalue(sumClientRanking.getNowshpamts(), sumClientRanking.getPastshpamts()));
                sumClientRanking.setGrowthrate(CRrate(sumClientRanking.getNowshpamts(), sumClientRanking.getPastshpamts()));
                sumClientRanking.setShpqy1growthrate(CRrate(sumClientRanking.getNowshpqy1(), sumClientRanking.getPastshpqy1()));
                if (Double.parseDouble(sumClientRanking.getGrowthrate()) < 0) {
                    sumClientRanking.setPaststyle("red");
                }
                sumClientRanking.setShpqy1chainrate(CRrate(sumClientRanking.getNowshpqy1(), sumClientRanking.getUltshpqy1()));
                sumClientRanking.setShpamtschainrate(CRrate(sumClientRanking.getNowshpamts(), sumClientRanking.getUltshpamts()));
                if (Double.parseDouble(sumClientRanking.getShpamtschainrate()) < 0) {
                    sumClientRanking.setUltstyle("red");
                }
                result.add(sumClientRanking);
                return result;
            } else {
                Double otherNowshpqy1 = 0.0, otherPastshpqy1 = 0.0, otherUltshpqy1 = 0.0;
                Double otherNowshpamts = 0.0, otherPastshpamts = 0.0, otherUltshpamts = 0.0;
                int i = 0;
                for (ClientRanking r : list) {
                    if ("0".equals(rowsPerPage) || i < Double.parseDouble(rowsPerPage)) {
                        //调整环比,同比,
                        r.setDifferencevalue(CRdifferencevalue(r.getNowshpamts(), r.getPastshpamts()));
                        r.setGrowthrate(CRrate(r.getNowshpamts(), r.getPastshpamts()));
                        r.setShpqy1growthrate(CRrate(r.getNowshpqy1(), r.getPastshpqy1()));
                        if (Double.parseDouble(r.getGrowthrate()) < 0) {
                            r.setPaststyle("red");
                        }
                        r.setShpqy1chainrate(CRrate(r.getNowshpqy1(), r.getUltshpqy1()));
                        r.setShpamtschainrate(CRrate(r.getNowshpamts(), r.getUltshpamts()));
                        if (Double.parseDouble(r.getShpamtschainrate()) < 0) {
                            r.setUltstyle("red");
                        }
                        if ("9999".equals(r.getNowrank())) {
                            r.setNowrank("");
                        }
                        if ("9999".equals(r.getPastrank())) {
                            r.setPastrank("");
                        }
                        result.add(r);
                    } else {
                        otherNowshpqy1 += Double.parseDouble(r.getNowshpqy1());
                        otherPastshpqy1 += Double.parseDouble(r.getPastshpqy1());
                        otherUltshpqy1 += Double.parseDouble(r.getUltshpqy1());
                        otherNowshpamts += Double.parseDouble(r.getNowshpamts());
                        otherPastshpamts += Double.parseDouble(r.getPastshpamts());
                        otherUltshpamts += Double.parseDouble(r.getUltshpamts());
                    }
                    //计算合计
                    topNowshpqy1 += Double.parseDouble(r.getNowshpqy1());
                    topPastshpqy1 += Double.parseDouble(r.getPastshpqy1());
                    topUltshpqy1 += Double.parseDouble(r.getUltshpqy1());
                    topNowshpamts += Double.parseDouble(r.getNowshpamts());
                    topPastshpamts += Double.parseDouble(r.getPastshpamts());
                    topUltshpamts += Double.parseDouble(r.getUltshpamts());
                    i++;
                }
                //选择所有时前端不会有"其他行"。
                //当刷新的页面的数据小于或者等于页面选择的数据。也没有其他
                if (!"0".equals(rowsPerPage) && list.size() > Integer.valueOf(rowsPerPage)) {
                    //其他
                    ClientRanking otherClientRanking = new ClientRanking();
                    otherClientRanking.setCusna("其他");
                    otherClientRanking.setNowshpqy1(otherNowshpqy1.toString());
                    otherClientRanking.setPastshpqy1(otherPastshpqy1.toString());
                    otherClientRanking.setUltshpqy1(otherUltshpqy1.toString());
                    otherClientRanking.setNowshpamts(otherNowshpamts.toString());
                    otherClientRanking.setPastshpamts(otherPastshpamts.toString());
                    otherClientRanking.setUltshpamts(otherUltshpamts.toString());

                    otherClientRanking.setDifferencevalue(CRdifferencevalue(otherClientRanking.getNowshpamts(), otherClientRanking.getPastshpamts()));
                    otherClientRanking.setGrowthrate(CRrate(otherClientRanking.getNowshpamts(), otherClientRanking.getPastshpamts()));
                    otherClientRanking.setShpqy1growthrate(CRrate(otherClientRanking.getNowshpqy1(), otherClientRanking.getPastshpqy1()));
                    if (Double.parseDouble(otherClientRanking.getGrowthrate()) < 0) {
                        otherClientRanking.setPaststyle("red");
                    }
                    otherClientRanking.setShpqy1chainrate(CRrate(otherClientRanking.getNowshpqy1(), otherClientRanking.getUltshpqy1()));
                    otherClientRanking.setShpamtschainrate(CRrate(otherClientRanking.getNowshpamts(), otherClientRanking.getUltshpamts()));
                    if (Double.parseDouble(otherClientRanking.getShpamtschainrate()) < 0) {
                        otherClientRanking.setUltstyle("red");
                    }
                    result.add(otherClientRanking);
                }

                //合计
                sumClientRanking.setNowshpqy1(topNowshpqy1.toString());
                sumClientRanking.setPastshpqy1(topPastshpqy1.toString());
                sumClientRanking.setUltshpqy1(topUltshpqy1.toString());
                sumClientRanking.setNowshpamts(topNowshpamts.toString());
                sumClientRanking.setPastshpamts(topPastshpamts.toString());
                sumClientRanking.setUltshpamts(topUltshpamts.toString());

                sumClientRanking.setDifferencevalue(CRdifferencevalue(sumClientRanking.getNowshpamts(), sumClientRanking.getPastshpamts()));
                sumClientRanking.setGrowthrate(CRrate(sumClientRanking.getNowshpamts(), sumClientRanking.getPastshpamts()));
                sumClientRanking.setShpqy1growthrate(CRrate(sumClientRanking.getNowshpqy1(), sumClientRanking.getPastshpqy1()));
                if (Double.parseDouble(sumClientRanking.getGrowthrate()) < 0) {
                    sumClientRanking.setPaststyle("red");
                }
                sumClientRanking.setShpqy1chainrate(CRrate(sumClientRanking.getNowshpqy1(), sumClientRanking.getUltshpqy1()));
                sumClientRanking.setShpamtschainrate(CRrate(sumClientRanking.getNowshpamts(), sumClientRanking.getUltshpamts()));
                if (Double.parseDouble(sumClientRanking.getShpamtschainrate()) < 0) {
                    sumClientRanking.setUltstyle("red");
                }
                result.add(sumClientRanking);
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("cn.hanbell.kpi.ejb.SalesTableBean.getClientList()" + e.toString());
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
    public List<ClientRanking> getClientListByCodeDA(int y, int m, LinkedHashMap<String, String> map, Boolean monthchecked, Boolean aggregatechecked, String rowsPerPage) {
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
