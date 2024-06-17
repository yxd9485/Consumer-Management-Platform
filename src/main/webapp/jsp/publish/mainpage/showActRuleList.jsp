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
				var url = "<%=cpath%>/actRule/showActRuleAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var actRuleKey = $(this).parents("td").data("key");
				var url = "<%=cpath%>/actRule/showActRuleEdit.do?actRuleKey="+actRuleKey;
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
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">发布查询</span></div>
					<div class="col-md-10 text-right">
						<!-- 用户key不为空 ne -->
						<a id="addItem" class="btn btn-blue">
							<i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增规则
						</a>
					</div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
	            <!--查询url  -->
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/actRule/showActRuleList.do">
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
			            <!--搜索栏  -->
		                    <div class="form-group little_distance search" style="line-height: 35px;">
		                         <div class="search-item">
									<label class="control-label">按关键词查询：</label>
									<input name="keyword" class="form-control input-width-larger keyword" autocomplete="off" maxlength="100"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">限时规则时间：</label>
                                    <input name="limitStGmt" id="limitStGmt" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'limitEndGmt\')}'})" />
                                    <label class="">-</label>
                                    <input name="limitEndGmt" id="limitEndGmt" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'limitStGmt\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">最后修改时间：</label>
                                    <input name=modStGmt id="modStGmt" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'modEndGmt\')}'})" />
                                    <label class="">-</label>
                                    <input name="modEndGmt" id="modEndGmt" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'modStGmt\')}'})" />
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
					            	<th style="width:12%;">扫码活动</th>
					            	<th style="width:12%;">限时规则时间</th>
					            	<th style="width:6%;">最后修改人</th>
					            	<th style="width:6%;" >最后修改时间</th>
					            	<th style="width:7%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<!--gt大于 lt小于-->
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
                                    </td>
					        		<td style="text-align:center;">
					        			<span>${item.vcodeActivityName}</span>
					        		</td>
                                     <td style="text-align:center;">
                                        <span>${fn:substring(item.limitStGmt, 0, 10)} ${fn:substring(item.limitStGmt, 11, 19)} —— ${fn:substring(item.limitEndGmt, 0, 10)} ${fn:substring(item.limitEndGmt, 11, 19)}</span>
                                    </td>
                                    <td style="text-align:center;">
                                        <span>${item.modUser}</span>
                                    </td>
                                    <td style="text-align:center;">
                                        <span>${fn:substring(item.modGmt, 0, 10)} ${fn:substring(item.modGmt, 11, 19)}</span>
                                    </td>
					        		<td data-key="${item.actRuleKey}" style="text-align: center;">
					        		         <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
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
