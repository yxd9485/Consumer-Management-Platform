-- 各省区数据库执行
CREATE TABLE `vps_vcode_prize_basic_info` (
  `info_key` varchar(36) NOT NULL COMMENT '主键',
  `prize_no` varchar(20) DEFAULT NULL COMMENT '大奖编号',
  `prize_name` varchar(20) DEFAULT NULL COMMENT '大奖名称',
  `prize_type` varchar(10) DEFAULT NULL COMMENT '大奖类型',
  `prize_win_pic` varchar(200) DEFAULT NULL COMMENT '奖品中出图片',
  `prize_earn_pic` varchar(200) DEFAULT NULL COMMENT '奖品领取图片',
  `prize_list_pic` varchar(200) DEFAULT NULL COMMENT '奖品列表图片',
  `verification_type` char(1) DEFAULT '1' COMMENT '核销类型：1 自提奖品，2 物流发货',
  `prize_short_name` varchar(20) DEFAULT NULL COMMENT '大奖简称',
  `prize_value` varchar(20) DEFAULT NULL COMMENT '奖品价值',
  `prize_endTime` datetime DEFAULT NULL COMMENT '中奖截止时间',
  `cash_prize_endTime` datetime DEFAULT NULL COMMENT '兑奖结束时间',
  `cash_prize_endDay` varchar(20) DEFAULT NULL COMMENT '中奖后有效兑奖天数',
  `isRecycle` varchar(1) DEFAULT NULL COMMENT '大奖是否回收',
  `is_check_captcha` char(1) DEFAULT '1' COMMENT '是否需要验证码：0否，1是',
  `is_idcard` char(1) DEFAULT '1' COMMENT '是否需要身份证号：0否，1是',
  `is_phone` varchar(1) DEFAULT NULL COMMENT '是否需要手机号 0否 1是',
  `is_name` varchar(1) DEFAULT NULL COMMENT '是否需要姓名 0否 1是',
  `is_address` varchar(1) DEFAULT NULL COMMENT '是否需要地址 0否 1是',
  `cashPrize` varchar(1) DEFAULT NULL COMMENT '兑奖时间类型 0 时间 1天数',
  `isMsg` varchar(1) DEFAULT NULL COMMENT '是否发送短信 0 否 1是',
  `win_prizeTime` varchar(1) DEFAULT '' COMMENT '是否限制中奖次数 0否 1是',
  `prize_content` text COMMENT '奖品说明',
  `is_used` varchar(1) DEFAULT NULL COMMENT '奖项是否被使用 0 未被中出使用 1已经中出使用',
  `DELETE_FLAG` varchar(1) DEFAULT NULL COMMENT '删除标识',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
  `CREATE_USER` varchar(36) DEFAULT NULL COMMENT '创建人',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  `UPDATE_USER` varchar(36) DEFAULT NULL COMMENT '修改人',
  PRIMARY KEY (`info_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

CREATE TABLE `vps_ladder_ui` (
  `info_key` varchar(36) NOT NULL COMMENT '主键',
  `rule_key` varchar(36) NOT NULL COMMENT '规则主键',
  `ladder_multiple` varchar(20) DEFAULT NULL COMMENT '阶梯倍数',
  `ladder_pic` varchar(100) DEFAULT NULL COMMENT '阶梯中奖图片',
  `delete_flag` varchar(1) DEFAULT NULL COMMENT '删除标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(36) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_user` varchar(36) DEFAULT NULL COMMENT '修改人',
  `ladder_type` varchar(1) DEFAULT NULL COMMENT '阶梯弹窗的方式 0.倍数，1.支',
  `ladder_st_num` varchar(255) DEFAULT NULL COMMENT '阶梯开始瓶数',
  `ladder_end_num` varchar(255) DEFAULT NULL COMMENT '阶梯结束瓶数',
  PRIMARY KEY (`info_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =============================中心库==================================

INSERT INTO `sys_function` (`FUNCTION_KEY`, `FUNCTION_NAME`, `PARENT_KEY`, `LINK_URL`, `FUNCTION_DESC`, `FUNCTION_LEVEL`, `MENU_ICON`, `FUNCTION_CODE`, `FUNCTION_STATUS`, `FUNCTION_TYPE`, `SEQUENCE_NUM`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('200900', '实物奖', '200000', 'vcodeActivityBigPrize/showBigPrizeList.do', '实物奖', '2', NULL, NULL, '1', '0', '100', '0', '2020-03-04 19:51:47', 'admin', '2020-03-04 19:51:53', 'admin');

insert into sys_role_function_relation values (UUID(), '1', '200900');
insert into sys_role_function_relation values (UUID(), '2', '200900');
