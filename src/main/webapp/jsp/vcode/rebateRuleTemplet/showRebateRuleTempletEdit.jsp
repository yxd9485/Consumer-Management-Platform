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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	
	<script>
		
		$(function(){
			
			// 为减少初始化时的计算量，默认为false
			calculateUnitFlag = false;
			
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
		});
		
		function initPage() {
            
            $("[data-toggle='popover']").popover();
            $('body').on('click', function(event) { 
                if ($("div.popover").size() > 0 
                        && $(event.target).closest("[data-toggle]").size() == 0 
                        && !($(event.target).is(".popover") || $(event.target).closest("div.popover").size() == 1)) {
                    $("[data-toggle='popover']").popover('toggle');
                }
            });

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
	        
	        // 金额格式
	        $("form").on("change", ".moneyFmt", function(){
	            $(this).val(Number($(this).val()).toFixed(2));
	        });
            
            // 输入首扫点比
            $("input[name='firstScanPercent']").on("input", function(){
            	var firstScanPercent = Number($(this).val());
            	var commonScanPercent = 100 - firstScanPercent;
            	$("input[name='commonScanPercent']").val(commonScanPercent).attr("maxVal", commonScanPercent);
            	
            	$("[id='totalPrizePercent']").each(function(i, obj){
            		$(this).css("color", $(this).text() == "100.0000%" ? "green" : "red");
            	});

                // 重新计算均价
                calculateUnit();
            });
            
            // 动态增加奖项配置项
            $("form").on("click", "#addPrizeItem", function(){
                var $scanPercent = $(this).closest("table.scan_type_table").find("input[name$='ScanPercent']");
            	if (Number($scanPercent.val()) == 0) {
            		$.fn.alert($scanPercent.attr("name") == "firstScanPercent" ? "请先配置首扫点比" : "请先配置普扫点比");
            		
            	} else {
	            	if ($(this).closest("tr").find("#NO").text() == "1") {
	            		var $cloneItem = $(this).closest("tr").clone(true, true);
	            		$cloneItem.find("input[name$='Money']").data("oldval", "0.00").val("0.00");
	            		$cloneItem.find("input[name$='Vpoints']").data("oldval", "0").val("0");
	            		$cloneItem.find("input[name$='Percent']").data("oldval", "0.00").val("0.00");
                        $cloneItem.find("input[name='scanNum'], input[name='prizePercentWarn']").val("");
	            		$cloneItem.find("label[id^='unit']").text("0.00");
	            		$cloneItem.find("#addPrizeItem").text("删除");
		                $(this).closest("tbody").find("tr:last-child").before($cloneItem);
                        $cloneItem.find("#randomType,[name='bigPrizeType']").trigger("change");
	            	} else {
	            		$triggerObj = $(this).closest("tbody").find("input[name='prizePercent']:first");
	                    $(this).closest("tr").remove();
	                    $triggerObj.trigger("change");

	                    // 重新计算均价
	                    calculateUnit();
	            	}
	                
	                $(this).closest("tbody").find("tr").each(function(i, obj){
	                	$(this).find("#NO").text(i+1);
	                });
            	}
            });
            
            // 奖项模式切换
            $("form").on("change", "#randomType", function(){
            	// 随机模式
            	if ($(this).val() == "0") {
	            	$(this).closest("tr").find("div.random-prize").css("display", "inline-flex");
	            	$(this).closest("tr").find("div.fixation-prize").css("display", "none");
                    $(this).closest("tr").find("div.random-prize input[name^='max']").trigger("change");
	            	
	            // 固定模式
            	} else {
	            	$(this).closest("tr").find("div.random-prize").css("display", "none");
	            	$(this).closest("tr").find("div.fixation-prize").css("display", "inline-flex");
                    $(this).closest("tr").find("div.fixation-prize input").trigger("change");
            	}

            	// 模式切换时重新计算均价
                calculateUnit();
            });
            
            // 大奖类型切换
            $("form").on("change", "[name='bigPrizeType'], [name='scanNum']", function(){
                var $closestTr = $(this).closest("tr");
                if ($(this).attr("name") == "bigPrizeType") {
                    $closestTr.find("[name='prizePayMoney']").val("0.00");
                    $closestTr.find("[name='prizeDiscount']").val("0.00");
                }
                $closestTr.find("#cogAmounts").val("0");
                if ($closestTr.find("[name='bigPrizeType']").val() == ""
                            && $closestTr.find("[name='scanNum']").val() == "") {
                    $closestTr.find("#cogAmountsLabel").css("display", "");
                    $closestTr.find("#cogAmounts").css("display", "none");
                } else {
                    $closestTr.find("#cogAmountsLabel").css("display", "none");
                    $closestTr.find("#cogAmounts").css("display", "inline");
                }
                
                if ($closestTr.find("[name='scanNum']").val() == "") {
                    $closestTr.find("#prizePercentWarnLabel").css("display", "none");
                    $closestTr.find("#prizePercentWarnDiv").css("display", "inline-flex");
                } else {
                    $closestTr.find("#prizePercentWarnLabel").css("display", "");
                    $closestTr.find("#prizePercentWarnDiv").css("display", "none");
                }
                
                // 模式切换时重新计算均价
                $closestTr.find("input[name='prizePercent']").trigger("change");
            });
            
            // 津贴卡类型切换
            $("form").on("change", "[name='allowanceType']", function(){
                if ($(this).val() == "") {
                    $(this).closest("tr").find("[name='allowanceMoney']").val("0.00");
                }
            });
            
            // 计算均价-随机
            $("form").on("change", "div.random-prize input", function(){
                var suffix = $(this).attr("name").substr(3);
                var $minInput = $(this).closest("td").find("input[name='min" + suffix + "']");
                var $maxInput = $(this).closest("td").find("input[name='max" + suffix + "']");
                var minVal = Number($minInput.val());
                var maxVal = Number($maxInput.val());
                if (minVal > maxVal) {
                    maxVal = minVal;
                }
                if (suffix == 'Money') {
                    $minInput.val(minVal.toFixed(2));
                    $maxInput.val(maxVal.toFixed(2));
                } else {
                    $minInput.val(minVal);
                    $maxInput.val(maxVal);
                }
                $(this).closest("tr").find("#unit" + suffix).text(((minVal + maxVal) / 2).toFixed(2));

                // 模式切换时重新计算均价
                calculateUnit();
            });
            // 计算均价-固定
            $("form").on("change", "div.fixation-prize input", function(){
                var suffix = $(this).attr("name").substr(8);
                if (suffix == 'Money') {
                    $(this).val(Number($(this).val()).toFixed(2));
                }else {
                    $(this).val(Number($(this).val()));
                }
                $(this).closest("tr").find("#unit" + suffix).text((Number($(this).val())).toFixed(2));

                // 模式切换时重新计算均价
                calculateUnit();
            });

            // 计算均价-待激活红包
            $("form").on("change", "div.WaitActivation-random-prize input", calculateAveragePrice);
            
            // 修改奖项配置项占比
            $("form").on("change", "input[name='prizePercent']", function(){
                var scanNumPercent = 0; // 阶梯奖项总百分比
                var bigPrizePercent = 0; // 大奖奖项总百分比
                var commonPercent = 0; // 普通奖项总百分比
                $(this).closest("tbody").find("input[name='prizePercent']").each(function(i,obj){
                    console.log("scanNum:" + $(this).closest("tr").find("[name='scanNum']").val());
                    if($(this).closest("tr").find("[name='scanNum']").val() !== '') {
                        scanNumPercent += Number($(this).val());
                    } else if($(this).closest("tr").find("[name='bigPrizeType']").val()) {
                        bigPrizePercent += Number($(this).val());
                    } else {
                        commonPercent += Number($(this).val());
                    }
                });
                
                var currVal = Number($(this).val());
                if($(this).closest("tr").find("[name='scanNum']").val() === '') {
                    var currTotalPercent = $(this).closest("tr").find("[name='bigPrizeType']").val() !== '' ? bigPrizePercent : commonPercent;
                } else {
                    currTotalPercent = currVal;
                }
                if (currTotalPercent > 100) {
                    currVal = 100 - (currTotalPercent - currVal);
                }
                $(this).val(currVal.toFixed(4));
                
                var totalPercent = commonPercent;
                if (bigPrizePercent > 0 && commonPercent == 0) {
                    totalPercent = bigPrizePercent;
                }
                if (commonPercent == 0 && bigPrizePercent == 0 && scanNumPercent > 0) {
                    totalPercent = 100;
                }
                if (totalPercent > 100) totalPercent = 100;
                $(this).closest("tbody").find("#totalPrizePercent").text(totalPercent.toFixed(4) + "%").css("color", totalPercent.toFixed(4) == 100 ? "green" : "red");

                // 重新计算均价
                calculateUnit();
            });
            $("form").on("input", "input[name='prizePercent']", function(){
                if (Number($(this).val()) >= 100) {
                    $(this).trigger("change");
                }
            });

            $("form").on("change", "input[name='prizePercentWarn']", function(){
                if ($(this).val() != '') {
                    var currVal = Number($(this).val());
                    $(this).val(currVal.toFixed(4));
                }
            });
            
            // 奖项配置项
            $(".cogItemCB").on("change", function() {
                var optCogItem = $(this).attr("id");
                var $parentTable = $(this).closest("table.scan_type_table");
                $parentTable.find("." + optCogItem).css("display", $(this).is(':checked') ? "" : "none");
            });
            $(".cogItemCB").change();
            
         	// 翻倍返利类型切换
         	if('${ruleTemplet.allowanceaRebateType}' == '0'){
         		$("#allowanceMoneyTr").css("display", "");
                $("#allowanceVpointsTr").css("display", "none");
         	}else{
         		$("#allowanceMoneyTr").css("display", "none");
                $("#allowanceVpointsTr").css("display", "");
         	}
            $("input[name='allowanceaRebateType']").on("change", function(){
                if ($(this).val() == "0") {
                	$("#allowanceVpointsTr input").val("");
                    $("#allowanceMoneyTr").css("display", "");
                    $("#allowanceVpointsTr").css("display", "none");
                } else {
                	$("#allowanceMoneyTr input").val("");
                	$("#allowanceMoneyTr").css("display", "none");
                    $("#allowanceVpointsTr").css("display", "");
                }
            });
            
            // 初始化每个奖项配置项显示
            initPrizeItem();
		}
		
		// 初始化每个奖项配置项显示
		function initPrizeItem() {
		    $("table[id$='ScanPrize']").each(function(){
                var trLength = $(this).find("tbody tr").size();
                $(this).find("tbody tr").each(function(i, obj) {
                    if (i < trLength - 1) {
                    	$(this).find("#randomType").trigger("change");
                    	if ($(this).find("#randomType").val() == "0") {
                    		$(this).find("div.fixation-prize input[name$='Money']").data("oldval", "0.00").val("0.00")
                    		$(this).find("div.fixation-prize input[name$='Vpoints']").data("oldval", "0").val("0");
                    	} else {
                    		$(this).find("div.random-prize input[name$='Money']").data("oldval", "0.00").val("0.00")
                    		$(this).find("div.random-prize input[name$='Vpoints']").data("oldval", "0").val("0");
                    	}
                    	if ($(this).find("[name='bigPrizeType']").val() != "" 
                    			|| $(this).find("[name='scanNum']").val() != "") {
                    		$(this).find("#cogAmountsLabel").css("display", "none");
                    		$(this).find("#cogAmounts").css("display", "inline");
                    	}
                    	$(this).find("input[name='prizePercent']").val(Number($(this).find("input[name='prizePercent']").val()).toFixed(4));
                    	if (i != 0) {
                            $(this).find("#addPrizeItem").text("删除");
                        }
                    }
                });
                $(this).find("tbody tr").eq(0).find("input[name='prizePercent']").trigger("change");
            });
		    
		    // 初始化完成后打开计算开关
		    calculateUnitFlag = true;
		    calculateUnit();
            calculateAveragePrice();
		}

        function calculateAveragePrice() {
            var $target = $("form").find("div.WaitActivation-random-prize input");
            $target.each(function () {
                var suffix = $(this).attr("name").substr(3);
                var $minInput = $(this).closest("td").find("input[name='min" + suffix + "']");
                var $maxInput = $(this).closest("td").find("input[name='max" + suffix + "']");
                var minVal = Number($minInput.val());
                var maxVal = Number($maxInput.val());
                if (minVal > maxVal) {
                    maxVal = minVal;
                }
                $minInput.val(minVal.toFixed(2));
                $maxInput.val(maxVal.toFixed(2));
                $(this).closest("tr").find("#unit" + suffix).text(((minVal + maxVal) / 2).toFixed(2));
            });
        }
		
		// 计算单瓶成本及积分
		function calculateUnit() {
			if(!calculateUnitFlag) return;
			
			// 假设总瓶数
			var totalPrize = 10000000;
			var totalMoney = 0.00;
			var totalVpoints = 0.00;
			var scanTypeQrcodeNum = 0;
			
			var itemQrcodeNum = 0;
			var itemUnitMoney = 0.00;
			var itemUnitVpoints = 0.00;
			var itemPercent = 0;
			$("table[id$='ScanPrize']").each(function(){
				
				if ($(this).attr("id") == "firstScanPrize") {
					scanTypeQrcodeNum = (totalPrize * Number($("input[name='firstScanPercent']").val()) / 100).toFixed(0);
				} else {
					scanTypeQrcodeNum = (totalPrize * (100 - Number($("input[name='firstScanPercent']").val())) / 100).toFixed(0);
				}
				
				var trLength = $(this).find("tbody tr").size();
				$(this).find("tbody tr").each(function(i, obj) {
                    if (i < trLength - 1) {
                        itemUnitMoney = Number($(this).find("#unitMoney").text());
                        itemUnitVpoints = Number($(this).find("#unitVpoints").text());
                        itemPercent = Number($(this).find("input[name='prizePercent']").val());
                        
                        if($(this).find("[name='cogAmounts']").css("display") == "none") {
                            itemQrcodeNum = (scanTypeQrcodeNum * itemPercent / 100).toFixed(0);
                        } else {
                            itemQrcodeNum = Number($(this).find("#cogAmounts").val());
                        }
                        totalMoney = totalMoney + itemQrcodeNum * itemUnitMoney;
                        totalVpoints = totalVpoints + itemQrcodeNum * itemUnitVpoints;
                    }
                });
			});
			
			// 单价
			$("input[name='unitMoney']").val("≈" + (totalMoney/totalPrize).toFixed(2));
			$("input[name='unitVpoints']").val("≈" + (totalVpoints/totalPrize).toFixed(2));
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 校验首扫奖项配置项百分比
		    if (Number($("input[name='firstScanPercent']").val()) > 0) {
		    	if ($("#firstScanPrize").find("#totalPrizePercent").text() != "100.0000%") {
		    		$.fn.alert("首扫各奖项配置项占比之和未达到100%");
		    		return false;
		    	}
		    }
			
			// 校验普扫奖项配置项百分比
            if ($("#commonScanPrize").find("#totalPrizePercent").text() != "100.0000%") {
                $.fn.alert("普扫各奖项配置项占比之和未达到100%");
                return false;
            }

            //待激活红包必填校验
            var isRangeEmpty = false;
            $('select[name="waitActivationPrizeKey"]').each(function () {
                if ($(this).val()) {
                    var $thisRow = $(this).closest('tr');
                    var minMoney = $thisRow.find('input[name="minWaitActivationMoney"]').val()
                    var maxMoney = $thisRow.find('input[name="maxWaitActivationMoney"]').val()
                    if (parseFloat(minMoney) === 0 || parseFloat(maxMoney) === 0) {
                        isRangeEmpty = true;
                        return false;
                    }
                }
            });
            if (isRangeEmpty) {
                $.fn.alert("选择待激活红包后积分红包范围必填!");
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
            <li class="current"><a> 基础配置</a></li>
            <li class="current"><a>活动规则模板</a></li>
            <li class="current"><a title="">修改模板</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/rebateRuleTemplet/doRebateRuleTempletEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${ruleTemplet.infoKey}" />
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>规则基本信息</h4>
                        <span class="marl10" title="温馨提示" data-html="true" 
                                data-container="body" data-toggle="popover" data-placement="auto" 
                                data-content="
                                <b>1</b>.中出配置项每行为每次扫码中出的金额及积分以配置为准，只显示非0的金额及积分。</br>
                                <b>2</b>.奖项类型为随机时，会在最小值及最大值区间内随机中出一个值；金额精确到分，积分为整数。</br>
                                <b>3</b>.中出配置项中不会出现以4结尾的金额及积分。">
                            <i class="iconfont icon-tixing" style="color: red; font-size: 20px;"></i>
                        </span>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">模板名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="templetName" tag="validate" value="${ruleTemplet.templetName}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">单品金额成本：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="unitMoney" readonly="readonly" value="≈${ruleTemplet.unitMoney}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="15" />
	                       					<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">单品积分成本：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="unitVpoints" readonly="readonly" value="≈${ruleTemplet.unitVpoints}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="15" />
	                       					<span class="blocker en-larger">积分</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                                <td class="ab_left"><label class="title">翻倍区间类型<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="allowanceaRebateType" value="0" <c:if test="${ruleTemplet.allowanceaRebateType eq '0'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">金额</span>
                                        <input type="radio" class="tab-radio" name="allowanceaRebateType" value="1" <c:if test="${ruleTemplet.allowanceaRebateType eq '1'}">checked="checked"</c:if> style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="allowanceMoneyTr">
                                <td class="ab_left"><label class="title">翻倍红包区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="allowanceaMinMoney" tag="validate" value="${ruleTemplet.allowanceaMinMoney}"
                                            class="form-control number money preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="allowanceaMaxMoney" tag="validate" value="${ruleTemplet.allowanceaMaxMoney}"
                                            class="form-control number money sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="allowanceVpointsTr">
                                <td class="ab_left"><label class="title">翻倍积分区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="allowanceaMinVpoints" tag="validate" value="${ruleTemplet.allowanceaMinVpoints}"
                                            class="form-control number preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0" maxVal="9999"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="allowanceaMaxVpoints" tag="validate" value="${ruleTemplet.allowanceaMaxVpoints}"
                                            class="form-control number sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0" maxVal="9999"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">模板状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									    <input name="status" type="hidden" value="${ruleTemplet.status}" />
									    <input id="status" type="checkbox" <c:if test="${ruleTemplet.status eq '1'}">checked</c:if> data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>首次扫码规则</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table scan_type_table">
                            <tr>
                                <td class="ab_left"><label class="title">首扫占比：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="firstScanPercent" tag="validate" autocomplete="off" value="${ruleTemplet.firstScanPercent}"
                                            class="form-control input-width-larger number integer minValue maxValue required" minVal="0" maxVal="100"/>
                                            <span class="blocker en-larger">%</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出配置项：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemMoney" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;红包
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemWaitActivation" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" >&nbsp;待激活红包
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemVpoints" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;商城积分
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemPrize" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;实物
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemAllowance" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;津贴卡
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemLadder" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;阶梯
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemCard" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;集卡
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main" colspan="4">
	                                <table id="firstScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 95%; margin: 0 auto; text-align: center; overflow: auto !important">
			                            <thead>
			                                <tr>
			                                    <th style="width:5%; text-align: center;">序号</th>
			                                    <th style="width:8%; text-align: center;">中出类型</th>
                                                <th style="width:20%; text-align: center;" class="cogItemMoney">积分红包范围（元）</th>
                                                <th style="width:13%; text-align: center;" class="cogItemMoney">红包均价（元）</th>
                                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">选择待激活红包</th>
                                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">待激活积分红包范围(元)</th>
                                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">待激活积分红包均价(元)</th>
                                                <th style="width:20%; text-align: center;" class="cogItemVpoints">商城积分范围</th>
                                                <th style="width:11%; text-align: center;" class="cogItemVpoints">积分均价</th>
                                                <th style="width:24%; text-align: center;" class="cogItemPrize">实物</th>
                                                <th style="width:11%; text-align: center;" class="cogItemPrize">支付金额(元)</th>
                                                <th style="width:9%; text-align: center;" class="cogItemPrize">优惠折扣</th>
                                                <th style="width:24%; text-align: center;" class="cogItemAllowance">津贴卡</th>
                                                <th style="width:11%; text-align: center;" class="cogItemAllowance">卡券金额(元)</th>
                                                <th style="width:11%; text-align: center;" class="cogItemLadder">阶梯</th>
                                                <th style="width:15%; text-align: center;" class="cogItemCard">集卡</th>
			                                    <th style="width:11%; text-align: center;">中出占比</th>
                                                <th style="width:11%; text-align: center;">占比预警</th>
                                                <th style="width:11%; text-align: center;">投放个数</th>
			                                </tr>
			                            </thead>
			                            <tbody>
			                                <c:forEach items="${ruleTemplet.firstDetailLst }" var="detail" varStatus="idx">
	                                            <tr>
	                                                 <td>
	                                                    <label id="NO" style="line-height: 30px;">${idx.count}</label>
	                                                 </td>
	                                                 <td>
	                                                    <input type="hidden" name="scanType" value="0">
	                                                    <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial;">
	                                                        <option value="1" <c:if test="${detail.randomType eq '1'}">selected</c:if>>固定</option>
	                                                        <option value="0" <c:if test="${detail.randomType eq '0'}">selected</c:if>>随机</option>
	                                                    </select>
	                                                 </td>
	                                                 <td class="cogItemMoney">
	                                                    <div class="random-prize content" style="display: none;">
		                                                    <input type="text" name="minMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
		                                                           tag="validate" data-oldval="${detail.minMoney}" value="${detail.minMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
		                                                    <label style="margin-top: 4px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
		                                                    <input type="text" name="maxMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
	                                                                tag="validate" data-oldval="${detail.maxMoney}" value="${detail.maxMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
	                                                    </div>
	                                                    <div class="fixation-prize" style="display: inline-flex;">
	                                                        <input type="text" name="fixationMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
	                                                                tag="validate" data-oldval="${detail.minMoney}" value="${detail.minMoney}" maxVal="9999.99" maxlength="7" style="display: initial; width: 200px !important;">
	                                                    </div>
	                                                 </td>
	                                                 <td class="cogItemMoney">
	                                                    <label id="unitMoney" style="line-height: 30px;">0.00</label>
	                                                 </td>
                                                     <td class="cogItemWaitActivation">
                                                         <div class="content">
                                                             <select name="waitActivationPrizeKey" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                                 <option value="">请选择</option>
                                                                 <c:forEach items="${waitActivationPrize}" var="item">
                                                                     <option value="${item.prizeKey}" <c:if test="${detail.waitActivationPrizeKey eq item.prizeKey}">selected</c:if>>${item.prizeName}</option>
                                                                 </c:forEach>
                                                             </select>
                                                         </div>
                                                     </td>
                                                     <td class="cogItemWaitActivation"  >
                                                         <div class="WaitActivation-random-prize content" style="display: inline-flex">
                                                             <input type="text" name="minWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                                    tag="validate" data-oldval="${detail.waitActivationMinMoney}" value="${detail.waitActivationMinMoney}" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                                             <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                                             <input type="text" name="maxWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                                    tag="validate" data-oldval="${detail.waitActivationMaxMoney}" value="${detail.waitActivationMaxMoney}" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                                         </div>
                                                     </td>
                                                     <td class="cogItemWaitActivation">
                                                         <label id="unitWaitActivationMoney" style="line-height: 30px;">0.00</label>
                                                     </td>
	                                                 <td class="cogItemVpoints">
	                                                    <div class="random-prize content" style="display: none;">
		                                                    <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue" 
		                                                       autocomplete="off" tag="validate" data-oldval="${detail.minVpoints}" value="${detail.minVpoints}" maxVal="999999" maxlength="6" style="display: initial;">
		                                                    <label style="line-height: 30px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
		                                                    <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue" 
		                                                     autocomplete="off" tag="validate" data-oldval="${detail.maxVpoints}" value="${detail.maxVpoints}" maxVal="999999" maxlength="6" style="display: initial;">
	                                                    </div>
	                                                    <div class="fixation-prize" style="display: inline-flex;">
	                                                        <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off" 
	                                                                tag="validate" data-oldval="${detail.minVpoints}" value="${detail.minVpoints}" maxVal="999999" maxlength="6" style="display: initial; width: 200px !important;">
	                                                    </div>
	                                                 </td>
	                                                 <td class="cogItemVpoints">
	                                                    <label id="unitVpoints" style="line-height: 30px;">0.00</label>
	                                                 </td>
	                                                 <td class="cogItemPrize">
	                                                    <div class="content">
	                                                        <select name="bigPrizeType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
	                                                            <option value="">请选择</option>
	                                                            <c:forEach items="${prizeTypeMap}" var="item">
	                                                                <option value="${item.key}" <c:if test="${detail.prizeType eq item.key}">selected</c:if>>${item.value}</option>
	                                                            </c:forEach>
	                                                        </select>
	                                                    </div>
	                                                 </td>
	                                                 <td class="cogItemPrize">
	                                                    <input type="text" name="prizePayMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
	                                                                tag="validate" data-oldval="${detail.prizePayMoney}" value="${detail.prizePayMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
	                                                 </td>
	                                                 <td class="cogItemPrize">
					                                    <input type="text" name="prizeDiscount" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
					                                                tag="validate" data-oldval="${detail.prizeDiscount}" value="${detail.prizeDiscount}" maxVal="10.00" maxlength="5" style="display: initial;">
					                                 </td>
	                                                 <td class="cogItemAllowance">
	                                                    <div class="content">
	                                                        <select name="allowanceType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
	                                                            <option value="">请选择</option>
	                                                            <c:forEach items="${allowanceTypeMap}" var="item">
	                                                                <option value="${item.key}" <c:if test="${detail.allowanceType eq item.key}">selected</c:if>>${item.value}</option>
	                                                            </c:forEach>
	                                                        </select>
	                                                    </div>
	                                                 </td>
	                                                 <td class="cogItemAllowance">
	                                                    <input type="text" name="allowanceMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
	                                                                tag="validate" data-oldval="${detail.allowanceMoney}" value="${detail.allowanceMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
	                                                 </td>
	                                                 <td class="cogItemLadder">
	                                                    <input type="text" name="scanNum" class="form-control input-width-small" autocomplete="off" 
	                                                                tag="validate" value="${detail.scanNum}" style="display: initial;">
	                                                 </td>
	                                                 <td class="cogItemCard">
	                                                    <div class="content">
	                                                        <select name="cardNo" class="form-control input-width-small" style="display: initial;">
	                                                            <option value="">请选择</option>
	                                                            <c:forEach items="${cardTypeMap}" var="item">
	                                                                <option value="${item.key}" <c:if test="${detail.cardNo eq item.key}">selected</c:if>>${item.value}</option>
	                                                            </c:forEach>
	                                                        </select>
	                                                    </div>
	                                                 </td>
	                                                 <td style="position: relative;">
	                                                    <div style="display: inline-flex;">
	                                                        <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue" 
	                                                         autocomplete="off" tag="validate" data-oldval="${detail.prizePercent}" value="${detail.prizePercent}" maxVal="100" maxlength="7" style="display: initial; width: 65px !important;">
	                                                        <label style="line-height: 30px;">%</label>
	                                                    </div>
	                                                 </td>
                                                     <td style="position: relative;">
	                                                    <label id="prizePercentWarnLabel" style="line-height: 30px; min-width: 55px; display: none;">--</label>
	                                                    <div id="prizePercentWarnDiv" style="display: inline-flex;">
	                                                        <input type="text" name="prizePercentWarn" class="form-control input-width-small number maxValue" 
	                                                         autocomplete="off" tag="validate" value="${detail.prizePercentWarn}" maxVal="100" maxlength="7" style="display: initial; width: 65px !important;">
	                                                        <label style="line-height: 30px;">%</label>
	                                                    </div>
                                                     </td>
	                                                 <td style="position: relative;">
	                                                    <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">--</label>
	                                                    <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="${detail.cogAmounts}" value="${detail.cogAmounts}" maxlength="10" autocomplete="off"  style="display: none; width: 60px !important;">
	                                                    <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
	                                                 </td>
	                                            </tr>
			                                </c:forEach>
				                            <tr>
				                                 <td>合计</td>
				                                 <td>--</td>
                                                 <td class="cogItemMoney">--</td>
                                                 <td class="cogItemMoney">--</td>
                                                 <td class="cogItemWaitActivation">--</td>
                                                 <td class="cogItemWaitActivation">--</td>
                                                 <td class="cogItemWaitActivation">--</td>
                                                 <td class="cogItemVpoints">--</td>
                                                 <td class="cogItemVpoints">--</td>
                                                 <td class="cogItemPrize">--</td>
                                                 <td class="cogItemPrize">--</td>
                                                 <td class="cogItemPrize">--</td>
                                                 <td class="cogItemAllowance">--</td>
                                                 <td class="cogItemAllowance">--</td>
                                                 <td class="cogItemLadder">--</td>
                                                 <td class="cogItemCard">--</td>
				                                 <td><span id="totalPrizePercent" data-currval="0" style="font-weight: bold;">0%</span></td>
                                                 <td>--</td>
                                                 <td>--</td>
				                            </tr>
			                            </tbody>
	                                </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>普通扫码规则</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table scan_type_table">
                            <tr>
                                <td class="ab_left"><label class="title">普扫占比：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="commonScanPercent" tag="validate" autocomplete="off" value="${ruleTemplet.commonScanPercent}" readonly="readonly"
                                            class="form-control input-width-larger number integer minValue maxValue required" minVal="0" maxVal="100"/>
                                            <span class="blocker en-larger">%</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出配置项：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemMoney" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;红包
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemWaitActivation" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" >&nbsp;待激活红包
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemVpoints" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;商城积分
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemPrize" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;实物
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemAllowance" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;津贴卡
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemLadder" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;阶梯
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="checkbox" id="cogItemCard" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;集卡
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main" colspan="4">
                                    <table id="commonScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 95%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                            <tr>
                                                <th style="width:5%; text-align: center;">序号</th>
                                                <th style="width:8%; text-align: center;">红包类型</th>
                                                <th style="width:20%; text-align: center;" class="cogItemMoney">积分红包范围（元）</th>
                                                <th style="width:13%; text-align: center;" class="cogItemMoney">红包均价（元）</th>
                                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">选择待激活红包</th>
                                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">待激活积分红包范围(元)</th>
                                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">待激活积分红包均价(元)</th>
                                                <th style="width:20%; text-align: center;" class="cogItemVpoints">商城积分范围</th>
                                                <th style="width:11%; text-align: center;" class="cogItemVpoints">积分红包均价</th>
                                                <th style="width:24%; text-align: center;" class="cogItemPrize">实物</th>
                                                <th style="width:11%; text-align: center;" class="cogItemPrize">支付金额(元)</th>
                                                <th style="width:9%;  text-align: center;" class="cogItemPrize">优惠折扣</th>
                                                <th style="width:24%; text-align: center;" class="cogItemAllowance">津贴卡</th>
                                                <th style="width:11%; text-align: center;" class="cogItemAllowance">卡券金额(元)</th>
                                                <th style="width:11%; text-align: center;" class="cogItemLadder">阶梯</th>
                                                <th style="width:15%; text-align: center;" class="cogItemCard">集卡</th>
                                                <th style="width:11%; text-align: center;">中出占比</th>
                                                <th style="width:11%; text-align: center;">占比预警</th>
                                                <th style="width:11%; text-align: center;">投放个数</th>
                                            </tr>
                                        </thead>
                                        <tbody>
	                                        <c:forEach items="${ruleTemplet.commonDetailLst }" var="detail" varStatus="idx">
	                                            <tr>
		                                            <td>
		                                                <label id="NO" style="line-height: 30px;">${idx.count}</label>
		                                            </td>
		                                            <td>
		                                               <input type="hidden" name="scanType" value="1">
		                                               <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial;">
		                                                <option value="1" <c:if test="${detail.randomType eq '1'}">selected</c:if>>固定</option>
		                                                <option value="0" <c:if test="${detail.randomType eq '0'}">selected</c:if>>随机</option>
		                                               </select>
		                                            </td>
		                                            <td class="cogItemMoney">
		                                               <div class="random-prize content" style="display: none;">
		                                                   <input type="text" name="minMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
		                                                          tag="validate" data-oldval="${detail.minMoney}" value="${detail.minMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
		                                                   <label style="margin-top: 4px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
		                                                   <input type="text" name="maxMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
		                                                           tag="validate" data-oldval="${detail.maxMoney}" value="${detail.maxMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
		                                               </div>
		                                               <div class="fixation-prize" style="display: inline-flex;">
		                                                   <input type="text" name="fixationMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
		                                                           tag="validate" data-oldval="${detail.minMoney}" value="${detail.minMoney}" maxVal="9999.99" maxlength="7" style="display: initial; width: 200px !important;">
		                                               </div>
		                                            </td>
		                                            <td class="cogItemMoney">
		                                               <label id="unitMoney" style="line-height: 30px;">0.00</label>
		                                            </td>
                                                    <td class="cogItemWaitActivation">
                                                        <div class="content">
                                                            <select name="waitActivationPrizeKey" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                                <option value="">请选择</option>
                                                                <c:forEach items="${waitActivationPrize}" var="item">
                                                                    <option value="${item.prizeKey}" <c:if test="${detail.waitActivationPrizeKey eq item.prizeKey}">selected</c:if>>${item.prizeName}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                    </td>
                                                    <td class="cogItemWaitActivation"  >
                                                        <div class="WaitActivation-random-prize content" style="display: inline-flex">
                                                            <input type="text" name="minWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                                   tag="validate" data-oldval="${detail.waitActivationMinMoney}" value="${detail.waitActivationMinMoney}" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                                            <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                                            <input type="text" name="maxWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                                   tag="validate" data-oldval="${detail.waitActivationMaxMoney}" value="${detail.waitActivationMaxMoney}" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                                        </div>
                                                    </td>
                                                    <td class="cogItemWaitActivation">
                                                        <label id="unitWaitActivationMoney" style="line-height: 30px;">0.00</label>
                                                    </td>
		                                            <td class="cogItemVpoints">
		                                               <div class="random-prize content" style="display: none;">
		                                                   <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue" 
		                                                      autocomplete="off" tag="validate" data-oldval="${detail.minVpoints}" value="${detail.minVpoints}" maxVal="999999" maxlength="6" style="display: initial;">
		                                                   <label style="line-height: 30px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
		                                                   <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue" 
		                                                    autocomplete="off" tag="validate" data-oldval="${detail.maxVpoints}" value="${detail.maxVpoints}" maxVal="999999" maxlength="6" style="display: initial;">
		                                               </div>
		                                               <div class="fixation-prize" style="display: inline-flex;">
		                                                   <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off" 
		                                                           tag="validate" data-oldval="${detail.minVpoints}" value="${detail.minVpoints}" maxVal="999999" maxlength="6" style="display: initial; width: 200px !important;">
		                                               </div>
		                                            </td>
		                                            <td class="cogItemVpoints">
		                                               <label id="unitVpoints" style="line-height: 30px;">0.00</label>
		                                            </td>
                                                     <td class="cogItemPrize">
                                                        <div class="content">
                                                            <select name="bigPrizeType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                                <option value="">请选择</option>
                                                                <c:forEach items="${prizeTypeMap}" var="item">
                                                                    <option value="${item.key}" <c:if test="${detail.prizeType eq item.key}">selected</c:if>>${item.value}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                     </td>
                                                     <td class="cogItemPrize">
                                                        <input type="text" name="prizePayMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
                                                                    tag="validate" data-oldval="${detail.prizePayMoney}" value="${detail.prizePayMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
                                                     </td>
                                                     <td class="cogItemPrize">
					                                    <input type="text" name="prizeDiscount" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
					                                                tag="validate" data-oldval="${detail.prizeDiscount}" value="${detail.prizeDiscount}" maxVal="10.00" maxlength="5" style="display: initial;">
					                                 </td>
                                                     <td class="cogItemAllowance">
                                                        <div class="content">
                                                            <select name="allowanceType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                                <option value="">请选择</option>
                                                                <c:forEach items="${allowanceTypeMap}" var="item">
                                                                    <option value="${item.key}" <c:if test="${detail.allowanceType eq item.key}">selected</c:if>>${item.value}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                     </td>
                                                     <td class="cogItemAllowance">
                                                        <input type="text" name="allowanceMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
                                                                    tag="validate" data-oldval="${detail.allowanceMoney}" value="${detail.allowanceMoney}" maxVal="9999.99" maxlength="7" style="display: initial;">
                                                     </td>
                                                     <td class="cogItemLadder">
                                                        <input type="text" name="scanNum" class="form-control input-width-small" autocomplete="off" 
                                                                    tag="validate" value="${detail.scanNum}" style="display: initial;">
                                                     </td>
                                                     <td class="cogItemCard">
                                                        <div class="content">
                                                            <select name="cardNo" class="form-control input-width-small" style="display: initial; width: 100% !important;">
                                                                <option value="">请选择</option>
                                                                <c:forEach items="${cardTypeMap}" var="item">
                                                                    <option value="${item.key}" <c:if test="${detail.cardNo eq item.key}">selected</c:if>>${item.value}</option>
                                                                </c:forEach>
                                                            </select>
                                                        </div>
                                                     </td>
                                                     <td style="position: relative;">
                                                        <div style="display: inline-flex;">
                                                            <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue" 
                                                             autocomplete="off" tag="validate" data-oldval="${detail.prizePercent}" value="${detail.prizePercent}" maxVal="100" maxlength="7" style="display: initial; width: 65px !important;">
                                                            <label style="line-height: 30px;">%</label>
                                                        </div>
                                                     </td>
                                                     <td style="position: relative;">
                                                        <label id="prizePercentWarnLabel" style="line-height: 30px; min-width: 55px; display: none;">--</label>
                                                        <div id="prizePercentWarnDiv" style="display: inline-flex;">
                                                            <input type="text" name="prizePercentWarn" class="form-control input-width-small number maxValue" 
                                                             autocomplete="off" tag="validate" value="${detail.prizePercentWarn}" maxVal="100" maxlength="7" style="display: initial; width: 65px !important;">
                                                            <label style="line-height: 30px;">%</label>
                                                        </div>
                                                     </td>
                                                     <td style="position: relative;">
                                                        <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">--</label>
                                                        <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="${detail.cogAmounts}" value="${detail.cogAmounts}" maxlength="10" autocomplete="off"  style="display: none; width: 60px !important;">
                                                        <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                     </td>
	                                            </tr>
	                                        </c:forEach>
                                            <tr>
                                                 <td>合计</td>
                                                 <td>--</td>
                                                 <td class="cogItemMoney">--</td>
                                                 <td class="cogItemMoney">--</td>
                                                 <td class="cogItemWaitActivation">--</td>
                                                 <td class="cogItemWaitActivation">--</td>
                                                 <td class="cogItemWaitActivation">--</td>
                                                 <td class="cogItemVpoints">--</td>
                                                 <td class="cogItemVpoints">--</td>
                                                 <td class="cogItemPrize">--</td>
                                                 <td class="cogItemPrize">--</td>
                                                 <td class="cogItemPrize">--</td>
                                                 <td class="cogItemAllowance">--</td>
                                                 <td class="cogItemAllowance">--</td>
                                                 <td class="cogItemLadder">--</td>
                                                 <td class="cogItemCard">--</td>
                                                 <td><span id="totalPrizePercent" data-currval="0" style="font-weight: bold;">0%</span></td>
                                                 <td>--</td>
                                                 <td>--</td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/rebateRuleTemplet/showRebateRuleTempletList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
