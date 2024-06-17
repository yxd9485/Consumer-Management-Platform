package com.dbt.platform.autocode.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.codefactory.bean.VpsVcodeFactory;

/**
 * 码源订单Bean
 * @author hanshimeng
 *
 */
@SuppressWarnings("serial")
public class VpsQrcodeOrder extends BasicProperties{
	
	/** 订单主键 **/
	private String orderKey; 
	/** 企业主键 */
	private String companyKey;
	/** 订单编号 **/
	private String orderNo; 
	/** 客户订单编号 **/
	private String clientOrderNo;
	/** 订单名称 **/
	private String orderName; 
	/** 订单相关SKU **/
	private String skuKey; 
	/** 赋码厂 **/
	private String qrcodeManufacture; 
	/** 啤酒厂 **/
	private String brewery; 
	/** 二维码格式 **/
	private String qrcodeFormat;

    /**
     * 二维码标识
     */
    private String qrcodeType;
	/** 文件格式：默认1txt，2mdb **/
	private String fileFormat; 
	/** 通道数量 **/
	private int channelCount; 
	/** 压缩包密码 **/
	private String packagePassword; 
	/** 是否一万一批次，默认0否，1是 **/
	private String isWanBatch;
	/** 订单状态：默认0未生成，1生成成功，2生成失败，3进行中 **/
	private String orderStatus;
	/** 操作人手机号 **/
	private String userPhone;
	/** 回传状态：0未回传、1已回传*/
	private String importFlag;
	/** 回传时间*/
	private String importTime;
	/** 批次Kek **/
	private String batchKeys;
	/** 订单关联的批次 **/
	private List<VcodeQrcodeBatchInfo> batchInfoList = new ArrayList<VcodeQrcodeBatchInfo>();
	
    // 扩展字段
    private String startDate;
    private String endDate;
    private String skuName;
    private String orderAndImportStatus;
	private String bagName;
	private String bagEmail;
	private String passwordName;
	private String passwordEmail;
	/** 订单总数数量(含损耗比) */
	private long orderTotalQrcodeCount;

	/** 生产厂主键 **/
	private String factoryId;
	/** 生产厂主键**/
	private String wineryId;
	/** 订单日期 **/
	private String orderTime;
	/** 订单预期数量 */
	private long orderQrcodeCount;
	/** 项目中文名 */
	private String projectName;
	/** 项目前缀 */
	private String projectOrderPrefix;
	//是否生成标签
	private String isLabel;
	/** 照片地址 */
	private String imageUrl;
	/** 抄送 */
	private String csEmail;
	
	private String bagTel;
	private String passwordTel;
	/** 三方赋码厂主键*/
	private String qrcodeFactoryId;
	/** 三方赋码厂名称*/
	private String qrcodeFactoryName;
	/** 三方赋码厂产线主键*/
	private String qrcodeProductLineId;
    /** 三方赋码厂产线名称*/
	private String qrcodeProductLineName;
	/** 三方赋码厂班次*/
	private String qrcodeWorkGroup;
	/** 三方赋码厂设备编号*/
	private String qrcodeMachineId;
	/** 回传码源数量*/
	private String qrcodeAmountSum;
	private String vcodeUniqueCode;
	/** 赋码率-百分比*/
	private String qrcodePercent;
	/** 有奖描述*/
	private String prizeDesc;
	/** 无奖描述*/
	private String noPrizeDesc;
	public VpsQrcodeOrder() {
	    super();
	}
	
    public VpsQrcodeOrder(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.orderNo = paramAry.length > 0 ? paramAry[0] : "";
        this.clientOrderNo = paramAry.length > 1 ? paramAry[1] : "";
        this.orderName = paramAry.length > 2 ? paramAry[2] : "";
        this.skuKey = paramAry.length > 3 ? paramAry[3] : "";
        this.qrcodeManufacture = paramAry.length > 4 ? paramAry[4] : "";
        this.brewery = paramAry.length > 5 ? paramAry[5] : "";
        this.orderStatus = paramAry.length > 6 ? paramAry[6] : "";
        this.importFlag = paramAry.length > 7 ? paramAry[7] : "";
        this.startDate = paramAry.length > 8 ? paramAry[8] : "";
        this.endDate = paramAry.length > 9 ? paramAry[9] : "";
    }

    public void setMail(VpsVcodeFactory  factory) {
        this.bagEmail = factory.getBagEmail();
        this.bagName = factory.getBagName();
        this.passwordEmail= factory.getPasswordEmail();
        this.passwordName=  factory.getPasswordName();
        this.isLabel = factory.getIsLabel();    
        this.bagTel = factory.getBagTel();
        this.passwordTel = factory.getPasswordTel();
    }

    public String getQrcodeType() { return qrcodeType; }

    public void setQrcodeType(String qrcodeType) { this.qrcodeType = qrcodeType; }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(String companyKey) {
        this.companyKey = companyKey;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getClientOrderNo() {
        return clientOrderNo;
    }

    public void setClientOrderNo(String clientOrderNo) {
        this.clientOrderNo = clientOrderNo;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getSkuKey() {
        return skuKey;
    }

    public void setSkuKey(String skuKey) {
        this.skuKey = skuKey;
    }

    public String getQrcodeManufacture() {
        return qrcodeManufacture;
    }

    public void setQrcodeManufacture(String qrcodeManufacture) {
        this.qrcodeManufacture = qrcodeManufacture;
    }

    public String getBrewery() {
        return brewery;
    }

    public void setBrewery(String brewery) {
        this.brewery = brewery;
    }

    public String getQrcodeFormat() {
        return qrcodeFormat;
    }

    public void setQrcodeFormat(String qrcodeFormat) {
        this.qrcodeFormat = qrcodeFormat;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public String getPackagePassword() {
        return packagePassword;
    }

    public void setPackagePassword(String packagePassword) {
        this.packagePassword = packagePassword;
    }

    public String getIsWanBatch() {
        return isWanBatch;
    }

    public void setIsWanBatch(String isWanBatch) {
        this.isWanBatch = isWanBatch;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getBatchKeys() {
        return batchKeys;
    }

    public void setBatchKeys(String batchKeys) {
        this.batchKeys = batchKeys;
    }

    public List<VcodeQrcodeBatchInfo> getBatchInfoList() {
        return batchInfoList;
    }

    public void setBatchInfoList(List<VcodeQrcodeBatchInfo> batchInfoList) {
        this.batchInfoList = batchInfoList;
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

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getImportFlag() {
        return importFlag;
    }

    public void setImportFlag(String importFlag) {
        this.importFlag = importFlag;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getOrderAndImportStatus() {
        return orderAndImportStatus;
    }

    public void setOrderAndImportStatus(String orderAndImportStatus) {
        this.orderAndImportStatus = orderAndImportStatus;
    }

	public String getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(String factoryId) {
		this.factoryId = factoryId;
	}

	public String getWineryId() {
		return wineryId;
	}

	public void setWineryId(String wineryId) {
		this.wineryId = wineryId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}



	public long getOrderQrcodeCount() {
		return orderQrcodeCount;
	}

	public void setOrderQrcodeCount(long orderQrcodeCount) {
		this.orderQrcodeCount = orderQrcodeCount;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectOrderPrefix() {
		return projectOrderPrefix;
	}

	public void setProjectOrderPrefix(String projectOrderPrefix) {
		this.projectOrderPrefix = projectOrderPrefix;
	}

	public String getIsLabel() {
		return isLabel;
	}

	public void setIsLabel(String isLabel) {
		this.isLabel = isLabel;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getBagName() {
		return bagName;
	}

	public void setBagName(String bagName) {
		this.bagName = bagName;
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

	public String getPasswordEmail() {
		return passwordEmail;
	}

	public void setPasswordEmail(String passwordEmail) {
		this.passwordEmail = passwordEmail;
	}

	
	
	public String getCsEmail() {
		return csEmail;
	}

	public void setCsEmail(String csEmail) {
		this.csEmail = csEmail;
	}

	
	
	public long getOrderTotalQrcodeCount() {
		return orderTotalQrcodeCount;
	}

	public void setOrderTotalQrcodeCount(long orderTotalQrcodeCount) {
		this.orderTotalQrcodeCount = orderTotalQrcodeCount;
	}
	
	

	public String getBagTel() {
		return bagTel;
	}

	public void setBagTel(String bagTel) {
		this.bagTel = bagTel;
	}

	public String getPasswordTel() {
		return passwordTel;
	}

	public void setPasswordTel(String passwordTel) {
		this.passwordTel = passwordTel;
	}

	public String getQrcodeFactoryId() {
        return qrcodeFactoryId;
    }

    public void setQrcodeFactoryId(String qrcodeFactoryId) {
        this.qrcodeFactoryId = qrcodeFactoryId;
    }

    public String getQrcodeFactoryName() {
        return qrcodeFactoryName;
    }

    public void setQrcodeFactoryName(String qrcodeFactoryName) {
        this.qrcodeFactoryName = qrcodeFactoryName;
    }

    public String getQrcodeProductLineId() {
        return qrcodeProductLineId;
    }

    public void setQrcodeProductLineId(String qrcodeProductLineId) {
        this.qrcodeProductLineId = qrcodeProductLineId;
    }

    public String getQrcodeProductLineName() {
        return qrcodeProductLineName;
    }

    public void setQrcodeProductLineName(String qrcodeProductLineName) {
        this.qrcodeProductLineName = qrcodeProductLineName;
    }

    public String getQrcodeWorkGroup() {
        return qrcodeWorkGroup;
    }

    public void setQrcodeWorkGroup(String qrcodeWorkGroup) {
        this.qrcodeWorkGroup = qrcodeWorkGroup;
    }

    public String getQrcodeMachineId() {
        return qrcodeMachineId;
    }

    public void setQrcodeMachineId(String qrcodeMachineId) {
        this.qrcodeMachineId = qrcodeMachineId;
    }

    public String getQrcodeAmountSum() {
        return qrcodeAmountSum;
    }

    public void setQrcodeAmountSum(String qrcodeAmountSum) {
        this.qrcodeAmountSum = qrcodeAmountSum;
    }

    public String getVcodeUniqueCode() {
        return vcodeUniqueCode;
    }

    public void setVcodeUniqueCode(String vcodeUniqueCode) {
        this.vcodeUniqueCode = vcodeUniqueCode;
    }

    public String getQrcodePercent() {
        return qrcodePercent;
    }

    public void setQrcodePercent(String qrcodePercent) {
        this.qrcodePercent = qrcodePercent;
    }

    public String getPrizeDesc() {
        return prizeDesc;
    }

    public void setPrizeDesc(String prizeDesc) {
        this.prizeDesc = prizeDesc;
    }

    public String getNoPrizeDesc() {
        return noPrizeDesc;
    }

    public void setNoPrizeDesc(String noPrizeDesc) {
        this.noPrizeDesc = noPrizeDesc;
    }
}
