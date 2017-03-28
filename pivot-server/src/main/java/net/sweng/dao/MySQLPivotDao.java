package net.sweng.dao;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import net.sweng.dao.helpers.QueryHelper;
import net.sweng.domain.ColumnDetail;
import net.sweng.domain.GenericRow;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created on 3/26/17.
 */
@Repository("mySQLPivotDao")
@Scope("prototype")
public class MySQLPivotDao extends AbstractPivotDao {

    private static final String TEST_DB =
            "SELECT current_date";

    private static final String SELECT =
            "SELECT * FROM {0} ";

    private static final String SELECT_HEADERS =
            "SELECT column_name " +
            "FROM information_schema.columns " +
            "WHERE table_schema = ? " +
            "AND table_name = ? ";

    private static final String SELECT_COL_VALUES =
            "SELECT DISTINCT({0}) FROM {1} ORDER BY 1 ASC";

    private static final String SELECT_TABLE_NAMES =
            "SELECT table_name " +
            "FROM information_schema.tables " +
            "WHERE table_schema = ? ";

    @Resource(name = "mySQLQueryHelper")
    private QueryHelper queryHelper;

    private String schema;

    public MySQLPivotDao(DataSource dataSource) {
        super(dataSource);
        jdbcTemplate.queryForList(TEST_DB);
        schema = ((MysqlConnectionPoolDataSource)((SharedPoolDataSource) dataSource)
                    .getConnectionPoolDataSource()).getDatabaseName();
    }

    @Override
    public TableData getRecords(String tableName) {
        List<String> columns = getHeaders(tableName);
        String[] columnNames = columns.toArray(new String[columns.size()]);

        List<GenericRow> data = jdbcTemplate.query(
                MessageFormat.format(SELECT, tableName), new GenericRowMapper(columns));
        return new TableData(columnNames, data);
    }

    @Override
    public List<String> getHeaders(String tableName) {
        List<Object> args = new ArrayList<>();
        args.add(schema);
        args.add(tableName);
        return jdbcTemplate.queryForList(SELECT_HEADERS, String.class, args.toArray());
    }

    @Override
    public TableData getReport(ReportParameters parameters, String tableName) throws InvalidDataTypeException {
        try {
            String[] headers = queryHelper.getHeaders(parameters);
            TableData td = new TableData(headers);
            List<Object> args = new ArrayList<>();
            if(!parameters.getReportColumns().isEmpty()) {
                appendColumnValues(td, parameters.getReportColumns(), parameters.getFileName());
            }
            if(!parameters.getReportFilter().isEmpty()) {
                appendColumnValues(td, parameters.getReportFilter(), parameters.getFileName());
                if(parameters.getFilterValues() != null && !parameters.getFilterValues().isEmpty()) {
                    args.addAll(parameters.getFilterValues());
                } else {
                    args.addAll(parameters.getReportFilter().stream().map(
                            filter -> td.getColumnValues(filter.getColumnName()).iterator().next()).collect(Collectors.toList()));
                }
            }
            List<GenericRow> data = jdbcTemplate.query(
                    queryHelper.buildQuery(parameters), new GenericRowMapper(Arrays.asList(headers)), args.toArray());
            td.setData(data);

            return td;
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "Unable to generate report.", ex);
            throw new InvalidDataTypeException(ex.getCause().getMessage(), ex);
        }
    }

    @Override
    public Map<String, TableData> getReportWithFilters(ReportParameters parameters, List<String> filterValues) throws InvalidDataTypeException {
        Map<String, TableData> dataMap = new HashMap<>();
        try {
            String[] headers = queryHelper.getHeaders(parameters);

            for(String filterValue: filterValues) {
                TableData td = new TableData(headers);
                List<Object> args = new ArrayList<>();
                if(!parameters.getReportColumns().isEmpty()) {
                    appendColumnValues(td, parameters.getReportColumns(), parameters.getFileName());
                }
                args.add(filterValue);
                List<GenericRow> data = jdbcTemplate.query(
                        queryHelper.buildQuery(parameters), new GenericRowMapper(Arrays.asList(headers)), args.toArray());
                td.setData(data);
                dataMap.put(filterValue, td);
            }

            return dataMap;
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "Unable to generate report.", ex);
            throw new InvalidDataTypeException(ex.getCause().getMessage(), ex);
        }
    }

    @Override
    public List<String> getTableNames() {
        return jdbcTemplate.queryForList(SELECT_TABLE_NAMES, String.class, schema);
    }

    private void appendColumnValues(TableData td, List<ColumnDetail> detailList, String tableName) {
        for(ColumnDetail detail: detailList) {
            List<String> cols = jdbcTemplate.queryForList(
                    MessageFormat.format(
                            SELECT_COL_VALUES,
                            detail.getColumnName(), tableName),
                    String.class
            );
            td.putColumnValues(detail.getColumnName(), cols);
        }
    }

}
