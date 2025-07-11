/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.ShoppingManufacturer;
import cn.hanbell.kpi.model.ShoppingHSDataModel;
import cn.hanbell.util.BaseLib;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class ShoppingAccomuntBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;
    @EJB
    private ShoppingManufacturerBean shoppingManufacturerBean;
    private final DecimalFormat df;
    private final DecimalFormat dfpercent;

    public ShoppingAccomuntBean() {
        this.df = new DecimalFormat("#,###");
        this.dfpercent = new DecimalFormat("0.00％");
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

    public List<Object[]> getDateDetail(String facno, Date date) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select a.*,b.itcls");
        sql.append(" from (");
        sql.append(" SELECT apmpyh.facno ,apmpyh.vdrno , purvdr.vdrna , apmpyh.itnbr,apmpyh.itdsc,  sum(apmpyh.acpamt) as  acpamt,sum(apmpyh.payqty) as payqty,purhad.userno,left(sponr,2) as 'sponr'");
        sql.append(" FROM apmpyh ,purvdr ,purhad");
        sql.append(" WHERE apmpyh.vdrno = purvdr.vdrno  and  purhad.facno = apmpyh.facno  and  purhad.prono = apmpyh.prono");
        sql.append(" and  purhad.pono = apmpyh.pono  and  apmpyh.pyhkind = '1'");
        sql.append(" AND apmpyh.facno =  '").append(facno).append("'  and apmpyh.prono ='1'");
        sql.append(" and year(apmpyh.trdat) =").append(String.valueOf(findYear(date)));
        sql.append(" and month(apmpyh.trdat) =").append(String.valueOf(findMonth(date)));
        sql.append("  group by  apmpyh.facno ,apmpyh.vdrno , purvdr.vdrna , apmpyh.itnbr,apmpyh.itdsc, purhad.userno,left(sponr,2) )a left join invmas b on a.itnbr=b.itnbr");
        erpEJB.setCompany(facno);
        Query query = erpEJB.getEntityManager().createNativeQuery(sql.toString());
        List<Object[]> list = query.getResultList();
        return list;
    }

    public List<Object[]> getList(Date date) {
        List<Object[]> list = new ArrayList<>();
        list.add(getDateByIsCenter("SHB全部", "C", date, false));
        list.add(getDateByIsCenter("SHB采购中心", "C", date, true));
        list.add(getDateByIsCenter("THB全部", "A", date, false));
        list.add(getDateByIsCenter("THB采购中心", "A", date, true));
        list.add(getDateByIsCenter("HS全部", "H", date, false));
        list.add(getDateByIsCenter("HS采购中心", "H", date, true));
        list.add(getDateByIsCenter("SCM全部", "K", date, false));
        list.add(getDateByIsCenter("SCM采购中心", "K", date, true));
        list.add(getDateByIsCenter("ZCM全部", "E", date, false));
        list.add(getDateByIsCenter("ZCM采购中心", "E", date, true));
//        list.add(getDateByIsCenter("HY全部", "Y", date, false));
//        list.add(getDateByIsCenter("HY采购中心", "Y", date, true));
        //计算合计指标
        Object[] o1 = new Object[]{"集团合计", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, ""};
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < list.size(); j = j + 2) {
                o1[i] = ((BigDecimal) o1[i]).add((BigDecimal) list.get(j)[i]);
            }
        }
        list.add(o1);
        Object[] o2 = new Object[]{"采购中心合计", BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
            BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "", ""};
        for (int i = 1; i <= 13; i++) {
            for (int j = 1; j < list.size(); j = j + 2) {
                o2[i] = ((BigDecimal) o2[i]).add((BigDecimal) list.get(j)[i]);
            }
        }
        list.add(o2);
        //调整占比：各分公司占集团的百分比
        for (int j = 0; j < list.size(); j = j + 2) {
            try {
                BigDecimal value = ((BigDecimal) list.get(j)[13]).multiply(new BigDecimal(100)).divide(((BigDecimal) list.get(list.size() - 2)[13]), 2, BigDecimal.ROUND_HALF_UP);
                list.get(j)[14] = value.toString() + "%";
            } catch (ArithmeticException e) {
                list.get(j)[14] = "-";
            }
        }
        for (int j = 1; j < list.size(); j = j + 2) {
            try {
                BigDecimal value = ((BigDecimal) list.get(j)[13]).multiply(new BigDecimal(100)).divide(((BigDecimal) list.get(j - 1)[13]), 2, BigDecimal.ROUND_HALF_UP);
                list.get(j)[15] = value.toString() + "%";
            } catch (ArithmeticException e) {
                list.get(j)[15] = "-";
            }
        }
        return list;
    }

    public Object[] getDateByIsCenter(String name, String facno, Date date, boolean iscenter) {
        Object[] row = new Object[16];
        row[0] = name;
        //循环获取12个月的数据
        StringBuffer sql = new StringBuffer();
        sql.append(" select CAST(right(yearmon,2) AS SIGNED) ,sum(acpamt)");
        sql.append(" from shoppingtable");
        sql.append(" where facno ='").append(facno).append("'");
        if (iscenter) {
            sql.append(" and iscenter=1 ");
        }
        sql.append(" and yearmon like '").append(String.valueOf(findYear(date))).append("%' group by yearmon order by yearmon ASC");
        Query query = shoppingManufacturerBean.getEntityManager().createNativeQuery(sql.toString());
        BigDecimal sum = BigDecimal.ZERO;
        try {
            List<Object[]> data = query.getResultList();
            for (int i = 1; i <= 12; i++) {
                if (i <= data.size()) {
                    row[i] = (java.math.BigDecimal) data.get(i - 1)[1];
                } else {
                    row[i] = new BigDecimal(0.0);
                }
                sum = sum.add((java.math.BigDecimal) row[i]);
            }
            row[13] = sum;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return row;
    }

    public ShoppingHSDataModel getSalary1(String facno, int year, boolean isHs) {
        if (facno == "C") {
            return getSHBSalary1(year, isHs);
        } else if (facno == "A") {
            return getTHBSalary1(year, isHs);
        }
        return null;
    }

    public ShoppingHSDataModel getSalary2(String facno, int year, boolean isHs) {
        if (facno == "C") {
            return getSHBSalary2(year, isHs);
        } else if (facno == "A") {
            return getTHBSalary2(year, isHs);
        }
        return null;
    }

    public ShoppingHSDataModel getSalary3(String facno, int year, boolean isHs) {
        if (facno == "C") {
            return getSHBSalary3(year, isHs);
        } else if (facno == "A") {
            return getTHBSalary3(year, isHs);
        }
        return null;
    }

    public ShoppingHSDataModel getSalary4(String facno, int year, boolean isHs) {
        if (facno == "C") {
            return getSHBSalary4(year, isHs);
        } else if (facno == "A") {
            return getTHBSalary4(year, isHs);
        }
        return null;
    }

    public ShoppingHSDataModel getWeight(String facno, int year, boolean isHs) {
        if (facno == "C") {
            return getSHBWeight(year, isHs);
        } else if (facno == "A") {
            return getTHBWeight(year, isHs);
        }
        return null;
    }

    //获取上海汉钟1字头的铸件金额
    private ShoppingHSDataModel getSHBSalary1(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select yearmon,sum(acpamt)/10000");
        sql.append(" from shoppingtable left join shoppingmenuweight on  shoppingmenuweight.facno='C' and shoppingmenuweight.itnbr=shoppingtable.itnbr");
        sql.append(" where  type2 ='铸件' And left(shoppingtable.itnbr,1)='1' and shoppingtable.facno='C' and yearmon like '").append(year).append("%' and sponr not in ('AC')");
        if (isHs) {
            sql.append(" and  vdrno='SZJ00065'");
        };
        sql.append(" group by yearmon");

        return returnMonthList(sql.toString());
    }

    //获取上海汉钟2,3字头的铸件金额
    private ShoppingHSDataModel getSHBSalary2(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  yearmon,sum(a.payqty*c.weight*a.price)/10000");
        sql.append(" from shoppingtable a left join shoppingmenuweight c on a.itnbr =c.itnbr and c.facno='C'");
        sql.append(" where    type2='铸件' And left(a.itnbr,1)!='1' and a.facno='C' and yearmon like '").append(year).append("%' and sponr not in ('AC') ");
        if (isHs) {
            sql.append(" and  a.vdrno='SZJ00065'");
        };
        sql.append(" group by yearmon");
        return returnMonthList(sql.toString());
    }

    //获取上海汉钟2,3字头的加工件金额
    private ShoppingHSDataModel getSHBSalary3(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  yearmon,sum(acpamt-ifnull(a.payqty,0)*ifnull(c.weight,0)*ifnull(a.price,0))/10000");
        sql.append(" from shoppingtable a left join shoppingmenuweight c on a.itnbr =c.itnbr and c.facno='C'");
        sql.append(" where   type2='铸件' And left(a.itnbr,1)!='1' and a.facno='C' and yearmon like '").append(year).append("%'  ");
        if (isHs) {
            sql.append(" and  a.vdrno='SZJ00065'");
        };
        sql.append(" group by yearmon");
        return returnMonthList(sql.toString());
    }

    //获取上海汉AC的加工件金额
    private ShoppingHSDataModel getSHBSalary4(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  yearmon,sum(acpamt)/10000");
        sql.append(" from shoppingtable a left join shoppingmenuweight c on a.itnbr =c.itnbr and c.facno='C'");
        sql.append(" where sponr='AC' and  type2='铸件' and a.facno='C' and yearmon like '").append(year).append("%'  ");
        if (isHs) {
            sql.append(" and  a.vdrno='SZJ00065'");
        };
        sql.append(" group by yearmon");
        return returnMonthList(sql.toString());
    }

    //获取台湾1字头的铸件金额
    private ShoppingHSDataModel getTHBSalary1(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select yearmon,sum(acpamt)/10000");
        sql.append(" from shoppingtable left join shoppingmenuweight on  shoppingmenuweight.facno='A' and shoppingmenuweight.itnbr=shoppingtable.itnbr");
        sql.append(" where  type2 ='铸件' And left(shoppingtable.itnbr,1)='1' and shoppingtable.facno='A' and yearmon like '").append(year).append("%' and sponr not in  ('BAKI','AAKI')");
        if (isHs) {
            sql.append(" and  vdrno='1139'");
        };
        sql.append(" group by yearmon");

        return returnMonthList(sql.toString());
    }

    //获取台湾2,3字头的铸件金额
    private ShoppingHSDataModel getTHBSalary2(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  yearmon,sum(a.payqty*c.weight*a.price)/10000");
        sql.append(" from shoppingtable a left join shoppingmenuweight c on a.itnbr =c.itnbr and c.facno='A'");
        sql.append(" where type2='铸件' And left(a.itnbr,1)!='1' and a.facno='A' and yearmon like '").append(year).append("%' and sponr not in  ('BAKI','AAKI')  ");
        if (isHs) {
            sql.append(" and  a.vdrno='1139'");
        };
        sql.append(" group by yearmon");
        return returnMonthList(sql.toString());
    }

    //获取台湾汉钟2,3字头的加工件金额
    private ShoppingHSDataModel getTHBSalary3(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  yearmon,sum(acpamt-ifnull(a.payqty,0)*ifnull(c.weight,0)*ifnull(a.price,0))/10000");
        sql.append(" from shoppingtable a left join shoppingmenuweight c on a.itnbr =c.itnbr and c.facno='A'");
        sql.append(" where    sponr not in('BAKI','AAKI') and  type2='铸件' And left(a.itnbr,1)!='1' and a.facno='A' and yearmon like '").append(year).append("%'  ");
        if (isHs) {
            sql.append(" and  a.vdrno='1139'");
        };
        sql.append(" group by yearmon");
        return returnMonthList(sql.toString());
    }

    //获取台湾汉钟托工的加工件金额
    private ShoppingHSDataModel getTHBSalary4(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select  yearmon,sum(acpamt)/10000");
        sql.append(" from shoppingtable a left join shoppingmenuweight c on a.itnbr =c.itnbr and c.facno='A'");
        sql.append(" where   sponr  in('BAKI','AAKI') and  type2='铸件' and a.facno='A' and yearmon like '").append(year).append("%'  ");
        if (isHs) {
            sql.append(" and  a.vdrno='1139'");
        };
        sql.append(" group by yearmon");
        return returnMonthList(sql.toString());
    }

    //获取上海汉钟1字头的铸件重量
    private ShoppingHSDataModel getSHBWeight(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select yearmon,sum(payqty*shoppingmenuweight.weight)/1000");
        sql.append(" from shoppingtable left join shoppingmenuweight on  shoppingmenuweight.facno='C' and shoppingmenuweight.itnbr=shoppingtable.itnbr");
        sql.append(" where  type2 ='铸件' and shoppingtable.facno='C' and yearmon like '").append(year).append("%' and sponr not in ('AC')");
        if (isHs) {
            sql.append(" and vdrno='SZJ00065'");
        };
        sql.append(" group by yearmon");

        return returnMonthList(sql.toString());
    }

    //获取台湾汉钟1字头的铸件重量
    private ShoppingHSDataModel getTHBWeight(int year, boolean isHs) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select yearmon,sum(payqty*shoppingmenuweight.weight)/1000");
        sql.append(" from shoppingtable left join shoppingmenuweight on  shoppingmenuweight.facno='A' and shoppingmenuweight.itnbr=shoppingtable.itnbr");
        sql.append(" where  type2 ='铸件'  and shoppingtable.facno='A' and yearmon like '").append(year).append("%' and sponr not in  ('BAKI','AAKI')");
        if (isHs) {
            sql.append(" and  vdrno='1139'");
        };
        sql.append(" group by yearmon");

        return returnMonthList(sql.toString());
    }

    private ShoppingHSDataModel returnMonthList(String sql) {
        Query query = shoppingManufacturerBean.getEntityManager().createNativeQuery(sql.toString());
        List<Object[]> data = query.getResultList();
        ShoppingHSDataModel result = new ShoppingHSDataModel();
        BigDecimal sum = new BigDecimal(0.0);
        for (int j = 0; j < data.size(); j++) {
            int month = Integer.valueOf(data.get(j)[0].toString().substring(4));
            switch (month) {
                case 1:
                    result.setM1(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM1());
                    break;
                case 2:
                    result.setM2(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM2());
                    break;
                case 3:
                    result.setM3(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM3());
                    break;
                case 4:
                    result.setM4(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM4());
                    break;
                case 5:
                    result.setM5(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM5());
                    break;
                case 6:
                    result.setM6(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM6());
                    break;
                case 7:
                    result.setM7(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM7());
                    break;
                case 8:
                    result.setM8(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM8());
                    break;
                case 9:
                    result.setM9(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM9());
                    break;
                case 10:
                    result.setM10(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM10());
                    break;
                case 11:
                    result.setM11(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM11());
                    break;
                case 12:
                    result.setM12(new BigDecimal(data.get(j)[1].toString()).setScale(0, RoundingMode.HALF_UP));
                    sum = sum.add(result.getM12());
                    break;

            }

        }
        result.setSum(sum);
        return result;
    }

}
