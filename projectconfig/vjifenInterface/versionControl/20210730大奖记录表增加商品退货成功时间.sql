-- 各省区数据库执行
ALTER TABLE `vps_vcode_prize_record`
ADD COLUMN `GOODS_RETURN_SUCCESS_TIME`  datetime NULL DEFAULT NULL COMMENT '商品退货成功时间' AFTER `USE_TIME`;

ALTER TABLE `vps_vcode_prize_record`
ADD INDEX `idx_goods_return_success_time` (`GOODS_RETURN_SUCCESS_TIME`) USING BTREE ;

