package net.sweng.controller;

import net.sweng.domain.OptionView;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import static net.sweng.config.HttpSessionHandler.getSessionAttribute;
import static net.sweng.config.SessionKeys.FILE_AVAILABLE;
import static net.sweng.domain.OptionView.*;

/**
 * Created on 2/12/17.
 */
@ManagedBean
@SessionScoped
public class MenuController extends AbstractView {

    private boolean uploadFileEnable;
    private boolean uploadDBEnable;
    private boolean dataTypeEnable;

    public void uploadFromFile(ActionEvent event) {
        enableView(UPLOAD_FILE);
    }

    public void uploadFromDatabase(ActionEvent event) {
        enableView(CONNECT_DATABASE);
    }

    public void setDataType(ActionEvent event) {
        if(!getSessionAttribute(FILE_AVAILABLE, Boolean.class)) {
            addMessage(bundle.getString("err_unable_execute"), bundle.getString("adv_upload_first"), FacesMessage.SEVERITY_FATAL);
        } else {
            enableView(DATA_TYPE);
        }
    }

    public void setRelations() {
        addMessage("Unable to execute action", "Feature not available", FacesMessage.SEVERITY_ERROR);
    }

    public void generate() {
        addMessage("Unable to execute action", "Feature not available", FacesMessage.SEVERITY_ERROR);
    }

    public void export() {
        addMessage("Unable to execute action", "Feature not available", FacesMessage.SEVERITY_ERROR);
    }

    public boolean isUploadFileEnable() {
        return uploadFileEnable;
    }

    public boolean isUploadDBEnable() {
        return uploadDBEnable;
    }

    public boolean isDataTypeEnable() {
        return dataTypeEnable;
    }

    private void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void enableView(OptionView view) {
        this.uploadFileEnable = false;
        this.uploadDBEnable = false;
        this.dataTypeEnable = false;
        switch (view) {
            case UPLOAD_FILE:
                this.uploadFileEnable = true;
                break;
            case CONNECT_DATABASE:
                this.uploadDBEnable = true;
                break;
            case DATA_TYPE:
                this.dataTypeEnable = true;
                break;
            case RELATIONS:
                this.dataTypeEnable = true;
                break;
            case GENERATE:
                this.dataTypeEnable = true;
                break;
            case EXPORT:
                this.dataTypeEnable = true;
                break;
        }
    }

}
