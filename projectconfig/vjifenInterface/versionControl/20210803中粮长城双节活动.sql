--  各省区数据库执行
CREATE TABLE `vps_ticket_promotion_user` (
  `INFO_KEY` varchar(36) NOT NULL,
  `USER_KEY` varchar(36) DEFAULT NULL COMMENT '用户主键',
  `PHONE_NUM` varchar(11) DEFAULT NULL COMMENT '手机号',
  `USER_NAME` varchar(10) DEFAULT NULL COMMENT '促销人员姓名',
  `USER_CODE` varchar(36) DEFAULT NULL COMMENT '促销人员外勤365编号',
  `TERMINAL_SYSTEM` varchar(36) DEFAULT NULL COMMENT '终端门店所属系统',
  `TERMINAL_NAME` varchar(50) DEFAULT NULL COMMENT '所属终端门店名称',
  `TERMINAL_ADDRESS` varchar(200) DEFAULT NULL COMMENT '终端门店详细地址',
  `PROVINCE` varchar(24) DEFAULT NULL,
  `CITY` varchar(24) DEFAULT NULL,
  `COUNTY` varchar(24) DEFAULT NULL,
  `TERMINAL_OPENID` varchar(36) DEFAULT NULL COMMENT '终端促销公众号openid',
  `DELETE_FLAG` varchar(1) DEFAULT NULL,
  `CREATE_USER` varchar(36) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(36) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`INFO_KEY`),
  UNIQUE KEY `idx_phone_num` (`PHONE_NUM`) USING BTREE,
  KEY `idx_terminal_openid` (`TERMINAL_OPENID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='终端门店促销人员表';

-- 增加促销人员用户主键
ALTER TABLE `vps_ticket_record` 
MODIFY COLUMN `TICKET_CHANNEL` varchar(2) NULL DEFAULT NULL COMMENT '渠道类型：0酒行渠道、1餐饮渠道、2KA渠道、3电商渠道、4定时抽奖、5促销激励、6销售之星' AFTER `INFO_KEY`,
ADD COLUMN `PROMOTION_USER_KEY` varchar(36) NULL COMMENT '促销人员用户主键' AFTER `TICKET_TERMINAL_NAME`,
ADD COLUMN `PROMOTION_TERMINAL_NAME` varchar(50) NULL COMMENT '促销人员所属门店名称' AFTER `PROMOTION_USER_KEY`,
ADD COLUMN `PROMOTION_EARN_FLAG`  varchar(1) NULL DEFAULT '0' COMMENT '促销员是否获取返利0不获取、1获取' AFTER `PROMOTION_TERMINAL_NAME`,
ADD COLUMN `TICKET_RECORD_KEY` varchar(36) NULL COMMENT '促销激励关联小票记录主键' AFTER `PROMOTION_EARN_FLAG`,
ADD COLUMN `SKU_KEY`  varchar(36) NULL COMMENT 'SKU主键' AFTER `GOODS_MONEY`,
ADD INDEX `idx_promotion_key`(`PROMOTION_USER_KEY`) USING BTREE,
ADD INDEX `idx_ticket_record_key`(`TICKET_RECORD_KEY`) USING BTREE;

-- SKU票面总价
ALTER TABLE `vps_ticket_record_sku_detail` 
ADD COLUMN `TICKET_SKU_MONEY` decimal(20, 2) NULL COMMENT 'sku票面总价' AFTER `TICKET_SKU_UNIT_MONEY`;

-- 大奖表增加物流公司及物流单号
ALTER TABLE `vps_vcode_prize_record`
ADD COLUMN `EXPRESS_COMPANY`  varchar(36) NULL COMMENT '物流公司' AFTER `ADDRESS`,
ADD COLUMN `EXPRESS_NUMBER`  varchar(36) NULL COMMENT '物流单号' AFTER `EXPRESS_COMPANY`,
ADD COLUMN `EXPRESS_SEND_MESSAGE`  varchar(100) NULL COMMENT '发货备注' AFTER `EXPRESS_NUMBER`;

-- 数据字典增加终端促销服务号appid
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('6cd2abc6-7bc8-11eb-a5fd-6e6d36e3a111', 'f9bd3717-4ca2-11ea-b627-6e6d36e3ad65', 'terminal_appid', 'wx80624d4d4c0b0319', '终端促销服务号appid', '终端促销服务号appid', '1', '0', '1', '2021-03-03 10:30:31', '1', '2021-03-08 17:15:53', '20');

-- 双节活动战区名称
CREATE TABLE `vps_ticket_wararea_report` (
  `big_area_name` varchar(36) DEFAULT NULL,
  `war_area_name` varchar(36) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='双节活动战区名称'

insert into vps_ticket_wararea_report values('北区', '冀南战区');
insert into vps_ticket_wararea_report values('北区', '北京战区');
insert into vps_ticket_wararea_report values('北区', '冀东战区');
insert into vps_ticket_wararea_report values('北区', '辽宁战区');
insert into vps_ticket_wararea_report values('北区', '天津战区');
insert into vps_ticket_wararea_report values('北区', '吉龙战区');
insert into vps_ticket_wararea_report values('北区', '冀西战区');
insert into vps_ticket_wararea_report values('东区', '浙江战区');
insert into vps_ticket_wararea_report values('东区', '安徽战区');
insert into vps_ticket_wararea_report values('东区', '上海战区');
insert into vps_ticket_wararea_report values('东区', '江苏战区');
insert into vps_ticket_wararea_report values('西区', '陕西战区');
insert into vps_ticket_wararea_report values('西区', '川藏战区');
insert into vps_ticket_wararea_report values('西区', '渝贵战区');
insert into vps_ticket_wararea_report values('南区', '桂云战区');
insert into vps_ticket_wararea_report values('南区', '福建战区');
insert into vps_ticket_wararea_report values('南区', '广东战区');
insert into vps_ticket_wararea_report values('南区', '海南战区');
insert into vps_ticket_wararea_report values('中区', '河南战区');
insert into vps_ticket_wararea_report values('中区', '山东战区');
insert into vps_ticket_wararea_report values('中区', '湖北战区');
insert into vps_ticket_wararea_report values('中区', '江西战区');
insert into vps_ticket_wararea_report values('中区', '湖南战区');
insert into vps_ticket_wararea_report values('其它', '其他战区');

-- 配置检查
检查vjf_appid、支付通道授权等

-- Redis 增加限制手机号
sadd zhongLCNY:promotionInvalidPhoneNumSet

-- 管理平台pom
	poi更新版本3.17、删除poi-ooxml、删除poi-ooxml-schemas、增加easyExcel