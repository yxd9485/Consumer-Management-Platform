package com.dbt.framework.util;

import java.util.UUID;

public class UUIDTools {
    private static UUIDTools uuidTools = null;

    /**
     * 空构造
     */
    private UUIDTools() {

    }

    /**
     * 单例模式
     * @return
     */
    public static synchronized UUIDTools getInstance() {
        if (uuidTools == null) {
            return new UUIDTools();
        }
        return uuidTools;
    }

    /**
     * 取得UUID
     * 
     * @return UUID.tostring
     */

    public String getUUID() {
        return UUID.randomUUID().toString();
    }
    
    public static void main(String[] args) {
    	String str = "d82380db-1bc2-45fc-b23f-bc24c1d802ea:10:1.00:0::10";
		for (int i = 0; i < str.split(":").length; i++) {
			System.out.println(str.split(":")[i]);
		}
		
		System.out.println("a:".split(":").length);
	}
}
