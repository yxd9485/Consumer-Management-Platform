<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%String cpath = request.getContextPath(); 
String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>导入码包页面</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
		});
		
		function validFile() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			var files = $("input.import-file").val();
			if(files == "") {
				$.fn.alert("未选择任何文件，不能导入!");
				return false;
			} else if(files.indexOf("zip") == -1) {
				$.fn.alert("不是有效的zip文件");
				return false;
			}
			
			// 防重复提交
			$(".btnSave").attr("disabled", "disabled");
			$("#divId").css("display","block");
			$("form").submit();
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
		.cool-wood {
			padding-left: 1em;
		}
		fieldset {
			border: 1px solid #d1d1d1;
			max-height: 160px;
			overflow: auto;
		}
		
		fieldset legend {
			font-weight: bold;
		}
		
		fieldset .remove-target {
			float: right;
			color: red;
			cursor: pointer;
		}
		
		fieldset .remove-target > span {
			margin-left: 2px;
		}
		
		fieldset .remove-target > span:hover {
			text-decoration: underline;
		}
		
		fieldset div {
			column-count: 2;
			max-height: 150px;
			height: auto;
			margin-top: 25px;
		}
		
		fieldset div > label {
			width: 48%;
		}
		
		fieldset div > label > span {
			margin-left: 8px;
			font-weight: normal;
		}
		
		.white {
			color: white;
		}
	</style>
  </head>
  
  <body>
  	<div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
  		<h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
  	</div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 返利管理</a></li>
            <li class="current"><a title="">一盖一码活动</a></li>
            <li class="current"><a title="">导入码包</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath %>/qrcodeBatchInfo/doQrcodeImport.do"  >
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header"><h4><i class="icon-download-alt"></i>导入</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                        		<td class="ab_left"><label class="title mart5">码源订单编号：<span class="required">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                                        <input name="orderNo" tag="validate" class="form-control input-width-large required marr20" autocomplete="off" maxlength="30"/>
                                        <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">导入文件：<span class="required">*</span></label></td>
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
					    	<a class="btn btn-primary btnSave btn-blue" onclick="validFile()">导 入</a>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<a class="btn btnReturn btn-radius3" href="<%=cpath%>/qrcodeBatchInfo/showBatchImportList.do?vjfSessionId=${vjfSessionId}">返 回</a>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
