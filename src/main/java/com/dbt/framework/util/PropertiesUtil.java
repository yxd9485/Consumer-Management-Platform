 /**
  * 项目名称：V积分
  * 文件名：PropertiesUtil.java
  * 作者：@王建
  * 创建时间： 2014/1/1
  * 功能描述: 配置文件工具类
  * 版本: V1.0
  */
package com.dbt.framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.TreeMap;

public class PropertiesUtil {
	
	private static Properties properties = new Properties();
	static {
		InputStream is = null;
		try {
			is = PropertiesUtil.class.getClassLoader()
					.getResourceAsStream("common.properties");
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getPropertyValue(String key) {
	    return properties.getProperty(key);
	}

	public static TreeMap<String, String> getPropertyByPrefix(String prefix, int subIndex) {
		TreeMap<String, String> map = new TreeMap<String, String>();
	    String key = "", value = "";
	    for (Object item : properties.keySet()) {
	        key = item.toString();
            if (key.startsWith(prefix) && !prefix.equals(key)) {
                value = getPropertyValue(key);
                try {
                    if (value != null && !"".equals(value.trim())) {
                            map.put(key.substring(subIndex), new String(value.getBytes("ISO-8859-1"), "UTF-8"));
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
		return map;
	}
}
