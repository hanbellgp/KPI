/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.lazy;

import cn.hanbell.kpi.control.UserManagedBean;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.PolicyDetail;
import com.lightshell.comm.BaseLazyModel;
import com.lightshell.comm.SuperEJB;
import java.util.List;
import java.util.Map;
import org.primefaces.model.SortOrder;

/**
 *
 * @author C2082
 */
public class PolicyDetailModel extends BaseLazyModel<PolicyDetail> {


    public PolicyDetailModel(SuperEJB superEJB) {
        this.superEJB = superEJB;
    }
}
