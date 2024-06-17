1.创建省区登录用户及关联角色（根据省区实际情况修改）-主库数据库
    -- 查询用户表最大用户KEY
    SELECT * FROM `sys_user_basis` ORDER BY cast(`USER_KEY` as UNSIGNED INTEGER) DESC ;

    -- 修改用户昵称、登录账户、密码和省区标识
    insert into sys_user_basis values ('46', 'admin', '安徽admin', '201609141', 'RTY0Qjc4RkMzQkM5MUJDQkM3REMyMzJCQThFQzU5RTA=', 'anhui', '1', '0', '0', NOW(), '0', NOW(), '0');
    insert into sys_user_basis values ('47', '安徽青啤', '安徽青啤', '201609141', 'MTA5Mjc2MjdDNjcxQzlCM0FENDNBOTdCQ0JFRURFMjg=', 'anhui', '1', '1', '0', NOW(), '0', NOW(), '0');
    insert into sys_user_basis values ('48', '安徽V积分财务', '安徽财务', '201609141', 'M0JEODFBMTVDRDhDMzU5Q0VFRERCRDVGOTkxMzAxRjY=', 'anhui', '1', '1', '0', NOW(), '0', NOW(), '0');
    insert into sys_user_basis values ('49', '安徽青啤查看', '安徽青啤查看', '201609141', 'QzgzRTdBMjM2Q0RCMkMzREQxRUI2MkFDMzg3QTcwNjY=', 'anhui', '1', '1', '0', NOW(), '0', NOW(), '0');
    --insert into sys_user_basis values ('9', '华南工厂激活查看', '华南工厂激活查看', '201609141', 'QkZFQjUzQzYxQjlDMDBCQ0E4MzRCQTg4M0Y2M0Y2Nzc=', 'anhui', '1', '1', '0', NOW(), '0', NOW(), '0');
    --insert into sys_user_basis values ('58', '湖南积分商城', '湖南积分商城', '201609141', 'Rjk2QjFCODNBRUZCNjE4RUZFMEQ4QkU2NTBGQkQxNkM=', 'hunan', '1', '1', '0', NOW(), '0', NOW(), '0');
    --insert into sys_user_basis values ('93', '山东秒杀', '山东秒杀', '201609141', 'QTJCQTdCQzZCQzRDRTJENzhGRDVBRThGRjM5MTM2MTk=', 'shandongagt', '1', '1', '0', NOW(), '0', NOW(), '0');

2.关联角色（根据省区实际情况修改）-主库数据库
    INSERT INTO `sys_user_role_relation` values (UUID(), '46', '1'); -- admin
    INSERT INTO `sys_user_role_relation` values (UUID(), '47', '2'); -- 项目经理
    INSERT INTO `sys_user_role_relation` values (UUID(), '48', '3'); -- 财务用户
    INSERT INTO `sys_user_role_relation` values (UUID(), '49', '4'); -- 查看用户
    -- INSERT INTO `sys_user_role_relation` values (UUID(), '58', '8'); -- 积分商城
    -- INSERT INTO `sys_user_role_relation` values (UUID(), '', '5');  -- 品牌用户（省区没有可以不加）
    -- INSERT INTO `sys_user_role_relation` values (UUID(), '9', '7'); -- 工厂查看（省区没有可以不加）
    -- INSERT INTO `sys_user_role_relation` values (UUID(), '93', '9'); -- 秒杀

3.省区filter数据迁移数据字典（使用模板生成）-当前省区数据库
    1. 插入前先删除之前的相关配置
        -- delete from `vps_sys_datadic_m` where `CATEGORY_KEY` in (select `CATEGORY_KEY`  from `vps_sys_diccategory_info` where `CATEGORY_CODE`  like 'filter%');
        -- delete from `vps_sys_diccategory_info` where `CATEGORY_CODE`  like 'filter%';
    2. 把省区的线上的filter文件粘贴到平台resource下并完maven成打包操作
    3. 找到编译后路径（target）下的init_sql.properties文件，拷贝数据到省区数据库执行
    -- 4. 更新支付通道及支付公众号（只有更换的省区才需要执行4~6）
        update vps_sys_datadic_m set data_value = '1516432041' where data_id = 'wxPay_mch_billno';
        update vps_sys_datadic_m set data_value = '1516432041' where data_id = 'wxPay_certPassword';
        update vps_sys_datadic_m set data_value = 'wxa42a20606316e2e9' where data_id = 'vjf_appid';
    -- 5. 清空对应hbopenid字段
        update `vps_consumer_thirdaccount_info` set `HBOPENID` ='' ;
    -- 6. 管理后台关闭提现开关，通知项目经理升级几分钟

5. 查看该省区功能差异描述2019和2020中版本差异，如果不是最新版本请执行相关功能的SQL升级到最新版本

6. 手动设置配置文件(平台)
   秒杀配置文件和逢尾数文件地址变更：获取原服务器/data/upload/cogFolder/  到新服务器/data/upload/cogFolder/${projectServcerName}/
   注册核销人员地址复制：获取原服务器/data/upload/checkuser/  到新服务器/data/upload/checkuser/

7.修改省区数据源连接数（根据当前省区配置的连接数去修改，目前默认60，一般情况下无需修改），并且执行以下连接及时生效（执行前修改对应省区标识：projectServerName）
    http://182.92.125.244:9898/vjifenPlatform/system/modifyProjectServerInfo.do?projectServerName=beixiao

8.使用postmain调用扫码接口、提现接口以及红包列表接口测试

9.前端部署上线

10.测试线上相关功能，此时job使用原来服务器的

11.测试通过，关闭原服务器（或原服务器job功能）然后修改主库数据字典的job运行省区，此时新系统中job生效，再监控下

4.项目经理手机号和客户手机号添加到白名单（主库）

12.通知前端志刚修改激活工具下该省区的调用路径以及添加省区标识（目前上线省区：四川、华南、广西、浙江、全麦、云南、江西、重庆、上海、辽宁、安徽、河南瓶装、甘肃、内蒙、山西、湖南、福建、海南、北销、山东、吉林、汾酒老白、江苏、中粮长城）

13.通知洪涛修改大地图跳转连接

14.平台地址：http://182.92.125.244:9898/vjifenPlatform/
   接口地址：http://39.102.31.134/vjifenInterface/


