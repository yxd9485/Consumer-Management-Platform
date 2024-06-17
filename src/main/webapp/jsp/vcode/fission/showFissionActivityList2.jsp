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
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
			
			// 新增
			$("#addActivity").click(function(){
				var url = "<%=cpath%>/doubleprize/showDoublePrizeAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/doubleprize/showDoublePrizeEdit.do?activityKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			
			// 规则
			$("a.rule").off();
			$("a.rule").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=6";
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/doubleprize/showDoublePrizeView.do?activityKey="+key;
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
            <li class="current"><a> 激活红包规则</a></li>
            <li class="current">
            	<c:choose>
            		<c:when test="${tabsFlag eq '0'}">失效规则</c:when>
            		<c:when test="${tabsFlag eq '1'}">生效规则</c:when>
            	</c:choose>
            </li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
			<a href="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1"
			   class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">生效规则</a>
			<a href="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=0"
			   class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">失效规则</a>
	    </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">激活红包规则查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addActivity" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;">新增规则</i>&nbsp;
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm2" method="post"
	                    action="<%=cpath%>/activateRedEnvelopeRuleCog/showFissionActivityList.do">
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
							<div class="form-group little_distance search"  style="line-height: 35px;">
									<div class="search-item">
										<label class="control-label">活动编号查询：</label>
										<input name="ruleNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
									</div>
                                    <div class="search-item">
				                        <label class="control-label">按关键字查询：</label>
				                        <input name="ruleName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
				                        <label class="control-label">启用状态：</label>
										<select name="stateFlag" class="form-control input-width-larger search" autocomplete="off" >
											<option style="padding: 20px;" value="">全部</option>
											<option value="0">停用</option>
											<option value="1">启用</option>
										</select>
                                    </div>
									<div class="search-item">
										<label class="control-label">规则名称：</label>
										<input name="ruleName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,10,15,10,9,9,10,8,8,9,12" />
                            <thead>
					            <tr>
                                    <th style="width:4%;">序号</th>
					            	<th style="width:9%;" data-ordercol="a.ruleNo">规则编号</th>
					            	<th style="width:15%;" data-ordercol="a.ruleName">规则名称</th>
					            	<th style="width:12%;" data-ordercol="a.startTime">活动日期范围</th>
					            	<th style="width:7%;" data-ordercol="a.shareSkuKeyList">分享SKU</th>
					            	<th style="width:7%;" data-ordercol="a.shareSkuKeyList">激活SKU</th>
					            	<th style="width:6%;" data-ordercol="a.stateFlag">启用状态</th>
					            	<th style="width:10%;" data-ordercol="a.createTime" data-orderdef>创建时间</th>
					            	<th style="width:15%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) gt 0}">
									<c:forEach items="${resultList}" var="activity" varStatus="idx">
										<tr>
											<td style="text-align:center;">
												<span>${idx.count}</span>
											</td>
											<td style="text-align:center;">
												<span>${activity.ruleNo}</span>
											</td>
											<td>
												<span>${activity.ruleName}</span>
											</td>
											<td style="text-align:center;">
												<span>从&nbsp;${activity.startTime}</span>
												<span>到&nbsp;${activity.endTime}</span>
											</td>
											<td style="text-align: right;">
												<span title="">${activity.shareSkuKeyList}</span>
											</td>
											<td style="text-align: right;">
												<span>${activity.activationSkuKeyList}</span>
											</td>
											<td  style="text-align: right;">
												<span>
													<c:choose>
														<c:when test="${activity.stateFlag == '0'}">停用</c:when>
														<c:when test="${activity.stateFlag == '1'}">启用</c:when>
													</c:choose>
												</span>
											</td>
											<td style="text-align:center;">
<%--												<span>${fn:substring(activity.createTime, 0, 19)}</span>--%>
												<span>${activity.createTime}</span>
											</td>
											<td data-key="${activity.activityKey}" style="text-align: center;">
												<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
												<a class="btn btn-xs view btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;查 看</a>
												<a class="btn btn-xs rule btn-brownness"> <i class="iconfont icon-xiugai"></i>&nbsp;
<%--													<c:choose>--%>
<%--														<c:when test="${ currentUser.roleKey ne '4'}">配置规则</c:when>--%>
<%--														<c:when test="${activity.moneyConfigFlag == '1' and currentUser.roleKey ne '4'}">修改规则</c:when>--%>
<%--														<c:otherwise>查看规则</c:otherwise>--%>
<%--													</c:choose>--%>
												</a>
											</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="11"><span>查无数据！</span></td>
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
						<h6 class="import_success">导入成功</h6>
						<h6 class="import_fail">导入失败</h6>
						<h6 class="not_delete">请至少选择一条数据进行删除</h6>
						<h6 class="is_delete">删除成功</h6>
						<h6 class="xx_delete">删除失败</h6>
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
