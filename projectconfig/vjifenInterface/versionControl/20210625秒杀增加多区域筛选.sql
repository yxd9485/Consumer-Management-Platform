
-- 各省区数据库执行
ALTER TABLE `seckill_activity` 
ADD COLUMN `filter_area_code` varchar(200) NULL COMMENT '筛选区域code集合' AFTER `county`,
ADD COLUMN `filter_area_name` varchar(1000) NULL COMMENT '筛选区域名称集合' AFTER `filter_area_code`;