--增加是否关注企业微信字段
alter table vps_consumer_user_info add IS_FOLLOW varchar(10) default '0' comment '是否关注企业微信';