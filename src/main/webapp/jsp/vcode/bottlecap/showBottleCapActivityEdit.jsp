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
    <title>添加积盖活动</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>


    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.2"></script>
    <%-- 	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/showimg.js"></script> --%>
    <script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        $(document).ready(function () {
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));

            initPage();
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });

        });

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
// 			// 页面校验
// 			var pc=$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").val();
// 			if(pc&&pc.indexOf('个')>-1){
// 				$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").hidden();
// 			}
            // 页面校验
            var v_flag = true;
            $(".validate_tips:not(:hidden)").each(function(){
                if($(this).text() != ""){
                    $.fn.alert($(this).text());
                    v_flag = false;
                }
            });
            if(!v_flag){
                return false;
            }

            var sHTML = $('.summernote').summernote('code');
            $('#roleDescribe').val(sHTML.trim());
            $('#bannerPic').val(imgSrc[0]);

            if(!sHTML){
                $.fn.alert("请输入说明");
                return false;
            }

            if(sHTML.trim() == '<p><br></p>' || sHTML.trim() == ''){
                $.fn.alert("请输入活动规则");
                return false;
            }

            var bottleCapAreaType = '${bottleCapAreaType}';
            var regionCogArr = [];
            $("input[name='regionCog']:checked").each(function(i){
                regionCogArr = $(this).val() + "," + regionCogArr;
            });

            if (bottleCapAreaType == '0' && regionCogArr == ''){
                $.fn.alert("请选择活动区域");
                return false;
            }

            var consumeCapArr = [];
            $("input[name='consumeBoottlecap']").each(function(i){
                consumeCapArr[i] = $(this).val();
            });

            for (var i = 0; i < consumeCapArr.length; i++) {
                for (var j = i + 1; j < consumeCapArr.length; j++) {
                    if (Number(consumeCapArr[i]) > Number(consumeCapArr[j])) {
                        $.fn.alert("前一个阶梯积盖数量不能大于后一个阶梯积盖数量!");
                        return false;
                    }
                }
            }

            if(imgSrc.length>1){
                $.fn.alert("只能上传一张图片");
                return false;
            }

            var terminalGroupArr = [];
            $("select[name='terminalGroup']").each(function(){
                terminalGroupArr = $(this).val() + "," + terminalGroupArr;
            });


            var isBegin = '${activityCogInfo.isBegin}';
                $.ajax({
                    url : "${basePath}/bottleCap/checkActivityRepeatDate.do",
                    data:{
                        "startDate":$("input[name='startDate']").val(),
                        "endDate":$("input[name='endDate']").val(),
                        "activityKey":$("input[name='activityKey']").val()
                    },
                    type : "POST",
                    dataType : "json",
                    async : false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data){
                        if(data == '验证失败'){
                            $.fn.alert("当前区域校验未通过,请联系管理员!");
                            validateResult = false;
                        }else if(data == '验证通过'){
                            validateResult = true;
                        } else {
                            $.fn.alert(data);
                            validateResult = false;
                        }
                    }
                });

            if(!validateResult){
                return false;
            }

            $("[name='receiveEndTime']").attr("disabled",false);
            return true;
        }

        function initPage() {
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
                    var blacklistCogFlag = "0";
                    $("input[mark=blacklist]:not([type=radio])").each(function(){
                        if($(this).val() != ""){
                            blacklistCogFlag = "1";
                        }
                    });
                    $("input[name=blacklistFlag]").val(blacklistCogFlag);

                    var flag = validForm();
                    if(flag) {
                        if(btnEvent == "2"){
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

            var imgData = '${activityCogInfo.bannerPic}';
            if (imgData) {
                showImg(imgData);
            }

            // 初始化区域配置
            var regionArr = '${activityCogInfo.regionCog}'.split(",");
            $.each(regionArr, function(idx, val, ary){
                $("[name='regionCog']").each(function() { //遍历所有复选框
                    if ($(this).val() == val) {
                        $(this).prop("checked", "checked");
                    }
                });
            });
            if (regionArr.length == '${fn:length(regionLst)}'){
                $('#allCheck').prop("checked", "checked");
            }


            // 增加sku
            $("form").on("click", "#addSku", function () {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addSku").text("删除");
                    $copySku.find("option:first").prop('selected', 'selected');
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });

            // 全选
            $("#allCheck").on("change", function () {
                if ($(this).prop("checked")) {
                    $("[name='regionCog']").prop("checked", "checked");
                } else {
                    $("[name='regionCog']").prop("checked", "");
                }
            });


            // 奖励类型切换
            $("form").on("change", "#rewardType", function(){
                if ($(this).val() == '0'){
                    // 现金
                    $(this).closest("tr").find("#vpointsCog").css("display", "none");
                    $(this).closest("tr").find("#prizeCog").css("display", "none");
                    $(this).closest("tr").find("#moneyCog").show();
                } else if($(this).val() == '1'){
                    // 积分
                    $(this).closest("tr").find("#moneyCog").css("display", "none");
                    $(this).closest("tr").find("#prizeCog").css("display", "none");
                    $(this).closest("tr").find("#vpointsCog").show();
                } else {
                    // 实物奖
                    $(this).closest("tr").find("#moneyCog").css("display", "none");
                    $(this).closest("tr").find("#vpointsCog").css("display", "none");
                    $(this).closest("tr").find("#prizeCog").show();
                }
            });

            // 新增奖项
            $("form").on("click", "#addPrizeItem", function(){
                if ($(this).text() == '新增') {
                    var $cloneItem = $(this).closest("tr").clone(true, true);
                    $cloneItem.find("#vpointsCog").css("display", "none");
                    $cloneItem.find("#prizeCog").css("display", "none");
                    $cloneItem.find("#moneyCog").show();
                    $cloneItem.find("input[name='minMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='maxMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='minVpoints']").data("oldval", "0").val("0");
                    $cloneItem.find("input[name='maxVpoints']").data("oldval", "0").val("0");
                    $cloneItem.find("input[name='bottleCapAmount']").val("0");
                    $cloneItem.find("input[name='launchAmount']").val("0");
                    $cloneItem.find("input[name='limitExchange']").val("0");
                    $cloneItem.find("#addPrizeItem").text("删除");
                    $(this).closest("tbody").append($cloneItem);
                } else {
                    $(this).closest("tr").remove();
                }

                $(this).closest("tbody").find("tr").each(function(i, obj){
                    $(this).find("#NO").text(i+1);
                    $(this).find("#ladder").attr("value",i+1);
                });
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

            $("input[name='activityType']").on("change", function () {
                if ($(this).val() == '0'){
                    // 现金
                    $("input[name='limitExchange']").attr("disabled",true);
                } else {
                    // 积分
                    $("input[name='limitExchange']").attr("disabled",false);
                }
            });

            $("input[name='launchAmount']").on("change", function () {
                var receive = $(this).closest("tr").find("input[name='receiveAmount']").val();
                var launch = $(this).val();
                if (Number(launch) < Number(receive)){
                    alert("投放个数不能小于已投放个数")
                    $(this).val(receive);
                }
            });

            $("input[name='consumeBoottlecap']").on("change", function () {
                var consumeCapArr = [];
                $("input[name='consumeBoottlecap']").each(function(i){
                    consumeCapArr[i] = $(this).val();
                });

                for (var i = 0; i < consumeCapArr.length; i++) {
                    for (var j = i + 1; j < consumeCapArr.length; j++) {
                        if (Number(consumeCapArr[i]) > Number(consumeCapArr[j])) {
                            if (consumeCapArr[j] != 0){
                                $.fn.alert("前一个阶梯积盖数量不能大于后一个阶梯积盖数量!");
                            }
                        }
                    }
                }
            });

            $("form").on("change", "#vpointsCog", function(){
                var $minVpointsInput = $(this).closest("td").find("input[name='minVpoints']");
                var $maxVpointsInput = $(this).closest("td").find("input[name='maxVpoints']");
                var minVpointsVal = Number($minVpointsInput.val());
                var maxVpointsVal = Number($maxVpointsInput.val());
                if (minVpointsVal > maxVpointsVal) {
                    maxVpointsVal = minVpointsVal;
                }
                $minVpointsInput.val(minVpointsVal);
                $maxVpointsInput.val(maxVpointsVal);
            });

            //增加门店分组
            $("form").on("click", "#addTerminalGroup", function () {

                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    if($(".terminalGroup").length>=5){return;}
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addTerminalGroup").text("删除");
                    $copySku.find("option:first").prop('selected', 'selected');
                    $(this).closest("td").append($copySku);
                } else {
                    $(this).closest("div").remove();
                }
            });

        }
        function uploadImages(files) {
            var formData = new FormData();
            for (f in files) {
                formData.append("file", files[f]);
            }
            // XMLHttpRequest 对象
            var xhr;
            if (XMLHttpRequest) {
                xhr = new XMLHttpRequest();
            } else {
                xhr = new ActiveXObject('Microsoft.XMLHTTP');
            }
            xhr.open("post", basePath+"/skuInfo/imgUploadUrl.do?vjfSessionId=" + $("#vjfSessionId").val(), true);
            xhr.onreadystatechange = function(){
                if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200){
// 		            console.info("上传完成");
                    var result = jQuery.parseJSON(xhr.responseText);
                    if(result["errMsg"]=="error"){
                        alert("上传失败");
                        return;
                    }
                    var srcList=result["ipUrl"].split(",");
                    for(var i = 0;i<srcList.length;i++){
                        $('.summernote').summernote('insertImage',srcList[i], srcList[i]);
                    }

                }else{
// 		        	console.info("上传失败");
                }
            };
            xhr.send(formData);
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
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">积盖活动</a></li>
            <li class="current"><a title="">修改积盖活动</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/bottleCap/doBottleCapActivityEdit.do">
                <input type="hidden" name="roleDescribe" id="roleDescribe" value="">
                <input type="hidden" name="bannerPic" id="bannerPic" value="">
                <input type="hidden" name="notEditStartDate" id="notEditStartDate" value="${activityCogInfo.startDate}">
                <input type="hidden" name="bottleCapAreaType" id="bottleCapAreaType" value="${bottleCapAreaType}">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName" tag="validate" value="${activityCogInfo.activityName}" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                               class="form-control input-width-larger  required" autocomplete="off" maxlength="25" />
                                        <input name="activityKey" value="${activityCogInfo.activityKey}" style="display: none">
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">活动有效期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="startDate" class="form-control input-width-medium required Wdate preTime" value="${activityCogInfo.startDate}" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" class="form-control input-width-medium required Wdate sufTime" value="${activityCogInfo.endDate}"
                                               tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="sku">
                                <td class="ab_left"><label class="title">sku：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                <c:forEach items="${fn:split(activityCogInfo.activitySku,',')}" var="skuItem" varStatus="idx">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="activitySku"
                                                tag="validate">
                                            <option value="">请选择sku</option>
                                            <c:if test="${!empty skuList}">
                                                <c:forEach items="${skuList}" var="item">
                                                    <option value="${item.skuKey}" <c:if test="${skuItem eq item.skuKey}"> selected="selected" </c:if>>
                                                            ${item.skuName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <c:if test="${idx.index eq 0 }">
                                            <label class="title mart5 btn-txt-add-red"
                                                   style="font-weight: normal; margin-left: 5px;" id="addSku">新增</label>
                                        </c:if>
                                        <c:if test="${idx.index > 0 }">
                                            <label class="title mart5 btn-txt-add-red"
                                                   style="font-weight: normal; margin-left: 5px;" id="addSku">删除</label>
                                        </c:if>
                                        <label class="validate_tips"></label>
                                    </div>
                                </c:forEach>
                                </td>
                            </tr>
                            <%--<tr style="height: 200px;">
                                <td class="ab_left"><label class="title">活动banner图：<span class="required">*</span><br/>建议700*262</label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 200px;width: 800px; float: left;" class="img-box full">
                                        <section class=" img-section">
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除作品图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动规则：<span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="summernote">
                                        ${activityCogInfo.roleDescribe}
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">备注：<span class=""></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="remarks" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="50" value="${activityCogInfo.remarks}" />
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>规则配置</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">活动类型<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                            <input type="radio" class="tab-radio firstRadioMoney" <c:if test="${activityCogInfo.activityType eq '0'}">checked</c:if>
                                                   <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                                   name="activityType" value="0" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">累计</span>
                                        <input type="radio" class="tab-radio firstRadioScore" name="activityType"
                                               <c:if test="${activityCogInfo.activityType eq '1'}">checked</c:if>
                                               <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                               value="1" style="float:left; cursor: pointer;" />
                                        <span class="blocker en-larger">消耗</span>
                                        <label class="validate_tips"></label>
                                        <label style="color: #db2d2a">备注：实物奖每日中出总个数、限制单人中出个数不受大奖模块化限制</label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_main" colspan="6">
                                    <table id="firstScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:5%; text-align: center;">阶梯</th>
                                            <th style="width:13%; text-align: center;" class="cogItemMoney">积盖数量</th>
                                            <th style="width:9%; text-align: center;" class="cogItemMoney">奖励类型</th>
                                            <th style="width:25%; text-align: center;" class="cogItemMoney">范围</th>
                                            <th style="width:13%; text-align: center;" class="cogItemVpoints">投放个数</th>
                                            <th style="width:13%; text-align: center;" class="cogItemVpoints">已投放个数</th>
                                            <th style="width:13%; text-align: center;" class="cogItemVpoints">已投放</th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">限制单人兑换次数</th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">领奖截止时间</th>
                                        <c:if test="${activityCogInfo.isBegin eq '0'}">
                                            <th style="width:13%; text-align: center;" class="cogItemVpoints">操作</th>
                                        </c:if>
                                        </tr>
                                        </thead>
                                        <tbody>
                                    <c:forEach items="${prizeCogInfoList}" var="item">
                                        <tr>
                                            <td>
                                                <label id="NO" style="line-height: 30px;">${item.ladder}</label>
                                                <input id="ladder" name="ladder" value="${item.ladder}" style="display: none">
                                                <input id="prizeInfoKey" name="prizeInfoKey" value="${item.infoKey}" style="display: none">
                                            </td>
                                            <td>
                                                <input type="text" name="consumeBoottlecap" class="form-control input-width-small number integer maxValue"  autocomplete="off" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                                       tag="validate" data-oldval="0" value="${item.consumeBoottlecap}" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                            </td>
                                            <td>
                                                <input type="hidden" name="scanType" value="1">
                                                <select id="rewardType" name="boottlecapPrizeType" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if> class="form-control input-width-small" style="display: initial; width: 60px !important;">
                                                    <option value="0" <c:if test="${item.boottlecapPrizeType eq '0'}">selected</c:if>>现金</option>
                                                    <%--<option value="1" <c:if test="${item.boottlecapPrizeType eq '1'}">selected</c:if>>积分</option>--%>
                                                    <option value="2" <c:if test="${item.boottlecapPrizeType eq '2'}">selected</c:if>>实物奖</option>
                                                </select>
                                            </td>
                                            <td>
                                                <div id="moneyCog" class="random-prize content" <c:if test="${item.boottlecapPrizeType eq '1' || item.boottlecapPrizeType eq '2'}">style="display: none" </c:if>>
                                                    <input type="text" name="minMoney" class="form-control money maxValue"  autocomplete="off" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                                           tag="validate" data-oldval="0.00" value="${item.minMoney}" maxVal="999.99" maxlength="6" style="position: relative;left: 9%;width: 30% !important">
                                                    <label style="position: relative;right: 35%;top: 3px;">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxMoney" class="form-control input-width-small money maxValue"  autocomplete="off" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                                           tag="validate" data-oldval="0.00" value="${item.maxMoney}" maxVal="999.99" maxlength="6" style="position: relative;left: 23%;width: 30% !important">
                                                </div>
                                                <div id="vpointsCog" class="random-prize content" <c:if test="${item.boottlecapPrizeType eq '0' || item.boottlecapPrizeType eq '2'}">style="display: none" </c:if>>
                                                    <input type="text" name="minVpoints" class="form-control input-width-small number maxValue" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                                           autocomplete="off" tag="validate" data-oldval="0" value="${item.minVpoints}" maxVal="999999" maxlength="6" style="position: relative;left: 18px;">
                                                    <label style="position: relative;right: 83px;top: 3px;">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxVpoints" class="form-control input-width-small number maxValue" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>
                                                           autocomplete="off" tag="validate" data-oldval="0" value="${item.maxVpoints}" maxVal="999999" maxlength="6" style="position: relative;left: 79px;right: 0px;">
                                                </div>
                                                <div id="prizeCog" class="content" <c:if test="${item.boottlecapPrizeType eq '0' || item.boottlecapPrizeType eq '1'}">style="display: none" </c:if>>
                                                    <select name="bigPrizeType" class="form-control input-width-normal" style="display: initial; width: 100% !important;" <c:if test="${activityCogInfo.isBegin ne '0'}">disabled</c:if>>
                                                        <option value="">请选择</option>
                                                        <c:forEach items="${bigPrizes}" var="iem">
                                                            <option value="${iem.prizeNo}" <c:if test="${item.bigPrizeType eq iem.prizeNo}">selected="selected"</c:if>>${iem.prizeName}</option>
                                                        </c:forEach>
                                                    </select>
                                                </div>
                                            </td>
                                            <td>
                                                <input type="text" name="launchAmount" class="form-control input-width-small number integer maxValue"  autocomplete="off" <c:if test="${activityCogInfo.isBegin eq '2'}">disabled</c:if>
                                                       tag="validate" data-oldval="0" value="${item.launchAmount}" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                            </td>
                                            <td>
                                                <input type="text" name="receiveAmount" class="form-control input-width-small number integer maxValue"  autocomplete="off" disabled
                                                       tag="validate" data-oldval="0" value="${item.receiveAmount}" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                            </td>
                                            <td>
                                                <input type="text"  class="form-control input-width-small number integer maxValue"  autocomplete="off" disabled
                                                       tag="validate" data-oldval="0"
                                                    <c:choose>
                                                        <c:when test="${item.boottlecapPrizeType eq '0'}"> value="${item.receiveMoneyAmount}" </c:when>
                                                        <c:when test="${item.boottlecapPrizeType eq '1'}">value="${item.receiveVpointsAmount}"</c:when>
                                                        <c:otherwise> value="" </c:otherwise>
                                                    </c:choose>
                                                        maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                            </td>
                                            <td>
                                                <input type="text" name="limitExchange" class="form-control input-width-small number integer maxValue"  autocomplete="off" <c:if test="${activityCogInfo.isBegin ne '0' or activityCogInfo.activityType ne '1'}">disabled</c:if>
                                                       tag="validate" data-oldval="0"
                                                <c:choose>
                                                <c:when test="${item.limitExchange == null || item.limitExchange == ''}"> value="1" </c:when>
                                                <c:otherwise> value="${item.limitExchange}" </c:otherwise>
                                                </c:choose>
                                                       maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;">
                                            </td>
                                            <td>
                                                <input name="receiveEndTime" id="receiveEndTime"
                                                       class="form-control input-width-medium Wdate required preTime" value="${item.receiveEndTime}"
                                                       tag="validate" style="width: 180px !important;" autocomplete="off" <c:if test="${activityCogInfo.isBegin ne '0' or item.boottlecapPrizeType ne '2'}">disabled</c:if>
                                                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                            </td>
                                            <c:if test="${activityCogInfo.isBegin eq '0'}">
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${item.ladder eq '1'}"> <label id="addPrizeItem" class="btn-txt-add-red">新增</label> </c:when>
                                                        <c:otherwise> <label id="addPrizeItem" class="btn-txt-add-red">删除</label> </c:otherwise>
                                                    </c:choose>
                                                </td>
                                            </c:if>
                                        </tr>
                                    </c:forEach>

                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <c:if test="${activityCogInfo.isBegin ne '2'}">
                                <button class="btn btn-red btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;
                            </c:if>
                            <button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/bottleCap/showBottleCapActivityList.do">返 回</button>
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
</body>
</html>