/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author C1749 该实体类只作为财务库存报表（各库别之产品别库存金额）使用 数据库无对应的实体类
 */
public class InvamountProduct implements Serializable {

    //库号
    private String whdsc;
    //A机组
    private BigDecimal divisionAA;
    //A机体
    private BigDecimal divisionAH;
    //无油机组
    private BigDecimal divisionAD;
    //R冷媒
    private BigDecimal divisionR;
    //R冷媒RT
    private BigDecimal divisionRT;
    //AR冷媒冷冻
    private BigDecimal divisionL;
    //P真空
    private BigDecimal divisionP;
    //涡旋
    private BigDecimal divisionS;
    //合计
    private BigDecimal total;

    public InvamountProduct() {
        
    }

    public String getWhdsc() {
        return whdsc;
    }

    public void setWhdsc(String whdsc) {
        this.whdsc = whdsc;
    }

    public BigDecimal getDivisionAA() {
        return divisionAA;
    }

    public void setDivisionAA(BigDecimal divisionAA) {
        this.divisionAA = divisionAA;
    }

    public BigDecimal getDivisionAH() {
        return divisionAH;
    }

    public void setDivisionAH(BigDecimal divisionAH) {
        this.divisionAH = divisionAH;
    }

    public BigDecimal getDivisionAD() {
        return divisionAD;
    }

    public void setDivisionAD(BigDecimal divisionAD) {
        this.divisionAD = divisionAD;
    }

    public BigDecimal getDivisionR() {
        return divisionR;
    }

    public void setDivisionR(BigDecimal divisionR) {
        this.divisionR = divisionR;
    }

    public BigDecimal getDivisionRT() {
        return divisionRT;
    }

    public void setDivisionRT(BigDecimal divisionRT) {
        this.divisionRT = divisionRT;
    }

    public BigDecimal getDivisionL() {
        return divisionL;
    }

    public void setDivisionL(BigDecimal divisionL) {
        this.divisionL = divisionL;
    }

    public BigDecimal getDivisionP() {
        return divisionP;
    }

    public void setDivisionP(BigDecimal divisionP) {
        this.divisionP = divisionP;
    }

    public BigDecimal getDivisionS() {
        return divisionS;
    }

    public void setDivisionS(BigDecimal divisionS) {
        this.divisionS = divisionS;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    

}
