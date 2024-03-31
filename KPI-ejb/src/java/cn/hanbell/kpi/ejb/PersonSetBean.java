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

    public boolean isadministrative(String officialrank, String duties) {
        if (officialrank.contains("A")) {
            officialrank = "A";
        } else if (officialrank.contains("B")) {
            officialrank = "B";
        }
        switch (duties) {
            case "代班长":
            case "副班长":
            case "班长":
                duties = "A";
                break;
            case "代组长":
            case "副组长":
            case "组长":
                duties = "B";
                break;
            case "代课长":
            case "副课长":
            case "课长":
                duties = "C";
                break;
            case "代副理":
            case "副理":
            case "副经理":
            case "经理":
                duties = "D";
                break;
            case "协理":
            case "副总经理":
            case "副董事长":
            case "执行副总经理":
            case "董事长":
                duties = "E";
                break;
            default:
                duties = "A";
        }
        if (officialrank.compareTo(duties) == 0) {
            return true;
        } else {
            return false;
        }
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
