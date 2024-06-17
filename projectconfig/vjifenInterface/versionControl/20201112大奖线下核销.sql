-- db main
INSERT INTO `sys_function` VALUES ('800600', '核销门店管理', '800000', 'terminal/showTerminalList.do', '核销门店管理', '2', null, null, '1', '0', '521', '0', '2020-11-12 14:59:21', 'admin', '2020-11-12 14:59:23', 'admin');
INSERT INTO `sys_role_function_relation` VALUES (uuid(), '1', '800600');
INSERT INTO `sys_role_function_relation` VALUES (uuid(), '2', '800600');

-- 增加核销人员门店主键
ALTER TABLE `vps_consumer_check_user_info`
ADD COLUMN `TERMINAL_KEY`  varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '门店主键' AFTER `ADDRESS`;

-- 门店表
CREATE TABLE `vps_terminal_info` (
  `TERMINAL_KEY` varchar(36) NOT NULL,
  `TERMINAL_NAME` varchar(128) DEFAULT NULL COMMENT '门店名称',
  `TERMINAL_NO` varchar(36) DEFAULT NULL COMMENT '门店编号',
  `IMAGE_URL` varchar(255) DEFAULT NULL COMMENT '门店图片',
  `AREACODE` varchar(10) DEFAULT NULL COMMENT '地区code',
  `PROVINCE` varchar(24) DEFAULT NULL COMMENT '省',
  `CITY` varchar(24) DEFAULT NULL COMMENT '市',
  `COUNTY` varchar(24) DEFAULT NULL COMMENT '县',
  `PHONE_NUM` varchar(25) DEFAULT NULL COMMENT '联系电话',
  `ADDRESS` varchar(50) DEFAULT NULL COMMENT '门店地址',
  `LONGITUDE` varchar(20) DEFAULT NULL COMMENT '经度',
  `LATITUDE` varchar(20) DEFAULT NULL COMMENT '纬度',
  `TERMINAL_CHECKUSER_KEY` varchar(36) DEFAULT NULL COMMENT '终端门店核销员主键',
  `STATUS` varchar(1) DEFAULT '0' COMMENT '门店状态：0启用、1禁用',
  `DELETE_FLAG` varchar(1) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(36) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`TERMINAL_KEY`),
  KEY `idx_longitude` (`LONGITUDE`) USING BTREE,
  KEY `idx_latitude` (`LATITUDE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店信息表';



CREATE TABLE `vps_prize_terminal_relation` (
  `RELATION_KEY` varchar(36) NOT NULL,
  `PRIZE_TYPE` varchar(10) NOT NULL COMMENT '奖品编号',
  `TERMINAL_KEY` varchar(36) NOT NULL COMMENT '门店key',
  `STATUS` varchar(1) DEFAULT NULL COMMENT '状态0兑换中1已暂停',
  PRIMARY KEY (`RELATION_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



ALTER TABLE `vps_vcode_prize_record`
ADD COLUMN `TERMINAL_KEY`  varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '门店主键' AFTER `GOODS_ID`;




