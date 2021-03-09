/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.hanbell.kpi.query;

/**
 *
 * @author C1749
 */
import cn.hanbell.kpi.ejb.tms.ProjectBean;
import cn.hanbell.kpi.entity.tms.Project;
import cn.hanbell.kpi.web.SuperQueryBean;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

@ManagedBean(name = "projectQueryBean")
@ViewScoped
public class ProjectQueryBean extends SuperQueryBean<Project> {
    @EJB
    private ProjectBean projectBean;

    public String projectSeq;
    public String projectName;
    private Project selectedProject;
    private List<Project> selectedProjects;

    public ProjectQueryBean() {
        super(Project.class);
    }

    @Override
    public void reset() {
        selectedProjects.clear();
    }

    @Override
    public void init() {
        selectedProject = new Project();
        selectedProjects = new ArrayList<>();
        selectedProjects = projectBean.getProjectData();
    }

    @Override
    public void query() {
        selectedProjects = new ArrayList<>();
        selectedProjects = projectBean.getProjectData();
        if (!selectedProjects.isEmpty()) {
            if (!"".equals(projectSeq) && projectSeq != null) {
                selectedProjects = selectedProjects.stream().filter(s -> s.getProjectSeq() == Integer.parseInt(projectSeq)).collect(Collectors.toList());
            }
            if (!"".equals(projectName) && projectName != null) {
                selectedProjects = selectedProjects.stream().filter(s -> s.getProjectName().contains(projectName)).collect(Collectors.toList());
            }
        }
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public List<Project> getSelectedProjects() {
        return selectedProjects;
    }

    @Override
    public void closeDialog() {
        if (selectedProject != null) {
            PrimeFaces.current().dialog().closeDynamic(selectedProject);
        } else {
            showErrorMsg("Error", "没有选择数据");
        }
    }

    public void onRowSelect(SelectEvent event) {
        Project p = (Project) event.getObject();
        selectedProject = p;
    }

    public void setSelectedProjects(List<Project> selectedProjects) {
        this.selectedProjects = selectedProjects;
    }

    public String getProjectSeq() {
        return projectSeq;
    }

    public void setProjectSeq(String projectSeq) {
        this.projectSeq = projectSeq;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}