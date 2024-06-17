-- 占比预警差值
ALTER TABLE `vps_vcode_activity_vpoints_cog`
ADD COLUMN `PRIZE_PERCENT_WARN`  double NULL COMMENT '占比预警差值' AFTER `PRIZE_PERCENT`;
ALTER TABLE `vps_vcode_activity_rebate_rule_templet_detail`
ADD COLUMN `PRIZE_PERCENT_WARN`  double NULL COMMENT '占比预警差值' AFTER `PRIZE_PERCENT`;

-- AI助手appid
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('23f9817e-bce3-11ea-8b2e-6e6d36e3ad65', 'f9bd3717-4ca2-11ea-b627-6e6d36e3ad65', 'ai_appid', 'wxd6c534b3305be4b7', 'AI助手appid', '', '1', '0', '1', NOW(), '1', NOW(), '1');

-- AI助手预警openid配置分类
INSERT INTO `vps_sys_diccategory_info` (`CATEGORY_KEY`, `CATEGORY_NAME`, `CATEGORY_CODE`, `DIC_TYPE`, `INVOKER`, `CATEGORY_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'AI助手预警openid配置', 'ai_remind_openid', '1', '0', 'AI助手预警openid配置', '109', '0', '1', NOW(), '1', NOW(), '1');


-- 预警openid
-- 3.0所有省区都执行
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('21e98002-bce3-11ea-8b2e-6e6d36e3ad65', '8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'ai_remind_tech_openid', 'oNs9Cv6tyCZ1cUEyJfA2sQlrPM0k,oNs9Cv3wjeaiu8oVMZk8YkNcbqxo,oNs9Cv7vQZp50rUQJ4gUW9WfL5_U,oNs9Cv7uW6wdCkJDizeAAkbeMeJY,oNs9Cv_fNQ-63TVrlHPb4VQPBRVE', '技术', '扫码、提现、大奖保存、微信支付等远行异常', '0', '0', '1', '2020-07-03 12:15:01', '1', '2020-07-08 15:38:12', '24004');
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('21e98001-bce3-11ea-8b2e-6e6d36e3ad65', '8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'ai_remind_pm_openid', '', '项目经理', '只需要项目经理接收', '0', '0', '1', '2020-07-03 12:15:01', '1', '2020-07-08 15:38:12', '24004');
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('21e98003-bce3-11ea-8b2e-6e6d36e3ad65', '8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'ai_remind_batch_activate', '', '批次激活预警', '', '3', '0', '1', '2020-07-03 12:15:01', '1', '2020-07-08 15:38:12', '24004');
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('21e9817e-bce3-11ea-8b2e-6e6d36e3ad65', '8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'ai_remind_prize_percent', '', '奖项占比预警openid', '多openid以半角逗号分割', '1', '0', '1', NOW(), '1', NOW(), '1');
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('21e9817e-b12f-11ea-8b2e-6e6d36e3ad65', '8ba86234-bce2-11ea-8b2e-6e6d36e3ad65', 'ai_remind_activity_end', '', '活动结束前48小时预警openid', '多openid以半角逗号分割', '2', '0', '1', NOW(), '1', NOW(), '1');

-- 以下按省区执行
-- 老白汾 --斯远
update vps_sys_datadic_m set data_value = 'oNs9Cv0D3Hj2cwPLtaI2N7m_0q5I,oNs9Cv4syThjGqad-nZckv3IlGWg' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv0D3Hj2cwPLtaI2N7m_0q5I,oNs9Cv4syThjGqad-nZckv3IlGWg' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv0D3Hj2cwPLtaI2N7m_0q5I,oNs9Cv4syThjGqad-nZckv3IlGWg' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv0D3Hj2cwPLtaI2N7m_0q5I,oNs9Cv4syThjGqad-nZckv3IlGWg' where data_id = 'ai_remind_activity_end';

-- 上海 - 王磊
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_activity_end';

-- 安徽
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_activity_end';

-- 辽宁
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_activity_end';

-- 甘肃
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_activity_end';

-- 江苏
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9CvyjG6JSkYGx0CtshsVUdb60' where data_id = 'ai_remind_activity_end';

-- 河南瓶装 - 新云
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

--云南
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

-- 广西
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

-- 吉林
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

-- 华南
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

-- 海南
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

-- 中粮海岸
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv-AaTpB_JjsNcSVcDslzP44' where data_id = 'ai_remind_activity_end';

-- 河北 - 秀平
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_activity_end';

-- 皇冠曲奇
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv1n7t2End-8qaubN8EwtqpQ' where data_id = 'ai_remind_activity_end';

-- 江西 - 孙雅洁
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_activity_end';

-- 四川
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_activity_end';

-- 内蒙
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv5uqY9XpHEWod0aME4gMKIQ' where data_id = 'ai_remind_activity_end';

-- 山东 - 金新
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_activity_end';

-- 重庆
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_activity_end';

-- 全麦白啤
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_activity_end';

-- 浙江
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_activity_end';

-- 北销
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_pm_openid';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_batch_activate';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_prize_percent';
update vps_sys_datadic_m set data_value = 'oNs9Cv93Z0Rz1FmPUq8RI79nVW8c' where data_id = 'ai_remind_activity_end';
