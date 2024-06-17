<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>
	<script>
		$(function(){

            // 初始化校验控件
            $.runtimeValidate($("#updateBatchDialog"));
            $.runtimeValidate($("#editContractYearDialog"));
			
            // 激活详情
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
            
            // 实时计算批次有效期
            $("#updateBatchDialog input[name='startDate'], #updateBatchDialog input[name='endDate']").on("focus", function(){
                var $startDate = $("#updateBatchDialog input[name='startDate']");
                var $endDate = $("#updateBatchDialog input[name='endDate']");
                var $diffDays = $("#updateBatchDialog #diffDays");
                var $validDays = $("#updateBatchDialog #validDays");
                if (Number($validDays.val()) > 0) {
                    if ($startDate.val() != "" && $endDate.val() == "") {
                        var endDate = new Date($startDate.val());
                        endDate.setDate(endDate.getDate() + Number($validDays.val()) - 1);
                        $endDate.val(endDate.Format("yyyy-MM-dd"));
                        $diffDays.text($validDays.val());
                    } else if ($startDate.val() == "" && $endDate.val() != "") {
                        var startDate = new Date($endDate.val());
                        startDate.setDate(startDate.getDate() - Number($validDays.val()) + 1);
                        $startDate.val(startDate.Format("yyyy-MM-dd"));
                        $diffDays.text($validDays.val());
                    }
                }
                
                if ($startDate.val() != "" && $endDate.val() != "") {
                    var startDate = new Date($startDate.val());
                    var endDate = new Date($endDate.val());
                    var days = (endDate - startDate)/ 1000 / 60 / 60 / 24 + 1;
                    if (days >= 0) {
                        $diffDays.text(days);
                    }
                }
            });
            
            // 弹出修改批次窗口
            $("a.edit").off();
            $("a.edit").on("click", function(){
                $("#updateBatchDialog input[name][type!='hidden']").val("");
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/qrcodeBatchInfo/showBatchValidDateUpdate.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {batchKey : key},
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#updateBatchDialog").modal("show");
                        $.each(data, function(key, val){
                        	$item = $("#updateBatchDialog #" + key);
                        	if ($item) {
	                            $("#updateBatchDialog #" + key).text(val);
	                            $("#updateBatchDialog #" + key).val(val);
                        	}
                        });
                    }
                });
            }); 

            // 确定修改批次
            $("#updateBatchDialog").delegate("#addBtn", "click", function(){
                
                // 输入元素校验
                var validateResult = $.submitValidate($("#updateBatchDialog"));
                if(!validateResult){
                    return false;
                }
                // 提交表单
                var url = "<%=cpath%>/qrcodeBatchInfo/doBatchValidDateUpdate.do";
                var paramJson = {};
                $("#updateBatchDialog input[name]").each(function(){
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
                        $("#updateBatchDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
            });

            //批次移出
            $("#removeBatch").off();
            $("#removeBatch").on("click",function(){
                var key = "";
                var batchName = "您确定移除以下批次吗? </br>"
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                    batchName = batchName + $(this).closest("tr").find("#batchName").text() + "</br>";
                })
                if (key == "") {
                    $.fn.alert("请选择要移出的批次!");
                } else {
                	$.fn.confirm(batchName, function(){
	                    var url = "${basePath}/qrcodeBatchInfo/doUpdateBatchBackForImport.do?batchKey="+key;
	                    $("form").attr("action", url);
	                    $("form").submit(); 
                	});
                }
            });

            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']:enabled").prop("checked","checked");
                } else {
                    $("[name='itemCB']:enabled").prop("checked","");
                }
            });
        });
	</script>
  
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动配置码源查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
		                   <a id="removeBatch" class="btn btn-red">
		                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 移出码源
		                   </a>
                           <a id="editContractYear" class="btn btn-blue">
                               <i class="iconfont icon-xiugai" style="font-size: 12px;"></i>&nbsp; 修改合同年份
                           </a>
	                   </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/qrcodeBatchInfo/showBatchForActivityUsedList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
                        <input type="hidden" name="tabsFlag" value="${tabsFlag}"/>
                        <input type="hidden" name="infoKey" value="${infoKey}"/>
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
			                        <label class="control-label">导入批号：</label>
			                        <input name="batchNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
		                        <div class="search-item">
			                        <label class="control-label">批码编号：</label>
			                        <input name="batchDesc" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
		                        </div>
		                        <div class="search-item">
			                        <label class="control-label">批码名称：</label>
			                        <input name="batchName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
		                        </div>
                                <div class="search-item">
                                    <label class="control-label">批码有效期：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">激活时间：</label>
                                    <input name="activateStartDate" id="activateStartDate" class="form-control input-width-medium Wdate search-date" 
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'activateEndDate\')}'})" />
                                    <label>-</label>
                                    <input name="activateEndDate" id="activateEndDate" class="form-control input-width-medium Wdate sufTime search-date" 
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'activateStartDate\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">批次状态：</label>
                                    <select name="batchStatus" class="form-control input-width-larger search" autocomplete="off" >
                                        <option value="">全部</option>
                                        <option value="0">未开始</option>
                                        <option value="1">已开始</option>
                                        <option value="2">已结束</option>
                                        <option value="3">未激活</option>
                                        <option value="4">已预警</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动配置码源列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="3,5,11,11,9,13,8,10,13,9,9,8" />
                            <thead>
                                <tr>
                                    <th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:8%;" data-ordercol="o.order_no">码源订单号</th>
                                    <th style="width:10%;" data-ordercol="b.client_order_no">客户订单号</th>
                                    <th style="width:6%;" data-ordercol="b.batch_no">导入批号</th>
                                    <th style="width:14%;" data-ordercol="b.batch_desc">批码编号</th>
                                    <th style="width:8%;" data-ordercol="(b.qrcode_amounts * b.pack_amounts)">码数量</th>
                                    <th style="width:8%;" data-ordercol="b.start_date">批码有效期</th>
                                    <th style="width:10%;" data-ordercol="b.activate_phone">激活人</th>
                                    <th style="width:10%;" data-ordercol="b.activate_time" data-orderdef>激活时间</th>
                                    <th style="width:10%;" data-ordercol="b.contract_Year">所属合同年份</th>
                                    <th style="width:6%;">操作</th>
                                </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
                                   <td style="text-align:center;">
                                      <input name="itemCB" type="checkbox" value="${item.batchKey}" />
                                   </td>
					        		<td style="text-align:center;
                                        <c:choose>
                                            <c:when test="${item.isBegin eq '0' or item.isBegin eq '-1'}">background-color: #ecb010;</c:when>
                                            <c:when test="${item.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${item.isBegin == '2'}">background-color: #999999;</c:when>
                                        </c:choose>">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td>
					        			<span>${item.orderNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.clientOrderNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.batchNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.batchDesc}(${item.batchName})</span>
					        		</td>
					        		<td style="text-align:right;">
					        			<span>${item.qrcodeAmounts}x${item.packAmounts}</span>
					        		</td>
					        		<td style="text-align:center; position: relative;">
										<c:choose>
											<c:when test="${empty(item.startDate)}">
                                                <span>未激活</span>
                                                <c:if test="${not empty(item.endDate)}">
	                                                <br><span>到${item.endDate}</span>
                                                </c:if>
											</c:when>
											<c:otherwise>
												<span>从${item.startDate}</span><br>
												<span>到${item.endDate}</span>
											</c:otherwise>
										</c:choose>
	                                    <c:if test="${activityValidFlag and item.expireWarning}">
	                                        <i class="iconfont icon-riqishijian" style="color: red; font-weight:bold; position: absolute; right: 0px; top:32%;" title="规则将要到期" ></i>
	                                    </c:if>
					        		</td>
					        		<td style="text-align:center;">
					        		    <c:if test="${not empty item.activatePhone}">
						        			<span>${item.activateUserName}(${item.activatePhone})</span>
					        		    </c:if>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(item.activateTime, 0, 19)}</span>
					        		</td>
                                    <td style="text-align:center;">
                                        <span id="contractYear">${item.contractYear}</span>
                                        <c:if test="${not empty item.contractChangeDate}">
                                            </br><span id="contractChangeDate">${item.contractChangeDate}</span> : <span id="nextContractYear">${item.nextContractYear}</span>
                                        </c:if>
                                    </td>
					        		<td data-key="${item.batchKey}" style="text-align: center;">
						        		<c:if test="${currentUser.roleKey ne '4'}">
						        		 <a class="btn btn-xs edit btn-brownness btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;批次修改</a>
						        		</c:if>
                                        <a class="btn btn-xs activateDetail btn-brownness btn-blue"><i class="iconfont icon-xinxi"></i> 激活详情</a>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="12"><span>查无数据！</span></td>
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
    <div class="modal fade" id="updateBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">批次修改时间</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table" style="line-height: 30px;">
                        <tr>
                            <td width="25%" class="text-right"><label class="title" style="margin-bottom: 0px;">客户订单号：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="batchKey" id="batchKey" type="hidden"/>
                                    <span id="clientOrderNo"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right"><label class="title" style="margin-bottom: 0px;">批次名称：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="batchName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right"><label class="title" style="margin-bottom: 0px;">所属活动：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="activityName"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right"><label class="title" style="margin-bottom: 0px;">活动日期：<span class="required">&nbsp;</span></label></td>
                            <td>
                                <div class="content">
                                    <span id="activityStartDate"></span>&nbsp;至&nbsp;<span id="activityEndDate"></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="text-right"><label class="title" style="margin-bottom: 0px;">批码有效期：<span class="required">*</span></label></td>
                            <td>
                                <div class="content" style="display: flex;">
                                    <input name="startDate" id="startDate" class="form-control input-width-normal Wdate required preTime" style="width: 105px !important;"
                                        tag="validate" autocomplete="off" value="${batchInfo.startDate }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                    <span class="blocker en-larger">至</span>
                                    <input name="endDate" id="endDate" class="form-control input-width-normal Wdate required sufTime" style="width: 105px !important;"
                                        tag="validate" autocomplete="off" value="${batchInfo.endDate }" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                    <input type="hidden" id="validDays" />
                                    <span id="diffDays" style="color: red;">0</span><span>天</span>
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
    <jsp:include page="showBatchContractYearTemplet.jsp"></jsp:include>
