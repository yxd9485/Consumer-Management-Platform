<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();

String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
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
				var url = "<%=cpath%>/terminal/addTerminal.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/terminal/showTerminal.do?terminalKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});

			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/terminal/delTerminal.do?terminalKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
			});
			
			// 授权奖品
			$("a.addPrize").off();
			$("a.addPrize").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/terminal/showPrizeAddLst.do?terminalKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 禁用
			$("a.jy").off();
			$("a.jy").on("click", function(){
				var key = $(this).parents("td").data("key");
				var currentRow=$(this).closest("tr"); 
                var status = currentRow.find("td:eq(0)").text();
				var url = "<%=cpath%>/terminal/updateTerminalStatus.do?status="+status+"&terminalKey="+key;
				
				$.fn.confirm("确认操作吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
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
			<li class="current"><a> 实物</a></li>
		</ul>
	</div>
	<div class="row">
		<div class="col-md-12 tabbable tabbable-custom">
			<div class="widget box">
				<div class="row">
					<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">实物查询</span></div>
					<div class="col-md-10 text-right">
						<a id="addItem" class="btn btn-blue">
							<i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增门店
						</a>
					</div>
				</div>
				<div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
				<div class="widget-content form-inline">
					<form class="listForm" method="post"
						  action="<%=cpath%>/terminal/showTerminalList.do">
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
										<label class="control-label">实物编号：</label>
										<input name="terminalNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>
									<div class="search-item">
										<label class="control-label">门店名称：</label>
										<input name="terminalName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>
			
									<div class="search-item">
	                                <label class="control-label">奖品类型：</label>
	                               		<select name="prizeType" tag="validate" class="form-control required input-width-large search" >
									    	<option value="">请选择</option>
	                                        <c:forEach items="${bigPrizes}" var="item">
	                                            <option value="${item.prizeNo}">${item.prizeName}</option>
	                                        </c:forEach>
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
							<thead>
							<tr>
								<th style="width:4%;">序号</th>
								<th style="width:8%;">门店编号</th>
								<th style="width:10%;">门店图片</th>
								<th style="width:12%;">门店名称</th>
								<th style="width:7%;">门店地址</th>
								<th style="width:6%;">联系电话</th>
								<th style="width:10%;">创建时间</th>
								<th style="width:5%;">状态</th>
								<th style="width:12%;">操作</th>	
							</tr>
							</thead>
							<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) gt 0}">
									<c:forEach items="${resultList}" var="item" varStatus="idx">
										<tr>
											<td style="display:none">
												<span>${item.status}</span>
											</td>
											
											<td style="text-align:center;">
												<span>${idx.count}</span>
											</td>
											<td>
												<span>${item.terminalNo}</span>
											</td>
											<td>
												<c:choose>
													<c:when test="${item.imageUrl eq ''}">
														<span></span>
													</c:when>
													<c:otherwise>	
														<span><img src="<%=imageServerUrl%>${item.imageUrl}" style="height: 80px;width: 170px; float: left;"></span>
													</c:otherwise>
												</c:choose>
											</td>
											<td>
												<span>${item.terminalName}</span>
											</td>
											<td>
												<span>${item.address}</span>
											</td>
											<td>
												<span>${item.phoneNum}</span>
											</td>
											<td>
												<span>${item.createTime}</span>
											</td>
											<td>
												<c:choose>     
												  <c:when test="${item.status eq '0'}">
												  		<span>启用中</span>
												  </c:when>
												  <c:otherwise>
												  		<span>已关闭</span>
												  </c:otherwise>											
												</c:choose>
											</td>
											
											<td data-key="${item.terminalKey}">
												<a class="btn btn-xs addPrize btn-orange" ><i class="iconfont icon-xiugai"></i>&nbsp;授权奖品</a>
												<a class="btn btn-xs edit btn-orange" ><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
												<a class="btn btn-xs edit btn-orange" ><i class="iconfont icon-xiugai"></i>&nbsp;门店详情</a>
												<c:choose>     
												  <c:when test="${item.status eq '0'}">
												  		<a class="btn btn-xs jy btn-orange" ><i class="iconfont icon-xiugai"></i>&nbsp;禁用</a>
												  </c:when>
												  <c:otherwise>
												  		<a class="btn btn-xs jy btn-orange" ><i class="iconfont icon-xiugai"></i>&nbsp;启用</a>
												  </c:otherwise>											
												</c:choose>																									
												<a class="btn btn-xs del btn-red" ><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>

											</td>	
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