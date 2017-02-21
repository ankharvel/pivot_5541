package net.sweng.controller;

import net.sweng.domain.TableData;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

import static net.sweng.config.ResourceHandler.getSessionResourcePath;

@ManagedBean
@ViewScoped
public class PreviewTableController extends AbstractTableController implements Serializable {

    @ManagedProperty(value = "#{uploadedTableController}")
    private UploadedTableController uploadedFilesTableController;

    @ManagedProperty(value = "#{msg.uploaded_files}")
    private String columnName;

    private boolean activeTable;

    public void setUploadedFilesTableController(UploadedTableController uploadedFilesTableController) {
        this.uploadedFilesTableController = uploadedFilesTableController;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public void initialize() {
        createDynamicColumns();
    }

    public void fillRecords(ActionEvent event) {
        String fileName = (String) uploadedFilesTableController.getSelectedFile().get(columnName);
        String s = getSessionResourcePath(fileName);
        TableData td = pivotController.generate(s);
        setColumnKeys(td.getColumnNames());
        createDynamicColumns();
        setRegisters(td.getData());
        activeTable = true;
    }

    public boolean isActiveTable() {
        return activeTable;
    }

}
