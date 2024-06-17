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
				var url = "<%=cpath%>/benediction/showExpandRuleAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/benediction/showExpandRuleEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/benediction/showExpandRuleView.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/benediction/doExpandRuleDelete.do?infoKey="+key;
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
        	<li class="current"><a> 膨胀规则</a></li>
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
	        <a href="<%=cpath%>/benediction/showExpandRuleList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">生效规则</a>
	        <a href="<%=cpath%>/benediction/showExpandRuleList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">失效规则</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">膨胀规则查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${currentUser.roleKey ne '4'}">
                           <a id="addItem" class="btn btn-blue">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增膨胀规则
                           </a>
                       </c:if>
                   </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/benediction/showExpandRuleList.do">
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
			                        <label class="control-label">规则编号：</label>
			                        <input name="expandRuleNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">规则名称：</label>
			                        <input name="expandRuleName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <c:if test="${tabsFlag eq '1'}">
	                                <div class="search-item">
		                                <label class="control-label">规则状态：</label>
		                                <select name="isBegin" class="form-control input-width-larger search" autocomplete="off" >
		                                    <option value="">全部</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">膨胀规则列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:10%;" data-ordercol="a.expand_rule_no" data-orderdef>规则编号</th>
					            	<th style="width:16%;" data-ordercol="a.expand_rule_name">规则名称</th>
					            	<th style="width:14%;" data-ordercol="a.begin_date">规则时间</th>
					            	<th style="width:18%;" data-ordercol="a.vcodeActivityName">关联活动</th>
					            	<th style="width:10%;" data-ordercol="a.expand_danping">单瓶分享金额</th>
					            	<th style="width:12%;" data-ordercol="a.create_time">创建时间</th>
					            	<th style="width:15%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;
                                        <c:choose>
                                            <c:when test="${item.isBegin == '0'}">background-color: #ecb010;</c:when>
                                            <c:when test="${item.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${item.isBegin == '2'}">background-color: #999999;</c:when>
                                            <c:otherwise>background-color: red;</c:otherwise>
                                        </c:choose>">
                                        <span>${idx.count}</span>
                                    </td>
					        		<td style="text-align:center;">
                                        <span>${item.expandRuleNo}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.expandRuleName}</span>
					        		</td>
                                    <td style="text-align:center;">
                                        <span>从${item.beginDate}到${item.endDate}</span>
                                    </td>
					        		<td style="text-align:center;">
					        			<span>${item.vcodeActivityName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.expandDanping}</span>
					        		</td>
                                    <td style="text-align:center;">
                                        <span>${fn:substring(item.createTime, 0, 19)}</span>
                                    </td>
					        		<td data-key="${item.infoKey}" style="text-align: center;">
					        			<c:if test="${tabsFlag eq '1' and currentUser.roleKey ne '4'}">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
						        			<c:if test="${item.isBegin eq '0'}">
							        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
						        			</c:if>
						        		</c:if>
						        		<c:if test="${tabsFlag eq '2' or currentUser.roleKey eq '4'}">
                                            <a class="btn btn-xs view btn-blue"><i class="iconfont icon-xinxi"></i>&nbsp;查 看</a>
						        		</c:if>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="8"><span>查无数据！</span></td>
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
