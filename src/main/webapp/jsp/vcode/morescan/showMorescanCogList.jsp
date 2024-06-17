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
				var url = "<%=cpath%>/activityMorescanCog/showMorescanCogAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityMorescanCog/showMorescanCogEdit.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityMorescanCog/showMorescanCogView.do?infoKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/activityMorescanCog/doMorescanCogDelete.do?infoKey="+key;
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
        	<li class="current"><a> 一码多扫</a></li>
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
	        <a href="<%=cpath%>/activityMorescanCog/showMorescanCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=1" 
	             class="btn <c:if test="${tabsFlag eq '1'}">btn-blue</c:if>">生效规则</a>
	        <a href="<%=cpath%>/activityMorescanCog/showMorescanCogList.do?vjfSessionId=${vjfSessionId}&tabsFlag=2" 
	             class="btn <c:if test="${tabsFlag eq '2'}">btn-blue</c:if>">无效规则</a>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">一码多扫查询</span></div>
                   <div class="col-md-10 text-right">
                       <c:if test="${currentUser.roleKey ne '4'}">
                           <a id="addItem" class="btn btn-blue">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增一码多扫
                           </a>
                       </c:if>
                   </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/activityMorescanCog/showMorescanCogList.do">
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
			                        <input name="morescanNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">规则名称：</label>
			                        <input name="morescanName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
                                <div class="search-item">
			                        <label class="control-label">中出模式：</label>
	                                <select name="prizePattern" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option style="padding: 20px;" value="">全部</option>
	                                    <option value="1">随机金额</option>
	                                    <option value="2">固定金额</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">一码多扫列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,19,9,14,9,9,9,6,9,9" />
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:9%;" data-ordercol="a.morescan_no">规则编号</th>
					            	<th style="width:20%;" data-ordercol="a.morescan_name">规则名称</th>
					            	<th style="width:10%;" data-ordercol="a.min_valid_date">规则时间</th>
					            	<th style="width:12%;" data-ordercol="a.vcodeActivityName">联接活动名称</th>
					            	<th style="width:6%;" data-ordercol="a.isBegin">活动状态</th>
					            	<th style="width:6%;" data-ordercol="a.prize_pattern">中出模式</th>
									<c:if test="${projectName == '1'}"> <th style="width:6%;" >扫码角色</th> </c:if>
					            	<th style="width:6%;" data-ordercol="a.valid_count">扫码次数</th>
					            	<th style="width:5%;" data-ordercol="a.status">状态</th>
					            	<th style="width:10%;" data-ordercol="a.create_time" data-orderdef>创建时间</th>
					            	<th style="width:12%;">操作</th>
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
                                        <span>${item.morescanNo}</span>
					        		</td>
					        		<td>
					        			<span>${item.morescanName}</span>
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
					        			<span>
					        			<c:choose>
					        				<c:when test="${item.prizePattern eq '1'}">随机奖项</c:when>
					        				<c:otherwise>固定奖项</c:otherwise>
					        			</c:choose>
					        			</span>
					        		</td>
									<c:if test="${projectName == '1'}">
										<td style="text-align:center;">
					        			<span>
					        			<c:choose>
											<c:when test="${item.roleInfo == null}">按次数中奖</c:when>
											<c:otherwise>
												<c:forEach items="${roleInfoAll }" var="roleItem">
													<c:forEach items="${fn:split(item.roleInfo, ',') }" var="role">
	                                    			<div>
	                                    				<c:if test="${fn:split(roleItem, ':')[0] eq role}">
															<span>${fn:split(roleItem, ':')[1]}</span>
														</c:if>
		                                        	</div>
													</c:forEach>
												</c:forEach>
											</c:otherwise>
										</c:choose>
					        			</span>
										</td>
									</c:if>
					        		<td style="text-align:center;">
					        			<span>${item.validCount}</span>
					        		</td>
					        		<td style="text-align:center;">
					        		     <c:choose>
					        		         <c:when test="${item.status eq '0'}">停用</c:when>
					        		         <c:when test="${item.status eq '1'}">启用</c:when>
					        		     </c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${fn:substring(item.createTime, 0, 19)}</span>
					        		</td>
						        	<td data-key="${item.infoKey}" style="text-align: center;">
					        		    <c:if test="${tabsFlag eq '1' and currentUser.roleKey ne '4'}">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
						        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
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
						        	<td colspan="11"><span>查无数据！</span></td>
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
