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
    <title>添加批次</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	  <link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	  <link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	  <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	  <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	  <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
	  <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	  <script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	  <script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script>
	$(document).ready(function () {
		// 初始化校验控件
		$.runtimeValidate($("form"));
		
		initPage();
		initGoodsValid();
	});
	function initGoodsValid(){
		$("select[name=goodsA]").on("change", function(){
			$option = $(this).find("option:selected");
			var realPay = ($option.data("realpay") / 100).toFixed(2);
			var goodsRemains = $option.data("goodsremains");
			$("#realPayA").html("商品原价："+realPay);
			$("#goodsRemainsA").html("商品库存："+goodsRemains);
			$("input[name='seckillTotalNum']").attr("maxVal", goodsRemains);
		});

		$("select[name=goodsB]").on("change", function(){
			$option = $(this).find("option:selected");
			var realPay = ($option.data("realpay") / 100).toFixed(2);
			var goodsRemains = $option.data("goodsremains");
			$("#realPayB").html("商品原价："+realPay);
			$("#goodsRemainsB").html("商品库存："+goodsRemains);
			$("input[name='seckillTotalNum']").attr("maxVal", goodsRemains);
		});
		$("select[name=goodsC]").on("change", function(){
			$option = $(this).find("option:selected");
			if($option.data("realpay")){
				var realPay = ($option.data("realpay") / 100).toFixed(2);
				var goodsRemains = $option.data("goodsremains");
				$("#realPayC").html("商品原价："+realPay);
				$("#goodsRemainsC").html("商品库存："+goodsRemains);
				$("input[name='seckillTotalNum']").attr("maxVal", goodsRemains);
			}

		});
		$("select[name=goodsA]").trigger("change");
		$("select[name=goodsB]").trigger("change");
		$("select[name=goodsC]").trigger("change");
	}
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

			if(true){
				let goodsA = $("[name='goodsA']").find("option:selected").val();
				let goodsB = $("[name='goodsB']").find("option:selected").val();
				let goodsC = $("[name='goodsC']").find("option:selected").val();
				if(goodsA==goodsB){
					$.fn.alert("商品A与商品B先择了同一商品")
					return false
				}
				if(goodsA==goodsC){
					$.fn.alert("商品A与商品C先择了同一商品")
					return false
				}
				if(goodsC==goodsB){
					$.fn.alert("商品B与商品C先择了同一商品")
					return false
				}
			}
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
				}
			});
			$(".btnSave").click(function(){
				checkGoodsInfo();
				return false;
			});
		}


		function checkGoodsInfo() {
			$.ajax({
				url:'<%=cpath%>/vpointsCombinationDiscount/checkGoodsInfo.do',
				type: 'POST',
				data: {
					"cogKey":$("[name='cogKey']").val(),
					"goodsA": $("[name='goodsA']").find("option:selected").val(),
					"goodsB": $("[name='goodsB']").find("option:selected").val(),
					"goodsC": $("[name='goodsC']").find("option:selected").val(),
					"endTime": $("[name='endTime']").val(),
					"startTime": $("[name='startTime']").val(),
					"openFlag": $("[name='openFlag']:checked").val()
				},
				dataType:'json',
				beforeSend:appendVjfSessionId,
				success: function(data){
					console.log("data = == = ",data)
					if(data.result.businessCode =='0'){
						$.fn.alert(data.result.msg);
					}else{
						var flag = validForm();
						if (flag) {
							$.fn.confirm("确认发布？", function(){
								$(".btnSave").attr("disabled", "disabled");
								$("#divId").css("display","block");
								$("form").attr("onsubmit", "return true;");
								$("form").submit();
							});
						}
					}
				},
				error:function(data){
				}
			});
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
		.top-only {
			border-top: 1px solid #e1e1e1;
		}
		.tab-radio {
			margin: 10px 0 0 !important;
		}
		.validate_tips {
			padding: 8px !important;
		}
		a {
			text-decoration: none;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
			<li class="current"><a> 首页</a></li>
			<li class="current"><a> 积分商城</a></li>
			<li class="current"><a> 组合优惠管理</a></li>
			<li class="current"><a> 修改活动</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form" enctype="multipart/form-data"
            	action="<%=cpath %>/vpointsCombinationDiscount/update.do">
				<input type="hidden" name="cogKey" value="${cog.cogKey}">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>修改活动</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">活动名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="name" tag="validate"  value="${cog.name}" class="form-control required" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
								<td class="ab_left"><label class="title">活动有效期：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="startTime" id="startDate" value="${cog.startTime}" disabled class="form-control input-width-medium Wdate required "
											   tag="validate" autocomplete="off" onfocus="WdatePicker({realDateFmt:'yyyy-MM-dd'})"  />
										<span class="blocker en-larger">-</span>
										<input name="endTime" id="endDate" value="${cog.endTime}" class="form-control input-width-medium Wdate required "
											   tag="validate" autocomplete="off" onfocus="WdatePicker({realDateFmt:'yyyy-MM-dd'})" />
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">是否开启：<span class="required">*</span></label></td>
								<td class="ab_right" colspan="3">
									<div class="content">
										<input name="openFlag" type="radio" style="float:left;margin-top: 10px"   value="1" <c:if test="${cog.openFlag =='1'}"> checked</c:if>/>
											<span class="blocker en-larger" >开启</span>
									</div>
									<div class="content">
										<input name="openFlag" type="radio" style="float:left; margin-top: 10px"   value="0"  <c:if test="${cog.openFlag =='0'}"> checked</c:if> />
										<span class="blocker en-larger" >停用</span>
									</div>

								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">商品A：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<select name="goodsA" tag="validate" disabled class="form-control input-width-larger required activitysku">
											<option value="">请选择</option>
											<c:if test="${!empty goodsList}">
												<c:forEach items="${goodsList}" var="item">
													<option value="${item.goodsId}" data-realvpoints="${item.goodsVpoints }" <c:if test="${cog.goodsA eq item.goodsId}"> selected</c:if>  data-realpay="${item.realPay }" data-goodsremains="${item.goodsRemains }">${item.goodsName}</option>
												</c:forEach>
											</c:if>
										</select>
										<span class="blocker en-larger" id="realPayA"></span>
										<span class="blocker en-larger" id="goodsRemainsA"></span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">商品B：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<select name="goodsB" tag="validate" disabled class="form-control input-width-larger required activitysku">
											<option value="">请选择</option>
											<c:if test="${!empty goodsList}">
												<c:forEach items="${goodsList}" var="item">
													<option value="${item.goodsId}" data-realvpoints="${item.goodsVpoints }" <c:if test="${cog.goodsB eq item.goodsId}"> selected</c:if> data-realpay="${item.realPay }" data-goodsremains="${item.goodsRemains }">${item.goodsName}</option>
												</c:forEach>
											</c:if>
										</select>
										<span class="blocker en-larger" id="realPayB"></span>
										<span class="blocker en-larger" id="goodsRemainsB"></span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">商品C：</label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<select name="goodsC"  disabled class="form-control input-width-larger  activitysku">
											<option value="">请选择</option>
											<c:if test="${!empty goodsList}">
												<c:forEach items="${goodsList}" var="item">
													<option value="${item.goodsId}" data-realvpoints="${item.goodsVpoints }" <c:if test="${cog.goodsC eq item.goodsId}"> selected</c:if> data-realpay="${item.realPay }" data-goodsremains="${item.goodsRemains }">${item.goodsName}</option>
												</c:forEach>
											</c:if>
										</select>
										<span class="blocker en-larger" id="realPayC"></span>
										<span class="blocker en-larger" id="goodsRemainsC"></span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">活动价格：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="activityPrice" value="${cog.activityPrice}" disabled tag="validate"
											   class="form-control input-width-medium money required" autocomplete="off" maxlength="50" />
										<span class="blocker en-larger">元</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">单人限购套数：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="singlePersonLimit" value="${cog.singlePersonLimit}" tag="validate"
											   class="form-control input-width-medium integer required" autocomplete="off" maxlength="50" />
										<span class="blocker en-larger">套（输入0则为不限制次数)</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">组合优惠总套数：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="totalNumber" value="${cog.totalNumber}" tag="validate"
											   class="form-control input-width-medium integer required" autocomplete="off" maxlength="50" />
										<span class="blocker en-larger">套</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">单日限购套数：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="singleDayLimit" value="${cog.singleDayLimit}" tag="validate"
											   class="form-control input-width-medium integer required" autocomplete="off" maxlength="50" />
										<span class="blocker en-larger">套</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vpointsCombinationDiscount/showList.do">返 回</button>
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
