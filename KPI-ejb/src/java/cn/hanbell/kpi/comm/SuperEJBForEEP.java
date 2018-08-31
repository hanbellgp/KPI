package cn.hanbell.kpi.comm;

import java.io.Serializable;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author C1879
 */
@Stateless
@LocalBean
public class SuperEJBForEEP implements Serializable {

    protected String company = "C";

    public SuperEJBForEEP() {
    }

    @PersistenceContext(unitName = "PU-SHBEEPXT")
    private EntityManager em_EEP_Client_HanBellXT;

    @PersistenceContext(unitName = "PU-SHBEEP")
    private EntityManager em_EEP_Client_HanBell;

    public EntityManager getEntityManager() {
        return getEntityManager(getCompany());
    }

    protected EntityManager getEntityManager(String facno) {
        switch (facno) {
            case "E":
                return em_EEP_Client_HanBellXT;
            case "C":
                return em_EEP_Client_HanBell;
            default:
                return em_EEP_Client_HanBell;
        }
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(String company) {
        this.company = company;
    }

}
