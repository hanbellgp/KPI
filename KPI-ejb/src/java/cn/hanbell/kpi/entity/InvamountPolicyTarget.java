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
 * @author C1749 当前类只做“库存金额按总经理室方针目标总表”对象使用，数据库无实体类
 */
public class InvamountPolicyTarget implements Serializable {

    //目标性质
    private String id;
    //责任单位
    private String deptName;
    //分类
    private String classify;
    //责任人
    private String responsible;
    //目标值
    private BigDecimal target;
    //当月实际值
    private BigDecimal actual;
    //差异1
    private BigDecimal difference1;
    //上月实际值
    private BigDecimal upactual;
    //差异2
    private BigDecimal difference2;
    //去年同期值
    protected BigDecimal benchmark;
    //差异3
    private BigDecimal difference3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public BigDecimal getDifference1() {
        return difference1;
    }

    public void setDifference1(BigDecimal difference1) {
        this.difference1 = difference1;
    }

    public BigDecimal getUpactual() {
        return upactual;
    }

    public void setUpactual(BigDecimal upactual) {
        this.upactual = upactual;
    }

    public BigDecimal getDifference2() {
        return difference2;
    }

    public void setDifference2(BigDecimal difference2) {
        this.difference2 = difference2;
    }

    public BigDecimal getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(BigDecimal benchmark) {
        this.benchmark = benchmark;
    }

    public BigDecimal getDifference3() {
        return difference3;
    }

    public void setDifference3(BigDecimal difference3) {
        this.difference3 = difference3;
    }

}
