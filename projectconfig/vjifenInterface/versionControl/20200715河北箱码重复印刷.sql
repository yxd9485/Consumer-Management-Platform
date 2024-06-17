
-- 指定码库表增加扩展字段
ALTER TABLE `vps_qrcode_hbkxshskwjd085006b20200521033_1` 
ADD COLUMN `QRCODE_NUM` varchar(20) NULL DEFAULT NULL COMMENT '二维码序列号' AFTER `TICKET_CODE`,
ADD COLUMN `QRCODE_NUM_USED` varchar(20) NULL DEFAULT NULL COMMENT '已使用二维码序列号' AFTER `QRCODE_NUM`;

-- 更新码库表中二维码序列号
导入20200715河北箱码重复印刷-NBY码库二维码序列号更新.sql

-- 更新历史扫码的is_finish状态
update vps_qrcode_hbkxshskwjd085006b20200521033_1 set IS_FINISH = '0' where USE_COUNT < 2;