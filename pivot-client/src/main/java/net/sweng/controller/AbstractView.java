package net.sweng.controller;

import java.util.ResourceBundle;

/**
 * Date on 2/23/17.
 */
public abstract class AbstractView {

    protected ResourceBundle bundle;

    public AbstractView() {
        this.bundle = ResourceBundle.getBundle("messages");
    }
}
