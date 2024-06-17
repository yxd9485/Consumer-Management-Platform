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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
		$(function(){
			
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).attr("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});

            
            $("#beginDate").on("blur", function(){
            	var ruleType = $(this).data("ruletype");
                var beginVal = $(this).val();
                var endVal = $("#endDate").val();
                if (ruleType == '3' && beginVal != "") {
                    if (!$.isNumeric(beginVal) || beginVal > 7 || beginVal < 1) {
                        $.fn.alert("请输入1~7的数字");
                        $(this).val("");
                        
                    } else if(endVal != "" && beginVal > endVal) {
                        $.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
                    }
                }
            });
            
            $("#endDate").on("blur", function(){
                var ruleType = $(this).data("ruletype");
                var beginVal = $("#beginDate").val();
                var endVal = $(this).val();
                if (ruleType == '3' && endVal != "") {
                    if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1) {
                        $.fn.alert("请输入1~7的数字");
                        $(this).val("");
                        
                    } else if(beginVal != "" && beginVal > endVal) {
                        $.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
                    }
                }
            });
            
         // 选择规则
			$("[name='appointRebateRuleKey']").on("change", function(){
				var restrict = $(this).find("option:selected").attr("data-value");
				if(typeof(restrict) != 'undefined' || restrict ==''){
                    $("[name='restrictVpoints'").val(restrict.split("-")[0]);
                    $("[name='restrictVpoints'").attr("readonly",true);
                    $("[name='restrictMoney'").val(restrict.split("-")[1]);
                    $("[name='restrictMoney'").attr("readonly",true);
                    $("[name='restrictBottle'").val(restrict.split("-")[2]);
                    $("[name='restrictBottle'").attr("readonly",true);
                    $("[name='restrictCount'").val(restrict.split("-")[3]);
                    $("[name='restrictCount'").attr("readonly",true);       
                    
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
                    $("[name='restrictVpoints'").val('');
                    $("[name='restrictMoney'").val('');
                    $("[name='restrictBottle'").val('');
                    $("[name='restrictCount'").val('');
                    $("[name='restrictVpoints'").attr("readonly",false);
                    $("[name='restrictMoney'").attr("readonly",false);
                    $("[name='restrictBottle'").attr("readonly",false);
                    $("[name='restrictCount'").attr("readonly",false);
					
					// 限制类型
					$("#restrictTimeType1").removeAttr("disabled");
					$("#restrictTimeType2").removeAttr("disabled");
					if('${rebateRuleCog.restrictTimeType}' == "0"){
						$("#restrictTimeType1").trigger("click");
					}else{
						$("#restrictTimeType2").trigger("click");
					}
					$("#restrictTimeType").val("");
					$("#restrictTimeType").attr("disabled", true);
				}
				
			});
         
			// 初始化校验控件
			$.runtimeValidate($("#import_form"));
			// 初始化功能
			initPage();
            
         $("#dateRange2").css("display", "none");
		});
		
		function validFile() {
            if ($("#beginDate:enabled").val() == "" || $("#endDate:enabled").val() == "") {
                $.fn.alert("日期范围不可为空!");
                return false;
            }
            
            if ($("#beginTime:enabled").val() == "" || $("#endTime:enabled").val() == "") {
                $.fn.alert("时间范围不可为空!");
                return false;
            }
            
            var files = $("input.import-file").val();
            if(files != "" && files.indexOf("xls") == -1) {
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

            var restrictVpoints = $("input[name='restrictVpoints']").val();
            var restrictMoney = $("input[name='restrictMoney']").val();
            var restrictBottle = $("input[name='restrictBottle']").val();
            var restrictCount = $("input[name='restrictCount']").val();
            if(('${rebateRuleCog.restrictVpoints}' > 0 || '${rebateRuleCog.restrictMoney}' > 0 || '${rebateRuleCog.restrictBottle}' > 0 || '${rebateRuleCog.restrictCount}' > 0) 
            		&& (restrictVpoints == 0 || restrictVpoints == "") && (restrictMoney == 0 || restrictMoney == "") && (restrictBottle == 0 || restrictBottle == "")){
            	if((restrictVpoints == "" || restrictVpoints == 0) && '${rebateRuleCog.restrictVpoints}' > 0){
                    $.fn.alert("限制积分不能清空，只能做调整");
                    return false;
                }
            	if((restrictMoney == "" || restrictMoney == 0) && '${rebateRuleCog.restrictMoney}' > 0){
            		$.fn.alert("限制金额不能清空，只能做调整");
            		return false;
            	}
            	if((restrictBottle == "" || restrictBottle == 0) && '${rebateRuleCog.restrictBottle}' > 0){
            		$.fn.alert("限制瓶数不能清空，只能做调整");
            		return false;
            	}
            	if((restrictCount == "" || restrictCount == 0) && '${rebateRuleCog.restrictCount}' > 0){
            		$.fn.alert("用户限制扫码次数不能清空，只能做调整");
            		return false;
            	}
            }
            
            // 默认值
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
			
			// 防重复提交
            $(".btnSave").attr("disabled", "disabled");
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
            <li class="current"><a title="">修改规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath%>/vcodeActivityRebateRule/importRebateRuleCogMoneyConfig.do" onsubmit="return validFile();">
            	<input type="hidden" name="rebateRuleKey" value="${rebateRuleCog.rebateRuleKey}" />
            	<input type="hidden" name="vcodeActivityKey" value="${rebateRuleCog.vcodeActivityKey}" />
            	<input type="hidden" name="areaCode" value="${rebateRuleCog.areaCode}" />
            	<input type="hidden" name="ruleType" value="${rebateRuleCog.ruleType}" />
            	<input type="hidden" name="activityType" value="${activityType}" />
            	<div class="widget box">
            		<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>导入</h4></div>
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
                        		<td class="ab_left"><label class="title mart5">规则区域：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<span>${rebateRuleCog.areaName}</span>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">规则类型：<span class="white">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content" id="ruleType" data-ruletype="${rebateRuleCog.ruleType}">
                                        <c:choose>
                                            <c:when test="${rebateRuleCog.ruleType == '1'}">节假日</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '2'}">时间段</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '3'}">周几</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '4'}">每天</c:when>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <c:choose>
                                        <c:when test="${rebateRuleCog.ruleType == '4' or rebateRuleCog.ruleType == '5'}">
		                                    <div class="content">
		                                        <span class="blocker">从</span>
		                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium" disabled="disabled"/>
		                                        <span class="blocker en-larger">至</span>
		                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium" disabled="disabled"/>
		                                    </div>
                                        </c:when>
                                        <c:when test="${rebateRuleCog.ruleType == '3'}">
		                                    <div class="content">
		                                        <span class="blocker">从</span>
		                                        <input name="beginDate" id="beginDate" class="form-control input-width-medium"
		                                        <c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly"</c:if> 
		                                        data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.beginDate }"/>
		                                        <span class="blocker en-larger">至</span>
		                                        <input name="endDate" id="endDate" class="form-control input-width-medium" 
		                                        <c:if test="${isEditFlag eq '2'}">readonly="readonly"</c:if>  
		                                        data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.endDate }"/>
		                                    </div>
                                        </c:when>
                                        <c:otherwise>
		                                    <div class="content">
		                                        <span class="blocker">从</span>
		                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium" 
		                                        	<c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly"</c:if> 
		                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.beginDate }"/>
		                                        <span class="blocker en-larger">至</span>
		                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium"  
		                                        	<c:if test="${isEditFlag eq '2'}">readonly="readonly"</c:if> 
		                                        	onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')||\'%y-%M-%d\'}'})" data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.endDate }"/>
		                                    </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                                <td class="ab_main">
	                                    <div class="content">
	                                        <span class="blocker">从</span>
	                                        <input name="beginTime" id="beginTime" class="form-control input-width-medium Wdate" 
	                                                <c:if test="${isEditFlag eq '1' or isEditFlag eq '2' }">readonly="readonly"</c:if> 
	                                                <c:if test="${rebateRuleCog.ruleType == '5'}">disabled="disabled"</c:if> 
	                                                onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" value="${rebateRuleCog.beginTime}"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endTime" id="endTime" class="form-control input-width-medium Wdate" 
	                                                <c:if test="${isEditFlag eq '2'}">readonly="readonly"</c:if>  
                                                    <c:if test="${rebateRuleCog.ruleType == '5'}">disabled="disabled"</c:if> 
	                                                onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime\')}'})" value="${rebateRuleCog.endTime}"/>
	                                    </div>
                                </td>
                            </tr>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">导入文件：<span class="required">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<input type="file" class="import-file" name="filePath" single  <c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly"</c:if>/>
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
                                    	<c:if test="${rebateRuleCog.areaName eq '省外' or isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">
                                    	<input type="hidden" name="hotAreaKey" value="${rebateRuleCog.hotAreaKey }">
                                    	</c:if>
                                        <select name="hotAreaKey" class="hotArea form-control" style="width: 480px;" 
                                        	<c:if test="${rebateRuleCog.areaName eq '省外' or isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
                                        	<option value="">请选择</option>	
                                        	<c:if test="${not empty(hotAreaList) }">
	                                        	<c:forEach items="${hotAreaList}" var="hotArea">
	                                        		<option value="${hotArea.hotAreaKey }" <c:if test="${hotArea.hotAreaKey == rebateRuleCog.hotAreaKey }">selected="selected"</c:if>>${hotArea.hotAreaName}</option>	
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
                                        	<c:if test="${isEditFlag eq '2' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">
                                        		<input name="eruptRuleInfo" type="hidden" value="${rebateRuleCog.eruptRuleInfo }">
                                        	</c:if>
                                        	<textarea rows="3" cols="50" name="eruptRuleInfo" style="width: 480px; 
                                        	<c:if test="${isEditFlag eq '2' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">background-color: #EDEDED;</c:if>" 
                                        	<c:if test="${isEditFlag eq '2' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled" </c:if>>${rebateRuleCog.eruptRuleInfo }</textarea>
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
                                    	<c:if test="${isEditFlag ne '0' or (rebateRuleCog.isValid eq '1' and isEditFlag ne '0') or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">
											<input type="hidden" name="appointRebateRuleKey" value="${rebateRuleCog.appointRebateRuleKey}">
										</c:if>
                                        <select name="appointRebateRuleKey" class="form-control" style="width:480px;" <c:if test="${isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
                                        	<option value="">请选择</option>	
                                         	<c:forEach items="${rebateRuleList}" var="rebateRule">
                                         		<c:if test="${rebateRule.rebateRuleKey ne rebateRuleCog.rebateRuleKey}">
	                                           	<c:choose>
	                                           		<c:when test="${rebateRule.ruleType eq '1'}">
	                                           			<option value="${rebateRule.rebateRuleKey}" data-value="${rebateRule.restrictVpoints}-${rebateRule.restrictMoney}-${rebateRule.restrictBottle}-${rebateRule.restrictCount}-${rebateRule.restrictTimeType}" 
	                                           				<c:if test="${rebateRule.rebateRuleKey eq  rebateRuleCog.appointRebateRuleKey}">selected</c:if>>
	                                           				节假日：${rebateRule.beginDate} ${rebateRule.beginTime} - ${rebateRule.endDate} ${rebateRule.endTime}
	                                           			</option>
	                                           		</c:when>
	                                           		<c:when test="${rebateRule.ruleType eq '2'}">
	                                           			<option value="${rebateRule.rebateRuleKey}" data-value="${rebateRule.restrictVpoints}-${rebateRule.restrictMoney}-${rebateRule.restrictBottle}-${rebateRule.restrictCount}-${rebateRule.restrictTimeType}"
	                                           				<c:if test="${rebateRule.rebateRuleKey eq  rebateRuleCog.appointRebateRuleKey}">selected</c:if>>
	                                           				时间段：${rebateRule.beginDate} ${rebateRule.beginTime} - ${rebateRule.endDate} ${rebateRule.endTime}
	                                           			</option>
	                                           		</c:when>
	                                           		<c:when test="${rebateRule.ruleType eq '3'}">
	                                           			<option value="${rebateRule.rebateRuleKey}" data-value="${rebateRule.restrictVpoints}-${rebateRule.restrictMoney}-${rebateRule.restrictBottle}-${rebateRule.restrictCount}-${rebateRule.restrictTimeType}"
	                                           				<c:if test="${rebateRule.rebateRuleKey eq  rebateRuleCog.appointRebateRuleKey}">selected</c:if>>
	                                           				周几：${rebateRule.beginDate} - ${rebateRule.endDate}
	                                           			</option>
	                                           		</c:when>
	                                            </c:choose>
	                                            </c:if>
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
                       					<input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" 
                       						<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>
                       						<c:if test="${rebateRuleCog.restrictTimeType eq '0'}">checked="checked"</c:if> 
                       						style="float: left; height:20px; cursor: pointer;">&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" 
                       						<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>
                       						<c:if test="${rebateRuleCog.restrictTimeType eq '1'}">checked="checked"</c:if> 
                       						style="float: left; height:20px; cursor: pointer;">&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" 
                                            <c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if>
                                            value='<fmt:formatNumber value="${rebateRuleCog.restrictVpoints == 0 ? '' : rebateRuleCog.restrictVpoints}" pattern="0"></fmt:formatNumber>' 
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
	                       				<input name="restrictMoney" tag="validate" 
	                       					<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if>
	                       					value='<fmt:formatNumber value="${rebateRuleCog.restrictMoney == 0 ? '' : rebateRuleCog.restrictMoney}" pattern="0.00"></fmt:formatNumber>' 
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
	                       				<input name="restrictBottle" tag="validate" 
	                       					<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if> 
	                       					value="${rebateRuleCog.restrictBottle eq '0' ? '' : rebateRuleCog.restrictBottle}" 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户限制获取次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictCount" tag="validate" 
	                       					<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if> 
	                       					value="${rebateRuleCog.restrictCount eq '0' ? '' : rebateRuleCog.restrictCount}" 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">是否清空扫码数据：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input type="checkbox" tag="validate" name="isClear" value="1" style="float: left;" <c:if test="${isEditFlag eq '1' or isEditFlag eq '2' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
	                       				<span class="blocker en-larger"></span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
			            	<c:if test="${isEditFlag ne '2'}">
					    		<button class="btn btn-blue btnSave" type="submit">保存</button> &nbsp;&nbsp;&nbsp;&nbsp;
					    	</c:if>
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
