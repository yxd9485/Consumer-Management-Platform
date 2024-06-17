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
    <title>编辑活动规则页面</title>

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

	<script>
	var basePath='<%=cpath%>';
	var allPath='<%=allPath%>';
	var imgSrc=[];
	var imgSrc1=[];
	var clickImg = function(obj){
		$("#addBatchDialog").modal("show");
		parent.$("#my_iframe").contents().find("#queryTY").click();
		//移除选中项
		$('input:checkbox').removeAttr('checked');
		$("#btnName").val(obj.id);
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

            // 初始化区域事件
            $("#addArea").closest("div").initZone("<%=cpath%>", true, "", true, true, false, true);
            // 兑奖截止类型
            $("[name='jump']").on("change", function(){
                if($(this).val() == '0') {
                    $(this).closest("div").find("input[name='prizeExpireDate']").removeAttr("disabled");
                    $(this).closest("div").find("input[name='jumpUrl']").attr("disabled", "disabled").val("");
                } else {
                    $(this).closest("div").find("input[name='prizeExpireDate']").attr("disabled", "disabled").val("");
                    $(this).closest("div").find("input[name='jumpUrl']").removeAttr("disabled");
                }
            });
			$("#addBatchDialog").delegate("#comBtn", "click", function(){
				$(window.frames["my_iframe"].document).find("input[type=radio]:checked").each(function(){
					var btnName = $("#btnName").val();
					var picWidth = $(this).siblings("div[name = picWidth]").html();
					var picHeight = $(this).siblings("div[name = picHeight]").html();
					var picX = $(this).siblings("div[name = picX]").html();
					var picY = $(this).siblings("div[name = picY]").html();
					var arr=btnName.split("U");
					$("#"+btnName).attr("src",src="https://img.vjifen.com/images/vma/"+ $(this).val());
					$('input[name="'+btnName+'"]').val($(this).val());
					$('input[name="'+arr[0]+'PicWidth"]').val(picWidth);
					$('input[name="'+arr[0]+'PicHeight"]').val(picHeight);
					$('input[name="'+arr[0]+'PicX"]').val(picX);
					$('input[name="'+arr[0]+'PicY"]').val(picY);
				});
				$("#addBatchDialog #closeBtn").trigger("click");
			});

		}
		function validForm() {
            var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			if($('input[name="defaultUrl"]').val() == ""){
				alert("请上传图片");
				return false;
			}
			// if($('input[name="limitUrl"]').val() == ""){
			// 	alert("请上传图片");
			// 	return false;
			// }

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
			left: 0;
			top: 0;
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
            	action="<%=cpath %>/actRule/actRuleAdd.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>新增活动规则</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
							<tr>
								<td class="ab_left"><label class="title">关联活动：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content skuInfo">
										<select name="vcodeActivityKey" tag="validate" class="form-control input-width-larger required activitysku">
											<option value="">请选择</option>
											<c:if test="${!empty activityCogLst}">
												<c:forEach items="${activityCogLst}" var="item">
													<option value="${item.vcodeActivityKey}">${item.vcodeActivityName}</option>
												</c:forEach>
											</c:if>
										</select>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">选择素材：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<div class="item">
											<img class="addImg"  id = "defaultUrl"  onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
											<input type="hidden" name="defaultUrl" >
											<input name="defaultPicWidth" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10" placeholder = "宽"  type="hidden"/>
											<input name="defaultPicHeight" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "高" type="hidden"/>
											<input name="defaultPicX" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "坐标x"type="hidden"/>
											<input name="defaultPicY" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10" placeholder = "坐标y" type="hidden"/>
										</div>
										<div style="clear: left;"></div>
									</div>
								</td>
							</tr>
	                       	<td class="ab_left"><label class="title"><h4 style="font-weight:bold;">限时规则:</h4></label></td>
                			<tr>
	                       		<td class="ab_left"><label class="title">展示时间：<span></span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="limitStGmt" id="limitStGmt" class="form-control input-width-medium Wdate required preTime"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', maxDate:'#F{$dp.$D(\'limitEndGmt\')}'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="limitEndGmt" id="limitEndGmt" class="form-control input-width-medium Wdate required sufTime"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'#F{$dp.$D(\'limitStGmt\')}'})" />

	                       			</div>
	                       		</td>
	                       	</tr>
							<input type="hidden" name="btnName" id="btnName" value="">
							<tr>
								<td class="ab_left"><label class="title">选择素材：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<div class="item">
											<img class="addImg"  id = "limitUrl"  onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
											<input type="hidden" name="limitUrl">
											<input name="limitPicWidth" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10" placeholder = "宽"  type="hidden"/>
											<input name="limitPicHeight" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "高" type="hidden"/>
											<input name="limitPicX" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "坐标x"type="hidden" />
											<input name="limitPicY" tag="validate"
												   class="form-control required" autocomplete="off" maxlength="10" placeholder = "坐标y" type="hidden"/>
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
											<iframe class="frame-body" name="my_iframe" src="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=11" frameborder="0" scrolling="y"></iframe>
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
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/actRule/showActRuleList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
