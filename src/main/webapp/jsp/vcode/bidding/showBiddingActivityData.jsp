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
  	<form class="listForm" method="post"></form>
    <div class="container" style="padding: 0px;">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
				<!-- 活动信息 -->
                <div class="widget-header">
                    <h4><i class="iconfont icon-xinxi"></i>基础活动信息</h4>
                </div>
                <div class="widget-content panel no-padding">
	                <table class="active_board_table">
					<!-- 扫码活动 -->
                        <tr>
                            <td class="ab_left" style="width: 4% !important;"><label class="title">活动名称：</label></td>
                            <td class="ab_main" style="width: 7% !important;">
                                <div class="content">
                                    <span>${biddingActivity.activityName}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 4% !important;"><label class="title">奖品名称：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 7% !important;">
                                <div class="content">
                                    <span>${biddingActivity.goodsName}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 4% !important;"><label class="title">活动时间：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                               		<span>${biddingActivity.startDate}</span>至
                               		<span>${biddingActivity.endDate}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 4% !important;"><label class="title">酒水专场：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 7% !important;">
                                <div class="content">
                                    <c:choose>
				        				<c:when test="${biddingActivity.isDedicated eq '1'}"><span>是</span></c:when>
				        				<c:otherwise>否</c:otherwise>
				        			</c:choose>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        	<td class="ab_left" style="width: 4% !important;"><label class="title">擂台类型：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                	<c:choose>
                                		<c:when test="${biddingActivity.activityType eq '1'}"><span>日擂台</span></c:when>
                                		<c:when test="${biddingActivity.activityType eq '2'}"><span>月擂台</span></c:when>
                                		<c:otherwise>-</c:otherwise>
                                	</c:choose>
                                </div>
                            </td>
                            
                            <td class="ab_left" style="width: 4% !important;"><label class="title">开场人数：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                	<c:choose>
                                		<c:when test="${biddingActivity.activityType eq '1'}"><span>${biddingActivity.openingNumber}</span></c:when>
                                		<c:otherwise><span>-</span></c:otherwise>
                                	</c:choose>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 4% !important;"><label class="title">虚拟人数：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                	<c:choose>
                                		<c:when test="${biddingActivity.activityType eq '1'}"><span>${biddingActivity.virtualNumber}</span></c:when>
                                		<c:otherwise><span>-</span></c:otherwise>
                                	</c:choose>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 4% !important;"><label class="title">兑换积分：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                               		<c:choose>
                                		<c:when test="${biddingActivity.activityType eq '2'}"><span>${biddingActivity.exchangeVpoints}</span></c:when>
                                		<c:otherwise><span>-</span></c:otherwise>
                                	</c:choose>
                                </div>
                            </td>
                        </tr>
	                </table>
                </div>
                
            </div>
            <div class="row">
            	<div class="widget box" id="tab_1_1">
		    		<iframe id="ruleFrame" src="<%=cpath%>/biddingPeriods/showBiddingPeriodsList.do?vjfSessionId=${vjfSessionId}&activityKey=${biddingActivity.activityKey}" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
		    	</div>
		    </div>
        </div>
    </div>
    </div>
  </body>
</html>
