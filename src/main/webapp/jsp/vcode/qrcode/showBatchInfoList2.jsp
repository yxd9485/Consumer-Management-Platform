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
    <title>青岛啤酒广西活动运营后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath }/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath }/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="${basePath }/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
	        var refresh = "${refresh}";
			if(refresh != "") {
				$("div.modal-body ."+refresh+"").show();
				$("div.modal-body :not(."+refresh+")").hide();
				$("#myModal").modal("show");
			}
			
			// 新增
			$("#createQrCode").click(function(){
				var url = "${basePath}/qrcodeBatchInfo/${vcodeActivityKey}/toAddBatchInfo.do?activityType=${activityCog.activityType}";
				$("#createQrCode").attr("href", url);        		         
			});
			// 导入码包
			$("#importQrCode").click(function(){
				var url = "${basePath}/qrcodeBatchInfo/${vcodeActivityKey}/toImportQrCode.do";
				$("#importQrCode").attr("href", url);        		         
			});
			
			// 导入批次二维码
			$("a.importBatchQrcode").click(function(){
				$("#divId").css("display","block");
				var fileName = $(this).parents("td").data("value");
				$("#fileName").val(fileName);
				var url = "${basePath}/qrcodeBatchInfo/importBatchQrcode.do";
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 删除批次
			$("a.delBatch").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/qrcodeBatchInfo/${vcodeActivityKey}/delBatchInfo.do?batchKey=" + key;
				 $.fn.confirm("确定要删除该批次吗？", function(){
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});
			
			// 修改批码时间
			$("a.editbatch").off();
			$("a.editbatch").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/qrcodeBatchInfo/toUpateBatchInfo.do?batchKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			//批次调整
			$("a.adjustbatch").off();
			$("a.adjustbatch").on("click",function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/qrcodeBatchInfo/toAdjustbatch.do?batchKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			//修改包码时间
			/* $("a.editpack").off();
			$("a.editpack").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "${basePath}/qrcodeBatchInfo/toUpatePackInfo.do?batchKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			}); */
			
			// 全选
			$("#allCB").on("change", function(){
				if($(this).prop("checked")){
					$("[name='itemCB'][disabled!='disabled']").attr("checked","checked");
				} else {
                    $("[name='itemCB'][disabled!='disabled']").removeAttr("checked", "");
				}
			});

            //批次调整
            $("a.adjustBatch").off();
            $("a.adjustBatch").on("click",function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                	key = $(this).val() + "," + key;
                })
                if (key == "") {
                	$.fn.alert("请选择要调整的批次!");
                } else {
	                var url = "${basePath}/qrcodeBatchInfo/toAdjustbatch.do?batchKey="+key;
	                $("form").attr("action", url);
	                $("form").submit(); 
                }
            });
            
            //创建码源订单
            $("a.adQrcodeOrder").off();
            $("a.adQrcodeOrder").on("click",function(){
                var key = "";
                $("[name='itemCB']:checked").each(function() {
                	key = $(this).val() + "," + key;
                })
                if (key == "") {
                	$.fn.alert("请选择生成订单所需批次!");
                } else {
	                var url = "${basePath}/qrcodeBatchInfo/toAdQrcodeOrder.do?batchKey="+key;
	                $("form").attr("action", url);
	                $("form").submit(); 
                }
            });
            
         	// 查询批次激活信息
            $("a.activateDetail").on("click", function(){
            	var batchKey = $(this).parents("td").data("key");
            	$.ajax({
		            type: "POST",
		            url: "<%=cpath%>/qrcodeBatchInfo/showActivateBatchLog.do",
		            async: false,
		            data: {"batchKey":batchKey},
		            dataType: "json",
		            beforeSend:appendVjfSessionId,
                    success:  function(result){
                        var content = "";
                        if(result){
                        	$.each(result, function(i, item) {
                        		content += "<tr><td>"+(i+1)+"</td><td>" + 
	                            	item.activatePhone +"("+item.activateUserName+")</td><td>" + 
	                            	item.activateTime +"</td><td>" + 
	                            	item.startDate +"</td><td>" + 
	                            	item.endDate +"</td></tr>";
                        	});
                        	$("#activateDiv #tbody").html(content);
                        } 
                        $("#activateDiv").modal("show");
		            },
		            error: function(data){
		            	$.fn.alert("查询异常!");
		            }
		        });
            });
		});
		function showMyFun(msg){
		    $(".modal-body .currentAlter").html(msg);
		    $("#myModal").modal("show");
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
        	<li class="current"><a>码源管理</a></li>
        	<c:if test="${activityCog.activityType eq '1' }">
        		<li class="current"><a title="">一罐一码规则</a></li>
        	</c:if>
        	<c:if test="${activityCog.activityType eq '2' }">
        		<li class="current"><a title="">一瓶一码规则</a></li>
        	</c:if>
        	<li class="current"><a title="">申请二维码</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="row">
                            <c:if test="${currentUser.roleKey ne '4'}">
                                <div class="dataTables_header clearfix">
                                    <div class="button_nav col-md-12">
	                                    <c:choose>
	                                        <c:when test="${activityCog.vcodeActivityName eq '默认批次专属活动' }">
	                                            <a id="importQrCode" href="#" class="btn importQrCode btn-red">
	                                                <i class="iconfont icon-daoru" style="font-size: 12px;"></i>&nbsp;导入码包文件</a>
	                                        </c:when>
	                                        <c:otherwise>
	                                            <a id="createQrCode" href="#" class="btn createQrCode btn-red">
	                                                <i class="iconfont icon-erweima" style="font-size: 12px;"></i>&nbsp;生成码源批次</a>
	                                        </c:otherwise>
	                                    </c:choose>
	                                    <c:if test="${fn:length(resultList) gt 0 and activityCog.vcodeActivityName ne '生成码源专属活动'}">
	                                    <a id="adjustBatch" href="#" class="btn adjustBatch btn-red">
	                                                <i class="iconfont icon-peizhi" style="font-size: 12px;"></i>&nbsp;批次批量调整</a>
	                                    </c:if>
	                                    <c:if test="${currentUser.roleKey ne '4'}">
	                                    	<a id="adjustBatch" href="#" class="btn adQrcodeOrder btn-primary">
	                                                <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;创建码源订单</a>
	                                    </c:if>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                        <div class="row">
                            <div class="dataTables_header clearfix">
			    				<form class="listForm" method="post"
			    					action="${basePath}/qrcodeBatchInfo/${vcodeActivityKey}/showBatchInfoList.do">
                            		<input type="hidden" class="tableTotalCount" value="${showCount}" />
                            		<input type="hidden" class="tableStartIndex" value="${startIndex}" />
                            		<input type="hidden" class="tablePerPage" value="${countPerPage}" />
                            		<input type="hidden" class="tableCurPage" value="${currentPage}" />
                                    <input type="hidden" name="queryParam" value="${queryParam}" />
                                    <input type="hidden" name="pageParam" value="${pageParam}" />
                                    <input type="hidden" name="activityType" value="${activityCog.activityType}" />
                                    <input type="hidden" name="vcodeActivityKey" value="${vcodeActivityKey}">
                                    <input type="hidden" name="fileName" id="fileName">
                            		<div class="form-group little_distance">
                            			<label class="control-label">导入批号：</label>
		                             	<input name="batchNo" id="batchNo" class="form-control input-width-medium" value="${batchInfo.batchNo }"/>
		                               	<label class="control-label">批码编号：</label>
		                             	<input name="batchDesc" id="batchDesc" class="form-control input-width-medium" value="${batchInfo.batchDesc }"/>
		                             	<label class="control-label">批码名称：</label>
		                             	<input name="batchName" id="batchName" class="form-control input-width-medium" value="${batchInfo.batchName }"/>
		                             	<label class="control-label">批码时间：</label>
		                             	<input name="startDate" id="startDate" class="input-width-medium Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd' });" value="${batchInfo.startDate }" style="margin-right: 0px;"/> 
		                             	<label class="control-label">至</label> 
		                             	<input name="endDate" id="endDate" class="input-width-medium Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'startDate\')}' });" value="${batchInfo.endDate }"/>
		                             	<label class="control-label">激活时间：</label>
		                             	<input name="activateTime" id="activateTime" class="input-width-medium Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd' });" value="${batchInfo.activateTime }" style="margin-right: 0px;"/> 
		                             	<button type="button" class="btn btn-primary btn-blue">搜 索</button>
		                             	<button type="button" class="btn btn-reset btn-radius3">重 置</button>
	                            	</div>
			    				</form>
                            </div>
                        </div>
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
					            	<th style="width:3%;">序号</th>
					            	<th style="width:9%;">导入批号</th>
					            	<th style="width:14%;">批码编号</th>
					            	<th style="width:14%;">批码名称</th>
					            	<th style="width:12%;">批码时间</th>
					            	<th style="width:5%;">码数量</th>
					            	<!-- 
					            	<th style="width:10%;">已扫码数量</th>
					            	<th style="width:10%;">未扫码数量</th> 
					            	-->
					            	<th style="width:5%;">包码数</th>
					            	<th style="width:5%;">状态</th>
					            	<th style="width:10%;">激活人</th>
					            	<th style="width:10%;">激活时间</th>
					            	<c:if test="${currentUser.roleKey ne '4'}">
					            	<th style="width:19%;">操作</th>
					            	</c:if>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="batchInfo" varStatus="idx">
					        	<tr>
					        	   <td>
					        	      <input name="itemCB" type="checkbox" value="${batchInfo.batchKey}" <c:if test="${batchInfo.isBegin != '0' && (activityCog.activityType != '3' || batchInfo.importFlag != '1')}">disabled="disabled"</c:if> />
					        	   </td>
					        		<td style="text-align:center;
					        			<c:choose>
                                            <c:when test="${batchInfo.isBegin == '0'}">background-color: #ecb010;</c:when>
                                            <c:when test="${batchInfo.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${batchInfo.isBegin == '2'}">background-color: #999999;</c:when>
                                            <c:when test="${vcodeActivityName eq '默认批次专属活动' and batchInfo.importFlag eq '0'}">background-color: red;</c:when>
                                        </c:choose>">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td>${ batchInfo.batchNo}</td>
					        		<td>${ batchInfo.batchDesc}</td>
					        		<td>${ batchInfo.batchName}</td>
					        		<td>
					        			<span>从&nbsp;${batchInfo.startDate}</span>
					        			<span>到&nbsp;${batchInfo.endDate}</span>
					        		</td>
					        		<td>
					        			<span>${batchInfo.qrcodeAmounts}</span>
					        		</td>
					        		<%-- 
					        		<td>
					        			<span>${batchInfo.scanCodeNum}</span>
					        		</td>
					        		<td>
					        			<span>${batchInfo.notScanCodeNum}</span>
					        		</td>
					        		--%>
					        		<td>
					        			<span>${batchInfo.packAmounts}</span>
					        		</td>
					        		<td style="text-align:center;">
				        				<c:choose>
                                            <c:when test="${batchInfo.isBegin == '0'}">
                                                <span style="color: #ecb010;"><b>待上线</b></span>
                                            </c:when>
                                            <c:when test="${batchInfo.isBegin == '1'}">
                                                <span style="color: #ea918c;"> <b>已上线</b> </span>
                                            </c:when>
                                            <c:when test="${batchInfo.isBegin == '2'}">
                                                <span style="color: #999999;"> <b>已下线</b> </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red;"><b>${batchInfo.isBegin}</b></span>
                                            </c:otherwise>
                                        </c:choose>
					        		</td>
					        		<td>
					        			<span>${batchInfo.activatePhone}</span>
					        			<c:if test="${fn:length(batchInfo.activateUserName) > 0}">(${batchInfo.activateUserName})</c:if>
					        		</td>
					        		<td>
					        			<span>${batchInfo.activateTime}</span>
					        		</td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
						        		<td data-key="${batchInfo.batchKey}" data-value="${batchInfo.fileName}">
						        			<c:choose>
						        				<c:when test="${vcodeActivityName eq '默认批次专属活动' and batchInfo.importFlag eq '0'}">
						        					<a class="btn btn-xs importBatchQrcode btn-orange"><i class="iconfont icon-daoru" style="font-size: 14px;"></i>&nbsp;导入二维码</a>
						        					<a id ="delBatch" class="btn btn-xs delBatch btn-red"><i class="iconfont icon-lajixiang" style="font-size: 15px;"></i>&nbsp;删 除</a>
						        				</c:when>
						        				<c:otherwise><a class="btn btn-xs editbatch btn-brownness"><i class="iconfont icon-xiugai"></i>&nbsp;修改</a></c:otherwise>
						        			</c:choose>
						        			<a class="btn btn-xs activateDetail btn-brownness">激活详情</a>
						        		</td>
						        	</c:if>
					        	</tr>
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
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
	      		<div style="padding-left: 20px; padding-top: 10px">${errorMsg}</div>
      			<div class="modal-body" style="padding: 5px;">
      				<h6 class="add_success">添加成功</h6>
      				<h6 class="add_failure">添加失败</h6>
      				<h6 class="update_success">编辑成功</h6>
      				<h6 class="update_failure">编辑失败</h6>
      				<h6 class="update_batch_success">批次调整成功</h6>
      				<h6 class="update_batch_failure">批次调整失败</h6>
      				<h6 class="del_batch_success">批次删除成功</h6>
      				<h6 class="del_batch_failure">批次删除失败</h6>
      				<h6 class="add_order_failure"></h6>
      				<%-- 
      				<h6 class="import_fail">${errorMsg}</h6>
      				<h6 class="import_success">${errorMsg}</h6> 
      				--%>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
	<div class="modal fade" id="activateDiv" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog" style="width: 750px;">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">激活信息</h4>
	      		</div>
	      		<div style="padding-left: 10px; padding-right: 10px; padding-top: 10px">
					<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:12%;">激活人</th>
					            	<th style="width:12%;">激活时间</th>
					            	<th style="width:10%;">开始时间</th>
					            	<th style="width:10%;">结束时间</th>
					            </tr>
					        </thead>
					        <tbody id="tbody"></tbody>
                        </table>
				</div>
	      		<div class="modal-footer" style="text-align: center;">
			        <button type="button" class="btn btn-default btn-blue" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
	</div>
  </body>
</html>
