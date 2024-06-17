ALTER TABLE `vps_consumer_user_info`
ADD COLUMN `CHANNEL` varchar(100) DEFAULT NULL COMMENT '创建用户的渠道' AFTER `REGISTER_TIME`;

ALTER TABLE `vps_consumer_user_info`
MODIFY COLUMN `CHANNEL` varchar(100) DEFAULT NULL COMMENT '创建用户的渠道:小程序分享 share_001' AFTER `REGISTER_TIME`;