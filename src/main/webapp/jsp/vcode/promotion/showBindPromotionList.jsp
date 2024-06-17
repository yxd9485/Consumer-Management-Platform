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
				var url = "<%=cpath%>/promotion/showBindPromotionAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/promotion/showBindPromotionEdit.do?activityKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			
			// 规则
			$("a.rule").off();
			$("a.rule").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=5";
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/promotion/showBindPromotionView.do?activityKey="+key;
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
        	<li class="current"><a> 捆绑升级</a></li>
            <li class="current">
            	<c:choose>
            		<c:when test="${tabsFlag eq '0'}">待上线</c:when>
            		<c:when test="${tabsFlag eq '1'}">已上线</c:when>
            		<c:when test="${tabsFlag eq '2'}">已下线</c:when>
            		<c:otherwise>全部活动</c:otherwise>
            	</c:choose>	
            </li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
	        <a href="<%=cpath%>/promotion/showBindPromotionList.do?vjfSessionId=${vjfSessionId}&tabsFlag=0" 
	             class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">待上线</a>
	        <a href="<%=cpath%>/promotion/showBindPromotionList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">已上线</a>
	        <a href="<%=cpath%>/promotion/showBindPromotionList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已下线</a>
	        <a href="<%=cpath%>/promotion/showBindPromotionList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3" 
	             class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">全部活动</a>
	    </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">捆绑升级查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addActivity" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增活动
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/promotion/showBindPromotionList.do">
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
		                    <div class="form-group little_distance search" style="">
			                    <div class="search-item">
			                        <label class="control-label">关键字：</label>
			                        <input name="keyword" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                            <input type="hidden" id="screenWidth1366" value="5,12,22,10,20,6,13,12" />
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:10%;" data-ordercol="a.activity_no">活动编号</th>
					            	<th style="width:20%;" data-ordercol="a.activity_name">活动名称</th>
					            	<th style="width:15%;" data-ordercol="a.start_date">活动时间</th>
					            	<th style="width:18%;" data-ordercol="a.promotionSkuName">促销SKU</th>
					            	<th style="width:6%;" data-ordercol="a.isBegin">状态</th>
					            	<th style="width:12%;" data-ordercol="a.create_time" data-orderdef>创建时间</th>
					            	<th style="width:15%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="activity" varStatus="idx">
					        	<tr>
                                    <td style="text-align:center;
                                        <c:choose>
                                            <c:when test="${activity.isBegin == '0'}">background-color: #ecb010;</c:when>
                                            <c:when test="${activity.isBegin == '1'}">background-color: #ea918c;</c:when>
                                            <c:when test="${activity.isBegin == '2'}">background-color: #999999;</c:when>
                                            <c:otherwise>background-color: red;</c:otherwise>
                                        </c:choose>">
                                        <span>${idx.count}</span>
                                    </td>
					        		<td style="text-align:center;">
                                        <span>${activity.activityNo}</span>
					        		</td>
					        		<td>
					        			<span>${activity.activityName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>从&nbsp;${activity.startDate}</span>
					        			<span>到&nbsp;${activity.endDate}</span>
					        		</td>
					        		<td>
					        			<span>${activity.promotionSkuName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<c:choose>
                                            <c:when test="${activity.isBegin == '0'}">
                                                <span style="color: #ecb010;"><b>待上线</b></span>
                                            </c:when>
                                            <c:when test="${activity.isBegin == '1'}">
                                                <span style="color: #ea918c;"> <b>已上线</b> </span>
                                            </c:when>
                                            <c:when test="${activity.isBegin == '2'}">
                                                <span style="color: #999999;"> <b>已下线</b> </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span style="color: red;"><b>${activity.isBegin}</b></span>
                                            </c:otherwise>
                                        </c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(activity.createTime, 0, 19)}</span>
					        		</td>
					        		<td data-key="${activity.activityKey}" style="text-align: center;">
					        			<c:if test="${currentUser.roleKey ne '4'}">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        			</c:if>
					        			<c:if test="${currentUser.roleKey eq '4'}">
						        			<a class="btn btn-xs view btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;查 看</a>
					        			</c:if>
				        				<a class="btn btn-xs rule btn-brownness"> <i class="iconfont icon-xiugai"></i>&nbsp;
					        			<c:choose>
					        				<c:when test="${activity.moneyConfigFlag == '0' and currentUser.roleKey ne '4'}">配置规则</c:when>
					        				<c:when test="${activity.moneyConfigFlag == '1' and currentUser.roleKey ne '4'}">修改规则</c:when>
					        				<c:otherwise>查看规则</c:otherwise>
					        			</c:choose>
				        				</a>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="<c:choose><c:when test="${currentUser.roleKey ne '4'}">8</c:when><c:otherwise>7</c:otherwise></c:choose>"><span>查无数据！</span></td>
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
