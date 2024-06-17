package com.dbt.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


/**
 * 反射工具类, 反射相关的bean都不能被代码混淆，否则会报错。
 */
public class ReflectUtil {

    private static Logger log = Logger.getLogger(ReflectUtil.class);
    
    /**
     * 获取fielName对应的值
     * 
     * @param fieldName 字段名称，obj对象中要包含get + FileName的方法
     * @param obj       目标bean, 此类不能被代码混淆
     * @return
     */
    public static Object getFieldValueByName(String fieldName, Object obj) {
        int fieldIndex = fieldName.indexOf(".");
        String tempName = "";
        if (fieldIndex > 0) {
            tempName = fieldName.substring(0, fieldIndex);
        } else {
            tempName = fieldName;
        }
        try {
            String firstLetter = tempName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + tempName.substring(1);
            Method method = obj.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(obj, new Object[] {});
            if (tempName == fieldName) {
                return value;
            } else {
                return getFieldValueByName(
                        fieldName.substring(fieldIndex+1), value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * 获取fielName对应的值集合
     * 
     * @param fieldName 字段名称，obj对象中要包含get + FileName的方法
     * @param objLst    目标bean集合, 此类不能被代码混淆
     * @return
     */
    public static <T> List<T> getFieldsValueByName(String fileName, List<?> objLst) {
        List<T> valLst = new ArrayList<T>();
        if (CollectionUtils.isNotEmpty(objLst) && StringUtils.isNotBlank(fileName)) {
            for (Object item : objLst) {
                valLst.add((T)getFieldValueByName(fileName, item));
            }
        }
        return valLst;
    }

    /**
     * 获取属性名称数组
     * 
     * @param obj   目标bean, 此类不能被代码混淆
     * @return 
     */
    public static String[] getFiledName(Object obj) {
        if (obj == null) return new String[]{};
        Field[] fields = obj.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     * 
     * @param obj   目标bean, 此类不能被代码混淆
     * @return
     */
    public static List<Map<String, Object>> getFiledsInfo(Object obj) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (obj == null) return list;
        Field[] fields = obj.getClass().getDeclaredFields();
        Map<String, Object> infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap<String, Object>();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), obj));
            list.add(infoMap);
        }
        return list;
    }
    
    /**
     * 获取fielName对应的值的总和
     * 
     * @param fieldName 字段名称，obj对象中要包含get + FileName的方法
     * @param objLst    目标bean集合, 此类不能被代码混淆
     * @return
     */
    public static int getFieldsValueTotalByName(String fileName, List<?> objLst) {
        int total = 0;
        if (CollectionUtils.isNotEmpty(objLst) && StringUtils.isNotBlank(fileName)) {
            for (Object item : objLst) {
            	total += (long)getFieldValueByName(fileName, item);
            }
        }
        return total;
    }
}
