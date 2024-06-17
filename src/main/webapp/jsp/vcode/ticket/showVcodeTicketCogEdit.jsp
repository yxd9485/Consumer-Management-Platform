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
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
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
            
            // 保存
			$(".btnSave").click(function(){
				var flag = validForm();
				if (flag) {
					var url = $(this).attr("url");
					$.fn.confirm("确认发布？", function(){
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			return true;
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
        	<li class="current"><a> 虚拟券管理</a></li>
        	<li class="current"><a title="">修改虚拟券</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vcodeTicketCog/doVcodeTicketCogEdit.do">
            	<input type="hidden" name="queryParam" value="${queryParam}" />
            	<input type="hidden" name="pageParam" value="${pageParam}" />
            	<input type="hidden" name="prizeType" value="${prizeType}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">虚拟券编码：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content">
	                       				<span class="blocker en-larger">${prizeType}</span>
									</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">虚拟券类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content">
		                       			<input type="radio" class="tab-radio" name="ticketType" <c:if test="${ticketCog.ticketType == '0' }">checked="checked" </c:if> value="0" style="float:left;" disabled="disabled"/>
	                       				<span class="blocker en-larger" style="padding-right: 8px;">链接</span>
		                       			<input type="radio" class="tab-radio" name="ticketType" <c:if test="${ticketCog.ticketType == '1' }">checked="checked" </c:if> value="1" style="float:left; margin-left: 5px;"  disabled="disabled"/>
	                       				<span class="blocker en-larger">券码</span>
									</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">虚拟券名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="ticketName" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="100" value="${ticketCog.ticketName}" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">推送消息标题：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="pushMsgTitle" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="100" value="${ticketCog.pushMsgTitle}"  />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">有效日期：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="startDate" class="form-control input-width-medium Wdate preTime" value="${ticketCog.startDate}"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-medium Wdate sufTime" value="${ticketCog.endDate}"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">券详情URL：<span class="required white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="detailUrl" tag="validate" 
	                       					class="form-control required" autocomplete="off" maxlength="200" value="${ticketCog.detailUrl}" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">虚拟券说明：<span class="white">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                        	<textarea rows="5" class="form-control required" autocomplete="off" id="ticketDesc" name="ticketDesc" >${ticketCog.ticketDesc}</textarea>
                                       		<label class="validate_tips"></label>
                        				</div>
                                    </div>
                                </td>
                            </tr>
                			<tr <c:if test="${ticketCog.ticketType != '1' }">style="display: none;"</c:if> >
	                       		<td class="ab_left"><label class="title">导入券码方式：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content">
		                       			<input type="radio" class="tab-radio" name="importType" checked="checked" value="0" style="float:left;" />
	                       				<span class="blocker en-larger" style="padding-right: 8px;" title="将本次导入的券码追加到券码池">追加</span>
		                       			<input type="radio" class="tab-radio" name="importType" value="1" style="float:left; margin-left: 5px;" />
	                       				<span class="blocker en-larger" title="清空券码池后再导入本次券码">清空</span>
									</div>
	                       		</td>
	                       	</tr>
                			<tr <c:if test="${ticketCog.ticketType != '1' }">style="display: none;"</c:if> >
                        		<td class="ab_left"><label class="title mart5">导入券码：<span class="required">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<input type="file" class="import-file" name="filePath" single />
                        			</div>
                        		</td>
                        	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vcodeTicketCog/showVcodeTicketCogList.do">返 回</button>
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
