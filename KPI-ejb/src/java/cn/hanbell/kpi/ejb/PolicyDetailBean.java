/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.PolicyDetail;
import java.util.List;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class PolicyDetailBean extends SuperEJBForKPI<PolicyDetail> {

    public PolicyDetailBean() {
        super(PolicyDetail.class);
    }

    @Override
    public List<PolicyDetail> findByPId(Object value) {
        List<PolicyDetail> detail = super.findByPId(value);
        detail.sort((PolicyDetail o1, PolicyDetail o2) -> {
            if (o1.getSeq() > o2.getSeq()) {
                return 1;
            } else {
                return -1;
            }
        });
        return detail; 
    }

}
