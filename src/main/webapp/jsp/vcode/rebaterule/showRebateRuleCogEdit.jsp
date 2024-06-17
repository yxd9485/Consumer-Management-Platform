<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%String cpath = request.getContextPath(); 
String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加积分活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
        $(function(){
	        // 初始化校验控件
	        $.runtimeValidate($("#import_form"));
	        // 初始化功能
	        initPage();
        });
        
        function initPage() {
        	
        	// 爆点规则：已结束规则、周几或每天不可修改
        	if ("${isEditFlag}" == '2' || "${rebateRuleCog.ruleType}" == '3' || "${rebateRuleCog.ruleType}" == '4') {
                $("div.erupt-content :input").attr("disabled",true);
                $("#addEruptItem, #addEruptPerItem").css("display","none");
        	}
			
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).attr("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
			
			// 保存
			$(".btnSave").click(function(){
				var flag = validFile();
				if (flag) {
                    if('1' === $("#projectName").val()){
                        if (!$('.roleInfo input[type=radio]:checked').val()) {
                            $.fn.alert("请至少选择一个扫码角色");
                            return false;
                        }
                    }
					var checkFlag = true;
					// 校验三重防护
		            var url = "<%=cpath%>/vcodeActivityRebateRule/checkTripleProtection.do";
		            var myForm = new FormData(document.getElementById("import_form"));
		            var formData = new FormData();            
		            <!--上传文件 -->
		            $uploadFile = $("input.import-file");
		            var files = $uploadFile.val();
		            if($("[name='prizeCogType']").val() == "0" &&  files != "") {
		            	formData.append("filePath", $uploadFile[0].files[0]);
		            }

		            console.log("isWaitActivation:"+myForm.getAll("isWaitActivation"))
		            <!-- 配置规则配置对象 后台解析-->
		            formData.append("rebateRuleKey", myForm.get("rebateRuleKey"));
		            formData.append("activityType", myForm.get("activityType"));
		            formData.append("prizeCogType", myForm.get("prizeCogType"));
		            formData.append("ruleTotalPrize", myForm.get("ruleTotalPrize"));
		     		formData.append("firstScanPercent",myForm.get("firstScanPercent"));
		            formData.append("vcodeActivityKey", myForm.get("vcodeActivityKey"));
		            formData.append("ruleUnitMoney", myForm.get("ruleUnitMoney"));
		            formData.append("ruleType", myForm.get("ruleType"));
		            formData.append("endDate", myForm.get("endDate"));
		            if	(myForm.get("moneyDanping") != ''){
		            	formData.append("moneyDanping", myForm.get("moneyDanping"));            	            	
		            }
		            formData.append("randomType", myForm.getAll("randomType"));
                    formData.append("isWaitActivation", myForm.getAll("isWaitActivation"));
                    formData.append("minMoney", myForm.getAll("minMoney"));
		            formData.append("scanType", myForm.getAll("scanType")); 
		            formData.append("fixationMoney", myForm.getAll("fixationMoney")); 
		            $.ajax({
		                type: "POST",
		                url: url,
		                data: formData,
		                dataType: "json",
		                async: false,
		                contentType : false,
		                processData: false,
		                beforeSend:appendVjfSessionId,
		                success:  function(data) {
		                	console.log(data);
		                	if (data["errMsg"]) {
		                		checkFlag = false;
		                		console.log(data["errMsg"])
		                		if(data["errMsg"] == "奖项配置项解析异常"){
		                			$.fn.alert(data["errMsg"]);
		                		}else{
		                			$.fn.confirm(data["errMsg"], function(){	        					
		                				$(".btnSave").attr("disabled", "disabled");
		        			            // 去除文件的disabled属性
		        		    			$("input.import-file").removeAttr("disabled", "disabled")
		        		    			$("form").attr("action", $(this).attr("url"));
		        						$("form").attr("onsubmit", "return true;");
		        						$("form").submit();	        					
		        					});		                			
		                		}
		                	}
		                }
		            }); 
		            if(checkFlag){
		            	$(".btnSave").attr("disabled", "disabled");
			            // 去除文件的disabled属性
		    			$("input.import-file").removeAttr("disabled", "disabled")
		    			$("form").attr("action", $(this).attr("url"));
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
		            }
				}
				return false;
			});
            
            $("#beginDate").on("blur", function(){
            	var ruleType = $(this).data("ruletype");
                var beginVal = $(this).val();
                var endVal = $("#endDate").val();
                if (ruleType == '3' && beginVal != "") {
                    if (!$.isNumeric(beginVal) || beginVal > 7 || beginVal < 1) {
                        $.fn.alert("请输入1~7的数字");
                        $(this).val("");
                        
                    } else if(endVal != "" && beginVal > endVal) {
                        $.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
                    }
                }
            });
            
            $("#endDate").on("blur", function(){
                var ruleType = $(this).data("ruletype");
                var beginVal = $("#beginDate").val();
                var endVal = $(this).val();
                if (ruleType == '3' && endVal != "") {
                    if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1) {
                        $.fn.alert("请输入1~7的数字");
                        $(this).val("");
                        
                    } else if(beginVal != "" && beginVal > endVal) {
                        $.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
                    }
                }
            });
            
            // 选择规则
			$("[name='appointRebateRuleKey']").on("change", function(){
				var restrict = $(this).find("option:selected").attr("data-value");
				if(typeof(restrict) != 'undefined' || restrict ==''){
                    $("[name='restrictVpoints']").val(restrict.split("-")[0]);
                    $("[name='restrictVpoints']").attr("readonly",true);
                    $("[name='restrictMoney']").val(Number(restrict.split("-")[1]).toFixed(2));
                    $("[name='restrictMoney']").attr("readonly",true);
                    $("[name='restrictBottle']").val(restrict.split("-")[2] == '0' ? "" : restrict.split("-")[2]);
                    $("[name='restrictBottle']").attr("readonly",true);
                    $("[name='restrictCount']").val(restrict.split("-")[3] == '0' ? "" : restrict.split("-")[3]);
                    $("[name='restrictCount']").attr("readonly",true);
                    
                    // 限制类型
                    if("0" == restrict.split("-")[4]){
                        $("#restrictTimeType1").trigger("click")
                    }else if("1" == restrict.split("-")[4]){
                        $("#restrictTimeType2").trigger("click")
                    } 
                    $("#restrictTimeType1").attr("disabled", true);
                    $("#restrictTimeType2").attr("disabled", true);
                    
                    $("#restrictTimeType").removeAttr("disabled");
                    $("#restrictTimeType").val(restrict.split("-")[4]);
                }else{
                    $("[name='restrictVpoints'").val('');
                    $("[name='restrictMoney'").val('');
                    $("[name='restrictBottle'").val('');
                    $("[name='restrictCount'").val('');
                    $("[name='restrictVpoints'").attr("readonly",false);
                    $("[name='restrictMoney'").attr("readonly",false);
                    $("[name='restrictBottle'").attr("readonly",false);
                    $("[name='restrictCount'").attr("readonly",false);
					
					// 限制类型
					$("#restrictTimeType1").removeAttr("disabled");
					$("#restrictTimeType2").removeAttr("disabled");
					if('${rebateRuleCog.restrictTimeType}' == "0"){
						$("#restrictTimeType1").trigger("click");
					}else{
						$("#restrictTimeType2").trigger("click");
					}
					$("#restrictTimeType").val("");
					$("#restrictTimeType").attr("disabled", true);
				}
				
			});

            // 配置规则方案
            $("[name='prizeCogType']").on("change", function(){
                if ($(this).val() == '1') {
                    $("div.templetitem").css("display", "flex");
                    $("div.fileitem").css("display", "none");
                    $("input.import-file").attr("disabled", "disabled").val("");
                } else {
                    $("div.templetitem").css("display", "none");
                    $("tr.preview-templet").css("display", "none");
                    $("div.fileitem").css("display", "flex");
                    $("input.import-file").removeAttr("disabled", "disabled");
                }
            });
            
            // 群组用户单选
            $("[name='isGroupUser']").on("change", function(){
            	// 如果选择了群组用户那么要默认的值
            	if ($(this).val() == '1') {           
            		$("[name='groupId']").css("display", "flex");
            			$.ajax({
                            type: "POST",
                            url: "<%=cpath%>/vcodeActivityRebateRule/findGroupList.do",
                            async: false,
                            dataType: "json",
                			beforeSend:appendVjfSessionId,
                            success: function(result){
                                var content = "<option value='' selected>请选择</option>";
                                if(result){
                                    $.each(result, function(i, item) {
    									content += "<option value='"+item.id+"'>"+item.name+"</option>";
                                    });
                                    $("[name='groupId']").html(content);
                                } else {
                                	$("[name='groupId']").html(content);
                                }
                            },
                            error: function(data){
                                $.fn.alert("群组查询错误!请联系研发");
                            }
                        }); 
            		           		
            	} else{
            		$("[name='groupId']").css("display", "none");	
            		$("#selectGroup").css("display", "none");
            		$("input[name='groupName']").val('');
            		$("#groupId").val('');
            		
            	}
            });
            
          //根据下拉框显示按钮
            $("[name='groupId']").change(function(){
				var groupType = $(this).val();
				if (groupType != "" && groupType != "请选择") {
					$("#selectGroup").css("display", "flex");
					var opts= $("#groupId").find("option:selected").text();
					 $("input[name='groupName']").val(opts);
				} else {
					$("#selectGroup").css("display", "none");
				}       				
			});
            
            // 查看群组弹窗 TODO
            $("a.edit").off();
            $("a.edit").on("click", function(){
                // 初始化批次数据
                var key = $("#groupId").val();
                var url = "<%=cpath%>/vcodeActivityRebateRule/findGroupInfo.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {groupId : key},
                    dataType: "json",
                    async: false,
                	beforeSend:appendVjfSessionId,
                    success: function(data) {
                    	// 弹出提示 
                		console.log(data);
                		if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                        } else{
                        	$("#selectloadDialog").modal("show");
                        	var groupInfo = data['groupInfo'];
                        	for(var name in groupInfo) {
                        	if (name == "valid" && groupInfo[name] == "-1"){
                        		$("#selectloadDialog input[name='" + name + "']").val('永久');
                        	}else{
                            	$("#selectloadDialog input[name='" + name + "']").val(groupInfo[name]);  
                            	$("#selectloadDialog textarea[name='" + name + "']").val(groupInfo[name]);
                        	}
                        }	
                		}
                    }
                });
            });

            
            if ("${rebateRuleCog.groupId}" != '' && "${not empty rebateRuleCog.groupId}") {
            	$("[name='groupId']").css("display", "flex");
            	$("#selectGroup").css("display", "flex");
            	var opts= $("#groupId").find("option:selected").text();
				 $("input[name='groupName']").val(opts);
        	}
             
            
            // 预览规则方案
            $("a.preview-templet").on("click", function(){
                if ($("tr.preview-templet").css("display") == 'none') {
                    $("tr.preview-templet").css("display", "");
                } else {
                    $("tr.preview-templet").css("display", "none");
                }
            });
            
            // 爆点新增
            $("form").on("click", "#addEruptItem", function(){
                if ($(this).text() == "新增") {
                    var count = $(this).closest("div.erupt-content").find("table").size();
                    var $cloneItem = $(this).closest("table").clone(true, true);
                    $cloneItem.addClass("mart30");
                    $cloneItem.find("div.eruptperitem:gt(0)").remove();
                    var $eruptStartDate = $cloneItem.find("#eruptStartDate0");
                    $eruptStartDate.attr("id", "eruptStartDate" + count).val("");
                    $eruptStartDate.attr("onfocus", $eruptStartDate.attr("onfocus").replace("eruptEndDate0", "eruptEndDate" + count));
                    var $eruptEndDate = $cloneItem.find("#eruptEndDate0");
                    $eruptEndDate.attr("id", "eruptEndDate" + count).val("");
                    $eruptEndDate.attr("onfocus", $eruptEndDate.attr("onfocus").replace("eruptStartDate0", "eruptStartDate" + count));
                    var $eruptStartTime = $cloneItem.find("#eruptStartTime0");
                    $eruptStartTime.attr("id", "eruptStartTime" + count).val("");
                    $eruptStartTime.attr("onfocus", $eruptStartTime.attr("onfocus").replace("eruptEndTime0", "eruptEndTime" + count));
                    var $eruptEndTime = $cloneItem.find("#eruptEndTime0");
                    $eruptEndTime.attr("id", "eruptEndTime" + count).val("");
                    $eruptEndTime.attr("onfocus", $eruptEndTime.attr("onfocus").replace("eruptStartTime0", "eruptStartTime" + count));
                    $cloneItem.find("[id$='Time']").val("");
                    $cloneItem.find("#eruptPerKey").val(getUuid());
                    $cloneItem.find("#eruptPerNum, #eruptPerMoney, #eruptPerVpoints, #eruptPerPrize, #eruptPerCount").removeAttr("disabled").data("oldval", "").val("");
                    $cloneItem.find("#addEruptItem").text("删除")
                    $(this).closest("div.erupt-content").append($cloneItem);
                } else {
                    $(this).closest("table").remove();
                }
            });
            
            // 爆点新增倍数
            if('${rebateRuleCog.eruptRuleInfo}' == ''){
            	$("#eruptPerKey").val(getUuid());
            }
            $("form").on("click", "#addEruptPerItem", function(){
                if ($(this).text().indexOf("+") > -1) {
                    $cloneItem = $(this).closest("div").clone(true, true);
                    $cloneItem.find("#eruptPerKey").val(getUuid());
                    $cloneItem.find("#eruptPerNum, #eruptPerMoney, #eruptPerVpoints, #eruptPerPrize, #eruptPerCount").removeAttr("disabled").data("oldval", "").val("");
                    $cloneItem.find("#addEruptPerItem").html("-");
                    $(this).closest("td.perItem").append($cloneItem);
                } else {
                    $(this).closest("div").remove();
                }
            });
            
            // 爆点中金额积分与大奖类型互斥
            $("form").on("change", "#eruptPerMoney, #eruptPerVpoints", function(){
                $eruptperitem = $(this).closest("div.eruptperitem");
                if ($eruptperitem.find("#eruptPerMoney").val() > 0 || $eruptperitem.find("#eruptPerVpoints").val() > 0) {
                    $eruptperitem.find("#eruptPerPrize").attr("disabled", "disabled");
                } else {
                    $eruptperitem.find("#eruptPerPrize").removeAttr("disabled");
                }
            });
            
            // 爆点中金额积分与大奖类型互斥
            $("form").on("change", "#eruptPerPrize", function(){
                $eruptperitem = $(this).closest("div.eruptperitem");
                if ($(this).val()) {
                    $eruptperitem.find("#eruptPerMoney").attr("disabled", "disabled");
                    $eruptperitem.find("#eruptPerVpoints").attr("disabled", "disabled");
                } else {
                    $eruptperitem.find("#eruptPerMoney").removeAttr("disabled");
                    $eruptperitem.find("#eruptPerVpoints").removeAttr("disabled");
                }
            });
            
            // 爆点输入倍数时
            $("form").on("change", "#eruptPerNum", function(){
                $eruptperitem = $(this).closest("div.eruptperitem");
                if ($eruptperitem.find("#eruptPerMoney").val() == "") {
                    $eruptperitem.find("#eruptPerMoney").data("oldval", "0.00").val("0.00");
                }
                if ($eruptperitem.find("#eruptPerVpoints").val() == "") {
                    $eruptperitem.find("#eruptPerVpoints").data("oldval", "0").val("0");
                }
                if ($eruptperitem.find("#eruptPerCount").val() == "") {
                    $eruptperitem.find("#eruptPerCount").data("oldval", "0").val("0");
                }
            });
            
            // 单击时清空选择的文件，当选择同一文件时才能触发change事件
            $("input[name='filePath']").on("click", function(){
                $(this).val("");
            });
            
            // 校验奖项配置项配置文件
            $("input[name='filePath']").on("change", function() {
                $uploadFile = $(this);
                var files = $uploadFile.val();
                if($("[name='prizeCogType']").val() != "0" 
                        || files == "" || files.indexOf("xls") == -1) {
                    return false;
                }
                $("div.fileitem input[id]").removeData("validval");
            
                // 提交表单
                var url = "<%=cpath%>/vcodeActivityRebateRule/checkActivityVpointsForImport.do";
                var formData = new FormData();
                formData.append("filePath", $uploadFile[0].files[0]);
                formData.append("activityType", $("input[name='activityType']").val());
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: false,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        console.log(data);
                        if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                            $("input[name='filePath']").val("");
                        } else {
                            var validFlag = true;
                            $.each(data, function(key){
                                var $validItem = $("div.fileitem #" + key);
                                console.log(key + "_" + $validItem.val() + "_" + data[key]);
                                $validItem.data("validval", data[key]);
                                if ($validItem.val() != "") {
                                    if (key == "minMoney" || key == "minVpoints") {
                                        if (Number($validItem.val()) > Number(data[key])) {
                                            validFlag = true;
                                        }
                                    } else if (Number($validItem.val()) < Number(data[key])) {
                                        validFlag = true;
                                    }
                                    if (validFlag) {
                                        validFlag = false;
                                        $validItem.css("color", "red");
                                        $validItem.trigger("change");
                                    }
                                }
                            });
                        }
                    }
                });
            });

            // 校验奖项配置项配置文件
            $("div.fileitem input[id]").on("change", function(){
                $validItem = $(this);
                var validItemId = $validItem.attr("id");
                if(validItemId == "minMoney" || validItemId == "maxMoney" ) {
                    if ($("div.fileitem #minMoney").val() != "" && $("div.fileitem #maxMoney").val() != ""
                            && Number($("div.fileitem #minMoney").val()) > Number($("div.fileitem #maxMoney").val())) {
                        $validItem.val("");
                        $.fn.alert("金额区间最小值必须要小于等于区间最大值！");
                    } 
                } else if(validItemId == "minVpoints" || validItemId == "maxVpoints" ) {
                    if ($("div.fileitem #minVpoints").val() != "" && $("div.fileitem #maxVpoints").val() != "" 
                            && Number($("div.fileitem #minVpoints").val()) > Number($("div.fileitem #maxVpoints").val())) {
                        $validItem.val("");
                        $.fn.alert("积分区间最小值必须要小于等于区间最大值！");
                    } 
                }
                
                $validItem.css("color", "#555");
                if ($validItem.val() != "") {
                    if (validItemId == "minMoney" && Number($validItem.val()) > Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最小金额小于校验金额区间最小值！");
                    } else if (validItemId == "maxMoney" && Number($validItem.val()) < Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最大金额大于校验金额区间最大值！");
                    } else if (validItemId == "minVpoints" && Number($validItem.val()) > Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最小积分小于校验积分区间最小值！");
                    } else if (validItemId == "maxVpoints" && Number($validItem.val()) < Number($validItem.data("validval"))) {
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件中的最大积分大于校验积分区间最大值！");
                    } else if ((validItemId == "totalMoney" || validItemId == "totalPrize" || validItemId == "totalVpoints")
                              && Number($validItem.val()) < Number($validItem.data("validval"))){
                        $validItem.css("color", "red");
                        $.fn.alert("奖项配置文件的" + $validItem.prev("span").text().replace("：","") + "大于校验值！");
                    }
                }
            });

            // 指定继承活动
            $("#appointActivityKey").on("change", function(){
                $("[name='appointRebateRuleKey']").html("<option value=''>请选择</option>");
                var vcodeActivityKey = $(this).val();
                var rebateRuleKey = "${rebateRuleCog.appointRebateRuleKey}";
                if (vcodeActivityKey == "") return;
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/vcodeActivityRebateRule/queryAppointRebateRule.do",
                    async: false,
                    data: {"vcodeActivityKey":vcodeActivityKey, "rebateRuleKey":rebateRuleKey},
                    dataType: "json",
                    beforeSend:appendVjfSessionId,
                    success:  function(data){
                        if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                        } else {
                            var content = "<option value=''>请选择</option>";
                            if(data["rebateRuleCogLst"]){
                                $.each(data["rebateRuleCogLst"], function(i, item) {
                                    content += "<option value='"+item.rebateRuleKey+"' data-value='" + item.restrictVpoints + "-" + item.restrictMoney + "-" + item.restrictBottle + "-" + item.restrictCount + "-" + item.restrictTimeType + "'>";
                                    if (item.ruleType == "1") {
                                        content += item.areaName + " 节假日："
                                    } else if (item.ruleType == "2") {
                                        content += item.areaName + " 时间段："
                                    }
                                    content += item.beginDate + " " + item.beginTime + " - " + item.endDate + " " + item.endTime + "</option>";
                                });
                            }
                            $("[name='appointRebateRuleKey']").html(content);
                            $("[name='appointRebateRuleKey']").val(rebateRuleKey).trigger("change");
                        }
                    }
                }); 
            });
            $("#appointActivityKey").val("${appointRebateRuleCog.vcodeActivityKey}").trigger("change");
            
            
         	// 翻倍返利类型切换
         	if('${rebateRuleCog.allowanceaRebateType}' == '0'){
                if (${activityType ne '8'}){
                    $("#allowanceMoneyTr").css("display", "");
                }
                $("#allowanceVpointsTr").css("display", "none");
         	}else{
         		$("#allowanceMoneyTr").css("display", "none");
         		if (${activityType ne '8'}){
                    $("#allowanceVpointsTr").css("display", "");
                }
         	}
            $("input[name='allowanceaRebateType']").on("change", function(){
                if ($(this).val() == "0") {
                	$("#allowanceVpointsTr input").val("");
                    $("#allowanceMoneyTr").css("display", "");
                    $("#allowanceVpointsTr").css("display", "none");
                } else {
                	$("#allowanceMoneyTr input").val("");
                	$("#allowanceMoneyTr").css("display", "none");
                    $("#allowanceVpointsTr").css("display", "");
                }
            });
		}
		
		function validFile() {
            if ($("#beginDate:enabled").val() == "" || $("#endDate:enabled").val() == "") {
                $.fn.alert("日期范围不可为空!");
                return false;
            }
            
            if ($("#beginTime:enabled").val() == "" || $("#endTime:enabled").val() == "") {
                $.fn.alert("时间范围不可为空!");
                return false;
            }
            
            if ($("[name='prizeCogType']:checked").val() == '0') {
	            var files = $("input.import-file").val();
	            if(files != "" && files.indexOf("xls") == -1) {
	                $.fn.alert("不是有效的EXCEL文件");
	                return false;
	            }
            } else {
            	if ($("#rebateRuleTemplet").val() != "请选择") {
	            	if (Number($("input[name='ruleTotalPrize']").val()) <= 0) {
	                    $.fn.alert("请输入有效的奖项总数!");
	                    return false;
	                }
	            	// 检验配置项的百分比
	                if(!validVpoints()){
	                    return false;
	                }
            	}
            }

            //待激活红包必填校验
            var isRangeEmpty = false;
            $('select[name="waitActivationPrizeKey"]').each(function () {
                if ($(this).val()) {
                    var $thisRow = $(this).closest('tr');
                    var minMoney = $thisRow.find('input[name="minWaitActivationMoney"]').val()
                    var maxMoney = $thisRow.find('input[name="maxWaitActivationMoney"]').val()
                    if (parseFloat(minMoney) === 0 || parseFloat(maxMoney) === 0) {
                        isRangeEmpty = true;
                        return false;
                    }
                }
            });
            if (isRangeEmpty) {
                $.fn.alert("选择待激活红包后积分红包范围必填!");
                return false;
            }

            // 爆点规则:只用于节假日及时间段规则
			var ruleType = $("[name='ruleType']").val();
			if (ruleType == "1" || ruleType == "2") {
				var eruptRule = "";
				var eruptFlag = false;
				$("div.erupt-content table.erupt_table").each(function(){
					var valNum = 0;
					$(this).find(":input:enabled:not(#eruptPerKey)").each(function(){
						if ($(this).val() != '') valNum++;
					});
					
				    if(valNum > 0 && valNum != $(this).find(":input:enabled:not(#eruptPerKey)").size()) {
				    	$.fn.alert("请完善爆点规则");
				    	eruptFlag = true;
				    	return false;
				    }
				    if (valNum > 0) {
					    eruptRule = eruptRule + $(this).find("input.eruptStartDate").val() + "#" + $(this).find("input.eruptEndDate").val() + ","
					    eruptRule = eruptRule + $(this).find("input.eruptStartTime").val() + "#" + $(this).find("input.eruptEndTime").val() + ","
					    $(this).find("div.eruptperitem").each(function(){
			                eruptRule = eruptRule + $(this).find("#eruptPerKey").val() + ":" + $(this).find("#eruptPerNum").val() + ":" + $(this).find("#eruptPerMoney").val() 
			                	+ ":" + $(this).find("#eruptPerVpoints").val() + ":" + $(this).find("#eruptPerPrize").val() + ":" + $(this).find("#eruptPerCount").val() + ","
					    });
					    eruptRule += ";";
				    }
				});
				$("input[name='eruptRuleInfo']").val(eruptRule);
				//if (eruptFlag) {return false;}

                if (eruptFlag) {
                    return false;
                }else{
                    var activityType = $("input[name=activityType]").val()
                    var eruptRuleInfo = $("input[name='eruptRuleInfo']").val();
                    console.log("=======================天降红包活动倍数中出规则互斥校验[activityType]:"+activityType)
                    console.log("=======================天降红包活动倍数中出规则互斥校验[eruptRuleInfo]:"+eruptRuleInfo)
                    if (activityType === '13' && eruptRuleInfo!==''){
                        // 天降红包活动倍数中出规则互斥校验
                        console.log("=======================天降红包活动倍数中出规则互斥校验开始")
                        var check =  checkRule();
                        console.log("=======================天降红包活动倍数中出规则互斥校验结束，结果："+check)
                        //天降红包活动没有倍数中出规则的时候才会返回true，不是true 就需要返回false，表示校验不通过
                        if (!check){
                            return false;
                        }
                    }
                }
			} else {
                $("input[name='eruptRuleInfo']").val();
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
			if($("input[name='moneyDanping']").val() == ""){
				$("input[name='moneyDanping']").val("0.00");
			}
			
			if($("input[name='restrictFirstDayBottle']").val() == ""){
                $("input[name='restrictFirstDayBottle']").val("0");
            }
			if($("input[name='restrictFirstDayMoney']").val() == ""){
				$("input[name='restrictFirstDayMoney']").val("0.00");
			}
			if($("input[name='restrictFirstMonthBottle']").val() == ""){
				$("input[name='restrictFirstMonthBottle']").val("0");
			}
			if($("input[name='restrictFirstMonthMoney']").val() == ""){
				$("input[name='restrictFirstMonthMoney']").val("0.00");
			}
			if($("input[name='restrictFirstTotalBottle']").val() == ""){
				$("input[name='restrictFirstTotalBottle']").val("0");
			}
			if($("input[name='restrictFirstTotalMoney']").val() == ""){
				$("input[name='restrictFirstTotalMoney']").val("0.00");
			}
			
			return true;
		}
		
		function getUuid() {
			  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
			    const r = Math.random() * 16 | 0;
			    const v = c === 'x' ? r : (r & 0x3 | 0x8);
			    return v.toString(16);
			  });
			}

        // 倍数中出规则-爆点规则互斥校验
        function checkRule() {
            var flag = false;
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/waitActivation/ruleCheckMultiple.do",
                async: false,
                data: {},
                dataType: "json",
                beforeSend: appendVjfSessionId,
                success: function (result) {
                    console.log("=======================result[success]:"+result["success"])
                    console.log("=======================result[isHaveMultiple]:"+result["isHaveMultiple"])
                    if (result["success"]) {
                        var isHaveMultiple = result["isHaveMultiple"]
                        if(isHaveMultiple){
                            $.fn.alert("天降红包活动已有倍数中出规则，爆点规则无法配置!");
                        }else{
                            flag = true;
                        }
                    }else{
                        $.fn.alert("天降红包倍数中出规则-爆点规则互斥校验异常!");
                    }
                },
                error: function (data) {
                    $.fn.alert("天降红包倍数中出规则-爆点规则互斥校验异常!");
                }
            });
            console.log("=======================ajax返回值:"+flag)
            return flag;
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
		.cool-wood {
			padding-left: 1em;
		}
		fieldset {
			border: 1px solid #d1d1d1;
			max-height: 160px;
			overflow: auto;
		}
		
		fieldset legend {
			font-weight: bold;
		}
		
		fieldset .remove-target {
			float: right;
			color: red;
			cursor: pointer;
		}
		
		fieldset .remove-target > span {
			margin-left: 2px;
		}
		
		fieldset .remove-target > span:hover {
			text-decoration: underline;
		}
		
		fieldset div {
			column-count: 2;
			max-height: 150px;
			height: auto;
			margin-top: 25px;
		}
		
		fieldset div > label {
			width: 48%;
		}
		
		fieldset div > label > span {
			margin-left: 8px;
			font-weight: normal;
		}
		
		.white {
			color: white;
		}
	</style>
  </head>
  
  <body>
    <div class="container" style="padding: 0px;">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath%>/vcodeActivityRebateRule/importRebateRuleCogMoneyConfig.do" onsubmit="return validFile();">
            	<input type="hidden" name="rebateRuleKey" value="${rebateRuleCog.rebateRuleKey}" />
            	<input type="hidden" name="vcodeActivityKey" value="${rebateRuleCog.vcodeActivityKey}" />
            	<input type="hidden" name="areaCode" value="${rebateRuleCog.areaCode}" />
            	<input type="hidden" name="ruleType" value="${rebateRuleCog.ruleType}" />
            	<input type="hidden" name="activityType" value="${activityType}" />
                <input type="hidden" name="projectName" id="projectName" value="${projectName}"/>
            	<div class="widget box">
<!--             	基础信息 -->
            		<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>基础信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                        		<td class="ab_left"><label class="title mart5">活动名称：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<span>${activityCog.vcodeActivityName}</span>
                        			</div>
                        		</td>
                        	</tr>
                        	<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="rebateRuleName" tag="validate" value="${rebateRuleCog.rebateRuleName}"
	                       					class="form-control required" style="width: 352px;" autocomplete="off" maxlength="100" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                        	<tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
	                       		<td class="ab_left"><label class="title">单瓶成本：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="moneyDanping" tag="validate"  value="<fmt:formatNumber value="${rebateRuleCog.moneyDanping == 0 ? '' : rebateRuleCog.moneyDanping}" pattern="0.00"/>"
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">首扫是否计入单瓶限制：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="firstScanDanpingLimit" name="firstScanDanpingLimit" value="1" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.firstScanDanpingLimit eq '1'}"> checked="checked" </c:if>>&nbsp;计入
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="firstScanDanpingLimit" name="firstScanDanpingLimit" value="0" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.firstScanDanpingLimit eq '0'}"> checked="checked" </c:if>>&nbsp;不计入
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title" title="仅限新用户阶梯规则：选择“启用”后，仅新用户可中该规则内配置的阶梯活动规则，该规则无需再进行“活动配置-阶梯规则”双重配置。&#10;如果希望老用户也中该阶梯规则，请单独新建规则与新用户区分，同时在“活动配置-阶梯规则”内同步相关配置。">仅限新用户阶梯规则：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="ruleNewUserLadder" name="ruleNewUserLadder" value="1" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.ruleNewUserLadder eq '1'}"> checked="checked" </c:if>>&nbsp;启用
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="ruleNewUserLadder" name="ruleNewUserLadder" value="0" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.ruleNewUserLadder eq '0'}"> checked="checked" </c:if>>&nbsp;不启用
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>

                            <c:if test="${projectName == '1'}">
                                <tr id="roleInfoTr">
                                    <td class="ab_left"><label class="title">扫码角色：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content roleInfo">
                                            <c:if test="${not empty(roleInfoAll)}">
                                                <c:forEach items="${roleInfoAll }" var="roleItem">
                                                    <div style="float: left; margin-right: 10px;">
                                                        <span>${fn:split(roleItem, ':')[1]}</span>
                                                        <input type="radio" tag="validate" name="allowScanRole" value="${fn:split(roleItem, ':')[0]}" style="float: left;"
                                                        <c:forEach items="${fn:split(rebateRuleCog.allowScanRole, ',') }" var="role">
                                                        <c:if test="${fn:split(roleItem, ':')[0] eq role}"> checked </c:if>
                                                        </c:forEach>

                                                        >
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">规则区域：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        				<span>${rebateRuleCog.areaName}</span>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">规则类型：<span class="white">*</span></label></td>
                                <td class="ab_main">
                                    <div class="content" id="ruleType" data-ruletype="${rebateRuleCog.ruleType}">
                                        <c:choose>
                                            <c:when test="${rebateRuleCog.ruleType == '1'}">节假日</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '2'}">时间段</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '3'}">周几</c:when>
                                            <c:when test="${rebateRuleCog.ruleType == '4'}">每天</c:when>
                                        </c:choose>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <c:choose>
                                        <c:when test="${rebateRuleCog.ruleType == '4' or rebateRuleCog.ruleType == '5'}">
		                                    <div class="content">
		                                        <span class="blocker">从</span>
		                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium" disabled="disabled"/>
		                                        <span class="blocker en-larger">至</span>
		                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium" disabled="disabled"/>
		                                    </div>
                                        </c:when>
                                        <c:when test="${rebateRuleCog.ruleType == '3'}">
		                                    <div class="content">
		                                        <span class="blocker">从</span>
		                                        <input name="beginDate" id="beginDate" class="form-control input-width-medium"
		                                        <c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly" disabled</c:if> 
		                                        data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.beginDate }"/>
		                                        <span class="blocker en-larger">至</span>
		                                        <input name="endDate" id="endDate" class="form-control input-width-medium" 
		                                        <c:if test="${isEditFlag eq '2'}">readonly="readonly" disabled</c:if>  
		                                        data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.endDate }"/>
		                                    </div>
                                        </c:when>
                                        <c:otherwise>
		                                    <div class="content">
		                                        <span class="blocker">从</span>
		                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium" 
		                                        	<c:if test="${isEditFlag eq '1' or isEditFlag eq '2'}">readonly="readonly" disabled</c:if> 
		                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.beginDate }"/>
		                                        <span class="blocker en-larger">至</span>
		                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium"  
		                                        	<c:if test="${isEditFlag eq '2'}">readonly="readonly" disabled</c:if> 
		                                        	onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')||\'%y-%M-%d\'}'})" data-ruletype="${rebateRuleCog.ruleType}" value="${rebateRuleCog.endDate }"/>
		                                    </div>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                                <td class="ab_main">
	                                    <div class="content">
	                                        <span class="blocker">从</span>
	                                        <input name="beginTime" id="beginTime" class="form-control input-width-medium Wdate" 
	                                                <c:if test="${isEditFlag eq '1' or isEditFlag eq '2' or rebateRuleCog.ruleType eq '5'}">readonly="readonly" disabled</c:if> 
	                                                onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" value="${rebateRuleCog.beginTime}"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endTime" id="endTime" class="form-control input-width-medium Wdate" 
	                                                <c:if test="${isEditFlag eq '2' or rebateRuleCog.ruleType eq '5'}">readonly="readonly" disabled</c:if>  
	                                                onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime\')}'})" value="${rebateRuleCog.endTime}"/>
	                                    </div>
                                </td>
                            </tr>
                            
                            <c:if test= "${groupSwitch eq '1'}">
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">精细化营销：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="isGroupUser" value="0" style="float: left; height:20px; cursor: pointer;" <c:if test= "${empty rebateRuleCog.groupId}"> checked="checked" </c:if>>&nbsp;全部用户
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            
                           
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <input type="hidden" name="groupName" />
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                <div style="line-height: 35px;">
                                         <div style="display: flex;">
                                             <div style="float: left; width: 80px; line-height: 25px;">
                                                  <input type="radio" name="isGroupUser"  value="1" style="float: left; height:20px; cursor: pointer;" <c:if test= "${not empty rebateRuleCog.groupId}"> checked="checked" </c:if>>
                                                  <span>&nbsp;群组用户</span>
                                             </div>
                                             <select name="groupId" id ="groupId" class="form-control input-width-larger marl5" style="display:none;" >
                                             		<option value="">请选择</option>
                                             		<c:if test="${	not empty rebateRuleCog.groupId}">  
                                             			<option value="${rebateRuleCog.groupId}" selected="selected">${rebateRuleCog.groupName}</option>   
                                             		</c:if>
                                             		<c:if test="${not empty(groupList) }">  
                                             			<c:forEach items="${groupList}" var="group">
                                             		    	<c:if test="${group.id != rebateRuleCog.groupId }">  
                                                    	  		<option value="${group.id }" >${group.name}</option>   
                                             		    	</c:if>
                                                		</c:forEach>                     			         	
                        			             	</c:if>
                        			         </select>
                        			         <a class="btn btn-xs edit btn-blue" id="selectGroup" class="btn btn-blue" style="display:none;">查看群组详情</a>
                                         </div>
                                 </div>
                                </td>                                
                            </tr>
                            </c:if>
                        
                            <tr>
                                <td class="ab_left"><label class="title">特殊优先级标签：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="specialLabel" tag="validate"  
                                            class="form-control input-width-larger" value="${rebateRuleCog.specialLabel}" autocomplete="off" maxlength="50" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">开启动效：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <span class="blocker">大于</span> 
                                        <input name="popupMoney" tag="validate" 
                                            value='<fmt:formatNumber value="${rebateRuleCog.popupMoney}" pattern="0.00"></fmt:formatNumber>'
                                            class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">是否开启红包雨<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="redPacketRain" value="0" <c:if test="${rebateRuleCog.redPacketRain eq '0'}">checked="checked"</c:if> style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">不开启</span>
                                        <input type="radio" class="tab-radio" name="redPacketRain" value="1" <c:if test="${rebateRuleCog.redPacketRain eq '1'}">checked="checked"</c:if> style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">开启</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">翻倍区间类型<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="allowanceaRebateType" value="0" <c:if test="${rebateRuleCog.allowanceaRebateType eq '0'}">checked="checked"</c:if> style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">金额</span>
                                        <input type="radio" class="tab-radio" name="allowanceaRebateType" value="1" <c:if test="${rebateRuleCog.allowanceaRebateType eq '1'}">checked="checked"</c:if> style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="allowanceMoneyTr" <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">翻倍红包区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="allowanceaMinMoney" tag="validate" value="${rebateRuleCog.allowanceaMinMoney}"
                                            class="form-control number money preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="allowanceaMaxMoney" tag="validate" value="${rebateRuleCog.allowanceaMaxMoney}"
                                            class="form-control number money sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="allowanceVpointsTr" <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">翻倍积分区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="allowanceaMinVpoints" tag="validate" value="${rebateRuleCog.allowanceaMinVpoints}"
                                            class="form-control number preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0" maxVal="9999"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="allowanceaMaxVpoints" tag="validate" value="${rebateRuleCog.allowanceaMaxVpoints}"
                                            class="form-control number sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0" maxVal="9999"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td class="ab_left"><label class="title mart5">指定热区：</label></td>
                                <td class="ab_main">
                                    <div class="content hot">
                                        <c:if test="${rebateRuleCog.areaName eq '省外' or isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">
                                        <input type="hidden" name="hotAreaKey" value="${rebateRuleCog.hotAreaKey }">
                                        </c:if>
                                        <select name="hotAreaKey" class="hotArea form-control" style="width: 480px;" 
                                            <c:if test="${rebateRuleCog.areaName eq '省外' or isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
                                            <option value="">请选择</option>   
                                            <c:if test="${not empty(hotAreaList) }">
                                                <c:forEach items="${hotAreaList}" var="hotArea">
                                                    <option value="${hotArea.hotAreaKey }" <c:if test="${hotArea.hotAreaKey == rebateRuleCog.hotAreaKey }">selected="selected"</c:if>>${hotArea.hotAreaName}</option>   
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <span style="color: green; margin-left: 5px;">注意：指定后符合该热区下的扫码才会中奖</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="vertical-align: top;"><label class="title mart5">配置规则方案：<span class="required">*</span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;">
                                         <div style="display: flex;">
                                             <div style="width:120px; text-align:right;">
                                                 <input name="prizeCogType" type="radio" style="margin: auto 0px;" value="0" /> 
                                                 <span>&nbsp;导入配置文件：</span>
                                             </div>
                                             <input type="file" class="import-file" name="filePath" single disabled="disabled"/>
                                             <a href="<%=cpath%>/upload/cogFolder/${projectServerName}/奖项配置项模板.xlsx?v=<%=System.currentTimeMillis()%>">模板下载</a>
                                         </div>
                                         <div class="content fileitem" style="display: flex; display: none;">
                                             <span style="width:120px; text-align:right;">配置文件验证：</span>
                                             <span class="marl5">奖项总数：</span>
                                             <input id="totalPrize" tag="validate" class="form-control input-width-small number integer" data-oldval="" autocomplete="off" maxlength="10"/>
                                             <span style="margin-left: 52px !important;">总金额：</span>
                                             <input id="totalMoney" tag="validate" class="form-control input-width-small number money" data-oldval="" autocomplete="off" maxlength="12"/>
                                             <span style="margin-left: 52px !important;">总积分：</span>
                                             <input id="totalVpoints" tag="validate" class="form-control input-width-small number" data-oldval="" autocomplete="off" maxlength="14"/>
                                         </div>
                                         <div class="content fileitem" style="display: flex; display: none;">
                                             <span style="width:120px; text-align:right;"></span>
                                             <span class="marl5">金额区间：</span>
                                             <input id="minMoney" tag="validate" class="form-control input-width-small number money" data-oldval="" autocomplete="off" maxlength="5"/>
                                             <span>&nbsp;-&nbsp;</span>
                                             <input id="maxMoney" tag="validate" class="form-control input-width-small numberl money" data-oldval="" autocomplete="off" maxlength="5"/>
                                             <span class="marl30">积分区间：</span>
                                             <input id="minVpoints" tag="validate" class="form-control input-width-small number integer" data-oldval="" autocomplete="off" maxlength="7"/>
                                             <span>&nbsp;-&nbsp;</span>
                                             <input id="maxVpoints" tag="validate" class="form-control input-width-small number integer" data-oldval="" autocomplete="off"  maxlength="7"/>
                                         </div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="vertical-align: top;"><label class="title mart5"><span class="required"> </span></label></td>
                                <td class="ab_main">
                                    <div style="line-height: 35px;">
                                        <div style="display: flex;">
                                             <div style="width:120px; text-align:right;">
                                                 <input name="prizeCogType" type="radio" style="margin: auto 0px;" value="1" checked="checked" /> 
                                                 <span>&nbsp;选择配置方案：</span>
                                             </div>
                                             <select id="rebateRuleTemplet" class="form-control input-width-larger marl5" >
                                                 <option>请选择</option>
                                                 <c:if test="${not empty rebateRuleTempletLst}">
                                                     <c:forEach items="${rebateRuleTempletLst}" var="item">
                                                     <option value="${item.infoKey}"
                                                     	data-allowancearebatetype="${item.allowanceaRebateType}" data-allowanceaminmoney="${item.allowanceaMinMoney}" data-allowanceamaxmoney="${item.allowanceaMaxMoney}" data-allowanceaminvpoints="${item.allowanceaMinVpoints}" data-allowanceamaxvpoints="${item.allowanceaMaxVpoints}">${item.templetName }</option>
                                                     </c:forEach>
                                                 </c:if>
                                             </select>
                                         </div>
                                         <div class="content mart5 templetitem" style="display: flex;">
                                             <span style="width:120px; text-align:right;">设置奖项总数：</span>
                                             <input name="ruleTotalPrize" tag="validate" class="form-control input-width-normal number positive marl5" autocomplete="off" maxlength="10"/>
                                             <a class="marl15 preview-templet" style="text-decoration: none; color: rgb(77, 116, 150); cursor:pointer;">预览配置方案</a>
                                             <label class="validate_tips"></label>
                                         </div>
                                    </div>
                                </td>
                            </tr>
<!--                            预览配置方案内容 -->
                            <tr class="preview-templet" style="display: none;">
                                <td colspan="2">
                                    <jsp:include page="showRebateRuleCogTempletPreview.jsp?v=2"></jsp:include>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">规则备注：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <textarea name="remarks" rows="5"
                                            class="form-control required" autocomplete="off" maxlength="200" >${rebateRuleCog.remarks}</textarea>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                       	</table>
               		</div>

<!--                爆点规则 (只有扫码活动且规则类型为节假日及时间段可配置， 规则结束后不可修改)-->
                	<c:if test="${(activityType == '0' || activityType eq '13') and dayTypeFlage ne '1'}">
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>爆点规则</h4></div>
                	<div class="widget-content panel no-padding erupt-content">
                        <input type="hidden" name="eruptRuleInfo"/>
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">是否限制用户中奖个数：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="radio" id="eruptRestrictTimeType1" name="eruptRestrictTimeType" value="0" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.eruptRestrictTimeType eq '0'}"> checked="checked" </c:if>>&nbsp;规则时间内一次
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="eruptRestrictTimeType2" name="eruptRestrictTimeType" value="1" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.eruptRestrictTimeType eq '1'}"> checked="checked" </c:if>>&nbsp;每天一次
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="eruptRestrictTimeType3" name="eruptRestrictTimeType" value="2" style="float: left; height:20px; cursor: pointer;" <c:if test= "${rebateRuleCog.eruptRestrictTimeType eq '2'}"> checked="checked" </c:if>>&nbsp;不限制
                                    </div>
                                    <span class="blocker en-larger"></span>
                                </td>
                            </tr>
                		</table>
                		
                        <c:set var="eruptAry" value="${fn:split(rebateRuleCog.eruptRuleInfo, ';')}"></c:set>
                        <c:if test="${empty rebateRuleCog.eruptRuleInfo}">
	                        <c:set var="eruptAry" value="${fn:split('#,#,::', ';')}"></c:set>
                        </c:if>
                        <c:forEach items="${eruptAry}" var="item" varStatus="itemIdx">
                        <c:set var="itemAry" value="${fn:split(item, ',')}"></c:set>
                        <table class="active_board_table erupt_table">
                            <tr>
                                <td class="ab_left"><label class="title">日期限制：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex;">
                                        <span class="blocker">从</span>
                                        <input id="eruptStartDate${itemIdx.index}" class="form-control input-width-medium Wdate preTime eruptStartDate" value="${fn:split(itemAry[0], '#')[0]}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'eruptEndDate${itemIdx.index}\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input id="eruptEndDate${itemIdx.index}" class="form-control input-width-medium Wdate sufTime eruptEndDate" value="${fn:split(itemAry[0], '#')[1]}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'eruptStartDate${itemIdx.index}\')}'})" />
                                        <label id="addEruptItem" class="blocker marl15 btn-txt-add-red"><c:choose><c:when test="${itemIdx.count == 1}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">时间限制：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="margin: 5px 0px; display: flex;">
                                        <span class="blocker">从</span>
                                        <input id="eruptStartTime${itemIdx.index}" class="form-control input-width-medium Wdate preTime eruptStartTime"  value="${fn:split(itemAry[1], '#')[0]}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'eruptEndTime${itemIdx.index}\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input id="eruptEndTime${itemIdx.index}" class="form-control input-width-medium Wdate sufTime eruptEndTime"  value="${fn:split(itemAry[1], '#')[1]}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'eruptStartTime${itemIdx.index}\')}'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="vertical-align: top;"><label class="title mart5">每隔分钟：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main perItem">
                                    <c:forEach items="${itemAry}" var="perItem" varStatus="perItemIdx">
	                                    <c:if test="${perItemIdx.count > 2}">
	                                    <div class="content eruptperitem" style="margin: 5px 0px; display: flex;">
	                                        <input id="eruptPerKey" tag="validate" class="form-control" type="hidden"
	                                           data-oldval="${fn:split(perItem, ':')[0]}" value="${fn:split(perItem, ':')[0]}" autocomplete="off"/>
	                                        <input id="eruptPerNum" tag="validate" class="form-control input-width-small number"
	                                           data-oldval="${fn:split(perItem, ':')[1]}" value="${fn:split(perItem, ':')[1]}" autocomplete="off" maxlength="9"/>
	                                        <span class="blocker en-larger marl30">金额</span>
	                                        <input id="eruptPerMoney" tag="validate" class="form-control input-width-small number money" <c:if test="${!fn:contains(perItem, '::')}">disabled</c:if> 
	                                           data-oldval="${fn:split(perItem, ':')[2]}" value="${fn:split(perItem, ':')[2]}" autocomplete="off" maxlength="9"/>
	                                        <span class="blocker en-larger marl30">积分</span>
	                                        <input id="eruptPerVpoints" tag="validate" class="form-control input-width-small number integer" <c:if test="${!fn:contains(perItem, '::')}">disabled</c:if>
	                                           data-oldval="${fn:split(perItem, ':')[3]}" value="${fn:split(perItem, ':')[3]}" autocomplete="off" maxlength="9"/>
	                                        <span class="blocker en-larger marl30">大奖</span>
	                                        <select id="eruptPerPrize" tag="validate" class="form-control input-width-larger" <c:if test="${fn:contains(perItem, '::')}">disabled</c:if> data-oldval="${fn:split(perItem, ':')[4]}" autocomplete="off">
	                                            <option value="">请选择</option>
	                                            <c:forEach items="${prizeTypeMap}" var="prizeItem">
	                                            <c:if test="${fn:startsWith(prizeItem.key, 'P00')}">
	                                                <option value="${prizeItem.key}" <c:if test="${(fn:contains(perItem, '::') ? '-' : fn:split(perItem, ':')[4]) eq prizeItem.key}">selected</c:if>>${prizeItem.value}</option>
	                                            </c:if>
	                                            </c:forEach>
	                                        </select>
	                                        <span class="blocker en-larger marl30">每天投放个数</span>
                                        	<input id="eruptPerCount" tag="validate" class="form-control input-width-small number integer" autocomplete="off" maxlength="9"
                                        		data-oldval="${fn:split(perItem, ':')[fn:contains(perItem, '::') ? 4 : 5]}" value="${fn:split(perItem, ':')[fn:contains(perItem, '::') ? 4 : 5]}" />
	                                        <c:if test="${isEditFlag ne '2'}">
	                                        <span id="addEruptPerItem" class="marl10 btn-txt-add-red" style="font-size: 20px !important; font-weight: bold; cursor: pointer; width: 30px; text-align: center;">
	                                           <c:choose><c:when test="${perItemIdx.count == 3}">+</c:when><c:otherwise>-</c:otherwise></c:choose></span>
	                                        </c:if>
	                                    </div>
	                                    </c:if>
                                    </c:forEach>
                                </td>
                            </tr>
                        </table>
                        </c:forEach>
             		</div>
                    </c:if>
                          
<!--                限制规则 -->
                    <div class="widget-header"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                	<div class="widget-content panel no-padding">
                        <table class="active_board_table erupt_table" style="border:2px solid red; border-collapse: inherit; <c:if test="${activityType eq '8' || activityType eq '13'}">display: none</c:if>">
                            <tr>
                                <td class="ab_left"><label class="title mart5">指定继承活动：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select id="appointActivityKey" class="form-control" style="width: 480px;" <c:if test="${isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
                                            <option value="">请选择</option>   
                                            <c:forEach items="${activityCogLst}" var="item">
                                                <option value="${item.vcodeActivityKey}">${item.vcodeActivityName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">指定现有规则：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <c:if test="${isEditFlag ne '0' or (rebateRuleCog.isValid eq '1' and isEditFlag ne '0') or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">
                                            <input type="hidden" name="appointRebateRuleKey" value="${rebateRuleCog.appointRebateRuleKey}">
                                        </c:if>
                                        <select name="appointRebateRuleKey" class="form-control" style="width:480px;" <c:if test="${isEditFlag ne '0' or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
                                            <option value="">请选择</option>   
                                        </select>
                                        <span style="color: green; margin-left: 5px;">注意：指定后只使用该规则的限制金额和限制瓶数</span>
                                    </div>
                                </td>
                            </tr>
                            </table>
                            <table class="active_board_table erupt_table" style="border:2px solid red; border-collapse: inherit;">
                			<tr>
	                       		<td class="ab_left"><label class="title">限制时间类型：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                       				<div style="float: left; width: 80px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" 
                       						<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>
                       						<c:if test="${rebateRuleCog.restrictTimeType eq '0'}">checked="checked"</c:if> 
                       						style="float: left; height:20px; cursor: pointer;">&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" 
                       						<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>
                       						<c:if test="${rebateRuleCog.restrictTimeType eq '1'}">checked="checked"</c:if> 
                       						style="float: left; height:20px; cursor: pointer;">&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" 
                                            <c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if>
                                            value='<fmt:formatNumber value="${rebateRuleCog.restrictVpoints == 0 ? '' : rebateRuleCog.restrictVpoints}" pattern="0"></fmt:formatNumber>' 
                                            class="form-control number input-width-small rule" autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                      		<tr>
	                       		<td class="ab_left"><label class="title">限制消费金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictMoney" tag="validate" 
	                       					<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if>
	                       					value='<fmt:formatNumber value="${rebateRuleCog.restrictMoney == 0 ? '' : rebateRuleCog.restrictMoney}" pattern="0.00"></fmt:formatNumber>' 
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制消费瓶数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictBottle" tag="validate" 
	                       					<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if> 
	                       					value="${rebateRuleCog.restrictBottle eq '0' ? '' : rebateRuleCog.restrictBottle}" 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户限制获取次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictCount" tag="validate" 
	                       					<c:if test="${not empty(rebateRuleCog.appointRebateRuleKey) or rebateRuleCog.ruleType eq '3' or rebateRuleCog.ruleType eq '4' or rebateRuleCog.ruleType eq '5'}">readonly="readonly"</c:if> 
	                       					value="${rebateRuleCog.restrictCount eq '0' ? '' : rebateRuleCog.restrictCount}" 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	</table>
	                       	<!--------------------------------------------- 首扫限制----------------------------------->
	                       	<table class="active_board_table erupt_table" style="border:2px solid green; border-collapse: inherit;<c:if test="${activityType eq '8' || activityType eq '13'}">display: none</c:if>">
                            <tr>
                                <td class="ab_left"><label class="title">限制总首扫次数：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictFirstTotalBottle" tag="validate" 
                                            value='<fmt:formatNumber value="${rebateRuleCog.restrictFirstTotalBottle == 0 ? '' : rebateRuleCog.restrictFirstTotalBottle}" pattern="0"></fmt:formatNumber>' 
                                            class="form-control number input-width-small rule" autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">次数</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                      		<tr>
	                       		<td class="ab_left"><label class="title">限制总首扫金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictFirstTotalMoney" tag="validate" 
	                       					value='<fmt:formatNumber value="${rebateRuleCog.restrictFirstTotalMoney == 0 ? '' : rebateRuleCog.restrictFirstTotalMoney}" pattern="0.00"></fmt:formatNumber>' 
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制每月首扫次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictFirstMonthBottle" tag="validate" 
	                       					value="${rebateRuleCog.restrictFirstMonthBottle eq '0' ? '' : rebateRuleCog.restrictFirstMonthBottle}" 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">次数</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制每月首扫金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictFirstMonthMoney" tag="validate" 
	                       					value='<fmt:formatNumber value="${rebateRuleCog.restrictFirstMonthMoney == 0 ? '' : rebateRuleCog.restrictFirstMonthMoney}" pattern="0.00"></fmt:formatNumber>' 
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制当天首扫次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictFirstDayBottle" tag="validate" 
	                       					value="${rebateRuleCog.restrictFirstDayBottle eq '0' ? '' : rebateRuleCog.restrictFirstDayBottle}" 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">次数</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制当天首扫金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictFirstDayMoney" tag="validate" 
	                       					value='<fmt:formatNumber value="${rebateRuleCog.restrictFirstDayMoney == 0 ? '' : rebateRuleCog.restrictFirstDayMoney}" pattern="0.00"></fmt:formatNumber>' 
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	</table>
	                       <table class="active_board_table erupt_table">
	                       	<tr>
	                       		<td class="ab_left"><label class="title">是否清空扫码数据：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input type="checkbox" tag="validate" name="isClear" value="1" style="float: left;" <c:if test="${isEditFlag eq '1' or isEditFlag eq '2' or rebateRuleCog.ruleType eq '5'}">disabled="disabled"</c:if>>
	                       				<span class="blocker en-larger"></span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                      </table>
                	</div>

<!--                按钮 -->
                	<div class="active_table_submit mart20">
			            <div class="button_place">
			            	<c:if test="${isEditFlag ne '2'}">
					    		<button class="btn btn-blue btnSave" type="submit">保 存</button> &nbsp;&nbsp;&nbsp;&nbsp;
					    	</c:if>
					    	<button class="btn btnReturn btn-radius3" url="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    <div class="modal fade" id="selectloadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content" style="top:20%;">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title" id="myModalLabel">群组详情</h4>
                    </div>
                    <div class="modal-body">
                        <table class="active_board_table">
                            <tr>
                                <td width="25%" class="text-right"><label class="title">群组描述：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content"> 
                                       <textarea name="desc"  class="form-control required input-width-larger" autocomplete="off" maxlength="30" disabled="disabled"></textarea>
                                       	<label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            
                            <tr>
                                <td width="25%" class="text-right"><label class="title">数据更新周期：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input name="up_cycle" 
                                               class="form-control required input-width-larger" autocomplete="off" maxlength="30" disabled="disabled"/>
                                        <label class="validate_tips" style="width:35%">天</label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" class="text-right"><label class="title">数据有效期：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input name="valid_date" 
                                               class="form-control required input-width-larger" autocomplete="off" maxlength="30" disabled="disabled"/>
                                        <label class="validate_tips" style="width:35%"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" class="text-right"><label class="title">群组过期时间：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                       <input name="valid" tag="validate"
                                           class="form-control required input-width-larger" autocomplete="off" maxlength="30" disabled="disabled" />
                                       <label class="validate_tips" style="width:35%">天</label>					
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td width="25%" class="text-right"><label class="title">群组人数：<span class="required">*</span></label></td>
                                <td>
                                    <div class="content">
                                        <input name="number" tag="validate"
                                               class="form-control required input-width-larger" autocomplete="off" maxlength="30" disabled="disabled"/>
                                        <label class="validate_tips" style="width:35%">人</label>
                                    </div>
                                </td>
                            </tr>
 
                        </table>
                    </div>
                    <div class="modal-footer">
                        <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </body>
</html>
