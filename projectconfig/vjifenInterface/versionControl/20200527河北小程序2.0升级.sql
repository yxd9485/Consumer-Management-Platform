-- 兑换记录表
ALTER TABLE `vpoints_exchange_log`
MODIFY COLUMN `exchange_status`  varchar(2)  NOT NULL COMMENT '兑换状态：-1=待支付、0=兑换成功、1=兑换失败、2=兑换中、3=订单已关闭、4=微信退款申请、 5=待发货撤单、6=微信退款失败、7=退货审核中、8=退货处理中、9=退货已完成、10=待提交退货物流' AFTER `exchange_pay`,
ADD COLUMN `goods_return_reason`  varchar(100) NULL COMMENT '退货原因' AFTER `earn_vpoints`,
ADD COLUMN `goods_return_time`  datetime NULL COMMENT '发起退货日期' AFTER `goods_return_reason`,
ADD COLUMN `goods_return_audit`  varchar(100) NULL COMMENT '退货审核备注' AFTER `goods_return_time`,
ADD COLUMN `goods_return_audit_user`  varchar(36) NULL COMMENT '退货审核者登录手机号' AFTER `goods_return_audit`,
ADD COLUMN `goods_return_express_company`  varchar(50) NULL COMMENT '退货物流公司名称' AFTER `goods_return_audit_user`,
ADD COLUMN `goods_return_express_number`  varchar(50) NULL COMMENT '退货物流编号' AFTER `goods_return_express_company`,
ADD COLUMN `customer_message`  varchar(500) NULL DEFAULT NULL COMMENT '客户留言' AFTER `address`;

-- 商品表
ALTER TABLE `vpoints_goods_info`
ADD COLUMN `goods_unit_name` varchar(10) DEFAULT NULL COMMENT '商品单位名称' AFTER `goods_url`,
ADD COLUMN `you_pin_flag`  varchar(255) NULL COMMENT '优品推荐商品标志：1否、0是' AFTER `good_present_sequence`,
ADD COLUMN `sale_num_sequence`  int NULL COMMENT '自定义销量排名' AFTER `you_pin_flag`,
ADD INDEX `idx_goods_start_time` (`goods_start_time`) USING BTREE ,
ADD INDEX `idx_goods_end_time` (`goods_end_time`) USING BTREE ;



-- 大奖记录表
ALTER TABLE vps_vcode_prize_record
ADD COLUMN `customer_message`  varchar(500) NULL DEFAULT NULL COMMENT '客户留言' AFTER `address`;

-- 商城秒杀提醒
ALTER TABLE `vpoints_goods_consumer_status_info`
ADD COLUMN `SEC_KILL_REMIND_FLAG`  varchar(255) NULL COMMENT '0-未预约、1-已预约、2已发送' AFTER `ARRIVAL_NOTICE_FLAG`,
ADD INDEX `idx_sec_kill_remind_FLAG` (`SEC_KILL_REMIND_FLAG`) USING BTREE ;

-- 商城秒杀预告
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('3f2d88f5-ca59-11ea-b869-6e6d36e3ad65', 'ec0be6e8-a17b-4425-b6cf-664454a4b840', 'vpoints_sec_kill_ad_img', '', '秒杀预告图片URL', '', '1', '0', '0', NOW(), '24001', NOW(), NULL);
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('3f2d890c-ca59-11ea-b869-6e6d36e3ad65', 'ec0be6e8-a17b-4425-b6cf-664454a4b840', 'vpoints_sec_kill_ad_url', '', '秒杀预告URL', '', '1', '0', '0', NOW(), '24001', NOW(), NULL);
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('3f2d891f-ca59-11ea-b869-6e6d36e3ad65', 'ec0be6e8-a17b-4425-b6cf-664454a4b840', 'vpoints_sec_kill_ad_days', '', '秒杀预告提前天数', '', '1', '0', '0', NOW(), '24001', NOW(), NULL);
update vps_sys_datadic_m set CATEGORY_KEY = (select CATEGORY_KEY from vps_sys_diccategory_info where CATEGORY_CODE = 'vpoints_estore_cog') where DATADIC_KEY in ('3f2d890c-ca59-11ea-b869-6e6d36e3ad65', '3f2d88f5-ca59-11ea-b869-6e6d36e3ad65', '3f2d891f-ca59-11ea-b869-6e6d36e3ad65');

-- 商城秒杀商品预约提醒模板消息
insert into vps_sys_datadic_m values (UUID(),'f9bd3717-4ca2-11ea-b627-6e6d36e3ad65', 'wechat_tmpmsg_paapplet_seckillremind', 'uwIAvJlIh8hZw3rvGNQGPd41kHD2GJYbZZY0d_PUQqc', '商城秒杀商品预约提醒', '商城秒杀商品预约提醒模板消息', 23, '0', 1, NOW(), '1', NOW(), '1');


-- 中心库执行
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('bdc1fdd9-6cac-4e14-8716-92dfb7f7cc27', '1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'vpoints_sec_kill_remind_job', 'hebei', '平台：商城秒杀预约提醒JOB', '', '30', '0', '1', '2020-07-20 17:26:02', '3', '2020-07-20 17:27:19', '3');
update vps_sys_datadic_m set CATEGORY_KEY = (select CATEGORY_KEY from vps_sys_diccategory_info where CATEGORY_CODE = 'project_job') where DATADIC_KEY in ('bdc1fdd9-6cac-4e14-8716-92dfb7f7cc27');

