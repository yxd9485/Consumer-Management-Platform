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
    <title>POPSS活动基础信息</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			// 初始化功能
			initPage();

            btnChange("${popssInfo.isExtractLimit}");
		});
		
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 日期
			var startDate = $("input[name='startTime']").val();
			var endDate = $("input[name='endTime']").val();
			if(startDate == "" || endDate == "") {
			    
				$("input[name='startTime']").parents(".content").find(".validate_tips").show();
				$("input[name='startTime']").parents(".content").find(".validate_tips").text("活动时间不能为空");
				$("input[name='startTime']").parents(".content").find(".validate_tips").addClass("valid_fail_text");
				return false;
			}
			
			// 积分兑换
			var skuKey = $("input[name='currencyPrice']").val();
			if(skuKey == ""){
                $(".firstRebate").find(".validate_tips").text("未填写积分兑换值");
                $(".firstRebate").find(".validate_tips").addClass("valid_fail_text");
                $(".rebateVpoints").find(".validate_tips").show();
				return false;
			}
            
            // 关注公众号
            /* var rebateVpoints = $("input[name='attentionVpoints']").val();
            if(rebateVpoints == ""){
                $(".rebateVpoints").find(".validate_tips").text("关注公众号送积分的值不能为空");
                $(".rebateVpoints").find(".validate_tips").addClass("valid_fail_text");
                $(".rebateVpoints").find(".validate_tips").show();
                return false;
            } */
			
			return true;
		}
		
		function initPage() {
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
		}

        function btnChange(values){
       		if(values == 1 || values == ""){
       			$("input[name='firstCash']").attr("disabled",false).attr("tag","validate").addClass("required");
   				$("input[name='secondCash']").attr("disabled",false).attr("tag","validate").addClass("required");
   				// 初始化校验控件
   				$.runtimeValidate($("#code_form"));
   			}else{
   				$("input[name='firstCash']").attr("disabled",true).removeAttr("tag").removeClass("required");
   		    	$("input[name='secondCash']").attr("disabled",true).removeAttr("tag").removeClass("required");   
   		    	$("input[name='firstCash']").parents(".content").find(".validate_tips").text("").removeClass("valid_fail_text");
   		    	$("input[name='secondCash']").parents(".content").find(".validate_tips").text("").removeClass("valid_fail_text");
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
       .tdd {
            word-break: break-all;
            word-wrap: break-word;
        }
        .active_board_table .ab_left {
            width: 20%;
            text-align: right;
            margin: 6px;
            padding: 6px;
        }
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> POPSS活动基础信息</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vpsWechatMovementActivityBaseInfo/savePopssActivityInfo.do">
            	<input type="hidden" name="infoKey" value="${popssInfo.infoKey}">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>基础设置</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
							<tr>
								<td class="ab_left"><label class="title">一个积分兑换：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">

										<input type="hidden" name="oldCurrencyPrice" value="${popssInfo.currencyPrice}"/>

										<input name="currencyPrice" tag="validate" style="width: 240px"
											   class="payMoney form-control  required money minValue maxValue" minval="-9999999.99"maxval="9999999.99" autocomplete="off"  maxlength="10" value="${popssInfo.currencyPrice}"/>
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">元</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
                		</table>
                	</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>活动任务设置</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">

							<tr>
								<td class="ab_left">1、<label class="title">关注公众号：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="attentionVpoints" tag="validate"
											   class="form-control required  number integer preValue input-width-larger" autocomplete="off" maxlength="10" value="${popssInfo.attentionVpoints}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">个/次 （首次关注，取关后不再赠送）</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr style="display: none;">
								<td class="ab_left">2、<label class="title">完善个人资料：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="finishUserInfoVpoints" tag="validate" disabled="disabled"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10" value="${popssInfo.finishUserInfoVpoints}"/>
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">个/次 （用户首次填写资料）</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>

							<tr style="display: none;">
								<td class="ab_left">3、<label class="title">邀请好友得积分：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="inviteFriendsVpoints" tag="validate" disabled="disabled"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10"value="${popssInfo.inviteFriendsVpoints}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">个/人</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr style="display: none;">
								<td class="ab_left"><label class="title">整个活动，每人最多可邀请：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="inviteFriendsLimit" tag="validate" disabled="disabled"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10"value="${popssInfo.inviteFriendsLimit}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">人</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
                		</table>
                	</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>活动规则设置</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left">1、<label class="title">活动时间：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="startTime" class="form-control input-width-medium Wdate required preTime" style="width: 105px !important;"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" value="${popssInfo.startTime}"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTime" class="form-control input-width-medium Wdate required sufTime" style="width: 105px !important;"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"value="${popssInfo.endTime}" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                            <tr>
								<td class="ab_left">2、<label class="title">支付积分参与活动：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="activityPayVpoints" tag="validate"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10" value="${popssInfo.activityPayVpoints}"/>
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">个/人</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left">2、<label class="title">支付积分参与周赛：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="activityWeekPayVpoints" tag="validate"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10" value="${popssInfo.activityWeekPayVpoints}"/>
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">个/人</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
							<tr>
								<td class="ab_left">3、<label class="title">每日活动人数上限：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="activityMaxnum" tag="validate"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10"value="${popssInfo.activityMaxnum}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">人</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
							<tr>
							<tr>
								<td class="ab_left">4、<label class="title">每日活动达标步数：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="stepLimit" tag="validate"
											   class="form-control input-width-larger number integer preValue required" autocomplete="off" maxlength="10"value="${popssInfo.stepLimit}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">步</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
							<tr>
								<td class="ab_left">5、<label class="title">周赛达标积分红包：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="weeksReachMoney" tag="validate"  style="width: 240px"
											   class="payMoney form-control  required money minValue maxValue" minval="1.00"maxval="9999999.99" autocomplete="off" maxlength="10" value="${popssInfo.weeksReachMoney}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">元</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>

                            <tr>
                                <td class="ab_left">6、<label class="title"> 虚拟活动数据倍数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="hidden" name="oldActivityMagnification" value="${empty(popssInfo.activityMagnification) ? 1.00 : popssInfo.activityMagnification}"/>
                                        <input name="activityMagnification" tag="validate" style="width: 240px"
                                               class="payMoney form-control required money minValue maxValue" minval="1.00"maxval="9999999.99" autocomplete="off" maxlength="10" value="${popssInfo.activityMagnification}"/>
                                        <span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">倍数 （增加虚拟活动人数=倍数*实际报名参加人数）格式参照:#.##</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left">7、<label class="title">明日活动资金池：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                    <input type="hidden" name="oldBonusPool" value="${popssInfo.bonusPool}"/>
                                        <input name="bonusPool" tag="validate"  style="width: 240px"
                                               class="payMoney form-control  required money minValue maxValue" minval="1.00"maxval="9999999.99" autocomplete="off" maxlength="10" value="${popssInfo.bonusPool}" />
                                        <span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            
                		</table>
                	</div>
					<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>活动提现设置</h4></div>
					<div class="widget-content panel no-padding">
						<table class="active_board_table">
							<tr>
								<td class="ab_left">1、<label class="title">是否设置提现门槛：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<select id ="isExtractLimit" name="isExtractLimit" onchange="btnChange(this[selectedIndex].value)" class="form-control input-width-larger" value="${popssInfo.isExtractLimit}">
                                            <option value="1" <c:if test="${popssInfo.isExtractLimit=='1' }">selected="selected"</c:if>>是</option>
                                            <option value="0" <c:if test="${popssInfo.isExtractLimit=='0' }">selected="selected"</c:if>>否</option>
										</select>
									</div>
								</td>
							</tr>
							<tr id = "firstMoneyTr">
								<td class="ab_left">2、<label class="title">第一次兑付（>=1元）：</label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="firstCash" style="width: 240px"
											   class="form-control money minValue maxValue" 
											   minval="-9999999.99" maxval="9999999.99" autocomplete="off" maxlength="10"  autocomplete="off" value="${popssInfo.firstCash}"/>
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">元</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr id = "secondMoneyTr">
								<td class="ab_left">3、<label class="title">第二次兑付（>=1元）：</label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="secondCash" style="width: 240px"
											   class="form-control money minValue maxValue" 
											   minval="-9999999.99" maxval="9999999.99" autocomplete="off" maxlength="10"  autocomplete="off" value="${popssInfo.secondCash}" />
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">元</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
						</table>
					</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
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
