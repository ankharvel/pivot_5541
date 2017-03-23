package net.sweng.domain;

import java.util.HashMap;

/**
 * Date on 2/25/17.
 */
public class GenericRow extends HashMap<String, Object> {

    private static final String DOUBLE_REGEX = "^\\-?\\d+\\.\\d+$";

    public static GenericRow createSingleton(String key, Object value) {
        GenericRow genericRow = new GenericRow();
        genericRow.put(key, value);
        return genericRow;
    }

    @Override
    public Object get(Object key) {
        Object o = super.get(key);
        if(o != null && o.toString().matches(DOUBLE_REGEX)) {
            return (double)Math.round(Double.valueOf(o.toString())*100)/100;
        }
        return o;
    }
}
