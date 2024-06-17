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
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=1"></script>
	
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
       		<li class="current"><a> Popss活动管理</a></li>
       		<li class="current"><a> 操作日志</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                    	<div class="row">
                            <div class="dataTables_header clearfix">
			    				<form class="listForm" method="post"
			    					action="<%=cpath%>/vpsWechatMovementActivityBaseInfo/showPopssActivityInfoList.do">
                            		<input type="hidden" class="tableTotalCount" value="${showCount}" />
                            		<input type="hidden" class="tableStartIndex" value="${startIndex}" />
                            		<input type="hidden" class="tablePerPage" value="${countPerPage}" />
                            		<input type="hidden" class="tableCurPage" value="${currentPage}" />
                            		<input type="hidden" name="queryParam" value="${queryParam}" />
                            		<input type="hidden" name="pageParam" value="${pageParam}" />
			    				</form>
                            </div>
                        </div>
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th>操作时间</th>
					            	<th>操作手机号</th>
					            	<th>积分单价</th>
					            	<th>关注得积分</th>
					            	<!-- 
					            	<th>完善信息得积分</th>
					            	<th>邀请好友得积分</th>
					            	<th>邀请人数上限</th> 
					            	-->
					            	<th>参与活动支付积分</th>
					            	<th>参与周赛支付积分</th>
					            	<th>参与人数上限</th>
					            	<th>达标步数</th>
					            	<th>周达标金额</th>
					            	<th>虚拟倍数</th>
					            	<th>活动时间</th>
					            	<th>是否设置兑付门槛</th>
					            	<th>兑付设置</th>
					            	
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(popssInfoList) gt 0}">
					        	<c:forEach items="${popssInfoList}" var="activity" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;"><span>${idx.index + 1}</span></td>
					        		<td style="text-align:center;"><span>${activity.createTime}</span></td>
					        		<td style="text-align:center;"><span>${activity.phoneNum}</span></td>
					        		<td style="text-align:center;"><span>${activity.currencyPrice}</span></td>
					        		<td style="text-align:center;"><span>${activity.attentionVpoints}</span></td>
					        		<%-- 
					        		<td style="text-align:center;"><span>${activity.finishUserInfoVpoints}</span></td>
					        		<td style="text-align:center;"><span>${activity.inviteFriendsVpoints}</span></td>
					        		<td style="text-align:center;"><span>${activity.inviteFriendsLimit}</span></td> 
					        		--%>
					        		<td style="text-align:center;"><span>${activity.activityPayVpoints}</span></td>
					        		<td style="text-align:center;"><span>${activity.activityWeekPayVpoints}</span></td>
					        		<td style="text-align:center;"><span>${activity.activityMaxnum}</span></td>
					        		<td style="text-align:center;"><span>${activity.stepLimit}</span></td>
					        		<td style="text-align:center;"><span>${activity.weeksReachMoney}</span></td>
					        		<td style="text-align:center;"><span>${activity.activityMagnification}</span></td>
					        		<td style="text-align:center;"><span>${activity.startTime} - ${activity.endTime}</span></td>
					        		<td style="text-align:center;"><span>${activity.isExtractLimit eq '0' ? '否' : '是'}</span></td>
					        		<td style="text-align:center;"><span>${activity.extractLimit}</span></td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="14"><span>查无数据！</span></td>
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
