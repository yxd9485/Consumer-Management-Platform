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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
    
	<script>
			
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#import_form"));
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
	
			 if ("${groupSwitch}" == '1' ) {
				    document.getElementById("showGroup").style.display="none";//隐藏	
					$("input[name='groupName']").val('');
		    		$("#groupId").val('');
	        }
			
    		
			// 类型为每天时，日期区域不可用
			$("[name='ruleType']").on("change", function(evt) {
				
				// 节假日、时间段
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
			
			$("div.week").css("display", "none");
            // 初始化省市县
            $("div.area").initZone("<%=cpath%>", false, "", true, false, true, false);
            // 初始当前活动
            $("#filterVcodeActivityKey").val("${activityCog.vcodeActivityKey}");
            // 初始当前规则类型
            $("[name='ruleType']").val("${rebateRuleCog.ruleType}").trigger("change");
			
            // 增加活动
            $("form").on("click", "#addActivity", function() {
                if ($(this).text() == "新增") {
                    var $activity = $("div.activity").eq(0).clone();
                    $activity.find("#addActivity").text("删除");
                    $("td.activity").append($activity);
                    
                } else {
                    $(this).closest("div.activity").remove();
                }
            });
            
            // 增加区域
            $("form").on("click", "#addArea", function() {
                if ($(this).text() == "新增") {
                    var $areaCopy = $("div.area").eq(0).clone();
                    $areaCopy.find("#addArea").text("删除");
                    $("td.area").append($areaCopy);
                    $areaCopy.initZone("<%=cpath%>", false, "", true, false, true, false);
                    
                } else {
                    $(this).closest("div.area").remove();
                }
            });

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
            
            // 群组用户
            $("input[name='isGroupUser']").click(function () {
            	// 如果选择了群组用户那么要默认的值
            	 if (this.checked) {         
            			document.getElementById("showGroup").style.display="";//显示
            			$("[name='groupId']").css("display", "flex");
            			var opts= $("#groupId").find("option:selected").text();
            			if (opts != "" && opts != "请选择") { 
            				console.log(opts);
               				$("input[name='groupName']").val(opts);   
               				$("#selectGroup").css("display", "flex"); 
            			}         		
            	} else{
            		document.getElementById("showGroup").style.display="none";//隐藏	
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
            
            // 查看群组弹窗
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
		
		
		function validFile() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}

            // 组装活动
            var vcodeActivityKeyAry = "";
            $("td.activity #filterVcodeActivityKey").each(function(i){
            	vcodeActivityKeyAry += $(this).val() + ",";
            });
            $("input[name='filterVcodeActivityKey']").val(vcodeActivityKeyAry);
            console.log("filterVcodeActivityKey" + vcodeActivityKeyAry);
			











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
	        console.log("filterDateAry" + dateAry);
			
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
            console.log("filterTimeAry" + timeAry);
			
			// 防重复提交
			$(".btnSave").attr("disabled", "disabled");
			return true;
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
    <div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="import_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vcodeActivityRebateRule/cloneRebateRuleCog.do" onsubmit="return validFile();">
                <input type="hidden" name="vcodeActivityKey" value="${activityCog.vcodeActivityKey}" />
                <input type="hidden" name="rebateRuleKey" value="${rebateRuleCog.rebateRuleKey}" />
                <input type="hidden" name="areaCode" value="${areaCode}" />
                <input type="hidden" name="activityType" value="${activityType}" />
            	<div class="widget box">
            		<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>基础信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                        		<td class="ab_left"><label class="title mart5">活动名称：<span class="white">*</span></label></td>
                        		<td class="ab_main activity">
                        		    <input type="hidden" name="filterVcodeActivityKey">
                        			<div class="content activity" style="display: flex; margin: 5px 0px;">
                        				<select class="form-control required" id="filterVcodeActivityKey" tag="validate" style="width: 360px;">
                                            <option value="">请选择</option>
                                            <c:forEach items="${activityCogList}" var="item">
	                                           <option value="${item.vcodeActivityKey}">
	                                               ${item.vcodeActivityName}
		                                           <c:choose>
		                                               <c:when test="${item.isBegin eq '0'}">(待上线)</c:when>
		                                               <c:when test="${item.isBegin eq '1'}">(已上线)</c:when>
		                                               <c:when test="${item.isBegin eq '2'}">(已下线)</c:when>
		                                           </c:choose>
	                                           </option>
	                                        </c:forEach>
	                                    </select>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addActivity">新增</label>
                                        <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
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
                                <input type="hidden" name="filterAreaCode" />
                                <input type="hidden" name="filterAreaName" />
                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>
                                <td class="ab_main area" colspan="3">
                                    <div class="area" style="display: flex; margin: 5px 0px;">
	                                    <select name="provinceAry" class="zProvince form-control input-width-normal"></select>
	                                    <select name="cityAry" class="zCity form-control input-width-normal"></select>
	                                    <select name="countyAry" class="zDistrict form-control input-width-normal"></select>
	                                    <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
                                    </div>
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
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addDate">新增</label>
                        			</div>
                        			<div class="content week" style="margin: 5px 0px;">
                                        <span class="blocker">从</span>
                                        <input name="beginDate" id="beginDate" class="form-control input-width-medium" autocomplete="off"/>
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
                        	<tr>
	                       		<td class="ab_left"><label class="title mart5">是否克隆爆点规则：<span class="white">*</span></label></td>
	                       		<td class="ab_main">
	                       			<div class="content erupt">
	                       				<input type="checkbox" name="isCloneErupt" value="1" style="float: left; margin-top: 9px;">
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                        <c:if test= "${groupSwitch eq '1'}">
      
                            <tr>
	                       		<td class="ab_left"><label class="title mart5">是否克隆群组用户：<span class="white">*</span></label></td>
	                       		<td class="ab_main">
	                       			<div class="content erupt">
	                       				<input type="checkbox" name="isGroupUser" id ="isGroupUser" value="1" style="float: left; margin-top: 9px;">
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            
                           
                            <tr>
                                <input type="hidden" name="groupName" />
                                <td class="ab_left"><label class="title"><span class="required"></span></label></td>
                                <td class="ab_main">
                                <div style="line-height: 35px;">
                                         <div style="display: flex;">
                                         		<div id="showGroup" style="float: left; width: 80px; line-height: 25px;">     
                                                  <input type="radio" value="1" style="float: left; height:20px; cursor: pointer;" checked="checked" >
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
                        			         <a class="btn btn-xs edit btn-red" id="selectGroup" class="btn btn-blue"  style="display:none;">查看群组详情</a>
                                         </div>
                                 </div>
                                </td>                                
                            </tr>
                           </c:if>
	                       	
	                       	
	                       	
                		</table>
                	</div>
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>注意事项</h4></div>
                	<div class="widget-content panel no-padding">
               		<table class="active_board_table">
                           <tr>
                               <td class="ab_left"><label class="title mart5">克隆规则：</label></td>
                               <td class="ab_main">
                                   <div class="content">基础信息模块：筛选区域、规则类型、日期范围、时间范围和导入配置项无法被克隆</div>
                                   <div class="content">中奖规则模块：指定热区和指定现有规则两项无法被克隆</div>
                                   <div class="content">勾选“是否克隆爆点规则”会将被克隆活动的爆点规则克隆到新规则中</div>
                               </td>
                           </tr>
                           </table>
               			</div>
                	<div class="widget-header" style="display: none;"><h4><i class="iconfont icon-daoru"></i>中奖规则</h4></div>
	                	<div class="widget-content panel no-padding" style="display: none;">
                		<table class="active_board_table">
                            <tr <c:if test="${activityType == '4' or activityType == '5' }"> style="display: none;"</c:if>>
                                <td class="ab_left"><label class="title mart5">爆点规则：</label></td>
                                <td class="ab_main">
                                    <div class="content">
                                        <div class="content">
                                        	${rebateRuleCog.eruptRuleInfo }
                        				</div>
                                    </div>
                                </td>
                            </tr>
                            </table>
                			</div>
                            
                            <div class="widget-header" style="display: none;"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                			<div class="widget-content panel no-padding" style="display: none;">
                			<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">限制时间类型：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                       				<div style="float: left; width: 80px; line-height: 25px;">
                       					<c:if test="${rebateRuleCog.restrictTimeType eq '0'}">规则时间</c:if>
                       					<c:if test="${rebateRuleCog.restrictTimeType eq '1'}">每天</c:if>
                       				</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                    	<fmt:formatNumber value="${rebateRuleCog.restrictVpoints == 0 ? '' : rebateRuleCog.restrictVpoints}" pattern="0"></fmt:formatNumber>
                                    </div>
                                </td>
                            </tr>
                      		<tr>
	                       		<td class="ab_left"><label class="title">限制消费金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<fmt:formatNumber value="${rebateRuleCog.restrictMoney == 0 ? '' : rebateRuleCog.restrictMoney}" pattern="0.00"></fmt:formatNumber>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制消费瓶数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				${rebateRuleCog.restrictBottle eq '0' ? '' : rebateRuleCog.restrictBottle}
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户限制获取次数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				${rebateRuleCog.restrictCount eq '0' ? '' : rebateRuleCog.restrictCount}
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" type="submit">保存</button>&nbsp;&nbsp;&nbsp;&nbsp;
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
