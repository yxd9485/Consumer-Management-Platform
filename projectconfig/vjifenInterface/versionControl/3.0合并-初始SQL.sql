--项目Job运行省区
--insert into vps_sys_diccategory_info values ('1642eb1a-5485-11ea-ba2a-6e6d36e3ad65','消费者促销Job运行省区', 'plateform_job', '1', '0', 'filter项目Job运行省区', 115, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'execute_extract_order_processing', 'ALL', '接口：核查处理中的提现记录job', '接口：核查处理中的提现记录job', 1, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'recharge_query', 'henanpz', '接口：手机充值查询结果job', '接口：手机充值查询结果job', 2, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_unpay_trade_for_expire', 'hebei', '接口：微信支付-处理未支付且已超时的交易记录 job', '接口：微信支付-处理未支付且已超时的交易记录 job', 3, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_trade_for_paying', 'hebei', '接口：微信支付-处理支付中订单的支付结果查询 job', '接口：微信支付-处理支付中订单的支付结果查询 job', 4, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_trade_for_refunding', 'hebei', '接口：微信支付-处理退款中订单的退款结果查询 job', '接口：微信支付-处理退款中订单的退款结果查询job', 5, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'dubious_user_convert_black_user', 'hebei', '平台：可疑用户一个月后自动进入黑名单job', '平台：可疑用户一个月后自动进入黑名单job', 10, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_bath_wait_activity_vpoints_cog', 'ALL', '平台：更新活动配置中各积分区间剩余数量job', '平台：更新活动配置中各积分区间剩余数量job', 11, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clear_double_prize_for_user', 'hebei', '平台：一码双奖清除已结束且未清除过的活动的标签job', '平台：一码双奖清除已结束且未清除过的活动的标签job', 12, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clear_double_prize_lottery', 'hebei', '平台：一码双奖清除过期的已中出奖项 job', '平台：一码双奖清除过期的已中出奖项 job', 13, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_express_sign_job', NULL, '平台：商城更新订单签收状态，发货后15天 job', '平台：商城更新订单签收状态，发货后15天job', 14, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'send_template_msg', 'hebei', '平台：模板消息推送 job', '平台：模板消息推送 job', 15, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'total_msg_expire_remind', 'ALL', '平台：统计规则到期job', '平台：统计规则到期job', 16, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clean_msg_expire_remind_info', 'ALL', '平台：清楚到期提醒job', '平台：清楚到期提醒job', 17, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'total_red_packet_msg_expire_remind', 'ALL', '平台：统计红包个数job', '平台：统计红包个数job', 18, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_release_of_suspects', 'ALL', '平台：可疑用户释放job', '平台：可疑用户释放job', 19, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'execute_rank_history', NULL, '平台：月度城市酒王排行job', '平台：月度城市酒王排行job', 20, '0', 1, NOW(), '1', NOW(), '1');
--insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'recycle_Prize', 'ALL', '平台：大奖回收job', '平台：大奖回收job', 21, '0', 1, NOW(), '1', NOW(), '1');


insert into vps_sys_diccategory_info values ('1642eb1a-5485-11ea-ba2a-6e6d36e3ad65','消费者促销Job运行省区', 'project_job', '1', '0', 'filter项目Job运行省区', 115, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'execute_extract_order_processing', 'sichuan', '接口：核查处理中的提现记录job', '接口：核查处理中的提现记录job', 1, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'recharge_query', NULL, '接口：手机充值查询结果job', '接口：手机充值查询结果job', 2, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_unpay_trade_for_expire', NULL, '接口：微信支付-处理未支付且已超时的交易记录 job', '接口：微信支付-处理未支付且已超时的交易记录 job', 3, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_trade_for_paying', NULL, '接口：微信支付-处理支付中订单的支付结果查询 job', '接口：微信支付-处理支付中订单的支付结果查询 job', 4, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_trade_for_refunding', NULL, '接口：微信支付-处理退款中订单的退款结果查询 job', '接口：微信支付-处理退款中订单的退款结果查询job', 5, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'dubious_user_convert_black_user', NULL, '平台：可疑用户一个月后自动进入黑名单job', '平台：可疑用户一个月后自动进入黑名单job', 10, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_bath_wait_activity_vpoints_cog', 'sichuan', '平台：更新活动配置中各积分区间剩余数量job', '平台：更新活动配置中各积分区间剩余数量job', 11, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clear_double_prize_for_user', NULL, '平台：一码双奖清除已结束且未清除过的活动的标签job', '平台：一码双奖清除已结束且未清除过的活动的标签job', 12, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clear_double_prize_lottery', NULL, '平台：一码双奖清除过期的已中出奖项 job', '平台：一码双奖清除过期的已中出奖项 job', 13, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_express_sign_job', NULL, '平台：商城更新订单签收状态，发货后15天 job', '平台：商城更新订单签收状态，发货后15天job', 14, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'send_template_msg', NULL, '平台：模板消息推送 job', '平台：模板消息推送 job', 15, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'total_msg_expire_remind', 'sichuan', '平台：统计规则到期job', '平台：统计规则到期job', 16, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clean_msg_expire_remind_info', 'sichuan', '平台：清楚到期提醒job', '平台：清楚到期提醒job', 17, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'total_red_packet_msg_expire_remind', 'sichuan', '平台：统计红包个数job', '平台：统计红包个数job', 18, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_release_of_suspects', 'sichuan', '平台：可疑用户释放job', '平台：可疑用户释放job', 19, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'execute_rank_history', NULL, '平台：月度城市酒王排行job', '平台：月度城市酒王排行job', 20, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'recycle_Prize', 'sichuan', '平台：大奖回收job', '平台：大奖回收job', 21, '0', 1, NOW(), '1', NOW(), '1');

-- 白啤
insert into vps_sys_diccategory_info values ('1642eb1a-5485-11ea-ba2a-6e6d36e3ad65','消费者促销Job运行省区', 'project_job', '1', '0', 'filter项目Job运行省区', 115, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'execute_extract_order_processing', 'qmbaipi', '接口：核查处理中的提现记录job', '接口：核查处理中的提现记录job', 1, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'recharge_query', NULL, '接口：手机充值查询结果job', '接口：手机充值查询结果job', 2, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_unpay_trade_for_expire', NULL, '接口：微信支付-处理未支付且已超时的交易记录 job', '接口：微信支付-处理未支付且已超时的交易记录 job', 3, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_trade_for_paying', NULL, '接口：微信支付-处理支付中订单的支付结果查询 job', '接口：微信支付-处理支付中订单的支付结果查询 job', 4, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'deal_trade_for_refunding', NULL, '接口：微信支付-处理退款中订单的退款结果查询 job', '接口：微信支付-处理退款中订单的退款结果查询job', 5, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'dubious_user_convert_black_user', NULL, '平台：可疑用户一个月后自动进入黑名单job', '平台：可疑用户一个月后自动进入黑名单job', 10, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_bath_wait_activity_vpoints_cog', 'qmbaipi', '平台：更新活动配置中各积分区间剩余数量job', '平台：更新活动配置中各积分区间剩余数量job', 11, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clear_double_prize_for_user', NULL, '平台：一码双奖清除已结束且未清除过的活动的标签job', '平台：一码双奖清除已结束且未清除过的活动的标签job', 12, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clear_double_prize_lottery', NULL, '平台：一码双奖清除过期的已中出奖项 job', '平台：一码双奖清除过期的已中出奖项 job', 13, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_express_sign_job', NULL, '平台：商城更新订单签收状态，发货后15天 job', '平台：商城更新订单签收状态，发货后15天job', 14, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'send_template_msg', NULL, '平台：模板消息推送 job', '平台：模板消息推送 job', 15, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'total_msg_expire_remind', 'qmbaipi', '平台：统计规则到期job', '平台：统计规则到期job', 16, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'clean_msg_expire_remind_info', 'qmbaipi', '平台：清楚到期提醒job', '平台：清楚到期提醒job', 17, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'total_red_packet_msg_expire_remind', 'qmbaipi', '平台：统计红包个数job', '平台：统计红包个数job', 18, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'update_release_of_suspects', 'qmbaipi', '平台：可疑用户释放job', '平台：可疑用户释放job', 19, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'execute_rank_history', NULL, '平台：月度城市酒王排行job', '平台：月度城市酒王排行job', 20, '0', 1, NOW(), '1', NOW(), '1');
insert into vps_sys_datadic_m values (UUID(),'1642eb1a-5485-11ea-ba2a-6e6d36e3ad65', 'recycle_Prize', 'qmbaipi', '平台：大奖回收job', '平台：大奖回收job', 21, '0', 1, NOW(), '1', NOW(), '1');




