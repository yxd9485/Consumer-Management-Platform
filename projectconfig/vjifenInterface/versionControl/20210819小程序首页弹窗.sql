-- 各省区数据库执行
alter table vps_ad_up add FRONT_DELAYED varchar(36) comment '前置延时' after POPNUM;
update vps_ad_up set FRONT_DELAYED = '1.5';