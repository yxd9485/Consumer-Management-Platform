package com.dbt.framework.securityauth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* 文件名：AuthAnnotation.java<br>
* 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br> 
* 描述: 安全认证注解<br>
* 修改人: 谷长鹏<br>
* 修改时间：2014-06-20 11:10:16<br>
* 修改内容：新增<br>
*/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionAnnotation {
	
	/**
	 * 方法对应的code值，与数据库中保持一致
	 * @return 
	 */
	String code() default "";
}
