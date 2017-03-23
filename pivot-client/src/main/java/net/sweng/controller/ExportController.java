package net.sweng.controller;

import net.sweng.domain.TableData;
import org.apache.commons.lang3.StringUtils;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.FacesEvent;

/**
 * Created on 3/23/17.
 */
@ManagedBean
@SessionScoped
public class ExportController extends AbstractTableController {

    private TableData tableData;
    private String customExporter;
    private String reportTableName;
    private String reportFileName;

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
}
