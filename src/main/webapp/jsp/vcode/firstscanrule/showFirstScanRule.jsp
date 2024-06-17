<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>码源订单编辑</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#adQrcodeOrderForm"));
			
			// 初始分组页面显示情况
			$("#radioTable :radio").on("change", function(){
				if("GROUP" == $(this).val()){
					$("#groupDiv").css("display","block");
				}else{
					$("#groupDiv").css("display","none");
				}
			});
			
		    // 选中状态回显
            $("[name='firstScanType'][value='${firstScanRule.firstScanType}']").trigger("change");
            $("[name='firstScanType'][value='${firstScanRule.firstScanType}']").prop("checked", "checked");
			// 初始首扫类型
			if("${firstScanRule.firstScanType}" != "GROUP"){
				$("#groupDiv").css("display","none");
			}
			
			// sku是否选择重复判断
			$("#groupDiv select[id='sku']").on("change",function(){
				var skuKeys = $("#skuKeys").val() + ",";
				var oldKey = $(this).data("oldval");
				var newKey = $(this).val();
				if(skuKeys.indexOf(newKey + ",") != -1){
					$.fn.alert($(this).find("option:selected").text() + "已再分组中，请重新选择");
					if(oldKey == ""){
						$(this).find("option:first").prop("selected", "selected");
					}else{
						$(this).find("option[value='"+oldKey+"']").prop("selected", "selected");
					}
				}else{
					// 拼接新的skuKeys
					if(skuKeys.charAt(skuKeys.length - 1) == ","){
						skuKeys += newKey;
					}else{
						skuKeys += "," + newKey;
					}
					
					// 删除更改之前旧的oldKey
					if(oldKey != ""){
						// 删除旧的skuKey
						skuKeys += ",";
                		skuKeys = skuKeys.replace(oldKey + ",","");
                		// 去掉最后一个逗号
                		if(skuKeys.charAt(skuKeys.length - 1) == ","){
    	            		skuKeys = skuKeys.substring(0, skuKeys.length - 1);
                		}
					}
					
					// 设置新的skuKeys
					$("#skuKeys").val(skuKeys);
					// 设置新的skuKey
					$(this).data("oldval",newKey);
				}
			});
			
			
			 // 新增分组
            $("form").on("click", "#addGroupItem", function(){
                if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "新增") {
	            	var $cloneItem = $(this).closest("table").clone(true, true);
	            	$cloneItem.find("div.sku").eq(0).nextAll().remove();
	            	var $groupName = $cloneItem.find("#groupName");
	            	$groupName.attr("id", "groupName").val("");
	            	$cloneItem.find("#sku").data("oldval", "").val("请选择");
	            	$cloneItem.find("#addGroupItem").text("删除")
	            	$(this).closest("div.group-content").append($cloneItem);
            	} else {
            		var delKey;
            		// 获取所有组内的skuKey（包含新增未提交的）
            		var skuKeys = $("#skuKeys").val() + ",";
            		// 获取删除的所有select
            		$(this).closest("table").find("#sku").each(function(){
            			// 当前删除的skuKey
            			delKey = $(this).data("oldval") + ",";	
            			// 删除指定的skuKey
                		skuKeys = skuKeys.replace(delKey,"");
            		});
            		
            		// 去掉最后一个逗号
            		if(skuKeys.charAt(skuKeys.length - 1) == ","){
	            		skuKeys = skuKeys.substring(0, skuKeys.length - 1);
            		}
            		// 新的所有组内的skuKey
            		$("#skuKeys").val(skuKeys);
            		// 页面删除属性
            		$(this).closest("table").remove();
            	}
            });
            
            // 新增SKU
            $("form").on("click", "#addSkuItem", function(){
                if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "+") {
	            	$cloneItem = $(this).closest("div").clone(true, true);
                    $cloneItem.find("#sku").data("oldval", "").val("请选择");
                    $cloneItem.find("#addSkuItem").html("-");
	            	$(this).closest("td.perItem").append($cloneItem);
            	} else {
            		// 获取要删除的skukey并删除掉
            		var delKey = $(this).closest("div").find("#sku").data("oldval") + ",";
            		// 获取所有组内的skuKey（包含新增未提交的）
            		var skuKeys = $("#skuKeys").val() + ",";
            		// 删除指定的skuKey
            		skuKeys = skuKeys.replace(delKey,"");
            		// 去掉最后一个逗号
            		if(skuKeys.charAt(skuKeys.length - 1) == ","){
	            		skuKeys = skuKeys.substring(0, skuKeys.length - 1);
            		}
            		// 新的所有组内的skuKey
            		$("#skuKeys").val(skuKeys);
            		// 页面删除属性
            		$(this).closest("div").remove();
            	}
            });
		});
		
		var a=[23,12,56,76,45];
		var b=[12,76,65,89];
		

		// 差集计算
		function cj(a,b){
			//因为splice()方法会改变原始数组，所以使用slice()方法克隆数组，保证原始数组不被改变，方便多次运算
			var aa=a.slice();
			console.log(aa);
			var bb=b.slice();
			console.log(bb);
			for(x in bb){
				var y=aa.indexOf(bb[x]);
				if(y>=0){
					aa.splice(y,1);
				}
			}
			return aa;
		}

		
		function validForm(){
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			
			if($("#radioTable :radio:checked").size() == 0){
				$.fn.alert("请选择首扫类型");
				return false;
			}else{
				var groupName = "";
				var skuKeys = "";
				var ruleValue = "";
				if($("#radioTable :radio:checked").val() == 'GROUP'){
					$("#groupDiv table").each(function(){
						groupName = $(this).find("input[id='groupName']").val();
						if(groupName != ""){
							ruleValue += groupName + "#";
						}else{
							validateResult = false;
							$.fn.alert("请完善分组信息");
							return false;
						}
						
						var idx = 0;
						var skuTotal = $(this).find("select[id='sku']").size();
						$(this).find("select[id='sku']").each(function(){
							skuKeys = $(this).val();
							if(skuKeys != "请选择"){
								idx ++;
								ruleValue += skuKeys;
								if(idx == skuTotal){
									ruleValue += "@";
								}else{
									ruleValue += "#";
								}
							}else{
								validateResult = false;
								$.fn.alert("请完善分组信息");
								return false;
							}
						});
						
					});
					
					// 判断所有SKU是否都已分组
					//var dbSkuKeys = "${skuKeyList}".split(",");
					//var newSkuKeys = skuKeys.split(",");
					//var result = cj(dbSkuKeys, newSkuKeys));
					//if(result.length > 0){
					//	validateResult = false;
					//	$.fn.alert("所有SKU必须设置分组");
					//	return false;
					//}
					
					// 规则赋值
					if(ruleValue != ""){
						ruleValue = ruleValue.substring(0,ruleValue.length -1);
						$("#ruleValue").val(ruleValue);
					}
				}
			}
			
			if(!validateResult){
				return false;
			}
			$("form").submit();
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
		.en-larger2 {
			margin-left: 12px;
		}
		.ex-larger {
			margin-right: 8px;
		}
		.validate_tips {
			padding: 8px !important;
		}
		div b{
			color:red;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 基础配置</a></li>
            <li class="current"><a title="">首扫规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<input type="hidden" id="skuKeys" value="${skuKeys }"/>
            <form method="post" class="form-horizontal row-border validate_form" id="adQrcodeOrderForm"
            	action="${basePath}/firstScanRule/doFirstScanRuleAddOrEdit.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="icon-list-alt"></i>首扫规则配置</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table id="radioTable" class="active_board_table">
                            <tr>
                            	<td class="ab_left" rowspan="4"><label class="title">首扫类型：</label></td>
                                <td class="ab_main">
                                	<input type="radio" name="firstScanType" value="USER"/> 按用户计算（同一省区同一用户扫过任意一支产品，只计算一次首扫）
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main">
                                	<input type="radio" name="firstScanType" value="ACTIVITY"/> 按活动计算（用户扫码每个活动，计算一次首扫）
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main">
                                	<input type="radio" name="firstScanType" value="SKU"/> 按SKU计算（用户扫码每个SKU，计算一次首扫）
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main">
                                	<input type="radio" name="firstScanType" value="GROUP"/> 按分组计算（同一分组内只计算一次首扫，未加入分组SKU则单独计算首扫，尽量SKU都加入分组，分组个数不限）
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div id="groupDiv" class="widget-content panel no-padding group-content">
                        <input id="ruleValue" type="hidden" name="ruleValue"/>
                        <c:choose>
                        	<c:when test="${firstScanRule.firstScanType != 'GROUP'}">
                        		<table class="active_board_table erupt_table" style="border:1px solid green; border-collapse: inherit;">
		                            <tr>
		                                <td class="ab_left"><label class="title">组名称：<span class="required">&nbsp;</span></label></td>
		                                <td class="ab_main" colspan="3">
		                                    <div class="content" style="display: flex;">
		                                        <input id="groupName" class="form-control input-width-larger required"/>
		                                        <label id="addGroupItem" class="blocker marl15 btn-txt-add-red">新增</label>
		                                        <label class="validate_tips"></label>
		                                    </div>
		                                </td>
		                            </tr>
		                            <tr>
		                                <td class="ab_left" style="vertical-align: top;"><label class="title mart5">选择SKU：<span class="required">&nbsp;</span></label></td>
		                                <td class="ab_main perItem">
		                                    <div class="content sku" style="margin: 5px 0px; display: flex;">
		                                        <select id="sku" class="form-control input-width-larger required">
		                                        	<option >请选择</option>
		                                        	<c:forEach items="${skuList}" var="sku">
		                                        		<option value="${sku.skuKey }">${sku.skuName }</option>
		                                        	</c:forEach>
		                                        </select>
		                                        <span id="addSkuItem" class="marl10 btn-txt-add-red" style="font-size: 20px !important; font-weight: bold; cursor: pointer; width: 30px; text-align: center;">+</span>
		                                    </div>
		                                </td>
		                            </tr>
		                		</table>
                        	</c:when>
                        	<c:otherwise>
                        		<c:forEach items="${firstScanRule.skuGroupRuleList }" var="skuGroupRule" varStatus="oneIdx">
                        			<table class="active_board_table erupt_table" style="border:1px solid green; border-collapse: inherit;">
			                            <tr>
			                                <td class="ab_left"><label class="title">组名称：<span class="required">&nbsp;</span></label></td>
			                                <td class="ab_main" colspan="3">
			                                    <div class="content" style="display: flex;">
			                                        <input id="groupName" class="form-control input-width-larger required" value="${skuGroupRule.skuGroupName }"/>
			                                        <c:choose>
			                                        	<c:when test="${oneIdx.index eq 0}">
			                                        		<label id="addGroupItem" class="blocker marl15 btn-txt-add-red">新增</label>
			                                        	</c:when>
			                                        	<c:otherwise>
			                                        		<label id="addGroupItem" class="blocker marl15 btn-txt-add-red">删除</label>
			                                        	</c:otherwise>
			                                        </c:choose>
			                                        
			                                        <label class="validate_tips"></label>
			                                    </div>
			                                </td>
			                            </tr>
			                            <tr>
			                                <td class="ab_left" style="vertical-align: top;"><label class="title mart5">选择SKU：<span class="required">&nbsp;</span></label></td>
			                                <td class="ab_main perItem">
			                                	<c:set var="idx" value="0"></c:set>
			                                	<c:forEach items="${fn:split(skuGroupRule.skuKeys,',') }" var="skuKey" varStatus="twoIdx">
			                                		<c:set var="idx" value="${idx + 1 }"></c:set>
				                                    <div class="content sku" style="margin: 5px 0px; display: flex;">
				                                        <select id="sku" class="form-control input-width-larger required" data-oldval="${skuKey }">
				                                        	<option>请选择</option>
				                                        	<c:forEach items="${skuList}" var="sku">
			                                        			<option value="${sku.skuKey }"
			                                        				<c:if test="${sku.skuKey eq skuKey}"> selected="selected" </c:if>>${sku.skuName }</option>
			                                        		</c:forEach>	
				                                        </select>
				                                        <c:choose>
				                                        	<c:when test="${twoIdx.index eq 0}">
																<span id="addSkuItem" class="marl10 btn-txt-add-red" 
						                                        	style="font-size: 20px !important; font-weight: bold; cursor: pointer; 
						                                        	width: 30px; text-align: center;">+</span>
															</c:when>
				                                        	<c:otherwise>
																<span id="addSkuItem" class="marl10 btn-txt-add-red" 
						                                        	style="font-size: 20px !important; font-weight: bold; cursor: pointer; 
						                                        	width: 30px; text-align: center;">-</span>
															</c:otherwise>
				                                        </c:choose>
				                                    </div>
			                                    </c:forEach>
			                                </td>
			                            </tr>
			                		</table>
                        		</c:forEach>
                        	</c:otherwise>
                        </c:choose>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<a class="btn btn-blue" data-event="1" onclick="validForm()">保 存</a>&nbsp;&nbsp;&nbsp;&nbsp;
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
