package net.sweng.dao;

import net.sweng.domain.ColumnDetail;
import net.sweng.domain.GenericRow;
import net.sweng.domain.ReportParameters;
import net.sweng.domain.TableData;
import net.sweng.domain.exceptions.InvalidDataTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

import static net.sweng.config.DBConfig.getSessionIdPrefix;

/**
 * Date on 2/13/17.
 */
@Repository
public class H2PivotDao implements PivotDao {

    private static final Logger logger = Logger.getGlobal();

    private static final String SELECT_FROM_CSV =
            "SELECT * FROM CSVREAD(''{0}'')";

    private static final String SELECT_COL_VALUES =
            "SELECT DISTINCT({0}) FROM {1} ORDER BY 1 ASC";

    private static final String SELECT_COLUMN_NAMES =
            "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ''{0}''";

    private static final String CLEAN_UP =
            "DROP ALL OBJECTS";

    private static final String DROP_IF_EXIST =
            "DROP TABLE IF EXISTS {0} ";

    private static final String CREATE =
            "CREATE TABLE {0} AS ";

    @Autowired
    private QueryHelper queryHelper;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init() {
        jdbcTemplate = new JdbcTemplate(dataSource);
        logger.info("Cleaning up database");
        jdbcTemplate.update(CLEAN_UP);
    }

    public TableData getRecordsFromCsv(String sourcePath) {
        String tableName = getTableName(sourcePath);
        createTableFromFile(sourcePath);
        List<String> columns = jdbcTemplate.queryForList(MessageFormat.format(SELECT_COLUMN_NAMES, tableName), String.class);
        String[] columnNames = columns.toArray(new String[columns.size()]);

        List<GenericRow> data = jdbcTemplate.query(
                MessageFormat.format(SELECT_FROM_CSV, sourcePath), new GenericRowMapper(columns));
        return new TableData(columnNames, data);
    }

    @Override
    public String[] getHeadersFromCsv(String sourcePath) {
        Set<String> columns = jdbcTemplate.queryForList(MessageFormat.format(SELECT_FROM_CSV + " LIMIT 1", sourcePath)).get(0).keySet();
        List<String> cols = new ArrayList<>(columns);
        Collections.sort(cols);
        return cols.toArray(new String[cols.size()]);
    }

    @Override
        public TableData getReportFromCsv(ReportParameters parameters, String sourcePath) throws InvalidDataTypeException {
        try {
            createTableFromFile(sourcePath);
            String[] headers = queryHelper.getHeaders(parameters);
            List<GenericRow> data = jdbcTemplate.query(
                    queryHelper.buildQuery(parameters), new GenericRowMapper(Arrays.asList(headers)));
            TableData td = new TableData(headers, data);
            if(!parameters.getReportColumns().isEmpty()) {
                appendPivotColumns(parameters, td);
            }
            return td;
        } catch (Exception ex) {
            throw new InvalidDataTypeException(ex.getCause().getMessage(), ex);
        }
    }

    private void appendPivotColumns(ReportParameters parameters, TableData td) {
        for(ColumnDetail detail: parameters.getReportColumns()) {
            List<String> cols = jdbcTemplate.queryForList(
                    MessageFormat.format(
                            SELECT_COL_VALUES,
                            detail.getColumnName(), getTableName(parameters.getFileName())),
                    String.class
            );
            td.putPivotColumns(detail.getColumnName(), cols);
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
