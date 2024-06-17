/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午6:52:28 by hanshimeng create
 * </pre>
 */
package com.dbt.framework.wechat.bean;

/**
 * 生成二维码内容场景
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2018年4月19日 </br>
 */
public class QrcodeScene {

	// scene_id	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
	// scene_str	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
	
	private String scene_id;
	private String scene_str;
	
	public QrcodeScene(){}
	public QrcodeScene(String scene_id, String scene_str) {
		this.scene_id = scene_id;
		this.scene_str = scene_str;
	}
	public String getScene_id() {
		return scene_id;
	}
	public void setScene_id(String scene_id) {
		this.scene_id = scene_id;
	}
	public String getScene_str() {
		return scene_str;
	}
	public void setScene_str(String scene_str) {
		this.scene_str = scene_str;
	}
	
}
