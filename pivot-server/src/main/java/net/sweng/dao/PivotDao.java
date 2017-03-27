package net.sweng.dao;

import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;

import java.util.List;
import java.util.Map;

/**
 * Date on 2/13/17.
 */
public interface PivotDao {

    /**
     * Brings the raw data from a table or file
     * @param source sourcePath or table name
     */
    TableData getRecords(String source);

    /**
     * Retrieve the table headers
     * @param source sourcePath or table name
     */
    List<String> getHeaders(String source);

    TableData getReport(ReportParameters parameters, String sourcePath) throws InvalidDataTypeException;

    Map<String,TableData> getReportWithFilters(ReportParameters parameters, List<String> filterValues) throws InvalidDataTypeException;

    /**
     * Retrieve available table names
     */
    List<String> getTableNames();

    /**
     * Close database pool connection
     */
    void closePool();

}
