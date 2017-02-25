package net.sweng.controller;

import net.sweng.domain.ColumnDetail;
import net.sweng.domain.TableCatalogue;
import org.primefaces.event.DragDropEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.util.HashSet;
import java.util.Set;

import static net.sweng.config.HttpSessionHandler.getSessionAttribute;
import static net.sweng.config.SessionKeys.TABLE_SCHEMA_CATALOGUE;

/**
 * Date on 2/25/17.
 */
@ManagedBean
@SessionScoped
public class GenerateReportController extends AbstractView {

    private Set<ColumnDetail> columnSource;
    private Set<ColumnDetail> reportRows;
    private Set<ColumnDetail> reportColumns;
    private Set<ColumnDetail> reportFilters;
    private Set<ColumnDetail> reportField;

    private boolean enableDragMenu;

    public void initialize(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            enableDragMenu = false;
            return;
        }
        TableCatalogue catalogue = getSessionAttribute(TABLE_SCHEMA_CATALOGUE, TableCatalogue.class);

        this.columnSource = new HashSet<>(catalogue.getColumnDetails(fileName));
        this.reportRows = new HashSet<>();
        this.reportColumns = new HashSet<>();
        this.reportFilters = new HashSet<>();
        this.reportField = new HashSet<>();
        enableDragMenu = true;
    }

    public void removeRow(ColumnDetail detail) {
        reportRows.remove(detail);
        columnSource.add(detail);
    }

    public void removeColumn(ColumnDetail detail) {
        reportColumns.remove(detail);
        columnSource.add(detail);
    }

    public void removeFilter(ColumnDetail detail) {
        reportFilters.remove(detail);
        columnSource.add(detail);
    }

    public void removeField(ColumnDetail detail) {
        reportField.remove(detail);
        columnSource.add(detail);
    }

//-------------------- EVENTS -------------------------------

    public void onSourceToRow(DragDropEvent ddEvent) {
        ColumnDetail dragElement = ((ColumnDetail) ddEvent.getData());
        reportRows.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onSourceToColumn(DragDropEvent ddEvent) {
        ColumnDetail dragElement = ((ColumnDetail) ddEvent.getData());
        reportColumns.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onSourceToFilter(DragDropEvent ddEvent) {
        ColumnDetail dragElement = ((ColumnDetail) ddEvent.getData());
        reportFilters.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onSourceToField(DragDropEvent ddEvent) {
        columnSource.addAll(reportField);
        reportField.clear();
        ColumnDetail dragElement = ((ColumnDetail) ddEvent.getData());
        reportField.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onMenuChange(ValueChangeEvent event) {
        String value = (String) event.getNewValue();
        initialize(value);
    }

//---------------------------  GETTERS ---------------------------------------

    public Set<ColumnDetail> getColumnSource() {
        return columnSource;
    }

    public Set<ColumnDetail> getReportRows() {
        return reportRows;
    }

    public Set<ColumnDetail> getReportColumns() {
        return reportColumns;
    }

    public Set<ColumnDetail> getReportFilters() {
        return reportFilters;
    }

    public Set<ColumnDetail> getReportField() {
        return reportField;
    }

    public boolean isEnableDragMenu() {
        return enableDragMenu;
    }
}
