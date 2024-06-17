<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%
	String cpath = request.getContextPath(); 
	String filePath = PropertiesUtil.getPropertyValue("image_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>中奖用户名单</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	
	<script>
		$(function(){
			$("button.btnReturn").click(function(){
				$("form").submit();
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
            <li class="current"><a>积分商城</a></li>
            <li class="current"><a title="">中奖用户信息</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" 
                action="${basePath}/vpointsExchange/getPrizeList.do">
            	<input type="hidden" name="queryParam" value="${queryParam}" />
            	<input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>中奖用户信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                		  <tbody>
	                			<tr>
		                       		<td class="ab_left"><label class="title">用户openid：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker">${bean.openid}</span>
		                       			</div>
		                       		</td>
		                       	</tr>
		                       	<tr>
		                       		<td class="ab_left"><label class="title">商城订单号：</label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<span class="blocker">${bean.exchangeId}</span>
		                       			</div>
		                       		</td>
		                       	</tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">昵称：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.nickName}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">商品名称：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.goodsName}</span>
	                                    </div>
	                                </td>
	                            </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">中奖V码：</label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker">${bean.prizeVcode}</span>
                                        </div>
                                    </td>
                                </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">联系人：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.realName}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">联系电话：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.phoneNum}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">身份证号：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.idCard}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">配送地址：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.address}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">中奖时间：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.earnTime}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">中奖SKU：</label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <span class="blocker">${bean.skuName}</span>
	                                    </div>
	                                </td>
	                            </tr>
	                       </tbody>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btnReturn btn-radius3" data-event="0">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body">
      				<h6></h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
    </div>
  </body>
</html>
