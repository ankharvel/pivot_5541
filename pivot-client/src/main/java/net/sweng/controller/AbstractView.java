package net.sweng.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;

/**
 * Date on 2/23/17.
 */
public abstract class AbstractView {

    protected ResourceBundle bundle;

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
