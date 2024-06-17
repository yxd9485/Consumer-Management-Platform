<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title></title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	
	<script>
		
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			
			// 初始化功能
			initPage();
		});
		
		function initPage() {

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
            $("input[name='perMantissaName']").on("blur",function(){
                var perMantissaName = $("input[name='perMantissaName']").val();
                if(perMantissaName == "") return;
                checkName(perMantissaName);
            });
            
            // 显示选择循环日期
            $("form").on("click", "#cycleDay", function(){
            	if ($(this).val()) {
            		$.each($(this).val().split(","), function(i, obj){
            			$("label.cycleday[value='" + obj +"']").addClass("cycleday-selected");
            		});
            	}
                $("#chooseCycleDayDialog").modal("show");
            });
            
            // 选择循环日期
            $("label.cycleday").on("click", function(){
                if ($(this).is(".cycleday-selected")) {
                    $(this).removeClass("cycleday-selected");
                } else {
                	if ($(this).text() == "每天") {
                        $("label.cycleday").removeClass("cycleday-selected");
                    } else {
                    	$("label.cycleday[value='每天']").removeClass("cycleday-selected");
                    }
                    $(this).addClass("cycleday-selected");
                }

                
            });
            
            // 确定选择循环日期
            $("#chooseCycleDayDialog").delegate("#addBtn", "click", function(){
            	var cycleDay = "";
            	$("label.cycleday-selected").each(function(){
            		cycleDay = cycleDay + $(this).text() + ",";
            	});
            	$("#cycleDay").val(cycleDay.substring(0, cycleDay.length -1));
            	$("#cycleDay").trigger("blur");
                $("#chooseCycleDayDialog #closeBtn").trigger("click");
            });
            
            // 增加尾数
            $("form").on("click", "#addMantissa", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true);
                    $copySku.find("#addMantissa").text("删除");
                    $copySku.find("#cycleMantissa").val('');
                    $(this).closest("td").append($copySku);
                    
                } else {
                    $(this).closest("div").remove();
                }
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
            
            // 兑奖截止类型
            $("[name='prizeExpireType']").on("change", function(){
                if($(this).val() == '0') {
                    $(this).closest("div").find("input[name='prizeExpireDate']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='prizeValidDay']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("input[name='prizeExpireDate']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='prizeValidDay']").removeAttr("disabled");
                }
            });
            
            // 初始化模板状态显示样式
            $("#status").bootstrapSwitch({  
                onSwitchChange:function(event,state){  
                    if(state==true){  
                       $("input:hidden[name='status']").val("1");
                    }else{  
                       $("input:hidden[name='status']").val("0");
                    }
                }
            });

            // 单击时清空选择的文件，当选择同一文件时才能触发change事件
            $("#prizeFile").on("click", function(){
                $(this).val("");
            });
            
            // 校验奖项配置项配置文件
            $("#prizeFile").on("change", function() {
                $uploadFile = $(this);
                var files = $uploadFile.val();
                if(files == "" || files.indexOf("xls") == -1) {
                    return false;
                }
            
                // 提交表单
                var url = "<%=cpath%>/perMantissaCog/checkPrizeCogExcel.do";
                var formData = new FormData();
                formData.append("prizeFile", $uploadFile[0].files[0]);
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: false,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        console.log(data);
                        if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                            $("#prizeFile").val("");
                        } else {
                        	var tbodyHtml = "";
                            $.each(data["excelLst"], function(i, item){
                            	tbodyHtml += "<tr>";
                            	tbodyHtml += "<td><label>" + (i + 1) + "</label></td>";
                            	tbodyHtml += "<td><label>" + item["prizeType"] + "</label></td>";
                            	tbodyHtml += "<td><label>" + item["money"] + "</label></td>";
                            	tbodyHtml += "<td><label>" + Number(item["unitMoney"]).toFixed(2) + "</label></td>";
                            	tbodyHtml += "<td><label>" + item["vpoints"] + "</label></td>";
                            	tbodyHtml += "<td><label>" + Number(item["unitVpoints"]).toFixed(2) + "</label></td>";
                            	tbodyHtml += "<td><label>" + (item["scanNum"] ? item["scanNum"] : "") + "</label></td>";
                            	tbodyHtml += "<td><label>" + item["amounts"] + "</label></td>";
                            	tbodyHtml += "<td><label>" + item["prizePercent"] + "%</label></td>";
                                tbodyHtml += "</tr>";
                            });
                        }
                        $("#prizeCogLst tbody").html(tbodyHtml);
                    }
                });
            });
		}
		
		// 初始文本框值
		function clearInput(object, type){
			var value = $(object).val();
			if(type == '1' && value == "0"){
				$(object).val("");
			}else if(type == '2' && value <= "0.00"){
				$(object).val("");
			}
		}
		
		// 检查value类型：object文本框，type类型：1数字，2金额
		function inspectValueType(object, type){
			var value = $(object).val();
			if(type == '1' && (type == "" || !/^[1-9][0-9]*$/.test(value))){
				$(object).val("0");
			}else if(type == '2'){
				if(type == "" || !/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value) || value < 0){
					$(object).val("0.00");
				}
			}else if(type == '3'){
				if(type == "" || !/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value) || value < 0){
					$(object).val("");
				}
			}
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(perMantissaName){
			$.ajax({
				url : "${basePath}/perMantissaCog/checkBussionName.do",
			    data:{
			    		"infoKey":"",
			    		"bussionName":perMantissaName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("规则名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 检验名称是否重复
			$("input[name='perMantissaName']").trigger("blur");
        	if(!flagStatus){
        		return false;
        	}
        	
        	// 拼接尾数
        	var cycleMantissa = "";
        	$("input[id='cycleMantissa']").each(function(){
        		cycleMantissa = cycleMantissa + $(this).val() + ",";
        	});
        	if (cycleMantissa != "") {
        		cycleMantissa = cycleMantissa.substring(0, cycleMantissa.length - 1);
        	}
        	$("input[name='cycleMantissa']").val(cycleMantissa);
        	
			// 拼接SKU
			var skuKey = "";
			var skuName = "";
			$("select[id='skuKeyAry']").each(function() {
				skuKey = skuKey + $(this).val() + ",";
				skuName = skuName + $(this).find("option:selected").text() + ",";
			});
			if (skuKey != "") {
				skuKey = skuKey.substring(0, skuKey.length - 1);
				skuName = skuName.substring(0, skuName.length - 1);
			}
			$("input[name='skuKey']").val(skuKey);
			$("input[name='skuName']").val(skuName);
			
			// 奖项配置项文件
			var files = $("#prizeFile").val();
            if(files == "" || files.indexOf("xls") == -1) {
            	$.fn.alert("请选择逢尾数奖项配置文件！");
                return false;
            }
			
			// 规则限制 
			if($("input[name='restrictVpoints']").val() == ""){
                $("input[name='restrictVpoints']").val("0");
            }
			if($("input[name='restrictMoney']").val() == ""){
				$("input[name='restrictMoney']").val("0.00");
			}
			if($("input[name='restrictBottle']").val() == ""){
				$("input[name='restrictBottle']").val("0");
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
		.cycleday {
		  width: 20px;
		  height: 20px;
		  cursor: pointer;
		  margin: 1px;
		  text-align: center;
		  border: 1px solid gray;
		}
		.cycleday-selected {
          background-color: #64E0E0;
        }
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 逢尾规则</a></li>
            <li class="current"><a> 新增逢尾数规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath%>/perMantissaCog/doPerMantissaCogAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
            	<div class="widget box">
            		<div class="widget-header">
						<h4><i class="iconfont icon-xinxi"></i>新增逢尾数规则</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="perMantissaName" tag="validate"  
	                       					class="form-control required" style="width: 330px;" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                				<input type="hidden" name="validDateRange" />
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<div class="content date" style="margin: 5px 0px; display: flex;">
                                        <input name="startDate" id="startDate" class="Wdate form-control input-width-medium required preTime" 
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium required sufTime" autocomplete="off"
                                            tag="validate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">指定日期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="cycleDay" name="cycleDay" tag="validate" 
                                            class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">循环瓶尾数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input type="hidden" name="cycleMantissa" />
                                    <div class="content" style="margin: 5px 0px; display: flex;">
                                        <input id="cycleMantissa" tag="validate"
                                            class="form-control input-width-small required number integer" autocomplete="off" maxlength="30" />
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addMantissa">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">促销SKU：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <input type="hidden" name="skuKey" />
                                    <input type="hidden" name="skuName" />
                                    <div class="content sku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" id="skuKeyAry" tag="validate">
                                            <option value="">请选择SKU</option>
                                            <c:if test="${!empty skuLst}">
                                            <c:forEach items="${skuLst}" var="sku">
                                            <option value="${sku.skuKey}" data-img="${sku.skuLogo}">${sku.skuName}</option>
                                            </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">兑出截止日期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="prizeExpireType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定日期</span>
                                        <input name="prizeExpireDate" class="form-control input-width-medium Wdate required"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <input type="radio" class="tab-radio" name="prizeExpireType" value="1" style="float:left; margin-left: 10px !important;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">有效天数</span>
                                        <input name="prizeValidDay" tag="validate" disabled="disabled"
                                            class="form-control input-width-small required number positive" autocomplete="off" maxlength="3" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">金额配置：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content" style="display: flex; line-height: 35px;">
                                        <input name="prizeFile" id="prizeFile" class="import-file" type="file" single/>
                                        <a href="<%=cpath%>/upload/cogFolder/${projectServerName}/逢尾数奖项配置项模板.xlsx?v=<%=System.currentTimeMillis()%>" >模板下载</a>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									    <input name="status" type="hidden" value="1" />
									    <input id="status" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">限制时间类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                       				<div style="float: left; width: 80px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
                                            class="form-control number positive input-width-small" autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        	<tr>
	                       		<td class="ab_left"><label class="title">限制消费金额：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictMoney" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number money input-width-small" autocomplete="off" maxlength="9" onblur="inspectValueType(this,'2')"/>
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制消费瓶数：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictBottle" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number positive input-width-small" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>配置金额</h4></div>
                    <div class="widget-content panel no-padding">
                        <table id="prizeCogLst" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
						    <thead>
						         <tr>
						             <th style="width:4%; text-align: center;">序号</th>
						             <th style="width:20%; text-align: center;">中出类型</th>
						             <th style="width:12%; text-align: center;">积分红包金额范围(元)</th>
						             <th style="width:10%; text-align: center;">积分红包均价(元)</th>
						             <th style="width:12%; text-align: center;">红包积分范围</th>
						             <th style="width:10%; text-align: center;">积分均价</th>
						             <th style="width:12%; text-align: center;">瓶数区间</th>
						             <th style="width:10%; text-align: center;">奖项个数</th>
						             <th style="width:10%; text-align: center;">奖项占比</th>
						         </tr>
						     </thead>
						     <tbody>
						     </tbody>
						 </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/perMantissaCog/showPerMantissaCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div class="modal fade" id="chooseCycleDayDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">选择指定日期</h4>
                </div>
                <div class="modal-body" style="margin-left: 20%;">
                    <div class="content">
                         <label class="cycleday" value="01">01</label>
                         <label class="cycleday" value="02">02</label>
                         <label class="cycleday" value="03">03</label>
                         <label class="cycleday" value="04">04</label>
                         <label class="cycleday" value="05">05</label>
                         <label class="cycleday" value="06">06</label>
                         <label class="cycleday" value="07">07</label>
                         <label class="cycleday" value="08">08</label>
                         <label class="cycleday" value="09">09</label>
                         <label class="cycleday" value="10">10</label>
                    </div>
                    <div class="content">
                         <label class="cycleday" value="11">11</label>
                         <label class="cycleday" value="12">12</label>
                         <label class="cycleday" value="13">13</label>
                         <label class="cycleday" value="14">14</label>
                         <label class="cycleday" value="15">15</label>
                         <label class="cycleday" value="16">16</label>
                         <label class="cycleday" value="17">17</label>
                         <label class="cycleday" value="18">18</label>
                         <label class="cycleday" value="19">19</label>
                         <label class="cycleday" value="20">20</label>
                    </div>
                    <div class="content">
                         <label class="cycleday" value="21">21</label>
                         <label class="cycleday" value="22">22</label>
                         <label class="cycleday" value="23">23</label>
                         <label class="cycleday" value="24">24</label>
                         <label class="cycleday" value="25">25</label>
                         <label class="cycleday" value="26">26</label>
                         <label class="cycleday" value="27">27</label>
                         <label class="cycleday" value="28">28</label>
                         <label class="cycleday" value="29">29</label>
                         <label class="cycleday" value="30">30</label>
                         <label class="cycleday" value="31">31</label>
                         <label class="cycleday" value="每天" style="width: 30px;">每天</label>
                     </div>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
  </body>
</html>
