/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb.eam;

import cn.hanbell.kpi.comm.SuperEJBForEAM;
import cn.hanbell.kpi.comm.SuperEJBForMES;
import cn.hanbell.kpi.entity.eam.EquipmentAnalyResult;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class WorkshopEquipmentBean extends SuperEJBForEAM<EquipmentAnalyResult> {

    @EJB
    private SuperEJBForMES mesEJB;

    public WorkshopEquipmentBean() {
        super(EquipmentAnalyResult.class);
    }

    /**
     * 获取车间月报数据
     *
     * @param year 获取数据的年份
     * @param type 获取的车间
     * @param reportType 根据选择的版本调换取值数据及计算方法
     * @return
     */
    public List<Object[]> getMonthlyReport(String year, String type, String reportType) {
        String str = type.substring(3, type.length());//获取不良数的类型
        //月度总计划工时及平均计划工时,当月设备数量
        StringBuilder sbAVA = new StringBuilder();
        sbAVA.append(" SELECT A.MONTH,SUM(A.AVAILABLEMINS) AVAILABLEMINS,SUM(A.AVAILABLEMINS)/COUNT(A.EQPID) AVA,COUNT(A.EQPID) EQPIDCount   FROM ( SELECT E.EQPID,SUM(E.AVAILABLEMINS) AVAILABLEMINS, month(E.PLANDATE)  MONTH FROM EQP_AVAILABLETIME_SCHEDULE E");
        sbAVA.append(" LEFT JOIN MEQP M ON E.EQPID=M.EQPID WHERE E.PLANDATE LIKE '").append(year).append("%' AND E.PLANDATE< getdate()  AND E.AVAILABLEMINS!=0 AND M.PRODUCTTYPE='").append(type).append("'");
        sbAVA.append(" GROUP BY month(E.PLANDATE), E.EQPID) A GROUP BY A.MONTH");

        Query query = mesEJB.getEntityManager().createNativeQuery(sbAVA.toString());
        List<Object[]> avaList = query.getResultList();
        //获取MTBF总合
        StringBuilder sbMTBF = new StringBuilder();
        sbMTBF.append(" SELECT A.MONTH,SUM(A.MTBF) FROM ( SELECT CASE WHEN   (cast((B.AVAILABLEMINS * 1.0 - A.ALARMTIME_LEN * 1.0) / A.counts * 1.0 AS DECIMAL(10, 2))) IS NULL THEN B.AVAILABLEMINS ELSE cast((B.AVAILABLEMINS * 1.0 - A.ALARMTIME_LEN * 1.0) / A.counts * 1.0 AS DECIMAL(10, 2)) END   MTBF,");
        sbMTBF.append(" B.MONTH  FROM (SELECT EQPID,COUNT(EQPID) counts,sum(datediff(MINUTE, ALARMSTARTTIME, ALARMENDTIME)) AS ALARMTIME_LEN,month(ALARMSTARTTIME) MONTH");
        sbMTBF.append(" FROM EQP_RESULT_ALARM WHERE ALARMSTARTTIME LIKE '").append(year).append("%' AND (SPECIALALARMID = 'B0001' OR SPECIALALARMID = 'A0001') AND");
        sbMTBF.append(" datediff(MINUTE, ALARMSTARTTIME, ALARMENDTIME) > 10 GROUP BY month(ALARMSTARTTIME), EQPID) A RIGHT JOIN (SELECT A.EQPID,SUM(AVAILABLEMINS) AVAILABLEMINS,month(PLANDATE) MONTH");
        sbMTBF.append(" FROM EQP_AVAILABLETIME_SCHEDULE A LEFT JOIN MEQP M ON A.EQPID = M.EQPID");
        sbMTBF.append(" WHERE PLANDATE LIKE '").append(year).append("%' AND PLANDATE< getdate()  and AVAILABLEMINS!=0  AND  M.PRODUCTTYPE = '").append(type).append("'");
        sbMTBF.append(" GROUP BY month(PLANDATE), A.EQPID) B ON A.EQPID = B.EQPID AND A.MONTH = B.MONTH )A  GROUP BY  A.MONTH");
        query = mesEJB.getEntityManager().createNativeQuery(sbMTBF.toString());
        List<Object[]> mtbfList = query.getResultList();

        //-- 大于10分钟的故障次数和60分钟以上的故障次数及故障停机时间和其他总的异常时间
        StringBuilder sbCount = new StringBuilder();
        sbCount.append("  SELECT  A.MONTH, SUM(A.counts) counts10,sum(A.counts60) counts60,(CASE WHEN SUM(A.ALARMTIME_LEN) IS NULL THEN 0 ELSE SUM(A.ALARMTIME_LEN) END) ALARMTIME_LEN,SUM(A.abnormal) abnormal");
        sbCount.append("  FROM ( SELECT E.EQPID, COUNT(CASE WHEN datediff(MINUTE, E.ALARMSTARTTIME, E.ALARMENDTIME) > 60 AND M.ALARMNAME = '设备故障' THEN E.EQPID END ) counts60, COUNT(CASE WHEN datediff(MINUTE, E.ALARMSTARTTIME, E.ALARMENDTIME) > 10 AND M.ALARMNAME = '设备故障'  THEN E.EQPID END ) counts,");
        sbCount.append("  sum(CASE WHEN datediff(MINUTE, E.ALARMSTARTTIME, E.ALARMENDTIME) > 10 AND M.ALARMNAME = '设备故障'  THEN  datediff(MINUTE, E.ALARMSTARTTIME, E.ALARMENDTIME )END) AS ALARMTIME_LEN,sum(datediff(MINUTE, E.ALARMSTARTTIME, E.ALARMENDTIME)) AS abnormal,");
        sbCount.append("  month(E.ALARMSTARTTIME)MONTH FROM EQP_RESULT_ALARM E LEFT JOIN MALARM M ON E.SPECIALALARMID = M.ALARMID WHERE E.ALARMSTARTTIME LIKE '").append(year).append("%'  AND M.ALARMTYPE = '").append(type).append("' GROUP BY month(E.ALARMSTARTTIME), E.EQPID ) A GROUP BY A.MONTH");
        query = mesEJB.getEntityManager().createNativeQuery(sbCount.toString());
        List<Object[]> countList = query.getResultList();

        //每月报工数及标准工时
        StringBuilder sbQty = new StringBuilder();
        sbQty.append(" SELECT month(PROCESSCOMPLETETIME) MONTH ,count(CASE WHEN A.STEPID LIKE '%").append(str).append("清洗%' THEN A.EQPID END )  QTY,sum(round(B.STD_TIME / 60, 1)) MINUTE ");
        sbQty.append(" FROM PROCESS_STEP  A   LEFT JOIN PROCESS_STEP_TIME B ON A.SYSID = B.SYSID  AND A.EQPID = B.EQPID");
        sbQty.append(" LEFT JOIN  MEQP C ON C.EQPID = B.EQPID WHERE C.PRODUCTTYPE = '").append(type).append("'  AND A.PROCESSCOMPLETETIME LIKE'%").append(year).append("%' GROUP BY  month(PROCESSCOMPLETETIME)");
        query = mesEJB.getEntityManager().createNativeQuery(sbQty.toString());
        List<Object[]> qtyList = query.getResultList();

        //不良数
        StringBuilder sbQG = new StringBuilder();
        sbQG.append(" SELECT month(B.PROJECTCREATETIME) MONTH, SUM(DEFECTNUM) AS QGSUM FROM ANALYSISRESULT_QCD A LEFT JOIN  FLOW_FORM_UQF_S_NOW B ON  A.PROJECTID=B.PROJECTID");
        sbQG.append(" WHERE  B.ISPROCESSED='Y' AND B.PROJECTCREATETIME LIKE '").append(year).append("%' AND SOURCESTEPIP LIKE '").append(str).append("%' AND B.UQFTYPE ='UQFG0003' GROUP BY month(B.PROJECTCREATETIME)");
        query = mesEJB.getEntityManager().createNativeQuery(sbQG.toString());
        List<Object[]> qgList = query.getResultList();
        //在报修系统中获取故障时间为60分钟以上的次数
        StringBuilder sb60Count = new StringBuilder();
        String strTypeString = "";
        if (type.equals("半成品方型件")) {
            strTypeString = "方型加工课";
        } else {
            strTypeString = "圆型加工课";
        }
        sb60Count.append(" SELECT month(hitchtime),count(CASE WHEN TIMESTAMPDIFF(MINUTE,hitchtime, completetime) > 60 THEN E.assetno END) as count60,count(*) count10 FROM equipmentrepair E LEFT JOIN assetcard A ON E.assetno=A.formid ");
        sb60Count.append(" WHERE A.remark IS NOT NULL AND TIMESTAMPDIFF(MINUTE, hitchtime,completetime)>10 AND A.deptname='").append(strTypeString).append("' AND hitchtime LIKE '%").append(year).append("%'");
        sb60Count.append(" AND hitchtime<now() AND hitchurgency='03' GROUP BY month(hitchtime)");
        query = this.getEntityManager().createNativeQuery(sb60Count.toString());
        List<Object[]> sb60CountList = query.getResultList();
        //将所有的数据按所需要的模板整合到一个List中
        List<Object[]> listMES = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            Object[] obj = new Object[12];
            for (Object[] ava : avaList) {
                if (i == Integer.parseInt(ava[0].toString())) {
                    obj[0] = ava[1];
                    obj[1] = ava[2];
                    obj[11] = ava[3];
                }
            }
            for (Object[] count : countList) {
                if (i == Integer.parseInt(count[0].toString())) {

                    obj[4] = count[3];
                    obj[5] = count[4];
                }
            }
            for (Object[] qty : qtyList) {
                if (i == Integer.parseInt(qty[0].toString())) {
                    obj[6] = qty[1];
                    obj[7] = qty[2];
                }
            }
            for (Object[] qg : qgList) {
                if (i == Integer.parseInt(qg[0].toString())) {
                    obj[8] = qg[1];
                }
            }
            for (Object[] mtbf : mtbfList) {
                if (i == Integer.parseInt(mtbf[0].toString())) {
                    obj[10] = mtbf[1];
                }
            }
            for (Object[] count60 : sb60CountList) {
                if (i == Integer.parseInt(count60[0].toString())) {
                    obj[3] = count60[1];
                    obj[2] = count60[2];
                }
            }
            obj[9] = i;
            listMES.add(obj);
        }
        //将List中的数据经过模板中的公式进行计算处理在存入新的List中
        List<Object[]> resultsMES = new ArrayList<>();
        for (Object[] oMes : listMES) {
            if (oMes[0] != null) {
                Object[] obj = new Object[19];
                obj[0] = oMes[0];
                obj[1] = oMes[1];
                obj[2] = oMes[2];
                obj[3] = oMes[3];
                obj[4] = oMes[4];
                BigDecimal HAVA = BigDecimal.valueOf(Double.valueOf(oMes[0].toString()));//月度总生产工时
                BigDecimal GAVA = BigDecimal.valueOf(Double.valueOf(oMes[1].toString()));//月度平均生产工时
                BigDecimal ALA = BigDecimal.valueOf(Double.valueOf(oMes[4].toString()));//故障停机时间
                BigDecimal count10;
                if (oMes[2] == null) {
                    count10 = BigDecimal.ONE;
                } else {
                    count10 = BigDecimal.valueOf(Double.valueOf(oMes[2].toString()));//10分钟以上的故障次数
                }
                BigDecimal abnormal = BigDecimal.valueOf(Double.valueOf(oMes[5].toString()));//总异常时间
                BigDecimal MINUTE = BigDecimal.valueOf(Double.valueOf(oMes[7].toString()));//产出标准工时
                BigDecimal QTY = BigDecimal.valueOf(Double.valueOf(oMes[6].toString()));//报工数
                BigDecimal QGQTY = BigDecimal.valueOf(Double.valueOf(oMes[8].toString()));//不良数
                BigDecimal MTBF = BigDecimal.valueOf(Double.valueOf(oMes[10].toString()));//各加工机MTBF总合
                BigDecimal avaCount = BigDecimal.valueOf(Double.valueOf(oMes[11].toString()));//所有加工机数量
                //汉钟版设备可动率
                obj[5] = ((HAVA.subtract(ALA)).divide(HAVA, 4, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);//月度生产总工时-故障停机时间/月度生产总工时
                //顾问版设备可动率
                obj[6] = ((GAVA.subtract(ALA)).divide(GAVA, 4, BigDecimal.ROUND_HALF_UP)).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);//月度平均生产总工时-故障停机时间/月度平均生产总工时
                //设备故障率(%)   故障停机工时合计/月度生产总工时
                obj[7] = ALA.divide(HAVA, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                //MTTR   故障停机工时合计/故障件数合计
                obj[8] = ALA.divide(count10, 4, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                //顾问版MTBF   月度平均生产总工时/故障件数合计
                obj[9] = GAVA.divide(count10, 4, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(60), 0, BigDecimal.ROUND_HALF_UP);
                //汉钟版MTBF   月度生产总工时-故障停机时间/故障件数合计
                obj[10] = (HAVA.subtract(ALA)).divide(count10, 0, BigDecimal.ROUND_HALF_UP).divide(BigDecimal.valueOf(60), 0, BigDecimal.ROUND_HALF_UP);

                obj[11] = oMes[5];
                //时间稼动率   （月度生产总工时-总异常时间）/月度生产总工时
                obj[12] = (HAVA.subtract(abnormal)).divide(HAVA, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                obj[13] = (HAVA.subtract(abnormal)).divide(HAVA, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                //性能稼动率   产出标准工时/(月度生产总工时-总异常时间)
                obj[14] = MINUTE.divide((HAVA.subtract(abnormal)), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                obj[15] = MINUTE.divide((HAVA.subtract(abnormal)), 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                //良率  报工数-不良单据)/报工数(不良不论厂内外责任，只要经过加工就计入)
                obj[16] = (QTY.subtract(QGQTY)).divide(QTY, 4, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, BigDecimal.ROUND_HALF_UP);
                obj[17] = QTY;
                obj[18] = oMes[9];//月份
                resultsMES.add(obj);
            }
        }

        List<Object[]> list = new ArrayList();
        Object[] obj1 = new Object[14];
        Object[] obj2 = new Object[14];
        Object[] obj3 = new Object[14];
        Object[] obj4 = new Object[14];
        Object[] obj5 = new Object[14];
        Object[] obj6 = new Object[14];
        Object[] obj7 = new Object[14];
        Object[] obj8 = new Object[14];
        Object[] obj9 = new Object[14];
        Object[] obj10 = new Object[14];
        Object[] obj11 = new Object[14];
        Object[] obj12 = new Object[14];
        obj1[0] = "月度生产总工时(分)";
        obj1[13] = "=全车间单台设备计划工时总和";
        obj2[0] = "故障件数合计(件)";
        obj2[13] = "=全设备故障件数总和(停线10分钟以上,报修开始到维修结束)来源MES异常报表";
        obj3[0] = "故障停机工时合计(分)";
        obj3[13] = "=全设备故障时间总和(停线10分钟以上,报修开始到维修结束) 来源MES异常报表";
        obj4[0] = "设备可动率(%)";
        obj4[13] = "=(月度生产总工时-故障停机工时合计)/月度生产总工时";
        obj5[0] = "MTTR(分/件)";
        obj5[13] = "=故障停机工时合计/故障件数合计";
        obj6[0] = "MTBF(小时/件)";
        obj6[13] = "=全车间单台设备MTBF总和均值(单台的MTBF=(计划工作时间-故障停机时间)/维修次数)\n"
                + "仅记录停线10分钟以上数据  ";
        obj7[0] = "时间稼动率(%)";
        obj7[13] = "=（厂内生管计划工时-异常报表工时合计）/厂内生管计划工时";
        obj8[0] = "性能稼动率(%)";
        obj8[13] = "=产出标准工时/(厂内生管计划工时-异常报表工时合计)";
        obj9[0] = "良率(%)";
        obj9[13] = "=(报工数-不良单据)/报工数(不良不论厂内外责任，只要经过加工就计入)";
        obj10[0] = "月平均OEE(%)";
        obj10[13] = "=时间稼动率*性能稼动率*良率";
        obj11[0] = "故障60分以上件数";
        obj11[13] = "=全车间单台设备计划工时总和";
        obj12[0] = "设备故障率(%)";
        obj12[13] = "=全设备故障件数总和(停线60分钟以上,报修开始到维修结束)来源MES异常报表";

        for (int i = 1; i <= 12; i++) {
            for (Object[] mes : resultsMES) {
                if (i == Integer.parseInt(mes[18].toString())) {
                    if (mes[1] != null) {
                        if (reportType.equals("H")) {
                            obj1[i] = mes[0];
                        } else {
                            obj1[i] = mes[1];
                        }
                    }
                    if (mes[2] != null) {
                        obj2[i] = mes[2];
                    }
                    obj3[i] = mes[4];
                    if (mes[5] != null) {
                        if (reportType.equals("H")) {
                            obj4[i] = mes[5];
                        } else {
                            obj4[i] = mes[6];
                        }
                    }

                    if (mes[7] != null) {
                        obj12[i] = mes[7];
                    }

                    obj5[i] = mes[8];
                    if (mes[10] != null) {
                        if (reportType.equals("H")) {
                            obj6[i] = mes[10];
                        } else {
                            obj6[i] = mes[9];
                        }
                    }

                    if (mes[12] != null) {
                        if (reportType.equals("H")) {
                            obj7[i] = mes[12];
                        } else {
                            obj7[i] = mes[13];
                        }
                    } else {
                        obj7[i] = 100.00;
                    }

                    if (mes[14] != null) {
                        if (reportType.equals("H")) {
                            obj8[i] = mes[14];
                        } else {
                            obj8[i] = mes[15];
                        }
                    } else {
                        obj8[i] = 100.00;
                    }

                    if (mes[16] != null) {
                        obj9[i] = mes[16];
                    } else {
                        obj9[i] = 100.00;
                    }
                    double monthOEE = (Double.parseDouble(obj7[i].toString()) / 100 * Double.parseDouble(obj8[i].toString()) / 100 * Double.parseDouble(obj9[i].toString()) / 100) * 100;
                    if (String.valueOf(monthOEE).length() >= 5) {
                        obj10[i] = String.valueOf(monthOEE).substring(0, 5);
                    } else {
                        obj10[i] = monthOEE;
                    }

                    if (mes[3] == null) {
                        obj11[i] = 0;
                    } else {
                        obj11[i] = mes[3];
                    }
                    break;
                }
            }
        }
        list.add(obj1);
        list.add(obj2);
        list.add(obj3);
        list.add(obj4);
        if (reportType.equals("H")) {
            list.add(obj12);
        }
        list.add(obj5);
        list.add(obj6);
        list.add(obj7);
        list.add(obj8);
        list.add(obj9);
        list.add(obj10);
        list.add(obj11);
        return list;
    }

    public List<Object[]> getMonthlyReport(int y, String string, String h) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * 获取报修异常数据
     *
     * @param company 公司别
     * @param thisDate 当前日期
     * @return
     */
    public List<Object[]> getEquipmentRepairAbnormal(String company, String thisDate) {
        StringBuilder sbAbnormal = new StringBuilder();
        sbAbnormal.append("SELECT E.formid,E.formdate,E.hitchtime,E.servicearrivetime,assetno,E.itemno,A.assetDesc,repairdeptname,E.repairusername,serviceusername,if(rstatus='10','已报修','维修到达')  rstatus FROM equipmentrepair  E  LEFT JOIN  assetcard  A  ON E.assetno=A.formid WHERE  E.formdate<'").append(thisDate).append("' AND  (rstatus='10' OR  rstatus='20')");
        if (company != null) {
            sbAbnormal.append("and E.company='").append(company).append("'");
        } 
        sbAbnormal.append("  ORDER BY  E.rstatus,E.hitchtime DESC");
        Query query = this.getEntityManager().createNativeQuery(sbAbnormal.toString());
        List<Object[]> abnormalList = query.getResultList();

        return abnormalList;

    }

}
