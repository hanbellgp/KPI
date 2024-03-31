/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PersonScorecard;
import cn.hanbell.kpi.entity.PersonScorecardDetail;
import com.lightshell.comm.SuperEntity;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class PersonScorecardBean extends SuperEJBForKPI<PersonScorecard> {

    public PersonScorecardBean() {
        super(PersonScorecard.class);
    }

    public PersonScorecard findByUseridAndYear(String userid, int y) {
        Query query = getEntityManager().createNamedQuery("PersonScorecard.findByUseridAndYear");
        query.setParameter("userid", userid);
        query.setParameter("year", y);
        try {
            return (PersonScorecard) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<PersonScorecard> findByYear(int y) {
        Query query = getEntityManager().createNamedQuery("PersonScorecard.findByYear");
        query.setParameter("year", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    //新增资料,一个表头多个明细
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void persist(Object entity, HashMap<SuperEJBForKPI, List<?>> detailAdded) {
        try {
            getEntityManager().persist(entity);
            if (detailAdded != null && !detailAdded.isEmpty()) {
                for (Map.Entry<SuperEJBForKPI, List<?>> entry : detailAdded.entrySet()) {
                    for (Object o : entry.getValue()) {
                        getEntityManager().persist(o);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //新事务更新资料,一个表头多个明细
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void syncUpdate(Object entity, HashMap<SuperEJBForKPI, List<?>> detailAdded, HashMap<SuperEJBForKPI, List<?>> detailEdited, HashMap<SuperEJBForKPI, List<?>> detailDeleted) {
        try {
            if (entity != null) {
                getEntityManager().merge(entity);
            }

            if (detailEdited != null && !detailEdited.isEmpty()) {
                for (Map.Entry<SuperEJBForKPI, List<?>> entry : detailEdited.entrySet()) {
                    for (Object o : entry.getValue()) {
                        getEntityManager().merge(o);
                    }
                }
            }
            if (detailDeleted != null && !detailDeleted.isEmpty()) {
                for (Map.Entry<SuperEJBForKPI, List<?>> entry : detailDeleted.entrySet()) {
                    for (Object o : entry.getValue()) {
                        if (getEntityManager().contains(o)) {
                            getEntityManager().remove(o);
                        } else {
                            getEntityManager().remove(getEntityManager().merge(o));
                        }
                    }
                }
            }
            if (detailAdded != null && !detailAdded.isEmpty()) {
                for (Map.Entry<SuperEJBForKPI, List<?>> entry : detailAdded.entrySet()) {
                    for (Object o : entry.getValue()) {
                        getEntityManager().persist(o);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BigDecimal getPorScore(PersonScorecard sc, int q) {
        try {
            Field f;
            Method method = null;
            f = sc.getClass().getDeclaredField(getColumn("porobjquarter", q));
            f.setAccessible(true);
            BigDecimal a = ((BigDecimal) f.get(sc));
            f = sc.getClass().getDeclaredField(getColumn("porsubquarter", q));
            f.setAccessible(true);
            BigDecimal b = ((BigDecimal) f.get(sc));
            return a.add(b);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double getStandardScore(Double score) {
        if (100 < score && score <= 120) {
            return score;
        } else if (90 < score && score <= 100) {
            return 100.00;
        } else if (85 < score && score <= 90) {
            return 90.00;
        } else if (75 < score && score <= 85) {
            return 70.00;
        } else if (60 < score && score <= 75) {
            return 50.00;
        } else {
            return 00.00;
        }
    }

    public Double getAmountOfScore(String level) {
        if ("A".equals(level)) {
            return 5.40;
        } else if ("B".equals(level)) {
            return 7.20;
        } else if ("C".equals(level)) {
            return 100.00;
        } else if ("D".equals(level)) {
            return 200.00;
        } else if ("E".equals(level)) {
            return 300.00;
        } else {
            return 00.00;
        }
    }

    public String getColumn(String type, int i) {
        return type.toLowerCase() + String.format("%01d", i);
    }
}
