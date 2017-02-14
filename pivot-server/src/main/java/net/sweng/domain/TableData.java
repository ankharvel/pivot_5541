package net.sweng.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Date on 2/14/17.
 */
public class TableData implements Serializable {

    private String[] columnNames;
    private List<Map<String, Object>> data;

    public TableData(String[] columnNames, List<Map<String, Object>> data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }
}
