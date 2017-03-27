package net.sweng.domain;

import java.io.Serializable;

/**
 * Created on 3/27/17.
 */
public class SourceDetail implements Serializable {

    private String sourceName;
    private SourceType sourceType;

    public SourceDetail(String sourceName, SourceType sourceType) {
        this.sourceName = sourceName;
        this.sourceType = sourceType;
    }

    public String getSourceName() {
        return sourceName;
    }

    public SourceType getSourceType() {
        return sourceType;
    }

    public enum SourceType {
        CSV("CSV"),
        TXT("TXT"),
        DATABASE("DB");

        private String label;

        SourceType(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SourceDetail that = (SourceDetail) o;

        if (getSourceName() != null ? !getSourceName().equals(that.getSourceName()) : that.getSourceName() != null)
            return false;
        return getSourceType() == that.getSourceType();

    }

    @Override
    public int hashCode() {
        int result = getSourceName() != null ? getSourceName().hashCode() : 0;
        result = 31 * result + (getSourceType() != null ? getSourceType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("");
        if (sourceType != null) {
            sb.append(sourceType.getLabel()).append(": ");
            int index = sourceName.indexOf(".");
            if (index > 0) {
                sb.append(sourceName.substring(0,index));
            } else {
                sb.append(sourceName);
            }
        }
        return sb.toString();
    }
}
