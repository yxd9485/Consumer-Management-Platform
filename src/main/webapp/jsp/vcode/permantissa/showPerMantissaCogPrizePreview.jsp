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
        
        // 初始化校验控件
        $.runtimeValidate($("form"));
        
        // 初始化功能
        initRuleTempletPage();
        
        // 初始化完成后，打开计算开关
        calculateUnitFlag = true;
    });
    
    function initRuleTempletPage() {
        
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
                    $cloneItem.find("label[id$='QrcodeNum']").text("0");
                    $cloneItem.find("#addPrizeItem").text("删除");
                    $(this).closest("tbody").find("tr:last-child").before($cloneItem);
                    $cloneItem.find("#randomType").trigger("change");
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
        
        // 修改奖项配置项占比
        $("form").on("change", "input[name='prizePercent']", function(){
            var totalPercent = 0;
            $(this).closest("tbody").find("input[name='prizePercent']").each(function(i,obj){
                totalPercent += Number($(this).val());
            });

            var currVal = Number($(this).val());
            if (totalPercent > 100) {
                $(this).val((100 - (totalPercent - currVal)).toFixed(2));
                totalPercent = 100;
            } else {
                $(this).val(currVal.toFixed(2));
            }
            $(this).closest("tbody").find("#totalPrizePercent").text(totalPercent.toFixed(2) + "%").css("color", totalPercent.toFixed(2) == 100 ? "green" : "red");

            // 重新计算均价
            calculateUnit();
        });
        $("form").on("input", "input[name='prizePercent']", function(){
            if (Number($(this).val()) >= 100) {
                $(this).trigger("change");
            }
        });
        
        // 奖项总数
        $("input[name='ruleTotalPrize']").on("input", function(){
        	// 重新计算均价
            calculateUnit();
        });
        
        // 选择配置方案
        $("#rebateRuleTemplet").on("change", function(){
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
        $currTr.find("#prizePercent").val(templetDetail["prizePercent"]).trigger("change");
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
	    var itemUnitMoney = 0.00;
	    var itemUnitVpoints = 0.00;
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
	                itemPercent = Number($(this).find("input[name='prizePercent']").val());
                    
                    itemQrcodeNum = (scanTypeQrcodeNum * itemPercent / 100).toFixed(0);
	                itemMoney = itemQrcodeNum * itemUnitMoney;
	                itemVpoints = itemQrcodeNum * itemUnitVpoints;
	                $(this).find("#itemTotalMoney").text(itemMoney.toFixed(2));
	                $(this).find("#itemTotalVpoints").text(itemVpoints.toFixed(2));
	                $(this).find("#itemQrcodeNum").text(itemQrcodeNum);
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
	    $("input[name='unitMoney']").val("≈" + (totalMoney/totalPrize).toFixed(2));
	    $("input[name='ruleTotalVpoints']").val("≈" + ((totalVpoints).toFixed(2) + "").replace(/(\d{1,3})(?=(\d{3})+(?:$|\.))/g,'$1,'));
	    $("input[name='unitVpoints']").val("≈" + (totalVpoints/totalPrize).toFixed(2));
	}
</script>
  
<table id="firstScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
    <thead>
         <tr>
             <th style="width:4%; text-align: center;">序号</th>
             <th style="width:8%; text-align: center;">红包类型</th>
             <th style="width:12%; text-align: center;">红包金额范围(元)</th>
             <th style="width:10%; text-align: center;">红包均价(元)</th>
             <th style="width:10%; text-align: center;">红包成本(元)</th>
             <th style="width:12%; text-align: center;">红包积分范围</th>
             <th style="width:8%; text-align: center;">积分均价</th>
             <th style="width:8%; text-align: center;">积分成本</th>
             <th style="width:12%; text-align: center;">瓶数区间</th>
             <th style="width:8%; text-align: center;">奖项占比</th>
             <th style="width:8%; text-align: center;">奖项个数</th>
         </tr>
     </thead>
     <tbody>
         <tr>
              <td style="text-align:center;">
                 <label id="NO" style="line-height: 30px;">1</label>
              </td>
              <td style="text-align:center;">
                 <input type="hidden" name="scanType" value="0">
                 <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial; width: 60px !important;">
                     <option value="1">固定</option>
                     <option value="0">随机</option>
                 </select>
              </td>
              <td>
                 <div class="random-prize content" style="display: none;">
                     <input type="text" name="minMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
                            tag="validate" data-oldval="0.00" value="0.00" maxVal="99.99" maxlength="5" style="display: initial; width: 60px !important;">
                     <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                     <input type="text" name="maxMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
                             tag="validate" data-oldval="0.00" value="0.00" maxVal="99.99" maxlength="5" style="display: initial; width: 60px !important;">
                 </div>
                 <div class="fixation-prize" style="display: inline-flex;">
                     <input type="text" name="fixationMoney" class="form-control input-width-small number maxValue"  autocomplete="off" 
                             tag="validate" data-oldval="0.00" value="0.00" maxVal="99.99" maxlength="5" style="display: initial; width: 132px !important;">
                 </div>
              </td>
              <td style="text-align:center;">
                 <label id="unitMoney" style="line-height: 30px;">0.00</label>
              </td>
              <td style="text-align:center;">
                 <label id="itemTotalMoney" style="line-height: 30px;">0.00</label>
              </td>
              <td>
                 <div class="random-prize content" style="display: none;">
                     <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue" 
                        autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="99999" maxlength="5" style="display: initial; width: 60px !important;">
                     <label style="line-height: 30px;">&nbsp;-&nbsp;</label>
                     <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue" 
                      autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="99999" maxlength="5" style="display: initial; width: 60px !important;">
                 </div>
                 <div class="fixation-prize" style="display: inline-flex;">
                     <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off" 
                             tag="validate" data-oldval="0" value="0" maxVal="99999" maxlength="5" style="display: initial; width: 132px !important;">
                 </div>
              </td>
              <td style="text-align:center;">
                 <label id="unitVpoints" style="line-height: 30px;">0.00</label>
              </td>
              <td style="text-align:center;">
                 <label id="itemTotalVpoints" style="line-height: 30px;">0.00</label>
              </td>
              <td>
                 <div class="scan_num content">
                     <input type="text" name="minScanNum" class="form-control input-width-small number integer maxValue" 
                        autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="99999" maxlength="5" style="display: initial; width: 60px !important;">
                     <label style="line-height: 30px;">&nbsp;-&nbsp;</label>
                     <input type="text" name="maxScanNum" class="form-control input-width-small number integer maxValue" 
                      autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="99999" maxlength="5" style="display: initial; width: 60px !important;">
                 </div>
              </td>
              <td style="position: relative;">
                 <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue" 
                  autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="100" maxlength="5" style="display: initial; width: 54px !important;">
                 <label style="line-height: 30px;">%</label>
              </td>
              <td style="position: relative;">
                 <label id="itemQrcodeNum" style="line-height: 30px;">0</label>
                 <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
              </td>
         </tr>
         <tr>
              <td style="text-align:center;">合计</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;">--</td>
              <td style="text-align:center;"><span id="totalPrizePercent" data-currval="0" style="font-weight: bold;">0%</span></td>
              <td style="text-align:center;">--</td>
         </tr>
     </tbody>
 </table>
