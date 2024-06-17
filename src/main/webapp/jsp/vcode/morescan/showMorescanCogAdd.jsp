<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title></title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	
	<script>
		
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			$("[data-toggle='popover']").popover();
			$('body').on('click', function(event) { 
				if ($("div.popover").size() > 0 
						&& $(event.target).closest("[data-toggle]").size() == 0 
						&& !($(event.target).is(".popover") || $(event.target).closest("div.popover").size() == 1)) {
		            $("[data-toggle='popover']").popover('toggle');
				}
            });
			
			// 初始化功能
			initPage();
			$('.scanType input[type=radio]').change(function (cg) {
				console.log("点击了一码多扫模式:",cg)
				let value = $('.scanType input[type=radio]:checked').val()
				if(value=='0'){
					$("#roleInfoTr").hide();
					$('.roleInfo input[type=checkbox]').removeAttr('checked');
				}else{
					$("#roleInfoTr").show();
				}
			})
			// 关联活动初始数据
			$("form").on("change", "#vcodeActivityKey", function(){
				var typeName = "";
				var $str = $(this).val();
				var skuName = $str.split("@")[1];
				var skuType = $str.split("@")[2];
				if(skuType == "0"){
					typeName = "瓶码";
				}else if(skuType == "1"){
					typeName = "罐码";
				}else if(skuType == "2"){
					typeName = "箱码";
				}else{
					typeName = "其他";
				}
				$("#skuId").val(skuName);
				$("#activityTypeId").val(typeName);
			});
			
			// 增加日期
            $("form").on("click", "#addDate", function() {
            	var idx = $("td.date div").index($(this).parent("div.date"));
                if (idx == 0) {
                    var count = $("td.date div").length;
                    if(count < 10){
                        var $dateCopy = $("div.date").eq(0).clone(true);
                        $("td.date").append($dateCopy);
                        $dateCopy.find("#addDate").text("删除");
                        var $beginDate = $("td.date div.date:last").find("input[name='beginDate']");
                        var $endDate = $("td.date div.date:last").find("input[name='endDate']");
                        $beginDate.attr("id", "beginDate" + count).val("");
                        $endDate.attr("id", "endDate" + count).val("");
                        $beginDate.attr("onfocus", $beginDate.attr("onfocus").replace("endDate", "endDate" + count));
                        $endDate.attr("onfocus", $endDate.attr("onfocus").replace("beginDate", "beginDate" + count));
                    }
                } else {
                    $(this).parent("div.date").remove();
                }
            });
            
         	// 增加时间
            $("form").on("click", "#addTime", function() {
                var idx = $("td.time div").index($(this).parent("div.time"));
                if (idx == 0) {
                    var count = $("td.time div").length;
                    if(count < 10){
                        var $timeCopy = $("div.time").eq(0).clone(true);
                        $("td.time").append($timeCopy);
                        $timeCopy.find("#addTime").text("删除");
                        var $beginTime = $("td.time div.time:last").find("input[name='beginTime']");
                        var $endTime = $("td.time div.time:last").find("input[name='endTime']");
                        $beginTime.attr("id", "beginTime" + count).val("00:00:00");
                        $endTime.attr("id", "endTime" + count).val("23:59:59");
                        $beginTime.attr("onfocus", $beginTime.attr("onfocus").replace("endTime", "endTime" + count));
                        $endTime.attr("onfocus", $endTime.attr("onfocus").replace("beginTime", "beginTime" + count));
                    }
                } else {
                    $(this).parent("div.time").remove();
                }
            });
            
            // 检验名称是否重复
            $("input[name='morescanName']").on("blur",function(){
            	var morescanName = $("input[name='morescanName']").val();
            	if(morescanName == "") return;
            	checkName(morescanName);
            });
		});
		
		function initPage() {

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
					$("form").attr("action", url);
					$("form").attr("onsubmit", "return true;");
					$("form").submit();
				}
				return false;
			});
            
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
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(morescanName){
			$.ajax({
				url : "${basePath}/activityMorescanCog/checkBussionName.do",
			    data:{
			    		"infoKey":"",
			    		"bussionName":morescanName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            beforeSend:appendVjfSessionId,
                    success:  function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("规则名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 检验名称是否重复
			var morescanName = $("input[name='morescanName']").val();
        	if(morescanName == "") return false;
        	checkName(morescanName);
        	if(!flagStatus){
        		return false;
        	}
			
			var returnFlag = false;
			var dateAry = "";
			
			// 组装多组日期
			$("td.date div.date").each(function(i){
            	var $beginDate = $(this).find("input[name='beginDate']");
            	var $endDate = $(this).find("input[name='endDate']");
            	if($beginDate.val() == "" || $endDate.val() == ""){
            		$.fn.alert("日期范围不能为空!");	
            		returnFlag = true;
            		return false; // 相当于break
            	}
            	dateAry += $beginDate.val() + "#" + $endDate.val() + ",";
            });

			if(returnFlag){
				return false;	
			}
			
			if(dateAry != ""){
				$("input[name='validDateRange']").val(dateAry.substring(0, dateAry.length - 1));
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
            	timeAry += $beginTime.val() + "#" + $endTime.val() + ",";
            });

			if(returnFlag){
				return false;	
			}
			
			if(timeAry != ""){
				$("input[name='validTimeRange']").val(timeAry.substring(0, timeAry.length - 1));
			}
			if('1' === $("#projectName").val()){
				if($('.scanType input[type=radio]:checked').val()) {
					if ($('.scanType input[type=radio]:checked').val() == '1') {
						if ($('.roleInfo input[type=checkbox]:checked').length == 0) {
							$.fn.alert("请至少选择一个扫码角色");
							return false;
						}
					}
				}
			}
			if($("#vcodeActivityKey").val() == ""){
				$.fn.alert("请选择连接活动");
        		return false;
			}
			// 设置扫码次数默认值
			var validCount = $("input[name='validCount']").val();
			if(validCount == "" || validCount == "0"){
				$("input[name='validCount']").val("1");
			}
			return true;
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
            <li class="current"><a> 一码多扫</a></li>
            <li class="current"><a>新增一码多扫</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/activityMorescanCog/doMorescanCogAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                <input type="hidden" name="projectName" id="projectName" value="${projectName}"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>新增一码多扫</h4>
                        <span class="marl10" title="温馨提示" data-html="true" 
                                data-container="body" data-toggle="popover" data-placement="auto" 
                                data-content="
                                <b>1</b>.日期范围与时间范围：选择的日期范围内执行时间范围，例如日期范围选择了“1号”至“5号”，</br>
                                &nbsp;&nbsp;&nbsp;时间范围选择了“11点”至“20点”，程序会按1号至5号，每天的11点至20点来执行一码多扫规则;</br>
                                <b>2</b>.一个活动只能被挂接一个有效一码多扫。被挂接后的活动，不会出现在连接活动下拉框中;</br>
                                <b>3</b>.单码首扫后有效天数：指单个二维码首次扫后，继续多少天有效，过了此有效天数，</br>
                                &nbsp;&nbsp;&nbsp;则二维码剩余次数失效。设置为0则根据规则时间执行。">
                            <i class="iconfont icon-tixing" style="color: red; font-size: 20px;"></i>
                        </span>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="morescanName" tag="validate" tabindex="-1" 
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                				<input type="hidden" name="validDateRange" />
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<div class="content date" style="margin: 5px 0px; display: flex;">
                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium required preTime" 
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium required sufTime" 
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}'})"/>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addDate">新增</label>
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="validTimeRange" />
                        		<td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                        		<td class="ab_main time">
                        			<div class="content time" style="margin: 5px 0px; display: flex;">
                                        <input name="beginTime" id="beginTime" class="form-control input-width-medium Wdate required preTime" 
                                        tag="validate" value="00:00:00" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'endTime\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endTime" id="endTime" class="form-control input-width-medium Wdate required sufTime" 
                                        tag="validate" value="23:59:59" autocomplete="off" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime\')}'})" />
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addTime">新增</label>
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">限定热区：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main">
                                    <div class="content hot">
                                        <select name="hotAreaKey" class="hotArea form-control" style="width: 480px;" >
                                            <option value="">请选择</option>   
                                            <c:if test="${not empty(hotAreaList) }">
                                                <c:forEach items="${hotAreaList}" var="hotArea">
                                                    <option value="${hotArea.hotAreaKey }" >${hotArea.hotAreaName}</option> 
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">中出模式：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<select class="form-control input-width-larger" name="prizePattern" readonly="readonly">
	                       					<option value="1">随机奖项</option>
	                       					<option value="2" style="display: none;">固定奖项</option>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">有效扫码次数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="validCount" tag="validate" class="form-control required number positive" autocomplete="off" maxlength="1" style="width: 240px;" value= "1"/>
										<span class="blocker en-larger">（有效扫码次数<2规则不生效）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">单码首扫后有效小时：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="qrcodeValidHour" tag="validate" class="form-control required number minValue" autocomplete="off" maxlength="6" minVal="0" style="width: 240px;" value= "0"/>
										<span class="blocker en-larger">（0时不限制扫码有效期）</span>
										<label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
							<c:if test="${projectName == '1'}">
								<tr>
									<td class="ab_left"><label class="title">一码多扫模式：<span class="required">*</span></label></td>
									<td class="ab_main" colspan="3">
										<div class="content scanType required">
											<div style="float: left; margin-right: 10px;">
												<span>按次数中奖</span>
												<input type="radio" tag="validate" class="required"  name="scanType" value="0" style="float: left;">
											</div>
											<div style="float: left; margin-right: 10px;">
												<span>按角色中奖</span>
												<input type="radio" tag="validate" class="required" checked name="scanType" value="1" style="float: left;">
											</div>
											<label class="validate_tips"></label>
										</div>
									</td>
								</tr>
								<tr id="roleInfoTr">
									<td class="ab_left"><label class="title">扫码角色：<span class="required">*</span></label></td>
									<td class="ab_main" colspan="3">
										<div class="content roleInfo">
											<c:if test="${not empty(roleInfoAll)}">
												<c:forEach items="${roleInfoAll }" var="roleItem">
													<div style="float: left; margin-right: 10px;">
														<span>${fn:split(roleItem, ':')[1]}</span>
														<input type="checkbox" tag="validate" name="roleInfo" value="${fn:split(roleItem, ':')[0]}" style="float: left;">
													</div>
												</c:forEach>
											</c:if>
											<label class="validate_tips"></label>
										</div>
									</td>
								</tr>
							</c:if>

                            <tr>
	                       		<td class="ab_left"><label class="title">联接活动：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<select class="form-control input-width-larger" id="vcodeActivityKey" name="vcodeActivityKey">
	                       					<option value="">请选择</option>
	                       					<c:forEach items="${activityList }" var="activity">
	                       						<option value="${activity.vcodeActivityKey }@${activity.skuName }@${activity.skuType }">
	                       							${activity.vcodeActivityName }
	                       						</option>
	                       					</c:forEach>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">SKU：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input id="skuId"  readonly="readonly"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">码源类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input id="activityTypeId" readonly="readonly" 
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									    <input name="status" type="hidden" value="1" />
									    <input id="status" type="checkbox" checked data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/activityMorescanCog/showMorescanCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
