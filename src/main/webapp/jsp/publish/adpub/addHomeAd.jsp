<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>新建首页轮播广告</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
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
		$("#btnName").val(id);
	}
	var clickImg1 = function(obj){
		$("#addBatchDialog1").modal("show");
		parent.$("#my_iframe").contents().find("#queryTY").click();
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

            // 触达人群类型
            $("input[name=crowdLimitType]").on("click",function(){
                if($(this).val() == '2' || $(this).val() == '3'){
                    $("#userGroupTr").css("display","");
                }else{
                    $("#userGroupTr").css("display","none");
                }
            });
            
            $("#status").bootstrapSwitch({  
                onSwitchChange:function(event,state){  
                    if(state==true){  
                       $("input:hidden[name='status']").val("1");
                    }else{  
                       $("input:hidden[name='status']").val("0");
                    }
                }
            });
            // 初始化区域事件
            $("#addArea").closest("div").initZone("<%=cpath%>", true, "", true, true, false, true);
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
            $("[name='jumpTyp']").on("change", function () {
                // 跳转连接
                $("tr.jumpurl").css("display","none");
                $("tr.jumpurl input").val('').hide();
                
                // 图片
                $("#picJumpUrl1").css("display","none");
                $(this).closest("div").find("#picJumpUrl").hide();
                $(this).closest("div").find("input[name='picJumpUrl']").val('');
                $(this).closest("div").find("#picJumpUrl").attr("src", "<%=cpath %>/inc/vpoints/img/a12.png")
                
                // 连接
                if ($(this).val() == '1') {
                    $("tr.jumpurl").show();
                    $("tr.jumpurl").find("input[name='jumpUrl']").show();
                }
                // 图片
                if ($(this).val() == '2') {
                    $("#picJumpUrl1").css("display","initial");
                    $(this).closest("div").find("#picJumpUrl").css("display","initial");
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
            	action="<%=cpath %>/adPub/doAdHomeAdd.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>新增首页轮播广告</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">标题：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="title" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="30" />
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
                                         <select name="provinceAry" class="zProvince form-control input-width-normal required" tag="validate"></select>
                                         <select name="cityAry" class="zCity form-control input-width-normal required"></select>
                                         <select name="countyAry" class="zDistrict form-control input-width-normal required"></select>
                                         <label class="validate_tips title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea"></label>
                                     </div>
                                </td>
                            </tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">展示时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="stGmt" id="startDate" class="form-control input-width-medium Wdate required preTime"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endGmt" id="endDate" class="form-control input-width-medium Wdate required sufTime"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">精准触达人群：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='0' checked="checked"/>不限制</label>
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='1'/>黑名单用户不可领取</label>
                                        <c:if test="${not empty(groupList)}">
	                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='2'/>指定群组参与</label>
	                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='3'/>指定群组不可参与</label>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${not empty(groupList)}"> 
                                <tr id="userGroupTr" style="display: none;">    
                                    <td class="ab_left"><label class="title"></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div>
                                            <c:forEach items="${groupList }" var="group">
                                                <label class="blocker en-larger"><input name="userGroupIds" type="checkbox" value='${group.id }'/>${group.name}</label>
                                            </c:forEach>
                                        </div>
                                    </td>
                                </tr>
                            </c:if>
                            <tr>
                                <td class="ab_left"><label class="title">跳转：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="0" checked="checked"/>无</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="1" />URL链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="2" />图片链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="3" />第三方小程序链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="4" />小程序内部链接</label>
                                        <a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=12"  id="picJumpUrl1" target= "my_iframe" class="addImg"  onclick="clickImg('picJumpUrl');" style="z-index: 3; opacity: 0; display: none"></a>
                                        <img class="addImg" id="picJumpUrl" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" style="display:none"/>
                                        <input type="hidden" name="picJumpUrl">
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
                            <tr>
	                       		<td class="ab_left"><label class="sequenceNo">顺序号：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="sequenceNo" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="2"  onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"  style="width: 120px;"/>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
								<input name="picWidth" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10" placeholder = "宽"  type="hidden"/>
								<input name="picHeight" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "高" type="hidden"/>
								<input name="picX" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "坐标x"type="hidden"/>
								<input name="picY" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10" placeholder = "坐标y" type="hidden"/>
							</tr>
							<input type="hidden" name="btnName" id="btnName" value="">
							<tr>
								<td class="ab_left"><label class="title">选择素材：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<div class="item">
											<a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=9"
											   target= "my_iframe" class="addImg1"  onclick="clickImg('picUrl');" style="z-index: 3;opacity: 0;"></a>
											<img class="addImg1"  id = "picUrl"   src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
											<input type="hidden" name="picUrl">
										</div>
										<div style="clear: left;"></div>
									</div>
									<c:choose>
										<c:when test="${projectServerName eq 'mengniuzhi'}">
											<span class="blocker en-larger" style="color: #b81900">蒙牛支码轮播图尺寸建议750*800</span>
										</c:when>
										<c:otherwise>
											<span class="blocker en-larger" style="color: #b81900">河北轮播图尺寸建议750*800</span>
										</c:otherwise>
									</c:choose>
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
											<iframe class="frame-body" name="my_iframe" src="" frameborder="0" scrolling="y"></iframe>
										</div>
										<div class="modal-footer">
											<button type="button" id="comBtn" class="btn btn-default do-add btn-red" data-dismiss="">确 认</button>
											<button type="button" id="closeBtn" class="btn btn-default" data-dismiss="modal">关 闭</button>
										</div>
									</div>
								</div>
							</div>
                		</table>
                	</div>
                	<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" >确认发布</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/adPub/showHomeAdList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
