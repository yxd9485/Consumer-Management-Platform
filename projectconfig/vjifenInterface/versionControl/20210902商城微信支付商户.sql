-- 各省区数据库执行

-- 商城微信支付通道
INSERT INTO `vps_sys_datadic_m` (`DATADIC_KEY`, `CATEGORY_KEY`, `DATA_ID`, `DATA_VALUE`, `DATA_ALIAS`, `DATA_EXPLAIN`, `SEQUENCE_NUM`, `DELETE_FLAG`, `VERSION`, `CREATE_TIME`, `CREATE_USER`, `UPDATE_TIME`, `UPDATE_USER`) 
VALUES ('de29fcf1-66c7-11eb-99b4-6e6d36e3adfe', '132c370a-4c9f-11ea-b627-6e6d36e3ad65', 'wxPay_shopping_mch_billno', '1613528905', '积分商城微信支付分配的商户号', '微信支付配置-积分商城微信支付分配的商户号', '2', '0', '1', '2021-02-04 17:03:37', '1', '2021-02-04 17:03:37', '1');

update vps_sys_datadic_m d1, vps_sys_datadic_m d2  set d1.data_value = d2.data_value where d1.data_id = 'wxPay_shopping_mch_billno' and d2.data_id = 'wxPay_mch_billno';