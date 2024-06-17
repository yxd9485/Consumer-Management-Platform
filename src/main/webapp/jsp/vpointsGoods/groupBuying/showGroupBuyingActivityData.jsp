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
                            <td class="ab_left" style="width: 5% !important;"><label class="title">活动名称：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span>${activityCog.activityName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">拼团商品：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span>${activityCog.goodsName}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">拼团价：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span>${activityCog.groupBuyingPay}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">拼团积分：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span>${activityCog.groupBuyingVpoints}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left" style="width: 5% !important;"><label class="title">创建时间：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content" style="width: 150px !important;">
                                    <span>${activityCog.createTime}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 5% !important;"><label class="title">活动时间：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content" style="width: 150px !important;">
                                    
                                    <c:choose>
                                    	<c:when test="${activityCog.ruleType eq '1' }">
                                    		<span>${activityCog.beginDate}</span>
                                    	</c:when>
                                    	<c:otherwise>
                                    		<span>周${activityCog.beginDate}</span>
                                    	</c:otherwise>
                                    </c:choose>
                                </div>
                            </td>
                            <td class="ab_left"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                            <td class="ab_left"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                        </tr>
	                </table>
                </div>
                
                
               <!-- 活动信息 -->
                <div class="widget-header">
                    <h4><i class="iconfont icon-xinxi"></i>数据汇总</h4>
                </div>
                <div class="widget-content panel no-padding">
	                <table class="active_board_table">
					<!-- 扫码活动 -->
                        <tr>
                            <td class="ab_left" style="width: 5% !important;"><label class="title">总库存：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span>${empty(statistics.goodsRemains) ? '-' : statistics.goodsRemains}</span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">拼团总成交金额：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span><fmt:formatNumber value="${(empty(statistics.totalMoney) ? 0 : statistics.totalMoney) /100}" pattern="#,#0.00#"/></span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">优惠总金额：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span><fmt:formatNumber value="${(empty(statistics.discountsMoney) ? 0 : statistics.discountsMoney) /100}" pattern="#,#0.00#"/></span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">拼团总成交积分：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span><fmt:formatNumber value="${empty(statistics.totalVpoints) ? 0 : statistics.totalVpoints}" pattern="#,##0"/></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left" style="width: 5% !important;"><label class="title">优惠总积分：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span><fmt:formatNumber value="${empty(statistics.discountsVpoints) ? 0 : statistics.discountsVpoints}" pattern="#,##0"/></span>
                                </div>
                            </td>
                             <td class="ab_left" style="width: 5% !important;"><label class="title">成交商品件数：</label></td>
                            <td class="ab_main" style="width: 5% !important;">
                                <div class="content">
                                    <span>${empty(statistics.orderCount) ? 0 : statistics.orderCount}</span>
                                </div>
                            </td>
                            <td class="ab_left"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                            <td class="ab_left"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                        </tr>
	                </table>
                </div>
            </div>
            <div class="row">
            	<div class="widget box" id="tab_1_1">
		    		<iframe id="ruleFrame" src="<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3&groupBuyingActivityKey=${groupBuyingActivityKey}" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
		    	</div>
		    </div>
        </div>
    </div>
    </div>
  </body>
</html>
