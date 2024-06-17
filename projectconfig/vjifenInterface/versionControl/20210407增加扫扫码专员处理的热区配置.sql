-- 各省区数据库执行
-- 扫码的地区在热区内按扫码专员处理
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('304b9bfb-2bc4-4a67-b51f-4e273564a123', '74d21e8e-eded-42bc-9961-aea9321565ee', 'sweep_code_restrain_hotarea', '', '扫码的地区在热区内按扫码专员处理', '热区主键,...', '1', '0', '1', now(), '1', now(), '1');
update vps_sys_datadic_m d, vps_sys_diccategory_info c set d.CATEGORY_KEY = c.CATEGORY_KEY where d.DATA_ID = 'sweep_code_restrain_hotarea' and c.CATEGORY_CODE = 'data_constant_config';
