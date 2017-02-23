package net.sweng.controller;

import net.sweng.domain.ColumnModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.jsf.FacesContextUtils;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Date on 2/14/17.
 */
public abstract class AbstractTableController extends AbstractView {

    private List<Map<String, Object>> registers = new ArrayList<>();

    private List<ColumnModel> columns = new ArrayList<>();
    private String[] columnKeys = new String[0];

    @Autowired
    protected PivotController pivotController;

    @PostConstruct
    private void init() {
        FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext.getCurrentInstance())
                .getAutowireCapableBeanFactory().autowireBean(this);
        initialize();
    }

    public abstract void initialize();

    public abstract void fillRecords(ActionEvent event);

    public List<ColumnModel> getColumns() {
        return columns;
    }

    public List<Map<String, Object>> getRegisters() {
        return registers;
    }

    public void setRegisters(List<Map<String, Object>> registers) {
        this.registers = registers;
    }

    public void setColumnKeys(String[] columnKeys) {
            this.columnKeys = columnKeys;
    }

    protected void createDynamicColumns() {
        columns.clear();
        for (String columnKey : columnKeys) {
            columns.add(new ColumnModel(columnKey.substring(0,1).toUpperCase().concat(columnKey.substring(1).toLowerCase()), columnKey));
        }
    }

}
