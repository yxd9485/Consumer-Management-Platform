-- 各省区数据库执行
ALTER TABLE `vps_common_packs_record`
ADD COLUMN `ADDRESS`  varchar(200) NULL COMMENT '详细地址' AFTER `COUNTY`,
ADD COLUMN `LONGITUDE`  varchar(30) NULL COMMENT '经度' AFTER `ADDRESS`,
ADD COLUMN `LATITUDE`  varchar(30) NULL COMMENT '纬度' AFTER `LONGITUDE`;