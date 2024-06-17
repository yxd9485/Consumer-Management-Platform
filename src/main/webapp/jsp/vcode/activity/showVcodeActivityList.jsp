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
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?childTab=1&infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityView.do?childTab=1&infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 规则
			$("a.rule").off();
			$("a.rule").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?childTab=3&infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 码源
			$("a.generate").off();
			$("a.generate").on("click", function(){
				$("input[name='queryParam']").val("");
				$("input[name='pageParam']").val("");
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/qrcodeBatchInfo/showBatchForActivityUsedList.do?infoKey="+key;
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
            <li class="current"><a> 活动配置</a></li>
            <li class="current"><a title="">
                <c:if test="${tabsFlag eq '1'}">待上线</c:if>
                <c:if test="${tabsFlag eq '2'}">已上线</c:if>
                <c:if test="${tabsFlag eq '3'}">已下线</c:if>
                <c:if test="${tabsFlag eq '4'}">全部活动</c:if>
            </a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable">
            <a href="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
                 class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">待上线</a>
            <a href="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
                 class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已上线</a>
            <a href="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3" 
                 class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">已下线</a>
            <a href="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=4" 
                 class="btn <c:if test="${tabsFlag eq '4'}">btn-blue</c:if>">全部活动</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addItem" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增活动
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vcodeActivity/showVcodeActivityList.do">
                        <input type="hidden" name="activityType" value="${activityType}" />
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
                        <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
			                        <label class="control-label">关键字查询：</label>
			                        <input name="vcodeActivityNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
<%--                                <div class="search-item">--%>
<%--                                    <label class="control-label">码源类型：</label>--%>
<%--                                    <select name="skuType" class="form-control input-width-larger search" autocomplete="off" >--%>
<%--                                        <option style="padding: 20px;" value="">全部</option>--%>
<%--                                        <c:choose>--%>
<%--                                            <c:when test="${!fn:startsWith(currentUser.projectServerName, 'mengniu')}">--%>
<%--                                                <option value="0">瓶码</option>--%>
<%--                                                <option value="1">罐码</option>--%>
<%--                                                <option value="2">箱码</option>--%>
<%--                                            </c:when>--%>
<%--                                            <c:otherwise>--%>
<%--                                                <option value="4">支码</option>--%>
<%--                                            </c:otherwise>--%>
<%--                                        </c:choose>--%>

<%--                                    </select>--%>
<%--                                </div>--%>
                                <div class="search-item">
                                    <label class="control-label">活动时间：</label>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                                <c:choose>
                                    <c:when test="${tabsFlag eq '4'}">
                                        <input type="hidden" id="screenWidth1366" value="5,9,14,9,11,9,9,9,9,9,9" />
		                                <tr>
		                                    <th style="width:4%;">序号</th>
		                                    <th style="width:8%;" data-ordercol="a.vcode_activity_no">活动编号</th>
		                                    <th style="width:18%;" data-ordercol="a.vcode_activity_name">活动名称</th>
		                                    <th style="width:12%;" data-ordercol="a.start_date">活动时间</th>
<%--		                                    <th style="width:12%;" data-ordercol="a.activity_year">活动年份</th>--%>
<%--		                                    <th style="width:15%;" data-ordercol="s.sku_name">SKU</th>--%>
<%--		                                    <th style="width:6%;" data-ordercol="s.sku_type">码源类型</th>--%>
		                                    <th style="width:6%;" data-ordercol="m.valid_count">一码多扫</th>
                                            <th style="width:6%;" data-ordercol="a.activity_version">活动版本</th>
		                                    <th style="width:10%;" data-ordercol="a.create_time" data-orderdef>创建时间</th>
		                                    <th style="width:6%;" data-ordercol="isBegin">活动状态</th>
		                                    <th style="width:15%;">操作</th>
		                                </tr>
                                    </c:when>
                                    <c:otherwise>
                                        <input type="hidden" id="screenWidth1366" value="5,12,17,9,12,9,9,9,9,9" />
		                                <tr>
		                                    <th style="width:4%;">序号</th>
		                                    <th style="width:8%;" data-ordercol="a.vcode_activity_no">活动编号</th>
		                                    <th style="width:17%;" data-ordercol="a.vcode_activity_name">活动名称</th>
		                                    <th style="width:12%;" data-ordercol="a.start_date">活动时间</th>
<%--											<th style="width:12%;" data-ordercol="a.activity_year">活动年份</th>--%>
<%--		                                    <th style="width:15%;" data-ordercol="s.sku_name">SKU</th>--%>
<%--		                                    <th style="width:6%;" data-ordercol="s.sku_type">码源类型</th>--%>
		                                    <th style="width:6%;" data-ordercol="m.valid_count">一码多扫</th>
		                                    <th style="width:6%;" data-ordercol="a.activity_version">活动版本</th>
		                                    <th style="width:11%;" data-ordercol="a.create_time" data-orderdef>创建时间</th>
		                                    <th style="width:15%;">操作</th>
		                                </tr>
                                    </c:otherwise>
                                </c:choose>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
									<c:choose>
										<c:when test="${tabsFlag eq '4'}">
											<td style="text-align:center;
	                                            <c:choose>
	                                                <c:when test="${item.isBegin == '0'}">background-color: #ecb010;</c:when>
	                                                <c:when test="${item.isBegin == '1'}">background-color: #ea918c;</c:when>
	                                                <c:when test="${item.isBegin == '2'}">background-color: #999999;</c:when>
	                                                <c:otherwise>background-color: red;</c:otherwise>
	                                            </c:choose>">
		                                        <span>${idx.count}</span>
		                                    </td>
										</c:when>
										<c:otherwise>
                                            <td style="text-align:center; <c:if test="${tabsFlag eq '1' and item.isWarning eq '1'}">background-color: red;</c:if>">
                                                <span>${idx.count}</span>
                                            </td>
										</c:otherwise>
									</c:choose>
					        		
					        		<td style="text-align:center;">
					        			<span>${item.vcodeActivityNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.vcodeActivityName}</span>
					        		</td>
					        		<td style="text-align:center;">
                                        <span>从${item.startDate}到${item.endDate}</span>
					        		</td>
<%--					        		<td style="text-align:center;">--%>
<%--                                        <span>${item.activityYear}</span>--%>
<%--					        		</td>--%>
<%--					        		<td>--%>
<%--					        			<span title="${item.commodityCode}">${item.skuName}</span>--%>
<%--					        		</td>--%>
<%--					        		<td style="text-align:center;">--%>
<%--                                         <c:choose>--%>
<%--                                             <c:when test="${item.skuType eq '0'}">瓶码</c:when>--%>
<%--                                             <c:when test="${item.skuType eq '1'}">罐码</c:when>--%>
<%--                                             <c:when test="${item.skuType eq '2'}">箱码</c:when>--%>
<%--                                             <c:when test="${item.skuType eq '4'}">支码</c:when>--%>
<%--                                             <c:otherwise>其他</c:otherwise>--%>
<%--                                         </c:choose>--%>
<%--					        		</td>--%>
					        		<td style="text-align:center;">
					        			<span>
					        				<c:choose>
					        					<c:when test="${item.validCount != null}">${item.validCount}</c:when>
					        					<c:otherwise>1</c:otherwise>
					        				</c:choose>
					        				</span>
					        		</td>
                                    <td>
                                    <c:forEach items="${versionLst}" var="versionItem">
                                        <c:if test="${versionItem.dataValue eq item.activityVersion}">${versionItem.dataAlias}</c:if>
                                    </c:forEach>
                                    </td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(item.createTime, 0, 19)}</span>
					        		</td>
					        		<c:if test="${tabsFlag eq '4' }">
	                                    <td style="text-align:center;">
	                                        <c:choose>
	                                            <c:when test="${item.isBegin == '0'}">
	                                                <span style="color: #ecb010;"><b>待上线</b></span>
	                                            </c:when>
	                                            <c:when test="${item.isBegin == '1'}">
	                                                <span style="color: #ea918c;"> <b>已上线</b> </span>
	                                            </c:when>
	                                            <c:when test="${item.isBegin == '2'}">
	                                                <span style="color: #999999;"> <b>已下线</b> </span>
	                                            </c:when>
	                                            <c:otherwise>
	                                                <span style="color: red;"><b>${item.isBegin}</b></span>
	                                            </c:otherwise>
	                                        </c:choose>
	                                    </td>
					        		</c:if>
                                    <td data-key="${item.vcodeActivityKey}" style="text-align: center;">
                                        <c:choose>
                                            <c:when test="${currentUser.roleKey eq '4'}">
                                                <a class="btn btn-xs view btn-blue"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;查 看</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                            </c:otherwise>
                                        </c:choose>
                                        <a class="btn btn-xs rule btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;规 则</a>
                                        <a class="btn btn-xs generate btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;码 源</a>
                                    </td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${tabsFlag eq '4'}">11</c:when><c:otherwise>10</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
                    <h6 class="edit_fail_lackFirstMoney">编辑失败：规则"${flagRuleErrMsg}"导入金额缺少首扫区间金额</h6>
                    <h6 class="edit_fail_lackFirstVpoints">编辑失败：规则"${flagRuleErrMsg}"导入积分缺少首扫区间积分</h6>
                    <h6 class="edit_fail_lackDoubtMoney">编辑失败：规则"${flagRuleErrMsg}"导入金额缺少可疑区间金额</h6>
                    <h6 class="edit_fail_lackDoubtVpoints">编辑失败：规则"${flagRuleErrMsg}"导入积分缺少可疑区间积分</h6>
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
