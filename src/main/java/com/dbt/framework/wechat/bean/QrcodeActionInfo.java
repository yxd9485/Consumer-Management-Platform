/**
 * <pre>
 * Copyright Digital Bay Technology Group Co. Ltd.All Rights Reserved.
 *
 * Original Author: hanshimeng
 *
 * ChangeLog:
 * 下午6:49:20 by hanshimeng create
 * </pre>
 */
package com.dbt.framework.wechat.bean;
/**
 * 生成二维码内容bean
 * @auther hanshimeng </br>
 * @version V1.0.0 </br>      
 * @createTime 2018年4月19日 </br>
 */
public class QrcodeActionInfo {

	private QrcodeScene scene;

	public QrcodeActionInfo(){}
	public QrcodeActionInfo(QrcodeScene scene) {
		this.scene = scene;
	}
	
	public QrcodeScene getScene() {
		return scene;
	}

	public void setScene(QrcodeScene scene) {
		this.scene = scene;
	}
	
	
	
}
