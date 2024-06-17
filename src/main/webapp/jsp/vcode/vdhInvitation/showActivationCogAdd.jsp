<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil" %>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%
    String cpath = request.getContextPath();
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String allPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + cpath;
%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/assets/js/custom/giftspack/utils.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUpForMany.js?v=1.1.6"></script>

    <%--活动开关--%>
    <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>

    <!--富文本编辑器-->
    <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>

    <%--树形行政区域--%>
    <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/demo.css" type="text/css">
    <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
    <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.core.js"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.excheck.js"></script>

    <script>
        var basePath = '<%=cpath%>';
        var allPath = '<%=allPath%>';
        var imgSrc = [];

        // 本界面上传图片要求
        var customerDefaults = {
            fileType: ["jpg", "png", "bmp", "jpeg"],   // 上传文件的类型
            fileSize: 1024 * 200 // 上传文件的大小 200K
        };

        // 行政区域
        var setting = {
            check: {
                enable: true,
                chkboxType: { "Y": "p", "N": "ps" }
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        var zNodes = eval('${areaJson}');

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

            // 检验活动名称是否重复
            $("input[name='activityName']").on("blur",function(){
                var activityName = $("input[name='activityName']").val();
                if(activityName === "") return;
                checkName(activityName);
            });

            // 富文本
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });

            // 行政区域初始化
            $.fn.zTree.init($("#tree"), setting, zNodes);

            // 邀请者活动区域动态显示
            $("input[name='inviterAreaType']").on("change", function() {
                var selectedValue = $(this).val();

                if (selectedValue === "0") {
                    $(".group").hide();
                    $(".region").show();
                } else if (selectedValue === "1") {
                    $(".region").hide();
                    $(".group").show();
                }
            });

            // 大区全选
            $("#allCheck").on("change", function () {
                if ($(this).prop("checked")) {
                    $("[name='regionCog']").prop("checked", "checked");
                } else {
                    $("[name='regionCog']").prop("checked", "");
                }
            });

            // 增加门店分组
            $("form").on("click", "#addTerminalGroup", function () {

                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addTerminalGroup").text("删除");
                    $copySku.find("option:first").prop('selected', 'selected');
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });

            // 新增SKU
            $("form").on("click", "#addSku", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addSku").text("删除");
                    $copySku.find('option:selected').eq(0).removeAttr("selected")
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });

            // 新增奖项
            $("form").on("click", "#addPrizeItem", function(){
                if ($(this).text() == '新增') {
                    var $cloneItem = $(this).closest("tr").clone(true, true);
                    $cloneItem.find("input[name='numOfPeople']").val("0");
                    $cloneItem.find("input[name='minMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='maxMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("#addPrizeItem").text("删除");
                    // 将新行添加到表格中
                    $(this).closest("tbody").append($cloneItem);
                } else {
                    $(this).closest("tr").remove();
                }

                $(this).closest("tbody").find("tr").each(function(i, obj){
                    $(this).find("label[name='NO']").text(i+1);
                    $(this).find("input[name='ladder']").attr("value",i+1);
                });
            });

            //新用户奖励设置格式处理
            $("form").on("blur", "input[name='minMoney'], input[name='maxMoney'], input[name='minVpoints'], input[name='maxVpoints']", function() {
                var $self = $(this);
                var val = $self.val();
                var isMinValue = $self.attr("name").startsWith("min");
                var isIntegerValue = $self.hasClass("number");

                // 校验格式是否正确
                if (val !== "") {
                    if (isIntegerValue) {
                        if (!/^\d*$/.test(val)) {
                            $.fn.alert(isMinValue ? "最小值格式不正确，请输入整数！" : "最大值格式不正确，请输入整数！");
                            $self.val("0");
                            return;
                        }
                    } else {
                        if (!/^\d+(\.\d{1,2})?$/.test(val)) {
                            $.fn.alert(isMinValue ? "最小值格式不正确，请输入两位小数！" : "最大值格式不正确，请输入两位小数！");
                            $self.val("0.00");
                            return;
                        }
                    }
                }

                // 校验最大值是否小于最小值
                if (!isMinValue) {
                    var $minValueInput = $self.closest("tr").find("input[name='" + $self.attr("name").replace("max", "min") + "']");
                    var maxVal = parseFloat(val);
                    var minVal = parseFloat($minValueInput.val());

                    if (maxVal < minVal) {
                        $.fn.alert("最大值不能小于最小值！");
                        $self.val(isIntegerValue ? "0" : "0.00");
                        return;
                    }
                } else {
                    var $maxValueInput = $self.closest("tr").find("input[name='" + $self.attr("name").replace("min", "max") + "']");
                    var minVal = parseFloat(val);
                    var maxVal = parseFloat($maxValueInput.val());

                    if (maxVal !== "" && maxVal !== 0 && minVal > maxVal) {
                        $.fn.alert("最小值不能大于最大值！");
                        $self.val(isIntegerValue ? "0" : "0.00");
                        return;
                    }
                }
            });

            // 老用户是否参加动态显示配置规则
            $("form").on("change", "input[name='oldUserIsInvolved']", function() {
                console.log("=======================老用户是否参加动态:"+$(this).val())
                var $rewardSetting = $("tr.reward-setting");
                if ($(this).prop("checked")) {
                    $rewardSetting.show();
                } else {
                    $rewardSetting.hide();
                }
            });

            // 保存
            $(".btnSave").click(function () {
                var flag = validForm();
                console.log("=======================表单校验最后返回:"+flag)
                if (flag) {
                    var url = $(this).attr("url");
                    $("form").attr("action", url);
                    $("form").attr("onsubmit", "return true;");
                    $("form").submit();
                }
                return false;
            });

            // 返回
            $(".btnReturn").click(function () {
                $("form").attr("action", $(this).data("url"));
                $("form").attr("onsubmit", "return true;");
                $("form").submit();
            });
        }

        function checkName(bussionName){
            $.ajax({
                url : "${basePath}/vdhInvitation/checkName.do",
                data:{
                    "infoKey":"",
                    "checkName":bussionName
                },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    if (data == "0") {
                        $.fn.alert("活动名称已存在，请重新输入");
                    }
                }
            });
        }

        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if (!validateResult) {
                return false;
            }

            // 图片处理
            var imgUrlArr = $("#imgUrl").val().split(",")
            var bigImgUrlArr = $("#bigImgUrl").val().split(",")
            var threeImgUrlArr = $("#threeImgUrl").val().split(",")
            var fourImgUrlArr = $("#fourImgUrl").val().split(",")
            if(imgUrlArr.length > 1){
                $.fn.alert("活动背景图片只能上传一张");
                return false;
            }
            if (bigImgUrlArr.length > 1) {
                $.fn.alert("活动攻略图片只能上传一张");
                return false;
            }
            if (threeImgUrlArr.length > 1) {
                $.fn.alert("分享海报背景图片只能上传一张");
                return false;
            }
            if (fourImgUrlArr.length > 1) {
                $.fn.alert("消费者扫宣传海报提示弹窗图片只能上传一张");
                return false;
            }
            $('#backgroundUrl').val(imgUrlArr);
            $('#introductionUrl').val(bigImgUrlArr);
            $('#posterBackgroundUrl').val(threeImgUrlArr);
            $('#modalWindowUrl').val(fourImgUrlArr);
            if(!$("input[name=backgroundUrl]").val()){
                $.fn.alert('请上传活动背景图片');
                return false;
            }
            /*if(!$("input[name=introductionUrl]").val()){
                $.fn.alert('请上传活动攻略图片');
                return false;
            }*/
            if(!$("input[name=posterBackgroundUrl]").val()){
                $.fn.alert('请上传分享海报背景图片');
                return false;
            }
            if(!$("input[name=modalWindowUrl]").val()){
                $.fn.alert('请上传消费者扫宣传海报提示弹窗图片');
                return false;
            }

            // 富文本编辑器
            var sHTML = $('.summernote').summernote('code');
            /*if(sHTML.trim() === '<p><br></p>' || sHTML.trim() === ''){
                $.fn.alert("请编辑活动规则");
                return false;
            }*/
            $('#rule').val(sHTML.trim());

            // 邀请人活动区域处理
            var selectedValue = '';
            var areaTypeRadios = document.getElementsByName("inviterAreaType");
            for (var i = 0; i < areaTypeRadios.length; i++) {
                if (areaTypeRadios[i].checked) {
                    selectedValue = areaTypeRadios[i].value;
                    break;
                }
            }
            if(selectedValue == '0'){
                var regionCogArr = '';
                $("input[name='regionCog']:checked").each(function(index){
                    regionCogArr += $(this).val();
                    if (index < $("input[name='regionCog']:checked").length - 1) {
                        regionCogArr += ",";
                    }
                });
                if (regionCogArr == ''){
                    $.fn.alert("请选择邀请人参加的活动区域");
                    return false;
                }
                $("#bigRegion").val(regionCogArr);
                $("#terminalGroup").val('');
            }else{
                var terminalGroupArr = '';
                $("select[name='tGroup']").each(function(index){
                    terminalGroupArr += $(this).val();
                    if (index < $("select[name='tGroup']").length - 1) {
                        terminalGroupArr += ",";
                    }
                });
                if (terminalGroupArr == ''){
                    $.fn.alert("请选择邀请人参加的活动区域");
                    return false;
                }
                $("#terminalGroup").val(terminalGroupArr);
                $("#bigRegion").val('');
            }

            // 被邀请人活动区域处理
            var treeObj = $.fn.zTree.getZTreeObj("tree");
            var nodes = treeObj.getCheckedNodes(true);
            if(nodes != ''){
                // 省-市-县;省-市;省;省-市-县...
                var lastChar;
                var province;
                var city;
                var areaStr="";
                var idx = 0;
                for ( var i = 0; i <nodes.length; i++){
                    // 省
                    if(nodes[i].level == '0'){
                        province = nodes[i].name;
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-'){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                        }
                        areaStr += province + "-";
                        idx = 0;
                    }
                    // 市
                    if(nodes[i].level == '1'){
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-' && idx > 0){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                        }
                        if(idx != 0){
                            areaStr += province + "-";
                        }
                        city = nodes[i].name;
                        areaStr += city;
                        areaStr += "-";
                        idx = 1;
                    }
                    // 区县
                    if(nodes[i].level == '2'){
                        if(idx == 2){
                            areaStr += province + "-" + city + "-";
                        }
                        areaStr += nodes[i].name;
                        areaStr += ";";
                        idx = 2;
                    }
                }
                if(lastChar.length > 0 || areaStr.length > 0){
                    lastChar = areaStr.substring(areaStr.length - 1);
                    if(lastChar == ';' || lastChar == '-'){
                        areaStr = areaStr.substring(0, areaStr.length - 1);
                    }
                }
                $("#inviteeArea").val(areaStr);
                if(!$("input[name=inviteeArea]").val() || $("input[name=inviteeArea]").val()==''){
                    $.fn.alert('请选择被邀请人扫码区域');
                    return false;
                }
            }else{
                $.fn.alert('请选择被邀请人扫码区域~');
                return false;
            }

            // 新用户奖励设置处理
            // 定义一个空字符串，用于保存所有行的数据
            var newUserRowData = "";
            var lastNumOfPeople = 0;
            var check = true;
            var checkMsg = "";
            // 使用 jQuery 选择器获取表格中所有的行（除了第一行标题行）
            $("#newUserPrize tbody tr").each(function(index) {
                // 获取当前行中每个单元格的值，并将它们用逗号分隔
                var ladder = $(this).find("input[name='ladder']").val();// 阶梯
                var numOfPeople = $(this).find("input[name='numOfPeople']").val();// 达标次数
                var minMoney = $(this).find("input[name='minMoney']").val();// 最小金额
                var maxMoney = $(this).find("input[name='maxMoney']").val();// 最大金额

                // 数据检查
                if (ladder == "" || ladder == null
                    || numOfPeople == "" || numOfPeople == null
                    || minMoney == "" || minMoney == null
                    || maxMoney == "" || maxMoney == null) {
                    check = false;
                    checkMsg = "新用户奖励设置第" + (index + 1) + "行中有信息需要完善！";
                    return false; // 停止遍历
                }else if(maxMoney <= 0){
                    check = false;
                    checkMsg = "新用户奖励设置第" + (index + 1) + "行中最大金额需大于0！";
                    return false; // 停止遍历
                }

                // 检查阶梯达标人数是否合法
                if(index === 0){
                    if (numOfPeople <= 0) {
                        check = false;
                        checkMsg = "新用户奖励设置第1阶梯达标人数只能大于0！";
                        return false; // 停止遍历
                    }
                } else if (index > 0 && parseInt(numOfPeople) <= lastNumOfPeople) {
                    check = false;
                    checkMsg = "新用户奖励设置第" + (index + 1) + "行达标人数需大于前一行！";
                    return false; // 停止遍历
                }
                lastNumOfPeople = parseInt(numOfPeople);

                // 将这些值拼接成一个逗号分隔的字符串，并添加到 rowData 中
                newUserRowData += ladder + "," + numOfPeople + ",0," + minMoney + "," + maxMoney + ";";
            });
            if (!check){
                $.fn.alert(checkMsg);
                return false;
            }else{
                $('#newUserRule').val(newUserRowData);
            }

            // 老用户奖励设置处理
            // 获取复选框的值，如果被选中返回 1，否则返回 0
            var oldUserIsInvolved = $("#oldUserIsInvolved").prop("checked") ? 1 : 0;
            if (oldUserIsInvolved === 1){
                // 定义一个空字符串，用于保存所有行的数据
                var oldUserRowData = "";
                var lastNumOfPeopleOld = 0;
                var checkOld = true;
                var checkMsgOld = "";
                // 使用 jQuery 选择器获取表格中所有的行（除了第一行标题行）
                $("#oldUserPrize tbody tr").each(function(index) {
                    // 获取当前行中每个单元格的值，并将它们用逗号分隔
                    var ladder = $(this).find("input[name='ladder']").val();
                    var numOfPeople = $(this).find("input[name='numOfPeople']").val();
                    var minMoney = $(this).find("input[name='minMoney']").val();
                    var maxMoney = $(this).find("input[name='maxMoney']").val();

                    // 数据检查处理
                    if (ladder == "" || ladder == null
                        || numOfPeople == "" || numOfPeople == null
                        || minMoney == "" || minMoney == null
                        || maxMoney == "" || maxMoney == null) {
                        checkOld = false;
                        checkMsgOld = "老用户奖励设置第" + (index + 1) + "行中有信息需要完善！";
                        return false; // 停止遍历
                    }else if(maxMoney <= 0){
                        checkOld = false;
                        checkMsgOld = "老用户奖励设置第" + (index + 1) + "行中最大金额需大于0！";
                        return false; // 停止遍历
                    }

                    // 检查阶梯达标人数是否合法
                    if(index === 0){
                        if (numOfPeople <= 0) {
                            checkOld = false;
                            checkMsgOld = "老用户奖励设置第1阶梯达标人数只能大于0！";
                            return false; // 停止遍历
                        }
                    } else if (index > 0 && parseInt(numOfPeople) <= lastNumOfPeopleOld) {
                        checkOld = false;
                        checkMsgOld = "老用户奖励设置第" + (index + 1) + "行达标人数需大于前一行！";
                        return false; // 停止遍历
                    }
                    lastNumOfPeopleOld = parseInt(numOfPeople);

                    // 将这些值拼接成一个逗号分隔的字符串，并添加到 rowData 中
                    oldUserRowData += ladder + "," + numOfPeople + ",0," + minMoney + "," + maxMoney + ";";
                });
                if (!checkOld){
                    $.fn.alert(checkMsgOld);
                    return false;
                }else{
                    $('#oldUserRule').val(oldUserRowData);
                }
            }


            return true;
        }

        // 富文本上传图片
        function uploadImages(files) {
            var formData = new FormData();
            for (f in files) {
                formData.append("file", files[f]);
            }
            var xhr;
            if (XMLHttpRequest) {
                xhr = new XMLHttpRequest();
            } else {
                xhr = new ActiveXObject('Microsoft.XMLHTTP');
            }
            xhr.open("post", basePath+"/skuInfo/imgUploadUrl.do?vjfSessionId=" + $("#vjfSessionId").val(), true);
            xhr.onreadystatechange = function(){
                if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200){
                    var result = jQuery.parseJSON(xhr.responseText);
                    if(result["errMsg"]=="error"){
                        $.fn.alert("上传失败");
                        return;
                    }
                    var srcList=result["ipUrl"].split(",");
                    console.log("=======================富文本上传图片 srcList:"+srcList)
                    for(var i = 0;i<srcList.length;i++){
                        console.log("=======================富文本上传图片 srcList[i]:"+srcList[i])
                        $('.summernote').summernote('insertImage',srcList[i], "名称");
                    }

                }else{
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
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> V店惠邀请有礼</a></li>
            <li class="current"><a> 新增活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
                  action="<%=cpath %>/vdhInvitation/addActivity.do">
                <input type="hidden" name="backgroundUrl" id="backgroundUrl" value="" />
                <input type="hidden" name="introductionUrl" id="introductionUrl" value="" />
                <input type="hidden" name="posterBackgroundUrl" id="posterBackgroundUrl" value="" />
                <input type="hidden" name="modalWindowUrl" id="modalWindowUrl" value="" />
                <input type="hidden" name="rule" id="rule" value="" />
                <input type="hidden" name="newUserRule" id="newUserRule" value="" />
                <input type="hidden" name="oldUserRule" id="oldUserRule" value="" />
                <input type="hidden" name="bigRegion" id="bigRegion" value="" />
                <input type="hidden" name="terminalGroup" id="terminalGroup" value="" />

                <div class="widget box">
                    <%--基础信息--%>
                    <div class="widget-header">
                        <h4><i class="iconfont icon-xinxi"></i>基础信息</h4>
                    </div>
                    <div class="widget-content panel no-padding">
                        <table class="active_board_table">
                            <%--活动开关--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动开关：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="status" type="hidden" value="1" />
                                        <input id="status" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning" />
                                    </div>
                                </td>
                            </tr>

                            <%--活动名称--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="activityName" tag="validate"
                                               class="form-control input-width-larger required" autocomplete="off"
                                               maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--活动简称--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动简称：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="shortName" tag="validate"
                                               class="form-control input-width-larger required varlength"
                                               autocomplete="off" data-length="10" maxlength="10" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--活动有效期--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动有效期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="startTime" id="startTime"
                                               class="form-control input-width-medium Wdate required preTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endTime\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTime" id="endTime"
                                               class="form-control input-width-medium Wdate required sufTime"
                                               tag="validate" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startTime\')}'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--活动背景--%>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">活动背景：<span class="required">*</span><br/></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="imgUrl" name="prizeImgAry" type="hidden" class='filevalue' />
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;"id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp" multiple />
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>

                            <%--活动攻略--%>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">活动攻略：<span class=""></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="bigImgUrl" name="prizeImgAry" type="hidden"
                                                   class='filevalue' />
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp" multiple />
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>

                            <%--分享海报背景--%>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">分享海报背景：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="threeImgUrl" name="prizeImgAry" type="hidden"
                                                   class='filevalue' />
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp" multiple />
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>

                            <%--富文本--%>
                            <tr>
                                <td class="ab_left"><label class="title">活动规则：<span class=""></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="summernote"></div>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <%--邀请人激励规则配置--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>邀请人激励规则配置</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <%--完成任务sku--%>
                            <tr>
                                <td class="ab_left"><label class="title">完成任务sku：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="skuKeyArray" tag="validate">
                                            <option value="">请选择SKU</option>
                                            <c:if test="${!empty skuList}">
                                                <c:forEach items="${skuList}" var="sku">
                                                    <option value="${sku.skuKey}" data-img="${sku.skuLogo}">${sku.skuName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addSku">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--新用户类型--%>
                            <tr>
                                <td class="ab_left"><label class="title">新用户类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" name="newUserType" value="0" style="float: left;  margin-top: 12px; cursor: pointer;"  checked /> <span class="blocker en-larger" style="margin-left: 2px;"> 当年未在本省区扫码</span>
                                        <input type="radio" name="newUserType" value="1" style="float: left;  margin-top: 12px; cursor: pointer;"  /><span class="blocker en-larger" style="margin-left: 2px;"> 累计未在本省区扫码</span>
                                    </div>
                                </td>
                            </tr>

                            <%--新用户奖励设置--%>
                            <tr>
                                <td class="ab_left"><label class="title">新用户奖励设置：<span class=""></span></label></td>
                            </tr>
                            <tr>
                                <td class="ab_main" colspan="6">
                                    <table id="newUserPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                            <tr>
                                                <th style="width:5%; text-align: center;">阶梯</th>
                                                <th style="width:13%; text-align: center;" class="cogItemMoney">达标次数范围最大值</th>
                                                <th style="width:9%; text-align: center;" class="cogItemMoney">奖励类型</th>
                                                <th style="width:25%; text-align: center;" class="cogItemMoney">每完成1次奖励范围</th>
                                                <th style="width:9%; text-align: center;" class="cogItemVpoints">操作</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <%--阶梯--%>
                                                <td>
                                                    <label name="NO" style="line-height: 30px;">1</label>
                                                    <input name="ladder" value="1" style="display: none" />
                                                </td>
                                                <%--达标人数--%>
                                                <td>
                                                    <input type="text" name="numOfPeople" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                           tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;" />
                                                </td>
                                                <%--激励类型--%>
                                                <td>
                                                    <span>现金</span>
                                                </td>
                                                <%--激励范围--%>
                                                <td>
                                                    <div name="moneyCog" class="random-prize content">
                                                        <input type="text" name="minMoney" class="form-control input-width-small money maxValue"  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="999.99" maxlength="6" style="position: relative;left: 9%;width: 30% !important" />
                                                        <label style="position: relative;right: 35%;top: 3px">&nbsp;-&nbsp;</label>
                                                        <input type="text" name="maxMoney" class="form-control input-width-small money maxValue"  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="999.99" maxlength="6" style="position: relative;left: 23%;width: 30% !important" />
                                                    </div>
                                                </td>
                                                <%--操作--%>
                                                <td>
                                                    <label id="addPrizeItem" class="btn-txt-add-red">新增</label>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>

                            <%--备注--%>
                            <tr>
                                <td class="ab_main" colspan="3">
                                    <div style="margin-top: 10px; color: #db2d2a; font-size: 12px; text-align: center;">
                                        备注一：达标次数范围最大值（示例：第一阶梯配置2，则表示1-2，第二阶梯配置4，表示3-4，下同）
                                        备注二：不在达标阶梯内，则不给予邀请人奖励（下同）。
                                    </div>
                                </td>
                            </tr>

                            <%--老用户是否参加--%>
                            <tr>
                                <td class="ab_left"><label class="title">老用户是否参加：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input id="oldUserIsInvolved" name="oldUserIsInvolved" style="float: left; margin-top:9px" type="checkbox" value="1" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--老用户奖励设置--%>
                            <tr class="reward-setting" style="display: none;">
                                <td class="ab_left"><label class="title">老用户奖励设置：<span class=""></span></label></td>
                            </tr>
                            <tr class="reward-setting" style="display: none;">
                                <td class="ab_main" colspan="6">
                                    <table id="oldUserPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 97%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:5%; text-align: center;">阶梯</th>
                                            <th style="width:13%; text-align: center;" class="cogItemMoney">达标次数范围最大值</th>
                                            <th style="width:9%; text-align: center;" class="cogItemMoney">奖励类型</th>
                                            <th style="width:25%; text-align: center;" class="cogItemMoney">每完成1次奖励范围</th>
                                            <th style="width:9%; text-align: center;" class="cogItemVpoints">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <%--阶梯--%>
                                            <td>
                                                <label id="NO" name="NO" style="line-height: 30px;">1</label>
                                                <input name="ladder" value="1" style="display: none" />
                                            </td>
                                            <%--达标人数--%>
                                            <td>
                                                <input type="text" name="numOfPeople" class="form-control input-width-small number integer maxValue"  autocomplete="off"
                                                       tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="6" style="display: initial; width: 132px !important;" />
                                            </td>
                                            <%--激励类型--%>
                                            <td>
                                                <span>现金</span>
                                            </td>
                                            <%--激励范围--%>
                                            <td>
                                                <div name="moneyCog" class="random-prize content">
                                                    <input type="text" name="minMoney" class="form-control input-width-small money maxValue"  autocomplete="off"
                                                           tag="validate" data-oldval="0.00" value="0.00" maxVal="999.99" maxlength="6" style="position: relative;left: 9%;width: 30% !important" />
                                                    <label style="position: relative;right: 35%;top: 3px">&nbsp;-&nbsp;</label>
                                                    <input type="text" name="maxMoney" class="form-control input-width-small money maxValue"  autocomplete="off"
                                                           tag="validate" data-oldval="0.00" value="0.00" maxVal="999.99" maxlength="6" style="position: relative;left: 23%;width: 30% !important" />
                                                </div>
                                            </td>
                                            <%--操作--%>
                                            <td>
                                                <label id="addPrizeItem" class="btn-txt-add-red">新增</label>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>

                            <%--邀请者与被邀请者绑定天数--%>
                            <tr>
                                <td class="ab_left" ><label class="title">邀请者与被邀请者绑定天数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="relationDays" value="1"  type="number" class="form-control input-width-larger integer minValue maxValue required"
                                               autocomplete="off" maxlength="5" minval="1" maxVal="365" tag="validate" />
                                        <span class="blocker en-larger"> 天（扫完邀请码后次日开始计算，只在此期间内扫码给予邀请者奖励）</span>
                                        <label class="validate_tips" style="width: 200px"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--每扫一次邀请码给予邀请者--%>
                            <tr>
                                <td class="ab_left" ><label class="title">每扫一次邀请码给予邀请者：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="incentiveNum" value="0"  type="number" class="form-control input-width-larger integer minValue maxValue required"
                                               autocomplete="off" maxlength="4" minval="0" maxVal="9999" tag="validate" />
                                        <span class="blocker en-larger"> 次奖励（0表示不限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>

                    <%--风控规则--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>邀请人风控规则</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <%--单人获得总次数上限制--%>
                            <tr>
                                <td class="ab_left" ><label class="title">单人获得总次数上限制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="maxIncentiveNum" type="number" class="form-control input-width-larger integer minValue maxValue required"
                                               autocomplete="off" value="0" maxlength="6" minval="0" maxVal="999999" tag="validate" />
                                        <span class="blocker en-larger"> 次（邀请者活动期间可获得奖励上限，0表示不限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--单人单日获得次数上限--%>
                            <tr>
                                <td class="ab_left" ><label class="title">单人单日获得次数上限：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="maxIncentiveNumDay" type="number" class="form-control input-width-larger integer minValue maxValue required"
                                               autocomplete="off" value="0" maxlength="6" minval="0" maxVal="999999" tag="validate" />
                                        <span class="blocker en-larger"> 次（邀请者每日可获得奖励上限，0表示不限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--邀请者与同一被邀请者只能产生--%>
                            <tr>
                                <td class="ab_left" ><label class="title">邀请者与同一被邀请者只能产生：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="maxIncentiveForInviter" type="number" class="form-control input-width-larger integer minValue maxValue required"
                                               autocomplete="off" value="0" maxlength="6" minval="0" maxVal="999999" tag="validate" />
                                        <span class="blocker en-larger"> 次奖励（0表示不限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <%--邀请者与同一被邀请者单日只能产生--%>
                            <tr>
                                <td class="ab_left" ><label class="title">邀请者与同一被邀请者单日只能产生：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="maxIncentiveForInviterDay" type="number" class="form-control input-width-larger integer minValue maxValue required"
                                               autocomplete="off" value="0" maxlength="6" minval="0" maxVal="999999" tag="validate" />
                                        <span class="blocker en-larger"> 次奖励（0表示不限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>

                    <%--邀请人参加的活动区域--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>邀请人参加的活动区域</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <%--区域类型--%>
                            <tr>
                                <td class="ab_left"><label class="title">区域类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" name="inviterAreaType" value="0" style="float: left;  margin-top: 12px; cursor: pointer;"  checked /> <span class="blocker en-larger" style="margin-left: 2px;"> 组织架构</span>
                                        <input type="radio" name="inviterAreaType" value="1" style="float: left;  margin-top: 12px; cursor: pointer;"  /><span class="blocker en-larger" style="margin-left: 2px;"> 终端分组</span>
                                    </div>
                                </td>
                            </tr>

                            <%--活动区域--%>
                            <tr class="region">
                                <td class="ab_left"><label class="title">活动区域：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div>
                                        <div style="float: left; margin-right: 10px;">
                                            <span>全选</span>
                                            <input id="allCheck" type="checkbox" tag="validate" style="float: left;">
                                        </div>
                                        <c:if test="${not empty(regionLst)}">
                                            <c:forEach items="${regionLst }" var="item">
                                                <div style="float: left; margin-right: 10px;">
                                                    <span>${item.region}</span>
                                                    <input type="checkbox" tag="validate" name="regionCog" value="${item.regionKey}" style="float: left;">
                                                </div>
                                            </c:forEach>
                                        </c:if>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr class="group" style="display: none;">
                                <td class="ab_left"><label class="title">活动区域：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main terminalGroupTdCog" colspan="3">
                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required terminalGroup" name="tGroup"
                                                tag="validate">
                                            <option value="">请选择门店分组</option>
                                            <c:forEach items="${groupLst}" var="item">
                                                <option value="${item.terminalGroup}">${item.terminalGroup}</option>
                                            </c:forEach>
                                        </select>
                                        <label class="title mart5 btn-txt-add-red addTerminalGroup"
                                               style="font-weight: normal; margin-left: 5px;" id="addTerminalGroup">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>

                    <%--邀请人费用限制--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>邀请人费用限制</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <%--活动总投放金额--%>
                            <tr>
                                <td class="ab_left"><label class="title"> 活动总投放金额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="money" type="number" value="0.00" class="form-control input-width-larger  number money preValue minValue maxValue required"
                                               autocomplete="off" minval="0" maxVal="999999999.99" maxlength="12" tag="validate" />
                                        <span class="blocker en-larger">元（0表示投放总金额为0元，最大金额为999,999,999.99元）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                        </table>
                    </div>

                    <%--被邀请人(消费者)--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>被邀请人(消费者)</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <%--消费者扫宣传海报提示弹窗--%>
                            <tr style="height: 163px;">
                                <td class="ab_left"><label class="title">消费者扫宣传海报提示弹窗：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">
                                        <section class="img-section" data-imgnum="4">
                                            <input id="fourImgUrl" name="prizeImgAry" type="hidden"
                                                   class='filevalue' />
                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">
                                                <section class="z_file fl">
                                                    <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">
                                                    <input type="file" name="file" id="file" class="file" value=""
                                                           accept="image/jpg,image/jpeg,image/png,image/bmp" multiple />
                                                </section>
                                            </div>
                                        </section>
                                    </div>
                                    <aside style="display: none;" class="mask works-mask">
                                        <div class="mask-content">
                                            <p class="del-p ">您确定要删除图片吗？</p>
                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span
                                                    class="wsdel-no">取消</span></p>
                                        </div>
                                    </aside>
                                </td>
                            </tr>

                            <%--消费者扫码是否在活动区域内强制绑定门店--%>
                            <tr>
                                <td class="ab_left"><label class="title">消费者扫码是否在活动区域内强制绑定门店：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" name="routineRebateSwitch" value="1" style="float: left;  margin-top: 12px; cursor: pointer;"  checked /> <span class="blocker en-larger" style="margin-left: 2px;"> 是</span>
                                        <input type="radio" name="routineRebateSwitch" value="0" style="float: left;  margin-top: 12px; cursor: pointer;"  /><span class="blocker en-larger" style="margin-left: 2px;"> 否</span>
                                    </div>
                                </td>
                            </tr>

                            <%--提示语--%>
                            <tr>
                                <td class="ab_left"><label class="title">消费者强制绑定门店提示语：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="prompt" tag="validate" class="form-control input-width-larger required"
                                               autocomplete="off" maxlength="50" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <%--被邀请人扫码区域配置(消费者)--%>
                    <div class="widget-header top-only">
                        <h4><i class="iconfont icon-xinxi"></i>被邀请人扫码区域配置(消费者)</h4>
                    </div>
                    <div class="widget-content panel no-padding filteruser">
                        <table class="active_board_table">
                            <tr id="shareAreaTr">
                                <td class="ab_left" style="display:table-cell; vertical-align:middle;"><label class="title">扫码区域选择：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input id="inviteeArea" name="inviteeArea" type="hidden" />
                                    <div class="content_wrap">
                                        <div class="zTreeDemoBackground left" >
                                            <ul id="tree" class="ztree" style="background-color: white;"></ul>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </div>

                    <%--保存与返回--%>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3" data-url="<%=cpath%>/vdhInvitation/showActivityList.do?vjfSessionId=${vjfSessionId}&isBegin=0">返 回</button>
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
