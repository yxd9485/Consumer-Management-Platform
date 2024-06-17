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
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	
	<script>
		$(function(){
            
            // 新建
            $("a.addItem").off();
            $("a.addItem").on("click", function(){
            	var url = "<%=cpath%>/sendWechatMsg/showSendTemplateMsgAdd.do";
            	$("form").attr("action",url);
            	$("form").submit();
            });
            
         	// 修改
            $("a.edit").off();
            $("a.edit").on("click", function(){
            	var key = $(this).parents("td").data("key");
            	var url = "<%=cpath%>/sendWechatMsg/showSendTemplateMsgEdit.do?infoKey=" + key;
            	$("form").attr("action",url);
            	$("form").submit();
            });
            
         	// 刪除
            $("a.del").off();
            $("a.del").on("click", function(){
           		var key = $(this).parents("td").data("key");
            	$.fn.confirm("确定要删除吗？", function(){
                	var url = "<%=cpath%>/sendWechatMsg/deleteById.do?infoKey=" + key;
                	$("form").attr("action",url);
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
            <li class="current"><a> 积分商城</a></li>
        	<li class="current"><a> 消息管理</a></li>
        </ul>
    </div>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">模板消息查询</span></div>
	               <div class="col-md-10 text-right">
                       <a class="btn btn-blue addItem">
                       <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新建模板消息
                       </a>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/sendWechatMsg/showSendTemplateMsgList.do">
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
			                        <label class="control-label">按关键字查询：</label>
			                        <input name="keyword" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">推送日期：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
                                </div>
                                <div class="search-item">
	                                <label class="control-label">推送状态：</label>
                                    <select name="status" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option style="padding: 20px;" value="0">待推送</option>
                                        <option style="padding: 20px;" value="1">已推送</option>
                                    </select>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">用户KEY：</label>
			                        <input name="userKey" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
	                                <label class="control-label">模板类型：</label>
                                    <select name="templateType" class="form-control input-width-larger search" autocomplete="off" >
                                        <option style="padding: 20px;" value="">全部</option>
                                        <option style="padding: 20px;" value="1">服务通知</option>
                                        <option style="padding: 20px;" value="2">单品推送</option>
                                        <option style="padding: 20px;" value="3">特殊推送</option>
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">模板消息列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
	                            <tr>
		                            <th style="width:10%;" data-ordercol="template_no">模板消息编号</th>
		                            <th style="width:10%;" data-ordercol="send_time">推送时间</th>
		                            <th style="width:10%;" data-ordercol="create_time" data-orderdef>创建时间</th>
		                            <th style="width:10%;" data-ordercol="message_type">消息类型</th>
		                            <th style="width:10%;" data-ordercol="template_type">模板类型</th>
		                            <th style="width:8%;" data-ordercol="status">推送状态</th>
		                            <th style="width:10%;" data-ordercol="status">推送数量</th>
		                            <th style="width:20%;" data-ordercol="describes">描述信息</th>
		                            <th style="width:12%;">操作</th>
		                        </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
						        	<c:forEach items="${resultList}" var="item" varStatus="idx">
							        	<tr>
							        		<td style="text-align:center;">
		                                        <span>${item.templateNo}</span>
							        		</td>
							        		<td style="text-align:center;">
							        			<span>${item.sendTime}</span>
							        		</td>
							        		<td style="text-align: center;">
							        			<span>${item.createTime}</span>
							        		</td>
							        		<td style="text-align: center;">
							        			<c:choose>
		                                            <c:when test="${item.messageType == '1'}">
		                                                <span>公众号推送</span>
		                                            </c:when>
		                                            <c:when test="${item.messageType == '2'}">
		                                                <span>小程序推送</span>
		                                            </c:when>
		                                            <c:otherwise><span>其他</span></c:otherwise>
		                                        </c:choose>
							        		</td>
							        		<td style="text-align: center;">
							        			<c:choose>
		                                            <c:when test="${item.templateType == '1'}">
		                                                <span>服务通知</span>
		                                            </c:when>
		                                            <c:when test="${item.templateType == '2'}">
		                                                <span>单品推送</span>
		                                            </c:when>
		                                            <c:when test="${item.templateType == '3'}">
		                                                <span>特殊推送</span>
		                                            </c:when>
		                                            <c:otherwise><span>其他</span></c:otherwise>
		                                        </c:choose>
							        		</td>
							        		<td style="text-align: center;">
							        			<c:choose>
		                                            <c:when test="${item.status == '0'}">
		                                                <span>待推送</span>
		                                            </c:when>
		                                            <c:when test="${item.status == '1'}">
		                                                <span>已推送</span>
		                                            </c:when>
		                                            <c:otherwise><span>其他</span></c:otherwise>
		                                        </c:choose>
							        		</td>
							        		<td style="text-align: center;">
							        			<c:choose>
		                                            <c:when test="${item.status == '0'}">
		                                                <span>-</span>
		                                            </c:when>
		                                            <c:when test="${item.status == '1'}">
		                                                <span>${item.sendCount }</span>
		                                            </c:when>
		                                            <c:otherwise><span>其他</span></c:otherwise>
		                                        </c:choose>
							        		</td>
							        		<td style="text-align:center;">
							        			<span>${item.describes}</span>
							        		</td>
							        		<td data-key="${item.infoKey}" style="text-align: center;">
							        			<c:if test="${currentUser.roleKey ne '4'}">
                                                    <c:choose>
                                                        <c:when test="${item.status == '0'}">
                                                            <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai" style="font-size: 14px;"></i>&nbsp;
                                                                修 改
                                                            </a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="btn btn-xs edit btn-blue"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;
                                                                查 看
                                                            </a>
                                                        </c:otherwise>
                                                    </c:choose>
							        				<c:if test="${item.status == '0'}">
							        					<a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang" style="font-size: 14px;"></i>&nbsp;删 除</a>
							        				</c:if>
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
	</div>
  </body>
</html>
