package net.sweng.controller;

import net.sweng.domain.DataType;
import net.sweng.domain.GenericRow;
import net.sweng.domain.TableCatalogue;
import net.sweng.domain.TableSchema;
import org.primefaces.event.CellEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.sweng.config.HttpSessionHandler.putSessionAttribute;
import static net.sweng.config.ResourceHandler.getSessionResourcePath;
import static net.sweng.config.SessionKeys.SCHEMA_AVAILABLE;
import static net.sweng.config.SessionKeys.TABLE_SCHEMA_CATALOGUE;

@ManagedBean
@SessionScoped
public class ColumnTypeTableController extends AbstractTableController implements Serializable {

    @ManagedProperty(value = "#{uploadedTableController}")
    private UploadedTableController uploadedFilesTableController;

    private String activeTable;

    private TableCatalogue catalogue;

    public void initialize() {
        catalogue = new TableCatalogue(this::obtainData);
        putSessionAttribute(TABLE_SCHEMA_CATALOGUE, catalogue);
        putSessionAttribute(SCHEMA_AVAILABLE, true);
        setColumnKeys(new String[]{bundle.getString("header_column"), bundle.getString("header_type")});
        createDynamicColumns();
    }

    public void fillRecords(ActionEvent event) {
        String fileName = (String) uploadedFilesTableController.getSelectedFile().get(bundle.getString("header_uploaded_files"));
        setRegisters(catalogue.get(fileName));
        activeTable = fileName;
    }

    private List<GenericRow> obtainData(String fileName) {
        String s = getSessionResourcePath(fileName);
        String[] headers = pivotController.readCSVHeaders(s);
        TableSchema tableSchema = new TableSchema();
        List<GenericRow> data = new ArrayList<>();
        for(String h: headers) {
            GenericRow record = new GenericRow();
            record.put(bundle.getString("header_column"), h);
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

//---------------------------- SETTERS ------------------------------------------------

    /**
     * All the setters are required to autowired the properties
     */

    public void setUploadedFilesTableController(UploadedTableController uploadedFilesTableController) {
        this.uploadedFilesTableController = uploadedFilesTableController;
    }

}
