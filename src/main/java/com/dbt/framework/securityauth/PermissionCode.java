package com.dbt.framework.securityauth;

/***
 * 文件名：AuthCode.java<br>
 * 版权：Copyright 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.<br>
 * 描述: 安全认证code编码值 修改人: 谷长鹏<br>
 * 修改时间：2014-06-20 14:18:16<br>
 * 修改内容：新增<br>
 */
public interface PermissionCode {

	/**
	 * 用户管理
	 * 
	 * @author gucp 2014-06-20
	 */
	public static interface userManager {
		/** 添加品牌账号即父账户 **/
		public static final String ADD_PARENTACCOUNT = "usermanager_addparent_account";
		/** 添加品牌用户即子账号 **/
		public static final String ADD_SONACCOUNT = "usermanager_addson_account";
		/** 添加dbt账户 */
		public static final String ADD_DBTACCOUNT = "usermanager_add_dbtccount";

		/** 编辑DBT账号 */
		public static final String EDIT_DBTACCOUNT = "usermanager_edit_dbtaccount";
		/** 编辑品牌账号 */
		public static final String EDIT_PARENTACCOUNT = "usermanager_edit_parentaccount";
		/** 编辑品牌用户 */
		public static final String EDIT_SONACCOUNT = "usermanager_edit_sonaccount";
		/** 启用、停用账号 */
		public static final String ACCOUNT_STATUS = "usernamager_account_status";
		/** 删除DBT账号 */
		public static final String DELETE_DBTACCOUNT = "usermanager_delete_dbt";
		/** 删除品牌账号 */
		public static final String DELETE_PARENTACCOUNT = "usermanager_delete_parent_account";
		/** 删除品牌用户 */
		public static final String DELETE_SONACCOUNT = "usermanager_delete_son_account";
	}

	/**
	 * 角色管理
	 * 
	 * @author gucp 2014-06-20
	 */
	public static interface roleManager {
		/** 添加 */
		public static final String ADD = "role_manager_add";
		/** 编辑 */
		public static final String EDIT = "role_manager_edit";
		/** 删除 */
		public static final String DELETE = "role_manager_delete";
		/** 系统功能角色授权 */
		public static final String SYS_FUN_ROLE_AUTHORIZE = "role_manager_sys_fun_role_authorize";
	}

	/**
	 * 企业信息
	 * 
	 * @author RoyFu
	 * @createTime 2014-06-25 10:52 AM
	 * @description
	 */
	public static interface enterpriseInfo {
		/** 修改企业信息 */
		public static final String EDIT_COMPANY = "enterprise_edit_info";
		/** 添加品牌 */
		public static final String ADD_BRAND = "enterprise_add_brand";
		/** 修改品牌 */
		public static final String EDIT_BRAND = "enterprise_edit_brand";
		/*** 删除品牌 */
		public static final String DELETE_BRAND = "enterprise_delete_brand";
		/** 添加品类 */
		public static final String ADD_CATEGORY = "enterprise_add_category";
		/** 修改品类 */
		public static final String EDIT_CATEGORY = "enterprise_edit_category";
		/** 删除品类 */
		public static final String DELETE_CATEGORY = "enterprise_delete_category";
		/** 添加SKU */
		public static final String ADD_SKU = "enterprise_add_sku";
		/** 修改SKU */
		public static final String EDIT_SKU = "enterprise_edit_sku";
		/** 删除SKU */
		public static final String DELETE_SKU = "enterprise_delete_sku";
	}

	/**
	 * 小票促销活动(扫小票活动)
	 * 
	 * @author RoyFu
	 * @createTime 2014-06-25 11:12 AM
	 * @description
	 */
	public static interface receiptsPromotion {
		/** 小票期数设置:添加期数 */
		public static final String ADD_PERIOD = "receipts_promotion_add_period";
		/** 添加活动 */
		public static final String ADD_PROMOTION = "receipts_promotion_add";
		/** 添加活动 查看往期活动 */
		public static final String CHECK_ACTIVITY_DETAIL = "check_activity_detail";
		/** 编辑活动 */
		public static final String EDIT_PROMOTION = "receipts_promotion_edit";
		/** 删除活动 */
		public static final String DELETE_PROMOTION = "receipts_promotion_delete";
	}

	/**
	 * 返利促销(V码)
	 * 
	 * @author Jiquanwei
	 * @createTime 2014-06-25 14:46:20
	 * @description
	 */
	public static interface vcodePromotion {

		/** 积分资源配置：添加活动 */
		public static final String ADD_VCODEPROMOTION = "vcode_promotion_add";
		/** 积分资源配置：修改活动 */
		public static final String EDIT_VCODEPROMOTION = "vcode_promotion_edit";
		/** 积分资源配置：删除活动 */
		public static final String DELETE_VCODEPROMOTION = "vcode_promotion_delete";
		/** 返利详情 */
		public static final String REBATE_DETAILS = "rebate_details";
	}

	/**
	 * V码管理
	 * 
	 * @author Jiquanwei
	 * @createTime 2014-06-25 14:46:20
	 * @description
	 */
	public static interface vcodeApplicate {
		/** 申请v码 */
		public static final String ADD_APPLICATE = "vcode_applicate_add";
		/** 编辑 */
		public static final String EDIT_APPLICATE = "vcode_applicate_edit";
	}

	/**
	 * 积分兑换
	 * 
	 * @author zhao
	 * @createTime 2014-06-25 15:10 AM
	 * @description
	 */
	public static interface convertIntegration {
		/** 充值 */
		public static final String CONVERT_RECHARGE = "convert_recharge";
		/** 添加订单号 */
		public static final String CONVERT_ADD_ORDER = "convert_add_order";
	}

	/**
	 * LOGO墙
	 * 
	 * @author yangxuan
	 * @createTime 2014-06-25 14:57 PM
	 * @description
	 */
	public static interface brandWant {
		/** 添加品牌 */
		public static final String ADD_BRANDWANT = "brand_want_add";
		/** 编辑 */
		public static final String EDIT_BRANDWANT = "brand_want_edit";
		/** 删除 */
		public static final String DELETE_BRANDWANT = "brand_want_delete";
	}

	/**
	 * 每周一奖活动
	 * 
	 * @author zhanglei
	 * @createTime 2014-06-25 14:12 PM
	 * @description
	 */
	public static interface weeklyPrize {

		/** 添加活动 */
		public static final String ADD_WEEKLYPRIZE = "weeklyprize_add";
		/** 编辑/排期 */
		public static final String EDIT_WEEKLYPRIZE = "weeklyprize_edit";
		/** 删除 */
		public static final String DELETE_WEEKLYPRIZE = "weeklyprize_delete";
		/** 审核 */
		public static final String WEEKLYPRIZE_APPROVAL = "weeklyprize_approval";
	}

	/**
	 * APP首页活动管理
	 * 
	 * @author xiewanpin
	 * @createTime 2014-06-25 16:07 PM
	 * @description
	 */
	public static interface homepageActivity {

		/** 添加活动 */
		public static final String ADD_HOMEPAGEACTIVITY = "homepage_activity_add";
		/** 编辑活动 */
		public static final String EDIT_HOMEPAGEACTIVITY = "homepage_activity_edit";
		/** 删除活动 */
		public static final String DELETE_HOMEPAGEACTIVITY = "homepage_activity_delete";
		/** 结束活动 */
		public static final String END_HOMEPAGEACTIVITY = "homepage_activity_end";
		/** 启动/暂停活动 */
		public static final String UPDETE_HOMEPAGEACTIVITY = "homepage_activity_updete";

	}

	/**
	 * 广告物料管理
	 * 
	 * @author RoyFu
	 * @createTime 2014-06-25 16:12
	 * @description
	 */
	public static interface advertisementManage {
		/** 添加 */
		public static final String ADD = "advertisement_manage_add";
		/** 修改 */
		public static final String EDIT = "advertisement_manage_edit";
		/** 删除 */
		public static final String DELETE = "advertisement_manage_delete";
	}

	/**
	 * 广告位管理
	 * 
	 * @author RoyFu
	 * @createTime 2014-06-25 16:15
	 * @description
	 */
	public static interface advertisementPlace {
		/** 添加广告位 */
		public static final String ADD_AD_PLACE = "advertisement_place_add";
		/** 修改广告位 */
		public static final String EDIT_AD_PLACE = "advertisement_place_edit";
		/** 删除广告位 */
		public static final String DELETE_AD_PLACE = "advertisement_place_delete";
		/** 投放广告 */
		public static final String PLACE_AD = "advertisement_manage_place";
	}

	/**
	 * 新小票活动的按钮权限CODE
	 * 
	 * @author HaoQi
	 * @createTime 2014-08-07
	 */
	public static interface rebateReceiptActivity {
		/** 添加活动 */
		public static final String ADD_ACTIVITY = "rebate_activity_add";
		/** 编辑/排期 */
		public static final String EDIT_ACTIVITY = "rebate_activity_edit";
		/** 删除 */
		public static final String DEL_ACTIVITY = "rebate_activity_del";
		/** 编辑小票 */
		public static final String EDIT_REBATERECEIPT = "rebate_receipt_edit";
		/** 返利详情 */
		public static final String REBATE_DETAILS = "rebate_details";
		/** 录入小票 */
		public static final String ADD_REBATERECEIPT = "rebate_receipt_add";
		/** 编辑返利列表小票活动(修改小票活动返利列表) */
		public static final String MODIFY_REBATE_LIST_ACTIVITY = "edit_list_activity";
		/** 运营操作权限 */
		public static final String OPERATION_ACTIVITY = "operation_activity";
		/** 海军操作权限 */
		public static final String NAVYCODE_ACTIVITY = "navycode_activity";
	}

	/**
	 * 数据字典的按钮权限CODE
	 * 
	 * @author HaoQi
	 * @createTime 2014-08-20
	 */
	public static interface dataDic {
		/** 添加类型 */
		public static final String ADD_DIC_CATEGORY = "dic_category_add";
		/** 编辑类型 */
		public static final String EDIT_DIC_CATEGORY = "dic_category_edit";
		/** 删除类型 */
		public static final String DEL_DIC_CATEGORY = "dic_category_del";

		/** 添加数据 */
		public static final String ADD_DIC_DATA = "dic_data_add";
		/** 编辑数据 */
		public static final String EDIT_DIC_DATA = "dic_data_edit";
		/** 删除数据 */
		public static final String DEL_DIC_DATA = "dic_data_del";
	}

	/**
	 * 初审列表
	 * 
	 * @author Jiquanwei
	 * @createTime 2014-08-30
	 */
	public static interface trialList {
		/** 查看所有数据 */
		public static final String VIEW_ALL_DATA = "firstTrialList_viewAllData";
	}

	/**
	 * 录入列表
	 * 
	 * @author Jiquanwei
	 * @createTime 2014-08-30
	 */
	public static interface writeList {
		/** 查看所有数据 */
		public static final String VIEW_ALL_DATA = "entryList_viewAllData";
		/** 根据SKU优先录入规则 */
		public static final String PRIORITY_ENTRY_RULES = "sku_priority_entry_rules";
		/** 录入列表-导出Excel */
		public static final String WRITE_EXPORT_EXCEL = "write_export_excel";
		/** 录入列表-导出txt */
		public static final String WRITE_EXPORT_TXT = "write_export_txt";
	}

	/**
	 * 红包活动列表权限
	 * 
	 * @author HaoQi
	 * @createTime 2014-11-17
	 */
	public static interface giftspackActivityList {
		/** 添加按钮 */
		public static final String ADD = "giftspack_activity_list_add";
		/** 编辑按钮 */
		public static final String EDIT = "giftspack_activity_list_edit";
		/** 删除按钮 */
		public static final String DEL = "giftspack_activity_list_del";
		/** 自营红包 */
		public static final String OPERATION_GIFTSPACK = "operation_giftspack";
	}

	/**
	 * 充值对账
	 * 
	 * @author RoyFu
	 * @createTime 2015-04-09
	 */
	public static interface rechargeBills {
		/** 财务权限 */
		public static final String FINANCE_AUTH = "finance";
		/** 客服权限 */
		public static final String SERVICE_AUTH = "custom_service";
	}

	public static interface receiptApproval {
		/** 可驳回小票审核管理 - 查看列表 */
		public static final String PROCESS_0_LIST = "process_0_list";
		/** 可驳回小票审核管理 - 数据审核 */
		public static final String PROCESS_0_DATA = "process_0_data";
		/** 小票头部管理 - 查看列表 */
		public static final String PROCESS_1_LIST = "process_1_list";
		/** 小票头部管理 - 数据审核 */
		public static final String PROCESS_1_DATA = "process_1_data";
		/** 小票录入（半自动审核） - 查看列表 */
		public static final String PROCESS_2_LIST = "process_2_list";
		/** 小票录入（半自动审核） - 数据审核 */
		public static final String PROCESS_2_DATA = "process_2_data";
		/** 半自动录入列表 - 查看列表 */
		public static final String PROCESS_3_LIST = "process_3_list";
		/** 半自动录入列表 - 数据审核 */
		public static final String PROCESS_3_DATA = "process_3_data";
		/** 小票人工审核- 查看列表 */
		public static final String PROCESS_4_LIST = "process_4_list";
		/** 小票人工审核 - 数据审核 */
		public static final String PROCESS_4_DATA = "process_4_data";
		/** 全人工录入列表 - 查看列表 */
		public static final String PROCESS_5_LIST = "process_5_list";
		/** 全人工录入列表 - 数据审核 */
		public static final String PROCESS_5_DATA = "process_5_data";

	}

	/**
	 * 小票统计
	 * 
	 * @author cpgu
	 * @createTime 2015-06-12
	 */
	public static interface receiptStatistics {
		/** 票头：个人 **/
		static final String PERSONAL_HEADSTATISTICS = "personal_headStatistics";
		/** 票头：所有 **/
		static final String LEADER_HEADSTATISTICS = "leader_headStatistics";
		/** 票头：导出 **/
		static final String EXPORT_HEADSTATISTICS ="export_headstatistics";
		/** 审核：个人 **/
		static final String PERSONAL_APPROVALPSTATISTICS = "personal_approvalpStatistics";
		/** 审核：所有 **/
		static final String LEADER_APPROVALSTATISTICS = "leader_approvalStatistics";
		/** 录入：个人 **/
		static final String PERSONAL_WRITESTATISTICS = "personal_writeStatistics";
		/** 录入：所有 **/
		static final String LEADER_WRITESTATISTICS = "leader_writeStatistics";
		/** 录入：导出 **/
		static final String EXPORT_WRITESTATISTICS = "export_writeStatistics";
	}
	
	/**
	 * 小票统计2.0
	 * 
	 * @author sunshunbo
	 * @createTime 2015-07-22
	 */
	public static interface receiptStatis {
		/** 票头：导出工作绩效表 **/
		static final String EXPORT_PERFORMANCE_EXL = "export_performance_exl";
		/** 票头：导出工资结算表 **/
		static final String EXPORT_SALARY_TXT = "export_salary_txt";
		/** 票头： 导出税务报表**/
		static final String EXPORT_TAX_EXL = "export_tax_exl";
		
		/** 人工审核：导出工作绩效表 **/
		static final String EXPORT_APPROVAL_PERFORMANCE_EXL = "export_approval_performance_exl";
		/** 人工审核：导出工资结算表 **/
		static final String EXPORT_APPROVAL_SALARY_TXT = "export_approval_salary_txt";
		/** 人工审核： 导出税务报表**/
		static final String EXPORT_APPROVAL_TAX_EXL = "export_approval_tax_exl";
		/** 人工审核： 外协导出明细**/
		static final String EXPORT_APPROVAL_OUTSOURCE_EXL = "export_approval_outsource_exl";

		/** 人工录入统计：工种查询 **/
		static final String EXPORT_MANUAL_WRITE_WORKTYPE = "manual_write_export_worktype";
		/** 人工录入统计：导出税务报表 **/
		static final String EXPORT_MANUAL_WRITE_TAX_EXL = "manual_write_export_tax_exl";
		/** 人工录入统计：导出工资结算报表 **/
		static final String EXPORT_MANUAL_WRITE_SALARY_TXT = "manual_write_export_salary_txt";
		/** 人工录入统计：导出工资绩效报表 **/
		static final String EXPORT_MANUAL_PERFORMANCE_EXL = "manual_write_export_performance_exl";
	
		/** 半自动录入统计：工种查询 **/
		static final String EXPORT_HALF_WRITE_WORKTYPE = "halfwrite_export_worktype";
		/** 半自动录入统计：导出税务报表 **/
		static final String EXPORT_HALF_WRITE_TAX_EXL = "halfwrite_export_tax_exl";
		/** 半自动录入统计：导出工资结算报表 **/
		static final String EXPORT_HALF_WRITE_SALARY_TXT = "halfwrite_export_salary_txt";
		/** 半自动录入统计：导出工资绩效报表 **/
		static final String EXPORT_HALF_PERFORMANCE_EXL = "halfwrite_export_performance_exl";
		
		/** 审核结果查询：补偿积分 **/
		static final String REISSUE_POINTS_COMMISSIONER = "reissue_points_commissioner";
	}
	
	public static interface kamanage {
		/** 查看超市组列表 */
		static final String LIST_MARKET_GROUP = "list_market_group";
		/** 添加超市组 */
		static final String ADD_MARKET_GROUP = "add_market_group";
		/** 修改超市组 */
		static final String EDIT_MARKET_GROUP = "edit_market_group";
	}
	
	/**
	 * 企业充值
	 * @createTime
	 * @description
	 */
	public static interface companyRecharge {
		/** 财务充值 */
		static final String FINANCE = "finance_recharge";
		/** 市场确认 */
		static final String MARKET = "market_confirm";
	}
}
