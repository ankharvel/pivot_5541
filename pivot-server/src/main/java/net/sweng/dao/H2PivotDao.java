package net.sweng.dao;

import net.sweng.config.DBConfig;
import net.sweng.dao.helpers.QueryHelper;
import net.sweng.domain.ColumnDetail;
import net.sweng.domain.GenericRow;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static net.sweng.config.DBConfig.getSessionIdPrefix;

/**
 * Date on 2/13/17.
 */
@Repository("h2PivotDao")
public class H2PivotDao extends AbstractPivotDao {

    private static final Logger logger = Logger.getGlobal();

    private static final String SELECT_FROM_CSV =
            "SELECT * FROM CSVREAD(''{0}'')";

    private static final String SELECT_COL_VALUES =
            "SELECT DISTINCT({0}) FROM {1} ORDER BY 1 ASC";

    private static final String SELECT_COLUMN_NAMES =
            "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ''{0}''";

    private static final String CLEAN_UP =
            "DROP ALL OBJECTS";

    private static final String SHOW_TABLES =
            "SHOW TABLES";

    private static final String DROP_IF_EXIST =
            "DROP TABLE IF EXISTS {0} ";

    private static final String CREATE =
            "CREATE TABLE {0} AS ";

    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String TABLE_SCHEMA = "TABLE_SCHEMA";

    @Resource(name = "h2QueryHelper")
    private QueryHelper queryHelper;

    public H2PivotDao(@Qualifier("dataSource") DataSource dataSource) {
        super(dataSource);
    }

    @PostConstruct
    private void init() {
        logger.info("Cleaning up database");
        jdbcTemplate.update(CLEAN_UP);
    }

    public TableData getRecords(String sourcePath) {
        String tableName = getTableName(sourcePath);
        createTableFromFile(sourcePath);
        List<String> columns = jdbcTemplate.queryForList(MessageFormat.format(SELECT_COLUMN_NAMES, tableName), String.class);
        String[] columnNames = columns.toArray(new String[columns.size()]);

        List<GenericRow> data = jdbcTemplate.query(
                MessageFormat.format(SELECT_FROM_CSV, sourcePath), new GenericRowMapper(columns));
        return new TableData(columnNames, data);
    }

    @Override
    public List<String> getHeaders(String sourcePath) {
        Set<String> columns = jdbcTemplate.queryForList(MessageFormat.format(SELECT_FROM_CSV + " LIMIT 1", sourcePath)).get(0).keySet();
        List<String> cols = new ArrayList<>(columns);
        Collections.sort(cols);
        return cols;
    }

    @Override
        public TableData getReport(ReportParameters parameters, String sourcePath) throws InvalidDataTypeException {
        try {
            createTableFromFile(sourcePath);
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
            throw new InvalidDataTypeException(ex.getCause().getMessage(), ex);
        }
    }

    @Override
    public List<String> getTableNames() {
        return new ArrayList<>();
    }

    public void cleanSessionTables() {
        List<String> headers = Arrays.asList(TABLE_NAME, TABLE_SCHEMA);
        List<GenericRow> data = jdbcTemplate.query(SHOW_TABLES, new GenericRowMapper(headers));
        data.stream().filter(d -> String.valueOf(d.get(TABLE_NAME)).endsWith("_" + DBConfig.getSessionIdPrefix())).forEach(row ->
                jdbcTemplate.update(MessageFormat.format(DROP_IF_EXIST, String.valueOf(row.get(TABLE_NAME))))
        );
    }

    private void appendColumnValues(TableData td, List<ColumnDetail> detailList, String fileName) {
        for(ColumnDetail detail: detailList) {
            List<String> cols = jdbcTemplate.queryForList(
                    MessageFormat.format(
                            SELECT_COL_VALUES,
                            detail.getColumnName(), getTableName(fileName)),
                    String.class
            );
            td.putColumnValues(detail.getColumnName(), cols);
        }
    }

    private void createTableFromFile(String sourcePath) {
        String tableName = getTableName(sourcePath);
        jdbcTemplate.update(MessageFormat.format(DROP_IF_EXIST, tableName));
        jdbcTemplate.update(MessageFormat.format(CREATE, tableName) +
                MessageFormat.format(SELECT_FROM_CSV, sourcePath));
    }

    private String getTableName(String sourcePath) {
        File file = new File(sourcePath);
        return (file.getName().substring(0, file.getName().indexOf(".")) + "_" + getSessionIdPrefix()).toUpperCase();
    }

}
