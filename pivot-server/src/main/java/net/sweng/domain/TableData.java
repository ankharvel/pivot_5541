package net.sweng.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date on 2/14/17.
 */
public class TableData implements Serializable {

    private String[] columnNames;
    private List<GenericRow> data;
    private Map<String, List<String>> columnValues;

    public TableData(String[] columnNames, List<GenericRow> data) {
        this.columnValues = new HashMap<>();
        this.columnNames = columnNames;
        this.data = data;
    }

    public TableData(String[] columnNames) {
        this.columnNames = columnNames;
        this.columnValues = new HashMap<>();
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setData(List<GenericRow> data) {
        this.data = data;
    }

    public List<GenericRow> getData() {
        return data;
    }

    public List<String> getColumnValues(String columnName) {
        return columnValues.get(columnName);
    }

    public void putColumnValues(String column, List<String> values) {
        columnValues.put(column, values);
    }

}
