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
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	
	<script>
		
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
            
            $("[data-toggle='popover']").popover();
            $('body').on('click', function(event) { 
                if ($("div.popover").size() > 0 
                        && $(event.target).closest("[data-toggle]").size() == 0 
                        && !($(event.target).is(".popover") || $(event.target).closest("div.popover").size() == 1)) {
                    $("[data-toggle='popover']").popover('toggle');
                }
            });
			
			// 初始化功能
			initPage();
			
			// 增加活动下拉框
            $("form").on("click", "#addActivity", function() {
            	var idx = $("td.activity div").index($(this).parent("div.activity"));
                if (idx == 0) {
                    var count = $("td.activity div").length;
                    if(count < 10){
                        var $activityCopy = $("div.activity").eq(0).clone(true);
                        $("td.activity").append($activityCopy);
                        $activityCopy.find("#addActivity").text("删除");
                        var $activity = $("td.activity div.activity:last").find("input[name='vcodeActivityKey']");
                        $activity.attr("id", "vcodeActivityKey" + count).val("");
                    }
                } else {
                    $(this).parent("div.activity").remove();
                }
            });
            
            // 增加阶梯规则
            $("form").on("click", "#addLadderItem", function() {
                var idx = $("td.ladderItem div").index($(this).parent("div.ladderItem"));
                if (idx == 0) {
                    if($("td.ladderItem div").length < 10){
                        var $timeCopy = $("div.ladderItem").eq(0).clone();
                        $("td.ladderItem").append($timeCopy);
                        $timeCopy.find("#addLadderItem").text("删除");   
                        $("td.ladderItem div.ladderItem:last").find("input").val("");
                    }
                } else {
                    $(this).parent("div.ladderItem").remove();
                }
                
            });
            
            // 检验名称是否重复
            $("input[name='taoName']").on("blur",function(){
            	var taoName = $("input[name='taoName']").val();
            	if(taoName == "") return;
            	checkName(taoName);
            });
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
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(taoName){
			$.ajax({
				url : "${basePath}/taoEasterEgg/checkBussionName.do",
			    data:{
			    		"infoKey":"",
			    		"bussionName":taoName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("规则名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}

		function validForm() {
			var returnFlag = false;
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 检验名称是否重复
			var taoName = $("input[name='taoName']").val();
        	if(taoName == "") return false;
        	checkName(taoName);
        	if(!flagStatus){
        		return false;
        	}
			
			// 组装活动KEY
			var activityAry = "";
			$("td.activity div.activity").each(function(i){
            	var $activity = $(this).find("select[name='vcodeActivityKey']");
            	if($activity.val() == ""){
            		$.fn.alert("指定活动不能为空!");
            		returnFlag = true;
            		return false; // 相当于break
            	}
            	if(activityAry.indexOf($activity.val()) == -1){
	            	activityAry += $activity.val() + ",";
            	}
            });

			if(returnFlag){
				return false;	
			}
			
			if(activityAry != ""){
				// 判断活动是否可配置
				var activityKeys = activityAry.substring(0, activityAry.length - 1);
				$.ajax({
					url : "${basePath}/taoEasterEgg/checkActivityForTaoEasterEgg.do",
				    data:{
				    		"activityKeys":activityKeys,
				    		"infoKey":"",
				    		"startDate":$("input[name='startDate']").val(),
				    		"endDate":$("input[name='endDate']").val()
				    	},
		            type : "POST",
		            dataType : "json",
		            async : false,
		            beforeSend:appendVjfSessionId,
                    success:  function(data){
		        	   if(data["errMsg"]!=''){
		        		   $.fn.alert(data["errMsg"]);
		        		   returnFlag = true;
						}else{
							returnFlag = false;
							$("input[name='vcodeActivityKeys']").val(activityKeys);
						}
		            }
			  	});
			}
			if(returnFlag){
				return false;	
			}
			
			// 组装多组倍数
            var ladderItemAry = "";
            $("td.ladderItem div.ladderItem").each(function(i){
                var $ladderItem = $(this).find("input[name='ladderItem']");
                var $ladderOrder = $(this).find("input[name='ladderOrder']");
                if($ladderOrder.val() == ""){
                    $.fn.alert("请输入彩蛋口令");
                    returnFlag = true;
                    return false; // 相当于break
                }
                ladderItemAry += $ladderItem.val() + ":" + $ladderOrder.val() + ";";
            });
            if(returnFlag){
                return false;   
            }
            if(ladderItemAry != ""){
                $("input[name='ladderRule']").val(ladderItemAry.substring(0, ladderItemAry.length - 1));
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
            <li class="current"><a> 淘彩蛋规则</a></li>
            <li class="current"><a>新增彩蛋规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/taoEasterEgg/doTaoEasterEggCogAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>彩蛋规则</h4>
                        <span class="marl10" title="温馨提示" data-html="true" 
                                data-container="body" data-toggle="popover" data-placement="auto" 
                                data-content="
                                <b>1</b>.使用前通知技术开启淘彩蛋功能开关;</br> 
                                <b>2</b>.入会口令与阶梯口令皆可配置多个，以半角逗号分隔;</br>
                                <b>3</b>.彩蛋口令规则中的阶梯配置方式与扫码活动中出规则一样;</br>
                                <b>4</b>.实物，优惠券，爆点活动，逢尾活动，逢百活动，秒杀活动不返回淘口令;</br>
                                <b>5</b>.淘彩蛋分为普通彩蛋和入会彩蛋，中出的阶梯口令包含于活动配置中的入会口令时，视为入会口令;</br>
                                <b>6</b>.活动类型分为单个活动及组合活动，单个活动为每个活动每天第N次扫码；组合活动为多个活动每天第N次扫码。">
                            <i class="iconfont icon-tixing" style="color: red; font-size: 20px;"></i>
                        </span>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="taoName" tag="validate" class="form-control required" 
	                       					style="width: 330px;" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<div class="content date" style="margin: 5px 0px; display: flex;">
                                        <input name="startDate" id="startDate" class="Wdate form-control input-width-medium required preTime" 
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium required sufTime" 
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">入会口令：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="taoMemberOrder" tag="validate" class="form-control required" 
                                            style="width: 330px;" autocomplete="off" maxlength="200" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
                               <td class="ab_left"><label class="title">活动类型：<span class="required">*</span></label></td>
                               <td class="ab_main" colspan="3">
                                   <div class="content">
                                       <select name="ladderType" tag="validate" class="form-control input-width-larger required">
                                           <option value="0">单个活动</option>
                                           <option value="1">组合活动</option>
                                       </select>
                                       <label class="validate_tips"></label>
                                   </div>
                               </td>
                           </tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">联接活动：<span class="required">*</span></label></td>
	                       		<td class="ab_main activity">
	                       			<input type="hidden" name="vcodeActivityKeys" />
	                       			<div class="content activity" style="margin: 5px 0px; display: flex;">
	                       				<select class="form-control input-width-larger" id="vcodeActivityKey" name="vcodeActivityKey">
	                       					<option value="">请选择</option>
	                       					<c:forEach items="${activityList }" var="activity">
	                       						<option value="${activity.vcodeActivityKey }">${activity.vcodeActivityName }</option>
	                       					</c:forEach>
	                       				</select>
	                       				<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addActivity">新增</label>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <input type="hidden" name="ladderRule" />
                                <td class="ab_left"><label class="title mart5">彩蛋规则：<span class="required">*</span></label></td>
                                <td class="ab_main ladderItem">
                                    <div class="content ladderItem" style="margin: 5px 0px;">
                                         <span class="blocker en-larger">阶梯</span>
                                         <input name="ladderItem" class="form-control input-width-larger" placeholder="为空时为默认口令" autocomplete="off" maxlength="100"/>
                                         <span class="blocker en-larger">&nbsp;&nbsp;彩蛋口令</span>
                                         <input name="ladderOrder" class="form-control input-width-larger" placeholder="口令以逗号分隔" style="width: 40% !important;" autocomplete="off" maxlength="1000" />
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addLadderItem">新增</label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/taoEasterEgg/showTaoEasterEggCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
