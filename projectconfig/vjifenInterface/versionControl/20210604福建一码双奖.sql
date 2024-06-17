-- 各省区数据库执行
ALTER TABLE `vps_common_packs_record`
MODIFY COLUMN `EARN_CHANNEL`  varchar(2) NULL DEFAULT NULL COMMENT '领取渠道：1集卡兑换、2商城兑换、3商城抽奖、4锦鲤红包、5一码双奖红包' AFTER `PRIZE_TYPE`,
ADD INDEX `idx_exchange_channel` (`EARN_CHANNEL`) USING BTREE ,
ADD INDEX `idx_earn_time` (`EARN_TIME`) USING BTREE ;
