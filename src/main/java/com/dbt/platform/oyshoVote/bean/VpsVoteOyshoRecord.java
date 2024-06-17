package com.dbt.platform.oyshoVote.bean;



import com.dbt.framework.base.bean.BasicProperties;
import com.dbt.framework.util.DateUtil;

/**
 * 投票记录Bean
 */
public class VpsVoteOyshoRecord extends BasicProperties {
    private static final long serialVersionUID = 1L;
    /** 记录主键 */
    private String infoKey;
    /** 用户主键 */
    private String userKey;
    /** 分享人主键*/
    private String shareUserKey;
    /** 导购员主键 */
    private int oyshoKey;
    /** 导购员投票*/
    private int vote;
    /** 投票日期 **/
    private String createDate;
    
    public VpsVoteOyshoRecord() {}
    public VpsVoteOyshoRecord(String userKey, String shareUserKey, int oyshoKey, int vote) {
    	this.userKey = userKey;
    	this.shareUserKey = shareUserKey;
    	this.oyshoKey = oyshoKey;
    	this.vote = vote;
    	this.createDate = DateUtil.getDate();
    	this.setCreateTime(DateUtil.getDateTime());
    }
	public String getInfoKey() {
		return infoKey;
	}
	public void setInfoKey(String infoKey) {
		this.infoKey = infoKey;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}
	public String getShareUserKey() {
		return shareUserKey;
	}
	public void setShareUserKey(String shareUserKey) {
		this.shareUserKey = shareUserKey;
	}
	public int getOyshoKey() {
		return oyshoKey;
	}
	public void setOyshoKey(int oyshoKey) {
		this.oyshoKey = oyshoKey;
	}
	public int getVote() {
		return vote;
	}
	public void setVote(int vote) {
		this.vote = vote;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
