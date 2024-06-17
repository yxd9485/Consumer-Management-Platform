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
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
		$(function(){
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).attr("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
			
			// 初始化校验控件
			$.runtimeValidate($("#import_form"));
			// 初始化功能
			initPage();
		});
		
		function validFile() {
            
            if($("[name=hotAreaName]").val() == "") {
            	$.fn.alert("请填写热区名称!");
                return false;
            }
            
            $("[name=coordinate]").val(allGps());
            if($("[name=coordinate]").val() == "") {
            	$.fn.alert("请画出热区的分布情况!");
                return false;
            }
            
			// 防重复提交
            $(".btnSave").attr("disabled", "disabled");
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
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">超级热区</a></li>
            <li class="current"><a title="">查看热区</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath%>/vcodeActivityHotArea/doHotAreaCogEdit.do" onsubmit="return validFile();">
            	<input type="hidden" name="hotAreaKey" value="${hotAreaCog.hotAreaKey}" />
                <input type="hidden" name="coordinate" value="${hotAreaCog.coordinate}"/>
                <input type="hidden" name="queryParam" value="${queryParam}" />
            	<div class="widget box">
                        <div class="widget-header">
                            <h4><i class="icon-download-alt"></i>配置</h4>
                            <div style="float: right; margin-left: 10px; margin-top: -2px;">
	                            <button class="btn btnReturn btn-radius3" url="<%=cpath%>/vcodeActivityHotArea/showHotAreaList.do">返 回</button>
                            </div>
                        </div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                                <td class="ab_left"><label class="title mart5">热区名称：<span class="white">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <input name="hotAreaName" type="text" class="form-control" value="${hotAreaCog.hotAreaName}" />
                                    </div>
                                </td>
                        	</tr>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">所属区域：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<span>${hotAreaCog.areaName}</span>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td colspan="2">
                                <jsp:include page="showHotAreaCogMap.jsp"></jsp:include>
                                </td>
                            </tr>
                		</table>
                	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
