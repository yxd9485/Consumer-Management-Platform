package com.dbt.framework.cache.bean;


/**
 * 文件名:MirrorCacheUtil.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.
 * 描述: 缓存读取记录<br>
 * 修改人: jiquanwei<br>
 * 修改时间：2014-11-21 14:29:20<br>
 * 修改内容：新增<br>
 */
public class CacheReadRecord{
	
	// 缓存key
	private String key;
	// 缓存value
	private String value;
	// 有效期
	private int expiration;
	// 更新时间
	private long modifyTime;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getExpiration() {
		return expiration;
	}
	public void setExpiration(int expiration) {
		this.expiration = expiration;
	}
	public long getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(long modifyTime) {
		this.modifyTime = modifyTime;
	}
}