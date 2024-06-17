<!-- 左侧菜单 create by cpgu 20140218 -->
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String cpath = request.getContextPath();
%>

<script type="text/javascript">
$(function(){
	$(".sub-menu li a").on('click', function() {
        $(".sub-menu li a").removeClass("cur");
        $(this).addClass("cur");
	});
	
	$("#nav>li").on("click", function() {
		var iconUrl = "";
		$("#nav>li>a>img").each(function(i){
			iconUrl = $(this).attr("src");
			$(this).attr("src", iconUrl.replace("2.png", ".png"));
		});
		iconUrl = $(this).find("a>img").attr("src");
		$(this).find("a>img").attr("src", iconUrl.replace(".png", "2.png"));
	});

// 	蒙牛菜单处理
	if ("${sysUser.projectServerName}" == "mengniu") {
		$("a:contains('消费者管理')").html($("a:contains('消费者管理')").html().replace("消费者管理", "终端用户管理"));
	}
});
</script>
<!-- 请从登陆页面跳过来, 
	http://localhost:8080/vPoint/  
	用户: admin ,密码:admin
	添加菜单请参照 sys_func,sys_role_func 表和用户管理. 在表里添加自已的菜单.
	-->
${menuHtml}