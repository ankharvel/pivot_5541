package net.sweng.domain;

import java.io.Serializable;

/**
 * Date on 2/13/17.
 */
public class ColumnModel implements Serializable {

    private String header;
    private String property;

    public ColumnModel(String header, String property) {
        this.header = header;
        this.property = property;
    }

    public String getHeader() {
        return header;
    }

    public String getProperty() {
        return property;
    }

}
