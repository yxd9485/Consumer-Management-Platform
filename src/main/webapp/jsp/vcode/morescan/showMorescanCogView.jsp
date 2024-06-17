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
		});
		
		function initPage() {

            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
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
            <li class="current"><a>修改一码多扫</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/activityMorescanCog/doMorescanCogEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${morescanCog.infoKey}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>修改一码多扫</h4>
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
	                       				<input name="morescanName" tag="validate"  
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${morescanCog.morescanName }" disabled="disabled"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                				<input type="hidden" name="validDateRange" />
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<c:forEach items="${fn:split(morescanCog.validDateRange,',')}" var="validDateRange">
	                        			<div class="content date" style="margin: 5px 0px;  display: flex;">
	                                        <input name="beginDate" id="beginDate" class="Wdate form-control input-width-medium required preTime"  autocomplete="off"
	                                            tag="validate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" value="${fn:split(validDateRange,'#')[0]}" disabled="disabled"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium required sufTime" value="${fn:split(validDateRange,'#')[1]}" 
	                                            tag="validate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate\')||\'%y-%M-%d\'}'})" autocomplete="off" disabled="disabled"/>
			                                <label class="validate_tips"></label>
	                        			</div>
	                        		</c:forEach>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="validTimeRange" />
                        		<td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
                        		<td class="ab_main time">
                        			<c:forEach items="${fn:split(morescanCog.validTimeRange,',')}" var="validTimeRange">
                                        <div class="content time" style="margin: 5px 0px; display: flex">
                                            <input name="beginTime" id="beginTime" class="form-control input-width-medium required Wdate"  autocomplete="off"
                                            tag="validate" onfocus="WdatePicker({dateFmt:'HH:mm:ss'})" value="${fn:split(validTimeRange,'#')[0]}" disabled="disabled"/>
                                            <span class="blocker en-larger">至</span>
                                            <input name="endTime" id="endTime" class="form-control input-width-medium required Wdate"  autocomplete="off"
                                            tag="validate" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime\')}'})" value="${fn:split(validTimeRange,'#')[1]}" disabled="disabled"/>
                                        </div>
                                    </c:forEach>
                        		</td>
                        	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">中奖模式：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content prizePattern">
	                       				<select class="form-control input-width-larger" name="prizePattern" readonly="readonly" disabled="disabled">
	                       					<option value="1" <c:if test="${morescanCog.prizePattern eq '1'}">selected</c:if>>随机奖项</option>
	                       					<option value="2" <c:if test="${morescanCog.prizePattern eq '2'}">selected</c:if> style="display: none;">固定奖项</option>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">有效扫码次数：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="validCount" tag="validate" class="form-control required number positive" autocomplete="off" maxlength="1" style="width: 240px;" value= "${morescanCog.validCount}" disabled="disabled"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">单码首扫后有效小数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="qrcodeValidHour" tag="validate" class="form-control required number minValue" autocomplete="off" maxlength="6" minVal="0" style="width: 240px;" value= "${morescanCog.qrcodeValidHour}" disabled="disabled"/>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">联接活动：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       			    <input type="hidden" name="vcodeActivityKey" value="${morescanCog.vcodeActivityKey}" disabled="disabled"/>
	                       				<select class="form-control input-width-larger" disabled="disabled" id="vcodeActivityKey" name="vcodeActivityKey" disabled="disabled">
	                       				    <c:choose>
                                                <c:when test="${not empty morescanCog.vcodeActivityName}">
                                                    <option value="">${morescanCog.vcodeActivityName}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="">请选择</option>
                                                </c:otherwise>
                                            </c:choose>
	                       				</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">SKU：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input id="skuId"  readonly="readonly" value="${morescanCog.skuName}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50" disabled="disabled"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">码源类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input id="activityTypeId" readonly="readonly" value="${morescanCog.skuType}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50"  disabled="disabled"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									    <input name="status" type="hidden" value=${morescanCog.status } />
									    <input id="status" type="checkbox" data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" 
									    data-off-color="warning" <c:if test="${morescanCog.status eq '1'}"> checked </c:if> disabled="disabled"/>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/activityMorescanCog/showMorescanCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>