/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.kpi.entity.Category;
import cn.hanbell.kpi.entity.IndicatorDetail;
import cn.hanbell.kpi.entity.JobSchedule;
import java.util.Calendar;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C0160
 */
@ManagedBean(name = "productSetManagedBean")
@SessionScoped
public class ProductSetManagedBean extends IndicatorSetManagedBean {

    private IndicatorDetail otherIndicator;
    private String otherLabel;

    /**
     * Creates a new instance of ProductIndicatorManagedBean
     */
    public ProductSetManagedBean() {
    }

    @Override
    public void create() {
        super.create();
        newEntity.setCompany(userManagedBean.getCompany());
        newEntity.setFormtype("N");
        newEntity.setFormkind("M");
        newEntity.setSeq(Calendar.getInstance().get(Calendar.YEAR));
        newEntity.setObjtype("P");
        newEntity.setLvl(0);
        newEntity.setValueMode("S");
        newEntity.setPerfCalc("AT");
    }

    @Override
    public void createDetail2() {
        super.createDetail2();
        newDetail2.setDeptno(currentEntity.getDeptno());
        newDetail2.setDeptname(currentEntity.getDeptname());
    }

    public void handleDialogReturnProductWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            Category e = (Category) event.getObject();
            currentEntity.setProductId(e.getId());
            currentEntity.setProduct(e.getCategory());
            currentEntity.setSortid(e.getSortid());
        }
    }

    public void handleDialogReturnProductWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            Category e = (Category) event.getObject();
            newEntity.setProductId(e.getId());
            newEntity.setProduct(e.getCategory());
            newEntity.setSortid(e.getSortid());
        }
    }

    public void handleDialogReturnJobScheduleWhenEdit(SelectEvent event) {
        if (event.getObject() != null && currentEntity != null) {
            JobSchedule e = (JobSchedule) event.getObject();
            currentEntity.setJobSchedule(e.getFormid());
        }
    }

    public void handleDialogReturnJobScheduleWhenNew(SelectEvent event) {
        if (event.getObject() != null && newEntity != null) {
            JobSchedule e = (JobSchedule) event.getObject();
            newEntity.setJobSchedule(e.getFormid());
        }
    }

    public void handleDialogReturnProductWhenDetail2Edit(SelectEvent event) {
        if (currentDetail2 != null && event.getObject() != null) {
            Category e = (Category) event.getObject();
            currentDetail2.setProductId(e.getId());
            currentDetail2.setProduct(e.getCategory());
        }
    }

    @Override
    public void init() {
        super.init();
        model.getFilterFields().put("objtype =", "P");
    }

    @Override
    public void query() {
        super.query();
        model.getFilterFields().put("objtype =", "P");
    }

    @Override
    public void reset() {
        super.reset();
        model.getFilterFields().put("objtype =", "P");
    }

    public void updateActualValue() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                Calendar c = Calendar.getInstance();
                c.setTime(queryDateBegin);
                if (currentEntity.getFreezeDate() != null) {
                    Calendar f = Calendar.getInstance();
                    f.setTime(currentEntity.getFreezeDate());
                    if (c.compareTo(f) < 1) {
                        showErrorMsg("Info", "资料已冻结不可再计算");
                        return;
                    }
                }
                indicatorBean.updateActual(currentEntity.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), Calendar.MONTH);
                currentEntity = indicatorBean.findById(currentEntity.getId());
                indicatorBean.updatePerformance(currentEntity);
                indicatorBean.update(currentEntity);
                showInfoMsg("Info", "更新实际值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    public void updateActualValueManual() {
        if (queryDateBegin != null && currentEntity != null) {
            try {
                Calendar c = Calendar.getInstance();
                c.setTime(queryDateBegin);
                if (currentEntity.getFreezeDate() != null) {
                    Calendar f = Calendar.getInstance();
                    f.setTime(currentEntity.getFreezeDate());
                    if (c.compareTo(f) < 1) {
                        showErrorMsg("Info", "资料已冻结不可再计算");
                        return;
                    }
                }
                indicatorBean.updateActualManual(currentEntity.getId(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.getTime(), Calendar.MONTH);
                currentEntity = indicatorBean.findById(currentEntity.getId());
                indicatorBean.updatePerformance(currentEntity);
                indicatorBean.update(currentEntity);
                showInfoMsg("Info", "更新实际值成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    public void updateOtherIndicator() {
        if (otherIndicator != null) {
            try {
                indicatorDetailBean.update(otherIndicator);
                showInfoMsg("Info", "更新其他数据成功");
            } catch (Exception ex) {
                showErrorMsg("Error", ex.toString());
            }
        }
    }

    /**
     * @return the otherIndicator
     */
    public IndicatorDetail getOtherIndicator() {
        return otherIndicator;
    }

    /**
     * @param otherIndicator the otherIndicator to set
     */
    public void setOtherIndicator(IndicatorDetail otherIndicator) {
        this.otherIndicator = otherIndicator;
    }

    public void setOtherIndicator(String label, IndicatorDetail otherIndicator) {
        this.otherLabel = label;
        this.otherIndicator = otherIndicator;
    }

    /**
     * @return the otherLabel
     */
    public String getOtherLabel() {
        return otherLabel;
    }

    /**
     * @param otherLabel the otherLabel to set
     */
    public void setOtherLabel(String otherLabel) {
        this.otherLabel = otherLabel;
    }

}
