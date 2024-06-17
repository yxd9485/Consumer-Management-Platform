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
			
			var flag = "${flag}";
			if(flag != "") {
				$("div.modal-body ."+flag+"").show();
				$("div.modal-body :not(."+flag+")").hide();
				$("#myModal").modal("show");
			}
			
			// 新增
			$("#addActivity").click(function(){
				var url = "<%=cpath%>/vcodeTicketCog/showVcodeTicketCogAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeTicketCog/showVcodeTicketCogEdit.do?prizeType="+key;
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
        	<li class="current"><a> 虚拟券管理</a></li>
            <li class="current"><a title="">虚拟券类别列表</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="row">
                            <div class="dataTables_header clearfix">
                            	<c:if test="${currentUser.roleKey ne '4'}">
	                            	<div class="button_nav col-md-12">
	                            	  <a id="addActivity" class="btn btn-blue">
	                            	      <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;
	                            	          新 增
	                            	  </a>
	                            	</div>
                            	</c:if>
			    				<form class="listForm" method="post"
			    					action="<%=cpath%>/vcodeActivityTicket/showVcodeActivityList.do">
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
					            	<th style="width:5%;">序号</th>
					            	<th style="width:12%;">虚拟券编码</th>
					            	<th style="width:30%;">虚拟券名称</th>
					            	<th style="width:20%;">有效日期</th>
					            	<th style="width:8%;">券码个数</th>
					            	<th style="width:8%;">中出个数</th>
					            	<th style="width:17%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.prizeType}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.ticketName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>从&nbsp;${item.startDate}</span>
					        			<span>到&nbsp;${item.endDate}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.ticketNum}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.usedNum}</span>
					        		</td>
					        		<td data-key="${item.prizeType}" style="text-align: center;">
					        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="5"><span>查无数据！</span></td>
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
      				<h6 class="edit_nonuniqueness">导入的部分券码已存在</h6>
      				<h6 class="edit_invalidDate">当前活动日期与已存在活动有交差</h6>
                    <h6 class="edit_fail_lackFirstMoney">编辑失败,规则"${flagRuleErrMsg}"导入金额缺少首扫区间金额</h6>
                    <h6 class="edit_fail_lackDoubtMoney">编辑失败,规则"${flagRuleErrMsg}"导入金额缺少可疑区间金额</h6>
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
