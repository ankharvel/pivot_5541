package net.sweng.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Date on 2/25/17.
 */
public class ReportParameters implements Serializable {

    private AggregationType aggregationType;
    private List<ColumnDetail> reportRows;
    private List<ColumnDetail> reportColumns;
    private List<ColumnDetail> reportFilter;
    private ColumnDetail field;
    private String fileName;

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public List<ColumnDetail> getReportRows() {
        return reportRows;
    }

    public void setReportRows(List<ColumnDetail> reportRows) {
        this.reportRows = reportRows;
    }

    public List<ColumnDetail> getReportColumns() {
        return reportColumns;
    }

    public void setReportColumns(List<ColumnDetail> reportColumns) {
        this.reportColumns = reportColumns;
    }

    public List<ColumnDetail> getReportFilter() {
        return reportFilter;
    }

    public void setReportFilter(List<ColumnDetail> reportFilter) {
        this.reportFilter = reportFilter;
    }

    public ColumnDetail getField() {
        return field;
    }

    public void setField(ColumnDetail field) {
        this.field = field;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
