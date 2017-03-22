package net.sweng.controller;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.TabChangeEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;

/**
 * Created on 3/21/17.
 */
@ManagedBean
@SessionScoped
public class MainPanelController extends AbstractView {

    private int accordionIndex = -1;

    @ManagedProperty(value = "#{parameterReportController}")
    private ParameterReportController parameterReportController;

    @ManagedProperty(value = "#{columnTypeTableController}")
    private ColumnTypeTableController columnTypeTableController;

    public void onMenuChange(ValueChangeEvent event) {
        String fileName = (String) event.getNewValue();
        if(StringUtils.isBlank(fileName)) accordionIndex = -1;
        parameterReportController.onMenuChange(event);
        columnTypeTableController.fillRecords(event);
    }

    public void onTabChange(TabChangeEvent event) {
        accordionIndex = event.getComponent().getChildren().indexOf(event.getTab());
    }

    public void loadCurrentParameters() {
        accordionIndex = 0;
        parameterReportController.loadParameters();
        columnTypeTableController.fillRecords(parameterReportController.getCurrentFileName());
    }

    public void clearParameters() {
        accordionIndex = 0;
        parameterReportController.clearCurrentFile();
    }

    public int getAccordionIndex() {
        return accordionIndex;
    }

    public void setAccordionIndex(int accordionIndex) {
        this.accordionIndex = accordionIndex;
    }

    //---------------------------- SETTERS ------------------------------------------------

    /**
     * All the setters are required to autowired the properties
     */

    public void setParameterReportController(ParameterReportController parameterReportController) {
        this.parameterReportController = parameterReportController;
    }

    public void setColumnTypeTableController(ColumnTypeTableController columnTypeTableController) {
        this.columnTypeTableController = columnTypeTableController;
    }
}
