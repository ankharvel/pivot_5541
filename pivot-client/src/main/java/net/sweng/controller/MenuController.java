package net.sweng.controller;

import net.sweng.domain.OptionView;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import static net.sweng.config.HttpSessionHandler.getSessionAttribute;
import static net.sweng.config.SessionKeys.FILE_AVAILABLE;
import static net.sweng.config.SessionKeys.SCHEMA_AVAILABLE;
import static net.sweng.domain.OptionView.*;

/**
 * Created on 2/12/17.
 */
@ManagedBean
@SessionScoped
public class MenuController extends AbstractView {

    private boolean uploadFileEnable;
    private boolean uploadDBEnable;
    private boolean schemaEnable;
    private boolean relationEnable;
    private boolean generateEnable;
    private boolean exportEnable;

    private String iconClass;
    private String menuTitle;

    public void uploadFromFile(ActionEvent event) {
        menuTitle = bundle.getString("subtitle_upload_files");
        iconClass = "fa fa-file-text";
        enableView(UPLOAD_FILE);
    }

    public void uploadFromDatabase(ActionEvent event) {
        menuTitle = bundle.getString("subtitle_connect_db");
        iconClass = "fa fa-database";
        enableView(CONNECT_DATABASE);
        addMessage(bundle.getString("info_not_available"), bundle.getString("info_under_construction"), FacesMessage.SEVERITY_ERROR);
    }

    public void setSchema(ActionEvent event) {
        if(getSessionAttribute(FILE_AVAILABLE, Boolean.class)) {
            menuTitle = bundle.getString("subtitle_schema");
            iconClass = "fa fa-expand";
            enableView(SCHEMA);
        } else {
            addMessage(bundle.getString("err_unable_execute"), bundle.getString("adv_upload_first"), FacesMessage.SEVERITY_FATAL);
        }
    }

    public void setRelations(ActionEvent event) {
        menuTitle = bundle.getString("subtitle_relations");
        iconClass = "fa fa-group";
        enableView(RELATIONS);
        addMessage(bundle.getString("info_not_available"), bundle.getString("info_under_construction"), FacesMessage.SEVERITY_ERROR);
    }

    public void generate(ActionEvent event) {
        if(getSessionAttribute(SCHEMA_AVAILABLE, Boolean.class)) {
            menuTitle = bundle.getString("subtitle_generate");
            iconClass = "fa fa-cubes";
            enableView(GENERATE);
        } else {
            addMessage(bundle.getString("err_unable_execute"), bundle.getString("adv_set_schema"), FacesMessage.SEVERITY_FATAL);
        }
    }

    public void export(ActionEvent event) {
        menuTitle = bundle.getString("subtitle_export");
        iconClass = "fa fa-download";
        enableView(EXPORT);
    }

    public boolean isUploadFileEnable() {
        return uploadFileEnable;
    }

    public boolean isUploadDBEnable() {
        return uploadDBEnable;
    }

    public boolean isSchemaEnable() {
        return schemaEnable;
    }

    public boolean isRelationEnable() {
        return relationEnable;
    }

    public boolean isGenerateEnable() {
        return generateEnable;
    }

    public boolean isExportEnable() {
        return exportEnable;
    }

    public String getIconClass() {
        return iconClass;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    private void addMessage(String summary, String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private void enableView(OptionView view) {
        this.uploadFileEnable = false;
        this.uploadDBEnable = false;
        this.schemaEnable = false;
        this.relationEnable = false;
        this.generateEnable = false;
        this.exportEnable = false;
        switch (view) {
            case UPLOAD_FILE:
                this.uploadFileEnable = true;
                break;
            case CONNECT_DATABASE:
                this.uploadDBEnable = true;
                break;
            case SCHEMA:
                this.schemaEnable = true;
                break;
            case RELATIONS:
                this.relationEnable = true;
                break;
            case GENERATE:
                this.generateEnable = true;
                break;
            case EXPORT:
                this.exportEnable = true;
                break;
        }
    }

}
