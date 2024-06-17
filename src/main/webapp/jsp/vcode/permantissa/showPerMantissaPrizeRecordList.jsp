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
			// 导出
			$("#export").on("click", function(){
				$("form").attr("action", "<%=cpath%>/perMantissaPrizeRecord/exportPerMantissaPrizeRecord.do");
				$("form").submit();
				
				// 还原查询Action
                $("form").attr("action", "<%=cpath%>/perMantissaPrizeRecord/showPerMantissaPrizeRecordList.do");
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
        	<li class="current"><a> 逢尾规则</a></li>
            <li class="current"><a title="">中奖详情</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
            	<div class="row">
	               	<div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">中奖用户查询</span></div>
                   <div class="col-md-10 text-right">
                       <a id="export" class="btn btn-blue">名单下载</a>
                   </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post"
	                    action="<%=cpath%>/perMantissaPrizeRecord/showPerMantissaPrizeRecordList.do">
	                    <input type="hidden" class="tableTotalCount" value="${showCount}" />
	                    <input type="hidden" class="tableStartIndex" value="${startIndex}" />
	                    <input type="hidden" class="tablePerPage" value="${countPerPage}" />
	                    <input type="hidden" class="tableCurPage" value="${currentPage}" />
	                    <input type="hidden" class="tableOrderCol" value="${orderCol}" />
	                    <input type="hidden" class="tableOrderType" value="${orderType}" />
	                    <input type="hidden" name="perMantissaKey" value="${queryBean.perMantissaKey}" />
	                    <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
	                    <input type="hidden" name="queryParam" value="${queryParam}" />
	                    <input type="hidden" name="pageParam" value="${pageParam}" />
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search">
                                <div class="search-item">
			                        <label class="control-label">用户ID：</label>
			                        <input name="userKey" class="form-control input-width-larger" autocomplete="off" maxlength="50"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">昵称：</label>
			                        <input name="nickName" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
                                </div>
                                <div class="search-item">
			                        <label class="control-label">中奖V码：</label>
			                        <input name="qrcodeContent" class="form-control input-width-larger" autocomplete="off" maxlength="30"/>
		                        </div>
                                <div class="search-item">
                                    <label class="control-label">中奖时间：</label>
                                    <input name="startDate" id="startDate" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" class="form-control input-width-medium Wdate sufTime search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})" />
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
                   <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">中奖用户列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:4%; text-align: center;">序号</th>
					            	<th style="width:15%;" data-ordercol="r.user_key">用户ID</th>
					            	<th style="width:8%;" data-ordercol="r.nick_name">昵称</th>
					            	<th style="width:8%;" data-ordercol="r.qrcode_content">中奖v码</th>
					            	<th style="width:12%;" data-ordercol="r.sku_key">中奖SKU</th>
					            	<th style="width:6%;" data-ordercol="r.prize_san_num">中奖瓶数</th>
					            	<th style="width:10%;" data-ordercol="r.prize_name">奖项名称</th>
					            	<th style="width:6%;" data-ordercol="r.earn_money">中奖金额</th>
					            	<th style="width:6%;" data-ordercol="r.earn_vpoints">中奖积分</th>
					            	<th style="width:10%;" data-ordercol="address">中奖区域</th>
					            	<th style="width:13%;" data-ordercol="r.create_time" data-orderdef>中奖时间</th>
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
					        			<span>${item.userKey}</span>
					        		</td>
					        		<td>
					        			<span>${item.nickName}</span>
					        		</td>
					        		<td style="text-align: center;">
					        			<span>${item.qrcodeContent}</span>
					        		</td>
					        		<td>
					        			<span>${item.skuName}</span>
					        		</td>
					        		<td style="text-align: center;">
					        			<span>${item.prizeScanNum}</span>
					        		</td>
                                    <td>
                                        <span>${item.prizeName}</span>
                                    </td>
					        		<td style="text-align: center;">
					        			<span>${item.earnMoney}</span>
					        		</td>
					        		<td style="text-align: center;">
					        			<span>${item.earnVpoints}</span>
					        		</td>
					        		<td>
					        			<span>${item.address}</span>
					        		</td>
					        		<td style="text-align: center;">
					        			<span>${fn:substring(item.createTime, 0, 19)}</span>
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
	</div>
  </body>
</html>
