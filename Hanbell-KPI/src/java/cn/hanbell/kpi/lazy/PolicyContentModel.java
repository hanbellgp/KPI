/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.entity.Policy;
import cn.hanbell.kpi.entity.PolicyContent;
import cn.hanbell.kpi.entity.PolicyDetail;
import cn.hanbell.kpi.entity.ScorecardContent;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author C2082
 */
public class PolicyContentModel extends BaseLazyModel<PolicyContent> {

    private final UserManagedBean userManagedBean;

    public PolicyContentModel(SuperEJB superEJB, UserManagedBean userManagedBean) {
        this.superEJB = superEJB;
        this.userManagedBean = userManagedBean;
    }

    @Override
    public List<PolicyContent> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
        filterFields.put("parent.company =", userManagedBean.getCompany());
        return super.load(first, pageSize, sortField, sortOrder, filters);
    }

}
