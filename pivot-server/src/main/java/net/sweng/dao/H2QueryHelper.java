package net.sweng.dao;

import net.sweng.domain.AggregationType;
import net.sweng.domain.ColumnDetail;
import net.sweng.domain.ReportParameters;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static net.sweng.config.DBConfig.getSessionIdPrefix;

/**
 * Date on 2/25/17.
 */
@Service
public class H2QueryHelper implements QueryHelper {

    private static final String AGG_SUFFIX = "_agg";
    private static final String COL_SUFFIX = "_col";

    public String buildQuery(ReportParameters parameters) {
        StringBuilder sql = new StringBuilder("SELECT ");
        appendColumns(sql, parameters.getReportRows());
        appendColumns(sql, parameters.getReportColumns(), COL_SUFFIX);
        appendAggregation(sql, parameters.getAggregationType(), parameters.getField());
        sql.append(" FROM ").append(getTableName(parameters.getFileName()));
        appendGroupBy(sql, parameters.getReportRows(), parameters.getReportColumns());
        appendOrder(sql, parameters.getReportRows());
        return sql.toString();
    }

    @Override
    public String[] getHeaders(ReportParameters parameters) {
        List<String> headers = new ArrayList<>();
        for(ColumnDetail detail: parameters.getReportRows()) {
            headers.add(detail.getColumnName());
        }
        for(ColumnDetail detail: parameters.getReportColumns()) {
            headers.add(detail.getColumnName() + COL_SUFFIX);
        }
        headers.add(parameters.getAggregationType().name() + AGG_SUFFIX);
        return headers.toArray(new String[headers.size()]);
    }

    private void appendColumns(StringBuilder sql, List<ColumnDetail> rows) {
        if(!rows.isEmpty()) {
            for(ColumnDetail detail: rows) {
                sql.append(" ").append(getCastedColumn(detail)).append(",");
            }
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
    }

    private void appendColumns(StringBuilder sql, List<ColumnDetail> rows, String suffix) {
        if(!rows.isEmpty()) {
            if(!sql.toString().trim().endsWith("SELECT")) {
                sql.append(", ");
            }
            for(ColumnDetail detail: rows) {
                sql.append(" ").append(detail.getColumnName());
                sql.append(" AS ").append(detail.getColumnName()).append(suffix).append(",");
            }
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
    }

    private void appendAggregation(StringBuilder sql, AggregationType type, ColumnDetail detail) {
        if(!sql.toString().trim().endsWith("SELECT")) {
            sql.append(", ");
        }
        switch (type) {
            case AVG:
                sql.append("AVG(");
                sql.append(getCastedColumn(detail));
                sql.append(") ");
                break;
            case COUNT:
                sql.append("COUNT(");
                sql.append(getCastedColumn(detail));
                sql.append(") ");
                break;
            case SUM:
                sql.append("SUM(");
                sql.append(getCastedColumn(detail));
                sql.append(") ");
                break;
        }
        sql.append("AS ").append(type.name()).append(AGG_SUFFIX);
    }

    private void appendGroupBy(StringBuilder sql, List<ColumnDetail> rows, List<ColumnDetail> columns) {
        if(!rows.isEmpty()) {
            sql.append(" GROUP BY ");
            for(ColumnDetail detail: rows) {
                sql.append(detail.getColumnName()).append(", ");
            }
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
        if(!columns.isEmpty()) {
            if(!sql.toString().trim().endsWith("SELECT")) {
                sql.append(", ");
            }
            for(ColumnDetail detail: columns) {
                sql.append(detail.getColumnName()).append(", ");
            }
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
    }

    private void appendOrder(StringBuilder sql, List<ColumnDetail> rows) {
        if(!rows.isEmpty()) {
            sql.append(" ORDER BY ");
            int i = 1;
            for(ColumnDetail ignored : rows) {
                sql.append(i++).append(", ");
            }
            sql.delete(sql.lastIndexOf(","), sql.length());
        }
    }

    private String getCastedColumn(ColumnDetail column) {
        switch (column.getDataType()) {
            case STRING:
                return column.getColumnName();
            case NUMERIC:
                return "CAST(" + column.getColumnName() + " AS DOUBLE)";
        }
        return column.getColumnName();
    }

    public static String getTableName(String fileName) {
        return (fileName.substring(0, fileName.indexOf(".")) + "_" + getSessionIdPrefix()).toUpperCase();
    }

}
