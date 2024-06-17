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
	
	<script>
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
            }
            // $(".form-control").attr("disabled", "disabled");
			// 初始化功能
			initPage();
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
            $("#userRewardFlag").bootstrapSwitch({
                onSwitchChange:function(event,state){
                    if(state==true){
                        $("input:hidden[name='userRewardFlag']").val("1");
                    }else{
                        $("input:hidden[name='userRewardFlag']").val("0");
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
            // 发货区域初始化
            $.fn.zTree.init($("#tree"), setting, zNodes);
            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
            

            

		}

		function validForm() {


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
    <div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>筛选用户中,请勿其他操作.....</b></h2>
    </div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
        	<li class="current"><a> 活动管理</a></li>
            <li class="current"><a title="">新增规则</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/activateRedEnvelopeRuleCog/updateActivityCog.do">
            	<input type="hidden" name="companyKey" value="${companyKey}" />
            	<input type="hidden" name="activityKey" value="${ActivateRedEnvelopeRuleCog.activityKey}"/>
            	<input type="hidden" name="isView" id="isView" value="${isView}"/>
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>查看激活积分红包规则</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
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
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({realDateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'startDate\')}'})"  />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="form-control input-width-medium Wdate required sufTime" value="${ActivateRedEnvelopeRuleCog.endDate}"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({realDateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'endDate\')}'})" />
                                        <span class="blocker en-larger">（整个活动持续时间）</span>
                                       	<label class="validate_tips"></label>
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

                            <tr>
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="show_content">
                                        <input type="radio" class="tab-radio" name="rewardType" value="1" style="float:left;" checked="checked"/>
                                        <span class="blocker en-larger">现金</span>
                                        <input name="moneyRegion"  type="number" value="${fn:split(ActivateRedEnvelopeRuleCog.moneyRegion, ',')[0]}"
                                               class="form-control input-width-small positive moneyRegion" style="float:left;" autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">元</label>
                                        <span class="blocker en-larger">-</span>
                                        <input name="moneyRegion"  type="number" value="${fn:split(ActivateRedEnvelopeRuleCog.moneyRegion, ',')[1]}"
                                               class="form-control input-width-small positive moneyRegion" style="float:left;"  autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">元</label>
                                    </div>

                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title"></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="show_content">
                                        <input type="radio" class="tab-radio" name="rewardType" value="0" style="float:left;"/>
                                        <span class="blocker en-larger">积分</span>
                                        <input name="pointRegion"  type="number"  value="${fn:split(ActivateRedEnvelopeRuleCog.pointRegion, ',')[0]}"
                                               class="form-control input-width-small positive pointRegion" style="float:left;"  autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">积分</label>
                                        <span class="blocker en-larger">-</span>
                                        <input name="pointRegion"  type="number"  value="${fn:split(ActivateRedEnvelopeRuleCog.pointRegion, ',')[1]}"
                                               class="form-control input-width-small positive pointRegion" style="float:left;"  autocomplete="off" maxlength="10" tag="validate"/>
                                        <label class="blocker en-larger">积分</label>
                                    </div>
                                </td>
                            </tr>


                            <tr>
                                <td class="ab_left"><label class="title">关注公众号开关：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="officialAccountFlag" type="hidden" value="${ActivateRedEnvelopeRuleCog.officialAccountFlag}" />
                                        <input id="officialAccountFlag" type="checkbox" <c:if test="${ActivateRedEnvelopeRuleCog.officialAccountFlag eq '1'}">checked</c:if> data-size="small" data-on-text="开" data-off-text="关" data-on-color="info" data-off-color="danger"/>
                                    </div>
                                    <label class="blocker en-larger">（开关开启后再分享裂变的流程中加入关注公众号的流程）</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">公众号链接：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="officialAccountUrl" tag="validate" value="${ActivateRedEnvelopeRuleCog.officialAccountUrl}"
                                               class="form-control required" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label id="receiveNumberTitle" class="title">激活积分红包可领取个数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="receiveNumber" value="${ActivateRedEnvelopeRuleCog.receiveNumber}"  type="number"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">次（激活积分红包超出扫码次数后，在扫码不中出红包）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label id="receiveTimeLimitTitle" class="title">激活积分红包领取时间限制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="receiveTimeLimit" value="${ActivateRedEnvelopeRuleCog.receiveTimeLimit}"  type="number"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">天（自分享之时加上限制时间，时间过期分享链接失效，如果输入0天表示无限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label id="taskCompletionTimeLimitTitle" class="title">激活积分红包任务完成时间限制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="taskCompletionTimeLimit" value="${ActivateRedEnvelopeRuleCog.taskCompletionTimeLimit}"  type="number"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">天（点开分享链接之时加上限制时间，时间过期激活积分红包任务失效，如果输入0天表示无限制）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label id="userReceiveNumberLimitTitle" class="title">用户可领取激活积分红包个数限制：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="userReceiveNumberLimit"  value="${ActivateRedEnvelopeRuleCog.userReceiveNumberLimit}"  type="number"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">个（用户超出可领取个数后，在领取不获得激活积分红包，如果输入0表示无限制，领取过激活积分红包未完成任务也算一个）</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">分享活动：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                        <select class="form-control input-width-larger required" name="shareActivityKey" tag="validate">
                                            <option value="">请选择活动</option>
                                            <c:if test="${!empty shareActivityKeyList}">
                                                <c:forEach items="${shareActivityKeyList}" var="activity">
                                                    <option value="${activity.vcodeActivityKey}" <c:if test="${ActivateRedEnvelopeRuleCog.shareVcodeActivityKey == activity.vcodeActivityKey}"> selected</c:if>>${activity.vcodeActivityName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
                                        <span class="blocker en-larger">（此活动内的sku可以进行激活积分红包活动分享）</span>
                                        <label class="validate_tips"></label>
                                    </div>
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
                                        <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addActivationSku">${idx.count == 1 ? '新增' : '删除'}</label>
                                        <label class="validate_tips"></label>
                                    </div>
                                    </c:forEach>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="display:table-cell; vertical-align:middle;"><label class="title">分享按钮指定区域：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <input id="shareArea" name="shareArea" type="hidden">
                                    <div class="content_wrap">
                                        <div class="zTreeDemoBackground left" >
                                            <ul id="tree" class="ztree" style="background-color: white;"></ul>
                                        </div>
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
                                    <label class="blocker en-larger">（停用期间不可分享和不中出奖项，可以领取任务，已领取的可以完成任务，停用期间扫的二维码启用后也不可分享）</label>
                                </td>
                            </tr>

                		</table>
                	</div>
                	<div class="widget-header top-only">
                		<h4><i class="iconfont icon-saoma"></i>限制规则</h4>
                    </div>
                	<div class="widget-content panel no-padding filteruser">
                		<table class="active_board_table">
                            <tr>
                                <td class="ab_left"><label class="title"> 限制类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content " >
                                        <input type="radio" class="tab-radio" name="limitType" value="0" style="float:left;"   <c:if test="${ActivateRedEnvelopeRuleCog.limitType eq '0' }"> checked="checked" </c:if>/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">当前规则</span>
                                        <input type="radio" class="tab-radio" name="limitType" value="1" style="float:left;"   <c:if test="${ActivateRedEnvelopeRuleCog.limitType eq '1' }"> checked="checked" </c:if>/>
                                        <span class="blocker en-larger" style="margin-left: 2px;">每天</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title"> 限制消费激活积分红包金额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="moneyLimit"  value="${ActivateRedEnvelopeRuleCog.moneyLimit}"  type="number"
                                            class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">  限制分享中出红包总额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="shareMoneyLimit"  type="number"  value="${ActivateRedEnvelopeRuleCog.shareMoneyLimit}"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

                            <tr>
                                <td class="ab_left"><label class="title">  限制分享中出积分总额：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="sharePointLimit"  type="number" value="${ActivateRedEnvelopeRuleCog.sharePointLimit}"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">元</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">  限制消费瓶数：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="numberLimit" value="${ActivateRedEnvelopeRuleCog.numberLimit}"  type="number"
                                               class="form-control input-width-larger positive required" autocomplete="off" maxlength="10" tag="validate"/>
                                        <span class="blocker en-larger">瓶</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
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
