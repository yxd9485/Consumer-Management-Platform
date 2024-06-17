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
            <li class="current"><a> 逢百规则</a></li>
            <li class="current"><a>修改逢百规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/activityPerhundredCog/doPerhundredCogEdit.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="infoKey" value="${perhundredCog.infoKey}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>编辑逢百规则</h4>
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
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="30" value="${perhundredCog.perhundredName }" disabled="disabled"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                				<input type="hidden" name="validDateRange" />
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<c:forEach items="${fn:split(perhundredCog.validDateRange,',')}" var="validDateRange" varStatus="idx">
	                        			<div class="content date" style="margin: 5px 0px;  display: flex;">
	                                        <input name="beginDate" id="beginDate${idx.index}" class="Wdate form-control input-width-medium required preTime" value="${fn:split(validDateRange,'#')[0]}" 
	                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate${idx.index}\')}'})" disabled="disabled"/>
	                                        <span class="blocker en-larger">至</span>
	                                        <input name="endDate" id="endDate${idx.index}" class="Wdate form-control input-width-medium required sufTime" value="${fn:split(validDateRange,'#')[1]}" 
	                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'beginDate${idx.index}\')}'})" disabled="disabled"/>
			                                <label class="validate_tips"></label>
	                        			</div>
	                        		</c:forEach>
                        		</td>
                        	</tr>
                			<tr>
                				<input type="hidden" name="perItems" />
                        		<td class="ab_left"><label class="title mart5">倍数：<span class="required">*</span></label></td>
                        		<td class="ab_main perItem">
                        			<c:forEach items="${fn:split(perhundredCog.perItems,';')}" var="item" varStatus="idx">
	                        			<div class="content perItem" style="margin: 5px 0px; display: flex">
	                                         <input name="perItem" class="form-control number positive input-width-small rule" autocomplete="off" maxlength="9" value="${fn:split(item,':')[0]}" onfocus="clearInput(this,'1')" onblur="inspectValueType(this, '1')" disabled="disabled"/>
	                                         <span class="blocker en-larger">金额</span>
	                                         <input name="moneys" class="form-control moneys input-width-small rule" autocomplete="off" maxlength="6" value="${fn:split(item,':')[1]}"  onfocus="clearInput(this,'2')" onblur="inspectValueType(this, '2')" disabled="disabled"/>
	                                         <span class="blocker en-larger">积分</span>
	                                         <input name="vpoints" class="form-control number positive input-width-small rule" autocomplete="off" maxlength="3" value="${fn:split(item,':')[2]}" onfocus="clearInput(this,'1')" onblur="inspectValueType(this, '1')" disabled="disabled"/>
	                        			</div>
	                        		</c:forEach>
                        		</td>
                        	</tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">联接活动：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content firstRebate">
	                       			    <input type="hidden" name="vcodeActivityKey" value="${perhundredCog.vcodeActivityKey}" />
	                       				<select class="form-control input-width-larger" disabled="disabled" id="vcodeActivityKey" name="vcodeActivityKey" disabled="disabled">
	                       				    <c:choose>
	                       				        <c:when test="${not empty perhundredCog.vcodeActivityName}">
			                       					<option value="">${perhundredCog.vcodeActivityName}</option>
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
	                       				<input id="skuId"  readonly="readonly" value="${perhundredCog.skuName}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50" disabled="disabled"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">码源类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input id="activityTypeId" readonly="readonly" value="${perhundredCog.skuType}"
	                       					class="form-control input-width-larger required" autocomplete="off" maxlength="50"  disabled="disabled"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									    <input name="status" type="hidden" value=${perhundredCog.status }  disabled="disabled"/>
									    <input id="status" type="checkbox" data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" 
									    data-off-color="warning" <c:if test="${perhundredCog.status eq '1'}"> checked </c:if> disabled="disabled"/>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>限制规则</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">限制时间类型：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<input type="hidden" id = "restrictTimeType" name="restrictTimeType" disabled="disabled">
                       				<div style="float: left; width: 80px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType1" name="restrictTimeType" value="0" 
                       						style="float: left; height:20px; cursor: pointer;"  disabled="disabled"
                       						<c:if test="${perhundredCog.restrictTimeType eq '0' }"> checked="checked" </c:if>>&nbsp;规则时间
                       				</div>
                       				<div style="float: left; width: 50px; line-height: 25px;">
                       					<input type="radio" tag="validate" id="restrictTimeType2" name="restrictTimeType" value="1" 
                       						style="float: left; height:20px; cursor: pointer;" disabled="disabled"
                       						<c:if test="${perhundredCog.restrictTimeType eq '1' }"> checked="checked" </c:if>>&nbsp;每天
                       				</div>
                       				<span class="blocker en-larger"></span>
                       				<label class="validate_tips"></label>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">限制消费积分：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="restrictVpoints" tag="validate" value="${perhundredCog.restrictVpoints eq 0 ? '' : perhundredCog.restrictVpoints}" 
                                            class="form-control number positive input-width-small rule" autocomplete="off" maxlength="9"  disabled="disabled"/>
                                        <span class="blocker en-larger">积分</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                        	<tr>
	                       		<td class="ab_left"><label class="title">限制消费金额：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictMoney" tag="validate" value="${perhundredCog.restrictMoney eq 0.00 ? '' : perhundredCog.restrictMoney}"
	                       					class="form-control number money input-width-small rule" autocomplete="off" maxlength="9"  onblur="inspectValueType(this,'2')"/ disabled="disabled">
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制消费瓶数：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="restrictBottle" tag="validate" value="${perhundredCog.restrictBottle eq 0 ? '' : perhundredCog.restrictBottle}"
	                       					class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8"  disabled="disabled"/>
	                       				<span class="blocker en-larger">瓶</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/activityPerhundredCog/showPerhundredCogList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>