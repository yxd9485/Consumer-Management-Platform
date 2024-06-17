<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.dbt.framework.util.PropertiesUtil"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>

<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
	<title>V积分管理后台</title>
	<jsp:include page="/inc/Main.jsp"></jsp:include>
	
	<script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

	<script>
		$(function(){

			// 初始化省市县
            $("div.area").initZone("<%=cpath%>", true, "${areaCode}", true, false, false);

			
         	// 取消授权
			$("a.del").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/terminal/delPrizeTerminal.do?bigPrizeType=${prizeType}&terminalKey="+key;
				 $.fn.confirm("确定取消授权吗？", function(){
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});

			// 授权
			$("a.add").click(function(){
				var key = $(this).parents("td").data("key");
				var url = "<%=cpath%>/terminal/addPrizeTerminal.do?bigPrizeType=${prizeType}&terminalKey="+key;
				 $.fn.confirm("确定授权吗？", function(){
					$("form").attr("action", url);
					$("form").submit(); 
				 });
			});
       
			 // 批量删除
            $("#delItems").off();
            $("#delItems").on("click", function(){
            	var terminalNum = $("[name='itemCB']:checked").size();
            	if (terminalNum == 0) {
            		$.fn.alert("请选择要取消的授权店！");
            	}           	
            	var terminalKeyLst = "";
                $("[name='itemCB']:checked").each(function(idx, obj) {            	
                	terminalKeyLst = $(this).val() + "," + terminalKeyLst;
                })                
                if (terminalKeyLst != "") {
	                var url = "<%=cpath%>/terminal/delTerminalLst.do?bigPrizeType=${prizeType}&terminalKeyLst="+terminalKeyLst;
	                $.fn.confirm("确认取消这" + terminalNum + "个授权吗？", function(){
	                    $("form").attr("action", url);
	                    $("form").submit();
	                });
                }
                
            });
            
            
            // 批量授权
            $("#addtems").off();
            $("#addtems").on("click", function(){
            	var terminalNum = $("[name='itemCB']:checked").size();
            	if (terminalNum == 0) {
            		$.fn.alert("请选择要批量授权的店！");
            	}            	
            	var terminalKeyLst = "";
                $("[name='itemCB']:checked").each(function(idx, obj) {              	
                	terminalKeyLst = $(this).val() + "," + terminalKeyLst;
                })                
                if (terminalKeyLst != "") {
	                var url = "<%=cpath%>/terminal/addTerminalLst.do?bigPrizeType=${prizeType}&terminalKeyLst="+terminalKeyLst;
	                $.fn.confirm("确认授权" + terminalNum + "个店？", function(){
	                    $("form").attr("action", url);
	                    $("form").submit();
	                });
                }
                
            });


            // 全选
            $("#allCB").on("change", function(){
                if($(this).prop("checked")){
                    $("[name='itemCB']").prop("checked","checked");
                } else {
                    $("[name='itemCB']").prop("checked","");
                }
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
			<li class="current"><a> 实物奖</a></li>
		</ul>
	</div>
	<div class="row">
		<div class="col-md-12 tabbable tabbable-custom">
			<div class="widget box">
				<div class="row">
					<div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">实物奖查询</span></div>
					<div class="col-md-10 text-right">
						<c:choose>
						   <c:when test="${sqStatus eq '1'}">
						   		<a id="delItems" class="btn btn-blue">
	                           		<i class="iconfont icon-lajixiang" style="font-size: 12px;"></i>&nbsp; 批量关闭授权
	                       		</a>
						   </c:when>
						   <c:otherwise>
						   		<a id="addtems" class="btn btn-blue">
	                           		<i class="iconfont icon-lajixiang" style="font-size: 12px;"></i>&nbsp; 批量授权
	                       		</a>
						   </c:otherwise>						
						</c:choose>
					</div>
				</div>
				<div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
				<div class="widget-content form-inline">
					<form class="listForm" method="post"
						  action="<%=cpath%>/terminal/showPrizeTerminalList.do?">
						<input type="hidden" class="tableTotalCount" value="${showCount}" />
						<input type="hidden" class="tableStartIndex" value="${startIndex}" />
						<input type="hidden" class="tablePerPage" value="${countPerPage}" />
						<input type="hidden" class="tableCurPage" value="${currentPage}" />
						<input type="hidden" class="tableOrderCol" value="${orderCol}" />
						<input type="hidden" class="tableOrderType" value="${orderType}" />
						<input type="hidden" name="queryParam" value="${queryParam}" />
						<input type="hidden" name="pageParam" value="${pageParam}" />
						<input type="hidden" name="prizeType" value="${prizeType}" />
						<input type="hidden" name="sqStatus" value="${sqStatus}" />	
						<div class="row">
			            <div class="col-md-12">
		                    <div class="form-group little_distance search area">
                                
                                <div class="search-item" style="width: 485px !important;">
			                        <label class="control-label">所属区域：</label>
	                                <select name="province" class="zProvince form-control input-width-normal search"></select>
	                                <select name="city" class="zCity form-control input-width-normal search"></select>
	                                <select name="county" class="zDistrict form-control input-width-normal search"></select>
                                </div>
                                
                                
                                <div class="search-item">
	                                <label class="control-label">状态：</label>
	                                <select name="sqStatus" class="form-control input-width-larger search" autocomplete="off" >
	                                    <option value="1" selected="selected">已授权</option>
	                                    <option value="2">未授权</option>
	                                </select>
                                </div>
                                
                                <div class="search-item">
										<label class="control-label">门店编号：</label>
										<input name="terminalNo" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
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
							<thead>
							<tr>
								<th style="width:3%;"><input type="checkbox" name="allCB" id="allCB" /></th>
								<th style="width:4%;">序号</th>
								<th style="width:8%;" data-orderdef>门店编号</th>
								<th style="width:10%;">门店图片</th>
								<th style="width:12%;">门店名称</th>
								<th style="width:7%;">联系电话</th>
								<th style="width:6%;" data-ordercol="create_time">创建时间</th>
								<th style="width:6%;">状态</th>
								<th style="width:15%;">操作</th>
							</tr>
							</thead>
							<tbody>
							<c:choose>
								<c:when test="${fn:length(resultList) gt 0}">
									<c:forEach items="${resultList}" var="item" varStatus="idx">
										<tr>
											<td style="text-align:center;">
                                      			<input name="itemCB" type="checkbox" value="${item.terminalKey}" />
                                   			</td>
											<td style="text-align:center;">
												<span>${idx.count}</span>
											</td>
											<td>
												<span>${item.terminalNo}</span>
											</td>
											<td>
												<c:choose>
													<c:when test="${item.imageUrl eq ''}">
														<span></span>
													</c:when>
													<c:otherwise>	
														<span><img src="<%=imageServerUrl%>${item.imageUrl}" style="height: 80px;width: 170px; float: left;"></span>
													</c:otherwise>
												</c:choose>
											</td>
					
											<td>
												<span>${item.terminalName}</span>
											</td>
											<td>
												<span>${item.phoneNum}</span>
											</td>
											<td>
												<span>${item.createTime}</span>
											</td>
											<td>
												<c:choose>     
												  <c:when test="${item.sqStatus eq '1'}">
												  		<span>已授权</span>
												  </c:when>
												  <c:otherwise>
												  		<span>未授权</span>
												  </c:otherwise>											
												</c:choose>
											</td>
											<td data-key="${item.terminalKey}" style="text-align: center;">
												<c:choose>     
												  <c:when test="${item.sqStatus eq '1'}">
												  		<a class="btn btn-xs del btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;关闭授权</a>
												  </c:when>
												  <c:otherwise>
												  		<a class="btn btn-xs add btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;授权</a>
												  </c:otherwise>											
												</c:choose>

											</td>	
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