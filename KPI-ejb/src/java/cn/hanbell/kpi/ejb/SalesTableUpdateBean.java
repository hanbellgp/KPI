/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.SalesTable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
public class SalesTableUpdateBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, String> queryParams;

    public SalesTableUpdateBean() {
        queryParams = new LinkedHashMap<>();
    }

    public List<SalesTable> getShipmentListSum(int y, int m, String da, String type) {
        queryParams.clear();
        List<SalesTable> SalesList = new ArrayList<>();
        List<SalesTable> list;
        if ("".equals(da.trim()) || "R".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C,C4,N,G,J");
            queryParams.put("depno", " IN ('1B000','1B100','1C000','1D000','1E000','1V000') ");
            queryParams.put("n_code_DA", " ='R' ");
            queryParams.put("ogdkid", " IN ('RL01') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AA".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN ('1Q000','1Q100') ");
            queryParams.put("n_code_DA", " ='AA' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN('1G100','1G110','1G500') ");
            queryParams.put("n_code_DA", " ='AH' ");
            queryParams.put("ogdkid", " IN('RL01','RL03') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "P".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN ('1H000','1H100') ");
            queryParams.put("n_code_DA", "= 'P'");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "S".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("depno", " IN ('1U000') ");
            queryParams.put("n_code_DA", " ='S'");
            queryParams.put("ogdkid", " IN ('RL01','RL03') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "RT".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K");
            queryParams.put("depno", " IN('5C000','5A000') ");
            queryParams.put("n_code_DA", " IN('RT') ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN('RL01','RL03') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "OH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K,E");
            queryParams.put("depno", " IN('5B000','8A000') ");
            queryParams.put("n_code_DA", " IN('OH') ");
            queryParams.put("n_code_DD", " IN ('02') ");
            queryParams.put("ogdkid", " IN('RL01','RL03') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        return SalesList;
    }

    public List<SalesTable> getSalesOrderListSum(int y, int m, String da, String type) {
        queryParams.clear();
        List<SalesTable> SalesList = new ArrayList<>();
        List<SalesTable> list;
        if ("".equals(da.trim()) || "R".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C,C4,N,G,J");
            queryParams.put("n_code_DA", " ='R' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AA".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", " ='AA' ");
            queryParams.put("n_code_DD", " IN ('02') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", " ='AH' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "P".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", "= 'P'");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "S".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", " ='S'");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "RT".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K");
            queryParams.put("n_code_DA", " IN('RT') ");
            queryParams.put("n_code_DD", " IN ('02') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "OH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K,E");
            queryParams.put("n_code_DA", " IN('OH') ");
            queryParams.put("n_code_DD", " IN ('02') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "外销零件".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", "1T100");
            queryParams.put("n_code_DD", " IN ('01') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        return SalesList;
    }

    public List<SalesTable> getServiceAmountListSum(int y, int m, String da, String type) {
        queryParams.clear();
        List<SalesTable> SalesList = new ArrayList<>();
        List<SalesTable> list;
        if ("".equals(da.trim()) || "R".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C,C4,N,G,J");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", " ='R' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AA".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C,C4,N,G,J");
            queryParams.put("decode", "1");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", "='AA' ");
            queryParams.put("n_code_DC", " LIKE 'AA%' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "AH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", "='AH' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "P".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", " ='P' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "S".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", " ='S' ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "RT".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K,C,C4,N,G,J");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", " IN('RT') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "OH".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "K,E");
            queryParams.put("ogdkid", " ='RL01' ");
            queryParams.put("n_code_DA", " IN('OH') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        if ("".equals(da.trim()) || "外销零件".equals(da.trim())) {
            queryParams.clear();
            queryParams.put("facno", "C");
            queryParams.put("n_code_DA", "1T100");
            queryParams.put("n_code_DD", " IN ('01') ");
            list = getSalesTableList(y, m, queryParams, type);
            if (list != null && !list.isEmpty()) {
                for (SalesTable salesTable : list) {
                    SalesList.add(salesTable);
                }
            }
        }
        return SalesList;
    }

    private List<SalesTable> getSalesTableList(int y, int m, LinkedHashMap<String, String> map, String type) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String[] arr = facno.split(",");
        List<SalesTable> returnlist = new ArrayList<>();
        List list = new ArrayList();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SalesTable st;
        try {
            for (String arr1 : arr) {
                //出货数据部分
                if ("Shipment".equals(type.trim())) {
                    list = getShipment(y, m, arr1, map);
                }
                //订单数据部分
                if ("SalesOrder".equals(type.trim())) {
                    list = getSalesOrder(y, m, arr1, map);
                }
                //收费服务部分
                if ("ServiceAmount".equals(type.trim())) {
                    list = getServiceAmount(y, m, arr1, map);
                }
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        st = new SalesTable();
                        Object[] row = (Object[]) list.get(i);
                        st.setFacno(row[0].toString());
                        st.setType(row[1].toString());
                        st.setItnbrcus(row[2] == null ? "null" : row[2].toString());
                        st.setCusno(row[3].toString());
                        st.setCusna(row[4].toString());
                        st.setParentcusno(row[5] == null ? "null" : row[5].toString());
                        st.setParentcusna(row[6] == null ? "null" : row[6].toString());
                        st.setCdrdate(df.parse(row[7].toString()));
                        st.setDeptno(row[8].toString());
                        st.setQuantity(BigDecimal.valueOf(Double.parseDouble(row[9].toString())));
                        st.setAmount(BigDecimal.valueOf(Double.parseDouble(row[10].toString())));
                        st.setNcodeDA(row[11] == null ? n_code_DA : row[11].toString());
                        st.setNcodeCD(row[12] == null ? "null" : row[12].toString());
                        st.setNcodeDC(row[13] == null ? "null" : row[13].toString());
                        st.setNcodeDD(row[14] == null ? "null" : row[14].toString());
                        st.setMancode(row[15].toString());
                        st.setManname(row[16].toString());
                        st.setHmark1(row[17] == null ? "null" : row[17].toString());
                        st.setHmark2(row[18] == null ? "null" : row[18].toString());
                        returnlist.add(st);
                    }
                }
            }
            return returnlist;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.ClientShipmentBean.getSalesTableListSum()-----" + e.toString());
            return null;
        }

    }

    private List getServiceAmount(int y, int m, String arrString, LinkedHashMap<String, String> map) {
        String decode = map.get("decode") != null ? map.get("decode") : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DC = map.get("n_code_DC") != null ? map.get("n_code_DC") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT facno,'ServiceAmount' AS type,itnbrcus,a.cusno,s.cusna,o.code,o.cdesc,cdrdate,depno,quantity,amount ");
        sb.append(" ,n_code_DA,n_code_CD,n_code_DC,n_code_DD,mancode,e.username AS manname,hmark1,hmark2 FROM (");
        if (!"1T100".equals(n_code_DA)) {
            sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
            sb.append("  and d.issevdta='Y' and h.facno='${facno}' ");
            if (!"".equals(decode)) {
                sb.append(" and h.decode ='").append(decode).append("' ");
            }
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2 ");
            sb.append(" from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
            sb.append(" and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='Y' and h.facno='${facno}' ");
            if (!"".equals(decode)) {
                sb.append(" and h.decode ='").append(decode).append("' ");
            }
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            //getARM232Value 加扣款单独列出来 只算金额不算台数
            sb.append(" union all ");
            sb.append(" SELECT h.facno,itnbrcus,h.cusno,h.trdat AS cdrdate ,d.depno,0 AS quantity, ");
            sb.append(" ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS amount ");
            sb.append(" ,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode,'ARM232' as hmark1,'ARM232' as hmark2 ");
            sb.append(" FROM armpmm h,armacq d,cdrdta s,cdrhad c WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno ");
            sb.append(" AND d.shpno=s.shpno AND d.shpseq = s.trseq AND s.shpno=c.shpno and s.issevdta='Y' and h.facno='${facno}' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and d.n_code_DC ").append(n_code_DC);
            }
            if (!"".equals(n_code_DD)) {
                sb.append(" and d.n_code_DD ").append(n_code_DD);
            }
            sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m} ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.trdat,d.depno,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode ");
            //getARM423Value 折让
            sb.append(" union all ");
            sb.append(" SELECT h.facno,'ARM423' as itnbrcus,d.ivocus AS 'cusno',h.recdate AS cdrdate,h.depno,0 AS quantity, ");
            sb.append(" ISNULL(sum(d.recamt),0) AS amount ,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
            sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno IN ('6001','6002') ");
            sb.append(" AND h.ogdkid ").append(ogdkid);
            if (!"".equals(n_code_DA)) {
                sb.append(" and h.n_code_DA ").append(n_code_DA);
            }
            if (!"".equals(n_code_DC)) {
                sb.append(" and h.n_code_DC ").append(n_code_DC);
            }
            sb.append("  AND h.n_code_DD  ='01' ");
            sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) =${m} ");
            sb.append(" GROUP BY h.facno,d.ivocus,h.recdate,h.depno,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");
        } else {
            if (y >= 2019) {
                sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
                sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,'WXLJ' AS hmark1,hmark2 ");
                sb.append(" from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno and h.houtsta<>'W' ");
                sb.append(" and h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and h.depno not like '1A%' ");
                sb.append(" and d.n_code_CD LIKE 'WX%' and d.n_code_DD  ='01' ");
                sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
                sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
                sb.append(" union all ");
                sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity,isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount, ");
                sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,'WXLJ' AS hmark1,hmark2 from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' ");
                sb.append(" and h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and h.depno not like '1A%' and d.n_code_CD LIKE 'WX%' and d.n_code_DD  ='01' ");
                sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
                sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            } else {
                sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
                sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,'WXLJ' AS hmark1,hmark2 ");
                sb.append(" from cdrhad h,cdrdta d where h.facno=d.facno and h.shpno=d.shpno AND h.houtsta<>'W' and h.depno='1T100' ");
                sb.append(" and h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','STW00003','KZJ00029') and d.n_code_DA IN ('AA','AH') ");
                sb.append(" and d.n_code_CD LIKE 'WX%' and d.n_code_DD  ='01' ");
                sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
                sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
                sb.append(" union all ");
                sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity,isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount, ");
                sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,'WXLJ' AS hmark1,hmark2 from cdrbhad h,cdrbdta d where h.facno=d.facno and h.bakno=d.bakno and h.baksta<>'W' and h.depno='1T100' ");
                sb.append(" and h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','STW00003','KZJ00029') and d.n_code_DA IN ('AA','AH')  ");
                sb.append(" and d.n_code_CD LIKE 'WX%' and d.n_code_DD  ='01' and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
                sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            }
        }
        sb.append(" ) a,cdrcus s LEFT OUTER JOIN miscode o on s.cusgroup = o.code and o.ckind= 'CB',secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");
        String sql = sb.toString().replace("${facno}", arrString).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        try {
            erpEJB.setCompany(arrString);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableUpdateBean.getSalesOrder SQL()----异常" + e.toString());
            return null;
        }

    }

    private List getSalesOrder(int y, int m, String arrString, LinkedHashMap<String, String> map) {
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT facno,'SalesOrder' AS type,itnbrcus,a.cusno,s.cusna,o.code,o.cdesc,cdrdate,depno,quantity,amount ");
        sb.append(" ,n_code_DA,n_code_CD,n_code_DC,n_code_DD,mancode,e.username AS manname,hmark1,hmark2 FROM (");
        if (!"1T100".equals(n_code_DA)) {
            sb.append(" select d.facno,itnbrcus,h.cusno,h.recdate AS cdrdate,depno,isnull(sum(d.cdrqy1),0) AS quantity, ");
            sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.tramts*h.ratio) else (d.tramts*h.ratio)/(h.taxrate+1) end)),0) as amount, ");
            sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
            sb.append(" and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
            sb.append(" and d.drecsta not in ('98','99','10') and d.n_code_DD ='00' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" AND  year(h.recdate)=${y} AND month(h.recdate)=${m} ");
            sb.append(" GROUP BY d.facno,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2 ");
            if (!"".equals(n_code_DD)) {
                sb.append(" union all ");
                sb.append(" select d.facno,itnbrcus,h.cusno,h.recdate AS cdrdate,depno,0 AS quantity, ");
                sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.tramts*h.ratio) else (d.tramts*h.ratio)/(h.taxrate+1) end)),0) as amount, ");
                sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
                sb.append(" from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno where h.hrecsta <> 'W' ");
                sb.append(" and h.cusno not in ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and isnull(h.hmark2,'') <> 'FW' and  h.facno='${facno}' ");
                sb.append(" and d.drecsta not in ('98','99','10') and d.n_code_DD ='02' ");
                if (!"".equals(n_code_DA)) {
                    sb.append(" and d.n_code_DA ").append(n_code_DA);
                }
                sb.append(" AND  year(h.recdate)=${y} AND month(h.recdate)=${m} ");
                sb.append(" GROUP BY d.facno,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2 ");
            }
        } else {
            if (y >= 2019) {
                sb.append(" select d.facno,itnbrcus,h.cusno,h.recdate AS cdrdate,depno,0 AS quantity,isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.tramts*h.ratio) else (d.tramts*h.ratio)/(h.taxrate+1) end)),0) as amount, ");
                sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,'WXLJ' AS hmark1,hmark2 from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno ");
                sb.append(" WHERE  h.hrecsta <> 'W' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') ");
                sb.append(" AND  h.facno='${facno}' and d.drecsta not in ('98','99','10') and h.depno not like '1A%' and d.n_code_CD LIKE 'WX%' and d.n_code_DD  ='01' ");
                sb.append(" AND  year(h.recdate)=${y} AND month(h.recdate)=${m} ");
                sb.append(" GROUP BY d.facno,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2 ");
            } else {
                sb.append(" select d.facno,itnbrcus,h.cusno,h.recdate AS cdrdate,depno,0 AS quantity,isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.tramts*h.ratio) else (d.tramts*h.ratio)/(h.taxrate+1) end)),0) as amount, ");
                sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,'WXLJ' AS hmark1,hmark2 from cdrdmas d inner join cdrhmas h on h.facno=d.facno and h.cdrno=d.cdrno ");
                sb.append(" WHERE  isnull(h.hmark2,'') <> 'FW' AND h.hrecsta <> 'W' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','STW00003','KZJ00029') and h.depno='1T100' ");
                sb.append(" AND  h.facno='${facno}' and d.drecsta not in ('98','99','10') and d.n_code_DA IN ('AA','AH') and d.n_code_CD LIKE 'WX%' and d.n_code_DD  ='01' ");
                sb.append(" AND  year(h.recdate)=${y} AND month(h.recdate)=${m} ");
                sb.append(" GROUP BY d.facno,itnbrcus,h.cusno,h.recdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ,mancode,hmark1,hmark2 ");
            }
        }

        sb.append(" ) a,cdrcus s LEFT OUTER JOIN miscode o on s.cusgroup = o.code and o.ckind= 'CB' ,secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");

        String sql = sb.toString().replace("${facno}", arrString).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        try {
            erpEJB.setCompany(arrString);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableUpdateBean.getSalesOrder SQL()----异常" + e.toString());
            return null;
        }
    }

    private List getShipment(int y, int m, String arrString, LinkedHashMap<String, String> map) {
        String depno = map.get("depno") != null ? map.get("depno") : "";
        String ogdkid = map.get("ogdkid") != null ? map.get("ogdkid") : "";
        String n_code_DA = map.get("n_code_DA") != null ? map.get("n_code_DA") : "";
        String n_code_DD = map.get("n_code_DD") != null ? map.get("n_code_DD") : "";
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT facno,'Shipment' AS type,itnbrcus,a.cusno,s.cusna,o.code,o.cdesc,cdrdate,depno,quantity,amount ");
        sb.append(",n_code_DA,n_code_CD,n_code_DC,n_code_DD,mancode,e.username AS manname,hmark1,hmark2 FROM ( ");
        //第一部分为整机出货销退
        sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno, ");
        sb.append(" sum(CASE  when d.n_code_DA='AA' THEN shpqy1 when d.n_code_DA!='AA' THEN shpqy1 when d.n_code_DA='AA' AND d.n_code_DC='SDS' THEN shpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
        sb.append(" ,mancode,hmark1,hmark2 from cdrdta d left join cdrhad h on d.shpno=h.shpno");
        sb.append(" where h.facno='${facno}' and h.houtsta <> 'W' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" union all ");
        sb.append("  select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno, ");
        sb.append(" -sum(CASE  when d.n_code_DA='AA' AND left(d.itnbr,1)='3' THEN bshpqy1 when d.n_code_DA!='AA' THEN bshpqy1 when d.n_code_DA='AA' AND d.n_code_DC='SDS' THEN shpqy1 ELSE 0 END ) as quantity, ");
        sb.append(" isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
        sb.append(" ,mancode,hmark1,hmark2 from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
        sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' and d.n_code_DD ='00' ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and d.n_code_DA ").append(n_code_DA);
        }
        sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");

        //如果算入后处理金额则单独处理，只算金额不算台数
        if (!"".equals(n_code_DD) && !n_code_DA.contains("AH")) {
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.shpdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),sum(case h.tax when '1' then (d.shpamts * h.ratio) else (d.shpamts * h.ratio)/(h.taxrate + 1) end)),0) as amount, ");
            sb.append(" d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" from cdrdta d left join cdrhad h on d.shpno=h.shpno ");
            sb.append(" where h.facno='${facno}' and h.houtsta <> 'W' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ").append(n_code_DD);
            sb.append(" AND  year(h.shpdate)=${y} AND month(h.shpdate)=${m} ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.shpdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
            sb.append(" union all ");
            sb.append(" select h.facno,itnbrcus,h.cusno,h.bakdate AS cdrdate,depno,0 as quantity, ");
            sb.append(" isnull(convert(decimal(16,4),-sum(case h.tax when '1' then (d.bakamts * h.ratio) else (d.bakamts * h.ratio)/(h.taxrate + 1) end)),0) as amount,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD ");
            sb.append(" ,mancode,hmark1,hmark2 from cdrbdta d left join cdrbhad h on  h.bakno=d.bakno ");
            sb.append(" where h.baksta <> 'W'  and  h.facno='${facno}' and h.cusno NOT IN ('SSD00107','SGD00088','SJS00254','SCQ00146','KZJ00029') and d.issevdta='N' ");
            if (!"".equals(n_code_DA)) {
                sb.append(" and d.n_code_DA ").append(n_code_DA);
            }
            sb.append(" and d.n_code_DD ").append(n_code_DD);
            sb.append(" AND  year(h.bakdate)=${y} AND month(h.bakdate)=${m} ");
            sb.append(" group by  h.facno,itnbrcus,h.cusno,h.bakdate,depno,d.n_code_DA,d.n_code_CD,d.n_code_DC,d.n_code_DD,mancode,hmark1,hmark2 ");
        }
        //getARM232Value 加扣款单独列出来 只算金额不算台数
        sb.append(" union all ");
        sb.append(" SELECT h.facno,itnbrcus,h.cusno,h.trdat AS cdrdate ,d.depno,0 AS quantity, ");
        sb.append(" ISNULL(SUM(CASE h.amtco WHEN 'P' THEN d.psamt WHEN 'M' THEN d.psamt *(-1) ELSE 0 END),0) AS amount ");
        sb.append(" ,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode,'ARM232' as hmark1,'ARM232' as hmark2 ");
        sb.append(" FROM armpmm h,armacq d,cdrdta s,cdrhad c WHERE h.facno=d.facno AND h.trno = d.trno AND d.facno = s.facno ");
        sb.append(" AND d.shpno=s.shpno AND d.shpseq = s.trseq AND s.shpno=c.shpno ");
        if (!"".equals(n_code_DA)) {
            sb.append(" and s.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND (s.n_code_DD  ='00'  or s.n_code_DD ").append(n_code_DD).append(" ) ");
        } else {
            sb.append("  AND s.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.trdat) = ${y}  and month(h.trdat) = ${m} ");
        sb.append(" group by  h.facno,itnbrcus,h.cusno,h.trdat,d.depno,s.n_code_DA,s.n_code_CD,s.n_code_DC,s.n_code_DD,mancode ");

        //getARM423Value 折让
        sb.append(" union all ");
        sb.append(" SELECT h.facno,'ARM423' as itnbrcus,d.ivocus AS 'cusno',h.recdate AS cdrdate,h.depno,0 AS quantity, ");
        sb.append(" ISNULL(sum(d.recamt),0) AS amount ,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");
        sb.append(" FROM armrec d,armrech h where d.facno=h.facno AND d.recno=h.recno ");
        sb.append(" AND h.prgno='ARM423' AND h.recstat='1' AND d.raccno IN ('6001','6002') ");
        sb.append(" AND h.ogdkid  ").append(ogdkid);
        if (!"".equals(n_code_DA)) {
            sb.append(" and h.n_code_DA ").append(n_code_DA);
        }
        if (!"".equals(n_code_DD)) {
            sb.append(" AND (h.n_code_DD  ='00'  or h.n_code_DD ").append(n_code_DD).append(" ) ");
        } else {
            sb.append("  AND h.n_code_DD  ='00' ");
        }
        sb.append(" and year(h.recdate) = ${y}   and month(h.recdate) =${m} ");
        sb.append(" GROUP BY h.facno,d.ivocus,h.recdate,h.depno,h.n_code_DA,h.n_code_CD,h.n_code_DC,h.n_code_DD,mancode,hmark1,hmark2 ");

        //getARM270Value 其它项金额 关联部门
        if (!"".equals(depno)) {
            sb.append(" union all ");
            sb.append(" SELECT h.facno,'' as itnbrcus,h.cusno,h.bildat AS cdrdate ,h.depno,0 AS quantity,ISNULL(sum(h.shpamt),0) AS amount, ");
            sb.append(getDA(depno)).append(" as n_code_DA, ");
            sb.append(" '' as  n_code_CD,");
            sb.append(" '' as  n_code_DC,");
            sb.append(" '00' as  n_code_DD,mancode,'ARM270' as hmark1,'ARM270' as hmark2 FROM armbil h WHERE 1=1 ");
            sb.append(" and h.rkd='RQ11' ");
            sb.append(" AND h.depno ").append(depno);
            sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) =${m} ");
            sb.append(" group by  h.facno,h.cusno,h.depno,h.bildat,mancode ");
            if (depno.contains("5B000")) {
                // 关于柯茂北京中矿之芦南芦北项目分期收款，期限为三年，每季度为1期，共12期（2018年4月至2021年3月），调整逻辑抓取金额/(1+7%*3)
                //2019年6月6日 财务周建芳调整此部分逻辑
                sb.append(" union all ");
                sb.append(" SELECT h.facno,'' as itnbrcus,h.cusno,h.bildat AS cdrdate ,h.depno,0 AS quantity,ISNULL(sum(h.shpamt/1.21),0) AS amount, ");
                sb.append(getDA(depno)).append(" as n_code_DA, ");
                sb.append(" '' as  n_code_CD,");
                sb.append(" '' as  n_code_DC,");
                sb.append(" '00' as  n_code_DD,mancode,'ARM270' as hmark1,'ARM270' as hmark2 FROM armbil h WHERE 1=1 ");
                sb.append(" and h.rkd in ('RQ51') ");
                sb.append(" AND h.depno ").append(depno);
                sb.append(" and year(h.bildat) = ${y}   and month(h.bildat) =${m} ");
                sb.append(" group by  h.facno,h.cusno,h.depno,h.bildat,mancode ");
            }
        }
        sb.append(" ) a,cdrcus s LEFT OUTER JOIN miscode o on s.cusgroup = o.code and o.ckind= 'CB',secuser e WHERE a.cusno=s.cusno AND a.mancode=e.userno ");

        String sql = sb.toString().replace("${facno}", arrString).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m));

        try {
            erpEJB.setCompany(arrString);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception e) {
            System.out.println("cn.hanbell.kpi.ejb.SalesTableUpdateBean.getShipment SQL()----异常" + e.toString());
            return null;
        }
    }

    private String getDA(String depno) {
        String aa = "'ARM270'";
        if (depno.contains("1B")) {
            aa = "'R'";
        }
        if (depno.contains("1C")) {
            aa = "'R'";
        }
        if (depno.contains("1D")) {
            aa = "'R'";
        }
        if (depno.contains("1E")) {
            aa = "'R'";
        }
        if (depno.contains("1V")) {
            aa = "'R'";
        }
        if (depno.contains("1Q")) {
            aa = "'AA'";
        }
        if (depno.contains("1G")) {
            aa = "'AH'";
        }
        if (depno.contains("1H")) {
            aa = "'P'";
        }
        if (depno.contains("1U")) {
            aa = "'S'";
        }
        if (depno.contains("5A")) {
            aa = "'RT'";
        }
        if (depno.contains("5B") || depno.contains("8A")) {
            aa = "'OH'";
        }
        if (depno.contains("5C")) {
            aa = "'RT'";
        }
        return aa;
    }

}
