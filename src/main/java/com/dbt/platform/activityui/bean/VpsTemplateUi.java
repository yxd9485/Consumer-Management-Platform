package com.dbt.platform.activityui.bean;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

/**
 * 活动模板UIBean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsTemplateUi extends BasicProperties{
	
	/** 主键 **/
	private String templateKey; 
	/** 编号 **/
	private String templateNo;
	/** 模板名称 **/
	private String templateName;
	/** 业务类型：1扫码，2万能签到 **/
	private String businessType;
	/** 时间类型：0每天，1时间段，2节假日 **/
	private String dateType;
	/** 其实日期 **/
	private String startDate;
	/** 结束日期 **/
	private String endDate;
	/** 封面 **/
	private String coverPic;
	/** 模板属性 **/
	private String templateProperty;
	//扫码红包首页
	/** 背景图  **/
	private PicInfo homeBackgroundPic;
	/** slogan宣传语  **/
	private PicInfo homeSloganPic;
	/** 拆红包图 **/
	private PicInfo homeOpenRedpacketPic;
	/** 品牌LOGO图 **/
	private PicInfo homeLogoPic;
	/** 发布状态：默认0，1预发布，2已发 **/
	private String publishStatus;
	/** 关联活动 **/
	private List<VpsActivityTemplateUi> activityTemplateUiList;
	
	
	public VpsTemplateUi() {}
	public VpsTemplateUi(String queryParam) {
		String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.templateName = paramAry.length > 0 ? paramAry[0] : "";
	}
	public String getTemplateKey() {
		return templateKey;
	}
	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}
	public String getTemplateNo() {
		return templateNo;
	}
	public void setTemplateNo(String templateNo) {
		this.templateNo = templateNo;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getCoverPic() {
		return coverPic;
	}
	public void setCoverPic(String coverPic) {
		this.coverPic = coverPic;
	}
	
	public String getTemplateProperty() {
		return templateProperty;
	}
	public void setTemplateProperty(String templateProperty) {
		this.templateProperty = templateProperty;
	}

	public void setActivityTemplateUiList(
			List<VpsActivityTemplateUi> activityTemplateUiList) {
		this.activityTemplateUiList = activityTemplateUiList;
	}

	public PicInfo getHomeBackgroundPic() {
		return homeBackgroundPic;
	}

	public void setHomeBackgroundPic(PicInfo homeBackgroundPic) {
		this.homeBackgroundPic = homeBackgroundPic;
	}

	public PicInfo getHomeSloganPic() {
		return homeSloganPic;
	}

	public void setHomeSloganPic(PicInfo homeSloganPic) {
		this.homeSloganPic = homeSloganPic;
	}

	public PicInfo getHomeOpenRedpacketPic() {
		return homeOpenRedpacketPic;
	}

	public void setHomeOpenRedpacketPic(PicInfo homeOpenRedpacketPic) {
		this.homeOpenRedpacketPic = homeOpenRedpacketPic;
	}

	public PicInfo getHomeLogoPic() {
		return homeLogoPic;
	}

	public void setHomeLogoPic(PicInfo homeLogoPic) {
		this.homeLogoPic = homeLogoPic;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public List<VpsActivityTemplateUi> getActivityTemplateUiList() {
		return activityTemplateUiList;
	}

	public static class PicInfo{
		private String picWidth;
		private String picHeight;
		private String picX;
		private String picY;
		private String picUrl;

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
