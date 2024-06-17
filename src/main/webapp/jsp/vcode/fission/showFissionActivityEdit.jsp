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
    <title>添加一码双奖活动</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
      <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
      <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
      <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
      <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
      <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
      <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	  <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
      <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>

      <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/demo.css" type="text/css">
      <link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
      <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.core.js"></script>
      <script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.excheck.js"></script>

      <link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
      <%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
      <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
      <script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>

	<script>
        var basePath='<%=cpath%>'
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
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
            var val = $("#isView").val();
            if (val==='1') {
                $(".form-control").attr("disabled", "disabled");
                $(".btnSave").attr("disabled", "disabled");
            }
            // $(".form-control").attr("disabled", "disabled");
			// 初始化功能
			initPage();
            $('.summernote').summernote({
                height: 200,
                tabsize: 1,
                lang: 'zh-CN'
            });

            let shareType = '${ActivateRedEnvelopeRuleCog.shareType}'
            if(shareType=='0'){
                $("#shareVcodeActivityKey").css("display", "none");
                $("#shareAreaTr").css("display", "none");
                $("#officialAccountFlagTr").css("display", "none");
                $("#shareVcodeActivityTr").css("display", "none");
                $("#officialAccountUrlTr").css("display", "none");
                $("#newUserTypeTr").css("display", "none");
                $(".shareAwardCogTr").css("display", "none");
                $(".shareRewardCogTr").show();
                $(".rewardReceiveLimitTr").show();
                $(".shareNewUserAwardCogTr").css("display", "none");
                $(".numberLimitTr").show();

                $(".sharerGetRewardDayLimitTr").css("display", "none");
                $(".sharerUserReceiveNumLimitTr").css("display", "none");
                $(".userReceiveNumberDayLimitTr").css("display", "none");
                $(".rickUserLimit").css("display", "none");
                $(".activityRuleTr").css("display", "none");
            }
            if(shareType=='1'){
                $("#shareVcodeActivityKey").show();
                $("#shareAreaTr").show();
                $("#officialAccountFlagTr")
                $("#shareVcodeActivityTr")
                $("#officialAccountUrlTr")
                $("#newUserTypeTr").css("display", "none");
                $(".shareAwardCogTr").css("display", "none");
                $(".rewardReceiveLimitTr").show();
                $(".shareAreaTitle").text("分享按钮指定区域");
                $(".shareNewUserAwardCogTr").css("display", "none");
                $(".numberLimitTr").show();

                $(".sharerGetRewardDayLimitTr").css("display", "none");
                $(".sharerUserReceiveNumLimitTr").css("display", "none");
                $(".userReceiveNumberDayLimitTr").css("display", "none");
                $(".rickUserLimit").css("display", "none");
                $(".activityRuleTr").css("display", "none");
            }
            if(shareType=='3'){
                $("#shareVcodeActivityKey").attr("disabled","disabled");
                $("#officialAccountFlagTr").css("display", "none");
                $("#shareVcodeActivityTr").css("display", "none");
                $("#officialAccountUrlTr").css("display", "none");
                $("#shareAreaTr").show();
                $("#newUserTypeTr").show();
                $(".shareRewardCogTr").css("display", "none");
                $(".rewardReceiveLimitTr").css("display", "none");
                $(".shareAreaTitle").text("活动区域");
                $(".shareAwardTypeCogTr").show();
                $(".shareNewUserAwardCogTr").show();
                $(".numberLimitTr").css("display", "none");
                $(".sharerGetRewardDayLimitTr").show();
                $(".sharerUserReceiveNumLimitTr").show();
                $(".userReceiveNumberDayLimitTr").show();
                $(".rickUserLimit").show();
                $(".activityRuleTr").show();
            }
 		});

		function initPage() {
            // 初始化模板状态显示样式
            $("#stateFlag").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='stateFlag']").val("1");
                    }else{
                        $("input:hidden[name='stateFlag']").val("0");
                    }
                }
            });
            // 初始化模板状态显示样式
            $("#shareFlag").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='shareFlag']").val("1");
                    }else{
                        $("input:hidden[name='shareFlag']").val("0");
                    }
                }
            });
            // 初始化模板状态显示样式
            $("#userRewardFlag").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='userRewardFlag']").val("1");
                        let rewardTyleVal = $("input[name='rewardType']:checked").val();
                        if(rewardTyleVal=='1'){
                            $(".pointRegion").val("0")
                            $(".pointRegion").attr("disabled","disabled");
                            $(".moneyRegion").removeAttr("disabled")
                        }else{
                            $(".moneyRegion").val("0.00")
                            $(".moneyRegion").attr("disabled","disabled");
                            $(".pointRegion").removeAttr("disabled")
                        }
                    }else{
                        $("input:hidden[name='userRewardFlag']").val("0");
                        $("input[name='pointRegion']").val("0");
                        $("input[name='moneyRegion']").val("0.00");
                        // $(".pointRegion").val("0")
                        // $(".pointRegion").attr("disabled","disabled");
                        // $(".moneyRegion").val("0.00")
                        // $(".moneyRegion").attr("disabled","disabled");
                    }
                }
            });
            // 初始化模板状态显示样式
            $("#officialAccountFlag").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='officialAccountFlag']").val("1");
                    }else{
                        $("input:hidden[name='officialAccountFlag']").val("0");
                    }
                }
            });
            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
            // 发货区域初始化
            $.fn.zTree.init($("#tree"), setting, zNodes);

            // 保存
			$(".btnSave").click(function(){

                var taskCompletionLimitDay = $("input[name='taskCompletionLimitDay']").val();
                var taskCompletionTimeLimit = $("input[name='taskCompletionTimeLimit']").val();
                if ((taskCompletionLimitDay == null || taskCompletionLimitDay == "") && (taskCompletionTimeLimit == null || taskCompletionTimeLimit == "")) {
                    $.fn.alert("请至少选择一种激活完成时间限制")
                    return false;
                }

                var acceptShareNewUserMinMoney = $("input[name='acceptShareNewUserMinMoney']").val();
                var acceptShareNewUserMaxMoney = $("input[name='acceptShareNewUserMaxMoney']").val();
                if((acceptShareNewUserMinMoney === "" && acceptShareNewUserMaxMoney !== "") || (acceptShareNewUserMinMoney !== "" && acceptShareNewUserMaxMoney === "")) {
                    $.fn.alert("被分享者为新用户时被分享者获得待激活的值应为区间")
                    return false;
                }

                var activationSkuKeyList = $("select[name='activationSkuKeyList']").val();
                if (activationSkuKeyList === "") {
                    $.fn.alert("请选择激活sku!")
                    return false;
                }


                var resultFlag = false;
                $("input[name='shareAwardType']:checked").each(function(i){
                    if ($(this).val() == '0') {
                        let totalNewUserPrizePercent = 0;
                        $("input[name='newUserPrizePercent']").each(function(i){
                            totalNewUserPrizePercent += parseInt($(this).val());
                        });
                        if (totalNewUserPrizePercent != 100) {
                            alert("新用户奖项配置中出占比不为100%");
                            resultFlag = true;
                        }
                    }

                    if ($(this).val() == '1') {
                        let totalOldUserPrizePercent = 0;
                        $("input[name='oldUserPrizePercent']").each(function(i){
                            totalOldUserPrizePercent += parseInt($(this).val());
                        });
                        if (totalOldUserPrizePercent != 100) {
                            alert("老用户奖项配置中出占比不为100%");
                            resultFlag = true;
                        }
                    }
                });

                if (resultFlag) {
                    return false;
                }

				var flag = validForm();

                var sHTML = $('.summernote').summernote('code');
                $('#roleDescribe').val(sHTML.trim());

                checkRule($("#activityKey").val());
                if(!flagStatus){
                    return false;
                }
                if(!stateFlagRule){
                    return false;
                }
				if (flag) {
					var url = $(this).attr("url");
					$.fn.confirm("确认发布？", function(){
			            $(".btnSave").attr("disabled", "disabled");
			            $("#divId").css("display","block");
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});
            rewardTypeValid();
            // 保存
            $("input[name='rewardType']").change(function(){
                rewardTypeValid();
            });


			// 增加SKU
            $("form").on("click", "#addShareSku", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addShareSku").text("删除");
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });
            // 增加SKU
            $("form").on("click", "#addActivationSku", function() {
                if ($(this).is("[disabled='disabled']")) return;
                if ($(this).text() == '新增') {
                    var $copySku = $(this).closest("div").clone(true, true);
                    $copySku.find("#addActivationSku").text("删除");
                    $(this).closest("td").append($copySku);

                } else {
                    $(this).closest("div").remove();
                }
            });
            $("input[name='shareType']").change(function(){
                let shareType = $("input[name='shareType']:checked").val();
                if(shareType=='0'){
                    $("#shareVcodeActivityKey").attr("disabled","disabled");
                    $("#shareAreaTr").attr("hidden","hidden");
                    $("#officialAccountFlagTr").attr("hidden","hidden");
                    $("#shareVcodeActivityTr").attr("hidden","hidden");
                    $("#officialAccountUrlTr").attr("hidden","hidden");
                }
                if(shareType=='1'){
                    $("#shareVcodeActivityKey").removeAttr("disabled","disabled");
                    $("#shareAreaTr").removeAttr("hidden","hidden");
                    $("#officialAccountFlagTr").removeAttr("hidden","hidden");
                    $("#shareVcodeActivityTr").removeAttr("hidden","hidden");
                    $("#officialAccountUrlTr").removeAttr("hidden","hidden");
                }
            });


            $("input[name='newUserType']").change(function(){
                let newUserType = $("input[name='newUserType']:checked").val();
                if(newUserType=='2'){
                    $("input[name='newUserAsk']").removeAttr("disabled")
                } else {
                    $("input[name='newUserAsk']").val('')
                    $("input[name='newUserAsk']").attr("disabled","disabled");
                }
            });


            // 检验名称是否重复
            $("input[name='ruleName']").on("blur",function(){
            	var ruleName = $("input[name='ruleName']").val();
            	var activityKey = $("input[name='activityKey']").val();
            	if(ruleName == "") return;
            	checkName(ruleName,activityKey);
            });

            $("input[name='newUserAsk']").on("blur", function(){
                var numReg = /^[0-9]*[1-9][0-9]*$/;
                if(!numReg.test($(this).val()) || $(this).val() < 0) {
                    $(this).val('1');
                }
            });

            $("input[name='taskCompletionTimeLimit']").on("blur", function(){
                var numReg = /^[0-9]*[1-9][0-9]*$/;
                if(!numReg.test($(this).val()) || $(this).val() < 0) {
                    $(this).val('1');
                }
            });

            $(".mpneyCog").on("blur", function(){
                var numReg = /^[0-9]+(.[0-9]{2})?$/;
                if(!numReg.test($(this).val()) || $(this).val() < 0) {
                    $(this).val('');
                }
            });

            // 新增奖项
            $("form").on("click", "#addPrizeItem", function(){
                if ($(this).text() == '新增') {
                    var $cloneItem = $(this).closest("tr").clone(true, true);
                    $cloneItem.find("#moneyCog").show();
                    $cloneItem.find("input[name='newUserPrizeMinMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='newUserPrizeMaxMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='oldUserPrizeMinMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='oldUserPrizeMaxMoney']").data("oldval", "0.00").val("0.00");
                    $cloneItem.find("input[name='oldUserPrizePercent']").val("0");
                    $cloneItem.find("input[name='newUserPrizePercent']").val("0");
                    $cloneItem.find("#addPrizeItem").text("删除");
                    $(this).closest("tbody").append($cloneItem);
                } else {
                    $(this).closest("tr").remove();
                }

                $(this).closest("tbody").find("tr").each(function(i, obj){
                    $(this).find("#NO").text(i+1);
                    $(this).find("#newUserPrizeNo").attr("value",i+1);
                    $(this).find("#oldUserPrizeNo").attr("value",i+1);
                });
            });

            $("[name='cashPrize']").on("change", function () {
                let cashPrize = $("input[name='cashPrize']:checked").val();
                if (cashPrize == '0') {
                    $("#taskCompletionLimitDay").css("display", "none");
                    $("input[name='taskCompletionLimitDay']").hide();
                    $("input[name='taskCompletionLimitDay']").val('');
                    $("input[name='taskCompletionTimeLimit']").show();
                }
                if (cashPrize == '1') {
                    $("#taskCompletionTimeLimit").css("display", "none");
                    $("#taskCompletionTimeLimit").val(null);
                    $(this).closest("div").find("input[name='taskCompletionLimitDay']").show();
                }
            });

            $("[name='shareAwardType']").on("click", function () {
                $(".newUserPrizeCog").css("display", "none");
                $(".oldUserPrizeCog").css("display", "none");
                $("input[name='shareAwardType']:checked").each(function(i){
                    if ($(this).val() == '0') {
                        $(".newUserPrizeCog").show();
                    }
                    if ($(this).val() == '1') {
                        $(".oldUserPrizeCog").show();
                    }
                });
            });

		}
        var stateFlagRule = false;
        function checkRule(activityKey){
            $.ajax({
                url : "${basePath}/activateRedEnvelopeRuleCog/checkRule.do",
                data:{
                    "ruleKey":activityKey
                },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    if (data == "0") {
                        $.fn.alert("中出规则未配置！请配置完成后修改!");
                        stateFlagRule = false;
                    } else if (data == "1") {
                        stateFlagRule = true;
                    }
                }
            });
        }

		function rewardTypeValid() {
            let rewardTyleVal = $("input[name='rewardType']:checked").val();
            if(rewardTyleVal=='1'){
                $(".pointRegion").val("0")
                $(".pointRegion").attr("disabled","disabled");
                $(".moneyRegion").removeAttr("disabled")
            }else{
                $(".moneyRegion").val("0.00")
                $(".moneyRegion").attr("disabled","disabled");
                $(".pointRegion").removeAttr("disabled")
            }
        }
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(bussionName,activityKey){
			$.ajax({
				url : "${basePath}/activateRedEnvelopeRuleCog/checkName.do",
			    data:{
			    		"activityKey":activityKey,
			    		"checkName":bussionName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    if (data == "0") {
                        $.fn.alert("活动名称已存在，请重新输入");
                        flagStatus = false;
                    } else if (data == "1") {
                        flagStatus = true;
                    }
                }
		  	});
		}

        var saveFlag = "";
        function checkSave(skuList,activateKeyList,startDate,endDate,shareType){
            $.ajax({
                url : "${basePath}/activateRedEnvelopeRuleCog/checkSave.do",
                data:{
                    "ruleKey":$('#activityKey').val(),
                    "skuList":skuList,
                    "activateKeyList":activateKeyList,
                    "startDate":startDate,
                    "endDate":endDate,
                    "shareType":shareType
                },
                type : "POST",
                dataType : "json",
                async : false,
                beforeSend: appendVjfSessionId,
                success: function (data) {
                    saveFlag = data;
                }
            });
        }

		function validForm() {
			// 检验名称是否重复
			var ruleName = $("input[name='ruleName']").val();
			var activityKey = $("input[name='activityKey']").val();
        	if(ruleName == "") return false;
        	checkName(ruleName,activityKey);
        	if(!flagStatus){
                $.fn.alert("活动名称已存在，请重新输入");
        		return false;
        	}
        	//检验活动与sku是否有冲突
            var startDate = $("input[name='startDate']").val();
            var endDate = $("input[name='endDate']").val();
            var shareType = $("input[name='shareType']:checked").val();
            var d = {};
            var t = $('form').serializeArray();
            var a = []
            var b = []
            $.each(t, function() {
                if(this.name=='activationSkuKeyList'){
                    a.push(this.value)
                    d[this.name] = a;
                }if(this.name=='shareVcodeActivityKey') {
                    b.push(this.value)
                    d[this.name] = b;
                }else{
                    d[this.name] = this.value;
                }
            });
            let skuList  = a.join(',');
            let activityList  = b.join(',');
            checkSave(skuList,activityList,startDate,endDate,shareType);
            if (saveFlag == '2') {
                $.fn.alert("活动有效期内sku有冲突，请修改后继续添加！");
                return false;
            }
            if (saveFlag == '1') {
                $.fn.alert("活动有效期关联活动有冲突，请修改后继续添加！");
                return false;
            }
            if( $("input:hidden[name='userRewardFlag']").val()=='1'){
                let rewardTyleVal = $("input[name='rewardType']:checked").val();
                if(rewardTyleVal=='1'){
                    let moneyRegionMin =  $("#moneyRegionMin").val();
                    let moneyRegionMax =  $("#moneyRegionMax").val();
                    let pointRegionMin =  $("#pointRegionMin").val("");
                    let pointRegionMax =  $("#pointRegionMax").val("");
                    if(!moneyRegionMin || !moneyRegionMax){
                        $.fn.alert("返利规则现金不能为空")
                        return false;
                    }
                    if(moneyRegionMin > moneyRegionMax){
                        $.fn.alert("返利规则现金，前一个数值不能大于后一个")
                        return false;
                    }
                }else{
                    let pointRegionMin =  $("#pointRegionMin").val();
                    let pointRegionMax =  $("#pointRegionMax").val();
                    let moneyRegionMin =  $("#moneyRegionMin").val("");
                    let moneyRegionMax =  $("#moneyRegionMax").val("");
                    if(!pointRegionMin || !pointRegionMax){
                        $.fn.alert("返利规则积分不能为空")
                        return false;
                    }
                    if(pointRegionMin > pointRegionMax){
                        $.fn.alert("返利规则积分，前一个数值不能大于后一个")
                        return false;
                    }
                }

            }
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
            // 获取发货地区
            var treeObj = $.fn.zTree.getZTreeObj("tree");
            var nodes = treeObj.getCheckedNodes(true);
            if (shareType == '3' ) {
                if (nodes == '') {
                    $.fn.alert("请选择活动区域");
                    return false;
                }

                if (skuList == null || skuList == "") {
                    $.fn.alert("请选择激活sku");
                    return false;
                }
                var riskUserLimitMinMoney = $("input[name='riskUserLimitMinMoney']").val();
                var riskUserLimitMaxMoney = $("input[name='riskUserLimitMaxMoney']").val();
                if (riskUserLimitMinMoney == null || riskUserLimitMinMoney == '' || riskUserLimitMaxMoney == null || riskUserLimitMaxMoney == '') {
                    $.fn.alert("请输入风险用户待激活配置");
                    return false;
                }
            }

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
                $("#shareArea").val(areaStr);
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
        a {
            text-decoration: none;
        }
	</style>
  </head>

  <body>
    <div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>筛选用户中,请勿其他操作.....</b></h2>
    </div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
        	<li class="current"><a> 活动管理</a></li>
        	<li class="current"><a> 分享裂变活动配置</a></li>
            <li class="current"><a title="">修改活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/activateRedEnvelopeRuleCog/updateActivityCog.do">
            	<input type="hidden" name="companyKey" value="${companyKey}" />
            	<input type="hidden" name="activityKey" id="activityKey" value="${ActivateRedEnvelopeRuleCog.activityKey}"/>
            	<input type="hidden" name="isView" id="isView" value="${isView}"/>
                <input type="hidden" name="limitType" value="0" style="float:left;" />
                <input type="hidden" name="roleDescribe" id="roleDescribe" value="">
                <input type="hidden" name="shareType" value="${ActivateRedEnvelopeRuleCog.shareType}">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>修改活动</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="ruleName" tag="validate" value="${ActivateRedEnvelopeRuleCog.ruleName}"
	                       					class="form-control required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">日期范围：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="startDate" id="startDate" class="form-control input-width-medium Wdate required preTime" value="${ActivateRedEnvelopeRuleCog.startDate}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({realDateFmt:'yyyy-MM-dd'})"  />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime" value="${ActivateRedEnvelopeRuleCog.endDate}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({realDateFmt:'yyyy-MM-dd'})" />
                                        <span class="blocker en-larger">（整个活动持续时间）</span>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title"> 分享类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" disabled name="shareType" value="1" style="float: left;  margin-top: 12px; cursor: pointer;"  <c:if test="${ActivateRedEnvelopeRuleCog.shareType=='1'}">checked</c:if> > <span class="blocker en-larger" style="margin-left: 2px;">二维码分享</span>
                                        <input type="radio" disabled name="shareType" value="0" style="float: left;  margin-top: 12px; cursor: pointer;"  <c:if test="${ActivateRedEnvelopeRuleCog.shareType=='0'}">checked</c:if>  ><span class="blocker en-larger" style="margin-left: 2px;"> 海报分享（B激活给予A奖励）</span>
                                        <input type="radio" disabled name="shareType" value="3" style="float: left;  margin-top: 12px; cursor: pointer;"  <c:if test="${ActivateRedEnvelopeRuleCog.shareType=='3'}">checked</c:if>  ><span class="blocker en-larger" style="margin-left: 2px;"> 海报分享（B激活给予A待激活红包）</span>
                                        <%--                                        <input type="radio" name="shareType" value="2" style="float: left;  margin-top: 12px; cursor: pointer;"  checked ><span class="blocker en-larger" style="margin-left: 2px;"> 海报、二维码同时分享</span>--%>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"> 激活积分红包分享按钮开关：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="shareFlag" type="hidden" value="${ActivateRedEnvelopeRuleCog.shareFlag}" />
                                        <input id="shareFlag" type="checkbox" <c:if test="${ActivateRedEnvelopeRuleCog.shareFlag eq '1'}">checked</c:if> data-size="small" data-on-text="开" data-off-text="关" data-on-color="info" data-off-color="danger"/>
                                    </div>
                                    <label class="blocker en-larger">（开关开启后用户可以通过按钮分享给好友）</label>
                                </td>
                            </tr>

                            <tr id="newUserTypeTr" <c:if test="${ActivateRedEnvelopeRuleCog.shareType ne '3'}"> style="display: none"</c:if>>
                                <td class="ab_left"><label class="title"> 新用户类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" name="newUserType" value="0" style="float: left;  margin-top: 12px; cursor: pointer;" <c:if test="${ActivateRedEnvelopeRuleCog.newUserType=='0'}">checked</c:if> > <span class="blocker en-larger" style="margin-left: 2px;">当年未在本省区扫码</span>
                                        <input type="radio" name="newUserType" value="1" style="float: left;  margin-top: 12px; cursor: pointer;" <c:if test="${ActivateRedEnvelopeRuleCog.newUserType=='1'}">checked</c:if> ><span class="blocker en-larger" style="margin-left: 2px;"> 累计未在本省区扫码</span>
                                        <input type="radio" name="newUserType" value="2" style="float: left;  margin-top: 12px; cursor: pointer;" <c:if test="${ActivateRedEnvelopeRuleCog.newUserType=='2'}">checked</c:if> >
                                        <input id="newUserAsk" name="newUserAsk" tag="validate" value="${ActivateRedEnvelopeRuleCog.newUserAsk}" <c:if test="${ActivateRedEnvelopeRuleCog.newUserType ne '2'}">disabled</c:if>
                                               class="form-control-small required maxValue" autocomplete="off" maxlength="6" style="float: left;  margin-top: 8px; cursor: pointer;" data-oldval="" maxVal="999"/>
                                        <span class="blocker en-larger">天内未在本省区扫码</span>
                                        <%--                                        <input type="radio" name="shareType" value="2" style="float: left;  margin-top: 12px; cursor: pointer;"  checked ><span class="blocker en-larger" style="margin-left: 2px;"> 海报、二维码同时分享</span>--%>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title"> 分享者激励开关：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userRewardFlag" type="hidden" value="${ActivateRedEnvelopeRuleCog.userRewardFlag}" />
                                        <input id="userRewardFlag" type="checkbox" <c:if test="${ActivateRedEnvelopeRuleCog.userRewardFlag eq '1'}">checked</c:if> data-size="small" data-on-text="开" data-off-text="关" data-on-color="info" data-off-color="danger"/>
                                    </div>
                                    <label class="blocker en-larger">（开关开启后被分享的用户完成扫码任务后分享者可得中出红包）</label>
                                </td>
                            </tr>

                            <tr class="shareRewardCogTr">
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="show_content content">
                                        <input type="radio" class="tab-radio" name="rewardType" value="1" style="float:left;" <c:if test="${ActivateRedEnvelopeRuleCog.rewardType=='1'}">checked</c:if> />
                                        <span class="blocker en-larger">现金</span>
                                        <input  id = "moneyRegionMin" name="moneyRegion"   value="${fn:split(ActivateRedEnvelopeRuleCog.moneyRegion, ',')[0]}"
                                               class="form-control input-width-small preValue money minValue maxValue moneyRegion" style="float:left;" autocomplete="off"  minVal="0" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">元</label>
                                        <span class="blocker en-larger">-</span>
                                        <input  id = "moneyRegionMax" name="moneyRegion"   value="${fn:split(ActivateRedEnvelopeRuleCog.moneyRegion, ',')[1]}"
                                               class="form-control input-width-small sufValue money minValue maxValue moneyRegion" style="float:left;"  autocomplete="off"  minVal="0" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">元</label>
                                        <label class="validate_tips"></label>
                                    </div>

                                </td>
                            </tr>

                            <tr class="shareRewardCogTr">
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="show_content content">
                                        <input type="radio" class="tab-radio" name="rewardType" value="0" style="float:left;" <c:if test="${ActivateRedEnvelopeRuleCog.rewardType=='0'}">checked</c:if>/>
                                        <span class="blocker en-larger">积分</span>
                                        <input id = "pointRegionMin" name="pointRegion"    value="${fn:split(ActivateRedEnvelopeRuleCog.pointRegion, ',')[0]}"
                                               class="form-control input-width-small number integer  preValue minValue maxValue  pointRegion" style="float:left;"  autocomplete="off"  minVal="0" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">积分</label>
                                        <span class="blocker en-larger">-</span>
                                        <input id = "pointRegionMax" name="pointRegion"    value="${fn:split(ActivateRedEnvelopeRuleCog.pointRegion, ',')[1]}"
                                               class="form-control input-width-small number integer  sufValue minValue maxValue  pointRegion" style="float:left;"  autocomplete="off"  minVal="0" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">积分</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr class="shareAwardCogTr shareAwardTypeCogTr"  <c:if test="${ActivateRedEnvelopeRuleCog.shareType ne '3'}"> style="display: none"</c:if>>
                                <td class="ab_left"><label class="title"> 分享者激励：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; margin-right: 10px;">
                                        <input name="shareAwardType" value="0" <c:if test="${fn:contains(ActivateRedEnvelopeRuleCog.shareAwardType, '0')}">checked</c:if> type="checkbox" tag="validate" style="float: left;  margin-top: 12px; cursor: pointer;"><span class="blocker en-larger" style="margin-left: 2px;"> 分享给新用户</span>
                                    </div>
                                </td>
                            </tr>

                            <tr class="shareAwardCogTr newUserPrizeCog" <c:if test="${!fn:contains(ActivateRedEnvelopeRuleCog.shareAwardType, '0')}">style="display: none"</c:if>>
                                <td class="ab_main" colspan="3">
                                    <table id="newUserPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 50%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:10%; text-align: center;">序号</th>
                                            <th style="width:50%; text-align: center;" class="cogItemMoney">待激活积分红包范围(元)</th>
                                            <th style="width:20%; text-align: center;" class="cogItemVpoints">中出占比</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:if test="${not empty newUserPrizeCogList}">
                                            <c:forEach items="${newUserPrizeCogList}" var="item">
                                                <tr>
                                                    <td>
                                                        <leabl id="NO" style="line-height: 30px;">${item.prizeNo}</leabl>
                                                        <input id="newUserPrizeNo" name="newUserPrizeNo" value="${item.prizeNo}" style="display: none">
                                                    </td>
                                                    <td>
                                                        <div id="moneyCog" class="random-prize content">
                                                            <input type="text" name="newUserPrizeMinMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                                   tag="validate" data-oldval="0.00" value="${item.minMoney}" maxVal="9999.99" maxlength="6" style="position: relative;left: 15%; width: 30% !important">
                                                            <label style="position: relative;right: 25%;top: 3px;">&nbsp;-&nbsp;</label>
                                                            <input type="text" name="newUserPrizeMaxMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                                   tag="validate" data-oldval="0.00" value="${item.maxMoney}" maxVal="9999.99" maxlength="6" style="position: relative;left: 35%; width: 30% !important">
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="newUserPrizePercent" class="form-control input-width-small number integer maxValue newUserPrizePercent"  autocomplete="off"
                                                               tag="validate" data-oldval="0" value="${item.prizePercent}" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                                        <label style="line-height: 30px;">%</label>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${item.prizeNo eq '1'}"> <label id="addPrizeItem" class="btn-txt-add-red">新增</label> </c:when>
                                                            <c:otherwise> <label id="addPrizeItem" class="btn-txt-add-red">删除</label> </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${empty newUserPrizeCogList}">
                                            <tr>
                                                <td>
                                                    <leabl id="NO" style="line-height: 30px;">1</leabl>
                                                    <input id="newUserPrizeNo" name="newUserPrizeNo" value="1" style="display: none">
                                                </td>
                                                <td>
                                                    <div id="moneyCog" class="random-prize content">
                                                        <input type="text" name="newUserPrizeMinMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="position: relative;left: 15%; width: 30% !important">
                                                        <label style="position: relative;right: 25%;top: 3px;">&nbsp;-&nbsp;</label>
                                                        <input type="text" name="newUserPrizeMaxMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="7" style="position: relative;left: 35%; width: 30% !important">
                                                    </div>
                                                </td>
                                                <td>
                                                    <input type="text" name="newUserPrizePercent" class="form-control input-width-small number integer maxValue newUserPrizePercent"  autocomplete="off"
                                                           tag="validate" data-oldval="0" value="" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                                    <label style="line-height: 30px;">%</label>
                                                </td>
                                                <td>
                                                    <label id="addPrizeItem" class="btn-txt-add-red">新增</label>
                                                </td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>

                            <tr class="shareAwardCogTr shareAwardTypeCogTr" <c:if test="${ActivateRedEnvelopeRuleCog.shareType ne '3'}"> style="display: none"</c:if>>
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; margin-right: 10px;">
                                        <input name="shareAwardType"  value="1" type="checkbox" tag="validate"  <c:if test="${fn:contains(ActivateRedEnvelopeRuleCog.shareAwardType, '1')}">checked</c:if> style="float: left;  margin-top: 12px; cursor: pointer;"><span class="blocker en-larger" style="margin-left: 2px;"> 分享给老用户</span>
                                    </div>
                                </td>
                            </tr>

                            <tr class="shareAwardCogTr oldUserPrizeCog" <c:if test="${!fn:contains(ActivateRedEnvelopeRuleCog.shareAwardType, '1')}">style="display: none"</c:if>>
                                <td class="ab_main" colspan="3">
                                    <table id="oldUserPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 50%; margin: 0 auto; text-align: center; overflow: auto !important">
                                        <thead>
                                        <tr>
                                            <th style="width:10%; text-align: center;">序号</th>
                                            <th style="width:50%; text-align: center;" class="cogItemMoney">待激活积分红包范围(元)</th>
                                            <th style="width:20%; text-align: center;" class="cogItemVpoints">中出占比</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:if test="${not empty oldUserPrizeCogList}">
                                            <c:forEach items="${oldUserPrizeCogList}" var="item">
                                                <tr>
                                                    <td>
                                                        <leabl id="NO" style="line-height: 30px;">${item.prizeNo}</leabl>
                                                        <input id="oldUserPrizeNo" name="oldUserPrizeNo" value="${item.prizeNo}" style="display: none">
                                                    </td>
                                                    <td>
                                                        <div id="moneyCog" class="random-prize content">
                                                            <input type="text" name="oldUserPrizeMinMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                                   tag="validate" data-oldval="0.00" value="${item.minMoney}" maxVal="9999.99" maxlength="6" style="position: relative;left: 15%; width: 30% !important">
                                                            <label style="position: relative;right: 25%;top: 3px;">&nbsp;-&nbsp;</label>
                                                            <input type="text" name="oldUserPrizeMaxMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                                   tag="validate" data-oldval="0.00" value="${item.maxMoney}" maxVal="9999.99" maxlength="6" style="position: relative;left: 35%; width: 30% !important">
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <input type="text" name="oldUserPrizePercent" class="form-control input-width-small number integer maxValue oldUserPrizePercent"  autocomplete="off"
                                                               tag="validate" data-oldval="0" value="${item.prizePercent}" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                                        <label style="line-height: 30px;">%</label>
                                                    </td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${item.prizeNo eq '1'}"> <label id="addPrizeItem" class="btn-txt-add-red">新增</label> </c:when>
                                                            <c:otherwise> <label id="addPrizeItem" class="btn-txt-add-red">删除</label> </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:if>
                                        <c:if test="${empty oldUserPrizeCogList}">
                                            <tr>
                                                <td>
                                                    <leabl id="NO" style="line-height: 30px;">1</leabl>
                                                    <input id="oldUserPrizeNo" name="oldUserPrizeNo" value="1" style="display: none">
                                                </td>
                                                <td>
                                                    <div id="moneyCog" class="random-prize content">
                                                        <input type="text" name="oldUserPrizeMinMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="6" style="position: relative;left: 15%; width: 30% !important">
                                                        <label style="position: relative;right: 25%;top: 3px;">&nbsp;-&nbsp;</label>
                                                        <input type="text" name="oldUserPrizeMaxMoney" class="form-control money maxValue mpneyCog"  autocomplete="off"
                                                               tag="validate" data-oldval="0.00" value="0.00" maxVal="9999.99" maxlength="6" style="position: relative;left: 35%; width: 30% !important">
                                                    </div>
                                                </td>
                                                <td>
                                                    <input type="text" name="oldUserPrizePercent" class="form-control input-width-small number integer maxValue oldUserPrizePercent"  autocomplete="off"
                                                           tag="validate" data-oldval="0" value="" maxVal="999999" maxlength="6" style="display: initial; width: 60px !important;">
                                                    <label style="line-height: 30px;">%</label>
                                                </td>
                                                <td>
                                                    <label id="addPrizeItem" class="btn-txt-add-red">新增</label>
                                                </td>
                                            </tr>
                                        </c:if>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>

                            <tr class="shareNewUserAwardCogTr" style="display: none">
                                <td class="ab_left"><label class="title"> </label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="show_content content">
                                        <span class="blocker en-larger">被分享者为新用户时被分享者获得待激活:</span>
                                        <input id = "acceptShareNewUserMinMoney" name="acceptShareNewUserMinMoney"   value="${ActivateRedEnvelopeRuleCog.acceptShareNewUserMinMoney}" tag="validate"
                                               class="form-control  preValue money minValue maxValue input-width-small  moneyRegion " style="float:left;" autocomplete="off" minVal="0.01" maxVal="9999.99" maxlength="10" />
                                        <label class="blocker en-larger">元</label>
                                        <span class="blocker en-larger">-</span>
                                        <input id = "acceptShareNewUserMaxMoney" name="acceptShareNewUserMaxMoney"   value="${ActivateRedEnvelopeRuleCog.acceptShareNewUserMaxMoney}" tag="validate"
                                               class="form-control sufValue money minValue maxValue input-width-small  moneyRegion " style="float:left;"  autocomplete="off" minVal="0.01" maxVal="9999.99" maxlength="10" />
                                        <label class="blocker en-larger">元</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>


                            <tr id="officialAccountFlagTr">
                                <td class="ab_left"><label class="title">关注公众号开关：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="officialAccountFlag" type="hidden" value="${ActivateRedEnvelopeRuleCog.officialAccountFlag}" />
                                        <input id="officialAccountFlag" type="checkbox" <c:if test="${ActivateRedEnvelopeRuleCog.officialAccountFlag eq '1'}">checked</c:if> data-size="small" data-on-text="开" data-off-text="关" data-on-color="info" data-off-color="danger"/>
                                    </div>
                                    <label class="blocker en-larger">（开关开启后再分享裂变的流程中加入关注公众号的流程）</label>
                                </td>
                            </tr>
                            <tr id="officialAccountUrlTr">
                                <td class="ab_left"><label class="title">公众号链接：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="officialAccountUrl"  value="${ActivateRedEnvelopeRuleCog.officialAccountUrl}"
                                               class="form-control " autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr class="rewardReceiveLimitTr">
                                <td class="ab_left"><label id="rewardReceiveLimit" class="title">激励红包门槛配置：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker en-larger">分享者获得激励红包需</span>
                                        <input name="rewardReceiveLimit" value="${ActivateRedEnvelopeRuleCog.rewardReceiveLimit}"   
                                               class="form-control input-width-small integer required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">人完成激活红包任务（输入0表示无需被分享者完成激活红包任务）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" ><label id="sharerGetRewardLimit" class="title">每个分享者领取激励总次数上限<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sharerGetRewardLimit" value="${ActivateRedEnvelopeRuleCog.sharerGetRewardLimit}"   
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">个（用户分享激励个数，超出分享激励个数后不再给激励红包，输入0表示无限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>


                            <tr class="sharerGetRewardDayLimitTr">
                                <td class="ab_left" ><label id="sharerGetRewardDayLimit" class="title">每个分享者单日领取激励总次数上限<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sharerGetRewardDayLimit" value="${ActivateRedEnvelopeRuleCog.sharerGetRewardDayLimit}"  
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">次（用户分享激励个数，超出分享激励个数后不再给激励红包，输入0表示无限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr class="sharerUserReceiveNumLimitTr">
                                <td class="ab_left" ><label id="sharerGetRewardUserLimit" class="title">在同一分享者里只能领<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sharerUserReceiveNumLimit" value="${ActivateRedEnvelopeRuleCog.sharerUserReceiveNumLimit}"  
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">次待激活</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label id="receiveNumberTitle" class="title">单人每日分享可让：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="receiveNumber" value="${ActivateRedEnvelopeRuleCog.receiveNumber}"  
                                               class="form-control input-width-larger thousand required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">个人领取</span>
                                        <label class="validate_tips"></label>

                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label id="receiveTimeLimitTitle" class="title">分享后领取时间限制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="receiveTimeLimit" value="${ActivateRedEnvelopeRuleCog.receiveTimeLimit}"  
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">天（自分享之时加上限制时间，时间过期分享链接失效，如果输入0天表示无限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">激活完成时间限制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="cashPrize" value="0"
                                               <c:if test="${not empty ActivateRedEnvelopeRuleCog.taskCompletionTimeLimit}">checked="checked"</c:if>
                                               style="float:left; margin-left: 20px !important;"  />
                                        <span class="blocker en-larger" style="margin-left: 2px;">中出后有效天数</span>
                                        <input name="taskCompletionTimeLimit" id="taskCompletionTimeLimit"
                                               value="${ActivateRedEnvelopeRuleCog.taskCompletionTimeLimit}"
                                               placeholder="0表示不限制"  maxVal="365" data-oldval=""  tag="validate"
                                               <c:if test="${empty ActivateRedEnvelopeRuleCog.taskCompletionTimeLimit}">style="display: none" </c:if>
                                               class="form-control input-width-larger required maxValue" autocomplete="off"/>
                                        <input type="radio" class="tab-radio" name="cashPrize" value="1"
                                               <c:if test="${not empty ActivateRedEnvelopeRuleCog.taskCompletionLimitDay}">checked="checked"</c:if>
                                               style="float:left;"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">固定截止时间</span>
                                        <input name="taskCompletionLimitDay" id="taskCompletionLimitDay"
                                               class="form-control input-width-medium Wdate required preTime"
                                               value="${ActivateRedEnvelopeRuleCog.taskCompletionLimitDay}"
                                               tag="validate" style="width: 180px !important;<c:if test="${empty ActivateRedEnvelopeRuleCog.taskCompletionLimitDay}">display:none;</c:if>" autocomplete="off"
                                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label id="userReceiveNumberLimitTitle" class="title">每个被分享者领取待激活总次数上限:<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userReceiveNumberLimit"  value="${ActivateRedEnvelopeRuleCog.userReceiveNumberLimit}"  
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">个（用户超出可领取个数后，再领取不获得激活积分红包，如果输入0表示无限制，领取过激活积分红包未完成任务也算一个）</span>
                                        <label class="validate_tips" style="display: none"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="userReceiveNumberDayLimitTr">
                                <td class="abt_left"><label id="userReceiveNumberDayLimitTitle" class="title">每个被分享者单日领取待激活总次数上限：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userReceiveNumberDayLimit" value="${ActivateRedEnvelopeRuleCog.userReceiveNumberDayLimit}"  
                                               class="form-control input-width-larger integer required" autocomplete="off"  maxlength="5" tag="validate"/>
                                        <span class="blocker en-larger">个（用户当天超出可领取个数后，再领取不获得激活积分红包，如果输入0表示无限制，领取过激活积分红包未完成任务也算一个）</span>
                                        <label class="validate_tips" style="display: none"></label>
                                    </div>
                                </td>
                            </tr>


                            <tr id="shareVcodeActivityTr">
                                <td class="ab_left"><label class="title">分享活动：<span class="required" id="shareActivityRequired">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <c:choose>
                                        <c:when test="${fn:length(ActivateRedEnvelopeRuleCog.shareVcodeActivityKeyArray) gt 0}">
                                            <c:forEach items="${ActivateRedEnvelopeRuleCog.shareVcodeActivityKeyArray}" var="vcodeActivityKey" varStatus="idx">
                                                <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                    <select class="form-control input-width-larger required"  name="shareVcodeActivityKey" tag="validate">
                                                        <option value="">请选择活动</option>
                                                        <c:if test="${!empty shareActivityKeyList}">
                                                            <c:forEach items="${shareActivityKeyList}" var="activity">
                                                                <option value="${activity.vcodeActivityKey}"  <c:if test="${vcodeActivityKey == activity.vcodeActivityKey}"> selected</c:if>>${activity.vcodeActivityName}</option>
                                                            </c:forEach>
                                                        </c:if>
                                                    </select>
                                                    <span class="blocker en-larger">（此活动内的sku可以进行激活积分红包活动分享）</span>
                                                    <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addActivationSku">${idx.count == 1 ? '新增' : '删除'}</label>
                                                    <label class="validate_tips"></label>
                                                </div>
                                            </c:forEach>
                                        </c:when>
                                        <c:otherwise>
                                            <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                <select class="form-control input-width-larger required"  name="shareVcodeActivityKey" tag="validate">
                                                    <option value="">请选择活动</option>
                                                    <c:if test="${!empty shareActivityKeyList}">
                                                        <c:forEach items="${shareActivityKeyList}" var="activity">
                                                            <option value="${activity.vcodeActivityKey}" >${activity.vcodeActivityName}</option>
                                                        </c:forEach>
                                                    </c:if>
                                                </select>
                                                <span class="blocker en-larger">（此活动内的sku可以进行激活积分红包活动分享）</span>
                                                <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addActivationSku">新增</label>
                                                <label class="validate_tips"></label>
                                            </div>
                                        </c:otherwise>
</c:choose>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">激活SKU：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <c:forEach items="${ActivateRedEnvelopeRuleCog.activationSkuKeyArray}" var="skuKey" varStatus="idx">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="activationSkuKeyList" tag="validate">
                                            <option value="">请选择SKU</option>
                                            <c:if test="${!empty skuList}">
                                                <c:forEach items="${skuList}" var="sku">
                                                    <option value="${sku.skuKey}" <c:if test="${skuKey == sku.skuKey}"> selected</c:if> data-img="${sku.skuLogo}" >${sku.skuName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <span class="blocker en-larger">（挂接此sku可以完成激活积分红包任务）</span>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px; color:green" id="addShareSku">${idx.count == 1 ? '新增' : '删除'}</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr id="shareAreaTr">
                                <td  class="ab_left shareAreaTitle" style="display:table-cell; vertical-align:middle;"><label class="title">分享按钮指定区域：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input id="shareArea" name="shareArea" type="hidden">
                                    <div class="content_wrap">
                                        <div class="zTreeDemoBackground left" >
                                            <ul id="tree" class="ztree" style="background-color: white;"></ul>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                            <tr class="activityRuleTr">
                                <td class="ab_left"><label class="title">活动规则：<span class="required"></span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="summernote">
                                        ${ActivateRedEnvelopeRuleCog.roleDescribe}
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="stateFlag" type="hidden" class="form-control" value="${ActivateRedEnvelopeRuleCog.stateFlag}" />
                                        <input id="stateFlag" type="checkbox"  class="form-control" <c:if test="${ActivateRedEnvelopeRuleCog.stateFlag eq '1'}">checked</c:if> data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                    <label class="blocker en-larger">（停用期间不可分享和不中出积分红包，已领取的可以完成任务，停用期间扫的二维码启用后也不可分享）</label>
                                </td>
                            </tr>

                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>限制规则</h4>
                    </div>
                	<div class="widget-content panel no-padding filteruser">
                		<table class="active_board_table">
<%--                            <tr>--%>
<%--                                <td class="ab_left"><label class="title"> 限制类型：<span class="required">*</span></label></td>--%>
<%--                                <td class="ab_main" colspan="3">--%>
<%--                                    <div class="content">--%>
<%--                                        <input type="radio" class="tab-radio" name="limitType" value="0" style="float:left;"   <c:if test="${ActivateRedEnvelopeRuleCog.limitType eq '0' }"> checked="checked" </c:if>/>--%>
<%--                                        <span class="blocker en-larger" style="margin-left: 2px;">当前规则</span>--%>
<%--                                        <input type="radio" class="tab-radio" name="limitType" value="1" style="float:left;"   <c:if test="${ActivateRedEnvelopeRuleCog.limitType eq '1' }"> checked="checked" </c:if>/>--%>
<%--                                        <span class="blocker en-larger" style="margin-left: 2px;">每天</span>--%>
<%--                                    </div>--%>
<%--                                </td>--%>
<%--                            </tr>--%>
                            <tr>
                                <td class="ab_left"><label class="title"> 限制消费激活积分红包金额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="moneyLimit"  value="${ActivateRedEnvelopeRuleCog.moneyLimit}"  
                                               class="form-control input-width-larger  number money preValue minValue maxValue  required" autocomplete="off" minVal="0" maxVal="999999999.99" maxlength="12" tag="validate"/>
                                        <span class="blocker en-larger">元<span class="required">（分享裂变金额）</span></span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
<%--                            <tr>--%>
<%--                                <td class="ab_left"><label class="title">  限制分享中出红包总额：<span class="required">*</span></label></td>--%>
<%--                                <td class="ab_main" colspan="3">--%>
<%--                                    <div class="content">--%>
<%--                                        <input name="shareMoneyLimit"    value="${ActivateRedEnvelopeRuleCog.shareMoneyLimit}"--%>
<%--                                               class="form-control input-width-larger number money preValue minValue maxValue  required" autocomplete="off" minVal="0" maxlength="10" tag="validate"/>--%>
<%--                                        <span class="blocker en-larger">元<span class="required">（分享裂变激励金额）</span></span>--%>
<%--                                        <label class="validate_tips"></label>--%>
<%--                                    </div>--%>
<%--                                </td>--%>
<%--                            </tr>--%>

                            <tr class="rickUserLimit">
                                <td class="ab_left"><label class="title"> </label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="show_content content">
                                        <span class="blocker en-larger">风险用户待激活:<span style="color: red" class="required">*</span></span>
                                        <input id = "riskUserLimitMinMoney" name="riskUserLimitMinMoney"  value="${ActivateRedEnvelopeRuleCog.riskUserLimitMinMoney}" tag="validate"
                                               class="form-control  preValue money minValue maxValue input-width-small  moneyRegion required" style="float:left;" autocomplete="off" minVal="0.01" maxVal="9999.99" maxlength="10" />
                                        <label class="blocker en-larger">元</label>
                                        <span class="blocker en-larger">-</span>
                                        <input id = "riskUserLimitMaxMoney;" name="riskUserLimitMaxMoney"   value="${ActivateRedEnvelopeRuleCog.riskUserLimitMaxMoney}" tag="validate"
                                               class="form-control sufValue money minValue maxValue input-width-small  moneyRegion required" style="float:left;"  autocomplete="off" minVal="0.01" maxVal="9999.99" maxlength="10" />
                                        <label class="blocker en-larger">元</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr class="numberLimitTr">
                                <td class="ab_left"><label class="title">  限制分享中出积分总额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sharePointLimit"   value="${ActivateRedEnvelopeRuleCog.sharePointLimit}"
                                               class="form-control input-width-larger integer required" autocomplete="off" minVal="0" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">分<span class="required">（分享裂变激励积分）</span></span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="numberLimitTr">
                                <td class="ab_left"><label class="title">  限制消费瓶数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="numberLimit" value="${ActivateRedEnvelopeRuleCog.numberLimit}"  
                                               class="form-control input-width-larger integer required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">瓶 </span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
