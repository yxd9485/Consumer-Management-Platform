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
    <title>编辑一码双奖活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
		});
		
		function initPage() {

            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
            
            // 保存
			$(".btnSave").click(function(){
				var flag = validForm();
				if (flag) {
					var url = $(this).attr("url");
					$.fn.confirm("确认发布？", function(){
                        $(".btnSave").attr("disabled", "disabled");
                        $("#divId").css("display","block");
						
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});
            
            // 抽奖机制
            $("form").on("change", "[name='lotteryType']", function(){
                $("tr.lotteryMoney, tr.lotteryScanNum").css("display", "none");
                if ($(this).val() == "0") {
                    $("tr.lotteryScanNum").css("display", "");
                } else {
                    $("tr.lotteryMoney").css("display", "");
                }
            });
            $(":radio[name='lotteryType'][value='${doublePrizeCog.lotteryType}']").attr("checked", "checked").trigger("change");
            
            // 抽奖触发金额
            $("form").on("click", "#addLotteryMoney", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copyItem = $(this).closest("div").clone(true, true);
                    $copyItem.find("#addLotteryMoney").text("删除");
                    $copyItem.find("[id$='Money']").val("");
                    $(this).closest("td").append($copyItem);
                    
                } else {
                    $(this).closest("div").remove();
                }
            });
            
			// 增加SKU
            $("form").on("click", "#addSku", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addSku").text("删除");
                    $(this).closest("td").append($copySku);
                    
                } else {
                    $(this).closest("div").remove();
                }
            });
            
            // 增加奖品限制
            $("form").on("click", "#addPrize", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copyPrize = $(this).closest("div").clone(true, true);
                    $copyPrize.find("#addPrize").text("删除");
                    $(this).closest("td").append($copyPrize);
                    
                } else {
                    $(this).closest("div").remove();
                }
            });
            
            // 增加区域
            $("form").on("click", "#addArea", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copyArea = $(this).closest("div").clone();
                    $copyArea.initZone("<%=cpath%>", true, "", true);
                    $copyArea.find("#addArea").text("删除");
                    $(this).closest("td").append($copyArea);
                    
                } else {
                    $(this).closest("div").remove();
                }
            });
            // 初始化区域事件
            $("#addArea").closest("div").initZone("<%=cpath%>", true, "", true);
            
            // 初始化筛选区域
            var filterAreaCodeAry = "${doublePrizeCog.filterAreaCode}".split(",");
            $.each(filterAreaCodeAry, function(idx, val, ary){
                if (val != '') {
                    if (idx > 0) $("#addArea").click();
                    $("td.area div:last-child").initZone("<%=cpath%>", true, val, true);
                }
            });
            
            // 筛选类型
            $("form").on("change", ":radio[name='filterType']", function(){
                if ("0" == $(this).val()) {
                	$("div.filteruser").find("[name='filterStartDate'], [name='filterEndDate']").removeAttr("disabled");
                	$("div.filteruser").find("[name='filterSkuKeyAry'], [name='filterSkuTotal'], #addSku").removeAttr("disabled");
                } else {
                	$("div.filteruser").find("[name='filterStartDate'], [name='filterEndDate']").attr("disabled", "disabled");
                	$("div.filteruser").find("[name='filterSkuKeyAry'], [name='filterSkuTotal'], #addSku").attr("disabled", "disabled");
                }
            });
            $(":radio[name='filterType'][value='${doublePrizeCog.filterType}']").trigger("change");
            
            /*
            // 周期类型
            $("form").on("change", ":radio[name='periodType']", function(){
                if ("0" == $(this).val()) {
                    $("#everyonePeriodLimit").text("每人每周中奖次数上限");
                } else if ("1" == $(this).val()) {
                    $("#everyonePeriodLimit").text("每人每月中奖次数上限");
                } else if ("2" == $(this).val()) {
                    $("#everyonePeriodLimit").text("每人每天中奖次数上限");
                } else if ("3" == $(this).val()) {
                    $("#everyonePeriodLimit").text("每人周期内中奖次数上限");
                } 
            });
            $(":radio[name='periodType'][value='${doublePrizeCog.periodType}']").trigger("change");
            */
            
            // 活动开始或结束时都要禁止修改筛选条件
            var currDate = new Date();
            var startDate = new Date("${doublePrizeCog.startDate}");
            if (currDate > startDate) {
                $("div.filteruser").find("input, select").attr("disabled", "disabled");
                $("div.filteruser").find("label[id$='Sku'], label[id$='Area']").attr("disabled", "disabled");
            }
            
         	// 检验名称是否重复
            $("input[name='activityName']").on("blur",function(){
            	var activityName = $("input[name='activityName']").val();
            	if(activityName == "") return;
            	checkName(activityName);
            });
            
            // 兑奖截止类型
            $("input[name='prizeExpireType']").on("change", function(){
                if($(this).val() == '0') {
                    $(this).closest("div").find("input[name='prizeExpireDate']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='prizeValidDay']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("input[name='prizeExpireDate']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='prizeValidDay']").removeAttr("disabled");
                }
            });
            $(":radio[name='prizeExpireType'][value='${doublePrizeCog.prizeExpireType}']").prop("checked", true).trigger("change");
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(bussionName){
			$.ajax({
				url : "${basePath}/doubleprize/checkBussionName.do",
			    data:{
			    		"infoKey":"${doublePrizeCog.activityKey}",
			    		"bussionName":bussionName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("活动名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}

		function validForm() {
			// 检验名称是否重复
			var activityName = $("input[name='activityName']").val();
        	if(activityName == "") return false;
        	checkName(activityName);
        	if(!flagStatus){
        		return false;
        	}
        	
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
            
            // 组建筛选区域
            var areaCode = "";
            var areaName = "";
            $("td.area div.area").each(function(i){
                var $province = $(this).find("select.zProvince");
                var $city = $(this).find("select.zCity");
                var $county = $(this).find("select.zDistrict");
                if ($county.val() != "") {
                    areaCode = areaCode + $county.val() + ",";
                } else if ($city.val() != "") {
                    areaCode = areaCode + $city.val() + ",";
                } else if ($province.val() != "") {
                    areaCode = areaCode + $province.val() + ",";
                } else {
                    areaCode = "";
                    areaName = "";
                    return false;
                }
                areaName = areaName + $province.find("option:selected").text() + "_" 
                + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
            });
            $("input[name='filterAreaCode']").val(areaCode);
            $("input[name='filterAreaName']").val(areaName);
			
            // 初始化奖项限制prizeLimit
            var prizeLimit = "";
            $("td.prize div.prize").each(function(i){
                prizeLimit += $(this).find("select").val() + ",";
            });
            $("input[name='prizeLimit']").val(prizeLimit);
            
            // 抽奖触发金额
            if ($("[name='lotteryType']:checked").val() == "0") {
                
                // 校验周期类型日期的合法性
                var periodType = $("[name='periodType']:checked").val();
                var startDate = $("[name='startDate']").val();
                var endDate = $("[name='endDate']").val();
                var ajaxErrFlag = false;
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/doubleprize/checkPeriodType.do",
                    async: false,
                    data: {"periodType":periodType, "startDate":startDate, "endDate":endDate},
                    dataType: "json",
                    beforeSend:appendVjfSessionId,
                    success:  function(result){
                        if (result["errMsg"]) {
                            ajaxErrFlag = true
                            $.fn.alert(result["errMsg"]);
                        }
                    }
                });
                if (ajaxErrFlag) return false;
                
                // 区间金额
                var minMoney = $("input[name='minMoney']").val();
                var maxMoney = $("input[name='maxMoney']").val();
                if((minMoney == "" && maxMoney != "") || (minMoney != "" && maxMoney == "")){
                    $(".moneyRange").find(".validate_tips").text("区间值不允许有单个");
                    $(".moneyRange").find(".validate_tips").addClass("valid_fail_text");
                    $(".moneyRange").find(".validate_tips").show();
                    return false;
                }
            } else {
                var lotteryMoney = "";
                $("tr.lotteryMoney div").each(function(i, obj){
                    lotteryMoney += $(obj).find("#lotteryMinMoney").val() + "-" + $(obj).find("#lotteryMaxMoney").val() + ",";
                });
                
                $("input[name='lotteryMoney']").val(lotteryMoney);
            }
            
			return true;
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
    <div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>筛选用户中,请勿其他操作.....</b></h2>
    </div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
        	<li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">一码双奖</a></li>
        	<li class="current"><a title="">修改活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/doubleprize/doDoublePrizeEdit.do">
            	<input type="hidden" name="activityKey" value="${doublePrizeCog.activityKey}" />
            	<input type="hidden" name="companyKey" value="${doublePrizeCog.companyKey}" />
            	<input type="hidden" name="filterPersonNum" value="${doublePrizeCog.filterPersonNum}" />
            	<input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                <input type="hidden" name="lotteryMoney" value="${doublePrizeCog.lotteryMoney}"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="activityName" tag="validate" value="${doublePrizeCog.activityName}"
	                       					class="form-control required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate required preTime" value="${doublePrizeCog.startDate}"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"  />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime" value="${doublePrizeCog.endDate}"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">促销SKU：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <c:forEach items="${doublePrizeCog.promotionSkuKeyAry}" var="itemPromSkuKey" varStatus="idx">
	                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
	                                        <select class="form-control input-width-larger required" name="promotionSkuKeyAry" tag="validate">
	                                            <option value="">请选择SKU</option>
	                                            <c:if test="${!empty skuList}">
		                                            <c:forEach items="${skuList}" var="sku">
		                                               <option value="${sku.skuKey}" <c:if test="${itemPromSkuKey == sku.skuKey}"> selected</c:if> data-img="${sku.skuLogo}" >${sku.skuName}</option>
		                                            </c:forEach>
	                                            </c:if>
	                                        </select>
	                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">${idx.count == 1 ? '新增' : '删除'}</label>
	                                        <label class="validate_tips"></label>
	                                    </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">抽奖机制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="lotteryType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">按扫码次数</span>
                                        <input type="radio" class="tab-radio" name="lotteryType" value="1" style="float:left;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">按扫码金额</span>
                                    </div>
                                </td>
                            </tr>
                            <tr class="lotteryMoney" style="display: none;">
                                <td class="ab_left"><label class="title">触发抽奖金额：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <c:forEach items="${fn:split(doublePrizeCog.lotteryMoney, ',')}" var="item" varStatus="idx">
	                                    <div class="content" style="display: flex; margin: 5px 0px;">
	                                        <span class="blocker en-larger">从</span>
	                                        <input id="lotteryMinMoney" tag="validate" value="${fn:split(item, '-')[0]}"
	                                            class="form-control input-width-small money preValue maxValue required" autocomplete="off" maxlength="5" maxVal="99"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input id="lotteryMaxMoney" tag="validate" value="${fn:split(item, '-')[1]}"
	                                            class="form-control input-width-small money sufValue maxValue required" autocomplete="off" maxlength="5" maxVal="99"/>
	                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addLotteryMoney">${idx.count == 1 ? '新增' : '删除'}</label>
	                                        <label class="validate_tips"></label>
	                                    </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr class="lotteryScanNum">
                                <td class="ab_left"><label class="title">周期类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="periodType" value="2" style="float:left;" <c:if test="${doublePrizeCog.periodType eq '2'}">checked</c:if> />
                                        <span class="blocker en-larger" style="margin-left: 2px;">自然天</span>
                                        <input type="radio" class="tab-radio" name="periodType" value="0" style="float:left;" <c:if test="${doublePrizeCog.periodType eq '0'}">checked</c:if> />
                                        <span class="blocker en-larger" style="margin-left: 2px;">自然周</span>
                                        <input type="radio" class="tab-radio" name="periodType" value="1" style="float:left;" <c:if test="${doublePrizeCog.periodType eq '1'}">checked</c:if> />
                                        <span class="blocker en-larger" style="margin-left: 2px;">自然月</span>
                                        <input type="radio" class="tab-radio" name="periodType" value="3" style="float:left;" <c:if test="${doublePrizeCog.periodType eq '3'}">checked</c:if> />
                                        <span class="blocker en-larger" style="margin-left: 2px;">活动周期</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">需关注公众号：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="subscribeStatus" class="form-control input-width-larger">
                                            <option value="0" <c:if test="${doublePrizeCog.subscribeStatus == '0' }"> selected</c:if>>否</option>
                                            <option value="1" <c:if test="${doublePrizeCog.subscribeStatus == '1' }"> selected</c:if>>是</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="prizeLimit" value="${doublePrizeCog.prizeLimit}"/>
                                <td class="ab_left"><label class="title">活动奖品：<span class="required">*</span></label></td>
                                <td class="ab_main prize" colspan="3">
                                    <c:forEach items="${prizeLimitAry}" var="prizeKey" varStatus="idx">
                                        <div class="content prize" style="display: flex; margin: 5px 0px;">
                                            <select class="form-control input-width-larger required" tag="validate">
                                                <option value="">请选择</option>
                                                <c:if test="${!empty prizeMap}">
                                                    <c:forEach items="${prizeMap}" var="prize">
                                                       <option value="${prize.key}" <c:if test="${prizeKey == prize.key}"> selected</c:if>>${prize.value}</option>
                                                    </c:forEach>
                                                </c:if>
                                            </select>
                                            <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addPrize">${idx.count == 1 ? '新增' : '删除'}</label>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr class="lotteryScanNum">
                                <td class="ab_left"><label class="title">中出时扫码金额区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content moneyRange">
                                        <span class="blocker en-larger">从</span>
                                        <input name="minMoney" tag="validate" value="${doublePrizeCog.minMoney}"
                                            class="form-control money preValue maxValue input-width-small" autocomplete="off" maxlength="5" maxVal="99"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="maxMoney" tag="validate" value="${doublePrizeCog.maxMoney}"
                                            class="form-control money sufValue maxValue input-width-small" autocomplete="off" maxlength="5" maxVal="99"/>
                                        <span class="blocker en-larger">(元)</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label id="everyonePeriodLimit" class="title">每人实物中出次数上限：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="everyoneLimit" value="${doublePrizeCog.everyoneLimit}"
                                            class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">每天实物中出次数上限：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="everyDayLimit" value="${doublePrizeCog.everyDayLimit}"
                                            class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动概述标题：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activitySummaryTitle" value="${doublePrizeCog.activitySummaryTitle}"
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="30" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动概述：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <textarea name="activitySummary" rows="5" tag="validate"
                                            class="form-control required" autocomplete="off" maxlength="500" >${doublePrizeCog.activitySummary}</textarea>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动说明：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<textarea name="activityDesc" rows="5" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="1000" >${doublePrizeCog.activityDesc}</textarea>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>用户筛选配置</h4>
                    </div>
                	<div class="widget-content panel no-padding filteruser">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">筛选类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="filterType" value="0" style="float:left;" <c:if test="${doublePrizeCog.filterType eq '0'}">checked</c:if>/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">过往用户</span>
                                        <input type="radio" class="tab-radio" name="filterType" value="1" style="float:left;" <c:if test="${doublePrizeCog.filterType eq '1'}">checked</c:if>/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">所有用户</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">筛选时间：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">从</span>
                                        <input name="filterStartDate" class="form-control input-width-medium Wdate required preTime" value="${doublePrizeCog.filterStartDate}"
                                           tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'%y-%M-%d'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="filterEndDate" class="form-control input-width-medium Wdate required sufTime" value="${doublePrizeCog.filterEndDate}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'%y-%M-%d'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="filterAreaCode" />
                                <input type="hidden" name="filterAreaName" />
                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>
                                <td class="ab_main area" colspan="3">
                                    <div class="area" style="display: flex; margin: 5px 0px;">
                                        <select name="provinceAry" class="zProvince form-control input-width-normal"></select>
                                        <select name="cityAry" class="zCity form-control input-width-normal"></select>
                                        <select name="countyAry" class="zDistrict form-control input-width-normal"></select>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">筛选SKU：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <c:forEach items="${doublePrizeCog.filterSkuKeyAry}" var="itemFilterSkuKey" varStatus="idx">
	                                    <div class="content sku filterSku" style="display: flex; margin: 5px 0px;">
	                                        <select class="form-control input-width-larger required" name="filterSkuKeyAry" tag="validate">
	                                            <option value="">请选择SKU</option>
	                                            <c:if test="${!empty skuList}">
	                                            <c:forEach items="${skuList}" var="sku">
	                                            <option value="${sku.skuKey}" <c:if test="${itemFilterSkuKey == sku.skuKey}"> selected</c:if> data-img="${sku.skuLogo}">${sku.skuName}</option>
	                                            </c:forEach>
	                                            </c:if>
	                                        </select>
	                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">${idx.count == 1 ? '新增' : '删除'}</label>
	                                        <label class="validate_tips"></label>
	                                    </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">筛选SKU扫码合计次数大于等于：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="filterSkuTotal" 
                                            class="form-control input-width-larger positive required" value="${doublePrizeCog.filterSkuTotal}" autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">可疑用户是否可参与：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="filterDoubtStatus" class="form-control input-width-larger">
                                            <option value="0" <c:if test="${doublePrizeCog.filterDoubtStatus == '0' }"> selected</c:if>>否</option>
                                            <option value="1" <c:if test="${doublePrizeCog.filterDoubtStatus == '1' }"> selected</c:if>>是</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/doubleprize/showDoublePrizeList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
