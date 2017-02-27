package net.sweng.domain;

/**
 * Date on 2/25/17.
 */
public enum AggregationType {

    AVG("lbl_avg"),
    COUNT("lbl_count"),
    SUM("lbl_sum");

    private String label;

    AggregationType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
