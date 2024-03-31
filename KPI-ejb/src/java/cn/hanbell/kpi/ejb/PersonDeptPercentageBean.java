/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PersonDeptPercentage;
import cn.hanbell.kpi.entity.PersonScorecard;
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
public class PersonDeptPercentageBean extends SuperEJBForKPI<PersonDeptPercentage> {

    public PersonDeptPercentageBean() {
        super(PersonDeptPercentage.class);
    }

    public List<PersonDeptPercentage> findByYear(int y) {
        Query query = getEntityManager().createNamedQuery("PersonDeptPercentage.findByYear");
        query.setParameter("year", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }
        public PersonDeptPercentage findByDeptnoAndYear(String deptno,int y) {
        Query query = getEntityManager().createNamedQuery("PersonDeptPercentage.findByYearAndDeptno");
        query.setParameter("deptno", deptno);
        query.setParameter("year", y);
        try {
            return (PersonDeptPercentage)query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }
}
