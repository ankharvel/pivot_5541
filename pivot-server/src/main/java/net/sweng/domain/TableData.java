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
    private Map<String, List<String>> pivotColumns;

    public TableData(String[] columnNames, List<GenericRow> data) {
        this.pivotColumns = new HashMap<>();
        this.columnNames = columnNames;
        this.data = data;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public List<GenericRow> getData() {
        return data;
    }

    public List<String> getPivotColumnValues(String columnName) {
        return pivotColumns.get(columnName);
    }

    public void putPivotColumns(String column, List<String> values) {
        pivotColumns.put(column, values);
    }

}
