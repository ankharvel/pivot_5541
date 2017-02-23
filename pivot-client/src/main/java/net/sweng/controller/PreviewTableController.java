package net.sweng.controller;

import net.sweng.domain.TableData;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

import static net.sweng.config.ResourceHandler.getSessionResourcePath;

@ManagedBean
@SessionScoped
public class PreviewTableController extends AbstractTableController implements Serializable {

    @ManagedProperty(value = "#{uploadedTableController}")
    private UploadedTableController uploadedFilesTableController;

    private String activeTable;

    public void setUploadedFilesTableController(UploadedTableController uploadedFilesTableController) {
        this.uploadedFilesTableController = uploadedFilesTableController;
    }

    public void initialize() {
        createDynamicColumns();
    }

    public void fillRecords(ActionEvent event) {
        String fileName = (String) uploadedFilesTableController.getSelectedFile().get(bundle.getString("header_uploaded_files"));
        String s = getSessionResourcePath(fileName);
        TableData td = pivotController.readCSV(s);
        setColumnKeys(td.getColumnNames());
        createDynamicColumns();
        setRegisters(td.getData());
        activeTable = fileName;
    }

    public String getActiveTable() {
        return activeTable;
    }
}
