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
    <title>添加一码双奖活动</title>
    
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
						
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});
            
            
        	// 检验名称是否重复
            $("input[name='activityName']").on("blur",function(){
            	var activityName = $("input[name='activityName']").val();
            	if(activityName == "") return;
            	checkName(activityName);
            });
        	
        	$("select[name='activityType']").on("change",function(){
        		if($(this).val() == '1'){
	    			$("[name='exchangeVpoints']").attr("disabled", "disabled").val("");
	    			$("[name='openingNumber']").removeAttr("disabled");
	    			$("[name='virtualNumber']").removeAttr("disabled");
	    			$("select[name='isDedicated']").removeAttr("disabled");
	    		}else if($(this).val() == '2'){
	    			$("[name='exchangeVpoints']").removeAttr("disabled");
	    			$("[name='openingNumber']").attr("disabled", "disabled").val("");
	    			$("[name='virtualNumber']").attr("disabled", "disabled").val("");
	    			$("select[name='isDedicated']").attr("disabled", "disabled");
    			}
        	});
        	
        	$("select[name='isDedicated']").on("change",function(){
        		if($(this).val() == '1'){
	    			$("[name='openingNumber']").attr("disabled", "disabled").val("");
	    			$("[name='virtualNumber']").attr("disabled", "disabled").val("");
	    		}else if($(this).val() == '0'){
	    			$("[name='openingNumber']").removeAttr("disabled");
	    			$("[name='virtualNumber']").removeAttr("disabled");
    			}
        	});
            
            // 兑奖截止类型
            $("[name='prizeExpireType']").on("change", function(){
            	if($(this).val() == '0') {
            		$(this).closest("div").find("input[name='prizeExpireDate']").removeAttr("disabled");
            		$(this).closest("div").find("input[name='prizeValidDay']").attr("disabled", "disabled").val("");
            	} else {
                    $(this).closest("div").find("input[name='prizeExpireDate']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='prizeValidDay']").removeAttr("disabled");
            	}
            });
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(bussionName){
			$.ajax({
				url : "${basePath}/biddingActivity/checkBussionName.do",
			    data:{
			    		"infoKey":"",
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
		
		// 检验月擂台是否重复
		function checkMonthBidding(startDate, endDate){
			$.ajax({
				url : "${basePath}/biddingActivity/checkMonthBidding.do",
			    data:{
			    		"infoKey":"",
			    		"startDate":startDate,
			    		"endDate":endDate
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("当前时间段内已存在月擂台，无法重复重建");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}
		
		// 检验日期是否为当月最后一天，true是
		function isLastDay(inputDate) {
		　　var d = new Date(inputDate.replace(/\-/, "/ "));
		　　var nd = new Date(d.getTime()+24*60*60*1000); //next day
		　　return (d.getMonth()!=nd.getMonth());

		} 

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 检验名称是否重复
			/* var activityName = $("input[name='activityName']").val();
        	if(activityName == "") return false;
        	checkName(activityName);
        	if(!flagStatus){
        		return false;
        	} */
        	
        	var activityType = $("select[name= 'activityType']").val();
        	if(activityType == '2'){
        		var startDate = $("input[name='startDate']").val();
    			var endDate = $("input[name='endDate']").val();
            	if(startDate == "" || endDate == "" ) return false;
            	
            	// 检验月擂台是否重复
            	checkMonthBidding(startDate, endDate);
            	if(!flagStatus){
            		return false;
            	}
            	
            	// 检查月擂台日期是否合法
            	if(startDate.split('-')[2] != '01' || !isLastDay(endDate)){
            		$.fn.alert("月擂台时间格式必须是某月第一天和某月最后一天");
            		return false;
            	}            	
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
        	<li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">竞价活动配置</a></li>
        	<li class="current"><a title="">新增擂台</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/biddingActivity/doBiddingActivityAdd.do">
            	<input type="hidden" name="companyKey" value="${companyKey}" />
            	<input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
            	<input type="hidden" name="lotteryMoney"/>
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
                                        <input name="activityName" 
                                            class="form-control input-width-larger positive required" autocomplete="off" maxlength="20" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate required preTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"  />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">擂台类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                        <select name="activityType" class="form-control input-width-larger required" tag="validate">
                                        	<option value="">请选择</option>
                                            <option value="1">日擂台</option>
                                            <option value="2">月擂台</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="dedicatedTr">
                                <td class="ab_left"><label class="title">是否酒水专场：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                        <select name="isDedicated" class="form-control input-width-larger required" tag="validate">
                                            <option value="0">否</option>
                                            <option value="1">是</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出产品名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                        <select name="goodsId" class="form-control input-width-larger required" tag="validate">
                                            <option value="">请选择</option>
	                                    	<c:forEach items="${goodsList }" var="item">
	                                    		<option value="${item.goodsId}">${item.goodsName }</option>
	                                    	</c:forEach>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="openingNumberTr">
                                <td class="ab_left"><label id="everyonePeriodLimit" class="title">开场人数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="openingNumber" 
                                            class="form-control input-width-larger required number integer" autocomplete="off" maxlength="5" minVal="0" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="virtualNumberTr">
                                <td class="ab_left"><label id="everyonePeriodLimit" class="title">虚拟开场人数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="virtualNumber" 
                                            class="form-control input-width-larger required number integer" autocomplete="off" maxlength="5" value="0"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">兑出截止日期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="prizeExpireType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定日期</span>
                                        <input name="prizeExpireDate" class="form-control input-width-medium Wdate required"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <input type="radio" class="tab-radio" name="prizeExpireType" value="1" style="float:left; margin-left: 10px !important;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">有效天数</span>
                                        <input name="prizeValidDay" tag="validate" disabled="disabled"
                                            class="form-control input-width-small required number positive" autocomplete="off" maxlength="3" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="vpointsTr">
                                <td class="ab_left"><label id="everyonePeriodLimit" class="title">兑换积分：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="exchangeVpoints" 
                                            class="form-control input-width-larger number positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/biddingActivity/showBiddingActivityList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
