/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndexWareh;
import java.util.Date;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class IndexWarehBean extends SuperEJBForKPI<IndexWareh> {

    public IndexWarehBean() {
        super(IndexWareh.class);
    }

    public java.util.List<IndexWareh> findByGenerno(String generno) {
        Query query = getEntityManager().createNamedQuery("IndexWareh.findByGenerno");
        query.setParameter("generno", generno.trim());
        try {
            return query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
    public String getFormId(Date day, String code, String format, int len) {
        return super.getFormId(day, code, format, len); //To change body of generated methods, choose Tools | Templates.
    }
    
}
