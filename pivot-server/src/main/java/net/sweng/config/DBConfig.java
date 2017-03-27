package net.sweng.config;

import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import org.apache.commons.dbcp.datasources.SharedPoolDataSource;
import org.postgresql.ds.PGConnectionPoolDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import static org.springframework.web.context.request.RequestContextHolder.currentRequestAttributes;

/**
 * Date on 2/13/17.
 */
@Configuration
@PropertySource(value = {"classpath:META-INF/configuration.properties"})
public class DBConfig {

    @Resource
    private Environment env;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName(env.getRequiredProperty("db.driver"));
        ds.setUrl(env.getRequiredProperty("db.url"));
        ds.setUsername(env.getRequiredProperty("db.username"));
        ds.setPassword(env.getRequiredProperty("db.password"));
        return ds;
    }

    @Bean(destroyMethod = "close")
    @Scope("prototype")
    public SharedPoolDataSource mySQLDataSource(String host, int port, String databaseName, String user, String password) {
        SharedPoolDataSource ds = new SharedPoolDataSource();
        MysqlConnectionPoolDataSource poolDataSource = new MysqlConnectionPoolDataSource();
        poolDataSource.setServerName(host);
        poolDataSource.setPortNumber(port);
        poolDataSource.setDatabaseName(databaseName);
        poolDataSource.setUser(user);
        poolDataSource.setPassword(password);
        ds.setConnectionPoolDataSource(poolDataSource);
        ds.setMaxActive(10);
        ds.setMaxIdle(5);
        ds.setMaxWait(5000);
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(true);
        ds.setTestWhileIdle(true);
        return ds;
    }

    @Bean(destroyMethod = "close")
    @Scope("prototype")
    public SharedPoolDataSource postgreSQLDataSource(String host, int port, String databaseName, String user, String password) {
        SharedPoolDataSource ds = new SharedPoolDataSource();
        PGConnectionPoolDataSource poolDataSource = new PGConnectionPoolDataSource();
        poolDataSource.setServerName(host);
        poolDataSource.setPortNumber(port);
        poolDataSource.setDatabaseName(databaseName);
        poolDataSource.setUser(user);
        poolDataSource.setPassword(password);
        ds.setConnectionPoolDataSource(poolDataSource);
        ds.setMaxActive(10);
        ds.setMaxIdle(5);
        ds.setMaxWait(5000);
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(true);
        ds.setTestWhileIdle(true);
        return ds;
    }

    public static Integer getSessionIdPrefix() {
        HttpServletRequest request = ((ServletRequestAttributes) currentRequestAttributes()).getRequest();
        return (Integer) request.getSession(false).getAttribute("session.id");
    }

    public static String getTempFolderPath() {
        HttpServletRequest request = ((ServletRequestAttributes) currentRequestAttributes()).getRequest();
        return (String) request.getSession(false).getAttribute("tmp.folder");
    }
}
