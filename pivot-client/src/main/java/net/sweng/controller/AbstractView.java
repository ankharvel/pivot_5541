package net.sweng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;

/**
 * Date on 2/23/17.
 */
public abstract class AbstractView {

    protected ResourceBundle bundle;

    @Autowired
    protected PivotController pivotController;

    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
    }

    public AbstractView() {
        this.bundle = ResourceBundle.getBundle("messages");
    }

    protected void addErrorMessage(String message) {
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_ERROR,
                bundle.getString("err_unable_execute"),
                message
        );
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
