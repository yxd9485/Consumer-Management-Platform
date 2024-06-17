package com.dbt.framework.util;
/**
* 文件名: Assertion.java
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述:  memcached检测是否有value值工具类
* 修改人: jiquanwei
* 修改时间：2014-09-04 10:18:24
* 修改内容：新增
*/

public class Assertion {

    private Assertion() {
    }

    public static void notNullValue(String name, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Null is not allowed for " + name);
        }
    }

}