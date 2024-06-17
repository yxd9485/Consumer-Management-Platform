package com.dbt.platform.autocode.service;

import com.dbt.platform.autocode.bean.VpsQrcodeOrder;

/**
 * module: autoCode <br/>
 * <p>
 * description: autoCode 公共业务类<br/>
 *
 * @author: XiaoDong.Yang
 * @date: 2023/9/22 12:58
 */
public interface BusinessService {

    /**
     * 组装最酒码源订单信息
     * @param vpsQrcodeOrder
     * @param dbType
     */
    void zuiJiuVpsQrcodeOrder(VpsQrcodeOrder vpsQrcodeOrder,String dbType);
}
