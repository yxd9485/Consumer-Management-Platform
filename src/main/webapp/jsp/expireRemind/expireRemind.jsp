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
    <title>POPSS活动基础信息</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			// 初始化功能
			initPage();

            btnChange(${popssInfo.isExtractLimit})
		});
		
		function validForm() {
		//	var validateResult = $.submitValidate($("form"));
			var validateResult = true;
			if( 1==$("#isExtractLimit").val()) {
                //提现校验
                var firstmoney = $("input[name='firstCash']").val();
                var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                if (!reg.test(firstmoney)) {
                    var l = $("#firstTip").val();
                    $("#firstTip").html("格式不对，参照如：1.00");
                    return
                }
                if (firstmoney == null && firstmoney == undefined && firstmoney == '') {
                    $("#firstTip").html("数据不能为空");
                    return
                }
                if(eval(firstmoney)>=eval(1)){
                    $("#firstTip").html("");
                }else{
                    $("#firstTip").html("提现金额要大于等于1");
                    return
                }
                var secondmoney = $("input[name='secondCash']").val();
                var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                if (!reg.test(secondmoney)) {

                    $("#secondTip").html("格式不对，参照如：1.00");
                    return
                }
                if (secondmoney == null && secondmoney == undefined && secondmoney == '') {
                    $("#secondTip").html("数据不能为空");
                    return
                }
                if(eval(secondmoney)>=eval(1)){
                    $("#secondTip").html("");
                }else{
                    $("#secondTip").html("提现金额要大于等于1");
                    return
                }
              // return true
            }
         /*   if (!validateResult) {
                return false;
            }*/
			// 页面校验
			var v_flag = true;
			$(".validate_tips:not(:hidden)").each(function(){
				if($(this).text() != ""){
					$.fn.alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
			
			// 日期
			var startDate = $("input[name='startTime']").val();
			var endDate = $("input[name='endTime']").val();
			if(startDate == "" || endDate == "") {
			    
				$("input[name='startTime']").parents(".content").find(".validate_tips").show();
				$("input[name='startTime']").parents(".content").find(".validate_tips").text("活动时间不能为空");
				$("input[name='startTime']").parents(".content").find(".validate_tips").addClass("valid_fail_text");
				return false;
			}
			
			// 泡币兑换
			var skuKey = $("input[name='currencyPrice']").val();
			if(skuKey == ""){
                $(".firstRebate").find(".validate_tips").text("未填写泡币兑换值");
                $(".firstRebate").find(".validate_tips").addClass("valid_fail_text");
                $(".rebateVpoints").find(".validate_tips").show();
				return false;
			}
            
            // 关注公众号
            var rebateVpoints = $("input[name='attentionVpoints']").val();
            if(rebateVpoints == ""){
                $(".rebateVpoints").find(".validate_tips").text("关注公众号送泡币的值不能为空");
                $(".rebateVpoints").find(".validate_tips").addClass("valid_fail_text");
                $(".rebateVpoints").find(".validate_tips").show();
                return false;
            }
			
			return true;
		}
		
		function initPage() {
			$("tr.doubt-rule").find("input.money").prop("disabled", true);
			$("tr.doubt-rule").find("input.number").prop("disabled", true);
			// 可疑用户
			$("tr.doubt-rule").delegate("input[type=radio]", "click", function(){
				var currentVal = $(this).val();
				if(currentVal == "1"){
					$("tr.doubt-rule:eq(0)").find("input.number").prop("disabled", false);
					$("tr.doubt-rule:eq(1)").find("input.money").prop("disabled", true);
					$("tr.doubt-rule:eq(1)").find("input.money").val("");
				} else {
					$("tr.doubt-rule:eq(1)").find("input.money").prop("disabled", false);
					$("tr.doubt-rule:eq(0)").find("input.number").prop("disabled", true);
					$("tr.doubt-rule:eq(0)").find("input.number").val("");
				}
			});
			/* 删除黑名单配置功能，黑名单用户均返回0.00元
			// 黑名单用户
            $("tr.danger-rule").find("input.number").prop("disabled", true);
			$("tr.danger-rule").delegate("input[type=radio]", "click", function(){
				var currentVal = $(this).val();
				if(currentVal == "1"){
					$("tr.danger-rule:eq(0)").find("input.number").prop("disabled", false);
					$("tr.danger-rule:eq(1)").find("input.number").prop("disabled", true);
					$("tr.danger-rule:eq(1)").find("input.number").val("");
				} else {
					$("tr.danger-rule:eq(1)").find("input.number").prop("disabled", false);
					$("tr.danger-rule:eq(0)").find("input.number").prop("disabled", true);
					$("tr.danger-rule:eq(0)").find("input.number").val("");
				}
			}); */
			
			$("select[name='skuKey']").change(function(){
				$(this).parents(".skuInfo").find(".validate_tips").text("");
				$(this).parents(".skuInfo").find(".validate_tips").removeClass("valid_fail_text");
			});

            $("input[name='firstCash']").blur(function () {   //输入框失去焦点的时候执行，form:input获取表格的输入框
				 var money=$("input[name='firstCash']").val();
                var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                if (!reg.test(money)) {
                   var l= $("#firstTip").val();
                    $("#firstTip").html("格式不对，参照如：1.00");
                    return
                }else{
                    $("#firstTip").html("");
				}
                if( money == null && money == undefined && money== ''){
                    $("#firstTip").html("数据不能为空");
                    return
				}else{
                    $("#firstTip").html("");
				}
				if(eval(money)>=eval(1)){
                    $("#firstTip").html("");
				}else{
                    $("#firstTip").html("提现金额要大于等于1");
                    return
				}

               
            });
         /*   $("input[name='activityMagnification']").blur(function () {
                var money=$("input[name='activityMagnification']").val();
                var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                if (!reg.test(money)) {

                    $("#secondTip").html("格式不对，参照如：1.00");
                    return ;}else{
                    $("#secondTip").html("");
                }
            });*/
                $("input[name='secondCash']").blur(function () {   //输入框失去焦点的时候执行，form:input获取表格的输入框
                var money=$("input[name='secondCash']").val();
                var reg = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
                if (!reg.test(money)) {
                    
                    $("#secondTip").html("格式不对，参照如：1.00");
                    return ;
                }else{
                    $("#secondTip").html("");
				}
                if( money == null && money == undefined && money== ''){
                    $("#secondTip").html("数据不能为空");
                    return ;
                }else{
                    $("#secondTip").html("");
				}

                if(eval(money)>=eval(1)){
                    $("#secondTip").html("");
                }else{
                    $("#secondTip").html("提现金额要大于等于1");
                    return ;
                }
            });
			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
				/*	var blacklistCogFlag = "0";
					$("input[mark=blacklist]:not([type=radio])").each(function(){
						if($(this).val() != ""){
							blacklistCogFlag = "1";
						}
					});
					$("input[name=blacklistFlag]").val(blacklistCogFlag);*/
					
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

        function btnChange(values){
		    if(values ==0){
                $("input[name='firstCash']").attr("disabled","disabled");
                $("#firstTip").html("");
                $("input[name='secondCash']").attr("disabled","disabled");
                $("#secondTip").html("");
                $("input[name='firstCash']").val("")
                $("input[name='secondCash']").val("")
			}else{
                $("input[name='firstCash']").removeAttr("disabled");
                $("#firstTip").html("");
                $("input[name='secondCash']").removeAttr("disabled");
                $("#secondTip").html("");
			}
		}
	</script>
	
	<style>
		.form-horizontal .form-group {
			margin-right: 0px;
			margin-left: 18px;
		}
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
       .tdd {
            word-break: break-all;
            word-wrap: break-word;
        }
        .active_board_table .ab_left {
            width: 20%;
            text-align: right;
            margin: 6px;
            padding: 6px;
        }
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> POPSS活动基础信息</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vpsWechatMovementActivityBaseInfo/savePopssActivityInfo.do">
            	<input type="hidden" name="infoKey" value="${popssInfo.infoKey}">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>基础设置</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
							<tr>
								<td class="ab_left"><label class="title">一个泡币兑换：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">

										<input type="hidden" name="oldCurrencyPrice" value="${popssInfo.currencyPrice}"/>

										<input name="currencyPrice" tag="validate" style="width: 240px"
											   class="payMoney form-control  required money minValue maxValue" minval="-9999999.99"maxval="9999999.99" autocomplete="off"  maxlength="10" value="${popssInfo.currencyPrice}"/>
										<span style="color: green; margin-left: 5px; margin-top: 8px; display: block; float:left; ">元</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
                		</table>
                	</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>轻松完成四步</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">

							<tr>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="attentionVpoints" tag="validate"
											   class="form-control required  number integer preValue input-width-larger" autocomplete="off" maxlength="10" value="${popssInfo.attentionVpoints}" />


									</div>
								</td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="attentionVpoints" tag="validate"
											   class="form-control required  number integer preValue input-width-larger" autocomplete="off" maxlength="10" value="${popssInfo.attentionVpoints}" />

									</div>
								</td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="attentionVpoints" tag="validate"
											   class="form-control required  number integer preValue input-width-larger" autocomplete="off" maxlength="10" value="${popssInfo.attentionVpoints}" />


									</div>
								</td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="attentionVpoints" tag="validate"
											   class="form-control required  number integer preValue input-width-larger" autocomplete="off" maxlength="10" value="${popssInfo.attentionVpoints}" />


									</div>
								</td>
							</tr>
							<tr>

                		</table>
                	</div>

					<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>最新提醒</h4></div>

					<div class="row">
						<div class="col-md-12">
							<div class="widget box">
								<div class="widget-content">
									<div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
										<div class="row">
											<div class="dataTables_header clearfix">
												<form class="form-horizontal marl30 listForm" method="post"
													  action="<%=cpath%>/homeData/homeDataPage.do">
													<input type="hidden" class="tableTotalCount" value="${showCount}" />
													<input type="hidden" class="tableStartIndex" value="${startIndex}" />
													<input type="hidden" class="tablePerPage" value="${countPerPage}" />
													<input type="hidden" class="tableCurPage" value="${currentPage}" />
													<input type="hidden" name="pageParam" />
													<input type="hidden" name="queryParam" />
													<div class="form-group" style="border-top: 0px solid #ececec ; padding-top: 0px ; padding-bottom: 0px ; margin-bottom: 0 ;">

														<button type="button"  class="btn btn-res btn-radius3">到期提醒</button>
													</div>
												</form>
											</div>
										</div>
										<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
											   id="dataTable_data">
											<thead>
											<tr>
												<th style="width:4%; text-align: center;">活动编号</th>
												<th style="width:10%; text-align: center;">活动类型</th>
												<th style="width:24%; text-align: center;">活动名称</th>
												<th style="width:14%; text-align: center;">活动时间</th>
												<th style="width:12%; text-align: center;">提醒类型</th>
												<th style="width:12%; text-align: center;">通知备注</th>
												<th style="width:10%; text-align: center;">收件时间</th>
												<c:if test="${transStatus != 2}">
													<th style="width:10%; text-align: center;">操作</th>
												</c:if>
											</tr>
											</thead>
											<tbody>
											<c:choose>
												<c:when test="${fn:length(msgList) gt 0}">
													<c:forEach items="${msgList}" var="recharge" varStatus="idx">
														<tr>
															<td style="text-align: center;"><span>${idx.count}</span></td>
															<td style="text-align: center;"><span>${recharge.infoKey}</span></td>
															<td style="text-align: center;"><span>${recharge.activityType}</span></td>
															<td style="text-align: center;"><span>${recharge.vcodeActivityKey}</span></td>
															<td style="text-align: center;">
																<span data-ref="remoney" data-value="${recharge.createTime}">${recharge.updateTime}</span>
															</td>
															<td style="text-align: center;"><span>${recharge.vcodeActivityKey}</span></td>
															<td style="text-align: center;">
															<span>
																<c:if test="${recharge.msgType == 0}">网银转账</c:if>
																<c:if test="${recharge.msgType == 1}">第三方支付</c:if>
																<c:if test="${recharge.msgType == 2}">支票</c:if>
																<c:if test="${recharge.msgType == 3}">欠款待付</c:if>
															</span>
															</td>
														<td style="text-align: center;"><span>${recharge.updateTime}</span></td>

													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
														<td colspan="${transStatus != 2 ? 8 : 7}"><span>查无数据！</span></td>
													</tr>
												</c:otherwise>
											</c:choose>
											</tbody>
										</table>
										<table id="pagination"></table>
									</div>
								</div>
							</div>
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
