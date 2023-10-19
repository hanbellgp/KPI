/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.ShoppingMenuWeight;
import cn.hanbell.kpi.entity.ShoppingTable;
import cn.hanbell.util.BaseLib;
import java.util.Date;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class ShoppingTableBean extends SuperEJBForKPI<ShoppingTable> {

    public ShoppingTableBean() {
        super(ShoppingTable.class);
    }

    public List<ShoppingTable> findByFacnoAndYearmon(String facno, String yearmon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select head.*");
        sql.append(" from shoppingtable head left join shoppingmanufacturer detail on head.facno=detail.facno and head.vdrno=detail.vdrno");
        sql.append(" where detail.facno is not null");
        sql.append(" and");
        sql.append(" head.facno='").append(facno).append("'");
        sql.append(" and");
        sql.append(" head.yearmon='").append(yearmon).append("'");
        Query q = this.getEntityManager().createNativeQuery(sql.toString(), ShoppingTable.class);
        try {
            List<ShoppingTable> result = q.getResultList();
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void deleteByYearmon(String yearmon) {
        StringBuffer sql = new StringBuffer();
        sql.append(" delete from shoppingtable where yearmon='").append(yearmon).append("'");
        try {
            this.getEntityManager().createNativeQuery(sql.toString()).executeUpdate();
        } catch (Exception e) {
        }
    }

    public void updateSHBType(Date d) {
        String yearmon = BaseLib.formatDate("yyyyMM", d);
        StringBuffer sql = new StringBuffer();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type= null ,iscenter=null where  yearmon = '%s' and facno='C';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='铸加',iscenter=1 where facno='C' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='C' and materialTypeName in ('托外加工','铸件','铸加'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='转子',iscenter=1 where facno='C' and yearmon like '%s' and vdrno in ('SZJ00065','SHB00016','SSH01164','SZJ00471','SSH00229','SSH00306','SSH02165') and itcls    in('1101','1801','2101','2401','2801','2201','3101','3201','3801');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='转子',iscenter=1 where facno='C' and yearmon like '%s' and vdrno in ('SZJ00065','SHB00016','SSH01164','SZJ00471','SSH00229','SSH00306','SSH02165') and itcls   not in('1101','1801','2101','2401','2801','2201','3101','3201','3801') and itdsc like '%%转子%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='电机',iscenter=1 where facno='C' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='电机');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='轴承',iscenter=1 where facno='C' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='轴承') and itdsc   like '%%轴承%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='油品',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='油品');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='阀类',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='阀类');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='刀具',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='刀具')and itcls  in('BS1','B001','C002');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='衬垫',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='衬垫')and itdsc   like '%%垫%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='接线盖板',iscenter=1  where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='接线盖板') and itdsc  like '%%盖板%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='接线盒',iscenter=1   where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='接线盒') and itdsc  like '%%接线盒%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='模具',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='模具') and itcls in('B005');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='模具',iscenter=1   where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName in ('铸件','托外加工','铸加')) and itcls in('B005');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='齿轮',iscenter=1  where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='齿轮');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='滤材',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='滤材');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='变频器',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='变频器');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='管件',iscenter=1  where facno='C' and yearmon like '%s' and vdrno in('SJS00441','SSH00631,','SZJ00529','SZJ00153','SSH00843') and userno='C0695';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='空压机',iscenter=1 where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C' and  materialTypeName ='空压机');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='其他',iscenter=1  where facno='C' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='C') and type is  null;", yearmon)).executeUpdate();
    }

    public void updateHSType(Date d) {
        String yearmon = BaseLib.formatDate("yyyyMM", d);
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type= null ,iscenter=null where  yearmon = '%s' and facno='H';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='铸加',iscenter=1 where facno='H' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='H' and materialTypeName in ('托外加工','铸件','铸加'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='转子',iscenter=1 where facno='H' and yearmon like '%s' and vdrno in ('HHN00008','HHB00010','HSH00087','HSH00277') and itcls in  ('2HZZ','2A04','2HTG','2101','2201','2801','3H','2013','2015','1A04','1HZZ');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='模具',iscenter=1 where facno='H' and yearmon like '%s'and vdrno in (select vdrno from shoppingmanufacturer where facno='H') and itcls in  ('B005');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='刀具',iscenter=1 where facno='H' and yearmon like '%s'and vdrno in (select vdrno from shoppingmanufacturer where facno='H') and itcls in  ('5C2','B001');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='其他',iscenter=1 where facno='H' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='H') and type is  null;", yearmon)).executeUpdate();

    }

    public void updateHYType(Date d) {
        String yearmon = BaseLib.formatDate("yyyyMM", d);
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type= null ,iscenter=null where  yearmon = '%s' and facno='Y';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='铸加',iscenter=1 where facno='Y' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='Y') and itcls in ('52','1AZ9');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='刀具',iscenter=1 where facno='Y' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='Y')and itcls in ('B001');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='其他',iscenter=1  where facno='Y' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='Y') and type is  null;", yearmon)).executeUpdate();

    }

    public void updateComerType(Date d) {
        String yearmon = BaseLib.formatDate("yyyyMM", d);
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type= null ,iscenter=null where  yearmon = '%s' and facno='K';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='铸加',iscenter=1 where facno='K' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='K' and materialTypeName in ('加工','铸件'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='电机',iscenter=1 where facno='K' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='K') and itcls in ('3J04','4047','3019');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='轴承',iscenter=1 where facno='K' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='K') and itcls in ('4009');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='模具',iscenter=1 where facno='K' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='K') and itcls in ('B005');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='衬垫',iscenter=1 where facno='K' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='K') and itdsc  like '%%衬垫%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='其他',iscenter=1  where facno='K' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='K') and type is  null;", yearmon)).executeUpdate();

    }

    public void updateZJComerType(Date d) {
        String yearmon = BaseLib.formatDate("yyyyMM", d);
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type= null ,iscenter=null where  yearmon = '%s' and facno='E';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='衬垫',iscenter=1 where facno='E' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='E') and itdsc  like '%%衬垫%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='其他',iscenter=1  where facno='E' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='E') and type is  null;", yearmon)).executeUpdate();

    }

    public void updateTHBType(Date d) {
        String yearmon = BaseLib.formatDate("yyyyMM", d);
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type= null ,iscenter=null where  yearmon = '%s' and facno='A';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='铸加',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('加工','鑄件'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='转子',iscenter=1 where facno='A' and yearmon like '%s'  and itcls in ('1011','2511','2521','3011','3511','1013','1511','1513','1521','1523','2011','2013','2512','2513','2522','3012','3013','3261','3311','3512','3513','3521','3522','3523','2523') and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('加工','鑄件'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='电机',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('電機'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='轴承',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('軸承'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='油品',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('油品','油品P'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='进口1',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in ('86010','86005','86005-1');", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='阀类',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('閥'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='阀类',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('閥'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='刀具',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('刀具'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='衬垫',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('襯墊'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='衬垫',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('襯墊'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='接线盖板',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in (select vdrno from shoppingmanufacturer where facno='A' and materialTypeName in ('蓋板'));", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='空压机',iscenter=1 where facno='A' and yearmon like '%s' and vdrno in ('86005') and itdsc like '%%空壓機組%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='模具',iscenter=1 where facno='A' and yearmon like '%s'and vdrno in (select vdrno from shoppingmanufacturer where facno='A') and itnbr like 'AT40%%';", yearmon)).executeUpdate();
        this.getEntityManager().createNativeQuery(String.format("update shoppingtable set type='其他',iscenter=1  where facno='A' and yearmon like '%s' and vdrno  in  (select vdrno from shoppingmanufacturer where facno='A') and type is  null;", yearmon)).executeUpdate();

    }
}
