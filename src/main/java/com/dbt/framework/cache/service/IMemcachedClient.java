package com.dbt.framework.cache.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

/**
* 文件名: IMemcachedClient.java
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述:  memcached接口工具类
* 修改人: jiquanwei
* 修改时间：2014-09-04 10:18:24
* 修改内容：新增
*/
public interface IMemcachedClient {

    boolean isInitialized();

    void initialize(List<InetSocketAddress> addresses) throws IOException;

    <T> void set(String key, int secondsToExpire, T value) throws IOException;

    <T> T get(String key) throws IOException;

    void delete(String key) throws IOException;
    
    void deleteAll() throws IOException;
    
    void removeByRegex(String key) throws Exception;

    /**
     * 删除指定前缀缓存key
     * @param key
     */
    void removeByPrefix(String key) throws Exception;
}