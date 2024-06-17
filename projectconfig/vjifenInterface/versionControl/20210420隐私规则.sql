
-- 各省区数据库执行
ALTER TABLE `vps_consumer_user_info`
ADD COLUMN `PRIVACY_RULE_FLAG`  varchar(1) NULL DEFAULT 0 COMMENT '隐私规则：0未同意、1已同意' AFTER `FIRST_SCODE_STATUS`;

-- 不能扫码省区Code集体
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('c991b405-45cc-11e7-b8cf-7cd30abda111', '74d21e8e-eded-42bc-9961-aea9321565ee', 'unsweep_areacode', '', '不能扫码省区Code集体', '省区code以半角逗号分隔，foreign:表示国外', '9', '0', '0', now(), '1', now(), '1');
update vps_sys_datadic_m d, vps_sys_diccategory_info c set d.CATEGORY_KEY = c.CATEGORY_KEY where d.DATADIC_KEY = 'c991b405-45cc-11e7-b8cf-7cd30abda111' and c.CATEGORY_CODE = 'data_constant_config';
update sys_area_m set OFFICIALCODE = '710000' where AREACODE = '710000';
update sys_area_m set OFFICIALCODE = null where AREACODE = '710202';
update sys_area_m set OFFICIALCODE = '810000' where AREACODE = '810000';
update sys_area_m set OFFICIALCODE = null where AREACODE = '810202';
update sys_area_m set OFFICIALCODE = '820000' where AREACODE = '820000';
update sys_area_m set OFFICIALCODE = null where AREACODE = '820202';