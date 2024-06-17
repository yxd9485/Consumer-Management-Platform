<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
	String cpath = request.getContextPath();
	String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
%>
<!DOCTYPE html>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
	<title>新增统计分组</title>

	<jsp:include page="/inc/Main.jsp"></jsp:include>
	<script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>

	<script>
		var basePath='<%=cpath%>';
		var allPath='<%=allPath%>';

		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));

			// 初始化功能
			initPage();
		});

		function initPage() {
			if("${statInfo.projectServerName}" == "quanbu"){
				$(".dasha").show();
			}else {
				$("."+ "${statInfo.projectServerName}").show();
			}
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

			// 增加SKU
			$("form").on("click", "#addSku", function() {
				if ($(this).is("[disabled='disabled']")) return;
				if ($(this).text() == '新增') {
					var $copySku = $(this).closest("div").clone(true, true);
					$copySku.find("#addSku").text("删除");
					$(this).closest("td").append($copySku);

				} else {
					$(this).closest("div").remove();
				}
			});
			$('#projectServerName').change(function(e){
				var a = e.target.value;
				if(a == "quanbu"){
					$(".dasha").show();
				}else{
					$(".dasha").hide();
					$("."+ a).show();
				}
				$("input[name='skuKey']").prop("checked",false);
			});

		}

		// 检验名称是否重复
		var flagStatus = false;
		function checkName(bussionName){
			$.ajax({
				url : "${basePath}/spuInfo/checkBussionName.do",
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
						$.fn.alert("SPU已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
				}
			});
		}

		function validForm() {
			var s;
			$("#skuList > table:visible").each(function(i) {
				s = $(this).find("input[name='skuKey']:checked").size()
			});

			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			if (s == 0){
				$.fn.alert("未选择SKU");
				return false;
			}
			return true;
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
			<li class="current"><a> 首页</a></li>
			<li class="current"><a title="">基础配置</a></li>
			<li class="current"><a title="">统计分组配置</a></li>
			<li class="current"><a title="">新增统计分组</a></li>
		</ul>
	</div>
	<div class="row">
		<div class="col-md-12 tabbable tabbable-custom">
			<form method="post" class="form-horizontal row-border validate_form" id="code_form"
				  action="<%=cpath %>/stat/doStatInfoEdit.do">
				<input type="hidden" name="queryParam" value="${queryParam}" />
				<input type="hidden" name="pageParam" value="${pageParam}" />
				<div class="widget box">
					<div class="widget-header">
						<h4><i class="iconfont icon-xinxi"></i>统计分组新增</h4>
					</div>
					<div class="widget-content panel no-padding">
						<table class="active_board_table">
							<tr>
								<td class="ab_left"><label class="title">统计分组名称：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="statName" tag="validate"
											   class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${statInfo.statName}"/>
										<input name="statInfoKey" tag="validate" type="hidden"
											   class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${statInfo.statInfoKey}"/>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">统计分组分类：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<select name="statType" class="form-control input-width-larger search" autocomplete="off" >
										<option value="SG00001"<c:if test="${statInfo.statType eq 'SG00001'}"> selected="selected"</c:if>>经典箱啤500*6</option>
										<option value="SG00002"<c:if test="${statInfo.statType eq 'SG00002'}"> selected="selected"</c:if>>经典箱啤500*12</option>
										<option value="SG00003"<c:if test="${statInfo.statType eq 'SG00003'}"> selected="selected"</c:if>>经典罐啤500*12</option>
										<option value="SG00004"<c:if test="${statInfo.statType eq 'SG00004'}"> selected="selected"</c:if>>纯生箱啤</option>
										<option value="SG00005"<c:if test="${statInfo.statType eq 'SG00005'}"> selected="selected"</c:if>>纯生罐啤</option>
										<option value="SG00006"<c:if test="${statInfo.statType eq 'SG00006'}"> selected="selected"</c:if>>崂山箱+罐啤</option>
										<option value="SG00008"<c:if test="${statInfo.statType eq 'SG00008'}"> selected="selected"</c:if>>青啤项目月报</option>
										<option value="SG00009"<c:if test="${statInfo.statType eq 'SG00009'}"> selected="selected"</c:if>>青啤总部月报</option>
										<option value="SG00007"<c:if test="${statInfo.statType eq 'SG00007'}"> selected="selected"</c:if>>其他</option>
									</select>
									<label class="validate_tips"></label>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">备注：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
											<input class="form-control" maxlength="200" tag="validate" autocomplete="off" name="remark" value="${statInfo.remark}"></input>
											<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">省区：<span class="required">*</span></label></td>
								<td class="ab_main " colspan="3">
									<div class="content projectServerName promotionSku" style="display: flex; margin: 5px 0px;">
										<select class="form-control input-width-larger required" name="projectServerName" id="projectServerName" tag="validate">
											<option value="">请选择省区</option>
											<option value="quanbu" <c:if test="${statInfo.projectServerName eq 'quanbu'}"> selected="selected"</c:if>>全部</option>
											<c:if test="${!empty serverList}">
												<c:forEach items="${serverList}" var="server">
													<option value="${server.projectServerName}"<c:if test="${server.projectServerName eq statInfo.projectServerName}"> selected="selected" </c:if>>${server.serverName}</option>

												</c:forEach>
											</c:if>
										</select>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="widget-header top-only">
						<h4><i class="iconfont icon-saoma"></i>SKU选择</h4>
					</div>
					<div id="skuList" class="widget-content panel no-padding">
						<table id="skuTable" class="active_board_table" style="border:1px solid green;">
							<tr>
								<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content skuInfo">
										<div style="position:relative;  float: left;" >
											<c:if test="${!empty skuList}">
												<c:forEach items="${skuList}" var="sku">
													<div class="${sku.projectServerName} dasha" style="display: none">
                                                        <label id="skuKeyCheckBoxLabel"><input class="wtf" name="skuKey" id="skuKeyCheckBox" type="checkbox" value="${sku.projectServerName}/${sku.serverName}/${sku.skuKey}/${sku.skuName}" tag="validate"
															   <c:if test="${fn:contains(skus, sku.skuWord)}">checked</c:if>/>${sku.serverName}-${sku.skuName}-<span style="color: red;">${sku.statName}</span></label>
													</div>
												</c:forEach>
											</c:if>
										</div>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
						</table>
					</div>
					<div class="active_table_submit mart20">
						<div class="button_place">
							<button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
							<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/stat/showStatInfoList.do">返 回</button>
						</div>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div id="imageDiv" style="display: none">
		<jsp:include page="/inc/upload/uploadImage.jsp"></jsp:include>
	</div>
</body>
</html>
