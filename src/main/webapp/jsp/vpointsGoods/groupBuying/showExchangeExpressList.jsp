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
	<script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js?v=1"></script>
	<script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=3"></script>
	
  </head>
  
    <script type="text/javascript">
		$(function(){
			// 查询结果导出
            $("a.exportDate").on("click", function(){
                $("form").attr("action","<%=cpath%>/vpointsExchange/exportOrderRecord.do");
                $("form").submit();
                
             	// 还原查询action
                $("form").attr("action","<%=cpath%>/vpointsExchange/showExchangeExpressList.do");
            });
		});
	</script>
  
  <body>
  	 <form class="listForm" method="post" action="<%=cpath%>/vpointsExchange/showExchangeExpressList.do">
  	 	  <input type="hidden" class="tableTotalCount" value="${showCount}" />
          <input type="hidden" class="tableStartIndex" value="${startIndex}" />
          <input type="hidden" class="tablePerPage" value="${countPerPage}" />
          <input type="hidden" class="tableCurPage" value="${currentPage}" />
          <input type="hidden" class="tableOrderCol" value="${orderCol}" />
          <input type="hidden" class="tableOrderType" value="${orderType}" />
          <input type="hidden" name="queryParam" value="${queryParam}" />
          <input type="hidden" name="pageParam" value="${pageParam}" />
		  <input type="hidden" name="vjfSessionId" value="${vjfSessionId }">
		  <input type="hidden" name="groupBuyingActivityKey" value="${groupBuyingActivityKey }">
		  <input type="hidden" name="tabsFlag" value="3">
	</form>
     <div class="col-md-12 tabbable tabbable-custom">
         <div class="widget box" id="tab_1_1">
             <div class="row">
                <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">拼团成交数据</span></div>
                <div class="col-md-10 text-right">
	                 <a class="btn btn-blue exportDate">
	                 		<i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp; 下载数据
	                 </a>
	          	</div>
             </div>
             <div class="widget-content">
                 <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                     <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"  id="dataTable_data">
                         <thead>
<!--                                 待发货 -->
                            <tr>
                                <th style="width:7%;" data-ordercol="e.exchange_id">订单编号</th>
                                <th style="width:7%;" data-ordercol="b.brand_name">兑换品牌</th>
                                <th style="width:7%;" data-ordercol="g.goods_name">商品名称</th>
                                <th style="width:11%;" data-ordercol="e.user_name">收件人</th>
                                <th style="width:5%;" data-ordercol="payType">购买方法</th>
                                <th style="width:6%;" data-ordercol="exchangePay">实付金额</th>
                                <th style="width:6%;" data-ordercol="e.discountsMoney">优惠金额</th>
                                <th style="width:6%;" data-ordercol="exchangeVpoints">实付积分</th>
                                <th style="width:6%;" data-ordercol="e.discountsVpoints">优惠积分</th>
                                <th style="width:8%;" data-ordercol="e.exchange_time" data-orderdef>订单时间</th>
                                <th style="width:8%;" data-ordercol="e.express_number">物流单号</th>
                                <th style="width:8%;" data-ordercol="e.express_send_time">发货时间</th>
                                <th style="width:8%;" data-ordercol="express_sign_time">收货时间</th>
                                <th style="width:6%;" data-ordercol="e.express_status">状态</th>
                            </tr>
		        		</thead>
		        <tbody>
		        	<c:choose>
		        	<c:when test="${fn:length(resultList) gt 0}">
		        	<c:forEach items="${resultList}" var="item" varStatus="idx">
		        	<tr>
		        		<td style="text-align:center;">
                                     <span id="exchangeId">${item.exchangeId}</span>
		        		</td>
		        		<td style="text-align:center;">
		        			<span id="brandName">${item.brandName}</span>
		        		</td>
		        		<td>
		        			<span id="goodsName">${item.goodsName}</span>
		        		</td>
		        		
                        <td>
                            <span id="userKey" style="display: none;">${item.userKey}</span>
                            <span id="userName" style="display: none;">${item.userName}</span>
                            <span id="phoneNum" style="display: none;">${item.phoneNum}</span>
                            <span id="address" style="display: none;">${item.address}</span>
                            <span>${item.userName} ${item.phoneNum} ${item.address} ${item.customerMessage}</span>
                        </td>
                        <td style="text-align:center;">
                            <c:choose>
                            	<c:when test="${item.exchangeVpoints > 0 and  item.exchangePay > 0}">混合支付</c:when>
                            	<c:when test="${item.exchangeVpoints > 0}">积分支付</c:when>
                            	<c:when test="${item.exchangePay > 0}">金额支付</c:when>
                            </c:choose>
                        </td>
		        		<td style="text-align: right;">
                                     <span id="exchangeNum" style="display: none;">${item.exchangeNum}</span>
		        			<c:if test="${item.exchangePay > 0}"><span><fmt:formatNumber value="${item.exchangePay/100}" pattern="#,#0.00#"/></span></c:if>
		        		</td>
		        		<td style="text-align: right;">
		        			<c:if test="${item.discountsMoney > 0}"><span><fmt:formatNumber value="${item.discountsMoney/100}" pattern="#,#0.00#"/></span></c:if>
		        		</td>
		        		<td style="text-align: right;">
		        			<c:if test="${item.exchangeVpoints > 0}"><span><fmt:formatNumber value="${item.exchangeVpoints}" pattern="#,##0"/></span></c:if>
		        		</td>
		        		<td style="text-align: right;">
		        			<c:if test="${item.discountsVpoints > 0}"><span><fmt:formatNumber value="${item.discountsVpoints}" pattern="#,##0"/></span></c:if>
		        		</td>
                        <td style="text-align:center;">
		        			<span>${fn:substring(item.exchangeTime, 0, 19)}</span>
		        		</td>
                                <td>
                                    <span id="expressCompany">${item.expressCompany}</span></br>
                                    <span id="expressNumber">${item.expressNumber}</span>
                                </td>
		        		<td style="text-align:center;">
		        			<span>${fn:substring(item.expressSendTime, 0, 19)}</span>
		        		</td>
		        		<td style="text-align:center;">
		        			<span>${fn:substring(item.expressSignTime, 0, 19)}</span>
		        		</td>
		        		<td style="text-align:center;">
		        		    <span>${item.orderStatus}</span>
		        		</td>
		        	</tr>
		        	</c:forEach>
		        	</c:when>
		        	<c:otherwise>
		        	<tr>
		        	  <td colspan="14"><span>查无数据！</span></td>
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
