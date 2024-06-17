/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午6:05:23 by hanshimeng create
 * </pre>
 */
package com.dbt.framework.wechat.bean;

import java.io.Serializable;

/**
 * 生成二维码Bean	
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2018年4月19日 </br>
 */
@SuppressWarnings("serial")
public class CreateQrcode implements Serializable{

//	expire_seconds	该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
//	action_name	二维码类型，QR_SCENE为临时的整型参数值，QR_STR_SCENE为临时的字符串参数值，QR_LIMIT_SCENE为永久的整型参数值，QR_LIMIT_STR_SCENE为永久的字符串参数值
//	action_info	二维码详细信息
//	scene_id	场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）
//	scene_str	场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
	
	private String expire_seconds;
	private String action_name;
	private QrcodeActionInfo action_info;
	
	public CreateQrcode(){}
	
	public CreateQrcode(String expire_seconds, String action_name, QrcodeActionInfo actionInfo) {
		this.expire_seconds = expire_seconds;
		this.action_name = action_name;
		this.action_info = actionInfo;
	}

	public QrcodeActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(QrcodeActionInfo action_info) {
		this.action_info = action_info;
	}

	public String getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(String expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getAction_name() {
		return action_name;
	}
	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}
}
