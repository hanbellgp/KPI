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
public class InventoryIndicator implements Serializable {

    // 目标性质
    private String id;
    // 责任单位
    private String deptName;
    // 分类
    private String category;
    // 目标值
    private BigDecimal target;
    // 上月实际值
    private BigDecimal upactual;
    // 当月实际值
    private BigDecimal actual;
    // 基准值
    private BigDecimal benchmark;
    // 与目标比
    private BigDecimal targetThan;
    //与上月比
    private BigDecimal upactualThan;
    //与同期比
    private BigDecimal benchmarkThan;

    //备注
    private String remark;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTarget() {
        return target;
    }

    public void setTarget(BigDecimal target) {
        this.target = target;
    }

    public BigDecimal getUpactual() {
        return upactual;
    }

    public void setUpactual(BigDecimal upactual) {
        this.upactual = upactual;
    }

    public BigDecimal getActual() {
        return actual;
    }

    public void setActual(BigDecimal actual) {
        this.actual = actual;
    }

    public BigDecimal getTargetThan() {
        return targetThan;
    }

    public void setTargetThan(BigDecimal targetThan) {
        this.targetThan = targetThan;
    }

    public BigDecimal getUpactualThan() {
        return upactualThan;
    }

    public void setUpactualThan(BigDecimal upactualThan) {
        this.upactualThan = upactualThan;
    }

    public BigDecimal getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(BigDecimal benchmark) {
        this.benchmark = benchmark;
    }

    public BigDecimal getBenchmarkThan() {
        return benchmarkThan;
    }

    public void setBenchmarkThan(BigDecimal benchmarkThan) {
        this.benchmarkThan = benchmarkThan;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
