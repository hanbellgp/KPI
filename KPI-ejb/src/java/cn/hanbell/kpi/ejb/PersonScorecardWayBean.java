/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.ejb;

import cn.hanbell.kpi.comm.SuperEJBForKPI;
import cn.hanbell.kpi.entity.IndicatorDepartment;
import cn.hanbell.kpi.entity.PersonScorecard;
import cn.hanbell.kpi.entity.PersonScorecardWay;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 *
 * @author C2082
 */
@Stateless
@LocalBean
public class PersonScorecardWayBean extends SuperEJBForKPI<PersonScorecardWay> {

    public PersonScorecardWayBean() {
        super(PersonScorecardWay.class);
    }

}
