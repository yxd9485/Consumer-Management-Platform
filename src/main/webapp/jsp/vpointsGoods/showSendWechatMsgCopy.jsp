<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加商品</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script>
		var basePath='<%=cpath%>';
		$(document).ready(function () {
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			initPage();
		});
		
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			

			return true;
		}
		
		function initPage() {
			// 底部按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				// 指定账户
				if(btnEvent == "1"){
					if($("#userKey").val() == ""){
						alert("请填写指定账户KEY");
						return false;
					}
				// 所有用户
				} else {
					var flag = validForm();
					if(flag) {
						if(confirm("确认推送给所有用户吗？")){
							$("#isAll").val("1");
						} else {
							return false;
						}
					} else {
						return false;
					}
					
					$.ajax({
						url : "${basePath}/sendWechatMsg/sendTemplateMsg.do",
						   data:{
							   "title":$("#title").val(),
							   "goodsName":$("#goodsName").val(),
							   "price":$("#price").val(),
							   "time":$("#time").val(),
							   "remark":$("#remark").val(),
							   "isAll":$("#isAll").val(),
							   "vpoints":$("#vpoints").val(),
							   "userKey":$("#userKey").val()
							   },
				           type : "POST",
				           dataType : "json",
				           async : false,
				           beforeSend:appendVjfSessionId,
                    success:  function(data){
				        	   if(data=="SUCCESS"){
									$.fn.alert("消息推送成功");
								}else{
									$.fn.alert("消息推送失败:" + data);
								}
				            }
				  	});
					
					return false;
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
        	<li class="current"><a> 积分商城</a></li>
        	<li class="current"><a title="">消息推送</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form">
            	<input type="hidden" name="isAll" id="isAll" value="0">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>模板品信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">模板名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content title">
	                       				<input name="title" id="title" tag="validate" style="width: 500px;"
	                       					class="form-control required" autocomplete="off" maxlength="20" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content goodsName">
	                       				<input name="goodsName" id="goodsName" tag="validate" style="width: 500px;"
	                       					class="form-control required" autocomplete="off" maxlength="10" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品价格：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="price" id="price" tag="validate" 
	                       					class="form-control required money input-width-small" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">兑换时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				 <input name="time" id="time" class="form-control input-width-medium required Wdate" style="width: 160px !important;"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">备注：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content commendInfo">
                                        <input name="remark" id="remark" tag="validate" style="width: 500px;" 
                                            class="form-control required" autocomplete="off" maxlength="20" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>筛选用户条件</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">账户积分：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
	                       				<input name="vpoints" id="vpoints" tag="validate"  value="0"
	                       					class="form-control required integer input-width-small" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">积分</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">指定账户：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content userKey">
                                        <input name="userKey" id = "userKey" tag="validate" style="width: 500px;" 
                                            class="form-control" autocomplete="off" maxlength="50" />
                                        <label class="validate_tips"></label>
                                    </div>
	                       		</td>
	                       	</tr>
	                      </table>
	                  </div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">指定账户推送</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btn-radius3 btnSave" data-event="2">所有账户推送</button>&nbsp;&nbsp;&nbsp;&nbsp;
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
