package net.sweng.domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Date on 2/21/17.
 */
public class TableSchema implements Serializable {

    private Map<String, DataType> columnSchema;

    public TableSchema() {
        this.columnSchema = new HashMap<>();
    }

    public DataType getColumnType(String columnName) {
        if(columnSchema.get(columnName) != null)
            return columnSchema.get(columnName);
        return DataType.STRING;
    }

}
