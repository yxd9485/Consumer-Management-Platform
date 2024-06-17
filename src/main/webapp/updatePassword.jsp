<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="/WEB-INF/tlds/securitytag.tld" prefix="st"%>
<!DOCTYPE html>
<%String cpath = request.getContextPath(); %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>增加角色</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath }/assets/js/tableFormValidate.js"></script>
    <script type="text/javascript">
    
    	$(function(){
    		$.runtimeValidate($("#pwd_form"));
    		
    		var result = "${result}";
    		if(result == "success"){
    			$.fn.confirm("修改成功，确认后返回登录页面",function(){
    				parent.location.href = "<%=cpath %>/system/logOut.do";
        		});
    		}
    	});
    
        function validateForm(){
        	var flag = $.submitValidate($("#pwd_form"));
        	if(flag){
        		$.fn.confirm("确定要修改您的密码吗？",function(){
        			$("form").submit();
        		});
        	}
		}
       
        
    </script>
    <style>
    	.password_table {
    		width: 72%;
    		margin: 8px auto;
    	}
    	
    	.password_table tr td {
    		margin: 10px;
    		padding: 10px;
    	}
    	
    	.password_table tr td:nth-child(1) {
    		width: 27%;
    		text-align: right;
    	}
    	
    	.password_table tr td:nth-child(2) {
    		width: 82%;
    	}
    	
    	.password_table tr td:nth-child(2) .form-control {
    		width: 50%;
    		float: left;
    	}
    	
    </style>
</head>
<body>
<div class="container">
	<div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
            <li class="current"><i class="icon-home"></i><a> 修改密码</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12">
        	<form id="pwd_form" method="post" class="form-horizontal row-border"
        		action="<%=cpath %>/system/updatePassword.do">
            <div class="widget box">
            	<div class="widget-header"><h4><i class="icon-cog"></i>修改密码</h4></div>
                <div class="widget-content">
                	<input id="userKey" type="hidden" name="userKey" value="${user.userKey}">
                    <table class="password_table">
                    	<tr>
                    		<td style="text-align:center;"><label class="title">原密码：<span class="required">*</span></label></td>
                    		<td style="text-align:center;">
                    			<div class="content">
                    				<input type="password" name="orgPwd" class="form-control required originalPwd"
                    					tag="validate"
                    					data-path="<%=cpath %>/system/validPassword.do?userKey=${user.userKey}" />
                    				<label class="validate_tips"></label>
                    			</div>
                    		</td>
                    	</tr>
                    	<tr>
                    		<td style="text-align:center;"><label class="title">新密码：<span class="required">*</span></label></td>
                    		<td style="text-align:center;">
                    			<div class="content">
                    				<input type="password" name="newPwd" class="form-control required pass"
                    					tag="validate" />
                    				<label class="validate_tips"></label>
                    			</div>
                    		</td>
                    	</tr>
                    	<tr>
                    		<td style="text-align:center;"><label class="title">确认新密码：<span class="required">*</span></label></td>
                    		<td style="text-align:center;">
                    			<div class="content">
                    				<input type="password" name="decPwd" class="form-control required pass passTwo"
                    					tag="validate" />
                    				<label class="validate_tips"></label>
                    			</div>
                    		</td>
                    	</tr>
                    </table>
                </div>
                <div class="active_table_submit plus_top">
		            <div class="button_place">
		            	<a class="btn btn-primary" oncancel="validateForm()">确 定</a>
		            	<button class="btn btn-reset btn-radius3 marl20 btnReset" type="reset">重 置</button>
		            </div>
	            </div>
            </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>