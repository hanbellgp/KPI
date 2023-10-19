/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.Scorecard;
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
public class PolicyBean extends SuperEJBForKPI<Policy> {

    public PolicyBean() {
        super(Policy.class);
    }

    public Policy findByCompanyNameAndYear(String company, String name, int y) {
        Query query = getEntityManager().createNamedQuery("Policy.findByCompanyNameAndYear");
        query.setParameter("company", company);
        query.setParameter("name", name);
        query.setParameter("year", y);
        try {
            return (Policy) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Policy> findByCompanyMenuAndYear(String company, String deptno, int y) {
        Query query = getEntityManager().createNamedQuery("Policy.findByCompanyMenuAndYear");
        query.setParameter("company", company);
        query.setParameter("deptno", deptno);
        query.setParameter("year", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public List<Policy> findByDepeno(String deptno) {
        Query query = getEntityManager().createNamedQuery("Policy.findByDeptno");
        query.setParameter("deptno", deptno);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
