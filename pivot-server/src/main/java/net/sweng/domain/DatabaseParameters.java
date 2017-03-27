package net.sweng.domain;

/**
 * Created on 3/26/17.
 */
public class DatabaseParameters {

    private DatabaseType dbType;
    private String host;
    private int port;
    private String databaseName;
    private String username;
    private String password;

    public DatabaseParameters(DatabaseType dbType, String host, int port, String databaseName, String username, String password) {
        this.dbType = dbType;
        this.host = host;
        this.port = port;
        this.databaseName = databaseName;
        this.username = username;
        this.password = password;
    }

    public DatabaseType getDbType() {
        return dbType;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
