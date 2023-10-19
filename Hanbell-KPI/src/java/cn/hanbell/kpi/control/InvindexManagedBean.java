/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.control;

import cn.hanbell.eap.entity.Product;
import cn.hanbell.eap.entity.SystemUser;
import cn.hanbell.kpi.ejb.IndexWarehBean;
import cn.hanbell.kpi.ejb.InvindexBean;
import cn.hanbell.kpi.ejb.InvindexDetailBean;
import cn.hanbell.kpi.entity.IndexWareh;
import cn.hanbell.kpi.entity.Indicator;
import cn.hanbell.kpi.entity.Invindex;
import cn.hanbell.kpi.entity.InvindexDetail;
import cn.hanbell.kpi.lazy.InvindexModel;
import cn.hanbell.kpi.web.SuperMultiBean;
import cn.hanbell.kpi.web.SuperSingleBean;
import com.lightshell.comm.SuperMultiManagedBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.ejb.EJB;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author C2082
 */
@ManagedBean(name = "invindexManagedBean")
@SessionScoped
public class InvindexManagedBean extends SuperMultiBean<Invindex, InvindexDetail> {

    @EJB
    private InvindexBean invindexBean;
    @EJB
    private InvindexDetailBean invindexDetailBean;
    @EJB
    private IndexWarehBean invindexWarehBean;

    private String queryFacno;
    private String queryIndno;
    private String queryGenerno;
    private String queryGenerna;
    private String queryFormid;
    private String queryGenzls;

    private String queryPopGenerno;
    private List<IndexWareh> warehs;

    private IndexWareh selectWareh;

    public InvindexManagedBean() {
        super(Invindex.class, InvindexDetail.class);
    }

    @Override
    public void init() {
        this.superEJB = invindexBean;
        this.detailEJB = invindexDetailBean;
        warehs = new ArrayList();
        model = new InvindexModel(invindexBean);
        queryFacno = "C";
        this.setQueryPopGenerno("A1");
        this.popQuery();
        super.init();
    }

    @Override
    public void query() {
        if (model != null) {
            model.getFilterFields().clear();
            if (queryFacno != null && !"".equals(queryFacno)) {
                model.getFilterFields().put("facno", queryFacno);
            }
            if (queryIndno != null && !"".equals(queryIndno)) {
                model.getFilterFields().put("indno", queryIndno);
            }
            if (queryGenerno != null && !"".equals(queryGenerno)) {
                model.getFilterFields().put("generno", queryGenerno);
            }
            if (queryGenerna != null && !"".equals(queryGenerna)) {
                model.getFilterFields().put("generna", queryGenerna);
            }
            if (queryFormid != null && !"".equals(queryFormid)) {
                model.getFilterFields().put("formid", queryFormid);
            }
            if (queryGenzls != null && !"".equals(queryGenzls)) {
                model.getFilterFields().put("genzls", queryGenzls);
            }
        }
    }

    public void updateHead() {
        if (this.currentEntity.getId() != null) {
            invindexBean.update(currentEntity);
            showErrorMsg("Info", "修改成功！！！");
        }
    }

    public void popQuery() {
        warehs = invindexWarehBean.findByGenerno(this.getQueryPopGenerno());
//         this.showInfoMsg("Info", "成功");
    }

    public void popDelete() {
        invindexWarehBean.delete(this.selectWareh);
        popQuery();
        this.showInfoMsg("Info", "成功");
    }

    public void popSave() {
        if (this.selectWareh.getCreator() == null) {
            this.selectWareh.setCreator(this.userManagedBean.getUserid());
            this.selectWareh.setCredateToNow();
        } else {
            this.selectWareh.setOptuser(this.userManagedBean.getUserid());
            this.selectWareh.setOptdateToNow();
        }
        invindexWarehBean.update(selectWareh);
        popQuery();
        this.showInfoMsg("Info", "成功");
    }

    public void openNewSelectWareh() {
        this.selectWareh = new IndexWareh();
        this.selectWareh.setGenerno(queryGenerno);
        popQuery();
    }

    @Override
    public void setCurrentEntity(Invindex currentEntity) {
        this.currentEntity = currentEntity;
        if (currentEntity != null) {
            this.detailList = invindexDetailBean.findByPId(currentEntity.getId());
        }
    }

    @Override
    public void update() {
        if (this.currentEntity == null) {
            return;
        }
        if (this.currentEntity.getId() != null) {
            super.update();
        }
    }

    @Override
    public void createDetail() {
        if (this.getNewDetail() == null) {
            try {
                this.newDetail = (InvindexDetail) this.detailClass.newInstance();
                this.newDetail.setSeq(this.getMaxSeq(this.detailList));
                this.newDetail.setGenerno(this.currentEntity.getGenerno());
                this.newDetail.setIndno(this.currentEntity.getIndno());
            } catch (IllegalAccessException | InstantiationException var2) {
                this.showErrorMsg("Error", var2.getMessage());
            }
        }

        this.setCurrentDetail(this.newDetail);
    }

    @Override
    public void deleteDetail() {
        super.deleteDetail();
    }

    @Override
    public void doConfirmDetail() {
        super.doConfirmDetail();
    }

    public String getQueryFacno() {
        return queryFacno;
    }

    public void setQueryFacno(String queryFacno) {
        this.queryFacno = queryFacno;
    }

    public String getQueryIndno() {
        return queryIndno;
    }

    public void setQueryIndno(String queryIndno) {
        this.queryIndno = queryIndno;
    }

    public String getQueryGenerno() {
        return queryGenerno;
    }

    public void setQueryGenerno(String queryGenerno) {
        this.queryGenerno = queryGenerno;
    }

    public String getQueryGenerna() {
        return queryGenerna;
    }

    public void setQueryGenerna(String queryGenerna) {
        this.queryGenerna = queryGenerna;
    }

    public String getQueryFormid() {
        return queryFormid;
    }

    public void setQueryFormid(String queryFormid) {
        this.queryFormid = queryFormid;
    }

    public String getQueryGenzls() {
        return queryGenzls;
    }

    public void setQueryGenzls(String queryGenzls) {
        this.queryGenzls = queryGenzls;
    }

    public String getQueryPopGenerno() {
        return queryPopGenerno;
    }

    public void setQueryPopGenerno(String queryPopGenerno) {
        this.queryPopGenerno = queryPopGenerno;
    }

    public List<IndexWareh> getWarehs() {
        return warehs;
    }

    public void setWarehs(List<IndexWareh> warehs) {
        this.warehs = warehs;
    }

    public IndexWareh getSelectWareh() {
        return selectWareh;
    }

    public void setSelectWareh(IndexWareh selectWareh) {
        this.selectWareh = selectWareh;
    }

}
