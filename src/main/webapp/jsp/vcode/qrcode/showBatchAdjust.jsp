<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>批次调整</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 批码有效期切换
			$("input[name='validDayRadio']").on("change", function(){
			    if ($(this).val() == '1') {
			    	$("input[name='validDays']").addClass("required");
			    } else {
                    $("input[name='validDays']").val("").removeClass("required").triggerHandler("focus");
                    $("input[name='validDays']").closest("td").find(".validate_tips").removeClass("valid_fail_text");
			    }
			});
		});
		
		function validForm(){

            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            
            // 确认提交
            $.fn.confirm($("input[name=batchNameTips]").val(), function(){
				$("form").submit();            	
            });
		}
		
	</script>
	
	<style>
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
		.validate_tips {
			padding: 8px !important;
		}
		div b{
			color:red;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 活动管理</a></li>
        	<li class="current"><a title="">批次管理</a></li>
        	<li class="current"><a title="">批次调整</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="adjustbatchForm"
            	action="${basePath}/qrcodeBatchInfo/doBatchAdjust.do">
           		<input type="hidden" name="batchKey" value="${batchKey}">
           		<input type="hidden" name="batchNameTips"  value="${batchNameTips}" />
           		<input type="hidden" name="oldBatchName"  value="${batchInfo.batchName}" />
           		<input type="hidden" name="oldVcodeActivityKey" value="${batchInfo.vcodeActivityKey}">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>批次调整</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		
                		<table class="active_board_table">
                		    <c:if test="${fn:length(batchInfoList) == 1}">
	                			<tr>
		                       		<td class="ab_left"><label class="title">修改原批次名称：<span class="required">*</span></label></td>
		                       		<td class="ab_main" colspan="3">
		                       			<div class="content">
		                       				<input name="batchName" tag="validate" value="${batchInfo.batchName}"
		                       					class="form-control required input-width-xlarge" autocomplete="off" maxlength="30" />
		                       				<label class="validate_tips"></label>
		                       			</div>
		                       		</td>
		                       	</tr>
	                       	</c:if>
	                       	<tr>
	                            <td class="ab_left"><label class="title">批码有效期：<span class="required">*</span></label></td>
	                            <td class="ab_main" colspan="3" style="line-height: 30px;">
	                                <div class="content" style="display: flex;">
	                                    <div style="width: 100%;">
	                                        <div style="display: flex;">
	                                            <input type="radio" class="tab-radio" name="validDayRadio" value="0" checked="checked" style="margin-top:8px; cursor: pointer;" />
	                                            <span>激活时间+SKU有效期</span><br>
	                                        </div>
	                                        <div style="display: flex;">
	                                            <input type="radio" class="tab-radio" name="validDayRadio" value="1" style="margin-top:8px; cursor: pointer;" />
	                                            <span>激活时间+&nbsp;</span>
	                                            <input name="validDays" tag="validate"
	                                                class="form-control positive" style="width: 50px;" autocomplete="off" maxlength="5" />
	                                            <span>&nbsp;天</span>
	                                            <label class="validate_tips" ></label>
	                                        </div>
	                                    </div>
	                                </div>
	                            </td>
	                        </tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">调整到新活动：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content vcodeActivityinfo">
	                       				<select class="form-control input-width-xlarge required" id="vcodeActivityKey" name="vcodeActivityKey" tag="validate">
	                       					<option value="">请选择活动</option>
	                       					<c:if test="${not empty activityCogList}">
	                       					<c:forEach items="${activityCogList}" var="activityCog">
	                       					<option value="${activityCog.vcodeActivityKey}">${activityCog.vcodeActivityName}</option>
	                       					</c:forEach>
	                       					</c:if>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<a class="btn btn-blue" data-event="1" onclick="validForm()">保 存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<a class="btn btnReturn btn-radius3"  href="${basePath}/qrcodeBatchInfo/showBatchImportList.do?vjfSessionId=${vjfSessionId}">返 回</a>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
