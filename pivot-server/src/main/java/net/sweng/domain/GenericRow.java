package net.sweng.domain;

import java.util.HashMap;

/**
 * Date on 2/25/17.
 */
public class GenericRow extends HashMap<String, Object> {

    public static GenericRow createSingleton(String key, Object value) {
        GenericRow genericRow = new GenericRow();
        genericRow.put(key, value);
        return genericRow;
    }

}
