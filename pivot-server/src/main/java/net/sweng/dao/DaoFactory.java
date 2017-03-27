package net.sweng.dao;

import net.sweng.config.DBConfig;
import net.sweng.domain.DatabaseParameters;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on 3/26/17.
 */
@Component
public class DaoFactory {

    private Map<Integer, PivotDao> daoMap;

    @Autowired
    private ApplicationContext applicationContext;

    public DaoFactory() {
        this.daoMap = new HashMap<>();
    }

    public PivotDao getDao() {
        return daoMap.get(DBConfig.getSessionIdPrefix());
    }

    public void createConnection(DatabaseParameters parameters) throws Exception {
        PivotDao pivotDao = daoMap.get(DBConfig.getSessionIdPrefix());
        DataSource dataSource;
        if(pivotDao != null) {
            destroyConnection();
        }
        switch (parameters.getDbType()) {
            case MYSQL:
                dataSource = (DataSource) applicationContext.getBean("mySQLDataSource",
                        parameters.getHost(),
                        parameters.getPort(),
                        parameters.getDatabaseName(),
                        parameters.getUsername(),
                        parameters.getPassword()
                );
                pivotDao = (PivotDao) applicationContext.getBean("mySQLPivotDao", dataSource);
                break;
            case POSTGRESQL:
                dataSource = (DataSource) applicationContext.getBean("postgreSQLDataSource",
                        parameters.getHost(),
                        parameters.getPort(),
                        parameters.getDatabaseName(),
                        parameters.getUsername(),
                        parameters.getPassword()
                );
                pivotDao = (PivotDao) applicationContext.getBean("postgreSQLPivotDao", dataSource);
                break;
        }
        daoMap.put(DBConfig.getSessionIdPrefix(), pivotDao);
    }

    private void destroyConnection() throws Exception {
        daoMap.get(DBConfig.getSessionIdPrefix()).closePool();
    }

}
