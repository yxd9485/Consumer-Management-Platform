<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
            
		});
	</script>
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
        	<li class="current"><a> 活动管理</a></li>
        	<li class="current"><a title="">
		        <c:if test="${tabsFlag eq '1'}">积分配置任务</c:if>
		        <c:if test="${tabsFlag eq '2'}">每日签到</c:if>
		        <c:if test="${tabsFlag eq '3'}">分享送积分</c:if>
	        </a></li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
	   &nbsp&nbsp&nbsp&nbsp     <a href="<%=cpath%>/integral/showTaskConfig.do?vjfSessionId=${vjfSessionId}"
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">积分配置任务</a>
	        <a href="<%=cpath%>/integral/showSignInEdit.do?vjfSessionId=${vjfSessionId}"
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">每日签到</a>
	        <a href="<%=cpath%>/integral/showShareEdit.do?vjfSessionId=${vjfSessionId}"
	             class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">分享送积分</a>
            <a href="<%=cpath%>/integral/showVipDailyList.do?vjfSessionId=${vjfSessionId}"
               class="btn <c:if test="${tabsFlag eq '4'}">btn-blue</c:if>">会员日常任务</a>
	    </div>
    </div>
        <div>
            <c:if test="${tabsFlag eq '1'}"><jsp:include page="taskConfig.jsp"></jsp:include></c:if>
            <c:if test="${tabsFlag eq '2'}"><jsp:include page="signIn.jsp"></jsp:include></c:if>
            <c:if test="${tabsFlag eq '3'}"><jsp:include page="share.jsp"></jsp:include></c:if>
            <c:if test="${tabsFlag eq '4'}"><jsp:include page="showVipDailyList.jsp"></jsp:include></c:if>
        </div>
    </div>
  </body>
</html>
