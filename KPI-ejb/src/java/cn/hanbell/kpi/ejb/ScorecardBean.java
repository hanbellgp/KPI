/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.Scorecard;
import cn.hanbell.kpi.entity.ScorecardAuditor;
import cn.hanbell.kpi.entity.ScorecardContent;
import cn.hanbell.kpi.entity.ScorecardDetail;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.Query;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class ScorecardBean extends SuperEJBForKPI<Scorecard> {

    @EJB
    private ScorecardDetailBean scorecardDetailBean;
    @EJB
    private ScorecardAuditorBean scorecardAuditorBean;

    public ScorecardBean() {
        super(Scorecard.class);
    }

    public List<Scorecard> findByCompanyAndMenuAndIsBscAndYear(String company, String value, boolean isbsc, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByCompanyAndMenuAndYear");
        query.setParameter("company", company);
        query.setParameter("menu", value);
        query.setParameter("isbsc", true);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Scorecard> findByCompanyMenuAndYear(String company, String value, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByCompanyMenuAndYear");
        query.setParameter("company", company);
        query.setParameter("menu", value);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public int findRowCount(String company, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByRowCount");
        query.setParameter("company", company);
        query.setParameter("seq", y);
        if (query.getSingleResult() == null) {
            return -1;
        } else {
            return Integer.parseInt(query.getSingleResult().toString());
        }
    }

    public List<Scorecard> findByCompanyAndSeq(String company, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByCompanyAndSeq");
        query.setParameter("company", company);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Scorecard> findByCompanyAndIsbsc(String company, boolean isbsc, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByCompanyAndSeqAndIsbsc");
        query.setParameter("company", company);
        query.setParameter("isbsc", isbsc);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public Scorecard findByNameAndSeq(String name, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByNameAndSeq");
        query.setParameter("name", name);
        query.setParameter("seq", y);
        try {
            return (Scorecard) query.getSingleResult();
        } catch (Exception ex) {
            return null;
        }
    }

    public List<Scorecard> findByStatusAndYear(String status, int y) {
        Query query = getEntityManager().createNamedQuery("Scorecard.findByStatusAndYear");
        query.setParameter("status", status);
        query.setParameter("seq", y);
        try {
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public String getColumn(String type, int i) {
        return type.toLowerCase() + String.format("%01d", i);
    }

    public List<ScorecardDetail> getDetail(Object value) {
        return scorecardDetailBean.findByPId(value);
    }

    public List<ScorecardAuditor> getAuditorDetail1(Object value, int quater) {
        List<ScorecardAuditor> list = scorecardAuditorBean.findByPId(value);
        list.removeIf(auditor -> auditor.getQuarter() != quater);
        return list;
    }

    public BigDecimal getContentScores(List<ScorecardContent> detail, String column) throws Exception {
        BigDecimal weight;
        BigDecimal score;
        BigDecimal total = BigDecimal.ZERO;
        Object value;
        Field f;
        try {
            for (ScorecardContent entity : detail) {
                f = entity.getClass().getDeclaredField(column);
                f.setAccessible(true);
                value = f.get(entity);
                if (value == null || "".equals(value)) {
                    continue;
                }
                if (entity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                    total = total.add(BigDecimal.valueOf(Double.valueOf(value.toString())));
                } else {
                    weight = entity.getWeight().divide(BigDecimal.valueOf(100), 2);
                    score = BigDecimal.valueOf(Double.valueOf(value.toString()));
                    total = total.add(score.multiply(weight));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error(ex);
            throw ex;
        }
        return total;
    }

    public BigDecimal getDetailScores(List<ScorecardDetail> detail, String column) throws Exception {
        BigDecimal weight;
        BigDecimal score;
        BigDecimal total = BigDecimal.ZERO;
        Object value;
        Field f;
        try {
            for (ScorecardDetail entity : detail) {
                f = entity.getClass().getDeclaredField(column);
                f.setAccessible(true);
                value = f.get(entity);
                if (value == null || "".equals(value)) {
                    continue;
                }
                if (entity.getWeight().compareTo(BigDecimal.ZERO) == 0) {
                    total = total.add(BigDecimal.valueOf(Double.valueOf(value.toString())));
                } else {
                    weight = entity.getWeight().divide(BigDecimal.valueOf(100), 2);
                    score = BigDecimal.valueOf(Double.valueOf(value.toString()));
                    total = total.add(score.multiply(weight));
                }
            }
        } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException ex) {
            log4j.error(ex);
            throw ex;
        }
        return total;
    }

    public void setPerf(ScorecardContent content, String n) {
        if (content.getPerformanceJexl() == null || "".equals(content.getPerformanceJexl())) {
            throw new NullPointerException("表达式为空");
        }
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression
        String jexlExp = content.getPerformanceJexl().replace("${n}", n);
        JexlExpression exp = jexl.createExpression(jexlExp);
        // Create a context
        JexlContext jc = new MapContext();
        jc.set("object", content);
        // Evaluate the expression
        try {
            Object value = exp.evaluate(jc);
            BigDecimal score = BigDecimal.valueOf(Double.valueOf(value.toString()));
            switch (n) {
                case "q1":
                    content.setPq1(score);
                    break;
                case "q2":
                    content.setPq2(score);
                    break;
                case "h1":
                    content.setPh1(score);
                    break;
                case "q3":
                    content.setPq3(score);
                    break;
                case "q4":
                    content.setPq4(score);
                    break;
                case "h2":
                    content.setPh2(score);
                    break;
                case "fy":
                    content.setPfy(score);
                    break;
                default:
            }
        } catch (NumberFormatException ex) {
            throw ex;
        }
    }

    public void setPerf(ScorecardDetail d, String n) {
        if (d.getPerformanceJexl() == null || "".equals(d.getPerformanceJexl())) {
            throw new NullPointerException("表达式为空");
        }
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression
        String jexlExp = d.getPerformanceJexl().replace("${n}", n);
        JexlExpression exp = jexl.createExpression(jexlExp);
        // Create a context
        JexlContext jc = new MapContext();
        jc.set("object", d);
        // Evaluate the expression
        try {
            Object value = exp.evaluate(jc);
            BigDecimal score = BigDecimal.valueOf(Double.valueOf(value.toString()));
            switch (n) {
                case "q1":
                    d.setPq1(score);
                    break;
                case "q2":
                    d.setPq2(score);
                    break;
                case "h1":
                    d.setPh1(score);
                    break;
                case "q3":
                    d.setPq3(score);
                    break;
                case "q4":
                    d.setPq4(score);
                    break;
                case "h2":
                    d.setPh2(score);
                    break;
                case "fy":
                    d.setPfy(score);
                    break;
                default:
            }
        } catch (NumberFormatException ex) {
            throw ex;
        }
    }

    public void setContentScore(ScorecardContent d, String n) throws Exception {
        if (d.getScoreJexl() == null || "".equals(d.getScoreJexl())) {
            throw new NullPointerException("表达式为空");
        }
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression
        String jexlExp = d.getScoreJexl().replace("${n}", n);
        JexlExpression exp = jexl.createExpression(jexlExp);
        // Create a context
        JexlContext jc = new MapContext();
        jc.set("object", d);
        // Evaluate the expression
        try {
            Object value = exp.evaluate(jc);
            BigDecimal score = BigDecimal.valueOf(Double.valueOf(value.toString()));
            if (d.getMinNum() != null && score.compareTo(d.getMinNum()) < 0) {
                score = d.getMinNum();
            }
            if (d.getMaxNum() != null && score.compareTo(d.getMaxNum()) > 0) {
                score = d.getMaxNum();
            }
            switch (n) {
                case "q1":
                    d.getDeptScore().setSq1(score);
                    d.getGeneralScore().setSq1(score);
                    break;
                case "q2":
                    d.getDeptScore().setSq2(score);
                    d.getGeneralScore().setSq2(score);
                    break;
                case "h1":
                    d.getDeptScore().setSh1(score);
                    d.getGeneralScore().setSh1(score);

                    break;
                case "q3":
                    d.getDeptScore().setSq3(score);
                    d.getGeneralScore().setSq3(score);
                    break;
                case "q4":
                    d.getDeptScore().setSq4(score);
                    d.getGeneralScore().setSq4(score);
                    break;
                case "h2":
                    d.getDeptScore().setSh2(score);
                    d.getGeneralScore().setSh2(score);
                    break;
                case "fy":
                    d.getDeptScore().setSfy(score);
                    d.getGeneralScore().setSfy(score);
                    break;
                default:
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void setDetailScore(ScorecardDetail d, String n) throws Exception {
        if (d.getScoreJexl() == null || "".equals(d.getScoreJexl())) {
            throw new NullPointerException("表达式为空");
        }
        JexlEngine jexl = new JexlBuilder().create();
        // Create an expression
        String jexlExp = d.getScoreJexl().replace("${n}", n);
        JexlExpression exp = jexl.createExpression(jexlExp);
        // Create a context
        JexlContext jc = new MapContext();
        jc.set("object", d);
        // Evaluate the expression
        try {
            Object value = exp.evaluate(jc);
            BigDecimal score = BigDecimal.valueOf(Double.valueOf(value.toString()));
            if (d.getMinNum() != null && score.compareTo(d.getMinNum()) < 0) {
                score = d.getMinNum();
            }
            if (d.getMaxNum() != null && score.compareTo(d.getMaxNum()) > 0) {
                score = d.getMaxNum();
            }
            switch (n) {
                case "q1":
                    d.getGeneralScore().setSq1(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                case "q2":
                    d.getGeneralScore().setSq2(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                case "h1":
                    d.getGeneralScore().setSh1(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                case "q3":
                    d.getGeneralScore().setSq3(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                case "q4":
                    d.getGeneralScore().setSq4(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                case "h2":
                    d.getGeneralScore().setSh2(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                case "fy":
                    d.getGeneralScore().setSfy(score.setScale(2, BigDecimal.ROUND_HALF_UP));
                    break;
                default:
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void setContentScoreByCoefficient(ScorecardContent d, String n) throws Exception {
        if (d.getScoreJexl() == null || "".equals(d.getScoreJexl())) {
            throw new NullPointerException("表达式为空");
        }
        double score = 0.0;
        double minDifference = 500;
        double minCoefficient = 0.0;
        BigDecimal target = BigDecimal.ZERO;
        switch (n) {
            case "q1":
                target = d.getPq1();
                break;
            case "q2":
                target = d.getPq2();
                break;
            case "q3":
                target = d.getPq3();
                break;
            case "q4":
                target = d.getPq4();
                break;
            case "h1":
                target = d.getPh1();
                break;
            case "h2":
                target = d.getPh2();
                break;
        }
        for (double i = 0.1; i <= 0.9; i = i + 0.1) {
            JexlEngine jexl = new JexlBuilder().create();
            String jexlExp = d.getScoreJexl().replace("object.c${n}", String.valueOf(i)).replace("${n}", n);
            JexlExpression exp = jexl.createExpression(jexlExp);
            JexlContext jc = new MapContext();
            jc.set("object", d);
            BigDecimal score1 = BigDecimal.valueOf(Double.valueOf(exp.evaluate(jc).toString()));
            double value = score1.doubleValue();
            if (Math.abs(target.doubleValue() - value) < minDifference) {
                minDifference = Math.abs(target.doubleValue() - value);
                minCoefficient = i;
                score = value;
              
            }
        }
        BigDecimal bigDecimalScore = BigDecimal.valueOf(score);
        if (d.getMinNum() != null && bigDecimalScore.compareTo(d.getMinNum()) < 0) {
            bigDecimalScore = d.getMinNum();
        }
        if (d.getMaxNum() != null && bigDecimalScore.compareTo(d.getMaxNum()) > 0) {
            bigDecimalScore = d.getMaxNum();
        }
        switch (n) {
            case "q1":
                d.getDeptScore().setSq1(bigDecimalScore);
                d.getGeneralScore().setSq1(bigDecimalScore);
                d.setCq1(BigDecimal.valueOf(minCoefficient));
                break;
            case "q2":
                d.getDeptScore().setSq2(bigDecimalScore);
                d.getGeneralScore().setSq2(bigDecimalScore);
                d.setCq2(BigDecimal.valueOf(minCoefficient));
                break;
            case "h1":
                d.getDeptScore().setSh1(bigDecimalScore);
                d.getGeneralScore().setSh1(bigDecimalScore);
                d.setCh1(BigDecimal.valueOf(minCoefficient));
                break;
            case "q3":
                d.getDeptScore().setSq3(bigDecimalScore);
                d.getGeneralScore().setSq3(bigDecimalScore);
                d.setCq3(BigDecimal.valueOf(minCoefficient));
                break;
            case "q4":
                d.getDeptScore().setSq4(bigDecimalScore);
                d.getGeneralScore().setSq4(bigDecimalScore);
                d.setCq4(BigDecimal.valueOf(minCoefficient));
                break;
            case "h2":
                d.getDeptScore().setSh2(bigDecimalScore);
                d.getGeneralScore().setSh2(bigDecimalScore);
                d.setCh2(BigDecimal.valueOf(minCoefficient));
                break;
            case "fy":
                d.getDeptScore().setSfy(bigDecimalScore);
                d.getGeneralScore().setSfy(bigDecimalScore);
                d.setCfy(BigDecimal.valueOf(minCoefficient));
                break;
            default:
        }
    }

}
