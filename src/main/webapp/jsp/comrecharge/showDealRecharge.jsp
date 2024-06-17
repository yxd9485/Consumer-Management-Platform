<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<%String cpath = request.getContextPath(); %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>详细信息</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript">
	   	$(function(){
	   		
	   		// 确认
            $(".button_place #btnSubmit").click(function(){
            	$.fn.confirm("确认要将此笔充值确认并入账吗？", function() {
                    $("form").attr("action", "<%=cpath %>/companyRecharge/dealCompanyRecharge.do");
                    $("form").submit();
                });
            });
	   	});
 	
	</script>
    <style>
    	.active_board_table {
    		width: 95%;
    	}
    </style>
</head>
<body>
<div class="container">
    <!--面包屑begin-->
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a>企业充值管理</a></li>
            <li class="current"><a>市场确认</a></li>
            <li class="current">
            	<c:if test="${rechargeInfo.transStatus == 1}"><a>确认</a></c:if>
            	<c:if test="${rechargeInfo.transStatus == 2}"><a>终止</a></c:if>
            </li>
        </ul>
    </div>
    <!--面包屑over-->
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form class="recharge_form" method="post" action="<%=cpath %>/companyRecharge/dealCompanyRecharge.do" >
            	<input type="hidden" name="preKey" value="${rechargeInfo.preKey}" />
            	<input type="hidden" name="companyKey" value="${rechargeInfo.companyKey}" />
                <input type="hidden" name="transStatus" value="${transStatus}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <div class="widget box">
                	<div class="widget-header"><h4><i class="icon-credit-card"></i>企业充值</h4></div>
                	<div class="widget-content panel nopadding">
                		<table class="active_board_table">
                        	<tr>
	                      		<td class="ab_left"><label class="title">合同编号：</label></td>
	                      		<td class="ab_main">
	                      			<div class="content">
	                      				<p>${rechargeInfo.contractNum}</p>
	                      				<input type="hidden" name="contractNum" value="${rechargeInfo.contractNum}" />
	                      			</div>
	                      		</td>
                      		</tr>
                            <tr>
	                      		<td class="ab_left"><label class="title">合同名称：</label></td>
	                      		<td class="ab_main">
	                      			<div class="content">
	                      				<p>${rechargeInfo.contractName}</p>
	                      				<input type="hidden" name="contractName" value="${rechargeInfo.contractName}" />
	                      			</div>
	                      		</td>
                      		</tr>
                            <tr>
	                      		<td class="ab_left"><label class="title">付款方式：</label></td>
	                      		<td class="ab_main">
	                      			<div class="content">
	                      				<p>
	                      					<c:if test="${rechargeInfo.transType == 1}">网银转账</c:if>
	                      					<c:if test="${rechargeInfo.transType == 2}">第三方支付工具</c:if>
	                      					<c:if test="${rechargeInfo.transType == 3}">支票</c:if>
	                      					<c:if test="${rechargeInfo.transType == 4}">欠款代付</c:if>
	                      				</p>
	                      				<input type="hidden" name="transType" value="${rechargeInfo.transType}" />
	                      			</div>
	                      		</td>
                      		</tr>
                            <tr>
	                      		<td class="ab_left"><label class="title">付款金额：</label></td>
	                      		<td class="ab_main">
	                      			<div class="content">
	                      				<p>
	                      					<fmt:formatNumber value="${rechargeInfo.rechargeMoney}" pattern="#,##0.0#" />
	                      					<span>元</span>
	                      				</p>
	                      				<input type="hidden" name="rechargeMoney" value="${rechargeInfo.rechargeMoney}" />
	                      			</div>
	                      		</td>
                      		</tr>
                      		<tr>
	                      		<td class="ab_left"><label class="title">充值积分：</label></td>
	                      		<td class="ab_main">
	                      			<div class="content">
	                      				<p>
	                      					<fmt:formatNumber value="${rechargeInfo.rechargeVpoints}" pattern="#,##0" />
	                      					<span>V积分</span>
	                      				</p>
	                      				<input type="hidden" name="rechargeVpoints" value="${rechargeInfo.rechargeVpoints}" />
	                      			</div>
	                      		</td>
                      		</tr>
                    	</table>
                	</div>
                	<div class="active_table_submit mart20">
						<div class="button_place">
							<a id="btnSubmit" class="btn btn-primary confirm btn-red" >确 认</a>&nbsp;&nbsp;&nbsp;&nbsp;
						    <a class="btn back btn-radius3" href="<%=cpath %>/companyRecharge/showCompanyConfirmList.do">返 回</a>
						</div>
					</div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>