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
	<script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

	<script>
		$(function(){

// 新增
			$("#addItem").click(function(){
				var url = "<%=cpath%>/vcodeActivityBigPrize/showBigPrizeAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivityBigPrize/showBigPrizeEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});

// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var currentRow=$(this).closest("tr");
				var prizeType = currentRow.find("td:eq(1)").text();
				var url = "<%=cpath%>/vcodeActivityBigPrize/doBigPrizeDel.do?infoKey="+key+'&prizeType='+prizeType;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});
			
			// 授权门店
			$("a.add").off();
			$("a.add").on("click", function(){
				var currentRow=$(this).closest("tr"); 
                var prizeType = currentRow.find("td:eq(1)").text();
				var url = "<%=cpath%>/terminal/showTerminalPrizeAdd.do?prizeType="+prizeType;
				$("form").attr("action", url);
				$("form").submit();
			});
		});
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
			<li class="current"><a> 活动管理</a></li>
			<li class="current"><a> 实物配置</a></li>
		</ul>
	</div>
	<div class="row">
		<div class="col-md-12 tabbable tabbable-custom">
			<div class="widget box">
				<div class="row">
					<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">实物查询</span></div>
					<div class="col-md-10 text-right">
						<c:if test="${currentUser.roleKey ne '4'}">
							<a id="addItem" class="btn btn-blue">
								<i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增实物
							</a>
						</c:if>
					</div>
				</div>
				<div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
				<div class="widget-content form-inline">
					<form class="listForm" method="post"
						  action="<%=cpath%>/vcodeActivityBigPrize/showBigPrizeList.do">
						<input type="hidden" class="tableTotalCount" value="${showCount}" />
						<input type="hidden" class="tableStartIndex" value="${startIndex}" />
						<input type="hidden" class="tablePerPage" value="${countPerPage}" />
						<input type="hidden" class="tableCurPage" value="${currentPage}" />
						<input type="hidden" class="tableOrderCol" value="${orderCol}" />
						<input type="hidden" class="tableOrderType" value="${orderType}" />
						<input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
						<input type="hidden" name="queryParam" value="${queryParam}" />
						<input type="hidden" name="pageParam" value="${pageParam}" />
						<div class="row">
							<div class="col-md-12 ">
								<div class="form-group little_distance search" style="line-height: 35px;">
									<div class="search-item">
										<label class="control-label">实物编号：</label>
										<input name="prizeNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>
									<div class="search-item">
										<label class="control-label">中出类型：</label>
										<select name="prizeType" class="form-control input-width-larger search" autocomplete="off" >
											<option value="">全部</option>
											<option value="P">实物</option>
										</select>
									</div>
									<div class="search-item">
                                        <label class="control-label">兑换红包：</label>
                                        <select name="isExchangeMoney" class="form-control input-width-larger search" autocomplete="off" >
                                            <option value="">全部</option>
                                            <option value="1">是</option>
                                            <option value="0">否</option>
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
					<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">发布列表</span></div>
				</div>
				<div class="widget-content">
					<div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
						<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
							   id="dataTable_data">
							<input type="hidden" id="screenWidth1366" value="5,9,14,9,13,9,9,9,9,6,9" />
							<thead>
							<tr>
								<th style="width:4%;">序号</th>
								<th style="width:8%;" data-ordercol="prize_no">实物编号</th>
								<th style="width:10%;">奖品图片</th>
								<th style="width:12%;" data-ordercol="prize_name">奖品全称</th>
								<th style="width:7%;" data-ordercol="prize_type">中出类型</th>
								<th style="width:7%;" data-ordercol="111">领取方式</th>
								<th style="width:7%;">是否兑换红包</th>
								<th style="width:7%;">兑换金额（元）</th>
								<th style="width:6%;" data-ordercol="">中出截止时间</th>
								<th style="width:6%;" data-ordercol="create_time">创建时间</th>
								<th style="width:7%;">兑出状态</th>
								<th style="width:15%;">操作</th>
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
												<span>${item.prizeNo}</span>
											</td>
											<td>
												<span><img src="${item.prizeListPic}" style="height: 80px;width: 170px; float: left;"></span>
											</td>
											<td>
												<span>${item.prizeName}</span>
											</td>
											<td>
												<c:choose>
													<c:when test="${item.prizeType eq 'P'}">实物</c:when>
													<c:when test="${item.prizeType eq 'V'}">虚拟奖</c:when>
													<c:when test="${item.prizeType eq 'T'}">劵码</c:when>
												</c:choose>
											</td>
											<td>
												<c:choose>
													<c:when test="${item.verificationType eq '0'}">自提方式</c:when>
													<c:when test="${item.verificationType eq '1'}">物流发货（有核销码）</c:when>
													<c:when test="${item.verificationType eq '3'}">物流发货（无核销码）</c:when>
													<c:when test="${item.verificationType eq '2'}">在线直接领取</c:when>
												</c:choose>
											</td>
											<td>
                                                <c:choose>
                                                    <c:when test="${item.isExchangeMoney eq '0'}">否</c:when>
                                                    <c:when test="${item.isExchangeMoney eq '1'}">是</c:when>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${item.isExchangeMoney eq '1'}">
                                                        <c:choose>
		                                                    <c:when test="${item.exchangeMinMoney eq item.exchangeMaxMoney}">
		                                                        <span><fmt:formatNumber value="${item.exchangeMinMoney }" pattern="#,##0.00#" /></span>
		                                                    </c:when>
		                                                    <c:otherwise>
		                                                        <span>
		                                                          <fmt:formatNumber value="${item.exchangeMinMoney }" pattern="#,##0.00#" />
		                                                           - 
		                                                           <fmt:formatNumber value="${item.exchangeMaxMoney }" pattern="#,##0.00#" />
		                                                        </span>
		                                                    </c:otherwise>
		                                                </c:choose>
                                                    </c:when>
                                                    <c:when test="${item.isExchangeMoney eq '0'}">无</c:when>
                                                </c:choose>
                                            </td>
											<td>
												<span>${item.prizeEndTime}</span>
											</td>
											<td>
												<span>${item.createTime}</span>
											</td>
											<td>
												<c:choose>
													<c:when test="${item.cashStatus eq '1'}">
													    <span>兑出中</span>
													</c:when>
													<c:otherwise>
														<span>已结束</span>
													</c:otherwise>
												</c:choose>
											</td>
											<td data-key="${item.infoKey}" style="text-align: center;">
												<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
												<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
												<a class="btn btn-xs add btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;授权门店</a>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="13"><span>查无数据！</span></td>
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
</body>
</html>