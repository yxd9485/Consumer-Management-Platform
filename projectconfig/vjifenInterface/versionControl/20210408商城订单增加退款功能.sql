-- 各省区数据库执行

ALTER TABLE `vpoints_trade_refund`
ADD COLUMN `refund_type`  varchar(1) NULL COMMENT '退款类型：0撤单/退货、1仅退款' AFTER `trade_no`,
ADD COLUMN `refund_vpoints`  bigint(20) NULL DEFAULT 0 COMMENT '退款积分' AFTER `refund_fee`,
ADD COLUMN `refund_reason`  varchar(100) NULL COMMENT '退款原因' AFTER `refund_vpoints`;

ALTER TABLE `vpoints_exchange_log`
ADD COLUMN `refund_pay`  bigint NULL DEFAULT 0 COMMENT '累计退款金额单位分' AFTER `exchange_time`,
ADD COLUMN `refund_vpoints`  bigint NULL DEFAULT 0 COMMENT '累计退款积分' AFTER `refund_pay`;