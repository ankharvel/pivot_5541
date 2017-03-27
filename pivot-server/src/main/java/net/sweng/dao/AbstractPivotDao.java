package net.sweng.dao;

import net.sweng.config.DBConfig;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on 3/26/17.
 */
@Scope("prototype")
public abstract class AbstractPivotDao implements PivotDao {

    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    public AbstractPivotDao(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void closePool() {
        if(dataSource instanceof SharedPoolDataSource) {
            try {
                Logger.getGlobal().log(Level.INFO, "Closing database connection for session: " + DBConfig.getSessionIdPrefix());
                ((SharedPoolDataSource) dataSource).close();
            } catch (Exception e) {
                Logger.getGlobal().log(Level.WARNING, "Unable to close database.");
            }
        }
    }

}
