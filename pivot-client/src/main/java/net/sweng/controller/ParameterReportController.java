package net.sweng.controller;

import net.sweng.domain.*;
import org.primefaces.event.DragDropEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static net.sweng.config.HttpSessionHandler.getSessionAttribute;
import static net.sweng.config.SessionKeys.TABLE_SCHEMA_CATALOGUE;

/**
 * Date on 2/25/17.
 */
@ManagedBean
@SessionScoped
public class ParameterReportController extends AbstractView {

    private List<GenericRow> columnSource;
    private List<GenericRow> reportRows;
    private List<GenericRow> reportColumns;
    private List<GenericRow> reportFilters;
    private List<GenericRow> reportField;

    private boolean enableDragMenu;
    private String currentFileName;
    private AggregationType aggregationType;

    public void initialize(String fileName) {
        if(fileName == null || fileName.isEmpty()) {
            enableDragMenu = false;
            currentFileName = null;
            return;
        }
        currentFileName = fileName;
        TableCatalogue catalogue = getSessionAttribute(TABLE_SCHEMA_CATALOGUE, TableCatalogue.class);

        this.columnSource = new ArrayList<>(catalogue.get(fileName));
        this.reportRows = new LinkedList<>();
        this.reportColumns = new LinkedList<>();
        this.reportFilters = new LinkedList<>();
        this.reportField = new LinkedList<>();
        enableDragMenu = true;
    }

    public void removeRow(GenericRow detail) {
        reportRows.remove(detail);
        columnSource.add(detail);
    }

    public void removeColumn(GenericRow detail) {
        reportColumns.remove(detail);
        columnSource.add(detail);
    }

    public void removeFilter(GenericRow detail) {
        reportFilters.remove(detail);
        columnSource.add(detail);
    }

    public void removeField(GenericRow detail) {
        reportField.remove(detail);
        columnSource.add(detail);
    }

    public ReportParameters generateParameters() {
        ReportParameters parameters = new ReportParameters();
        parameters.setReportRows(toDetails(reportRows));
        parameters.setReportColumns(toDetails(reportColumns));
        parameters.setReportFilter(toDetails(reportFilters));
        parameters.setField(toDetails(reportField).iterator().next());
        parameters.setAggregationType(aggregationType);
        parameters.setFileName(currentFileName);
        return parameters;
    }

    private List<ColumnDetail> toDetails(List<GenericRow> rows) {
        List<ColumnDetail> details = new LinkedList<>();
        for(GenericRow row: rows) {
            ColumnDetail detail = new ColumnDetail(
                    row.get(bundle.getString("header_column")).toString(),
                    DataType.valueOf(row.get(bundle.getString("header_type")).toString())
            );
            details.add(detail);
        }
        return details;
    }

//-------------------- EVENTS -------------------------------

    public void onSourceToRow(DragDropEvent ddEvent) {
        GenericRow dragElement = ((GenericRow) ddEvent.getData());
        reportRows.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onSourceToColumn(DragDropEvent ddEvent) {
        columnSource.addAll(reportColumns);
        reportColumns.clear();
        GenericRow dragElement = ((GenericRow) ddEvent.getData());
        reportColumns.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onSourceToFilter(DragDropEvent ddEvent) {
        GenericRow dragElement = ((GenericRow) ddEvent.getData());
        reportFilters.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onSourceToField(DragDropEvent ddEvent) {
        columnSource.addAll(reportField);
        reportField.clear();
        GenericRow dragElement = ((GenericRow) ddEvent.getData());
        reportField.add(dragElement);
        columnSource.remove(dragElement);
    }

    public void onMenuChange(ValueChangeEvent event) {
        String value = (String) event.getNewValue();
        initialize(value);
    }

    public void onAggregationChange(ValueChangeEvent event) {
        if(event.getNewValue() == null || ((String) event.getNewValue()).isEmpty()) {
            aggregationType = null;
        } else {
            String value = (String) event.getNewValue();
            aggregationType = AggregationType.valueOf(value);
        }

    }

//---------------------------  GETTERS ---------------------------------------

    public List<GenericRow> getColumnSource() {
        return columnSource;
    }

    public List<GenericRow> getReportRows() {
        return reportRows;
    }

    public List<GenericRow> getReportColumns() {
        return reportColumns;
    }

    public List<GenericRow> getReportFilters() {
        return reportFilters;
    }

    public List<GenericRow> getReportField() {
        return reportField;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public boolean isEnableDragMenu() {
        return enableDragMenu;
    }
}
