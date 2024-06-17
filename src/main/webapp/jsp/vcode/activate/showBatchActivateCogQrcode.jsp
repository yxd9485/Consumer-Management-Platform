<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加一万一批次激活人员</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="${basePath}/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="${basePath}/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="${basePath}/assets/js/custom/giftspack/utils.js"></script>
	<style>
		.blocker {
			float: left;
			vertical-align: middle;
			margin-right: 8px;
			margin-top: 8px;
		}
		.en-larger {
			margin-left: 8px;
		}
		.en-larger2 {
			margin-left: 12px;
		}
		.ex-larger {
			margin-right: 8px;
		}
		.validate_tips {
			padding: 8px !important;
		}
		div b{
			color:red;
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><i class="icon-home"></i><a> 激活人员管理</a></li>
        	<li class="current"><a title="">新增激活人员</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
           	<div class="widget box" align="center">
           		<div class="widget-header" style="width: 50%;">
           			<h4><i class="icon-list-alt"></i>扫码注册二维码</h4>
           			<h4 id="" style="float: right; margin-top: 10px; cursor: pointer;">
           				<a href="${basePath}/batchActivate/showActivateCogList.do?vjfSessionId=${vjfSessionId}">返回</a>
           			</h4>
           		</div>
               	<div class="widget-content panel no-padding" style="width: 50%;">
               		<div style="width: 100%;">
				     	<img style="width: 300px; hight:300px; display: block; margin: auto;" align="center" src="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=${ticket }">
			     	</div>
               	</div>
           	</div>
        </div>
    </div>
    </div>
  </body>
</html>
