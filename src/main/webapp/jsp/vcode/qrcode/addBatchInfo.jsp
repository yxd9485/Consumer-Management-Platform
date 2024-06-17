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
			
			//生成包吗数失去焦点事件
			$("input[name='packAmounts'], input[name='qrcodeAmounts']").blur(function(){
				var qrAmounts=$("input[name=qrcodeAmounts]").val();
				
				var val = $("input[name='packAmounts']").val();
				if($.isNumeric(val)&&$.isNumeric(qrAmounts)){
					$("input[name='packAmounts']").next().text(parseInt(val * qrAmounts));
					$("input[name='packAmounts']").next().next().text(" 个二维码");
				}
			});
		});
		
		function validForm(){
			var validateResult = true,
				batchDesc=$("input[name=batchDesc]"),
				batchName=$("input[name=batchName]"),
				startDate=$("input[name=startDate]"),
				endDate=$("input[name=endDate]"),
				qrcodeAmounts=$("input[name=qrcodeAmounts]"),
				packAmounts=$("input[name=packAmounts]");
			
			batchDesc.next("label").removeClass("valid_fail_text");
			batchDesc.next("label").html("");
			batchName.next("label").removeClass("valid_fail_text");
			batchName.next("label").html("");
			endDate.next("label").removeClass("valid_fail_text");
			endDate.next("label").html("");
			$("samp").next("label").removeClass("valid_fail_text");
			$("samp").next("label").html("");
			if(batchDesc.val()==''){
				batchDesc.next("label").addClass("valid_fail_text");
				batchDesc.next("label").html("批码编号不能为空");
				return false;
			}
			if(batchName.val()==''){
				batchName.next("label").addClass("valid_fail_text");
				batchName.next("label").html("批码名称不能为空");
				return false;
			}
			if(startDate.val()=='' || endDate.val()==''){
				if(endDate.next("label").css("display")=='none'){
					endDate.next("label").css("display","block");
				}
				endDate.next("label").addClass("valid_fail_text");
				endDate.next("label").html("时间不能为空");
				return false;
			}else if(startDate.val()!='' && endDate.val()!=''){
				if(startDate.val() > endDate.val()){
					endDate.next("label").addClass("valid_fail_text");
					endDate.next("label").html("前一个时间不能大于后一个");
					return false;	
				}
			}
			if(qrcodeAmounts.val()==''||packAmounts.val()==''){
				if($("samp").next("label").css("display")=='none'){
					$("samp").next("label").css("display","block");
				}
				$("samp").next("label").addClass("valid_fail_text");
				$("samp").next("label").html("生成包码数不能为空");
				return false;
			}else if(!$.isNumeric(qrcodeAmounts.val())||!$.isNumeric(packAmounts.val())){
				$("samp").next("label").addClass("valid_fail_text");
				$("samp").next("label").html("请输入数字");
				return false;
			}
			else if(!/^[1-9][0-9]*$/.test(qrcodeAmounts.val())||!/^[1-9][0-9]*$/.test(packAmounts.val())){
				$("samp").next("label").addClass("valid_fail_text");
				$("samp").next("label").html("请输入正整数");
				return false;
			}
			
			if(validateResult) {
				var vcodeActivityKey = $("input[name='vcodeActivityKey']").val();
				$.ajax({
					url : "${basePath}/qrcodeBatchInfo/isLegal.do",
					   data:{
						   "vcodeActivityKey":vcodeActivityKey,
						   "startDate":startDate.val(),
						   "endDate":endDate.val(),
						   "batchDesc":batchDesc.val()},
			           type : "POST",
			           dataType : "json",
			           async : false,
			           beforeSend:appendVjfSessionId,
                    success:  function(data){
			        	   if(data=="1"){
								$.fn.alert("批码说明已经存在，请修改后提交");
								validateResult=false;
							}else if(data=="2"){
								$.fn.confirm("修改的时间不在活动时间范围内，你确定要修改吗？",function(){
									$("form").submit();
								});
							}else{
								$("form").submit();
							}
			            }
			  	});
			}
			// return validateResult;
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
        	<li class="current"><a title="">生成二维码</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="addBatchInfoForm"
            	action="${basePath}/qrcodeBatchInfo/addBatchInfo.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>生成二维码</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<input type="hidden" name="vcodeActivityKey" value="${vcodeActivityKey}">
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
	                	<input type="hidden" name="vcodeFlag" value="0">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">批码编号：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="batchDesc" tag="validate"
	                       					class="form-control required input-width-xlarge" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">批码名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="batchName" tag="validate"
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
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-normal Wdate required sufTime"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">生成包码数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="qrcodeAmounts" tag="validate"
	                       					class="form-control required number positive input-width-normal" placeholder="包中二维码数" autocomplete="off" />
	                       		  		<b class="blocker en-larger"> X </b> 
	                       		  		<input name="packAmounts" tag="validate"
	                       					class="form-control required number positive input-width-normal" placeholder="包码数" autocomplete="off" maxlength="4" />
	                       				<b class="blocker en-larger"></b>
	                       				<samp class="blocker en-larger"></samp>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<a class="btn btn-blue" data-event="1" onclick="validForm()">保 存</a>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<a class="btn btnReturn btn-radius3"  href="${basePath}/qrcodeBatchInfo/${vcodeActivityKey}/showBatchInfoList.do?activityType=${activityType}">返 回</a>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
