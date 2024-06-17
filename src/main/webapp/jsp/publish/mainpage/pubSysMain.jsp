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
        	<li class="current"><a> 发布系统</a></li>
        	<li class="current"><a title="">
		        <c:if test="${tabsFlag eq '1'}">扫码弹窗</c:if>
		        <c:if test="${tabsFlag eq '2'}">首页轮播图</c:if>
		        <c:if test="${tabsFlag eq '3'}">商城轮播图</c:if>
		        <c:if test="${tabsFlag eq '4'}">活动规则</c:if>
		        <c:if test="${tabsFlag eq '5'}">商城首页视频</c:if>
                <c:if test="${tabsFlag eq '6'}">活动专区</c:if>
	        </a></li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
            <c:choose>
                <c:when test="${roleType == 'ShopOperate'}">
                    &nbsp&nbsp&nbsp&nbsp
                    <a href="<%=cpath%>/adPub/showAdUpList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">扫码弹窗</a>
                    <a href="<%=cpath%>/adPub/showHomeAdList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">首页轮播图</a>
                    <a href="<%=cpath%>/adPub/showShopAdList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">商城轮播图</a>
                    <a href="<%=cpath%>/actRegion/showActRegionList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '6'}">btn-blue</c:if>">活动专区</a>
                </c:when>
                <c:otherwise>
                    &nbsp&nbsp&nbsp&nbsp
                    <a href="<%=cpath%>/adPub/showAdUpList.do?vjfSessionId=${vjfSessionId}"
                                                class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">扫码弹窗</a>
                    <a href="<%=cpath%>/adPub/showHomeAdList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">首页轮播图</a>
                    <a href="<%=cpath%>/adPub/showShopAdList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">商城轮播图</a>
                    <a href="<%=cpath%>/actRule/showActRuleList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '4'}">btn-blue</c:if>">活动规则</a>
                    <a href="<%=cpath%>/actVideo/showVideoList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '5'}">btn-blue</c:if>">商城首页视频</a>
                    <a href="<%=cpath%>/actRegion/showActRegionList.do?vjfSessionId=${vjfSessionId}"
                       class="btn <c:if test="${tabsFlag eq '6'}">btn-blue</c:if>">活动专区</a>
                </c:otherwise>
            </c:choose>

	    </div>
    </div>
    <c:if test="${tabsFlag eq '1'}"><jsp:include page="showAdUpList.jsp"></jsp:include></c:if>
    <c:if test="${tabsFlag eq '2'}"><jsp:include page="showAdHomeList.jsp"></jsp:include></c:if>
    <c:if test="${tabsFlag eq '3'}"><jsp:include page="showAdShopList.jsp"></jsp:include></c:if>
    <c:if test="${tabsFlag eq '4'}"><jsp:include page="showActRuleList.jsp"></jsp:include></c:if>
    <c:if test="${tabsFlag eq '5'}"><jsp:include page="showVideoList.jsp"></jsp:include></c:if>
    <c:if test="${tabsFlag eq '6'}"><jsp:include page="showAdRegionList.jsp"></jsp:include></c:if>
    </div>
    
  </body>
</html>
