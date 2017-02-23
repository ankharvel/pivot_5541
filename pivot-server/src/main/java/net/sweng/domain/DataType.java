package net.sweng.domain;

/**
 * Date on 2/21/17.
 */
public enum DataType {

    STRING("lbl_text"),
    NUMERIC("lbl_numeric");

    private String lbl;

    DataType(String lbl) {
        this.lbl = lbl;
    }

    public String getLbl() {
        return lbl;
    }
}
