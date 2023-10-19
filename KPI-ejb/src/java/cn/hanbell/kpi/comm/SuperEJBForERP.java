/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.comm;

import java.io.Serializable;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author C0160
 */
@Stateless
@LocalBean
public class SuperEJBForERP implements Serializable {

    protected String company = "C";

    @PersistenceContext(unitName = "PU_shberp")
    private EntityManager em_shberp;

    @PersistenceContext(unitName = "PU_gzerp")
    private EntityManager em_gzerp;

    @PersistenceContext(unitName = "PU_jnerp")
    private EntityManager em_jnerp;

    @PersistenceContext(unitName = "PU_njerp")
    private EntityManager em_njerp;

    @PersistenceContext(unitName = "PU_cqerp")
    private EntityManager em_cqerp;

    @PersistenceContext(unitName = "PU_chuxiongerp")
    private EntityManager em_chuxiongerp;
        
    @PersistenceContext(unitName = "PU_hansonerp")
    private EntityManager em_hansonerp;

    @PersistenceContext(unitName = "PU_hanyoungerp")
    private EntityManager em_hanyoungerp;

    @PersistenceContext(unitName = "PU_comererp")
    private EntityManager em_comererp;

    @PersistenceContext(unitName = "PU_zjcomererp")
    private EntityManager em_zjcomererp;

    @PersistenceContext(unitName = "PU_qtcerp")
    private EntityManager em_qtcerp;

    @PersistenceContext(unitName = "PU_sderp")
    private EntityManager em_sderp;

    @PersistenceContext(unitName = "PU_hkerp")
    private EntityManager em_hkerp;

    @PersistenceContext(unitName = "PU_thberp")
    private EntityManager em_thberp;

    @PersistenceContext(unitName = "PU_vhberp")
    private EntityManager em_vhberp;

    @PersistenceContext(unitName = "SHBEXCH-ejbPU")
    private EntityManager em_shbexcherp;
    
    public SuperEJBForERP() {

    }

    public synchronized EntityManager getEntityManager() {
        return getEntityManager(getCompany());
    }

    protected EntityManager getEntityManager(String facno) {
        switch (facno) {
            case "C":
                return em_shberp;
            case "G":
                return em_gzerp;
            case "J":
                return em_jnerp;
            case "N":
                return em_njerp;
            case "C4":
                return em_cqerp;
            case "F":
                return em_chuxiongerp;
            case "H":
                return em_hansonerp;
            case "Y":
                return em_hanyoungerp;
            case "K":
                return em_comererp;
            case "E":
                return em_zjcomererp;
            case "Q":
                return em_qtcerp;
            case "V":
                return em_vhberp;
            case "W":
                return em_sderp;
            case "X":
                return em_hkerp;
            case "A":
                return em_thberp;
            case "CEXCH"://上海ERP中间表
                return em_shbexcherp;
            default:
                return em_shberp;
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
    public synchronized void setCompany(String company) {
        this.company = company;
    }

}
