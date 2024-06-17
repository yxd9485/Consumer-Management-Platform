<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
%>

<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

	<script>
		$(function(){
            // 初始化校验控件
            $.runtimeValidate($("#addBatchDialog"));
			
			// 弹出创建批次窗口
			$("#addItem").on("click", function(){
				$(".activate").hide();
				$(".active_board_table input").removeAttr("disabled");
				// 重置输入框
				$("#addBatchDialog input[name]").val("");
                
                $("#addBatchDialog").modal("show");
			});
            
            // 弹出修改批次窗口
            $("a.edit").off();
            $("a.edit").on("click", function(){
				$(".activate").hide();
				$(".active_board_table input").removeAttr("disabled");
                // 初始化批次数据
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/giftCardQrcodeOrderAction/findGiftCardQrcodeOrder.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {batchKey : key},
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success: function(data) {
                        $("#addBatchDialog").modal("show");
                        var batchInfo = data['orderInfo'];
                        for(var name in batchInfo) {
                            $("#addBatchDialog input[name='" + name + "']").val(batchInfo[name]);
                        }
                    }
                });
            });
            // 弹出激活批次窗口
            $("a.activateQrcode").off();
            $("a.activateQrcode").on("click", function(){
				$(".activate").show();
				$(".active_board_table input").attr("disabled", "disabled");
				$(".activate input").removeAttr("disabled");
                // 初始化批次数据
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/giftCardQrcodeOrderAction/findGiftCardQrcodeOrder.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {batchKey : key},
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success: function(data) {
                        $("#addBatchDialog").modal("show");
                        var batchInfo = data['orderInfo'];
                        for(var name in batchInfo) {
                            $("#addBatchDialog input[name='" + name + "']").val(batchInfo[name]);
                        }
                    }
                });
            });

            // 确定添加批次
            $("#addBatchDialog").delegate("#addBtn", "click", function(){
            	// 输入元素校验
            	var validateResult = $.submitValidate($("#addBatchDialog"));
                if(!validateResult){
                    return false;
                }
                // 提交表单
                var url = "<%=cpath%>/giftCardQrcodeOrderAction/doGiftCardQrcodeOrderAdd.do";
                if ($("#addBatchDialog input[name='qrcodeOrderKey']").val() != "") {
                	url = "<%=cpath%>/giftCardQrcodeOrderAction/doGiftCardQrcodeOrderEdit.do";
                } 
                var paramJson = {};
                $("#addBatchDialog input[name]").each(function(){
                	paramJson[$(this).attr("name")] = $(this).val();
                });
                paramJson['giftCardInfoKey']=$("#giftCardInfoKey").val()
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success: function(data) {
                        $("#addBatchDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    },
                    error:function(data){
                        alert(data.responseText());
                    }
                });
            });
            
            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/giftCardQrcodeOrderAction/doGiftCardQrcodeOrderDelete.do?orderKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });

            
            // 生成码
            $("a.createQrcode").click(function(){
                var $td = $(this).parent("td");
                var key = $td.data("key");
                var url = "<%=cpath%>/giftCardQrcodeOrderAction/createCode.do?orderKey=" + key;
                 $.fn.confirm("确定要生成兑付卡码源订单吗？", function(){
                    $td.html("进行中...");
                    $("form").attr("action", url);
                    $("form").submit(); 
                 });
            //   $(this).parent("td").html("进行中...");
            });
        });

        function downloadQrcode(createDate, qrcodeOrderKey, orderName, qrcodeNum) {
            var url = "${basePath}/giftCardQrcodeOrderAction/downloadGiftCardCode.do?createDate=" + createDate + "&qrcodeOrderKey="
                + qrcodeOrderKey + "&orderName=" + orderName + "&qrcodeNum=" + qrcodeNum
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
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a> 首页</a></li>
        	<li class="current"><a> 礼品卡管理</a></li>
        	<li class="current"><a> 兑付卡</a></li>
            <li class="current"><a title=""> 兑付卡码源</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;"> 兑付卡码源订单</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.userKey ne '-1'}">
	                       <a id="addItem" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 创建订单
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/giftCardQrcodeOrderAction/showGiftCardQrcodeOrderList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
	                    <input type="hidden" name="giftCardInfoKey" id="giftCardInfoKey" value="${giftCardInfoKey}" />
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search">
                                <div class="search-item">
			                        <label class="control-label">订单名称：</label>
			                        <input name="orderName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">兑付卡码源订单列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                                <tr>
                                    <th style="width:5%;">序号</th>
                                    <th style="width:15%;" data-ordercol="qrcode_order_key">订单编号</th>
                                    <th style="width:15%;" data-ordercol="order_name">订单名称</th>
                                    <th style="width:10%;" data-ordercol="cog.gift_card_name">兑付卡名称</th>
                                    <th style="width:5%;" data-ordercol="qrcode_num">码数量</th>
                                    <th style="width:15%;" data-ordercol="qrcode_num">制卡人</th>
                                    <th style="width:10%;" >订单状态</th>
                                    <th style="width:10%;" >激活状态</th>
                                    <th style="width:15%;" data-ordercol="info.create_time" data-orderdef>创建时间</th>
                                    <th style="width:20%;">操作</th>
                                </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td>
					        			<span>${item.qrcodeOrderKey}</span>
					        		</td>
					        		<td>
					        			<span>${item.orderName}</span>
					        		</td>
					        		<td>
					        			<span>${item.giftCardName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.qrcodeNum}</span>
					        		</td>
					        		<td style="text-align:center;">
										<span>${item.cardMaker}</span><br>
										<span>${item.cardMakerPhone}</span>
					        		</td>
					        		<td style="text-align:center;">
					        		     <c:choose>
					        		         <c:when test="${item.orderStatus eq '0' }">未生成</c:when>
					        		         <c:when test="${item.orderStatus eq '1' }">生成成功</c:when>
					        		         <c:when test="${item.orderStatus eq '2' }">生成失败</c:when>
					        		         <c:when test="${item.orderStatus eq '3' }">生成中...</c:when>
					        		         <c:otherwise>---</c:otherwise>
					        		     </c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        		     <c:choose>
					        		         <c:when test="${item.activateStatus eq '1' }">已激活</c:when>
					        		         <c:otherwise>未激活</c:otherwise>
					        		     </c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.createTime}</span>
					        		</td>
					        		<c:if test="${currentUser.userKey ne '-1'}">
					        		<td data-key="${item.qrcodeOrderKey}" style="text-align: center;">
					        			<a class="btn btn-xs edit btn-orange">&nbsp;修 改</a>
					        		    <c:if test="${item.orderStatus eq '0' or item.orderStatus eq '2' }">
					        			    <a class="btn btn-xs del btn-orange">&nbsp;删 除</a>
					        		    </c:if>
                                        <c:if test="${item.orderStatus eq '0' }">
		        			                <a class="btn btn-xs createQrcode btn-blue">&nbsp;生成码源</a>
                                        </c:if>
                                        <c:if test="${item.orderStatus eq '1' and  item.activateStatus != '1'}">
		        			                <a class="btn btn-xs activateQrcode btn-blue">&nbsp;激活码源</a>
                                        </c:if>
                                        <c:if test="${item.orderStatus eq '1' }">
		        			                <a class="btn btn-xs btn-blue" onclick="downloadQrcode('${item.createDate}', '${item.qrcodeOrderKey}', '${item.orderName}', '${item.qrcodeNum}')">&nbsp;下载码源</a>
                                        </c:if>
					        		</td>
					        		</c:if>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${currentUser.userKey ne '-1'}">6</c:when><c:otherwise>5</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
    <div class="modal fade" id="addBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
        <div class="modal-dialog">
            <div class="modal-content" style="top:20%;">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">创建兑付卡码源订单</h4>
                </div>
                <div class="modal-body">
	                <table class="active_board_table">
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">订单名称：<span class="required">*</span></label></td>
		                    <td>
		                        <div class="content">
		                            <input name="qrcodeOrderKey" id="" type="hidden"/>
		                            <input name="orderName" tag="validate"
		                                class="form-control input-width-larger required" autocomplete="off" maxlength="30" />
		                            <label class="validate_tips" style="width:35%"></label>
		                        </div>
		                    </td>
		                </tr>
		                <tr>
		                    <td width="25%" class="text-right"><label class="title">码数量：<span class="required">*</span></label></td>
		                    <td>
		                        <div class="content">
                                    <input name="qrcodeNum" tag="validate"
                                        class="form-control input-width-larger required number positive maxValue" autocomplete="off" maxVal="100000" maxlength="6" />
		                            <label class="validate_tips" style="width:35%"></label>
		                        </div>
		                    </td>
		                </tr>

						<tr>
							<td width="25%" class="text-right"><label class="title">制卡人：<span class="required">*</span></label></td>
							<td>
								<div class="content">
									<input name="cardMaker"  tag="validate"
										   class="form-control input-width-larger  required" autocomplete="off" maxlength="10" />
									<label class="validate_tips" style="width:35%"></label>
								</div>
							</td>
						</tr>
						<tr>
							<td width="25%" class="text-right"><label class="title">制卡人联系方式：<span class="required">*</span></label></td>
							<td>
								<div class="content">
									<input name="cardMakerPhone"  tag="validate"
										   class="form-control input-width-larger phone required"  autocomplete="off"
										   maxlength="11" />
									<label class="validate_tips" style="width:35%"></label>
								</div>
							</td>
						</tr>
						<tr>
							<td width="25%" class="text-right"><label class="title">备注：</label></td>
							<td>
								<div class="content">
									<input name="remarks"
										   class="form-control input-width-larger " autocomplete="off" maxlength="30" />
									<label class="validate_tips" style="width:35%"></label>
								</div>
							</td>
						</tr>
						<tr class="activate">
							<td width="25%" class="text-right"><label class="title">有效期：<span class="required">*</span></label></td>
							<td class="content" >
								<div class="content">
									<input name="startDate" id="startDate" class="form-control input-width-medium Wdate required" tag="validate"
										   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd ', minDate:'%y-%M-%d'})" autocomplete="off"/>
									<span class="blocker en-larger" style="float: left">至</span>
									<input name="endDate" id="endDate" class="form-control input-width-medium Wdate required" tag="validate"
										   onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" autocomplete="off"/>
								</div>
							</td>
						</tr>
						<tr class="activate">
							<td width="25%" class="text-right"><label class="title">激活人姓名：<span class="required">*</span></label></td>
							<td>
								<div class="content">
									<input name="activateUser" tag="validate"
										   class="form-control input-width-larger required" autocomplete="off" maxlength="10" />
									<label class="validate_tips" style="width:35%"></label>
								</div>
							</td>
						</tr>
						<tr class="activate">
							<td width="25%" class="text-right"><label class="title">激活人联系方式：<span class="required">*</span></label></td>
							<td>
								<div class="content">
									<input name="activatePhone" tag="validate"
										   class="form-control phone input-width-larger required"
										   autocomplete="off"  maxlength="11" />
									<label class="validate_tips" style="width:35%"></label>
								</div>
							</td>
						</tr>
	               </table>
                </div>
                <div class="modal-footer">
                    <button type="button" id="addBtn" class="btn btn-default do-add btn-blue" data-dismiss="">确 认</button>
                    <button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
                </div>
            </div>
        </div>
    </div>
	</div>
  </body>
</html>
