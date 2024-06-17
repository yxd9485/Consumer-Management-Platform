<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
String cpath = request.getContextPath(); 
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加字典类型</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	<script type="text/javascript" src="<%=cpath %>/assets/js/tableFormValidate.js"></script>
    
	<script>
		$(function(){
			//初始化校验控件
			$.runtimeValidate($("#sample_form_focus"));
		});
		
	</script>
	
  </head>
  
  <body>
  <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 系统管理</a></li>
	        <li class="current">数据字典</li>
	        <li class="current">数据字典·类型</li>
        	<li class="current">添加</li>
        </ul>
    </div>
    <div class="row mart10">
        <div class="col-md-12">
        	<form method="post" class="form-horizontal row-border " id="sample_form_focus" class="post_form"
        		action="<%=cpath %>/sysDicCategoryInfo/addCategory.do" style="margin-top: 1em;" novalidate="novalidate" onsubmit="return $.submitValidate($(this));" >
	            <div class="widget box">
	            	<div class="widget-header"><h4><i class="icon-pencil"></i>字典类型信息</h4></div>
	                <div class="widget-content panel">
						<table class="active_board_table">
			               	<tr>
			               		<td class="ab_left"><label class="title">类型名称：<span class="required">*</span></label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<input name="categoryName" tag="validate"
			               					class="form-control input-width-large required" autocomplete="off" maxlength="100" />
			                              	<label class="validate_tips"></label>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">类型Code：<span class="required">*</span></label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<input name="categoryCode" tag="validate"
			               					class="form-control input-width-large required" autocomplete="off" maxlength="100" />
			                              	<label class="validate_tips"></label>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">是否可编辑：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<input type="radio" name="dicType" value="1" class="form-control" style="width:20px;" checked="checked" /><label class="star_text" style="margin-top:10px;">是</label>
			               				<input type="radio" name="dicType" value="0" class="form-control" style="width:20px;" /><label class="star_text" style="margin-top:10px;">否</label>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">使用平台：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<select name="invoker" id="invoker" class="form-control input-width-normal">
			               					<option value="0">全部</option>
			               					<option value="1">运营</option>
			               					<option value="2">接口</option>
			               				</select>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">类型说明：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<input name="categoryExplain" class="form-control" maxlength="256"/>
			               			</div>
			               		</td>
			               	</tr>
		               </table>
					</div>
		            
					<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-primary btnSave btn-blue" type="submit" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
							<a href="<%=cpath%>/sysDicCategoryInfo/listCategory.do?vjfSessionId=${vjfSessionId}" class="btn btn-radius3">返 回</a>
			            </div>
					</div>
            	</div>
            </form>
        </div>
      </div>
    </div>
  </body>
</html>