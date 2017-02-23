package net.sweng.dao;

import net.sweng.domain.TableData;

/**
 * Date on 2/13/17.
 */
public interface PivotDao {

    TableData getRecordsFromCsv(String sourcePath);

    String[] getHeadersFromCsv(String sourcePath);

}
