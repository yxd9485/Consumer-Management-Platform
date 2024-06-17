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
	<script type="text/javascript" src="${basePath}/plugins/jquery.imgareaselect-0.9.10/scripts/ajaxfileupload.js"></script> 
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#updatePackInfoForm"));
			
			//保存
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
					var flag = validForm();
					if(flag) {
						return true;
					} else {
						return false;
					}
				}
			});
			//文件导入
			/* $("div .import-file").change(function(){
				if($(this).val()==""){  
	               return false;  
        	  	}  
			
				$.ajaxFileUpload({
					url:"${basePath}/qrcodeBatchInfo/importFile.do",
                    secureuri:false,
                    fileElementId:'filePath',
                    dataType:'JSON',           
                    beforeSend:appendVjfSessionId,
                    success: function(data){
                    	if(data){
	                    	var startIndex=data.indexOf("{"),
	                    	    endIndex=data.indexOf("}"),
	                    	    jsonStr=data.substring(startIndex,endIndex+1),
	                    		jsonData=$.parseJSON(jsonStr);
	                    	//赋值导入包吗号数
	                    	if(jsonData.packNumStr==''){
		                    	$("b.pack_num").text(jsonData.count);
	                    	}else{
	                    		$("b.pack_num").text(jsonData.count+"个");
	                    	}
		                    $("input[name=packCodeArr]").val(jsonData.packNumStr);
                    	}
                    },
                    error:function(data, status, e){
                        alert("导入文件失败！");  
                    }  
                });  
			}); */
			
			//文件格式下载
			$("a.download").click(function(){
				var url = "${basePath}/qrcodeBatchInfo/downloadFileFormat.do";
				$("form").attr("action", url);
				$("form").submit();
				$("form").attr("action", "${basePath}/qrcodeBatchInfo/upatePackInfo.do");
			});
			
		});
		
		//文件导入
		function importFile(fileThis){
			if($(fileThis).val()==""){  
	               return false;  
     	  	}  
			
				$.ajaxFileUpload({
					url:"${basePath}/qrcodeBatchInfo/importFile.do",
                 secureuri:false,
                 fileElementId:'filePath',
                 dataType:'JSON',           
                 beforeSend:appendVjfSessionId,
                    success: function(data){
                 	if(data){
	                    	var startIndex=data.indexOf("{"),
	                    	    endIndex=data.indexOf("}"),
	                    	    jsonStr=data.substring(startIndex,endIndex+1),
	                    		jsonData=$.parseJSON(jsonStr);
	                    	//赋值导入包吗号数
	                    	if(jsonData.packNumStr==''){
		                    	$("b.pack_num").text(jsonData.count);
	                    	}else{
	                    		$("b.pack_num").text(jsonData.count+"个");
	                    	}
		                    $("input[name=packCodeArr]").val(jsonData.packNumStr);
                 	}
                 },
                 error:function(data, status, e){
                     alert("导入文件失败！");  
                 }  
             }); 
		}
		
		function validForm(){
			var startDate = $("input[name=startDate]").val();
			var endDate = $("input[name=endDate]").val();
			if($("input[name=packCodeArr]").val()==''){
				alert("导入的包码号数为0或者未上传文件！");
				return false;
			}
			if($("input[name=startDate]").val() == ''){
				alert("开始时间不能为空!");
				return false;
			}
			if($("input[name=endDate]").val() == ''){
				alert("结束时间不能为空!");
				return false;
			}
			if(Date.parse(endDate)<Date.parse(startDate)){
				return false;
			}
			/* var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			} */
			return true;
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
		.ex-larger {
			margin-right: 8px;
		}
		.validate_tips {
			padding: 8px !important;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><i class="icon-home"></i><a> 活动管理</a></li>
        	<li class="current"><a title="">修改包码</a></li>
        </ul>
    </div>
     <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="updatePackInfoForm"
            	action="${basePath}/qrcodeBatchInfo/upatePackInfo.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>修改包码时间</h4>
            		</div>
                	<div class="widget-content no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">导入修改的包码号文件：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input type="file" class="import-file blocker en-larger" name="filePath" id="filePath" onchange="importFile($(this))"/>
	                       				<a href="#" class="btn download btn-info blocker en-larger btn-blue">文件格式下载</a>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">导入包码号数：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input type="hidden" name="vcodeActivityKey" value="${batchInfo.vcodeActivityKey}">
	                       				<input type="hidden" name="batchKey" value="${packInfo.packKey }">
	                       				<input type="hidden" name="packCodeArr">
	                       				<b class="pack_num blocker en-larger"></b>
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
                		</table>
                	</div>
                	<div class="active_table_submit mili-cent mart10">
			            <div class="button_place">
					    	<button class="btn btn-primary btnSave" data-event="1">保 存</button>
					    	<button class="btn btnReturn" data-event="0" data-url="${basePath}/qrcodeBatchInfo/${batchInfo.vcodeActivityKey}/showBatchInfoList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
