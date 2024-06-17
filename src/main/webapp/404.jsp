<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
	<base href="<%=basePath%>">
   
	<title></title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	
	<link href="assets/css/error.css" rel="stylesheet" type="text/css" />
	
	<script type="text/javascript">
		function loginBack(){
			location.href = "<%=path %>/system/logOut.do";
		}
	</script>
</head>
<body class="error">
	<div class="error_pic">
		<p>
			<span>您要访问的页面不存在，请联系管理员或</span>
			<a class="back_to_index" target="_parent" href="<%=path %>/system/logOut.do">返回首页</a>
		</p>
	</div>
	<div class="footer_copy">
		<span class="copyright">&copy; 2014 Digital Bay Technology Group. Co. Ltd. All Rights Reserved.</span>
	</div>
</body>
</html>
