package net.sweng.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * Date on 2/23/17.
 */
public class TableCatalogue extends HashMap<String, List<GenericRow>> {

    private Function<String, List<GenericRow>> dataSupplier;
    private ResourceBundle bundle = ResourceBundle.getBundle("messages");

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

//    public List<ColumnDetail> getColumnDetails(Object key) {
//        List<GenericRow> rowList = get(key);
//        List<ColumnDetail> details = new ArrayList<>();
//        rowList.forEach(r -> details.add(
//                new ColumnDetail(
//                        r.get(bundle.getString("header_column")).toString(),
//                        DataType.valueOf(r.get(bundle.getString("header_type")).toString()))
//        ));
//        return details;
//    }

}
