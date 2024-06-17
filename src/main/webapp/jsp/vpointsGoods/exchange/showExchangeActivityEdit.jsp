<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
    String imagePath = PropertiesUtil.getPropertyValue("image_server_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>换购活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
    <script>
        var chk_value = [];
        var goodsIdList = [];
        var exchangeGoodsIdList = [];
        var goodsList = [];
        var changeCheckBoxArray = [];
        var changeRadioId = null;
        var goodsCheckboxAllList = [];
        var goodsRadioAllList = [];
        var goodsObj = ${activityJSON};
        $(function(){
            initGoodsList(goodsObj.goodsIdList);
            // 初始化校验控件
            $.runtimeValidate($("div.tab-content"));
            
            // 初始化功能
            initPage();
            
         	// 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});

        
        });
        
        function initPage() {
            $(".qrcodeUploadDialog, #goodsSelectRadioBtn").on("click", function () {
                $("#dialogType").val("1");
                $("#selectRadioBtb").show()
                $("#selectCheckboxBtb").hide()
                var goodsName = $("#keyWord").val()
                var categoryParent = $("#categoryParent option:selected").val()
                var exchangeType = $("#exchangeType option:selected").val()
                var paramJson = {
                    "goodsName":goodsName,
                    "categoryParent":categoryParent,
                    "exchangeType":exchangeType,
                    "exchangeActivityType":"1",
                    "goodsIdArrayStr":changeRadioId,
                }
                var url = "<%=cpath%>/vpointsGoods/getShopGoods.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(datas) {
                        console.log("datas",datas)
                        var reply = datas.reply;
                        if(datas.result.businessCode==='0'){
                            goodsList = reply;
                            goodsRadioAllList = goodsRadioAllList.length==0 ? reply : goodsRadioAllList;
                            $("#goodsDetailsBody").empty()
                            for (var index in reply) {
                                var goodsUrl = "<%=imagePath%>/"+  reply[index].goodsUrl?.split(",")[0]
                                var exchangeType = getExchangeType(reply[index].exchangeType)

                                //回显选中
                                var inputType = '';
                                if(changeRadioId && changeRadioId==reply[index].goodsId){
                                    inputType = `checked`
                                }
                                var realMoney = reply[index].realPay / 100;
                                 $("#goodsDetailsBody").append('<tr>' +
                                    '<td class="text_center firstTd" style="vertical-align: middle;">' +
                                     '<input type="radio" name="activityGoodsIdRaido"' +
                                     inputType +
                                     ' value="'+reply[index].goodsId+'">' +
                                     '</td>' +
                                    '<td class="text_center" style="vertical-align: middle;"><img src="'+goodsUrl+'"></td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsName+'</br><li style="color: red;list-style: none;">'+realMoney+'元</li></td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+exchangeType+'</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+reply[index].categoryParent+'</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsNum+'</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsRemains+'</td>' +
                                    '</tr>')
                            }
                        }
                        $("#qrcodeUploadDialog input.import-file").val("");
                        $("#qrcodeUploadDialog").modal("show");
                        $('input[name="activityGoodsIdRaido"]').on('change',function(){
                            changeRadioId = $(this).val();
                            console.log("changeRadioId=",changeRadioId)
                        });
                    }
                });
               
            });
            $(".exchangeGoods, #goodsSelectCheckboxBtn").on("click", function () {
                $("#dialogType").val("2");
                $("#selectRadioBtb").hide()
                $("#selectCheckboxBtb").show()

                var goodsName = $("#keyWord").val()
                var categoryParent = $("#categoryParent option:selected").val()
                var exchangeType = $("#exchangeType option:selected").val()
                var paramJson = {
                    "goodsName": goodsName,
                    "categoryParent": categoryParent,
                    "exchangeType": exchangeType,
                }
                console.log("paramJson",paramJson);
                var url = "<%=cpath%>/vpointsGoods/getShopGoods.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend: appendVjfSessionId,
                    success: function (datas) {
                        console.log("datase", datas)
                        var reply = datas.reply;
                        if (datas.result.businessCode === '0') {
                            goodsList = reply;
                            goodsCheckboxAllList = goodsCheckboxAllList.length==0 ? reply : goodsCheckboxAllList;
                            $("#goodsDetailsBody").empty()
                            for (var index in reply) {
                                var goodsUrl = "<%=imagePath%>/" + reply[index].goodsUrl?.split(",")[0]
                                var exchangeType = getExchangeType(reply[index].exchangeType)
                                var realMoney = reply[index].realPay / 100;
                                //回显选中
                                var inputType = '';
                                if(changeCheckBoxArray.length > 0 && changeCheckBoxArray.indexOf(reply[index].goodsId)>-1){
                                    inputType = `checked`
                                }
                                $("#goodsDetailsBody").append('<tr>' +
                                    '<td class="text_center firstTd" style="vertical-align: middle;">' +
                                    '<input type="checkbox" name="goodsDetailsBodyCheckbox" ' +
                                    inputType +
                                    ' value="' + reply[index].goodsId + '">' +
                                    '</td>' +
                                    '<td class="text_center" style="vertical-align: middle;"><img src="' + goodsUrl + '"></td>' +
                                    '<td class="text_center" style="vertical-align: middle;">' + reply[index].goodsName + '</br><li style="color: red;list-style: none;">' + realMoney + '元</li></td>' +
                                    '<td class="text_center" style="vertical-align: middle;">' + exchangeType + '</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">' + reply[index].categoryParent + '</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">' + reply[index].goodsNum + '</td>' +
                                    '<td class="text_center" style="vertical-align: middle;">' + reply[index].goodsRemains + '</td>' +
                                    '</tr>')
                            }
                        }
                        $("#qrcodeUploadDialog input.import-file").val("");
                        $("#qrcodeUploadDialog").modal("show");

                        $('input[name="goodsDetailsBodyCheckbox"]').on('change',function(){
                            if($(this).is(':checked')){
                                changeCheckBoxArray.push($(this).val())
                            }else{
                                changeCheckBoxArray = removeElement(changeCheckBoxArray,$(this).val())
                            }
                            console.log("changeCheckBoxArray",changeCheckBoxArray)
                        });
                    }
                });

            });
       
            $("#addBtn").on("click", function () {
                if ("1" === $("#dialogType").val()) {
                    chk_value = [];
                    $('input[name="activityGoodsIdRaido"]:checked').each(function () {   // 遍历input输入框中name=active 选中状态的值。
                        chk_value.push($(this).parent().parent());
                    });
                    $("#qrcodeUploadDialog #closeBtn").trigger("click");
                    $("#goodsDetailsBodyTable").empty()
                    for (let i = 0; i < chk_value.length; i++) {
                        chk_value[i].prevObject.remove();
                        $("#goodsDetailsBodyTable").append(chk_value[i]);
                    }
                    $("[name='activityGoodsId']").remove()
                    $("#code_form").append('<input type="hidden" name="activityGoodsId" value="' + changeRadioId + '">')
                }
                if ("2" === $("#dialogType").val()) {
                    $("#qrcodeUploadDialog #closeBtn").trigger("click");
                    $("#exchangeGoodsTable").empty()
                    for (let i = 0; i < goodsCheckboxAllList.length; i++) {
                        if (changeCheckBoxArray.indexOf(goodsCheckboxAllList[i].goodsId) < 0) {
                            continue;
                        }
                        var goodsUrl = "<%=imagePath%>/" + goodsCheckboxAllList[i].goodsUrl?.split(",")[0]
                        var exchangeType = getExchangeType(goodsCheckboxAllList[i].exchangeType)
                        var realMoney = goodsCheckboxAllList[i].realPay / 100;
                        $("#exchangeGoodsTable").append('<tr>' +
                        '<td class="text_center" style="vertical-align: middle;"><img src="' + goodsUrl + '">' +
                        '<input type="hidden" value="'+goodsCheckboxAllList[i].goodsId+'" name="goodsIdList">' +
                        '</td>' +
                        '<td class="text_center" style="vertical-align: middle;">' + goodsCheckboxAllList[i].goodsName + '</br><li style="color: red;list-style: none;">' + realMoney + '元</li></td>' +
                        '<td class="text_center" style="vertical-align: middle;" title="金额">' +
                        '<div class="content">' +
                        '<input type="number" name="exchangePriceList" ' +
                        ' tag="validate"  ' +
                        ' class="payMoney form-control  required money minValue maxValue" ' +
                        ' autocomplete="off" maxlength="9" value="'+realMoney+'">' +
                        ' <label class="validate_tips" style="width: 66%"></label>' +
                        '</div>' +
                        '</td>' +
                        '<td class="text_center" style="vertical-align: middle;">' + exchangeType + '</td>' +
                        '<td class="text_center" style="vertical-align: middle;">' + goodsCheckboxAllList[i].categoryParent + '</td>' +
                        '<td class="text_center" style="vertical-align: middle;">' + goodsCheckboxAllList[i].goodsNum + '</td>' +
                        '<td class="text_center" style="vertical-align: middle;">' + goodsCheckboxAllList[i].goodsRemains + '</td>' +
                        '</tr>')
                    }

                    exchangePriceListChange()

                }
            });
            $("#goodsResetCheckboxBtn,#goodsResetRadioBtn").on("click", function () {
                $("#categoryParent").find("option").eq(0).prop("selected", "selected");
                $("#exchangeType").find("option").eq(0).prop("selected", "selected");
                $("#keyWord").val("");
            });
            $(".close,#closeBtn").on("click", function () {
                $("#categoryParent").find("option").eq(0).prop("selected", "selected");
                $("#exchangeType").find("option").eq(0).prop("selected", "selected");
                $("#keyWord").val("");
            });
            // 保存活动信息
            $(".btnSave").on("click", function(){
                if (!changeRadioId) {
                    $.fn.alert("活动商品选择不能为空")
                    return;
                }
                if (!changeCheckBoxArray || changeCheckBoxArray.length < 1) {
                    $.fn.alert("换购商品选择不能为空")
                    return;
                }
                validForm();

            });

            
        }
        function removeElement(arr, item) {
            return arr.filter(function(i){
                return i!=item;
            })
        }
        function checkGoodsActivity() {
            var checkGoodsActivityUrl = "<%=cpath%>/exchangeActivity/checkGoodsActivity.do";

            var that = $(this);
            $.ajax({
                type: "POST",
                url: checkGoodsActivityUrl,
                data: {
                    "goodsIdList": changeRadioId,
                    "startDate": $("#startDate").val(),
                    "endDate": $("#endDate").val(),
                    "infoKey": $("#infoKey").val(),
                },
                dataType: "json",
                async: false,
                beforeSend: appendVjfSessionId,
                success: function (datas) {
                    console.log("datas", datas)
                    if(datas.result.businessCode=='0'){
                        $.fn.alert(datas.result.msg);
                        return false;
                    }
                    var url = that.attr("url");
                    $.fn.confirm("确认发布？", function(){
                        $(".btnSave").attr("disabled", "disabled");
                        $("#divId").css("display","block");
                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                    return true;
                }
            });
        }
        function exchangePriceListChange() {
            $("[name='exchangePriceList']").on('change',function () {
                var validmoeny = $.validMoney($(this));
                var tips = $(this).parent().find(".validate_tips");
                if(!validmoeny){
                    tips.addClass("valid_fail_text");
                    tips.html("请输入正确的金额#.##");
                    tips.show();
                }else{
                    tips.removeClass("valid_fail_text");
                    tips.html("");
                    tips.hide();
                }
            })
            $("[name='exchangePriceList']").each(function(){
                $(this).triggerHandler("change");
            })
        }
        function validForm() {
            $("[name='exchangePriceList']").each(function(){
                $(this).triggerHandler("change");
            })
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }
            checkGoodsActivity();
            return true;
        }
        function initGoodsList(goods) {
            let paramJson2 = {
            }
            var url = "<%=cpath%>/vpointsGoods/getShopGoods.do";
            $.ajax({
                type: "POST",
                url: url,
                data: paramJson2,
                dataType: "json",
                async: false,
                beforeSend:appendVjfSessionId,
                success:  function(datas) {
                    console.log("datas",datas)
                    var reply = datas.reply;
                    if(datas.result.businessCode==='0'){
                        goodsList = reply;
                        $("#goodsDetailsBodyTable").empty()
                        $("#exchangeGoodsTable").empty()
                        for (var index in reply) {
                            if('${activityCog.activityGoodsId}'!=reply[index].goodsId){
                                continue;
                            }
                            changeRadioId = reply[index].goodsId;
                            var goodsUrl = "<%=imagePath%>/"+  reply[index].goodsUrl?.split(",")[0]
                            var exchangeType = getExchangeType(reply[index].exchangeType)
                            var realMoney = reply[index].realPay / 100;
                            $("#goodsDetailsBodyTable").append('<tr>' +
                                '<td class="text_center" style="vertical-align: middle;"><img src="'+goodsUrl+'"></td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsName+'</br><li style="color: red;list-style: none;">'+realMoney+'元</li></td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+exchangeType+'</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].categoryParent+'</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsNum+'</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsRemains+'</td>' +
                                '</tr>')

                        }
                        var listIndex = 0;
                        for (var index in reply) {
                            if(goods.indexOf(reply[index].goodsId)<0){
                                continue;
                            }
                            exchangeGoodsIdList.push(reply[index].goodsId);
                            changeCheckBoxArray.push(reply[index].goodsId);
                            var goodsUrl = "<%=imagePath%>/"+  reply[index].goodsUrl?.split(",")[0]
                            var exchangeType = getExchangeType(reply[index].exchangeType)
                            var realMoney = reply[index].realPay / 100;
                            var exchangePriceNum = goodsObj.exchangePriceList ? goodsObj.exchangePriceList[listIndex] : "";
                            $("#exchangeGoodsTable").append('<tr>' +
                                '<td class="text_center" style="vertical-align: middle;">' +
                                '<img src="'+goodsUrl+'">' +
                                '<input type="hidden" value="'+reply[index].goodsId+'" name="goodsIdList">' +
                                '</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsName+'</br><li style="color: red;list-style: none;">'+realMoney+'元</li></td>' +
                                '<td class="text_center" style="vertical-align: middle;">   ' +
                                '<div class="content">' +
                                '<input type="number" name="exchangePriceList" ' +
                                ' tag="validate"  ' +
                                ' class="payMoney form-control  required money minValue maxValue" ' +
                                ' autocomplete="off" maxlength="9" value="'+exchangePriceNum+'">' +
                                ' <label class="validate_tips" style="width: 66%"></label>' +
                                '</div>' +
                                '</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+exchangeType+'</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].categoryParent+'</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsNum+'</td>' +
                                '<td class="text_center" style="vertical-align: middle;">'+reply[index].goodsRemains+'</td>' +
                                '</tr>')
                            listIndex++;
                        }
                        exchangePriceListChange()
                    }
                }
            });
        }
        function getExchangeType(exchange) {
            if ("1" == exchange) {
                return "实物";
            }
            if ("2" == exchange) {
                return "电子券";
            }
            if ("3" == exchange) {
                return "话费";
            }
        }
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
        .blocker {
            float: left;
            vertical-align: middle;
            margin-right: 8px;
            margin-top: 8px;
        }
        .en-larger {
            margin-left: 8px;
        }
       
        .validate_tips {
            padding: 8px !important;
        }
       
        .tablebox table {
            width: 100%;
        }

        .tablebox table th,
        .tablebox table td {
            font-size: 12px;
            color: #7ca6f4;
            line-height: 26px;
            text-align: center;
        }

        .tablebox table tr th {
            background: #022852;
            cursor: pointer;
        }

        .tablebox table tr td {
            border-bottom: 1px solid #202e64;
        }


        tr td img{
            width: 25px;
            margin-top: 5px;
        }
        tr td p{
            width: 60px;
            height: 20px;
            font-weight: bold;
            border-radius: 5px;
            margin: auto;
            text-align: center;
            line-height: 20px;
            color: #000000;
        }
    </style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 换购活动</a></li>
            <li class="current"><a> 修改活动</a></li>
        </ul>
    </div>
    
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" action="<%=cpath%>/exchangeActivity/exchangeActivityUpdate.do" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="infoKey" id="infoKey" value="${activityCog.infoKey}">
                <input type="hidden" name="dialogType" id="dialogType" value="">
                <div class="widget box activityinfo">
				<!--- 活动信息 -->
                    <div class="tab-content">
                        <div class="widget-header">
                            <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                        </div>
                        <div class="widget-content panel no-padding">
                            <table class="active_board_table">
                                <tr>
                                    <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <input name="infoName" tag="validate" style="width: 400px !important;" value="${activityCog.infoName}"
                                                class="form-control required" autocomplete="off" maxlength="30"/>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
	                        		<td class="ab_left"><label class="title mart5">活动时间：<span class="required">*</span></label></td>
	                        		<td class="ab_main">
	                        			<div class="content" style="margin: 5px 0px;">
	                                        <input name="startDate" id="startDate" value="${activityCog.startDate}" class="form-control input-width-medium Wdate required"  tag="validate"
	                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd ', minDate:'%y-%M-%d'})" autocomplete="off"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endDate" id="endDate" value="${activityCog.endDate}" class="form-control input-width-medium Wdate required"  tag="validate"
	                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" autocomplete="off"/>
	                                        <label class="validate_tips"></label>
	                        			</div>
	                        			
	                        		</td>
	                        	</tr>
                                <tr>
                                    <td class="ab_left"><label class="title">优惠叠加：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content ">
                                            <span class="blocker en-larger">不叠加其他营销活动</span>
                                            <label class="validate_tips"></label>
                                        </div>
        
                                    </td>
                                </tr>
                                 <tr>
                                    <td class="ab_left"><label class="title">活动商品<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
										<div class="bottom-left">
                                            <a class="blue qrcodeUploadDialog" href="#">选择商品</a>
										</div>
                                    </td>
                                </tr>
                                 <tr>
                                    <td class="ab_left"><label class="title"><span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
										<div class="bottom-left">
                                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                                     style=''>
                                                <thead>
                                                <tr>
                                                    <th style="width: 6%;text-align: center;" >图片</th>
                                                    <th style="width: 13%;text-align: center;" >商品名称</th>
                                                    <th style="width: 6%;text-align: center;" >商品分类</th>
                                                    <th style="width: 6%;text-align: center;" >一级分类</th>
                                                    <th style="width: 6%;text-align: center;" >库存</th>
                                                    <th style="width: 6%;text-align: center;" >剩余个数</th>
                                                </tr>
                                                </thead>
                                                <tbody id="goodsDetailsBodyTable">
                                                
                                                </tbody>
                                            </table>
										</div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title">使用门槛：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content">
                                            <span class="blocker en-larger">活动总额满</span>
                                            <input name="consumerBuyLimit" tag="validate" value="${activityCog.consumerBuyLimit}"
                                                   class="form-control required money preValue maxValue input-width-small rule" minVal="0.0" maxVal="99999" autocomplete="off" maxlength="9" />
                                            <span class="blocker en-larger">元可换购</span>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title"><span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="bottom-left">
                                            <a class="blue exchangeGoods" href="#">换购商品</a>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="ab_left"><label class="title"><span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="bottom-left">
                                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                                   style=''>
                                                <thead>
                                                <tr>
                                                    <th style="width: 8%;text-align: center;" >图片</th>
                                                    <th style="width: 15%;text-align: center;" >商品名称</th>
                                                    <th style="width: 10%;text-align: center;" >换购价格(元)</th>
                                                    <th style="width: 6%;text-align: center;" >商品分类</th>
                                                    <th style="width: 6%;text-align: center;" >一级分类</th>
                                                    <th style="width: 6%;text-align: center;" >库存</th>
                                                    <th style="width: 6%;text-align: center;" >剩余个数</th>
                                                </tr>
                                                </thead>
                                                <tbody id="exchangeGoodsTable">
                    
                                                </tbody>
                                            </table>
                                        </div>
                                    </td>
                                </tr>
                               
                            </table>
                        </div>
                        <div class="active_table_submit mart20">
                            <div class="button_place">
                                <a class="btn btn-blue btnSave">保 存</a>
                                <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/exchangeActivity/showExchangeActivityList">返 回</button>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    </div>
<%--    <div id="tableMater" style="margin-top:-5px;max-height: 278px; overflow: auto; box-sizing: border-box;"><jsp:include page="table1.jsp"></jsp:include></div>--%>

    <div class="modal fade" id="qrcodeUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-hidden="false"  style="margin-top:-5px;max-height: 100%; overflow: auto; box-sizing: border-box;">
        <div class="modal-dialog" >
            <div class="modal-content" style="top:5%;left: -75%; width: 250%; height: auto;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel2">选择可用商品</h4>
                </div>
                
                <div class="widget-content form-inline">
                        <div class="row">
                            <div class="col-md-9 text-center">
                                <div class="form-group  search mart20" style="line-height: 35px;">
                                    <div class="search-item">
                                        <label class="control-label">商品分类：</label>
                                        <select name="goodsType" id="exchangeType" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <option value="1">实物</option>
                                            <option value="2">电子券</option>
                                            <option value="3">话费</option>
                                        </select>
                                    </div>
                                    <div class="search-item">
                                        <label class="control-label">一级分类：</label>
                                        <select name="firstType" id="categoryParent" class="form-control input-width-larger search"
                                                autocomplete="off">
                                            <option style="padding: 20px;" value="">全部</option>
                                            <c:forEach items="#{firstCategoryList}" var="cate">
                                                <option style="padding: 20px;" value="${cate.categoryType}">${cate.categoryName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div class="search-item" >
                                        <label class="control-label" style="width: 105px !important;">商品名称：</label>
                                        <input name="keyWord" id="keyWord" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                   
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12 text-center" style=" margin-top: 70px !important;" id="selectRadioBtb" >
                                    <button type="button" class="btn btn-primary btn-blue" id="goodsSelectRadioBtn">查 询</button>
                                    <button type="button" class="btn btn-reset btn-radius3 marl20" id="goodsResetRadioBtn">重 置</button>
                                </div>
                                <div class="col-md-12 text-center" style=" margin-top: 70px !important;" id="selectCheckboxBtb" >
                                    <button type="button" class="btn btn-primary btn-blue" id="goodsSelectCheckboxBtn">查 询</button>
                                    <button type="button" class="btn btn-reset btn-radius3 marl20" id="goodsResetCheckboxBtn">重 置</button>
                                </div>
                            </div>
                        </div>
                </div>
                <div class="modal-body" id="modal-body" >
                    <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                           id="dataTable_data"  style='white-space: nowrap; '>
                        <thead>
                        <tr>
                            <th style="width: 4%;text-align: center;" ></th>
                            <th style="width: 6%;text-align: center;" >图片</th>
                            <th style="width: 13%;text-align: center;" >商品名称</th>
                            <th style="width: 6%;text-align: center;" >商品分类</th>
                            <th style="width: 6%;text-align: center;" >一级分类</th>
                            <th style="width: 6%;text-align: center;" >库存</th>
                            <th style="width: 6%;text-align: center;" >剩余个数</th>
                        </tr>
                        </thead>
                        <tbody id="goodsDetailsBody">
                     
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-orange" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
  </body>
</html>
