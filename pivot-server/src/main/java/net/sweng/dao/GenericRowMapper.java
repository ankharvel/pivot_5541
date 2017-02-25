package net.sweng.dao;

import net.sweng.domain.GenericRow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Date on 2/25/17.
 */
public class GenericRowMapper implements RowMapper<GenericRow> {

    private List<String> columns;

    public GenericRowMapper(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public GenericRow mapRow(ResultSet rs, int i) throws SQLException {
        GenericRow row = new GenericRow();
        for(String col: columns) {
            row.put(col, rs.getString(col));
        }
        return row;
    }

}
