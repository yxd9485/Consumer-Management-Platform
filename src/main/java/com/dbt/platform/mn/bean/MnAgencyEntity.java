package com.dbt.platform.mn.bean;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author bing_huang
 * @since 2021-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("com_agency_info")
public class MnAgencyEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID（主键）
     */
    private Long id;

    /**
     * 	父id
     */
    private Long pid;

    /**
     * 	SAP编码
     */
    private String sapcode;

    /**
     * 	名称
     */
    private String agency_name;

    /**
     * 	供货公司简称
     */
    private String agency_nickname;

    /**
     * 	所属省
     */
    private String agency_province;

    /**
     * 	所属市
     */
    private String agency_city;

    /**
     * 	所属县
     */
    private String agency_country;
    /**
     * 	公司所在详细地址
     */
    private String agency_address;

    /**
     * 	联系人
     */
    private String agency_contact;
    /**
     * 	联系电话
     */
    private String agency_mobile;
    /**
     * 	库房地址
     */
    private String street;
    /**
     * 	库房收货人
     */
    private String zzcus901;
    /**
     * 	状态
     */
    private String status;
    /**
     * 	库房收货人手机
     */
    private String telf1;
    /**
     * 	统一社会信用代码
     */
    private String zzcus002;
    /**
     * 	类型
     */
    private String agency_type;
    /**
     * 	法人
     */
    private String zzcus014;
    /**
     * 	是否为一般纳税人
     */
    private String zzcus012;
    /**
     * 	大区id
     */
    private String vkbur;
    /**
     * 	大区名称
     */
    private String vkburdesc;
    /**
     * 	省区id
     */
    private String vkgrp;
    /**
     * 	省区名称
     */
    private String vkgrpdesc;
    /**
     * 	备注
     */
    private String com_remark;
    /**
     * 	同步状态
     */
    private String aufsd;
    /**
     * 创建人id
     */
    private Long creuser;

    /**
     * 修改人id
     */
    private Long upuser;

    /**
     * 创建时间
     */
    private String cretime;

    /**
     * 更新时间
     */
    private String uptime;
    
    
}
