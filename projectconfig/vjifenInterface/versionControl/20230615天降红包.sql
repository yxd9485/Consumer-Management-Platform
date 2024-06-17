INSERT INTO `sys_function` VALUES ('402200', '天降红包活动', '400000', NULL, NULL, 'waitActivation/showWaitActivationConfig.do', '天降红包活动配置', '2', NULL, NULL, '1', '0', 362, '0', '2023-06-06 17:56:29', 'admin', '2023-06-06 17:56:29', 'admin');
INSERT INTO `sys_role_function_relation` VALUES (UUID(), '1', '402200');
INSERT INTO `sys_role_function_relation` VALUES (UUID(), '2', '402200');
INSERT INTO `sys_role_function_relation` VALUES (UUID(), '4', '402200');

ALTER TABLE `vps_vcode_activity_vpoints_cog`
ADD COLUMN `IS_WAIT_ACTIVATION` varchar(1) NULL COMMENT '是否为待激活现金红包 0否 1是' AFTER `PRIZE_TYPE`;


--application.properties   # 不验证参数长度接口配置  ,/waitActivation/*.do

--检查 vps_vcode_activity_rebate_rule_cog 规则表活动类型 ACTIVITY_TYPE 长度是不是2

ALTER TABLE `vps_vcode_activity_rebate_rule_cog` MODIFY COLUMN `ACTIVITY_TYPE` varchar(2) NULL DEFAULT NULL  AFTER `VCODE_ACTIVITY_KEY`;


