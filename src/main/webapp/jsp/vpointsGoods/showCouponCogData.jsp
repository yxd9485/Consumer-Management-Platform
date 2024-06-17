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
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
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
	    
	    $(function(){
            // 提示
            $("[data-toggle='popover']").popover();
            $('body').on('click', function(event) { 
                if ($("div.popover").size() > 0 
                        && $(event.target).closest("[data-toggle]").size() == 0 
                        && !($(event.target).is(".popover") || $(event.target).closest("div.popover").size() == 1)) {
                    $("[data-toggle='popover']").popover('toggle');
                }
            });
            
	    	$("a.issueData").off();
		    $("a.issueData").on("click",function(){
		    	$("a.issueData").addClass("btn-red");
		    	$("a.verificationData").removeClass("btn-red");
		    	
		    	$("#ruleFrame").attr("src","<%=cpath%>/couponReceiveRecord/showReceiveRecordList.do?vjfSessionId=${vjfSessionId}&tabsIndex=0&couponKey=${couponKey}");
		    });
		    
		    $("a.verificationData").off();
		    $("a.verificationData").on("click",function(){
		    	$("a.issueData").removeClass("btn-red");
		    	$("a.verificationData").addClass("btn-red");
		    	
		    	$("#ruleFrame").attr("src","<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=5&couponKey=${couponKey}");
		    });
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
  
      <form class="listForm" method="post"></form>
    <div class="container" style="padding: 0px;">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
				<!-- 活动信息 -->
                <div class="widget-header">
                    <h4><i class="iconfont icon-xinxi"></i>优惠券信息</h4>
                    <span class="marl10" title="温馨提示" data-html="true" 
                            data-container="body" data-toggle="popover" data-placement="auto" 
                            data-content="
                            <b>1</b>.汇总数据不包含待支付、兑换失败、订单已关闭、待发货撤单、退货已完成订单。</br>
                            <b>2</b>.购买商品件数：使用该优惠券购买的商品数量。</br>
                            <b>3</b>.用券总成交额：使用该优惠券的订单付款总金额。</br>
                            <b>4</b>.用券笔单价：用券总成交额 / 使用该优惠券的付款订单数。</br>
                            <b>5</b>.费效比：优惠总金额 / 用券总成交额。</br>
                            <b>6</b>.优惠总金额：使用该优惠券优惠的总金额。">
                        <i class="iconfont icon-tixing" style="color: red; font-size: 20px;"></i>
                    </span>
                </div>
                <div class="widget-content panel no-padding">
	                <table class="active_board_table">
					<!-- 扫码活动 -->
                        <tr>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">优惠券名称：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCog.couponName}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">优惠券类型：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                	<c:choose>
                                		<c:when test="${couponCog.couponType eq '0'}"><span>满减券</span></c:when>
                                		<c:when test="${couponCog.couponType eq '1'}"><span>直减券</span></c:when>
                                		<c:when test="${couponCog.couponType eq '2'}"><span>折扣券</span></c:when>
                                	</c:choose>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">适用类型：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <c:choose>
                                		<c:when test="${couponCog.couponGoodsType eq '0'}"><span>通用券</span></c:when>
                                		<c:when test="${couponCog.couponGoodsType eq '1'}"><span>单品券</span></c:when>
                                	</c:choose>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">使用时间：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCog.startDate}</span>至<span>${couponCog.endDate}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">创建时间：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content" style="width: 150px !important;">
                                    <span>${couponCog.createTime}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 5% !important;"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                            <td class="ab_left" style="width: 5% !important;"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                            <td class="ab_left" style="width: 5% !important;"></td>
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
                            <td class="ab_left" style="width: 8% !important;"><label class="title">发行总量：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.couponNum}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">优惠券领取量：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.couponReceiveNum}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">优惠券核销量：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.useNum}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">独立用户：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.userNum}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">用券总成交额：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content" style="width: 150px !important;">
                                    <span><fmt:formatNumber type="number" value="${couponCollect.totalPay / 100}" pattern="#0.00"/>元</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">用券比单价：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content" style="width: 150px !important;">
                                    <span><fmt:formatNumber type="number" value="${couponCollect.orderPayPrice / 100}" pattern="#0.00"/>元</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">优惠总金额：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span><fmt:formatNumber type="number" value="${couponCollect.totalDiscountsPay / 100}" pattern="#0.00"/>元</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">费效比：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.discountsPayRatio}%</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">用券总成交积分：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.totalVpoints}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">用券比单积分：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.orderVpointsPrice}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">优惠总积分：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.totalDiscountsVpoints}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">积分效比：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content">
                                    <span>${couponCollect.discountsVpointsRatio}%</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left" style="width: 8% !important;"><label class="title">购买商品件数：</label></td>
                            <td class="ab_main" style="width: 17% !important;">
                                <div class="content" style="width: 150px !important;">
                                    <span>${couponCollect.totalNum}</span>
                                </div>
                            </td>
                            <td class="ab_left" style="width: 5% !important;"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                            <td class="ab_left" style="width: 5% !important;"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                            <td class="ab_left" style="width: 5% !important;"></td>
                            <td class="ab_main" style="width: 5% !important;"></td>
                        </tr>
	                </table>
                </div>
            </div>
           <div class="row">
			<div class="col-md-12">
				<a class="btn <c:if test="${tabsIndex eq '0'}">btn-blue</c:if> issueData" style="margin-left: 15px;">发行数据</a>
				<a class="btn <c:if test="${tabsIndex eq '1'}">btn-blue</c:if> verificationData">核销数据</a>
   			</div>
   			<div class="col-md-12">
	    		<iframe id="ruleFrame" src="<%=cpath%>/couponReceiveRecord/showReceiveRecordList.do?vjfSessionId=${vjfSessionId}&tabsIndex=0&couponKey=${couponKey}" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
        	</div>
    	</div>
  </body>
</html>
