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
            // 核销下载
            $("a.download").off();
            $("a.download").on("click", function(){
                $("form").attr("action", "<%=cpath%>/verificationDetail/exportVerificationDetailList.do");
                $("form").submit();
                
                // 还原查询action
                $("form").attr("action", "<%=cpath%>/verificationDetail/showVerificationDetailList.do");
            });
            
            // 预览核销下载
            $("a.downloadPreview").off();
            $("a.downloadPreview").on("click", function(){
                $("form").attr("action", "<%=cpath%>/verificationDetail/exportPreviewVerificationDetailList.do");
                $("form").submit();
                
                // 还原预览查询action
                $("form").attr("action", "<%=cpath%>/verificationDetail/showPreviewVerificationDetailList.do");
            });
            
            // 生成核销
            $("a.addVerification").off();
            $("a.addVerification").on("click", function(){
                var url = "<%=cpath%>/verification/doVerificationInfoAdd.do";
                $.ajax({
                    type: "POST",
                    url: url,
                    data: {
                    	"verificationEndDate":"${verificationEndDate}",
                    	"brandKeys":"${brandKeys}"
                    },
                    dataType: "json",
                    async: false,
                    beforeSend:appendVjfSessionId,
                    success:  function(data) {
                        $.fn.alert(data['errMsg'], function(){
                        	if(data['errMsg'] =="生成核销记录成功"){
                        		$("form").attr("action", "<%=cpath%>/verification/doforwardList.do");
                        		$("form").submit();
                        	}
                        });
                    }
                });
            });
            
            // 确认核销
            $("a.verification").off();
            $("a.verification").on("click", function(){
            	$.fn.confirm("确认该条核销记录？确认后无法再修改时间，也无法终止。", function(){
                    $("form").attr("action", "<%=cpath%>/verification/updateVerificationStatus.do?status=1");
                    $("form").submit();
                });
            });
            
            // 终止核销
            $("a.termination").off();
            $("a.termination").on("click", function(){
                var key = $(this).closest("td").data("key");
                var url = "<%=cpath%>/verification/updateVerificationStatus.do?status=2";
                $.fn.confirm("确认终止该条核销记录？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
            
            // 取消预览
            $("a.cancelPreview").off();
            $("a.cancelPreview").on("click", function(){
                $("form").attr("action", "<%=cpath%>/verification/doforwardList.do");
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
        	<li class="current"><a> 首页</a></li>
            <li class="current"><a> 积分商城</a></li>
        	<li class="current"><a> 核销管理</a></li>
        	<li class="current"><a> 核销列表</a></li>
        </ul>
    </div>
    <form class="listForm" method="post" 
	      <c:choose>
		   		<c:when test="${operationType eq 'preview'}">
		   			action="<%=cpath%>/verificationDetail/showPreviewVerificationDetailList.do"
		   		</c:when>
		   		<c:otherwise>
		   			action="<%=cpath%>/verificationDetail/showVerificationDetailList.do"
		   		</c:otherwise>
		  </c:choose>
      >
       <input type="hidden" class="tableTotalCount" value="${showCount}" />
       <input type="hidden" class="tableStartIndex" value="${startIndex}" />
       <input type="hidden" class="tablePerPage" value="${countPerPage}" />
       <input type="hidden" class="tableCurPage" value="${currentPage}" />
       <input type="hidden" class="tableOrderCol" value="${orderCol}" />
       <input type="hidden" class="tableOrderType" value="${orderType}" />
       <input type="hidden" name="queryParam" value="${queryParam}" />
       <input type="hidden" name="pageParam" value="${pageParam}" />
       <input type="hidden" name="verificationId" value="${verificationId}" />
       <input type="hidden" name="verificationEndDate" value="${verificationEndDate}" />
       <input type="hidden" name="brandKeys" value="${brandKeys}" />
    </form>
    <div class="row mart20">
        <div class="col-md-12 tabbable tabbable-custom">
        	<div class="widget box">
<!--             核销记录概述 -->
                <div class="widget-header">
                    <h4><i class="iconfont icon-xinxi"></i>待核销信息预览确认</h4>
                </div>
                <div class="widget-content panel no-padding">
                    <table class="active_board_table">
                        <tr>
                            <td class="ab_left"><label class="title">核销编号：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span <c:if test="${empty(verificationInfo.verificationId)}">style="color: red;"</c:if>>
                                    	${empty(verificationInfo.verificationId) ? '核销预览' : verificationInfo.verificationId}
                                    </span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">核销时间：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span>${verificationInfo.startDate}&nbsp;&nbsp;至&nbsp;&nbsp;${verificationInfo.endDate}</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="ab_left"><label class="title">兑换总积分：<span class="required"></span></label></td>
                            <td class="ab_main" style="width: 25% !important;">
                                <div class="content">
                                    <span><fmt:formatNumber value="${verificationInfo.totalVpoints}" pattern="#,##0"/></span>
                                </div>
                            </td>
                            <td class="ab_left"><label class="title">商品总价：<span class="required"></span></label></td>
                            <td class="ab_main">
                                <div class="content">
                                    <span><fmt:formatNumber value="${verificationInfo.totalMoney}" pattern="#,##0.00"/>元</span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="4" class="text-right">
		                        <c:choose>
	                       			<c:when test="${operationType eq 'preview'}">
	                       				<a class="btn btn-blue downloadPreview"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp;预览核销下载 </a>
	                       				<c:if test="${verificationFlag eq '1'}">
	                       					<a class="btn btn-blue addVerification"><i class="iconfont icon-queren" style="font-size: 14px;"></i>&nbsp;生成核销 </a>
	                       				</c:if>
	                       				<a class="btn btn-blue cancelPreview"><i class="iconfont icon-zhongzhi" style="font-size: 14px;"></i>&nbsp;取消预览 </a>
	                       			</c:when>
	                       			<c:otherwise>
	                       				<a class="btn btn-blue download"><i class="iconfont icon-xiazai" style="font-size: 14px;"></i>&nbsp;核销表格下载 </a>
	                       				<c:if test="${verificationFlag eq '1'}">
	                       					<a class="btn btn-blue verification"><i class="iconfont icon-queren" style="font-size: 14px;"></i>&nbsp;确认核销 </a>
	                       					<a class="btn btn-blue termination"><i class="iconfont icon-zhongzhi" style="font-size: 14px;"></i>&nbsp;终止核销 </a>
	                       				</c:if>
	                       			</c:otherwise>
	                       		</c:choose>
                            </td>
                        </tr>
                    </table>
                </div>
<!--            待核销列表 -->
            	<div class="row mart30">
                   <div class="col-md-2"><span style="font-size: 15px; font-weight: bold;">待核销列表</span></div>
                </div>
                <div class="widget-content">
                    <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                        <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                               id="dataTable_data">
                            <thead>
	                            <tr>
		                            <th style="width:5%;">序号</th>
		                            <th style="width:10%;" data-ordercol="t.brand_name" data-orderdef>品牌名称</th>
		                            <th style="width:20%;" data-ordercol="t.goods_name">商品名称</th>
		                            <th style="width:13%;" data-ordercol="t.goods_client_no">商品编号</th>
		                            <th style="width:8%;" data-ordercol="t.exchange_num">总数量</th>
		                            <th style="width:13%;" data-ordercol="t.exchange_vpoints">兑换总积分</th>
		                            <th style="width:8%;" data-ordercol="t.unit_money">商品单价</th>
		                            <th style="width:13%;" data-ordercol="t.total_money">商品总价</th>
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
		                                        <span>${item.brandName}</span>
							        		</td>
							        		<td>
							        			<span>${item.goodsName}</span>
							        		</td>
							        		<td>
							        			<span>${item.goodsClientNo}</span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.exchangeNum}" pattern="#,##0"/></span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.exchangeVpoints}" pattern="#,##0"/></span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.unitMoney}" pattern="#,##0.00"/></span>
							        		</td>
							        		<td style="text-align: right;">
							        			<span><fmt:formatNumber value="${item.totalMoney}" pattern="#,##0.00"/></span>
							        		</td>
							        	</tr>
						        	</c:forEach>
					        	</c:when>
					        	<c:otherwise>
						        	<tr>
	                                    <td colspan="8"><span>查无数据！</span></td>
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
