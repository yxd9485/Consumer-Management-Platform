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
				var url = "<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			// 数据
			$("a.data").off();
			$("a.data").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityData.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 停用
			$("a.stop").off();
			$("a.stop").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/groupBuyingActivity/updateActivityStop.do";
				$.fn.confirm("停用后将不可恢复，确定停用该拼团活动？", function() {
					$.ajax({
	                    type: "POST",
	                    url: url,
	                    data: {"infoKey": key},
	                    dataType: "json",
	                    async: false,
	                    beforeSend:appendVjfSessionId,
	                    success:  function(data) {
                    		$.fn.alert(data['errMsg'], function(){
                        		$("button.btn-primary").trigger("click");
                            });
	                    }
	                });
                });
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/groupBuyingActivity/delActivity.do";
				$.fn.confirm("确定要删除吗？", function() {
					$.ajax({
	                    type: "POST",
	                    url: url,
	                    data: {"infoKey": key},
	                    dataType: "json",
	                    async: false,
	                    beforeSend:appendVjfSessionId,
	                    success:  function(data) {
                    		$.fn.alert(data['errMsg'], function(){
                        		$("button.btn-primary").trigger("click");
                            });
	                    }
	                });
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
            <li class="current"><a> 积分商城</a></li>
            <li class="current"><a> 拼团配置</a></li>
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
            <a href="<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
                 class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">待上线</a>
            <a href="<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
                 class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已上线</a>
            <a href="<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3" 
                 class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">已下线</a>
            <a href="<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=4" 
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
	                    action="<%=cpath%>/groupBuyingActivity/showGroupBuyingActivityList.do">
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
			                        <label class="control-label">关键字：</label>
			                        <input name="keyWord" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
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
                                <input type="hidden" id="screenWidth1366" value="5,9,14,9,11,9,9,9,9,9,9" />
                                <tr>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:8%;" data-ordercol="a.activity_no">拼团编号</th>
                                    <th style="width:15%;" data-ordercol="a.activity_name">活动名称</th>
                                    <th style="width:14%;" data-ordercol="g.goods_name">拼团商品</th>
                                    <th style="width:6%;" data-ordercol="a.group_buying_pay" data-orderdef>拼团价格</th>
                                    <th style="width:6%;" data-ordercol="a.group_buying_vpoints" data-orderdef>拼团积分</th>
                                    <th style="width:6%;" data-ordercol="a.is_stop" data-orderdef>是否停用</th>
                                    <th style="width:10%;" data-ordercol="a.begin_date" data-orderdef>活动时间</th>
                                    <th style="width:6%;" data-ordercol="a.group_buying_periods_num">拼团总库存</th>
                                    <th style="width:10%;" data-ordercol="a.create_time" data-orderdef>创建时间</th>
                                    <th style="width:15%;">操作</th>
                                </tr>
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
                                            <td style="text-align:center;">
                                                <span>${idx.count}</span>
                                            </td>
										</c:otherwise>
									</c:choose>
					        		
					        		<td style="text-align:center;">
					        			<span>${item.activityNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.activityName}</span>
					        		</td>
					        		<td>
					        			<span>${item.goodsName}</span>
					        		</td>
					        		<td>
					        			<span>${item.groupBuyingPay}</span>
					        		</td>
					        		<td>
					        			<span>${item.groupBuyingVpoints}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<c:choose>
					        				<c:when test="${item.isStop eq '1' }"><span style="color: red;">是</span></c:when>
					        				<c:otherwise>否</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<%-- <td>
					        			<span>${item.groupBuyingVpoints}</span>
					        		</td> --%>
					        		<td>
					        			<c:choose>
					        				<c:when test="${item.ruleType eq '1' }">
					        					<span>${item.beginDate}到${item.endDate}</span>
					        				</c:when>
					        				<c:when test="${item.ruleType eq '2' }">
					        					<span>周${item.beginDate}到周${item.endDate}</span>
					        				</c:when>
					        			</c:choose>
					        		</td>
					        		<td>
					        			<span>${item.groupBuyingTotalNum}</span>
					        		</td>
					        		<td>
					        			<span>${item.createTime}</span>
					        		</td>
                                    <td data-key="${item.infoKey}" style="text-align: center;">
                                    	<a class="btn btn-xs data btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;数 据</a>
                                        <c:choose>
                                            <c:when test="${currentUser.roleKey eq '4'}">
                                                <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;查 看</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                                <c:if test="${item.isBegin eq '1' and item.isStop eq '0'}">
                                                	<a class="btn btn-xs stop btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;停 用</a>
                                                </c:if>
                                                <c:if test="${item.isBegin eq '0' }">
                                                	<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
                                                </c:if>
                                            </c:otherwise>
                                        </c:choose>
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
