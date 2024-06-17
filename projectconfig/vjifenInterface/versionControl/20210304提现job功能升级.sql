#省区数据库执行
-- 增加是否job提现失败账户标志 
ALTER TABLE `vps_consumer_account_info` 
ADD COLUMN `SEND_GIFT_JOB_FAIL` varchar(1) NULL DEFAULT 0 COMMENT '是否job提现失败账户:0否、1是' AFTER `IS_HN_UPDATE`;

-- 大奖表增加所属合同年份
ALTER TABLE `vps_vcode_prize_record` 
ADD COLUMN `CONTRACT_YEAR` varchar(4) NULL COMMENT '所属合同年份' AFTER `PRIZE_IMG`;