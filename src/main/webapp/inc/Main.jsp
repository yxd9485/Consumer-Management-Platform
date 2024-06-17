<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%	
    String basePath = request.getContextPath();
	/**String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";*/
	session.setAttribute("basePath",basePath);
	
	String userId = (String)session.getAttribute("userId");
 %>
	<link href="<%=basePath %>/bootstrap/css/bootstrap.min.css?v=1.1.2" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>/assets/css/main.css?v=3.0.1" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>/assets/css/plugins.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>/assets/css/responsive.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>/assets/css/icons.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="<%=basePath %>/assets/css/fontawesome/font-awesome.min.css?v=3.0.0">
    <link href="<%=basePath %>/assets/css/plugins.css?v=3.0.0" rel="stylesheet" type="text/css"/><!-- 必须 -->
    <link href="<%=basePath %>/assets/css/style.css?v=3.0.3" rel="stylesheet" type="text/css"/>
    <link href="<%=basePath %>/plugins/iconfont/iconfont.css?v=3.0.0" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="<%=basePath %>/assets/js/libs/jquery-1.10.2.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/plugins/jquery-ui/jquery-ui-1.10.2.custom.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/libs/breakpoints.js"></script><!-- 必须 -->
    <script type="text/javascript" src="<%=basePath %>/plugins/respond/respond.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/plugins/slimscroll/jquery.slimscroll.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/plugins/slimscroll/jquery.slimscroll.horizontal.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>/assets/js/app.js"></script><!-- 必须 -->
    <script type="text/javascript" src="<%=basePath %>/assets/js/common.js"></script><!-- 必须 -->
    <jsp:include page="/jsp/common/modalDialog.jsp"></jsp:include>
    

    <script>
        $(function(){
            $("form").append("<input type='hidden' id='vjfSessionId' name='vjfSessionId' value='" + $("#vjfSessionId", parent.document).val() + "' />");

//    	 	蒙牛菜单处理
			if ($("#projectServerName", parent.document).val() == "mengniu") {
	    		$("a:contains('消费者管理')").each(function(i,obj){
	    			$(this).html($(this).html().replace("消费者管理", "终端用户管理"));
	    		});
			}
			
			// 省区禁用
			$(".serverdis").each(function(i, obj) {
				if ($(this).data("serverdis")) {
					var serverNameAry = $(this).data("serverdis").split(",");
					var serverName = $(window.top.document).find("#projectServerName").val();
					$.each(serverNameAry, function(i, val){
						if (serverName == val) {
							$(obj).attr("disabled", "disabled");
						}
					});
				}
			});
            
            // 省区启用
            $(".serverenb").each(function(i, obj) {
                if ($(this).data("serverenb")) {
                    var serverNameAry = $(this).data("serverenb").split(",");
                    var serverName = $(window.top.document).find("#projectServerName").val();
                    $.each(serverNameAry, function(i, val){
                        if (serverName == val) {
                            $(obj).removeAttr("disabled");
                        }
                    });
                }
            });
			
			// 省区隐藏
			$(".serverhid").each(function(i, obj) {
				if ($(this).data("serverhid")) {
					var serverName = $(window.top.document).find("#projectServerName").val();
					var serverNameAry = $(this).data("serverhid").split(",");
					$.each(serverNameAry, function(i, val){
						if (serverName == val) {
							$(obj).css("display", "none");
						}
					});
				}
			});
            
            // 省区显示
            $(".servershow").each(function(i, obj) {
                if ($(this).data("servershow")) {
                    var serverName = $(window.top.document).find("#projectServerName").val();
                    var serverNameAry = $(this).data("servershow").split(",");
                    $.each(serverNameAry, function(i, val){
                        if (serverName == val) {
                            $(obj).css("display", "");
                        }
                    });
                }
            });
			
			// 图片缩放
			$("img.imgzoon").on("click", function(){
				$(this).ImgZoomIn();
			});
        });
        
        function appendVjfSessionId() {
        	if (this.url.indexOf("?") == -1) {
        		this.url += "?vjfSessionId=" + $("#vjfSessionId").val();
        	} else {
        		this.url += "&vjfSessionId=" + $("#vjfSessionId").val()
        	}
        }
        
        // 图片大图
        $.fn.ImgZoomIn = function () {
        	var mainFrame = $(window.top.document).find("#mainFrame").contents();
        	bgstr = '<div id="ImgZoomInBG" style=" background:#000000; filter:Alpha(Opacity=70); opacity:0.7; position:fixed; left:0; top:0; z-index:10000; width:100%; height:100%; display:none;"></div>';
        	imgstr = '<div id="ImgZoomInImageScroll" style="position: fixed;left: 0px;overflow: scroll;top: 0px;z-index: 10000;width: 100%;height: 100%;"><img id="ImgZoomInImage" src="' + $(this).attr('src')+'" onclick=$(\'#ImgZoomInImageScroll\').hide();$(\'#ImgZoomInBG\').hide(); style="cursor:pointer; margin: auto;display: block;" /></div>';
        	if ($(mainFrame).find('#ImgZoomInBG').length < 1) {
        		$(mainFrame).find('body').append(bgstr);
        	}
        	if ($(mainFrame).find('#ImgZoomInImage').length < 1) {
        		$(mainFrame).find('body').append(imgstr);
        	} else {
        		$(mainFrame).find('#ImgZoomInImage').attr('src', $(this).attr('src'));
        	}
        	$(mainFrame).find('#ImgZoomInBG').show();
        	$(mainFrame).find('#ImgZoomInImageScroll').show();
       	};
    </script>
