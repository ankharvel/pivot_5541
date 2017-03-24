package net.sweng.controller;

import net.sweng.config.DBConfig;
import net.sweng.dao.PivotDao;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date on 2/13/17.
 */
@Controller
public class PivotController {

    @Autowired
    private PivotDao pivotDao;

    public TableData readCSV(String fileName) {
        try {
            return pivotDao.getRecordsFromCsv(getSourcePath(fileName));
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public String[] readCSVHeaders(String fileName) {
        try {
            return pivotDao.getHeadersFromCsv(getSourcePath(fileName));
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return new String[0];
        }
    }

    public TableData generateReportFromCSV(ReportParameters parameters) throws InvalidDataTypeException {
        return pivotDao.getReportFromCsv(parameters, getSourcePath(parameters.getFileName()));
    }

    public Map<String, TableData> generateReportWithAllFilters(ReportParameters parameters, List<String> filterValues) throws InvalidDataTypeException {
        return pivotDao.getReportFromCsvWithAllFilters(parameters, filterValues);
    }

    private String getSourcePath(String fileName) {
        return new File(DBConfig.getTempFolderPath(), fileName).getPath();
    }

}
