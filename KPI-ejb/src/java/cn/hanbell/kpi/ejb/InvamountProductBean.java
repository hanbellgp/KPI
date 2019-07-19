/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForERP;
import cn.hanbell.kpi.entity.InvamountProduct;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author C1749
 */
@Stateless
@LocalBean
public class InvamountProductBean implements Serializable {

    @EJB
    private SuperEJBForERP erpEJB;

    protected Logger log4j = LogManager.getLogger();

    public InvamountProductBean() {

    }

    //各产品别之库存统计表
    //取到产品别库存金额数据集合 --各库别之产品别库存金额表
    public List<InvamountProduct> getInvamountProductDataList(int y, int m) {
        List<InvamountProduct> inventoryList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append(" select a.whdsc, ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre =  'A'  then amount end),0))as 'AA', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'AJ' then amount end),0)) as 'AH', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'AD' then amount end),0)) as 'SDS', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when (a.genre = 'R' or a.genre = 'RG') then amount end),0))as 'R', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'RT' then amount end),0)) as 'RT', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'L'  then amount end),0)) as 'L', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'P'  then amount end),0)) as 'P', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'S'  then amount end),0)) as 'S' ");
        sb.append(" from  invamount a LEFT OUTER JOIN invwh w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
        sb.append(" where w.costyn = 'Y' and a.facno = 'C' and a.prono = '1' and a.yearmon='${y}${m}' ");
        sb.append(" GROUP BY a.whdsc ");
        sb.append(" UNION ");
        sb.append(" select a.whdsc, ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre =  'A'  then amount end),0))as 'AA', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'AJ' then amount end),0)) as 'AH', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'AD' then amount end),0)) as 'SDS', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when (a.genre = 'R' or a.genre = 'RG') then amount end),0))as 'R', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'RT' then amount end),0)) as 'RT', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'L'  then amount end),0)) as 'L', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'P'  then amount end),0)) as 'P', ");
        sb.append(" convert(DECIMAL(18,2),isnull(sum(CASE when a.genre = 'S'  then amount end),0)) as 'S' ");
        sb.append(" from  invamount a LEFT OUTER JOIN invwh w on w.facno = a.facno and w.prono = a.prono and w.wareh = a.wareh ");
        sb.append(" where a.facno = 'C' and a.prono = '1' and a.trtype in ('ZZ') and a.yearmon='${y}${m}' ");
        sb.append(" GROUP BY a.whdsc ");

        String sql = sb.toString().replace("${y}", String.valueOf(y)).replace("${m}", String.valueOf(getMon(m)));
        try {
            InvamountProduct it;
            Query query = erpEJB.getEntityManager().createNativeQuery(sql);
            List result = query.getResultList();
            if (!result.isEmpty() && result != null) {
                for (int i = 0; i < result.size(); i++) {
                    it = new InvamountProduct();
                    Object[] row = (Object[]) result.get(i);
                    it.setWhdsc(row[0].toString());
                    it.setDivisionAA(BigDecimal.valueOf(Double.valueOf(row[1].toString())));
                    it.setDivisionAH(BigDecimal.valueOf(Double.valueOf(row[2].toString())));
                    it.setDivisionAD(BigDecimal.valueOf(Double.valueOf(row[3].toString())));
                    it.setDivisionR(BigDecimal.valueOf(Double.valueOf(row[4].toString())));
                    it.setDivisionRT(BigDecimal.valueOf(Double.valueOf(row[5].toString())));
                    it.setDivisionL(BigDecimal.valueOf(Double.valueOf(row[6].toString())));
                    it.setDivisionP(BigDecimal.valueOf(Double.valueOf(row[7].toString())));
                    it.setDivisionS(BigDecimal.valueOf(Double.valueOf(row[8].toString())));
                    BigDecimal total = ((BigDecimal) row[1]).add((BigDecimal) row[2]).add((BigDecimal) row[3]).add((BigDecimal) row[4])
                            .add((BigDecimal) row[5]).add((BigDecimal) row[6]).add((BigDecimal) row[7]).add((BigDecimal) row[8]);
                    it.setTotal(total);
                    inventoryList.add(it);
                }
            }
            return inventoryList;

        } catch (Exception ex) {
            log4j.error("InvamountProductBean-getInvamountProductDataList()异常！", ex);
        }
        return null;
    }

    //给集合添加最后一列的合计项 -- --各库别之产品别库存金额表
    public List<InvamountProduct> getInvamountProductResultList(int y, int m) {
        List invList = getInvamountProductDataList(y, m);
        Iterator<InvamountProduct> itr = invList.iterator();
        InvamountProduct ity = new InvamountProduct();
        ity.setWhdsc("合计");
        try {
            while (itr.hasNext()) {
                InvamountProduct inventory = itr.next();
                ity.setDivisionAA(inventory.getDivisionAA().add(ity.getDivisionAA() == null ? new BigDecimal(0) : ity.getDivisionAA()));
                ity.setDivisionAH(inventory.getDivisionAH().add(ity.getDivisionAH() == null ? new BigDecimal(0) : ity.getDivisionAH()));
                ity.setDivisionAD(inventory.getDivisionAD().add(ity.getDivisionAD() == null ? new BigDecimal(0) : ity.getDivisionAD()));
                ity.setDivisionR(inventory.getDivisionR().add(ity.getDivisionR() == null ? new BigDecimal(0) : ity.getDivisionR()));
                ity.setDivisionRT(inventory.getDivisionRT().add(ity.getDivisionRT() == null ? new BigDecimal(0) : ity.getDivisionRT()));
                ity.setDivisionL(inventory.getDivisionL().add(ity.getDivisionL() == null ? new BigDecimal(0) : ity.getDivisionL()));
                ity.setDivisionP(inventory.getDivisionP().add(ity.getDivisionP() == null ? new BigDecimal(0) : ity.getDivisionP()));
                ity.setDivisionS(inventory.getDivisionS().add(ity.getDivisionS() == null ? new BigDecimal(0) : ity.getDivisionS()));
                ity.setTotal(inventory.getTotal().add(ity.getTotal() == null ? new BigDecimal(0) : ity.getTotal()));
            }
            invList.add(ity);
            return invList;
        } catch (Exception ex) {
            log4j.error("InvamountProductBean-getInvamountProductResultList()异常！", ex);
        }
        return null;
    }

    //当前月份 m
    private String getMon(int m) {
        if (m < 10) {
            return "0" + m;
        }
        return String.valueOf(m);
    }

}
