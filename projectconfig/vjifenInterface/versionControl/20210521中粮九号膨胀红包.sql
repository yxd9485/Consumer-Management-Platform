--膨胀红包-结束日期
INSERT INTO vps_sys_datadic_m VALUES ('1ba15cd5-b62d-404e-89aa-58d4341fd0b6', '74d21e8e-eded-42bc-9961-aea9321565ee', 'benediction_end_date', '2022-04-30', '膨胀红包-结束日期', '', 9999, '0', 1, '2021-4-8 13:24:05', '20', '2021-5-10 15:26:22', '20');
--可分享红包最低个数
INSERT INTO vps_sys_datadic_m VALUES ('341ce853-fc70-460f-a5ae-d786edf5ba75', '74d21e8e-eded-42bc-9961-aea9321565ee', 'benediction_share_num', '3', '膨胀红包-可分享红包最低个数', '', 9999, '0', 1, '2021-4-13 17:15:58', '20', '2021-4-13 17:27:06', '20');
--膨胀红包-过期天数	
INSERT INTO vps_sys_datadic_m VALUES ('6f54d21b-32bd-4aab-ae81-593207565f86', '74d21e8e-eded-42bc-9961-aea9321565ee', 'benediction_days', '30', '膨胀红包-过期天数', '', 9999, '0', 1, '2021-4-13 18:26:59', '20', '2021-4-13 18:27:27', '20');
--膨胀红包-概率区间
INSERT INTO vps_sys_datadic_m VALUES ('a0e6cf90-e3c6-469e-b782-05f3d820d0f2', '74d21e8e-eded-42bc-9961-aea9321565ee', 'benediction_section_rate', '0.45-0.6', '膨胀红包-概率区间 ', '', 9999, '0', 1, '2021-4-13 17:39:26', '20', '2021-4-15 17:43:42', '20');
--膨胀红包-大金额红包个数
INSERT INTO vps_sys_datadic_m VALUES ('bd35e37e-420b-4cc5-b336-5d0478d6c442', '74d21e8e-eded-42bc-9961-aea9321565ee', 'benediction_big_money_rate', '2', '膨胀红包-大金额红包个数', '', 9999, '0', 1, '2021-4-13 17:44:48', '20', '2021-4-13 18:10:25', '20');
--膨胀红包-金额区间
INSERT INTO vps_sys_datadic_m VALUES ('e0173b08-0c4f-40cb-bf79-37146dda853f', '74d21e8e-eded-42bc-9961-aea9321565ee', 'benediction_section_money', '0.5-5.0', '膨胀红包-金额区间', '', 9999, '0', 1, '2021-4-13 18:11:25', '20', '2021-4-15 13:50:13', '20');


CREATE TABLE `vps_vcode_benediction_qrcode` (
  `INFO_KEY` varchar(36) NOT NULL,
  `USER_KEY` varchar(36) DEFAULT NULL COMMENT '用户主键',
  `USER_NAME` varchar(100) DEFAULT NULL COMMENT '用户昵称',
  `PRIZE_VCODE` varchar(32) DEFAULT NULL COMMENT 'V码',
  `STATUS` varchar(1) DEFAULT NULL COMMENT '状态：0未领完、1已领完',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '录入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`INFO_KEY`),
  KEY `idx_user_key` (`USER_KEY`) USING BTREE,
  KEY `idx_prize_vcode` (`PRIZE_VCODE`) USING BTREE,
  KEY `idx_status` (`STATUS`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='膨胀红包V码表';





CREATE TABLE `vps_vcode_benediction_expand_record` (
  `INFO_KEY` varchar(36) NOT NULL COMMENT '主键',
  `USER_NAME` varchar(255) DEFAULT NULL COMMENT '领取人名称',
  `USER_KEY` varchar(36) DEFAULT NULL COMMENT '领取人主键',
  `HEAD_IMG_URL` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `EXPAND_AMOUNT` decimal(11,2) DEFAULT NULL COMMENT '膨胀金额',
  `PRIZE_VCODE` varchar(32) DEFAULT NULL COMMENT 'V码',
  `STATUS` varchar(1) DEFAULT NULL COMMENT '是否已使用状态：0否、1已使用',
  `RECEIVE_TIME` datetime DEFAULT NULL COMMENT '领取时间',
  `EXPIRE_TIME` datetime DEFAULT NULL COMMENT '有效截止日期',
  `CREATE_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`INFO_KEY`),
  KEY `idx_user_key` (`USER_KEY`) USING BTREE,
  KEY `idx_status` (`STATUS`) USING BTREE,
  KEY `idx_prize_vcode` (`PRIZE_VCODE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='膨胀红包记录';

