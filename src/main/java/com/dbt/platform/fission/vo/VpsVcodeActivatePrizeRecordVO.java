package com.dbt.platform.fission.vo;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author wangshuda
 * @since 2022-01-12
 */
@Data
public class VpsVcodeActivatePrizeRecordVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String userKey;

    /**
     * 中奖用户id
     */
    private String openid;

    /**
     * 中奖人昵称
     */
    private String nickName;

    /**
     * 中奖二维码
     */
    private String winQrcodeContent;

    /**
     * 中奖产品
     */
    private String winSkuName;

    /**
     * 中奖sku key
     */
    private String winSkuKey;
    /**
     * 规则名称
     */
    private String ruleName;

    /**
     * 中奖状态
     */
    private String status;

    /**
     * 中出金额
     */
    private String winningAmount;

    /**
     * 中出积分
     */
    private String winningPoint;

    /**
     * 分享人userKey
     */
    private String shareUserKey;

    /**
     * 分享人昵称
     */
    private String shareUserName;

    /**
     * 中奖时间
     */
    private Date winDate;

    /**
     * 结束时间
     */
    private Date winEndTime;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;
    private String startDate;
    private String endDate;
    /**
     * 激活状态 0 待激活 1 已激活
     */
    private String moneyFlag;
    /**
     * 中出规则
     */
    private String activityKey;
    private String obtainFlag;
    private List packTables;
    public VpsVcodeActivatePrizeRecordVO(){

    }
    public VpsVcodeActivatePrizeRecordVO(String queryParam){
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.userKey = paramAry.length > 0 ? paramAry[0] : null;
        this.nickName = paramAry.length > 1 ? paramAry[1] : null;
        this.winQrcodeContent = paramAry.length > 2 ? paramAry[2] : null;
        this.moneyFlag = paramAry.length > 3 ? paramAry[3] : null;
        this.startDate = paramAry.length > 4 ? paramAry[4] : null;
        this.endDate = paramAry.length > 5 ? paramAry[5] : null;
        this.ruleName = paramAry.length > 6 ? paramAry[6] : null;
        this.obtainFlag = paramAry.length > 7 ? paramAry[7] : null;
    }


    public static final String USER_KEY = "user_key";

    public static final String USER_ID = "user_id";

    public static final String NICK_NAME = "nick_name";

    public static final String WIN_QRCODE_CONTENT = "win_qrcode_content";

    public static final String WIN_SKU_NAME = "win_sku_name";

    public static final String WIN_SKU_KEY = "win_sku_key";

    public static final String STATUS = "status";

    public static final String WINNING_AMOUNT = "winning_amount";

    public static final String WINNING_POINT = "winning_point";

    public static final String SHARE_USER_KEY = "share_user_key";

    public static final String SHARE_USER_NAME = "share_user_name";

    public static final String WIN_DATE = "win_date";

    public static final String WIN_END_TIME = "win_end_time";

    public static final String CREATE_TIME = "create_time";

    public static final String CREATE_USER = "create_user";

    public static final String UPDATE_TIME = "update_time";

    public static final String UPDATE_USER = "update_user";

}
