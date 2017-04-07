package net.sweng.controller;

import net.sweng.config.DBConfig;
import net.sweng.dao.DaoFactory;
import net.sweng.dao.H2PivotDao;
import net.sweng.dao.PivotDao;
import net.sweng.domain.DatabaseParameters;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import net.sweng.domain.exceptions.InvalidDatabaseConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date on 2/13/17.
 */
@Controller
public class PivotController {

    @Resource(name = "h2PivotDao")
    private PivotDao pivotDao;

    @Autowired
    private DaoFactory daoFactory;

    public TableData readCSV(String fileName) {
        try {
            return pivotDao.getRecords(getSourcePath(fileName));
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public TableData readTable(String tableNaeme) {
        try {
            return daoFactory.getDao().getRecords(tableNaeme);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }


    public List<String> readCSVHeaders(String fileName) {
        try {
            return pivotDao.getHeaders(getSourcePath(fileName));
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public List<String> readTableHeaders(String tableName) {
        try {
            return daoFactory.getDao().getHeaders(tableName);
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public TableData generateReportFromCSV(ReportParameters parameters) throws InvalidDataTypeException {
        return pivotDao.getReport(parameters, getSourcePath(parameters.getFileName()));
    }

    public TableData generateReportFromTable(ReportParameters parameters) throws InvalidDataTypeException {
        return daoFactory.getDao().getReport(parameters, parameters.getFileName());
    }

    public Map<String, TableData> generateReportWithFiltersFromCSV(ReportParameters parameters, List<String> filterValues) throws InvalidDataTypeException {
        return pivotDao.getReportWithFilters(parameters, filterValues);
    }

    public Map<String, TableData> generateReportWithFiltersFromTable(ReportParameters parameters, List<String> filterValues) throws InvalidDataTypeException {
        return daoFactory.getDao().getReportWithFilters(parameters, filterValues);
    }

    public List<String> obtainTableNames() {
        try {
            return daoFactory.getDao().getTableNames();
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, ex.getMessage(), ex);
            return new ArrayList<>();
        }
    }

    public void connectToDB(DatabaseParameters dbParameters) throws InvalidDatabaseConnection {
        try {
            daoFactory.createConnection(dbParameters);
        } catch (Exception ex) {
            String msg = "Invalid database credentials";
            Logger.getGlobal().log(Level.SEVERE, msg + ". ClientID: " +  DBConfig.getSessionIdPrefix());
            throw new InvalidDatabaseConnection(msg);
        }
    }

    public void destroySession() {
        Logger.getGlobal().log(Level.INFO, "Destroying session: " + DBConfig.getSessionIdPrefix());
        PivotDao dao = daoFactory.getDao();
        if(dao != null) {
            dao.closePool();
        }
        ((H2PivotDao) pivotDao).cleanSessionTables();
    }

    private String getSourcePath(String fileName) {
        return new File(DBConfig.getTempFolderPath(), fileName).getPath();
    }

}
