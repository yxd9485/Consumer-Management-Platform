package com.dbt.platform.codefactory.bean;

import org.springframework.util.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;

public class VpsVcodeFactory extends BasicProperties {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String factoryName;
	private String factoryShort;
	private String factoryType;
	private int channelCount;
	private String projectServer;
	private String projectServerName;
	private String serverWinery;
	private String serverWineryName;
	private String lossRatio;
	private Long batchNum;
	private String bagName;
	private String bagTel;
	private String bagEmail;
	private String passwordName;
	private String passwordTel;
	private String passwordEmail;
	private String deleteFlag;
	/** 是否一万一批次，默认0否，1是 **/
	private String isWanBatch;
	//是否生成标签
	private String isLabel;
	/** 二维码格式 **/
	private String qrcodeFormat; 
	/** 是否合并码包文件，0不合并、1合并*/
	private String qrcodeMerageFlag;
	
	public VpsVcodeFactory() {
	}
	
	public VpsVcodeFactory(String params) {
		if (!StringUtils.isEmpty(params)) {
			String[] values = params.split(",");
			this.factoryName = values.length > 0 ? values[0] : null;
			this.projectServer = values.length > 1 ? values[1] : null;
			this.factoryType = values.length > 2 ? values[2] : null;
			this.serverWinery = values.length > 3 ? values[3] : null;
		}
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	
	public String getFactoryShort() {
		return factoryShort;
	}

	public void setFactoryShort(String factoryShort) {
		this.factoryShort = factoryShort;
	}

	public String getFactoryType() {
		return factoryType;
	}
	public void setFactoryType(String factoryType) {
		this.factoryType = factoryType;
	}

	public int getChannelCount() {
		return channelCount;
	}

	public void setChannelCount(int channelCount) {
		this.channelCount = channelCount;
	}

	public String getServerWinery() {
		return serverWinery;
	}
	public void setServerWinery(String serverWinery) {
		this.serverWinery = serverWinery;
	}
	public String getLossRatio() {
		return lossRatio;
	}
	public void setLossRatio(String lossRatio) {
		this.lossRatio = lossRatio;
	}

	public Long getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(Long batchNum) {
		this.batchNum = batchNum;
	}

	public String getBagName() {
		return bagName;
	}
	public void setBagName(String bagName) {
		this.bagName = bagName;
	}
	public String getBagTel() {
		return bagTel;
	}
	public void setBagTel(String bagTel) {
		this.bagTel = bagTel;
	}
	public String getBagEmail() {
		return bagEmail;
	}
	public void setBagEmail(String bagEmail) {
		this.bagEmail = bagEmail;
	}
	public String getPasswordName() {
		return passwordName;
	}
	public void setPasswordName(String passwordName) {
		this.passwordName = passwordName;
	}
	public String getPasswordTel() {
		return passwordTel;
	}
	public void setPasswordTel(String passwordTel) {
		this.passwordTel = passwordTel;
	}
	public String getPasswordEmail() {
		return passwordEmail;
	}
	public void setPasswordEmail(String passwordEmail) {
		this.passwordEmail = passwordEmail;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getProjectServer() {
		return projectServer;
	}

	public void setProjectServer(String projectServer) {
		this.projectServer = projectServer;
	}

	public String getProjectServerName() {
		return projectServerName;
	}

	public void setProjectServerName(String projectServerName) {
		this.projectServerName = projectServerName;
	}

	public String getServerWineryName() {
		return serverWineryName;
	}

	public void setServerWineryName(String serverWineryName) {
		this.serverWineryName = serverWineryName;
	}

	public String getIsLabel() {
		return isLabel;
	}

	public void setIsLabel(String isLabel) {
		this.isLabel = isLabel;
	}

	public String getQrcodeFormat() {
		return qrcodeFormat;
	}

	public void setQrcodeFormat(String qrcodeFormat) {
		this.qrcodeFormat = qrcodeFormat;
	}

	public String getIsWanBatch() {
		return isWanBatch;
	}

	public void setIsWanBatch(String isWanBatch) {
		this.isWanBatch = isWanBatch;
	}

    public String getQrcodeMerageFlag() {
        return qrcodeMerageFlag;
    }

    public void setQrcodeMerageFlag(String qrcodeMerageFlag) {
        this.qrcodeMerageFlag = qrcodeMerageFlag;
    }
}
    
