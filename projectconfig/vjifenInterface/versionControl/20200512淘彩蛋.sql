-- 省区数据库
-- 淘彩蛋活动配置
CREATE TABLE `vps_vcode_tao_easteregg_cog` (
  `INFO_KEY` varchar(36) NOT NULL COMMENT '主键',
  `TAO_NO` varchar(20) DEFAULT NULL COMMENT '规则编号',
  `TAO_NAME` varchar(50) DEFAULT NULL COMMENT '规则名称',
  `START_DATE` date DEFAULT NULL COMMENT '开始日期',
  `END_DATE` date DEFAULT NULL COMMENT '结束日期',
  `TAO_MEMBER_ORDER` varchar(200) DEFAULT NULL COMMENT '入会口令集合',
  `LADDER_TYPE` varchar(1) DEFAULT '0' COMMENT '活动扫码计数类型：0单活动、1组合活动',
  `VCODE_ACTIVITY_KEYS` varchar(500) DEFAULT NULL COMMENT '关联活动主键集合',
  `LADDER_RULE` text COMMENT '阶梯规则',
  `DELETE_FLAG` varchar(1) DEFAULT NULL,
  `CREATE_TIME` datetime DEFAULT NULL,
  `CREATE_USER` varchar(36) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `UPDATE_USER` varchar(36) DEFAULT NULL,
  PRIMARY KEY (`INFO_KEY`),
  KEY `idx_startDate` (`START_DATE`) USING BTREE,
  KEY `idx_endDate` (`END_DATE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='淘彩蛋活动配置';

-- 只执行3.0合并版省区
insert into vps_sys_datadic_m values ('62ce64bd-4cb6-11ea-b627-6e6d36e3a777','62ce64bd-4cb6-11ea-b627-6e6d36e3ad65', 'switch_taoEasterEgg', '1', '淘彩蛋开关', '功能开关配置- 0关 1开', 18, '0', 1, NOW(), '1', NOW(), '1');


-- 中心库
-- 菜单
insert into sys_function values('401400','淘彩蛋规则','400000','taoEasterEgg/showTaoEasterEggCogList.do','淘彩蛋规则','2','',null, '1', '0', '310', '0', NOW(), 'admin', NOW(), 'admin'); update sys_function set SEQUENCE_NUM = '310' where FUNCTION_KEY = '401200'; 
insert into sys_role_function_relation values(UUID(),'1', '401400');        
insert into sys_role_function_relation values(UUID(),'2', '401400');    