<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%String cpath = request.getContextPath(); 
String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
		$(function(){
			
			$(function(){
				// 初始化校验控件
				$.runtimeValidate($("#import_form"));
				// 初始化功能
				initPage();
			});
			
            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).attr("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});

			// 类型为每天时，日期区域不可用
			$("[name='ruleType']").on("change", function(evt) {
				
				if($(this).val() == '3' || $(this).val() == '4' || $(this).val() == '5'){
					$("[name='hotAreaKey']").attr("disabled",true);
					$("[name='eruptRuleInfo']").attr("disabled",true);
					$("[name='eruptRuleInfo']").css("background-color","#EDEDED");
					
					$("[name='appointRebateRuleKey']").attr("disabled",true);
                    $("[name='restrictVpoints']").attr("readonly",true);
	                $("[name='restrictMoney']").attr("readonly",true);
	                $("[name='restrictBottle']").attr("readonly",true);
	                $("[name='restrictCount']").attr("readonly",true);
	                $("#restrictTimeType1").attr("disabled",true);
	                $("#restrictTimeType2").attr("disabled",true);
	                
				}else{
					$("[name='appointRebateRuleKey']").attr("disabled",false);
                    $("[name='restrictVpoints']").attr("readonly",false);
	                $("[name='restrictMoney']").attr("readonly",false);
	                $("[name='restrictBottle']").attr("readonly",false);
	                $("[name='restrictCount']").attr("readonly",false);
	                $("#restrictTimeType1").attr("disabled",false);
	                $("#restrictTimeType2").attr("disabled",false);
	                $("[name='eruptRuleInfo']").attr("disabled",false);
					$("[name='eruptRuleInfo']").css("background-color","white");
					if ("${areaName}" != "省外") {
						$("[name='hotAreaKey']").attr("disabled",false);
					}
				}
				
				// 节假日、每天
				if ($(this).val() == '1' || $(this).val() == '2') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input").removeAttr("disabled");
                    $("div.week input").attr("disabled", "disabled");
                    $("#beginTime").removeAttr("disabled");
                    $("#endTime").removeAttr("disabled");
				}
                
                // 周几
                if ($(this).val() == '3' ) {
                    $("div.date").css("display", "none");
                    $("div.week").css("display", "block");
                    $("div.date input").attr("disabled", "disabled");
                    $("div.week input").removeAttr("disabled");
                    $("#beginTime").removeAttr("disabled");
                    $("#endTime").removeAttr("disabled");
                }
                
                // 每天
                if ($(this).val() == '4') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input").attr("disabled", "disabled");
                    $("div.week input").attr("disabled", "disabled");
                    $("#beginTime").removeAttr("disabled");
                    $("#endTime").removeAttr("disabled");
                }
                
                // 自然周签到
                if ($(this).val() == '5') {
                    $("#div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input").attr("disabled", "disabled");
                    $("div.week input").attr("disabled", "disabled");
                    $("#beginTime").attr("disabled", "disabled");
                    $("#endTime").attr("disabled", "disabled");
                } 
				$("#beginDate:enabled").val("");
				$("#endDate:enabled").val("");
			});
			$("[name='ruleType']").trigger("change");
			
			$("div.week #beginDate").on("blur", function(){
				var beginVal = $(this).val();
				var endVal = $("div.week #endDate").val();
				if (beginVal != "") {
					if (!$.isNumeric(beginVal) || beginVal > 7 || beginVal < 1) {
						$.fn.alert("请输入1~7的数字");
	                    $(this).val("");
	                    
					} else if(endVal != "" && beginVal > endVal) {
						$.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
					}
				}
			});
			
			/* $("div.week #endDate").on("blur", function(){
				var beginVal = $("div.week #beginDate").val();
				var endVal = $(this).val();
				if (endVal != "") {
					if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1) {
						$.fn.alert("请输入1~7的数字");
	                    $(this).val("");
	                    
					} else if(beginVal != "" && beginVal > endVal) {
						$.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
					}
				}
			}); */
			
			// 选择规则
			$("[name='appointRebateRuleKey']").on("change", function(){
				var restrict = $(this).find("option:selected").attr("data-value");
				if(typeof(restrict) != 'undefined' || restrict ==''){
                    $("input[name='restrictVpoints']").val(restrict.split("-")[0]);
                    $("input[name='restrictVpoints']").attr("readonly",true);
                    $("input[name='restrictMoney']").val(restrict.split("-")[1]);
                    $("input[name='restrictMoney']").attr("readonly",true);
                    $("input[name='restrictBottle']").val(restrict.split("-")[2]);
                    $("input[name='restrictBottle']").attr("readonly",true);    
                    $("input[name='restrictCount']").val(restrict.split("-")[3]);
                    $("input[name='restrictCount']").attr("readonly",true);
					
					// 限制类型
					if("0" == restrict.split("-")[4]){
						$("#restrictTimeType1").trigger("click")
					}else if("1" == restrict.split("-")[4]){
						$("#restrictTimeType2").trigger("click")
					} 
					$("#restrictTimeType1").attr("disabled", true);
					$("#restrictTimeType2").attr("disabled", true);
					
					$("#restrictTimeType").removeAttr("disabled");
					$("#restrictTimeType").val(restrict.split("-")[4]);
						
				}else{
                    $("input[name='restrictVpoints']").val('');
                    $("input[name='restrictMoney']").val('');
                    $("input[name='restrictBottle']").val('');
                    $("input[name='restrictCount']").val('');
                    $("input[name='restrictVpoints']").attr("readonly",false);
                    $("input[name='restrictMoney']").attr("readonly",false);
                    $("input[name='restrictBottle']").attr("readonly",false);
                    $("input[name='restrictCount']").attr("readonly",false);
					
					// 限制类型
					$("#restrictTimeType1").removeAttr("disabled").trigger("click");
					$("#restrictTimeType2").removeAttr("disabled");
					$("#restrictTimeType").val("");
					$("#restrictTimeType").attr("disabled", true);
					
					
				}
				
			});
			$("div.week").css("display", "none");
			
			// 省市县变化时初始化热区
            $("form").on("change", "div.area select", function(){
                if ($(this).val() != "") {
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
                        } 
                        areaName = areaName + $province.find("option:selected").text() + "_" 
                        + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
                    });
                    if(areaCode != ""){
                    	$("input[name='filterAreaCode']").val(areaCode.substring(0, areaCode.length - 1));
                        $("input[name='filterAreaName']").val(areaName.substring(0, areaName.length - 1));	
                    }
                    
                    // 加载区域对应的热区
                    findHotAreaListByAreaCode(areaCode);
                    
                }
            });
            
            // 初始化省市县
            var allArea = '${allAreaFlage}'=='1' ? true : false;
            $("div.area").initZone("<%=cpath%>", false, "", true, allArea, true, false);
            
            
            
            // 增加区域
            $("form").on("click", "#addArea", function() {
            	$("td.area div.area:first-child").find("#delArea").css("display", "");
            	
            	var areaCopy = $("div.area").eq(0).clone();
            	$("td.area").append(areaCopy);
                $("td.area div.area:last-child").initZone("<%=cpath%>", false, "", true, allArea, true, false);
                
                if(!$("[name='hotAreaKey']").attr("disabled")){
                	$("[name='hotAreaKey']").attr("disabled", true);
                }
                if(!$("[name='appointRebateRuleKey']").attr("disabled")){
                	$("[name='appointRebateRuleKey']").attr("disabled", true);
                }
            });
            // 删除区域
            $("form").on("click", "#delArea", function() {
            	$(this).parent("div.area").remove();
            	if ($("td.area div.area").size() == 1) {
                    $("td.area div.area:first-child").find("#delArea").css("display", "none");
                    
                    $("[name='hotAreaKey']").attr("disabled", false);
                    $("[name='appointRebateRuleKey']").attr("disabled", false);
                }
            	// 触发初始化热区
            	$("div.area select").eq(0).change();
            });
            
            
         	// 增加日期
            $("form").on("click", "#addDate", function() {
            	var dateCopy = $("div.date").eq(0).clone();
            	$("td.date").append(dateCopy);
                $("td.date div.date").find("#delDate").css("display", "");
            });
            // 删除日期
            $("form").on("click", "#delDate", function() {
            	$(this).parent("div.date").remove();
            	if ($("td.date div.date").size() == 1) {
                    $("td.date div.date").find("#delDate").css("display", "none");
                }
            });
            
         	// 增加周几
            $("form").on("click", "#addWeek", function() {
            	var weekCopy = $("div.week").eq(0).clone();
            	$("td.date").append(weekCopy);
                $("td.date div.week").find("#delWeek").css("display", "");
            });
            // 删除周几
            $("form").on("click", "#delWeek", function() {
            	$(this).parent("div.week").remove();
            	if ($("td.date div.week").size() == 1) {
                    $("td.date div.week").find("#delWeek").css("display", "none");
                }
            });
            
         	// 增加时间
            $("form").on("click", "#addTime", function() {
            	var timeCopy = $("div.time").eq(0).clone();
            	$("td.time").append(timeCopy);
                $("td.time div.time").find("#delTime").css("display", "");
            });
            // 删除时间
            $("form").on("click", "#delTime", function() {
            	$(this).parent("div.time").remove();
            	if ($("td.time div.time").size() == 1) {
                    $("td.time div.time").find("#delTime").css("display", "none");
                }
            });
            
         	// 根据地区获取热区
            function findHotAreaListByAreaCode(areaCode){
                if(areaCode == "" || areaCode == null) return;
                var oldVal = $("#hotAreaKey").val();
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/vcodeActivityHotArea/findHotAreaListByAreaCode.do",
                    async: false,
                    data: {"areaCode":areaCode},
                    dataType: "json",
                    beforeSend:appendVjfSessionId,
                    success:  function(result){
                        var content = "<option value=''>请选择</option>";
                        if(result){
                            $.each(result, function(i, item) {
                                if (i >= 0) {
                                    if (oldVal == item.hotAreaKey) {
                                        content += "<option value='"+item.hotAreaKey+"' selected>"+item.hotAreaName+"</option>";
                                    } else {
                                        content += "<option value='"+item.hotAreaKey+"'>"+item.hotAreaName+"</option>";
                                    }
                                }
                            });
                            $("#hotAreaKey").html(content);
                        } else {
                            $("#hotAreaKey").html(content);
                        }
                    },
                    error: function(data){
                        $.fn.alert("热区回显错误!");
                    }
                }); 
            }
			
			// 初始化校验控件
			$.runtimeValidate($("#import_form"));
			// 初始化功能
			initPage();
		});
		
		function changeWeek(obj){
			var beginVal = $(obj).parent(".week").find("input[name='beginDate']").val();
			var endVal = $(obj).val();
			if (endVal != "") {
				if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1) {
					$.fn.alert("请输入1~7的数字");
                    $(obj).val("");
                    $(obj).parent(".week").find("input[name='beginDate']").val('');
				} else if(beginVal != "" && beginVal > endVal) {
					$.fn.alert("起始值不可大于终止值!");
                       $(obj).val("");
                       $(obj).parent(".week").find("input[name='beginDate']").val('');
				}
			}
		}
		
		function validFile() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			var returnFlag = false;
			var dateAry = "";
			var divType = "";
			var ruleType = $("select[name='ruleType']").val();
			if(ruleType == "1" || ruleType == "2"){
				divType = "date";
			}else if(ruleType == "3"){
				divType = "week";
			}
			
			// 组装多组日期
			if(divType != ""){
				$("td.date div." + divType).each(function(i){
	            	var $beginDate = $(this).find("input[name='beginDate']");
	            	var $endDate = $(this).find("input[name='endDate']");
	            	if($beginDate.val() == "" || $endDate.val() == ""){
	            		if(divType == "date"){
	            			$.fn.alert("日期范围不能为空!");	
	            		}else{
	            			$.fn.alert("周几不能为空!");
	            		}
	            		returnFlag = true;
	            		return false; // 相当于break
	            	}
	            	dateAry += $beginDate.val() + "@" + $endDate.val() + ",";
	            });

				if(returnFlag){
					return false;	
				}
				
				if(dateAry != ""){
					$("input[name='filterDateAry']").val(dateAry.substring(0, dateAry.length - 1));
				}	
			}
			
			// 组装多组时间
			var timeAry = "";
			$("td.time div.time").each(function(i){
            	var $beginTime = $(this).find("input[name='beginTime']");
            	var $endTime = $(this).find("input[name='endTime']");
            	if($beginTime.val() == "" || $endTime.val() == ""){
            		$.fn.alert("时间范围不能为空!");
            		returnFlag = true;
            		return false; // 相当于break
            	}
            	timeAry += $beginTime.val() + "@" + $endTime.val() + ",";
            });

			if(returnFlag){
				return false;	
			}
			
			if(timeAry != ""){
				$("input[name='filterTimeAry']").val(timeAry.substring(0, timeAry.length - 1));
			}
			
			var files = $("input.import-file").val();
			if(files == "") {
				$.fn.alert("未选择任何文件，不能导入!");
				return false;
			} else if(files.indexOf("xls") == -1) {
				$.fn.alert("不是有效的EXCEL文件");
				return false;
			}
			
			// 爆点规则
			var eruptRuleInfo = $("[name='eruptRuleInfo']").val();
			if(eruptRuleInfo != ""){
				if(eruptRuleInfo.indexOf(",") == -1
						|| eruptRuleInfo.split(",")[0].indexOf("#") == -1
						|| eruptRuleInfo.split(",")[1].indexOf(":") == -1
						|| eruptRuleInfo.indexOf("，") > -1
						|| eruptRuleInfo.indexOf("：") > -1){
					$.fn.alert("爆点规则格式不正确");
					return false;
				}
			}
			
			// 防重复提交
			$(".btnSave").attr("disabled", "disabled");
			

            if($("input[name='restrictVpoints']").val() == ""){
                $("input[name='restrictVpoints']").val("0");
            }
			if($("input[name='restrictMoney']").val() == ""){
				$("input[name='restrictMoney']").val("0.00");
			}
			if($("input[name='restrictBottle']").val() == ""){
				$("input[name='restrictBottle']").val("0");
			}
			if($("input[name='restrictCount']").val() == ""){
				$("input[name='restrictCount']").val("0");
			}
			return true;
		}
		
	</script>
	
	<style>
	   .blocker {
            float: left;
            vertical-align: middle;
            margin-right: 8px;
            margin-top: 8px;
        }
        .en-larger {
            margin-left: 8px;
        }
		.cool-wood {
			padding-left: 1em;
		}
		fieldset {
			border: 1px solid #d1d1d1;
			max-height: 160px;
			overflow: auto;
		}
		
		fieldset legend {
			font-weight: bold;
		}
		
		fieldset .remove-target {
			float: right;
			color: red;
			cursor: pointer;
		}
		
		fieldset .remove-target > span {
			margin-left: 2px;
		}
		
		fieldset .remove-target > span:hover {
			text-decoration: underline;
		}
		
		fieldset div {
			column-count: 2;
			max-height: 150px;
			height: auto;
			margin-top: 25px;
		}
		
		fieldset div > label {
			width: 48%;
		}
		
		fieldset div > label > span {
			margin-left: 8px;
			font-weight: normal;
		}
		
		.white {
			color: white;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 规则管理</a></li>
            <c:if test="${activityType eq '1' }">
        		<li class="current"><a title="">一罐一码规则</a></li>
        	</c:if>
        	<c:if test="${activityType eq '2' }">
        		<li class="current"><a title="">一瓶一码规则</a></li>
        	</c:if>
        	<li class="current"><a title="">规则设置</a></li>
            <li class="current"><a title="">添加规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vcodeActivityRebateRule/importRebateRuleCogMoneyConfig.do" onsubmit="return validFile();">
                <input type="hidden" name="vcodeActivityKey" value="${activityCog.vcodeActivityKey}" />
                <input type="hidden" name="areaCode" value="${areaCode}" />
                <input type="hidden" name="activityType" value="${activityType}" />
            	<div class="widget box">
            		<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>基础信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                        		<td class="ab_left"><label class="title mart5">活动名称：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<span>${activityCog.vcodeActivityName}</span>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <input type="hidden" name="filterAreaCode" value="${areaCode}"/>
                                <input type="hidden" name="filterAreaName" />
                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>
                                <td class="ab_main area" colspan="3">
                                 	<c:if test="${empty(areaName)}">
	                                    <div class="area" style="display: flex; margin: 5px 0px;">
		                                    <select name="provinceAry" class="zProvince form-control input-width-normal"></select>
		                                    <select name="cityAry" class="zCity form-control input-width-normal"></select>
		                                    <select name="countyAry" class="zDistrict form-control input-width-normal"></select>
		                                    <c:if test="${allAreaFlage=='0'}">
			                                    <label class="title mart5" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
			                                    <label class="title mart5" style="font-weight: normal; margin-left: 5px; display: none;" id="delArea">删除</label>
		                                    </c:if>
	                                    </div>
                                    </c:if>
                                    <c:if test="${not empty(areaName)}">
                                    <div class="content areaName">
                                        <span>${areaName}</span>
                                    </div>
                                    </c:if>
                                </td>
                               <!--  <div id="areaTemp" style="display: none;">
                                        <div style="display: flex; margin: 5px 0px;">
                                            <select name="provinceAry" class="zProvince form-control input-width-normal"></select>
                                            <select name="cityAry" class="zCity form-control input-width-normal"></select>
                                            <select name="countyAry" class="zDistrict form-control input-width-normal"></select>
                                            <label class="title mart5" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
                                            <label class="title mart5" style="font-weight: normal; margin-left: 5px;" id="delArea">删除</label>
                                        </div>
                                </div> -->
                            </tr>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">规则类型：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        			    <select name="ruleType" style="height: 25px;">
	                        			    <c:if test="${dayTypeFlage != '1'}">
	                                            <option value="1">节假日</option>
	                                            <option value="2" selected="selected">时间段</option>
	                                            <option value="3">周几</option>
                                                <option value="4">每天</option>
	                        			    </c:if>
	                        			     <c:if test="${dayTypeFlage == '1'}">
                                                <option value="4">每天</option>
                                            </c:if>
                        			    </select>
                        			    <c:if test="${dayTypeFlage == '1'}">
                        			         <span style="color: green; margin-left: 5px;">注意：首次添加活动规则，规则类型只能选择 "每天"</span>
                        			    </c:if>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="filterDateAry" />
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<div class="content date" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium" 
                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium" 
                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')||\'%y-%M-%d\'}'})"/>
                                        <c:if test="${allAreaFlage=='0'}">
	                                        <label class="title mart5" style="font-weight: normal; margin-left: 5px;" id="addDate">新增</label>
			                                <label class="title mart5" style="font-weight: normal; margin-left: 5px; display: none;" id="delDate">删除</label>
			                            </c:if>
                        			</div>
                        			<div class="content week" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginDate" id="beginDate" class="form-control input-width-medium" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium" onblur="changeWeek(this)"/>
                                        <label class="title mart5" style="font-weight: normal; margin-left: 5px;" id="addWeek">新增</label>
		                                <label class="title mart5" style="font-weight: normal; margin-left: 5px; display: none;" id="delWeek">删除</label>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="filterTimeAry" />
                        		<td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                        		<td class="ab_main time">
                        			<div class="content time" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginTime" id="beginTime" class="form-control input-width-medium Wdate" 
                                        onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" value="00:00:00"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTime" id="endTime" class="form-control input-width-medium Wdate" 
                                        onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime\')}'})" value="23:59:59"/>
                                        <label class="title mart5" style="font-weight: normal; margin-left: 5px;" id="addTime">新增</label>
		                                <label class="title mart5" style="font-weight: normal; margin-left: 5px; display: none;" id="delTime">删除</label>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">导入文件：<span class="required">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<input type="file" class="import-file" name="filePath" single />
                        			</div>
                        		</td>
                        	</tr>
                		</table>
                	</div>
                	
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>中奖规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                        	<tr>
                                <td class="ab_left"><label class="title mart5">指定热区：</label></td>
                                <td class="ab_main">
                                    <div class="content hot">
                                        <select id="hotAreaKey" name="hotAreaKey" class="hotArea form-control" style="width: 480px;" 
                                        	<c:if test="${areaName ne '省外' or dayTypeFlage eq '1' or allAreaFlage eq '1'}">disabled="disabled"</c:if>>
                                        	<option value="">请选择</option>	
                                        	<c:if test="${not empty(hotAreaList) }">
	                                        	<c:forEach items="${hotAreaList}" var="hotArea">
	                                        		<option value="${hotArea.hotAreaKey }" >${hotArea.hotAreaName}</option>	
	                                        	</c:forEach>
	                                        </c:if>
                                        </select>
                                        <span style="color: green; margin-left: 5px;">注意：指定后符合该热区下的扫码才会中奖</span>
                                    </div>
                                </td>
                            </tr>
                            <tr <c:if test="${activityType == '4' or activityType == '5' }"> style="display: none;"</c:if>>
                                <td class="ab_left"><label class="title mart5">爆点规则：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                        	<textarea rows="3" cols="50" name="eruptRuleInfo" style="width: 480px;" <c:if test="${dayTypeFlage == '1'}">disabled="disabled"</c:if>></textarea>
                        				</div>
                                        <span style="color: green; margin-left: 5px; line-height: 1;">格式：开始日期#结束日期,开始时间#结束时间,分钟:金额,分钟:金额;</span></br>
                                        <span style="color: green; margin-left: 5px; line-height: 2;">格式：2017-09-01#2017-09-05,12:00:00#12:59:59,3:5.18,10:18.88;</span></br>
                                        <span style="color: #46A3FF; margin-left: 5px; line-height: 2;">支持限制时间类型：按规则时间或每天</span>
                                    </div>
                                </td>
                            </tr>
                        	<tr>
                                <td class="ab_left"><label class="title mart5">指定现有规则：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select name="appointRebateRuleKey" class="form-control" style="width: 480px;" <c:if test="${dayTypeFlage == '1'}">disabled="disabled"</c:if>>
                                        	<option value="">请选择</option>	
                                         	<c:forEach items="${rebateRuleList}" var="rebateRule">
	                                           	<c:choose>
	                                           		<c:when test="${rebateRule.ruleType eq '1'}">
	                                           			<option value="${rebateRule.rebateRuleKey}" data-value="${rebateRule.restrictVpoints}-${rebateRule.restrictMoney}-${rebateRule.restrictBottle}-${rebateRule.restrictCount}-${rebateRule.restrictTimeType}">
	                                           				节假日：${rebateRule.beginDate} ${rebateRule.beginTime} - ${rebateRule.endDate} ${rebateRule.endTime}
	                                           			</option>
	                                           		</c:when>
	                                           		<c:when test="${rebateRule.ruleType eq '2'}">
	                                           			<option value="${rebateRule.rebateRuleKey}" data-value="${rebateRule.restrictVpoints}-${rebateRule.restrictMoney}-${rebateRule.restrictBottle}-${rebateRule.restrictCount}-${rebateRule.restrictTimeType}">
	                                           				时间段：${rebateRule.beginDate} ${rebateRule.beginTime} - ${rebateRule.endDate} ${rebateRule.endTime}
	                                           			</option>
	                                           		</c:when>
	                                           		<c:when test="${rebateRule.ruleType eq '3'}">
	                                           			<option value="${rebateRule.rebateRuleKey}" data-value="${rebateRule.restrictVpoints}-${rebateRule.restrictMoney}-${rebateRule.restrictBottle}-${rebateRule.restrictCount}-${rebateRule.restrictTimeType}">
	                                           				周几：${rebateRule.beginDate} - ${rebateRule.endDate}
	                                           			</option>
	                                           		</c:when>
	                                            </c:choose>
                                         	</c:forEach>
                                        </select>
                                        <span style="color: green; margin-left: 5px;">注意：指定后只使用该规则的限制金额和限制瓶数</span>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">限制时间类型：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                       				<div style="float: left; width: 80px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
                                            class="form-control number input-width-small rule" autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        	<tr>
	                       		<td class="ab_left"><label class="title">限制消费金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictMoney" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制消费瓶数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictBottle" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制用户获取次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictCount" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" type="submit">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" url="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
