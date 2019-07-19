/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.InvamountBusinessUnit;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
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
public class InvamountBUBean extends SuperEJBForKPI<InvamountBusinessUnit> {

    @EJB
    InvamountBUUpdateBean invamountBUUpdateBean;

    public InvamountBUBean() {
        super(InvamountBusinessUnit.class);
    }

    //获取数据库的List
    private List<InvamountBusinessUnit> findByPk(String facno, String prono, String creyear, String wareh, String whdsc, String categories, String genre) {
        Query query = getEntityManager().createNamedQuery("InvamountBusinessUnit.findByPk");
        query.setParameter("facno", facno);
        query.setParameter("prono", prono);
        query.setParameter("creyear", creyear);
        query.setParameter("wareh", wareh);
        query.setParameter("whdsc", whdsc);
        query.setParameter("categories", categories);
        query.setParameter("genre", genre);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    //更新数据到KPI的InvamountBusinessUnit表中
    public boolean updateInvamountBusinessUnit(int y, int m) {
        List<InvamountBusinessUnit> newList = invamountBUUpdateBean.getInvamountBUList(y, m);
        try {
            if (!newList.isEmpty()) {
                for (InvamountBusinessUnit ib : newList) {
                    //判断资料库是否存在相同的数据
                    String facno = ib.getInvamountBusinessUnitPK().getFacno();
                    String prono = ib.getInvamountBusinessUnitPK().getProno();
                    String creyear = ib.getInvamountBusinessUnitPK().getCreyear();
                    String wareh = ib.getInvamountBusinessUnitPK().getWareh();
                    String whdsc = ib.getInvamountBusinessUnitPK().getWhdsc();
                    String categories = ib.getInvamountBusinessUnitPK().getCategories();
                    String genre = ib.getInvamountBusinessUnitPK().getGenre();
                    //循环每一行数据 判断当前插入数据 数据库是否存在 如果存在就更新数据 不存在就插入新的数据行
                    List flagList = findByPk(facno, prono, creyear, wareh, whdsc, categories, genre);
                    if (!flagList.isEmpty() && flagList.size() > 0) {
                        InvamountBusinessUnit a = newList.get(newList.indexOf(ib));
                        switch (m) {
                            case 1:
                                a.setN01(ib.getN01());
                                break;
                            case 2:
                                a.setN02(ib.getN02());
                                break;
                            case 3:
                                a.setN03(ib.getN03());
                                break;
                            case 4:
                                a.setN04(ib.getN04());
                                break;
                            case 5:
                                a.setN05(ib.getN05());
                                break;
                            case 6:
                                a.setN06(ib.getN06());
                                break;
                            case 7:
                                a.setN07(ib.getN07());
                                break;
                            case 8:
                                a.setN08(ib.getN08());
                                break;
                            case 9:
                                a.setN09(ib.getN09());
                                break;
                            case 10:
                                a.setN10(ib.getN10());
                                break;
                            case 11:
                                a.setN11(ib.getN11());
                                break;
                            case 12:
                                a.setN12(ib.getN12());
                                break;
                        }
                        //如果存在就更新当前这一笔数据
                        super.update(a);
                    } else {
                        //当前数据库不存在就插入该笔数据
                        super.persist(ib);
                    }
                }
                return true;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return false;
    }

    //获取数据源 为KPI的
    private List getDataList(String type, String genre, int y, int m) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select whdsc, ");
        sb.append("sum(n").append(getMon(m - 2)).append("),");
        sb.append("sum(n").append(getMon(m - 1)).append("),");
        sb.append("sum(n").append(getMon(m)).append(") ");
        sb.append(" from invamountbusinessunit WHERE 1=1  ");
        if (!type.equals("N") && !type.equals("")) {
            switch (type) {
                case "SC":
                    sb.append("AND categories  = 'A1'");
                    break;
                case "YY":
                    sb.append("AND categories  = 'A2'");
                    break;
                case "FW":
                    sb.append("AND categories  = 'A3'");
                    break;
                case "YF":
                    sb.append("AND categories  = 'A6'");
                    break;
                default:
                    sb.append(" ");
                    break;
            }
        }
        if (!genre.equals("N") && !genre.equals("")) {
            sb.append(" and genre = '").append(genre).append("'");
        }
        sb.append(" GROUP BY whdsc ");
        //服务在制和生产在制的数据
        if (!type.equals("N") && !type.equals("")) {
            sb.append(" UNION ");
            sb.append(" select whdsc, ");
            sb.append("sum(n").append(getMon(m - 2)).append("),");
            sb.append("sum(n").append(getMon(m - 1)).append("),");
            sb.append("sum(n").append(getMon(m)).append(") ");
            sb.append(" from invamountbusinessunit WHERE 1=1  ");
            //判断当前事业部性质
            switch (type) {
                case "SC":
                    sb.append(" AND wareh = 'SCZZ' ");
                    break;
                case "FW":
                    sb.append(" AND wareh = 'FWZZ' ");
                    break;
                default:
                    sb.append(" AND wareh = 'YFZZ' ");
            }
            //取到产品别
            if (!genre.equals("N") && !genre.equals("")) {
                sb.append(" and genre = '").append(genre).append("'");
            }
            sb.append(" GROUP BY whdsc ");
        }
        //借出总仓 借厂商的数据归生产目标；借客户借员工的数据归服务目标
        if (!type.equals("N") && !type.equals("")) {
            switch (type) {
                case "SC":
                    sb.append(" UNION ");
                    sb.append(" select whdsc, ");
                    sb.append("sum(n").append(getMon(m - 2)).append("),");
                    sb.append("sum(n").append(getMon(m - 1)).append("),");
                    sb.append("sum(n").append(getMon(m)).append(") ");
                    sb.append(" from invamountbusinessunit WHERE 1=1  ");
                    sb.append(" AND whdsc LIKE ('借厂商%') ");
                    if (!genre.equals("")) {
                        sb.append(" and genre = '").append(genre).append("'");
                    }
                    sb.append(" GROUP BY whdsc ");
                    break;
                case "FW":
                    sb.append(" UNION ");
                    sb.append(" select whdsc, ");
                    sb.append("sum(n").append(getMon(m - 2)).append("),");
                    sb.append("sum(n").append(getMon(m - 1)).append("),");
                    sb.append("sum(n").append(getMon(m)).append(") ");
                    sb.append(" from invamountbusinessunit WHERE 1=1  ");
                    sb.append(" AND whdsc IN ('借客户','借员工') ");
                    if (!genre.equals("N") && !genre.equals("")) {
                        sb.append(" and genre = '").append(genre).append("'");
                    }
                    sb.append(" GROUP BY whdsc ");
                    break;
            }
        }
        String sql = sb.toString().replace("${y}", String.valueOf(y));
        Query query = this.getEntityManager().createNativeQuery(sql);
        try {
            List result = query.getResultList();
            return result;
        } catch (Exception ex) {
            log4j.error("getDataList()异常！", ex);
        }
        return null;
    }

    //取到当月的合计值 算占比
    private BigDecimal getTotalAmts(String type, String genre, int y, int m) {
        List itsList = getDataList(type, genre, y, m);
        BigDecimal totalAmts = BigDecimal.ZERO;
        if (!itsList.isEmpty()) {
            for (int i = 0; i < itsList.size(); i++) {
                Object[] row = (Object[]) itsList.get(i);
                totalAmts = totalAmts.add((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]);
            }
        }
        return totalAmts;
    }

    //增加合计项和差异项和最后一行的合计 返回最终呈现的List
    public List<InvamountBusinessUnit> getBusinessUnitsResultList(String type, String genre, int y, int m) {
        List<InvamountBusinessUnit> tempList = new ArrayList<>();
        List dataList = getDataList(type, genre, y, m);
        BigDecimal totalAmts = getTotalAmts(type, genre, y, m);
        InvamountBusinessUnit ibu;
        try {
            if (!dataList.isEmpty()) {
                for (int i = 0; i < dataList.size(); i++) {
                    Object[] row = (Object[]) dataList.get(i);
                    ibu = new InvamountBusinessUnit("", "", "", "", row[0].toString(), "", "");
                    ibu.setN01((BigDecimal) row[1] == null ? new BigDecimal(0) : (BigDecimal) row[1]);
                    ibu.setN02((BigDecimal) row[2] == null ? new BigDecimal(0) : (BigDecimal) row[2]);
                    ibu.setN03((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]);
                    BigDecimal difference = ((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]).subtract((BigDecimal) row[2] == null ? new BigDecimal(0) : (BigDecimal) row[2]);
                    ibu.setDifference(difference);
                    if (totalAmts.compareTo(BigDecimal.ZERO) > 0) {
                        ibu.setProportion((((BigDecimal) row[3] == null ? new BigDecimal(0) : (BigDecimal) row[3]).divide(totalAmts, 5, BigDecimal.ROUND_UP).multiply(BigDecimal.valueOf(100))).setScale(3, BigDecimal.ROUND_HALF_UP));
                    } else {
                        ibu.setProportion(BigDecimal.ZERO);
                    }
                    tempList.add(ibu);
                }
            }
            //再添加最后一列合计项
            Iterator<InvamountBusinessUnit> itr = tempList.iterator();
            InvamountBusinessUnit ibu1 = new InvamountBusinessUnit("", "", "", "", "合计", "", "");
            while (itr.hasNext()) {
                InvamountBusinessUnit ibu2 = itr.next();
                ibu1.setN01(ibu2.getN01().add(ibu1.getN01() == null ? new BigDecimal(0) : ibu1.getN01()));
                ibu1.setN02(ibu2.getN02().add(ibu1.getN02() == null ? new BigDecimal(0) : ibu1.getN02()));
                ibu1.setN03(ibu2.getN03().add(ibu1.getN03() == null ? new BigDecimal(0) : ibu1.getN03()));
                ibu1.setDifference(ibu2.getDifference().add(ibu1.getDifference() == null ? new BigDecimal(0) : ibu1.getDifference()));
                ibu1.setProportion(BigDecimal.valueOf(100));
            }
            tempList.add(ibu1);
            return tempList;
        } catch (Exception ex) {
            log4j.error("getBusinessUnitsResultList()异常！", ex);
        }
        return null;
    }

    private String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

}
