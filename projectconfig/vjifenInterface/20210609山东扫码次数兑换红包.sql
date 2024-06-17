-- 各省区数据库执行
-- 其它红包记录增加奖项描述字段 
ALTER TABLE `vps_common_packs_record`
ADD COLUMN `PRIZE_DESC`  varchar(50) NULL COMMENT '奖项描述' AFTER `PRIZE_TYPE`;

-- 积盖兑红包相关数据字典
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('5d1207bf-eca5-4ec6-90f1-cf3b0ddc9345', '62ce64bd-4cb6-11ea-b627-6e6d36e3ad65', 'switch_scannumlottery', '0', '积盖兑红包功能开关', '1：开（可以正常操作）、 0：关、yyyy-MM-dd或yyyy-MM-dd HH:mm:ss:指定日期后关闭', '1', '0', '1', '2021-06-09 17:47:24', '24003', '2021-06-09 18:07:37', '24003');
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('5b02ba0c-9f84-4b18-a81b-8a257759fd8b', 'a7098116-530a-11ea-ba2a-6e6d36e3ad65', 'scannumlottery_date_limit', '', '积盖况红包活动期限', '格式：活动开始日期yyyy-MM-dd#活动结束日期yyyy-MM-dd', '200', '0', '1', '2021-06-09 16:58:11', '24003', NULL, NULL);
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('b508e891-2051-4363-b5ef-5412c934d0c0', 'a7098116-530a-11ea-ba2a-6e6d36e3ad65', 'scannumlottery_sku_limit', '', '积盖兑红包参与SKU集合', '格式：SKU,SKU...', '200', '0', '1', '2021-06-09 16:59:42', '24003', NULL, NULL);
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('e69f6eb7-a8c7-40d1-9543-a422711f991e', 'a7098116-530a-11ea-ba2a-6e6d36e3ad65', 'scannumlottery_prize_item', '', '积盖兑红包奖项集合', '格式：扫码次数:兑换红包金额,扫码次数:兑换红包金额...', '200', '0', '1', '2021-06-09 17:00:58', '24003', NULL, NULL);
