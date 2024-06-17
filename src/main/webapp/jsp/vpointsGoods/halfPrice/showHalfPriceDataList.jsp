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
			
			// 数据
			$("a.data").off();
			$("a.data").on("click", function(){
				var url = "<%=cpath%>/halfPriceActivity/importHalfPriceActivityData.do";
				$("form").attr("action", url);
				$("form").submit();
                $("form").attr("action", "<%=cpath%>/halfPriceActivity/showHalfPriceDataList.do");
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
            <li class="current"><a> 折扣活动数据</a></li>

        </ul>
    </div>
    <div class="row">
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <div class="widget box">
	            <div class="row">
	               <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">数据查询</span></div>
	               <div class="col-md-10 text-right">
		               <c:if test="${currentUser.roleKey ne '4'}">
	                       <a id="addItem" class="btn data btn-blue">
	                           <i class="iconfont  icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 导出列表
	                       </a>
                       </c:if>
	               </div>
	            </div>
                <div class="row"><div class="col-md-12"><hr style="height: 2px; margin: 10px 0px;"/></div></div>
	            <div class="widget-content form-inline">
		            <form class="listForm" method="post" action="<%=cpath%>/halfPriceActivity/showHalfPriceDataList.do">
                        <input type="hidden" class="tableTotalCount" value="${showCount}" />
                        <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                        <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                        <input type="hidden" class="tableCurPage" value="${currentPage}" />
                        <input type="hidden" name="pageParam" value="${pageParam}" />
                   
	                <div class="row">
			            <div class="col-md-12 ">
		                    <div class="form-group little_distance search" style="line-height: 35px;">
                                <div class="search-item">
			                        <label class="control-label">订单编号：</label>
                                    <input type="hidden" name="infoKey" id="infoKey" value="${activityCogVO.infoKey}">
                                    <label>
                                        <input name="exchangeId" class="form-control input-width-larger" value="${activityCogVO.exchangeId}" autocomplete="off" maxlength="30"/>
                                    </label>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">商品名称：</label>
                                    <label>
                                        <input name="goodsName" class="form-control input-width-larger" value="${activityCogVO.goodsName}" autocomplete="off" maxlength="30"/>
                                    </label>
                                </div>
            
                                <div class="search-item">
                                    <label class="control-label">订单金额：</label>
                                    <label>
                                        <input type="number" name="startRealPay" value="${activityCogVO.startRealPay}" class="form-control  integer number
                                           input-width-small" autocomplete="off" maxlength="30"/>
                                    </label>
                                    <label>-</label>
                                    <label>
                                        <input type="number" name="endRealPay" value="${activityCogVO.endRealPay}" class="form-control
                                         integer number  input-width-small" autocomplete="off" maxlength="30"/>
                                    </label>
                                </div>
                                <div class="search-item">
                                    <label class="control-label">订单时间：</label>
                                    <input name="startDate" id="startDate" value="${activityCogVO.startDate}" class="form-control input-width-medium Wdate search-date"
                                        tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})" />
                                    <label class="">-</label>
                                    <input name="endDate" id="endDate" value="${activityCogVO.endDate}" class="form-control input-width-medium Wdate sufTime search-date"
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
                   <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">活动列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
                                <input type="hidden" id="screenWidth1366" value="5,9,14,9,11,9,9,9,9,9,9" />
                                <tr>
                                    <th style="width:4%;" >序号</th>
                                    <th style="width:8%;" >订单编号</th>
                                    <th style="width:15%;" >商品名称</th>
                                    <th style="width:4%;" >商品单价（元）</th>
                                    <th style="width:4%;" >第二件价格（元）</th>
                                    <th style="width:4%;" >下单数量</th>
                                    <th style="width:4%;" >订单类型</th>
                                    <th style="width:4%;" >购买方式</th>
                                    <th style="width:6%;" >订单金额（元）</th>
                                    <th style="width:8%;" >订单时间</th>
                                    <th style="width:8%;" >收件人</th>
                                    <th style="width:4%;" >订单状态</th>
                                </tr>
					        </thead>
					        <tbody>
                            <c:choose>
                                <c:when test="${fn:length(resultList) gt 0}">
                                    <c:forEach items="${resultList}" var="item" varStatus="idx">
                                        <tr>
                                            <td style="text-align:center;"><span>${idx.count}</span>
                                            </td>
                                            <td style="text-align:center;">
                                                <span>${item.exchangeId}</span>
                                            </td>
                                            <td>
                                                <span>${item.goodsName}</span>
                                            </td>
                                            <td>
                                                <span><fmt:formatNumber value="${item.goodsOriginalPrice/100}" pattern="#,##0.00#"/></span>
                                            </td>
                                            <td>
					        			        <span>
                                                    <fmt:formatNumber
                                                    value="${(item.exchangePay-(item.goodsOriginalPrice*(item.exchangeNum-1)))/100}"
                                                    pattern="#,##0.00#"/>
                                                </span>
                                            </td>
                                            <td>
                                                <span>${item.exchangeNum}</span>
                                            </td>
                                            <td>
                                                <span>${item.orderType}</span>
                                            </td>
                                            <td>
                                                <span>${item.payTypeName}</span>
                                            </td>
                                            <td>
                                                <span>
                                                    <fmt:formatNumber
                                                    value="${item.exchangePay/100}"
                                                    pattern="#,##0.00#"/>
                                                </span>
                                            </td>
                                            <td>
                                                <span>${item.exchangeTime}</span>
                                            </td>
                                            <td>
                                                <span>${item.userName} ${item.phoneNum} ${item.address} ${item.customerMessage}</span>
                                            </td>
                                          
                                            <td>
                                                <span>${item.orderStatus}</span>
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
