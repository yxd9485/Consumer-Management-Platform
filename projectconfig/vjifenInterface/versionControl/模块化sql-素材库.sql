-- 素材库 主库执行
INSERT INTO `saas_db_main`.`sys_user_basis` (`USER_KEY`, `NICK_NAME`, `USER_NAME`, `COMPANY_KEY`, `PROJECT_SERVER_NAME`, `USER_PASSWORD`, `USER_STATUS`, `USER_TYPE`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('94', 'ui管理员', 'ui管理员', '201609141', 'piclib', 'MjEyMzJGMjk3QTU3QTVBNzQzODk0QTBFNEE4MDFGQzM=', '1', '1', '0', '2020-03-30 14:07:28', '1', '2020-03-30 14:07:32', '1');
INSERT INTO `saas_db_main`.`sys_user_role_relation` (`RELATION_KEY`, `USER_KEY`, `ROLE_KEY`) VALUES ('fdd4ff45-6e46-11ea-9e8e-6e6d36e3ad33', '94', '10');
INSERT INTO `saas_db_main`.`sys_role` (`ROLE_KEY`, `ROLE_NAME`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('10', 'UI管理员', '0', '2020-03-30 11:28:32', '1', '2020-03-30 11:28:22', '1');
INSERT INTO `saas_db_main`.`sys_role_function_relation` (`RELATION_KEY`, `ROLE_KEY`, `FUNCTION_KEY`) VALUES ('fdd4ff45-6e46-11ea-9e8e-6e6d36e3ad65', '10', '900100');
INSERT INTO `saas_db_main`.`sys_role_function_relation` (`RELATION_KEY`, `ROLE_KEY`, `FUNCTION_KEY`) VALUES ('d8497366-724a-11ea-9e8e-6e6d36e3ad65', '10', '900000');
INSERT INTO `saas_db_main`.`sys_function` (`FUNCTION_KEY`, `FUNCTION_NAME`, `PARENT_KEY`, `LINK_URL`, `FUNCTION_DESC`, `FUNCTION_LEVEL`, `MENU_ICON`, `FUNCTION_CODE`, `FUNCTION_STATUS`, `FUNCTION_TYPE`, `SEQUENCE_NUM`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('900000', '素材管理', '0', '#', '素材管理', '2', 'm-icon-setting', NULL, '1', '0', '570', '0', '2020-03-30 13:51:23', 'admin', '2020-03-30 13:51:31', 'admin');
INSERT INTO `saas_db_main`.`sys_function` (`FUNCTION_KEY`, `FUNCTION_NAME`, `PARENT_KEY`, `LINK_URL`, `FUNCTION_DESC`, `FUNCTION_LEVEL`, `MENU_ICON`, `FUNCTION_CODE`, `FUNCTION_STATUS`, `FUNCTION_TYPE`, `SEQUENCE_NUM`, `DELETE_FLAG`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('900100', '素材库', '900000', 'picLib/showPicLibList.do', '素材库', '3', NULL, NULL, '1', '0', '250', '0', '2020-03-25 11:15:30', 'admin', '2020-03-25 11:15:41', 'admin');

CREATE TABLE `vps_pic_library` (
			`INFO_KEY` varchar(36) NOT NULL COMMENT '主键',
			`PIC_NAME` varchar(36) NOT NULL COMMENT '素材名称',
			`PIC_TEMPLATE` varchar(20) DEFAULT NULL COMMENT '默认模板',
			`PIC_GROUP` varchar(10) DEFAULT NULL COMMENT '所属分组 1.logo 2.slogan 3.背景图 4.红包样式 5.品牌产品图 6.提示图 7.按钮样式 8.弹窗广告 9.首页轮播 10.商城轮播 11.活动规则 12.图片链接',
			`PIC_WIDTH` varchar(25) DEFAULT NULL COMMENT '分辨率宽',
			`PIC_HEIGHT` varchar(25) DEFAULT NULL COMMENT '分辨率高',
			`PIC_X` varchar(25) DEFAULT NULL COMMENT 'x坐标',
			`PIC_Y` varchar(25) DEFAULT NULL COMMENT 'y坐标',
			`PIC_URL` varchar(100) DEFAULT NULL COMMENT '图片url',
			`DELETE_FLAG` varchar(1) DEFAULT NULL COMMENT '删除标识',
			`IS_DEFAULT` varchar(1) DEFAULT NULL COMMENT '是否为默认图片',
			`START_POINT` varchar(1) DEFAULT NULL COMMENT '坐标起点',
			`CREATE_TIME` datetime DEFAULT NULL COMMENT '创建时间',
			`CREATE_USER` varchar(36) DEFAULT NULL COMMENT '创建人',
			`UPDATE_TIME` datetime DEFAULT NULL COMMENT '修改时间',
			`UPDATE_USER` varchar(36) DEFAULT NULL COMMENT '修改人',
	PRIMARY KEY (`INFO_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

