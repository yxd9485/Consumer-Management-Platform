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
    <title>修改活动</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js"></script>
	<script>
	var excelStatus=false;
	$(document).ready(function () {
		$("#dataTable_data tbody tr:nth-last-of-type(1)").css("background","#FFA73C");
		$("#dataTable_data tbody tr:nth-last-of-type(1) td").css("background","none");
		// 初始化校验控件
		$.runtimeValidate($("form"));
		
		initPage();		
        
        // 增加区域
        $("form").on("click", "#addArea", function() {
            if ($(this).is("[disabled='disabled']")) return;
            if ($(this).text() == '新增') {
                var $copyArea = $(this).closest("div").clone();
                $copyArea.initZone("<%=cpath%>", true, "", true);
                $copyArea.find("#addArea").text("删除");
                $(this).closest("td").append($copyArea);
                
            } else {
                $(this).closest("div").remove();
            }
        });
        // 初始化区域事件
        $("#addArea").closest("div").initZone("<%=cpath%>", true, "", true);
        
        // 初始化筛选区域
        var filterAreaCodeAry = "${bean.filterAreaCode}".split(",");
        $.each(filterAreaCodeAry, function(idx, val, ary){
            if (val != '') {
                if (idx > 0) $("#addArea").click();
                $("td.area div:last-child").initZone("<%=cpath%>", true, val, true);
            }
        });
        
		$("#batchFile").bind('change',function(){
			var formData = new FormData();
		    var name = $("#batchFile").val();
		    formData.append("file",$("#batchFile")[0].files[0]);
		    formData.append("name",name);
		    $.ajax({
		        url : '<%=cpath %>/seckill/beforeCheckFile.do',
		        type : 'POST',
		        async : false,
		        data : formData,
		        processData : false,
		        contentType : false,
		        beforeSend:appendVjfSessionId,
                    success:  function(data) {
		        	if(data.indexOf("error")>-1){
		        		excelStatus=false;
		        		alert(data);
		        		return;
		        	}
		        	excelStatus=true;
		        	var strHtml="";
               		var jdata=eval("(" + data + ")"); 
               		var len=jdata.length;
					$(jdata).each(function(i,k){
						
						if(i==(len-1)){
							strHtml+="<tr style='background-color:#FFA73C'>";
							strHtml+="<td class='text_center' style='vertical-align: middle;'>累计</td>";
						}else{
							strHtml+="<tr>";
							strHtml+="<td class='text_center' style='vertical-align: middle;'>"+(i+1) +"</td>";
						}
				 		strHtml+="<td class='text_center' style='vertical-align: middle;'>"+k.prizeType +"</td>";
				 		strHtml+="<td class='text_center' style='vertical-align: middle;'>"+k.vcodeMoney +"</td>";
				 		strHtml+="<td class='text_center' style='vertical-align: middle;'>"+k.cogamounts +"</td>";
				 		strHtml+="<td class='text_center' style='vertical-align: middle;'>"+k.rangeVal +"</td>";
				 		strHtml+="<td class='text_center' style='vertical-align: middle;'>"+k.percent+"</td>";
				 		strHtml+="</tr>";
				 	});
// 					strHtml+="</tbody>";
					$("#dataTable_data tbody").html("");
					$("#dataTable_data tbody").html(strHtml);
					$("#dataTable_data tbody tr:nth-last-of-type(1)").css("background","#FFA73C");
					$("#dataTable_data tbody tr:nth-last-of-type(1) td").css("background","none");
		        }
		    });
		});
	});
		 
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			// 页面校验
			var v_flag = true;
			$(".validate_tips:not(:hidden)").each(function(){
				if($(this).text() != ""){
					alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
			var batchFile=$("#batchFile").val();
			if(batchFile&&!excelStatus){
				alert("文件内容有误");
				return false;
			}
			
			
			
			var timeSet=document.getElementById("seckillTimeSet").value;
			if(timeSet){
				var res=checkDate(timeSet);
				if(res!=true){
					alert(res);
					return false;
				}
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
            $("input[name='filterAreaCode']").val(areaCode);
            $("input[name='filterAreaName']").val(areaName);
						
			var nameFlag=false;
			var seckillName=document.getElementById("seckillName").value;
			var seckillId=document.getElementById("seckillId").value;
			$.ajax({
				url:'<%=cpath%>/seckill/checkName.do?seckillName='+seckillName+'&seckillId='+seckillId,
				async: false,
				type: 'POST',
					beforeSend:appendVjfSessionId,
                    success: function(data){
						if(data=='SUCCESS'){
							nameFlag=true;
						}else{
							nameFlag=false;
						}
					},
					error:function(data){
	           			alert('fail');
	     			 }
			});
			if(!nameFlag){
				alert("活动名称重复");
				return false;
			}
			$('#seckillType').attr("disabled",false);
			$('#seckillTimeSet').attr("disabled",false);
			$("input:radio").attr("disabled",false);
			return true;
		}
		
		function initPage() {
			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
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
		}
		function checkDate(str){
			var dateList = str.split('#');
			var dateListObj = [];
			for(var i=0;i<dateList.length;i++){
				if(dateList[i].split(',').length!=2){
					return '格式有误：时间段格式错误';
				}else{
					var startTime = dateList[i].split(',')[0];
					var endTime = dateList[i].split(',')[1];
					if(checkTime(startTime,'开始时间')===true){
						if(checkTime(endTime,'结束时间')===true){
							return compareTime(startTime,endTime);
						}else{
							return checkTime(endTime,'结束时间');
							break;
						}
					}else{
						return checkTime(startTime,'开始时间');
						break;
					}
				}
				
				
			}
		}
		function checkTime(time,msg){
			if(time.split(':').length!=3){
				return alert(msg+'格式有误');
			}else{
				var h = time.split(':')[0],
					m = time.split(':')[1],
					s = time.split(':')[2];
				
				if(h>23||h<0||m>59||m<0||s>59||s<0){
					return '格式有误：'+msg+'格式有误';
				}else if(h.length>2||s.length>2||m.length>2){
					return '格式有误：'+msg+'格式有误';
				}else{
					return true;
				}
			}
		}
		
		function compareTime(startTime,endTime){
			var sNum = startTime.split(':')[0]*3600+startTime.split(':')[1]*60+startTime.split(':')[2]*1;
			var eNum = endTime.split(':')[0]*3600+endTime.split(':')[1]*60+endTime.split(':')[2]*1;
			if(isNaN(sNum)||isNaN(eNum)){
				return '时间区间格式有误';
			}
			if(sNum>=eNum){
				return '格式有误：结束时间小于开始时间！'
			}else{
				return true;
			}
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
        	<li class="current"><a> 秒杀活动</a></li>
        	<li class="current"><a title=""> 修改活动</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath %>/seckill/updateSeckill.do">
            	<input type="hidden" name="seckillVcode" id="seckillVcode" value="${bean.seckillVcode}">
                <input type="hidden" name="seckillUrl" id="seckillUrl" value="${bean.seckillUrl}">
                <input type="hidden" name="seckillId" id="seckillId" value="${bean.seckillId}">
                <input type="hidden" name="seckillRuleKey" id="seckillRuleKey" value="${bean.seckillRuleKey}">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>活动信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content seckillName">
	                       				<input name="seckillName" id="seckillName" tag="validate" value="${bean.seckillName}"
	                       					class="form-control required" autocomplete="off" maxlength="50" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">活动类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content typeInfo">
	                       			<select name="seckillType" id="seckillType" disabled tag="validate" class="form-control required input-width-large search" >
								    	<option value="">请选择</option>
								    	<option value="1" <c:if test="${bean.seckillType eq '1'}"> selected="selected"</c:if>>电视秒杀</option>
							    		<option value="2" <c:if test="${bean.seckillType eq '2'}"> selected="selected"</c:if>>商场秒杀</option>
							    		<option value="3" <c:if test="${bean.seckillType eq '3'}"> selected="selected"</c:if>>文章秒杀</option>
							    		<option value="4" <c:if test="${bean.seckillType eq '4'}"> selected="selected"</c:if>>红包雨秒杀</option>
								    </select>
                                       	<label class="validate_tips"></label>
                                       	<label class="ajax_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">活动状态：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content seckillStatus">
		                       			<input type="radio" name="seckillStatus" id="seckillStatus" value="1" <c:if test="${bean.seckillStatus eq '1'}">  checked="checked"</c:if>/><label>开启</label>
		                       			<input type="radio" name="seckillStatus" id="seckillStatus" value="0" <c:if test="${bean.seckillStatus eq '0'}">  checked="checked"</c:if>/><label>关闭</label>
		                       		</div>
	                       		</td>
	                       	</tr>
                            <tr>
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
							<tr>
								<td class="ab_left"><label class="title mart5">限定热区：<span class="required">&nbsp;</span></label></td>
								<td class="ab_main">
									<div class="content hot">
										<select id="hotAreaKey" name="hotAreaKey" class="hotArea form-control" style="width: 352px;">
											<option value="">请选择</option>
											<c:if test="${not empty(hotAreaList) }">
												<c:forEach items="${hotAreaList}" var="hotArea">
													<option value="${hotArea.hotAreaKey }" <c:if test="${hotArea.hotAreaKey == bean.hotAreaKey }">selected="selected"</c:if>>${hotArea.hotAreaName}</option>
												</c:forEach>
											</c:if>
										</select>
										<span style="color: green; margin-left: 5px;">注意：指定该热区后，只有热区内才能扫码</span>
									</div>
								</td>
							</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">日期限制：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content seckillDateType">
		                       			<input type="radio" name="seckillDateType" id="seckillDateType"  disabled  value="1" <c:if test="${bean.seckillDateType eq '1'}">  checked="checked"</c:if>/><label>限制时间</label>
		                       			<input type="radio" name="seckillDateType" id="seckillDateType"  disabled  value="2" <c:if test="${bean.seckillDateType eq '2'}">  checked="checked"</c:if>/><label>限制时间与时间段</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">活动时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="startDate" class="form-control input-width-medium required Wdate preTime" value="${bean.startDate}"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input name="endDate" class="form-control input-width-medium required Wdate sufTime" value="${bean.endDate}"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">当日时间段：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content seckillTimeSet">
	                       				<textarea name="seckillTimeSet" id="seckillTimeSet"
	                       				<c:if test="${bean.seckillType ne '1'}"> disabled</c:if>
	                       				 tag="validate" rows="5" cols="100" >${bean.seckillTimeSet}</textarea>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">是否关注扫码：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
		                       		<div class="content followScanType">
		                       			<input type="radio" name="followScanType" id="followScanType" value="1" <c:if test="${bean.followScanType eq '1'}">  checked="checked"</c:if>/><label>是</label>
		                       			<input type="radio" name="followScanType" id="followScanType" value="0"  <c:if test="${bean.followScanType eq '0'}">  checked="checked"</c:if>/><label>否</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">积分红包总个数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="seckillCountLimit" tag="validate"  value="${bean.seckillCountLimit}"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">个</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">积分红包总金额：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="seckillMoneyLimit" tag="validate"  value="${bean.seckillMoneyLimit}" 
	                       					class="form-control required money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户扫码限制类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">

		                       		<div class="content userLimitType">
		                       			<input type="radio" name="userLimitType" id="userLimitType" disabled value="1"  <c:if test="${bean.userLimitType eq '1'}">  checked="checked"</c:if>/><label>总个数</label>
		                       			<input type="radio" name="userLimitType" id="userLimitType" disabled value="2" <c:if test="${bean.userLimitType eq '2'}">  checked="checked"</c:if>/><label>单日个数</label>
		                       			<input type="radio" name="userLimitType" id="userLimitType" disabled value="3" <c:if test="${bean.userLimitType eq '3'}">  checked="checked"</c:if>/><label>单时间段个数</label>
		                       		</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户扫码上限：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="seckillUserlimit" tag="validate"  value="${bean.seckillUserlimit}"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">个</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	
	                       	 <tr>
	                       		<td class="ab_left"><label class="title">金额配置：</label></td>
	                       		<td class="ab_main">
	                       			<div class="content">
	                       				<input style="bottom: 0px;" name="batchFile" id="batchFile" class="import-file" type="file" single/>
                                        <a href="<%=cpath%>/upload/cogFolder/${projectServerName}/秒杀奖项配置项模板.xlsx?v=<%=System.currentTimeMillis()%>">模板下载</a>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                	<c:choose>
	                    <c:when test="${fn:length(moneyList) gt 0}">
	                    	<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                                   id="dataTable_data" style="background-color:#EEEEEE">
		                       <thead>
		                          <tr>
		                              <th  class="text_center"  style="width: 15%;vertical-align: middle;">金额概率表</th>
		                              <th  class="text_center" style="width: 15%;vertical-align: middle;">类型</th>
		                              <th  class="text_center" style="width: 15%;vertical-align: middle;">金额</th>
		                              <th  class="text_center" style="width: 20%;vertical-align: middle;">个数</th>
		                              <th  class="text_center" style="width: 20%;vertical-align: middle;">权重</th>
		                              <th  class="text_center" style="width: 15%;vertical-align: middle;">概率</th>
		                          </tr>
		                      </thead>
		                       <tbody>
		                        <c:forEach items="${moneyList}" var="sec" varStatus="status">
			                        <tr>
	                                  <td class="text_center" style="vertical-align: middle;">${status.count}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${sec.prizeType}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${sec.vcodeMoney}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${sec.cogamounts}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${sec.rangeVal}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${sec.percent}</td>
	                                </tr>
		                        </c:forEach>
		                       	<tr style="background-color:#FFA73C">
	                                  <td class="text_center" style="vertical-align: middle;">累计</td>
	                                  <td class="text_center" style="vertical-align: middle;">--</td>
	                                  <td class="text_center" style="vertical-align: middle;">${moneyAll.vcodeMoney}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${moneyAll.cogamounts}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${moneyAll.rangeVal}</td>
	                                  <td class="text_center" style="vertical-align: middle;">${moneyAll.percent}</td>
	                             </tr>	 
		                       </tbody>
                  </table>
	                    </c:when>
                    </c:choose>
                	
                                   
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/seckill/getSeckillList.do">返 回</button>
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
