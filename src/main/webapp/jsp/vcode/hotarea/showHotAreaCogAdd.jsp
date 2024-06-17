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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
		$(function(){
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
					$("form").attr("action", url);
					$("form").attr("onsubmit", "return true;");
					$("form").submit();
				}
				return false;
			});

			// 检验名称是否重复
            $("input[name='hotAreaName']").on("blur",function(){
            	var hotAreaName = $("input[name='hotAreaName']").val();
            	if(hotAreaName == "") return;
            	checkName(hotAreaName);
            });
			
            // 有指定区域时，不允许修改
            if ("${areaName}" != "") {
                $("div.areaName").css("display", "block");
                $("div.position").css("display", "none");
            }
			
			// 省市县
			$("div.position .province").on("change", function(){
				var areaCode = $(this).val();
				if (areaCode != '' && areaCode != null) {
					$("[name=areaCode]").val(areaCode);
				}
		        $.ajax({
		            type: "POST",
		            url: "<%=cpath%>/sysArea/findAreaByParentId.do",
		            async: false,
		            data: {"parentId":$(this).val()},
		            dataType: "json",
		            beforeSend:appendVjfSessionId,
                    success:  function(result){
                        var content = "<option value=''>--</option>";
                        if(result){
                        	$.each(result, function(i, item) {
                        		if (i > 0) {
	                                content += "<option value='"+item.areaCode+"'>"+item.areaName+"</option>";
                        		}
                        	});
                        	$("div.position .city").html(content);
                        } else {
                            $("div.position .city").html(content);
                        }
                        $("div.position .city").trigger("change");
		            },
		            error: function(data){
		            	$.fn.alert("地区回显错误!");
		            }
		        });
			});
			$("div.position .city").on("change", function(){
                var areaCode = $(this).val();
                if (areaCode != '' && areaCode != null) {
                    $("[name=areaCode]").val(areaCode);
                    changeCenterCity($(this).children('option:selected').text());
                }
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/sysArea/findAreaByParentId.do",
                    async: false,
                    data: {"parentId":$(this).val()},
                    dataType: "json",
                    beforeSend:appendVjfSessionId,
                    success:  function(result){
                        var content = "<option value=''>--</option>";
                        if(result){
                            $.each(result, function(i, item) {
                                if (i > 0) {
                                    content += "<option value='"+item.areaCode+"'>"+item.areaName+"</option>";
                                }
                            });
                            $("div.position .county").html(content);
                        } else {
                        	$("div.position .county").html(content);
                        }
                    },
                    error: function(data){
                    	$.fn.alert("地区回显错误!");
                    }
                });      
			});
            $("div.position .county").on("change", function(){
                var areaCode = $(this).val();
                if (areaCode != '' && areaCode != null) {
                    $("[name=areaCode]").val(areaCode);
                    changeCenterCity($(this).children('option:selected').text());
                }     
            });
            $("div.position .province").trigger("change");
			
			// 初始化校验控件
			$.runtimeValidate($("#import_form"));
			// 初始化功能
			initPage();
		});
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(bussionName){
			$.ajax({
				url : "${basePath}/vcodeActivityHotArea/checkBussionName.do",
			    data:{
			    		"infoKey":"",
			    		"bussionName":bussionName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("热区名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}
		
		function validFile() {
			
			if($("[name=hotAreaName]").val() == "") {
				$.fn.alert("请填写热区名称!");
				return false;
			}
			
			// 检验名称是否重复
			var hotAreaName = $("input[name='hotAreaName']").val();
        	if(hotAreaName == "") return false;
        	checkName(hotAreaName);
        	if(!flagStatus){
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
        	<li class="current"><a> 首页管理</a></li>
        	<li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">超级热区</a></li>
            <li class="current"><a title="">新增热区</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vcodeActivityHotArea/doHotAreaCogAdd.do" onsubmit="return validFile();">
                <input type="hidden" name="areaCode" value="${areaCode}" />
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="coordinate"/>
            	<div class="widget box">
						<div class="widget-header">
							<h4><i class="iconfont icon-peizhi"></i>配 置</h4>
							<div  class="button_place" style="float: right; margin-left: 10px; margin-top: -2px;">
	                            <button class="btn btn-blue btnSave" type="submit">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
	                            <button class="btn btnReturn btn-radius3" data-url="<%=cpath%>/vcodeActivityHotArea/showHotAreaList.do">返 回</button>
                            </div>
						</div>
						<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                        		<td class="ab_left"><label class="title mart5">热区名称：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        			    <input name="hotAreaName" class="form-control" style="width: 380px;"/>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">所属区域：<span class="white">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content position">
                                        <select class="province form-control input-width-normal">
                                         <c:forEach items="${areaLst}" var="area">
                                             <option value="${area.areaCode}">${area.areaName}</option>
                                         </c:forEach>
                                        </select>
                                        <select class="city form-control input-width-normal" style="margin-left: 10px;"></select>
                                        <select class="county form-control input-width-normal" style="margin-left: 10px;"></select>
                                    </div>
                                    <div class="content areaName" style="display: none;">
                                        <span>${areaName}</span>
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
