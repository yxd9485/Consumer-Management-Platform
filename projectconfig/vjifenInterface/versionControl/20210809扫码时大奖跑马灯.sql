-- 各省区数据库执行
INSERT INTO `vps_sys_datadic_m`(`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('5bb5b6f6-1f83-4211-9321-a3fbe4d7c111', '62ce64bd-4cb6-11ea-b627-6e6d36e3ad65', 'switch_sweep_bigprize_marquee', '0', '扫码时大奖跑马灯开关0关闭1开启', '扫码时大奖跑马灯开关', 1, '0', 1, now(), '24004', NULL, NULL);

INSERT INTO `vps_sys_datadic_m`(`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('13bd8a56-ab66-4a11-bb69-b125c8d0f111', '74d21e8e-eded-42bc-9961-aea9321565ee', 'marquee_bigprize_lst', '', '跑马灯大奖类型', '', 1, '0', 1, now(), '1', now(), '1');

ALTER TABLE `vps_vcode_prize_record`  
ADD INDEX `idx_prize_type` (`GRAND_PRIZE_TYPE`) USING BTREE ;  

-- 可疑用户允许中出奖项类型配置
INSERT INTO `vps_sys_datadic_m`(`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('13bd8a56-ab66-4a11-bb69-b125c8d0f222', '74d21e8e-eded-42bc-9961-aea9321565ee', 'doubt_allow_prize_type', '', '可疑用户允许中出奖项类型配置', '可疑用户允许中出奖项类型配置,默认包含0、1、2不用配置', 1, '0', 1, now(), '1', now(), '1');
