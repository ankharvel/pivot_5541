package net.sweng.dao;

import net.sweng.domain.TableData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.io.File;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private static final String SELECT_COLUMN_NAMES =
            "SELECT column_name FROM INFORMATION_SCHEMA.COLUMNS WHERE table_name = ''{0}''";

    private static final String CLEAN_UP =
            "DROP ALL OBJECTS";

    private static final String DROP_IF_EXIST =
            "DROP TABLE IF EXISTS {0} ";

    private static final String CREATE =
            "CREATE TABLE {0} AS ";

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
        String tableName = getTableName(getSessionIdPrefix(), sourcePath);
        jdbcTemplate.update(MessageFormat.format(DROP_IF_EXIST, tableName));
        jdbcTemplate.update(MessageFormat.format(CREATE, tableName) +
                MessageFormat.format(SELECT_FROM_CSV, sourcePath));
        List<String> columns = jdbcTemplate.queryForList(MessageFormat.format(SELECT_COLUMN_NAMES, tableName), String.class);
        String[] columnNames = columns.toArray(new String[columns.size()]);

        List<Map<String, Object>> data = jdbcTemplate.queryForList(
                MessageFormat.format(SELECT_FROM_CSV, sourcePath));
        return new TableData(columnNames, data);
    }

    @Override
    public String[] getHeadersFromCsv(String sourcePath) {
        Set<String> columns = jdbcTemplate.queryForList(MessageFormat.format(SELECT_FROM_CSV + " LIMIT 1", sourcePath)).get(0).keySet();
        return columns.toArray(new String[columns.size()]);
    }

    private String getTableName(int sessionPrefix, String sourcePath) {
        File file = new File(sourcePath);
        return (file.getName().substring(0, file.getName().indexOf(".")) + "_" + sessionPrefix).toUpperCase();
    }

}
