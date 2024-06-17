<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#addBatchInfoForm"));
			
			//显示二维码个数
			var packNum="${batchInfo.packAmounts}",
				qrcodeAmounts="${batchInfo.qrcodeAmounts}";
			if($.isNumeric(packNum)&&$.isNumeric(qrcodeAmounts)){
				$(".content b").text(thousandth(packNum * qrcodeAmounts));	
			}
		});
		
		function validForm(){
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}

			var vcodeActivityKey = $("input[name='vcodeActivityKey']").val();
			var startDate = $("input[name='startDate']").val();
			var endDate = $("input[name='endDate']").val();
			
			$.ajax({
				url : "${basePath}/vcodeActivity/judgeActivityTime.do",
				   data:{
					   "vcodeActivityKey":vcodeActivityKey,
					   "startDate":startDate,
					   "endDate":endDate},
		           type : "POST",
		           async : false,
		           dataType : "json",
		           async : false,
		           beforeSend:appendVjfSessionId,
                    success:  function(data){
						if(data=="1"){
							$.fn.confirm("修改的时间不在活动时间范围内，你确定要修改吗？",function(){
								$("form").submit();
							});
						}else{
							$("form").submit();
						}
			        }
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
        	<li class="current"><a>码源管理</a></li>
        	<c:if test="${activityType eq '1' }">
        		<li class="current"><a title="">一罐一码规则</a></li>
        	</c:if>
        	<c:if test="${activityType eq '2' }">
        		<li class="current"><a title="">一瓶一码规则</a></li>
        	</c:if>
        	<li class="current"><a title="">申请二维码</a></li>
        	<li class="current"><a title="">修改批码</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="addBatchInfoForm"
            	action="${basePath}/qrcodeBatchInfo/upateBatchInfo.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>修改批码时间</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<input type="hidden" name="vcodeActivityKey" value="${batchInfo.vcodeActivityKey}">
	                    <input type="hidden" name="batchKey" value="${batchInfo.batchKey }">
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">批码编号：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span>${batchInfo.batchDesc }</span>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       		<tr>
	                       		<td class="ab_left"><label class="title">批码名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="batchName" tag="validate" value="${batchInfo.batchName }"
	                       					class="form-control required input-width-xlarge" autocomplete="off" maxlength="100" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="startDate" class="form-control input-width-normal Wdate required preTime"
                                       		tag="validate" autocomplete="off" value="${batchInfo.startDate }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-normal Wdate required sufTime"
                                       		tag="validate" autocomplete="off" value="${batchInfo.endDate }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">生成包码数：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       		  				${batchInfo.qrcodeAmounts } X ${batchInfo.packAmounts } &nbsp;&nbsp;&nbsp;&nbsp;=<b ></b>
	                       		  			    <samp>&nbsp;个二维码</samp>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<a class="btn btn-blue" data-event="1" onclick="validForm()">保 存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<a class="btn btnReturn btn-radius3" data-event="0" href="${basePath}/qrcodeBatchInfo/${batchInfo.vcodeActivityKey}/showBatchInfoList.do?vjfSessionId=${vjfSessionId}&activityType=${activityType}">返 回</a>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
