package net.sweng.controller;

import net.sweng.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.CellEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.sweng.config.HttpSessionHandler.putSessionAttribute;
import static net.sweng.config.SessionKeys.SCHEMA_AVAILABLE;
import static net.sweng.config.SessionKeys.TABLE_SCHEMA_CATALOGUE;

@ManagedBean
@SessionScoped
public class ColumnTypeTableController extends AbstractTableController implements Serializable {

    private String activeTable;
    private boolean enableView;

    private TableCatalogue catalogue;

    public void initialize() {
        catalogue = new TableCatalogue(this::obtainData);
        putSessionAttribute(TABLE_SCHEMA_CATALOGUE, catalogue);
        putSessionAttribute(SCHEMA_AVAILABLE, true);
        setColumnKeys(new String[]{bundle.getString("header_column"), bundle.getString("header_type")});
        createDynamicColumns();
    }

    public void fillRecords(FacesEvent event) {
        SourceDetail detail = (SourceDetail) ((ValueChangeEvent)event).getNewValue();
        if(StringUtils.isBlank(detail.getSourceName()) || detail.getSourceType().equals(SourceDetail.SourceType.DATABASE)) {
            enableView = false;
            return;
        }
        fillRecords(detail.getSourceName());
    }

    public void fillRecords(String fileName) {
        setRegisters(catalogue.get(fileName));
        activeTable = fileName;
        enableView = true;
    }

    private List<GenericRow> obtainData(String fileName) {
        List<String> headers = pivotController.readCSVHeaders(fileName);
        TableSchema tableSchema = new TableSchema();
        List<GenericRow> data = new ArrayList<>();
        for(String h: headers) {
            GenericRow record = new GenericRow();
            record.put(bundle.getString("header_column"), h);
            record.put(bundle.getString("header_column_alias"), h);
            record.put(bundle.getString("header_type"), tableSchema.getColumnType(h));
            data.add(record);
        }
        return data;
    }

    public String getActiveTable() {
        return activeTable;
    }

    public List<DataType> getTypes() {
        return Arrays.asList(DataType.values());
    }

    public String getLabel(DataType type) {
        return bundle.getString(type.getLbl());
    }

    public boolean isEnableView() {
        return enableView;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    bundle.getString("info_cell_updated"),
                    MessageFormat.format(
                            bundle.getString("info_change_values"),
                            getLabel(DataType.valueOf(oldValue.toString())),
                            getLabel(DataType.valueOf(newValue.toString()))
                    ));
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

}
