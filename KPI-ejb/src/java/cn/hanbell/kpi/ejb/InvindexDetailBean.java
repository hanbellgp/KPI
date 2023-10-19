/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Indicatorgrant;
import cn.hanbell.kpi.entity.InvindexDetail;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class InvindexDetailBean extends SuperEJBForKPI<InvindexDetail> {

    public InvindexDetailBean() {
        super(InvindexDetail.class);
    }

    public List<InvindexDetail> findByGenerno(String generno) {
        Query query = getEntityManager().createNamedQuery("InvindexDetail.findByGenerno");
        query.setParameter("generno", generno.trim());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<InvindexDetail> findByIndno(String indno) {
        Query query = getEntityManager().createNamedQuery("InvindexDetail.findByIndno");
        query.setParameter("indno", indno.trim());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }
     public List<InvindexDetail> findByWareh(String wareh) {
        Query query = getEntityManager().createNamedQuery("InvindexDetail.findByWareh");
        query.setParameter("wareh", wareh);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<Object[]> getWarehs(String generno) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select head.genzls,detail.wareh,detail.whdsc,head.formid,head.generna");
        sb.append(" from invindexdetail detail left  join invindex head on head.id=detail.pid where head.generno='").append(generno).append("'");
        sb.append(" order by head.indno ASC");
        try {
            Query q = getEntityManager().createNativeQuery(sb.toString());
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
       public List<Object[]> getWarehAndSortid(String generno) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select wareh,whdsc,max(sort)");
        sb.append(" from invindexdetail where generno='").append(generno).append("' group by wareh,whdsc order by max(sort)asc");
        try {
            Query q = getEntityManager().createNativeQuery(sb.toString());
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
