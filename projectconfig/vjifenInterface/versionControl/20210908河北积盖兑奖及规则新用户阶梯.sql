-- 各省区数据库执行
ALTER TABLE `vps_vcode_activity_rebate_rule_cog`
ADD COLUMN `RULE_NEW_USER_LADDER`  varchar(1) NULL DEFAULT 0 COMMENT '是否启用规则新用户阶梯:0不启用、1启用' AFTER `FIRST_SCAN_DANPING_LIMIT`;

-- 积盖兑奖类型
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('e69f6eb7-a8c7-40d1-9543-a422711f992f', 'a7098116-530a-11ea-ba2a-6e6d36e3ad65', 'scannumlottery_type', '1', '积盖兑奖类型', '积盖兑奖类型：0每次兑换消耗次数（山东模式）、1每月兑换一次（河北模式）', '200', '0', '1', '2021-06-09 17:00:58', '24003', NULL, NULL);
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('f34f6eb7-a8c7-40d1-9543-a422711f992f', 'a7098116-530a-11ea-ba2a-6e6d36e3ad65', 'scannumlottery_area_limit', '', '积盖兑奖限定参加区域', '格式：省,市...；只支持省市级别，区域之前用单角逗号分隔', '200', '0', '1', '2021-06-09 17:00:58', '24003', NULL, NULL);
update vps_sys_datadic_m set DATA_EXPLAIN = '格式：扫码次数:兑换红包金额保留2位小数,扫码次数:大奖类型...' where data_id = 'scannumlottery_prize_item';

-- 检查河北省区是否已执行“20210609山东扫码次数兑换红包.sql”
-- 变更缓存版本号