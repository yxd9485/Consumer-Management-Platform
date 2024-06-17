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
            $("#timeType").change(function(){
                var timeType=document.getElementById("timeType").value;
                if(timeType=="2"){
                    $("input[name='cycleDay']").attr('disabled',true);
                    $("input[name='week']").attr('disabled',false);
                }else if(timeType=="1"){
                    $("input[name='cycleDay']").attr('disabled',false);
                    $("input[name='week']").attr('disabled',true);
                }else{
                    $("input[name='cycleDay']").attr('disabled',true);
                    $("input[name='week']").attr('disabled',true);
                }
            });
            var timeType1  = '${signIn.timeType}';
            if (timeType1 == "2") {
                $("input[name='cycleDay']").attr('disabled',true);
                $("input[name='week']").attr('disabled',false);
            }
            if (timeType1 == "1") {
                $("input[name='cycleDay']").attr('disabled',false);
                $("input[name='week']").attr('disabled',true);
            }
            if (timeType1 == "0" || timeType1 == '') {
                $("input[name='cycleDay']").attr('disabled',true);
                $("input[name='week']").attr('disabled',true);
            }
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
            $(".btnSave").click(function () {
                var flag = validForm();
                if (flag) {
                    var url = $(this).attr("url");
                    $.fn.confirm("确认发布？", function () {

                        $(".btnSave").attr("disabled", "disabled");

                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
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

            // 修改奖项配置项占比
            $("form").on("change", "input[name='prizePercent']", function(){
                var totalPercent = 0; 
                $(this).closest("tbody").find("input[name='prizePercent']").each(function (i, obj) {
                	totalPercent += Number($(this).val());
                });

                var currVal = Number($(this).val());
                if (totalPercent > 100) {
                	$(this).val((100 - (totalPercent - currVal)).toFixed(2));
                	totalPercent = 100;
                }else{
                	$(this).val(currVal.toFixed(2));	
                }
                
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
                if ($("#firstScanPrize").find("#totalPrizePercent").text() != "100.0000%") {
                    $.fn.alert("每日签到配置项占比之和未达到100%");
                    return false;
                }
            if(document.getElementById("timeType").value != "" ){
                if ($("#commonScanPrize").find("#totalPrizePercent").text() != "100.0000%") {
                    $.fn.alert("特殊日期配置配置项占比之和未达到100%");
                    return false;
                }
                if (document.getElementById("timeType").value == "2" && $("input[name='week']:checked").length == 0) {
                    $.fn.alert("请按周勾选复选框");
                    return false;
                }
                
                if (document.getElementById("timeType").value == "1" && document.getElementById("cycleDay").value == "") {
                    $.fn.alert("请填写日期");
                    return false;
                }
            }

            return true;
        }
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
<script>

</script>
<div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath%>/integral/doSignInEdit.do">
                <input name="infoKey" type="hidden" value="${signIn.infoKey }">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>规则基本信息</h4>
                    </div>
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>每日签到</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table scan_type_table">
                       	<tr>
                              <td style="text-align: left; padding-left: 50px; width: 7%;"><label class="title">展示时间：<span
                                      class="required">*</span></label></td>
                              <td class="ab_main" colspan="3">
                                  <div class="content">
                                      <span class="blocker">从</span>
                                      <input name="startDate" id="startDate"
                                             class="form-control input-width-medium Wdate required preTime"
                                             tag="validate" style="width: 180px !important;"
                                             autocomplete="off" value="${fn:substring(signIn.startDate, 0, 19)}"
                                             onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
                                      <span class="blocker en-larger">至</span>
                                      <input name="endDate" id="endDate"
                                             class="form-control input-width-medium Wdate required sufTime"
                                             tag="validate" style="width: 180px !important;"
                                             autocomplete="off" value="${fn:substring(signIn.endDate, 0, 19)}"
                                             onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})"/>
                                      <label class="validate_tips"></label>
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
                                            <th style="width:20%; text-align: center;" class="cogItemVpoints">红包积分范围</th>
                                            <th style="width:11%; text-align: center;" class="cogItemVpoints">积分均价</th>
                                            <th style="width:11%; text-align: center;">奖项占比</th>
                                            <th style="width:11%; text-align: center;">奖项个数</th>
                                            <th style="width:11%; text-align: center;">剩余个数</th>
                                        </tr>
                                        </thead>
                                        <tbody>

                                        <c:choose>
                                            <c:when test="${signInInfo == null}">
                                                <tr>
                                                    <td>
                                                        <label id="NO" style="line-height: 30px;">1</label>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="specialType" value="0">
                                                        <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial;">
                                                            <option value="1">固定</option>
                                                            <option value="0">随机</option>
                                                        </select>
                                                    </td>
                                                    <td class="cogItemVpoints">
                                                        <div class="random-prize content" style="display: none;">
                                                            <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue"
                                                                   autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 50px !important;">
                                                            <label style="line-height: 30px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
                                                            <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue"
                                                                   autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 50px !important;">
                                                        </div>
                                                        <div class="fixation-prize" style="display: inline-flex;">
                                                            <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                                   tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 50px !important;">
                                                        </div>
                                                    </td>
                                                    <td class="cogItemVpoints">
                                                        <label id="unitVpoints" style="line-height: 30px;">0.00</label>
                                                    </td>
                                                    <td style="position: relative;">
                                                        <div style="display: inline-flex;">
                                                            <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue"
                                                                   autocomplete="off" tag="validate" data-oldval="0.0000" value="0.0000" maxVal="100" maxlength="7" style="display: initial; width: 50px !important;">
                                                            <label style="line-height: 30px;">%</label>
                                                        </div>
                                                    </td>
                                                    <td style="position: relative;">
                                                        <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="0" value="0" maxlength="7" autocomplete="off"  style="display: none; width: 60px !important;">
                                                    </td>
                                                    <td style="position: relative;">
                                                        <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">--</label>
                                                        <input type="text" id="restAmounts" name="restAmounts" class="form-control input-width-small number" data-oldval="0" value="0" maxlength="7" autocomplete="off" readonly style="width: 60px !important;">
                                                        <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${signInInfo}" var="detail" varStatus="idx">
                                                    <tr>
                                                        <td>
                                                            <label id="NO" style="line-height: 30px;">${idx.count}</label>
                                                        </td>
                                                        <td>
                                                            <input type="hidden" name="specialType" value="0">
                                                            <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial;">
                                                                <option value="1" <c:if test="${detail.randomType eq '1'}">selected</c:if>>固定</option>
                                                                <option value="0" <c:if test="${detail.randomType eq '0'}">selected</c:if>>随机</option>
                                                            </select>
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
                                                        <td style="position: relative;">
                                                            <div style="display: inline-flex;">
                                                                <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue"
                                                                       autocomplete="off" tag="validate" data-oldval="${detail.prizePercent}" value="${detail.prizePercent}" maxVal="100" maxlength="7" style="display: initial; width: 65px !important;">
                                                                <label style="line-height: 30px;">%</label>
                                                            </div>
                                                        </td>
                                                        <td style="position: relative;">
                                                            <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="${detail.cogAmounts}" value="${detail.cogAmounts}" maxlength="7" autocomplete="off"  style="display: none; width: 60px !important;">
                                                        </td>
                                                        <td style="position: relative;">
                                                            <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">--</label>
                                                            <input type="text" id="restAmounts" name="restAmounts" class="form-control input-width-small number" data-oldval="${detail.restAmounts}" value="${detail.restAmounts}" maxlength="7" autocomplete="off" readonly  style=" width: 60px !important;">
                                                            <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr>
                                            <td>合计</td>
                                            <td>--</td>
                                            <td class="cogItemVpoints">--</td>
                                            <td class="cogItemVpoints">--</td>
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
                        <h4><i class="iconfont icon-xinxi"></i>特殊日期配置</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table scan_type_table">
                            <c:choose>
                            <c:when test="${signIn == null}">
                            <tr>
                                <td class="ab_left"><label class="title mart5">日期类型：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                        <div class="content">
                                            <select id="timeType" name="timeType" class="form-control input-width-larger search" autocomplete="off" οnchange="checkSelect(this.value)">
                                                <option value="">请选择</option>
                                                <option value="1">按天</option>
                                                <option value="2">按周</option>
                                            </select>
                                        </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">指定日期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="cycleDay" name="cycleDay" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off" maxlength="30" disabled/>
<%--                                        <label class="validate_tips"></label>--%>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">星期几：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox" id="week"  name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="1">&nbsp;周一
                                </div>
                                <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox"  id="week"  name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="2">&nbsp;周二
                                </div>
                               <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox" id="week"  name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="3">&nbsp;周三
                               </div>
                                <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="4">&nbsp;周四
                                </div>
                               <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox" id="week" name="week"  class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="5">&nbsp;周五
                               </div>
                            <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox"  id="week"  name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="6">&nbsp;周六
                            </div>
                              <div style="float: left; width: 120px; line-height: 25px;">
                                <input type="checkbox"   id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="7">&nbsp;周日
                              </div>
                                <tr>
                            </c:when>

                                <c:otherwise>
                                <tr>
                                    <td class="ab_left"><label class="title mart5">日期类型：<span class="required">*</span></label></td>
                                    <td class="ab_main">
                                        <div class="content">
                                            <select id="timeType" name="timeType" class="form-control input-width-larger search" autocomplete="off" οnchange="checkSelect(this.value)">
                                                <option value="" <c:if test="${signIn.timeType eq ''}">selected="selected"</c:if>>请选择</option>
                                                <option value="1" <c:if test="${signIn.timeType eq 1}">selected="selected"</c:if>>按天</</option>
                                                <option value="2" <c:if test="${signIn.timeType eq 2}">selected="selected"</c:if>>按周</option>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">指定日期：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input id="cycleDay" name="cycleDay" tag="validate" value="${signIn.cycleDay}"
                                                   class="form-control input-width-larger required" autocomplete="off" maxlength="30" >
<%--                                            <label class="validate_tips"></label>--%>
                                        </div>
                                    </td>
                                </tr>
                            <tr>
                                        <td class="ab_left"><label class="title">星期几：<span class="required"> </span></label></td>
                                        <td class="ab_main" colspan="3">
                                     <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="1"<c:if test="${fn:contains(signIn.week,'1')}">checked</c:if>>&nbsp;周一
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;"value="2"<c:if test="${fn:contains(signIn.week,'2')}">checked</c:if>>&nbsp;周二
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="3"<c:if test="${fn:contains(signIn.week,'3')}">checked</c:if>>&nbsp;周三
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week"  name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="4"<c:if test="${fn:contains(signIn.week,'4')}">checked</c:if>>&nbsp;周四
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="5"<c:if test="${fn:contains(signIn.week,'5')}">checked</c:if>>&nbsp;周五
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="6"<c:if test="${fn:contains(signIn.week,'6')}">checked</c:if>>&nbsp;周六
                                    </div>
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="checkbox" id="week" name="week" class="cogItemCB" style="float: left; height:20px; cursor: pointer;" value="7"<c:if test="${fn:contains(signIn.week,'7')}">checked</c:if>>&nbsp;周日
                                    </div>
                                    </div>
                                </td>
                            </tr>
                    </c:otherwise>
                    </c:choose>
                            <tr>
                                <td class="ab_main" colspan="4">
                                    <table id="commonScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 95%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:5%; text-align: center;">序号</th>
                                            <th style="width:8%; text-align: center;">中出类型</th>
                                            <th style="width:20%; text-align: center;" class="cogItemVpoints">红包积分范围</th>
                                            <th style="width:11%; text-align: center;" class="cogItemVpoints">积分均价</th>
                                            <th style="width:11%; text-align: center;">奖项占比</th>
                                            <th style="width:11%; text-align: center;">奖项个数</th>
                                            <th style="width:11%; text-align: center;">剩余个数</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:choose>
                                            <c:when test="${specialDayInfo == null}">
                                                <tr>
                                                    <td>
                                                        <label id="NO" style="line-height: 30px;">1</label>
                                                    </td>
                                                    <td>
                                                        <input type="hidden" name="specialType" value="1">
                                                        <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial;">
                                                            <option value="1">固定</option>
                                                            <option value="0">随机</option>
                                                        </select>
                                                    </td>
                                                    <td class="cogItemVpoints">
                                                        <div class="random-prize content" style="display: none;">
                                                            <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue"
                                                                   autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 50px !important;">
                                                            <label style="line-height: 30px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
                                                            <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue"
                                                                   autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 50px !important;">
                                                        </div>
                                                        <div class="fixation-prize" style="display: inline-flex;">
                                                            <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                                   tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 50px !important;">
                                                        </div>
                                                    </td>
                                                    <td class="cogItemVpoints">
                                                        <label id="unitVpoints" style="line-height: 30px;">0.00</label>
                                                    </td>
                                                    <td style="position: relative;">
                                                        <div style="display: inline-flex;">
                                                            <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue"
                                                                   autocomplete="off" tag="validate" data-oldval="0.0000" value="0.0000" maxVal="100" maxlength="7" style="display: initial; width: 50px !important;">
                                                            <label style="line-height: 30px;">%</label>
                                                        </div>
                                                    </td>
                                                    <td style="position: relative;">
                                                        <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="0" value="0" maxlength="7" autocomplete="off"  style="display: none; width: 60px !important;">
                                                    </td>
                                                    <td style="position: relative;">
                                                        <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">--</label>
                                                        <input type="text" id="restAmounts" name="restAmounts" class="form-control input-width-small number" data-oldval="0" value="0" maxlength="7" autocomplete="off" readonly  style="width: 60px !important;">
                                                        <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                    </td>
                                                </tr>
                                            </c:when>
                                            <c:otherwise>
                                                <c:forEach items="${specialDayInfo}" var="detail1" varStatus="idx">
                                                    <tr>
                                                        <td>
                                                            <label id="NO" style="line-height: 30px;">${idx.count}</label>
                                                        </td>
                                                        <td>
                                                            <input type="hidden" name="specialType" value="1">
                                                            <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial;">
                                                                <option value="1" <c:if test="${detail1.randomType eq '1'}">selected</c:if>>固定</option>
                                                                <option value="0" <c:if test="${detail1.randomType eq '0'}">selected</c:if>>随机</option>
                                                            </select>
                                                        </td>
                                                        <td class="cogItemVpoints">
                                                            <div class="random-prize content" style="display: none;">
                                                                <input type="text" name="minVpoints" class="form-control input-width-small number integer maxValue"
                                                                       autocomplete="off" tag="validate" data-oldval="${detail1.minVpoints}" value="${detail1.minVpoints}" maxVal="999999" maxlength="6" style="display: initial;">
                                                                <label style="line-height: 30px;">&nbsp;&nbsp;-&nbsp;&nbsp;</label>
                                                                <input type="text" name="maxVpoints" class="form-control input-width-small number integer maxValue"
                                                                       autocomplete="off" tag="validate" data-oldval="${detail1.maxVpoints}" value="${detail1.maxVpoints}" maxVal="999999" maxlength="6" style="display: initial;">
                                                            </div>
                                                            <div class="fixation-prize" style="display: inline-flex;">
                                                                <input type="text" name="fixationVpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                                       tag="validate" data-oldval="${detail1.minVpoints}" value="${detail1.minVpoints}" maxVal="999999" maxlength="6" style="display: initial; width: 200px !important;">
                                                            </div>
                                                        </td>
                                                        <td class="cogItemVpoints">
                                                            <label id="unitVpoints" style="line-height: 30px;">0.00</label>
                                                        </td>
                                                        <td style="position: relative;">
                                                            <div style="display: inline-flex;">
                                                                <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue"
                                                                       autocomplete="off" tag="validate" data-oldval="${detail1.prizePercent}" value="${detail1.prizePercent}" maxVal="100" maxlength="7" style="display: initial; width: 65px !important;">
                                                                <label style="line-height: 30px;">%</label>
                                                            </div>
                                                        </td>
                                                        <td style="position: relative;">
                                                            <input type="text" id="cogAmounts" name="cogAmounts" class="form-control input-width-small number" data-oldval="${detail1.cogAmounts}" value="${detail1.cogAmounts}" maxlength="7" autocomplete="off"  style="display: none; width: 60px !important;">
                                                        </td>
                                                        <td style="position: relative;">
                                                            <label id="cogAmountsLabel" style="line-height: 30px; min-width: 55px;">--</label>
                                                            <input type="text" id="restAmounts" name="restAmounts" class="form-control input-width-small number" data-oldval="${detail1.restAmounts}" value="${detail1.restAmounts}" maxlength="7" autocomplete="off" readonly style="width: 60px !important;">
                                                            <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                        </td>
                                                    </tr>
                                                </c:forEach>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr>
                                            <td>合计</td>
                                            <td>--</td>
                                            <td class="cogItemVpoints">--</td>
                                            <td class="cogItemVpoints">--</td>
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
                            <button class="btn btn-blue btnSave marr20" >保 存</button>
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
