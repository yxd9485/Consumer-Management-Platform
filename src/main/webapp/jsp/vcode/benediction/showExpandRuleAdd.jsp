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
            
            // 兑奖截止类型
            $("[name='expireType']").on("change", function(){
                if($(this).val() == '0') {
                    $(this).closest("div").find("input[name='expireDate']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='expireDay']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("input[name='expireDate']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='expireDay']").removeAttr("disabled");
                }
            });
		}

		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}

            // 校验单瓶膨胀成功配置
            var expandDanping = Number($("input[name='expandDanping']").val());
            var expandNum = Number($("input[name='expandNum']").val());
            var expandMinMoney = Number($("input[name='expandMinMoney']").val());
            var expandMxnMoney = Number($("input[name='expandMaxMoney']").val());
            if (expandDanping < expandNum * expandMinMoney) {
                $.fn.alert("膨胀单瓶成本不得小于膨胀红包个数 * 膨胀红包最小金额");
                return false;
            }
            if (expandDanping > expandNum * expandMaxMoney) {
                $.fn.alert("膨胀单瓶成本不得大于膨胀红包个数 * 膨胀红包最大金额");
                return false;
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
		.cycleday {
		  width: 20px;
		  height: 20px;
		  cursor: pointer;
		  margin: 1px;
		  text-align: center;
		  border: 1px solid gray;
		}
		.cycleday-selected {
          background-color: #64E0E0;
        }
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 膨胀规则</a></li>
            <li class="current"><a> 新增膨胀规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" 
            	action="<%=cpath%>/benediction/doExpandRuleAdd.do">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
            	<div class="widget box">
            		<div class="widget-header">
						<h4><i class="iconfont icon-xinxi"></i>新增膨胀规则</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="expandRuleName" tag="validate"  
	                       					class="form-control required" style="width: 330px;" autocomplete="off" maxlength="50" />
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
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">关联活动：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="vcodeActivityKey" tag="validate">
                                            <option value="">请选择活动</option>
                                            <c:if test="${!empty activityLst}">
                                            <c:forEach items="${activityLst}" var="item">
                                            <option value="${item.vcodeActivityKey}" >${item.vcodeActivityName}</option>
                                            </c:forEach>
                                            </c:if>
                                        </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">膨胀单瓶成本：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="expandDanping" tag="validate" 
                                            class="form-control input-width-larger number money minValue" autocomplete="off" minVal="0.01" maxlength="6" />
                                            <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">单瓶膨胀红包个数：<span class="required"> </span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="expandNum" tag="validate" 
                                            class="form-control input-width-larger number positive minValue maxValue" autocomplete="off" minVal="1" maxValue="100" maxlength="6" />
                                            <span class="blocker en-larger">个</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">单个膨胀红包范围：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content doubt">
                                        <span class="blocker en-larger">从</span>
                                        <input id="expandMinMoney" name="expandMinMoney" tag="validate" 
                                            class="form-control number money preValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span class="blocker en-larger">至</span>
                                        <input id="expandMaxMoney" name="expandMaxMoney" tag="validate" mark="blacklist"
                                            class="form-control number money sufValue minValue maxValue input-width-small" autocomplete="off" maxlength="5" minVal="0.01" maxVal="99.99"/>
                                        <span class="blocker en-larger">(元)</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">分享红包领取截止日期：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="tab-radio" name="expireType" value="0" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">指定日期</span>
                                        <input name="expireDate" class="form-control input-width-medium Wdate required"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <input type="radio" class="tab-radio" name="expireType" value="1" style="float:left; margin-left: 10px !important;" />
                                        <span class="blocker en-larger" style="margin-left: 2px;">有效天数</span>
                                        <input name="expireDay" tag="validate" disabled="disabled"
                                            class="form-control input-width-small required number positive" autocomplete="off" maxlength="3" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/benediction/showExpandRuleList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
  </body>
</html>
