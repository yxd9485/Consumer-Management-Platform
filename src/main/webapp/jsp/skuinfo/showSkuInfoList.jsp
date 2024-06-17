<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
	String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
	String pathPrefix = cpath + "/" + imagePathPrx;
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
			$("#addActivity").click(function(){
				var url = "<%=cpath%>/skuInfo/showSkuInfoAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});

			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/skuInfo/showSkuInfoEdit.do?skuKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/skuInfo/doSkuInfoDelete.do?skuKey="+key;
				$.fn.confirm("确认删除吗？", function(){
					$("form").attr("action", url);
					$("form").submit();
				});
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
	</style>
</head>

<body>
<div class="container">
	<div class="crumbs">
		<ul id="breadcrumbs" class="breadcrumb">
			<li class="current"><a> 首页</a></li>
			<li class="current"><a> 基础配置</a></li>
			<li class="current"><a title="">产品列表</a></li>
		</ul>
	</div>
	<div class="row">
		<div class="col-md-12 tabbable tabbable-custom">
			<div class="widget box">
				<div class="row">
					<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品查询</span></div>
					<div class="col-md-10 text-right">
						<c:if test="${currentUser.roleKey ne '4'}">
							<a id="addActivity" class="btn btn-blue">
								<i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
							</a>
						</c:if>
					</div>
				</div>
				<div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
				<div class="widget-content form-inline">
					<form class="listForm" method="post" action="<%=cpath%>/skuInfo/showSkuInfoList.do">
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
										<label class="control-label">SKU名称：</label>
										<input name="skuName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>
									<%--<div class="search-item">
										<label class="control-label">69码：</label>
										<input name="commodityCode" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>--%>
									<div class="search-item">
										<label class="control-label">SKU主键：</label>
										<input name="sku_colume" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>
									<div class="search-item">
										<label class="control-label">有效天数：</label>
										<input name="validDay" class="form-control input-width-larger" autocomplete="off" maxlength="30" oninput = "value=value.replace(/[^\d]/g,'')"/>
									</div>
									<div class="search-item">
										<label class="control-label">产品类型：</label>
										<select name="skuType" class="form-control input-width-larger search" autocomplete="off" >
											<option value="">全部</option>
                                               <c:choose>
                                                   <c:when test="${currentUser.projectServerName eq 'zuijiu' }">
                                                      <option value="">请选择</option>
                                                      <option value="0">瓶码</option>
                                                      <option value="2">箱码</option>
                                                   </c:when>
                                                   <c:when test="${currentUser.projectServerName eq 'mengniu' or currentUser.projectServerName eq 'mengniuzhi' }">
                                                      <option value="">请选择</option>
                                                      <option value="2">箱码</option>
                                                      <option value="4">支码</option>
                                                       <option value="3">其他</option>
                                                   </c:when>
                                                   <c:otherwise>
                                                      <option value="0">瓶码</option>
                                                      <option value="1">罐码</option>
                                                      <option value="2">箱码</option>
                                                      <option value="3">其他</option>
                                                   </c:otherwise>
                                               </c:choose>
										</select>
									</div>
									<div class="search-item" <c:if test="${currentUser.projectServerName eq 'mengniu' or currentUser.projectServerName eq 'mengniuzhi' }">style="display: none" </c:if>>
										<label class="control-label">产品码类型：</label>
										<select name="operationsType" class="form-control input-width-larger search" autocomplete="off">
											<option value="">全部</option>
											<option value="1">仅消费者促销活动</option>
											<option value="2">仅终端促销活动</option>
											<option value="3">消费者+终端一码双扫</option>
											<option value="4">无促销活动</option>
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
					<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">产品列表</span></div>
				</div>
				<div class="widget-content">
					<div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
						<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
							   id="dataTable_data">
							<thead>
							<tr>
								<th style="width:4%;">序号</th>
								<th style="width:6%;">产品图片</th>
								<c:if test="${currentUser.projectServerName eq 'zuijiu'}">
									<th style="width:8%;">品牌介绍页</th>
								</c:if>
								<th style="width:14%;" data-ordercol="sku_name">SKU名称</th>
								<th style="width:10%;">SKU主键</th>
								<%--<th style="width:11%;" data-ordercol="commodity_code">69码</th>--%>
								<c:if test="${currentUser.projectServerName ne 'mengniu' and currentUser.projectServerName ne 'mengniuzhi' }">
								<th style="width:10%;">产品码类型</th>
								</c:if>
								<th style="width:10%;" data-ordercol="commodity_code">品牌统一编码</th>
								<th style="width:12%;" data-ordercol="commodity_code">品牌统一名称</th>
								<th style="width:6%;" data-ordercol="valid_day">有效天数</th>
								<th style="width:6%;" data-ordercol="sku_type">产品类型</th>
								<th style="width:6%;" data-ordercol="push_msg_flag">推送日报</th>
                                <c:if test="${currentUser.projectServerName eq 'qingpi'}">
                                    <th style="width:7%;" data-ordercol="item_name">item名称</th>
                                </c:if>
								<th style="width:11%;" data-ordercol="create_time" data-orderdef>创建时间</th>
								<c:if test="${currentUser.roleKey ne '4'}">
									<th style="width:10%;">操作</th>
								</c:if>
							</tr>
							</thead>
							<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) gt 0}">
									<c:forEach items="${resultList}" var="skuInfo" varStatus="idx">
										<tr>
											<td style="text-align:center;">
												<span>${idx.count}</span>
											</td>
											<td style="text-align:center;">
                                        <span>
                                            <c:if test="${not empty(skuInfo.skuLogo)}">
                                            <img onmouseover="imgBig('<%=imageServerUrl%>${skuInfo.skuLogo}');" onmouseleave="imgSmall();"
												 style="width: 50px;height: 50px; cursor: pointer;" alt="Logo" src="<%=imageServerUrl%>${skuInfo.skuLogo}"/>
											</c:if>
                                        </span>
											</td>
											<c:if test="${currentUser.projectServerName eq 'zuijiu'}">
												<td style="text-align:center;">
                                        <span>
                                            <c:if test="${not empty(skuInfo.brandIntroduceUrl)}">
                                            <img onmouseover="imgBig('<%=imageServerUrl%>${skuInfo.brandIntroduceUrl}');" onmouseleave="imgSmall();"
												 style="width: 50px;height: 50px; cursor: pointer;" alt="Logo" src="<%=imageServerUrl%>${skuInfo.brandIntroduceUrl}"/>
											</c:if>
                                        </span>
												</td>

											</c:if>
											<td style="text-align:center;">
												<span>${skuInfo.skuName}</span>
											</td>
												<%--	<td style="text-align:center;">
                                                        <span title="${skuInfo.commodityCode}">${skuInfo.skuName}</span>
                                                    </td>--%>
												<%-- 		<td style="text-align:center;">
                                                             <span>${skuInfo.commodityCode}</span>
                                                         </td>--%>
											<td style="text-align:center;">
												<span>${skuInfo.skuKey}</span>
											</td>
											<c:if test="${currentUser.projectServerName ne 'mengniu' and currentUser.projectServerName ne 'mengniuzhi' }">
												<td style="text-align:center;">
													<c:choose>
														<c:when test="${skuInfo.operationsType eq '1'}">仅消费者促销活动</c:when>
														<c:when test="${skuInfo.operationsType eq '2'}">仅终端促销活动</c:when>
														<c:when test="${skuInfo.operationsType eq '3'}">消费者+终端一码双扫</c:when>
														<c:when test="${skuInfo.operationsType eq '4'}">无促销活动</c:when>
														<c:otherwise></c:otherwise>
													</c:choose>
												</td>
											</c:if>
											<td style="text-align:center;">
												<span>${skuInfo.unificationCode}</span>
											</td>
											<td style="text-align:center;">
												<span>${skuInfo.unificationName}</span>
											</td>
											<td style="text-align:center;">
												<span>${skuInfo.validDay}</span>
											</td>
											<td style="text-align:center;">
												<c:choose>
													<c:when test="${skuInfo.skuType eq '0'}">瓶码</c:when>
													<c:when test="${skuInfo.skuType eq '1'}">罐码</c:when>
													<c:when test="${skuInfo.skuType eq '2'}">箱码</c:when>
													<c:when test="${skuInfo.skuType eq '3'}">其它</c:when>
													<c:when test="${skuInfo.skuType eq '4'}">支码</c:when>
												</c:choose>
											</td>
											<td style="text-align:center;">
												<c:choose>
													<c:when test="${skuInfo.pushMsgFlag eq '1'}">推送</c:when>
													<c:otherwise>不推送</c:otherwise>
												</c:choose>
											</td>
                                            <c:if test="${currentUser.projectServerName eq 'qingpi'}">
                                                <td style="text-align:center;">
                                                    <span>${skuInfo.itemName}</span>
                                                </td>
                                            </c:if>
											<td style="text-align:center;">
												<span>${fn:substring(skuInfo.createTime, 0, 19)}</span>
											</td>
											<c:if test="${currentUser.roleKey ne '4'}">
												<td data-key="${skuInfo.skuKey}" style="text-align: center;">
													<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
													<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
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
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content" style="top:30%;">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">提示消息</h4>
				</div>
				<div class="modal-body">
					<h6 class="add_success">添加成功</h6>
					<h6 class="add_fail">添加失败</h6>
					<h6 class="edit_success">编辑成功</h6>
					<h6 class="edit_fail">编辑失败</h6>
					<h6 class="del_success">删除成功</h6>
					<h6 class="del_fail">删除失败</h6>
					<h6 class="del_invalid">删除失败,先删除该sku关联的活动</h6>
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
