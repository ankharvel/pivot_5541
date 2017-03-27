package net.sweng.dao.helpers;

import net.sweng.domain.ColumnDetail;
import org.springframework.stereotype.Service;

/**
 * Created on 3/27/17.
 */
@Service
public class PostgreSQLQueryHelper extends AbstractQueryHelper {

    @Override
    protected String getCastedColumn(ColumnDetail column) {
        return column.getColumnName();
    }

    @Override
    protected String getTableName(String tableName) {
        return tableName;
    }
}
