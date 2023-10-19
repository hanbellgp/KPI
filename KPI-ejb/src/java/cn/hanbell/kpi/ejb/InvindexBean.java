/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Invindex;
import cn.hanbell.kpi.entity.InvindexDetail;
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
public class InvindexBean extends SuperEJBForKPI<Invindex> {

    public InvindexBean() {
        super(Invindex.class);
    }

    public String getGengerna(String generno) {
        switch (generno) {
            case "A1":
                return "生产库存金额";
            case "A2":
                return "营业库存金额";
            case "A3":
                return "服务库存金额";
            case "A4":
                return "借出未归金额";
            case "A5":
                return "其他金额";
            default:
                return "";
        }
    }

    public Invindex findByGenernoAndFormid(String generno, String formid) {
        Query query = getEntityManager().createNamedQuery("Invindex.findByGenernoAndFormid");
        query.setParameter("generno", generno.trim());
        query.setParameter("formid", formid.trim());
        try {
            return (Invindex) query.getSingleResult();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<Object[]> getRemarkByGenerno(String generno) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select distinct formid,generna");
        sb.append(" from invindex where  generno='").append(generno).append("'");
        try {
            Query q = getEntityManager().createNativeQuery(sb.toString());
            return q.getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
