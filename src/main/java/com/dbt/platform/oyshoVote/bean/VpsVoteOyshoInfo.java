package com.dbt.platform.oyshoVote.bean;



import com.dbt.framework.base.bean.BasicProperties;


/**
 * 投票导购员Bean
 */
public class VpsVoteOyshoInfo extends BasicProperties {
    private static final long serialVersionUID = 1L;
    /** 导购员主键 */
    private int oyshoKey;
    /** 导购员名称 */
    private String oyshoName;
    /** 导购员编号*/
    private String oyshoNo;
    private String userKey;
    /** 导购员图片 */
    private String imageUrl;
    /** 导购员授权图片 */
    private String sgsImageUrl;
    /** 省 */
    private String province;
    /** 市 */
    private String city;
    /** 县*/
    private String county;
    /** 导购员地址*/
    private String address;
    /** 所属战区 **/
    private String warAreaName;
    /** 导购员介绍*/
    private String introduce;
    /** 导购员投票*/
    private int vote;
    /** 虚拟投票*/
    private int virtualVote;
    /** 虚拟用户数*/
    private int virtualUser;
    /** 展示图片，多个以英文逗号分隔*/
    private String uploadPic;
    
    /** 排名 **/
    private int ranking;
    /** 编号:00001 **/
    private String oyshoNumber;
    /** 导购员下用户当天投票数 **/
    private int oyshoUserDayVote;
    private int firstImgH;
    private String ticketChannel;
    
    public VpsVoteOyshoInfo() {}
    public VpsVoteOyshoInfo(String oyshoNo, String userKey, String oyshoName, String imageUrl, String sgsImageUrl, String province, 
    		String city, String county, String address, String introduce, String uploadPic, String warAreaName, int firstImgH, String ticketChannel) {
    	this.oyshoNo = oyshoNo;
    	this.userKey = userKey;
    	this.oyshoName = oyshoName;
    	this.imageUrl = imageUrl;
    	this.sgsImageUrl = sgsImageUrl;
    	this.province = province;
    	this.city = city;
    	this.county = county;
    	this.address = address;
    	this.introduce = introduce;
    	this.uploadPic = uploadPic;
    	this.warAreaName = warAreaName;
    	this.firstImgH = firstImgH;
    	this.ticketChannel = ticketChannel;
    	fillFields(this.userKey);
    }
    
	public int getOyshoKey() {
		return oyshoKey;
	}
	public void setOyshoKey(int oyshoKey) {
		this.oyshoKey = oyshoKey;
	}
	public String getOyshoName() {
		return oyshoName;
	}
	public void setOyshoName(String oyshoName) {
		this.oyshoName = oyshoName;
	}
	public String getOyshoNo() {
		return oyshoNo;
	}
	public void setOyshoNo(String oyshoNo) {
		this.oyshoNo = oyshoNo;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSgsImageUrl() {
		return sgsImageUrl;
	}
	public void setSgsImageUrl(String sgsImageUrl) {
		this.sgsImageUrl = sgsImageUrl;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIntroduce() {
		return introduce;
	}
	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public int getVirtualVote() {
		return virtualVote;
	}
	public void setVirtualVote(int virtualVote) {
		this.virtualVote = virtualVote;
	}
	public int getVirtualUser() {
		return virtualUser;
	}
	public void setVirtualUser(int virtualUser) {
		this.virtualUser = virtualUser;
	}
	public String getUploadPic() {
		return uploadPic;
	}
	public void setUploadPic(String uploadPic) {
		this.uploadPic = uploadPic;
	}
	public int getRanking() {
		return ranking;
	}
	public void setRanking(int ranking) {
		this.ranking = ranking;
	}
	public String getOyshoNumber() {
		return oyshoNumber;
	}
	public void setOyshoNumber(String oyshoNumber) {
		this.oyshoNumber = oyshoNumber;
	}
	public int getOyshoUserDayVote() {
		return oyshoUserDayVote;
	}
	public void setOyshoUserDayVote(int oyshoUserDayVote) {
		this.oyshoUserDayVote = oyshoUserDayVote;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getWarAreaName() {
		return warAreaName;
	}
	public void setWarAreaName(String warAreaName) {
		this.warAreaName = warAreaName;
	}
    public int getFirstImgH() {
        return firstImgH;
    }
    public void setFirstImgH(int firstImgH) {
        this.firstImgH = firstImgH;
    }
    public String getTicketChannel() {
        return ticketChannel;
    }
    public void setTicketChannel(String ticketChannel) {
        this.ticketChannel = ticketChannel;
    }
	
}
