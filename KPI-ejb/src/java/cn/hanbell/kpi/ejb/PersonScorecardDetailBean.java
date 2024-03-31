/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.PersonScorecardDetail;
import java.math.BigDecimal;
import java.util.ArrayList;
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
public class PersonScorecardDetailBean extends SuperEJBForKPI<PersonScorecardDetail> {

    public PersonScorecardDetailBean() {
        super(PersonScorecardDetail.class);
    }

    public List<PersonScorecardDetail> getSubjectiveAssessmentDetail(int pid, String accessmentmethdo, int q) {
        List<PersonScorecardDetail> detail = new ArrayList<PersonScorecardDetail>();
        switch (accessmentmethdo) {
            case "I":
            case "J":
                detail.add(new PersonScorecardDetail(pid, 1, "S", q, "工作创新", BigDecimal.ZERO, new BigDecimal("0.3")));
                detail.add(new PersonScorecardDetail(pid, 2, "S", q, "工作品质", BigDecimal.ZERO, new BigDecimal("0.3")));
                detail.add(new PersonScorecardDetail(pid, 3, "S", q, "积极性", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 4, "S", q, "服务性", BigDecimal.ZERO, new BigDecimal("0.1")));
                detail.add(new PersonScorecardDetail(pid, 5, "S", q, "沟通协调", BigDecimal.ZERO, new BigDecimal("0.1")));
                break;
            case "K":
            case "M":
                detail.add(new PersonScorecardDetail(pid, 1, "S", q, "创新能力", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 2, "S", q, "工作积极主动性", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 3, "S", q, "责任心/职业操守 ", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 4, "S", q, "沟通协调能力 ", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 5, "S", q, "自我学习能力 ", BigDecimal.ZERO, new BigDecimal("0.2")));
                break;
            case "L":
            case "N":
                detail.add(new PersonScorecardDetail(pid, 1, "S", q, "团队管理", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 2, "S", q, "创新能力", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 3, "S", q, "责任心/职业操守", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 4, "S", q, "人才培养", BigDecimal.ZERO, new BigDecimal("0.2")));
                detail.add(new PersonScorecardDetail(pid, 5, "S", q, "沟通协调 ", BigDecimal.ZERO, new BigDecimal("0.2")));
                break;
        }
        return detail;
    }

    public List<PersonScorecardDetail> findByPidAndQuarterAndType(int pid, int quarter, String type) {
        try {
            Query query = getEntityManager().createNamedQuery("PersonScorecardDetail.findByPIdAndQuarteAndType");
            query.setParameter("pid", pid);
            query.setParameter("quarter", quarter);
            query.setParameter("type", type);
            return query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    public int getMaxSeq1(int pid, int quarter, String type) {
        try {
            Query query = getEntityManager().createNamedQuery("PersonScorecardDetail.findByPIdAndQuarteAndType");
            query.setParameter("pid", pid);
            query.setParameter("quarter", quarter);
            query.setParameter("type", type);
            List<PersonScorecardDetail> details = query.getResultList();
            int max = 0;
            for (PersonScorecardDetail d : details) {
                if (max < d.getSeq()) {
                    max = d.getSeq();
                }
            }
            return max;
        } catch (Exception ex) {
            return 0;
        }
    }

}
