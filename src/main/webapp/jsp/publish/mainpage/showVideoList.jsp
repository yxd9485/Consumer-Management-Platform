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
	  <link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	  <script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script>
		$(function(){
			$.fn.confirms = function(content, callBack,btnCancelCallBack) {
				$("#modalConfirmDlg .modal-title").text("提示信息");
				$("#modalConfirmDlg .modal-body p").html(content);
				$("#modalConfirmDlg .modal-body p").css("display", "");
				$("#modalConfirmDlg #btnCancel").css("display", "");
				$("#modalConfirmDlg #btnSure").text("确定");
				$("#modalConfirmDlg #btnSure").unbind("click");
				$("#modalConfirmDlg #btnSure").on("click", function() {
					$(this).unbind("click");
					callBack();
				});
				$("#modalConfirmDlg #btnCancel").on('click', function () {
					$(this).unbind("click");
					btnCancelCallBack();
				});

				// 延迟100毫秒定位弹框的位置
				setTimeout(function(){
					$("#modalConfirmDlg").modal("show");
					var dialogH = $("#modalConfirmDlg").height();
					if (dialogH > screen.height) {
						if ($("#mainFrame", window.parent.parent.document).size() > 0) {
							$("#modalConfirmDlg .modal-content").css("top", ($("#mainFrame", window.parent.parent.document).contents().scrollTop() + screen.height * 0.0) + "px");
						}
					}
				}, 100);
			}
			// 新增
			$("#addItem").click(function(){
				var url = "<%=cpath%>/actVideo/showVideoAdd.do";
				$("form").attr("action", url);
				$("form").submit();
			});
			// 编辑
			$("a.edit").off();
			$("a.edit").on("click", function(){
				var actRuleKey = $(this).parents("td").data("key");
				var url = "<%=cpath%>/actVideo/showVideoEdit.do?videoKey="+actRuleKey;
				$("form").attr("action", url);
				$("form").submit();
			});
			// 删除
			$("a.delete").off();
			$("a.delete").on("click", function(){
				var actRuleKey = $(this).parents("td").data("key");
				$.fn.confirm("确认删除",function () {
					var url = "<%=cpath%>/actVideo/delete.do?videoKey="+actRuleKey;
					$("form").attr("action", url);
					$("form").submit();
				})
			});


			// 初始化模板状态显示样式
			$(".showFlag").bootstrapSwitch({
				onSwitchChange:function(event,state) {
					var key = $(this).parents("td").data("key");

					if(state==true){
						$("input:hidden[name='showFlag']").val("1");
						$.fn.confirms("视频展示状态变为‘是’后,其他展示视频状态都会变为’否‘", function() {
							updateShowFlag(key, '1');
							// $("a.rule").trigger("click");
							<%--var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=7";--%>
							<%--$("form").attr("action", url);--%>
							<%--$("form").submit();--%>
						},function () {
							$("button.btn-primary").trigger("click");
						});
					}else{
						$("input:hidden[name='showFlag']").val("0");
						$.fn.confirms("视频展示状态变为‘否’后,将不展示此视频", function() {
							updateShowFlag(key, '0');
							// $("a.rule").trigger("click");
							<%--var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=7";--%>
							<%--$("form").attr("action", url);--%>
							<%--$("form").submit();--%>
						},function () {
							$("button.btn-primary").trigger("click");
						});
					}

					<%--$.ajax({--%>
					<%--	url : "${basePath}/activateRedEnvelopeRuleCog/checkRule.do",--%>
					<%--	data:{--%>
					<%--		"ruleKey":key--%>
					<%--	},--%>
					<%--	type : "POST",--%>
					<%--	dataType : "json",--%>
					<%--	async : false,--%>
					<%--	beforeSend: appendVjfSessionId,--%>
					<%--	success: function (data) {--%>
					<%--		if (data == "0") {--%>
					<%--			$.fn.confirms("中出规则未配置！请配置完成后修改!", function() {--%>
					<%--				// $("a.rule").trigger("click");--%>
					<%--				var url = "<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vcodeActivityKey=" + key + "&activityType=7";--%>
					<%--				$("form").attr("action", url);--%>
					<%--				$("form").submit();--%>
					<%--			},function () {--%>
					<%--				$("button.btn-primary").trigger("click");--%>
					<%--			});--%>
					<%--			return;--%>
					<%--		}--%>
					<%--		if(state==true){--%>
					<%--			$("input:hidden[name='showFlag']").val("1");--%>
					<%--		}else{--%>
					<%--			$("input:hidden[name='showFlag']").val("0");--%>
					<%--		}--%>
					<%--		updateStateFlag(key);--%>
					<%--	}--%>
					<%--});--%>
				}
			});
        });


		function updateShowFlag(key,showFlag) {
			$.ajax({
				url : "${basePath}/actVideo/updateShowFlag.do",
				data:{
					"videoKey":key,
					"showFlag":showFlag
				},
				type : "POST",
				dataType : "json",
				async : true,
				beforeSend: appendVjfSessionId,
				success: function (data) {
					$.fn.alert("展示状态修改成功", function () {
						$("button.btn-primary").trigger("click")
					})
				}
			});
		}
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
							<i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新建视频
						</a>
					</div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
	            <!--查询url  -->
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/actVideo/showVideoList.do">
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
									<input name="keyword" class="form-control input-width-larger keyword" autocomplete="off" maxlength="100"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">状态：</label>
                                  	<select class="form-control input-width-larger search" name="status" autocomplete="off" >
										<option value="">全部</option>
										<option value="1">已上线</option>
										<option value="0">已下线</option>
										<option value="2">待上线</option>
									</select>
                                </div>

								<div class="search-item">
									<label class="control-label">展示时间：</label>
									<input name=startTime id="showStartTime" class="form-control input-width-medium Wdate search-date"
										   tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'showEndTime\')}'})" />
									<label class="">-</label>
									<input name="endTime" id="showEndTime" class="form-control input-width-medium Wdate sufTime search-date"
										   tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'showStartTime\')}'})" />
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
					            	<th style="width:10%;">标题</th>
					            	<th style="width:4%;">状态</th>
					            	<th style="width:16%;">展示时间</th>
					            	<th style="width:4%;" >是否展示</th>
					            	<th style="width:7%;">最后修改人</th>
					            	<th style="width:7%;">修改时间</th>
					            	<th style="width:6%;">操作</th>
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
					        			<span>${item.title}</span>
					        		</td>
                                     <td style="text-align:center;">
										 <span>${item.status}</span>
									 </td>
                                    <td style="text-align:center;">
                                        <span>${item.startTime} - ${item.endTime}</span>
                                    </td>
                                    <td style="text-align:center;" data-key="${item.videoKey}">
										<input name="showFlag" type="hidden" class="form-control" value="${item.showFlag}" />
										<input type="checkbox"  class="form-control showFlag" <c:if test="${item.showFlag eq '1'}">checked</c:if> data-size="small" data-on-text="是" data-off-text="否" data-on-color="success" data-off-color="warning"/>
									</td>
                                    <td style="text-align:center;">
                                        <span>${item.updateUser}</span>
                                    </td>
                                    <td style="text-align:center;">
                                        <span>${item.modTime}</span>
                                    </td>
					        		<td data-key="${item.videoKey}" style="text-align: center;">
					        		         <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
					        		         <a class="btn btn-xs delete btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
					        	</tr>
					        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
					        	<tr >
					        		<td colspan="18"><span>查无数据！</span></td>
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
