/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.eap.ejb.SystemUserBean;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PersonScorecard;
import cn.hanbell.wco.ejb.Agent1000002Bean;
import com.lightshell.comm.BaseLib;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
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

    @EJB
    private Agent1000002Bean agent1000002Bean;
    @EJB
    private SystemUserBean systemUserBean;

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

    public Object getScore(PersonScorecard sc, String column, int q) {
        try {
            Field f;
            Method method = null;
            f = sc.getClass().getDeclaredField(getColumn(column, q));
            f.setAccessible(true);
            return f.get(sc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Double getStandardScore(String officialrank, Double score) {
        if (officialrank.contains("A") || officialrank.contains("B")) {
            if (score > 120) {
                return 120.00;
            } else if (120 >= score && score > 100) {
                return score;
            } else if (100 >= score && score >= 94) {
                return 100.00;
            } else if (94 > score && score >= 90) {
                return 90.00;
            } else if (90 > score && score >= 87) {
                return 80.00;
            } else if (87 > score && score >= 82) {
                return 70.00;
            } else if (82 > score && score >= 80) {
                return 50.00;
            } else if (score < 80) {
                return 0.00;
            }
        } else if (officialrank.contains("C") || officialrank.contains("D") || officialrank.contains("E")) {
            if (score > 120) {
                return 120.00;
            } else if (120 >= score && score > 105) {
                return score;
            } else if (105 >= score && score >= 99) {
                return 100.00;
            } else if (99 > score && score >= 95) {
                return 90.00;
            } else if (95 > score && score >= 87) {
                return 80.00;
            } else if (87 > score && score >= 82) {
                return 70.00;
            } else if (82 > score && score >= 80) {
                return 50.00;
            } else if (score < 80) {
                return 0.00;
            }
        }
        return 0.00;
    }

    public Double getAmountOfScore(String level, boolean isadmin) {
        if ("A".equals(level)) {
            return 5.40;
        } else if ("B".equals(level)) {
            return 7.20;
        } else if ("C".equals(level)) {
            if (isadmin) {
                return 20.00;
            } else {
                return 19.5;
            }
        } else if ("D".equals(level)) {
            if (isadmin) {
                return 39.0;
            } else {
                return 38.5;
            }
        } else if ("E".equals(level)) {
            return 41.0;
        }
        return 0.0;
    }

    public String getColumn(String type, int i) {
        return type.toLowerCase() + String.format("%01d", i);
    }

    public boolean sendmsg(PersonScorecard psd, int quarter) {
        agent1000002Bean.initConfiguration();
        StringBuilder msg = new StringBuilder();
        msg.append("### 个人绩效考核:").append(BaseLib.formatDate("yyyy/MM/dd hh:mm", new Date()));
        BigDecimal porobjquarter = (BigDecimal) getScore(psd, "objectivepro", quarter);
        BigDecimal porsubquarter = (BigDecimal) getScore(psd, "subjectivitypro", quarter);
        BigDecimal subquarter = (BigDecimal) getScore(psd, "subjectivity", quarter);
        msg.append("\\n").append("");
        msg.append("\\n").append(">**考核详情**");
        msg.append("\\n").append(">考核人：").append(psd.getUsername()).append("(").append(psd.getUserid()).append(")");
        msg.append("\\n").append(">部门名称：<font color=\"warning\">").append(psd.getPersonset().getDeptname()).append("</font>");
        msg.append("\\n").append(">岗位：<font color=\"info\">").append(psd.getPersonset().getDuties()).append("</font>");
        msg.append("\\n").append(">职等：<font color=\"info\">").append(psd.getPersonset().getOfficialrank()).append("</font>");
        if (psd.getPersonset().getAssessmentmethod().equals("I") || psd.getPersonset().getAssessmentmethod().equals("J")) {
                if (porobjquarter.compareTo(BigDecimal.ZERO) != 0 && porsubquarter.compareTo(BigDecimal.ZERO) != 0 ) {
                    msg.append("\\n").append(">主观分数：<font color=\"comment\">").append(porsubquarter).append("</font>");
                    msg.append("\\n").append(">客观分数：<font color=\"comment\">").append(porobjquarter).append("</font>");
                    msg.append("\\n").append(">合计分数：<font color=\"comment\">").append(porsubquarter.add(porobjquarter)).append("</font>");
                    msg.append("\\n").append("");
                    msg.append("\\n").append("><font color=\"warning\">若您对考核结果存在任何异议或疑问，请及时前往系统进行调整与反馈:</font>");
                    //发送消息
                    SystemUser u1 = this.systemUserBean.findByUserId(psd.getUserid());
                    SystemUser u2 = this.systemUserBean.findByUserId(u1.getManagerId());
                    if (!"C0002".equals(u2.getManagerId())) {
                        agent1000002Bean.sendMsgToUser("C2082", "markdown", msg.toString());
                    }
                }
            } else if (!psd.getPersonset().getOfficialrank().equals("E")) {
                if (subquarter.compareTo(BigDecimal.ZERO) != 0 ) {
                    msg.append("\\n").append(">主观分数：<font color=\"comment\">").append(subquarter).append("</font>");
                    msg.append("\\n").append("");
                    msg.append("\\n").append("><font color=\"warning\">若您对考核结果存在任何异议或疑问，请及时前往系统进行调整与反馈:</font>");
                    //发送消息
                    SystemUser u1 = this.systemUserBean.findByUserId(psd.getUserid());
                    SystemUser u2 = this.systemUserBean.findByUserId(u1.getManagerId());
                    if (!"C0002".equals(u2.getManagerId())) {
                      agent1000002Bean.sendMsgToUser("C2082", "markdown", msg.toString());
                    }
                }

            }
        return true;
    }
}
