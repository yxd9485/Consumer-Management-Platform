<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化功能
			initPage();
		});
		
        function initPage() {
        	
            // SKU类型
            var skuType = "${activityCog.skuType}";
            var skuTypeName = "";
            if (skuType == '0') {
                skuTypeName = "瓶码";
            } else if (skuType == '1') {
                skuTypeName = "罐码";
            } else if (skuType == '2') {
                skuTypeName = "箱码";
            } 
            $("input[name='skuType']").val(skuTypeName);
        	
        	// Tab切换
        	$("div.tab-group a").on("click", function(i){
        		// 导航
                var tabIndex = $("div.tab-group a").index($(this));
        		$("ul.breadcrumb #currTab").text($(this).text());
        		$("ul.breadcrumb #currTab").data("tabindex", tabIndex);
        		$("div.activityinfo").css("display", tabIndex == 2 ? "none" : "");
        		
        		// 按钮状态
        		$("div.tab-group a").removeClass("btn-red");
        		$(this).addClass("btn-red");
        		
        		// 显示内容
        		$("div.tab-content").css("display", "none");
                $("div.tab-content").eq(tabIndex).css("display", "");
        	});
        	$("div.tab-group a").eq(Number("${childTab}") - 1).trigger("click");
        	
        	// 保存活动信息及风控规则
        	$(".btnNext").on("click", function(){
        		// 当前Tab
        		var tabIndex = $("ul.breadcrumb #currTab").data("tabindex");
                // 切换Tab
                $("div.tab-group a").eq(tabIndex + 1).trigger("click");
        		
        	});
        }

		// 定时更新iframe的高度
        var iframeClock = setInterval("setIframeHeight()", 50);
        function setIframeHeight() {
        	iframe = document.getElementById('ruleFrame');
            if (iframe) {
                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
	        	if (iframeWin.document.body) {
	        	    // iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
	        	    iframe.height = iframeWin.document.body.scrollHeight;
	        	}
        	}
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
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 活动配置</a></li>
            <li class="current"><a> 修改活动</a></li>
            <li class="current"><a id="currTab" data-tabindex="0"> 活动信息</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tab-group">
            <a class="btn tab btn-red">活动信息</a>
            <a class="btn tab">风控规则</a>
            <a class="btn tab">配置活动规则</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="mainForm"
                        action="<%=cpath%>/vcodeActivity/showVcodeActivityList.do">
                <input type="hidden" name="vcodeActivityKey" value="${activityCog.vcodeActivityKey}" />
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
	            <div class="widget box activityinfo">
<!-- 	                活动信息 -->
                    <div class="tab-content">
	            		<div class="widget-header">
	            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
	            		</div>
	                	<div class="widget-content panel no-padding">
	                		<table class="active_board_table">
	                			<tr>
		                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="vcodeActivityName" tag="validate" value="${activityCog.vcodeActivityName}" disabled="disabled"
		                       				   style="width: 400px !important;" class="form-control required" autocomplete="off" maxlength="30"/>
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
	                			<tr>
		                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker">从</span>
	                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate required preTime" value="${activityCog.startDate}" disabled="disabled"
	                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
	                                       	<span class="blocker en-larger">至</span>
	                                       	<input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime" value="${activityCog.endDate}" disabled="disabled"
	                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
	                                       	<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content skuInfo">
		                       				<select name="skuKey" tag="validate" disabled="disabled" class="form-control input-width-larger required activitysku" disabled="disabled">
		                       					<option value="">请选择</option>
		                       					<c:if test="${!empty skuList}">
		                       					<c:forEach items="${skuList}" var="sku">
		                       					<option value="${sku.skuKey}" data-skutype="${sku.skuType}" <c:if test="${activityCog.skuKey eq sku.skuKey}">selected</c:if>>${sku.skuName}</option>
		                       					</c:forEach>
		                       					</c:if>
		                       				</select>
	                                       	<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">码源类型：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content skuInfo">
		                       				<input name="skuType" class="form-control input-width-larger" readonly="readonly" disabled="disabled"/>
		                       			</div>
		                       		</td>
		                       	</tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">活动版本：<span class="required">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <select name="activityVersion" tag="validate" class="form-control input-width-larger required" disabled="disabled">
                                                <option value="">请选择</option>
                                                <c:if test="${!empty versionLst}">
                                                <c:forEach items="${versionLst}" var="item" >
                                                <option value="${item.dataValue}" <c:if test="${activityCog.activityVersion eq item.dataValue}">selected</c:if>>${item.dataAlias}</option>
                                                </c:forEach>
                                                </c:if>
	                                        </select>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                		</table>
	                	</div>
<!-- 	                	高级规则-逢百规则 -->
	                	<c:if test="${perHundredCog != null}">
	                	<div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>高级规则 —— 逢百规则</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
	                            <tr>
	                                <td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="morescanName" tag="validate"  disabled="disabled"
	                                            class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${perHundredCog.perhundredName}" disabled="disabled"/>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">规则时间：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
		                                <c:forEach items="${fn:split(perHundredCog.validDateRange, ',')}" var="item">
                                        <div class="content" style="margin: 5px 0px; display: flex;">
                                            <span class="blocker">从</span>
                                            <input class="form-control input-width-medium Wdate required preTime" value="${fn:split(item, '#')[0]}"
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" disabled="disabled" />
                                            <span class="blocker en-larger">至</span>
                                            <input class="form-control input-width-medium Wdate required sufTime" value="${fn:split(item, '#')[1]}"
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" disabled="disabled" />
                                            <label class="validate_tips"></label>
                                        </div>
		                                </c:forEach>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title mart5">倍数：<span class="required">*</span></label></td>
	                                <td class="ab_main perItem">
		                                <c:set var="perItems" value="${fn:split(perHundredCog.perItems, ';')}"></c:set>
		                                <c:forEach items="${perItems}" var="item">
	                                    <div class="content perItem" style="margin: 5px 0px; display: flex;">
	                                         <input name="perItem" class="form-control number input-width-small" autocomplete="off" maxlength="9" value="${fn:split(item, ':')[0]}" disabled="disabled"/>
	                                         <span class="blocker en-larger marl30">金额</span>
	                                         <input name="perItem" class="form-control number input-width-small" autocomplete="off" maxlength="9" value="${fn:split(item, ':')[1]}" disabled="disabled"/>
	                                         <span class="blocker en-larger marl30">积分</span>
	                                         <input name="perItem" class="form-control number input-width-small" autocomplete="off" maxlength="9" value="${fn:split(item, ':')[2]}" disabled="disabled"/>
	                                    </div>
		                                </c:forEach>
	                                </td>
                                </tr>
                                <tr>
	                                <td class="ab_left"><label class="title">限制时间类型：<span class="white">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
	                                    <div style="float: left; width: 80px; line-height: 25px;">
	                                        <input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" 
	                                            style="float: left; height:20px; cursor: pointer;" disabled="disabled"
	                                            <c:if test="${perHundredCog.restrictTimeType eq '0' }"> checked="checked" </c:if>>&nbsp;规则时间
	                                    </div>
	                                    <div style="float: left; width: 50px; line-height: 25px;">
	                                        <input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" 
	                                            style="float: left; height:20px; cursor: pointer;" disabled="disabled"
	                                            <c:if test="${perHundredCog.restrictTimeType eq '1' }"> checked="checked" </c:if>>&nbsp;每天
	                                    </div>
	                                    <span class="blocker en-larger"></span>
	                                    <label class="validate_tips"></label>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="restrictVpoints" tag="validate" value="${perHundredCog.restrictVpoints eq 0 ? '' : perHundredCog.restrictVpoints}" 
	                                            class="form-control number input-width-small rule" autocomplete="off" maxlength="9" disabled="disabled"/>
	                                        <span class="blocker en-larger">积分</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">限制消费金额：<span class="white">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="restrictMoney" tag="validate" value="${perHundredCog.restrictMoney eq 0.00 ? '' : perHundredCog.restrictMoney}"
	                                            class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" disabled="disabled"/>
	                                        <span class="blocker en-larger">元</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">限制消费瓶数：<span class="white">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="restrictBottle" tag="validate" value="${perHundredCog.restrictBottle eq 0 ? '' : perHundredCog.restrictBottle}"
	                                            class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" disabled="disabled"/>
	                                        <span class="blocker en-larger">瓶</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
                            </table>
                        </div>
                        </c:if>
<!-- 	                	高级规则-一码多扫 -->
	                	<c:if test="${moreScanCog != null}">
	                	<div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>高级规则 —— 一码多扫</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="morescanName" tag="validate"  disabled="disabled"
                                                class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${moreScanCog.morescanName}"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">日期范围：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <c:forEach items="${fn:split(moreScanCog.validDateRange, ',')}" var="item">
                                        <div class="content" style="margin: 5px 0px; display: flex;">
                                            <span class="blocker">从</span>
                                            <input class="form-control input-width-medium Wdate required preTime" value="${fn:split(item, '#')[0]}"
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" disabled="disabled" />
                                            <span class="blocker en-larger">至</span>
                                            <input class="form-control input-width-medium Wdate required sufTime" value="${fn:split(item, '#')[1]}"
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" disabled="disabled" />
                                            <label class="validate_tips"></label>
                                        </div>
                                        </c:forEach>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">时间范围：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <c:forEach items="${fn:split(moreScanCog.validTimeRange, ',')}" var="item">
                                        <div class="content" style="margin: 5px 0px; display: flex;">
                                            <span class="blocker">从</span>
                                            <input class="form-control input-width-medium Wdate required preTime" value="${fn:split(item, '#')[0]}"
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" disabled="disabled" />
                                            <span class="blocker en-larger">至</span>
                                            <input class="form-control input-width-medium Wdate required sufTime" value="${fn:split(item, '#')[1]}"
                                                autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" disabled="disabled" />
                                            <label class="validate_tips"></label>
                                        </div>
                                        </c:forEach>
                                    </td>
                                </tr>
                                <tr>
	                                <td class="ab_left"><label class="title">中奖模式：<span class="white">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content prizePattern">
	                                        <select class="form-control input-width-larger" name="prizePattern" readonly="readonly" disabled="disabled">
	                                            <option value="1" <c:if test="${moreScanCog.prizePattern eq '1'}">selected</c:if>>随机奖项</option>
	                                            <option value="2" <c:if test="${moreScanCog.prizePattern eq '2'}">selected</c:if> style="display: none;">固定奖项</option>
	                                        </select>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">有效扫码次数：<span class="white">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="validCount" tag="validate" class="form-control number positive" disabled="disabled"
	                                                   autocomplete="off" maxlength="1" style="width: 240px;" value= "${moreScanCog.validCount}"/>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">单码首扫后有效小时：<span class="required">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="qrcodeValidHour" tag="validate" class="form-control number" disabled="disabled"
	                                           autocomplete="off" maxlength="3" style="width: 240px;" value= "${moreScanCog.qrcodeValidHour}"/>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
                            </table>
                        </div>
                        </c:if>
	                	<div class="active_table_submit mart20">
	                        <div class="button_place">
	                            <a class="btn btn-blue btnNext">下一步</a>
	                        </div>
                        </div>
                	</div>
<!--          风控规则 -->
                	<div class="tab-content" style="display: none;">
	                	<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>风控规则</h4></div>
	                	<div class="widget-content panel no-padding">
	                		<table class="active_board_table">
	                			<tr>
	                                <td class="ab_left"><label class="title">按分钟识别：<span class="required"> </span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="sameMinuteRestrict" tag="validate" title="同一分钟扫码次数大于或等于" value="${activityCog.sameMinuteRestrict}"
	                                            class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5"  disabled="disabled"/>
	                                            <span class="blocker en-larger">次</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">按每天识别：<span class="required"> </span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="sameDayRestrict" tag="validate" title="同一天扫码次数大于或等于" value="${activityCog.sameDayRestrict}"
	                                            class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5"  disabled="disabled"/>
	                                            <span class="blocker en-larger">次</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">按累计识别：<span class="required"> </span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="historyTimesRestrict" tag="validate" title="历史累计扫码次数大于或等于" value="${activityCog.historyTimesRestrict}"
	                                            class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5"  disabled="disabled"/>
	                                            <span class="blocker en-larger">次</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">可疑返利类型<span class="required">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtRebateType" value="0" <c:if test="${activityCog.doubtRebateType eq '0'}">checked</c:if> style="float:left; cursor: pointer;"  disabled="disabled"/>
	                                        <span class="blocker en-larger">金额</span>
	                                        <input type="radio" class="tab-radio doubtRadioScore" name="doubtRebateType" value="1" <c:if test="${activityCog.doubtRebateType eq '1'}">checked</c:if>  style="float:left; cursor: pointer;"  disabled="disabled"/>
	                                        <span class="blocker en-larger">积分</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr class="doubt-rule">
	                                <td class="ab_left"><label class="title">返利区间：<span class="required">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content doubt">
	                                        <span class="blocker en-larger">从</span>
	                                        <input id="doubtRuleRangeMin" name="doubtRuleRangeMin" tag="validate" mark="blacklist" value="${activityCog.doubtRuleRangeMin}"
	                                            class="form-control required money preValue maxValue input-width-small" autocomplete="off" maxlength="5" maxVal="99" disabled="disabled"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input id="doubtRuleRangeMax" name="doubtRuleRangeMax" tag="validate" mark="blacklist" value="${activityCog.doubtRuleRangeMax}"
	                                            class="form-control required money sufValue maxValue input-width-small" autocomplete="off" maxlength="5" maxVal="99" disabled="disabled"/>
	                                        <span id="doubtMoneylable" class="blocker en-larger" <c:if test="${activityCog.doubtRebateType eq '1'}">style="display: none;"</c:if>>(元)</span>
	                                        <span id="doubtScorelable" class="blocker en-larger" <c:if test="${activityCog.doubtRebateType eq '0'}">style="display: none;"</c:if>>(积分)</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr style="display: none;">
	                                <td class="ab_left"><label class="title">模板概述：<span class="required"> </span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <textarea id="templetDesc" rows="5" readonly="readonly" disabled="disabled"
	                                            class="form-control required" autocomplete="off" maxlength="500" ></textarea>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                		</table>
                	    </div>
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                                <a class="btn btn-blue btnNext">下一步</a>
                            </div>
                        </div>
                	</div>
            	</div>
<!--          配置活动规则 -->
           	   <div class="tab-content" style="margin-top:-20px; display: none;">
           	       <iframe id="ruleFrame" class="mart20" src="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vjfSessionId=${vjfSessionId}&activityType=${activityType}&vcodeActivityKey=${activityCog.vcodeActivityKey}" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
           	   </div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
