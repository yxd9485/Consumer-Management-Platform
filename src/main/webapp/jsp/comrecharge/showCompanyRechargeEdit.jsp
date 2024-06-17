<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%String cpath = request.getContextPath(); %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>详细信息</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/tableFormValidate.js?v=5"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>

    <script type="text/javascript">

	    $(function () {
	        $.runtimeValidate($("#addBatchDialog"));

	        //提交
            $(".button_place #btnSubmit").click(function(){
            	if (validForm()) {
            		$.fn.confirm("确定提交当前企业的修改充值请求吗？", function() {
                        $("form").attr("action", "<%=cpath %>/companyRecharge/doEditCompanyRecharge.do");
                        $("form").submit();
                    });
            	}
            });

	    });

	    // 表单校验
        function validForm() {
            var validateResult = $.submitValidate($("form"));
            if(!validateResult){
                return false;
            }
            return true;;
        }
    </script>
    <style>
    	.active_board_table {
    		width: 95%;
    	}
    	.active_board_table tr td .content input, .active_board_table tr td .content select {
    		width: 41% !important;
    	}
    	.active_board_table tr td .content b {
    		height: 28px;
    		line-height: 28px;
    		float: left;
    		margin-left: 8px;
    	}

    	.active_board_table tr td .content b strong {
    		color: green;
    	}

    	.top-tips {
    		float: left;
    		color: red;
    		height: 28px;
    		line-height: 28px;
    		margin-left: 8xp;
    	}
    	.white {
    		color: white;
    	}
    </style>
</head>
<body>
<div class="container">
    <!--面包屑begin-->
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><a>企业充值管理</a></li>
            <li class="current"><a>财务充值</a></li>
            <li class="current"><a>新充值</a></li>
        </ul>
    </div>
    <!--面包屑over-->
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" action="<%=cpath %>/companyRecharge/doCompanyRecharge.do"
                class="form-horizontal row-border" id="sample_form_focus">
				<input name="preKey" type="hidden" class="form-control required contractNum" tag="validate" maxlength="20" value="${info.preKey}" autocomplete="off" />
            <div class="widget box">
                <div class="widget-header"><h4><i class="iconfont icon-peizhi"></i>企业充值</h4></div>
                <div class="widget-content panel nopadding">
                    <table class="active_board_table">
                        <tr>
	                  		<td class="ab_left"><label class="title">合同编号：<span class="required">*</span></label></td>
	                  		<td class="ab_main">
	                  			<div class="content">
	                  				<input name="contractNum" class="form-control required contractNum" tag="validate" maxlength="20" value="${info.contractNum}" autocomplete="off" />
	                                <label class="validate_tips valid-num"></label>
	                  			</div>
	                  		</td>
	                  	</tr>
                        <tr>
	                  		<td class="ab_left"><label class="title">合同名称：<span class="required">*</span></label></td>
	                  		<td class="ab_main">
	                  			<div class="content">
	                  				<input name="contractName" class="form-control required contractName" tag="validate" maxlength="50" value="${info.contractName}"  autocomplete="off" />
	                                 	<label class="validate_tips"></label>
	                  			</div>
	                  		</td>
	                  	</tr>
                        <tr>
	                  		<td class="ab_left"><label class="title">付款方式：</label><span class="white">*</span></td>
	                  		<td class="ab_main">
	                  			<div class="content">
	                  				<select class="form-control required" name="transType">

									    	 <option value="1"<c:if test="${info.transType eq '0'}">selected</c:if> >网银转账</option>
	                                         <option value="2" <c:if test="${info.transType eq '0'}">selected</c:if>>第三方支付工具</option>
	                                         <option value="3" <c:if test="${info.transType eq '0'}">selected</c:if>>支票</option>
	                                         <option value="4" <c:if test="${info.transType eq '0'}">selected</c:if>>欠款代付</option>
	                                     </select>
	                  			</div>
	                  		</td>
	                  	</tr>
                       	<tr>
	                  		<td class="ab_left"><label class="title">付款金额：<span class="required">*</span></label></td>
	                  		<td class="ab_main">
	                  			<div class="content">
	                  				<input name="rechargeMoney" maxLength="10" class="payMoney form-control required money minValue maxValue"
	                  					minVal="-9999999.99" maxVal="9999999.99" tag="validate"  autocomplete="off"  value="${info.rechargeMoney}"/>
	                                   			<b><strong>(1元 = 100积分)</strong> 元</b>
	                                 	<label class="validate_tips"></label><label class="top-tips"></label>
	                  			</div>
	                  		</td>
	                  	</tr>
	                  	<tr style="display: none;">
	                  		<td class="ab_left"><label class="title">充值积分：</label><span class="white">*</span></td>
	                  		<td class="ab_main">
	                  			<div class="content">
	                  				<input class="form-control showPoints" name="rechargeVpoints" readonly="readonly" value="${info.rechargeVpoints}" /> <b>V分</b>
	                  			</div>
	                  		</td>
	                  	</tr>
                    </table>
                </div>
                <div class="active_table_submit mart20">
                    <div class="button_place">
                        <a id="btnSubmit" href="#" class="btn btn-blue">保 存</a>&nbsp;&nbsp;&nbsp;&nbsp;
                        <a id="btnBack" href="<%=cpath %>/companyRecharge/showCompanyRechargeList.do" class="btn btn-radius3">返 回</a>
                    </div>
                </div>
            </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>