<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String cpath = request.getContextPath(); 
	String filePath = PropertiesUtil.getPropertyValue("image_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	
	<script>
		$(function(){

            // 返回
            $(".btnReturn").click(function(){
                $("form").submit();
            });
            
            // 保存
            $(".btnReturnAudit").click(function(){
                var url =  $(this).data("url");
                var tips = "确认" + $(this).text() + "?"
                $.fn.confirm(tips, function(){
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                });
            });
		});
	</script>
	
	<style>
		.white {
			color: white;
		}
		.blocker {
			float: left;
			vertical-align: middle;
			margin-right: 8px;
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
		.approval-table {
			table-layout: fixed;
			width: 98%;
			margin: 8px auto;
			border-top: 1px solid #e1e1e1;
			border-left: 1px solid #e1e1e1;
		}
		.approval-table > thead > tr > th, .approval-table > tbody > tr > td {
			border-right: 1px solid #e1e1e1;
			border-bottom: 1px solid #e1e1e1;
		}
		.approval-table > thead > tr > th {
			padding: 6px;
			text-align: center;
			background-color: #efefef;
		}
		.approval-table > tbody > tr > td {
			padding: 4px;
			padding-left: 1em;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 订单管理</a></li>
            <li class="current"><a title=""> 订单详情</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" 
                action="${basePath}/vpointsExchange/showExchangeExpressList.do">
                        <input type="hidden" name="exchangeId" value="${item.exchangeId}"/>
                        <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>订单详情</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                		  <tbody>
	                			<tr>
		                       		<td class="ab_left"><label class="title">订单编号：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker">${item.exchangeId}</span>
		                       			</div>
		                       		</td>
		                       	</tr>
	                			<tr>
		                       		<td class="ab_left"><label class="title">用户主键：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker">${item.userKey}</span>
		                       			</div>
		                       		</td>
		                       	</tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">兑换品牌：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.brandName}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">商品名称：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.goodsName}</span>
	                                    </div>
	                                </td>
	                            </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">商品编号：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker">${item.goodsClientNo}</span>
                                        </div>
                                    </td>
                                </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">兑换数据：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.exchangeNum}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">购买方式：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">
	                                        <c:if test="${item.exchangeVpoints > 0}">积分兑换</c:if>
	                                        <c:if test="${item.exchangePay > 0}">现金支付</c:if>
	                                        </span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">订单积分金额：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">
	                                        <c:if test="${item.exchangeVpoints > 0}"><span><fmt:formatNumber value="${item.exchangeVpoints}" pattern="#,##0"/>积分</span></c:if>
	                                        <c:if test="${item.exchangePay > 0}"><span><fmt:formatNumber value="${item.exchangePay/100}" pattern="#,#0.00#"/>元</span></c:if>
	                                        </span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">使用权益：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.quanYiType}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">收货人：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.userName}</span>
	                                    </div>
	                                </td>
	                            </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">收货人手机号：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker">${item.phoneNum}</span>
                                        </div>
                                    </td>
                                </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">收货人地址：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.address}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">收货人留言：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.customerMessage}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">发货物流公司：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.expressCompany}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">发货物流编号：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.expressNumber}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">订单时间：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${fn:substring(item.exchangeTime, 0, 19)}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">发货时间：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${fn:substring(item.expressSendTime, 0, 19)}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">确认收货时间：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${fn:substring(item.expressSignTime, 0, 19)}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">退货时间：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${fn:substring(item.goodsReturnTime, 0, 19)}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">退货原因：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${fn:substring(item.goodsReturnReason, 0, 19)}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">退货描述：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.returnComment}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">退货凭证：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                    	<c:forEach items="${fn:split(item.returnImageUrl,',')}" var="url">
	                                    		<a href="${url}" target="_blank">
		                                        	<img width="50" height="100" src="${url}" />
		                                        </a>
	                                    	</c:forEach>
	                                    </div>
	                                </td>
	                            </tr>
	                            
	                            
	                            
                                <tr>
                                    <td class="ab_left"><label class="title">退货物流公司：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker">${item.goodsReturnExpressCompany}</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">退货物流编号：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker">${item.goodsReturnExpressNumber}</span>
                                        </div>
                                    </td>
                                </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">订单状态：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${item.orderStatus}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">退货备注：<span class="required">*</span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                    
	                                        <textarea name="goodsReturnAudit" rows="5" tag="validate" <c:if test="${item.exchangeStatus ne '7'}">disabled="disabled"</c:if>
	                                            class="form-control required" autocomplete="off" maxlength="1000" >${item.goodsReturnAudit}</textarea>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
	                       </tbody>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <c:if test="${item.exchangeStatus eq '7'}">
                                <a class="btn btn-blue btnReturnAudit goodsAudit" data-url="${basePath}/vpointsExchange/goodsReturnAudit.do?optType=0">审核通过</a>&nbsp;&nbsp;&nbsp;&nbsp;
                                <a class="btn btn-blue btnReturnAudit goodsRefuse" data-url="${basePath}/vpointsExchange/goodsReturnAudit.do?optType=1">驳回</a>
                            </c:if>
                        </div>
                    </div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
