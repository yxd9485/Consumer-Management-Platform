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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
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
			$.runtimeValidate($("#code_form"));
			
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
            
         	// 增加倍数
            $("form").on("click", "#addPerItem", function() {
                var idx = $("td.perItem div").index($(this).parent("div.perItem"));
                if (idx == 0) {
	            	if($("td.perItem div").length < 10){
	            		var $timeCopy = $("div.perItem").eq(0).clone();
	                	$("td.perItem").append($timeCopy);
	                	$timeCopy.find("#addPerItem").text("删除");	
	                    $("td.perItem div.perItem:last").find("input[name='perItem']").val("0");
	                    $("td.perItem div.perItem:last").find("input[name='moneys']").val("0.00");
	                    $("td.perItem div.perItem:last").find("input[name='vpoints']").val("0");
	            	}
                } else {
                	$(this).parent("div.perItem").remove();
                }
            	
            });
            
         	// 检验名称是否重复
            $("input[name='perhundredName']").on("blur",function(){
            	var perhundredName = $("input[name='perhundredName']").val();
            	if(perhundredName == "") return;
            	checkName(perhundredName);
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
		
		// 初始文本框值
		function clearInput(object, type){
			var value = $(object).val();
			if(type == '1' && value == "0"){
				$(object).val("");
			}else if(type == '2' && value <= "0.00"){
				$(object).val("");
			}
		}
		
		// 检查value类型：object文本框，type类型：1数字，2金额
		function inspectValueType(object, type){
			var value = $(object).val();
			if(type == '1' && (type == "" || !/^[1-9][0-9]*$/.test(value))){
				$(object).val("0");
			}else if(type == '2'){
				if(type == "" || !/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value) || value < 0){
					$(object).val("0.00");
				}
			}else if(type == '3'){
				if(type == "" || !/^(-?([1-9]\d{0,8})|0)(\.\d{2})$/.test(value) || value < 0){
					$(object).val("");
				}
			}
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(perhundredName){
			$.ajax({
				url : "${basePath}/activityPerhundredCog/checkBussionName.do",
			    data:{
			    		"infoKey":"",
			    		"bussionName":perhundredName
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
			var perhundredName = $("input[name='perhundredName']").val();
        	if(perhundredName == "") return false;
        	checkName(perhundredName);
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
			
			// 组装多组倍数
			var perItemAry = "";
			$("td.perItem div.perItem").each(function(i){
            	var $perItem = $(this).find("input[name='perItem']");
            	var $moneys = $(this).find("input[name='moneys']");
            	var $vpoints = $(this).find("input[name='vpoints']");
            	if($perItem.val() == "0"){
            		$.fn.alert("倍数必须大于0!");
            		returnFlag = true;
            		return false; // 相当于break
            	}
            	perItemAry += $perItem.val() + ":" + $moneys.val() + ":" + $vpoints.val() + ";";
            });
			if(returnFlag){
				return false;	
			}
			if(perItemAry != ""){
				$("input[name='perItems']").val(perItemAry.substring(0, perItemAry.length - 1));
			}
			
			if($("#vcodeActivityKey").val() == ""){
				$.fn.alert("请选择连接活动");
        		return false;
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
            <li class="current"><a> 逢百规则</a></li>
            <li class="current"><a>新增逢百规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/activityPerhundredCog/doPerhundredCogAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
            	<div class="widget box">
            		<div class="widget-header">
						<h4><i class="iconfont icon-xinxi"></i>新增逢百规则</h4>
                        <span class="marl10" title="温馨提示" data-html="true" 
                                data-container="body" data-toggle="popover" data-placement="auto" 
                                data-content="
                                <b>1</b>.限制类型：当前规则，指当前逢百规则整个规则的限制；每天，当前规则中每天的限制;</br> 
                                <b>2</b>.限制规则：执行过程中，不能超出限制，超出部分不会中出逢百金额;</br>
                                <b>3</b>.相同倍数中出规则：如用户中出两个倍数金额，则只给出最大倍数金额，忽略小倍数金额。</br>
                                &nbsp;&nbsp;&nbsp;如：设置了10倍与100倍逢百规则，在100倍时，只给出100倍的金额，不再给出10倍金额;</br>
                                <b>4</b>.一个活动只能被挂接一个有效逢百规则。被挂接后的活动，不会出现在连接活动下拉框中。">
                            <i class="iconfont icon-tixing" style="color: red; font-size: 20px;"></i>
                        </span>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="perhundredName" tag="validate"  
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
                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium required sufTime" autocomplete="off"
                                            tag="validate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')}'})" />
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addDate">新增</label>
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="perItems" />
                        		<td class="ab_left"><label class="title mart5">倍数：<span class="required">*</span></label></td>
                        		<td class="ab_main perItem">
                        			<div class="content perItem" style="margin: 5px 0px;">
                                         <input name="perItem" class="form-control number positive input-width-small" autocomplete="off" maxlength="9" value="0" onfocus="clearInput(this,'1')" onblur="inspectValueType(this, '1')"/>
                                         <span class="blocker en-larger">金额</span>
                                         <input name="moneys" class="form-control money input-width-small" autocomplete="off" maxlength="6" value="0.00" onfocus="clearInput(this,'2')" onblur="inspectValueType(this, '2')"/>
                                         <span class="blocker en-larger">积分</span>
                                         <input name="vpoints" class="form-control number positive input-width-small" autocomplete="off" maxlength="3" value="0" onfocus="clearInput(this,'1')" onblur="inspectValueType(this, '1')"/>
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addPerItem">新增</label>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">联接活动：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       				<select class="form-control input-width-larger" tag="validate" id="vcodeActivityKey" name="vcodeActivityKey">
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
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">限制时间类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                       				<div style="float: left; width: 80px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：</label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
                                            class="form-control number positive input-width-small" autocomplete="off" maxlength="9" />
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        	<tr>
	                       		<td class="ab_left"><label class="title">限制消费金额：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictMoney" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number money input-width-small" autocomplete="off" maxlength="9" onblur="inspectValueType(this,'2')"/>
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制消费瓶数：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictBottle" tag="validate" <c:if test="${dayTypeFlage == '1'}">readonly="readonly"</c:if> 
	                       					class="form-control number positive input-width-small" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/activityPerhundredCog/showPerhundredCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    
    
    
<script>
</script>
  </body>
</html>
