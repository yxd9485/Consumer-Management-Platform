<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>跑马灯管理</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>

    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet"
          type="text/css">
    <script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>

    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];

        $(function () {
            // 初始化校验控件
            $.runtimeValidate($("form"));
            //容积千分符
            $("input[name='volume']").on('keyup', function () {
                $("input[name='volume']").val(thousandth($("input[name='volume']").val()));
            });

            // 初始化功能
            initPage();
        });

        function initPage() {
            // 增加金额
            $("form").on("click", "#AddMoneyMax", function () {
                if ($(this).is("[disabled='disabled']")) return;
                let idxx = $(".AddMoneyMax").length
                if(  idxx<=4){
                    if ($(this).text() == '新增') {
                        var $copySku = $(this).parent().closest("div").clone(true, true);
                        $copySku.find("#AddMoneyMax").text("删除");
                        $copySku.find("#moneyMax").val("")
                        $copySku.find("#moneyMin").val("")
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

            // 增加范围积分
            $("form").on("click", "#AddRangeVpointsMax", function () {
                if ($(this).is("[disabled='disabled']")) return;
                let idxx = $(".AddRangeVpointsMax").length
                if(  idxx<=4){
                    if ($(this).text() == '新增') {
                        var $copySku = $(this).parent().closest("div").clone(true, true);
                        $copySku.find("#AddRangeVpointsMax").text("删除");
                        $copySku.find("#rangeVpointsMax").val("")
                        $copySku.find("#rangeVpointsMin").val("")
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

            //增加优惠券
            $("form").on("click", "#AddShopCoupon", function () {
                if ($(this).is("[disabled='disabled']")) return;
                let idxx2 = $(".AddShopCoupon").length
                if(  idxx2<=4) {
                    if ($(this).text() == '新增') {
                        var $copySku = $(this).parent().closest("div").clone(true, true);
                        $copySku.find("#AddShopCoupon").text("删除");
                        $copySku.find('option:selected').eq(0).removeAttr("selected")
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

            if (${prizeCog.turntablePic != null}){
                var imgData = '${prizeCog.turntablePic}';
                if (imgData) {
                    showImg(imgData);
                }
            } else if (${prizeCog.turntablePrizeType eq '3'}){
                var imgData = 'prizeImg/xxcy.png';
                if (imgData) {
                    showImg(imgData);
                }
            } else if (${prizeCog.turntablePrizeType eq '0'}){
                var imgData = 'prizeImg/cash.png';
                if (imgData) {
                    showImg(imgData);
                }
            } else if (${prizeCog.turntablePrizeType eq '1'}){
                var imgData = 'prizeImg/vpoint.png';
                if (imgData) {
                    showImg(imgData);
                }
            }else if (${prizeCog.turntablePrizeType eq '7'}){
                var imgData = 'prizeImg/vpoint.png';
                if (imgData) {
                    showImg(imgData);
                }
            }



            // 初始化模板状态显示样式
            $("#status").bootstrapSwitch({
                onSwitchChange: function (event, state) {
                    if (state == true) {
                        $("input:hidden[name='status']").val("0");
                    } else {
                        $("input:hidden[name='status']").val("1");
                    }
                }
            });


            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });

            // 保存
            $(".btnSave").click(function () {
                var validateResult = $.submitValidate($("form"));
                if (!validateResult) {
                    alert("输入数据存在异常");
                    $("form").attr("onsubmit", "return true;");
                    return false;
                }

                if(imgSrc.length==0){
                    alert("请上传活动奖品图片");
                    return false;
                }
                if (imgSrc.length > 1) {
                    $.fn.alert("奖品图片最多上传1张");
                    return false;
                } else {
                    $("[name='turntablePic']").val(imgSrc[0]);
                }

                if (imgSrc[0].indexOf("png") == -1){
                    alert("请上传png格式的图片")
                    return false;
                }

                var url = $(this).attr("url");
                $("form").attr("action", url);
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
                return false;
            });

                if(${prizeCog.turntablePrizeType eq "2"}){
                    $("#money").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#prizeType").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                }else if (${prizeCog.turntablePrizeType eq "1"}){
                    $("#money").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                } else if (${prizeCog.turntablePrizeType eq "0"}){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#money").removeAttr("disabled");
                    $(".moneyMin").removeAttr("disabled", "disabled");
                    $(".moneyMax").removeAttr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                } else if (${prizeCog.turntablePrizeType eq "4"}){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").removeAttr("disabled");
                    $("#money").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                }else if (${prizeCog.turntablePrizeType eq "5"}){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").removeAttr("disabled");
                    $("#onceMore").attr("disabled", "disabled");
                }  else if (${prizeCog.turntablePrizeType eq "6"}){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#launchNumber").attr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled","disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").removeAttr("disabled", "disabled");
                } else if (${prizeCog.turntablePrizeType eq "7"}){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $("#rangeVpoints").removeAttr("disabled");
                    $(".rangeVpointsMin").removeAttr("disabled", "disabled");
                    $(".rangeVpointsMax").removeAttr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                }else {
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled","disabled");
                    $("#launchNumber").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                }


            $('input[name=turntablePrizeType]').on("click",function(){
                $(".validate_tips").removeClass("valid_fail_text");
                $(".validate_tips").html("");
                $(".validate_tips").hide();
                // 清除图片
                $('#xxcy').remove();
                $('#cash').remove();
                $('#vpoint').remove();
                if (imgSrc.length > 0){
                    var imgId = getShortId(imgSrc[0]);
                    $('#'+imgId).remove();
                }
                imgSrc = [];
                let turntablePrizeType = $(this).val()
                if(turntablePrizeType == '2'){
                    $("#money").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#prizeType").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#prizeType option:first").prop('selected','selected');
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                    $("#launchNumber").trigger("change");
                }else if (turntablePrizeType == '1'){
                    $("#money").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $("#prizeType").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#vpoints").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                    $("#vpoints").trigger("change");
                    $("#launchNumber").trigger("change");
                    var imgData = 'prizeImg/vpoint.png';
                    if (imgData) {
                        showImg(imgData);
                    }
                } else if (turntablePrizeType == '0'){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").removeAttr("disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $(".moneyMin").removeAttr("disabled", "disabled");
                    $(".moneyMax").removeAttr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");

                    $(".moneyMin").trigger("change");
                    $(".moneyMax").trigger("change");
                    $("#launchNumber").trigger("change");
                    var imgData = 'prizeImg/cash.png';
                    if (imgData) {
                        showImg(imgData);
                    }
                } else if (turntablePrizeType == '4'){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").removeAttr("disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                    $("#launchNumber").trigger("change");
                } else if (turntablePrizeType == '5'){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").removeAttr("disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                    $("#launchNumber").trigger("change");
                } else if (turntablePrizeType== '6'){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $("#launchNumber").attr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled","disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").removeAttr("disabled", "disabled");
                    $(".onceMore").trigger("change");
                } else if (turntablePrizeType == '7'){
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#launchNumber").removeAttr("disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");
                    $("#rangeVpoints").removeAttr("disabled");
                    $(".rangeVpointsMin").removeAttr("disabled", "disabled");
                    $(".rangeVpointsMax").removeAttr("disabled", "disabled");

                    $(".rangeVpointsMin").trigger("change");
                    $(".rangeVpointsMax").trigger("change");
                    $("#launchNumber").trigger("change");
                    var imgData = 'prizeImg/vpoint.png';
                    if (imgData) {
                        showImg(imgData);
                    }
                }  else {
                    $("#prizeType").attr("disabled", "disabled");
                    $("#vpoints").attr("disabled", "disabled");
                    $("#coupon").attr("disabled", "disabled");
                    $("#money").attr("disabled", "disabled");
                    $("#rangeVpoints").attr("disabled", "disabled");
                    $(".shopCoupon").attr("disabled", "disabled");
                    $("#launchNumber").attr("disabled", "disabled");
                    $(".moneyMin").attr("disabled", "disabled");
                    $(".moneyMax").attr("disabled", "disabled");
                    $(".rangeVpointsMin").attr("disabled", "disabled");
                    $(".rangeVpointsMax").attr("disabled", "disabled");
                    $("#onceMore").attr("disabled", "disabled");

                    var imgData = 'prizeImg/xxcy.png';
                    if (imgData) {
                        showImg(imgData);
                    }
                }

            });

            $("[name='prizeType']").on("change", function () {
                // 清除图片
                $('#xxcy').remove();
                $('#cash').remove();
                $('#vpoint').remove();
                if (imgSrc.length > 0){
                    var imgId = getShortId(imgSrc[0]);
                    $('#'+imgId).remove();
                }
                imgSrc = [];
                var imgData = $(this).find("option:selected").attr("img");
                if (imgData) {
                    showImg(imgData);
                }
            });
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
            <li class="current"><a title="">活动管理</a></li>
            <li class="current"><a title="">幸运转盘</a></li>
            <li class="current"><a title="">新增奖品</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/turntable/doTurntablePrizeEdit.do">
                <input type="hidden" name="infoKey" value="${prizeCog.infoKey}"/>
                <input type="hidden" name="turntableActivityKey" value="${prizeCog.turntableActivityKey}"/>
                <input type="hidden" name="turntablePosition" value="${prizeCog.turntablePosition}"/>
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基本信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <input type="hidden" name="areaCode"/>
                        <input type="hidden" name="turntablePic" id="picUrl" value="">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">奖品名称：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="turntablePrizeName" tag="validate" value="${prizeCog.turntablePrizeName}"
                                               class="form-control input-width-larger required varlength" placeholder="最多可填6字"
                                               autocomplete="off" data-length="10" maxlength="7"/>
                                        <span class="blocker en-larger" style="margin-top: 0px; color: red">/ 为换行符 同一奖品名称仅能使用一次!</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">奖项类型：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="turntablePrizeType" value="2"
                                               style="float: left; height:20px; cursor: pointer;" <c:if test="${prizeCog.turntablePrizeType eq '2' }">checked="checked"</c:if>">&nbsp;实物奖
                                    </div>
                                    <select id="prizeType" name="prizeType" class="form-control input-width-small" style="float: left; cursor: pointer;" disabled>
                                        <option value="">请选择</option>
                                        <c:if test="${!empty bigPrizes}">
                                        <c:forEach items="${bigPrizes}" var="prize">
                                        <option value="${prize.prizeNo}" img="${prize.prizeWinPic}" pt="1"
                                        <c:if test="${prizeCog.prizeType eq prize.prizeNo}"> selected="selected" </c:if>>
                                                ${prize.prizeName}
                                            </c:forEach>
                                            </c:if>
                                    </select>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" <c:if test="${prizeCog.turntablePrizeType eq '4' }">checked="checked"</c:if> value="4"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>三方优惠券</span>
                                            </div>
                                            <select id="coupon" name="prizeType" class="form-control input-width-small" style="float: left; cursor: pointer;" disabled>
                                                <option value="">请选择</option>
                                                <c:if test="${!empty couponList}">
                                                <c:forEach items="${couponList}" var="coupon">
                                                <option value="${coupon.ticketNo}" img="${coupon.ticketPicUrl}" pt="2"
                                                <c:if test="${prizeCog.prizeType eq coupon.ticketNo}"> selected="selected" </c:if>>
                                                        ${coupon.ticketName}
                                                    </c:forEach>
                                                    </c:if>
                                            </select>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" <c:if test="${prizeCog.turntablePrizeType eq '5' }">checked="checked"</c:if> value="5"
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>商城优惠券</span>
                                            </div>
<%--                                            <select id="shopCoupon" name="prizeType" class="form-control input-width-small"--%>
<%--                                                    disabled style="float: left; cursor: pointer;">--%>
<%--                                                <option value="">请选择</option>--%>
<%--                                            <c:if test="${!empty shopCouponList}">--%>
<%--                                                <c:forEach items="${shopCouponList}" var="item">--%>
<%--                                                    <option value="${item.couponNo}" img="${item.couponActivityImgUrl}" pt="3"--%>
<%--                                                            <c:if test="${prizeCog.prizeType eq item.couponNo}"> selected="selected" </c:if>>--%>
<%--                                                            ${item.couponName}</option>--%>
<%--                                                </c:forEach>--%>
<%--                                            </c:if>--%>
<%--                                            </select>--%>
<%--                                            <label class="validate_tips"></label>--%>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                        <c:choose>
                                            <c:when test="${!empty prizeCog.prizeType}">
                                                <c:forEach items="${fn:split(prizeCog.prizeType,',' )}" var="prizeType" varStatus="idx">
                                                    <div style="display: flex;">
                                                        <div class="content" style="display: flex; margin: 5px 0px;">
                                                            <select id="shopCoupon" name="prizeType" class="form-control shopCoupon input-width-xlarge"
                                                                    disabled style="float: left; cursor: pointer;">
                                                                <option value="">请选择</option>
                                                                <c:if test="${!empty shopCouponList}">
                                                                    <c:forEach items="${shopCouponList}" var="item">
                                                                        <option value="${item.couponNo}" img="${item.couponActivityImgUrl}" pt="3"
                                                                                <c:if test="${prizeType eq item.couponNo}"> selected="selected" </c:if>>
                                                                                ${item.couponName}</option>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </select>
                                                            <label class="title mart5 btn-txt-add-red AddShopCoupon" style="font-weight: normal; margin-left: 5px;width:100px" id="AddShopCoupon"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                                            <label class="validate_tips"></label>
                                                        </div>
                                                    </div>
                                                </c:forEach>
                                            </c:when>
                                            <c:otherwise>
                                                <div style="display: flex;">
                                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                                        <select id="shopCoupon" name="prizeType" class="form-control shopCoupon input-width-xlarge"
                                                                disabled style="float: left; cursor: pointer;">
                                                            <option value="">请选择</option>
                                                            <c:if test="${!empty shopCouponList}">
                                                                <c:forEach items="${shopCouponList}" var="item">
                                                                    <option value="${item.couponNo}" img="${item.couponActivityImgUrl}" pt="3">
                                                                            ${item.couponName}</option>
                                                                </c:forEach>
                                                            </c:if>
                                                        </select>
                                                        <label class="title mart5 btn-txt-add-red AddShopCoupon" style="font-weight: normal; margin-left: 5px;width:100px" id="AddShopCoupon">新增</label>
                                                        <label class="validate_tips"></label>
                                                    </div>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                </td>
                            </tr>



                            <c:choose>
                                <c:when test="${vpointsType eq '0'}">
                                    <tr>
                                        <input type="hidden" name="groupName"/>
                                        <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                        <td class="ab_main">
                                            <div style="line-height: 35px;">
                                                <div style="display: flex;" class="content">
                                                    <div style="float: left; width: 80px; line-height: 25px;">
                                                        <input type="radio" name="turntablePrizeType" value="1" <c:if test="${prizeCog.turntablePrizeType eq '1' }">checked="checked"</c:if>
                                                               style="float: left; height:20px; cursor: pointer;">
                                                        <span>积分</span>
                                                    </div>
                                                    <input name="vpoints" id="vpoints"
                                                           class="form-control required thousand input-width-small rule" tag="validate" disabled  <c:if test="${prizeCog.vpoints != null &&  prizeCog.vpoints != 0}">value="${prizeCog.vpoints}</c:if>"
                                                           autocomplete="off" maxlength="9"/>
                                                    <span class="blocker en-larger" style="margin-top: 0px;">积分</span>
                                                    <label class="validate_tips"></label>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:when>

                                <%--范围积分--%>
                                <c:otherwise>
                                    <tr>
                                        <input type="hidden" name="groupName"/>
                                        <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                        <td class="ab_main">
                                            <div style="line-height: 35px;" class="content">
                                                <div style="display: flex;">
                                                    <div style="float: left; width: 80px; line-height: 25px;">
                                                        <input type="radio" name="turntablePrizeType" value="7" <c:if test="${prizeCog.turntablePrizeType eq '7' }">checked="checked"</c:if>
                                                               style="float: left; height:20px; cursor: pointer;">
                                                        <span>积分</span>
                                                    </div>

                                                    <label class="validate_tips"></label>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <input type="hidden" name="groupName"/>
                                        <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                        <td class="ab_main">
                                            <c:choose>
                                                <c:when test="${!empty prizeCog.rangeVpoints}">
                                                    <c:forEach items="${fn:split(prizeCog.rangeVpoints,';' )}" var="rangeVpoints" varStatus="idx">
                                                        <div style="display: flex;">
                                                            <div class="content " style="display: flex; margin: 5px 0px;">
                                                                <input name="rangeVpointsMin" id="rangeVpointsMin"
                                                                       class="form-control thousand rangeVpoints rangeVpointsMin required preValue minValue maxValue input-width-small rule" tag="validate" minval="0" disabled value="${fn:split(rangeVpoints,':' )[0]}"
                                                                       autocomplete="off" maxlength="9"/>
                                                                <span>-</span>
                                                                <input name="rangeVpointsMax" id="rangeVpointsMax"
                                                                       class="form-control thousand rangeVpoints rangeVpointsMax required sufValue minValue maxValue input-width-small rule" tag="validate" minval="0" disabled value="${fn:split(rangeVpoints,':' )[1]}"
                                                                       autocomplete="off" maxlength="9"/>
                                                                <span class="blocker en-larger" style="margin-top: 0px;">积分</span>
                                                                <label class="title mart5 btn-txt-add-red AddRangeVpointsMax" style="font-weight: normal; margin-left: 5px;width:100px" id="AddRangeVpointsMax"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                                                <label class="validate_tips" style="width: 400px"></label>
                                                            </div>
                                                        </div>
                                                    </c:forEach>

                                                </c:when>
                                                <c:otherwise>
                                                    <div style="display: flex;">
                                                        <div class="content " style="display: flex; margin: 5px 0px;">
                                                            <input name="rangeVpointsMin" id="rangeVpointsMin"
                                                                   class="form-control thousand rangeVpoints rangeVpointsMin required preValue minValue maxValue input-width-small rule" minval="0" tag="validate" disabled
                                                                   autocomplete="off" maxlength="9"/>
                                                            <span>-</span>
                                                            <input name="rangeVpointsMax" id="rangeVpointsMax"
                                                                   class="form-control thousand rangeVpoints rangeVpointsMax required sufValue minValue maxValue input-width-small rule" minval="0" tag="validate" disabled
                                                                   autocomplete="off" maxlength="9"/>
                                                            <span class="blocker en-larger" style="margin-top: 0px;">积分</span>
                                                            <label class="title mart5 btn-txt-add-red AddRangeVpointsMax" style="font-weight: normal; margin-left: 5px;width:100px" id="AddRangeVpointsMax">新增</label>
                                                            <label class="validate_tips" style="width: 400px"></label>
                                                        </div>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>

                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;" class="content">
                                        <div style="display: flex;">
                                            <div style="float: left; width: 80px; line-height: 25px;">
                                                <input type="radio" name="turntablePrizeType" value="0" <c:if test="${prizeCog.turntablePrizeType eq '0' }">checked="checked"</c:if>
                                                       style="float: left; height:20px; cursor: pointer;">
                                                <span>红包</span>
                                            </div>

                                            <label class="validate_tips"></label>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="groupName"/>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                            <c:choose>
                                                <c:when test="${!empty prizeCog.money}">
                                                    <c:forEach items="${fn:split(prizeCog.money,';' )}" var="money" varStatus="idx">
                                                        <div style="display: flex;">
                                                            <div class="content " style="display: flex; margin: 5px 0px;">
                                                                <input name="moneyMin" id="moneyMin"
                                                                       class="form-control money required preValue  minValue maxValue moneyMin input-width-small rule" tag="validate" minval="0.00" disabled value="${fn:split(money,':' )[0]}"
                                                                       autocomplete="off" maxlength="9"/>
                                                                <span>-</span>
                                                                <input name="moneyMax" id="moneyMax"
                                                                       class="form-control money moneyMax sufValue minValue  maxValue   required input-width-small rule" tag="validate" minval="0.00" disabled value="${fn:split(money,':' )[1]}"
                                                                       autocomplete="off" maxlength="9"/>
                                                                <span class="blocker en-larger" style="margin-top: 0px;">元</span>
                                                                <label class="title mart5 btn-txt-add-red AddMoneyMax" style="font-weight: normal; margin-left: 5px;width:100px" id="AddMoneyMax"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                                                <label class="validate_tips" style="width: 400px"></label>
                                                            </div>
                                                        </div>
                                                    </c:forEach>

                                                </c:when>
                                                <c:otherwise>
                                                    <div style="display: flex;">
                                                        <div class="content " style="display: flex; margin: 5px 0px;">
                                                            <input name="moneyMin" id="moneyMin"
                                                                   class="form-control money preValue  minValue maxValue required moneyMin input-width-small rule" minval="0.00" tag="validate" disabled
                                                                   autocomplete="off" maxlength="9"/>
                                                            <span>-</span>
                                                            <input name="moneyMax" id="moneyMax"
                                                                   class="form-control money sufValue minValue maxValue  moneyMax required input-width-small rule" minval="0.00" tag="validate" disabled
                                                                   autocomplete="off" maxlength="9"/>
                                                            <span class="blocker en-larger" style="margin-top: 0px;">元</span>
                                                            <label class="title mart5 btn-txt-add-red AddMoneyMax" style="font-weight: normal; margin-left: 5px;width:100px" id="AddMoneyMax">新增</label>
                                                            <label class="validate_tips" style="width: 400px"></label>
                                                        </div>
                                                    </div>
                                                </c:otherwise>
                                            </c:choose>
                                </td>
                            </tr>
                            <c:if test="${turntableActivityCog.drainageType ne '2' && turntableActivityCog.drainageType ne '3'}">
                                <tr id="onceMoreTr">
                                    <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div style="display: flex;">
                                            <div class="content " style="display: flex; margin: 5px 0px;">
                                                <input type="radio" name="turntablePrizeType" value="6" <c:if test="${prizeCog.turntablePrizeType eq '6' }">checked="checked"</c:if>
                                                       style="float: left; height:20px; cursor: pointer;">&nbsp;<span class="blocker en-larger" style="width: 100px;">再来一次</span>
                                                <input name="onceMore" id="onceMore"
                                                       class="form-control  required  minValue maxValue onceMore input-width-small rule" tag="validate" minval="0" maxval="99" disabled <c:if test="${prizeCog.onceMore != null}">value="${prizeCog.onceMore}</c:if>"
                                                       autocomplete="off" maxlength="2"/>  <span class="blocker en-larger" style="">%</span>
                                                <label class="validate_tips" style="width: 400px"></label>
                                            </div>
                                        </div>

                                    </td>
                                </tr>
                            </c:if>

                            <tr>
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="turntablePrizeType" value="3" <c:if test="${prizeCog.turntablePrizeType eq '3' }">checked="checked"</c:if>
                                               style="float: left; height:20px; cursor: pointer;">&nbsp;谢谢参与
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">奖品发放量：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="launchNumber" name="launchNumber" tag="validate" disabled
                                               class="form-control input-width-larger thousand required varlength" value="${prizeCog.launchNumber}"
                                               autocomplete="off" data-length="11" maxlength="11"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr style="height: 60px;">
                                <td class="ab_left"><label class="title">转盘显示图片：<span class="required">*</span><br/></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;"
                                                 id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
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
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"
                                    data-url="<%=cpath%>/turntable/showTurntableActivityList.do">返
                                回
                            </button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    <div id="imageDiv" style="display: none">
        <jsp:include page="/inc/upload/uploadImage.jsp"></jsp:include>
    </div>
</div>
</body>
</html>
