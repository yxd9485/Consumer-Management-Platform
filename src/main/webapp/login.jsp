<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%String cpath = request.getContextPath(); %>
<!DOCTYPE html>

<script src="https://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js">
</script>

<html lang="en">  
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
   	<title>Vjifen</title>
   	<c:choose>
   	    <c:when test="${fn:startsWith(singleProjectServerName, 'mengniu')}">
            <link rel="shortcut icon" type="image/x-icon" href="<%=cpath %>/images/favicon-mengniu.png" media="screen" /> 
   	    </c:when>
   	    <c:otherwise>
            <link rel="shortcut icon" type="image/x-icon" href="<%=cpath %>/images/favicon.ico" media="screen" /> 
   	    </c:otherwise>
   	</c:choose>
    <link href="<%=cpath %>/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
    <link href="<%=cpath %>/assets/css/main.css?v=3.0.1" rel="stylesheet" type="text/css" />
    <link href="<%=cpath %>/assets/css/plugins.css" rel="stylesheet" type="text/css" />
    <link href="<%=cpath %>/assets/css/responsive.css" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath %>/assets/css/icons.css" rel="stylesheet" type="text/css" />
    <link href="<%=cpath %>/assets/css/login.css?v=1.1.6" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="<%=cpath %>/assets/css/fontawesome/font-awesome.min.css">
    <!--[if IE 7]>
      <link rel="stylesheet" href="<%=cpath %>/assets/css/fontawesome/font-awesome-ie7.min.css">
    <![endif]-->
    <!--[if IE 8]>
      <link href="<%=cpath %>/assets/css/ie8.css" rel="stylesheet" type="text/css" />
    <![endif]-->
    <script type="text/javascript" src="<%=cpath %>/assets/js/libs/jquery-1.10.2.min.js">
    </script>
    <script type="text/javascript" src="<%=cpath %>/bootstrap/js/bootstrap.min.js">
    </script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/libs/lodash.compat.min.js">
    </script>

    <!--[if lt IE 9]>
    <script src="<%=cpath %>/assets/js/libs/html5shiv.js"> </script>

    <![endif]-->
    
    <style type="text/css">

		.hoderwhite::-webkit-input-placeholder{
		        color: rgba(256,256,256,0.9);
		}
		.hoderwhite::-moz-placeholder{  //不知道为何火狐的placeholder的颜色是粉红色，怎么改都不行，希望有大牛路过帮忙指点
		        color: rgba(256,256,256,0.9);       
		}
        .hoderwhite:-moz-placeholder{  //不知道为何火狐的placeholder的颜色是粉红色，怎么改都不行，希望有大牛路过帮忙指点
                color: rgba(256,256,256,0.9);       
        }
		.hoderwhite:-ms-input-placeholder{  //由于我的IE刚好是IE9，支持不了placeholder，所以也测试不了(⊙﹏⊙)，有IE10以上的娃可以帮我试试
		        color: rgba(256,256,256,0.9);        
		}
        
	    input:-webkit-autofill , textarea:-webkit-autofill, select:-webkit-autofill {  
		    -webkit-text-fill-color: #ededed !important;  
		    -webkit-box-shadow: 0 0 0px 1000px transparent  inset !important;  
		    background-color:transparent;  
		    background-image: none;  
		    transition: background-color 50000s ease-in-out 0s; //背景色透明  生效时长  过渡效果  启用时延迟的时间  
		}  
		input {  
		     background-color:transparent;  
		}

        .nav-tab {
            height: 40px;
            line-height: 40px;
            display: flex;
            text-align: center;
            font-size: 16px;
            color: black;
        }

        .nav1 {
            border-right: 1px solid #e5e9ed;
        }

        .nav1, .nav2 {
            flex: 1;
            border-bottom: 1px solid #e5e9ed;
            background: #F2F2F2;
        }

        .nav-no-active {
            background: none;
            border-bottom: none;
            color: white;
        }

        .content2 {
            display: none;
        }
    </style>
    <script type="text/javascript">
        var clock = '';
        var nums = 60;

	    $(function() {
	        var windowHeight = $(window).height();
	        $("body").height((windowHeight) + "px" );
            $("#phoneNumBtn").on("click",loadValidCode);

            $("#nav1").click(function() {
                $("#nav2").addClass("nav-no-active");
                $("#nav1").removeClass("nav-no-active");
                $("#login_password").css('display','none');
                $("#login_qrcode").css('display','block');
            });
            $("#nav2").click(function() {
                $("#nav1").addClass("nav-no-active");
                $("#nav2").removeClass("nav-no-active");
                $("#login_password").css('display','block');
                $("#login_qrcode").css('display','none');
            });

            $("#username1").bind("input propertychange",function() {
                fun();
            });
	    });

        function debounce(method, delay) {
            var timer = null;
            return function () {
                var context = this, args = arguments;
                if(timer){
                    clearTimeout(timer);
                    timer = null;

                }
                timer = setTimeout(function () {
                    method.apply(context, args);
                }, delay);
            }
        }
        var fun = debounce(function() {
            new WxLogin({
                self_redirect:false,
                id:"login_container",
                appid: "${appid}",
                scope: "snsapi_login",
                redirect_uri: encodeURI("${redirect_uri}"),
                state: "${state}-" + $("#username1").val(),
                style: "white",
                href: "https://vd.vjifen.com/static/css/impowerBox.css"
            });
            $('#login_container iframe').attr('sandbox', 'allow-scripts allow-top-navigation');
        }, 1000);

        function bodyResize(){
            var windowHeight = $(window).height();
            var navbarHeight = $(".header").height();
            $("body").height((windowHeight - navbarHeight) + "px" );
        }
    	
		function validateValue() {
			var userName = $("#username").val();
			var password = $("#password").val();
			var phoneNum = $("#phoneNum").val();
			var veriCode = $("#veriCode").val();
			if(userName == ""){
				alert("请输入用户名！");
				return false;
			} 
			if(password == ""){
				alert("请输入密码！");
				return false;
			}
			if(phoneNum == ""){
				alert("请输入手机号！");
				return false;
			}
			if(veriCode == ""){
				alert("请输入验证码！");
				return false;
			}
            $("#form1").attr("action", "<%=cpath %>/system/login.do");
            $("#form1").submit();
            return true;
		}
		
		function loadValidCode(){
            $("#phoneNumBtn").unbind();
            var phoneNum = $("#phoneNum").val();
            var userName = $("#username").val();
            if(phoneNum == ""){
                alert("请输入手机号！");
                $("#phoneNumBtn").on("click",loadValidCode);
                return false;
            }
            if(userName==""){
                alert("请输入用户名！");
                $("#userName").on("click",loadValidCode);
                return false;
            }
            
            $.ajax({
                type: "POST",
                url: "<%=cpath %>/system/getPhoneVeriCode.do",
                dataType: "text",
                data:{"phoneNum": phoneNum,
                        "userName":userName},
                    success:  function(data) {
                    if("0" == data){
                        $("#phoneNumBtn").html( nums + '秒重新获取');
                        clock = setInterval(doLoop, 1000); //一秒执行一次,这是一个定时器
                    	alert("验证码发送成功！");
                    } else if("1" == data){
                        alert("验证码发送失败！");
                        
                    } else if("2" == data){
                        alert("您输入的手机号没有登录权限！");
                    }
                }
            });
        }

        function doLoop(btn) {
            nums--;
            if (nums > 0) {
                $("#phoneNumBtn").html( nums + '秒重新获取');
            } else {
                clearInterval(clock); //清除js定时器
// 		        btn.disabled = false;
                $("#phoneNumBtn").html('点击重新获取');
                $("#phoneNumBtn").on("click",loadValidCode);
                nums = 60; //重置时间
            }
        }
    
    </script>
    
    <style>
	.code {
		background-color: #1f252c;
		font-family: Arial;
		font-style: italic;
		color: #003c86;
		font-size: 16px;
		border: 0;
		padding: 2px 3px;
		margin-bottom: 18px;
		letter-spacing: 3px;
		font-weight: bolder;            
		float: left;           
		cursor: pointer;
		width: 180px;
		height: 32px;
		line-height: 32px;
		text-align: center;
		vertical-align: middle;
	}
	.check-code {
	    text-decoration: none;
	    font-size: 12px;
	    color: #288bc4;
	    margin-left: 8px;
	    margin-top: 8px;
    }
	.check-code:hover, .check-code:visited, .check-code:active {
   		text-decoration: none;
   		color: #288bc4;
    }
    </style>
    
</head>
<body onresize="bodyResize();" class="login" >
	<div class="login-box">
		<div class="logo">
		  <c:choose>
		      <c:when test="${fn:startsWith(singleProjectServerName, 'mengniu')}"><img src="<%=cpath %>/assets/img/login/login-logo-mengniu.png"/></c:when>
		      <c:otherwise><img src="<%=cpath %>/assets/img/login/login-logo.png"/></c:otherwise>
		  </c:choose>
		</div>
		<div class="box">
			<div class="content">
                <div class="nav-tab">
                    <div id="nav1" class="nav1 nav-no-active" >微信扫码登录</div>
                    <div id="nav2" class="nav2">账号登录</div>
                </div>

                <div id= "login_password" style="margin: 15px 0 0 0">
                    <form class="form-vertical login-form" id="form1" name="form1" action="" method="post">
                        <div class="form-group">
                            <div class="input-icon">
                               <li style="height: 100%; width: 35px; background: url(<%=cpath %>/assets/img/login/login-user.png) no-repeat 13px; background-size:18px auto; position: absolute; left:0; top:0;"></li>
                                <input type="text" name="userName" id="username" class="form-control hoderwhite" placeholder="用户名"
                                        autofocus="autofocus" data-rule-required="true" data-msg-required="请输入用户名." autocomplete="off"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-icon">
                                <li style="height: 100%; width: 35px; background: url(<%=cpath %>/assets/img/login/login-pwd.png) no-repeat 13px; background-size:16px auto; position: absolute; left:0; top:0;"></li>
                                <input type="password" name="userPassword" id="password" class="form-control hoderwhite" placeholder="密码"
                                        data-rule-required="true" data-msg-required="请输入密码." autocomplete="off"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-icon">
                                <li style="height: 100%; width: 35px; background: url(<%=cpath %>/assets/img/login/login-phone.png) no-repeat 14px; background-size:14px auto; position: absolute; left:0; top:0;"></li>
                                <input type="text" name="phoneNum" id="phoneNum" maxlength="11" class="form-control hoderwhite" placeholder="手机号"
                                        data-rule-required="true" data-msg-required="请输入手机号." autocomplete="off" />
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="input-icon verify">
                                <li style="height: 100%; width: 35px; background: url(<%=cpath %>/assets/img/login/login-verify.png) no-repeat 13px; background-size:16px auto; position: absolute; left:0; top:0;"></li>
                                <input type="text" name="veriCode" id="veriCode" maxlength="4" class="form-control hoderwhite" placeholder="验证码" autocomplete="off"
                                        data-rule-required="true" data-msg-required="请输入验证码." />
                            </div>
                            <div name="phoneNumBtn" id="phoneNumBtn" class="phoneNumBtn">获取验证码</div>
                        </div>
                      <div class="form-actions">
                        <button type="button" id="loginbtn" onclick="validateValue();"  class="btn btn-block">登录</button>
                      </div>
                      <div><span style="color: red"> ${message}</span></div>
                    </form>
                </div>

                <div id="login_qrcode" class="content2" style="margin: 15px 0 0 0">
                    <div class="form-group">
                        <div class="input-icon">
                            <li style="height: 100%; width: 35px; background: url(<%=cpath %>/assets/img/login/login-user.png) no-repeat 13px; background-size:18px auto; position: absolute; left:0; top:0;"></li>
                            <input type="text" name="username1" id="username1" class="form-control hoderwhite" placeholder="账号"
                                   autofocus="autofocus" data-rule-required="true" data-msg-required="请输入账号"/>
                        </div>
                    </div>
                    <div id="login_container" style="height: 220px"></div>
                </div>

<%--            <div id="login_container"></div>--%>
          </div>
        </div>
    </div>
    <div class="copyright">
      <span>©2023 微积分创新科技（北京）股份有限公司</span>
    </div>
  </body>

  <script>
      new WxLogin({
          self_redirect:false,
          id:"login_container",
          appid: "${appid}",
          scope: "snsapi_login",
          redirect_uri: encodeURI("${redirect_uri}"),
          state: "${state}",
          style: "white",
          href: "https://vd.vjifen.com/static/css/impowerBox.css"
      });

      $('#login_container iframe').attr('sandbox', 'allow-scripts allow-top-navigation');
  </script>

</html>