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
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=1"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
			
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			// 初始化功能
			initPage();
		});
		
		function initPage() {
			
            // 返回
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
                    <!-- 配置规则配置对象 后台解析-->
                    formData.append("activityType", myForm.get("activityType"));
                    formData.append("prizeCogType", myForm.get("prizeCogType"));
                    formData.append("ruleTotalPrize", myForm.get("ruleTotalPrize"));
                    formData.append("firstScanPercent",myForm.get("firstScanPercent"));
                    formData.append("vcodeActivityKey", myForm.get("vcodeActivityKey"));
                    formData.append("ruleUnitMoney", myForm.get("ruleUnitMoney"));
                    formData.append("ruleType", myForm.get("ruleType"));
                    formData.append("endDate", myForm.get("endDate"));
                    if  (myForm.get("moneyDanping") != ''){
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

			// 类型为每天时，日期区域不可用
			$("[name='ruleType']").on("change", function(evt) {
				
				if($(this).val() == '3' || $(this).val() == '4' || $(this).val() == '5'){
					$("[name='hotAreaKey']").attr("disabled",true);
					$("div.erupt-content :input, #addEruptItem, #addEruptPerItem").attr("disabled","disabled");
					
					$("#appointActivityKey").attr("disabled",true);
					$("[name='appointRebateRuleKey']").attr("disabled",true);
                    $("[name='restrictVpoints']").attr("readonly",true);
	                $("[name='restrictMoney']").attr("readonly",true);
	                $("[name='restrictBottle']").attr("readonly",true);
	                $("[name='restrictCount']").attr("readonly",true);
	                $("#restrictTimeType1").attr("disabled",true);
	                $("#restrictTimeType2").attr("disabled",true);
	                
				}else{
                    $("div.erupt-content :input, #addEruptItem, #addEruptPerItem").removeAttr("disabled");

                    $("#appointActivityKey").attr("disabled",false);
					$("[name='appointRebateRuleKey']").attr("disabled",false);
                    $("[name='restrictVpoints']").attr("readonly",false);
	                $("[name='restrictMoney']").attr("readonly",false);
	                $("[name='restrictBottle']").attr("readonly",false);
	                $("[name='restrictCount']").attr("readonly",false);
	                $("#restrictTimeType1").attr("disabled",false);
	                $("#restrictTimeType2").attr("disabled",false);
	                
					if ("${areaName}" != "省外") {
						$("[name='hotAreaKey']").attr("disabled",false);
					}
				}
				
				// 节假日、每天
				if ($(this).val() == '1' || $(this).val() == '2') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input, #addDate").removeAttr("disabled");
                    $("div.week input, #addWeek").attr("disabled", "disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
				}
                
                // 周几
                if ($(this).val() == '3' ) {
                    $("div.date").css("display", "none");
                    $("div.week").css("display", "block");
                    $("div.date input, #addDate").attr("disabled", "disabled");
                    $("div.week input, #addWeek").removeAttr("disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
                }
                
                // 每天
                if ($(this).val() == '4') {
                    $("div.date").css("display", "block");
                    $("div.week").css("display", "none");
                    $("div.date input, #addDate").attr("disabled", "disabled");
                    $("div.week input, #addWeek").attr("disabled", "disabled");
                    $("[name='beginTime'], [name='endTime'], #addTime").removeAttr("disabled");
                }
				$("[name='beginDate']:enabled").val("");
				$("[name='endDate']:enabled").val("");
			});
			$("[name='ruleType']").trigger("change");
			
			$("div.week #beginDate").on("blur", function(){
				var beginVal = $(this).val();
				var endVal = $("div.week #endDate").val();
				if (beginVal != "") {
					if (!$.isNumeric(beginVal) || beginVal > 7 || beginVal < 1) {
						$.fn.alert("请输入1~7的数字");
	                    $(this).val("");
	                    
					} else if(endVal != "" && beginVal > endVal) {
						$.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
					}
				}
			});
			
			/* $("div.week #endDate").on("blur", function(){
				var beginVal = $("div.week #beginDate").val();
				var endVal = $(this).val();
				if (endVal != "") {
					if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1) {
						$.fn.alert("请输入1~7的数字");
	                    $(this).val("");
	                    
					} else if(beginVal != "" && beginVal > endVal) {
						$.fn.alert("日期起始值不可大于终止值!");
                        $(this).val("");
					}
				}
			}); */
			
			// 选择规则
			$("[name='appointRebateRuleKey']").on("change", function(){
				var restrict = $(this).find("option:selected").attr("data-value");
				if(typeof(restrict) != 'undefined' || restrict ==''){
                    $("input[name='restrictVpoints']").val(restrict.split("-")[0]);
                    $("input[name='restrictVpoints']").attr("readonly",true);
                    $("input[name='restrictMoney']").val(Number(restrict.split("-")[1]).toFixed(2));
                    $("input[name='restrictMoney']").attr("readonly",true);
                    $("input[name='restrictBottle']").val(restrict.split("-")[2] == '0' ? "" : restrict.split("-")[2]);
                    $("input[name='restrictBottle']").attr("readonly",true);    
                    $("input[name='restrictCount']").val(restrict.split("-")[3] == '0' ? "" : restrict.split("-")[3]);
                    $("input[name='restrictCount']").attr("readonly",true);
					
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
                    $("input[name='restrictVpoints']").val('');
                    $("input[name='restrictMoney']").val('');
                    $("input[name='restrictBottle']").val('');
                    $("input[name='restrictCount']").val('');
                    $("input[name='restrictVpoints']").attr("readonly",false);
                    $("input[name='restrictMoney']").attr("readonly",false);
                    $("input[name='restrictBottle']").attr("readonly",false);
                    $("input[name='restrictCount']").attr("readonly",false);
					
					// 限制类型
					$("#restrictTimeType1").removeAttr("disabled").trigger("click");
					$("#restrictTimeType2").removeAttr("disabled");
					$("#restrictTimeType").val("");
					$("#restrictTimeType").attr("disabled", true);
					
					
				}
				
			});
			$("div.week").css("display", "none");
			
            // 增加蒙牛组织机构
            $("form").on("click", "#addDepartment", function() {
                
                if ($(this).text() == "新增") {
                    var $areaCopy = $("div.department").eq(0).clone();
                    $areaCopy.find("#addDepartment").text("删除");
                    $areaCopy.find("select[name='dmProvinceAry']").html("<option value=''>请选择</option>");
                    $areaCopy.find("select[name='agencyAry']").html("<option value=''>请选择</option>");
                    $("td.department").append($areaCopy);
                    
                } else {
                    $(this).closest("div.department").remove();
                }

                if ($("td.area div.area").size() > 1) {
                    $("[name='hotAreaKey']").attr("disabled", true);
                    $("[name='appointRebateRuleKey']").attr("disabled", true);
                    $("#appointActivityKey").attr("disabled",true);
                    
                } else {
                    $("div.area select").eq(0).change(); // 触发初始化热区
                    $("[name='hotAreaKey']").attr("disabled", false);
                    $("[name='appointRebateRuleKey']").attr("disabled", false);
                    $("#appointActivityKey").attr("disabled",false);
                }
            });
            
            // 增加青啤组织机构
            $("form").on("click", "#addOrganization", function() {
                
                if ($(this).text() == "新增") {
                    var $organizationCopy = $("div.organization").eq(0).clone();
                    $organizationCopy.find("#addOrganization").text("删除");
                    // $organizationCopy.find("select[name='orgBigRegionAry']").html("<option value=''>请选择</option>");
                    $organizationCopy.find("select[name='orgSecondaryAry']").html("<option value=''>请选择</option>");
                    $("td.organization").append($organizationCopy);
                    
                } else {
                    $(this).closest("div.organization").remove();
                }
            });
			
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
                            areaCode = areaCode + $province.val() + ",";
                        } 
                    });
                    
                    // 加载区域对应的热区
                    findHotAreaListByAreaCode(areaCode);
                    
                }
            });
            
            // 初始化省市县
            var allArea = '${allAreaFlage}'=='1' ? true : false;
            $("div.area").initZone("<%=cpath%>", false, "", true, allArea, true, false);

            // 增加区域
            $("form").on("click", "#addArea", function() {
                
                if ($(this).text() == "新增") {
                    var $areaCopy = $("div.area").eq(0).clone();
                    $areaCopy.find("#addArea").text("删除");
                    $("td.area").append($areaCopy);
                    $areaCopy.initZone("<%=cpath%>", false, "", true, allArea, true, false);
                    
                } else {
                    $(this).closest("div.area").remove();
                }

                if ($("td.area div.area").size() > 1) {
                    $("[name='hotAreaKey']").attr("disabled", true);
                    $("[name='appointRebateRuleKey']").attr("disabled", true);
                    $("#appointActivityKey").attr("disabled",true);
                    
                } else {
                    $("div.area select").eq(0).change(); // 触发初始化热区
                    $("[name='hotAreaKey']").attr("disabled", false);
                    $("[name='appointRebateRuleKey']").attr("disabled", false);
                    $("#appointActivityKey").attr("disabled",false);
                }
            });
            
            
         	// 增加日期
            $("form").on("click", "#addDate", function() {
            	if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "新增") {
            		var count = $(this).closest("td.date").find("div.date").size();
	            	var $dateCopy = $("div.date").eq(0).clone();
	            	$dateCopy.find("#addDate").text("删除");
	            	var $beginDate = $dateCopy.find("#beginDate0");
                    $beginDate.attr("id", "beginDate" + count).val("");
                    $beginDate.attr("onfocus", $beginDate.attr("onfocus").replace("endDate0", "endDate" + count));
	            	var $endDate = $dateCopy.find("#endDate0");
                    $endDate.attr("id", "endDate" + count).val("");
                    $endDate.attr("onfocus", $endDate.attr("onfocus").replace("beginDate0", "beginDate" + count));
	            	$("td.date").append($dateCopy);
            	} else {
            		$(this).closest("div.date").remove();
            	}
            });
            
         	// 增加周几
            $("form").on("click", "#addWeek", function() {
                if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "新增") {
	            	var $weekCopy = $("div.week").eq(0).clone();
	            	$weekCopy.find("#addWeek").text("删除");
                    $weekCopy.find("#beginDate,#endDate").val("");
	            	$("td.date").append($weekCopy);
            	} else {
                    $(this).closest("div.week").remove();
            	}
            });
            
         	// 增加时间
            $("form").on("click", "#addTime", function() {
                if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "新增") {
                    var count = $(this).closest("td.time").find("div.time").size();
	            	var $timeCopy = $("div.time").eq(0).clone();
	            	$timeCopy.find("#addTime").text("删除");
	            	var $beginTime = $timeCopy.find("#beginTime0");
                    $beginTime.attr("id", "beginTime" + count).val("00:00:00");
                    $beginTime.attr("onfocus", $beginTime.attr("onfocus").replace("endTime0", "endTime" + count));
	            	var $endTime = $timeCopy.find("#endTime0");
                    $endTime.attr("id", "endTime" + count).val("23:59:59");
                    $endTime.attr("onfocus", $endTime.attr("onfocus").replace("beginTime0", "beginTime" + count));
	            	$("td.time").append($timeCopy);
            	} else {
	            	$(this).closest("div.time").remove();
            	}
            });

			
            // 返利范围初始化
            if('${allAreaFlage}' == '1'){
            	$("#areaTrId").css("display", "");
                $("#departmentTrId").css("display", "none");
                $("#organizationTrId").css("display", "none");
                $("#rebateScopeTrId").css("display", "none");
            }else {
            	$("#rebateScopeTrId").css("display", "");
            	if('${projectServerName}' == 'mengniu'){
                    $("#areaTrId").css("display", "none");
                    $("#organizationTrId").css("display", "none");
                    $("#departmentTrId").css("display", "");
                    $("#rebateScopeType2").attr("checked", "checked");
                }else if(${not empty(bigRegionList)}){
                    $("#areaTrId").css("display", "none");
                    $("#departmentTrId").css("display", "none");
                    $("#organizationTrId").css("display", "");
                    $("#rebateScopeType2").attr("checked", "checked");
                }else{
                	$("#areaTrId").css("display", "");
                    $("#departmentTrId").css("display", "none");
                    $("#organizationTrId").css("display", "none");
                    $("#rebateScopeTrId").css("display", "none");                
                }
            }
            

            // 返利范围选择
            $("input[name='rebateScopeType']").on("change", function(){
                if($(this).attr("id") == "rebateScopeType1"){
                    $("#areaTrId").css("display", "");
                    $("#departmentTrId").css("display", "none");
                    $("#organizationTrId").css("display", "none");
                }else{
                    $("#areaTrId").css("display", "none");
                    if('${projectServerName}' == 'mengniu'){
                    	$("#departmentTrId").css("display", "");
                    	$("#organizationTrId").css("display", "none");
                    }else if(${not empty(bigRegionList)}){
                    	$("#organizationTrId").css("display", "");
                    	$("#departmentTrId").css("display", "none");
                    }
                    
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
            		$("input[name='groupName']").val("");
            		$("[name='groupId']").html("");
            		
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
                    	if (data["errMsg"]) {
                            $.fn.alert(data["errMsg"]);
                        } else{
                        $("#selectloadDialog").modal("show");
                        var groupInfo = data['groupInfo'];
                        for(var name in groupInfo) {
                        	console.log(name + "_" + "_" + groupInfo[name]);
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
                if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "新增") {
            		var count = $(this).closest("div.erupt-content").find("table").size();
	            	var $cloneItem = $(this).closest("table").clone(true, true);
	            	$cloneItem.addClass("mart30");
	            	$cloneItem.find("div.eruptperitem:gt(0)").remove();
	            	var $eruptStartDate = $cloneItem.find("#eruptStartDate");
	            	$eruptStartDate.attr("id", "eruptStartDate" + count).val("");
	            	$eruptStartDate.attr("onfocus", $eruptStartDate.attr("onfocus").replace("eruptEndDate", "eruptEndDate" + count));
	            	var $eruptEndDate = $cloneItem.find("#eruptEndDate");
	            	$eruptEndDate.attr("id", "eruptEndDate" + count).val("");
	            	$eruptEndDate.attr("onfocus", $eruptEndDate.attr("onfocus").replace("eruptStartDate", "eruptStartDate" + count));
                    var $eruptStartTime = $cloneItem.find("#eruptStartTime");
                    $eruptStartTime.attr("id", "eruptStartTime" + count).val("");
                    $eruptStartTime.attr("onfocus", $eruptStartTime.attr("onfocus").replace("eruptEndTime", "eruptEndTime" + count));
                    var $eruptEndTime = $cloneItem.find("#eruptEndTime");
                    $eruptEndTime.attr("id", "eruptEndTime" + count).val("");
                    $eruptEndTime.attr("onfocus", $eruptEndTime.attr("onfocus").replace("eruptStartTime", "eruptStartTime" + count));
                    $cloneItem.find("#eruptPerKey").val(getUuid());
                    $cloneItem.find("#eruptPerNum, #eruptPerMoney, #eruptPerVpoints, #eruptPerPrize, #eruptPerCount").removeAttr("disabled").data("oldval", "").val("");
	            	$cloneItem.find("#addEruptItem").text("删除")
	            	$(this).closest("div.erupt-content").append($cloneItem);
            	} else {
            		$(this).closest("table").remove();
            	}
            });
            
            // 爆点新增倍数
            $("#eruptPerKey").val(getUuid());
            $("form").on("click", "#addEruptPerItem", function(){
                if ($(this).is("[disabled]")) return;
            	if ($(this).text() == "+") {
	            	$cloneItem = $(this).closest("div").clone(true, true);
                    $cloneItem.find("#eruptPerKey").val(getUuid());
                    $cloneItem.find("#eruptPerNum, #eruptPerMoney, #eruptPerVpoints, #eruptPerPrize, #eruptPerCount").removeAttr("disabled").data("oldval", "").val("");
                    $cloneItem.find("#addEruptPerItem").html("-");
	            	$(this).closest("td.perItem").append($cloneItem);
            	} else {
            		$(this).closest("div").remove();
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
                    		var validFlag = false;
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
            	if (vcodeActivityKey == "") return;
            	$.ajax({
                    type: "POST",
                    url: "<%=cpath%>/vcodeActivityRebateRule/queryAppointRebateRule.do",
                    async: false,
                    data: {"vcodeActivityKey":vcodeActivityKey},
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
                    	}
                    }
                }); 
            });
            
         	// 翻倍返利类型切换
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



		   
		// 查询蒙牛组织机构
		function checkDmRegionAry(dmRegion){
			var $departmentDiv = $(dmRegion).parent(".department");
            var departmentId = $(dmRegion).val();
            if(departmentId == '' || departmentId == null){
                $departmentDiv.find("select[name='dmProvinceAry']").html("<option value=''>请选择</option>");
                $departmentDiv.find("select[name='agencyAry']").html("<option value=''>请选择</option>");
                return;
            } 
            
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/mnDepartment/queryListByPid.do",
                async: false,
                data: {"pid":departmentId},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                    success:  function(result){
                    var content = "<option value=''>请选择</option>";
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0 && item.mdgprovinceid != '' && item.mdgprovinceid != null) {
                                content += "<option value='"+item.id+"'>"+item.dep_name+"</option>";
                            }
                        });
                    }
                    $departmentDiv.find("select[name='dmProvinceAry']").html(content);
                    $departmentDiv.find("select[name='agencyAry']").html("<option value=''>请选择</option>");
                },
                error: function(data){
                    $.fn.alert("组织机构回显错误!");
                }
            });
		}
		
        // 查询经销商
        function checkDmProvinceAry(dmProvince){
            var $departmentDiv = $(dmProvince).parent(".department");
            var id = $(dmProvince).val();
            if(id == '' || id == null){
                $departmentDiv.find("select[name='agencyAry']").html("<option value=''>请选择</option>");
                return;
            } 

            $.ajax({
                type: "POST",
                url: "<%=cpath%>/mnAgency/queryListByMap.do",
                async: false,
                data: {"departmentId":id, "agency_type":"0"},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                    success:  function(result){
                    var content = "<option value=''>请选择</option>";
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0) {
                                content += "<option value='"+item.id+"'>"+item.agency_name+"</option>";
                            }
                        });
                    }
                    $departmentDiv.find("select[name='agencyAry']").html(content);
                },
                error: function(data){
                    $.fn.alert("经销商回显错误!");
                }
            }); 
        }

     	// 查询青啤组织机构
		function checkOrgBigRegionAry(orgBigRegion){
			var $organizationDiv = $(orgBigRegion).parent(".organization");
            var bigRegionName = $(orgBigRegion).val();
            if(bigRegionName == '' || bigRegionName == null){
                $organizationDiv.find("select[name='orgSecondaryAry']").html("<option value=''>请选择</option>");
                return;
            } 
            
            $.ajax({
                type: "POST",
                url: "<%=cpath%>/organization/queryListByBigRegionName.do",
                async: false,
                data: {"bigRegionName":bigRegionName},
                dataType: "json",
                beforeSend:appendVjfSessionId,
                    success:  function(result){
                    var content = "<option value=''>请选择</option>";
                    if(result){
                        $.each(result, function(i, item) {
                            if (i >= 0 && item.secondaryName != '' && item.secondaryName != null) {
                                content += "<option value='"+item.secondaryName+"'>"+item.secondaryName+"</option>";
                            }
                        });
                    }
                    $organizationDiv.find("select[name='orgSecondaryAry']").html(content);
                },
                error: function(data){
                    $.fn.alert("组织机构回显错误!");
                }
            });
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
		
		function changeWeek(obj){
			var beginVal = $(obj).parent(".week").find("input[name='beginDate']").val();
			var endVal = $(obj).val();
			if (endVal != "") {
				if (!$.isNumeric(endVal) || beginVal > 7 || beginVal < 1 || endVal > 7) {
					$.fn.alert("请输入1~7的数字");
                    $(obj).val("");
                    $(obj).parent(".week").find("input[name='beginDate']").val('');
				} else if(beginVal != "" && beginVal > endVal) {
					$.fn.alert("起始值不可大于终止值!");
                       $(obj).val("");
                       $(obj).parent(".week").find("input[name='beginDate']").val('');
				}
			}
		}
		
		function validFile() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}

			if('${empty(areaCode)}'){
				var areaFlag = false;
	            var departmentFlag = false;
	            var organizationFlag = false;
	            if('${projectServerName}' == 'mengniu' && '${allAreaFlage}' != '1'){
	                if($("input[name='rebateScopeType']:checked").val() == "0"){
	                    areaFlag = true;
	                }else{
		                if($("select[name='dmRegionAry']").val() == ''){
		                	$.fn.alert("请选择组织机构!");   
		                	return false;
				        }
	                    departmentFlag = true;
	                }
	            }else if(${not empty(bigRegionList)} && '${allAreaFlage}' != '1'){
	                if($("input[name='rebateScopeType']:checked").val() == "0"){
	                    areaFlag = true;
	                }else{
		                if($("select[name='orgBigRegionAry']").val() == ''){
		                	$.fn.alert("请选择组织机构!");   
		                	return false;
				        }
		                organizationFlag = true;
	                }
	            }else{
	                areaFlag = true;            
	            }

	            // 规则区域
	            if(areaFlag){
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
	                    } 
	                    areaName = areaName + $province.find("option:selected").text() + "_" 
	                    + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
	                });
	                if(areaCode != ""){
	                    $("input[name='filterAreaCode']").val(areaCode.substring(0, areaCode.length - 1));
	                    $("input[name='filterAreaName']").val(areaName.substring(0, areaName.length - 1));  
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

	            // 蒙牛组织机构
	            if(departmentFlag){
	                var departmentIds = "";
	                var departmentNames = "";
	                $("td.department div.department").each(function(i){
	                    var $dmRegionAry = $(this).find("select.zDmRegionAry");
	                    var $dmProvinceAry = $(this).find("select.zDmProvinceAry");
	                    var $agencyAry = $(this).find("select.zAgencyAry");
	                    
	                    if ($dmRegionAry.val() != "") {
	                        departmentIds = departmentIds + $dmRegionAry.val() + ($dmProvinceAry.val() != "" ? "," : ";");
	                    }
	                    if ($dmProvinceAry.val() != "") {
	                        departmentIds = departmentIds + $dmProvinceAry.val() + ($agencyAry.val() != "" ? "," : ";");
	                    }
	                    if ($agencyAry.val() != "") {
	                        departmentIds = departmentIds + $agencyAry.val() + ";";
	                    }
	                     
	                    departmentNames = departmentNames + $dmRegionAry.find("option:selected").text() + "_" 
	                    + $dmProvinceAry.find("option:selected").text() + "_" + $agencyAry.find("option:selected").text() + ";";
	                });
	                if(departmentIds != ""){
	                    $("input[name='filterDepartmentIds']").val(departmentIds.substring(0, departmentIds.length - 1));
	                    $("input[name='filterDepartmentNames']").val(departmentNames.substring(0, departmentNames.length - 1));  
	                }
	            }

	         	// 青啤组织机构 
	            if(organizationFlag){
	                var organizationIds = "";
	                var organizationNames = "";
	                $("td.organization div.organization").each(function(i){
	                    var $orgBigRegionAry = $(this).find("select.zOrgBigRegionAry");
	                    var $orgSecondaryAry = $(this).find("select.zOrgSecondaryAry");
	                    
	                    if ($orgBigRegionAry.val() != "") {
	                        organizationIds = organizationIds + $orgBigRegionAry.val() + ($orgSecondaryAry.val() != "" ? "," : ";");
	                    }
	                    if ($orgSecondaryAry.val() != "") {
	                        organizationIds = organizationIds + $orgSecondaryAry.val() + ";";
	                    }
	                     
	                    organizationNames = organizationNames + $orgBigRegionAry.find("option:selected").text() + "_" 
	                    + $orgSecondaryAry.find("option:selected").text() + ";";
	                });
	                if(organizationIds != ""){
	                    $("input[name='filterOrganizationIds']").val(organizationIds.substring(0, organizationIds.length - 1));
	                    $("input[name='filterOrganizationNames']").val(organizationNames.substring(0, organizationNames.length - 1));  
	                }
	            }
            }
            
            // 规则类型
			var returnFlag = false;
			var dateAry = "";
			var divType = "";
			var ruleType = $("select[name='ruleType']").val();
			if(ruleType == "1" || ruleType == "2"){
				divType = "date";
			}else if(ruleType == "3"){
				divType = "week";
			}
			
			// 组装多组日期
			if(divType != ""){
				$("td.date div." + divType).each(function(i){
	            	var $beginDate = $(this).find("input[name='beginDate']");
	            	var $endDate = $(this).find("input[name='endDate']");
	            	if($beginDate.val() == "" || $endDate.val() == ""){
	            		if(divType == "date"){
	            			$.fn.alert("日期范围不能为空!");	
	            		}else{
	            			$.fn.alert("周几不能为空!");
	            		}
	            		returnFlag = true;
	            		return false; // 相当于break
	            	}
	            	dateAry += $beginDate.val() + "@" + $endDate.val() + ",";
	            });

				if(returnFlag){
					return false;	
				}
				
				if(dateAry != ""){
					$("input[name='filterDateAry']").val(dateAry.substring(0, dateAry.length - 1));
				}	
			}
			
			// 组装多组时间
			var timeAry = "";
			$("td.time div.time").each(function(i){
            	var $beginTime = $(this).find("input[name='beginTime']");
            	var $endTime = $(this).find("input[name='endTime']");
            	if($beginTime.val() == "" || $endTime.val() == ""){
            		$.fn.alert("时间范围不能为空!");
            		returnFlag = true;
            		return false; // 相当于break
            	}
            	timeAry += $beginTime.val() + "@" + $endTime.val() + ",";
            });

			if(returnFlag){
				return false;	
			}
			
			if(timeAry != ""){
				$("input[name='filterTimeAry']").val(timeAry.substring(0, timeAry.length - 1));
			}
			
			if ($("[name='prizeCogType']:checked").val() == '0') {
				var files = $("input.import-file").val();
				if(files == "") {
					$.fn.alert("未选择任何文件，不能导入!");
					return false;
				} else if(files.indexOf("xls") == -1) {
					$.fn.alert("不是有效的EXCEL文件!");
					return false;
				}
			} else {
				if (Number($("input[name='ruleTotalPrize']").val()) <= 0) {
	                $.fn.alert("请输入有效的奖项总数!");
	                return false;
	            }

				// 检验配置项的百分比
                if(!validVpoints()){
                    return false;
                }
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
			                eruptRule = eruptRule + $(this).find("#eruptPerKey").val() + ":" + $(this).find("#eruptPerNum").val() + ":" + $(this).find("#eruptPerMoney").val() + ":" 
			                	+ $(this).find("#eruptPerVpoints").val() + ":" + $(this).find("#eruptPerPrize").val() + ":" + $(this).find("#eruptPerCount").val() + ","
					    });
					    eruptRule += ";";
				    }
				});
				$("input[name='eruptRuleInfo']").val(eruptRule);

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
            	action="<%=cpath %>/vcodeActivityRebateRule/importRebateRuleCogMoneyConfig.do" onsubmit="return validFile();">
                <input type="hidden" name="vcodeActivityKey" value="${activityCog.vcodeActivityKey}" />
                <input type="hidden" name="areaCode" value="${areaCode}" />
                <input type="hidden" name="departmentIds" value="${departmentIds}" />
                <input type="hidden" name="organizationIds" value="${organizationIds}" />
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
	                       				<input name="rebateRuleName" tag="validate"  
	                       					class="form-control required" style="width: 352px;" autocomplete="off" maxlength="100" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                        	<tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
	                       		<td class="ab_left"><label class="title">单瓶成本：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="moneyDanping" tag="validate"  
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
                                        <input type="radio" id="firstScanDanpingLimit" name="firstScanDanpingLimit" value="1" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;计入
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="firstScanDanpingLimit" name="firstScanDanpingLimit" value="0" style="float: left; height:20px; cursor: pointer;">&nbsp;不计入
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title" title="仅限新用户阶梯规则：选择“启用”后，仅新用户可中该规则内配置的阶梯活动规则，该规则无需再进行“活动配置-阶梯规则”双重配置。&#10;如果希望老用户也中该阶梯规则，请单独新建规则与新用户区分，同时在“活动配置-阶梯规则”内同步相关配置。">仅限新用户阶梯规则：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="ruleNewUserLadder" name="ruleNewUserLadder" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;启用
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="ruleNewUserLadder" name="ruleNewUserLadder" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;不启用
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <c:if test="${projectName == '1'}">
                                <tr id="roleInfoTr">
                                    <td class="ab_left"><label class="title">扫码/级联激励角色：<span class="required">*</span></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div class="content roleInfo"  >
                                            <c:if test="${not empty(roleInfoAll)}">
                                                <c:forEach items="${roleInfoAll }" var="roleItem">
                                                    <div style="float: left; margin-right: 10px;">
                                                        <span>${fn:split(roleItem, ':')[1]}</span>
                                                        <input type="radio"  name="allowScanRole"  value="${fn:split(roleItem, ':')[0]}" style="float: left;">
                                                    </div>
                                                </c:forEach>
                                            </c:if>
                                            <label class="validate_tips"></label>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${empty(areaCode) and empty(departmentIds) and empty(organizationIds)}">
                            <tr id="rebateScopeTrId">
                                <td class="ab_left"><label class="title">返利范围<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="radio" id="rebateScopeType1" name="rebateScopeType" value="0" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">区域</span>
                                        <input type="radio" class="radio" id="rebateScopeType2" name="rebateScopeType" value="1" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">组织机构</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            </c:if>
                            <!-- 公共行政区域tr -->
                            <tr id="areaTrId">
                                <input type="hidden" name="filterAreaCode" value="${areaCode}"/>
                                <input type="hidden" name="filterAreaName" />
                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>
                                <td class="ab_main area" colspan="3">
                                 	<c:if test="${empty(areaName)}">
	                                    <div class="area" style="display: flex; margin: 5px 0px;">
		                                    <select name="provinceAry" class="zProvince form-control input-width-normal"></select>
		                                    <select name="cityAry" class="zCity form-control input-width-normal"></select>
		                                    <select name="countyAry" class="zDistrict form-control input-width-normal"></select>
		                                    <c:if test="${allAreaFlage=='0'}">
			                                    <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
		                                    </c:if>
	                                    </div>
                                    </c:if>
                                    <c:if test="${not empty(areaName)}">
                                    <div class="content areaName">
                                        <span>${areaName}</span>
                                    </div>
                                    </c:if>
                                </td>
                            </tr>
                            <!-- 蒙牛组织机构专用tr -->
                            <tr id="departmentTrId">
                                <input type="hidden" name="filterDepartmentIds" value="${departmentIds}"/>
                                <input type="hidden" name="filterDepartmentNames" />
                                <td class="ab_left"><label class="title">组织架构：<span class="required">*</span></label></td>
                                <td class="ab_main department" colspan="3">
                                 	<c:if test="${empty(areaName)}">
	                                    <div class="department" style="display: flex; margin: 5px 0px;">
	                                        <select name="dmRegionAry" class="zDmRegionAry form-control input-width-normal" onchange="checkDmRegionAry(this)">
	                                           <option value="">请选择</option>
	                                           <c:forEach items="${departmentList }" var="department">
	                                               <option value="${department.id }">${department.dep_name }</option>
	                                           </c:forEach>
	                                        </select>
	                                        <select name="dmProvinceAry" class="zDmProvinceAry form-control input-width-normal" onchange="checkDmProvinceAry(this)">
	                                           <option value="">请选择</option>
	                                        </select>
	                                        <select name="agencyAry" class="zAgencyAry form-control input-width-normal">
	                                           <option value="">请选择</option>
	                                        </select>
	                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addDepartment">新增</label>
	                                    </div>
                                    </c:if>
                                    <c:if test="${not empty(areaName)}">
                                    <div class="content areaName">
                                        <span>${areaName}</span>
                                    </div>
                                    </c:if>
                                </td>
                            </tr>
                             <!--  青啤组织机构专用tr -->
                            <tr id="organizationTrId">
                                <input type="hidden" name="filterOrganizationIds" value="${organizationIds}"/>
                                <input type="hidden" name="filterOrganizationNames" />
                                <td class="ab_left"><label class="title">组织架构：<span class="required">*</span></label></td>
                                <td class="ab_main organization" colspan="3">
                                 	<c:if test="${empty(areaName)}">
	                                    <div class="organization" style="display: flex; margin: 5px 0px;">
	                                        <select name="orgBigRegionAry" class="zOrgBigRegionAry form-control input-width-normal" onchange="checkOrgBigRegionAry(this)">
	                                           <option value="">请选择</option>
	                                           <c:forEach items="${bigRegionList }" var="item">
	                                               <option value="${item.bigRegionName }">${item.bigRegionName }</option>
	                                           </c:forEach>
	                                        </select>
	                                        <select name="orgSecondaryAry" class="zOrgSecondaryAry form-control input-width-normal">
	                                           <option value="">请选择</option>
	                                        </select>
	                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addOrganization">新增</label>
	                                    </div>
                                    </c:if>
                                    <c:if test="${not empty(areaName)}">
                                    <div class="content areaName">
                                        <span>${areaName}</span>
                                    </div>
                                    </c:if>
                                </td>
                            </tr>
                			<tr>
                        		<td class="ab_left"><label class="title mart5">规则类型：<span class="white">*</span></label></td>
                        		<td class="ab_main">
                        			<div class="content">
                        			    <select name="ruleType" style="height: 25px;">
	                        			    <c:if test="${dayTypeFlage != '1'}">
	                                            <option value="1">节假日</option>
	                                            <option value="2" selected="selected">时间段</option>
	                                            <option value="3">周几</option>
                                                <option value="4">每天</option>
	                        			    </c:if>
	                        			     <c:if test="${dayTypeFlage == '1'}">
                                                <option value="4">每天</option>
                                            </c:if>
                        			    </select>
                        			    <c:if test="${dayTypeFlage == '1'}">
                        			         <span style="color: green; margin-left: 5px;">注意：首次添加活动规则，规则类型只能选择 "每天"</span>
                        			    </c:if>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="filterDateAry" />
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<div class="content date" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginDate" id="beginDate0" class="Wdate form-control input-width-medium" 
                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate0\')}'})" autocomplete="off"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate0" class="Wdate form-control input-width-medium" 
                                            onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate0\')}'})" autocomplete="off"/>
                                        <c:if test="${allAreaFlage=='0'}">
	                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addDate">新增</label>
			                            </c:if>
                        			</div>
                        			<div class="content week" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginDate" id="beginDate" class="form-control input-width-medium"  autocomplete="off"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium" onblur="changeWeek(this)" autocomplete="off"/>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addWeek">新增</label>
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
                                        <input name="endTime" id="endTime0" class="form-control input-width-medium Wdate"
                                        onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime0\')}'})" value="23:59:59" autocomplete="off"/>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addTime">新增</label>
                        			</div>
                        		</td>
                        	</tr>
                      
                        <c:if test= "${groupSwitch eq '1'}" >
                        	 <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">精细化营销：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="isGroupUser" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;全部用户
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>
                            <c:if test= "${allAreaFlage != '1' and dayTypeFlage != '1'}">
                            <tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <input type="hidden" name="groupName" />
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                <div style="line-height: 35px;">
                                         <div style="display: flex;">
                                             <div style="float: left; width: 80px; line-height: 25px;">
                                                  <input type="radio" name="isGroupUser"  value="1" style="float: left; height:20px; cursor: pointer;" >
                                                  <span>&nbsp;群组用户</span>
                                             </div>
                                             <select name="groupId" id ="groupId" class="form-control input-width-larger marl5" style="display:none;" >                      			         	
                        			             	<option value="">请选择</option> 
                        			         </select>
                        			         <a class="btn btn-xs edit btn-blue" id="selectGroup" class="btn btn-blue" style="display:none;">查看群组详情</a>
                                         </div>
                                 </div>
                                </td>                                
                            </tr>
                            </c:if>
 						</c:if>
                        
                            <tr>
                                <td class="ab_left"><label class="title">特殊优先级标签：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="specialLabel" tag="validate"  
                                            class="form-control input-width-larger" autocomplete="off" maxlength="50" />
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
                                        <input type="radio" class="radio" name="redPacketRain" value="0" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">不开启</span>
                                        <input type="radio" class="radio" name="redPacketRain" value="1" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">开启</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        	<tr <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">翻倍区间类型<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="radio" name="allowanceaRebateType" value="0" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">金额</span>
                                        <input type="radio" class="radio" name="allowanceaRebateType" value="1" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="allowanceMoneyTr" <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">翻倍红包区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="allowanceaMinMoney" tag="validate"
                                            class="form-control number money preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="allowanceaMaxMoney" tag="validate"
                                            class="form-control number money sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="allowanceVpointsTr" style="display: none;" <c:if test="${activityType eq '8'}">style="display: none"</c:if>>
                                <td class="ab_left"><label class="title">翻倍积分区间：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="allowanceaMinVpoints" tag="validate"
                                            class="form-control number preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0" maxVal="9999"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="allowanceaMaxVpoints" tag="validate"
                                            class="form-control number sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0" maxVal="9999"/>
                                        <span id="doubtMoneylable" class="blocker en-larger">分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			
                        	<tr>
                                <td class="ab_left"><label class="title mart5">限定热区：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main">
                                    <div class="content hot">
                                        <select id="hotAreaKey" name="hotAreaKey" class="hotArea form-control" style="width: 352px;" 
                                            <c:if test="${areaName ne '省外' or dayTypeFlage eq '1' or allAreaFlage eq '1'}">disabled="disabled"</c:if>>
                                            <option value="">请选择</option>   
                                            <c:if test="${not empty(hotAreaList) }">
                                                <c:forEach items="${hotAreaList}" var="hotArea">
                                                    <option value="${hotArea.hotAreaKey }" >${hotArea.hotAreaName}</option> 
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
                                                 <input name="prizeCogType" type="radio" style="margin: auto 0px;" value="0"> 
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
	                        			         <input name="prizeCogType" type="radio" style="margin: auto 0px;" value="1" checked="checked"> 
	                        			         <span>&nbsp;选择配置方案：</span>
	                        			     </div>
                        			         <select id="rebateRuleTemplet" class="form-control input-width-larger marl5">
                        			             <option>请选择</option>
                        			             <c:if test="${not empty rebateRuleTempletLst}">
                        			                 <c:forEach items="${rebateRuleTempletLst}" var="item">
	                        			                 <option value="${item.infoKey}" data-allowancearebatetype="${item.allowanceaRebateType}" data-allowanceaminmoney="${item.allowanceaMinMoney}" data-allowanceamaxmoney="${item.allowanceaMaxMoney}" data-allowanceaminvpoints="${item.allowanceaMinVpoints}" data-allowanceamaxvpoints="${item.allowanceaMaxVpoints}">${item.templetName }</option>
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
<!--                         	预览配置方案内容 -->
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
                                            class="form-control required" autocomplete="off" maxlength="200" ></textarea>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
<!--                爆点规则 (只有扫码活动且规则类型为节假日及时间段可配置） -->
                    <c:if test="${ (activityType == '0' || activityType eq '13') and dayTypeFlage ne '1'}">
                	<div class="widget-header"><h4><i class="iconfont icon-daoru erupt-title"></i>爆点规则</h4></div>
                	<div class="widget-content panel no-padding erupt-content">
                        <input type="hidden" name="eruptRuleInfo"/>
                        <table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title">中出次数限制：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 120px; line-height: 25px;">
                                        <input type="radio" id="eruptRestrictTimeType1" name="eruptRestrictTimeType" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;规则时间内一次
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="eruptRestrictTimeType2" name="eruptRestrictTimeType" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;每天一次
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" id="eruptRestrictTimeType3" name="eruptRestrictTimeType" value="2" style="float: left; height:20px; cursor: pointer;">&nbsp;不限制
                                    </div>
                                    <span class="blocker en-larger"></span>
                                </td>
                            </tr>
                		</table>
                		<table class="active_board_table erupt_table">
                            <tr>
                                <td class="ab_left"><label class="title">日期限制：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex;">
                                        <span class="blocker">从</span>
                                        <input id="eruptStartDate" class="form-control input-width-medium Wdate preTime eruptStartDate"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'eruptEndDate\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input id="eruptEndDate" class="form-control input-width-medium Wdate sufTime eruptEndDate"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'eruptStartDate\')}'})" />
                                        <label id="addEruptItem" class="blocker marl15 btn-txt-add-red">新增</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">时间限制：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="margin: 5px 0px; display: flex;">
                                        <span class="blocker">从</span>
                                        <input id="eruptStartTime" class="form-control input-width-medium Wdate preTime eruptStartTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', startDate:'00:00:00', maxDate:'#F{$dp.$D(\'eruptEndTime\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input id="eruptEndTime" class="form-control input-width-medium Wdate sufTime eruptEndTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', startDate:'23:59:59', minDate:'#F{$dp.$D(\'eruptStartTime\')}'})" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="vertical-align: top;"><label class="title mart5">每隔分钟：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main perItem">
                                    <div class="content eruptperitem" style="margin: 5px 0px; display: flex;">
                                        <input id="eruptPerKey" type="hidden" class="form-control" data-oldval="" autocomplete="off" />
                                        <input id="eruptPerNum" tag="validate" class="form-control input-width-small number" data-oldval="" autocomplete="off" maxlength="9"/>
                                        <span class="blocker en-larger marl30">金额</span>
                                        <input id="eruptPerMoney" tag="validate" class="form-control input-width-small number money" data-oldval="" autocomplete="off" maxlength="9"/>
                                        <span class="blocker en-larger marl30">积分</span>
                                        <input id="eruptPerVpoints" tag="validate" class="form-control input-width-small number integer" data-oldval="" autocomplete="off" maxlength="9"/>
                                        <span class="blocker en-larger marl30">大奖</span>
                                        <select id="eruptPerPrize" tag="validate" class="form-control input-width-larger" data-oldval="" autocomplete="off">
	                                        <option value="">请选择</option>
	                                        <c:forEach items="${prizeTypeMap}" var="item">
	                                        <c:if test="${fn:startsWith(item.key, 'P00')}">
	                                            <option value="${item.key}">${item.value}</option>
	                                        </c:if>
	                                        </c:forEach>
	                                    </select>
	                                    <span class="blocker en-larger marl30">每天投放个数</span>
                                        <input id="eruptPerCount" tag="validate" class="form-control input-width-small number integer" data-oldval="" autocomplete="off" maxlength="9"/>
                                        <span id="addEruptPerItem" class="marl10 btn-txt-add-red" style="font-size: 20px !important; font-weight: bold; cursor: pointer; width: 30px; text-align: center;">+</span>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	</c:if>
<!--                 	限制规则 -->
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table erupt_table" style="border:2px solid red; border-collapse: inherit; <c:if test="${activityType eq '8' || activityType eq '13'}">display: none</c:if>">
                            <tr>
                                <td class="ab_left"><label class="title mart5">指定继承活动：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select id="appointActivityKey" class="form-control" style="width: 480px;" <c:if test="${dayTypeFlage == '1'}">disabled="disabled"</c:if>>
                                            <option value="">请选择</option>   
                                            <c:forEach items="${activityCogLst}" var="item">
                                                <option value="${item.vcodeActivityKey}">${item.vcodeActivityName}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">指定继承规则：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <select name="appointRebateRuleKey" class="form-control" style="width: 480px;" <c:if test="${dayTypeFlage == '1'}">disabled="disabled"</c:if>>
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
                       					<input type="radio" id="restrictTimeType1" name="restrictTimeType" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" id="restrictTimeType2" name="restrictTimeType" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
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
	                       				<input name="restrictMoney" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
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
	                       				<input name="restrictBottle" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制用户获取次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictCount" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">次</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                		<!--------------------------------------------- 首扫限制----------------------------------->
                        <!---转盘抽奖不显示首扫限制--------->
                		 <table class="active_board_table erupt_table" style="border:2px solid green; border-collapse: inherit;<c:if test="${activityType eq '8' || activityType eq '13'}">display: none</c:if>">
                            <tr>
                                <td class="ab_left"><label class="title">限制总首扫次数：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictFirstTotalBottle" tag="validate"
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
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" type="submit">保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
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
