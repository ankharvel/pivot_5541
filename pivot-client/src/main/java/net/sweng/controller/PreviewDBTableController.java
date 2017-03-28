package net.sweng.controller;

import net.sweng.domain.TableData;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.FacesEvent;

@ManagedBean
@SessionScoped
public class PreviewDBTableController extends AbstractTableController {

    @ManagedProperty(value = "#{databaseController}")
    private DatabaseController databaseController;

    private String activeTable;

    public void initialize() {
        createDynamicColumns();
    }

    public void fillRecords(FacesEvent event) {
        String fileName = (String) databaseController.getSelectedTable().get(bundle.getString("header_tables"));
        TableData td = pivotController.readTable(fileName);
        setColumnKeys(td.getColumnNames());
        createDynamicColumns();
        setRegisters(td.getData());
        activeTable = fileName;
    }

    public String getActiveTable() {
        return activeTable;
    }

    public void setDatabaseController(DatabaseController databaseController) {
        this.databaseController = databaseController;
    }
}
