<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
String pathPrefix = cpath + "/" + imagePathPrx;
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js?v=1"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=3"></script>
	
	<script>
		$(function(){
            
            // 初始化界面
			var isUpdateAddr = 0;
            
			//复选框弹入弹出
            $("#checkboxImg").click(function() {
                $("#checkboxDiv").toggle();
            });
            $("#determineDiv").click(function() {
                $("#checkboxDiv").toggle();
            });
            //复选框选中事件
            $('input[name="checkboxValue"]').click(function(){
                /*if( $('input[name="checkboxValue"]').checked(true)){
                    alert(1)
                }*/
                var chk_value =[];
                $('input[name="checkboxValue"]:checked').each(function(){
                    chk_value.push($(this).val());
                });
                //alert(chk_value);
                $('#checkboxImg').val(chk_value);
            });
            
			// 弹出修改批次窗口
            $("a.send, a.editExp, a.editAddr").off();
            $("a.send, a.editExp, a.editAddr").on("click", function(){
            	// 拼团状态
            	var groupBuyingStatus = $(this).parents("td").data("gbstatus");
            	if(groupBuyingStatus == '2'){
            		$.fn.alert("等待成团，无法发货！");
					return false;            		
            	}
            	
                // 弹出对话框
                $("#expressDialog input[name]").val("");
                $("#expressDialog span[id]").text("");
                $("#expressDialog").modal("show");
            	// 标题
            	if ($(this).hasClass("send")) {
            		isUpdateAddr = 0;
            		$("#expressDialog #myModalLabel").text("物流发货");
            		isUpdateAddr = 0;
            	} else if ($(this).hasClass("editExp")){
                    $("#expressDialog #myModalLabel").text("修改物流发货");
            	}else{
            		isUpdateAddr = 1;
            		$("#expressDialog #myModalLabel").text("修改收货信息");
            	}
            	
            	// 订单主键
            	var key = $(this).parents("td").data("key");
            	$("#expressDialog").find("input[name='exchangeId']").val(key);
            	// 订单信息
            	$(this).closest("tr").find("span[id]").each(function(){
            		$("#expressDialog").find("span[id='" + $(this).attr("id") + "']").text($(this).text());
            		$("#expressDialog").find("input[name='" + $(this).attr("id") + "']").val($(this).text());
            	});
            	
            	// 是否禁用收货信息的文本框
            	if ($(this).hasClass("editAddr")) {
            		$("#expressDialog input[name='expressCompany']").closest("tr").css("display","none");
            		$("#expressDialog input[name='expressNumber']").closest("tr").css("display","none");
            		$("#expressDialog input[type='text']").attr("disabled",false);
            	}else{
            		$("#expressDialog input[type='text']").attr("disabled",true);
            		$("#expressDialog input[name='expressCompany']").attr("disabled",false).closest("tr").css("display", "");
            		$("#expressDialog input[name='expressNumber']").attr("disabled",false).closest("tr").css("display", "");
            		
            	}
            });

            // 确定修改物流单号
            $.runtimeValidate($("#expressDialog"));
            $("#expressDialog").delegate("#addBtn", "click", function(){
                // 输入元素校验
                var validateResult = $.submitValidate($("#expressDialog"));
                if(!validateResult){
                    return false;
                }
                // 提交表单
                var url = "";
                var paramJson = {};
                
                if(isUpdateAddr == 0){
                	url = "<%=cpath%>/vpointsExchange/updateExpressInfo.do"; 	
                }else{
                	url = "<%=cpath%>/vpointsExchange/updateOrderAddress.do";
                }
                $("#expressDialog input[name]").each(function(){
                	paramJson[$(this).attr("name")] = $(this).val();	
                });
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#expressDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });
            
            // 撤单
            $("a.revoke, a.returnConfirm").off();
            $("a.revoke, a.returnConfirm").on("click", function(){
            	
                // 弹出对话框
                $("#revokeOrderDialog").modal("show");
                $("#revokeOrderDialog #addBtn").data("biztype", $(this).hasClass("revoke") ? "0" : "1");
                // 订单主键
                var key = $(this).parents("td").data("key");
                $("#revokeOrderDialog").find("input[name='exchangeId']").val(key);
                
                // 最大可退款金额
                var $exchangeMoney = $(this).parents("tr").find("#exchangeMoney");
                var maxRefundMoney = $exchangeMoney.data("maxrefundpay") == null ? 0.00 : $exchangeMoney.data("maxrefundpay")/100;
                $("#revokeOrderDialog").find("#maxRefundMoney").val(maxRefundMoney.toFixed(2));
                $("#revokeOrderDialog").find("#refundMoney").attr("maxVal", maxRefundMoney.toFixed(2));
                
                // 最大可退款积分
                var $exchangeVpoints = $(this).parents("tr").find("#exchangeVpoints");
                var maxRefundVpoints = $exchangeVpoints.data("maxrefundvpoints") == null ? 0 : $exchangeVpoints.data("maxrefundvpoints");
                $("#revokeOrderDialog").find("#maxRefundVpoints").val(maxRefundVpoints);
                $("#revokeOrderDialog").find("#refundVpoints").attr("maxVal", maxRefundVpoints);
            });

            // 确定上传发货清单
            $.runtimeValidate($("#revokeOrderDialog"));
            $("#revokeOrderDialog").delegate("#addBtn", "click", function(){
            	
            	// 退款类型
            	if($("input[name='refundType']:checked").size() == 0) {
            		$.fn.alert("请选择退款类型");
            		 return false;
            	}

                // 输入元素校验
                var validateResult = $.submitValidate($("#revokeOrderDialog"));
                if(!validateResult){
                    return false;
                }

                // 最大可退款金额
                var maxRefundMoney = $("#revokeOrderDialog").find("#maxRefundMoney").val();
                // 最大可退款积分
                var maxRefundVpoints = $("#revokeOrderDialog").find("#maxRefundVpoints").val();

                var refundMoney = $("#revokeOrderDialog").find("#refundMoney").val();
                var refundVpoints = $("#revokeOrderDialog").find("#refundVpoints").val();

               if (parseFloat(maxRefundMoney) === parseFloat(refundMoney) && parseInt(maxRefundVpoints) === parseInt(refundVpoints)
                   && $("input[name='refundType']:checked").val() != '0'){
                   $.fn.alert("金额与积分全部退款时,退款类型不能为仅退款");
                   return false;
               }
                var paramJson = {};
                $("#revokeOrderDialog input[name]").each(function(){
                    paramJson[$(this).attr("name")] = $(this).val();    
                });
                paramJson["refundType"] = $("input[name='refundType']:checked").val();
                paramJson["revokeOrderReason"] = $("#revokeOrderReason").val();
                var optType = $(this).data("biztype");
                $.fn.confirm("确认要退款吗？", function() {
                    $.ajax({
                        type: "POST",
                        url: "<%=cpath%>/vpointsExchange/revokeOrder.do?optType=" + optType,
                        data: paramJson,
                        dataType: "json",
                        async: false,
                        beforeSend:appendVjfSessionId,
                    success:  function(data) {
	                        $("#revokeOrderDialog #closeBtn").trigger("click");
	                        $.fn.alert(data['errMsg'], function(){
	                            $("button.btn-primary").trigger("click");
	                        });
                        }
                    });
                });
            });
            
            // 退货审核
            $("a.returnAudit").off();
            $("a.returnAudit").on("click", function(){
                var key = $(this).parents("td").data("key");
                $("form").attr("action", "<%=cpath%>/vpointsExchange/showExchangeExpressView.do?infoKey=" + key);
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/vpointsExchange/showExchangeExpressList.do");
                
            });
            
            // 弹出上传发货清单
            $(".uploadItem").off();
            $(".uploadItem").on("click", function(){
                // 弹出对话框
                $("#expressUploadDialog input.import-file").val("");
                $("#expressUploadDialog").modal("show");
            });

            // 确定上传发货清单
            $("#expressUploadDialog").delegate("#addBtn", "click", function(){
                $uploadFile = $("#expressUploadDialog input.import-file");
                var files = $uploadFile.val();
                if(files == "") {
                    $.fn.alert("未选择任何文件，不能导入!");
                    return false;
                } else if(files.indexOf("xls") == -1 && files.indexOf("xlsx") == -1 && files.indexOf("csv") == -1) {
                    $.fn.alert("不是有效的EXCEL文件");
                    return false;
                }
            
                // 提交表单
                var url = "<%=cpath%>/vpointsExchange/importExchangeExpressList.do";
                var formData = new FormData();
                formData.append("filePath", $uploadFile[0].files[0]);
                $.ajax({
                    type: "POST",
                    url: url,
                    data: formData,
                    dataType: "json",
                    async: false,
                    contentType : false,
                    processData: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#expressUploadDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });
            
            // 下载订单列表
            $(".export").on("click", function(){
				$(".export").attr("disabled", "disabled");
                $("form").attr("action", "<%=cpath%>/vpointsExchange/exportExchangeExpressList.do");
                $("form").submit();
				setTimeout(function () {
					// 还原查询action
					$("form").attr("action", "<%=cpath%>/vpointsExchange/showExchangeExpressList.do");
				},2000)
            });
            
			// 品牌切换
			$("[name='brandId']").on("change", function() {
                $("[name='goodsId']").html("<option value=''>全部</option>");
                
				var brandId = $(this).val();
				if (brandId) {
	                var url = "<%=cpath%>/vpointsGoods/queryGoodsByBrandId.do";
	                $.ajax({
	                    type: "POST",
	                    url: url,
	                    data: {brandId : brandId},
	                    dataType: "json",
	                    async: false,
	                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        	$("[id='checkboxDiv']").html('');
	                        $.each(data['goodsAry'], function(i, obj) {
	                        	$("[id='checkboxDiv']").append("<label><input type='checkbox' name='checkboxValue' value='"+obj.goodsId+"'>"+obj.goodsName+"</label><br>");
	                        });
	                        $("[id='checkboxDiv']").append("<div id='determineDiv' class='determineCls'>确定</div>");
	                        
	                        // 回显商品信息
	                        var newParams = $("input[name='queryParam']").val();
	                        if(newParams != ''){
	                        	var goodsGro = newParams.split(",")[2];
		                        if(goodsGro != '' && goodsGro.split(";").length > -1){
		                        	changeData(goodsGro);
		                        }
	                        }
	                        
	                        
	                        // 特殊处理
	                        // $("[name='goodsId']").val($("[name='goodsId']").data("currval"));
	                        // $("[name='goodsId']").removeData("currval");
	                        
	                        $("#determineDiv").click(function() {
				                $("#checkboxDiv").toggle();
				            });
	                        
	                        //复选框选中事件
	                        $('input[name="checkboxValue"]').click(function(){
	                            var chk_value =[];
	                            $('input[name="checkboxValue"]:checked').each(function(){
	                                chk_value.push($(this).val());
	                            });
	                            $('#checkboxImg').val(chk_value);
	                        });
	                    }
	                });
				}
			});
			$("[name='brandId']").trigger("change");
		});
		
		function changeData(param){
    		var dataNew=param.replaceAll(";",",");
    		$("input[name=checkboxImg]").val(dataNew);
            var datas=param.split(";");
            for(j = 0; j < datas.length; j++) {
            	   var va=datas[j];
            	   $('input[name="checkboxValue"]').each(function(){
            		   var ck=$(this).val();
            		   if(ck==va){
            			   $(this).prop("checked", true);
            		   }
                   });   
            } 
    	}
	</script>
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
		}
		
		.checkboxImg {
			width: 80px;
			height: 20px;
			border: 1px solid #999999;
			/* background: url(answerImg.png) no-repeat center right; /自己弄一张13px下拉列表框的图标/ */
			}
			.checkboxDiv {
            width: 240px;
			padding-left:5px;
			padding-right:5px;
			color:white;
			position:fixed;
			margin-left:94px;
			margin-top:-10px;
			z-index:99;
			background-color: rgba(100,100,100,1);
			display: none;
			border: 1px solid #999999;
			border-top: none;
			overflow-y:auto; 
			overflow-x:auto; 
			min-height:10px;
			max-height:400px;
			min-width:200px;
			    }
	    .determineCls {
	        width: 76px;
	        height: 20px;
	        line-height: 20px;
	        border: 1px solid #999999;
	        font-size: 12px;
	        margin: 3px auto;
	        border-radius: 5px;
	        text-align: center;
	        cursor: pointer;
	    }
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商城</a></li>
        	<li class="current"><a> 订单管理</a></li>
            <li class="current">
            	<c:choose>
            		<c:when test="${tabsFlag eq '0'}">待发货</c:when>
            		<c:when test="${tabsFlag eq '1'}">已发货</c:when>
            		<c:when test="${tabsFlag eq '2'}">已完成</c:when>
            		<c:when test="${tabsFlag eq '4'}">退货</c:when>
            		<c:otherwise>全部订单</c:otherwise>
            	</c:choose>	
            </li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
	        <a href="<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=0" 
	             class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">待发货</a>
	        <a href="<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">已发货</a>
	        <a href="<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已完成</a>
	        <a href="<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=4" 
	             class="btn <c:if test="${tabsFlag eq '4'}">btn-blue</c:if>">退货订单</a>
	        <a href="<%=cpath%>/vpointsExchange/showExchangeExpressList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3" 
	             class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">全部订单</a>
	    </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">订单查询</span></div>
	               <div class="col-md-10 text-right">
	                  <c:if test="${tabsFlag eq '0'}">
						  <button class="btn btn-blue downItem export">  <i class="iconfont icon-xiazai" style="font-size: 14px;"></i>待发货表格下载</button>
	                  </c:if>
	                  <c:if test="${tabsFlag eq '1'}">
						  <button class="btn btn-blue uploadItem">  <i class="iconfont icon-shangchuan" style="font-size: 14px;"></i>&nbsp; 发货表格上传</button>
	                  </c:if>
	                  <c:if test="${tabsFlag eq '2'}">
						  <button class="btn btn-blue downItem export"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 已完成表格下载</button>
	                  </c:if>
	                  <c:if test="${tabsFlag eq '3'}">
						  <button class="btn btn-blue downItem export"> <i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 全部订单下载</button>
	                  </c:if>
	                  <c:if test="${tabsFlag eq '4'}">
						  <button class="btn btn-blue downItem export">  <i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 退货订单下载</button>
	                  </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vpointsExchange/showExchangeExpressList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
	                    <input type="hidden" name="token" value="${token}" />
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
			                        <label class="control-label">订单编号：</label>
			                        <input name="exchangeId" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">商品类型：</label>
                                    <select name="brandId" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <c:forEach items="${brandLst}" var="item">
                                            <option value="${item.brandId}">${item.brandName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">兑换商品：</label>
                                    <input id="checkboxImg" autocomplete="off" name="checkboxImg" type="text"  value="" class="form-control input-width-larger" placeholder="点击选择">
	                                <div id="checkboxDiv" class="checkboxDiv" style="line-height: -10px;"></div>
                                </div>
                                <c:if test="${tabsFlag > 0}">
                                    <div class="search-item">
		                                <label class="control-label">物流单号：</label>
		                                <input name="expressNumber" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
	                                </div>
                                </c:if>
                                <div class="search-item">
	                                <label class="control-label">收货关键字：</label>
	                                <input name="userName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <c:if test="${tabsFlag > 2}">
                                    <div class="search-item">
		                                <label class="control-label">订单状态：</label>
	                                    <select name="orderStatus" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option style="padding: 20px;" value="">全部</option>
	                                        <c:if test="${tabsFlag eq 3}">
		                                        <option style="padding: 20px;" value="0">待支付</option>
		                                        <option style="padding: 20px;" value="1">待发货</option>
		                                        <option style="padding: 20px;" value="2">已发货</option>
		                                        <option style="padding: 20px;" value="3">已完成</option>
		                                        <option style="padding: 20px;" value="4">兑换失败</option>
		                                        <option style="padding: 20px;" value="5">兑换中</option>
		                                        <option style="padding: 20px;" value="6">订单已关闭</option>
		                                        <option style="padding: 20px;" value="7">微信退款申请</option>
		                                        <option style="padding: 20px;" value="8">已撤单</option>
		                                        <option style="padding: 20px;" value="9">微信退款失败</option>
	                                        </c:if>
	                                        <option style="padding: 20px;" value="10">退货审核中</option>
	                                        <c:if test="${tabsFlag eq 3}">
		                                        <option style="padding: 20px;" value="11">退货已驳回</option>
	                                        </c:if>
	                                        <option style="padding: 20px;" value="12">待提交退货物流</option>
	                                        <option style="padding: 20px;" value="13">退货处理中</option>
	                                        <option style="padding: 20px;" value="14">退货已完成</option>
	                                    </select>
	                                </div>
                                </c:if>
                                <div class="search-item">
                                    <label class="control-label">支付单号：</label>
                                    <input name="transactionId" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">使用权益：</label>
                                    <select name="quanYiType" class="form-control input-width-larger search">
                                       <option value="">全部</option>
                                       <c:forEach items="${exchangeCardTypeMap}" var="item">
                                           <option value="${item.value}">${item.value}</option>
                                       </c:forEach>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">购买方式：</label>
                                    <select name="expressStatus" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option style="padding: 20px;" value="0">积分</option>
                                        <option style="padding: 20px;" value="1">金额</option>
                                        <option style="padding: 20px;" value="3">混合</option>
                                        <option style="padding: 20px;" value="4">礼品卡兑换</option>
                                    </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">订单时间：</label>
                                    <input name="exchangeStartDate" id="exchangeStartDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'exchangeEndDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="exchangeEndDate" id="exchangeEndDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'exchangeStartDate\')}'})" />
                                </div>
                                <c:if test="${tabsFlag > 0}">
                                <div class="search-item">
                                    <label class="control-label">发货时间：</label>
                                    <input name="sendStartDate" id="sendStartDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'sendEndDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="sendEndDate" id="sendEndDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'sendStartDate\')}'})" />
                                </div>
                                </c:if>
                                <c:if test="${tabsFlag > 1}">
                                <div class="search-item">
                                    <label class="control-label">到货时间：</label>
                                    <input name="signStartDate" id="signStartDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'signEndDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="signEndDate" id="signEndDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'signStartDate\')}'})" />
                                </div>
                                </c:if>

                                <div class="search-item">
                                    <label class="control-label">订单类型：</label>
                                    <select name="orderType" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option style="padding: 20px;" value="0">常规订单</option>
                                        <option style="padding: 20px;" value="1">秒杀订单</option>
                                        <option style="padding: 20px;" value="2">拼团订单</option>
                                        <option style="padding: 20px;" value="3">尊享卡订单</option>
                                        <option style="padding: 20px;" value="4">礼品卡订单</option>
                                        <option style="padding: 20px;" value="5">组合优惠订单</option>
                                        <option style="padding: 20px;" value="6">折扣订单</option>
                                        <option style="padding: 20px;" value="7">换购订单</option>
                                    </select>
                                </div>
		                    </div>
			            </div>
		            </div>
		            <div class="row">
		               <div class="col-md-12 text-center mart20">
		                   <button type="button" class="btn btn-primary btn-blue">查 询</button>
		                   <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
		               </div>
		            </div>
	                </form>
	            </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">订单列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"  id="dataTable_data">
                            <thead>
<!--                                 待发货 -->
	                            <c:if test="${tabsFlag eq '0' }">
                           			<tr>
	                                    <th style="width:8%;" data-ordercol="e.exchange_id">订单编号</th>
	                                    <th style="width:7%;" data-ordercol="b.brand_name">商品类型</th>
	                                    <th style="width:12%;" data-ordercol="g.goods_name">商品名称</th>
	                                    <th style="width:15%;" data-ordercol="e.user_name">收件人</th>
	                                    <th style="width:6%;" >订单类型</th>
	                                    <th style="width:5%;" data-ordercol="payType">购买方法</th>
	                                    <th style="width:7%;" data-ordercol="exchangePay">订单金额（元）</th>
                                        <th style="width:7%;" data-ordercol="exchangeVpoints">订单积分</th>
	                                    <th style="width:8%;" data-ordercol="quanYiType">使用权益</th>
	                                    <th style="width:8%;" data-ordercol="gf.goods_name">赠品名称</th>
	                                    <th style="width:9%;" data-ordercol="e.customer_message">留言内容</th>
	                                    <th style="width:9%;" data-ordercol="e.exchange_time" data-orderdef>订单时间</th>
	                                    <c:if test="${serverName eq 'henanpz' }">
	                                       <th style="width:9%;" data-ordercol="e.update_time" data-orderdef>领取时间</th>
	                                    </c:if>
	                                    <th style="width:6%;">操作</th>
	                                </tr>
	                            </c:if>
<!--                                 已发货 -->
	                            <c:if test="${tabsFlag eq '1' }">
                                    <tr>
                                        <th style="width:8%;" data-ordercol="e.exchange_id">订单编号</th>
                                        <th style="width:5%;" data-ordercol="b.brand_name">商品类型</th>
                                        <th style="width:10%;" data-ordercol="g.goods_name">商品名称</th>
                                        <th style="width:11%;" data-ordercol="e.user_name">收件人</th>
										<th style="width:5%;" >订单类型</th>
                                        <th style="width:5%;" data-ordercol="payType">购买方法</th>
                                        <th style="width:5%;" data-ordercol="exchangePay">订单金额（元）</th>
                                        <th style="width:5%;" data-ordercol="exchangeVpoints">订单积分</th>
                                        <th style="width:8%;" data-ordercol="quanYiType">使用权益</th>
                                        <th style="width:8%;" data-ordercol="gf.goods_name">赠品名称</th>
                                        <th style="width:9%;" data-ordercol="e.exchange_time" data-orderdef>订单时间</th>
                                        <th style="width:7%;" data-ordercol="e.express_number">物流单号</th>
                                        <th style="width:7%;" data-ordercol="e.express_send_time" data-orderdef>发货时间</th>
                                        <th style="width:6%;">操作</th>
                                    </tr>
	                            </c:if>
<!--                                 已完成 -->
	                            <c:if test="${tabsFlag eq '2' }">
                                    <tr>
                                        <th style="width:8%;" data-ordercol="e.exchange_id">订单编号</th>
                                        <th style="width:5%;" data-ordercol="b.brand_name">商品类型</th>
                                        <th style="width:7%;" data-ordercol="g.goods_name">商品名称</th>
                                        <th style="width:11%;" data-ordercol="e.user_name">收件人</th>
										<th style="width:5%;" >订单类型</th>
                                        <th style="width:5%;" data-ordercol="payType">购买方法</th>
                                        <th style="width:5%;" data-ordercol="exchangePay">订单金额（元）</th>
                                        <th style="width:5%;" data-ordercol="exchangeVpoints">订单积分</th>
                                        <th style="width:8%;" data-ordercol="quanYiType">使用权益</th>
                                        <th style="width:8%;" data-ordercol="gf.goods_name">赠品名称</th>
                                        <th style="width:4%;" >礼品卡</br>状态</th>
                                        <th style="width:7%;" data-ordercol="e.exchange_time">订单时间</th>
                                        <th style="width:7%;" data-ordercol="e.express_number">物流单号</th>
                                        <th style="width:5%;" data-ordercol="e.express_send_time">发货时间</th>
                                        <th style="width:5%;" data-ordercol="express_sign_time" data-orderdef>收货时间</th>
                                        <th style="width:6%;">操作</th>
                                    </tr>
	                            </c:if>
<!--                                 全部订单 -->
	                            <c:if test="${tabsFlag eq '3' }">
                                    <tr>
                                        <th style="width:7%;" data-ordercol="e.exchange_id">订单编号</th>
                                        <th style="width:5%;" data-ordercol="b.brand_name">商品类型</th>
                                        <th style="width:7%;" data-ordercol="g.goods_name">商品名称</th>
                                        <th style="width:11%;" data-ordercol="e.user_name">收件人</th>
										<th style="width:5%;" >订单类型</th>
                                        <th style="width:5%;" data-ordercol="payType">购买方法</th>
                                        <th style="width:6%;" data-ordercol="exchangePay">金额（元）</th>
                                        <th style="width:6%;" data-ordercol="exchangeVpoints">积分</th>
                                        <th style="width:8%;" data-ordercol="quanYiType">使用权益</th>
                                        <th style="width:7%;" data-ordercol="gf.goods_name">赠品名称</th>
                                        <th style="width:4%;" >礼品卡<br>状态</th>
                                        <th style="width:7%;" data-ordercol="e.exchange_time" data-orderdef>订单时间</th>
                                        <th style="width:6%;" data-ordercol="e.express_number">物流单号</th>
                                        <th style="width:6%;" data-ordercol="e.express_send_time">发货时间</th>
                                        <th style="width:6%;" data-ordercol="express_sign_time">收货时间</th>
                                        <th style="width:4%;" data-ordercol="e.express_status">状态</th>
                                    </tr>
	                            </c:if>
<!--                                 退货订单 -->
	                            <c:if test="${tabsFlag eq '4' }">
                                    <tr>
                                        <th style="width:7%;" data-ordercol="e.exchange_id">订单编号</th>
                                        <th style="width:5%;" data-ordercol="b.brand_name">商品类型</th>
                                        <th style="width:7%;" data-ordercol="g.goods_name">商品名称</th>
                                        <th style="width:11%;" data-ordercol="e.user_name">收件人</th>
										<th style="width:5%;" >订单类型</th>
                                        <th style="width:5%;" data-ordercol="payType">购买方法</th>
                                        <th style="width:5%;" data-ordercol="exchangePay">订单金额（元）</th>
                                        <th style="width:5%;" data-ordercol="exchangeVpoints">订单积分</th>
                                        <th style="width:8%;" data-ordercol="quanYiType">使用权益</th>
                                        <th style="width:8%;" data-ordercol="gf.goods_name">赠品名称</th>
                                        <th style="width:5%;" data-ordercol="express_sign_time" data-orderdef>签收时间</th>
                                        <th style="width:5%;" data-ordercol="e.goods_return_reason">退货原因</th>
                                        <th style="width:7%;" data-ordercol="e.goods_return_express_number">退货物流</th>
                                        <th style="width:5%;" data-ordercol="e.goods_return_time">退货时间</th>
                                        <th style="width:4%;" data-ordercol="e.express_status">状态</th>
                                        <th style="width:6%;">操作</th>
                                    </tr>
	                            </c:if>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span id="exchangeId">${item.exchangeId}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span id="brandName">${item.brandName}</span>
					        		</td>
					        		<td>
					        			<span id="goodsName">${item.goodsName}</span>
					        		</td>
                                    <td>
                                        <span id="userKey" style="display: none;">${item.userKey}</span>
                                        <span id="userName" style="display: none;">${item.userName}</span>
                                        <span id="phoneNum" style="display: none;">${item.phoneNum}</span>
                                        <span id="address" style="display: none;">${item.address}</span>
                                        <span>${item.userName} ${item.phoneNum} ${item.address}</span>
                                    </td>
									<td  style="text-align:center;">
										<span > ${item.orderType}</span>
									</td>
                                    <td style="text-align:center;">
                                        <c:choose>
                                        	<c:when test="${item.exchangeVpoints > 0 and  item.exchangePay > 0}">混合支付</c:when>
                                        	<c:when test="${item.giftCardPay > 0 and  item.exchangePay > 0}">金额支付 + 礼品卡兑换</c:when>
                                        	<c:when test="${item.giftCardPay > 0}">礼品卡兑换</c:when>
                                        	<c:when test="${item.exchangeVpoints > 0}">积分支付</c:when>
                                        	<c:when test="${item.exchangePay > 0}">金额支付</c:when>
                                        </c:choose>
                                    </td>
					        		<td style="text-align: right;">
                                        <span id="exchangeNum" style="display: none;">${item.exchangeNum}</span>
					        			<c:if test="${item.exchangePay > 0}"><span id="exchangeMoney" data-maxrefundpay="${item.exchangePay - item.refundPay}"><fmt:formatNumber value="${item.exchangePay/100}" pattern="#,##0.00#"/></span></c:if>
					        			<c:if test="${item.refundPay > 0}"><br/><span>已退：<fmt:formatNumber value="${item.refundPay/100}" pattern="#,##0.00#"/></span></c:if>
					        		</td>
					        		<td style="text-align: right;">
					        			<c:if test="${item.exchangeVpoints > 0}"><span id="exchangeVpoints" data-maxrefundvpoints="${item.exchangeVpoints - item.refundVpoints}"><fmt:formatNumber value="${item.exchangeVpoints}" pattern="#,##0"/></span></c:if>
					        			<c:if test="${item.refundVpoints > 0}"><br/><span>已退：<fmt:formatNumber value="${item.refundVpoints}" pattern="#,##0"/></span></c:if>
					        		</td>
                                    <td>
                                        <span>${item.quanYiType}</span>
                                    </td>
                                    <td>
                                        <c:if test="${not empty item.giftGoodsName}"><span>${item.giftGoodsName} x ${item.giftGoodsNum}</span></c:if>
                                    </td>
                                    <c:if test="${tabsFlag eq '2' or tabsFlag eq '3'}">
                                        <td style="text-align:center;">
                                            <span>
                                                <c:choose>
                                                    <c:when test="${item.giftCardStatus eq '1'}">已领取</c:when>
                                                    <c:when test="${item.giftCardStatus eq '0'}">未领取</c:when>
                                                    <c:otherwise>--</c:otherwise>
                                                </c:choose>
                                            </span>
                                        </td>
                                    </c:if>
                                    <c:choose>
                                        <c:when test="${tabsFlag eq 4}">
	                                        <td style="text-align:center;">
	                                            <span>${fn:substring(item.expressSignTime, 0, 19)}</span>
	                                        </td>
                                            <td style="text-align:center;">
                                                <span>${item.goodsReturnReason}</span>
                                            </td>
                                            <td>
                                                <span id="expressCompany">${item.goodsReturnExpressCompany}</span></br>
                                                <span id="expressNumber">${item.goodsReturnExpressNumber}</span></br>
												<span id="expressOrderId">${item.expressOrderId}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${fn:substring(item.goodsReturnTime, 0, 19)}</span>
                                            </td>
                                        </c:when>
                                        <c:otherwise>
                                            <c:if test="${tabsFlag eq '0'}">
                                                <td>
                                                    <span>${item.customerMessage}</span>
                                                </td>
                                            </c:if>
							        		<td style="text-align:center;">
							        			<span>${fn:substring(item.exchangeTime, 0, 19)}</span>
							        		</td>
							        		<c:if test="${serverName eq 'henanpz' and tabsFlag eq '0'}">
								        		<td style="text-align:center;">
								        			<span>${fn:substring(item.updateTime, 0, 19)}</span>
								        		</td>
								        	</c:if>
							        		<c:if test="${tabsFlag > 0}">
	                                            <td>
	                                                <span id="expressCompany">${item.expressCompany}</span></br>
	                                                <span id="expressNumber">${item.expressNumber}</span></br>
	                                                <span id="expressOrderId">${item.expressOrderId}</span>
	                                            </td>
								        		<td style="text-align:center;">
								        			<span>${fn:substring(item.expressSendTime, 0, 19)}</span>
								        		</td>
							        		</c:if>
							        		<c:if test="${tabsFlag > 1}">
								        		<td style="text-align:center;">
								        			<span>${fn:substring(item.expressSignTime, 0, 19)}</span>
								        		</td>
							        		</c:if>
                                        </c:otherwise>
                                    </c:choose>
					        		<c:if test="${tabsFlag eq '3' or tabsFlag eq '4'}">
						        		<td style="text-align:center;">
						        		    <span>${item.orderStatus}</span>
						        		</td>
					        		</c:if>
					        	
					        		<c:if test="${tabsFlag == '0' or tabsFlag == '1' or tabsFlag == '4' or tabsFlag == '2'}">
						        		<td data-key="${item.exchangeId}" data-gbstatus="${item.groupBuyingStatus }" style="text-align: center;">
						        			<c:if test="${tabsFlag eq '0' and item.categoryType ne 'SFJSD'}">
							        			<a class="btn btn-xs send btn-orange"><i class="iconfont icon-fahuo" style="font-size: 14px;"></i>&nbsp;发 货</a>
							        			<a class="btn btn-xs editAddr btn-orange"><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>&nbsp;修 改</a>
								        		<a class="btn btn-xs revoke btn-red"><i class="iconfont icon-yichu" style="font-size: 14px;"></i>&nbsp;撤 单</a>
							        		</c:if>
						        			<c:if test="${tabsFlag eq '1' and item.categoryType ne 'SFJSD'}">
							        			<a class="btn btn-xs editExp btn-orange"><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>&nbsp;修 改</a>
                                                <a class="btn btn-xs revoke btn-red"><i class="iconfont icon-yichu" style="font-size: 14px;"></i>&nbsp;撤 单</a>
						        			</c:if>
                                            <c:if test="${tabsFlag eq '4'}">
                                                <c:choose>
	                                                <c:when test="${item.exchangeStatus eq '7' }">
	                                                    <a class="btn btn-xs returnAudit btn-orange"><i class="iconfont icon-fahuo" style="font-size: 14px;"></i>&nbsp;审 核</a>
	                                                </c:when>
	                                                <c:when test="${item.exchangeStatus eq '8' }">
		                                                <a class="btn btn-xs returnConfirm btn-red"><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>&nbsp;退款确认</a>
	                                                </c:when>
	                                                <c:otherwise>——</c:otherwise>
                                                </c:choose>
                                            </c:if>
                                            <c:if test="${tabsFlag eq '2' and item.categoryType ne 'SFJSD'}">
                                                <a class="btn btn-xs revoke btn-red"><i class="iconfont icon-yichu" style="font-size: 14px;"></i>&nbsp;撤 单</a>
                                            </c:if>
						        		</td>
					        		</c:if>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        	  <c:choose>
					        	      <c:when test="${tabsFlag eq '0'}">
						        		<td colspan="13"><span>查无数据！</span></td>
					        	      </c:when>
					        	      <c:when test="${tabsFlag eq '1'}">
						        		<td colspan="14"><span>查无数据！</span></td>
					        	      </c:when>
					        	      <c:when test="${tabsFlag eq '2'}">
						        		<td colspan="14"><span>查无数据！</span></td>
					        	      </c:when>
					        	      <c:when test="${tabsFlag eq '3'}">
						        		<td colspan="15"><span>查无数据！</span></td>
					        	      </c:when>
					        	      <c:when test="${tabsFlag eq '4'}">
						        		<td colspan="16"><span>查无数据！</span></td>
					        	      </c:when>
					        	  </c:choose>
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
    <div class="modal fade" id="expressDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">物流发货</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                    	<tr>
                            <td width="25%" class="text-right"><label class="title">用户主键：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="userKey"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">兑换品牌：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="exchangeId" type="hidden"/>
                                    <span id="brandName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">商品名称：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="goodsName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">兑换数量：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="exchangeNum"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">收件人：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="userName" type="text" tag="validate"
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="30">
                                        <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">电话：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="phoneNum" type="text" tag="validate"
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="30">
                                        <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">地址：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="address" type="text" tag="validate"
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="100">
                                        <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">物流公司：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="expressCompany" type="text" 
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="10" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">物流单号：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="expressNumber" type="text" 
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                   </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="expressUploadDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">批量更新物流单号</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">发货清单：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input type="file" class="import-file" id="expressFile" name="filePath" single style="padding-bottom: 15px; margin-left: 10px;"/>
                                </div>
                            </td>
                        </tr>
                   </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="revokeOrderDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">退款确认</h4>
                    <input name="exchangeId" type="hidden"/>
                </div>
                <div class="modal-body">
                    <table class="active_board_table">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">退款类型：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <label><input type="radio" class="tab-radio" name="refundType" value="0" />撤单/退货</label>&nbsp;&nbsp;
                                    <label><input type="radio" class="tab-radio" name="refundType" value="1" />仅退款（订单状态不变）</label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">最大可退款金额：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input id="maxRefundMoney" type="text" 
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="30" disabled="disabled" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">最大可退款积分：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input id="maxRefundVpoints" type="text" 
                                        class="form-control input-width-larger required" autocomplete="off" maxlength="30" disabled="disabled" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">可退款金额：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input id="refundMoney" name="refundMoney" type="text"  tag="validate"
                                        class="form-control input-width-larger required money minValue maxValue" minVal="0.00" autocomplete="off" maxlength="30"/>
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">可退款积分：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input id="refundVpoints" name="refundVpoints" type="text"  tag="validate"
                                        class="form-control input-width-larger required integer minValue maxValue" minVal="0" autocomplete="off" maxlength="30"/>
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                        
                            <td width="25%" class="text-right"><label class="title">备注：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <textarea id="revokeOrderReason" name="revokeOrderReason"
                                        class="form-control input-width-larger required" autocomplete="off" rows="5" maxlength="255"></textarea>
                                </div>
                            </td>
                        </tr>
                   </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">取 消</button>
                </div>
            </div>
        </div>
    </div>
	</div>
  </body>
</html>
