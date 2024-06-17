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
    <title>青啤运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="${basePath }/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
	        var refresh = "${refresh}";
			if(refresh != "") {
				$("div.modal-body ."+refresh+"").show();
				// $("div.modal-body :not(."+refresh+")").hide();
				$("#myModal").modal("show");
			}
			
			// 生成码
			$("a.addOrder").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var url = "${basePath}/vpsQrcodeOrder/createCode.do?orderKey=" + key;
				 $.fn.confirm("确定要生成码源吗？", function(){
					$td.html("进行中...");
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			//	 $(this).parent("td").html("进行中...");
			});


			// 重新生成码（只有失败状态才会重新生成）
			$("a.anewAddOrder").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var url = "${basePath}/vpsQrcodeOrder/createCode.do?orderKey=" + key;
				 $.fn.confirm("确定要生成码源吗？", function(){
					$td.html("进行中...");
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});
			/**
			 * 自动将form表单封装成json对象
			 */
			$.fn.serializeObject = function() {
				var o = {};
				var a = this.serializeArray();
				$.each(a, function() {
					if (o[this.name]) {
						if (!o[this.name].push) {
							o[this.name] = [ o[this.name] ];
						}
						o[this.name].push(this.value || '');
					} else {
						o[this.name] = this.value || '';
					}
				});
				return o;
			};
			// 重新生成码（只有失败状态才会重新生成）
			$("#exportOrder").click(function(){
				 $.fn.confirm("确定要导出订单吗？", function(){
					 $("form").attr("action", "<%=cpath%>/vpsQrcodeOrder/exportOrder.do");
					 var submit = $("form").submit();
					 // 还原查询action
					 $("form").attr("action", "<%=cpath%>/major/showQrcodeOrderList.do");
				 });
			});

			// 重新生成打印标签
			$("a.newAddLabel").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var url = "${basePath}/vpsQrcodeOrder/newAddLabel.do?orderKey=" + key;
				$.fn.confirm("确定要重新生成打印标签？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});
			
			// 删除订单
			$("a.delOrder").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/vpsQrcodeOrder/doQrcodeOrderDelete.do?orderKey=" + key;
				 $.fn.confirm("确定要撤销该订单吗？", function(){
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});
			

            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']").prop("checked","checked");
                } else {
                    $("[name='itemCB']").prop("checked","");
                }
            });
            
            // 实现Collapse同时只展开一个的功能
            $(".paneltitle").on("click", function(){
                var collapse = $(this).attr("aria-controls");
                $("[id^='collapseInner']").each(function(obj){
                    if ($(this).attr("id") != collapse) {
                        $(this).removeClass("in");
                        $(this).addClass("collapse");
                    }
                });
            });
            
          //创建码源订单
            $("a.adQrcodeOrder").off();
            $("a.adQrcodeOrder").on("click",function(){
                var url = "${basePath}/vpsQrcodeOrder/showQrcodeOrderAdd.do?";
                $("form").attr("action", url);
                $("form").submit(); 
                
            });
            
         	// 发送码包二次确认
			$("a.sendMeilPack").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var confirmUrl = "${basePath}/vpsQrcodeOrder/confirmMeil.do?orderKey=" + key;	          
	            $.ajax({
	                  type: "POST",
	                  url: confirmUrl,
	                  dataType: "json",
	                  async: false,
	                  contentType : false,
	                  processData: false,
	                  beforeSend:appendVjfSessionId,
	                  success: function(data) {
	                         $("#mailBatchDialog").modal("show");
	                         	var vpsQrcodeOrder= data['vpsQrcodeOrder'];
	                        	for(var name in vpsQrcodeOrder) {
	                         	  $("#mailBatchDialog label[name='" + name + "']").text(vpsQrcodeOrder[name]);	 	                        			                        	
	                       }                        		                     
	                   },
	                   error: function(data){
                           $.fn.alert("错误!请联系研发");
                       }
	              });
			});
         	
         	
			
       	
			//发送码包
            $("#mailBatchDialog").delegate("#sendMailBtn", "click", function(){
            	 // 弹出遮罩层
                $("#qrcodeFloatDiv").show();
            	 var url = "${basePath}/vpsQrcodeOrder/sendMeilMessage.do";
            	 var formData = new FormData();
            	 formData.append("orderKey", $("#mailBatchDialog label[name='orderKey']").text());
            	 formData.append("isLabel", $("#mailBatchDialog label[name='isLabel']").text());
            	 formData.append("orderNo", $("#mailBatchDialog label[name='orderNo']").text());
            	 formData.append("skuKey", $("#mailBatchDialog label[name='skuKey']").text());
            	 formData.append("orderTime", $("#mailBatchDialog label[name='orderTime']").text());
            	 formData.append("orderQrcodeCount", $("#mailBatchDialog label[name='orderQrcodeCount']").text());
            	 formData.append("brewery", $("#mailBatchDialog label[name='brewery']").text());
            	 formData.append("qrcodeManufacture", $("#mailBatchDialog label[name='qrcodeManufacture']").text());
            	 formData.append("createTime", $("#mailBatchDialog label[name='createTime']").text());
            	 formData.append("imageUrl", $("#mailBatchDialog label[name='imageUrl']").text());
            	 formData.append("orderName", $("#mailBatchDialog label[name='orderName']").text());
            	 formData.append("bagName", $("#mailBatchDialog label[name='bagName']").text());
            	 formData.append("bagEmail", $("#mailBatchDialog label[name='bagEmail']").text());
            	 formData.append("clientOrderNo", $("#mailBatchDialog label[name='clientOrderNo']").text());
            	 formData.append("isWanBatch", $("#mailBatchDialog label[name='isWanBatch']").text());
            	 formData.append("bagTel", $("#mailBatchDialog label[name='bagTel']").text());
          
            	 $.ajax({
						type: "POST",
		                url: url,
		                data: formData,
		                dataType: "json",
		                async: true,
		                contentType : false,
	                    processData: false,
	                    beforeSend:appendVjfSessionId,
		                success:function(data) {	  
		                	 // 隐藏遮罩层
	                        $("#qrcodeFloatDiv").css("display","none");
	                        $("#mailBatchDialog #closeBtn").trigger("click");
	                        $.fn.alert(data['errMsg'], function(){
	                            $("button.btn-primary").trigger("click");
	                        });
	                    }
					});
            });
         	
			
			
			
         	// 发送密码二次确认
			$("a.sendMeilPassword").click(function(){
				var $td = $(this).parent("td");
				var key = $td.data("key");
				var confirmUrl = "${basePath}/vpsQrcodeOrder/confirmMeil.do?orderKey=" + key;	          
	            $.ajax({
	                  type: "POST",
	                  url: confirmUrl,
	                  dataType: "json",
	                  async: false,
	                  contentType : false,
	                  processData: false,
	                  beforeSend:appendVjfSessionId,
	                  success: function(data) {
              	  			 	$("#passwordBatchDialog").modal("show");
	                         	var vpsQrcodeOrder= data['vpsQrcodeOrder'];
	                        	for(var name in vpsQrcodeOrder) {
	                         	$("#passwordBatchDialog label[name='" + name + "']").text(vpsQrcodeOrder[name]);	 	                        			                        	
	                       }
              	  				                	  		                                                		                     
	                   },
	                   error: function(data){
                           $.fn.alert("错误!请联系研发");
                       }
	              });
			});
         	
         	
			//发送密码邮件
            $("#passwordBatchDialog").delegate("#sendMailBtn", "click", function(){
            	 // 弹出遮罩层
                $("#qrcodeFloatDiv").show();
            	 var url = "${basePath}/vpsQrcodeOrder/sendMeilPassWord.do";
            	 var formData = new FormData();  
            	 formData.append("orderKey", $("#passwordBatchDialog label[name='orderKey']").text());
            	 formData.append("orderName", $("#passwordBatchDialog label[name='orderName']").text());
            	 formData.append("clientOrderNo", $("#passwordBatchDialog label[name='clientOrderNo']").text());
            	 formData.append("passwordName", $("#passwordBatchDialog label[name='passwordName']").text());
            	 formData.append("passwordEmail", $("#passwordBatchDialog label[name='passwordEmail']").text());
            	 formData.append("csEmail", $("#passwordBatchDialog label[name='csEmail']").text());
            	 formData.append("packagePassword", $("#passwordBatchDialog label[name='packagePassword']").text());
            	 formData.append("orderStatus", $("#passwordBatchDialog label[name='orderStatus']").text());
            	 formData.append("isWanBatch", $("#passwordBatchDialog label[name='isWanBatch']").text());
            	 formData.append("passwordTel", $("#passwordBatchDialog label[name='passwordTel']").text());
            	 $.ajax({
						type: "POST",
		                url: url,
		                data: formData,
		                dataType: "json",
		                async: true,
		                contentType : false,
	                    processData: false,
	                    beforeSend:appendVjfSessionId,
		                success:function(data) {	
		                	 // 隐藏遮罩层
	                        $("#qrcodeFloatDiv").css("display","none");
	                        $("#passwordBatchDialog #closeBtn").trigger("click");
	                        $.fn.alert(data['errMsg'], function(){
	                            $("button.btn-primary").trigger("click");
	                        });
	                    }
					});
            });
			
		 });

        function downloadQrcode(orderKey, projectServerName, orderNo, orderName) {
            var url = "${basePath}/vpsQrcodeOrder/downloadCode.do?orderKey=" + orderKey + "&projectServerName="
                + projectServerName + "&orderNo=" + orderNo + "&orderName=" + orderName
                + "&vjfSessionId=" + $("#vjfSessionId").val();
            window.open(url,"_blank");
        }

	</script>
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
			text-align: center;
		}
	</style>
  </head>
  
  <body>
  	<div id="divId" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
  		<h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
  	</div>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 码源管理</a></li>
            <li class="current"><a title="">码源订单管理</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">码源订单查询</span></div>
                   <div class="col-md-10 text-right">
                    		<a id="addOrder" class="btn btn-blue adQrcodeOrder">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 创建码源订单
	                       </a>
						   <a id="exportOrder" class="btn btn-blue">
							   <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 导出订单
						   </a>
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" action="${basePath}/vpsQrcodeOrder/showQrcodeOrderList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                        <input type="hidden" class="tableOrderType" value="${orderType}" />
                        <input type="hidden" name="queryParam" value="${queryParam}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                    <div class="row">
                        <div class="col-md-12 ">
	                        <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
		                            <label class="control-label">码源订单编号：</label>
		                            <input name="orderNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div> 
                                <div class="search-item">
		                            <label class="control-label">客户订单编号：</label>
		                            <input name="clientOrderNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">订单名称：</label>
		                            <input name="orderName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">SKU：</label>
	                                <select name="skuKey" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
		                                <c:if test="${not empty skuLst}">
			                                <c:forEach items="${skuLst}" var="sku">
			                                  <option value="${sku.skuKey}">${sku.skuName}</option>
			                                </c:forEach>
		                                </c:if>
	                                </select>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">赋码厂：</label>
	                                <select name="qrcodeManufacture" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <c:if test="${not empty qrcodeManufactureGro}">
		                                    <c:forEach items="${qrcodeManufactureGro}" var="item">
		                                       <option value="${item.factoryName}">${item.factoryName}</option>
		                                    </c:forEach>
	                                    </c:if>
	                                </select>
                                </div>
                                <div class="search-item">
		                            <label class="control-label">生产工厂：</label>
	                                <select name="brewery" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <c:if test="${not empty factoryNameGro}">
	                                        <c:forEach items="${factoryNameGro}" var="item">
	                                           <option value="${item.wineryName}">${item.wineryName}</option>
	                                        </c:forEach>
	                                    </c:if>
	                                </select>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">订单状态：</label>
	                                <select name="orderStatus" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <option value="0">未生成</option>
	                                    <option value="1">成功</option>
	                                    <option value="2">失败</option>
	                                </select>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">回传状态：</label>
	                                <select name="importFlag" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <option value="0">未回传</option>
	                                    <option value="1">已回传</option>
	                                </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">创建时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">码源订单列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20" id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,9,13,10,6,7,6,5,7,8,10" />
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:8%;" data-ordercol="qr.order_no">码源订单编号</th>
					            	<th style="width:8%;" data-ordercol="qr.client_order_no">客户订单编号</th>
					            	<th style="width:15%;" data-ordercol="qr.order_name">订单名称</th>
					            	<th style="width:12%;" data-ordercol="sku.sku_name">SKU</th>
					            	<th style="width:5%;" data-ordercol="qr.qrcode_manufacture">赋码厂</th>
					            	<th style="width:6%;" data-ordercol="qr.brewery">生产工厂</th>
					            	<th style="width:5%;" data-ordercol="qr.qrcode_percent">赋码率</th>
					            	<th style="width:7%;" >文字描述</th>
					            	<th style="width:6%;" data-ordercol="orderAndImportStatus">订单状态</th>
					            	<th style="width:8%;" data-ordercol="qr.create_time" data-orderdef>创建时间</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:16%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="order" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${order.orderNo}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${order.clientOrderNo}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: left;">${order.orderName}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: left;">${order.skuName}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: center;">${order.qrcodeManufacture}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; text-align: left;">${order.brewery}</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px; "><fmt:formatNumber type="number" maxFractionDigits="4" value="${order.qrcodePercent}"/>%</td>
					        		<td  class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px;  text-align: center;">
                                        <c:if test="${not empty order.noPrizeDesc}">无奖：${order.noPrizeDesc}<br/></c:if>
                                        <c:if test="${not empty order.prizeDesc}">有奖：${order.prizeDesc}</c:if>
					        		</td>
					        		<td  class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px;  text-align: center;">
				        				<c:choose>
                                            <c:when test="${order.orderAndImportStatus == '0'}">未生成</c:when>
                                            <c:when test="${order.orderAndImportStatus == '1'}">成功</c:when>
                                            <c:when test="${order.orderAndImportStatus == '2'}">失败</c:when>
                                            <c:when test="${order.orderAndImportStatus == '3'}">进行中</c:when>
                                            <c:when test="${order.orderAndImportStatus == '4'}">已发送码包邮件</c:when>
                                            <c:when test="${order.orderAndImportStatus == '5'}">已发送密码邮件</c:when>
                                            <c:when test="${order.orderAndImportStatus == '6'}">已回传</c:when>
                                        </c:choose>
					        		</td>
					        		<td class="paneltitle" data-toggle="collapse" data-parent="#accordionRule${idx.count}" href="#collapseInnerArea${idx.count}" aria-expanded="false" aria-controls="collapseInnerArea${idx.count}" style="cursor: pointer;  border-top-width: 0px;  text-align: center;"><span>${fn:substring(order.createTime, 0, 19)}</span></td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
						        		<td id="operationTd" data-key="${order.orderKey}" data-value="">
						        			<c:if test="${currentUser.roleKey eq '1' and order.orderStatus eq '0'}">
						        				<a class="btn btn-xs delOrder btn-red"></i>&nbsp;删 除</a>
						        			</c:if>
						        			<c:if test="${order.orderStatus eq '0'}">
						        				<a class="btn btn-xs addOrder btn-orange"></i>&nbsp;生成码源</a>
						        			</c:if>
						        			<c:if test="${order.orderStatus eq '2'}">
						        				<a class="btn btn-xs anewAddOrder btn-orange"></i>&nbsp;重新生成</a>
						        			</c:if>
											<c:if test="${currentUser.roleKey eq '1' and order.isLabel eq '1'}">
												<a class="btn btn-xs newAddLabel btn-orange"></i>&nbsp;重新生成打印标签</a>
											</c:if>
						        			<c:if test="${order.orderStatus eq '3'}">进行中...</c:if>
							        		<c:if test="${order.orderStatus eq '1' or order.orderStatus eq '4' or order.orderStatus eq '5'}">	
												<c:if test="${order.orderStatus eq '1'}">
							        				<a  class="btn btn-xs sendMeilPack btn-orange"  >&nbsp;发送码包邮件</a>
												</c:if>
												<c:if test="${order.orderStatus != '5'}">
							        				<a class="btn btn-xs sendMeilPassword btn-orange" >&nbsp;发送密码邮件</a>
												</c:if>
                                                <c:if test="${order.orderStatus eq '1' and order.isLabel eq '1'}">
                                                    <a class="btn btn-xs downlabeImage btn-orange"
                                                       onclick="downloadQrcode('${order.orderKey}', '${projectServerName}', '${order.orderNo}', '${order.orderName}_labeImage')">
                                                        &nbsp;下载打印标签</a>
                                                </c:if>
                                                <a class="btn btn-xs downloadQrcode btn-orange"
                                                   onclick="downloadQrcode('${order.orderKey}', '${projectServerName}', '${order.orderNo}', '${order.orderName}')">
                                                    &nbsp;下载码包码源</a>
							        		</c:if>
						        		</td>
						        	</c:if>
					        	</tr>
					        	
					        	
					        	
					        	<tr style="background-color: white;"><td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">12</c:when><c:otherwise>11</c:otherwise></c:choose>" style="margin: 0px; padding: 0px; border-bottom-width: 1px; border-top-width: 0px;">
                                   <div id="collapseInnerArea${idx.count}" class="panel-collapse collapse" role="tabpanel" aria-labelledby="heading${idx.count}">
                                     <div class="panel-body" style="padding:5px;">
                                     
                                         <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top tableborder">
                                          <thead>
                                              <tr class="success" style="background-color: #f0d2d0;">
	                                              	<th style="width:5%;">序号</th>
									            	<th style="width:30%;">批码编号</th>
									            	<th style="width:35%;">批码名称</th>
									            	<th style="width:15%;">码数量</th>
									            	<th style="width:15%;">包码数</th>
                                              </tr>
                                          </thead>
                                          <tbody>
                                          <c:choose>
                                              <c:when test="${fn:length(order.batchInfoList) gt 0}">
                                                 <c:forEach items="${order.batchInfoList}" var="batchInfo" varStatus="batchIdx">
                                                  <tr>
                                                  		<td style="text-align:center;">
					                                        <span>${batchIdx.count}</span>
										        		</td>
										        		<td>${ batchInfo.batchDesc}</td>
										        		<td>${ batchInfo.batchName}</td>
										        		<td><span>${batchInfo.qrcodeAmounts}</span></td>
										        		<td><span>${batchInfo.packAmounts}</span></td>
                                                  </tr>
                                                 </c:forEach>
                                              </c:when>
                                              <c:otherwise>
                                                  <tr>
                                                      <td colspan="5"><span>查无数据！</span></td>
                                                  </tr>
                                              </c:otherwise>
                                          </c:choose>
                                          </tbody>
                                         </table>
                                     </div>
                                   </div>
                                  </td></tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">12</c:when><c:otherwise>11</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
    <div class="modal fade" id="myModal" name="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
	      		<div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
      			<div class="modal-body" style="padding: 5px;">
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
  <div id="qrcodeFloatDiv" style="width:100%; height: 100%; border: 1px solid #D4CD49; background-color:rgba(0,0,0,0.5); position:fixed;left:0;top:0;z-index: 9999; display: none;">
        <h2 align="center" style="margin-top: 21%;color: blue;"><b>处理中,请勿其他操作.....</b></h2>
    </div>
	
	 <div class="modal fade" id="mailBatchDialog" name="mailBatchDialog"  tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">确定发送码包邮件</h4>
                </div>
                <div class="modal-body">
	                <table class="active_board_table">
	                	<label  name="orderKey" style="display: none;"> </label>
	                	<label 	name="isLabel" style="display: none;"></label>
	                	<label 	name="skuKey" style="display: none;"></label>
	                	<label 	name="orderTime" style="display: none;"></label>
	                	<label 	name="orderQrcodeCount" style="display: none;"></label>
	                	<label 	name="brewery" style="display: none;"></label>
	                	<label 	name="qrcodeManufacture" style="display: none;"></label>	                	
	                	<label 	name="orderNo" style="display: none;"></label>	                	
	                	<label 	name="createTime" style="display: none;"></label>
	                	<label 	name="imageUrl" style="display: none;"></label>
	                	<label  name="isWanBatch" style="display: none;"> </label>
	                	<label  name="bagTel" style="display: none;"> </label>
                        <tr>
		                    <td width="25%" class="text-right"><label class="title">客户订单号：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="clientOrderNo" style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>
		                 <tr>
		                    <td width="25%" class="text-right"><label class="title">订单名称：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="orderName" style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">码包接收人：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="bagName"  style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>	
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">邮箱：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="bagEmail"  style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>	                            
	               </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="sendMailBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="passwordBatchDialog" name="passwordBatchDialog"  tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">确定发送密码邮件</h4>
                </div>
                <div class="modal-body">
	                <table class="active_board_table">
	                	<label 	name="packagePassword" style="display: none;"></label>
	                	<label 	name="orderStatus" style="display: none;"></label>
	                	<label  name="orderKey" style="display: none;"> </label>
	                	<label  name="isWanBatch" style="display: none;"> </label>
	                	<label  name="passwordTel" style="display: none;"> </label>
                        <tr>
		                    <td width="25%" class="text-right"><label class="title">客户订单号：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="clientOrderNo" style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>
		                 <tr>
		                    <td width="25%" class="text-right"><label class="title">订单名称：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="orderName" style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">码包接收人：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="passwordName"  style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>	
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">邮箱：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="passwordEmail"  style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>	
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">同时密送邮箱：<span class="required"></span></label></td>
		                    <td>
		                        <div class="content">
		        					<label name="csEmail"  style="line-height: 30px;"></label>
		                        </div>
		                    </td>
		                </tr>                            
	               </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="sendMailBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
	</div>
  </body>
</html>
