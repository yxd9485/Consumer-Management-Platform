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
            // 初始化校验控件
            $.runtimeValidate($("div.tab-content"));
            
            // 初始化功能
            initPage();
            
         	// 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
         	
			$("input[name=crowdLimitType]").on("click",function(){
         		if($(this).val() == '2' || $(this).val() == '3'){
         			$("#userGroupTr").css("display","");
         		}else{
         			$("#userGroupTr").css("display","none");
         		}
         	});
        });
        
        function initPage() {
        	
        	// 类型为每天时，日期区域不可用
			$("[name='ruleType']").on("change", function(evt) {
				
				if ($(this).val() == '1') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input, #addDate").removeAttr("disabled");
                    $("div.week input, #addWeek").attr("disabled", "disabled");
				}
                
                // 周几
                if ($(this).val() == '2' ) {
                    $("div.date").css("display", "none");
                    $("div.week").css("display", "block");
                    $("div.date input, #addDate").attr("disabled", "disabled");
                    $("div.week input, #addWeek").removeAttr("disabled");
                }
                
				$("[name='beginDate']:disabled").val("");
				$("[name='endDate']:disabled").val("");
			});
			$("[name='ruleType']").val('${activityCog.ruleType}');
        	$("[name='ruleType']").trigger("change");
			
			// 判断周几输入格式 
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
			
			// 开启限购联动
			$("input[name=isGroupBuyingLimit]").on("change",function(){
				if($(this).prop("checked")){
					$("input[name=isGroupBuyingLimit]").val("1");
					$("input[name=groupBuyingLimitNum]").attr("disabled", false);
				}else{
					$("input[name=isGroupBuyingLimit]").val("0");
					$("input[name=groupBuyingLimitNum]").val('');
					$("input[name=groupBuyingLimitNum]").attr("disabled", true);
				}	
			});
			
			// 开启预告联动
			$("input[name=isNotice]").on("change",function(){
				if($(this).prop("checked")){
					$("input[name=isNotice]").val("1");
					$("select[name=noticeHour]").attr("disabled", false);
				}else{
					$("input[name=isNotice]").val("0");
					$("select[name=noticeHour]").attr("disabled", true);
				}	
			});
			
			
			$("select[name=goodsId]").on("change", function(){
				$option = $(this).find("option:selected");
				var realPay = ($option.data("realpay") / 100).toFixed(2);
				var realVpoints = $option.data("realvpoints");
				var goodsRemains = $option.data("goodsremains");
				$("#realPay").html(realPay);
				$("#realVpoints").html(realVpoints);
				$("#goodsRemains").html(goodsRemains);
			});
			$("select[name=goodsId]").trigger("change");
			
            
            // 保存活动信息及风控规则
            $(".btnSave").on("click", function(){
                // 输入元素校验
                $validContent = $(this).closest("div.tab-content");
                var validateResult = $.submitValidate($validContent);
                if(!validateResult){
                    return false;
                }
                
                // JSON
                var paramJson = {};
                $(this).closest("div.tab-content").find(":input:enabled[type!=hidden][name]").each(function(){
                    if($(this).attr("type") == "radio") {
                        if ($(this).is(":checked")) {
                            paramJson[$(this).attr("name")] = $(this).val();
                        }
                    } else {
                        paramJson[$(this).attr("name")] = $(this).val();
                    }
                });
                paramJson['infoKey']='${activityCog.infoKey}';
                
                // 处理群组
                var userGroupIds = "";
                $("input[name=userGroupIds]").each(function(){
                	if($(this).is(":checked")){
                		userGroupIds += $(this).val() + ',';
                	}
                });
                if(userGroupIds != '' && userGroupIds.length > 0){
                	userGroupIds = userGroupIds.substring(0, userGroupIds.length - 1);
                }
                paramJson["userGroupIds"] = userGroupIds;

                // 提交表单
                var url = "<%=cpath%>/groupBuyingActivity/doGroupBuyingActivityEdit.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        if (data['errMsg'] == "保存成功") {
                            $.fn.alert(data['errMsg'], function(){
                            	$("form").attr("action", $(".btnReturn").data("url"));
                				$("form").attr("onsubmit", "return true;");
                				$("form").submit();
                            });
                        } else {
                        	$.fn.alert(data['errMsg'], function(){});
                        }
                    }
                });
                
            });
            
        }
        
        function changeWeek(obj){
			var beginVal = $(obj).parent(".week").find("input[name='beginDate']").val();
			var endVal = $(obj).val();
			if (endVal != "") {
				if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1 || endVal > 7) {
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
        
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            
            return true;
        }
        
        // 定时更新iframe的高度
        var iframeClock = setInterval("setIframeHeight()", 50);
        function setIframeHeight() {
            iframe = document.getElementById('ruleFrame');
            if (iframe) {
                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
                if (iframeWin.document.body) {
                    iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
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
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 拼团配置</a></li>
            <li class="current"><a> 修改活动</a></li>
        </ul>
    </div>
    
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                <div class="widget box activityinfo">
				<!--- 活动信息 -->
                    <div class="tab-content">
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="activityName" tag="validate" style="width: 400px !important;"
                                                class="form-control required" autocomplete="off" maxlength="30" value="${activityCog.activityName }" />
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                        		<td class="ab_left"><label class="title mart5">时间类型：<span class="required">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        			    <select name="ruleType" class="form-control input-width-medium">
	                                            <option value="1" <c:if test="${activityCog.ruleType eq '1'}"> selected="selected" </c:if>>时间段</option>
	                                            <%-- <option value="2" <c:if test="${activityCog.ruleType eq '2'}"> selected="selected" </c:if>>周几 --%>
                        			    </select>
                        			</div>
                        		</td>
                        	</tr>
                                <tr>
	                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
	                        		<td class="ab_main date">
	                        			<div class="content date" style="margin: 5px 0px;">
	                                        <input name="beginDate" id="beginDate" class="form-control input-width-medium Wdate required"  tag="validate" value="${activityCog.beginDate }"  
	                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" autocomplete="off"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate required"  tag="validate" value="${activityCog.endDate }"
	                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}'})" autocomplete="off"/>
	                                        <label class="validate_tips"></label>
	                        			</div>
	                        			<div class="content week" style="margin: 5px 0px;">
	                                        <input name="beginDate" id="beginDate" class="form-control number preValue minValue maxValue input-width-medium" tag="validate" value="${activityCog.beginDate }" autocomplete="off" />
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endDate" id="endDate" class="form-control number preValue minValue maxValue input-width-medium" tag="validate" value="${activityCog.endDate }" onblur="changeWeek(this)" autocomplete="off"/>
	                        			</div>
	                        		</td>
	                        	</tr>
                                <tr>
                                    <td class="ab_left"><label class="title">商品选择：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content goods">
                                            <select name="goodsId" tag="validate" class="form-control input-width-larger required activitysku">
                                                <option value="">请选择</option>
                                                <c:if test="${!empty goodsList}">
                                                <c:forEach items="${goodsList}" var="item">
                                                <option value="${item.goodsId}" data-realvpoints="${item.goodsVpoints }" data-realpay="${item.realPay }" data-goodsremains="${item.goodsRemains }" <c:if test="${activityCog.goodsId eq item.goodsId}"> selected="selected" </c:if>>${item.goodsName}</option>
                                                </c:forEach>
                                                </c:if>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
		                       		<td class="ab_left"><label class="title">商品原价：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker en-larger" id="realPay"></span>
		                       				<span class="blocker en-larger">元</span>
		                       				<span class="blocker en-larger" style="color: green;">该价格为商品优惠后的价格</span>
		                       			</div>
		                       		</td>
		                       	</tr>
                                <tr>
		                       		<td class="ab_left"><label class="title">拼团价格：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="groupBuyingPay" tag="validate" value="${activityCog.groupBuyingPay }"  readonly  unselectable="on" 
		                       					class="form-control required money input-width-small rule" autocomplete="off" maxlength="9" />
		                       				<span class="blocker en-larger">元</span>
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">商品原积分：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker en-larger" id="realVpoints"></span>
		                       				<span class="blocker en-larger">积分</span>
		                       				<span class="blocker en-larger">该积分为商品优惠后的价格</span>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">拼团积分：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="groupBuyingVpoints" tag="validate"  value="${activityCog.groupBuyingVpoints }"
		                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="9" />
		                       				<span class="blocker en-larger">积分</span>
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
                                <tr>
		                       		<td class="ab_left"><label class="title">现有库存：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker en-larger" id="goodsRemains"></span>
		                       				<span class="blocker en-larger">件</span>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">拼团活动总库存：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="groupBuyingTotalNum" tag="validate" value="${activityCog.groupBuyingTotalNum }" disabled="disabled"
		                       					class="form-control required integer input-width-small rule minValue" autocomplete="off" minVal="1" maxlength="9" />
		                       				<span class="blocker en-larger">件</span>
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">拼团有效期：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<select name="orderValidHour" class="form-control input-width-medium">
	                                            <option value="12" <c:if test="${activityCog.orderValidHour eq '12' }"> selected="selected" </c:if>>12</option>
	                                            <option value="24" <c:if test="${activityCog.orderValidHour eq '24' }"> selected="selected" </c:if>>24</option>
	                                            <option value="36" <c:if test="${activityCog.orderValidHour eq '36' }"> selected="selected" </c:if>>36</option>
	                                            <option value="48" <c:if test="${activityCog.orderValidHour eq '48' }"> selected="selected" </c:if>>48</option>
	                        			    </select>
		                       				<span class="blocker en-larger">时</span>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">限制进行中的团数量：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="limitGroupNum" tag="validate" value="${activityCog.limitGroupNum }" 
		                       					class="form-control required integer input-width-small rule minValue" autocomplete="off" minval="1" maxlength="9" />
		                       				<span class="blocker en-larger">个</span>
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">成团人数：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="reachNum" tag="validate" value="${activityCog.reachNum }" 
		                       					class="form-control required integer input-width-small rule minValue" autocomplete="off" minval="2" maxlength="9" />
		                       				<span class="blocker en-larger">人</span>
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
                            </table>
                        </div>
                        
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>拼团限制</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">每人限购：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                        	<span class="blocker en-larger">
												<input name="isGroupBuyingLimit" type="checkbox" value="${activityCog.isGroupBuyingLimit }" <c:if test="${activityCog.isGroupBuyingLimit eq '1'}"> checked="checked" </c:if>/>
											</span>
                                            <span class="blocker en-larger">开启</span>
                                            <span class="blocker en-larger">每人可购买</span>
                                            <input name="groupBuyingLimitNum" class="form-control required integer input-width-small rule" autocomplete="off" maxlength="2" value="${activityCog.groupBuyingLimitNum }" 
                                            	<c:if test="${activityCog.isGroupBuyingLimit ne '1'}"> disabled="disabled" </c:if>/>
                                            <span class="blocker en-larger">件</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr style="display: none;">
                                    <td class="ab_left"><label class="title">优惠叠加：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                        	<span class="blocker en-larger">
                                            	<input name="isUseDiscounts" type="radio" value='0' <c:if test="${activityCog.isUseDiscounts eq '0'}"> checked="checked" </c:if>/>
                                            </span>
                                            <span class="blocker en-larger">不叠加其他优惠券</span>
                                            <span class="blocker en-larger">
                                            	<input name="isUseDiscounts" type="radio" value='1' <c:if test="${activityCog.isUseDiscounts eq '1'}"> checked="checked" </c:if>/>
                                            </span>
                                            <span class="blocker en-larger">叠加其他优惠券</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr  style="display: none;">
                                    <td class="ab_left"><label class="title">人群限制：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                        	<span class="blocker en-larger">
                                            	<input name="crowdLimitType" type="radio" value='0' <c:if test="${activityCog.crowdLimitType eq '0'}"> checked="checked" </c:if>/>
                                            </span>
                                            <span class="blocker en-larger">不限制</span>
                                            <%-- <span class="blocker en-larger">
                                            	<input name="crowdLimitType" type="radio" value='1' <c:if test="${activityCog.crowdLimitType eq '1'}"> checked="checked" </c:if>/>
                                            </span>
                                            <span class="blocker en-larger">黑名单用户不可参与</span> --%>
                                            
                                            <c:if test="${not empty(groupList)}">
	                                            <span class="blocker en-larger">
	                                            	<input name="crowdLimitType" type="radio" value='2' <c:if test="${activityCog.crowdLimitType eq '2'}"> checked="checked" </c:if>/>
	                                            </span>
	                                            <span class="blocker en-larger">指定群组参与</span>
	                                            <span class="blocker en-larger">
	                                            	<input name="crowdLimitType" type="radio" value='3' <c:if test="${activityCog.crowdLimitType eq '3'}"> checked="checked" </c:if>/>
	                                            </span>
	                                            <span class="blocker en-larger">指定群组不可参与</span>
	                                       	</c:if>
                                        </div>
                                    </td>
                                </tr>
                                <c:if test="${not empty(groupList)}"> 
	                                <tr id="userGroupTr" <c:if test="${activityCog.crowdLimitType eq '0' or activityCog.crowdLimitType eq '1'}"> style="display: none;" </c:if>>	
	                                	<td class="ab_left"><label class="title"></label></td>
	                                	<td class="ab_main" colspan="3">
	                                		<div>
		                                        <c:forEach items="${groupList }" var="group">
		                                        	<c:set var="checkedStr" value=""></c:set>
		                                        	<c:forEach items="${fn:split(activityCog.userGroupIds, ',')}" var='groupId'>
		                                        		<c:if test="${groupId eq group.id}">
		                                        			<c:set var="checkedStr" value="checked='checked'"></c:set>
		                                        		</c:if>
		                                        	</c:forEach>
	                                        		<span class="blocker en-larger">
		                                            	<input name="userGroupIds" type="checkbox" value='${group.id }' ${checkedStr}/>
		                                            </span>
		                                            <span class="blocker en-larger">${group.name}</span>
	                                        	</c:forEach>
		                                    </div>
	                                	</td>
	                                </tr>
								</c:if>
                            </table>
                        </div>
                        <div class="widget-header"  style="display: none;">
                            <h4><i class="iconfont icon-xinxi"></i>其他设置</h4>
                        </div>
                        <div class="widget-content panel no-padding" style="display: none;">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">预告：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                        	<span class="blocker en-larger">
                                            	<input name="isNotice" type="checkbox" value="${activityCog.isNotice }" <c:if test="${activityCog.isNotice eq '1'}"> checked="checked" </c:if>/>
                                            </span>
                                            <span class="blocker en-larger">开启</span>
                                            <span class="blocker en-larger">活动开始前</span>
	                        			    <select name="noticeHour" class="form-control input-width-medium" <c:if test="${activityCog.isNotice ne '1'}"> disabled="disabled" </c:if>>
	                                            <option value="12" <c:if test="${activityCog.noticeHour eq '12' }"> selected="selected" </c:if>>12</option>
	                                            <option value="24" <c:if test="${activityCog.noticeHour eq '24' }"> selected="selected" </c:if>>24</option>
	                                            <option value="36" <c:if test="${activityCog.noticeHour eq '36' }"> selected="selected" </c:if>>36</option>
	                                            <option value="48" <c:if test="${activityCog.noticeHour eq '48' }"> selected="selected" </c:if>>48</option>
	                                            <option value="60" <c:if test="${activityCog.noticeHour eq '60' }"> selected="selected" </c:if>>60</option>
	                                            <option value="72" <c:if test="${activityCog.noticeHour eq '72' }"> selected="selected" </c:if>>72</option>
	                        			    </select>
	                        			    <span class="blocker en-larger">小时提示预告</span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                            	<c:if test="${activityCog.isBegin == '0'}">
                                	<a class="btn btn-blue btnSave">保 存</a>
                                </c:if>
                                <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityList.do">返 回</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
