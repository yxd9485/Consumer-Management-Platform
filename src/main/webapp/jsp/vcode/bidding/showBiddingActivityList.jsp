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
				var url = "<%=cpath%>/biddingActivity/showBiddingActivityAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/biddingActivity/showBiddingActivityEdit.do?activityKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			
			// 数 据
			$("a.data").off();
			$("a.data").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/biddingActivity/showBiddingActivityData.do?activityKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				$.fn.confirm("确认删除吗？", function(){
	                $.ajax({
	                    type: "POST",
	                    url: "<%=cpath%>/biddingActivity/doBiddingActivityDel.do",
	                    async: false,
	                    data: {"activityKey":key},
	                    beforeSend:appendVjfSessionId,
	                    dataType: "json",
	                    success:  function(data){
	                    	if(data['errMsg'] == '删除成功'){
	                    		$.fn.alert(data['errMsg'], function(){
		                    		$("button.btn-primary").trigger("click");
	                            });
	                    	}else{
	                    		$.fn.alert(data['errMsg']);
	                    	}
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
            <li class="current"><a> 活动管理</a></li>
        	<li class="current"><a> 竞价活动配置</a></li>
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
	        <a href="<%=cpath%>/biddingActivity/showBiddingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=0" 
	             class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">待上线</a>
	        <a href="<%=cpath%>/biddingActivity/showBiddingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">已上线</a>
	        <a href="<%=cpath%>/biddingActivity/showBiddingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">已下线</a>
	        <a href="<%=cpath%>/biddingActivity/showBiddingActivityList.do?vjfSessionId=${vjfSessionId}&tabsFlag=3" 
	             class="btn <c:if test="${tabsFlag eq '3'}">btn-blue</c:if>">全部活动</a>
	    </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">一码双奖查询</span></div>
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
		            <form class="listForm" method="post" action="<%=cpath%>/biddingActivity/showBiddingActivityList.do">
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
	                                <div class="search-item">
				                        <label class="control-label">中出产品名称</label>
	                                    <select name="goodsId"  class="form-control input-width-larger search" autocomplete="off">
                                    		<option style="padding: 20px;" value="">全部</option>
	                                    	<c:forEach items="${goodsList }" var="item">
	                                    		<option value="${item.goodsId}">${item.goodsName }</option>
	                                    	</c:forEach>
	                                    </select>
                                    </div>
                                    <div class="search-item">
				                        <label class="control-label">擂台类型</label>
				                        <select name="activityType" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option value="">全部</option>
	                                        <option value="1">日擂台</option>
	                                        <option value="2">月擂台</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,10,15,10,9,9,10,8,8,9,12" />
                            <thead>
					            <tr>
                                    <th style="width:5%;">序号</th>
					            	<th style="width:10%;" data-ordercol="cog.activity_no">活动编号</th>
					            	<th style="width:15%;" data-ordercol="cog.activity_name">活动名称</th>
					            	<th style="width:9%;" data-ordercol="cog.activity_type">擂台类型</th>
					            	<th style="width:15%;" data-ordercol="cog.goods_name">中出产品名称</th>
					            	<th style="width:9%;" data-ordercol="cog.is_dedicated">是否酒水专场</th>
					            	<th style="width:12%;" data-ordercol="cog.start_date">擂台时间</th>
					            	<th style="width:10%;" data-ordercol="cog.create_time" data-orderdef>创建时间</th>
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
					        		<td style="text-align:center;">
					        			<span>${activity.activityName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<c:choose>
					        				<c:when test="${activity.activityType eq '1'}"><span>日擂台</span></c:when>
					        				<c:when test="${activity.activityType eq '2'}"><span>月擂台</span></c:when>
					        				<c:otherwise>-</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td>
					        			<span>${activity.goodsName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<c:choose>
					        				<c:when test="${activity.isDedicated eq '1'}"><span>是</span></c:when>
					        				<c:otherwise>否</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${activity.startDate}</span>
					        			<span>到&nbsp;${activity.endDate}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(activity.createTime, 0, 19)}</span>
					        		</td>
					        		<td data-key="${activity.activityKey}" style="text-align: center;">
					        			<c:if test="${currentUser.roleKey ne '4'}">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        			</c:if>
						        		<a class="btn btn-xs data btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;数 据</a>
						        		<c:if test="${activity.periodsCount eq 0 }">
						        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
						        		</c:if>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="9"><span>查无数据！</span></td>
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
