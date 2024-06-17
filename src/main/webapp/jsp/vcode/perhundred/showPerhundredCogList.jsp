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
				var url = "<%=cpath%>/activityPerhundredCog/showPerhundredCogAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityPerhundredCog/showPerhundredCogEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityPerhundredCog/showPerhundredCogView.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 中奖纪录
			$("a.detail").off();
			$("a.detail").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityPerhundredPrizeRecord/showPerhundredPrizeRecordList.do?perhundredKey="+key;
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityPerhundredCog/doPerhundredCogDelete.do?infoKey="+key;
				$.fn.confirm("确认删除吗？", function(){
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
        	<li class="current"><a> 逢百规则</a></li>
            <li class="current">
            	<c:choose>
            		<c:when test="${tabsFlag eq '1'}">生效规则</c:when>
            		<c:otherwise>失效规则</c:otherwise>
            	</c:choose>	
            </li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable">
	        <a href="<%=cpath%>/activityPerhundredCog/showPerhundredCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">生效规则</a>
	        <a href="<%=cpath%>/activityPerhundredCog/showPerhundredCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">失效规则</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">逢百规则查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${currentUser.roleKey ne '4'}">
                           <a id="addItem" class="btn btn-blue">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增逢百规则
                           </a>
                       </c:if>
                   </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/activityPerhundredCog/showPerhundredCogList.do">
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
			                        <label class="control-label">编号查询：</label>
			                        <input name="perhundredNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">规则名称：</label>
			                        <input name="perhundredName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">启用状态：</label>
	                                <select name="status" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <option value="0">停用</option>
	                                    <option value="1">启用</option>
	                                </select>
                                </div>
                                <c:if test="${tabsFlag eq '1'}">
	                                <div class="search-item">
		                                <label class="control-label">规则状态：</label>
		                                <select name="isBegin" class="form-control input-width-larger search" autocomplete="off" >
		                                    <option style="padding: 20px;" value="">全部</option>
		                                    <option value="1">已上线</option>
		                                    <option value="0">待上线</option>
		                                </select>
                                    </div>
                                </c:if>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">逢百规则列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,14,9,13,9,9,9,9,6,9" />
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:8%;" data-ordercol="a.perhundred_no">规则编号</th>
					            	<th style="width:19%;" data-ordercol="a.perhundred_name">规则名称</th>
					            	<th style="width:10%;" data-ordercol="a.min_valid_date">规则时间</th>
					            	<th style="width:13%;" data-ordercol="a.vcodeActivityName">联接活动</th>
					            	<th style="width:7%;" data-ordercol="a.isBegin">规则状态</th>
					            	<th style="width:7%;" data-ordercol="a.restrict_vpoints">限制积分</th>
					            	<th style="width:7%;" data-ordercol="a.restrict_money">限制金额</th>
					            	<th style="width:7%;" data-ordercol="a.restrict_bottle">限制瓶数</th>
					            	<th style="width:6%;" data-ordercol="a.status">状态</th>
					            	<th style="width:15%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align: center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td>
                                        <span>${item.perhundredNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.perhundredName}</span>
					        		</td>
					        		<td>
					        			<div style="overflow:hidden;cursor: pointer; height: 35px;" title="${item.validDateRange}">
					        				<span>${item.validDateRange}</span>
					        			</div>
					        		</td>
					        		<td>
					        		     <span>${item.vcodeActivityName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>
											<c:choose>
					        				<c:when test="${item.isBegin eq '0'}">待上线</c:when>
					        				<c:when test="${item.isBegin eq '1'}">已上线</c:when>
					        				<c:otherwise>已下线</c:otherwise>
					        			</c:choose>
										</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span title="消耗积分">(${item.ruleTotalVpoints})</span>
					        			<c:if test="${item.restrictVpoints > 0}">
						        			<hr style="margin-bottom: 0px; margin-top: 0px; border-top: 1px solid #C7BDBD;">
						        			<span title="限制积分">${item.restrictVpoints}</span>
					        			</c:if>
					        		</td>
					        		<td style="text-align:center;">
					        			<span title="消耗金额">(${item.ruleTotalMoney})</span>
					        			<c:if test="${item.restrictMoney > 0}">
						        			<hr style="margin-bottom: 0px; margin-top: 0px; border-top: 1px solid #C7BDBD;">
						        			<span title="限制金额">${item.restrictMoney}</span>
					        			</c:if>
					        		</td>
					        		<td style="text-align:center;">
					        			<span title="消耗瓶数">(${item.ruleTotalBottle})</span>
					        			<c:if test="${item.restrictBottle > 0}">
						        			<hr style="margin-bottom: 0px; margin-top: 0px; border-top: 1px solid #C7BDBD;">
						        			<span title="限制瓶数">${item.restrictBottle}</span>
					        			</c:if>
					        		</td>
					        		<td style="text-align:center;">
					        		     <c:choose>
					        		         <c:when test="${item.status eq '0'}">停用</c:when>
					        		         <c:when test="${item.status eq '1'}">启用</c:when>
					        		     </c:choose>
					        		</td>
					        		<td data-key="${item.infoKey}" style="text-align: center;">
					        			<c:if test="${tabsFlag eq '1' and currentUser.roleKey ne '4'}">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
						        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
						        		</c:if>
						        		<c:if test="${tabsFlag eq '2' or currentUser.roleKey eq '4'}">
                                            <a class="btn btn-xs view btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;查 看</a>
						        		</c:if>
					        			<a class="btn btn-xs detail btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;详 情</a>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="12"><span>查无数据！</span></td>
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