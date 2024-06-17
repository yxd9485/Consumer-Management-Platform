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
			
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var state = $(this).parents("td").data("state");
				var value = $(this).parents("td").data("value");
				var tip = "";
				if(state == "0"){
					tip = "是否删除活动：\"" + value + "\"";
					state = "99";
				}else if(state == "1"){
					tip = "点击确认后，则会更新活动状态为“已处理”！";
					state = "2";
				} 
				$.fn.confirm(tip , function(){
	                $.ajax({
	                    type: "POST",
	                    url: "<%=cpath%>/marketingNode/doMarketingNodeEdit.do",
	                    async: false,
	                    data: {"nodeId":key, "state":state},
	                    beforeSend:appendVjfSessionId,
	                    dataType: "json",
	                    success:  function(data){
	                    	if(data == 'success'){
	                    		$.fn.alert("操作成功", function(){
		                    		$("button.btn-primary").trigger("click");
	                            });
	                    	}else{
	                    		$.fn.alert(data);
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
            <li class="current"><a> 提醒消息</a></li>
        	<li class="current"><a> 计划提醒</a></li>
        </ul>
    </div>
    <div class="row">
	    <div class="col-md-12 tabbable">
	        <a href="#" 
	             class="btn <c:if test="${tabsFlag eq '0'}">btn-blue</c:if>">计划提醒</a>
	    </div>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">计划查询</span></div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post" action="<%=cpath%>/marketingNode/showMarketingNodeList.do">
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
				                        <label class="control-label">计划名称：</label>
				                        <input name="plan_name" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
				                        <label class="control-label">流程名称：</label>
				                        <input name="task_name" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>
                                    <div class="search-item">
				                        <label class="control-label">活动名称：</label>
				                        <input name="data_value" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                    </div>

                                    <div class="search-item">
				                        <label class="control-label">活动状态：</label>
				                        <select name="state" class="form-control input-width-larger search" autocomplete="off" >
	                                        <option value="">全部</option>
	                                        <option value="0">待删除</option>
	                                        <option value="1">未配置</option>
	                                        <option value="2">待上线</option>
	                                        <option value="3">已上线</option>
	                                        <option value="4">完成且成功</option>
	                                        <option value="5">完成且失败</option>
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
					            	<th style="width:10%;" data-ordercol="cog.activity_no">计划名称</th>
					            	<th style="width:15%;" data-ordercol="cog.activity_name">流程名称</th>
					            	<th style="width:13%;" data-ordercol="cog.activity_type">活动类型</th>
					            	<th style="width:15%;" data-ordercol="cog.goods_name">活动标题</th>
					            	<th style="width:12%;" data-ordercol="cog.is_dedicated">活动时间</th>
					            	<th style="width:10%;" data-ordercol="cog.start_date">活动状态</th>
					            	<th style="width:10%;" data-ordercol="cog.create_time">备注</th>
					            	<th style="width:10%;">操作</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="item" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;">
					        			${idx.count }
					        		</td>
                                    <td style="text-align:center;">
                                        ${item.plan_name }
                                    </td>
					        		<td style="text-align:center;">
                                        ${item.task_name }
					        		</td>
					        		<td style="text-align:center;">
					        			<c:choose>
					        				<c:when test="${item.type eq '1'}"><span>触达节点：</span></c:when>
					        				<c:when test="${item.type eq '2'}"><span>活动节点：</span></c:when>
					        				<c:when test="${item.type eq '3'}"><span>自定义节点：</span></c:when>
					        				<c:otherwise>-</c:otherwise>
					        			</c:choose>
					        			<c:choose>
					        				<c:when test="${item.type eq '1'}">
					        					<c:choose>
													<c:when test="${item.data_type eq '1'}"><span>短信</span></c:when>
													<c:when test="${item.data_type eq '2'}"><span>公众号</span></c:when>
													<c:when test="${item.data_type eq '3'}"><span>企业微信</span></c:when>
													<c:when test="${item.data_type eq '4'}"><span>AI语音</span></c:when>
													<c:otherwise>其他</c:otherwise>
												</c:choose>
											</c:when>
					        				<c:when test="${item.type eq '2'}">
												<c:choose>
													<c:when test="${item.data_type eq '1'}"><span>扫点得</span></c:when>
													<c:when test="${item.data_type eq '2'}"><span>万能签到</span></c:when>
													<c:when test="${item.data_type eq '3'}"><span>捆绑升级</span></c:when>
													<c:when test="${item.data_type eq '4'}"><span>一码双奖</span></c:when>
													<c:when test="${item.data_type eq '5'}"><span>一码多扫</span></c:when>
													<c:when test="${item.data_type eq '6'}"><span>逢百规则</span></c:when>
													<c:when test="${item.data_type eq '7'}"><span>秒杀活动</span></c:when>
													<c:when test="${item.data_type eq '8'}"><span>逢尾规则</span></c:when>
													<c:when test="${item.data_type eq '9'}"><span>阶梯规则</span></c:when>
													<c:otherwise>其他</c:otherwise>
												</c:choose>
											</c:when>
					        				<c:when test="${item.type eq '3'}"><span>自定义节点</span></c:when>
					        				<c:otherwise>-</c:otherwise>
					        			</c:choose>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.data_value}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${item.date_start}</span>
					        			<span>到&nbsp;${item.date_end}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span></span>
					        			<c:choose>
											<c:when test="${item.state eq '0'}"><span>待删除</span></c:when>
											<c:when test="${item.state eq '1'}"><span>未配置</span></c:when>
											<c:when test="${item.state eq '2'}"><span>待上线</span></c:when>
											<c:when test="${item.state eq '3'}"><span>已上线</span></c:when>
											<c:when test="${item.state eq '4'}"><span>完成且成功</span></c:when>
											<c:when test="${item.state eq '5'}"><span>完成且失败</span></c:when>
											<c:when test="${item.state eq '99'}"><span>已删除</span></c:when>
											<c:otherwise>其他</c:otherwise>
										</c:choose>
					        		</td>
					        		<td style="text-align:center;">
                                        ${item.remark}
                                    </td>
					        		<td data-key="${item.id}" data-state="${item.state}" data-value="${item.data_value}" style="text-align: center;">
					        			<c:if test="${currentUser.roleKey ne '4'}">
					        				<c:choose>
											<c:when test="${item.state eq '1'}">
												<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;未处理
</a>
											</c:when>
											<c:when test="${item.state eq '0'}">
												<a class="btn btn-xs edit btn-red"><i class="iconfont icon-xiugai"></i>&nbsp;删 除</a>
											</c:when>
											<c:otherwise>已处理</c:otherwise>
										</c:choose>
						        			
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
