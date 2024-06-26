/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.eap.entity.Department;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PersonSet;
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
public class PersonSetBean extends SuperEJBForKPI<PersonSet> {

    public PersonSetBean() {
        super(PersonSet.class);
    }

    

    public PersonSet findByUserid(String userid) {
        Query query = getEntityManager().createNamedQuery("PersonSet.findByUserid");
        query.setParameter("userid", userid);
        List<PersonSet> list = query.getResultList();
        if (list.size() == 1) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
