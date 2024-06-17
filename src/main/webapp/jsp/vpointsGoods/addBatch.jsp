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
    <title>添加批次</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
<%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
	<script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>
	
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js"></script>
<%-- 	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/showimg.js"></script> --%>
	<script>
	$(document).ready(function () {
		// 初始化校验控件
		$.runtimeValidate($("form"));
		
		initPage();
	});
		 
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			// 页面校验
			var v_flag = true;
			$(".validate_tips:not(:hidden)").each(function(){
				if($(this).text() != ""){
					alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
// 			// 名称
//             var batchName = $("input[name='batchName']").val();
//             if(batchName == ""){
//                 $(".batchName").find(".validate_tips").text("批次名称不允许为空");
//                 $(".batchName").find(".validate_tips").addClass("valid_fail_text");
//                 $(".batchName").find(".validate_tips").show();
//                 return false;
//             }
//             var batchFile = $("input.import-file").val();
//             if(batchFile == ""){
//                 $(".batchFile").find(".validate_tips").text("请上传电子券信息");
//                 $(".batchFile").find(".validate_tips").addClass("valid_fail_text");
//                 $(".batchFile").find(".validate_tips").show();
//                 return false;
//             }else if(batchFile.indexOf("xls") == -1) {
//             	 $(".batchFile").find(".validate_tips").text("请上传xls或xlsx格式");
// 				return false;
// 			}
            var files = $("input.import-file").val();
			if(files == "") {
				alert("未选择任何文件，不能导入!");
				return false;
			} else if(files.indexOf("xls") == -1) {
				alert("不是有效的EXCEL文件");
				return false;
			}
// 			if($("select[name='companyKey']")){
// 				var companyKey = $("select[name='companyKey']").val();
// 				if(companyKey == ""){
// 					$("select[name='companyKey']").parents(".companyInfo").find(".validate_tips").text("请选择所属企业");
// 					$("select[name='companyKey']").parents(".companyInfo").find(".validate_tips").addClass("valid_fail_text");
// 					return false;
// 				}
// 			}
			return true;
		}
		
		function initPage() {
			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
					var blacklistCogFlag = "0";
					$("input[mark=blacklist]:not([type=radio])").each(function(){
						if($(this).val() != ""){
							blacklistCogFlag = "1";
						}
					});
					$("input[name=blacklistFlag]").val(blacklistCogFlag);
					
					var flag = validForm();
					if(flag) {
						if(btnEvent == "2"){
							if(confirm("确认发布？")){
								return true;
							} else {
								return false;
							}
						} else {
							return true;
						}
					} else {
						return false;
					}
				}
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
        	<li class="current"><a> 积分商城</a></li>
        	<li class="current"><a title="">电子券批次</a></li>
        	<li class="current"><a title="">添加批次</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vpointsCoupon/addBatch.do">
            	<input type="hidden" name="goodsContent" id="goodsContent" value="">
                <input type="hidden" name="goodsUrl" id="goodsUrl" value="">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>批次信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">批次名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content batchName">
	                       				<input name="batchName" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="50" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
<%-- 	                       	 <c:if test="${fn:length(companyList) gt 0}"> --%>
<!-- 	                       	 <tr> -->
<!-- 	                       		<td class="ab_left"><label class="title">所属企业：<span class="required">*</span></label></td> -->
<!-- 	                       		<td class="ab_main" colspan="3"> -->
<!-- 	                       			<div class="content companyInfo"> -->
<!-- 	                       			<select name="companyKey" id="companyKey" tag="validate" class="form-control required input-width-large search" > -->
<!-- 								    		<option value="">请选择</option> -->
<%-- 	                                        <c:forEach items="${companyList}" var="item"> --%>
<%-- 									    		<option value="${item.companyKey}">${item.companyName}</option> --%>
<%-- 									    	</c:forEach> --%>
<!-- 								    </select> -->
<!--                                        	<label class="validate_tips"></label> -->
<!-- 	                       			</div> -->
<!-- 	                       		</td> -->
<!-- 	                       	</tr> -->
<%-- 	                       	 </c:if> --%>
	                       	 <tr>
	                       		<td class="ab_left"><label class="title">电子券信息：<span class="required">*</span></label></td>
	                       		<td class="ab_main">
	                       			<div class="content">
	                       				<input style="bottom: 0px;" name="batchFile" class="import-file" type="file" single/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vpointsCoupon/getBatchList.do">返 回</button>
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
