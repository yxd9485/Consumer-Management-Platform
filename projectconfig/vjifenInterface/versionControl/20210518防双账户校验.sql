-- 各省区数据执行
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('d615f8f5-6478-4378-b381-b39b50701b27', '8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'project_openid_prefix', '', 'openid前三位', 'openid前三位，用于防止项目appid配置错误而造成双账户问题；未配置视为不校验', '15', '0', '1', '2021-05-17 13:53:39', '24004', NULL, NULL);
