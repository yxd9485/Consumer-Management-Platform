<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%> 
<!DOCTYPE html>
<% String cpath = request.getContextPath();
String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
String pathPrefix = cpath + "/" + imagePathPrx;
String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
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
				var url = "<%=cpath%>/vcodeFactory/showVcodeFactoryAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeFactory/showVcodeFactoryEdit.do?Key="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeFactory/doVcodeFactoryDelete.do?Key="+key;
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
        	<li class="current"><a> 基础配置</a></li>
            <li class="current"><a title="">赋码厂列表</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">赋码厂查询</span></div>
	               <div class="col-md-10 text-right">
	                       <a id="addActivity" class="btn btn-blue">
	                           <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新 增
	                       </a>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/vcodeFactory/showVcodeFactoryList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
	                <div class="row">
			            <div class="col-md-12 ">
                            <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
			                        <label class="control-label">赋码厂名称：</label>
			                        <input name="factoryName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                               
                                <div class="search-item">
	                                <label class="control-label">服务的省区：</label>
	                               <select name="projectServer" tag="validate" class="form-control required input-width-large search" >
									    	<option value="">请选择</option>
	                                        <c:forEach items="${projectServerLst}" var="item">
	                                            <option value="${item.projectServerName}">${item.serverName}</option>
	                                        </c:forEach>
									</select>
                                </div>
                                
                                <div class="search-item">
	                                <label class="control-label">赋码厂类型：</label>
	                                 <select name="factoryType" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option value="">全部</option>
										<c:choose>
											<c:when test="${!fn:startsWith(currentUser.projectServerName, 'mengniu')}">
												<option value="1">瓶盖厂</option>
												<option value="2">拉环厂</option>
												<option value="3">纸箱厂</option>
											</c:when>
											<c:otherwise>
												<option value="4">支码厂</option>
												<option value="3">纸箱厂</option>
											</c:otherwise>
										</c:choose>
	                                </select>
                                </div>
                                
                                 <div class="search-item">
	                                <label class="control-label">服务的工厂：</label>
	                               <select name="serverWinery" tag="validate" class="form-control required input-width-large search" >
									    	<option value="">请选择</option>
	                                        <c:forEach items="${wineryLst}" var="item">
	                                            <option value="${item.id}">${item.wineryName}</option>
	                                        </c:forEach>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">赋码厂列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:5%;">序号</th>
					            	<th style="width:10%;">名称</th>
					            	<th style="width:5%;">类型</th>
					            	<th style="width:5%;">通道数</th>
					            	<th style="width:20%;">服务的省区</th>
					            	<th style="width:20%;">服务的工厂</th>
					            	<th style="width:12%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="factory" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${factory.factoryName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<c:if test="${factory.factoryType eq '1'}">
		                                	<span>瓶盖厂</span>
		                                </c:if>
		                                <c:if test="${factory.factoryType eq '2'}">
		                                	<span>拉环厂</span>
		                                </c:if>
		                                <c:if test="${factory.factoryType eq '3'}">
		                                	<span>纸箱厂</span>
		                                </c:if>
					        			
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${factory.channelCount}</span>
					        		</td>
					        		<td>
					        			<span>${factory.projectServerName}</span>
					        		</td>
					        		
					        		<td>
					        			<span>${factory.serverWineryName}</span>
					        		</td>
					        		<td data-key="${factory.id}" style="text-align: center;">
					        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
					        		</td>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="7"><span>查无数据！</span></td>
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
