<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%String cpath = request.getContextPath(); 
String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
    
<script>
			
	$(function(){
        
        // 为减少初始化时的计算量，默认为false
        calculateUnitFlag = false;
        initPrizeItemFlag = false;
        
        // 初始化校验控件
        $.runtimeValidate($("form"));
        
        // 初始化功能
        initRuleTempletPage();
        
        // 初始化完成后，打开计算开关
        calculateUnitFlag = true;
    });
    
    function initRuleTempletPage() {
    	
    	// 金额格式
    	$("form").on("change", ".moneyFmt", function(){
    		$(this).val(Number($(this).val()).toFixed(2));
        });
    	
        // 奖项配置项
        $("form").on("change", ".cogItemCB", function(){
            var optCogItem = $(this).attr("id");
            var $parentTable = $(this).closest("table.scan_type_table");
            $parentTable.find("." + optCogItem).css("display", $(this).is(':checked') ? "" : "none");
        });
        $(".cogItemCB").change();

        // 待激活红包提示
        $("form").on("change",'#cogItemWaitActivation', function() {
            console.info("---------待激活红包")
            var $parentTable = $(this).closest('table.scan_type_table');
            console.info($parentTable);
            $parentTable.find(".waitActivationTips").css("display", $(this).is(':checked') ? "" : "none");
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
                $.fn.alert($scanPercent.attr("name") == "firstScanPercent" ? "请先配置首扫占比" : "请先配置普扫占比");
                
            } else {
                if ($(this).closest("tr").find("#NO").text() == "1") {
                    var $cloneItem = $(this).closest("tr").clone(true, true);
                    $cloneItem.find("input[name$='Money']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name$='Vpoints']").data("oldval", "0").val("0");
                    $cloneItem.find("input[name$='Percent']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("label[id^='unit'], label[id$='TotalMoney'], label[id$='TotalVpoints']").text("0.00");
                    $cloneItem.find("input[name='scanNum'],input[name='bigPrizeType'],input[name='cardNo'],input[name='prizePercentWarn']").val("");
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
            $closestTr.find("#cogAmountsLabel").text("0");
            $closestTr.find("[name='cogAmounts']").val("0");
            if ($closestTr.find("[name='bigPrizeType']").val() == ""
                && $closestTr.find("[name='scanNum']").val() == "") {
                $closestTr.find("#cogAmountsLabel").css("display", "");
                $closestTr.find("[name='cogAmounts']").css("display", "none");
            } else {
                $closestTr.find("#cogAmountsLabel").css("display", "none");
                $closestTr.find("[name='cogAmounts']").css("display", "inline");
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

        // 计算均价-待激活红包
        $("form").on("change", "div.WaitActivation-random-prize input", function () {
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

            // 计算成本
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
        
        // 修改奖项配置项占比
        $("form").on("change", "input[name='prizePercent']", function(){
        	if (initPrizeItemFlag) return;
            var scanNumPercent = 0; // 阶梯奖项总百分比
            var bigPrizePercent = 0; // 大奖奖项总百分比
            var commonPercent = 0; // 普通奖项总百分比
            $(this).closest("tbody").find("input[name='prizePercent']").each(function(i,obj){
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
        
        // 奖项总数
        $("input[name='ruleTotalPrize']").on("input", function(){
        	// 重新计算均价
            calculateUnit();
        });
        
        // 选择配置方案
        $("#rebateRuleTemplet").on("change", function(){
        	
        	// 校验翻倍红包区间
        	$currOption = $(this).find("option:selected");
        	var allowanceaRebateType = $currOption.data("allowancearebatetype");
        	var allowanceaRebateTypeForRule = $("input[name='allowanceaRebateType']").val();
        	if("0" == allowanceaRebateType){
        		if(allowanceaRebateType == allowanceaRebateTypeForRule){
        			var allowanceaMinMoney = $currOption.data("allowanceaminmoney");
                	var allowanceaMaxMoney = $currOption.data("allowanceamaxmoney");
                	if ($("input[name='allowanceaMinMoney']").val() != "" 
                			&& $("input[name='allowanceaMaxMoney']").val() != "") {
                		if ($("input[name='allowanceaMinMoney']").val() != allowanceaMinMoney
                				|| $("input[name='allowanceaMaxMoney']").val() != allowanceaMaxMoney) {
                			$.fn.alert("所选模板的翻倍金额区间为：" + allowanceaMinMoney + "-" + allowanceaMaxMoney + ", 与规则翻倍配置不一致");
                		}
                	}else {
                		$("input[name='allowanceaMinMoney']").val(allowanceaMinMoney);
                		$("input[name='allowanceaMaxMoney']").val(allowanceaMaxMoney);
                	}	
        		}else{
        			$.fn.alert("翻倍规则不一致：所选模板为翻倍金额区间，规则设置为翻倍积分区间");
        		}
        		
        	} else if("1" == allowanceaRebateType){
        		if(allowanceaRebateType == allowanceaRebateTypeForRule){
        			var allowanceaMinVpoints = $currOption.data("allowanceaminvpoints");
                	var allowanceaMaxVpoints = $currOption.data("allowanceamaxvpoints");
                	if ($("input[name='allowanceaMinVpoints']").val() != "" 
                			&& $("input[name='allowanceaMaxVpoints']").val() != "") {
                		if ($("input[name='allowanceaMinVpoints']").val() != allowanceaMinVpoints
                				|| $("input[name='allowanceaMaxVpoints']").val() != allowanceaMaxVpoints) {
                			$.fn.alert("所选模板的翻倍积分区间为：" + allowanceaMinVpoints + "-" + allowanceaMaxVpoints + ", 与规则翻倍配置不一致");
                		}
                	}else {
                		$("input[name='allowanceaMinVpoints']").val(allowanceaMinVpoints);
                		$("input[name='allowanceaMaxMoney']").val(allowanceaMaxMoney);
                	}
        		}else{
        			$.fn.alert("翻倍规则不一致：所选模板为翻倍积分区间，规则设置为翻倍金额区间");
        		}
        		
        	}
        	
        	
        	// 重置奖项配置项
            resetPrizeItem();
        	
        	// 获取当前规则方案
            var key = $(this).val();
        	if (key != "" && key != "请选择") {
	            var url = "<%=cpath%>/vcodeActivityRebateRule/findRebateRuleTemplet.do";
	            $.ajax({
	                type: "POST",
	                url: url,
	                data: {templetKey : key},
	                dataType: "json",
	                async: false,
	                beforeSend:appendVjfSessionId,
                    success:  function(data) {
	                    var ruleTemplet = data['ruleTemplet'];
	                    
	                    // 开始初始化文案
	                    initPrizeItemFlag = true;
	                    
	                    // 首扫占比
	                    $("input[name='firstScanPercent']").val(ruleTemplet["firstScanPercent"]);
	                    $("input[name='firstScanPercent']").trigger("input");
	                    
	                    // 首扫
	                    $.each(ruleTemplet['firstDetailLst'], function(i, item){
	                        initPrizeItem($("#firstScanPrize"), item, i);
	                    });
	                    
	                    // 普扫
	                    $.each(ruleTemplet['commonDetailLst'], function(i, item){
	                        initPrizeItem($("#commonScanPrize"), item, i);
	                    });

	                    // 初化显示列表
	                    $(".cogItemCB").change();
	                    
	                    // 初始化结束
	                    initPrizeItemFlag = false;
	                    
	                    // 计算总的百分比
                        $("#commonScanPrize").find("tbody tr").eq(0).find("input[name='prizePercent']").trigger("change");
	                    $("#firstScanPrize").find("tbody tr").eq(0).find("input[name='prizePercent']").trigger("change");
	
	                    // 初始化完成后打开计算开关
	                    calculateUnitFlag = true;
	                    calculateUnit();
	                }
	            });
        	}
        });
    }
    
    // 初始化每个奖项配置项显示
    function initPrizeItem($parentTable, templetDetail, itemIndex) {
    	// 增加一行
    	if (itemIndex > 0) {
	    	$parentTable.find("#addPrizeItem:first").trigger("click");
    	}
    	
    	// 赋值
        var $currTr = $parentTable.find("tbody tr").eq(itemIndex);
        $currTr.find("#randomType").val(templetDetail["randomType"]).trigger("change");
        if (templetDetail["randomType"] == "0") {
        	$currTr.find("div.random-prize input[name='minMoney']").data("oldval", templetDetail["minMoney"]).val(templetDetail["minMoney"]);
        	$currTr.find("div.random-prize input[name='maxMoney']").data("oldval", templetDetail["maxMoney"]).val(templetDetail["maxMoney"]);
        	$currTr.find("div.random-prize input[name='maxMoney']").trigger("change");
            $currTr.find("div.random-prize input[name='minVpoints']").data("oldval", templetDetail["minVpoints"]).val(templetDetail["minVpoints"]);
            $currTr.find("div.random-prize input[name='maxVpoints']").data("oldval", templetDetail["maxVpoints"]).val(templetDetail["maxVpoints"]);
            $currTr.find("div.random-prize input[name='maxVpoints']").trigger("change");
        } else {
        	$currTr.find("div.fixation-prize input[name='fixationMoney']").data("oldval", templetDetail["minMoney"]).val(templetDetail["minMoney"]);
        	$currTr.find("div.fixation-prize input[name='fixationMoney']").trigger("change");
            $currTr.find("div.fixation-prize input[name='fixationVpoints']").data("oldval", templetDetail["minVpoints"]).val(templetDetail["minVpoints"]);
            $currTr.find("div.fixation-prize input[name='fixationVpoints']").trigger("change");
        }
        if ($currTr.find("[name='bigPrizeType']").find("option[value='" + templetDetail["prizeType"] + "']").size() == 1) {
	        $currTr.find("[name='bigPrizeType']").find("option[value='" + templetDetail["prizeType"] + "']").attr("selected", "selected");
        } else {
            $currTr.find("[name='bigPrizeType']").val("");
        }
        $currTr.find("[name='prizePayMoney']").val(templetDetail["prizePayMoney"]).trigger("change");
        $currTr.find("[name='prizeDiscount']").val(templetDetail["prizeDiscount"]).trigger("change");
        $currTr.find("[name='allowanceType']").find("option[value=" + templetDetail["allowanceType"] + "]").attr("selected", "selected");
        $currTr.find("[name='allowanceMoney']").val(templetDetail["allowanceMoney"]).trigger("change");
        $currTr.find("[name='scanNum']").val(templetDetail["scanNum"]).trigger("change");
        $currTr.find("[name='cardNo']").find("option[value=" + templetDetail["cardNo"] + "]").attr("selected", "selected");
        $currTr.find("[name='cogAmounts']").val(templetDetail["cogAmounts"]);
        $currTr.find("#prizePercent").val(Number(templetDetail["prizePercent"]).toFixed(4));
        if (templetDetail["prizePercentWarn"]) {
	        $currTr.find("[name='prizePercentWarn']").val(Number(templetDetail["prizePercentWarn"]).toFixed(4));
        }

        if (templetDetail["isScanqrcodeWaitActivation"] == 1) {
            $currTr.find("[name='waitActivationPrizeKey']").find("option[value='" + templetDetail["waitActivationPrizeKey"] + "']").attr("selected", "selected");
            $currTr.find("div.WaitActivation-random-prize input[name='minWaitActivationMoney']").data("oldval", templetDetail["waitActivationMinMoney"]).val(templetDetail["waitActivationMinMoney"]);
            $currTr.find("div.WaitActivation-random-prize input[name='maxWaitActivationMoney']").data("oldval", templetDetail["waitActivationMaxMoney"]).val(templetDetail["waitActivationMaxMoney"]);
            $currTr.find("div.WaitActivation-random-prize input[name='maxWaitActivationMoney']").trigger("change");
        }else{
            $currTr.find("[name='waitActivationPrizeKey']").find("option[value='']").attr("selected", "selected");
        }
    }
    
    // 重置奖项配置项
    function resetPrizeItem() {
        // 初始化完成后打开计算开关
        calculateUnitFlag = false;
    	
    	// 初始化默认奖项配置项
    	var $cloneItem = $("#firstScanPrize").find("tbody tr:first").clone(true, true);
        $cloneItem.find("input[name$='Money']").data("oldval", "0.00").val("0.00");
        $cloneItem.find("input[name$='Vpoints']").data("oldval", "0").val("0");
        $cloneItem.find("input[name$='Percent']").data("oldval", "0.00").val("0.00");
        $cloneItem.find("input[name='scanNum'],input[name='bigPrizeType'],input[name='cardNo']").val("");
        $cloneItem.find("label[id^='unit'], label[id$='TotalMoney'], label[id$='TotalVpoints']").text("0.00");
        $cloneItem.find("label[id$='QrcodeNum']").text("0");
        $cloneItem.find("#addPrizeItem").text("新增");
        
        // 首扫
        $cloneItem.find("input[name='scanType']").val("0");
        $("input[name='firstScanPercent']").val("0");
        $("input[name='firstScanPercent']").trigger("input");
        $("#firstScanPrize").find("tbody tr:lt(" + ($("#firstScanPrize tbody tr").size() -1) + ")").remove();
        $("#firstScanPrize").find("tbody tr:last-child").before($cloneItem.clone(true, true));
        $("#firstScanPrize").find("tbody tr:first").find("#prizePercent").trigger("change");
        
        // 普扫
        $cloneItem.find("input[name='scanType']").val("1");
        $("#commonScanPrize").find("tbody tr:lt(" + ($("#commonScanPrize tbody tr").size() -1) + ")").remove();
        $("#commonScanPrize").find("tbody tr:last-child").before($cloneItem.clone(true, true));
        $("#commonScanPrize").find("tbody tr:first").find("#prizePercent").trigger("change");
    }
		
    // 计算单瓶成本及积分
	function calculateUnit() {
	    if(!calculateUnitFlag) return;
	    
	    // 假设总瓶数
	    var totalPrize = $("input[name='ruleTotalPrize']").val() ? $("input[name='ruleTotalPrize']").val() : 0;
	    var totalMoney = 0.00;
	    var totalVpoints = 0.00;
	    var scanTypeQrcodeNum = 0;
	    
        var itemTotalMoney = 0.00;
        var itemTotalVpoints = 0.00;
        var itemQrcodeNum = 0;
        var itemMoney = 0.00;
        var itemVpoints = 0.00;
        var itemWaitActivationMoney = 0.00;
	    var itemUnitMoney = 0.00;
	    var itemUnitVpoints = 0.00;
	    var itemUnitWaitActivationMoney = 0.00;
	    var itemPercent = 0;
	    $("table[id$='ScanPrize']").each(function(){
            itemTotalMoney = 0.00;
            itemTotalVpoints = 0;
	        
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
	                itemUnitWaitActivationMoney = Number($(this).find("#unitWaitActivationMoney").text());
	                itemPercent = Number($(this).find("input[name='prizePercent']").val());
                    
	                if($(this).find("[name='cogAmounts']").css("display") == "none") {
	                    itemQrcodeNum = (scanTypeQrcodeNum * itemPercent / 100).toFixed(0);
	                } else {
                        itemQrcodeNum = Number($(this).find("[name='cogAmounts']").val());
	                }
	                itemMoney = itemQrcodeNum * itemUnitMoney;
	                itemVpoints = itemQrcodeNum * itemUnitVpoints;
	                itemWaitActivationMoney = itemQrcodeNum * itemUnitWaitActivationMoney;
	                $(this).find("#itemTotalMoney").text(itemMoney.toFixed(2));
	                $(this).find("#itemTotalVpoints").text(itemVpoints.toFixed(2));
	                $(this).find("#itemTotalWaitActivationMoney").text(itemWaitActivationMoney.toFixed(2));
	                $(this).find("#cogAmountsLabel").text(itemQrcodeNum);
	                $(this).find("[name='cogAmounts']").val(itemQrcodeNum);
	                itemTotalMoney += itemMoney;
	                itemTotalVpoints += itemVpoints;
	            }
	        });
	        $(this).closest("tbody").find("[id$='ScanQrcodeNum']").val((scanTypeQrcodeNum + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,'));
	        $(this).closest("tbody").find("[id$='ScanTotalMoney']").val(((itemTotalMoney).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,'));
	        $(this).closest("tbody").find("[id$='ScanTotalVpoints']").val(((itemTotalVpoints).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,'));
	        
            totalMoney += itemTotalMoney;
            totalVpoints += itemTotalVpoints;
	    });
	    
	    // 单价
	    $("input[name='ruleTotalMoney']").val("≈" + ((totalMoney).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,'));
	    $("input[name='ruleUnitMoney']").val("≈" + (totalMoney/totalPrize).toFixed(2));
	    $("input[name='ruleTotalVpoints']").val("≈" + ((totalVpoints).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,'));
	    $("input[name='ruleUnitVpoints']").val("≈" + (totalVpoints/totalPrize).toFixed(2));
	}
    
	function validVpoints() {
        
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
        
        return true;
    }
</script>

  
<!--                         	预览配置方案内容 -->
<div class="widget box" style="margin: 0px;">
	<div class="widget-header">
	    <h4><i class="iconfont icon-xinxi"></i>规则基本信息</h4>
	</div>
	<div class="widget-content panel no-padding">
	    <table class="active_board_table">
	        <tr>
	            <td class="ab_left"><label class="title">总金额成本：<span class="required">*</span></label></td>
	            <td class="ab_main" style="width: 25% !important;">
	                <div class="content">
	                    <input name="ruleTotalMoney" readonly="readonly" value="≈0.00" 
	                        class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
	                        <span class="blocker en-larger">元</span>
	                </div>
	            </td>
	            <td class="ab_left"><label>单品金额成本：<span class="required">*</span></label></td>
	            <td class="ab_main">
	                <div class="content">
	                    <input name="ruleUnitMoney" readonly="readonly" value="≈0.00" 
                            class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
	                        <span class="blocker en-larger">元</span>
	                </div>
	            </td>
	        </tr>
	        <tr>
	            <td class="ab_left"><label class="title">总积分成本：<span class="required">*</span></label></td>
	            <td class="ab_main" style="width: 30% !important;">
	                <div class="content">
	                    <input name="ruleTotalVpoints" readonly="readonly" value="≈0.00" 
                            class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
	                        <span class="blocker en-larger">积分</span>
	                </div>
	            </td>
	            <td class="ab_left"><label class="title">单品积分成本：<span class="required">*</span></label></td>
	            <td class="ab_main">
	                <div class="content">
	                    <input name="ruleUnitVpoints" readonly="readonly" value="≈0.00" 
                            class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
	                        <span class="blocker en-larger">积分</span>
	                </div>
	            </td>
	        </tr>
	    </table>
	</div>
    <!---转盘抽奖不配置首扫规则--------->

             <div class="widget-header"  <c:if test="${activityType eq '8' || activityType eq '13'}">style="display: none"</c:if>>
                 <h4><i class="iconfont icon-xinxi"></i>首次扫码规则</h4>
             </div>
             <div class="widget-content panel no-padding" <c:if test="${activityType eq '8' || activityType eq '13'}">style="display: none"</c:if>>
                 <table class="active_board_table scan_type_table">
                     <tr>
                         <td class="ab_left"><label class="title">首扫占比：<span class="required">*</span></label></td>
                         <td class="ab_main" style="width: 30% !important;">
                             <div class="content">
                                 <input name="firstScanPercent" tag="validate" autocomplete="off" value="0" style="width: 160px !important;"
                                        class="form-control input-width-larger number integer minValue maxValue required" minVal="0" maxVal="100"/>
                                 <span class="blocker en-larger">%</span>
                                 <label class="validate_tips"></label>
                             </div>
                         </td>
                         <td class="ab_left"><label class="title">首扫码数：<span class="required">*</span></label></td>
                         <td class="ab_main">
                             <div class="content">
                                 <input id="firstScanQrcodeNum"  value="0" readonly="readonly"
                                        class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
                                 <span class="blocker en-larger">个</span>
                             </div>
                         </td>
                     </tr>
                     <tr>
                         <td class="ab_left"><label class="title">首扫总金额：<span class="required">*</span></label></td>
                         <td class="ab_main" style="width: 25% !important;">
                             <div class="content">
                                 <input id="firstScanTotalMoney" readonly="readonly" value="≈0.00"
                                        class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
                                 <span class="blocker en-larger">元</span>
                             </div>
                         </td>
                         <td class="ab_left"><label class="title">首扫总积分：<span class="required">*</span></label></td>
                         <td class="ab_main">
                             <div class="content">
                                 <input id="firstScanTotalVpoints" readonly="readonly" value="≈0.00"
                                        class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold; width: 160px !important;"/>
                                 <span class="blocker en-larger">积分</span>
                             </div>
                         </td>
                     </tr>
                     <tr>
                         <td class="ab_left"><label class="title">中出配置项：<span class="required"> </span></label></td>
                         <td class="ab_main" colspan="3">
                             <div style="float: left; width: 120px; line-height: 25px;">
                                 <input type="checkbox" id="cogItemMoney" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;积分红包
                             </div>
                             <c:if test="${activityType eq '0'}">
                             <div style="float: left; width: 120px; line-height: 25px;">
                                 <input type="checkbox" id="cogItemWaitActivation" class="cogItemCB" style="float: left; height:20px; cursor: pointer;">&nbsp;待激活红包
                             </div>
                             </c:if>
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
                     <tr class="waitActivationTips" style="display: none">
                         <td class="ab_left"><label class="title" style="color: #c33">提示：</label></td>
                         <td class="ab_main" colspan="4">
                             <label style="color: #c33">不允许待激活红包和实物奖同时配置!</label>
                         </td>
                     </tr>
                     <tr>
                         <td class="ab_main" colspan="4">
                             <table id="firstScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                                 <thead>
                                 <tr>
                                     <th style="width:5%; text-align: center;">序号</th>
                                     <th style="width:8%; text-align: center;">中出类型</th>
                                     <th style="width:13%; text-align: center;" class="cogItemMoney">积分红包范围(元)</th>
                                     <th style="width:9%; text-align: center;" class="cogItemMoney">积分红包均价(元)</th>
                                     <th style="width:9%; text-align: center;" class="cogItemMoney">积分红包成本(元)</th>
                                    <c:if test="${activityType eq '0'}">
                                     <th style="width:13%; text-align: center;" class="cogItemWaitActivation">选择待激活红包</th>
                                     <th style="width:9%; text-align: center;" class="cogItemWaitActivation">待激活积分红包范围(元)</th>
                                     <th style="width:9%; text-align: center;" class="cogItemWaitActivation">待激活积分红包均价(元)</th>
                                     <th style="width:9%; text-align: center;" class="cogItemWaitActivation">待激活积分红包成本(元)</th>
                                    </c:if>
                                     <th style="width:13%; text-align: center;" class="cogItemVpoints">商城积分范围</th>
                                     <th style="width:9%; text-align: center;" class="cogItemVpoints">积分均价</th>
                                     <th style="width:9%; text-align: center;" class="cogItemVpoints">积分成本</th>
                                     <th style="width:12%; text-align: center;" class="cogItemPrize">实物奖</th>
                                     <th style="width:9%; text-align: center;" class="cogItemPrize">支付金额(元)</th>
                                     <th style="width:9%; text-align: center;" class="cogItemPrize">优惠折扣</th>
                                     <th style="width:12%; text-align: center;" class="cogItemAllowance">津贴卡</th>
                                     <th style="width:9%; text-align: center;" class="cogItemAllowance">卡券金额(元)</th>
                                     <th style="width:11%; text-align: center;" class="cogItemLadder">阶梯</th>
                                     <th style="width:12%; text-align: center;" class="cogItemCard">集卡</th>
                                     <th style="width:8%; text-align: center;">中出占比</th>
                                     <th style="width:8%; text-align: center;">占比预警</th>
                                     <th style="width:13%; text-align: center;">投放个数</th>
                                 </tr>
                                 </thead>
                                 <tbody>
                                 <tr>
                                     <td>
                                         <label id="NO" style="line-height: 30px;">1</label>
                                     </td>
                                     <td>
                                         <input type="hidden" name="scanType" value="0">
                                         <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial; width: 60px !important;">
                                             <option value="1">固定</option>
                                             <option value="0">随机</option>
                                         </select>
                                     </td>
                                     <td class="cogItemMoney">
                                         <div class="random-prize content" style="display: none;">
                                             <input type="text" name="minMoney" class="form-control input-width-small number maxValue"  autocomplete="off"
                                                    tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                             <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                             <input type="text" name="maxMoney" class="form-control input-width-small number maxValue"  autocomplete="off"
                                                    tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                         </div>
                                         <div class="fixation-prize" style="display: inline-flex;">
                                             <input type="text" name="fixationMoney" class="form-control input-width-small number maxValue"  autocomplete="off"
                                                    tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 132px !important;">
                                         </div>
                                     </td>
                                     <td class="cogItemMoney">
                                         <label id="unitMoney" style="line-height: 30px;">0.00</label>
                                     </td>
                                     <td class="cogItemMoney">
                                         <label id="itemTotalMoney" style="line-height: 30px; min-width: 40px;">0.00</label>
                                     </td>
                                    <c:if test="${activityType eq '0'}">
                                     <td class="cogItemWaitActivation">
                                         <div class="content">
                                             <select name="waitActivationPrizeKey" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                 <option value="">请选择</option>
                                                 <c:forEach items="${waitActivationPrize}" var="item">
                                                     <option value="${item.prizeKey}">${item.prizeName}</option>
                                                 </c:forEach>
                                             </select>
                                         </div>
                                     </td>
                                     <td class="cogItemWaitActivation"  >
                                         <div class="WaitActivation-random-prize content" style="display: inline-flex">
                                             <input type="text" name="minWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                    tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                             <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                             <input type="text" name="maxWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                    tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                         </div>
                                     </td>
                                     <td class="cogItemWaitActivation">
                                         <label id="unitWaitActivationMoney" style="line-height: 30px;">0.00</label>
                                     </td>
                                     <td class="cogItemWaitActivation">
                                         <label id="itemTotalWaitActivationMoney" style="line-height: 30px; min-width: 40px;">0.00</label>
                                     </td>
                                    </c:if>
                                     <td class="cogItemVpoints">
                                         <div class="random-prize content" style="display: none;">
                                             <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue"
                                                    autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                             <label style="line-height: 30px;">&nbsp;-&nbsp;</label>
                                             <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue"
                                                    autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                         </div>
                                         <div class="fixation-prize" style="display: inline-flex;">
                                             <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                    tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                         </div>
                                     </td>
                                     <td class="cogItemVpoints">
                                         <label id="unitVpoints" style="line-height: 30px;">0.00</label>
                                     </td>
                                     <td class="cogItemVpoints">
                                         <label id="itemTotalVpoints" style="line-height: 30px; min-width: 40px;">0.00</label>
                                     </td>
                                     <td class="cogItemPrize">
                                         <div class="content">
                                             <select name="bigPrizeType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                 <option value="">请选择</option>
                                                 <c:forEach items="${prizeTypeMap}" var="item">
                                                     <option value="${item.key}">${item.value}</option>
                                                 </c:forEach>
                                             </select>
                                         </div>
                                     </td>
                                     <td class="cogItemPrize">
                                         <input type="text" name="prizePayMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial;">
                                     </td>
                                     <td class="cogItemPrize">
                                         <input type="text" name="prizeDiscount" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="10.00" maxlength="5" style="display: initial;">
                                     </td>
                                     <td class="cogItemAllowance">
                                         <div class="content">
                                             <select name="allowanceType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                                 <option value="">请选择</option>
                                                 <c:forEach items="${allowanceTypeMap}" var="item">
                                                     <option value="${item.key}">${item.value}</option>
                                                 </c:forEach>
                                             </select>
                                         </div>
                                     </td>
                                     <td class="cogItemAllowance">
                                         <input type="text" name="allowanceMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial;">
                                     </td>
                                     <td class="cogItemLadder">
                                         <input type="text" name="scanNum" class="form-control input-width-small" autocomplete="off"
                                                tag="validate"  style="display: initial;">
                                     </td>
                                     <td class="cogItemCard">
                                         <div class="content">
                                             <select name="cardNo" class="form-control input-width-small" style="display: initial;">
                                                 <option value="">请选择</option>
                                                 <c:forEach items="${cardTypeMap}" var="item">
                                                     <option value="${item.key}">${item.value}</option>
                                                 </c:forEach>
                                             </select>
                                         </div>
                                     </td>
                                     <td style="position: relative;">
                                         <div style="display: inline-flex;">
                                             <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue"
                                                    autocomplete="off" tag="validate" data-oldval="0.0000" value="0.0000" maxVal="100" maxlength="7" style="display: initial; width: 60px !important;">
                                             <label style="line-height: 30px;">%</label>
                                         </div>
                                     </td>
                                     <td style="position: relative;">
                                         <label id="prizePercentWarnLabel" style="line-height: 30px; min-width: 55px; display: none;">--</label>
                                         <div id="prizePercentWarnDiv" style="display: inline-flex;">
                                             <input type="text" name="prizePercentWarn" class="form-control input-width-small number maxValue"
                                                    autocomplete="off" tag="validate" maxVal="100" maxlength="7" style="display: initial; width: 60px !important;">
                                             <label style="line-height: 30px;">%</label>
                                         </div>
                                     </td>
                                     <td style="position: relative;">
                                         <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">0</label>
                                         <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="0" value="0" maxlength="10" autocomplete="off"  style="display: none; width: 60px !important;">
                                         <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                     </td>
                                 </tr>
                                 <tr>
                                     <td>合计</td>
                                     <td>--</td>
                                     <td class="cogItemMoney">--</td>
                                     <td class="cogItemMoney">--</td>
                                     <td class="cogItemMoney">--</td>
                                    <c:if test="${activityType eq '0'}">
                                     <td class="cogItemWaitActivation">--</td>
                                     <td class="cogItemWaitActivation">--</td>
                                     <td class="cogItemWaitActivation">--</td>
                                     <td class="cogItemWaitActivation">--</td>
                                    </c:if>
                                     <td class="cogItemVpoints">--</td>
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
                <td class="ab_main" style="width: 30% !important;">
                    <div class="content">
                        <input name="commonScanPercent" tag="validate" autocomplete="off" value="100" readonly="readonly" style="width: 160px !important;"
                            class="form-control input-width-larger number integer minValue maxValue required" minVal="0" maxVal="100"/>
                        <span class="blocker en-larger">%</span>
                        <label class="validate_tips"></label>
                    </div>
                </td>
                <td class="ab_left"><label class="title">普扫码数：<span class="required">*</span></label></td>
                <td class="ab_main">
                    <div class="content">
                        <input id="commonScanQrcodeNum" value="0" readonly="readonly"
                            class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold;width: 160px !important;"/>
                        <span class="blocker en-larger">个</span>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="ab_left"><label class="title">普扫总金额：<span class="required">*</span></label></td>
                <td class="ab_main" style="width: 25% !important;">
                    <div class="content">
                        <input id="commonScanTotalMoney" readonly="readonly" value="≈0.00" 
                            class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold;width: 160px !important;"/>
                            <span class="blocker en-larger">元</span>
                    </div>
                </td>
                <td class="ab_left"><label class="title">普扫总积分：<span class="required">*</span></label></td>
                <td class="ab_main">
                    <div class="content">
                        <input id="commonScanTotalVpoints" readonly="readonly" value="≈0.00" 
                            class="form-control input-width-larger"tabindex="-1" style="color: red; font-weight: bold;width: 160px !important;"/>
                            <span class="blocker en-larger">积分</span>
                    </div>
                </td>
            </tr>
	        <tr>
	            <td class="ab_left"><label class="title">中出配置项：<span class="required"> </span></label></td>
	            <td class="ab_main" colspan="3">
                    <div style="float: left; width: 120px; line-height: 25px;">
                        <input type="checkbox" id="cogItemMoney" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;积分红包
                    </div>
                    <c:if test="${activityType eq '0'}">
                    <div style="float: left; width: 120px; line-height: 25px;">
                        <input type="checkbox" id="cogItemWaitActivation" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" >&nbsp;待激活红包
                    </div>
                    </c:if>
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
            <tr class="waitActivationTips" style="display: none">
                <td class="ab_left"><label class="title" style="color: #c33">提示：</label></td>
                <td class="ab_main" colspan="4">
                    <label style="color: #c33">不允许待激活红包和实物奖同时配置!</label>
                </td>
            </tr>
	        <tr>
	            <td class="ab_main" colspan="4">
	                <table id="commonScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
	                    <thead>
	                        <tr>
                                <th style="width:5%; text-align: center;">序号</th>
                                <th style="width:8%; text-align: center;">中出类型</th>
                                <c:if test="${activityType eq '13'}">
                                <th style="width:8%; text-align: center;" class="cogItemMoney">是否天降红包待激活</th></c:if>
                                <th style="width:13%; text-align: center;" class="cogItemMoney">积分红包范围(元)</th>
                                <th style="width:9%; text-align: center;" class="cogItemMoney">积分红包均价(元)</th>
                                <th style="width:9%; text-align: center;" class="cogItemMoney">积分红包成本(元)</th>
                                <c:if test="${activityType eq '0'}">
                                <th style="width:13%; text-align: center;" class="cogItemWaitActivation">选择待激活红包</th>
                                <th style="width:9%; text-align: center;" class="cogItemWaitActivation">待激活积分红包范围(元)</th>
                                <th style="width:9%; text-align: center;" class="cogItemWaitActivation">待激活积分红包均价(元)</th>
                                <th style="width:9%; text-align: center;" class="cogItemWaitActivation">待激活积分红包成本(元)</th>
                                </c:if>
                                <th style="width:13%; text-align: center;" class="cogItemVpoints">商城积分范围</th>
                                <th style="width:9%; text-align: center;" class="cogItemVpoints">积分均价</th>
                                <th style="width:9%; text-align: center;" class="cogItemVpoints">积分成本</th>
                                <th style="width:12%; text-align: center;" class="cogItemPrize">实物奖</th>
                                <th style="width:9%; text-align: center;" class="cogItemPrize">支付金额(元)</th>
                                <th style="width:9%; text-align: center;" class="cogItemPrize">优惠折扣</th>
                                <th style="width:12%; text-align: center;" class="cogItemAllowance">津贴卡</th>
                                <th style="width:9%; text-align: center;" class="cogItemAllowance">卡券金额(元)</th>
                                <th style="width:11%; text-align: center;" class="cogItemLadder">阶梯</th>
                                <th style="width:12%; text-align: center;" class="cogItemCard">集卡</th>
                                <th style="width:8%; text-align: center;">中出占比</th>
                                <th style="width:8%; text-align: center;">占比预警</th>
                                <th style="width:13%; text-align: center;">投放个数</th>
	                        </tr>
	                    </thead>
	                    <tbody>
                            <tr>
                                 <td>
                                    <label id="NO" style="line-height: 30px;">1</label>
                                 </td>
                                 <td>
                                    <input type="hidden" name="scanType" value="1">
                                    <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial; width: 60px !important;">
                                        <option value="1">固定</option>
                                        <option value="0">随机</option>
                                    </select>
                                 </td>
                                 <c:if test="${activityType eq '13'}">
                                     <td  class="cogItemMoney">
                                        <select id="isWaitActivation" name="isWaitActivation" class="form-control input-width-small" style="display: initial; width: 60px !important;">
                                            <option value="0">否</option>
                                            <option value="1">是</option>
                                        </select>
                                     </td>
                                 </c:if>
                                 <td class="cogItemMoney">
                                    <div class="random-prize content" style="display: none;">
                                        <input type="text" name="minMoney" class="form-control input-width-small number maxValue"  autocomplete="off"
                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                        <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                        <input type="text" name="maxMoney" class="form-control input-width-small number maxValue"  autocomplete="off"
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                    </div>
                                    <div class="fixation-prize" style="display: inline-flex;">
                                        <input type="text" name="fixationMoney" class="form-control input-width-small number maxValue"  autocomplete="off"
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 132px !important;">
                                    </div>
                                 </td>
                                 <td class="cogItemMoney">
                                    <label id="unitMoney" style="line-height: 30px;">0.00</label>
                                 </td>
                                 <td class="cogItemMoney">
                                    <label id="itemTotalMoney" style="line-height: 30px; min-width: 40px;">0.00</label>
                                 </td>
                                <c:if test="${activityType eq '0'}">
                                <td class="cogItemWaitActivation">
                                    <div class="content">
                                        <select name="waitActivationPrizeKey" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                            <option value="">请选择</option>
                                            <c:forEach items="${waitActivationPrize}" var="item">
                                                <option value="${item.prizeKey}">${item.prizeName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                                <td class="cogItemWaitActivation"  >
                                    <div class="WaitActivation-random-prize content" style="display: inline-flex">
                                        <input type="text" name="minWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                        <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                        <input type="text" name="maxWaitActivationMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off"
                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                    </div>
                                </td>
                                <td class="cogItemWaitActivation">
                                    <label id="unitWaitActivationMoney" style="line-height: 30px;">0.00</label>
                                </td>
                                <td class="cogItemWaitActivation">
                                    <label id="itemTotalWaitActivationMoney" style="line-height: 30px; min-width: 40px;">0.00</label>
                                </td>
                                </c:if>
                                 <td class="cogItemVpoints">
                                    <div class="random-prize content" style="display: none;">
                                        <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue" 
                                           autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                        <label style="line-height: 30px;">&nbsp;-&nbsp;</label>
                                        <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue" 
                                         autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                    </div>
                                    <div class="fixation-prize" style="display: inline-flex;">
                                        <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off" 
                                                tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                    </div>
                                 </td>
                                 <td class="cogItemVpoints">
                                    <label id="unitVpoints" style="line-height: 30px;">0.00</label>
                                 </td>
                                 <td class="cogItemVpoints">
                                    <label id="itemTotalVpoints" style="line-height: 30px; min-width: 40px;">0.00</label>
                                 </td>
                                 <td class="cogItemPrize">
                                    <div class="content">
                                        <select name="bigPrizeType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                            <option value="">请选择</option>
                                            <c:forEach items="${prizeTypeMap}" var="item">
                                                <option value="${item.key}">${item.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                 </td>
                                 <td class="cogItemPrize">
                                    <input type="text" name="prizePayMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial;">
                                 </td>
                                 <td class="cogItemPrize">
                                    <input type="text" name="prizeDiscount" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="10.00" maxlength="5" style="display: initial;">
                                 </td>
                                 <td class="cogItemAllowance">
                                    <div class="content">
                                        <select name="allowanceType" class="form-control input-width-normal" style="display: initial; width: 100% !important;">
                                            <option value="">请选择</option>
                                            <c:forEach items="${allowanceTypeMap}" var="item">
                                                <option value="${item.key}">${item.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                 </td>
                                 <td class="cogItemAllowance">
                                    <input type="text" name="allowanceMoney" class="form-control input-width-small moneyFmt number maxValue"  autocomplete="off" 
                                                tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="display: initial;">
                                 </td>
                                 <td class="cogItemLadder">
                                    <input type="text" name="scanNum" class="form-control input-width-small" autocomplete="off" 
                                                tag="validate"  style="display: initial;">
                                 </td>
                                 <td class="cogItemCard">
                                    <div class="content">
                                        <select name="cardNo" class="form-control input-width-small" style="display: initial;">
                                            <option value="">请选择</option>
                                            <c:forEach items="${cardTypeMap}" var="item">
                                                <option value="${item.key}">${item.value}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                 </td>
                                 <td style="position: relative;">
                                    <div style="display: inline-flex;">
                                        <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue" 
                                         autocomplete="off" tag="validate" data-oldval="0.0000" value="0.0000" maxVal="100" maxlength="7" style="display: initial; width: 60px !important;">
                                        <label style="line-height: 30px;">%</label>
                                    </div>
                                 </td>
                                 <td style="position: relative;">
                                    <label id="prizePercentWarnLabel" style="line-height: 30px; min-width: 55px; display: none;">--</label>
                                    <div id="prizePercentWarnDiv" style="display: inline-flex;">
                                        <input type="text" name="prizePercentWarn" class="form-control input-width-small number maxValue" 
                                         autocomplete="off" tag="validate" maxVal="100" maxlength="7" style="display: initial; width: 60px !important;">
                                        <label style="line-height: 30px;">%</label>
                                    </div>
                                 </td>
                                 <td style="position: relative;">
                                    <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">0</label>
                                    <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" maxlength="10" autocomplete="off"  style="display: none; width: 60px !important;">
                                    <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                 </td>
                            </tr>
	                        <tr>
	                             <td>合计</td>
	                             <td>--</td>
                                 <td class="cogItemMoney">--</td>
                                 <td class="cogItemMoney">--</td>
                                 <td class="cogItemMoney">--</td>
                                 <td class="cogItemWaitActivation">--</td>
                                 <td class="cogItemWaitActivation">--</td>
                                 <td class="cogItemWaitActivation">--</td>
                                 <td class="cogItemWaitActivation">--</td>
                                 <td class="cogItemVpoints">--</td>
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
</div>
