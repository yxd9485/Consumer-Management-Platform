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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=3"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	
  </head>
  
  <script type="text/javascript">
		$(function(){
			// 查询结果导出
            $("a.exportDate").on("click", function(){
            	$("form").attr("action","<%=cpath%>/vpointsExchange/exportOrderRecord.do");
                $("form").submit();
                
             	// 还原查询action
                $("form").attr("action","<%=cpath%>/biddingPeriods/showBiddingPeriodsList.do");
            });
			
			// 查询中奖信息
			$("a.prizeView").on("click",function(){
				var key = $(this).parents("td").data("key");
				$("form").attr("action","<%=cpath%>/biddingPeriods/findMajorInfoByExchangeId.do?exchangeId=" + key);
                $("form").submit();
                
             	// 还原查询action
                $("form").attr("action","<%=cpath%>/biddingPeriods/showBiddingPeriodsList.do");
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
  <body>
	<form class="listForm" method="post" action="<%=cpath%>/biddingPeriods/showBiddingPeriodsList.do">
		  <input type="hidden" class="tableTotalCount" value="${showCount}" />
          <input type="hidden" class="tableStartIndex" value="${startIndex}" />
          <input type="hidden" class="tablePerPage" value="${countPerPage}" />
          <input type="hidden" class="tableCurPage" value="${currentPage}" />
          <input type="hidden" class="tableOrderCol" value="${orderCol}" />
          <input type="hidden" class="tableOrderType" value="${orderType}" />
          <input type="hidden" name="queryParam" value="${queryParam}" />
          <input type="hidden" name="pageParam" value="${pageParam}" />
          <input type="hidden" name="vjfSessionId" value="${vjfSessionId }">
		  <input type="hidden" name="activityKey" value="${activityKey }">
		  <input type="hidden" name="tabsFlag" value="3">
	</form>
     <div class="col-md-12 tabbable tabbable-custom">
         <div class="widget box" id="tab_1_1">
             <div class="row">
                <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">数据列表</span></div>
                <div class="col-md-10 text-right" style="display: none;">
	                 <a class="btn btn-blue exportDate">
	                 		<i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 下载数据
	                 </a>
	          	</div>
             </div>
             <div class="widget-content">
                 <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                     <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"  id="dataTable_data">
                         <thead>
<!--                                 待发货 -->
                            <tr>
                                <th style="width:13%;" data-ordercol="e.exchange_id">期数</th>
                                <th style="width:13%;" data-ordercol="b.brand_name">预约人数</th>
                                <th style="width:13%;" data-ordercol="g.goods_name">参与人数</th>
                                <th style="width:13%;" data-ordercol="g.goods_name">擂台时间</th>
                                <th style="width:13%;" data-ordercol="g.goods_name">状态</th>
                                <th style="width:14%;" data-ordercol="e.user_name">操作</th>
                            </tr>
		        		</thead>
		        	<tbody class="contentBody">
			        	<c:choose>
				        	<c:when test="${fn:length(resultList) gt 0}">
				        	<c:forEach items="${resultList}" var="item" varStatus="idx">
				        	<tr>
				        		<td style="text-align:center;">
		                            <span>${item.periodsNumber}</span>
				        		</td>
				        		<td style="text-align:center;">
				        			<span>${item.subscribeNumber}</span>
				        		</td>
				        		<td style="text-align:center;">
				        			<span>${item.participateNumber}</span>
				        		</td>
				        		<td style="text-align:center;">
				        			<span>${fn:split(item.createTime, ' ')[0]}</span>
				        		</td>
		                        <td style="text-align:center;">
		                        	<c:choose>
		                        		<c:when test="${item.status eq '0'}">进行中</c:when>
		                        		<c:when test="${item.status eq '1'}">已结束</c:when>
		                        		<c:otherwise>-</c:otherwise>
		                        	</c:choose>
				        		</td>
				        		<td style="text-align:center;" data-key="${item.exchangeId}">
				        			<a class="btn btn-xs prizeView btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;中奖数据</a>
				        		</td>
				        	</tr>
				        	</c:forEach>
				        	</c:when>
				        	<c:otherwise>
				        	<tr>
				        	  <td colspan="6"><span>查无数据！</span></td>
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
  </body>
</html>
