package net.sweng.domain;

/**
 * Created on 3/25/17.
 */
public enum DatabaseType {

    MYSQL,
    POSTGRESQL;

    public static DatabaseType getDatabaseType(String value) {
        for(DatabaseType type: DatabaseType.values()) {
            if(type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }

}
