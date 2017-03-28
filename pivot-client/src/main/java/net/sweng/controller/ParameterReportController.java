package net.sweng.controller;

import net.sweng.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.event.DragDropEvent;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.text.MessageFormat;
import java.util.*;

import static net.sweng.config.HttpSessionHandler.getSessionAttribute;
import static net.sweng.config.SessionKeys.TABLE_SCHEMA_CATALOGUE;

/**
 * Date on 2/25/17.
 */
@ManagedBean
@SessionScoped
public class ParameterReportController extends AbstractView {

    private static final int MAX_RECORDS = 8;

    private List<GenericRow> columnSource;
    private List<GenericRow> reportRows;
    private List<GenericRow> reportColumns;
    private List<GenericRow> reportFilters;
    private List<GenericRow> reportField;

    private List<ReportParameters> parametersList;
    private ReportParameters selectedParameters;
    private boolean enableDragMenu;
    private String currentFileName;
    private String aggregationName;
    private String customStyle;
    private boolean fromFile;
    private int editIndex = -1;

    @PostConstruct
    private void init(){
        customStyle = bundle.getString("style_default");
        parametersList = new LinkedList<>();
    }

    public void initialize(SourceDetail sourceDetail) {
        if(StringUtils.isBlank(sourceDetail.getSourceName())) {
            enableDragMenu = false;
            currentFileName = null;
            return;
        }
        currentFileName = sourceDetail.getSourceName();

        if(sourceDetail.getSourceType().equals(SourceDetail.SourceType.DATABASE)) {
            this.columnSource = convertToGenericRow(pivotController.readTableHeaders(currentFileName));
            this.fromFile = false;
        } else {
            TableCatalogue catalogue = getSessionAttribute(TABLE_SCHEMA_CATALOGUE, TableCatalogue.class);
            this.columnSource = new ArrayList<>(catalogue.get(currentFileName));
            this.fromFile = true;
        }
        this.reportRows = new LinkedList<>();
        this.reportColumns = new LinkedList<>();
        this.reportFilters = new LinkedList<>();
        this.reportField = new LinkedList<>();
        this.columnSource.sort(new GenericRowComparator());

        customStyle = columnSource.size() > MAX_RECORDS ? bundle.getString("style_scroll") : bundle.getString("style_default");
        enableDragMenu = true;
    }

    private List<GenericRow> convertToGenericRow(List<String> headers) {
        List<GenericRow> records = new ArrayList<>();
        for(String h: headers) {
            GenericRow record = new GenericRow();
            record.put(bundle.getString("header_column"), h);
            record.put(bundle.getString("header_type"), DataType.STRING);
            records.add(record);
        }
        return records;
    }

    public void removeRow(GenericRow detail) {
        reportRows.remove(detail);
        columnSource.add(detail);
        columnSource.sort(new GenericRowComparator());
    }

    public void removeColumn(GenericRow detail) {
        reportColumns.remove(detail);
        columnSource.add(detail);
        columnSource.sort(new GenericRowComparator());
    }

    public void removeFilter(GenericRow detail) {
        reportFilters.remove(detail);
        columnSource.add(detail);
        columnSource.sort(new GenericRowComparator());
    }

    public void removeField(GenericRow detail) {
        reportField.remove(detail);
        columnSource.add(detail);
        columnSource.sort(new GenericRowComparator());
    }

    public void addParameters() {
        ReportParameters parameters = new ReportParameters();
        parameters.setReportRows(toDetails(reportRows));
        parameters.setReportColumns(toDetails(reportColumns));
        parameters.setReportFilter(toDetails(reportFilters));
        parameters.setField(toDetails(reportField).iterator().next());
        parameters.setAggregationType(AggregationType.valueOf(aggregationName));
        parameters.setFileName(currentFileName);
        selectedParameters = parameters;
        if(editIndex >= 0) {
            parametersList.remove(editIndex);
            parametersList.add(editIndex, parameters);
        } else {
            parametersList.add(parameters);
        }
    }

    public ReportParameters generateParameters() {
        return selectedParameters;
    }

    public String obtainLabel(ReportParameters parameters) {
        return String.valueOf(parametersList.indexOf(parameters) + 1).concat(". ").concat(parameters.getFileName());
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
        columnSource.addAll(reportFilters);
        reportFilters.clear();
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
        editIndex = -1;
        aggregationName = "";
        SourceDetail value = (SourceDetail) event.getNewValue();
        initialize(value);
    }

    public void loadParameters() {
        currentFileName = selectedParameters.getFileName();
        initialize(new SourceDetail(currentFileName, obtainType(currentFileName)));
        updateColumnParameters(selectedParameters.getReportRows(), reportRows);
        updateColumnParameters(selectedParameters.getReportColumns(), reportColumns);
        updateColumnParameters(selectedParameters.getReportFilter(), reportFilters);
        updateColumnParameters(Collections.singletonList(selectedParameters.getField()), reportField);
        aggregationName = selectedParameters.getAggregationType().name();
        editIndex = parametersList.indexOf(selectedParameters);
    }

    private SourceDetail.SourceType obtainType(String currentFileName) {
        for(SourceDetail.SourceType type: SourceDetail.SourceType.values()) {
            if(currentFileName.toLowerCase().endsWith("." + type.name().toLowerCase())) {
                return type;
            }
        }
        return SourceDetail.SourceType.DATABASE;
    }

    private void updateColumnParameters(List<ColumnDetail> source, List<GenericRow> toUpdate) {
        source.stream().forEachOrdered(data -> {
            GenericRow row = columnSource.stream().filter(r ->
                    r.get(bundle.getString("header_column")).toString().equalsIgnoreCase(data.getColumnName())).findFirst().get();
            toUpdate.add(row);
            columnSource.remove(row);
        });
    }

    public void clearCurrentFile() {
        currentFileName = "";
        aggregationName = "";
    }

    public String getFormattedFilter() {
        String name = selectedParameters.getReportFilter().iterator().next().getColumnName();
        return name.charAt(0) + name.toLowerCase().substring(1) + ": ";
    }

    public String formatDroppableValue(String value) {
        if (value.length() <= 22) {
            return value;
        }
        return value.substring(0, 20) + "..";
    }

    private class GenericRowComparator implements Comparator<GenericRow> {
        @Override
        public int compare(GenericRow o1, GenericRow o2) {
            return String.valueOf(o1.get(bundle.getString("header_column"))).compareTo(
                    String.valueOf(o2.get(bundle.getString("header_column"))));
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

    public List<ReportParameters> getParametersList() {
        return parametersList;
    }

    public String getCurrentFileName() {
        return currentFileName;
    }

    public String getAggregationName() {
        return aggregationName;
    }

    public void setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
    }

    public String getCustomStyle() {
        return customStyle;
    }

    public boolean isEnableDragMenu() {
        return enableDragMenu;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public boolean isFromFile() {
        return fromFile;
    }

    public ReportParameters getSelectedParameters() {
        return selectedParameters;
    }

    public void setSelectedParameters(ReportParameters selectedParameters) {
        this.selectedParameters = selectedParameters;
    }

    public void setSelectedParameters(Object o) {
        try {
            this.selectedParameters = parametersList.stream().filter(
                    p -> p.toString().equalsIgnoreCase(o.toString())).findFirst().get();
        } catch (Exception ex) {
            addErrorMessage(MessageFormat.format(bundle.getString("err_casting_error"), ReportParameters.class.getSimpleName()));
        }
    }

}
