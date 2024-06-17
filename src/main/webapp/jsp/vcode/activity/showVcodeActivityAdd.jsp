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
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/select2/select2.min.js"></script>
    <link href="<%=cpath %>/inc/select2/select2.min.css" rel="stylesheet" type="text/css"/>

    
    <script>
        $(function(){

            //实现下拉框多选
            $('#skuList').select2({
                closeOnSelect: false,
                placeholder: "请选择",
            });

            // 初始化校验控件
            $.runtimeValidate($("div.tab-content"));
            
            // 初始化功能
            initPage();
        });
        
        function initPage() {

            // Tab切换
            $("div.tab-group a").on("click", function(){
                // 导航
                var tabIndex = $("div.tab-group a").index($(this));
                if (tabIndex != 0 && $(":hidden[name='vcodeActivityKey']").val() == '') {
                    $.fn.alert("请先创建并保存活动信息");
                } else if (tabIndex == 2 && $("div.tab-group a:eq(1)").data("saved") != "true") {
                    $.fn.alert("请先配置并保存风控规则！");
                } else {
                    $("ul.breadcrumb #currTab").text($(this).text());
                    $("ul.breadcrumb #currTab").data("tabindex", tabIndex);
                    $("div.activityinfo").css("display", tabIndex == 2 ? "none" : "");
                    
                    // 按钮状态
                    $("div.tab-group a").removeClass("btn-red");
                    $(this).addClass("btn-red");
                    
                    // 显示内容
                    $("div.tab-content").css("display", "none");
                    $("div.tab-content").eq(tabIndex).css("display", "");
                }
                
            });
            var tabObj = {};
            function locadTabObjData() {
                tabObj["tab"]={
                    sameMinuteRestrict: "",
                    sameDayRestrict:"",
                    sameMonthRestrict:"",
                    historyTimesRestrict:"",
                    doubtRuleRangeMin:"",
                    doubtRuleRangeMax:""
                }
            }

            $(".tabsObj").on("change",function(){
                if(!tabObj['tab']){
                    locadTabObjData();
                }
                let tabsObj = $("#tabsObj").val();
                if(!tabsObj){
                    tabsObj = 0;
                }
                var sameMinuteRestrict = $("input[name='sameMinuteRestrict']").val();
                var sameDayRestrict = $("input[name='sameDayRestrict']").val();
                var sameMonthRestrict = $("input[name='sameMonthRestrict']").val();
                var historyTimesRestrict = $("input[name='historyTimesRestrict']").val();
                let scanRole = $("#scanRole").val();
                if(sameMinuteRestrict || sameDayRestrict || sameMonthRestrict || historyTimesRestrict ) {
                    tabObj['tab' + scanRole] = {
                        scanRole: scanRole,
                        sameMinuteRestrict: $("input[name='sameMinuteRestrict']").val(),
                        sameDayRestrict: $("input[name='sameDayRestrict']").val(),
                        sameMonthRestrict: $("input[name='sameMonthRestrict']").val(),
                        historyTimesRestrict: $("input[name='historyTimesRestrict']").val(),
                        doubtfulTimeLimitType: $("input[name='doubtfulTimeLimitType']:checked").val(),
                        doubtRebateType: $("input[name='doubtRebateType']:checked").val(),
                        doubtRuleRangeMin: $("input[name='doubtRuleRangeMin']").val(),
                        doubtRuleRangeMax: $("input[name='doubtRuleRangeMax']").val(),
                        everyDayScanCount: $("input[name='everyDayScanCount']").val(),
                        everyWeekScanCount: $("input[name='everyWeekScanCount']").val(),
                        everyMonthScanCount: $("input[name='everyMonthScanCount']").val(),
                        activityScanCount: $("input[name='activityScanCount']").val(),
                    }
                }else{
                    tabObj['tab' + scanRole] = {}
                }
                console.log("tabObj add = ",tabObj)
            })
            // Tab1切换
            $("div.tab-group1 a").on("click", function(){
                if(!$("#tabsObj").val()){
                    $("#tabsObj").trigger("change");
                }else{
                    let sameMinuteRestrict = $("input[name='sameMinuteRestrict']").val();
                    let sameDayRestrict = $("input[name='sameDayRestrict']").val();
                    let sameMonthRestrict = $("input[name='sameMonthRestrict']").val();
                    let historyTimesRestrict = $("input[name='historyTimesRestrict']").val();
                    let doubtRuleRangeMin = $("input[name='doubtRuleRangeMin']").val();
                    let doubtRuleRangeMax = $("input[name='doubtRuleRangeMax']").val();
                    let doubtfulTimeLimitType = $("input[name='doubtfulTimeLimitType']:checked").val();
                    let doubtRebateType = $("input[name='doubtRebateType']:checked").val();

                    if(sameMinuteRestrict || sameDayRestrict || sameMonthRestrict || historyTimesRestrict ){
                        if(!doubtRuleRangeMin || !doubtRuleRangeMax|| !doubtfulTimeLimitType || !doubtRebateType ){
                            $.fn.alert("有必填项未填写");
                            return false;
                        }
                    }
                }
                $currBtn = $(this);
                $validContent = $(this).closest("div.tab-content");
                var validateResult = $.submitValidate($validContent);
                if(!validateResult){
                    $.fn.alert("填写验证未通过");
                    return false;
                }
                var ref = $(this).attr("ref");
                $("#scanRole").val(ref);
                // 导航
                var tabIndex = $("div.tab-group1 a").index($(this));
                //模板选为空
                $("#doubtTemplet option").eq(0).removeAttr("selected")
                $("#doubtTemplet option").eq(0).attr("selected", true);
                //错误验证切换清除
                $(".validate_tips").removeClass("valid_fail_text");
                $(".validate_tips").html("");
                $(".validate_tips").hide();
                // 按钮状态
                $("div.tab-group1 a").removeClass("btn-red");
                $(this).addClass("btn-red");
                $("#tabsObj").val(tabIndex);
                // 显示内容
                if(tabObj['tab'+ref]){
                    let obj = tabObj['tab' + ref];
                    $(":radio[name='doubtfulTimeLimitType']").removeAttr("checked");
                    $(":radio[name='doubtRebateType']").removeAttr("checked");
                    $("input[name='sameMinuteRestrict']").val(obj.sameMinuteRestrict);
                    $("input[name='sameDayRestrict']").val(obj.sameDayRestrict);
                    $("input[name='sameMonthRestrict']").val(obj.sameMonthRestrict);
                    $("input[name='historyTimesRestrict']").val(obj.historyTimesRestrict);
                    $("input[name='everyDayScanCount']").val(obj.everyDayScanCount);
                    $("input[name='everyWeekScanCount']").val(obj.everyWeekScanCount);
                    $("input[name='everyMonthScanCount']").val(obj.everyMonthScanCount);
                    $("input[name='activityScanCount']").val(obj.activityScanCount);
                    $(":radio[name='doubtfulTimeLimitType'][value="+obj.doubtfulTimeLimitType+"]").prop("checked","true");
                    $(":radio[name='doubtRebateType'][value="+obj.doubtRebateType+"]").prop("checked","true");
                    changeDoubtRebateType();
                    $("input[name='doubtRuleRangeMin']").val(obj.doubtRuleRangeMin);
                    $("input[name='doubtRuleRangeMax']").val(obj.doubtRuleRangeMax);
                }else {
                    $("input[name='sameMinuteRestrict']").val("");
                    $("input[name='sameDayRestrict']").val("");
                    $("input[name='sameMonthRestrict']").val("");
                    $("input[name='historyTimesRestrict']").val("");
                    $("input[name='doubtfulTimeLimitType']").removeAttr("checked");
                    $("input[name='doubtRebateType']").removeAttr("checked");
                    $("input[name='doubtRuleRangeMin']").val("");
                    $("input[name='doubtRuleRangeMax']").val("");
                    $("input[name='everyDayScanCount']").val("");
                    $("input[name='everyWeekScanCount']").val("");
                    $("input[name='everyMonthScanCount']").val("");
                    $("input[name='activityScanCount']").val("");
                }


            });
            <%--$("div.tab-group a").eq(Number("${childTab}") - 1).trigger("click");--%>
            <%--$("div.tab-group1 a").eq(Number("${childTab}") - 1).trigger("click");--%>
            
            // // SKU选择
            // $(".activitysku").on("change", function(){
            //     var skuType = $(this).find("option:selected").data("skutype");
            //     var skuTypeName = "";
            //     if (skuType == '0') {
            //         skuTypeName = "瓶码";
            //     } else if (skuType == '1') {
            //         skuTypeName = "罐码";
            //     } else if (skuType == '2') {
            //         skuTypeName = "箱码";
            //     }
            //     $("input[name='skuType']").val(skuTypeName);
            // });
            
            // 保存活动信息及风控规则
            $(".btnSave").on("click", function(){
                // 输入元素校验
                $validContent = $(this).closest("div.tab-content");
                var validateResult = $.submitValidate($validContent);
                if(!validateResult){
                    $.fn.alert("填写验证未通过");
                    return false;
                }
                if('1' === $("#projectName").val()){
                    if ($('.roleInfo input[type=checkbox]:checked').length == 0) {
                        $.fn.alert("请至少选择一个扫码角色");
                        return false;
                    }
                }
                // 当前Tab
                var tabIndex = $("ul.breadcrumb #currTab").data("tabindex");

                // JSON
                var paramJson = {};
                $(this).closest("div.tab-content").find(":input:enabled[type!=hidden][name]").each(function(){
                    if($(this).attr("type") == "radio") {
                        if ($(this).is(":checked")) {
                            paramJson[$(this).attr("name")] = $(this).val();
                        }
                    } else {
                        paramJson[$(this).attr("name")] = $(this).val();
                    }
                });
                //js获取复选框值
                var obj = document.getElementsByName("allowScanRole");//选择所有name="interest"的对象，返回数组
                var allowScanRole='';//如果这样定义var s;变量s中会默认被赋个null值
                for(var i=0;i<obj.length;i++){
                    //取到对象数组后，我们来循环检测它是不是被选中
                    if(obj[i].checked){
                        allowScanRole+=obj[i].value+',';   //如果选中，将value添加到变量s中
                    }
                }
                if(allowScanRole){
                    allowScanRole = allowScanRole.substr(0, allowScanRole.length - 1);
                }

                paramJson["allowScanRole"] = allowScanRole;
                let tabList = [];
                try{
                    Object.keys(tabObj).forEach(function(key){
                        let objtab = tabObj[key];
                        if(objtab.sameMinuteRestrict || objtab.sameDayRestrict || objtab.sameMonthRestrict || objtab.historyTimesRestrict ){
                            if(!objtab.doubtRuleRangeMin || !objtab.doubtRuleRangeMax||  (!objtab.doubtfulTimeLimitType && objtab.doubtfulTimeLimitType!=0) || !objtab.doubtRebateType ){
                                throw new Error("有必填项未填写");
                            }else{
                                if(parseInt(objtab.doubtRuleRangeMax) < parseInt(objtab.doubtRuleRangeMin) ){
                                    throw new Error("返利区间前一个数值不能大于后一个");
                                }
                                tabList.push(objtab)
                            }
                        }
                    });
                }catch (e) {
                    $.fn.alert(e.message);
                    return
                }
                paramJson["doubtableList"] = JSON.stringify(tabList);
                let tabobjw = tabObj['tab'];
                //判断必填项
                if(tabobjw) {
                    if (!(tabobjw.sameMinuteRestrict || tabobjw.sameDayRestrict || tabobjw.sameMonthRestrict || tabobjw.historyTimesRestrict)) {
                        $.fn.alert("通用风控规则必填一项");
                        return false;
                    }
                    if (tabobjw.sameMinuteRestrict == 0 && tabobjw.sameDayRestrict == 0 && tabobjw.historyTimesRestrict == 0 && tabobjw.sameMonthRestrict == 0) {
                        $.fn.alert("四种识别次数中至少一个要大于0");
                        return false;
                    }
                    if (!tabobjw.doubtfulTimeLimitType) {
                        $.fn.alert("可疑时间限制必须选择其中一个");
                        return false;
                    }
                    if (!tabobjw.doubtRebateType || !tabobjw.doubtRuleRangeMax || !tabobjw.doubtRuleRangeMin) {
                        $.fn.alert("请先配置并保存风控规则");
                        return false;
                    }
                }
                
                // 级联奖励
                var incentiveRoleAry = [];
                $(".cascadeIncentiveRole:checked:enabled").each(function(){
                    incentiveRoleAry.push($(this).val());
                });
                paramJson["cascadeIncentiveRole"] = incentiveRoleAry.join(",");

                // 多个SKU
                var selectedSkuKeys = [];
                // 获取并遍历所有选中的选项
                $('#skuList option:selected').each(function() {
                    selectedSkuKeys.push($(this).val()); // 将每个选中选项的value值添加到数组中
                });
                if(selectedSkuKeys.length == 0){
                    $.fn.alert("请选择SKU！");
                    return false;
                }else {
                    var skuList = selectedSkuKeys.join(','); // 使用join方法将数组元素以逗号连接成字符串
                    paramJson["skuList"] = skuList;
                }
                
                // 提交表单
                var vcodeActivityKey = $(":hidden[name='vcodeActivityKey']").val();
                var url = "<%=cpath%>/vcodeActivity/doVcodeActivityAdd.do";
                if (vcodeActivityKey != "") {
                    url = "<%=cpath%>/vcodeActivity/doVcodeActivityEdit.do";
                    paramJson["vcodeActivityKey"] = vcodeActivityKey;
                }
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        if (data['errMsg'] != "保存成功") {
                            $.fn.alert(data['errMsg'], function(){
                                $("button.btn-primary").trigger("click");
                            });
                        } else {
                            // 活动添加成功后活动主键
                            if (data["vcodeActivityKey"]) {
                                $(":hidden[name='vcodeActivityKey']").val(data["vcodeActivityKey"]);
                                $("#ruleFrame").attr("src", $("#ruleFrame").attr("src") + data["vcodeActivityKey"]);
                            } else {
                                $("#ruleFrame").attr("src", $("#ruleFrame").attr("src"));
                            }
                            // 保存过风控规则
                            if (tabIndex == 1) {
                                $("div.tab-group a:eq(1)").data("saved", "true");
                            }
                            // 切换Tab
                            $("div.tab-group a").eq(tabIndex + 1).trigger("click");
                        }
                    }
                });
                
            });

            // 可疑返利类型切换
            $("input[name='doubtRebateType']").on("change", function(){
                $("div.doubt input").val("");
                if ($(this).val() == "0") {
                    $("div.doubt input").addClass("money").removeClass("integer");
                    $("div.doubt input").attr("minVal", "0.00").attr("maxVal", "99.99");
                } else {
                    $("div.doubt input").addClass("integer").removeClass("money");
                    $("div.doubt input").attr("minVal", "0").attr("maxVal", "9999");
                }
                $("#doubtMoneylable").css("display", $(this).val() == "0" ? "" : "none");
                $("#doubtScorelable").css("display", $(this).val() == "1" ? "" : "none");
            });
            
            // 风控模板
            $("#doubtTemplet").on("change", function() {

                $option = $(this).find("option:selected");
                
                var timelimittype = $option.data("timelimittype")
                $("[name='sameMinuteRestrict']").val($option.data("minute"))
                $("[name='sameDayRestrict']").val($option.data("day"))
                $("[name='sameMonthRestrict']").val($option.data("month"))
                $("[name='historyTimesRestrict']").val($option.data("history"))
                if ( typeof(timelimittype) != "undefined") {
                    $(":radio[name='doubtfulTimeLimitType'][value="+timelimittype+"]").prop("checked","true");
                } else{
                   var timelimit=0;
                    $("input[name='doubtfulTimeLimitType']").eq(0).prop("checked", "true");
                  
                }
                $("[name='doubtRebateType'][value='" + $option.data("rebatetype") + "']").click();
                $("[name='doubtRuleRangeMin']").val($option.data("rangemin"))
                $("[name='doubtRuleRangeMax']").val($option.data("rangemax"))
                
                if ($option.data("desc")) {
                    $("#templetDesc").closest("tr").css("display", "");
                    $("#templetDesc").text($option.data("desc"));
                } else {
                    $("#templetDesc").closest("tr").css("display", "none")
                    $("#templetDesc").text("");
                }
            });
            
            // 扫码角色切换时
            $("[name='allowScanRole']").on("change", function() {
            	$("#agentIncentive, #dealerIncentive").attr("disabled", "disabled");
            	$("[name='allowScanRole']:checked").each(function(){
            		var itemVal = $(this).val();
            		if (itemVal == '0') {
                        $("#dealerIncentive").removeAttr("disabled");
            		} else if (itemVal == '1') {
            			$("#agentIncentive").removeAttr("disabled");
            		}
            	});
            });
        }
        function changeDoubtRebateType(){
            $("div.doubt input").val("");
            if ($("input[name='doubtRebateType']:checked").val() == "0") {
                $("div.doubt input").addClass("money").removeClass("integer");
                $("div.doubt input").attr("minVal", "0.00").attr("maxVal", "99.99");
            } else {
                $("div.doubt input").addClass("integer").removeClass("money");
                $("div.doubt input").attr("minVal", "0").attr("maxVal", "9999");
            }
            $("#doubtMoneylable").css("display", $("input[name='doubtRebateType']:checked").val() == "0" ? "" : "none");
            $("#doubtScorelable").css("display",$("input[name='doubtRebateType']:checked").val() == "1" ? "" : "none");
        }
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            
            return true;
        }
        
        // 校验可疑配置：四种识别次数中至少一个要大于0
        function checkDoubtCog() {
            var sameMinuteRestrict = Number($("input[name='sameMinuteRestrict']").val());
            var sameDayRestrict = Number($("input[name='sameDayRestrict']").val());
            var historyTimesRestrict = Number($("input[name='historyTimesRestrict']").val());
            var sameMonthRestrict = Number($("input[name='sameMonthRestrict']").val());
            if (sameMinuteRestrict == 0 && sameDayRestrict == 0 && historyTimesRestrict == 0&& sameMonthRestrict == 0) {
                return false;
            } else {
                return true;
            }
        }
        // 校验可疑配置：四种识别次数中至少一个要大于0
        function checkDoubtLimit() {
            var val=$('input:radio[name="doubtfulTimeLimitType"]:checked').val();
            if (null==val) {
                return false;
            } else {
                return true;
            }
        }

        // 定时更新iframe的高度
        var iframeClock = setInterval("setIframeHeight()", 50);
        function setIframeHeight() {
            iframe = document.getElementById('ruleFrame');
            if (iframe) {
                var iframeWin = iframe.contentWindow || iframe.contentDocument.parentWindow;
                if (iframeWin.document.body) {
                    iframe.height = iframeWin.document.documentElement.scrollHeight || iframeWin.document.body.scrollHeight;
                }
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
    </style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 活动配置</a></li>
            <li class="current"><a> 新增活动</a></li>
            <li class="current"><a id="currTab" data-tabindex="0"> 活动信息</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tab-group">
            <a class="btn tab btn-red">活动信息</a>
            <a class="btn tab">风控规则</a>
            <a class="btn tab">配置活动规则</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="vcodeActivityKey"/>
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                <input type="hidden" id="tabsObj" class="tabsObj" value=""/>
                <input type="hidden" id="scanRole" class="scanRole" value=""/>
                <input type="hidden" name="projectName" id="projectName" value="${projectName}"/>
                <div class="widget box activityinfo">
<!-- 	                活动信息 -->
                    <div class="tab-content">
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="vcodeActivityName" tag="validate" style="width: 400px !important;"
                                                class="form-control required" autocomplete="off" maxlength="50"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker">从</span>
                                            <input name="startDate" id="startDate" class="form-control input-width-medium Wdate required preTime"
                                                tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                            <span class="blocker en-larger">至</span>
                                            <input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime"
                                                tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content ">
                                            <select multiple="multiple" class="form-control input-width-large" style="width: 400px !important;" id="skuList">
                                                <option value="">请选择</option>
                                                <c:if test="${!empty skuList}">
                                                <c:forEach items="${skuList}" var="sku">
                                                <option value="${sku.skuKey}" data-skutype="${sku.skuType}">${sku.skuName}</option>
                                                </c:forEach>
                                                </c:if>
                                            </select>
                                        </div>
                                    </td>
                                </tr>
<%--	                                <tr>--%>
<%--	                                    <td class="ab_left"><label class="title">活动年份：<span class="required">*</span></label></td>--%>
<%--	                                    <td class="ab_main" colspan="3">--%>
<%--	                                        <div class="content">--%>
<%--	                                            <input name="activityYear" tag="validate" style="width: 400px !important;"--%>
<%--	                                                class="form-control required integer number minValue" autocomplete="off" maxlength="50" minVal="2020"/>--%>
<%--	                                            <label class="validate_tips"></label>--%>
<%--	                                        </div>--%>
<%--	                                    </td>--%>
<%--	                                </tr>--%>
<%--                                <tr>--%>
<%--                                    <td class="ab_left"><label class="title">码源类型：<span class="required">*</span></label></td>--%>
<%--                                    <td class="ab_main" colspan="3">--%>
<%--                                        <div class="content skuInfo">--%>
<%--                                            <input name="skuType" class="form-control input-width-larger" readonly="readonly" disabled="disabled"/>--%>
<%--                                        </div>--%>
<%--                                    </td>--%>
<%--                                </tr>--%>
                                <tr>
                                    <td class="ab_left"><label class="title">活动版本：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <select name="activityVersion" tag="validate" class="form-control input-width-larger required">
                                                <option value="">请选择</option>
                                                <c:if test="${!empty versionLst}">
                                                <c:forEach items="${versionLst}" var="item" >
                                                <option value="${item.dataValue}">${item.dataAlias}</option>
                                                </c:forEach>
                                                </c:if>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <c:if test="${projectName == '1'}">
                                <tr id="roleInfoTr">
                                    <td class="ab_left"><label class="title">扫码角色：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content roleInfo"  >
                                            <c:if test="${not empty(roleInfoAll)}">
                                                <c:forEach items="${roleInfoAll }" var="roleItem">
                                                    <div style="float: left; margin-right: 10px;">
                                                        <span>${fn:split(roleItem, ':')[1]}</span>
                                                        <input type="checkbox"  name="allowScanRole"  value="${fn:split(roleItem, ':')[0]}" style="float: left;">
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr >
                                    <td class="ab_left"><label class="title">级联激励角色：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content"  >
                                            <div style="float: left; margin-right: 10px;">
                                                <span>经/分销商</span>
                                                <input type="checkbox" class="cascadeIncentiveRole" id="dealerIncentive" value="2,3" disabled="disabled" style="float: left;">
                                            </div>
                                            <div style="float: left; margin-right: 10px;">
                                                <span>网服</span>
                                                <input type="checkbox" class="cascadeIncentiveRole" id="agentIncentive" value="4" disabled="disabled" style="float: left;">
                                            </div>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                </c:if>
                            </table>
                        </div>
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                                <a class="btn btn-blue btnSave">保存并下一步</a>
                            </div>
                        </div>
                    </div>
<!--          风控规则 -->
                    <div class="tab-content" style="display: none;">
                        <div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>风控规则</h4></div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <c:if test="${projectName == '1'}">
                                    <tr>
                                        <td class="ab_left"><label class="title">角色：<span class="required"> </span></label></td>
                                        <td class="ab_main" colspan="3">
                                            <div class="content skuInfo">
                                                <div class="col-md-12 tabbable tab-group1">
                                                    <a class="btn tab1 btn-red" ref="">通用</a>
                                                    <c:if test="${not empty(roleInfoAll)}">
                                                        <c:forEach items="${roleInfoAll }" var="roleItem">
                                                            <a class="btn tab1" ref="${fn:split(roleItem, ':')[0]}">${fn:split(roleItem, ':')[1]}</a>
                                                        </c:forEach>
                                                    </c:if>

                                                </div>
                                            </div>
                                        </td>
                                    </tr>

                                </c:if>
                                <tr>
                                    <td class="ab_left"><label class="title">引用模板：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content skuInfo">
                                            <select id="doubtTemplet" class="form-control input-width-larger tabsObj">
                                                <option value="">请选择</option>
                                                <c:if test="${!empty doubtTempletLst}">
                                                <c:forEach items="${doubtTempletLst}" var="item">
                                                <option data-minute="${item.sameMinuteRestrict}" 
                                                        data-day="${item.sameDayRestrict}" data-month="${item.sameMonthRestrict}"
                                                        data-history="${item.historyTimesRestrict}" data-timeLimitType="${item.doubtfulTimeLimitType}" 
                                                    data-rebatetype="${item.doubtRebateType}" data-rangemin="${item.doubtRuleRangeMin}" data-rangemax="${item.doubtRuleRangeMax}"
                                                    data-desc="${item.templetDesc}">${item.templetName}</option>
                                                </c:forEach>
                                                </c:if>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">按分钟识别：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="sameMinuteRestrict" tag="validate" title="同一分钟扫码次数大于或等于"
                                                class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
                                                <span class="blocker en-larger">次</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">按每天识别：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="sameDayRestrict" tag="validate" title="同一天扫码次数大于或等于"
                                                class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
                                                <span class="blocker en-larger">次</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">按每月识别：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="sameMonthRestrict" tag="validate" title="同一月扫码次数大于或等于"
                                                   class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
                                            <span class="blocker en-larger">次</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">按累计识别：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="historyTimesRestrict" tag="validate" title="历史累计扫码次数大于或等于"
                                                class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
                                                <span class="blocker en-larger">次</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">可疑中出类型<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input type="radio" class="tab-radio doubtRadioMoney tabsObj" checked="checked" name="doubtRebateType" value="0"  style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">积分红包</span>
                                            <input type="radio" class="tab-radio doubtRadioScore tabsObj" name="doubtRebateType" value="1" style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">商城积分</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr class="doubt-rule">
                                    <td class="ab_left"><label class="title">中出区间：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content doubt">
                                            <span class="blocker en-larger">从</span>
                                            <input id="doubtRuleRangeMin" name="doubtRuleRangeMin" tag="validate" mark="blacklist"
                                                class="form-control  number money preValue minValue maxValue input-width-small tabsObj" autocomplete="off" maxlength="5" minVal="0.00" maxVal="99.99"/>
                                            <span class="blocker en-larger">至</span>
                                            <input id="doubtRuleRangeMax" name="doubtRuleRangeMax" tag="validate" mark="blacklist"
                                                class="form-control  number money sufValue minValue maxValue input-width-small tabsObj" autocomplete="off" maxlength="5" minVal="0.00" maxVal="99.99"/>
                                            <span id="doubtMoneylable" class="blocker en-larger">(元)</span>
                                            <span id="doubtScorelable" class="blocker en-larger" style="display: none;">(积分)</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr style="display: none;">
                                    <td class="ab_left"><label class="title">模板概述：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <textarea id="templetDesc" rows="5" readonly="readonly" disabled="disabled"
                                                class="form-control required tabsObj" autocomplete="off" maxlength="500" ></textarea>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"valign="top"><label class="title">可疑时间限制
                                        <span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content" id="limitTimeType">
                                            <input type="radio" class="tab-radio doubtRadioMoney tabsObj" name="doubtfulTimeLimitType" value="0" checked="checked" style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">3天</span>
                                            <input type="radio" class="tab-radio doubtRadioScore tabsObj" name="doubtfulTimeLimitType" value="1" style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">7天</span>
                                            <input type="radio" class="tab-radio doubtRadioScore tabsObj" name="doubtfulTimeLimitType" value="2" style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">15天</span>
                                            <input type="radio" class="tab-radio doubtRadioScore tabsObj" name="doubtfulTimeLimitType" value="3" style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">30天</span>     <span
                                                class="blocker en-larger"style="margin-left: 47px;">所有天数结束后自动释放可疑用户
                                        </span>
                                            <label class="validate_tips"></label>
                                        </div>

                                        <div class="content" colspan="3"style="float:left;width:95%">
                                            <input type="radio" class="tab-radio doubtRadioMoney tabsObj"
                                                   name="doubtfulTimeLimitType" value="4"  style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">本周</span><span
                                                class="blocker en-larger"style="margin-left: 200px;">
											仅加入本周算入可疑，下自然周自动释放</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                        <br>
                                        <div class="content" colspan="3"style="float:left;width:95%">
                                            <input type="radio" class="tab-radio doubtRadioMoney tabsObj" name="doubtfulTimeLimitType" value="5"  style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">本月</span> <span
                                                class="blocker en-larger" style="margin-left: 200px;">仅加入本月可算入可疑，下自然月自动释放</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                        <br>
                                        <div class="content" colspan="3"style="float:left;width:95%">
                                            <input type="radio" class="tab-radio doubtRadioMoney tabsObj"
                                                   name="doubtfulTimeLimitType" value="6"  style="float:left; cursor: pointer;" />
                                            <span class="blocker en-larger">永久</span> <span
                                                class="blocker en-larger" style="margin-left: 200px;">永久算入可疑，只能主动释放
                                        </span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </div>
                        
                        <!-- 风控规则 - 用户扫码次数限制规则 -->
	                	<div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>风控规则 —— 用户扫码次数限制规则 </h4>
                        </div>
                        <div class="widget-content panel no-padding">
	                		<table class="active_board_table">
	                            <tr>
	                                <td class="ab_left"><label class="title">每天扫码次数：<span class="required"> </span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="everyDayScanCount" tag="validate" title="每天扫码次数大于或等于" 
	                                            class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
	                                            <span class="blocker en-larger">次</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
								<tr>
									<td class="ab_left"><label class="title">每周扫码次数：<span class="required"> </span></label></td>
									<td class="ab_main" colspan="3">
										<div class="content">
											<input name="everyWeekScanCount" tag="validate" title="每分钟扫码次数大于或等于" 
												   class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
											<span class="blocker en-larger">次</span>
											<label class="validate_tips"></label>
										</div>
									</td>
								</tr>
	                            <tr>
	                                <td class="ab_left"><label class="title">每月扫码次数：<span class="required"> </span></label></td>
	                                <td class="ab_main" colspan="3">
	                                    <div class="content">
	                                        <input name="everyMonthScanCount" tag="validate" title="每月扫码次数大于或等于" 
	                                            class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
	                                            <span class="blocker en-larger">次</span>
	                                        <label class="validate_tips"></label>
	                                    </div>
	                                </td>
	                            </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">累计扫码次数：<span class="required"> </span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="activityScanCount" tag="validate" title="每月扫码次数大于或等于"
                                                   class="form-control input-width-larger number positive minValue tabsObj" autocomplete="off" minVal="1" maxlength="5" />
                                            <span class="blocker en-larger">次</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
	                		</table>
                	    </div>
                        
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                                <a class="btn btn-blue btnSave">保存并下一步</a>
                            </div>
                        </div>
                    </div>
                </div>
<!--          配置活动规则 -->
               <div class="tab-content" style="margin-top:-20px; display: none;">
                   <iframe id="ruleFrame" class="mart20" src="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vjfSessionId=${vjfSessionId}&activityType=${activityType}&vcodeActivityKey=" style="width: 100%;" scrolling="no" frameborder="no" border="0" onload="setIframeHeight()" on></iframe>
               </div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
