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
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vcodeActivityKey="+key;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			
			// 规则
			$("a.rule").off();
			$("a.rule").on("click", function(){
				var key = $(this).parents("td").data("key");
				var activityVersion = $(this).data("key");
				var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleList.do?vcodeActivityKey=" + key + "&activityVersion=" + activityVersion;
				$("form").attr("action", url);
				$("form").submit(); 
			});
			
			// 查看
			$("a.view").off();
			$("a.view").on("click", function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/vcodeActivity/showVcodeActivityView.do?vcodeActivityKey="+key;
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
        	<c:if test="${menuType eq 'activity'}">
        		<li class="current"><a> 活动管理</a></li>
        		<li class="current">
                <c:choose>
                    <c:when test="${activityType == '1'}">一罐一码活动</c:when>
                    <c:when test="${activityType == '2'}">一瓶一码活动</c:when>
                </c:choose>
            </li>
        	</c:if>
        	<c:if test="${menuType eq 'rule'}">
        		<li class="current"><a> 规则管理</a></li>
        		<li class="current">
                <c:choose>
                    <c:when test="${activityType == '1'}">一罐一码规则</c:when>
                    <c:when test="${activityType == '2'}">一瓶一码规则</c:when>
                </c:choose>
            </li>
        	</c:if>
        	<c:if test="${menuType eq 'qrcode'}">
        		<li class="current"><a> 码源管理</a></li>
        		<li class="current">
                <c:choose>
                    <c:when test="${activityType == '1'}">一罐一码码源</c:when>
                    <c:when test="${activityType == '2'}">一瓶一码码源</c:when>
                </c:choose>
            </li>
        	</c:if>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box" id="tab_1_1">
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <div class="row">
                            <div class="dataTables_header clearfix">
                            	<c:if test="${currentUser.roleKey ne '4' and menuType eq 'activity'}">
	                            	<div class="button_nav col-md-12">
	                            	  <a id="addActivity" class="btn btn-blue">
	                            	      <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp;
	                            	          新 增
	                            	  </a>
	                            	</div>
                            	</c:if>
			    				<form class="listForm" method="post"
			    					action="<%=cpath%>/vcodeActivity/showVcodeActivityList.do">
                            		<input type="hidden" class="tableTotalCount" value="${showCount}" />
                            		<input type="hidden" class="tableStartIndex" value="${startIndex}" />
                            		<input type="hidden" class="tablePerPage" value="${countPerPage}" />
                            		<input type="hidden" class="tableCurPage" value="${currentPage}" />
                            		<input type="hidden" name="queryParam" value="${queryParam}" />
                            		<input type="hidden" name="pageParam" value="${pageParam}" />
                            		<input type="hidden" name="activityType" value="${activityType}" />
                            		<input type="hidden" name="menuType" value="${menuType}" />
			    				</form>
                            </div>
                        </div>
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:4%;">序号</th>
					            	<th style="width:30%;">活动名称</th>
					            	<th style="width:15%;">活动时间</th>
					            	<th style="width:15%;">产品SKU</th>
					            	<th style="width:6%;display: none;">区域</th>
					            	<th style="width:8%;">是否一码多扫</th>
					            	<th style="width:8%;">中奖模式</th>
					            	<th style="width:8%;">有效扫码次数</th>
					            	<th style="width:10%;">状态</th>
					            	<th style="width:12%;">操作</th>
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
					        		<td>
					        			<span>${activity.vcodeActivityName}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>
						        			<fmt:parseDate value="${activity.startDate}" pattern="yyyy-MM-dd" var="startDate"/>
						        			<fmt:formatDate value="${startDate}" pattern="yyyy.MM.dd" />
					        			</span>
					        			<span>
					        				&nbsp;-&nbsp;
					        				<fmt:parseDate value="${activity.endDate}" pattern="yyyy-MM-dd" var="endDate"/>
						        			<fmt:formatDate value="${endDate}" pattern="yyyy.MM.dd" />
					        			</span>
					        		</td>
					        		<td>
					        			<div style="width:200px; white-space:nowrap;overflow:hidden;text-overflow:ellipsis; cursor: pointer;" title="${activity.skuName}">
					        				${activity.skuName}
					        			</div>
					        		</td>
					        		<td style="text-align:center; display: none;">
					        			<c:choose>
				        					<c:when test="${activity.activityVersion == '1'}">
				        						<span>广东省</span>
				        					</c:when>
				        					<c:when test="${activity.activityVersion == '2'}">
				        						<span>海南省</span>
				        					</c:when>
				        				</c:choose>
					        		</td>
					        		<td align="center">
					        			<span>
					        				<c:choose>
					        					<c:when test="${activity.isMoreSweep eq 0}">否</c:when>
					        					<c:otherwise>是</c:otherwise>
					        				</c:choose>
					        			</span>
					        		</td>
					        		<td>
					        			<span>
					        				<c:choose>
					        					<c:when test="${activity.prizePattern eq 1}">随机奖项</c:when>
					        					<c:otherwise>其他奖项</c:otherwise>
					        				</c:choose>
					        			</span>
					        		</td>
					        		<td align="center">
					        			<span>${activity.validCount}</span>
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
					        		<td data-key="${activity.vcodeActivityKey}" style="text-align: center;">
					        			<c:choose>
					        				<c:when test="${menuType eq 'activity'}">
							        			<c:if test="${currentUser.roleKey ne '4' and activity.vcodeActivityName ne '生成码源专属活动' and activity.vcodeActivityName ne '默认批次专属活动'}">
								        			<a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
							        			</c:if>
							        			<c:if test="${currentUser.roleKey eq '4'}">
								        			<a class="btn btn-xs view btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;查 看</a>
							        			</c:if>
					        				</c:when>
					        				<c:when test="${menuType eq 'rule'}">
					        					<c:if test="${activity.vcodeActivityName ne '生成码源专属活动' and activity.vcodeActivityName ne '默认批次专属活动'}">
							        				<a class="btn btn-xs rule btn-red" data-key="${activity.activityVersion}"><i class="iconfont icon-xiugai"></i>&nbsp;
								        			<c:choose>
								        				<c:when test="${activity.moneyConfigFlag == '0' and currentUser.roleKey ne '4'}">配置规则</c:when>
								        				<c:when test="${activity.moneyConfigFlag == '1' and currentUser.roleKey ne '4'}">修改规则</c:when>
								        				<c:otherwise>查看规则</c:otherwise>
								        			</c:choose>
							        				</a>
							        			</c:if>		
					        				</c:when>
					        				<c:when test="${menuType eq 'qrcode'}">
					        					<a class="btn btn-xs generate btn-red" href="<%=cpath%>/qrcodeBatchInfo/${activity.vcodeActivityKey}/showBatchInfoList.do?menuType=${menuType}">
					        						<i class="iconfont icon-erweima" style="font-size: 14px;"></i>&nbsp;
								        				<c:if test="${currentUser.roleKey eq '4'}">查看二维码</c:if>
								        				<c:if test="${currentUser.roleKey eq '1' or currentUser.roleKey ne '2'}">申请二维码</c:if>
					        					</a>
					        				</c:when>
					        			</c:choose>
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
                    <h6 class="edit_fail_lackFirstMoney">编辑失败：规则"${flagRuleErrMsg}"导入金额缺少首扫区间金额</h6>
                    <h6 class="edit_fail_lackFirstVpoints">编辑失败：规则"${flagRuleErrMsg}"导入积分缺少首扫区间积分</h6>
                    <h6 class="edit_fail_lackDoubtMoney">编辑失败：规则"${flagRuleErrMsg}"导入金额缺少可疑区间金额</h6>
                    <h6 class="edit_fail_lackDoubtVpoints">编辑失败：规则"${flagRuleErrMsg}"导入积分缺少可疑区间积分</h6>
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
