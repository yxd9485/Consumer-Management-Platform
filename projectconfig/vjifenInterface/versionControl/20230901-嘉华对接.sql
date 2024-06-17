ALTER TABLE VPS_SKU_INFO ADD COLUMN JH_SKU VARCHAR(255) NULL COMMENT '嘉华sku' AFTER ZD_SKU;
INSERT INTO `vjifen_common3_other`.`vps_sys_diccategory_info`(`CATEGORY_KEY`, `CATEGORY_NAME`, `CATEGORY_CODE`, `DIC_TYPE`, `INVOKER`, `CATEGORY_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('feed189d-0cbb-4815-8d03-042e23ce2c57', '嘉华溯源产品地址', 'filter_jiahua_address', '1', 0, '嘉华溯源产品地址', NULL, '0', 1, '2023-09-13 11:33:56', '996', '2023-09-13 11:33:56', '996');
INSERT INTO `vjifen_common3_other`.`vps_sys_diccategory_info`(`CATEGORY_KEY`, `CATEGORY_NAME`, `CATEGORY_CODE`, `DIC_TYPE`, `INVOKER`, `CATEGORY_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('62ce64bd-4cb6-11ea-b627-6e6d36e3ad65', 'filter功能开关配置', 'filter_switch_setting', '1', 0, '功能开关配置', 112, '0', 1, '2023-04-11 10:56:02', '1', '2023-04-11 10:56:02', '1');
INSERT INTO `vjifen_common3_other`.`vps_sys_datadic_m`(`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('b9bd5515-46d3-4463-a546-7c5da3a10cc8', '62ce64bd-4cb6-11ea-b627-6e6d36e3ad65', 'switch_jh_sweep', '1', '嘉华开关', '嘉华码源配置', 1, '0', 1, '2023-08-31 13:00:05', '996', '2023-09-18 11:05:11', '996');
INSERT INTO `vjifen_common3_other`.`vps_sys_datadic_m`(`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) VALUES ('7390a22c-4f9e-404a-83cb-1e17b15557d8', 'feed189d-0cbb-4815-8d03-042e23ce2c57', 'jia_hua_product_code', '237650, 237648, 285723, 237401, 237404, 237405, 244282, 248235, 284612, 291027, 293351', '嘉华溯源编码', '', 1, '0', 1, '2023-09-13 11:38:18', '996', '2023-09-18 11:10:50', '996');
/*
测试重点
1 查看界面 产品配置 新增嘉华SKU 隐藏掉其他平台SKU(新增及修改)
2 查看 VPS_SKU_INFO 表中 是否有JH_SKU 字段 是否有值
3 查看 生成码源包 SKU 配置活动规则是否有效
4 校验链路是否完整
5 查看 码库批次id 是否更新
6 校验 批次活动金额分配是否 有效
*/
