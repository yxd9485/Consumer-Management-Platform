<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath();
%>
<%@ page import="java.util.UUID"%>
<%
    UUID uuid = UUID.randomUUID();
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
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>

    <script>

        $(function(){
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));

            $("[data-toggle='popover']").popover();
            $('body').on('click', function(event) {
                if ($("div.popover").size() > 0
                    && $(event.target).closest("[data-toggle]").size() == 0
                    && !($(event.target).is(".popover") || $(event.target).closest("div.popover").size() == 1)) {
                    $("[data-toggle='popover']").popover('toggle');
                }
            });

            // 初始化功能
            initPage();

            // 关联活动初始数据
            $("form").on("change", "#vcodeActivityKey", function(){
                var typeName = "";
                var $str = $(this).val();
                var skuName = $str.split("@")[1];
                var skuType = $str.split("@")[2];
                if(skuType == "0"){
                    typeName = "瓶码";
                }else if(skuType == "1"){
                    typeName = "罐码";
                }else if(skuType == "2"){
                    typeName = "箱码";
                }else{
                    typeName = "其他";
                }
                $("#skuId").val(skuName);
                $("#activityTypeId").val(typeName);
            });

            // 增加日期
            $("form").on("click", "#addDate", function() {
                var idx = $("td.date div").index($(this).parent("div.date"));
                if (idx == 0) {
                    var count = $("td.date div").length;
                    if(count < 10){
                        var $dateCopy = $("div.date").eq(0).clone(true);
                        $("td.date").append($dateCopy);
                        $dateCopy.find("#addDate").text("删除");
                        var $beginDate = $("td.date div.date:last").find("input[name='beginDate']");
                        var $endDate = $("td.date div.date:last").find("input[name='endDate']");
                        $beginDate.attr("id", "beginDate" + count).val("");
                        $endDate.attr("id", "endDate" + count).val("");
                        $beginDate.attr("onfocus", $beginDate.attr("onfocus").replace("endDate0", "endDate" + count));
                        $endDate.attr("onfocus", $endDate.attr("onfocus").replace("beginDate0", "beginDate" + count));
                    }
                } else {
                    $(this).parent("div.date").remove();
                }
            });

            // 增加倍数
            $("form").on("click", "#addPerItem", function() {
                var idx = $("td.perItem div").index($(this).parent("div.perItem"));
                if (idx == 0) {
                    if($("td.perItem div").length < 10){
                        var $timeCopy = $("div.perItem").eq(0).clone();
                        $("td.perItem").append($timeCopy);
                        $timeCopy.find("#addPerItem").text("删除");
                        $("td.perItem div.perItem:last").find("input[name='uuid']").val("");
                        $("td.perItem div.perItem:last").find("input[name='perItem']").val("0");
                        $("td.perItem div.perItem:last").find("input[name='moneys']").val("0.00");
                        $("td.perItem div.perItem:last").find("input[name='vpoints']").val("0");
                        $("td.perItem div.perItem:last").find("input[name='launchPerDay']").val("0");
                    }
                } else {
                    $(this).parent("div.perItem").remove();
                }

            });

            // 检验名称是否重复
            $("input[name='frequencyName']").on("blur",function(){
                var frequencyName = $("input[name='frequencyName']").val();
                if(frequencyName == "") return;
                checkName(frequencyName);
            });
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

                    var areaCode = "";
                    var areaName = "";
                    // 组建筛选区域
                    $("td.area div.area").each(function (i) {
                        var $province = $(this).find("select.zProvince");
                        var $city = $(this).find("select.zCity");
                        var $county = $(this).find("select.zDistrict");
                        if ($county.val() != "") {
                            areaCode = areaCode + $county.val() + ",";
                        } else if ($city.val() != "") {
                            areaCode = areaCode + $city.val() + ",";
                        } else if ($province.val() != "") {
                            areaCode = areaCode + $province.val() + ",";
                        } else {
                            areaCode = "";
                            areaName = "";
                            return false;
                        }
                        $("input[name='province']").val($province.find("option:selected").text());
                        $("input[name='city']").val($city.find("option:selected").text());
                        $("input[name='county']").val($county.find("option:selected").text());
                        areaName = areaName + $province.find("option:selected").text() + "_"
                            + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
                    });
                    if(areaName.indexOf("全部") != -1){
                        areaCode="000000"
                    }
                    $("input[name='areaRange']").val(areaCode);

                    var url = $(this).attr("url");
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                }
                return false;
            });

            // 关联活动初始数据
            var typeName = "";
            var $str = $("#vcodeActivityKey").val();
            var skuName = $str.split("@")[1];
            var skuType = $str.split("@")[2];
            if(skuType == "0"){
                typeName = "瓶码";
            }else if(skuType == "1"){
                typeName = "罐码";
            }else if(skuType == "2"){
                typeName = "箱码";
            }else{
                typeName = "其他";
            }
            $("#skuId").val(skuName);
            $("#activityTypeId").val(typeName);

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

            // 增加活动下拉框
            $("form").on("click", "#addActivity", function() {
                var idx = $("td.activity div").index($(this).parent("div.activity"));
                if (idx == 0) {
                    var count = $("td.activity div").length;
                    if(count < 10){
                        var $activityCopy = $("div.activity").eq(0).clone(true);
                        $activityCopy.find("[name='vcodeActivityKey']").val("");
                        $("td.activity").append($activityCopy);
                        $activityCopy.find("#addActivity").text("删除");
                        var $activity = $("td.activity div.activity:last").find("input[name='vcodeActivityKey']");
                        $activity.attr("id", "vcodeActivityKey" + count).val("");
                    }
                } else {
                    $(this).parent("div.activity").remove();
                }
            });
            // 初始化筛选区域
            $("#addArea").closest("div").initZone("<%=cpath%>", false, "", false, true, false, true);


            // 增加区域
            $("form").on("click", "#addArea", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copyArea = $(this).closest("div").clone();
                    $copyArea.initZone("<%=cpath%>", false, "", false, true, false, true);
                    $copyArea.find("#addArea").text("删除");
                    $(this).closest("td").append($copyArea);

                } else {
                    $(this).closest("div").remove();
                }
            });

            // 初始化筛选区域
            var filterAreaCodeAry = "${frequencyCog.areaRange}".split(",");
            $.each(filterAreaCodeAry, function (idx, val, ary) {
                if (val != '') {
                    if (val == '000000') {

                        $("td.area div:last-child").find("select").val(val);
                    } else {
                        if (idx > 0) $("#addArea").click();
                        $("td.area div:last-child").initZone("<%=cpath%>", false, val, false, true, false, true);
                    }
                }
            });

            $('input[name=restrictTimeType]').on("click", function () {
                let restrictTimeType = $('input[name=restrictTimeType]:checked').val();
                if (restrictTimeType == '0') {
                    $("#remark1").show();
                    $("#remark2").hide();
                } else if(restrictTimeType == '1') {
                    $("#remark2").show();
                    $("#remark1").hide();
                }
            });

            let restrictTimeType = '${frequencyCog.restrictTimeType}';
            if (restrictTimeType == '0') {
                $("#remark1").show();
                $("#remark2").hide();
            } else if(restrictTimeType == '1') {
                $("#remark2").show();
                $("#remark1").hide();
            }
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
        function checkName(frequencyName){
            $.ajax({
                url : "${basePath}/frequency/checkBussionName.do",
                data:{
                    "infoKey":"${frequencyCog.infoKey}",
                    "bussionName":frequencyName
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
        
        function validCogByActivityKey(infoKey, vcodeActivityKeys, beginDate, endDate) {
            flagStatus = false;
            $.ajax({
                url : "${basePath}/frequency/validCogByActivityKey.do",
                data:{
                    "infoKey":infoKey,
                    "vcodeActivityKeys":vcodeActivityKeys,
                    "beginDate":beginDate,
                    "endDate":endDate
                },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend:appendVjfSessionId,
                success:  function(data){
                    if (data['errMsg'] != ''){
                        flagStatus = false;
                        alert(data['errMsg']);
                    } else {
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
            var frequencyName = $("input[name='frequencyName']").val();
            if(frequencyName == "") return false;
            checkName(frequencyName);
            if(!flagStatus){
                return false;
            }

            var returnFlag = false;
            var dateAry = "";
            var minBeginDate = "";
            var maxEndDate = "";

            // 组装多组日期
            $("td.date div.date").each(function(i){
                var $beginDate = $(this).find("input[name='beginDate']");
                var $endDate = $(this).find("input[name='endDate']");
                var $beginTime = $(this).find("input[name='beginTime']");
                var $endTime = $(this).find("input[name='endTime']");
                if($beginDate.val() == "" || $endDate.val() == ""){
                    $.fn.alert("日期范围不能为空!");
                    returnFlag = true;
                    return false; // 相当于break
                }
                if($beginTime.val() == "" || $endTime.val() == ""){
                    $.fn.alert("时间范围不能为空!");
                    returnFlag = true;
                    return false; // 相当于break
                }

                dateAry += $beginDate.val() + "#" + $endDate.val() + "#" + $beginTime.val() + "#" + $endTime.val() + ",";
                
                // 计算最小开始日期及最大结束日期
                if (minBeginDate == "" || $beginDate.val() < minBeginDate) {
                    minBeginDate = $beginDate.val();
                } 
                if (maxEndDate == "" || $endDate.val() > maxEndDate) {
                    maxEndDate = $endDate.val();
                } 
            });

            if(returnFlag){
                return false;
            }

            if(dateAry != ""){
                $("input[name='validTimeRange']").val(dateAry.substring(0, dateAry.length - 1));
            }

            // 组装多组倍数
            var perItemAry = "";
            $("td.perItem div.perItem").each(function(i){
                var $uuid = $(this).find("input[name='uuid']");
                var $perItem = $(this).find("input[name='perItem']");
                var $moneys = $(this).find("input[name='moneys']");
                var $vpoints = $(this).find("input[name='vpoints']");
                var $launchPerDay = $(this).find("input[name='launchPerDay']");
                if($perItem.val() == "0"){
                    $.fn.alert("频次配置必须大于0!");
                    returnFlag = true;
                    return false; // 相当于break
                }
                if($moneys.val() == "0" || $moneys.val() == ""){
                    $.fn.alert("频次配置必须大于0!");
                    returnFlag = true;
                    return false; // 相当于break
                }
                if($vpoints.val() == ""){
                    $vpoints.val(0)
                }
                if($launchPerDay.val() == "0" || $launchPerDay.val() == ""){
                    $.fn.alert("频次配置必须大于0!");
                    returnFlag = true;
                    return false; // 相当于break
                }
                if ($uuid.val() == '' || $uuid.val() == null) {
                    $uuid.val(uuid());
                }
                perItemAry += $uuid.val() + ":" + $perItem.val() + ":" + $moneys.val() + ":" + $vpoints.val() + ":"  + $launchPerDay.val() + ";";
            });

            if(returnFlag){
                return false;
            }

            if(perItemAry != ""){
                $("input[name='freItems']").val(perItemAry.substring(0, perItemAry.length - 1));
            }

            var vcodeActivityKeys = "";
            $("#vcodeActivityKey").each(function(i,obj){
                vcodeActivityKeys = vcodeActivityKeys + "," + $(obj).val();
            });
            
            // 校验活动是否有冲突
            validCogByActivityKey($("input[name='infoKey']").val(), vcodeActivityKeys, minBeginDate, maxEndDate);
            if(!flagStatus){
                return false;
            }

            if($("input[name='restrictVpoints']").val() == ""){
                $("input[name='restrictVpoints']").val("0");
            }
            if($("input[name='restrictMoney']").val() == ""){
                $("input[name='restrictMoney']").val("0.00");
            }
            if($("input[name='restrictBottle']").val() == ""){
                $("input[name='restrictBottle']").val("0");
            }
            if($("input[name='restrictCount']").val() == ""){
                $("input[name='restrictCount']").val("0");
            }
            return true;
        }

        // 生成uuid
        function uuid() {
            var s = [];
            var hexDigits = "0123456789abcdef";
            for (var i = 0; i < 36; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[14] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
            s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[8] = s[13] = s[18] = s[23] = "-";

            return s.join("");
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
            <li class="current"><a> 频次配置</a></li>
            <li class="current"><a>修改频次配置</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath%>/frequency/editFrequencyCog.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${frequencyCog.infoKey}" />
                <input type="hidden" name="areaRange"/>
                <input type="hidden" name="validTimeRange"/>
                <input type="hidden" name="freItems"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>修改频次规则</h4>
                    </div>
                    <div class="widget-header">
                        <h4 style="color: red">1、用户当年首次扫码不中出120元红包，19.03元等其他金额不限制。
                            2、每行频次规则，每个用户仅限获得一次红包。
                            3、频次红包中出优先级：由上至下</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="frequencyName" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${frequencyCog.frequencyName }"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="validDateRange" />
                                <td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                                <td class="ab_main date">
                                    <c:forEach items="${fn:split(frequencyCog.validTimeRange,',')}" var="validDateRange" varStatus="idx">
                                        <div class="content date" style="margin: 5px 0px;  display: flex;">
                                            <input name="beginDate" id="beginDate${idx.index}" class="Wdate form-control input-width-medium required preTime" value="${fn:split(validDateRange,'#')[0]}"
                                                   tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate${idx.index}\')}'})"/>
                                            <span class="blocker en-larger">至</span>
                                            <input name="endDate" id="endDate${idx.index}" class="Wdate form-control input-width-medium required sufTime" value="${fn:split(validDateRange,'#')[1]}"
                                                   tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate${idx.index}\')}'})"/>
                                            <span class="blocker en-larger">时间</span>
                                            <input name="beginTime" id="beginTime" class="form-control input-width-medium Wdate required" value="${fn:split(validDateRange,'#')[2]}"
                                                   tag="validate" value="00:00:00" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" />
                                            <span class="blocker en-larger">至</span>
                                            <input name="endTime" id="endTime" class="form-control input-width-medium Wdate required" value="${fn:split(validDateRange,'#')[3]}"
                                                   tag="validate" value="23:59:59" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime\')}'})" />
                                            <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addDate"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出限制类型：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0"
                                               style="float: left; height:20px; cursor: pointer;"
                                        <c:if test="${frequencyCog.restrictTimeType eq '0' }"> checked="checked" </c:if>>&nbsp;规则时间
                                    </div>
                                    <div style="float: left; width: 50px; line-height: 25px;">
                                        <input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1"
                                               style="float: left; height:20px; cursor: pointer;"
                                        <c:if test="${frequencyCog.restrictTimeType eq '1' }"> checked="checked" </c:if>>&nbsp;每天
                                    </div>
                                    <b id="remark1" style="color: red;display: none">规则时间内每个频次规则中能中出一次。</b>
                                    <b id="remark2" style="color: red;display: none">规则时间内每天每个频次规则中能中出一次。</b>
                                    <span class="blocker en-larger"></span>
                                    <label class="validate_tips" STYLE="display: none"></label>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出次数限制:<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker en-larger">每人中</span>
                                        <input name="restrictUserCount" tag="validate" value="${frequencyCog.restrictUserCount}"
                                               class="form-control number positive required input-width-small" autocomplete="off" maxlength="8" />
                                        <span class="blocker en-larger">次</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="perItems" />
                                <td class="ab_left"><label class="title mart5">活动频次：<span class="required">*</span></label></td>
                                <td class="ab_main perItem">
                                    <c:forEach items="${fn:split(frequencyCog.freItems,';')}" var="item" varStatus="idx">
                                        <div class="content perItem" style="margin: 5px 0px;">
                                            <span class="blocker en-larger">每抽</span>
                                            <input hidden name="uuid" value="${fn:split(item,':')[0]}">
                                            <input type="text" name="perItem" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                   tag="validate" data-oldval="0" value="${fn:split(item,':')[1]}" maxVal="999999" maxlength="6">
                                            <span class="blocker en-larger">次，金额</span>
                                            <input type="text" name="moneys" class="form-control input-width-small money maxValue"  autocomplete="off"
                                                   tag="validate" data-oldval="0.00" maxVal="9999.00" value="${fn:split(item,':')[2]}" maxlength="7">
                                            <span class="blocker en-larger">积分</span>
                                            <input type="text" name="vpoints" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                   tag="validate" data-oldval="0" value="${fn:split(item,':')[3]}" maxVal="9999" maxlength="4">
                                            <span class="blocker en-larger">每天投放个数</span>
                                            <input type="text" name="launchPerDay" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                   tag="validate" data-oldval="0" value="${fn:split(item,':')[4]}" maxVal="99999999" maxlength="8">
                                            <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addPerItem"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                        </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">区域限制：<span class=""></span></label>
                                </td>
                                <td class="ab_main area" colspan="3">
                                    <div class="area" style="display: flex; margin: 5px 0px;">
                                        <select name="provinceAry"
                                                class="zProvince form-control input-width-normal"></select>
                                        <select name="cityAry"
                                                class="zCity form-control input-width-normal"></select>
                                        <select name="countyAry"
                                                class="zDistrict form-control input-width-normal"></select>
                                        <label class="title mart5 btn-txt-add-red"
                                               style="font-weight: normal; margin-left: 5px;"
                                               id="addArea">新增</label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">联接活动：<span class="required">*</span></label></td>
                                <td class="ab_main activity">
                                    <input type="hidden" name="vcodeActivityKeys" />
                                    <c:forEach items="${fn:split(frequencyCog.vcodeActivityKey, ',')}" var="activityKey" varStatus="idx">
                                        <div class="content activity" style="margin: 5px 0px; display: flex;">
                                            <select class="form-control input-width-larger" id="vcodeActivityKey" name="vcodeActivityKey" disabled="disabled">
                                                <option value="">请选择</option>
                                                <c:forEach items="${activityList }" var="activity">
                                                    <option value="${activity.vcodeActivityKey }"
                                                            <c:if test="${activityKey eq activity.vcodeActivityKey}"> selected="selected" </c:if>>
                                                            ${activity.vcodeActivityName }
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="status" type="hidden" value=${frequencyCog.status } />
                                        <input id="status" type="checkbox" data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success"
                                               data-off-color="warning" <c:if test="${frequencyCog.status eq '1'}"> checked </c:if>/>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <c:if test="${frequencyCog.isBegin ne '2'}">
                                <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/frequency/showFrequencyCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</body>
</html>