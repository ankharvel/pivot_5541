package net.sweng.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Date on 2/14/17.
 */
public class TableData implements Serializable {

    private String[] columnNames;
    private List<GenericRow> data;

    public TableData(String[] columnNames, List<GenericRow> data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public List<GenericRow> getData() {
        return data;
    }
}
