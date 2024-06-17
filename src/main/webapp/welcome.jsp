<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>--%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();
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
	       background-size: 100% 100%;
	       background-repeat: no-repeat;
	   }
		.pin-img {
            height:13%;
		    position: fixed;
		    left: 23%;
		    bottom: 50%;
		}
		.line_box{
		  height:29%;
		  width:0%;
          position:absolute;
          left:25%;
          transform-origin:left top;
          bottom:10%;
          overflow:hidden;
          animation:lines 3.5s ease both 1s;          
		}
		.line_box img{
		  height:100%;
		  display:block;
		  width:auto;
		  max-width:none;
		}
		#dot{
		  height:29%;
		  position:absolute;
		  left:25%;
		  bottom:10%;
		  opacity:0;
		  animation:dot .5s ease both;
		}
		@keyframes lines{
		  0%{
		      width:0%;
		  }
		  100%{
		      width:75%;
		  }
		}
		@keyframes dot{
          0%{
              opacity:0;
          }
          100%{
              opacity:1;
          }
        }
	</style>
</head>
<body onresize="bodyResize();">
   	<img class="pin-img" src="<%=cpath %>/assets/img/main/main-flag.png"/>
    <img src="<%=cpath %>/assets/img/main/dot.png"/ id="dot">
    <div class="line_box"><img  src="<%=cpath %>/assets/img/main/line.png" id="line"/></div>
</body>
</html>