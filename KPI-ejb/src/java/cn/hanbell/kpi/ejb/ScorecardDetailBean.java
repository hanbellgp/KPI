/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.ScorecardDetail;
import cn.hanbell.kpi.entity.ScorecardExplanation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ScorecardDetailBean extends SuperEJBForKPI<ScorecardDetail> {

    @EJB
    private ScorecardExplanationBean scorecardExplanationBean;

    public ScorecardDetailBean() {
        super(ScorecardDetail.class);
    }

    @Override
    public void delete(ScorecardDetail entity) {
        super.delete(entity);
        scorecardExplanationBean.deleteByPId(entity.getId());
    }

    @Override
    public void persist(ScorecardDetail entity) {
        super.persist(entity);
        this.getEntityManager().flush();
        //自评
        ScorecardExplanation s = new ScorecardExplanation();
        s.setPid(entity.getId());
        s.setSeq(1);
        s.setType("S");
        scorecardExplanationBean.persist(s);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setSelfScore(s);
        //部门
        ScorecardExplanation d = new ScorecardExplanation();
        d.setPid(entity.getId());
        d.setSeq(2);
        d.setType("D");
        scorecardExplanationBean.persist(d);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setDeptScore(d);
        //老总
        ScorecardExplanation g = new ScorecardExplanation();
        g.setPid(entity.getId());
        g.setSeq(3);
        g.setType("G");
        scorecardExplanationBean.persist(g);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setGeneralScore(g);
        //其他
        ScorecardExplanation o = new ScorecardExplanation();
        o.setPid(entity.getId());
        o.setSeq(4);
        o.setType("O");
        scorecardExplanationBean.persist(o);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setOtherScore(o);
        //一阶原因
        ScorecardExplanation c1 = new ScorecardExplanation();
        c1.setPid(entity.getId());
        c1.setSeq(5);
        c1.setType("C1");
        scorecardExplanationBean.persist(c1);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setCauseScore1(c1);
        //一阶对策
        ScorecardExplanation s1 = new ScorecardExplanation();
        s1.setPid(entity.getId());
        s1.setSeq(6);
        s1.setType("S1");
        scorecardExplanationBean.persist(s1);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setSummaryScore1(s1);
        //二阶对策
        ScorecardExplanation c2 = new ScorecardExplanation();
        c2.setPid(entity.getId());
        c2.setSeq(7);
        c2.setType("C2");
        scorecardExplanationBean.persist(c2);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setCauseScore2(c2);
        //二阶对策
        ScorecardExplanation s2 = new ScorecardExplanation();
        s2.setPid(entity.getId());
        s2.setSeq(8);
        s2.setType("S2");
        scorecardExplanationBean.persist(s2);
        scorecardExplanationBean.getEntityManager().flush();
        entity.setSummaryScore2(s2);
        //设置后保存
        update(entity);
    }

    @Override
    public ScorecardDetail update(ScorecardDetail entity) {
        scorecardExplanationBean.update(entity.getSelfScore());
        scorecardExplanationBean.update(entity.getDeptScore());
        scorecardExplanationBean.update(entity.getGeneralScore());
        scorecardExplanationBean.update(entity.getOtherScore());
        scorecardExplanationBean.update(entity.getCauseScore1());
        scorecardExplanationBean.update(entity.getSummaryScore1());
        scorecardExplanationBean.update(entity.getCauseScore2());
        scorecardExplanationBean.update(entity.getSummaryScore2());
        scorecardExplanationBean.getEntityManager().flush();
        return super.update(entity);
    }

    public ScorecardDetail findByPidAndContent(int pid, String content) {
        Query query = getEntityManager().createNamedQuery("ScorecardDetail.findByPidAndContent");
        query.setParameter("pid", pid);
        query.setParameter("content", content);
        try {
            return (ScorecardDetail) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    //把&lt;br /&gt;格式变成\r\n的格式
    public String formatEAPEnt(Object value) {
        if (value == null) {
            return "";
        }
        String v = String.valueOf(value);
        Matcher m = Pattern.compile("(?m)^.*$").matcher(v);
        StringBuffer resultValue = new StringBuffer();
        while (m.find()) {
            resultValue.append(m.group()).append("\r\n");
        }
        return resultValue.toString().trim().replaceAll("<br />", "\r\n");
    }

    public String formatOAEnt(Object value) {
        if (value == null) {
            return "";
        } else {
            String v = String.valueOf(value);
            Matcher m = Pattern.compile("(?m)^.*$").matcher(v);
            StringBuffer resultValue = new StringBuffer();
            while (m.find()) {
                resultValue.append(m.group().trim()).append("&lt;br /&gt;");
            }
            return resultValue.toString().endsWith("&lt;br /&gt;") ? resultValue.substring(0, resultValue.length() - 12) : resultValue.toString();
        }
    }
}
