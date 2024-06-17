package com.dbt.framework.util;

import java.util.HashMap;

public class UserThreadLocalUtil {

    private static ThreadLocal<HashMap<String, Object>> userMap = new ThreadLocal<HashMap<String,Object>>();
    
    public static void put(String key, Object val) {
        if (userMap.get() == null) userMap.set(new HashMap<String,Object>());
        userMap.get().put(key, val);
    }
    
    public static <T> T get(String key) {
        if (userMap.get() == null)  return null;
        return (T)userMap.get().get(key);
    }
    
    public static void remove(String key) {
        if (userMap.get() == null)  return;
        userMap.get().remove(key);
    }
    
    public static void clear() {
        if (userMap.get() == null)  return;
        userMap.get().clear();
    }
}
