package com.dbt.platform.publish.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 规则表
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VpsActVideoVO implements Serializable {

    private static final long serialVersionUID = 1L;


    private String videoKey;
    /**
     * 标题
     */
    private String title;
    /**
     * skukey
     */
    private String goodsId;

    /**
     * 区域编号
     */
    private String areaCode;

    /**
     * 限时开始时间
     */
    private String startTime;

    /**
     * 限时结束时间
     */
    private String endTime;

    /**
     * 区域限制
     */
    private String areaLimit;

    /**
     * 视频素材
     */
    private String videoPath;

    private Date createTime;

    private String createUser;

    private Date updateTime;

    private String updateUser;

    /**
     * 区域名称
     */
    private String areaName;

    /**
     * 人群限制类型：默认0不限制，1黑名单不可参与、2指定群组参与、3指定群组参不与
     */
    private String crowdLimitType;

    /**
     * 分组ID集合
     */
    private String userGroupIds;

    /**
     * 顺序编号
     */
    private Integer sequenceno;

    /** 展示状态 0 不展示 1 展示*/
    private String showFlag;
    private String keyword;
    private String status;
    private String modStGmt;
    private String modEndGmt;
    private String modTime;
    private String videoCoverImage;


    public VpsActVideoVO(){}

    public VpsActVideoVO(String queryParam) {
        String[] paramAry = StringUtils.defaultIfBlank(queryParam, "").split(",");
        this.keyword = paramAry.length > 0 ? paramAry[0] : "";
        this.status = paramAry.length > 1 ? paramAry[1] : "";
        this.startTime = paramAry.length > 2 ? paramAry[2] : "";
        this.endTime = paramAry.length > 3 ? paramAry[3] : "";
        this.modStGmt = paramAry.length > 4 ? paramAry[4] : "";
        this.modEndGmt = paramAry.length > 5 ? paramAry[5] : "";
    }

}
