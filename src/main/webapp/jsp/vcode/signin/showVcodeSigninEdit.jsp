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
    <title>添加签到活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
			
			// 检验名称是否重复
            $("input[name='activityName']").on("blur",function(){
            	var activityName = $("input[name='activityName']").val();
            	if(activityName == "") return;
            	checkName(activityName);
            });
		});
		
		function initPage() {
			
			// 活动已开始不能修改签到周期类型
			var currDate = new Date();
			var startDate = new Date("${signinCog.startDate}");
			var endDate = new Date("${signinCog.endDate}" + " 23:59:59");
			if (currDate >= startDate && currDate <= endDate ) {
				$("[name='periodType']").attr("disabled", "disabled");
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
					$.fn.confirm("确认发布？", function(){
						
						$("#skuList > table:hidden").remove();
						
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});
			
			// 增加SKU
			$("#addSku").click(function(){
				$("#skuList").append($("#addSkuTemp").html());
				$("#skuList > table:visible").each(function(i){
					$(this).find("input[name='continueFlag']").attr("value", i);
				});
				$.runtimeValidate($("form"));
			});
			
			// 删除SKU
			$("form").on("click", "#delSku", function(){
				var delBtn = $(this);
				$.fn.confirm("确定要删除此SKU吗？", function(){
					$(delBtn).parents("#skuTable").remove();
				});
			});
			
			// 签到SKU关系
			$("[name='skuRelationType']").change(function(){
				var skuRelationType = $(this).val();
				$("#skuList > table").each(function(i){
					$(this).css("display", "none");
					if (i == 0 && skuRelationType == "2") {
						$(this).css("display", "");
						$("#addSku").css("display", "none");
						
					}
					if (skuRelationType != "2") {
						if (i > 0) {
							$(this).css("display", "");
						}
						$("#addSku").css("display", "");
					}
				});
			});
			$("[name='skuRelationType']").change();
			
			// 签到类型
			$("form").on("change", "[name='signType']", function(){
				if ($(this).val() == '1') {
					$(this).parents("table").find("[name='continueFlag']").prop("disabled", true);
					$(this).parents("table").find("[name='continueFlag']").removeAttr("checked")
				} else {
					$(this).parents("table").find("[name='continueFlag']").prop("disabled", false);
				}
			});
			$("[name='signType']").change();

			// 显示SKU Logo图标
			$("form").on("change", "#skuKeySelect", function(){
				var skuImg = $(this).find("option:checked").data("img");
				$(this).parents("td").find("#skuImg").attr("src", "https://img.vjifen.com/images/vma/" + skuImg);
			});
			$("[name='skuKey']").change();
			$("form").on("mouseover", "#skuKeyCheckBox, #skuKeyCheckBoxLabel", function(){
				var skuImg = $(this).data("img");
				$(this).parents("td").find("#skuImg").attr("src", "https://img.vjifen.com/images/vma/" + skuImg);
			});
			$("form").on("mouseout", "#skuKeyCheckBox, #skuKeyCheckBoxLabel", function(){
				$(this).parents("td").find("#skuImg").attr("src", "");
			});
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(bussionName){
			$.ajax({
				url : "${basePath}/vcodeSignin/checkBussionName.do",
			    data:{
			    		"infoKey":"${signinCog.activityKey}",
			    		"bussionName":bussionName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("活动名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}

		function validForm() {
			// 检验名称是否重复
			var activityName = $("input[name='activityName']").val();
        	if(activityName == "") return false;
        	checkName(activityName);
        	if(!flagStatus){
        		return false;
        	}
        	
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			if ($("#skuList > table:visible").size() <= 0) {
				$.fn.alert("请添加签到SKU配置");
				return false;
			}
			
			// SKU限制关系校验
            var validFlag = true;
            var skuRelationType = $("[name='skuRelationType']").val();
            $("#skuList > table:visible").each(function(i){
                
                // SKU校验
                if (skuRelationType == "2") {
                    if ($(this).find("input[name='skuKey']:checked").size() == 0) {
                        $(this).find("input[name='skuKey']").parents(".content").find(".validate_tips").show();
                        $(this).find("input[name='skuKey']").parents(".content").find(".validate_tips").text("请选择要限制的SKU");
                        $(this).find("input[name='skuKey']").parents(".content").find(".validate_tips").addClass("valid_fail_text");
                        validFlag = false;
                        return false;
                    }
                } else {
                    if ($(this).find("[name='skuKey']").val() == "") {
                        $(this).find("[name='skuKey']").parents(".content").find(".validate_tips").show();
                        $(this).find("[name='skuKey']").parents(".content").find(".validate_tips").text("请选择要限制的SKU");
                        $(this).find("[name='skuKey']").parents(".content").find(".validate_tips").addClass("valid_fail_text");
                        validFlag = false;
                        return false;
                    }
                }
                
                // 签到次数
                var signNum = $(this).find("input[name='signNum']").val();
                if (signNum == "") {
                    $(this).find("input[name='signNum']").parents(".content").find(".validate_tips").show();
                    $(this).find("input[name='signNum']").parents(".content").find(".validate_tips").text("请填写签到值数");
                    $(this).find("input[name='signNum']").parents(".content").find(".validate_tips").addClass("valid_fail_text");
                    validFlag = false;
                    return false;
                }
            });
            if (!validFlag) return false;

            $.ajax({
                url : "${basePath}/vcodeSignin/validSignDate.do",
                data:{
                        "periodType":$("[name='periodType']").val(),
                        "startDate":$("input[name='startDate']").val(),
                        "endDate":$("input[name='endDate']").val()
                    },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend:appendVjfSessionId,
                success:  function(data){
                    if (data["errMsg"]) {
                        validFlag = false;
                        $.fn.alert(data["errMsg"]);
                    }
                }
            });
            if (!validFlag) return false;
			
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
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">万能签到</a></li>
            <li class="current"><a title="">修改活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vcodeSignin/doVcodeSigninEdit.do">
            	<input type="hidden" name="activityKey" value="${signinCog.activityKey}" />
            	<input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="activityName" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="200" value="${signinCog.activityName}"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">签到周期：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="periodType" class="form-control input-width-larger">
	                       					<option value="0">周签到</option>
	                       					<option <c:if test="${signinCog.periodType == '1'}"> selected</c:if> value="1">月签到</option>
	                       				</select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">周期红包上限：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="prizeUpperLimit" tag="validate" value="${signinCog.prizeUpperLimit}"
	                       					class="form-control input-width-larger positive" autocomplete="off" maxlength="10" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="startDate" class="form-control input-width-medium Wdate required preTime"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" value="${signinCog.startDate}"/>
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-medium Wdate required sufTime"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" value="${signinCog.endDate}"/>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">扫码限制次数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="limitNum" tag="validate"
	                       					class="form-control input-width-normal positive required" autocomplete="off" maxlength="5" value="${signinCog.limitNum}"/>
	                       				<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">筛选出”前一签到周期，扫码次数小于等于该数值的用户”参与本次签到活动</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">可疑用户是否参与：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="doubtRebateFlag" style="float: left; margin-top:9px" type="checkbox" value="1" 
	                       					<c:if test="${signinCog.doubtRebateFlag == '1'}"> checked</c:if>  />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">SKU关系：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="skuRelationType" class="form-control input-width-larger">
	                       					<option value="0">并且</option>
	                       					<option <c:if test="${signinCog.skuRelationType == '1'}"> selected</c:if> value="1">或者</option>
	                       					<option <c:if test="${signinCog.skuRelationType == '2'}"> selected</c:if> value="2">混合</option>
	                       				</select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">活动说明：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<textarea name="activityDesc" tag="validate" rows="5"
	                       					class="form-control required" autocomplete="off" maxlength="1000" >${signinCog.activityDesc}</textarea>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>Sku限制关系配置</h4>
                     	<a id="addSku" class="btn btn-blue" style="position: relative; float:right; margin-right: 10px;"><i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 16px;"></i>&nbsp;新 增</a>
                    </div>
                	<div id="skuList" class="widget-content panel no-padding">
                		<table id="skuTable" class="active_board_table" style="border:1px solid green; display: none;">
	                       	<tr>
	                       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content skuInfo">
	                       				<div style="position:relative; display: inline-block; float: left;" >
	                       					<c:if test="${!empty skuList}">
		                       					<c:forEach items="${skuList}" var="sku">
		                       						<input name="skuKey" type="checkbox" id="skuKeyCheckBox" value="${sku.skuKey}" data-img="${sku.skuLogo}" tag="validate"
		                       						<c:if test="${signinCog.skuRelationType == '2' and fn:contains(signinSkuCogLst[0].skuKey, sku.skuKey) }">checked</c:if> /><label id="skuKeyCheckBoxLabel" data-img="${sku.skuLogo}">${sku.skuName}</label></br>
		                       					</c:forEach>
	                       					</c:if>
                       					</div>
                                       	<label class="validate_tips"></label>
	                       			</div>
                                    <img id="skuImg" alt="" src="" width="43px" height="130px" style="position: absolute; display: block; margin-left: 400px;" >
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">签到类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<select name="signType" class="form-control input-width-larger">
	                       					<option value="0">天</option>
	                       					<option <c:if test="${signinCog.skuRelationType == '2' and signinSkuCogLst[0].signType == '1' }">selected</c:if> value="1">次</option>
	                       				</select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">签到数值：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="signNum" tag="validate"
	                       					class="form-control input-width-larger positive" autocomplete="off" maxlength="5" value="${signinSkuCogLst[0].signNum}"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">是否连续：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="continueFlag" type="checkbox" style="float: left; margin-top:9px" value="1" 
	                       				<c:if test="${signinCog.skuRelationType == '2' and signinSkuCogLst[0].continueFlag == '1'}">checked</c:if>/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                		<c:if test="${signinCog.skuRelationType != '2' }">
                			<c:forEach items="${signinSkuCogLst}" var="skuCogItem" varStatus="status">
	                			<table id="skuTable" class="active_board_table" style="border:1px solid green;">
							       	<tr>
							       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content skuInfo">
	                       						<input name="infoKey" type="hidden" value="${skuCogItem.infoKey}" >
							       				<select class="form-control input-width-larger" id="skuKeySelect" name="skuKey" tag="validate">
							       					<option value="">请选择SKU</option>
							       					<c:if test="${!empty skuList}">
							       					<c:forEach items="${skuList}" var="sku">
							       						<option <c:if test="${skuCogItem.skuKey == sku.skuKey}">selected</c:if> value="${sku.skuKey}" data-img="${sku.skuLogo}">${sku.skuName}</option>
							       					</c:forEach>
							       					</c:if>
							       				</select>
							                    <label class="validate_tips"></label>
							       			</div>
							       			<a id="delSku" class="btn btn-red" style="position: relative; float:right; margin-right: 10px;"><i class="iconfont icon-lajixiang" style="font-size: 12px;"></i>&nbsp;删 除</a>
							       			<img id="skuImg" alt="" src="" width="43px" height="130px" style="position: absolute;  display: block; margin-left: 400px;" >
							       		</td>
							       	</tr>
									<tr>
							       		<td class="ab_left"><label class="title">签到类型：<span class="required">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content">
							       				<select name="signType" class="form-control input-width-larger">
							       					<option value="0">天</option>
							       					<option <c:if test="${skuCogItem.signType == '1'}">selected</c:if> value="1">次</option>
							       				</select>
							       				<label class="validate_tips"></label>
							       			</div>
							       		</td>
							       	</tr>
									<tr>
							       		<td class="ab_left"><label class="title">签到数值：<span class="required">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content">
							       				<input name="signNum" tag="validate"
							       					class="form-control input-width-larger positive" autocomplete="off" maxlength="5" value="${skuCogItem.signNum}" />
							       				<label class="validate_tips"></label>
							       			</div>
							       		</td>
							       	</tr>
									<tr>
							       		<td class="ab_left"><label class="title">是否连续：<span class="white">*</span></label></td>
							       		<td class="ab_main" colspan="3">
							       			<div class="content">
							       				<input name="continueFlag" type="checkbox" style="float: left; margin-top:9px" value="${status.index}"
							       				<c:if test="${skuCogItem.continueFlag == '1' }">checked</c:if> />
							       				<label class="validate_tips"></label>
							       			</div>
							       		</td>
							       	</tr>
								</table>
                			</c:forEach>
                		</c:if>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/vcodeSignin/showVcodeSigninList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    
	<div id="addSkuTemp" style="display: none;">
		<table id="skuTable" class="active_board_table" style="border:1px solid green;">
	       	<tr>
	       		<td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
	       		<td class="ab_main" colspan="3">
	       			<div class="content skuInfo">
	       				<select class="form-control input-width-larger" name="skuKey" id="skuKeySelect" tag="validate">
	       					<option value="">请选择SKU</option>
	       					<c:if test="${!empty skuList}">
	       					<c:forEach items="${skuList}" var="sku">
	       						<option value="${sku.skuKey}" data-img="${sku.skuLogo}">${sku.skuName}</option>
	       					</c:forEach>
	       					</c:if>
	       				</select>
	                    <label class="validate_tips"></label>
	       			</div>
	       			<a id="delSku" class="btn btn-red" style="position: relative; float:right; margin-right: 10px;"><i class="iconfont icon-lajixiang" style="font-size: 12px;"></i>&nbsp;删 除</a>
	       			<img id="skuImg" alt="" src="" width="43px" height="130px" style="position: absolute;  display: block; margin-left: 400px;" >
	       		</td>
	       	</tr>
			<tr>
	       		<td class="ab_left"><label class="title">签到类型：<span class="required">*</span></label></td>
	       		<td class="ab_main" colspan="3">
	       			<div class="content">
	       				<select name="signType" class="form-control input-width-larger">
	       					<option value="0">天</option>
	       					<option value="1">次</option>
	       				</select>
	       				<label class="validate_tips"></label>
	       			</div>
	       		</td>
	       	</tr>
			<tr>
	       		<td class="ab_left"><label class="title">签到数值：<span class="required">*</span></label></td>
	       		<td class="ab_main" colspan="3">
	       			<div class="content">
	       				<input name="signNum" tag="validate"
	       					class="form-control input-width-larger positive" autocomplete="off" maxlength="5" />
	       				<label class="validate_tips"></label>
	       			</div>
	       		</td>
	       	</tr>
			<tr>
	       		<td class="ab_left"><label class="title">是否连续：<span class="white">*</span></label></td>
	       		<td class="ab_main" colspan="3">
	       			<div class="content">
	       				<input name="continueFlag" type="checkbox" style="float: left; margin-top:9px" value="1" />
	       				<label class="validate_tips"></label>
	       			</div>
	       		</td>
	       	</tr>
		</table>
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
