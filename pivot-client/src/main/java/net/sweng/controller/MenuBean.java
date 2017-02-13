package net.sweng.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

/**
 * Created on 2/12/17.
 */
@ManagedBean
@SessionScoped
public class MenuBean {

    private boolean uploadFileEnable;
    private boolean uploadDBEnable;

    public void uploadFromFile(ActionEvent event) {
        this.uploadFileEnable = true;
        this.uploadDBEnable = false;
    }

    public void uploadFromDatabase(ActionEvent event) {
        this.uploadFileEnable = false;
        this.uploadDBEnable = true;
    }

    public void selectRange() {
        addMessage("Unable to execute action", "Method not available", FacesMessage.SEVERITY_ERROR);
    }

    public void setRelations() {
        addMessage("Unable to execute action", "Method not available", FacesMessage.SEVERITY_ERROR);
    }

    public void generate() {
        addMessage("Unable to execute action", "Method not available", FacesMessage.SEVERITY_ERROR);
    }

    public void export() {
        addMessage("Unable to execute action", "Method not available", FacesMessage.SEVERITY_ERROR);
    }

    public boolean isUploadFileEnable() {
        return uploadFileEnable;
    }

    public boolean isUploadDBEnable() {
        return uploadDBEnable;
    }

    public void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
