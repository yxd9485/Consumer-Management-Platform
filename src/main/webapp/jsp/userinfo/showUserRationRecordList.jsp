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
    <title>河北管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination2.js"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<script>

	    $(function(){
	        
	    });
	    
		function validFile(){
			// 日期
			var startDate = $("input[name='startDate']").val();
			if(startDate == "") {
				$.fn.alert("请选择查询时间");
				return false;
			}else{
				/* if(startDate < '2017-10-01'){
					$.fn.alert("查询时间必须大于2017-10-01");
					return false;	
				} */
			}
			
		}
	</script>
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
		}
		.doubt-table {
			width: 98%;
			maring: 0 auto;
		}
		.doubt-table > thead > tr > th {
			font-weight: normal;
		}
		b {
			margin: 0 8px;
			color: #c33;
		}
		.doubt-table > tbody > tr > td {
			padding: 6px;
		}
		
		.doubt-table > tbody > tr > td:nth-child(1) {
			text-align: right;
		}
		.doubt-list-value {
			width: 95%;
			height: 32px;
			border: 1px solid #e1e1e1;
			background-color: #fff;
		}
		.doubt-list-value:focus {
			border: 1px solid #e1e1e1;
		}
		.valid-place, .required {
			color: #c33;
		}
	</style>
  </head>
  <body>
  	<img id="large" src="" style="display: none; width: 460px;height: 460px;position: fixed;top: 24%;left: 34%;z-index:1;">
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a>首页</a></li>
        	<li class="current"><a>消费者管理</a></li>
        	<li class="current"><a title="">兑付记录</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
        <div class="widget box">
                <div class="row">
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">提现记录查询</span></div>
                   <div class="col-md-10 text-right">
                   </div>
                </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
                <div class="widget-content form-inline">
                    <form class="listForm" method="post" 
                        action="<%=cpath%>/vcodeRationRecord/showUserRationRecordList.do" onsubmit="return validFile();">
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
			                            <label class="control-label">资金流水单号：</label>
			                            <input name="paymentNo" class="form-control input-width-larger" autocomplete="off" maxlength="40"/>
	                                </div>
	                                <div class="search-item">
			                            <label class="control-label">选择时间：</label>
			                            <input name="startDate" class="form-control input-width-medium Wdate"
			                                tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd'})" />
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">提现记录列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
                               id="dataTable_data">
                            <thead>
					            <tr>
					            	<th style="width:6%;">序号</th>
					            	<!-- <th style="width:19%;">微信openid</th> -->
					            	<th style="width:19%;" data-ordercol="r.user_key">用户userKey</th>
					            	<th style="width:15%;" data-ordercol="u.nick_name">微信昵称</th>
					            	<th style="width:15%;" data-ordercol="r.payment_no">资金流水单号</th>
					            	<th style="width:15%;" data-ordercol="r.earn_money">兑付金额</th>
					            	<th style="width:15%;" data-ordercol="r.earn_time" data-orderdef>兑付时间</th>
					            	<th style="width:15%;" data-ordercol="r.extract_status">状态</th>
					            </tr>
					        </thead>
					        <tbody>
					        	<c:choose>
					        	<c:when test="${fn:length(resultList) gt 0}">
					        	<c:forEach items="${resultList}" var="result" varStatus="idx">
					        	<tr>
					        		<td style="text-align:center;"><span>${idx.count}</span></td>
					        		<td>
					        			<%-- <span>${result.openid}</span> --%>
					        			<span>
					        			    <c:choose>
					        			        <c:when test="${serverName eq 'qmbaipi'}">
                                                    <a href="http://59.110.53.118:9008/DBTMainEntStats/vcodeQuery/queryListPage.do?qmbp&${queryBean.startDate }&${result.userKey}" target="_black">            					        			        
					        			        </c:when>
					        			        <c:otherwise>
					        			            <a href="http://59.110.53.118:9008/DBTMainEntStats/vcodeQuery/queryListPage.do?${serverName}&${queryBean.startDate }&${result.userKey}" target="_black">
					        			        </c:otherwise>
					        			    </c:choose>
					        				
					        				${result.userKey}
					        				</a>
					        			</span>
					        		</td>
					        		<td>
					        			<span>${result.nickName}</span>
					        		</td>
					        		<td>
					        			<span>${result.paymentNo}</span>
					        		</td>
					        		<td>
					        			<span>${result.earnMoney}</span>
					        		</td>
					        		<td style="text-align:center;">
					        			<span>${result.earnTime}</span>
					        		</td>
					        		<td style="text-align:center;">
			        				    <span>
				        				    <c:choose>
				        				    	<c:when test="${result.extractStatus eq '0'}">成功</c:when>
				        				    	<c:when test="${result.extractStatus eq '1'}">失败</c:when>
				        				    	<c:when test="${result.extractStatus eq '2'}">进行中</c:when>
				        				    </c:choose>
			        				    </span>
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
			        <button type="button" class="close msgClose" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body">
      				<h6 class="edit_success">编辑成功</h6>
      				<h6 class="edit_fail">编辑失败</h6>
                    <h6 class="custom_msg">定制消息</h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default msgClose btn-red" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
	</div>
  </body>
</html>
