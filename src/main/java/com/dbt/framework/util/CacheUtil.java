package com.dbt.framework.util;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

import com.dbt.framework.cache.service.IMemcachedClient;
import com.dbt.framework.cache.service.impl.XmemcachedClient;
import com.dbt.web.VjifenManageConfig;

/**
 * 缓存基础工具类
 * <pre>
 * Copyright (c) Digital Bay Technology Group. Co. Ltd. All Rights Reserved
 *
 * Original Author: sunshunbo
 *
 * ChangeLog:
 * 2015-5-21 by sunshunbo create
 * </pre>
 */
public class CacheUtil {
   private static IMemcachedClient memcachedClient=new XmemcachedClient(new VjifenManageConfig());
   
   public static  boolean isInitialized(){
	   return memcachedClient.isInitialized();
   }

   public static void initialize(List<InetSocketAddress> addresses) throws IOException{
	   memcachedClient.initialize(addresses);
   }

   public static <T> void set(String key, int secondsToExpire, T value) throws IOException{
	   memcachedClient.set(key, secondsToExpire, value);
   }

   public static <T> T get(String key) throws IOException{
	  return  memcachedClient.get(key);
   }

   public static void delete(String key) throws IOException{
	   memcachedClient.delete(key);
   }
   
   @Deprecated
   public static void removeByRegex(String key) throws Exception{
	   memcachedClient.removeByRegex(key);
   }

}
