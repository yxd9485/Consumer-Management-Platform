<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>--%>
<!DOCTYPE html>
<% 
	String cpath = request.getContextPath();
	String funcImgName = request.getParameter("funcImg");
%>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>V积分管理后台</title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/jquery.uniform.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/jquery.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/pagination/ajax.pagination.js"></script>
    <script type="text/javascript" src="<%=cpath %>/inc/custom/form-actions.js?v=2"></script>
    <script>
    
        $(function() {
            var windowHeight = $(window).height();
            var navbarHeight = $(".header").height();
            $("body").height((windowHeight - navbarHeight) + "px" );
        });
    
	    function bodyResize(){
	        var windowHeight = $(window).height();
	        var navbarHeight = $(".header").height();
	        $("body").height((windowHeight - navbarHeight) + "px" );
	    }
    </script>

	<style>
	   body {
	       background-image: url(../assets/img/main/main-bg2.png);
	       background-size: 100% 100%;
	       background-repeat: no-repeat;
	   }
		.pin-img {
            height:95%;
            margin-left:60px;
		    position: fixed;
		}
	</style>
</head>
<body onresize="bodyResize();">
   	<img class="pin-img" src="<%=cpath %>/assets/img/func/<%=funcImgName%>"/>
</body>
</html>