<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String cpath = request.getContextPath(); 
	String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");

	String activityVersion1 = PropertiesUtil.getPropertyValue("activityVersion_type1");
	activityVersion1 = new String(activityVersion1.getBytes("ISO-8859-1"), "UTF-8");
	String activityVersion2 = PropertiesUtil.getPropertyValue("activityVersion_type2");
	if (StringUtils.isNotBlank(activityVersion2)) {
	    activityVersion2 = new String(activityVersion2.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion3 = PropertiesUtil.getPropertyValue("activityVersion_type3");
	if (StringUtils.isNotBlank(activityVersion3)) {
	    activityVersion3 = new String(activityVersion3.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion4 = PropertiesUtil.getPropertyValue("activityVersion_type4");
	if (StringUtils.isNotBlank(activityVersion4)) {
	    activityVersion4 = new String(activityVersion4.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion5 = PropertiesUtil.getPropertyValue("activityVersion_type5");
	if (StringUtils.isNotBlank(activityVersion5)) {
	    activityVersion5 = new String(activityVersion5.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion6 = PropertiesUtil.getPropertyValue("activityVersion_type6");
	if (StringUtils.isNotBlank(activityVersion6)) {
	    activityVersion6 = new String(activityVersion6.getBytes("ISO-8859-1"), "UTF-8");
	}
    String activityVersion7 = PropertiesUtil.getPropertyValue("activityVersion_type7");
    if (StringUtils.isNotBlank(activityVersion7)) {
        activityVersion7 = new String(activityVersion7.getBytes("ISO-8859-1"), "UTF-8");
    }
    String activityVersion8 = PropertiesUtil.getPropertyValue("activityVersion_type8");
    if (StringUtils.isNotBlank(activityVersion8)) {
        activityVersion8 = new String(activityVersion8.getBytes("ISO-8859-1"), "UTF-8");
    }
    String activityVersion9 = PropertiesUtil.getPropertyValue("activityVersion_type9");
    if (StringUtils.isNotBlank(activityVersion9)) {
        activityVersion9 = new String(activityVersion9.getBytes("ISO-8859-1"), "UTF-8");
    }
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
	<script type="text/javascript" src="<%=cpath%>/assets/js/plugins/zonesheets.js?v=3"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			// 初始化功能
			initPage();
		});
		
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			// 页面校验
			var v_flag = true;
			$(".validate_tips:not(:hidden)").each(function(){
				if($(this).text() != ""){
					$.fn.alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
			// 日期
			var startDate = $("input[name='startDate']").val();
			var endDate = $("input[name='endDate']").val();
			if(startDate == "" || endDate == "") {
				$("input[name='startDate']").parents(".content").find(".validate_tips").show();
				$("input[name='startDate']").parents(".content").find(".validate_tips").text("活动时间不能为空");
				$("input[name='startDate']").parents(".content").find(".validate_tips").addClass("valid_fail_text");
				return false;
			}
			
			// SKU
			var skuKey = $("select[name='skuKey']:active").val();
			if(skuKey == ""){
				$("select[name='skuKey']:active").parents(".skuInfo").find(".validate_tips").text("未选SKU");
				$("select[name='skuKey']:active").parents(".skuInfo").find(".validate_tips").addClass("valid_fail_text");
				return false;
			}
            
			// 一码多扫时间范围
			var sDate = $("input[name='sDate']").val();
			var eDate = $("input[name='eDate']").val();
			var sTime = $("input[name='sTime']").val();
			var eTime = $("input[name='eTime']").val();
			if(sDate != "" && eDate != "" && sTime != "" && eTime != ""){
				if(!compareTime(sDate + " " + sTime, eDate + " " + eTime)){
					$("#stimeLabel").text("前一时间不能大于后一时间").addClass("valid_fail_text").show();
	                return false;
				}
			}
			
            // 扫码积分
            var rebateVpoints = $("input[name='rebateVpoints']").val();
            if(rebateVpoints == ""){
                $(".rebateVpoints").find(".validate_tips").text("扫码积分不允许为空");
                $(".rebateVpoints").find(".validate_tips").addClass("valid_fail_text");
                $(".rebateVpoints").find(".validate_tips").show();
                return false;
            }
			// 区间匹配与校对[首次扫码]
			var firstMin = $("input[name='firstRebateMin']").val();
			var firstMax = $("input[name='firstRebateMax']").val();
			if((firstMin == "" && firstMax != "") || (firstMin != "" && firstMax == "")){
				$(".firstRebate").find(".validate_tips").text("区间值不允许有单个");
				$(".firstRebate").find(".validate_tips").addClass("valid_fail_text");
				$(".firstRebate").find(".validate_tips").show();
				return false;
			}
			// 区间匹配与校对[可疑用户]
			var doubtType = $("input[name='doubtRuleType']:checked").val();
			if(doubtType == "2") {
				var doubtMin = $("input[name='doubtRuleRangeMin']").val();
				var doubtMax = $("input[name='doubtRuleRangeMax']").val();
				if((doubtMin == "" && doubtMax != "") || (doubtMin != "" && doubtMax == "")){
					$(".doubt").find(".validate_tips").text("可疑用户区间值不允许有单个");
					$(".doubt").find(".validate_tips").addClass("valid_fail_text");
					$(".doubt").find(".validate_tips").show();
					return false;
				}
			}
			/* 删除黑名单配置功能，黑名单用户均返回0.00元
			// 区间匹配与校对[黑名单用户]
			var dangerType = $("input[name='dangerRuleType']:checked").val();
			if(dangerType == "2") {
				var dangerMin = $("input[name='dangerRuleRangeMin']").val();
				var dangerMax = $("input[name='dangerRuleRangeMax']").val();
				if((dangerMin == "" && dangerMax != "") || (dangerMin != "" && dangerMax == "")){
					$(".danger").find(".validate_tips").text("黑名单用户区间值不允许有单个");
					$(".danger").find(".validate_tips").addClass("valid_fail_text");
					$(".danger").find(".validate_tips").show();
					return false;
				}
			} */
			//add by cpgu 20160516 begin
			//规则1、规则2、规则3校验 
			var ruleCogFlag = "0";
			$(".rule").each(function(){
				if($(this).val() != ""){
					ruleCogFlag = "1";
				}
			});
			if(ruleCogFlag==='0'){
				$.fn.alert("规则一或规则二或规则三必须填一个！");
				return false;
			}
			//可疑账户和金额规则检验 
			var dubious_checked=$("#dubious-doubtRuleType").is(":checked"),
				sumrule_checked=$("#sumrule-doubtRuleType").is(":checked");
			if(!dubious_checked&&!sumrule_checked){
				$.fn.alert("可疑账户和金额规则必须选择其中一个！");
				return false;
			}else if(dubious_checked && $("input[name='doubtRuleCoe']").val() == ""){
				$.fn.alert("可疑账户配置系数不能为空！");
				return false;
			}else if(sumrule_checked 
					&& ($("input[name='doubtRuleRangeMin']").val() == ""
					|| $("input[name='doubtRuleRangeMax']").val() == "")){
				$.fn.alert("可疑账户配置区间不能为空！");
				return false;
			}
			
			//黑名单账户和金额规则检验 
			/* var dangerRuleCoefficient=$("#dangerRuleCoefficient").is(":checked"),
				dangerRuleMoney=$("#dangerRuleMoney").is(":checked");
			if(!dangerRuleCoefficient&&!dangerRuleMoney){
				$.fn.alert("黑名单账户和金额规则必须选择其中一个！");
				return false;
			}else if(dangerRuleCoefficient && $("input[name='dangerRuleCoe']").val() == ""){
				$.fn.alert("黑名单配置系数不能为空！");
				return false;
			}else if(dangerRuleMoney 
					&& ($("input[name='dangerRuleRangeMin']").val() == ""
					|| $("input[name='dangerRuleRangeMax']").val() == "")){
				$.fn.alert("黑名单配置区间不能为空！");
				return false;
			} */
			
			return true;
		}
		
		function compareTime(startDate, endDate) {   
			 if (startDate.length > 0 && endDate.length > 0) {   
			    var startDateTemp = startDate.split(" ");   
			    var endDateTemp = endDate.split(" ");   
			                   
			    var arrStartDate = startDateTemp[0].split("-");   
			    var arrEndDate = endDateTemp[0].split("-");   
			  
			    var arrStartTime = startDateTemp[1].split(":");   
			    var arrEndTime = endDateTemp[1].split(":");   
			  
				var allStartDate = new Date(arrStartDate[0], arrStartDate[1], arrStartDate[2], arrStartTime[0], arrStartTime[1], arrStartTime[2]);   
				var allEndDate = new Date(arrEndDate[0], arrEndDate[1], arrEndDate[2], arrEndTime[0], arrEndTime[1], arrEndTime[2]);   
				                   
				if (allStartDate.getTime() > allEndDate.getTime()) {   
				        // alert("startTime不能大于endTime，不能通过");   
				        return false;   
				} else {   
				    // alert("startTime小于endTime，所以通过了");   
				    return true;   
			    }   
			} else {   
			    // alert("时间不能为空");   
			    return false;   
			}   
		}
		
		function initPage() {
			$(".isMoreSweepO").on("change", function(){
				$("select[name='prizePattern']").attr("readonly", true);
				$("input[name='validCount']").attr("readonly", true);
				$("input[name='sDate']").attr("disabled", true).attr("class","form-control input-width-medium Wdate");
				$("input[name='eDate']").attr("disabled", true).attr("class","form-control input-width-medium Wdate");
				$("input[name='sTime']").attr("disabled", true).attr("class","form-control input-width-medium Wdate");
				$("input[name='eTime']").attr("disabled", true).attr("class","form-control input-width-medium Wdate");
				$("#sdateLabel").html("").removeClass("valid_fail_text");
				$("#stimeLabel").html("").removeClass("valid_fail_text");
			});
			
			$(".isMoreSweepT").on("change", function(){
				$("select[name='prizePattern']").attr("readonly", false);
				$("input[name='validCount']").attr("readonly", false);
				$("input[name='sDate']").attr("disabled", false).attr("class","form-control input-width-medium Wdate required preTime");
				$("input[name='eDate']").attr("disabled", false).attr("class","form-control input-width-medium Wdate required sufTime");
				$("input[name='sTime']").attr("disabled", false).attr("class","form-control input-width-medium Wdate required");
				$("input[name='eTime']").attr("disabled", false).attr("class","form-control input-width-medium Wdate required");
				$("#sdateLabel").addClass("validate_tips");
				$("#stimeLabel").addClass("validate_tips");
			});
			
            // 切换首扫返利类型
            if("${activityCog.firstRebateType}" == "1"){
                $("#firstMoneylable").css("display", "none");
                $("#firstRebateMin").removeClass("money").addClass("integer").attr("maxVal","999");
                $("#firstRebateMax").removeClass("money").addClass("integer").attr("maxVal","999");
            }else if("${activityCog.firstRebateType}" == "0"){
                $("#firstScorelable").css("display", "none");
                $("#firstRebateMin").removeClass("integer").addClass("money").attr("maxVal","99");
                $("#firstRebateMax").removeClass("integer").addClass("money").attr("maxVal","99");
            }
            
            $(".firstRadioMoney").on("change",function(){
                $("#firstMoneylable").css("display", "block");
                $("#firstScorelable").css("display", "none");
                $("#firstRebateMin").val("").removeClass("integer").addClass("money").attr("maxVal","99");
                $("#firstRebateMax").val("").removeClass("integer").addClass("money").attr("maxVal","99");
            });
            $(".firstRadioScore").on("change",function(){
                $("#firstMoneylable").css("display", "none");
                $("#firstScorelable").css("display", "block");
                $("#firstRebateMin").val("").removeClass("money").addClass("integer").attr("maxVal","999");
                $("#firstRebateMax").val("").removeClass("money").addClass("integer").attr("maxVal","999");
            });
            
            // 切换可疑返利类型
            if("${activityCog.doubtRebateType}" == "1"){
                $("#doubtMoneylable").css("display", "none");   
                $("#doubtRuleRangeMin").removeClass("money").addClass("number").attr("maxVal","999");
                $("#doubtRuleRangeMax").removeClass("money").addClass("number").attr("maxVal","999");
            }else if("${activityCog.doubtRebateType}" == "0"){
                $("#doubtScorelable").css("display", "none");
                $("#doubtRuleRangeMin").removeClass("number").addClass("money").attr("maxVal","99");
                $("#doubtRuleRangeMax").removeClass("number").addClass("money").attr("maxVal","99");
            }
            
            $(".doubtRadioMoney").on("change",function(){
                $("#doubtMoneylable").css("display", "block");
                $("#doubtScorelable").css("display", "none");
                $("#doubtRuleRangeMin").val("").removeClass("number").addClass("money").attr("maxVal","99");
                $("#doubtRuleRangeMax").val("").removeClass("number").addClass("money").attr("maxVal","99");
            });
            $(".doubtRadioScore").on("change",function(){
                $("#doubtMoneylable").css("display", "none");
                $("#doubtScorelable").css("display", "block");
                $("#doubtRuleRangeMin").val("").removeClass("money").addClass("number").attr("maxVal","999");
                $("#doubtRuleRangeMax").val("").removeClass("money").addClass("number").attr("maxVal","999");
            });
            
			// 可疑用户
			$("tr.doubt-rule").delegate("input[type=radio]", "click", function(){
				var currentVal = $(this).val();
                if(currentVal == "1"){
                    $("tr.doubt-rule:eq(0)").find("input.number").prop("disabled", false);
                    $("tr.doubt-rule:eq(0)").find("input.money").prop("disabled", false);
                    $("tr.doubt-rule:eq(1)").find("input.money").prop("disabled", true);
                    $("tr.doubt-rule:eq(1)").find("input.number").prop("disabled", true);
                    $("tr.doubt-rule:eq(1)").find("input.money").val("");
                    $("tr.doubt-rule:eq(1)").find("input.number").val("");
                } else {
                    $("tr.doubt-rule:eq(1)").find("input.money").prop("disabled", false);
                    $("tr.doubt-rule:eq(1)").find("input.number").prop("disabled", false);
                    $("tr.doubt-rule:eq(0)").find("input.number").prop("disabled", true);
                    $("tr.doubt-rule:eq(0)").find("input.money").prop("disabled", true);
                    $("tr.doubt-rule:eq(0)").find("input.number").val("");
                    $("tr.doubt-rule:eq(0)").find("input.money").val("");
                }
			});
			/** 删除黑名单配置功能，黑名单用户均返回0.00元
			// 黑名单用户
			$("tr.danger-rule").delegate("input[type=radio]", "click", function(){
				var currentVal = $(this).val();
				if(currentVal == "1"){
					$("tr.danger-rule:eq(0)").find("input.number").prop("disabled", false);
					$("tr.danger-rule:eq(1)").find("input.number").prop("disabled", true);
					$("tr.danger-rule:eq(1)").find("input.number").val("");
				} else {
					$("tr.danger-rule:eq(1)").find("input.number").prop("disabled", false);
					$("tr.danger-rule:eq(0)").find("input.number").prop("disabled", true);
					$("tr.danger-rule:eq(0)").find("input.number").val("");
				}
			}); */
			// 处理配置
			var doubtType = "${activityCog.doubtRuleType}";
			if(doubtType == "1") {
				$("tr.doubt-rule").find("input[type=radio][value='1']").prop("checked", true);
				$("tr.doubt-rule").find("input[type=radio][value='1']").trigger("click");
			} else if(doubtType == "2") {
				$("tr.doubt-rule").find("input[type=radio][value='2']").prop("checked", true);
				$("tr.doubt-rule").find("input[type=radio][value='2']").trigger("click");
			} else {
				$("tr.doubt-rule").find("input.number").prop("disabled", true);
			}
			/* 删除黑名单配置功能，黑名单用户均返回0.00元
			var dangerType = "${activityCog.dangerRuleType}";
			if(dangerType == "1") {
				$("tr.danger-rule").find("input[type=radio][value='1']").prop("checked", true);
				$("tr.danger-rule").find("input[type=radio][value='1']").trigger("click");
			} else if(dangerType == "2") {
				$("tr.danger-rule").find("input[type=radio][value='2']").prop("checked", true);
				$("tr.danger-rule").find("input[type=radio][value='2']").trigger("click");
			} else {
				$("tr.danger-rule").find("input.number").prop("disabled", true);
			} */
			
			$("select[name='skuKey']:active").change(function(){
				$(this).parents(".skuInfo").find(".validate_tips").text("");
				$(this).parents(".skuInfo").find(".validate_tips").removeClass("valid_fail_text");
			});
			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
					var blacklistCogFlag = "0";
					$("input[mark=blacklist]:not([type=radio])").each(function(){
						if($(this).val() != ""){
							blacklistCogFlag = "1";
						}
					});
					$("input[name=blacklistFlag]").val(blacklistCogFlag);
					
					var flag = validForm();
					if(flag) {
						if(btnEvent == "2"){
							if(confirm("确认提交？")){
								return true;
							} else {
								return false;
							}
						} else {
							return true;
						}
					} else {
						return false;
					}
				}
			});
		}
		
	</script>
	
	<style>
		.white {
			color: white;
		}
		.blocker {
			float: left;
			vertical-align: middle;
			margin-right: 8px;
			margin-top: 8px;
		}
		.en-larger {
			margin-left: 8px;
		}
		.en-larger2 {
			margin-left: 12px;
		}
		.ex-larger {
			margin-right: 8px;
		}
		.show-sku-name {
			float: left;
			margin-left: 8px;
			margin-top: 8px;
		}
		.top-only {
			border-top: 1px solid #e1e1e1;
		}
		.tab-radio {
			margin: 10px 0 0 !important;
		}
		.validate_tips {
			padding: 8px !important;
		}
		.mx-right {
			float: right;
			margin-top: 2px;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 活动管理</a></li>
        	<li class="current"><a title="">
                <c:choose>
                    <c:when test="${activityCog.activityType == '1'}">一罐一码活动</c:when>
                    <c:when test="${activityCog.activityType == '2'}">一瓶一码活动</c:when>
                </c:choose>
            </a></li>
        	<li class="current"><a title="">修改活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeActivity/doVcodeActivityEdit.do?menuType=activity">
            	<input type="hidden" name="vcodeActivityKey" value="${activityCog.vcodeActivityKey}" />
            	<input type="hidden" name="activityType" value="${activityCog.activityType}" />
            	<input type="hidden" name="blacklistFlag" value="" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            			<a class="btn btn-info mx-right ex-larger reCatch btn-blue" style="display:none;"
            				data-url="<%=cpath%>/vpointsPool/reloadVpointsPool.do">刷新企业积分</a>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="vcodeActivityName" tag="validate" value="${activityCog.vcodeActivityName}"
	                       					class="form-control required" autocomplete="off" maxlength="200" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="startDate" class="form-control input-width-medium Wdate required preTime"
                                       		tag="validate" autocomplete="off" value="${activityCog.startDate}"
                                       		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-medium Wdate required sufTime"
                                       		tag="validate" autocomplete="off" value="${activityCog.endDate}"
                                       		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content skuInfo">
	                       				<select class="form-control input-width-larger" name="skuKey" <c:if test="${!empty activityCog.skuKey}">disabled</c:if>>
	                       					<option>请选择SKU</option>
	                       					<c:if test="${!empty skuList}">
	                       					<c:forEach items="${skuList}" var="sku">
	                       					<option value="${sku.skuKey}" <c:if test='${sku.skuKey == activityCog.skuKey}'>selected</c:if> >${sku.skuName}</option>
	                       					</c:forEach>
	                       					</c:if>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动版本：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select class="form-control input-width-larger" name="activityVersion">
                                            <option <c:if test="${activityCog.activityVersion == '1'}">selected</c:if> value="1"><%=activityVersion1%></option>
                                            <% if (activityVersion2 != null && activityVersion2.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '2'}">selected</c:if> value = "2"><%=activityVersion2%></option>
                                            <%} %>
                                            <% if (activityVersion3 != null && activityVersion3.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '3'}">selected</c:if> value = "3"><%=activityVersion3%></option>
                                            <%} %>
                                            <% if (activityVersion4 != null && activityVersion4.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '4'}">selected</c:if> value = "4"><%=activityVersion4%></option>
                                            <%} %>
                                            <% if (activityVersion5 != null && activityVersion5.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '5'}">selected</c:if> value = "5"><%=activityVersion5%></option>
                                            <%} %>
                                            <% if (activityVersion6 != null && activityVersion6.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '6'}">selected</c:if> value = "6"><%=activityVersion6%></option>
                                            <%} %>
                                            <% if (activityVersion7 != null && activityVersion7.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '7'}">selected</c:if> value = "7"><%=activityVersion7%></option>
                                            <%} %>
                                            <% if (activityVersion8 != null && activityVersion8.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '8'}">selected</c:if> value = "8"><%=activityVersion8%></option>
                                            <%} %>
                                            <% if (activityVersion9 != null && activityVersion9.length() > 0) {%>
                                                <option <c:if test="${activityCog.activityVersion == '9'}">selected</c:if> value = "9"><%=activityVersion9%></option>
                                            <%} %>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
	                       	<%-- <tr>
                        		<td class="ab_left"><label class="title">关注引导页链接：<span class="required">*</span></label></td>
                        		<td class="ab_main" colspan="3">
                        			<div class="content">
                        				<input name="guideLink" tag="validate" value="${activityCog.guideLink}"
	                       					class="form-control required" autocomplete="off" maxlength="200" />
                        				<label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr> --%>
                		</table>
                	</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>一码多扫规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">一码多扫<span class="white en-larger2">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content isMoreSweep">
                                        <input type="radio" class="tab-radio isMoreSweepO" name="isMoreSweep" value="0" 
                                        <c:if test="${activityCog.isMoreSweep eq '0'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">否</span>
                                        <input type="radio" class="tab-radio isMoreSweepT" name="isMoreSweep" value="1" 
                                        <c:if test="${activityCog.isMoreSweep eq '1'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">是</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">日期范围：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="sDate" class="form-control input-width-medium Wdate preTime
                                        	<c:if test="${activityCog.isMoreSweep eq '1'}"> required</c:if> "
                                       		tag="validate" autocomplete="off" value="${activityCog.SDate}"
                                       		<c:if test="${activityCog.isMoreSweep eq '0'}">disabled="disabled"</c:if>
                                       		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span> 
                                       	<input name="eDate" class="form-control input-width-medium Wdate sufTime
											<c:if test="${activityCog.isMoreSweep eq '1'}"> required</c:if> "
                                       		tag="validate" autocomplete="off" value="${activityCog.EDate}" 
                                       		<c:if test="${activityCog.isMoreSweep eq '0'}">disabled="disabled"</c:if>
                                       		onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label id="sdateLabel" class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                                <td class="ab_left"><label class="title mart5">时间范围：</label></td>
                                <td class="ab_main">
	                                    <div class="content">
	                                        <span class="blocker">从</span>
	                                        <input name="sTime" class="form-control input-width-medium Wdate  
	                                        		<c:if test="${activityCog.isMoreSweep eq '1'}"> required</c:if>" 
	                                                <c:if test="${activityCog.isMoreSweep eq '0'}">disabled="disabled"</c:if> 
	                                                tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" value="${activityCog.STime}"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="eTime" class="form-control input-width-medium Wdate  
	                                        		<c:if test="${activityCog.isMoreSweep eq '1'}"> required</c:if>" 
	                                                <c:if test="${activityCog.isMoreSweep eq '0'}">disabled="disabled"</c:if>
	                                                tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" value="${activityCog.ETime}"/>
	                                        <label id="stimeLabel" class="validate_tips"></label>
	                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">中奖模式：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<select class="form-control input-width-larger" name="prizePattern" 
	                       					<c:if test="${activityCog.isMoreSweep eq '0'}">readonly="readonly"</c:if>>
	                       					<option value="1" <c:if test="${activityCog.prizePattern eq '1'}">selected="selected"</c:if>>随机奖项</option>
	                       					<option value="2" <c:if test="${activityCog.prizePattern eq '2'}">selected="selected"</c:if> style="display: none;">固定奖项</option>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">有效扫码次数：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="validCount" tag="validate" class="form-control number positive" autocomplete="off" maxlength="1" 
                                        style="width: 240px;" value= "${activityCog.validCount}" <c:if test="${activityCog.isMoreSweep eq '0'}">readonly="readonly"</c:if>/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>扫码获得金额规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">首扫类型<span class="white en-larger2">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio firstRadioMoney" name="firstRebateType" value="0"
                                        <c:if test="${activityCog.firstRebateType eq '0'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">金额</span>
                                        <input type="radio" class="tab-radio firstRadioScore" name="firstRebateType" value="1" 
                                        <c:if test="${activityCog.firstRebateType eq '1'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">首次扫码：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<span class="blocker">从</span>
	                       				<input id="firstRebateMin" name="firstRebateMin" tag="validate" value="${activityCog.firstRebateMin}"
	                       					class="form-control money preValue maxValue input-width-small" autocomplete="off" maxlength="5"  maxVal="99" />
	                       				<span class="blocker en-larger">至</span>
	                       				<input id="firstRebateMax" name="firstRebateMax" tag="validate" value="${activityCog.firstRebateMax}"
	                       					class="form-control money sufValue maxValue input-width-small" autocomplete="off" maxlength="5"  maxVal="99" />
                                        <span id="firstMoneylable" class="blocker en-larger">(元)</span>
                                        <span id="firstScorelable" class="blocker en-larger">(分)</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">首扫预约：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="firstRebateReserver" class="form-control" value="${activityCog.firstRebateReserver}" autocomplete="off" maxlength="150" style="width: 380px;"/>
                                        <span style="color: green; margin-left: 5px; line-height: 2;">格式：2017-09-01 00:00:00#2017-09-01 23:59:59,1.08-2.00</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">逢百规则：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                        	<textarea rows="3" cols="50" name="dotRuleInfo" style="width: 480px;">${activityCog.dotRuleInfo }</textarea>
                        				</div>
                                        <span style="color: green; margin-left: 5px; line-height: 3;">格式：开始日期#结束日期,扫码倍数:金额,扫码倍数:金额,扫码倍数:金额;</span></br>
                                        <span style="color: green; margin-left: 5px; line-height: 2;">格式：2017-09-01#2017-09-05,100:5.18,500:6.66,1000:18.88;</span>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>可疑账户规则(满足一个即为可疑)</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则一：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">同一分钟扫码次数大于或等于</span>
	                       				<input name="sameMinuteRestrict" tag="validate" mark="blacklist"
	                       					class="form-control number positive input-width-small rule" autocomplete="off"
	                       					maxlength="4" value="${activityCog.sameMinuteRestrict}" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">规则二：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">同一天扫码次数大于或等于</span>
	                       				<input name="sameDayRestrict" tag="validate" mark="blacklist"
	                       					class="form-control number positive input-width-small rule" autocomplete="off"
	                       					maxlength="4" value="${activityCog.sameDayRestrict}" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">规则三：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">历史累计扫码次数大于或等于</span>
	                       				<input name="historyTimesRestrict" tag="validate" mark="blacklist"
	                       					class="form-control number positive input-width-small rule" autocomplete="off"
	                       					maxlength="4" value="${activityCog.historyTimesRestrict}" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">可疑返利类型<span class="white en-larger2">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtRebateType" value="0" 
                                        <c:if test="${activityCog.doubtRebateType eq '0'}"> checked="checked" </c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">金额</span>
                                        <input type="radio" class="tab-radio doubtRadioScore" name="doubtRebateType" value="1" 
                                        <c:if test="${activityCog.doubtRebateType eq '1'}"> checked="checked" </c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr class="doubt-rule">
	                       		<td class="ab_left"><label class="title">可疑账户<span class="white en-larger2">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input type="radio" class="tab-radio" id="dubious-doubtRuleType" name="doubtRuleType" value="1" style="float:left;" />
	                       				<span class="blocker en-larger">系数形式</span>
	                       				<input name="doubtRuleCoe" tag="validate" mark="blacklist"
	                       					value="${activityCog.doubtRuleCoe}"
	                       					class="form-control number positive maxValue input-width-small ex-larger"
	                       					autocomplete="off" maxlength="9" maxVal="100" />
                                        <span class="blocker en-larger">%</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr class="doubt-rule">
	                       		<td class="ab_left"><label class="title">金额规则：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content doubt">
	                       				<input type="radio" class="tab-radio" id="sumrule-doubtRuleType" name="doubtRuleType" value="2" style="float:left;" />
	                       				<span class="blocker en-larger">区间形式</span>
	                       				<span class="blocker en-larger">从</span>
	                       				<input id="doubtRuleRangeMin" name="doubtRuleRangeMin" tag="validate" mark="blacklist"
	                       					value="${activityCog.doubtRuleRangeMin}"
	                       					class="form-control money sufValue maxValue input-width-small" autocomplete="off" maxlength="5" maxVal="99" />
	                       				<span class="blocker en-larger">至</span>
	                       				<input id="doubtRuleRangeMax" name="doubtRuleRangeMax" tag="validate" mark="blacklist"
	                       					value="${activityCog.doubtRuleRangeMax}"
	                       					class="form-control money sufValue maxValue input-width-small" autocomplete="off" maxlength="5" maxVal="99" />
                                        <span id="doubtMoneylable" class="blocker en-larger">(元)</span>
                                        <span id="doubtScorelable" class="blocker en-larger">(分)</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<%-- 删除黑名单配置功能，黑名单用户均返回0.00元
                			<tr class="danger-rule">
	                       		<td class="ab_left"><label class="title">黑名单账户<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input type="radio" class="tab-radio" id="dangerRuleCoefficient" name="dangerRuleType" value="1" style="float:left;" />
	                       				<span class="blocker en-larger">系数形式</span>
	                       				<input name="dangerRuleCoe" tag="validate" mark="blacklist"
	                       					value="${activityCog.dangerRuleCoe}"
	                       					class="form-control number positive maxValue input-width-small ex-larger"
	                       					autocomplete="off" maxlength="9" maxVal="100" />
	                       				<span class="blocker en-larger">%</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr class="danger-rule">
	                       		<td class="ab_left"><label class="title">金额规则：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content danger">
	                       				<input type="radio" class="tab-radio" id="dangerRuleMoney" name="dangerRuleType" value="2" style="float:left;" />
	                       				<span class="blocker en-larger">区间形式</span>
	                       				<span class="blocker en-larger">从</span>
	                       				<input name="dangerRuleRangeMin" tag="validate" mark="blacklist"
	                       					value="${activityCog.dangerRuleRangeMin}"
	                       					class="form-control number integer preValue input-width-small" autocomplete="off" maxlength="5" />
	                       				<span class="blocker en-larger">至</span>
	                       				<input name="dangerRuleRangeMax" tag="validate" mark="blacklist"
	                       					value="${activityCog.dangerRuleRangeMax}"
	                       					class="form-control number integer sufValue input-width-small" autocomplete="off" maxlength="5" />
	                       				<span class="blocker en-larger">(分)</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>  --%>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?menuType=activity">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body">
      				<h6></h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
    </div>
  </body>
</html>
