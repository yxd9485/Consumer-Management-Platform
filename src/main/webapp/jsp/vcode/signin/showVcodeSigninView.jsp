<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加签到活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
		});
		
		function initPage() {

            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
		}
		
	</script>
	
	<style>
		.white {
			color: white;
		}
		.blocker {
			float: left;
			vertical-align: middle;
			margin-right: 8px;
			margin-top: 8px;
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
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 营销模板管理</a></li>
            <li class="current"><a title="">万能签到</a></li>
        	<li class="current"><a title="">查看活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeSignin/doVcodeSigninEdit.do">
            	<input type="hidden" name="activityKey" value="${signinCog.activityKey}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">${signinCog.activityName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">签到周期：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:choose>
	                       					<c:when test="${signinCog.periodType == '1'}">月签到</c:when>
	                       					<c:otherwise>周签到</c:otherwise>
	                       				</c:choose>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                       					<span class="blocker">从</span>
                       					<span class="blocker en-larger">${signinCog.startDate}</span>
                       					<span class="blocker en-larger">到</span>
                       					<span class="blocker en-larger">${signinCog.endDate}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">扫码限制次数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">${signinCog.limitNum}</span>
	                       				<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">筛选出”前一签到周期，扫码次数小于等于该数值的用户”参与本次签到活动</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">可疑用户是否参与：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="doubtRebateFlag" style="float: left; margin-top:9px" type="checkbox" value="1" disabled="disabled"
	                       					<c:if test="${signinCog.doubtRebateFlag == '1'}"> checked</c:if>  />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">SKU关系：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:choose>
	                       					<c:when test="${signinCog.skuRelationType == '1'}">或者</c:when>
	                       					<c:when test="${signinCog.skuRelationType == '2'}">混合</c:when>
	                       					<c:otherwise>并且</c:otherwise>
	                       				</c:choose>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动说明：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<textarea rows="5" readonly="readonly">${signinCog.activityDesc}</textarea>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>Sku限制关系配置</h4>
                    </div>
                	<div id="skuList" class="widget-content panel no-padding">
	                	<c:if test="${signinCog.skuRelationType == '2'}">
	                		<table id="skuTable" class="active_board_table" style="border:1px solid green;">
		                       	<tr>
		                       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content skuInfo">
		                       				<div style="position:relative; display: inline-block; float: left;" >
		                       					<c:if test="${!empty skuList}">
			                       					<c:forEach items="${skuList}" var="sku">
			                       						<c:if test="${fn:contains(signinSkuCogLst[0].skuKey, sku.skuKey) }">${sku.skuName}</c:if></br>
			                       					</c:forEach>
		                       					</c:if>
	                       					</div>
		                       			</div>
		                       		</td>
		                       	</tr>
	                			<tr>
		                       		<td class="ab_left"><label class="title">签到类型：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<c:choose>
		                       					<c:when test="${signinSkuCogLst[0].signType == '1' }">次</c:when>
		                       					<c:otherwise>天</c:otherwise>
		                       				</c:choose>
		                       			</div>
		                       		</td>
		                       	</tr>
	                			<tr>
		                       		<td class="ab_left"><label class="title">签到数值：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
	                       					<span class="blocker">${signinSkuCogLst[0].signNum}</span>
		                       			</div>
		                       		</td>
		                       	</tr>
	                			<tr>
		                       		<td class="ab_left"><label class="title">是否连续：<span class="white">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="continueFlag" type="checkbox" style="float: left; margin-top:9px" value="1" disabled="disabled"
		                       				<c:if test="${signinCog.skuRelationType == '2' and signinSkuCogLst[0].continueFlag == '1'}">checked</c:if>/>
		                       			</div>
		                       		</td>
		                       	</tr>
	                		</table>
                		</c:if>
                		<c:if test="${signinCog.skuRelationType != '2' }">
                			<c:forEach items="${signinSkuCogLst}" var="skuCogItem" varStatus="status">
	                			<table id="skuTable" class="active_board_table" style="border:1px solid green;">
							       	<tr>
							       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content skuInfo">
						       					<c:if test="${!empty skuList}">
							       					<c:forEach items="${skuList}" var="sku">
							       						<option <c:if test="${skuCogItem.skuKey == sku.skuKey}">${sku.skuName}</c:if>>
							       					</c:forEach>
						       					</c:if>
							       			</div>
							       		</td>
							       	</tr>
									<tr>
							       		<td class="ab_left"><label class="title">签到类型：<span class="required">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content">
							       				<c:choose>
							       					<c:when test="${skuCogItem.signType == '1'}">次</c:when>
							       					<c:otherwise>天</c:otherwise>
							       				</c:choose>
							       			</div>
							       		</td>
							       	</tr>
									<tr>
							       		<td class="ab_left"><label class="title">签到数值：<span class="required">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content">
	                       						<span class="blocker">${skuCogItem.signNum}</span>
							       			</div>
							       		</td>
							       	</tr>
									<tr>
							       		<td class="ab_left"><label class="title">是否连续：<span class="white">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content">
							       				<input name="continueFlag" type="checkbox" style="float: left; margin-top:9px" value="${status.index}" disabled="disabled"
							       				<c:if test="${skuCogItem.continueFlag == '1' }">checked</c:if> />
							       				<label class="validate_tips"></label>
							       			</div>
							       		</td>
							       	</tr>
								</table>
                			</c:forEach>
                		</c:if>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/vcodeSignin/showVcodeSigninList.do">返 回</button>
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
