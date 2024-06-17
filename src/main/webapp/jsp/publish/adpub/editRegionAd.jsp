<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%
    String cpath = request.getContextPath(); 
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>编辑弹窗广告页面</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=6"></script>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=5"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
		<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
		<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	
    
	
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	
	<script>
	var basePath='<%=cpath%>';
	var allPath='<%=allPath%>';
	var imgSrc=[];
	var clickImg = function(id){
		$("#addBatchDialog").modal("show");
		parent.$("#my_iframe").contents().find("#queryTY").click();
		//移除选中项
		$('input:checkbox').removeAttr('checked');
		$("#btnName").val(id);
	}

    // 本界面上传图片要求
    var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };
    
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			// 初始化功能
			initPage();
		});
		
		function initPage() {
			$("#picUrl").attr("src",src="https://img.vjifen.com/images/vma/"+'${vpsAdRegion.picUrl}');
			$("input[name= picUrl]").val('${vpsAdRegion.picUrl}');

            var position = '${vpsAdRegion.position}'
            if(position !== '0'){
                $('#sectionGroupTr').hide(); // 否则隐藏所属栏目行
                $('#sectionGroup').val("0"); // 栏目重置为默认
            }


            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
            
            // 保存
			$(".btnSave").click(function(){
				var flag = validForm();
				if (flag) {
					var url = $(this).attr("url");
					$.fn.confirm("确认发布？", function(){
						
			            $(".btnSave").attr("disabled", "disabled");
						
						$("form").attr("action", url);
						$("form").attr("onsubmit", "return true;");
						$("form").submit();
					});
				}
				return false;
			});


            // 获取所有name为'position'的单选按钮
            var positionRadios = $('input[name="position"]');

            // 为每个展示位置的单选按钮添加点击事件监听
            positionRadios.click(function () {

                var projectServerName = $('#projectServerName').val();
                var sizeTip = $('.mengniuzhiTips'); // 获取尺寸建议元素

                if (projectServerName === 'mengniuzhi') {
                    if ($(this).val() === '0') { // 当选中的是首页（value为0）
                        $('#sectionGroupTr').show(); // 显示所属栏目行
                    } else {
                        $('#sectionGroupTr').hide(); // 否则隐藏所属栏目行
                        $('#sectionGroup').val("0"); // 栏目重置为默认
                    }

                    if ($(this).val() === '2') { // 当选中的是任务（value为2）
                        sizeTip.text('尺寸建议716*179px'); // 设置任务的尺寸建议
                    } else {
                        sizeTip.text('尺寸建议宽度346px，高度不限，常用高度为221px'); // 设置首页的尺寸建议
                    }
                }

                //只有首页允许选择二级跳转
                var jumpType = $('input:radio[name="jumpTyp"]:checked').val();
                if (jumpType == '2') {
                    if ($(this).val() === '0') { // 当选中的是首页（value为0）
                        $("#canDoubleJump").show();
                    }else{
                        $("#canDoubleJump input[name='canDoubleJump'][value='0']").prop("checked", true).trigger("change");
                        $("#canDoubleJump").hide();
                    }
                }

            });
            
            // 触达用户类型
            $("input[name=crowdLimitType]").on("click",function(){
                if($(this).val() == '2' || $(this).val() == '3'){
                    $("#userGroupTr").css("display","");
                }else{
                    $("#userGroupTr").css("display","none");
                }
            });
            
            $("#pubStat").bootstrapSwitch({  
                onSwitchChange:function(event,state){  
                    if(state==true){  
                       $("input:hidden[name='pubStat']").val("1");
                    }else{  
                       $("input:hidden[name='pubStat']").val("0");
                    }
                }
            });
            
         	// 初始化区域事件
			$("#addArea").closest("div").initZone("<%=cpath%>", true, "", true, true, false, true);
			// 初始化筛选区域
			var filterAreaCodeAry = "${vpsAdRegion.areaCode}".split(",");
			$.each(filterAreaCodeAry, function(idx, val, ary){
				if (val != '') {
					if(val == '000000'){
						$("td.area div:last-child").find("select").val(val);
					}else{
						if (idx > 0) $("#addArea").click();
						$("td.area div:last-child").initZone("<%=cpath%>", true, val, true, true, false, true);	
					}
				}
			});
            $("[name='jumpTyp']").on("change", function () {
                // 跳转连接
                $("tr.jumpurl").css("display","none");
                $("tr.jumpurl input").val('').hide();
                
                // 图片
                $("#picJumpUrlTr").hide();
                $("#picJumpUrlTr").find("#picJumpUrl").hide();
                $("#picJumpUrlTr").find("input[name='picJumpUrl']").val('');
                $("#picJumpUrlTr").find("#picJumpUrl").attr("src", "<%=cpath %>/inc/vpoints/img/a12.png")
                $("#canDoubleJump input[name='canDoubleJump'][value='0']").prop("checked", true).trigger("change");
                $("#canDoubleJump").hide();


                // 连接
                if ($(this).val() == '1') {
                    $("tr.jumpurl").show();
                    $("tr.jumpurl").find("input[name='jumpUrl']").show();
                }
                // 图片
                if ($(this).val() == '2') {
                    $("#picJumpUrlTr").show();
                    $("#picJumpUrlTr").find("#picJumpUrl").show();

                    //只有位置为首页才允许二级跳转
                    var position = $('input:radio[name="position"]:checked').val();
                    if (position == '0') {
                        $("#canDoubleJump").show();
                    }
                }
                // 三方小程序
                if ($(this).val() == '3') {
                    $("tr.jumpurl").show();
                    $("tr.jumpurl").find("input[name='appid']").show();
                    $("tr.jumpurl").find("input[name='jumpUrl']").show();
                }
                // 内部小程序
                if ($(this).val() == '4') {
                    $("tr.jumpurl").show();
                    $("tr.jumpurl").find("input[name='jumpUrl']").show();
                }
            });

            $("[name='canDoubleJump']").on("change", function () {
                // 否
                if ($(this).val() == '0') {
                    $("#doubleJumpType input[name='doubleJumpType'][value='1']").prop("checked", true);
                    $("#doubleJumpType").hide();
                    $("#doubleJumpUrl").hide();
                    $("#doubleJumpPic").hide();
                    $("#doubleJumpBtnPic").find("input[name='doubleJumpBtnUrl']").val('');
                    $("#doubleJumpBtnPic").find("#doubleJumpBtnUrl").attr("src", "<%=cpath %>/inc/vpoints/img/a12.png")
                    $("#doubleJumpBtnPic").hide();

                }
                // 是
                if ($(this).val() == '1') {
                    $("#doubleJumpType").show();
                    $("#doubleJumpBtnPic").show();
                    $("[name='doubleJumpType']").trigger("change");
                }
            });

            $("[name='doubleJumpType']").on("change", function () {
                // 跳转连接
                $("#doubleJumpUrl").hide();
                $("#doubleJumpUrl").find("input[name='doubleJumpUrl']").val('').hide();
                $("#doubleJumpUrl").find("input[name='doubleJumpAppid']").val('').hide();
                // 图片
                $("#doubleJumpPic").hide();
                $("#doubleJumpPic").find("input[name='doubleJumpPicUrl']").val('');
                $("#doubleJumpPic").find("#doubleJumpPicUrl").attr("src", "<%=cpath %>/inc/vpoints/img/a12.png")
                // 连接
                if ($(this).val() == '1') {
                    $("#doubleJumpUrl").show();
                    $("#doubleJumpUrl").find("input[name='doubleJumpUrl']").show();
                }
                // 图片
                if ($(this).val() == '2') {
                    $("#doubleJumpPic").show();
                }
                // 三方小程序
                if ($(this).val() == '3') {
                    $("#doubleJumpUrl").show();
                    $("#doubleJumpUrl").find("input[name='doubleJumpAppid']").show();
                    $("#doubleJumpUrl").find("input[name='doubleJumpUrl']").show();
                }
                // 内部小程序
                if ($(this).val() == '4') {
                    $("#doubleJumpUrl").show();
                    $("#doubleJumpUrl").find("input[name='doubleJumpUrl']").show();
                }
            });
            $("[name='jumpTyp'][value='${vpsAdRegion.jumpTyp}']").attr("checked", "checked").change();

            // 初始化数据显示
            var jumpType = '${vpsAdRegion.jumpTyp}';
            if (jumpType == "2") {
                $("#picJumpUrl").attr("src",src='https://img.vjifen.com/images/vma/'+'${vpsAdRegion.picJumpUrl}');
                $("input[name= picJumpUrl]").val('${vpsAdRegion.picJumpUrl}');

                //是否可跳转初始化
                $("[name='canDoubleJump'][value='${vpsAdRegion.canDoubleJump}']").attr("checked", "checked").change();
                var canDoubleJump = '${vpsAdRegion.canDoubleJump}'
                if (canDoubleJump == '1') {
                    //按钮图片
                    $("#doubleJumpBtnUrl").attr("src", src = "https://img.vjifen.com/images/vma/" + '${vpsAdRegion.doubleJumpBtnUrl}');
                    $("input[name= doubleJumpBtnUrl]").val('${vpsAdRegion.doubleJumpBtnUrl}');
                    //跳转类型
                    $("[name='doubleJumpType'][value='${vpsAdRegion.doubleJumpType}']").attr("checked", "checked").change();
                    // 二级初始化数据显示
                    var doubleJumpType = '${vpsAdRegion.doubleJumpType}';
                    if (doubleJumpType == "2") {
                        $("#doubleJumpPicUrl").attr("src",src='https://img.vjifen.com/images/vma/'+'${vpsAdRegion.doubleJumpPicUrl}');
                        $("input[name= doubleJumpPicUrl]").val('${vpsAdRegion.doubleJumpPicUrl}');
                    } else if (doubleJumpType == "1") {
                        $("input[name= doubleJumpUrl]").val('${vpsAdRegion.doubleJumpUrl}');
                    } else if (doubleJumpType == "3" || doubleJumpType == "4" ) {
                        $("input[name= doubleJumpAppid]").val('${vpsAdRegion.doubleJumpAppid}');
                        $("input[name= doubleJumpUrl]").val('${vpsAdRegion.doubleJumpUrl}');
                    }
                }

            } else if (jumpType == "1") {
                $("tr.jumpurl").find("input[name='jumpUrl']").val('${vpsAdRegion.jumpUrl}');
            } else if (jumpType == "3" || jumpType == "4" ) {
                $("tr.jumpurl").find("input[name='appid']").val('${vpsAdRegion.appid}');
                $("tr.jumpurl").find("input[name='jumpUrl']").val('${vpsAdRegion.jumpUrl}');
            }
			$("#addBatchDialog").delegate("#comBtn", "click", function(){
				$(window.frames["my_iframe"].document).find("input[type=radio]:checked").each(function(){
					var btnName = $("#btnName").val();
					var picWidth = $(this).siblings("div[name = picWidth]").html();
					var picHeight = $(this).siblings("div[name = picHeight]").html();
					var picX = $(this).siblings("div[name = picX]").html();
					var picY = $(this).siblings("div[name = picY]").html();
					$("#"+btnName).attr("src",src="https://img.vjifen.com/images/vma/"+ $(this).val());
					$('input[name="'+btnName+'"]').val($(this).val());
					$('input[name="picWidth"]').val(picWidth);
					$('input[name="picHeight"]').val(picHeight);
					$('input[name="picX"]').val(picX);
					$('input[name="picY"]').val(picY);
				});
				$("#addBatchDialog #closeBtn").trigger("click");
			});
         
		}
		function validForm() {
            var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
            // 组建筛选区域
            var areaCode = "";
            var areaName = "";
            $("td.area div.area").each(function(i){
                var $province = $(this).find("select.zProvince");
                var $city = $(this).find("select.zCity");
                var $county = $(this).find("select.zDistrict");
                if ($county.val() != "") {
                    areaCode = areaCode + $county.val() + ",";
                } else if ($city.val() != "") {
                    areaCode = areaCode + $city.val() + ",";
                } else if ($province.val() != "") {
                    areaCode = areaCode + $province.val() + ",";
                } else {
                    areaCode = "";
                    areaName = "";
                    return false;
                }
                areaName = areaName + $province.find("option:selected").text() + "_" 
                + $city.find("option:selected").text() + "_" + $county.find("option:selected").text() + ";"
			});
			if(areaName.indexOf("全部") != -1){
				areaCode="000000"
			}
			$("input[name='areaCode']").val(areaCode);
			$("input[name='areaName']").val(areaName);
			if(areaCode == ""){
				alert("请筛选区域！")
				return false;
			}
			if($('input[name="picUrl"]').val() == ""){
				alert("请上传图片");
				return false;
			}

            var jumpType = $('input:radio[name="jumpTyp"]:checked').val();
            if (jumpType == "3" && $("[name='appid']").val() == "") {
                alert("跳转小程序appid不能为空！")
                return false;
            }
            if((jumpType == "3" || jumpType == "1") && $("[name='jumpUrl']").val() == ""){
                alert("跳转链接不能为空！")
                return false;
            }

            if(jumpType == "2"){

                if ($("[name='picJumpUrl']").val() == ""){
                    alert("跳转图片不能为空！")
                    return false;
                }

                var canDoubleJump = $('input:radio[name="canDoubleJump"]:checked').val();
                if (canDoubleJump == "1"){
                    if ($("[name='doubleJumpBtnUrl']").val() == ""){
                        alert("二级跳转按钮不能为空！")
                        return false;
                    }
                    var doubleJumpType = $('input:radio[name="doubleJumpType"]:checked').val();
                    if (doubleJumpType == "3" && $("[name='doubleJumpAppid']").val() == "") {
                        alert("二级跳转小程序appid不能为空！")
                        return false;
                    }
                    if((doubleJumpType == "3" || doubleJumpType == "1"|| doubleJumpType == "4") && $("[name='doubleJumpUrl']").val() == ""){
                        alert("二级跳转链接不能为空！")
                        return false;
                    }
                    if(doubleJumpType == "2" && $("[name='doubleJumpPicUrl']").val() == ""){
                        alert("二级跳转图片不能为空！")
                        return false;
                    }
                }
            }

            return true;
		}
	</script>
	
	<style>
		.white {
			color: white;
		}
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
		.show-sku-name {
			float: left;
			margin-left: 8px;
			margin-top: 8px;
		}
		.top-only {
			border-top: 1px solid #e1e1e1;
		}
		.tab-radio {
			margin: 10px 0 0 !important;
		}
		.validate_tips {
			padding: 8px !important;
		}
		.mx-right {
			float: right;
			margin-top: 2px;
		}
		.modal-body1 {
			display: block;
			height: 500px;
		}
		.frame-body {
			display: block;
			height: 100%;
			width: 100%;
		}
		.modal-dialog {
			display: block;
			width: 800px;
		}
		.item {
			width: 190px;
			height: 190px;
			float: left;
			position: relative;
			margin: 20px;
		}
		.addImg {
            width: 190px;
            height: 190px;
            position: absolute;
            z-index: 2;
            cursor: pointer;
		}
		.addImg1 {
			width: 190px;
			height: 190px;
			position: absolute;
			left: 0px;
			top: 0px;
			z-index: 2;
			cursor: pointer;
		}
		/*.preview,.preBlock{*/
		/*    position: absolute;*/
		/*    width: 190px;*/
		/*    height: 190px;*/
		/*    left: 0;*/
		/*    top: 0;*/
		/*}*/
		.delete {
			width: 30px;
			position: absolute;
			right: -30px;
			top: -15px;
			cursor: pointer;
			display: none;
		}
		.preBlock img {
			display: block;
			width: 190px;
			height: 190px;
		}
		/*.content{*/
		/*    width: 200px;*/
		/*}*/
		.upload_input{
			display: block;
			width: 0;
			height: 0;
			-webkit-opacity: 0.0;
			/* Netscape and Older than Firefox 0.9 */
			-moz-opacity: 0.0;
			/* Safari 1.x (pre WebKit!) 老式khtml内核的Safari浏览器*/
			-khtml-opacity: 0.0;
			/* IE9 + etc...modern browsers */
			opacity: .0;
			/* IE 4-9 */
			filter:alpha(opacity=0);
			/*This works in IE 8 & 9 too*/
			-ms-filter:"progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";
			/*IE4-IE9*/
			filter:progid:DXImageTransform.Microsoft.Alpha(Opacity=0);
		}
	</style>
  </head>
  
  <body>
    <div class="container">
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/actRegion/doAdRegionEdit.do">
                <input type="hidden" name="unmodifiedPosition" value="${vpsAdRegion.unmodifiedPosition}">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>修改商城广告</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                            <input type="hidden" id="projectServerName" value="${currentUser.projectServerName}" />
                            <tr style="display:none">
	                       		<td class="ab_left"><label class="adShopKey"><span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name=infoKey tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="30" value = "${vpsAdRegion.infoKey}" />
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
	                       		<td class="ab_left"><label class="title">标题：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="title" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="30" value = "${vpsAdRegion.title}" <c:if test= "${vpsAdRegion.isDefault eq '0'}"> readonly ="readonly" </c:if>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>

							<tr>
                                <input type="hidden" name="areaCode" />
                                <input type="hidden" name="areaName" />
                                
                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>
                                <td class="ab_main area" colspan="3">
                                     <div class="content area" style="display: flex; margin: 5px 0px;">
                                         <select name="provinceAry" class="zProvince form-control input-width-normal required" tag="validate"  <c:if test="${vpsAdRegion.isDefault eq '0'}"> readonly ="readonly" </c:if>></select>
                                         <select name="cityAry" class="zCity form-control input-width-normal required" <c:if test="${vpsAdRegion.isDefault eq '0'}"> readonly ="readonly" </c:if>></select>
                                         <select name="countyAry" class="zDistrict form-control input-width-normal required" <c:if test="${vpsAdRegion.isDefault eq '0'}"> readonly ="readonly" </c:if>></select>
                                         <label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea"></label>
                                     </div>
                                </td>
                                
                               
                            </tr>
							<tr>
	                       		<td class="ab_left"><label class="title">展示时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="stGmt" id="startDate" class="form-control input-width-medium Wdate required preTime" value="${fn:substring(vpsAdRegion.stGmt, 0, 19)}"
                                            tag="validate" autocomplete="off" style="width: 180px !important;" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" <c:if test="${vpsAdRegion.isDefault eq '0'}"> readonly ="readonly" </c:if>/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endGmt" id="endDate" class="form-control input-width-medium Wdate required sufTime" value="${fn:substring(vpsAdRegion.endGmt, 0, 19)}"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" <c:if test="${vpsAdRegion.isDefault eq '0'}"> readonly ="readonly" </c:if>/>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">展示位置：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" name="position" value="0" <c:if test="${vpsAdRegion.position eq '0'}">checked</c:if>/>首页
                                        <input type="radio" name="position" value="1" <c:if test="${vpsAdRegion.position eq '1'}">checked</c:if> />商城
                                        <input type="radio" name="position" value="2" <c:if test="${vpsAdRegion.position eq '2'}">checked</c:if> />任务
                                    </div>
                                </td>
                            </tr>
                            <tr id = "sectionGroupTr" <c:if test="${!fn:startsWith(currentUser.projectServerName, 'mengniu') }">STYLE="display: none" </c:if>>
                                <td class="ab_left"><label class="title">所属栏目：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <select name="sectionGroup" id="sectionGroup" class = "form-control input-width-medium required" tag="validate">
                                            <option value="0" <c:if test="${vpsAdRegion.sectionGroup eq '0'}">selected</c:if> >热门活动</option>
                                            <option value="1" <c:if test="${vpsAdRegion.sectionGroup eq '1'}">selected</c:if> >京东商城</option>
                                            <option value="2" <c:if test="${vpsAdRegion.sectionGroup eq '2'}">selected</c:if> >冻鲜荟</option>
                                            <option value="3" <c:if test="${vpsAdRegion.sectionGroup eq '3'}">selected</c:if> >沃尔玛</option>
                                            <option value="4" <c:if test="${vpsAdRegion.sectionGroup eq '4'}">selected</c:if> >小象超市</option>
                                        </select>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">精准触达人群：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='0' <c:if test="${vpsAdRegion.crowdLimitType eq '0'}">checked</c:if>/>不限制</label>
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='1' <c:if test="${vpsAdRegion.crowdLimitType eq '1'}">checked</c:if>/>黑名单及可疑用户不可领取</label>
                                        <c:if test="${not empty(groupList)}">
                                            <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='2' <c:if test="${vpsAdRegion.crowdLimitType eq '2'}">checked</c:if>/>指定群组参与</label>
                                            <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='3' <c:if test="${vpsAdRegion.crowdLimitType eq '3'}">checked</c:if>/>指定群组不可参与</label>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${not empty(groupList)}"> 
                                <tr id="userGroupTr" <c:if test="${vpsAdRegion.crowdLimitType eq '0' or vpsAdRegion.crowdLimitType eq '1'}"> style="display: none;" </c:if>>
                                    <td class="ab_left"><label class="title"></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div>
                                            <c:forEach items="${groupList }" var="group">
                                                <c:set var="checkedStr" value=""></c:set>
                                                <c:forEach items="${fn:split(vpsAdRegion.userGroupIds, ',')}" var='groupId'>
                                                    <c:if test="${groupId eq group.id}">
                                                        <c:set var="checkedStr" value="checked='checked'"></c:set>
                                                    </c:if>
                                                </c:forEach>
                                                <label class="blocker en-larger"><input name="userGroupIds" type="checkbox" value='${group.id }' ${checkedStr}/>${group.name}</label>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="ab_left"><label class="title">活动入口图：<span class="required">*</span>
                                    <c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="mengniuzhiTips blocker en-larger" style="color: #b81900">尺寸建议宽度346px，高度不限，常用高度为221px</span></c:if>
                                </label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=16"
                                               target= "my_iframe" class="addImg1"  onclick="clickImg('picUrl');" style="z-index: 3;opacity: 0;"></a>
                                            <img class="addImg1"  id = "picUrl" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
                                            <input type="hidden" name="picUrl">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                            <tr>
	                       		<td class="ab_left"><label class="sequenceNo">顺序号：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="sequenceNo" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="2" value = "${vpsAdRegion.sequenceNo}" style="width: 120px;"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
								<input name="picWidth" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10" placeholder = "宽"  type="hidden" value = "${vpsAdRegion.picWidth}"/>
								<input name="picHeight" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "高" type="hidden" value = "${vpsAdRegion.picHeight}"/>
								<input name="picX" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "坐标x"type="hidden" value = "${vpsAdRegion.picX}"/>
								<input name="picY" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10" placeholder = "坐标y" type="hidden" value = "${vpsAdRegion.picY}"/>
							</tr>
							<input type="hidden" name="btnName" id="btnName" value="">
                           <tr <c:if test= "${vpsAdRegion.isDefault eq '0'}"> STYLE="display: none" </c:if>>
                                <td class="ab_left"><label class="title">跳转：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="0" checked="checked"/>无</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="1" />URL链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="2" />图片链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="3" />第三方小程序链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="4" />小程序内部链接</label>
                                    </div>
                                </td>
                            </tr>
                            <tr class="jumpurl" style="display: none;">
                                <td class="ab_left"><label class="title">跳转链接：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="appid" class="form-control input-width-larger required" autocomplete="off" style="display:none;" placeholder="小程序appid"/>
                                        <input name="jumpUrl" class="form-control required" autocomplete="off" style="width: 600px;display:none;" placeholder="跳转连接"/>
                                    </div>
                                </td>
                            </tr>
                            <tr id = "picJumpUrlTr" style="display:none">
                                <td class="ab_left"><label class="title">跳转图片：<span class="required">*</span>
                                    <c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="mengniuzhiTips blocker en-larger" style="color: #b81900">尺寸建议宽度750px，高度不限</span></c:if>
                                </label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=16"  id="picJumpUrl1" target= "my_iframe" class="addImg1"  onclick="clickImg('picJumpUrl');"
                                                    <c:choose>
                                                        <c:when test="${vpsAdRegion.jumpTyp == '2'}">style="z-index: 3; opacity: 0;"</c:when>
                                                        <c:otherwise>style="z-index: 3; opacity: 0; display: none"</c:otherwise>
                                                    </c:choose>
                                               style="z-index: 3; opacity: 0; display: none"></a>
                                            <img class="addImg1"  id = "picJumpUrl"  src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20"  <c:if test= "${vpsAdRegion.jumpTyp ne '2'}"> style="display:none" </c:if>>
                                            <input type="hidden" name="picJumpUrl" value="${vpsAdRegion.picJumpUrl}">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                            <tr id = "canDoubleJump" style="display:none">
                                <td class="ab_left"><label class="title">图片是否可跳转：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="canDoubleJump" type="radio" value='0'/>否</label>
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="canDoubleJump" type="radio" value='1'/>是</label>
                                    </div>
                                </td>
                            </tr>
                            <tr id = "doubleJumpBtnPic" style="display:none">
                                <td class="ab_left"><label class="title">二级跳转按钮：<span class="required">*</span>
                                    <c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="mengniuzhiTips blocker en-larger" style="color: #b81900">尺寸建议宽度750px，高度不限，展示在页面底部</span></c:if>
                                </label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=16"  target= "my_iframe" class="addImg1"  onclick="clickImg('doubleJumpBtnUrl');" style="z-index: 3;opacity: 0;"></a>
                                            <img class="addImg1" id="doubleJumpBtnUrl" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
                                            <input type="hidden" name="doubleJumpBtnUrl">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>
                            <tr id = "doubleJumpType" style="display:none">
                                <td class="ab_left"><label class="title">二级跳转类型：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="doubleJumpType" value="1" checked="checked"/>URL链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="doubleJumpType" value="2" />图片链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="doubleJumpType" value="3" />第三方小程序链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="doubleJumpType" value="4" />小程序内部链接</label>
                                    </div>
                                </td>
                            </tr>
                            <tr id="doubleJumpUrl" style="display: none;">
                                <td class="ab_left"><label class="title">二级跳转链接：<span class="required">*</span></label>
                                </td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input name="doubleJumpAppid" class="form-control input-width-larger required" autocomplete="off" style="display:none;" placeholder="小程序appid"/>
                                        <input name="doubleJumpUrl" class="form-control required" autocomplete="off" style="width: 600px;display:none;" placeholder="跳转连接"/>
                                    </div>
                                </td>
                            </tr>
                            <tr id = "doubleJumpPic" style="display:none">
                                <td class="ab_left"><label class="title">二级跳转图片：<span class="required">*</span>
                                    <c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="mengniuzhiTips blocker en-larger" style="color: #b81900">尺寸建议宽度750px，高度不限</span></c:if>
                                </label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <div class="item">
                                            <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=16"  target= "my_iframe" class="addImg1"  onclick="clickImg('doubleJumpPicUrl');" style="z-index: 3;opacity: 0;"></a>
                                            <img class="addImg1" id="doubleJumpPicUrl" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
                                            <input type="hidden" name="doubleJumpPicUrl">
                                        </div>
                                        <div style="clear: left;"></div>
                                    </div>
                                </td>
                            </tr>


                            <div class="modal fade" id="addBatchDialog" tabindex="-1"  data-backdrop="static" aria-labelledby="myModalLabel" aria-hidden="false">
								<div class="modal-dialog">
									<div class="modal-content" style="top:1%;">
										<div class="modal-header">
											<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
											<h4 class="modal-title" >选择素材</h4>
										</div>
										<div class="modal-body1">
											<iframe class="frame-body" name="my_iframe" src="" scrolling="y"></iframe>
										</div>
										<div class="modal-footer">
											<button type="button" id="comBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
											<button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
										</div>
									</div>
								</div>
							</div>
							<tr>

						</table>
					</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >确认发布</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/actRegion/showActRegionList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
