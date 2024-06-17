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
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    
	<script>
		$(function(){
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).attr("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
		});
		
		function validFile() {
			var files = $("input.import-file").val();
			if(files == "") {
				$.fn.alert("未选择任何文件，不能导入!");
				return false;
			} else if(files.indexOf("xls") == -1) {
				$.fn.alert("不是有效的EXCEL文件");
				return false;
			}
			return true;
		}
		
	</script>
	
	<style>
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
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><i class="icon-home"></i><a> 返利管理</a></li>
        	<li class="current"><a title="">V码活动 3.0</a></li>
        	<li class="current"><a title="">V码活动·审批</a></li>
        	<li class="current"><a title="">导入金额配置</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vcodeActivity/importActivityMoneyConfig.do" onsubmit="return validFile();">
            	<input type="hidden" name="vcodeActivityKey" value="${activityCog.vcodeActivityKey}" />
            	<input type="hidden" name="queryParam" value="${queryParam}" />
            	<input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
            		<div class="widget-header"><h4><i class="icon-download-alt"></i>导入</h4></div>
                	<div class="widget-content no-padding">
                		<table class="active_board_table">
                			<tr>
                        		<td class="ab_left"><label class="title mart5">活动名称：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<span>${activityCog.vcodeActivityName}</span>
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
                	<div class="active_table_submit mili-cent mart10">
			            <div class="button_place">
					    	<button class="btn btn-primary btnSave" type="submit">导入</button>
					    	<button class="btn btnReturn" url="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?menuType=activity">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
