<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>新增实物奖页面</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>

    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];
        var clickImg = function (id) {
            $("#addBatchDialog").modal("show");
            parent.$("#my_iframe").contents().find("#queryTY").click();
            //移除选中项
            $('input:checkbox').removeAttr('checked');
            $("#btnName").val(id);
        }

        // 本界面上传图片要求
        var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };
        $(function(){
            // 初始化校验控件
            $.runtimeValidate($("#code_form"));

            // 初始化功能
            initPage();
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });

            // 折扣现金切换
            $("input[name=isExchangeMoney]").on("change", function(){
                if('1' == $(this).val()){
                    $("#exchangeMoneyId").css("display", "");
                }else{
                    $("input[name='exchangeMinMoney']").val(0.00);
                    $("input[name='exchangeMaxMoney']").val(0.00);
                	$("#exchangeMoneyId").css("display", "none");
                }
             });
        });

        function initPage() {
        	
        	$("[data-toggle='popover']").popover();
			$('bodyMsg').on('click', function(event) { 
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
            $(".btnSave").click(function(){
                var flag = validForm();
                if (flag) {
                    var url = $(this).attr("url");
                    $.fn.confirm("确认发布？", function(){

                        $(".btnSave").attr("disabled", "disabled");

                        $("form").attr("action", url);
                        $("form").attr("onsubmit", "return true;");
                        $("form").submit();
                    });
                }
                return false;
            });
            $("#isName").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='isName']").val("1");
                    }else{
                        $("input:hidden[name='isName']").val("0");
                    }
                }
            });
            $("#isIdcard").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='isIdcard']").val("1");
                    }else{
                        $("input:hidden[name='isIdcard']").val("0");
                    }
                }
            });
            $("#isCheckCaptcha").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='isCheckCaptcha']").val("1");
                        $("input:hidden[name='isPhone']").val("1");
                        $("#isPhone").bootstrapSwitch("state",true);

                    }else{
                        $("input:hidden[name='isCheckCaptcha']").val("0");
                        $("input:hidden[name='isPhone']").val("0");
                        $("#isPhone").bootstrapSwitch("state",false);
                    }
                }
            });
            $("#isAddress").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='isAddress']").val("1");
                    }else{
                        $("input:hidden[name='isAddress']").val("0");
                    }
                }
            });
            $("#isPhone").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='isPhone']").val("1");
                    }else{
                        $("input:hidden[name='isPhone']").val("0");
                    }
                }
            });
            $("#addBatchDialog").delegate("#comBtn", "click", function () {
                $(window.frames["my_iframe"].document).find("input[type=radio]:checked").each(function () {
                    var btnName = $("#btnName").val();
                    var picWidth = $(this).siblings("div[name = picWidth]").html();
                    var picHeight = $(this).siblings("div[name = picHeight]").html();
                    var picX = $(this).siblings("div[name = picX]").html();
                    var picY = $(this).siblings("div[name = picY]").html();
                    $("#" + btnName).attr("src", "https://img.vjifen.com/images/vma/" + $(this).val());
                    $('input[name="' + btnName + '"]').val("https://img.vjifen.com/images/vma/" + $(this).val());
                });
                $("#addBatchDialog #closeBtn").trigger("click");
            });
            $("[name='cashPrize']").on("change", function () {
                if ($(this).val() == '0') {
                    $("#cashPrizeEndDay").css("display", "none");
                    $(this).closest("div").find("input[name='cashPrizeEndDay']").hide();
                    $(this).closest("div").find("input[name='cashPrizeEndDay']").val('');
                    $(this).closest("div").find("input[name='cashPrizeEndTime']").show();
                }
                if ($(this).val() == '1') {
                    $("#cashPrizeEndTime").css("display", "none");
                    $(this).closest("div").find("input[name='cashPrizeEndDay']").show();
                }
            });

            // 是否为限制所有限制类大奖只中一次
            $("input[name='winPrizeTime']").on("change", function(){
            	if($(this).val() == '0') {
            		$("tr.everyoneLimitType").css("display", "");
            		//$("tr.everyoneLimitType input").removeAttr("disabled");
            	} else {
                    $("tr.everyoneLimitType").css("display", "none");
                    //$("tr.everyoneLimitType input").attr("disabled", "disabled");
            	}
            });
            $("input[name='winPrizeTime'][value='1']").triggerHandler("change");
        }
        //验证金融数字和添加数字和小数点
        function onlyNum(that){
            var _value= $(that).val();
            //确保最多两位小数
            var regs = "/^(([1-9]\\d*)|(([0]\\.\\d{1,2}|[1-9][0-9]*\\.\\d{1,2})))$/";
            regs=regs.substr(1,regs.length - 2) ;
            var reg = new RegExp(regs);
            if (!reg.test(_value)) {
                return false;
            }
            //去除非数字字符
            var vv = Math.round(that.value.replace(/[^\d.]/g,"") * 100) /100;    vv = Number(vv);
            vv =vv.toFixed(2);//四舍五入
            that.value =  vv;
        }

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }

            if($("input[name=isExchangeMoney]:checked").val() == '1'){
                if($("input[name='exchangeMinMoney']").val() == '' || $("input[name='exchangeMaxMoney']").val() == ''){
                	alert("兑换金额区间不能为空");
                    return false;
                }else if($("input[name='exchangeMinMoney']").val() == 0.00 || $("input[name='exchangeMaxMoney']").val() == 0.00){
                    alert("兑换金额区间值必须大于0.00元");
                    return false;
                }
            }

            if($("input[name= prizeListPic]").val() == "" || $("input[name= prizeListPic]").val() == null){
                alert("列表图不能为空");
                return false;
            }
            if($("input[name= prizeWinPic]").val() == "" || $("input[name= prizeWinPic]").val() == null){
                alert("中出图不能为空");
                return false;
            }
            if($("input[name= prizeEarnPic]").val() == "" || $("input[name= prizeEarnPic]").val() == null){
                alert("领奖图不能为空");
                return false;
            }
            if($("input[name= prizeEndTime]").val() == "" || $("input[name= prizeEndTime]").val() == null){
                alert("中奖截至时间不能为空");
                return false;
            }
            if($("input[name= cashPrizeEndTime]").val() == "" && $("input[name= cashPrizeEndDay]").val() == ""){
                alert("兑奖截止时间或者截止天数不能为空");
                return false;
            }
            var sHTML = $('.summernote').summernote('code');
            $('#prizeContent').val(sHTML.trim());
            $('#goodsUrl').val($("#imgUrl").val());
            $('#goodsBigUrl').val($("#bigImgUrl").val());

            if(!sHTML){
                alert("请输入说明");
                return false;
            }
            return true;
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
        /*.addImg1 {*/
        /*    width: 100px;*/
        /*    height: 100px;*/
        /*    position: absolute;*/
        /*    z-index: 2;*/
        /*    cursor: pointer;*/
        /*}*/
        .addImg1 {
            width: 120px;
            height: 120px;
            position: absolute;
            left: 0;
            top: 0;
            z-index: 2;
            cursor: pointer;
        }
        .modal-body1 {
            display: block;
            height: 600px;
        }
        .frame-body {
            display: block;
            height: 100%;
            width: 100%;
        }
        .modal-dialog {
            display: block;
            width: 800px;
        }
        .item {
            width: 120px;
            height: 120px;
            float: left;
            position: relative;
            margin: 20px;
        }
    </style>
</head>

<body>
<div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/vcodeActivityBigPrize/doBigPrizeAdd.do">
                <div class="widget box">
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>新增实物</h4>
                         <span class="marl10" title="温馨提示" data-html="true"
                                data-container="body" data-toggle="popover" data-placement="auto"
                                data-content="
                                <b>1</b>.实物全称用于前端中出实物名字使用，请务必填写准确。</br>
                                <b>2</b>.兑出截止时间，如选择有效兑出天数。示例:如有效兑出天数5天，</br>
                                &nbsp;&nbsp;&nbsp;如用户1月1日14:00中出，有效兑出天数截止1月6日23:59:59。</br>
                                <b>3</b>.是否限制中出限制，勾选“限制”当前项目所有限制的实物每自然年只能中出1次，</br>
                                &nbsp;&nbsp;&nbsp;勾选“不限制”当前实物不限制中出次数，（不包括可疑用户，黑名单用户）。</br>
                                <b>4</b>.可疑用户不中出实物。</br>
                                <b>5</b>.领实物方式填写注意，务必与前端开发保持一致。</br>
                                <b>6</b>.实物中出图片，仅通用小程序项目支持配置，其他项目配置该图片无效。</br>
                                <b>7</b>.实物列表图，所有项目均支持配置，显示在个人中心--扫码中出食物列表图片。</br>
                                <b>8</b>.领实物方式：“自提方式”类型限制核销人员必须为绑定兑换点身份可核销成功。</br>
                                &nbsp;&nbsp;&nbsp;未绑定兑换点注册的核销人员仅可核销“物流发货”“在线直接领取”的实物。</br>
                                <b>9</b>.积分抽中实物仅中出截止日期生效。</br>
                                <b>10</b>.积分抽中实物中出不限制个数，与扫码中出个数无关系。">
                            <i class="iconfont icon-tixing" id="bodyMsg" style="color: red; font-size: 20px;"></i>
                        </span>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr style="display:none">
                                <td class="ab_main" colspan="3">
                                </td>
                            </tr>
                            <tr>
                                <input type="hidden" name="prizeContent" id="prizeContent" value="">
                                <td class="ab_left"><label class="title">实物全称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="prizeName" tag="validate"
                                               class="form-control required" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">中出类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="prizeType" class="form-control input-width-larger search" autocomplete="off" >
                                            <option value="P">实物</option>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${showLxPrizeType eq '1'}">
                            <tr>
                                <input type="hidden" name="goodsContent" id="goodsContent" value="">
                                <td class="ab_left"><label class="title">N元乐购奖项编码：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="lxPrizeType" class="form-control input-width-larger" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            </c:if>
                            <tr>
                                <td class="ab_left"><label class="title">中出截至时间：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker"></span>
                                        <input name="prizeEndTime" id="prizeEndTime"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" style="width: 180px !important;" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">兑出截至时间：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="cashPrize" value="0"
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">固定截止时间</span>
                                        <input name="cashPrizeEndTime" id="cashPrizeEndTime"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" style="width: 180px !important;" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                        <input type="radio" class="tab-radio" name="cashPrize" value="1"
                                               style="float:left; margin-left: 20px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">中出后有效兑出天数</span>
                                        <input name="cashPrizeEndDay"
                                               class="form-control required" autocomplete="off"
                                               style="width: 200px;display:none;"/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">实物兑换红包：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="isExchangeMoney" value="0"
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">不可兑换</span>
                                        <input type="radio" class="tab-radio" name="isExchangeMoney" value="1"
                                               style="float:left; margin-left: 10px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">兑换</span>
                                    </div>
                                    <div class="content moneyRange" id="exchangeMoneyId" style="display: none;">
                                        <input name="exchangeMinMoney" tag="validate" value="0"
                                            class="form-control money preValue minValue input-width-small" autocomplete="off" maxlength="6" minVal="0.01" maxVal="500"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="exchangeMaxMoney" tag="validate" value="0"
                                            class="form-control money sufValue maxValue input-width-small" autocomplete="off" maxlength="6" minVal="0.01" maxVal="500"/>
                                        <span class="blocker en-larger">(元)</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">实物过期是否回收：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="isRecycle" value="0"
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">回收</span>
                                        <input type="radio" class="tab-radio" name="isRecycle" value="1"
                                               style="float:left; margin-left: 10px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">不回收</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">是否短信通知：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="isMsg" value="0"
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">否</span>
                                        <input type="radio" class="tab-radio" name="isMsg" value="1"
                                               style="float:left; margin-left: 10px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">是</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">每年限制类奖项每人是否只中一次：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="winPrizeTime" value="0"
                                               style="float:left; margin-left: 10px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">否</span>
                                        <input type="radio" class="tab-radio" name="winPrizeTime" value="1"
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">是</span>
                                    </div>
                                </td>
                            </tr>
                            <tr class="everyoneLimitType">
                                <td class="ab_left"><label class="title">当前奖项每人扫码中奖次数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="everyoneLimitType" value="0" 
                                               style="float:left; margin-left: 10px !important;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">每周</span>
                                        <input type="radio" class="tab-radio" name="everyoneLimitType" value="1" 
                                               style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">每月</span>
                                        <input name="everyoneLimitNum" tag="validate" value="0"
                                               class="form-control input-width-small required number integer"  autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">个（0时不限制）</span>       
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">每天扫码中出限制次数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="dayLimitNum" tag="validate" value="1"
                                               class="form-control input-width-small required number integer"  autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">个（0时不限制）</span>       
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">兑出咨询电话：<span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="cashPrizeTel" tag="validate"
                                               class="form-control required"  autocomplete="off" maxlength="50" />
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">咨询时间：<span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="cashAdvisoryTime" tag="validate"
                                               class="form-control required"  autocomplete="off" maxlength="250" />
                                    </div>
                                </td>
                            </tr>
                            <input type="hidden" name="btnName" id="btnName" value="">
                            <tr>
                                <td class="ab_left"><label class="title">实物中出图片：<span class="required">*</span></label>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=13"
                                               target= "my_iframe" class="addImg1"  onclick="clickImg('prizeWinPic');" style="z-index: 3; opacity: 0;"></a>
                                            <img class="addImg1" id="prizeWinPic"
                                                 src="<%=cpath %>/inc/vpoints/img/a12.png"/>
                                            <input type="hidden" name="prizeWinPic">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">领实物图片：<span class="required">*</span></label>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=13"
                                               target= "my_iframe" class="addImg1"  onclick="clickImg('prizeEarnPic');" style="z-index: 3; opacity: 0;"></a>
                                            <img class="addImg1" id="prizeEarnPic"
                                                 src="<%=cpath %>/inc/vpoints/img/a12.png"/>
                                            <input type="hidden" name="prizeEarnPic">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">奖品列表图片：<span class="required">*</span></label>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=13"
                                               target= "my_iframe" class="addImg1"  onclick="clickImg('prizeListPic');" style="z-index: 3; opacity: 0;"></a>
                                            <img class="addImg1" id="prizeListPic"
                                                 src="<%=cpath %>/inc/vpoints/img/a12.png"/>
                                            <input type="hidden" name="prizeListPic">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
<%--                            <tr>--%>
<%--                                <td class="ab_left"><label class="title">奖品说明：<span class="white">*</span></label></td>--%>
<%--                                <td class="ab_main" colspan="3">--%>
<%--                                    <div class="content">--%>
<%--                                        <textarea name="prizeExplain" class="k-edit" rows="5" maxlength="200"></textarea>--%>
<%--                                    </div>--%>
<%--                                </td>--%>
<%--                            </tr>--%>
                            <tr>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
		                                <div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>实物说明</h4></div>
			                            <div class="summernote"><span></span></div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="widget-header"><h4><i class="iconfont icon-daoru"></i>领实物方式</h4></div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">领实物方式：</label></td>
                                <td class="ab_main" colspan="3">
                                        <select name="verificationType" class="form-control input-width-larger search" autocomplete="off" >
                                        <option value="0">自提方式</option>
                                        <option value="1">物流发货（有核销码）</option>
                                        <option value="3">物流发货（无核销码）</option>
                                            <option value="2">在线直接领取</option>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">姓名<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isName" type="hidden" value="1" />
                                        <input id="isName" type="checkbox"  data-on-text="需要" data-off-text="不需要" checked/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">手机号<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isPhone" type="hidden" value="1" />
                                        <input id="isPhone" type="checkbox" data-on-text="需要" data-off-text="不需要" checked/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">身份证<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isIdcard" type="hidden" value="1" />
                                        <input id="isIdcard" type="checkbox" data-on-text="需要" data-off-text="不需要"  checked/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">地址<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isAddress" type="hidden" value="1" />
                                        <input id="isAddress" type="checkbox"  data-on-text="需要" data-off-text="不需要" checked/>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">是否需要验证<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="isCheckCaptcha" type="hidden" value="1" />
                                        <input id="isCheckCaptcha" type="checkbox" data-on-text="需要" data-off-text="不需要" checked/>
                                    </div>
                                </td>
                            </tr>
                            <div class="modal fade" id="addBatchDialog" tabindex="-1" data-backdrop="static"
                                 aria-labelledby="myModalLabel" aria-hidden="false">
                                <div class="modal-dialog">
                                    <div class="modal-content" style="top:1%;">
                                        <div class="modal-body1">
                                            <iframe class="frame-body" name="my_iframe"
                                                    src=""
                                                    scrolling="y"></iframe>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" id="comBtn" class="btn btn-default do-add btn-red"
                                                    data-dismiss="">确 认
                                            </button>
                                            <button type="button" id="closeBtn" class="btn btn-default"
                                                    data-dismiss="modal">关 闭
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </table>
                    </div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave" >确认发布</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/vcodeActivityBigPrize/showBigPrizeList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>
