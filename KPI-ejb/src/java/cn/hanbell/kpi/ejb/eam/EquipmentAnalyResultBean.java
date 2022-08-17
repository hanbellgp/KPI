/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.eam;

import cn.hanbell.kpi.comm.SuperEJBForEAM;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResult;
import cn.hanbell.kpi.entity.eam.EquipmentStandard;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C2079
 */
@Stateless
@LocalBean
public class EquipmentAnalyResultBean extends SuperEJBForEAM<EquipmentAnalyResult> {

    @EJB
    private SuperEJBForMES mesEJB;

    public EquipmentAnalyResultBean() {
        super(EquipmentAnalyResult.class);
    }

    /**
     * 自动生成自主点检排程 根据传入的等级及日期来获取生成的基准
     *
     * @param date
     * @param standardlevel
     * @return
     */
    public List<EquipmentStandard> getMonthlyReport(String date, String standardlevel) throws ParseException {
        StringBuilder sbSql = new StringBuilder();
        StringBuilder sbMES = new StringBuilder();
        sbSql.append(" SELECT E.* FROM equipmentstandard E  LEFT JOIN assetcard A  ON E.assetno=A.formid  WHERE  E.status='V' ");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date d = formatter.parse(date);//将String格式转为日期格式
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        if (standardlevel != null && !"".equals(standardlevel)) {
            cal.add(Calendar.DATE, 1);//获取明天日期
            sbSql.append(" AND E.nexttime>='").append(date).append("' ");
            sbSql.append(" AND E.nexttime<'").append(formatter.format(cal.getTime())).append("'");
            sbSql.append(" AND E.standardlevel='").append(standardlevel).append("'");
        } else {
            cal.add(Calendar.MONTH, 1);//获取下个月1号时间,因计划排程为每月1号所以直接月份相加
            sbSql.append(" AND E.nexttime>='").append(date).append("' ");
            sbSql.append(" AND E.nexttime<'").append(formatter.format(cal.getTime())).append("'");
            sbSql.append(" AND E.standardlevel!='一级'");
        }

        Query query = this.getEntityManager().createNativeQuery(sbSql.toString(), EquipmentStandard.class);
        List<EquipmentStandard> sList = query.getResultList();
        return sList;
    }

    //根据资产编号获取对应资产
    public Object[] findByAssetno(String value) {
        StringBuilder sbSql = new StringBuilder();
        sbSql.append(" SELECT * FROM assetcard WHERE  formid='").append(value).append("' ");
        Query query = this.getEntityManager().createNativeQuery(sbSql.toString());
        try {
            Object o = query.getSingleResult();
            return (Object[]) o;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Object[]> getUnqualifiedEquipmentAnalyResult(String deptname, String staDate) throws ParseException {
        StringBuffer eSql = new StringBuffer();
        StringBuilder sbSql = new StringBuilder();
        StringBuilder sbMES = new StringBuilder();
        List<String> sMESList = new ArrayList<>();
        List<Object> sCadeList = new ArrayList<>();
        String str = "";
        eSql.append(" SELECT E.formid,E.formdate, E.assetno,E.assetdesc,E.deptname,E.startdate,E.enddate,A.remark,NULL FROM equipmentanalyresult E LEFT JOIN assetcard A ON E.assetno=A.formid   WHERE (TIMESTAMPDIFF(MINUTE,startdate,enddate  )<2 OR enddate IS  NULL )");
        eSql.append("  AND E.formdate ='").append(staDate).append("' and E.deptname='").append(deptname).append("'  AND standardlevel='一级' and E.company='C'");
        Query query = this.getEntityManager().createNativeQuery(eSql.toString());
        List<Object[]> list = query.getResultList();
        //获取整天计划停机的机台编号
        sbMES.append("   SELECT  EQPID  FROM PLAN_DOWNTIME WHERE PLANDATE='").append(staDate.replace('-', '/')).append("' AND AVAILABLEMINS >1400");//默认停机时间大于1400算停机
        query = mesEJB.getEntityManager().createNativeQuery(sbMES.toString());
        sMESList = query.getResultList();
        if (!sMESList.isEmpty()) {
            for (String objects : sMESList) {
                str = str + "'" + objects + "',";
            }
            str = str.substring(0, str.length() - 1);
        }
        if (!"".equals(str)) {
            sbSql.append(" SELECT formid FROM assetcard WHERE  remark IN (").append(str).append(") ");
            query = this.getEntityManager().createNativeQuery(sbSql.toString());
            sCadeList = query.getResultList();
            for (Object[] eResult : list) {
                for (Object obj : sCadeList) {
                    if (eResult[2].toString().equals(obj)) {
                        eResult[8] = ("停机");
                    }
                }
            }
        }

        return list;
    }

}
