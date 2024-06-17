<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath();
    String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>

    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>

    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];

        // 本界面上传图片要求
        var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };

        $(function(){
            // 初始化校验控件
            $.runtimeValidate($("form"));
            $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
            //容积千分符
            $("input[name='volume']").on('keyup', function () {
                $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
            });

            // 规则
            $("a.rule").off();
            $("a.rule").on("click", function(){
                if (!$('input[name="activityKey"]').val()) return;
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=13";
                $("form").attr("action", url);
                $("form").submit();
            });

            // 初始化功能
            initPage();
        });

        function initPage() {

            //待激活红包有效期动态效果
            $('input[name="validityType"]').change(function() {
                $('.valid_validityType').hide().html('');
                console.log('...',  $(this).val())
                if ($(this).val() === '0') {
                    $('input[name="effectiveDays"]').show();
                    $('input[name="fixedDeadline"]').hide();
                } else if ($(this).val() === '1') {
                    $('input[name="fixedDeadline"]').show();
                    $('input[name="effectiveDays"]').hide().val('');
                }
            });

            $('input[name="endDate"]').focus(function() {
                var limitTime = "WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'" + $(this).val() + " 23:59:59' })"
                $('input[name="fixedDeadline"]').attr('onfocus', limitTime)
            })

            $('input[name="effectiveDays"]').keyup(function () {
                if (parseInt($(this).val()) > 60) {
                    $('.valid_validityType').show().html('当前可用的最大数值为60');
                } else {
                    $('.valid_validityType').hide().html('');
                }
            })

            $('input[name="fixedDeadline"]').focus(function () {
                $('.valid_validityType').hide().html('');
            })

            // 增加倍数中出规则
            $("form").on("click", "#AddMoneyMax", function () {
                let idxx = $(".AddMoneyMax").length
                if(idxx<=4){
                    if ($(this).text() == '新增') {
                        var $copySku = $(this).parent().closest("div").clone(true, true);
                        $copySku.find("#AddMoneyMax").text("删除");
                        $copySku.find("#infoKey").val("");
                        $copySku.find("#drawNum").val("");
                        $copySku.find("#money").val("");
                        $copySku.find("#putInNum").val("");
                        $copySku.find("#todayNum").val("");
                        $(this).closest("td").append($copySku);
                    } else {
                        $(this).closest("div").remove();
                    }
                }else{
                    if ($(this).text() == '删除') {
                        $(this).closest("div").remove();
                    }
                }
            });

            // 增加频次
            $("form").on("click", "#addPerItem", function() {
                var idx = $("td.perItem div").index($(this).parent("div.perItem"));
                if (idx == 0) {
                    if($("td.perItem div").length < 12){
                        var $timeCopy = $("div.perItem").eq(0).clone();
                        $("td.perItem").append($timeCopy);
                        $timeCopy.find("#addPerItem").text("删除");
                        $("td.perItem div.perItem:last").find("input[name='uuid']").val("");
                        $("td.perItem div.perItem:last").find("input[name='perItem']").val("");
                        $("td.perItem div.perItem:last").find("input[name='moneys']").val("");
                        $("td.perItem div.perItem:last").find("input[name='launchPerDay']").val("");
                        $("td.perItem div.perItem:last").find("input[name='todayGrantNum']").val("");
                    }
                } else {
                    $(this).parent("div.perItem").remove();
                }

            });

            // 增加SKU
            $("form").on("click", "#addActivationSku", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addActivationSku").text("删除");
                    $copySku.find('option:selected').eq(0).removeAttr("selected")
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
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

                if(flag){
                    //倍数中出规则校验
                    var needCheckRule = true;
                    var multipledivs = $("div[class*='multiplediv']");
                    multipledivs.each(function(devindex) {
                        var inputs = $(this).find(":input:enabled:not(#infoKey)");
                        inputs.each(function(index) {
                            if ($(this).val() === '') {
                                needCheckRule = false;
                            }
                        });
                    });

                    console.log("=======================needCheckRule:"+needCheckRule)
                    if (needCheckRule){
                        flag = checkRule(flag);
                        console.log("=======================checkRule校验方法结束后报存方法内获取的flag:"+flag)
                    }
                }

                if(flag){
                    //组装行政区域参数
                    var activityScopeType = $('input[name="activityScopeType"]:checked').val();
                    if (activityScopeType === "0"){
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
                        $("input[name='areaCode']").val(areaCode);
                    }
                }

                if (flag) {
                    $("input[name='volume']").val(unThousandth($("input[name='volume']").val()));
                    var url = $(this).attr("url");
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                    if ($('input[name="activityKey"]').val()) {
                        $("a.rule").removeClass('disabled')
                    }
                }
                return false;
            });

            var imgData='${waitActivationRule.ruleImageUrl}';
            if(imgData){
                showImg(imgData);
            }

            if (!$('input[name="activityKey"]').val()) {
                $("a.rule").addClass('disabled')
            }

            // 活动范围选择 hotAreaTrId
            $("input[name='activityScopeType']").on("change", function(){
                if($(this).attr("id") == "activityScopeType1"){
                    $("#areaTrId").css("display", "");
                    $("#hotAreaTrId").css("display", "none");
                }else{
                    $("#areaTrId").css("display", "none");
                    $("#hotAreaTrId").css("display", "");
                }
            });

            // 增加区域
            $("form").on("click", "#addArea", function () {
                if ($(this).text() == "新增") {
                    var $areaCopy = $("div.area").eq(0).clone();
                    $areaCopy.find("#addArea").text("删除");
                    $("td.area").append($areaCopy);
                    $areaCopy.initZone("<%=cpath%>", false, "", false, true, false, true);

                } else {
                    $(this).closest("div.area").remove();
                }
            });

            // 初始化省市县
            $("#addArea").closest("div").initZone("<%=cpath%>", false, "", false, true, false, true);

            var filterAreaCodeAry = "${waitActivationRule.areaCode}".split(",");
            console.log("===========>初始化省市县aa  filterAreaCodeAry:"+filterAreaCodeAry);
            $.each(filterAreaCodeAry, function (idx, val, ary) {
                console.log("===========>初始化省市县aa  val:"+val);
                if (val != '') {
                    if (val == '000000') {

                        $("td.area div:last-child").find("select").val(val);
                    } else {
                        if (idx > 0) $("#addArea").click();
                        $("td.area div:last-child").initZone("<%=cpath%>", false, val, false, true, false, true);
                    }
                }
            });
            /*var activityScopeType = "${waitActivationRule.activityScopeType}";
            if (activityScopeType==="0"){
                var filterAreaCodeAry = "${waitActivationRule.areaCode}".split(",");
                console.log("===========>初始化省市县  filterAreaCodeAry:"+filterAreaCodeAry);
                $.each(filterAreaCodeAry, function (idx, val, ary) {
                    console.log("===========>初始化省市县  val:"+val);
                    if (val != '') {
                        if (val == '000000') {
                            $("td.area div:last-child").find("select").val(val);
                        } else {
                            if (idx > 0) $("#addArea").click();
                            $("td.area div:last-child").initZone("<%=cpath%>", false, val, false, true, false, true);
                        }
                    }
                });
            }else{
                $("div.area").initZone("<%=cpath%>", false, "", false, true, false, true);
            }*/
        }

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            let msg = ''
            if ($('input[name="validityType"]:checked').val() === '0' && !$('input[name="effectiveDays"]').val()) {
                msg = '中出后有效天数不能为空';
            } else if ($('input[name="validityType"]:checked').val() === '1' && !$('input[name="fixedDeadline"]').val()) {
                msg = '固定截止日期不能为空';
            }

            if ($('input[name="validityType"]:checked').val() === '0' && parseInt($('input[name="effectiveDays"]').val()) > 60) {
                msg = '当前可用的最大数值为60';
            }
            if (msg) {
                $(".valid_validityType").show().html(msg);
                return false
            } else {
                $(".valid_validityType").hide().html('');
            }

            if(!validateResult){
                return false;
            }

            if (imgSrc.length == 0) {
                $('.valid_file').html('请上传活动规则图片');
                return false;
            }
            if (imgSrc.length > 1) {
                $('.valid_file').html('活动规则图片最多上传1张');
                return false;
            } else {
                $('.valid_file').html('');
                $("[name='ruleImageUrl']").val(imgSrc[0]);
            }

            /*寻宝说明图片校验*/
            console.log("===========>寻宝说明图片校验:"+$("input[name= productImageUrl]").val())
            if($("input[name= productImageUrl]").val() == "" || $("input[name= productImageUrl]").val() == null){
                $('.valid_file_pro').html('请上传寻宝说明图片');
                return false;
            }
            var productImageUrl = $("input[name= productImageUrl]").val()
            if (productImageUrl.split(",").length > 1){
                $('.valid_file_pro').html('寻宝说明图片最多上传1张');
                return false;
            }

            //倍数中出规则校验
            console.log("===========>倍数中出规则校验开始")
            var multipleCheck = false;
            var multipleFlag = false;
            var multipledivs = $("div[class*='multiplediv']");
            multipledivs.each(function(devindex) {
                var valNum = 0;
                var inputs = $(this).find(":input:enabled:not(#infoKey)");
                inputs.each(function(index) {
                    console.log("===========>当前循环的input索引："+index)
                    console.log("===========>当前循环的input值："+$(this).val())
                    if ($(this).val() !== '') {
                        valNum++;
                        if (index === 0 && $(this).val() === '0'){
                            $.fn.alert("倍数中出规则：每抽__次，值不能为0！");
                            multipleCheck = true;
                            return;
                        }
                        if (index === 1 && $(this).val() > 120){
                            $.fn.alert("倍数中出规则：必中__元，值不能超过120！");
                            multipleCheck = true;
                            return;
                        }
                    }
                });
                if (multipleCheck){
                    return;
                }
                if (valNum > 0 && valNum !== inputs.size()) {
                    $.fn.alert("请完善倍数中出规则");
                    multipleCheck = true;
                    return;
                }
                //如果只有一个dev,全都没有值，则不做倍数中出待激活比例校验
                if (multipledivs.size()===1 && valNum === 0){
                    return;
                }
                //如果不是第一个dev,全都没有值，则提示删除空白倍数中出规则
                if (multipledivs.size() > 1 && valNum===0){
                    $.fn.alert("请完善倍数中出规则");
                    multipleCheck = true;
                    return;
                }
                //如果循环到最后一个dev，multipleCheck没有变成true 就是multipleCheck都通过了
                if (devindex === multipledivs.size()-1 && !multipleCheck){
                    multipleFlag = true;
                }
            });
            if(multipleCheck){
                return false;
            }
            if (multipleFlag && $("input[name= multipleVpointsRatio]").val() === ''){
                $.fn.alert("请完善倍数中出待激活比例");
                return false;
             }
            console.log("===========>倍数中出规则校验结束")

            //活动范围为热区时的校验
            var activityScopeType = $('input[name="activityScopeType"]:checked').val();
            if (activityScopeType === "1"){
                var selectedValue = $('select[name="hotAreaKey"]').find('option:selected').val();
                console.log("===========>活动范围为热区时的校验:"+selectedValue);
                if (selectedValue === "") {
                    $.fn.alert("当前活动范围类型为热区，请选则一个热区作为活动范围");
                    return false;
                }
            }

            //频次校验
            var perItemAry = "";
            var perItemCheckAry = [];
            var returnFlag = false;
            $("td.perItem div.perItem").each(function(i){
                var $uuid = $(this).find("input[name='uuid']");
                var $perItem = $(this).find("input[name='perItem']");
                var $moneys = $(this).find("input[name='moneys']");
                var $launchPerDay = $(this).find("input[name='launchPerDay']");
                if ($perItem.val() != "" && $moneys.val() != "" && $launchPerDay.val() != ""){
                    var res = perItemCheckAry.indexOf($perItem.val());
                    if (res == -1){
                        perItemCheckAry.push($perItem.val());
                        if ($uuid.val() == '' || $uuid.val() == null){
                            perItemAry += $perItem.val() + ":" + $moneys.val() + ":" + $launchPerDay.val() + ";";
                        }else{
                            perItemAry += $uuid.val() + ":" + $perItem.val() + ":" + $moneys.val() + ":" + $launchPerDay.val() + ";";
                        }
                    }else{
                        $.fn.alert("用户首次激活红包后，抽奖次数配置出现重复值!");
                        returnFlag = true;
                        return false; // 相当于break
                    }
                }
            });
            if(returnFlag){
                return false;
            }
            if(perItemAry != ""){
                $("input[name='freItems']").val(perItemAry.substring(0, perItemAry.length - 1));
            }

            return true;
        }

        // 倍数中出规则-爆点规则互斥校验
        function checkRule(flag) {
            var activityKey = $("input[name=activityKey]").val()
            var startDate = $("input[name=startDate]").val()
            var endDate = $("input[name=endDate]").val()
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/waitActivation/multipleCheckRule.do",
                async: false,
                data: {"activityKey":activityKey,"startDate":startDate,"endDate":endDate},
                dataType: "json",
                beforeSend: appendVjfSessionId,
                success: function (result) {
                    console.log("=======================result:"+result)
                    console.log("=======================result[success]:"+result["success"])
                    console.log("=======================result[rebateRuleCogList]:"+result["rebateRuleCogList"])
                    if (result["success"]) {
                        var rebateRuleCogList = result["rebateRuleCogList"]
                        if(Array.isArray(rebateRuleCogList) && rebateRuleCogList.length > 0){
                            var ruleNames = [];
                            for (var i = 0; i < rebateRuleCogList.length; i++) {
                                var currentElement = rebateRuleCogList[i];
                                var ruleName = currentElement.rebateRuleName;
                                ruleNames.push(ruleName);
                            }
                            var ruleNamesStr = ruleNames.join(", ");
                            $.fn.alert("倍数中出规则-爆点规则互斥校验失败，规则名称："+ruleNamesStr);
                            flag = false;
                        }
                    }else{
                        $.fn.alert("倍数中出规则-爆点规则互斥校验异常!");
                        flag = false;
                    }
                },
                error: function (data) {
                    $.fn.alert("倍数中出规则-爆点规则互斥校验异常!");
                    flag = false;
                }
            });

            console.log("=======================checkRule校验方法最后返回:"+flag)
            return flag;
        }

        // 检查value类型：object文本框，type类型：1数字，2金额
        function inspectValueType(object, type){
            var value = $(object).val();
            if(value!=''){
                if(type == '1'){
                    if (!/^[1-9][0-9]*$/.test(value) || value <= 0){
                        $(object).val("");
                    }
                }else if(type == '2'){
                    if(!/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value) || value <= 0){
                        $(object).val("0.00");
                    }
                }else if(type == '3'){
                    if(!/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value) || value <= 0){
                        $(object).val("");
                        $.fn.alert("需是含有小数点后两位的数值且大于0，如1.00，最大值199.99");
                    }else{
                        if(value > 199.99){
                            $(object).val("");
                            $.fn.alert("最大值199.99");
                        }
                    }
                }
            }
        }
    </script>

    <style>
        .white {
            color: white;
        }
        a.rule.disabled {
            background: #999;
            color: #fff;
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
        .validate_tips, .validate_tips_valid_fail_text {
            padding: 8px !important;
        }
        .validate_tips_valid_fail_text {
            color: #c54242;
            font-weight: bold;
            width: 30%;
            height: 30px;
            float: left;
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
            <li class="current"><a title="">活动管理</a></li>
            <li class="current"><a title="">天降红包配置</a></li>
        </ul>
    </div>

    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post"
                  class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/waitActivation/editWaitActivationConfig.do">
                <input type="hidden" name="activityKey" id="activityKey" value="${waitActivationRule.activityKey}"/>
                <input type="hidden" id="ruleImageUrl" name="ruleImageUrl" value="${waitActivationRule.ruleImageUrl}" />
                <input type="hidden" id="productImageUrl" name="productImageUrl" value="${waitActivationRule.productImageUrl}" />
                <input type="hidden" name="areaCode" value="${waitActivationRule.areaCode}" />
                <input type="hidden" name="freItems"/>
                <%--配置活动--%>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>配置活动</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <%--活动名称--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName"
                                               class="form-control required"
                                               tag="validate" autocomplete="off" maxlength="15"
                                               value="${waitActivationRule.activityName}"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--活动时间--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="startDate" id="startDate"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" autocomplete="off" value="${waitActivationRule.startDate}"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" autocomplete="off" value="${waitActivationRule.endDate}"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--参与周期--%>
                            <tr>
                                <td class="ab_left"><label class="title mart5">参与周期：<span class="required">*</span></label></td>
                                <td class="ab_main time">
                                    <div class="content time" style="margin: 5px 0px; display: flex;">
                                        <input name="startTimeSpan" id="startTimeSpan"
                                               class="form-control input-width-medium required Wdate"
                                               tag="validate" autocomplete="off" value="${waitActivationRule.startTimeSpan}"
                                               onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'endTimeSpan\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTimeSpan" id="endTimeSpan"
                                               class="form-control input-width-medium required Wdate"
                                               tag="validate" autocomplete="off" value="${waitActivationRule.endTimeSpan}"
                                               onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'startTimeSpan\')}'})"/>
                                        <span class="blocker en-larger">活动时间内每天</span>
                                    </div>
                                </td>
                            </tr>

                            <%--活动范围--%>
                            <tr id="activityScopeTrId">
                                <td class="ab_left"><label class="title">活动范围：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <%--默认选中区域--%>
                                    <c:if test="${empty(waitActivationRule.activityScopeType) or waitActivationRule.activityScopeType=='0'}">
                                        <div class="content">
                                            <input type="radio" class="radio" id="activityScopeType1" name="activityScopeType" value="0" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
                                            <span class="blocker en-larger">区域</span>
                                            <input type="radio" class="radio" id="activityScopeType2" name="activityScopeType" value="1" style="float:left; cursor: pointer; min-height: 33px;" />
                                            <span class="blocker en-larger">热区</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty(waitActivationRule.activityScopeType) and waitActivationRule.activityScopeType=='1'}">
                                        <div class="content">
                                            <input type="radio" class="radio" id="activityScopeType1" name="activityScopeType" value="0" style="float:left; cursor: pointer; min-height: 33px;" />
                                            <span class="blocker en-larger">区域</span>
                                            <input type="radio" class="radio" id="activityScopeType2" name="activityScopeType" value="1" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
                                            <span class="blocker en-larger">热区</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </c:if>
                                </td>
                            </tr>
                            <!-- 公共行政区域tr -->
                            <tr id="areaTrId" <c:if test="${waitActivationRule.activityScopeType != null and waitActivationRule.activityScopeType=='1'}">style="display: none" </c:if>>
                                <td class="ab_left"><label class="title">限定区域：<span class="required">*</span></label></td>
                                <td class="ab_main area" colspan="3">
                                    <div class="area" style="display: flex; margin: 5px 0px;">
                                        <select name="provinceAry" class="zProvince form-control input-width-normal"></select>
                                        <select name="cityAry" class="zCity form-control input-width-normal"></select>
                                        <select name="countyAry" class="zDistrict form-control input-width-normal"></select>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
                                    </div>
                                </td>
                            </tr>
                            <%--限定热区--%>
                            <tr id="hotAreaTrId" <c:if test="${waitActivationRule.activityScopeType == null or waitActivationRule.activityScopeType=='0'}">style="display: none" </c:if>>
                                <td class="ab_left"><label class="title mart5">限定热区：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <c:choose>
                                        <c:when test="${waitActivationRule.hotAreaKey != null}">
                                            <div class="content hot">
                                                <select name="hotAreaKey" class="hotArea form-control" style="width: 480px;">
                                                    <option value="">请选择</option>
                                                    <c:if test="${not empty(hotAreaList)}">
                                                        <c:forEach items="${hotAreaList}" var="hotArea">
                                                            <option value="${hotArea.hotAreaKey }" <c:if test="${hotArea.hotAreaKey == waitActivationRule.hotAreaKey }">selected="selected"</c:if>>${hotArea.hotAreaName}</option>
                                                        </c:forEach>
                                                    </c:if>
                                                </select>
                                            </div>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="content hot">
                                                <select id="hotAreaKey" name="hotAreaKey" class="hotArea form-control" style="width: 352px;">
                                                    <option value="">请选择</option>
                                                    <c:if test="${not empty(hotAreaList)}">
                                                        <c:forEach items="${hotAreaList}" var="hotArea">
                                                            <option value="${hotArea.hotAreaKey }">${hotArea.hotAreaName}</option>
                                                        </c:forEach>
                                                    </c:if>
                                                </select>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <%--倍数红包--%>
                            <%--每天活动倍数中出--%>
                            <tr>
                                <td class="ab_left"><label class="title mart5">每天活动倍数中出：</label></td>
                                <td class="ab_main time">
                                    <c:choose>
                                        <c:when test="${fn:length(multipleRuleList) > 0}">
                                            <c:forEach items="${multipleRuleList}" var="multipleRule" varStatus="idx">
                                                <div class="content time multiplediv" style="margin: 5px 0px; display: flex;">
                                                    <input type="hidden" name="infoKey" id="infoKey" value="${multipleRule.infoKey}"/>
                                                    <span class="blocker en-larger" style="margin-top: 5px;">每抽</span>
                                                    <input name="drawNum" id="drawNum"
                                                           class="form-control input-width-small number integer"
                                                           autocomplete="off" maxlength="9" tag="validate"
                                                           value="${multipleRule.drawNum}"/>
                                                    <span class="blocker en-larger" style="margin-top: 5px;">次，必中</span>
                                                    <input name="money" id="money"
                                                           class="form-control input-width-small number money"
                                                           autocomplete="off" maxlength="9" tag="validate"
                                                           value="<fmt:formatNumber value='${multipleRule.money}' pattern='#0.00'/>"/>
                                                    <span class="blocker en-larger" style="margin-top: 5px;">元，每天投放</span>
                                                    <input name="putInNum" id="putInNum"
                                                           class="form-control input-width-small number integer"
                                                           autocomplete="off" maxlength="9" tag="validate"
                                                           value="${multipleRule.putInNum}"/>
                                                    <span class="blocker en-larger" style="margin-top: 5px;">个，已中出</span>
                                                    <input id="todayNum"
                                                           type="text" disabled="disabled"
                                                           class="form-control input-width-small"
                                                           autocomplete="off"
                                                           value="${multipleRule.todayNum}"/>
                                                    <label class="title mart5 btn-txt-add-red AddMoneyMax" style="font-weight: normal; margin-left: 5px;width:50px" id="AddMoneyMax"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                                    <label class="validate_tips" style="width: 100px"></label>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="content time multiplediv" style="margin: 5px 0px; display: flex;">
                                                <span class="blocker en-larger" style="margin-top: 5px;">每抽</span>
                                                <input name="drawNum" id="drawNum"
                                                       class="form-control input-width-small number integer"
                                                       autocomplete="off" maxlength="9" tag="validate"/>
                                                <span class="blocker en-larger" style="margin-top: 5px;">次，必中</span>
                                                <input name="money" id="money"
                                                       class="form-control input-width-small number money"
                                                       autocomplete="off" maxlength="9" tag="validate"/>
                                                <span class="blocker en-larger" style="margin-top: 5px;">元，每天投放</span>
                                                <input name="putInNum" id="putInNum"
                                                       class="form-control input-width-small number integer"
                                                       autocomplete="off" maxlength="9" tag="validate"/>
                                                <span class="blocker en-larger" style="margin-top: 5px;">个</span>
                                                <label class="title mart5 btn-txt-add-red AddMoneyMax" style="font-weight: normal; margin-left: 5px;width:50px" id="AddMoneyMax">新增</label>
                                                <label class="validate_tips" style="width: 200px"></label>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <%--倍数中出待激活比例--%>
                            <tr>
                                <td class="ab_left" ><label id="boomVpoints" class="title">倍数中出待激活比例：<span> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="multipleVpointsRatio"
                                               class="form-control input-width-larger minValue maxValue integer"
                                               tag="validate" autocomplete="off" type="number" minVal="0" maxVal="100"
                                               value="${waitActivationRule.multipleVpointsRatio}"/>
                                        <span class="blocker en-larger"> % </span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--拟投放金额--%>
                            <tr>
                                <td class="ab_left"><label class="title"> 拟投放金额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="proposedInvestmentAmount"
                                               class="form-control input-width-larger  number money preValue minValue maxValue  required"
                                               tag="validate" autocomplete="off" type="number" minVal="0" maxVal="999999999.00"
                                               value="${waitActivationRule.proposedInvestmentAmount}" />
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--已投放金额--%>
                            <tr>
                                <td class="ab_left"><label class="title"> 已投放金额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker en-larger"> ${waitActivationRule.postedAmount} 元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>


                    <%--配置用户策略--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>用户策略（仅支持待激活红包）</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <tr>
                                <input type="hidden" name="perItems" />
                                <td class="ab_left"><label class="title mart5">用户首次激活红包后：</label></td>
                                <td class="ab_main perItem">
                                    <c:choose>
                                        <c:when test="${not empty waitActivationRule.freItems}">
                                            <c:forEach items="${fn:split(waitActivationRule.freItems,';')}" var="item" varStatus="idx">
                                                <div class="content perItem" style="margin: 5px 0px;">
                                                    <span class="blocker en-larger">第</span>
                                                    <input hidden name="uuid" value="${fn:split(item,':')[0]}"/>
                                                    <input name="perItem" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                           tag="validate" value="${fn:split(item,':')[1]}" maxVal="9999" maxlength="4" onblur="inspectValueType(this, '1')"/>
                                                    <span class="blocker en-larger">次，金额</span>
                                                    <input name="moneys" class="form-control input-width-small number money maxValue"  autocomplete="off"
                                                           tag="validate" value="${fn:split(item,':')[2]}" maxVal="199.99"  maxlength="6" onblur="inspectValueType(this, '3')"/>
                                                    <span class="blocker en-larger">元，每天投放数</span>
                                                    <input name="launchPerDay" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                           tag="validate" value="${fn:split(item,':')[3]}" maxVal="99999999" maxlength="8" onblur="inspectValueType(this, '1')"/>
                                                    <span class="blocker en-larger" style="margin-top: 5px;">已中数</span>
                                                    <input type="text" name="todayGrantNum" class="form-control input-width-small" autocomplete="off"
                                                           disabled="disabled" value="${fn:split(item,':')[4]}"/>
                                                    <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addPerItem"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="content perItem" style="margin: 5px 0px;">
                                                <span class="blocker en-larger">第</span>
                                                <input name="perItem" class="form-control input-width-small number positive maxValue"  autocomplete="off"
                                                       tag="validate" maxVal="9999" maxlength="4" onblur="inspectValueType(this, '1')"/>
                                                <span class="blocker en-larger">次，金额</span>
                                                <input name="moneys" class="form-control input-width-small money maxValue"  autocomplete="off"
                                                       tag="validate" maxVal="199.99"  maxlength="6" onblur="inspectValueType(this, '3')"/>
                                                <span class="blocker en-larger">元，每天投放个数</span>
                                                <input name="launchPerDay" class="form-control input-width-small number positive maxValue"  autocomplete="off"
                                                       tag="validate" maxVal="99999999" maxlength="8" onblur="inspectValueType(this, '1')"/>
                                                <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addPerItem">新增</label>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </table>
                    </div>


                    <%--配置激活红包--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-saoma"></i>配置激活红包</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <%--同时领取待激活红包上限--%>
                            <tr>
                                <td class="ab_left" ><label id="sharerGetRewardLimit" class="title">待激活红包上限：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="redEnvelopeLimit"
                                               class="form-control input-width-larger minValue maxValue integer required"
                                               tag="validate" autocomplete="off" type="number" minVal="0" maxVal="999999999"
                                               value="${waitActivationRule.redEnvelopeLimit}"/>
                                        <span class="blocker en-larger">个（用户持有待激活红包的最大个数，0表示不限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--待激活红包有效期--%>
                            <tr>
                                <td class="ab_left"><label class="title">待激活红包有效期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <c:choose>
                                            <c:when test="${waitActivationRule.validityType != null}">
                                                <input type="radio" name="validityType" value="0" maxlength="2"
                                                       class="tab-radio" style="float:left;"
                                                        <c:if test= "${waitActivationRule.validityType eq '0'}"> checked="checked"</c:if>>
                                                <span class="blocker en-larger" style="margin-left: 2px;">中出后有效天数</span>
                                                <input name="effectiveDays" id="effectiveDays" type="number"
                                                       class="form-control "
                                                        <c:if test="${waitActivationRule.validityType eq '0'}"> style="width: 200px;" autocomplete="off"</c:if>
                                                        <c:if test="${waitActivationRule.validityType eq '1'}"> style="width: 200px;display:none;" </c:if>
                                                       value="${waitActivationRule.effectiveDays}" />

                                                <input type="radio"  name="validityType" value="1"
                                                       class="tab-radio" style="float:left; margin-left: 10px !important;"
                                                        <c:if test= "${waitActivationRule.validityType eq '1'}"> checked="checked" </c:if>>
                                                <span class="blocker en-larger" style="margin-left: 2px;">固定截至日期</span>
                                                <input name="fixedDeadline" id="fixedDeadline"
                                                       class="form-control input-width-medium Wdate  preTime"
                                                        <c:if test="${waitActivationRule.validityType eq '1'}"> style="width: 180px !important;"</c:if>
                                                        <c:if test="${waitActivationRule.validityType eq '0'}"> style="width: 180px !important;display:none;"</c:if>
                                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate: '${waitActivationRule.endDate} 23:59:59' })"
                                                       value="${waitActivationRule.fixedDeadline}" />
                                               <span class="valid_validityType validate_tips_valid_fail_text"></span>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="radio" name="validityType" value="0"
                                                       class="tab-radio" style="float:left;" checked="checked"/>
                                                <span class="blocker en-larger" style="margin-left: 2px;">中出后有效兑出天数</span>
                                                <input name="effectiveDays" id="effectiveDays" type="number"
                                                       class="form-control " autocomplete="off"
                                                       style="width: 200px;"/>

                                                <input type="radio" name="validityType" value="1"
                                                       class="tab-radio" style="float:left; margin-left: 20px !important;"/>
                                                <span class="blocker en-larger" style="margin-left: 2px;">固定截止时间</span>
                                                <input name="fixedDeadline" id="fixedDeadline"
                                                       class="form-control input-width-medium Wdate  preTime"
                                                      style="width: 180px !important;display:none;" autocomplete="off"
                                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(${waitActivationRule.endDate} 23:59:59)}' })" />
                                                <span class="valid_validityType validate_tips_valid_fail_text"></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>

                            <%--爆点红包待激活比例--%>
                            <tr>
                                <td class="ab_left" ><label id="boomVpoints" class="title">爆点红包待激活比例：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="boomVpointsRatio"
                                               class="form-control input-width-larger minValue maxValue integer required"
                                               tag="validate" autocomplete="off" type="number" minVal="0" maxVal="100"
                                               value="${waitActivationRule.boomVpointsRatio}"/>
                                        <span class="blocker en-larger"> % </span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--激活sku--%>
                            <tr>
                                <td class="ab_left"><label class="title">激活SKU：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <c:choose>
                                        <c:when test="${waitActivationRule.skuKeys != null}">
                                            <c:forEach items="${waitActivationRule.skuKeys}" var="skuKey" varStatus="idx">
                                                <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                    <select class="form-control input-width-larger required" name="skuKeyArray" tag="validate">
                                                        <option value="">请选择SKU</option>
                                                        <c:if test="${!empty skuList}">
                                                            <c:forEach items="${skuList}" var="sku">
                                                                <option value="${sku.skuKey}" <c:if test="${skuKey == sku.skuKey}"> selected</c:if> data-img="${sku.skuLogo}" >${sku.skuName}</option>
                                                            </c:forEach>
                                                        </c:if>
                                                    </select>
                                                    <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addActivationSku">${idx.count == 1 ? '新增' : '删除'}</label>
                                                    <label class="validate_tips"></label>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                <select class="form-control input-width-larger required" name="skuKeyArray" tag="validate">
                                                    <option value="">请选择SKU</option>
                                                    <c:if test="${!empty skuList}">
                                                        <c:forEach items="${skuList}" var="sku">
                                                            <option value="${sku.skuKey}" data-img="${sku.skuLogo}">${sku.skuName}</option>
                                                        </c:forEach>
                                                    </c:if>
                                                </select>
                                                <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addActivationSku">新增</label>
                                                <label class="validate_tips"></label>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>

                            <%--寻宝说明上传--%>
                            <tr style="height: 200px;">
                                <td class="ab_left"><label class="title">寻宝说明上传：<span class="required">*</span><br/></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px;" >
                                        <form enctype="multipart/form-data">
                                            <input type="hidden" id="imagepath" name="">  <!-- 保存的图片id 用于表单提交 -->
                                            <input id="imgUrl" name="prizeImgAry" type="hidden" class='filevalue' />
                                            <!-- 上传按钮 -->
                                            <div id="upload_duixiang" class="scroll">
                                                <img src="<%=cpath %>/inc/vpoints/img/a11.png" style="width: 130px;height:120px">
                                            </div>
                                            <div class="show scroll"  >
                                            </div> <!-- 输出图片 -->
                                        </form>
                                        <span class="valid_file_pro  validate_tips_valid_fail_text"></span>
                                    </div>
                                </td>
                            </tr>

                            <%--活动规则上传--%>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">活动规则上传：<span
                                        class="required">*</span><br/></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;"
                                                 id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file"
                                                           value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>
                                                </section>
                                            </div>
                                        </section>
                                        <span class="valid_file  validate_tips_valid_fail_text"></span>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除作品图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>
                        </table>
                    </div>


                    <%--规则配置--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-saoma"></i>规则配置</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <tr>
                                <td data-key="${waitActivationRule.activityKey}">
                                    <a class="btn btn-xs rule btn-orange"><i
                                            class="iconfont icon-xiugai"></i>&nbsp;编辑规则</a>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" >保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>



<link rel="stylesheet" type="text/css" href="<%=cpath%>/inc/goods/upload/FraUpload.css">
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/Sortable.js"></script>
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/MD5.js"></script>
<script type="text/javascript" src="<%=cpath%>/inc/goods/upload/FraUpload.js"></script>
<%--必须加onload--%>
<script onload="this" >
    var ids = [];
    var idsView = [];
    var ids2View = [];
    var ids3View = [];
    function addUrlImg(view_DOM,imgUrl,md5) {
        var html = '<li data-md5="'+md5+'">'+
            '<img style="width: 130px;height:120px" src=""/>'+
            '<div class="file-footer-buttons">'+
            '<span class="icon-loading" title="正在上传..."></span>'+
            '<span class="iconfont icon-delete" title="删除文件"></span>'+
            '</div>'+
            '</li>';
        $(view_DOM).append(html);
        $(view_DOM).find("li[data-md5='"+md5+"'] img:eq(0)").attr('src',imgUrl)
    }
    /**
     * 判断变量是否为空
     */
    function empty(value) {
        if (value == "" || value == undefined || value == null || value == false || value == [] || value == {}) {
            return true;
        } else {
            return false;
        }
    }

    // 寻宝说明图片上传
    var a = $("#upload_duixiang").FraUpload({
        view: ".show",      // 视图输出位置
        url:  basePath+"/skuInfo/imgUploadUrl.do", // 上传接口
        fetch: "img",   // 视图现在只支持img
        debug: false,    // 是否开启调试模式
        beforeSend:appendVjfSessionId,
        /* 外部获得的回调接口 */
        onLoad: function (e,view) { // 选择文件的回调方法
            let goodsUrl = 	$("#productImageUrl").val()
            console.log("===========>goodsUrl:"+goodsUrl)
            var goodsUrls = goodsUrl.split(",");
            for (let i = 0; i < goodsUrls.length; i++) {
                let md5 = goodsUrls[i];
                if(md5){
                    addUrlImg(view,imageServerUrl+"/"+goodsUrls[i],md5)
                    ids.push(goodsUrls[i]);
                    let fileAll = {
                        filename: goodsUrls[i].split("/")[goodsUrls[i].split("/").length-1],
                        size: 0,
                        type: "image/jpeg",
                        obj: {name:goodsUrls[i].split("/")[goodsUrls[i].split("/").length-1]},
                        md5: md5,
                        is_upload: '1',
                        ajax:{},
                    }
                    let responseStr = {path:goodsUrls[i]};
                    fileAll['ajax'] = JSON.stringify(responseStr);
                    idsView.push(fileAll);
                }
            }
            //等待初始化完成持行此方法
            setTimeout(function () {
                onload_setView(idsView)
            },1000)
        },
        breforePort: function (e) {         // 发送前触发
            console.log("===========>寻宝说明图片上传 发送前触发")
            $('.valid_file_pro').html('');
        },
        successPort: function (e) {         // 发送成功触发
            console.log("===========>寻宝说明图片上传 发送成功触发")
            onload_image()
        },
        errorPort: function (e) {       // 发送失败触发
            onload_image()
        },
        deletePost: function (e) {    // 删除文件触发
            console.log("===========>寻宝说明图片上传 删除文件触发")
            console.log("===========>寻宝说明图片上传 删除文件触发  删除前  ids.length:"+ids.length)
            console.log("===========>寻宝说明图片上传 删除文件触发  删除前  productImageUrl:"+$("input[name= productImageUrl]").val())
            var delBefor = ids.length

            onload_image()
            $('.valid_file_pro').html('');

            var delAfter = ids.length

            console.log("===========>寻宝说明图片上传 删除文件触发  删除后  ids.length:"+ids.length)
            console.log("===========>寻宝说明图片上传 删除文件触发  删除后  productImageUrl:"+$("input[name= productImageUrl]").val())
            if(delBefor === 1 && delAfter === 1){
                console.log("===========>寻宝说明图片已经删除完了但是productImageUrl还有原始值，则清空productImageUrl")
                $("#productImageUrl").val('');
            }
        },
        sort: function (e) {      // 排序触发
            onload_image()
        },
    });
    function onload_setView() {
        a.FraUpload.setViewCreate(idsView);
    }
    // 获取图片上传信息
    function onload_image() {
        var res = a.FraUpload.show()
        if(Object.keys(res).length!=0){
            ids = []
            for (let k in res) {
                let this_val = res[k]
                if (!empty(this_val['is_upload']) && !empty(this_val['ajax'])) {
                    let ajax_value = this_val['ajax'];
                    ajax_value = eval("(" + ajax_value + ")");
                    ids.push(ajax_value.path)
                }
            }
            console.log("===========>寻宝说明图片上传 onload_image ids:"+ids)
            $("#productImageUrl").val(ids);
        }
    }
</script>

</html>
