package net.sweng.domain;

import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

/**
 * This object stores the list of columns (names and types) for every file uploaded to the system
 * Date on 2/23/17.
 */
public class TableCatalogue extends HashMap<String, List<GenericRow>> {

    private Function<String, List<GenericRow>> dataSupplier;

    public TableCatalogue(Function<String, List<GenericRow>> dataSupplier) {
        this.dataSupplier = dataSupplier;
    }

    @Override
    public List<GenericRow> get(Object key) {
        if(super.get(key) == null) {
            List<GenericRow> data = dataSupplier.apply(key.toString());
            put(key.toString(), data);
            return data;
        } else {
            return super.get(key);
        }
    }

}
