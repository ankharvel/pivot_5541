package net.sweng.controller;

import net.sweng.domain.DatabaseParameters;
import net.sweng.domain.DatabaseType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 3/25/17.
 */
@ManagedBean
@SessionScoped
public class DatabaseController extends AbstractView {

    private DatabaseType dbType;
    private String host;
    private String port;
    private String databaseName;
    private String username;
    private String password;
    private boolean connected;
    private boolean onChangeEvent;

    public void onMenuChange(ValueChangeEvent event) {
        onChangeEvent = true;
    }

    public void cleanForm() {
        onChangeEvent = false;
    }

    public void connect(ActionEvent event) {
        if (connected) return;
        DatabaseParameters parameters = new DatabaseParameters(dbType, host, Integer.parseInt(port), databaseName, username, password);
        if (pivotController.connectToDB(parameters)) {
            connected = true;
        }
    }

    public void disconnect(ActionEvent event) {
        if (!connected) return;
        connected = false;
    }

    public List<String> databaseTables() {
        if (!connected) return new ArrayList<>();
        return pivotController.obtainTableNames();
    }

    public DatabaseType[] obtainDBTypeValues() {
        return DatabaseType.values();
    }

//--------------------------------  GETTERS AND SETTERS ----------------------------------------------------

    public DatabaseType getDbType() {
        return dbType;
    }

    public void setDbType(DatabaseType dbType) {
        this.dbType = dbType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isOnChangeEvent() {
        return onChangeEvent;
    }
}
