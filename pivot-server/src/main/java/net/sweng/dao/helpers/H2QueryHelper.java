package net.sweng.dao.helpers;

import net.sweng.domain.ColumnDetail;
import org.springframework.stereotype.Service;

import static net.sweng.config.DBConfig.getSessionIdPrefix;

/**
 * Date on 2/25/17.
 */
@Service
public class H2QueryHelper extends AbstractQueryHelper {

    protected String getCastedColumn(ColumnDetail column) {
        switch (column.getDataType()) {
            case STRING:
                return column.getColumnName();
            case NUMERIC:
                return "CAST(" + column.getColumnName() + " AS DOUBLE)";
        }
        return column.getColumnName();
    }

    protected String getTableName(String fileName) {
        return (fileName.substring(0, fileName.indexOf(".")) + "_" + getSessionIdPrefix()).toUpperCase();
    }

}
