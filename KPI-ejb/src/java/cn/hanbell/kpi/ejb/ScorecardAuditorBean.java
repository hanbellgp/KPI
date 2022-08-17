/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ScorecardAuditor;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author Administrator
 */
@Stateless
@LocalBean
public class ScorecardAuditorBean extends SuperEJBForKPI<ScorecardAuditor> {

    public ScorecardAuditorBean() {
        super(ScorecardAuditor.class);
    }

    public List<ScorecardAuditor> findByPidAndAuditorId(int pid, String auditorId) {
        Query query = getEntityManager().createNamedQuery("ScorecardAuditor.findByPidAndAuditorId");
        query.setParameter("pid", pid);
        query.setParameter("auditorId", auditorId);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<ScorecardAuditor> findByPidAndQuarter(int pid, int seq, int quarter) {
        Query query = getEntityManager().createNamedQuery("ScorecardAuditor.findByPidAndSeqAndQuarter");
        query.setParameter("pid", pid);
        query.setParameter("seq", seq);
        query.setParameter("quarter", quarter);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }
}
