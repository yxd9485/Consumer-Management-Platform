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
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>红包雨活动</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>

    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        $(document).ready(function () {
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));

            initPage();

        });

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            // 校验普扫奖项配置项百分比
            console.log($("#totalPrizePercent").text())
            if ($("#totalPrizePercent").text() != "100.0000%") {
                $.fn.alert("占比之和未达到100%");
                return false;
            }
            if (imgSrc.length > 1) {
                $.fn.alert("活动规则图片最多上传1张");
                return false;
            } else {
                $("[name='ruleContentUrl']").val(imgSrc[0]);
            }
            // 组建筛选区域
            var areaCode = "";
            var areaName = "";
            $("td.area div.area").each(function(i){
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
                areaName = areaName + $province.find("option:selected").text() + "_"
                    + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
            });
            $("input[name='areaCode']").val(areaCode);
            $("input[name='areaName']").val(areaName);
            if(areaCode == ""){
                alert("请筛选区域！")
                return false;
            }
/*            var validTime = false
            var startDate = $("#startDate").val()
            var endDate = $("#endDate").val()
            $.ajax({
                url : "${basePath}/redEnvelopeRain/checkDateTime.do",
                data:{
                    "infoKey":null,
                    "startDate":startDate,
                    "endDate":endDate,

                    "areaCodes":areaCode,
                },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    if (data) {
                        $.fn.alert("活动时间冲突");
                        validTime = false
                    } else  {
                        validTime = true
                    }
                }
            });
            if(!validTime){
                return false;
            }*/

// 			// 页面校验
// 			var pc=$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").val();
// 			if(pc&&pc.indexOf('个')>-1){
// 				$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").hidden();
// 			}
            // 页面校验
            // var v_flag = true;
            // $(".validate_tips:not(:hidden)").each(function(){
            //     if($(this).text() != ""){
            //         $.fn.alert($(this).text());
            //         v_flag = false;
            //     }
            // });
            // if(!validTime){
            //     return false;
            // }

            return true;
        }

        function initPage() {
            // 初始化模板状态显示样式
            $("#statusFlag").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if (state == true) {
                        $("input:hidden[name='statusFlag']").val("1");
                    } else {
                        $("input:hidden[name='statusFlag']").val("0");
                    }
                }
            });
            // 修改奖项配置项占比
            $("form").on("change", "input[name='prizePercent']", function(){
                var commonPercent = 0; // 普通奖项总百分比
                $(this).closest("tbody").find("input[name='prizePercent']").each(function(i,obj){
                    commonPercent += Number($(this).val());
                });
                console.log(commonPercent,"=commonPercent")
                var currVal = Number($(this).val());
                var currTotalPercent  = commonPercent;
                if (currTotalPercent > 100) {
                    currVal = 100 - (currTotalPercent - currVal);
                }
                $(this).val(currVal.toFixed(4));
                var totalPercent = commonPercent;

                if (totalPercent > 100){
                    totalPercent = 100;
                }
                $("#totalPrizePercent").text(totalPercent.toFixed(4) + "%").css("color", totalPercent.toFixed(4) == 100 ? "green" : "red");
                //
                // // 重新计算均价
                // calculateUnit();
            });
            // 底部按钮事件
            $(".button_place").find("button").click(function(){
                var btnEvent = $(this).data("event");

                // 返回
                if(btnEvent == "0"){
                    var url = $(this).data("url");
                    $("form").attr("onsubmit", "");
                    $("form").attr("action", url);
                    $("form").submit();

                    // 保存生效
                } else {
                    var flag = validForm();
                    if(flag) {
                        if(btnEvent == "1"){
                            if(confirm("确认发布？")){
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return true;
                        }
                    } else {
                        return false;
                    }
                }
            });

            // 初始化区域事件
            $(".addArea").closest("div").initZone("<%=cpath%>", false, "", true, true, false, true);
            $("form").on("click","select[name=provinceAry]",function(){
                let provinecCode = $(this).closest("div").find("select[name=provinceAry] option:selected").val();
                if(provinecCode=='000000'){

                    return;
                }
            })
            // 省市县变化时初始化热区
            $("form").on("change", "div.area select", function(){
                if ($(this).val() != "") {
                    var areaCode = "";
                    $("td.area div.area").each(function(i){
                        var $province = $(this).find("select.zProvince");
                        var $city = $(this).find("select.zCity");
                        var $county = $(this).find("select.zDistrict");
                        if ($county.val() != "") {
                            areaCode = areaCode + $county.val() + ",";
                        } else if ($city.val() != "") {
                            areaCode = areaCode + $city.val() + ",";
                        } else if ($province.val() != "") {
                            areareaCodeaCode = areaCode + $province.val() + ",";
                        }
                    });

                    // 加载区域对应的热区
                    findHotAreaListByAreaCode(areaCode);

                }
            });

            // 增加区域
            $("form").on("click", ".addArea", function () {
                if ($(this).is("[disabled='disabled']")) return;
                let idxCount = $(".addArea").length

                if(idxCount< 20){
                    if ($(this).text() == '新增') {
                        var $copyArea = $(this).closest("div").clone();
                        $copyArea.initZone("<%=cpath%>", true, "", true, true, false, true);
                        $copyArea.find(".addArea").text("删除");
                        $copyArea.css("color", "red");
                        $(this).closest("td").append($copyArea);

                    } else {
                        $(this).closest("div").remove();
                    }
                }else{
                    if($(this).text() == '删除'){
                        $(this).closest("div").remove();
                    }
                }
                if( $(".addArea").length>1){
                    $("#hotAreaKey").find('option:selected').eq(0).removeAttr("selected")
                    $("#hotAreaKey").attr("disabled", "disabled")
                }else{
                    $("#hotAreaKey").removeAttr("disabled")
                }

            });

            //关联活动新增
            $("form").on("click", "#addVcodeActitivy", function() {
                if ($(this).is("[disabled='disabled']")) return;
                let idxCount = $(".addVcodeActitivy").length
                if(idxCount< 10){
                    if ($(this).text() == '新增') {
                        var $copyVcodeActitivy = $(this).closest("div").clone(true, true);
                        $copyVcodeActitivy.find("#addVcodeActitivy").text("删除");
                        $copyVcodeActitivy.css("color", "red");
                        $(this).closest("td").append($copyVcodeActitivy);

                    } else {
                        $(this).closest("div").remove();
                    }
                }else{
                    if ($(this).text() == '删除') {
                        $(this).closest("div").remove();
                    }
                }

            });
            $("form").on("click", "#addTime", function() {
                let idxCount = $(".addTime").length
                if(idxCount<10){
                    var idx = $("td.time div").index($(this).parent("div.time"));
                    if (idx == 0) {
                        var count = $("td.time div").length;
                        if(count < 10){
                            var $timeCopy = $("div.time").eq(0).clone(true);
                            $("td.time").append($timeCopy);
                            $timeCopy.find("#addTime").text("删除");
                            $timeCopy.find("#addTime").css("color", "red");
                            var $beginTime = $("td.time div.time:last").find("input[name='beginTime']");
                            var $endTime = $("td.time div.time:last").find("input[name='endTime']");
                            $beginTime.attr("id", "beginTime" + count).val("00:00:00");
                            $endTime.attr("id", "endTime" + count).val("23:59:59");
                            $beginTime.attr("onfocus", $beginTime.attr("onfocus").replace("endTime0", "endTime" + count));
                            $endTime.attr("onfocus", $endTime.attr("onfocus").replace("beginTime0", "beginTime" + count));
                        }
                    } else {
                        $(this).parent("div.time").remove();
                    }
                }else{
                    if ($(this).text() == '删除') {
                        $(this).closest("div").remove();
                    }
                }

            });


            // 新增奖项
            $("form").on("click", "#addPrizeItem", function(){
                if ($(this).text() == '新增') {
                    var $cloneItem = $(this).closest("tr").clone(true, true);
                    $cloneItem.find("#moneyCog").show();
                    $cloneItem.find("input[name='minMoney']").data("oldval", "0.00").val("0.30");
                    $cloneItem.find("input[name='maxMoney']").data("oldval", "0.00").val("0.30");

                    $cloneItem.find("input[name='fixationMoney']").val("0.30");
                    $cloneItem.find("input[name='prizePercent']").val("0.0000");
                    $cloneItem.find("input[name='unitMoney']").val("0.30");
                    $cloneItem.find("#addPrizeItem").text("删除");
                    $cloneItem.find("#addPrizeItem").css("color","red");
                    $(this).closest("tbody").append($cloneItem);
                } else {
                    $(this).closest("tr").remove();
                }
                $("#randomType").trigger("change");
                $("div.random-prize input").trigger("change");

            });

            $("form").on("change", "#moneyCog", function(){
                var $minMoneyInput = $(this).closest("td").find("input[name='minMoney']");
                var $maxMoneyInput = $(this).closest("td").find("input[name='maxMoney']");
                var minMoneyVal = Number($minMoneyInput.val());
                var maxMoneyVal = Number($maxMoneyInput.val());
                if (minMoneyVal > maxMoneyVal) {
                    maxMoneyVal = minMoneyVal;
                }
                $minMoneyInput.val(minMoneyVal.toFixed(2));
                $maxMoneyInput.val(maxMoneyVal.toFixed(2));
            });
            $("form").on("blur", "input[name='minMoney']", function(){
                if($(this).val()<0.3){
                    $(this).val("0.30")
                    $("#moneyCog").trigger("change");
                    $("div.random-prize input").trigger("change");
                }

            });
            $("form").on("blur", "input[name='fixationMoney']", function(){
                if($(this).val()<0.3){
                    $(this).val("0.30")
                    $("#moneyCog").trigger("change");
                    $("div.random-prize input").trigger("change");
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
                console.log("suffix=",suffix)
                if (suffix == 'Money') {
                    $minInput.val(minVal.toFixed(2));
                    $maxInput.val(maxVal.toFixed(2));
                } else {
                    $minInput.val(minVal);
                    $maxInput.val(maxVal);
                }
                console.log("#unit" + suffix);
                $(this).closest("tr").find("input[name='unitMoney']").val(((minVal + maxVal) / 2).toFixed(2));

            });
            // 计算均价-固定
            $("form").on("change", "div.fixation-prize input", function(){
                var suffix = $(this).attr("name").substr(8);
                console.log("suffix=",suffix)
                if (suffix == 'Money') {
                    $(this).val(Number($(this).val()).toFixed(2));
                }else {
                    $(this).val(Number($(this).val()));
                }
                $(this).closest("tr").find("input[name='unitMoney']").val((Number($(this).val())).toFixed(2));

            });
            // 奖项模式切换
            $("form").on("change", "#randomType", function(){
                // 随机模式
                if ($(this).val() == "0") {
                    $(this).closest("tr").find("div.random-prize").css("display", "inline-flex");
                    $(this).closest("tr").find("div.fixation-prize").css("display", "none");
                    $(this).closest("tr").find("div.random-prize input").trigger("change");
                    // 固定模式
                } else {
                    $(this).closest("tr").find("div.random-prize").css("display", "none");
                    $(this).closest("tr").find("div.fixation-prize").css("display", "inline-flex");
                    $(this).closest("tr").find("div.fixation-prize input").trigger("change");
                }
                // // 模式切换时重新计算均价
                // calculateUnit();
            });
            $("#randomType").trigger("change");


        }
        // 根据地区获取热区
        function findHotAreaListByAreaCode(areaCode){
            if(areaCode == "" || areaCode == null) return;
            var oldVal = $("#hotAreaKey").val();
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/vcodeActivityHotArea/findHotAreaListByAreaCode.do",
                async: false,
                data: {"areaCode":areaCode},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                success:  function(result){
                    var content = "<option value=''>请选择</option>";
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0) {
                                if (oldVal == item.hotAreaKey) {
                                    content += "<option value='"+item.hotAreaKey+"' selected>"+item.hotAreaName+"</option>";
                                } else {
                                    content += "<option value='"+item.hotAreaKey+"'>"+item.hotAreaName+"</option>";
                                }
                            }
                        });
                        $("#hotAreaKey").html(content);
                    } else {
                        $("#hotAreaKey").html(content);
                    }
                },
                error: function(data){
                    $.fn.alert("热区回显错误!");
                }
            });
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

        .top-only {
            border-top: 1px solid #e1e1e1;
        }
        .tab-radio {
            margin: 10px 0 0 !important;
        }
        .validate_tips {
            padding: 8px !important;
        }

    </style>
</head>

<body>
<div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">红包雨活动</a></li>
            <li class="current"><a title="">新增红包活动</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/redEnvelopeRain/redEnvelopeSave.do">
                <input type="hidden" name="roleDescribe" id="roleDescribe" value="">
                <input type="hidden" name="bannerPic" id="bannerPic" value="">
                <input type="hidden" name="ruleContentUrl" id="ruleContentUrl" value="">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title"> 活动开关：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="statusFlag" type="hidden" value="0" />
                                        <input id="statusFlag" type="checkbox"  data-size="small" data-on-text="开" data-off-text="关" data-on-color="info" data-off-color="danger"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName" tag="validate"
                                               class="form-control input-width-larger  required" autocomplete="off" maxlength="25" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="areaCode"/>
                                <input type="hidden" name="areaName"/>

                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main area" colspan="3">
                                    <div class="content area" style="display: flex; margin: 5px 0px;">
                                        <select  name="provinceAry"
                                                 class="zProvince form-control input-width-normal required"></select>
                                        <select  name="cityAry"
                                                 class="zCity form-control input-width-normal required"></select>
                                        <select  name="countyAry"
                                                 class="zDistrict form-control input-width-normal required"></select>
                                        <label class=" title mart5 btn-txt-add-red addArea"
                                               style="font-weight: normal; margin-left: 5px;">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>


                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动有效期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="startDate" id="startDate"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}',minDate:'%y-%M-%d'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="filterTimeAry" />
                                <td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                                <td class="ab_main time">
                                    <div class="content time" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginTime" id="beginTime0" class="form-control input-width-medium Wdate"
                                               onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'endTime0\')}'})" value="00:00:00" autocomplete="off"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTime" id="endTime0" class="form-control input-width-medium Wdate addTime"
                                               onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime0\')}'})" value="23:59:59" autocomplete="off"/>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addTime">新增</label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title mart5">限定热区：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main">
                                    <div class="content hot">
                                        <select id="hotAreaKey" name="hotAreaKey" class="hotArea form-control" style="width: 352px;">
                                            <option value="">请选择</option>
                                            <c:if test="${not empty(hotAreaList) }">
                                                <c:forEach items="${hotAreaList}" var="hotArea">
                                                    <option value="${hotArea.hotAreaKey }" >${hotArea.hotAreaName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <span style="color: green; margin-left: 5px;">注意：指定该热区后，只有热区内才能参与红包雨活动</span>
                                    </div>
                                </td>
                            </tr>
                            <tr id="shareVcodeActivityTr">
                                <td class="ab_left"><label class="title">关联活动：<span class="required" id="shareActivityRequired">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required addVcodeActitivy" id="vcodeActivityKey" name="vcodeActivityKey" tag="validate">
                                            <option value="">请选择活动</option>
                                            <c:if test="${!empty shareActivityKeyList}">
                                                <c:forEach items="${shareActivityKeyList}" var="activity">
                                                    <option value="${activity.vcodeActivityKey}" >${activity.vcodeActivityName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <span class="blocker en-larger"></span>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addVcodeActitivy">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">活动规则上传：</label></td>
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
                    <div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>规则配置<span style="color: red">(金额最小0.30元)</span></h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                            <tr>
                                <td class="ab_main" colspan="5">
                                    <table id="commonScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:9%; text-align: center;" class="cogItemMoney">中出类型</th>
                                            <th style="width:25%; text-align: center;" class="cogItemMoney">范围（元）</th>
                                            <th style="width:13%; text-align: center;" class="cogItemVpoints">均价（元）</th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">中出占比</th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td>
                                                    <input type="hidden" name="scanType" value="1">
                                                    <select id="randomType" name="randomType" class="form-control input-width-small" style="display: initial; width: 60px !important;">
                                                        <option value="0">随机</option>
                                                        <option value="1">固定</option>
                                                    </select>
                                                </td>
                                                <td>
                                                    <div class="random-prize content" style="display: none;">
                                                        <input type="text" name="minMoney" class="form-control input-width-small number  maxValue  "  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.30"  minVal="0.30" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                                        <label style="margin-top: 4px;">&nbsp;-&nbsp;</label>
                                                        <input type="text" name="maxMoney" class="form-control input-width-small number maxValue  "  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.30" minVal="0.30" maxVal="9999.99" maxlength="7" style="display: initial; width: 60px !important;">
                                                    </div>
                                                    <div class="fixation-prize" style="display: inline-flex;">
                                                        <input type="text" name="fixationMoney" class="form-control input-width-small number maxValue "  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.30" minVal="0.30" maxVal="9999.99" maxlength="7" style="display: initial; width: 132px !important;">
                                                    </div>
                                                </td>
                                                <td>
                                                    <input type="text" name="unitMoney" id="unitMoney" readonly="readonly"  class="form-control input-width-small number integer maxValue unitMoney"  autocomplete="off"
                                                           tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                                </td>
                                                <td style="position: relative;">
                                                    <div style="display: inline-flex;">
                                                        <input type="text" id="prizePercent" name="prizePercent" class="form-control input-width-small number maxValue"
                                                               autocomplete="off" tag="validate" data-oldval="0.0000" value="0.0000" maxVal="100" maxlength="7" style="display: initial; width: 60px !important;">
                                                        <label style="line-height: 30px;">%</label>
                                                    </div>
                                                </td>
                                                <td>
                                                    <label id="addPrizeItem" class="btn-txt-add-red">新增</label>
                                                </td>
                                            </tr>

                                        </tbody>
                                        <tbody >
                                        <table  class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%;    margin: 0 auto; text-align: center; overflow: auto !important">
                                            <tbody style=" background-color: rgb(206, 206, 206);">
                                            <tr>
                                                <td  style="width:9%; text-align: center;">合计</td>
                                                <td style="width:25%; text-align: center;"  class="">--</td>
                                                <td style="width:13%; text-align: center;"  class="">--</td>
                                                <td style="width:9%; text-align: center;" ><span id="totalPrizePercent" data-currval="0" style="font-weight: bold;">0%</span></td>
                                                <td style="width:9%; text-align: center;" >--</td>
                                            </tr>

                                            </tbody>
                                        </table>

                                        </tbody>
                                    </table>
                                </td>
                            </tr>

                        </table>
                    </div>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-saoma"></i>限制规则</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title"> 限制类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="limitType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">活动时间</span>
                                        <input type="radio" class="tab-radio" name="limitType" value="1" style="float:left;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">每天</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"> 限制消费金额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictMoney"  type="text" value="0.00"
                                               class="form-control input-width-larger   money  minValue maxValue required" autocomplete="off" minval="0" min="0" maxlength="9" tag="validate"/>
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">  限制消费瓶数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictBottle"  type="text" value="0"
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="9" tag="validate"/>
                                        <span class="blocker en-larger">瓶</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">  限制单用户获取次数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictUserCount"  type="text" value="0"
                                               class="form-control input-width-larger integer required" autocomplete="off" minval="0" maxlength="9" tag="validate"/>
                                        <span class="blocker en-larger">次</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-red btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/redEnvelopeRain/showRedEnvelopeQueryList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
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
<div id="imageDiv" style="display: none">
    <jsp:include page="/inc/upload/uploadImage.jsp"></jsp:include>
</div>
</body>
</html>
