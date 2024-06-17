<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String cpath = request.getContextPath(); 
	String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
	
	String activityVersion1 = PropertiesUtil.getPropertyValue("activityVersion_type1");
	activityVersion1 = new String(activityVersion1.getBytes("ISO-8859-1"), "UTF-8");
	String activityVersion2 = PropertiesUtil.getPropertyValue("activityVersion_type2");
	if (StringUtils.isNotBlank(activityVersion2)) {
	    activityVersion2 = new String(activityVersion2.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion3 = PropertiesUtil.getPropertyValue("activityVersion_type3");
	if (StringUtils.isNotBlank(activityVersion3)) {
	    activityVersion3 = new String(activityVersion3.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion4 = PropertiesUtil.getPropertyValue("activityVersion_type4");
	if (StringUtils.isNotBlank(activityVersion4)) {
	    activityVersion4 = new String(activityVersion4.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion5 = PropertiesUtil.getPropertyValue("activityVersion_type5");
	if (StringUtils.isNotBlank(activityVersion5)) {
	    activityVersion5 = new String(activityVersion5.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion6 = PropertiesUtil.getPropertyValue("activityVersion_type6");
	if (StringUtils.isNotBlank(activityVersion6)) {
	    activityVersion6 = new String(activityVersion6.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion7 = PropertiesUtil.getPropertyValue("activityVersion_type7");
	if (StringUtils.isNotBlank(activityVersion7)) {
	    activityVersion7 = new String(activityVersion7.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion8 = PropertiesUtil.getPropertyValue("activityVersion_type8");
	if (StringUtils.isNotBlank(activityVersion8)) {
	    activityVersion8 = new String(activityVersion8.getBytes("ISO-8859-1"), "UTF-8");
	}
	String activityVersion9 = PropertiesUtil.getPropertyValue("activityVersion_type9");
	if (StringUtils.isNotBlank(activityVersion9)) {
	    activityVersion9 = new String(activityVersion9.getBytes("ISO-8859-1"), "UTF-8");
	}
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="<%=cpath%>/assets/js/plugins/zonesheets.js?v=3"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/custom/loadSku.js"></script>
	
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
        	<c:if test="${menuType eq 'activity'}">
        		<li class="current"><a> 活动管理</a></li>
        		<li class="current">
                <c:choose>
                    <c:when test="${activityType == '1'}">一罐一码活动</c:when>
                    <c:when test="${activityType == '2'}">一瓶一码活动</c:when>
                </c:choose>
            </li>
        	</c:if>
        	<c:if test="${menuType eq 'rule'}">
        		<li class="current"><a> 规则管理</a></li>
        		<li class="current">
                <c:choose>
                    <c:when test="${activityType == '1'}">一罐一码规则</c:when>
                    <c:when test="${activityType == '2'}">一瓶一码规则</c:when>
                </c:choose>
            </li>
        	</c:if>
        	<c:if test="${menuType eq 'qrcode'}">
        		<li class="current"><a> 码源管理</a></li>
        		<li class="current">
                <c:choose>
                    <c:when test="${activityType == '1'}">一罐一码码源</c:when>
                    <c:when test="${activityType == '2'}">一瓶一码码源</c:when>
                </c:choose>
            </li>
        	</c:if>
        	<li class="current"><a title="">查看活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeActivity/showVcodeActivityList.do?menuType=activity">
            	<input type="hidden" name="queryParam" value="${queryParam}" />
            	<input type="hidden" name="pageParam" value="${pageParam}" />
            	<input type="hidden" name="activityType" value="${activityCog.activityType}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">${activityCog.vcodeActivityName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                       					<span class="blocker">从</span>
                       					<span class="blocker en-larger">${activityCog.startDate}</span>
                       					<span class="blocker en-larger">到</span>
                       					<span class="blocker en-larger">${activityCog.endDate}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">SKU：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content skuInfo">
	                       				<span class="blocker">${activityCog.skuName}</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	 <tr>
                                <td class="ab_left"><label class="title">活动版本：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="">
                                            <c:if test="${activityCog.activityVersion == '1'}"><%=activityVersion1%></c:if>
                                            <c:if test="${activityCog.activityVersion == '2'}"><%=activityVersion2%></c:if>
                                            <c:if test="${activityCog.activityVersion == '3'}"><%=activityVersion3%></c:if>
                                            <c:if test="${activityCog.activityVersion == '4'}"><%=activityVersion4%></c:if>
                                            <c:if test="${activityCog.activityVersion == '5'}"><%=activityVersion5%></c:if>
                                            <c:if test="${activityCog.activityVersion == '6'}"><%=activityVersion6%></c:if>
                                            <c:if test="${activityCog.activityVersion == '7'}"><%=activityVersion7%></c:if>
                                            <c:if test="${activityCog.activityVersion == '8'}"><%=activityVersion8%></c:if>
                                            <c:if test="${activityCog.activityVersion == '9'}"><%=activityVersion9%></c:if>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<c:if test="${activityCog.firstRebateFlag == '1'}">
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>扫码获得积分规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">首次扫码：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<span class="blocker">从</span>
	                       				<span class="blocker en-larger">${activityCog.firstRebateMin}</span>
	                       				<span class="blocker en-larger">至</span>
	                       				<span class="blocker en-larger">${activityCog.firstRebateMax}</span>
	                       				<span class="blocker en-larger">(元)</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	</c:if>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>可疑账户规则(满足一个即为可疑)</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则一：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">同一分钟扫码次数大于或等于</span>
	                       				<span class="blocker en-larger">${activityCog.sameMinuteRestrict}</span>
	                       				<span class="blocker en-larger">次</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">规则二：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">同一天扫码次数大于或等于</span>
	                       				<span class="blocker en-larger">${activityCog.sameDayRestrict}</span>
	                       				<span class="blocker en-larger">次</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">规则三：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">历史累计扫码次数大于或等于</span>
	                       				<span class="blocker en-larger">${activityCog.historyTimesRestrict}</span>
	                       				<span class="blocker en-larger">次</span>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr class="doubt-rule">
	                       		<td class="ab_left"><label class="title">可疑账户&nbsp;&nbsp;&nbsp;&nbsp;<br/>积分规则：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       		<br/>
	                       			<div class="content">
	                       				<c:if test="${activityCog.doubtRuleType == '1'}">
	                       				<span class="blocker">系数形式</span>
	                       				<span class="blocker en-larger">${activityCog.doubtRuleCoe} %</span>
	                       				</c:if>
	                       				<c:if test="${activityCog.doubtRuleType == '2'}">
	                       				<span class="blocker">区间形式</span>
	                       				<span class="blocker en-larger">从</span>
	                       				<span class="blocker en-larger">${activityCog.doubtRuleRangeMin}</span>
	                       				<span class="blocker en-larger">至</span>
	                       				<span class="blocker en-larger">${activityCog.doubtRuleRangeMax}</span>
	                       				<span class="blocker en-larger">(元)</span>
	                       				</c:if>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr class="danger-rule" style="display: none;">
	                       		<td class="ab_left"><label class="title">黑名单账户&nbsp;&nbsp;<br/>积分规则：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<c:if test="${activityCog.dangerRuleType == '1'}">
	                       				<span class="blocker">系数形式</span>
	                       				<span class="blocker en-larger">${activityCog.dangerRuleCoe}</span>
	                       				</c:if>
	                       				<c:if test="${activityCog.dangerRuleType == '2'}">
	                       				<span class="blocker">区间形式</span>
	                       				<span class="blocker en-larger">从</span>
	                       				<span class="blocker en-larger">${activityCog.dangerRuleRangeMin}</span>
	                       				<span class="blocker en-larger">至</span>
	                       				<span class="blocker en-larger">${activityCog.dangerRuleRangeMax}</span>
	                       				<span class="blocker en-larger">(积分)</span>
	                       				</c:if>
	                       			</div>
	                       		</td>
	                       	</tr>
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
