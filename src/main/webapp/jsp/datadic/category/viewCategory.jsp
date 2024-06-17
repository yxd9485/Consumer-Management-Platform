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
    <title>查看字典类型</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
  </head>
  
  <body>
  <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><i class="icon-home"></i><a> 系统管理</a></li>
	        <li class="current">数据字典</li>
	        <li class="current">数据字典·类型</li>
        	<li class="current">查看</li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12">
        	<form method="post" class="form-horizontal row-border " id="sample_form_focus" class="post_form"
        		action="<%=cpath %>/sysDicCategoryInfo/editCategory.do" style="margin-top: 1em;" novalidate="novalidate" onsubmit="return $.submitValidate($(this));" >
	            <input type="hidden" name="categoryKey" value="${category.categoryKey }" />
	            <div class="widget box">
	            	<div class="widget-header"><h4><i class="icon-pencil"></i>字典类型信息</h4></div>
	                <div class="widget-content">
						<table class="active_board_table">
			               	<tr>
			               		<td class="ab_left"><label class="title">类型名称：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<p>${category.categoryName }</p>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">类型Code：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			                            <p>${category.categoryCode }</p>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">是否可编辑：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<p>
				               				<c:if test="${category.dicType=='1' }">是</c:if>
				               				<c:if test="${category.dicType=='0' }">否</c:if>
			               				</p>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">使用平台：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<p>
				               				<c:if test="${category.invoker=='0' }">全部</c:if>
				               				<c:if test="${category.invoker=='1' }">运营</c:if>
				               				<c:if test="${category.invoker=='2' }">接口</c:if>
			               				</p>
			               			</div>
			               		</td>
			               	</tr>
			               	
			               	<tr>
			               		<td class="ab_left"><label class="title">类型说明：</label></td>
			               		<td class="ab_main" colspan="3">
			               			<div class="content">
			               				<p>${category.categoryExplain}</p>
			               			</div>
			               		</td>
			               	</tr>
			               	
		               </table>
					</div>
		            
					<div class="active_table_submit mili-cent">
			            <div class="button_place">
							<a href="<%=cpath%>/sysDicCategoryInfo/listCategory.do?vjfSessionId=${vjfSessionId}" class="btn">返 回</a>
			            </div>
					</div>
            	</div>
            </form>
        </div>
      </div>
    </div>
  </body>
</html>