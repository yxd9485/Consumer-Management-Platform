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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

	<script>
		$(function(){
            // 初始化校验控件
            $.runtimeValidate($("#addBatchDialog"));

            // 弹出修改批次窗口
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/companyRecharge/findConfirmInfo.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {preKey:key,type:"2"},//2 代表 确认
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#myModalLabelName").html("企业充值确认");
                        $("#addBatchDialog").modal("show");
                        var rechargeInfo = data['rechargeInfo'];
                        for(var name in rechargeInfo) {
                            $("#addBatchDialog input[name='" + name + "']").val(rechargeInfo[name]);
                        }
                    }
                });
            });
            //   驳回操作 确认操作
            $("#addBatchDialog").delegate("#addBtn", "click", function(){

                // 提交表单
                var url = "<%=cpath%>/companyRecharge/confirmUpdate.do";
                var preKey = $("input[name='preKey']").val()
                var type = $("input[name='type']").val()
				var status;
                  if(type =="1"){
                      status=3;
                  }else{
                      status=2;
                  }
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {preKey:preKey,status:status,type:type},
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#addBatchDialog #closeBtn").trigger("click");
                        $.fn.alert(data['errMsg'], function(){
                            $("button.btn-primary").trigger("click");
                        });
                    }
                });
         
            });
            // 弹出驳回批次窗口
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/companyRecharge/findConfirmInfo.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {preKey:key,type:"1"},  // 1 代表 驳回
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $("#myModalLabelName").html("企业充值驳回");
                        $("#addBatchDialog").modal("show");
                        var rechargeInfo = data['rechargeInfo'];
                        for(var name in rechargeInfo) {
                            $("#addBatchDialog input[name='" + name + "']").val(rechargeInfo[name]);
                        }
                    }
                });
            });

            // 新增
			$("#toAdd").click(function(){
				var url = "<%=cpath%>/companyRecharge/showCompanyConfirmList.do";
				$("form").attr("action", url);
				$("form").submit();
			});

            
        });

        function imgBig(_url){
            if(!_url){
                return;
            }
            var img2 = document.querySelector('#large');
            img2.src=_url;
            img2.style.display = 'block';
        }
        function imgSmall(){
            var img2 = document.querySelector('#large');
            img2.style.display = 'none';
        }
	</script>
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
		}
		.active_board_table .ab_main {
			width: 65% !important;
			margin: 6px;
			padding: 6px;
		}
	</style>
  </head>

  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a>首页</a></li>
			<li class="current"><a>企业充值管理</a></li>
			<li class="current"><a>市场确认</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">市场确认查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                     <%--  <a id="toAdd" class="btn btn-red">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;新充值
	                       </a>--%>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath %>/companyRecharge/showCompanyConfirmList.do">
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
		                    <div class="form-group little_distance search">
                                <div class="search-item">
									<label class="control-label">按关键词查询：</label>
									<input name="keyword" class="form-control input-width-larger keyword" autocomplete="off" maxlength="100"/>
                                </div>
                                <div class="search-item">
									<label class="control-label">交易方式：</label>
									<select name="transType" class="form-control input-width-larger transType search" >
										<option value="">全部</option>
										<option value="1">网银转账</option>
										<option value="2">第三方支付</option>
										<option value="3">支票</option>
										<option value="4">欠款待付</option>
									</select>
                                </div>
                                <div class="search-item">
									<label class="control-label">充值状态：</label>
									<select name="transStatus" class="form-control input-width-larger transStatus search" >
										<option value="">全部</option>
										<option value="1">待确认</option>
										<option value="2">已确认</option>
										<!--                                             <option value="3">已终止</option> -->
									</select>
                                </div>
		                    </div>
			            </div>
		            </div>
		            <div class="row">
		               <div class="col-md-12 text-center mart20">
		                   <button type="button" class="btn btn-primary btn-blue">搜 索</button>
		                   <button type="button" class="btn btn-reset btn-radius3 marl20">重 置</button>
		               </div>
		            </div>
	                </form>
	            </div>
            </div>
            <div class="widget box" id="tab_1_1">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">市场确认列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                          <%--  <input type="hidden" id="screenWidth1366" value="5,10,10,7,8,8,6,6,10,8,8,8,8" />--%>
                            <thead>
							<tr>
								<th style="width:5%; text-align: center;">序号</th>
								<th style="width:10%; text-align: center;" data-ordercol="p.CONTRACT_NUM">合同编号</th>
								<th style="width:20%; text-align: center;" data-ordercol="p.CONTRACT_NAME">合同名称</th>
								<th style="width:12%; text-align: center;" data-ordercol="p.RECHARGE_TIME" data-orderdef>充值时间</th>
								<th style="width:12%; text-align: center;" data-ordercol="p.RECHARGE_MONEY">充值金额(元)</th>
								<th style="width:11%; text-align: center;" data-ordercol="p.RECHARGE_VPOINTS">充值积分(个)</th>
								<th style="width:8%; text-align: center;" data-ordercol="p.TRANS_TYPE">交易方式</th>
								<th style="width:10%; text-align: center;" data-ordercol="p.TRANS_STATUS">充值状态</th>
								<%-- <c:if test="${transStatus != 2}">--%>
									<th style="width:14%; text-align: center;">操作</th>
							<%--	</c:if>--%>
							</tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(rechargeList) gt 0}">
					        	<c:forEach items="${rechargeList}" var="recharge" varStatus="idx">
					        	<tr>
									<td style="text-align: center;"><span>${idx.count}</span></td>
									<td style="text-align: center;"><span>${recharge.contractNum}</span></td>
									<td style="text-align: center;"><span>${recharge.contractName}</span></td>
									<td style="text-align: center;"><span>${fn:substring(recharge.rechargeTime, 0, 19)}</span></td>
									<td style="text-align: center;">
										<span data-ref="remoney" data-value="${recharge.rechargeMoneyChar}">${recharge.rechargeMoneyChar}</span>
									</td>
									<td style="text-align: center;">
										<span data-ref="recharge" data-value="${recharge.rechargeVpoints}">${recharge.rechargeVpoints}</span>
									</td>
									<td style="text-align: center;">
                                        	<span>
                                        		<c:if test="${recharge.transType == 1}">网银转账</c:if>
                                        		<c:if test="${recharge.transType == 2}">第三方支付</c:if>
                                        		<c:if test="${recharge.transType == 3}">支票</c:if>
                                        		<c:if test="${recharge.transType == 4}">欠款待付</c:if>
                                        	</span>
									</td>
									<td style="text-align: center;">
                                        	<span>
                                        		<c:if test="${recharge.transStatus == 1}">待确认</c:if>
                                        		<c:if test="${recharge.transStatus == 2}">已确认</c:if>
                                        		<c:if test="${recharge.transStatus == 3}">驳回</c:if>
                                        	</span>
									</td>
									<%--<c:if test="${recharge.transStatus == 1}">--%>
                                  <%--  <c:if test="${transStatus != 2}">--%>
										<td data-key="${recharge.preKey}" style="text-align:center;">
	                                        	<span>
														<c:if test="${recharge.transStatus == 1}">	<a class="btn btn-xs edit terminal btn-danger btn-red"><%--<i class="iconfont icon-xiugai"></i>--%>&nbsp;确 认</a></c:if>
                                        	        	<c:if test="${recharge.transStatus == 2  or recharge.transStatus == 3}">— — </c:if>
                                        	        	<c:if test="${recharge.transStatus == 1}">	<a 
                                                                class="btn btn-xs del terminal btn-danger " style=" color: #fff;
    background-color: #39b3d7;
    border-color: #269abc" 
                                                        ><%--<i class="icon-edit"></i>--%>&nbsp;驳 回</a></c:if>

	                                        	</span>
										</td>
                                <%--    </c:if>--%>
								<%--	</c:if>--%>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">9</c:when><c:otherwise>8</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
		<div	class="modal fade" id="addBatchDialog" tabindex="-1" role="dialog" data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
		<div class="modal-dialog">
			<div class="modal-content" style="top:20%;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabelName">企业充值</h4>
				</div>
				<div class="modal-body">
					<table class="active_board_table">
						<input type="hidden" name="preKey" id="preKey" value="${rechargeInfo.preKey}"></input>
						<input type="hidden" name="type" id="type" value="${rechargeInfo.type}"></input>
						<tr>
							<td class="ab_left"><label class="title">合同编号：<span class="required">*</span></label></td>
							<td class="ab_main">
								<div class="content">
									<input  readonly="readonly" name="contractNum" class="form-control required contractNum" tag="validate" maxlength="20" value="${rechargeInfo.contractNum}" autocomplete="off" />
									<label class="validate_tips valid-num"></label>
								</div>
							</td>
						</tr>
						<tr>
							<td class="ab_left"><label class="title">合同名称：<span class="required">*</span></label></td>
							<td class="ab_main">
								<div class="content">
									<input readonly="readonly" name="contractName" class="form-control required contractName" tag="validate" maxlength="50" value="${rechargeInfo.contractName}"  autocomplete="off" />
									<label class="validate_tips"></label>
								</div>
							</td>
						</tr>
						<tr>
							<td class="ab_left"><label class="title">付款方式：</label><span class="red">*</span></td>
							<td class="ab_main">
								<div class="content">
									<select readonly="readonly" class="form-control required" name="transType">

										<option value="1"<c:if test="${rechargeInfo.transType eq '0'}">selected</c:if> >网银转账</option>
										<option value="2" <c:if test="${rechargeInfo.transType eq '0'}">selected</c:if>>第三方支付工具</option>
										<option value="3" <c:if test="${rechargeInfo.transType eq '0'}">selected</c:if>>支票</option>
										<option value="4" <c:if test="${rechargeInfo.transType eq '0'}">selected</c:if>>欠款代付</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td class="ab_left"><label class="title">充值金额：<span class="required">*</span></label></td>
							<td class="ab_main">
								<div class="content">
									<input readonly="readonly" name="rechargeMoney" maxLength="10" class="payMoney form-control required money minValue maxValue"
										   minVal="-9999999.99" maxVal="9999999.99" tag="validate" autocomplete="off" autocomplete="off"  value="${rechargeInfo.rechargeMoney}"/>
									<b><strong>(1元 = 100积分)</strong> 元</b>
									<label class="validate_tips"></label><label class="top-tips"></label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td class="ab_left"><label class="title">充值积分：</label><span class="white">*</span></td>
							<td class="ab_main">
								<div class="content">
									<input readonly="readonly" class="form-control showPoints" name="rechargeVpoints" readonly="readonly" value="${rechargeInfo.rechargeVpoints}" /> <b>V分</b>
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
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">提示消息</h4>
				</div>
				<div class="modal-body">
					<h6 class="success">充值申请已提交</h6>
					<h6 class="delSuccess">充值申请已删除</h6>
					<h6 class="fail">提交失败</h6>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">确 定</button>
				</div>
			</div>
	  	</div>
	</div>
	</div>
  </body>
</html>
