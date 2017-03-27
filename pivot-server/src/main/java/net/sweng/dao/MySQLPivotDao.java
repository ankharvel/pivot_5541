package net.sweng.dao;

import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created on 3/26/17.
 */
@Repository("mySQLPivotDao")
@Scope("prototype")
public class MySQLPivotDao extends AbstractPivotDao {

    private static final String TEST_DB =
            "SELECT now()";

    public MySQLPivotDao(DataSource dataSource) {
        super(dataSource);
        jdbcTemplate.queryForList(TEST_DB);
    }

    @Override
    public TableData getRecords(String sourcePath) {
        return null;
    }

    @Override
    public List<String> getHeaders(String sourcePath) {
        return new ArrayList<>();
    }

    @Override
    public TableData getReport(ReportParameters parameters, String sourcePath) throws InvalidDataTypeException {
        return null;
    }

    @Override
    public Map<String, TableData> getReportWithFilters(ReportParameters parameters, List<String> filterValues) throws InvalidDataTypeException {
        return null;
    }

    @Override
    public List<String> getTableNames() {
        return new ArrayList<>();
    }
}
