package net.sweng.controller;

import net.sweng.domain.FilteredData;
import net.sweng.domain.GenericRow;
import net.sweng.domain.TableData;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.FacesEvent;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 3/23/17.
 */
@ManagedBean
@SessionScoped
public class ExportController extends AbstractTableController {

    private static final int MAX_REPORT_ROWS = 12;
    private static final String exportMenuId = "expMenu";
    private static final String exportLoopId = "expLoopTbl";
    private static final String exportInnerTableId = "expInnerTable";

    private TableData tableData;
    private String customExporter;
    private String reportTableName;
    private String reportFileName;
    private List<FilteredData> filteredDataList;
    private boolean multipleReportEnable;
    private int subReportIndex;

    @Override
    public void initialize() {
        createDynamicColumns();
    }

    @Override
    public void fillRecords(FacesEvent event) {
        setColumnKeys(tableData.getColumnNames());
        createDynamicColumns();
        setRegisters(tableData.getData());
        reportFileName = bundle.getString("menu_report");
    }

    public List<GenericRow> validate(List<GenericRow> rows) {
        if(rows != null) {
            return rows;
        } else {
            if(!multipleReportEnable) {
                multipleReportEnable = true;
                subReportIndex = 0;
            }
            return filteredDataList.get(subReportIndex++).getRows();
        }
    }

    public String obtainSubReportName(String filterName) {
        if(StringUtils.isNotBlank(filterName)) {
            multipleReportEnable = false;
            return filterName;
        }
        if(subReportIndex >= filteredDataList.size()) subReportIndex = 0;
        return filteredDataList.get(subReportIndex).getFilterName();
    }

    public String obtainInnerTableReferences() {
        StringBuilder sb = new StringBuilder();
        for(FilteredData data: filteredDataList) {
            String path = StringUtils.join(
                    Arrays.asList(exportMenuId, exportLoopId, filteredDataList.indexOf(data), exportInnerTableId), ":"
            );
            sb.append(path).append(",");
        }
        return sb.toString().substring(0,sb.length()-1);
    }

    public boolean paginationRequired() {
        return getRegisters().size() > MAX_REPORT_ROWS;
    }

    public void setTableData(TableData tableData) {
        this.tableData = tableData;
    }

    public String getCustomExporter() {
        return customExporter;
    }

    public void setCustomExporter(String customExporter) {
        this.customExporter = customExporter;
    }

    public String getReportTableName() {
        return reportTableName;
    }

    public void setReportTableName(String reportTableName) {
        this.reportTableName = reportTableName;
    }

    public String getReportFileName() {
        return StringUtils.isBlank(reportFileName) ? bundle.getString("menu_report") : reportFileName;
    }

    public void setReportFileName(String reportFileName) {
        this.reportFileName = reportFileName;
    }

    public List<FilteredData> getFilteredDataList() {
        return filteredDataList;
    }

    public void setFilteredDataList(List<FilteredData> filteredDataList) {
        this.filteredDataList = filteredDataList;
    }

    public String getExportMenuId() {
        return exportMenuId;
    }

    public String getExportLoopId() {
        return exportLoopId;
    }

    public String getExportInnerTableId() {
        return exportInnerTableId;
    }

}
