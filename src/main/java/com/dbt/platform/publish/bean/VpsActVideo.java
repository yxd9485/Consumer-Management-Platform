package com.dbt.platform.publish.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * 规则表
 * </p>
 *
 * @author wangshuda
 * @since 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("vps_act_video")
public class VpsActVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("VIDEO_KEY")
    private String videoKey;

    @TableField("title")
    private String title;

    /**
     * skukey
     */
    @TableField("goods_id")
    private String goodsId;

    /**
     * 区域编号
     */
    @TableField("area_code")
    private String areaCode;

    /**
     * 限时开始时间
     */
    @TableField("START_TIME")
    private Date startTime;

    /**
     * 限时结束时间
     */
    @TableField("END_TIME")
    private Date endTime;

    /**
     * 区域限制
     */
    @TableField("AREA_LIMIT")
    private String areaLimit;

    /**
     * 视频素材
     */
    @TableField("VIDEO_PATH")
    private String videoPath;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("CREATE_USER")
    private String createUser;

    @TableField("UPDATE_TIME")
    private Date updateTime;

    @TableField("UPDATE_USER")
    private String updateUser;

    /**
     * 区域名称
     */
    @TableField("area_name")
    private String areaName;

    /**
     * 人群限制类型：默认0不限制，1黑名单不可参与、2指定群组参与、3指定群组参不与
     */
    @TableField("CROWD_LIMIT_TYPE")
    private String crowdLimitType;

    /**
     * 分组ID集合
     */
    @TableField("USER_GROUP_IDS")
    private String userGroupIds;

    /**
     * 顺序编号
     */
    @TableField("SEQUENCENO")
    private Integer sequenceno;
    @TableField("video_cover_image")
    private String videoCoverImage;
    @TableField("show_flag")
    private String showFlag;


    @TableField(select = false)
    private String status;
    @TableField(select = false)
    private Date modStGmt;
    @TableField(select = false)
    private Date modEndGmt;


    public static final String VIDEO_KEY = "VIDEO_KEY";

    public static final String GOODS_ID = "goods_id";

    public static final String AREA_CODE = "area_code";

    public static final String START_TIME = "START_TIME";

    public static final String END_TIME = "END_TIME";

    public static final String AREA_LIMIT = "AREA_LIMIT";

    public static final String VIDEO_PATH = "VIDEO_PATH";

    public static final String CREATE_TIME = "CREATE_TIME";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_TIME = "UPDATE_TIME";

    public static final String UPDATE_USER = "UPDATE_USER";

    public static final String AREA_NAME = "area_name";

    public static final String CROWD_LIMIT_TYPE = "CROWD_LIMIT_TYPE";

    public static final String USER_GROUP_IDS = "USER_GROUP_IDS";

    public static final String SEQUENCENO = "SEQUENCENO";

}
