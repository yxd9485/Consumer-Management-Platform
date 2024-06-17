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
    <title>导入门店</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>
	
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js"></script>
	<script>
/*	$(document).ready(function () {*/
$(function () {
		// 初始化校验控件
		$.runtimeValidate($("form"));
		
		initPage();
	});
		 
		function validForm() {

		//	 var files = $("input.import-file").val();
          var files=  $("input[name=batchFile]").val();

				if(files != ""&&files.indexOf("xls") == -1) {
					$.fn.alert("不是有效的EXCEL文件");
					return false;
				}
            if( typeof(files)==null|| files.length == 0 ||   files == "" || files == null) {
                $.fn.alert("EXCEL文件不能为空");
                return false;
            }
            $("#saveButton").attr("disabled","disabled");
            return true;
		}
		
		function initPage() {

			// 按钮事件
			$(".button_place").find("button").click(function(){
                var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
                   window.location.href="<%=cpath %>/promotionUserAction/showPromotionUserList.do?vjfSessionId=${vjfSessionId}";
                   return false
                } else {
                   var result= validForm();
                    if(!result){
                        return false;
					}
                  var Browser=  myBrowser();
                    if(Browser=="Ch") {
                        $("form").ajaxSubmit(function (data) {
                            console.log(data.toString());
                            var json = eval("(" + data + ")");
                            $.fn.alert(json .errorMsg.toString(), function () {
                                window.location.href = "<%=cpath %>/promotionUserAction/showPromotionUserList.do?vjfSessionId=${vjfSessionId}";
                            });
                        });
                    }else{

                        $("form").ajaxForm(function (data) {
                            console.log(data.toString());
                            var json = eval("(" + data + ")");
                             $.fn.alert(json.errorMsg.toString(), function () {
                                 window.location.href = "<%=cpath %>/promotionUserAction/showPromotionUserList.do?vjfSessionId=${vjfSessionId}";
                             });
                        });
					}
				}
			});
		}
function myBrowser(){
    var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
    var isOpera = userAgent.indexOf("Opera") > -1;

    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    } //判断是否Firefox浏览器
    if (userAgent.indexOf("Chrome") > -1){
        return "Ch";
    }
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
        	<li class="current"><a> 小票审核</a></li>
        	<li class="current"><a title="">${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }管理</a></li>
        	<li class="current"><a title="">导入${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath%>/promotionUserAction/importMessage.do" <%--onsubmit="return validFile();"--%>>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
							<tr>
								
								<td class="ab_left"><label class="title">${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }导入模板&nbsp&nbsp：&nbsp</label></td>
								<td class="ab_main">
									<div class="content">
									<a  href="<%=cpath%>/upload/cogFolder/zhongLCNY/促销员导入模板.xlsx?v=<%=System.currentTimeMillis()%>" style="font-size: 13px !important;">模板下载</a>
									
									</div>
								</td>
							</tr>
	                       	 <tr>
	                       		<td class="ab_left"><label class="title">导入${currentUser.projectServerName eq 'zhongLJH' ? '服务员' : '促销员' }信息：<span class="required">*</span></label></td>
	                       		<td class="ab_main">
	                       			<div class="content">
	                       				<input style="bottom: 0px;" name="batchFile" class="import-file" type="file" single/>
	                       			</div>
	                       		</td>
								 <td><label class="validate_tips"></label></td>
	                       	</tr>
							
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button id="saveButton" class="btn btn-blue btnSave" data-event="1"  <%--onclick="validFile();"--%>>保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
							<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath %>/promotionUserAction/showPromotionUserList.do">返 回</button>
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
