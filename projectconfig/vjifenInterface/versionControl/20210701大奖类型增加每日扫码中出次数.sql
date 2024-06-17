-- 各省区数据库执行
ALTER TABLE `vps_vcode_prize_basic_info` 
ADD COLUMN `day_limit_num` int(0) NULL COMMENT '每天扫码中出次数限制' AFTER `cash_advisory_time`;