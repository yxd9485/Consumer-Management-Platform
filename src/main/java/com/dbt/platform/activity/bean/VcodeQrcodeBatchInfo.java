package com.dbt.platform.activity.bean;

import org.apache.commons.lang.StringUtils;

import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.base.bean.Constant;

/**
 * 文件名: VcodeQrcodeBatchInfo.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: V码二维码批次信息bean<br>
 * 修改人: cpgu<br>
 * 修改时间：2016-04-22 10:30:15<br>
 * 修改内容：新增<br>
 */
public class VcodeQrcodeBatchInfo extends BasicProperties {

	/** **/
	private static final long serialVersionUID = 1L;
	/** */
	private String batchKey;
	/** 主键 */
	private String vcodeActivityKey;
	/** 客户订单号 */
	private String clientOrderNo;
	/** 导入二维码批次编号 **/
	private String batchNo;
	/** 批次描述 */
	private String batchDesc;
    /** 批次名称 */
    private String batchName;
	/** 起始时间 */
	private String startDate;
	/** 结束时间 */
	private String endDate;
	/** 码包数量 */
	private Long packAmounts;
	/** 生成码状态：0：未生成；1：已生成 */
	private String vcodeFlag;
	/** 每包中，二维码数量 **/
	private Long qrcodeAmounts;
	
	
	/** V码数量 **/
	private Long vcodeCounts;
	/** 实际赋码入库量*/
	private long realVcodeCounts;
	/** 已扫码个数 **/
	private Long scanCodeNum;
	/** 未扫码个数 **/
	private Long notScanCodeNum;
	/** V码活动积分分布配置 **/
	private VcodePacksRecord packsRecord;
	/** 是否开始 0 待上线；1 已上线；2 已下线； 3：预激活 */
	private String isBegin;
	/** 码包是否导入成功  默认0 否  1 是**/
	private String importFlag;
	/** 导入码包文件名称 **/
	private String fileName;
    private String activateOpenid;
    private String activateUserName;
    private String activatePhone;
    private String activateFactoryName;
    private String activateTime;
    private String province;
    private String city;
    private String county;
    private String longitude;
    private String latitude;
    
 	private String companyName;
 	private String shortName; 
 	private String skuName;
 	private String skuKey;
 	private String orderKey;
 	private String companyKey;
 	private String libName;
 	private String vcodeUniqueCode;
 	private String orderNo;
 	private String importTime;
    /** 每个活动下批次数 **/
    private String total;
    private String activityName;
    private String activityNo;
    private String adjustUser;
    private String adjustTime;
    private String activateStartDate;
    private String activateEndDate;
    private String activityType;
    private String batchStatus;
    private boolean expireWarning;
    
    /** 批次状态：0新建、1创建码源订单、2已回传、3已挂接活动 */
    private String status;
    /** 复制批次个数*/
    private int batchCopyNum;
    private String brewery;
    /** 激活状态**/
    private String activationStatus;
    private String contractYear; //所属合同年份
    private String contractChangeDate; // 下一个所属合同年份切换日期
    private String nextContractYear; // 下一个所属合同年份
    
    private int id;
    /** 小批次共用碼库的批次主键 */
    private String smallBatchParentKey;
    private int batchStartAutoNo;

    public VcodeQrcodeBatchInfo() {
        super();
    }
    
    public VcodeQrcodeBatchInfo(int id, String batchDesc, String batchName, Long qrcodeAmounts,long packAmounts) {
		this.id= id;
		this.batchDesc = batchDesc;
		this.batchName = batchName;
		this.qrcodeAmounts = qrcodeAmounts;
		this.packAmounts = packAmounts;
		
	}

    public VcodeQrcodeBatchInfo(String queryParam, String qrcodeBatchType) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        if (Constant.QrcodeBatchType.type_0.equals(qrcodeBatchType)) {
            this.clientOrderNo = paramAry.length > 0 ? paramAry[0] : "";
            this.batchDesc = paramAry.length > 1 ? paramAry[1] : "";
            this.batchName = paramAry.length > 2 ? paramAry[2] : "";
            
        } else if (Constant.QrcodeBatchType.type_2.equals(qrcodeBatchType)) {
            this.orderNo = paramAry.length > 0 ? paramAry[0] : "";
            this.clientOrderNo = paramAry.length > 1 ? paramAry[1] : "";
            this.batchNo = paramAry.length > 2 ? paramAry[2] : "";
            this.batchDesc = paramAry.length > 3 ? paramAry[3] : "";
            this.batchName = paramAry.length > 4 ? paramAry[4] : "";
            
        } else if (Constant.QrcodeBatchType.type_4.equals(qrcodeBatchType)) {
            this.skuKey = paramAry.length > 0 ? paramAry[0] : "";
            this.activateFactoryName = paramAry.length > 1 ? paramAry[1] : "";
            this.batchDesc = paramAry.length > 2 ? paramAry[2].trim() : "";
            this.activateUserName = paramAry.length > 3 ? paramAry[3] : "";
            this.activatePhone = paramAry.length > 4 ? paramAry[4] : "";
            this.activateStartDate = paramAry.length > 5 ? paramAry[5] : "";
            this.activateEndDate = paramAry.length > 6 ? paramAry[6] : "";
            this.batchName = paramAry.length > 7 ? paramAry[7].trim() : "";
            this.activationStatus = paramAry.length >8 ? paramAry[8].trim() : "";
            
        } else if (Constant.QrcodeBatchType.type_5.equals(qrcodeBatchType)) {
            this.orderNo = paramAry.length > 0 ? paramAry[0] : "";
            this.clientOrderNo = paramAry.length > 1 ? paramAry[1] : "";
            this.batchNo = paramAry.length > 2 ? paramAry[2] : "";
            this.batchDesc = paramAry.length > 3 ? paramAry[3] : "";
            this.batchName = paramAry.length > 4 ? paramAry[4] : "";
            this.activityName = paramAry.length > 5 ? paramAry[5] : "";
            this.activityNo = paramAry.length > 6 ? paramAry[6] : "";
            this.startDate = paramAry.length > 7 ? paramAry[7] : "";
            this.endDate = paramAry.length > 8 ? paramAry[8] : "";
            this.activateStartDate = paramAry.length > 9 ? paramAry[9] : "";
            this.activateEndDate = paramAry.length > 10 ? paramAry[10] : "";
            
        } else if (Constant.QrcodeBatchType.type_6.equals(qrcodeBatchType)) {
            this.orderNo = paramAry.length > 0 ? paramAry[0] : "";
            this.clientOrderNo = paramAry.length > 1 ? paramAry[1] : "";
            this.batchNo = paramAry.length > 2 ? paramAry[2] : "";
            this.batchDesc = paramAry.length > 3 ? paramAry[3] : "";
            this.batchName = paramAry.length > 4 ? paramAry[4] : "";
            this.startDate = paramAry.length > 5 ? paramAry[5] : "";
            this.endDate = paramAry.length > 6 ? paramAry[6] : "";
            this.activateStartDate = paramAry.length > 7 ? paramAry[7] : "";
            this.activateEndDate = paramAry.length > 8 ? paramAry[8] : "";
            this.batchStatus = paramAry.length > 9 ? paramAry[9] : "";
        }
    }

    public String getActivationStatus () {
        return activationStatus;
    }
    
    public void setActivationStatus (String activationStatus) {
        this.activationStatus = activationStatus;
    }

    public String getBrewery () {
        return brewery;
    }
    
    public void setBrewery (String brewery) {
        this.brewery = brewery;
    }

    public int getBatchCopyNum() {
        return batchCopyNum;
    }

    public void setBatchCopyNum(int batchCopyNum) {
        this.batchCopyNum = batchCopyNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityNo() {
        return activityNo;
    }

    public void setActivityOrderNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getAdjustUser() {
        return adjustUser;
    }

    public void setAdjustUser(String adjustUser) {
        this.adjustUser = adjustUser;
    }

    public void setActivityNo(String activityNo) {
        this.activityNo = activityNo;
    }

    public String getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(String adjustTime) {
        this.adjustTime = adjustTime;
    }

    public String getActivateStartDate() {
        return activateStartDate;
    }

    public void setActivateStartDate(String activateStartDate) {
        this.activateStartDate = activateStartDate;
    }

    public String getActivateEndDate() {
        return activateEndDate;
    }

    public void setActivateEndDate(String activateEndDate) {
        this.activateEndDate = activateEndDate;
    }

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
	public String getClientOrderNo() {
        return clientOrderNo;
    }

    public void setClientOrderNo(String clientOrderNo) {
        this.clientOrderNo = clientOrderNo;
    }

    public String getVcodeUniqueCode() {
		return vcodeUniqueCode;
	}

	public void setVcodeUniqueCode(String vcodeUniqueCode) {
		this.vcodeUniqueCode = vcodeUniqueCode;
	}

	public String getLibName() {
		return libName;
	}

	public void setLibName(String libName) {
		this.libName = libName;
	}

	public String getCompanyKey() {
		return companyKey;
	}

	public void setCompanyKey(String companyKey) {
		this.companyKey = companyKey;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getSkuKey() {
		return skuKey;
	}

	public void setSkuKey(String skuKey) {
		this.skuKey = skuKey;
	}

	public String getActivateUserName() {
		return activateUserName;
	}

	public void setActivateUserName(String activateUserName) {
		this.activateUserName = activateUserName;
	}

	public String getActivateFactoryName() {
		return activateFactoryName;
	}

	public void setActivateFactoryName(String activateFactoryName) {
		this.activateFactoryName = activateFactoryName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getActivateOpenid() {
        return activateOpenid;
    }

    public void setActivateOpenid(String activateOpenid) {
        this.activateOpenid = activateOpenid;
    }

    public String getActivatePhone() {
        return activatePhone;
    }

    public void setActivatePhone(String activatePhone) {
        this.activatePhone = activatePhone;
    }

    public String getActivateTime() {
        return activateTime;
    }

    public void setActivateTime(String activateTime) {
        this.activateTime = activateTime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getImportFlag() {
		return importFlag;
	}

	public void setImportFlag(String importFlag) {
		this.importFlag = importFlag;
	}

	public String getIsBegin() {
		return isBegin;
	}

	public void setIsBegin(String isBegin) {
		this.isBegin = isBegin;
	}

	public Long getScanCodeNum() {
		return scanCodeNum;
	}

	public void setScanCodeNum(Long scanCodeNum) {
		this.scanCodeNum = scanCodeNum;
	}

	public Long getNotScanCodeNum() {
		return notScanCodeNum;
	}

	public void setNotScanCodeNum(Long notScanCodeNum) {
		this.notScanCodeNum = notScanCodeNum;
	}

	public VcodePacksRecord getPacksRecord() {
		return packsRecord;
	}

	public void setPacksRecord(VcodePacksRecord packsRecord) {
		this.packsRecord = packsRecord;
	}

	public Long getVcodeCounts() {
		return vcodeCounts;
	}

	public void setVcodeCounts(Long vcodeCounts) {
		this.vcodeCounts = vcodeCounts;
	}
	
	public long getRealVcodeCounts() {
        return realVcodeCounts;
    }

    public void setRealVcodeCounts(long realVcodeCounts) {
        this.realVcodeCounts = realVcodeCounts;
    }

    public Long getQrcodeAmounts() {
		return qrcodeAmounts;
	}

	public void setQrcodeAmounts(Long qrcodeAmounts) {
		this.qrcodeAmounts = qrcodeAmounts;
	}

	public String getBatchKey() {
		return batchKey;
	}

	public void setBatchKey(String batchKey) {
		this.batchKey = batchKey;
	}

	public String getVcodeActivityKey() {
		return vcodeActivityKey;
	}

	public void setVcodeActivityKey(String vcodeActivityKey) {
		this.vcodeActivityKey = vcodeActivityKey;
	}

	public String getBatchDesc() {
		return batchDesc;
	}

	public void setBatchDesc(String batchDesc) {
		this.batchDesc = batchDesc;
	}

	public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
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

    public Long getPackAmounts() {
		return packAmounts;
	}

	public void setPackAmounts(Long packAmounts) {
		this.packAmounts = packAmounts;
	}

	public String getVcodeFlag() {
		return vcodeFlag;
	}

	public void setVcodeFlag(String vcodeFlag) {
		this.vcodeFlag = vcodeFlag;
	}

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public boolean isExpireWarning() {
        return expireWarning;
    }

    public void setExpireWarning(boolean expireWarning) {
        this.expireWarning = expireWarning;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

    public String getContractYear() {
        return contractYear;
    }

    public void setContractYear(String contractYear) {
        this.contractYear = contractYear;
    }

    public String getContractChangeDate() {
        return contractChangeDate;
    }

    public void setContractChangeDate(String contractChangeDate) {
        this.contractChangeDate = contractChangeDate;
    }

    public String getNextContractYear() {
        return nextContractYear;
    }

    public void setNextContractYear(String nextContractYear) {
        this.nextContractYear = nextContractYear;
    }

    public String getSmallBatchParentKey() {
        return smallBatchParentKey;
    }

    public void setSmallBatchParentKey(String smallBatchParentKey) {
        this.smallBatchParentKey = smallBatchParentKey;
    }

    public int getBatchStartAutoNo() {
        return batchStartAutoNo;
    }

    public void setBatchStartAutoNo(int batchStartAutoNo) {
        this.batchStartAutoNo = batchStartAutoNo;
    }

}
