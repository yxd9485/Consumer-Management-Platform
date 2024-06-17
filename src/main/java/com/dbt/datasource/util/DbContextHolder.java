package com.dbt.datasource.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.vjifen.server.base.datasource.DDS;

public class DbContextHolder {
    private static final ThreadLocal<Map<String, Object>> mapHolder = new ThreadLocal<>();

    public static void setDBType(String dbType) {
        DDS.use(dbType);
    }

    public static String getDBType() {
        return DDS.get();
    }

    public static void clearDBType() {
        DDS.remove();
    }

    public static void setMapVal(String key, Object val) {
        if (mapHolder.get() == null) {
            mapHolder.set(new ConcurrentHashMap<String, Object>());
        }
        mapHolder.get().put(key, val);
    }

    public static Object getMapVal(String key) {
        if (mapHolder.get() == null) {
            return null;
        }
        return mapHolder.get().get(key);
    }

    public static void clearMapVal() {
        if (mapHolder.get() != null) {
            mapHolder.get().clear();
        }
    }
}
