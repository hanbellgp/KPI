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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        sbSql.append(" SELECT E.* FROM equipmentstandard E   WHERE  E.status='V' ");
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
        sMESList = getEPQIDDowntime();
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
    /**
     * 获取自主保全阶段超时未点选数据
     * @return 
     */
    public List getOvertimeEquipmentAnalyStage() {
        String resultSql = "SELECT B.deptname,B.formid,B.assetDesc,B.remark,A.stage,A.plandate,TIMESTAMPDIFF(DAY ,plandate,NOW()) DIFF FROM equipmentanalystage A LEFT JOIN assetcard B ON A.formid=B.formid WHERE A.status!='Y' AND  TIMESTAMPDIFF(DAY ,plandate,NOW())>2 ORDER BY  formid,stage";
        Query query = getEntityManager().createNativeQuery(resultSql);
        List<Object[]> results = query.getResultList();//已生成的计划保全单
        return results;
    }

    /**
     * 自主保全实施表
     *
     * @param formdate
     * @return
     */
    public List getImplementation(String formdate) {
        String resultSql = "SELECT A.deptname,A.assetno,a.assetdesc, B.totCount,b.sCount,DAY FROM (SELECT formid,assetno,assetdesc,deptname,DAY(formdate) DAY  FROM equipmentanalyresult WHERE formdate LIKE '%" + formdate + "%' AND company='C'AND standardlevel='一级' ) A LEFT JOIN (SELECT pid,COUNT(PID) totCount,CASE pid WHEN edate IS     NULL THEN count(pid) ELSE 0 END sCount FROM equipmentanalyresultdta GROUP BY pid) B ON A.formid=B.pid ORDER BY A.deptname";
        Query query = getEntityManager().createNativeQuery(resultSql);
        List<Object[]> results = query.getResultList();//已生成的计划保全单
        String assetCardSql = "SELECT A.formid, A.remark,deptname FROM assetcard A LEFT JOIN  assetitem I ON A.itemno=I.itemno WHERE  A.remark IS NOT NULL  and I.categoryid=3 AND A. company='C'  AND qty!=0 ORDER BY remark";
        query = getEntityManager().createNativeQuery(assetCardSql);
        List<Object[]> cList = query.getResultList();//已生成的计划保全单
        Map<String, List<Object[]>> map = new HashMap<>();
        results.forEach(result -> {
            if (map.containsKey(result[1].toString())) {//判断是否已存在对应键号
                map.get(result[1].toString()).add(result);//直接在对应的map中添加数据
            } else {//map中不存在，新建key，用来存放数据
                List<Object[]> tmpList = new ArrayList<>();
                tmpList.add(result);
                map.put(result[1].toString(), tmpList);//新增一个键号
            }
        });
        List moList1 = new ArrayList();
        for (Map.Entry<String, List<Object[]>> entry : map.entrySet()) {
            List<Object[]> list = entry.getValue();//取出对应的值
            Object[] obj1 = new Object[69];
            for (Object[] obj : list) {
                obj1[0] = obj[0];
                obj1[1] = obj[1];
                obj1[2] = obj[2];
                for (Object[] c : cList) {
                    if (c[0].equals(obj[1])) {
                        obj1[3] = c[1];
                    }
                }
                for (int i = 1; i <= 31; i++) {
                    if (Integer.parseInt(obj[5].toString()) == i) {
                        obj1[i * 2 + 2] = obj[3];
                        obj1[i * 2 + 3] = obj[4];
                    }
                }
            }
            moList1.add(obj1);
        }
        return moList1;
    }

    /**
     * 计划保全实施表
     *
     * @param formdate
     * @return
     */
    public List getImplementationYear(String formdate) {
        String resultSql = "SELECT A.deptname,A.assetno,a.assetdesc, B.totCount,b.sCount,DAY FROM (SELECT formid,assetno,assetdesc,deptname,MONTH(formdate) DAY  FROM equipmentanalyresult WHERE formdate LIKE '%" + formdate + "%' AND company='C' and standardlevel!='一级' ) A LEFT JOIN (SELECT pid,COUNT(PID) totCount,CASE pid WHEN edate IS     NULL THEN count(pid) ELSE 0 END sCount FROM equipmentanalyresultdta GROUP BY pid) B ON A.formid=B.pid ORDER BY A.deptname";
        Query query = getEntityManager().createNativeQuery(resultSql);
        List<Object[]> results = query.getResultList();//已生成的计划保全单
        String assetCardSql = "SELECT A.formid, A.remark,deptname FROM assetcard A LEFT JOIN  assetitem I ON A.itemno=I.itemno WHERE  A.remark IS NOT NULL  and I.categoryid=3 AND A. company='C'  AND qty!=0 ORDER BY remark";
        query = getEntityManager().createNativeQuery(assetCardSql);
        List<Object[]> cList = query.getResultList();//已生成的计划保全单
        Map<String, List<Object[]>> map = new HashMap<>();
        results.forEach(result -> {
            if (map.containsKey(result[1].toString())) {//判断是否已存在对应键号
                map.get(result[1].toString()).add(result);//直接在对应的map中添加数据
            } else {//map中不存在，新建key，用来存放数据
                List<Object[]> tmpList = new ArrayList<>();
                tmpList.add(result);
                map.put(result[1].toString(), tmpList);//新增一个键号
            }
        });
        List moList1 = new ArrayList();
        for (Map.Entry<String, List<Object[]>> entry : map.entrySet()) {
            List<Object[]> list = entry.getValue();//取出对应的值
            Object[] obj1 = new Object[29];
            for (Object[] obj : list) {
                obj1[0] = obj[0];
                obj1[1] = obj[1];
                obj1[2] = obj[2];
                for (Object[] c : cList) {
                    if (c[0].equals(obj[1])) {
                        obj1[3] = c[1];
                    }
                }
                for (int i = 1; i <= 12; i++) {
                    if (Integer.parseInt(obj[5].toString()) == i) {
                        obj1[i * 2 + 2] = obj[3];
                        obj1[i * 2 + 3] = obj[4];
                    }
                }
            }
            moList1.add(obj1);
        }
        return moList1;
    }

    //获取当天停机的机台编号
    public List<String> getEPQIDDowntime() {
        List<String> fMESList = new ArrayList<>();
        List<String> yMESList = new ArrayList<>();
        String stopSql = "";
        stopSql = " SELECT EQPID FROM  PLAN_SEMI_SQUARE WHERE  PLANDATE = convert(char,getdate(),111)  AND PRODUCTID='计划停机' AND WORKHOUR LIKE '%1440%'";

        Query query = mesEJB.getEntityManager().createNativeQuery(stopSql.toString());
        fMESList = query.getResultList();
        stopSql = "   SELECT EQPID FROM  PLAN_SEMI_CIRCLE WHERE  PLANDATE =convert(char,dateadd(dd,-1,getdate()),111) AND PRODUCTID='计划停机' AND WORKHOUR LIKE '%1440%'";
        query = mesEJB.getEntityManager().createNativeQuery(stopSql.toString());
        yMESList = query.getResultList();
        fMESList.addAll(yMESList);
        return fMESList;
    }
    /**
     * 获取备件库存到预警线的数据
     * @return 
     */
       public List getEquipmentSpare() {
        String resultSql = "SELECT A.sparenum,A.sparedesc,A.sparemodel,A.qty,B.minstock FROM(SELECT T.sparenum,S.sparedesc,S.sparemodel,sum(T.qty) as qty"
                + "   FROM equipmentsparestock T LEFT JOIN equipmentspare S ON T.sparenum = S.sparenum AND S.company = 'C'  LEFT JOIN equipmentspareclass C ON S.scategory = C.scategory AND C.company = 'C'"
                + "   LEFT JOIN equipmentsparemid M ON S.scategory = M.scategory AND S.mcategory = M.mcategory AND M.company = 'C'  WHERE T.sarea = '枫泾厂' AND T.company = 'C'  GROUP BY  sparenum,sparemodel) A LEFT JOIN equipmentspare B ON A.sparenum = B.sparenum AND B.company = 'C'  WHERE A.qty<minstock ORDER BY  sparenum";
        Query query = getEntityManager().createNativeQuery(resultSql);
        List<Object[]> results = query.getResultList();
        return results;
    }
       /**
        * 获取维修完成后48小时未结案单子
        * @return 
        */
      public List getPendingEquipment() {
        String resultSql = "SELECT formid,assetno,repairusername,serviceusername,TIMESTAMPDIFF(HOUR, hitchtime, NOW()),rstatus FROM equipmentrepair WHERE rstatus>30  and rstatus<95 AND TIMESTAMPDIFF(HOUR, hitchtime, NOW())>48";
        Query query = getEntityManager().createNativeQuery(resultSql);
        List<Object[]> results = query.getResultList();
        return results;
    }
    
}
