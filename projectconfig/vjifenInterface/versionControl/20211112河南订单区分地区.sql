-- 浙江订单管理账户（主库执行）
-- 密码：6DMYJFBSQ
-- 用户key需要重新查询
insert into sys_user_basis values ('146', '浙江订单', '浙江订单管理', '201609141', 'NEZBRkE5QkI5QzM5MzM2RkQ4RDI0RDQ0MkQ1RDQ3Nzc=', 'henanpz', '1', '1', '0', NOW(), '0', NOW(), '0');
INSERT INTO `sys_user_role_relation` values (UUID(), '146', '6'); -- 订单管理


-- 河南数据库执行
update vps_sys_datadic_m set `DATA_VALUE` = '41,33' WHERE `DATA_ID` = 'project_area';

-- 积分商城订单区分地区显示的节点时间（上线时需要修改时间）
insert into `vps_sys_datadic_m`(`DATADIC_KEY`,`CATEGORY_KEY`,`DATA_ID`,`DATA_VALUE`,`DATA_ALIAS`,`DATA_EXPLAIN`,`SEQUENCE_NUM`,`DELETE_FLAG`,`VERSION`,`CREATE_TIME`,`CREATE_USER`,`UPDATE_TIME`,`UPDATE_USER`)
values(UUID(),'ec0be6e8-a17b-4425-b6cf-664454a4b840','vpoints_order_area_time','2021-11-12 23:59:59','积分商城订单区分地区显示的节点时间','商城订单区分地区显示的节点时间',8,'0',1,'2021-11-12 17:10:37','50','2021-11-12 17:11:01','50');
