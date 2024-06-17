<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<% 
    String cpath = request.getContextPath();
    String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
    String pathPrefix = cpath + "/" + imagePathPrx;
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    
    <script>
	    // 定时更新iframe的高度
	    var iframeClock = setInterval("setIframeHeight()", 50);
	    function setIframeHeight() {
	        iframe = document.getElementById('ruleFrame');
	        if (iframe) {
	            var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
	            if (iframeWin.document.body) {
	                // iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
	                iframe.height = iframeWin.document.body.scrollHeight;
	            }
	        }
	    }
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
            <c:if test="${activityType eq '4' }">
        		<li class="current"><a title="">万能签到</a></li>
        	</c:if>
        	<c:if test="${activityType eq '5' }">
        		<li class="current"><a title="">捆绑升级</a></li>
        	</c:if>
        	<c:if test="${activityType eq '6' }">
        		<li class="current"><a title="">一码双奖</a></li>
        	</c:if>
        	<c:if test="${activityType eq '7' }">
        		<li class="current"><a title="">激活红包规则</a></li>
        	</c:if>
            <li class="current"><a> 配置活动规则</a></li>
        </ul>
    </div>
    <div class="row">
        <input type='hidden' id='vjfSessionId' name='vjfSessionId' value='${vjfSessionId}' />
        <div class="col-md-12 tabbable tabbable-custom">
            <iframe id="ruleFrame" src="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vjfSessionId=${vjfSessionId}&activityType=${activityType}&vcodeActivityKey=${vcodeActivityKey}" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
        </div>
    </div>
    </div>
  </body>
</html>
