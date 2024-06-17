<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title></title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	
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
					$("form").attr("action", url);
					$("form").attr("onsubmit", "return true;");
					$("form").submit();
				}
				return false;
			});
            
            // 初始化模板状态显示样式
            $("#status").bootstrapSwitch({  
                onSwitchChange:function(event,state){  
                    if(state==true){  
                       $("input:hidden[name='status']").val("1");
                    }else{  
                       $("input:hidden[name='status']").val("0");
                    }
                }
            });
            
            // 可疑返利类型切换
            $("input[name='doubtRebateType']").on("change", function(){
            	$("div.doubt input").val("");
            	if ($(this).val() == "0") {
            		$("div.doubt input").addClass("money").removeClass("integer");
                    $("div.doubt input").attr("minVal", "0.00").attr("maxVal", "99.99");
            	} else {
                    $("div.doubt input").addClass("integer").removeClass("money");
                    $("div.doubt input").attr("minVal", "0").attr("maxVal", "9999");
            	}
                $("#doubtMoneylable").css("display", $(this).val() == "0" ? "" : "none");
                $("#doubtScorelable").css("display", $(this).val() == "1" ? "" : "none");
            });
            // 编辑界面初始化
            if ($("input[name='doubtRebateType']:checked").val() == "1") {
                $("#doubtMoneylable").css("display", "none");
                $("#doubtScorelable").css("display", "");
                $("div.doubt input").addClass("integer").removeClass("money");
                $("div.doubt input").attr("minVal", "0").attr("maxVal", "9999");
            }
            
		}
		

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 识别次数校验
			var sameMinuteRestrict = Number($("input[name='sameMinuteRestrict']").val());
            var sameDayRestrict = Number($("input[name='sameDayRestrict']").val());
            var historyTimesRestrict = Number($("input[name='historyTimesRestrict']").val());
            var sameMonthRestrict = Number($("input[name='sameMonthRestrict']").val());
            if (sameMinuteRestrict == 0 && sameDayRestrict == 0 && historyTimesRestrict == 0&& sameMonthRestrict == 0) {
            	$.fn.alert("四种识别次数中至少一个要大于0");
            	return false;
            }
            var val=$('input:radio[name="doubtfulTimeLimitType"]:checked').val();

            if(val==null){
                $.fn.alert("可疑时间限制必须选择其中一个");
                return false;
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
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 基础配置</a></li>
            <li class="current"><a title="">风控规则模板</a></li>
            <li class="current"><a title="">修改风控规则模板</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/doubtTemplet/doDoubtTempletEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${doubtTemplet.infoKey}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>规则基本信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">模板名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="templetName" tag="validate"  value="${doubtTemplet.templetName}" 
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">模板状态：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="status" type="hidden" value="${doubtTemplet.status}" />
                                        <input id="status" type="checkbox" <c:if test="${doubtTemplet.status eq '1'}">checked</c:if> data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">按分钟识别：<span class="required"> </span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="sameMinuteRestrict" tag="validate" placeholder="同一分钟扫码次数大于或等于" value="${doubtTemplet.sameMinuteRestrict}"
	                       					class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5" />
	                       					<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">按每天识别：<span class="required"> </span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="sameDayRestrict" tag="validate" placeholder="同一天扫码次数大于或等于" value="${doubtTemplet.sameDayRestrict}"
	                       					class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5" />
	                       					<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">按每月识别：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sameMonthRestrict" tag="validate" placeholder="同一月扫码次数大于或等于" value="${doubtTemplet.sameMonthRestrict}"
                                               class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5" />
                                        <span class="blocker en-larger">次</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">按累计识别：<span class="required"> </span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="historyTimesRestrict" tag="validate" placeholder="历史累计扫码次数大于或等于" value="${doubtTemplet.historyTimesRestrict}"
	                       					class="form-control input-width-larger number positive minValue" autocomplete="off" minVal="1" maxlength="5" />
	                       					<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
                                <td class="ab_left"><label class="title">可疑中出类型<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtRebateType" value="0" <c:if test="${doubtTemplet.doubtRebateType eq '0'}">checked</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">积分红包</span>
                                        <input type="radio" class="tab-radio doubtRadioScore" name="doubtRebateType" value="1" <c:if test="${doubtTemplet.doubtRebateType eq '1'}">checked</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">商城积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="doubt-rule">
                                <td class="ab_left"><label class="title">中出区间：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content doubt">
                                        <span class="blocker en-larger">从</span>
                                        <input id="doubtRuleRangeMin" name="doubtRuleRangeMin" tag="validate" mark="blacklist" value="${doubtTemplet.doubtRuleRangeMin}"
                                            class="form-control required number money preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.00" maxVal="99.99"/>
                                        <span class="blocker en-larger">至</span>
                                        <input id="doubtRuleRangeMax" name="doubtRuleRangeMax" tag="validate" mark="blacklist" value="${doubtTemplet.doubtRuleRangeMax}"
                                            class="form-control required number money sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.00" maxVal="99.99"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">(元)</span>
                                        <span id="doubtScorelable" class="blocker en-larger" style="display: none;">(积分)</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">模板概述：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <textarea name="templetDesc" rows="5"
                                            class="form-control required" autocomplete="off" maxlength="500" >${doubtTemplet.templetDesc}</textarea>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"valign="top"><label class="title">可疑时间限制
                                    <span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtfulTimeLimitType" value="0"<c:if test="${doubtTemplet.doubtfulTimeLimitType eq '0'}">checked="checked"</c:if>  style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">3天</span>
                                        <input type="radio" class="tab-radio doubtRadioScore" name="doubtfulTimeLimitType" value="1" <c:if test="${doubtTemplet.doubtfulTimeLimitType eq '1'}">checked="checked"</c:if>style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">7天</span>
                                        <input type="radio" class="tab-radio doubtRadioScore" name="doubtfulTimeLimitType" value="2"<c:if test="${doubtTemplet.doubtfulTimeLimitType eq '2'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">15天</span>
                                        <input type="radio" class="tab-radio doubtRadioScore" name="doubtfulTimeLimitType" value="3" <c:if test="${doubtTemplet.doubtfulTimeLimitType eq '3'}">checked="checked"</c:if>style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">30天</span>     <span
                                            class="blocker en-larger"style="margin-left: 47px;">所有天数结束后自动释放可疑用户</span>
                                        <label class="validate_tips"></label>
                                    </div>

                                    <div class="content" colspan="3"style="float:left;width:95%">
                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtfulTimeLimitType" value="4" <c:if test="${doubtTemplet.doubtfulTimeLimitType eq '4'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">本周</span><span
                                            class="blocker en-larger"style="margin-left: 200px;">
											仅加入本周算入可疑，下自然周自动释放</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                    <br>
                                    <div class="content" colspan="3"style="float:left;width:95%">
                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtfulTimeLimitType" value="5" <c:if test="${doubtTemplet.doubtfulTimeLimitType eq '5'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">本月</span> <span
                                            class="blocker en-larger" style="margin-left: 200px;">仅加入本月可算入可疑，下自然月自动释放</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                    <br>
                                    <div class="content" colspan="3"style="float:left;width:95%">
                                        <input type="radio" class="tab-radio doubtRadioMoney" name="doubtfulTimeLimitType" value="6" <c:if test="${doubtTemplet.doubtfulTimeLimitType eq '6'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">永久</span> <span
                                            class="blocker en-larger" style="margin-left: 200px;">永久算入可疑，只能主动释放</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/doubtTemplet/showDoubtTempletList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
