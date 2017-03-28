package net.sweng.controller;

import net.sweng.domain.SourceDetail;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.TabChangeEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @ManagedProperty(value = "#{uploadedTableController}")
    private UploadedTableController uploadedTableController;

    @ManagedProperty(value = "#{databaseController}")
    private DatabaseController databaseController;

    private SourceDetail selectedSource;
    private List<SourceDetail> sourceDetailList;

    @PostConstruct
    public void init() {
        sourceDetailList = new ArrayList<>();
        SourceDetail emptyDetail = new SourceDetail("", null);
        sourceDetailList.add(emptyDetail);
        selectedSource = emptyDetail;
    }

    public List<SourceDetail> obtainCurrentSources() {
        List<SourceDetail> details = new ArrayList<>();
        try {
            details.add(new SourceDetail("", null));
            uploadedTableController.getUploadedFiles().stream().forEach(f -> details.add(
                    new SourceDetail(f, SourceDetail.SourceType.valueOf(
                            f.substring(f.indexOf(".") + 1).toUpperCase()
                    ))
            ));
            databaseController.databaseTables().stream().forEach(t -> details.add(
                    new SourceDetail(t, SourceDetail.SourceType.DATABASE)
            ));
            Collections.sort(details, (o1, o2) -> o1.toString().compareTo(o2.toString()));
            sourceDetailList = details;
        } catch (Exception ex) {
            addErrorMessage("Invalid source file name");
        }
        return details;
    }

    public void onMenuChange(ValueChangeEvent event) {
        SourceDetail value = (SourceDetail) event.getNewValue();
        if(StringUtils.isBlank(value.getSourceName())) accordionIndex = -1;
        parameterReportController.onMenuChange(event);
        columnTypeTableController.fillRecords(event);
    }

    public void onTabChange(TabChangeEvent event) {
        accordionIndex = event.getComponent().getChildren().indexOf(event.getTab());
    }

    public void loadCurrentParameters() {
        accordionIndex = 0;
        parameterReportController.loadParameters();
        if(parameterReportController.isFromFile()) {
            columnTypeTableController.fillRecords(parameterReportController.getCurrentFileName());
        }
    }

    public void clearParameters() {
        accordionIndex = 0;
        parameterReportController.clearCurrentFile();
        selectedSource = sourceDetailList.get(0);
    }

    public int getAccordionIndex() {
        return accordionIndex;
    }

    public void setAccordionIndex(int accordionIndex) {
        this.accordionIndex = accordionIndex;
    }

    public SourceDetail getSelectedSource() {
        return selectedSource;
    }

    public List<SourceDetail> getSourceDetailList() {
        return sourceDetailList;
    }

    public void setSelectedSource(SourceDetail selectedSource) {
        this.selectedSource = selectedSource;
    }

    public boolean isColumnTypeEnabled() {
        return selectedSource.getSourceType() != null && !selectedSource.getSourceType().equals(SourceDetail.SourceType.DATABASE);
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

    public void setUploadedTableController(UploadedTableController uploadedTableController) {
        this.uploadedTableController = uploadedTableController;
    }

    public void setDatabaseController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }
}
