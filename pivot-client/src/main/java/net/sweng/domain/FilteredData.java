package net.sweng.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created on 3/23/17.
 */
public class FilteredData implements Serializable {

    private List<GenericRow> rows;
    private String filterName;

    public FilteredData(List<GenericRow> rows, String filterName) {
        this.rows = rows;
        this.filterName = filterName;
    }

    public List<GenericRow> getRows() {
        return rows;
    }

    public void setRows(List<GenericRow> rows) {
        this.rows = rows;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }
}
