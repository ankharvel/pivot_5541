package net.sweng.dao;

import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;

/**
 * Date on 2/13/17.
 */
public interface PivotDao {

    TableData getRecordsFromCsv(String sourcePath);

    String[] getHeadersFromCsv(String sourcePath);

    TableData getReportFromCsv(ReportParameters parameters, String sourcePath) throws InvalidDataTypeException;

}
