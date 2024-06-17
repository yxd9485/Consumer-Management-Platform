<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%
String cpath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>数据字典修改</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	<script type="text/javascript" src="<%=cpath %>/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script>
		$(function(){
			//初始化校验控件
			$.runtimeValidate($("#sample_form_focus"));
			$.loadedValidate($("#sample_form_focus"));
			
			var queryParam = encodeURI("${queryParam}");
			
			$("a.back").click(function(){
		    	var href=$(this).attr("prevHref");
				href += "&queryParam="+queryParam;
				$(this).attr("href", href);
		    });
		    
		});
		function validate(obj){
			var flag = true;
        	$("form[id$='_focus'] :input[tag=validate]").each(function(){
        		var $parent = $(this).parents(".content");
				var $prev = $parent.parent().prev().find("label.title");
				var $Msg = $parent.find(".validate_tips");
				var value = $(this).val().trim();
				if($(this).is(".required") && value == ""){
					//必填项
					$Msg.addClass("valid_fail_text");
					var text = ($prev.text().replace("：","").replace(":","").replace("*",""))+"不能为空";
					$Msg.html(text);
					$Msg.show();
					flag = false;
				}
        	});
			return flag;
		}
	</script>
	
	<style>
		.selectmargin{margin-left:5px;}
	</style>
  </head>
  
  <body>
  <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 系统管理</a></li>
	        <li class="current">数据字典</li>
	        <li class="current">数据字典·数据</li>
            <li class="current"><a title="">修改</a></li>
        </ul>
    </div>
    <div class="row mart10">
        <div class="col-md-12">
        	<form method="post" class="form-horizontal row-border" id="sample_form_focus" class="post_form"
        		action="<%=cpath %>/sysDataDic/updateDataDic.do" style="margin-top: 1em;" onsubmit="return validate(this)">
            <div class="widget box">
            	<div class="widget-header"><h4><i class="icon-pencil"></i>数据字典信息</h4></div>
                <div class="widget-content panel">
  						<input type="hidden" id="dataDicKey" name="dataDicKey" value="${sysDataDic.dataDicKey}" />
  						<input type="hidden" id="info" name="info" />
  						<input type="hidden" name="queryParam" id="queryParam" value="${queryParam}"/>
  						<table class="active_board_table">
                        	<tr>
                        		<td class="ab_left"><label class="title">字典类型：<span class="required">*</span></label></td>
                        		<td class="ab_main" colspan="3">
                        			<div class="content"><select name="categoryKey" id="categoryKey" class="form-control input-width-large">
                        				<c:forEach items="${categoryList}" var="categoryList">
											<c:if test="${categoryList.categoryKey == sysDataDic.categoryKey}">
														<option value="${categoryList.categoryKey}" selected="selected">
														${categoryList.categoryName}
														</option>
											</c:if>
											<c:if test="${categoryList.categoryKey != sysDataDic.categoryKey}">
														<option value="${categoryList.categoryKey}">
														${categoryList.categoryName}
														</option>
											</c:if>
										</c:forEach>
                        				</select>
                        			</div>
                        		</td>
                        	</tr>
                        	<tr>
                        		<td class="ab_left"><label class="title">数据key：<span class="required">*</span></label></td>
                        		<td class="ab_main" colspan="3">
                        			<div class="content">
                        				<input name="dataId" tag="validate"
                        					class="form-control required width-200" autocomplete="off" maxlength="100" placeholder="数据key" value="${sysDataDic.dataId}"/>
                                       	<label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                        	<tr>
                    		<td class="ab_left"><label class="title">数据值：</label></td>
                    		<td class="ab_main" colspan="3">
                    			<div class="content">
                    				<input name="dataValue" tag="validate"
                        					class="form-control width-300" autocomplete="off" maxlength="3072" placeholder="数据值" value="${sysDataDic.dataValue}"/>
                    				<label class="validate_tips"></label>
                    			</div>
                    		</td>
                    	</tr>
                        	<tr>
                        		<td class="ab_left"><label class="title">排序号：<span class="required">*</span></label></td>
                        		<td class="ab_main" colspan="3">
                        			<div class="content">
                        				<input name="sequenceNum" tag="validate"
                        					class="form-control required width-100 positive" autocomplete="off" maxlength="100" placeholder="排序号" value="${sysDataDic.sequenceNum}"/>
                        				<label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                        	<tr>
                    		<td class="ab_left"><label class="title">别名：</label></td>
                    		<td class="ab_main" colspan="3">
                    			<div class="content">
                    				<input name="dataAlias" tag="validate"
                        					class="form-control width-200 positive" autocomplete="off" maxlength="100" placeholder="别名" value="${sysDataDic.dataAlias}"/>
                    				<label class="validate_tips"></label>
                    			</div>
                    		</td>
                    	</tr>
                    	<tr>
                                <td class="ab_left"><label class="title">说明：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <textarea class="form-control" maxlength="4000" tag="validate" autocomplete="off" name="dataExplain">${sysDataDic.dataExplain}</textarea>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                </div>
	            <div class="active_table_submit mart20">
		            <div class="button_place">
				    	<button class="btn btn-primary btnSubmit btn-blue" type="submit" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
						<a prevHref="<%=cpath%>/sysDataDic/dataDicList.do?vjfSessionId=${vjfSessionId}" class="btn back btn-radius3">返 回</a>
		            </div>
	            </div>
            </div>
            </form>
        </div>
    </div>
    
    </div>
  </body>
</html>
