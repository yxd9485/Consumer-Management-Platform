
CREATE TABLE `vps_questionnaire_order` (
  `INFO_KEY` varchar(36) NOT NULL,
  `USER_KEY` varchar(36) DEFAULT NULL COMMENT '用户主键',
  `ORDER_NO` varchar(100) DEFAULT NULL COMMENT '京东商品编号',
  `PRIZE_VCODE` varchar(32) DEFAULT NULL COMMENT 'V码',
  `CREATE_TIME` datetime DEFAULT NULL COMMENT '录入时间',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`INFO_KEY`),
  UNIQUE KEY `idx_prize_vcode` (`PRIZE_VCODE`) USING BTREE,
  UNIQUE KEY `idx_user_key` (`USER_KEY`) USING BTREE,
  KEY `idx_order_no` (`ORDER_NO`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调查问卷订单表';




ALTER TABLE `vps_questionnaire_answer`
ADD COLUMN `GROUP_ID` varchar(36) NULL DEFAULT NULL COMMENT '分组id' AFTER `OPTIONS`;



INSERT INTO `vps_questionnaire_question` VALUES ('1', 'zhongLJHQuestion', '您是:', '男士@#女士', '0', '0', 1, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('2', 'zhongLJHQuestion', '您的年龄是？', '18岁以下@#18~24岁@#25~30岁@#31~40岁@#41~50岁@#50岁以上', '0', '0', 2, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('3', 'zhongLJHQuestion', '您认为长城玖的口感是？', '满意@#偏酸@#偏涩@#偏甜', '0', '0', 3, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('4', 'zhongLJHQuestion', '您认为长城玖的包装设计是？', '大众所爱@#符合我的审美@#普普通通@#不喜欢', '0', '0', 4, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('5', 'zhongLJHQuestion', '您认为长城玖的性价比是？', '非常高@#高@#一般@#低@#偏低', '0', '0', 5, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('6', 'zhongLJHQuestion', '您会把长城玖推荐给谁？', '同事@#朋友@#家人@#其他@#不推荐', '0', '1', 6, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('7', 'zhongLJHQuestion', '您认为长城玖适用于以下哪些场景饮用？', '商务宴请@#朋友、家人聚餐@#自饮小酌@#其他', '0', '1', 7, '0', now(), NULL, NULL, NULL);
INSERT INTO `vps_questionnaire_question` VALUES ('8', 'zhongLJHQuestion', '其他建议（如您的建议在优化产品中被采纳，您将获得长城玖赠品一箱）', NULL, '0', '2', 8, '0', now(), NULL, NULL, NULL);
