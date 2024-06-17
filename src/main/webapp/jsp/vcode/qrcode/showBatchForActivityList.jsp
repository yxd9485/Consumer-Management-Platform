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
            var refresh = "${refresh}";
            if(refresh != "") {
                $("div.modal-body ."+refresh+"").show();
                $("div.modal-body :not(."+refresh+")").hide();
                $("#myModal").modal("show");
            }

            // 初始化校验控件
            $.runtimeValidate($("#adjustBatchDialog"));

            // 批码有效期切换
            $("input[name='validDayRadio']").on("change", function(){
                if ($(this).val() == '1') {
                    $("input[name='endDate']").addClass("required").removeAttr("disabled");
                } else {
                    $("input[name='endDate']").val("").removeClass("required").attr("disabled", "disabled");
                    $("input[name='endDate']").closest("td").find(".validate_tips").removeClass("valid_fail_text");
                }
            });

            // 弹出批次调整窗口
            $("a.adjustbatchitem").on("click", function(){
                $("#adjustBatchDialog :input[name]:not(:radio)").val("");
                $("#adjustBatchDialog .modal-title").text("批次调整");
                $("#adjustBatchDialog tr:lt(2)").css("display", "");
                $("#adjustBatchDialog input[name='batchKey']").val($(this).parents("td").data("key"));
                $("#adjustBatchDialog #clientOrderNo").text($(this).closest("tr").find("#clientOrderNo").text());
                $("#adjustBatchDialog input[name='batchName']").val($(this).closest("tr").find("#batchName").text());
                $("#adjustBatchDialog input[name='validDayRadio']").eq(0).prop("checked", "true");
                $("#adjustBatchDialog").modal("show");
                
                // 默认当前活动选中
                $("#adjustBatchDialog [name='vcodeActivityKey']").val("${infoKey}");
            });

            //弹出批次批量调整窗口
            $("#adjustBatch").off();
            $("#adjustBatch").on("click",function(){
                var key = "";
                var batchName = "";
                $("[name='itemCB']:checked").each(function() {
                    key = $(this).val() + "," + key;
                    batchName = $(this).closest("tr").find("#batchName").text() + "</br>" + batchName;
                })
                if (key == "") {
                    $.fn.alert("请选择要调整的批次!");
                } else {
                    $("#adjustBatchDialog :input[name]:not(:radio)").val("");
                    $("#adjustBatchDialog .modal-title").text("批量批次调整");
                    $("#adjustBatchDialog tr:lt(2)").css("display", "none");
                    $("#adjustBatchDialog input[name='batchKey']").val(key);
                    $("#adjustBatchDialog input[name='batchName']").val(batchName);
                    $("#adjustBatchDialog input[name='validDayRadio']").eq(0).prop("checked", "true");
                    $("#adjustBatchDialog").modal("show");
                    
                    // 默认当前活动选中
                    $("#adjustBatchDialog [name='vcodeActivityKey']").val("${infoKey}");
                }
            });
            
            // 确定调整批次
            $("#adjustBatchDialog").delegate("#addBtn", "click", function(){
                
                // 输入元素校验
                var validateResult = $.submitValidate($("#adjustBatchDialog"));
                if(!validateResult){
                    return false;
                }

                // 判断活动是否可配置
                var vcodeActivityKey = $('select[name="vcodeActivityKey"]').val();
                var batchKey = $('input[name="batchKey"]').val();
                var flag = true;
                $.ajax({
                    url: "${basePath}/qrcodeBatchInfo/checkActivityContainSku.do",
                    data: {
                        "vcodeActivityKey": vcodeActivityKey,
                        "batchKey": batchKey
                    },
                    type: "POST",
                    dataType: "json",
                    async: false,
                    beforeSend: appendVjfSessionId,
                    success: function (data) {
                        if (data == "1") {
                            flag = false;
                        } else {
                            $.fn.alert("当前批次所属SKU不属于该活动下!");
                        }
                    },
                    error: function () {
                        $.fn.alert("检查SKU失败！");
                    }
                });

                if (flag) {
                    return false;
                }


                var tips = "您确定调整以下批次吗? </br>" + $("#adjustBatchDialog input[name='batchName']").val();
                $.fn.confirm(tips, function(){
                    // 提交表单
                    var url = "<%=cpath%>/qrcodeBatchInfo/doBatchAdjustAjax.do";
                    var paramJson = {};
                    $("#adjustBatchDialog :input[name]").each(function(){
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
                            $("#adjustBatchDialog #closeBtn").trigger("click");
                            $.fn.alert(data['errMsg'], function(){
                                $("button.btn-primary").trigger("click");
                            });
                        }
                    });
                });
            });
            
            // 批次调整时活动选择
            $("#adjustBatchDialog [name='vcodeActivityKey']").on("change", function(){
                if ($(this).val()) {
                    $(this).attr("title", $(this).find("option:selected").text().replace(/[ \r\n]/g, ""));
                } else {
                    $(this).removeAttr("title");
                }
            });

            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']").prop("checked","checked");
                } else {
                    $("[name='itemCB']").prop("checked","");
                }
            });
        });
	</script>
  
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">码源批次查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="adjustBatch" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 批量调整批次
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
	                    action="<%=cpath%>/qrcodeBatchInfo/showBatchForActivityList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
                        <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                        <input type="hidden" name="infoKey" value="${infoKey }"/>
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
			                        <label class="control-label">码源订单号：</label>
			                        <input name="orderNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">客户订单号：</label>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">码源回传列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" id="dataTable_data">
                            <thead>
                                <tr>
                                    <th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:10%;" data-ordercol="o.order_no">码源订单号</th>
                                    <th style="width:10%;" data-ordercol="b.client_order_no">客户订单号</th>
                                    <th style="width:8%;" data-ordercol="b.batch_no">导入批号</th>
                                    <th style="width:12%;" data-ordercol="b.batch_desc">批码编号</th>
                                    <th style="width:15%;" data-ordercol="b.batch_name">批码名称</th>
                                    <th style="width:9%;" data-ordercol="(b.qrcode_amounts * b.pack_amounts)">码数量</th>
                                    <th style="width:10%;" data-ordercol="b.create_time" data-orderdef>回传时间</th>
                                    <th style="width:10%;" data-ordercol="b.contract_Year">所属合同年份</th>
                                    <c:if test="${currentUser.roleKey ne '4'}">
                                    <th style="width:8%;">操作</th>
                                    </c:if>
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
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td>
					        			<span>${item.orderNo}</span>
					        		</td>
					        		<td>
					        			<span id="clientOrderNo">${item.clientOrderNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.batchNo}</span>
					        		</td>
					        		<td>
					        			<span id="batchDesc">${item.batchDesc}</span>
					        		</td>
					        		<td>
					        			<span id="batchName">${item.batchName}</span>
					        		</td>
					        		<td style="text-align:right;">
					        			<span>${item.qrcodeAmounts}x${item.packAmounts}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(item.importTime, 0, 19)}</span>
					        		</td>
                                    <td style="text-align:center;">
                                        <span id="contractYear">${item.contractYear}</span>
                                        <c:if test="${not empty item.contractChangeDate}">
                                            </br><span id="contractChangeDate">${item.contractChangeDate}</span> : <span id="nextContractYear">${item.nextContractYear}</span>
                                        </c:if>
                                    </td>
					        		<c:if test="${currentUser.roleKey ne '4'}">
					        		<td data-key="${item.batchKey}" style="text-align: center;">
					        			<a class="btn btn-xs adjustbatchitem btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;调整批次</a>
					        		</td>
					        		</c:if>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">11</c:when><c:otherwise>10</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
    <div class="modal fade" id="adjustBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">批量批次调整</h4>
                </div>
                <div class="modal-body">
                    <table class="active_board_table" style="line-height: 30px;">
                        <tr>
                            <td width="25%" class="text-right"><label class="title">客户订单编号：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                  <input type="hidden" name="batchKey">
                                  <label id="clientOrderNo"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">批次名称：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <input name="batchName" tag="validate"
                                        class="form-control required input-width-larger" autocomplete="off" maxlength="30" />
                                    <label class="validate_tips" style="width:35%"></label>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">批码有效期：<span class="required">*</span></label></td>
                            <td>
                                <div class="content" style="display: flex; line-height: 30px;">
                                    <div style="width: 100%;">
                                         <div style="display: flex;">
                                             <input type="radio" class="tab-radio" name="validDayRadio" value="0" checked="checked" style="margin-top:8px; cursor: pointer;" />
                                             <span>激活时间+SKU有效期</span><br>
                                         </div>
                                         <div style="display: flex;">
                                             <input type="radio" class="tab-radio" name="validDayRadio" value="1" style="margin-top:8px; cursor: pointer;" />
                                             <span>指定截止日期&nbsp;</span>
                                             <input name="endDate" id="endDate" class="Wdate form-control input-width-medium positive" disabled="disabled"
                                                onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{\'%y-%M-%d\'}'})" autocomplete="off" tag="validate"/>
                                             <label class="validate_tips" style="width:35%"></label>
                                         </div>
                                     </div>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="25%" class="text-right"><label class="title">调整到新活动：<span class="required">*</span></label></td>
                            <td>
                                <div class="content">
                                    <select class="form-control input-width-larger required" name="vcodeActivityKey" tag="validate">
                                         <option value="">请选择</option>
                                         <c:if test="${not empty activityCogList}">
                                         <c:forEach items="${activityCogList}" var="activityItem">
	                                         <option value="${activityItem.vcodeActivityKey}">
	                                            ${activityItem.vcodeActivityName}
	                                            <c:choose>
	                                                <c:when test="${activityItem.isBegin eq '0'}">(待上线)</c:when>
	                                                <c:when test="${activityItem.isBegin eq '1'}">(已上线)</c:when>
	                                                <c:when test="${activityItem.isBegin eq '2'}">(已下线)</c:when>
	                                            </c:choose>
	                                         </option>
                                         </c:forEach>
                                         </c:if>
                                    </select>
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
