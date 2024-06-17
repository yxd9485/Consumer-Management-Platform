package com.dbt.platform.autocode.service.impl;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;
import com.dbt.platform.autocode.service.BusinessService;
import org.springframework.stereotype.Service;

/**
 * module: 应用模块名称<br/>
 * <p>
 * description: 描述<br/>
 *
 * @author XiaoDong.Yang
 * @date: 2023/9/22 12:59
 */
@Service
public class BusinessServiceImpl implements BusinessService {
    @Override
    public void zuiJiuVpsQrcodeOrder(VpsQrcodeOrder vpsQrcodeOrder,String dbType) {
        // 最酒码源类型
        if (dbType.equals("zuijiu")) {
            String oldOrderName = vpsQrcodeOrder.getOrderName();
            switch (vpsQrcodeOrder.getQrcodeType()) {
                case "X":
                    vpsQrcodeOrder.setOrderName(oldOrderName+"-"+"箱码");
                    break;
                case "W":
                    vpsQrcodeOrder.setOrderName(oldOrderName+"-"+"盖外码");
                    break;
                case "N":
                    vpsQrcodeOrder.setOrderName(oldOrderName+"-"+"盖内码");
                    break;
                default:
                    break;
            }
        }

    }
}
