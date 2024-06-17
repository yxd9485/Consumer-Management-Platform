ALTER TABLE `vpoints_exchange_log` 
ADD COLUMN `revoke_order_reason` varchar(255) NULL COMMENT '撤单原因' AFTER `goods_return_express_number`;