/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cn.hanson.kpi.evaluation;

import com.lightshell.comm.BaseLib;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
public abstract class ShipmentTon extends Shipment {

    public ShipmentTon() {
        super();
    }

    @Override
    public BigDecimal getValue(int y, int m, Date d, int type, LinkedHashMap<String, Object> map) {
        // 获得查询参数
        String facno = map.get("facno") != null ? map.get("facno").toString() : "";
        String cusno = map.get("cusno") != null ? map.get("cusno").toString() : "";// 客户
        String protype = map.get("protype") != null ? map.get("protype").toString() : "";// 种类
        String variety = map.get("variety") != null ? map.get("variety").toString() : "";// 分类
        String cuspono = map.get("cuspono") != null ? map.get("cuspono").toString() : "";// 客户采购单号

        BigDecimal shpton = BigDecimal.ZERO;
        BigDecimal bakton = BigDecimal.ZERO;
        BigDecimal armton = BigDecimal.ZERO;

        StringBuilder sb = new StringBuilder();
        // 出货
        // 2022/5/23不管是否单位换算都乘以换算率，非铸件 * 0.0 消除重量
        sb.append(" select isnull(sum(cast((case substring(s.judco,1,1)+s.fvco ");
        sb.append(" when '4F' then d.shpqy1*s.rate2 else d.shpqy1*isnull(s.rate2,0) end) as decimal(17,2))),0) ");
        sb.append(" from cdrdta d,cdrhad h,invmas s,cdrhmas c ");
        sb.append(" where h.facno=d.facno and h.shpno=d.shpno and d.itnbr=s.itnbr ");
        sb.append(" and d.facno=c.facno and d.cdrno=c.cdrno ");
        sb.append(" and h.houtsta not in ('N','W') and h.trtype in ('L1A','S1A') ");
        if (!"".equals(cusno)) {
            sb.append(" and h.cusno ").append(cusno);
        }
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(variety)) {
            if (!"OTH".equals(variety)) {
                sb.append(" and s.itcls in (select itcls from bsc_zlitcls where salitcls = '").append(variety)
                    .append("')");
            } else {
                sb.append(" and s.itcls not in (select itcls from bsc_zlitcls ) ");
            }
        }
        // if (!"".equals(cuspono)) {
        // sb.append(cuspono);
        // }
        // 重量目标不含商流直接排除
        sb.append(" and isnull(c.cuspono,'') NOT LIKE '%恒工%' and isnull(c.cuspono,'') NOT LIKE '%上海卓准%'");
        sb.append(" and year(h.shpdate) = ${y} and month(h.shpdate)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" and h.shpdate<= '${d}' ");
                break;
            case 5:
                // 日
                sb.append(" and h.shpdate= '${d}' ");
                break;
            default:
                sb.append(" and h.shpdate<= '${d}' ");
        }
        String cdrdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m))
            .replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);

        sb.setLength(0);
        // 销退不区分未开票退货还是已开票退货 2021/8/30
        // 未开票退货条件 h.owarehyn='Y'
        // 2022/5/23不管是否单位换算都乘以换算率，非铸件 * 0.0 消除重量
        sb.append(" select isnull(sum(cast((case substring(s.judco,1,1)+s.fvco ");
        sb.append(" when '4F' then d.bshpqy1*s.rate2 else d.bshpqy1*isnull(s.rate2,0) end) as decimal(17,2))),0) ");
        sb.append(" from cdrbdta d,cdrbhad h,invmas s,cdrhmas c ");
        sb.append(" where h.facno=d.facno and h.bakno=d.bakno and d.itnbr=s.itnbr ");
        sb.append(" and d.facno=c.facno and d.cdrno=c.cdrno ");
        sb.append(" and h.baksta not in ('N','W') and h.trtype in ('L2A','L2B','S2A','S2B')  ");
        if (!"".equals(cusno)) {
            sb.append(" and h.cusno ").append(cusno);
        }
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(variety)) {
            if (!"OTH".equals(variety)) {
                sb.append(" and s.itcls in (select itcls from bsc_zlitcls where salitcls = '").append(variety)
                    .append("')");
            } else {
                sb.append(" and s.itcls not in (select itcls from bsc_zlitcls ) ");
            }
        }
        // 重量目标不含商流销退排除
        sb.append(" and isnull(c.cuspono,'') NOT LIKE '%恒工%' and isnull(c.cuspono,'') NOT LIKE '%上海卓准%'");
        sb.append(" and year(h.bakdate) = ${y} and month(h.bakdate)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" and h.bakdate<= '${d}' ");
                break;
            case 5:
                // 日
                sb.append(" and h.bakdate= '${d}' ");
                break;
            default:
                sb.append(" and h.bakdate<= '${d}' ");
        }
        String cdrbdta = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m))
            .replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);

        sb.setLength(0);
        // 已开票销退
        // 2022/5/23不管是否单位换算都乘以换算率，非铸件 * 0.0 消除重量
        sb.append(" select isnull(sum(cast((case substring(s.judco,1,1)+s.fvco ");
        sb.append(" when '4F' then d.bshpqy1*s.rate2 else d.bshpqy1*isnull(s.rate2,0) end) as decimal(17,2))),0) ");
        sb.append(" from armblos a,cdrbdta d,invmas s ");
        sb.append(" where a.facno=d.facno ");
        sb.append(" and a.bakno=d.bakno and a.trseq=d.trseq and s.itnbr=d.itnbr ");
        sb.append(" and a.facno='${facno}' ");
        if (!"".equals(cusno)) {
            sb.append(" and a.ivocus ").append(cusno);
        }
        if (!"".equals(protype)) {
            sb.append(" and left(s.spdsc,2) ").append(protype);
        }
        if (!"".equals(variety)) {
            if (!"OTH".equals(variety)) {
                sb.append(" and s.itcls in (select itcls from bsc_zlitcls where salitcls = '").append(variety)
                    .append("')");
            } else {
                sb.append(" and s.itcls not in (select itcls from bsc_zlitcls ) ");
            }
        }
        sb.append(" and year(a.bildat) = ${y} and month(a.bildat)= ${m} ");
        switch (type) {
            case 2:
                // 月
                sb.append(" and a.bildat<= '${d}' ");
                break;
            case 5:
                // 日
                sb.append(" and a.bildat= '${d}' ");
                break;
            default:
                sb.append(" and a.bildat<= '${d}' ");
        }
        String armblos = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(m))
            .replace("${d}", BaseLib.formatDate("yyyyMMdd", d)).replace("${facno}", facno);
        // 获取数值
        superEJB.setCompany(facno);
        Query query1 = superEJB.getEntityManager().createNativeQuery(cdrdta);
        Query query2 = superEJB.getEntityManager().createNativeQuery(cdrbdta);
        // Query query3 = superEJB.getEntityManager().createNativeQuery(armblos);
        try {
            Object shp = query1.getSingleResult();
            Object shpbak = query2.getSingleResult();
            // Object armbak = query3.getSingleResult();
            shpton = (BigDecimal)shp;
            bakton = (BigDecimal)shpbak;
            // armton = (BigDecimal)armbak;
            return shpton.subtract(bakton).subtract(armton);
        } catch (Exception ex) {
            log4j.error("ShipmentTon.getValue()异常", ex);
        }
        return BigDecimal.ZERO;
    }

}
