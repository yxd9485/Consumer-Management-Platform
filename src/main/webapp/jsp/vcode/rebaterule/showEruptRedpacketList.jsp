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
	<script type="text/javascript">
		$(function(){
			// 返回
			$("#showRebateRuleList").click(function(){
				var vcodeActivityKey = $("#vcodeActivityKey").val();
				var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vcodeActivityKey=" + vcodeActivityKey;
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
    <div class="container" style="padding: 0px;">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                    	<div class="row">
                            <div class="dataTables_header clearfix">
                            	<div class="col-md-12 text-right" >
							  		<a id="showRebateRuleList" class="btn btn-primary btn-default">返 回</a>
							  	</div>
                            </div>
                        </div>
                        <form class="listForm" method="post" action="<%=cpath%>/vcodeActivityRebateRule/queryEruptRedpacketList.do">
                        <input type="hidden" name="rebateRuleKey" value="${rebateRuleKey}" />
                    	<input type="hidden" id="vcodeActivityKey" value="${vcodeActivityKey}" />
                        <input type="hidden" name="activityType" value="${activityType}" />
                    	<input type="hidden" class="tableTotalCount" value="${showCount}" />
                   		<input type="hidden" class="tableStartIndex" value="${startIndex}" />
                   		<input type="hidden" class="tablePerPage" value="${countPerPage}" />
                   		<input type="hidden" class="tableCurPage" value="${currentPage}" />
                   		<input type="hidden" name="queryParam" value="${queryParam}" />
                   		<input type="hidden" name="pageParam" value="${pageParam}" />
                   		</form>
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:15%;">中奖时间</th>
					            	<th style="width:10%;">中奖金额</th>
					            	<th style="width:10%;">中奖积分</th>
					            	<th style="width:16%;">中奖奖项</th>
					            	<th style="width:15%;">用户昵称</th>
					            	<th style="width:10%;">省</th>
					            	<th style="width:10%;">市</th>
					            	<th style="width:10%;">区</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(packsRecordList) gt 0}">
					        	<c:forEach items="${packsRecordList}" var="packsRecord" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td align="center">
					        			<span>${packsRecord.earnTime}</span>
					        		</td>
					        		<td  align="center">
					        			<span><fmt:formatNumber value="${packsRecord.earnMoney}" pattern="#,##0.00#" /></span>
					        		</td>
					        		<td  align="center">
					        			<span>${packsRecord.earnVpoints}</span>
					        		</td>
					        		<td  align="center">
					        			<span>${packsRecord.earnPrizeName}</span>
					        		</td>
					        		<td>
					        			<span>${packsRecord.nickName }</span>
					        		</td>
					        		<td align="center">
					        			<span>${packsRecord.province}</span>
					        		</td>
					        		<td align="center">
					        			<span>${packsRecord.city}</span>
					        		</td>
					        		<td align="center">
					        			<span>${packsRecord.county}</span>
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
	</div>
  </body>
</html>
