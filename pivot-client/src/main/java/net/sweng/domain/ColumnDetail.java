package net.sweng.domain;

/**
 * Date on 2/25/17.
 */
public class ColumnDetail {

    private String columnName;
    private DataType dataType;

    public ColumnDetail(String columnName, DataType dataType) {
        this.columnName = columnName;
        this.dataType = dataType;
    }

    public String getColumnName() {
        return columnName;
    }

    public DataType getDataType() {
        return dataType;
    }
}
