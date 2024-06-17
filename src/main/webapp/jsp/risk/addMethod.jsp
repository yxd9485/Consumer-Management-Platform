<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ page import="com.dbt.framework.securityauth.PermissionCode"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加接口</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script>
	var excelStatus=false;
	$(document).ready(function () {
		// 初始化校验控件
		$.runtimeValidate($("form"));
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
					alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
			var handleType=$("input[name='handleType']:checked").val();
			var handleServiceName=document.getElementById("handleServiceName").value;
			
			if(handleType==1&&!handleServiceName){
				alert("bean类型时,处理类名不能为空!");
				return false;
			}
			
			var nameFlag=false;
			var methodName=document.getElementById("methodName").value;
			$.ajax({
				url:'<%=cpath%>/riskMethod/checkName.do?methodName='+methodName,
				async: false,
				type: 'POST',
					beforeSend:appendVjfSessionId,
                    success: function(data){
						if(data=='SUCCESS'){
							nameFlag=true;
						}else{
							nameFlag=false;
						}
					},
					error:function(data){
	           			alert('fail');
	     			 }
			});
			if(!nameFlag){
				alert("接口名称重复");
				return false;
			}
			return true;
		}
		function initPage() {
			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
					var flag = validForm();
					if(flag) {
						if(btnEvent == "2"){
							if(confirm("确认发布？")){
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
        	<li class="current"><a> 风控配置</a></li>
        	<li class="current"><a title=""> 添加接口</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath %>/riskMethod/addMethod.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>接口信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">接口名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content methodName">
	                       				<input name="methodName" id="methodName" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="500" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">接口描述：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content methodDesc">
	                       				<input name="methodDesc" id="methodDesc" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="500" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">接口状态：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content methodStatus">
		                       			<input type="radio" name="methodStatus" name="methodStatus" value="0"  checked="checked" /><label>执行中</label>
		                       			<input type="radio" name="methodStatus" name="methodStatus" value="1"/><label>已暂停</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">留存日志：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content methodStatus">
		                       			<input type="radio" name="logStatus" name="logStatus" value="0"  checked="checked" /><label>留存</label>
		                       			<input type="radio" name="logStatus" name="logStatus" value="1"/><label>不留存</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">接口限流数量：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content limitNum">
	                       				<input name="limitNum" id="limitNum" tag="validate"
	                       					class="form-control number input-width-small required" autocomplete="off" maxlength="10" value="0" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户防并发秒数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content userLimitNum">
	                       				<input name="userLimitNum" id="userLimitNum" tag="validate"
	                       					class="form-control number input-width-small required" autocomplete="off" maxlength="10" value="0" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">业务并发标识<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content userCheckStatus">
		                       			<input type="radio" name="isBusinessFlag" name="userCheckStatus" value="0"  /><label>是</label>
		                       			<input type="radio" name="isBusinessFlag" name="userCheckStatus" value="1" checked="checked" /><label>否</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">校验用户：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content userCheckStatus">
		                       			<input type="radio" name="userCheckStatus" name="userCheckStatus" value="0"  /><label>校验</label>
		                       			<input type="radio" name="userCheckStatus" name="userCheckStatus" value="1" checked="checked" /><label>不校验</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">黑名单用户：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content userBlackCheck">
		                       			<input type="radio" name="userBlackCheck" name="userBlackCheck" value="0"  /><label>校验</label>
		                       			<input type="radio" name="userBlackCheck" name="userBlackCheck" value="1" checked="checked" /><label>不校验</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">可疑用户：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content userSuspiciousCheck">
		                       			<input type="radio" name="userSuspiciousCheck" name="userSuspiciousCheck" value="0"/><label>校验</label>
		                       			<input type="radio" name="userSuspiciousCheck" name="userSuspiciousCheck" value="1" checked="checked" /><label>不校验</label>
		                       		</div>
	                       		</td>
	                       	</tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">请求摘要：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content requestRmk">
	                       				<input name="requestRmk" id="requestRmk" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="500" />
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">请求摘要描述：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content requestRmkMsg">
	                       				<input name="requestRmkMsg" id="requestRmkMsg" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="500" />
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">响应摘要：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content responseRmk">
	                       				<input name="responseRmk" id="responseRmk" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="500" />
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">响应摘要描述：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content responseRmkMsg">
	                       				<input name="responseRmkMsg" id="responseRmkMsg" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="500" />
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
	                       		<td class="ab_left"><label class="title">风控处理类类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content handleType">
		                       			<input type="radio" name="handleType" name="handleType" value="0" checked="checked" /><label>无</label>
		                       			<input type="radio" name="handleType" name="handleType" value="1" /><label>bean类</label>
		                       			<input type="radio" name="handleType" name="handleType" value="2" /><label>GLUE(java)</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">处理类名：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content handleServiceName">
	                       				<input name="handleServiceName" id="handleServiceName" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="50" />
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
                		</table>
                	</div>
                	
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/riskMethod/getMethodList.do">返 回</button>
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
