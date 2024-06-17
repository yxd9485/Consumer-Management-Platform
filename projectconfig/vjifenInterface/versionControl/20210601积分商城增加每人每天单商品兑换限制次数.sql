-- 各省区数据库增加
ALTER TABLE `vpoints_goods_info`
ADD COLUMN `goods_user_day_limit`  int NULL DEFAULT NULL COMMENT '每人每天兑换限制个数' AFTER `goods_limit`;