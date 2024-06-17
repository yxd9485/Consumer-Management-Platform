package com.dbt.framework.cache.service.impl;

import static com.dbt.framework.util.Assertion.notNullValue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.regex.Pattern;

import com.dbt.web.VjifenManageConfig;
import lombok.extern.slf4j.Slf4j;
import net.rubyeye.xmemcached.*;
import net.rubyeye.xmemcached.utils.AddrUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.framework.cache.service.IMemcachedClient;

/**
* 文件名: XmemcachedClient
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved. 
* 描述:  memcached接口实现工具类
* 修改人: jiquanwei
* 修改时间：2014-09-04 10:18:24
* 修改内容：新增
*/
@Component("xmemcachedClient")
@Slf4j
public class XmemcachedClient implements IMemcachedClient {
	private final static Log LOG = LogFactory.getLog(XmemcachedClient.class);

	private String version;
    private List<InetSocketAddress> address;
    @Autowired
    private MemcachedClient memcached;



    public XmemcachedClient(VjifenManageConfig config){
        this.version = config.getMemCacheVersion();
        this.address = AddrUtil.getAddresses(config.getMemCacheAddress());
    }

    /**
     * 初始化memcached客户端
     */
    public boolean isInitialized() {
        return memcached != null;
    }
    /**
     * 连接memcache服务端
     * @param addresses 服务地址
     */
    public void initialize(List<InetSocketAddress> addresses) throws IOException {
        this.address = addresses;
        notNullValue("addresses", addresses);
        this.memcached = new XMemcachedClient(addresses);
    }
    /**
     * 加入memcache缓存
     * @param key               缓存key
     * @param secondsToExpire   过期时间
     * @param value             缓存值
     */
    public <T> void set(String key, int secondsToExpire, T value) {
    	key = key + version;
    	if(StringUtils.isNotBlank(DbContextHolder.getDBType())){
    		key = DbContextHolder.getDBType() + ":" + key;
    	}
        notNullValue("key", key);
        try {
            memcached.set(key, secondsToExpire, value);
        } catch (Exception e) {
            String failedMessage = "Failed to set value on memcached! " +
                    "(key:" + key + ",secondsToExpire:" + secondsToExpire + ",value:" + value + ")";
            LOG.error(failedMessage);
        }
    }
   
    /**
     * 取出缓存
     * @param key   缓存key
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
    	key = key + version;
    	if(StringUtils.isNotBlank(DbContextHolder.getDBType())) {
    		key = DbContextHolder.getDBType() + ":" + key;
    	}
        notNullValue("key", key);
        try {
            return (T) memcached.get(key);
        } catch (Exception e) {
            String failedMessage = "Failed to get value on memcached! (key:" + key + ")";
            LOG.error(failedMessage);
            return null;
        }
    }
    /**
     * 删除缓存
     * @param key   缓存key
     */
    @Override
    public void delete(String key) throws IOException {
    	key = key + version;
    	if(StringUtils.isNotBlank(DbContextHolder.getDBType())){
    		key = DbContextHolder.getDBType() + ":" + key;
    	}
    	notNullValue("key", key);
    	try {
            memcached.delete(key);
        } catch (Exception e) {
            String failedMessage = "Failed to delete value on memcached! (key:" + key + ")";
            throw new IOException(failedMessage, e);
        }
    }

    public void deleteAll() throws IOException {
        try {
            if (null != address && !address.isEmpty()) {
                for (InetSocketAddress inetSocketAddress : address) {
                    memcached.flushAll(inetSocketAddress);
                }
            }
        } catch (Exception e) {
            String failedMessage  = "In the method CustXMemcachedClient.deleteAll() exists error";
            LOG.error(failedMessage, e);
            throw new IOException(failedMessage, e);
        }
    }
    
	/**
     * 正则表达式删除缓存
     * @param regex 正则表达式
     */
	@Deprecated
	@Override
	public void removeByRegex(String regex) throws Exception {
    	// 迭代的只是一个keys的快照，因此可以在迭代时删除
    	Pattern p = Pattern.compile(regex);
    	if (null != address && !address.isEmpty()) {
    		String cacheKey;// 缓存key
    		for (InetSocketAddress inetSocketAddress : address) {
    			KeyIterator keyIterator = memcached.getKeyIterator(inetSocketAddress);
    			if (null != keyIterator) {
    				while (keyIterator.hasNext()) {
    					cacheKey = keyIterator.next();
    					if (p.matcher(cacheKey).find()) {
    						memcached.delete(cacheKey);
    					}
    				}
    			}
    		}
    	}
	}

    @Deprecated
    @Override
    public void removeByPrefix(String key) throws Exception {
        if(StringUtils.isNotBlank(DbContextHolder.getDBType())){
            key = DbContextHolder.getDBType() + ":" + key;
        }
        // 迭代的只是一个keys的快照，因此可以在迭代时删除
        if (null != address && !address.isEmpty()) {
            String cacheKey;// 缓存key
            for (InetSocketAddress inetSocketAddress : address) {
                KeyIterator keyIterator = memcached.getKeyIterator(inetSocketAddress);
                if (null != keyIterator) {
                    while (keyIterator.hasNext()) {
                        cacheKey = keyIterator.next();
                        if (cacheKey.startsWith(key)) {
                            memcached.delete(cacheKey);
                        }
                    }
                }
            }
        }
    }
}