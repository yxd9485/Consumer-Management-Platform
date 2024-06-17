package com.dbt.platform.activity.bean;



import com.dbt.framework.base.bean.BasicProperties;

/**
 * 门店大奖关系bean
 */
public class VpsPrizeTerminalRelation extends BasicProperties {
    
    private static final long serialVersionUID = 1L;
    /** 主键 */
    private String relationKey;
    /** 大奖类型 */
    private String prizeType;;
    /** 门店主键 */
    private String terminalKey;
    private String status;
    public VpsPrizeTerminalRelation() {
		
	}
    
    public VpsPrizeTerminalRelation(String terminalKey, String prizeType) {
		this.terminalKey = terminalKey;
		this.prizeType = prizeType;
   	}
    
	public String getRelationKey() {
		return relationKey;
	}
	public void setRelationKey(String relationKey) {
		this.relationKey = relationKey;
	}
	public String getPrizeType() {
		return prizeType;
	}
	public void setPrizeType(String prizeType) {
		this.prizeType = prizeType;
	}
	public String getTerminalKey() {
		return terminalKey;
	}
	public void setTerminalKey(String terminalKey) {
		this.terminalKey = terminalKey;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
    
}
