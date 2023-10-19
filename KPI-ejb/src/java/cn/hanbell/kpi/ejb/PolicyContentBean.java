/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PolicyContent;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class PolicyContentBean extends SuperEJBForKPI<PolicyContent> {

    public PolicyContentBean() {
        super(PolicyContent.class);
    }

    public String getColumn(String type, int i) {
        return type.toLowerCase() + String.format("%01d", i);
    }

    public PolicyContent findByPidAndSeq(int pid, int seq) {
        Query query = getEntityManager().createNamedQuery("PolicyContent.findByPidAndSeq");
        query.setParameter("pid", pid);
        query.setParameter("seq", seq);
        try {
            return (PolicyContent) query.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public BigDecimal getPerformance(String calculationmethod, BigDecimal target, BigDecimal actual) {
        //实际/目标
        if ("A".equals(calculationmethod)) {
            //目标为0，则达成率为100%
            if (target.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.TEN.multiply(BigDecimal.TEN);
            } else {
                return actual.divide(target, 4, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.TEN).multiply(BigDecimal.TEN);
            }
        } else if ("B".equals(calculationmethod)) {
            //目标/实际
            if (actual.setScale(2,BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.TEN.multiply(BigDecimal.TEN);
            } else {
                return target.divide(actual, 4, BigDecimal.ROUND_HALF_UP).multiply( BigDecimal.TEN).multiply(BigDecimal.TEN);
            }
        } else {
            return BigDecimal.ZERO;
        }
    }
}
