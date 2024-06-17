package com.dbt.platform.activityui.bean;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 模板属性Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class TemplateProperty extends BasicProperties{
	
	/** 背景图  **/
	private  PicInfo backgroundPic;
	/** slogan宣传语  **/
	private  PicInfo sloganPic;
	/** 拆红包图 **/
	private  PicInfo openRedpacketPic;
	/** 品牌LOGO图 **/
	private  PicInfo logoPic;

	public PicInfo getBackgroundPic() {
		return backgroundPic;
	}

	public void setBackgroundPic(PicInfo backgroundPic) {
		this.backgroundPic = backgroundPic;
	}

	public PicInfo getSloganPic() {
		return sloganPic;
	}

	public void setSloganPic(PicInfo sloganPic) {
		this.sloganPic = sloganPic;
	}

	public PicInfo getOpenRedpacketPic() {
		return openRedpacketPic;
	}

	public void setOpenRedpacketPic(PicInfo openRedpacketPic) {
		this.openRedpacketPic = openRedpacketPic;
	}

	public PicInfo getLogoPic() {
		return logoPic;
	}

	public void setLogoPic(PicInfo logoPic) {
		this.logoPic = logoPic;
	}

	private static class PicInfo{
		private String picName;
		private String picWidth;
		private String picHeight;
		private String picX;
		private String picY;
		private String picUrl;

		public String getPicName() {
			return picName;
		}

		public void setPicName(String picName) {
			this.picName = picName;
		}

		public String getPicWidth() {
			return picWidth;
		}

		public void setPicWidth(String picWidth) {
			this.picWidth = picWidth;
		}

		public String getPicHeight() {
			return picHeight;
		}

		public void setPicHeight(String picHeight) {
			this.picHeight = picHeight;
		}

		public String getPicX() {
			return picX;
		}

		public void setPicX(String picX) {
			this.picX = picX;
		}

		public String getPicY() {
			return picY;
		}

		public void setPicY(String picY) {
			this.picY = picY;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}
	}
}
