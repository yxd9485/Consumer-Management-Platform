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
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js?v=1"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=3"></script>
	
	<style>
		table.table tr th {
			text-align: center;
		}
		table.table tr td {
			vertical-align: middle;
		}
		
		.checkboxImg {
			width: 80px;
			height: 20px;
			border: 1px solid #999999;
			/* background: url(answerImg.png) no-repeat center right; /自己弄一张13px下拉列表框的图标/ */
			}
			.checkboxDiv {
            width: 240px;
			padding-left:5px;
			padding-right:5px;
			color:white;
			position:fixed;
			margin-left:94px;
			margin-top:-10px;
			z-index:99;
			background-color: rgba(100,100,100,1);
			display: none;
			border: 1px solid #999999;
			border-top: none;
			overflow-y:auto; 
			overflow-x:auto; 
			min-height:10px;
			max-height:400px;
			min-width:200px;
			    }
	    .determineCls {
	        width: 76px;
	        height: 20px;
	        line-height: 20px;
	        border: 1px solid #999999;
	        font-size: 12px;
	        margin: 3px auto;
	        border-radius: 5px;
	        text-align: center;
	        cursor: pointer;
	    }
	</style>
	
	<script type="text/javascript">
		$(function(){
			// 查询结果导出
            $("a.exportReceiveRecord").on("click", function(){
                $("form").attr("action", "<%=cpath%>/couponReceiveRecord/exportReceiveRecord.do");
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/couponReceiveRecord/showReceiveRecordList.do");
            });
		});
	</script>
  </head>
  
  <body>
  	  <form class="listForm" method="post" action="<%=cpath%>/couponReceiveRecord/showReceiveRecordList.do">
          <input type="hidden" class="tableTotalCount" value="${showCount}" />
          <input type="hidden" class="tableStartIndex" value="${startIndex}" />
          <input type="hidden" class="tablePerPage" value="${countPerPage}" />
          <input type="hidden" class="tableCurPage" value="${currentPage}" />
          <input type="hidden" class="tableOrderCol" value="${orderCol}" />
          <input type="hidden" class="tableOrderType" value="${orderType}" />
          <input type="hidden" name="queryParam" value="${queryParam}" />
          <input type="hidden" name="pageParam" value="${pageParam}" />
  	  	  <input type="hidden" name="couponKey" value="${couponKey}">
  	  </form>
	  <div class="col-md-12 tabbable tabbable-custom">
	      <div class="widget box" id="tab_1_1">
	          <div class="row">
	             <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">优惠券发行详情</span></div>
	             <div class="col-md-10 text-right">
	                 <a class="btn btn-blue exportReceiveRecord">
	                 		<i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 下载发行数据
	                 </a>
	          	</div>
	          </div>
	          <div class="widget-content">
	              <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
	                  <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"  id="dataTable_data">
	                      <thead>
	                       <tr>
	                              <th style="width:5%;">序号</th>
	                              <th style="width:10%;">领取人</th>
	                              <th style="width:10%;">手机号</th>
	                              <th style="width:20%;">领券区域</th>
	                              <th style="width:18%;">领取时间</th>
	                              <th style="width:18%;">使用时间</th>
	                              <th style="width:18%;">失效时间</th>
	                          </tr>
	       </thead>
	       <tbody>
	       	<c:choose>
	       	<c:when test="${fn:length(resultList) gt 0}">
	       	<c:forEach items="${resultList}" var="item" varStatus="idx">
	       	<tr>
	       		<td style="text-align:center;">
	                                  <span>${idx.count}</span>
	       		</td>
	       		<td style="text-align:center;">
	       			<span>${item.nickName}</span>
	       		</td>
	       		<td>
	       			<span>${item.phoneNumber}</span>
	       		</td>
	       		<td>
	       			<span>${item.province}-${item.city}-${item.county}</span>
	       		</td>
	       		<td>
	       			<span>${item.receiveTime}</span>
	       		</td>
	       		<td>
	       			<span>${item.useTime}</span>
	       		</td>
	       		<td>
	       			<span>${item.expireTime}</span>
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
  </body>
</html>