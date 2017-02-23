package net.sweng.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Date on 2/23/17.
 */
public class TableCatalogue extends HashMap<String, List<Map<String, Object>>> {

    private Function<String, List<Map<String, Object>>> dataSupplier;

    public TableCatalogue(Function<String, List<Map<String, Object>>> dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

    @Override
    public List<Map<String, Object>> get(Object key) {
        if(super.get(key) == null) {
            List<Map<String, Object>> data = dataSupplier.apply(key.toString());
            put(key.toString(), data);
            return data;
        } else {
            return super.get(key);
        }
    }

}
