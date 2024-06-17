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
				var url = "<%=cpath%>/adPub/showAdUpAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/adPub/showAdUpEdit.do?adUpKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 删除
			$("a.del").off();
			$("a.del").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/adPub/adUpDelete.do?adPubKey="+key;
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
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">发布查询</span></div>
                   <div class="col-md-10 text-right">
                   <!-- 用户key不为空 ne -->
                           <a id="addItem" class="btn btn-blue">
                               <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新建广告图
                           </a>
                   </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
	            <!--查询url  -->
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/adPub/showAdUpList.do">
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
			            <!--搜索栏  -->
		                    <div class="form-group little_distance search" style="line-height: 35px;">
		                         <div class="search-item">
									<label class="control-label">按关键词查询：</label>
									<input name="keyword" class="form-control input-width-larger keyword"  placeholder = "请输入SKU/标题/最后修改人" autocomplete="off" maxlength="100"/>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">状态：</label>
	                                <select name="picStatus" class="form-control input-width-larger search" autocomplete="off" >
										<option value="">全部</option>
										<option value="0">待发布</option>
										<option value="1">已发布</option>
										<option value="2">已过期</option>
	                                </select>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">展示时间：</label>
                                    <input name="stGmt" id="stGmt" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endGmt\')}'})" />
                                    <label class="">-</label>
                                    <input name="endGmt" id="endGmt" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'stGmt\')}'})" />
                                </div>
                                <div class="search-item">
                                    <label class="control-label">最后修改时间：</label>
                                    <input name=modStGmt id="modStGmt" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'modEndGmt\')}'})" />
                                    <label class="">-</label>
                                    <input name="modEndGmt" id="modEndGmt" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'modStGmt\')}'})" />
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">发布列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <input type="hidden" id="screenWidth1366" value="5,9,14,9,13,9,9,9,9,6,9" />
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:18%;">SKU</th>
					            	<th style="width:15%;">标题</th>
					            	<th style="width:5%;" >状态</th>
									<th style="width:6%;" >启用状态</th>
					            	<th style="width:12%;">展示时间</th>
									<th style="width:6%;" >弹窗位置</th>
									<th style="width:8%;">弹窗关闭方式</th>
									<th style="width:4%;">排序</th>
									<th style="width:4%;">弹窗分组</th>
					            	<th style="width:7%;">最后修改人</th>
					            	<th style="width:8%;">最后修改时间</th>
					            	<th style="width:7%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<!--gt大于 lt小于-->
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
                                        <span>${idx.count}</span>
                                    </td>
					        		<td style="text-align:center;">
                                        <span>${item.skuNm}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.title}</span>
					        		</td>
                                    <td style="text-align:center;">
                                         <c:choose>
					        		         <c:when test="${item.picStatus eq '0'}">待发布</c:when>
					        		         <c:when test="${item.picStatus eq '1'}">已发布</c:when>
											 <c:when test="${item.picStatus eq '2'}">已过期</c:when>
					        		     </c:choose>
                                    </td>
									<td style="text-align:center;">
										<c:choose>
											<c:when test="${item.pubStat eq '0'}">未启用</c:when>
											<c:when test="${item.pubStat eq '1'}">启用</c:when>
										</c:choose>
									</td>
                                     <td style="text-align:center;">
                                        <span>${fn:substring(item.stGmt, 0, 10)} ${fn:substring(item.stGmt, 11, 19)} <br/> —— <br/> ${fn:substring(item.endGmt, 0, 10)} ${fn:substring(item.endGmt, 11, 19)}</span>
                                     </td>
									<td style="text-align:center;">
										<c:choose>
											<c:when test="${item.adLoc eq '1'}">拆开前</c:when>
											<c:when test="${item.adLoc eq '2'}">拆开后</c:when>
											<c:when test="${item.adLoc eq '3'}">兑付后</c:when>
											<c:when test="${item.adLoc eq '4'}">小程序商城首页</c:when>
										</c:choose>
									</td>
									<td style="text-align:center;">
										<c:choose>
											<c:when test="${item.closeType eq '1'}">手动关闭</c:when>
											<c:when test="${item.closeType eq '2'}">自动关闭${item.closeTime}秒</c:when>
										</c:choose>
									</td>
									<td style="text-align:center;">
										<span>${item.sequenceno}</span>
									</td>
									<td style="text-align:center;">
										<span>${item.popGroupName}</span>
									</td>
                                    <td style="text-align:center;">
                                        <span>${item.modUser}</span>
                                    </td>
                                    <td style="text-align:center;">
                                        <span>${fn:substring(item.modGmt, 0, 10)} <br/> ${fn:substring(item.modGmt, 11, 19)}</span>
                                    </td>
					        		<td data-key="${item.adPubKey}" style="text-align: center;">
						        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
						        			<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr>
					        		<td colspan="13"><span>查无数据！</span></td>
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
