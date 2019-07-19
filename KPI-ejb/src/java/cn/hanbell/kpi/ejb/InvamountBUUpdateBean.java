/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.InvamountBusinessUnit;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InvamountBUUpdateBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected LinkedHashMap<String, String> queryParams;

    public InvamountBUUpdateBean() {
        queryParams = new LinkedHashMap<>();
    }

    public List<InvamountBusinessUnit> getInvamountBUList(int y, int m) {
        queryParams.clear();
        queryParams.put("facno", "C");
        queryParams.put("prono", "1");
        List<InvamountBusinessUnit> resultData = getNewInvAmtBUList(y, m, queryParams);
        return resultData;
    }

    private List<InvamountBusinessUnit> getNewInvAmtBUList(int y, int m, LinkedHashMap<String, String> map) {
        List<InvamountBusinessUnit> InvAmtBUList = new ArrayList<>();
        InvamountBusinessUnit ibu;
        List list;
        try {
            list = getNowMonInvAmtList(y, m, map);
            if (list != null && !list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    Object[] row = (Object[]) list.get(i);
                    String facno = row[0] != null ? row[0].toString() : " ";
                    String prono = row[1] != null ? row[1].toString() : " ";
                    String creyear = row[2] != null ? row[2].toString() : " ";
                    String wareh = row[3] != null ? row[3].toString() : " ";
                    String whdsc = row[4] != null ? row[4].toString() : " ";
                    String categories = row[5] != null ? row[5].toString() : " ";
                    String genre = row[6] != null ? row[6].toString() : " ";
                    ibu = new InvamountBusinessUnit(facno, prono, creyear, wareh, whdsc, categories, genre);
                    switch (m) {
                        case 1:
                            ibu.setN01(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 2:
                            ibu.setN02(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 3:
                            ibu.setN03(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 4:
                            ibu.setN04(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 5:
                            ibu.setN05(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 6:
                            ibu.setN06(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 7:
                            ibu.setN07(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 8:
                            ibu.setN08(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 9:
                            ibu.setN09(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 10:
                            ibu.setN10(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 11:
                            ibu.setN11(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                        case 12:
                            ibu.setN12(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                            break;
                    }
                    ibu.setDifference(BigDecimal.ZERO);
                    ibu.setProportion(BigDecimal.ZERO);
                    InvAmtBUList.add(ibu);
                }
            }
            return InvAmtBUList;
        } catch (Exception ex) {
        }
        return null;

    }

    //获取当前月的库存金额明细
    private List getNowMonInvAmtList(int y, int m, LinkedHashMap<String, String> map) {
        String facno = map.get("facno") != null ? map.get("facno") : "";
        String prono = map.get("prono") != null ? map.get("prono") : "";
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.facno,a.prono,substring(a.yearmon,1,4),a.wareh,a.whdsc,w.genreno,a.genre,isnull(sum(a.amount) ,0)  ");
        sb.append(" from invamount a LEFT OUTER JOIN invindexdta w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh  ");
        sb.append(" WHERE a.facno = '${facno}' AND a.prono = '${prono}' AND a.genre NOT LIKE '%,%' AND a.genre <> 'QT'   ");
        sb.append(" AND a.yearmon = '${y}${m}' ");
        sb.append(" GROUP BY a.facno,a.prono,substring(a.yearmon,1,4),a.wareh,a.whdsc,w.genreno,a.genre ");
        String sql = sb.toString().replace("${facno}", String.valueOf(facno)).replace("${prono}", String.valueOf(prono)).replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(getMon(m)));
        try {
            erpEJB.setCompany(facno);
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            System.out.println("在执行InvamountBUUpdateBean的getNowMonInvAmtList（）方法时失败！！！" + ex.toString());
        }
        return null;

    }

    //获取月份单月自动补0
    private String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

}
