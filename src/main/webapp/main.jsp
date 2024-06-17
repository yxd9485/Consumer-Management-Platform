<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<% String cpath = request.getContextPath();%>
<html>
<head>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0" />
	<c:choose>
		<c:when test="${sysUser.projectServerName eq 'mengniu'}">
			<title>冰品冰码运营管理平台</title>
			<link rel="shortcut icon" type="image/x-icon" href="<%=cpath %>/images/favicon-mengniu.png" media="screen" />
		</c:when>
		<c:when test="${sysUser.projectServerName eq 'mengniuzhi'}">
			<title>蒙牛冰品一支一码运营管理平台</title>
			<link rel="shortcut icon" type="image/x-icon" href="<%=cpath %>/images/favicon-mengniu.png" media="screen" />
		</c:when>
		<c:otherwise>
			<title>${projectName}-消费者管理平台</title>
			<link rel="shortcut icon" type="image/x-icon" href="<%=cpath %>/images/favicon.ico" media="screen" />
		</c:otherwise>
	</c:choose>
	<link href="<%=cpath %>/bootstrap/css/bootstrap.min.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath %>/assets/css/main.css?v=3.0.1" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath %>/assets/css/plugins.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath %>/assets/css/responsive.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath %>/assets/css/icons.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link href="<%=cpath %>/plugins/iconfont/iconfont.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=cpath %>/assets/css/fontawesome/font-awesome.min.css?v=3.0.0">
    <link href="<%=cpath %>/assets/css/plugins.css?v=3.0.0" rel="stylesheet" type="text/css"/><!-- 必须 -->
    <link href="<%=cpath %>/assets/css/style.css?v=3.0.3" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=cpath %>/assets/js/libs/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/plugins/jquery-ui/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/libs/breakpoints.js"></script><!-- 必须 -->
    <script type="text/javascript" src="<%=cpath %>/plugins/respond/respond.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/plugins/slimscroll/jquery.slimscroll.horizontal.min.js"></script>
    <script type="text/javascript" src="<%=cpath %>/assets/js/app.js"></script><!-- 必须 -->
    <jsp:include page="/jsp/common/modalDialog.jsp"></jsp:include>
	<script type="text/javascript">
	$(document).ready(function() {
		App.init();

		var vjfSessionId = '${vjfSessionId}';
		if("${sysUser.userKey}" == '-2'){
			mainFrame("/searchBatchActivate/showSearchActivateBatchList.do?vjfSessionId=" + vjfSessionId);
		}else if ("${sysUser.userName}" == 'ui管理员') {
			mainFrame("/picLib/showPicLibList.do?vjfSessionId=" + vjfSessionId);
		}else if ("${sysUser.picBrandType}") {
            mainFrame("/picLib/showPicLibList.do?vjfSessionId=" + vjfSessionId);
        }else if ("${sysUser.userName}" == '青啤订单查看') {
            mainFrame("/vpointsExchange/showExchangeExpressList.do?vjfSessionId=" + vjfSessionId);
		}else if ("${sysUser.userName}" == '长城玖号派样') {
            mainFrame("/questionOrderAction/showQuestionOrderList.do?vjfSessionId=" + vjfSessionId);
		}else if ("${roleKey}" == '8') {
            mainFrame("/vpointsGoods/getGoodsList.do?vjfSessionId=" + vjfSessionId);
		}else if ("${roleKey}" == '13') {
            mainFrame("/adPub/showAdUpList.do?vjfSessionId=" + vjfSessionId);
		}else if ("${sysUser.projectServerName}" != 'zhongLCNY' && ("${roleKey}" == '1' || "${roleKey}" == '2' || "${roleKey}" == '3' || "${roleKey}" == '4' || "${roleKey}" == '11')) {
			mainFrame("/homeData/homeDataPage.do?vjfSessionId=" + vjfSessionId);
		}else{
            mainFrame("/welcome.jsp");
		}

		$("#sidebar").delegate("a", "click", function(m){
			m.preventDefault();
			m.stopPropagation();
			var $this = $(this);
			var url = $this.attr("href");
			if(url && url != "" && url != "#"){
				if (url.indexOf("?") == -1) {
					url += "?vjfSessionId=" + vjfSessionId;
				} else {
                    url += "&vjfSessionId=" + vjfSessionId;
				}
				$this.attr("href", "#");
				$this.attr("urlLink", url);
			} else {
				url = $this.attr("urlLink");
			}
			$this.parents(".nav-list:first").find("li").removeClass("active");
			$this.parent().addClass("active");
			
			// 中粮CNY特殊处理
			if ("${sysUser.projectServerName}" == 'zhongLCNY' && url.indexOf("homeDataPage.do") > 0) {
				mainFrame("/welcome.jsp");
			} else {
				$("#mainFrame").attr("src", url);
			}
		});
		
		// logo
		var projectServerName = $("#projectServerName").val();
		if (projectServerName.indexOf('mengniu') != -1) {
			$("#logo").attr("src", "../assets/img/main/main-logo-mengniu.png");
		}

		bodyResize();

	});


	if (location.host == "envato.stammtec.de" || location.host == "themes.stammtec.de") {
		var _paq = _paq || [];
		_paq.push([ "trackPageView" ]);
		_paq.push([ "enableLinkTracking" ]);
		(function() {
			var a = (("https:" == document.location.protocol) ? "https" : "http") + "://analytics.stammtec.de/";
			_paq.push([ "setTrackerUrl", a + "piwik.php" ]);
			_paq.push([ "setSiteId", "17" ]);
			var e = document, c = e.createElement("script"), b = e.getElementsByTagName("script")[0];
			c.type = "text/javascript";
			c.defer = true;
			c.async = true;
			c.src = a + "piwik.js";
			b.parentNode.insertBefore(c, b);
		})();
	};
	//add by cpgu 20140409
	function mainFrame(mainUrl){
	 	document.getElementById("mainFrame").src="<%=cpath %>"+mainUrl;
	}
	function loginOut(){
		//todo 后续更改为confirm
		$.fn.confirm("您确认要退出吗？",function(){
			location.href="<%=cpath %>/system/logOut.do";
		});
	}
    function openMenu(url){
       $("#sidebar-content a").each(function(){
           $(this).removeAttr("style");
       });
        var iconUrl=$("#"+url).parent().parents("li").find("a").find("img").attr("src");
        $("#"+url).parent().parents("li").find("a").find("img").attr("src", iconUrl.replace(".png", "2.png"));
 
       
        $("#"+url).addClass("cur");
        $("#"+url).parent().parents("li").addClass("open active");
        $("#"+url).parent().parents(".sub-menu").css("display", "block");
      
	}

    function expireRmind(){
            location.href="<%=cpath%>/expireRemindAction/expireRemindList.do?vjfSessionId=${vjfSessionId}" ;
    }

	function bodyResize(){
		var windowHeight = $(window).height();
		var navbarHeight = $(".header").height();
		$("#mainFrame").height((windowHeight - navbarHeight) + "px" );
	}
</script>
</head>
<body onresize="bodyResize();" style="overflow-x:hidden; overflow-y:hidden;">
    <input type="hidden" id="vjfSessionId" name="vjfSessionId" value="${vjfSessionId}" />
    <input type="hidden" id="projectServerName" value="${sysUser.projectServerName}" />
	<div class="header navbar navbar-fixed-top" role="banner">
		<div>
			<ul class="nav navbar-nav">
				<li class="nav-toggle">
					<a href="javascript:void(0);" title=""><i class="icon-reorder"> </i></a>
				</li>
			</ul>

			<a href="#" class="toggle-sidebar bs-tooltip" data-placement="bottom" data-original-title="Toggle navigation"><img id="logo" alt="" src="../assets/img/main/main-logo.png" style="height: 60px; width: auto;"/></a>

            <div class="title-top">
                <c:if test="${not(sysUser.userKey eq '-2' or sysUser.userName eq '青啤订单查看' or roleKey eq '8' or roleKey eq '12')}">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown user">
                            <a href="<%=cpath%>/expireRemindAction/expireRemindListCondition.do?vjfSessionId=${vjfSessionId}" target="mainFrame" class="dropdown-toggle">
                                <c:if test="${roleKey eq '1' or roleKey eq '2' or roleKey eq '3' or roleKey eq '4'}">
                                    <i class="iconfont icon-riqishijian" title="规则将要到期"></i>
                                    <span class="msgRemind"id="msgRemind">消息提醒:${count } </span>
                                </c:if>
                            </a>
                        </li>
                    </ul>
                </c:if>
               <c:if test="${!fn:startsWith(sysUser.projectServerName, 'mengniu')}">

<%--                <ul class="nav navbar-nav navbar-right">--%>
<%--                    <li class="dropdown user">--%>
<%--                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-male"></i> <span class="username">${sysUser.userName }</span> <i class="icon-caret-down small"></i></a>--%>
<%--                        <ul class="dropdown-menu">--%>

<%--                            &lt;%&ndash; <li><a href="<%=cpath %>/system/modifyPassword.do" class="dropdown-toggle" target="mainFrame"><i class="icon-cog"></i>修改密码</a></li> &ndash;%&gt;--%>
<%--                            <li><a href="#" onclick="loginOut();"><i class="icon-off"></i>退出</a></li>--%>
<%--                        </ul>--%>
<%--                    </li>--%>
<%--                </ul>--%>
                   <ul class="nav navbar-nav navbar-right">
                       <li class="dropdown user">
                           <a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="icon-male"></i> <span class="username">${sysUser.userName }</span> <i class="icon-caret-down small"></i></a>
                           <ul class="dropdown-menu">
                               <c:forEach items="${link}" var="users">
                                <li><a href="${users.platformUrl}?token=${token}&username=${users.username}" target="_blank"><i class="icon-male"></i>${users.platformName}</a></li>
                               </c:forEach>
                                   <%-- <li><a href="<%=cpath %>/system/modifyPassword.do" class="dropdown-toggle" target="mainFrame"><i class="icon-cog"></i>修改密码</a></li> --%>
                               <li><a href="#" onclick="loginOut();"><i class="icon-off"></i>退出</a></li>
                           </ul>
                       </li>
                   </ul>

               </c:if>
                <div class="headertxt">
                    <c:choose>
                        <c:when test="${sysUser.projectServerName == 'mengniu'}">
                            麒麟终端营销运营管理平台
                        </c:when>
                        <c:when test="${sysUser.projectServerName == 'mengniuzhi'}">
                            蒙牛冰品一支一码营销运营管理平台
                        </c:when>
                        <c:otherwise>
                            消费者管理平台
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
		</div>
	</div>
	<div id="container">
		<div id="sidebar" class="sidebar-fixed">
			<div id="sidebar-content">
				<div class="">
				<jsp:include page="leftMenu.jsp"></jsp:include>
				</div>
			</div>
			<div class="resizeable" style="display: none;"></div>
		</div>
		<div id="content">
			<div id="mainFrameContainer">
				<iframe src="" allowtransparency="true" style="width:100%; padding:0; margin:0; border-width: 0; overflow: scroll; background: transparent;"
         			title="test" frameborder="0" id="mainFrame" name="mainFrame"></iframe>
			</div>
		</div>
	</div>
</body>
</html>