/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.InventoryProduct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InventoryProductBean extends SuperEJBForKPI<InventoryProduct> {

    @EJB
    SuperEJBForERP superEJBForERP;

    protected LinkedHashMap<String, String> queryStringParams;

    protected Logger log4j = LogManager.getLogger();

    String[] strArr;

    public InventoryProductBean() {
        super(InventoryProduct.class);
        queryStringParams = new LinkedHashMap<>();
    }

    public boolean queryInventoryProductIsExist(InventoryProduct ip) {
        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByUnique");
        try {
            query.setParameter("yearmon", ip.getYearmon());
            query.setParameter("whdsc", ip.getWhdsc());
            query.setParameter("genre", ip.getGenre());
            query.setParameter("trtype", ip.getTrtype());
            query.setParameter("itclscode", ip.getItclscode());
            query.setParameter("categories", ip.getCategories());
            if (query.getResultList().isEmpty()) {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("cn.hanbell.kpi.ejb.InventoryProductBean.queryInventoryProductIsExist()" + ex.toString());
        }
        return true;
    }

    public List getEditList(InventoryProduct ip) {
        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByEditRow");
        try {
            query.setParameter("facno", ip.getFacno());
            query.setParameter("yearmon", ip.getYearmon());
            query.setParameter("wareh", ip.getWareh());
            query.setParameter("itclscode", ip.getItclscode());
            query.setParameter("genre", ip.getGenre());
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            System.out.println("cn.hanbell.kpi.ejb.InventoryProductBean.getEditList()" + ex.toString());
        }
        return null;
    }

    public List getDataByFindByYearmon(String yearmon) {
        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByYearmon");
        try {
            query.setParameter("yearmon", yearmon);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            System.out.println("cn.hanbell.kpi.ejb.InventoryProductBean.getDataByFindByYearmon()" + ex.toString());
        }
        return null;
    }

    // 获取ERP的invamount的数据 存到KPI的inventoryproduct表中去
    private List getDataForERPList(int y, int m, LinkedHashMap<String, String> map, String facno) {
        StringBuilder sb = new StringBuilder();
        if (facno.equals("C")) {
            sb.append("  SELECT facno,yearmon,wareh,whdsc,genre,trtype,deptno,itclscode,sum(amount),0.0 as amamount FROM invamount WHERE facno = 'C' AND prono = '1' AND yearmon = '${y}${m}' AND genre NOT LIKE '%,%' AND genre NOT LIKE '%QT%'");
            sb.append("  group by facno,yearmon,trtype,deptno,wareh,whdsc,genre,itclscode");
            sb.append("  UNION ALL");
            sb.append(" SELECT facno,yearmon,wareh,whdsc,genre,trtype,deptno,itclscode,sum(amount),0.0 as amamount  FROM invamount WHERE facno <> 'C' AND prono = '1' AND yearmon = '${y}${m}'");
            sb.append(" group by facno,yearmon,trtype,deptno,wareh,whdsc,genre,itclscode");

        } else {
            sb.append(" SELECT a.facno,a.yearmon,a.trtype,a.deptno,a.wareh,a.whdsc,");
            sb.append(" (case when  d.genre <> '' then  d.genre else  a.genre end ), ");
            sb.append(" a.itclscode,d.genreno,h.genzls,sum(a.amount) AS amount,'' AS amamount  ");
            sb.append(" FROM invamount a LEFT OUTER JOIN invwh w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
            sb.append(" LEFT JOIN invindexdta d ON a.facno = d.facno AND a.prono = d.prono AND a.wareh = d.wareh  ");
            sb.append(" LEFT JOIN invindexhad h ON h.facno = d.facno AND h.prono = d.prono AND h.indno = d.indno  ");
            sb.append(" where a.facno='${facno}' and a.prono = '1'  ");
            sb.append(" AND a.genre NOT LIKE 'GY' ");
            sb.append(" and a.yearmon='${y}${m}'  ");
            sb.append(" GROUP BY a.facno,a.trtype,a.deptno,a.yearmon,a.wareh,a.whdsc,(case when  d.genre <> '' then  d.genre else  a.genre end ), ");
            sb.append(" a.itclscode,d.genreno,h.genzls ");
        }
        String sql = sb.toString().replace("${facno}", String.valueOf(facno))
                .replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(getMon(m)));
        try {
            superEJBForERP.setCompany(facno);
            Query query = superEJBForERP.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            log4j.error("InventoryProductBean.getDataForERPList()异常！！！", ex.toString());
        }
        return null;
    }

    // 更新到KPI的inventoryproduct表中的方法 在mailBean里设置自动排程更新数据
    public boolean updateInventoryProduct(int y, int m, String facno) {
        queryStringParams.clear();
        queryStringParams.put("prono", "1");
        // 取到数据源
        List dataList = getDataForERPList(y, m, queryStringParams, facno);
        List<InventoryProduct> ipResultList = new ArrayList<>();
        InventoryProduct ip;
        try {
            // 判读当前list 是否有数据 有就更新 没有就返回空
            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    ip = new InventoryProduct();
                    Object[] row = (Object[]) dataList.get(i);
                    ip.setFacno(row[0].toString());
                    ip.setYearmon(row[1].toString());
                    ip.setTrtype(row[5].toString());
                    ip.setDeptno(row[6] != null ? row[6].toString() : "");
                    ip.setWareh(row[2].toString());
                    ip.setWhdsc(row[3].toString());
                    ip.setIndicatorno("");

                    String genre = row[4] != null ? row[4].toString() : "";
                    String itclscode = row[7].toString();
                    //兴塔的空压机体整机归为空压机组库存(兴塔成品仓库）
                    if (row[2].toString().equals("EW01") && genre.equals("AJ") && itclscode.equals("2") && row[0].toString().equals("C")) {
                        ip.setGenre("A");
                    } else {
                        ip.setGenre(genre);
                    }
                    ip.setItclscode(itclscode);
                    ip.setCategories("");
                    ip.setAmount(BigDecimal.valueOf(Double.valueOf(row[8].toString())));
                    ip.setAmamount(BigDecimal.ZERO);
                    //“兴塔空压零件库”，将“分类1”剔除日立、A、AJ后，将不是“A”的全部替换为“A,AJ”；
                    if (row[2].toString().equals("EAKF02") && !genre.equals("AJ") && !genre.equals("AD") && !genre.equals("A") && row[0].toString().equals("C")) {
                        StringBuffer sql = new StringBuffer("select genre2,ratio from invshare where genre1='A,AJ' and year(validtime)=");
                        superEJBForERP.setCompany(facno);
                        Query query = superEJBForERP.getEntityManager().createNativeQuery(sql.append(y).toString());
                        List<Object[]> result = query.getResultList();
                        if (result == null || result.isEmpty() || result.size() != 2) {
                            throw new Exception("invshare表中A,AJ部分有问题，请检查！");
                        }
                        InventoryProduct entity = (InventoryProduct) ip.clone();
                        BigDecimal amount = (BigDecimal) row[8];
                        for (Object[] o : result) {
                            if ("A".equals(o[0])) {
                                ip.setAmount(amount.multiply((BigDecimal) o[1]));
                                ip.setGenre("A");
                            } else if ("AJ".equals(o[0])) {
//                                entity.setAmount(BigDecimal.valueOf(Double.valueOf(row[8].toString())).multiply(new BigDecimal((String) o[1])));
//                                entity.setAmount((BigDecimal) row[8]);
                                entity.setAmount(amount.multiply((BigDecimal) o[1]));
                                entity.setGenre("AJ");
                            }

                        }
                        ipResultList.add(entity);
                    } else {
                        ip.setGenre(genre);
                    }

                    ipResultList.add(ip);
                }
                if (!ipResultList.isEmpty()) {
                    if (!facno.equals("") && facno.equals("C")) {
                        this.getEntityManager()
                                .createNativeQuery("delete from inventoryproduct where facno <> 'K' and yearmon ='" + y + getMon(m) + "'").executeUpdate();
                    } else {
                        this.getEntityManager()
                                .createNativeQuery("delete from inventoryproduct where facno = 'K' and yearmon ='" + y + getMon(m) + "'").executeUpdate();
                    }
                    for (InventoryProduct e : ipResultList) {
                        this.persist(e);
                    }
                    //先调整借厂商，借客户，借员工
                    if (!facno.equals("") && facno.equals("C")) {
                        this.getEntityManager().createNativeQuery("update inventoryproduct set wareh='JCZC-JKH' where facno <> 'K' and whdsc='借客户' and yearmon='" + y + getMon(m) + "'").executeUpdate();
                        this.getEntityManager().createNativeQuery("update inventoryproduct set wareh='JCZC-JYG' where facno <> 'K' and whdsc='借员工' and yearmon='" + y + getMon(m) + "'").executeUpdate();
                        this.getEntityManager().createNativeQuery("update inventoryproduct set wareh='JCZC-JCS' where facno <> 'K' and whdsc='借厂商' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        //在调整叶愉芬的19条逻辑
                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='P' where (genre<>'P')and (whdsc like '%真空%' or whdsc like '%P%') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='AJ' where (genre<>'P')and (whdsc like '%A机体倒扣仓%' or whdsc like '%装二判别库%') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='A'  where (whdsc like '%华东A零件库%' or whdsc like '%重庆A零件库%') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='R' where (whdsc like '%装配一倒扣仓%' or whdsc like '%装一判别库%' or whdsc like '%美的外租仓%') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='P' where   wareh='EPM01' and genre in('R','L','R,A','R,AJ','A','AJ') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='A' where   wareh='EM01' and genre in('R','L','R,A','R,AJ','A','AJ') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='S' where  whdsc like '%涡旋%' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='A' where  whdsc like  '%机组%' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='P' where  whdsc like '%隆基%' or whdsc like '%四川晶科%'"
                                        + "or whdsc like '%内蒙古中环%' or whdsc like '%包头阿特斯%'"
                                        + "or whdsc like '%楚雄零件库%' or whdsc like '%内蒙古中环%'"
                                        + "or whdsc like '%青海高景%' or whdsc like '%眉山成品库%'"
                                        + "or whdsc like '%四川京运通%' or whdsc like '%包头双良%' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='日立' where whdsc like '%兴塔无油零件%' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='AJ' where whdsc like '%A机体倒扣二%' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='AJ' where whdsc like '%AC专线库%' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='A' where whdsc like '%兴塔补料库%' and genre not in ('P','S') and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='AJ' where wareh='ETKF02' and yearmon='" + y + getMon(m) + "'").executeUpdate();

                        this.getEntityManager()
                                .createNativeQuery("update inventoryproduct set genre='A'  where wareh='EW01'  and genre not in('AD','P','S','RT') and yearmon='" + y + getMon(m) + "'").executeUpdate();
                    }
                }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log4j.error("InventoryProductBean.updateInventoryProduct()方法异常！！", ex.toString());
        }
        return false;
    }

    // 各产品别之库存统计表
    // 取到KPI的invamountproduct表中的数据做呈现
    private List getDataforKPIInvamountProductList(int y, int m, String facno) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select whdsc, ");
        sb.append(" ifnull(sum(CASE when genre =  'A'  then amount + amamount end),0) as 'AA', ");
        sb.append(" ifnull(sum(CASE when genre =  'AJ'  then amount + amamount end),0) as 'AH', ");
        sb.append(" ifnull(sum(CASE when genre =  'AD'  then amount + amamount end),0) as 'SDS', ");
        sb.append(" ifnull(sum(CASE when (genre = 'R' or genre = 'RG') then amount + amamount end),0) as 'R', ");
        sb.append(" ifnull(sum(CASE when genre =  'RT'  then amount + amamount end),0) as 'RT', ");
        sb.append(" ifnull(sum(CASE when genre = 'L'  then amount + amamount end),0) as 'L', ");
        sb.append(" ifnull(sum(CASE when genre = 'P'  then amount + amamount end),0) as 'P', ");
        sb.append(" ifnull(sum(CASE when genre = 'S'  then amount + amamount end),0) as 'S' ");
        sb.append(" from inventoryproduct where facno = '${facno}' and yearmon = '${y}${m}' ");
        sb.append(" GROUP BY whdsc ");
        String sql = sb.toString().replace("${facno}", String.valueOf(facno)).replace("${y}", String.valueOf(y))
                .replace("${m}", String.valueOf(getMon(m)));
        try {
            Query query = this.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("InventoryProductBean.getDataforKPIInvamountProductList()异常！！！", ex.toString());
        }
        return null;
    }

    public List<String[]> getDisplayInvamountProductList(int y, int m, String facno) {
        queryStringParams.clear();
        queryStringParams.put("facno", "C");
        List<String[]> dataResultList = new ArrayList<>();
        // 取到集合
        List dataDisplayList = getDataforKPIInvamountProductList(y, m, facno);
        try {
            if (!dataDisplayList.isEmpty()) {
                // 定义最后一列的数据类型
                BigDecimal cos1 = BigDecimal.ZERO;
                BigDecimal cos2 = BigDecimal.ZERO;
                BigDecimal cos3 = BigDecimal.ZERO;
                BigDecimal cos4 = BigDecimal.ZERO;
                BigDecimal cos5 = BigDecimal.ZERO;
                BigDecimal cos6 = BigDecimal.ZERO;
                BigDecimal cos7 = BigDecimal.ZERO;
                BigDecimal cos8 = BigDecimal.ZERO;
                BigDecimal cos9 = BigDecimal.ZERO;
                // 循环添加合计行、合计列
                for (int i = 0; i < dataDisplayList.size(); i++) {
                    strArr = new String[10];
                    Object[] row = (Object[]) dataDisplayList.get(i);
                    strArr[0] = row[0].toString();
                    strArr[1] = row[1].toString();
                    cos1 = cos1.add(BigDecimal.valueOf(Double.parseDouble(row[1].toString())));
                    strArr[2] = row[2].toString();
                    cos2 = cos2.add(BigDecimal.valueOf(Double.parseDouble(row[2].toString())));
                    strArr[3] = row[3].toString();
                    cos3 = cos3.add(BigDecimal.valueOf(Double.parseDouble(row[3].toString())));
                    strArr[4] = row[4].toString();
                    cos4 = cos4.add(BigDecimal.valueOf(Double.parseDouble(row[4].toString())));
                    strArr[5] = row[5].toString();
                    cos5 = cos5.add(BigDecimal.valueOf(Double.parseDouble(row[5].toString())));
                    strArr[6] = row[6].toString();
                    cos6 = cos6.add(BigDecimal.valueOf(Double.parseDouble(row[6].toString())));
                    strArr[7] = row[7].toString();
                    cos7 = cos7.add(BigDecimal.valueOf(Double.parseDouble(row[7].toString())));
                    strArr[8] = row[8].toString();
                    cos8 = cos8.add(BigDecimal.valueOf(Double.parseDouble(row[8].toString())));
                    // 添加最后一列的合计列
                    BigDecimal total = ((BigDecimal) row[1]).add((BigDecimal) row[2]).add((BigDecimal) row[3])
                            .add((BigDecimal) row[4]).add((BigDecimal) row[5]).add((BigDecimal) row[6]).add((BigDecimal) row[7])
                            .add((BigDecimal) row[8]);
                    strArr[9] = total.toString();
                    cos9 = cos9.add(total);
                    dataResultList.add(strArr);
                }
                // 添加最后一行的合计行
                String[] strArrTatal = new String[10];
                strArrTatal[0] = "合计";
                strArrTatal[1] = cos1.toString();
                strArrTatal[2] = cos2.toString();
                strArrTatal[3] = cos3.toString();
                strArrTatal[4] = cos4.toString();
                strArrTatal[5] = cos5.toString();
                strArrTatal[6] = cos6.toString();
                strArrTatal[7] = cos7.toString();
                strArrTatal[8] = cos8.toString();
                strArrTatal[9] = cos9.toString();
                dataResultList.add(strArrTatal);
            }
            return dataResultList;
        } catch (Exception ex) {
            log4j.error("InventoryProductBean.getDisplayInvamountProductList()异常！！！", ex.toString());
        }
        return null;
    }

    // 当前月份 m
    private String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

    //根据仓库号获取数据
    public List<InventoryProduct> getDetailsByWareh(int y, int m, String facno, String wareh) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append(" select * from inventoryproduct where facno ='").append(facno).append("'");
        sql.append(" and yearmon='").append(String.valueOf(y)).append(getMon(m)).append("'");
        sql.append(" and wareh ").append(wareh);
        Query query = this.getEntityManager().createNativeQuery(sql.toString(), InventoryProduct.class);
        return query.getResultList();
    }

    public List<InventoryProduct> findByFacnoAndYearmon(String facno, int y, int m) {

        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByFacnoAndYearmon");
        try {
            query.setParameter("yearmon", String.valueOf(y) + getMon(m));
            query.setParameter("facno", facno);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<InventoryProduct> findByFacnoAndYearmonAndCategories(String facno, String yearmon,String categories) {
        Query query = this.getEntityManager().createNamedQuery("InventoryProduct.findByFacnoAndYearmonAndCategories");
        try {
            query.setParameter("yearmon", yearmon);
            query.setParameter("facno", facno);
             query.setParameter("categories", categories);
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
