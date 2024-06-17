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

			// 区域类型选择
			$("input[name='activityScopeType']").on("change", function(){
				if($(this).attr("id") == "activityScopeType0"){
					$("#areaTrId").css("display", "");
					$("#hotAreaTrId").css("display", "none");
				}else{
					$("#areaTrId").css("display", "none");
					$("#hotAreaTrId").css("display", "");
				}
			});



			$("#picUrl").attr("src",src='https://img.vjifen.com/images/vma/'+'${vpsAdPub.picUrl}');
            $("input[name= picUrl]").val('${vpsAdPub.picUrl}');
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
            
            // 触达用户类型
            $("input[name=crowdLimitType]").on("click",function(){
                if($(this).val() == '2' || $(this).val() == '3'){
                    $("#userGroupTr").css("display","");
                }else{
                    $("#userGroupTr").css("display","none");
                }
            });

			// 弹窗显示位置为小程序商城首页sku不可选
			$("input[name=adLoc]").on("click",function(){
				if($(this).val() == '4' || $(this).val() == '5'){
					$("#skuKey").attr("disabled", "disabled");
					$("#popNumType").text("每天弹出次数");
					$("#repeateCodeShow").attr("disabled", "disabled");
					$("#repeateCodeShow1").attr("disabled", "disabled");
				}else{
					$("#skuKey").removeAttr("disabled");
					$("#popNumType").text("单人单天扫码弹出阶梯");
					$("#repeateCodeShow").removeAttr("disabled");
					$("#repeateCodeShow1").removeAttr("disabled");
				}
				if ($(this).val() == '1'){
					$("#frontDelayed").attr("disabled", "disabled");
				}else {
					$("#frontDelayed").removeAttr("disabled");
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
			//
			$("[name='closeType']").on("change", function(){
				if($(this).val() == '1') {
					$(this).closest("div").find("select[name='closeTime']").attr("disabled", "disabled").val("");
				} else {
					$(this).closest("div").find("select[name='closeTime']").removeAttr("disabled");
				}
			});
			// 增加时间
			$("form").on("click", "#addTime", function() {
				var idx = $("td.time div").index($(this).parent("div.time"));
				if (idx == 0) {
					var count = $("td.time div").length;
					if(count < 10){
						var $timeCopy = $("div.time").eq(0).clone(true);
						$("td.time").append($timeCopy);
						$timeCopy.find("#addTime").text("删除");
						var $beginTime = $("td.time div.time:last").find("input[name='beginTime']");
						var $endTime = $("td.time div.time:last").find("input[name='endTime']");
						$beginTime.attr("id", "beginTime" + count).val("00:00:00");
						$endTime.attr("id", "endTime" + count).val("23:59:59");
						$beginTime.attr("onfocus", $beginTime.attr("onfocus").replace("endTime0", "endTime" + count));
						$endTime.attr("onfocus", $endTime.attr("onfocus").replace("beginTime0", "beginTime" + count));
					}
				} else {
					$(this).parent("div.time").remove();
				}
			});

			// 增加SKU
			$("form").on("click", "#addSku", function() {
				if ($(this).is("[disabled='disabled']")) return;
				if ($(this).text() == '新增') {
					var $copySku = $(this).closest("div").clone(true, true);
					$copySku.find("option:first").prop('selected', 'selected');
					$copySku.find("#addSku").text("删除");
					$(this).closest("td").append($copySku);

				} else {
					$(this).closest("div").remove();
				}
			});
			// 初始化区域事件
			$("#addArea").closest("div").initZone("<%=cpath%>", true, "", true, true, false, true);
			// 增加区域
			$("form").on("click", "#addArea", function() {
				if ($(this).is("[disabled='disabled']")) return;
				if ($(this).text() == '新增') {
					var $copyArea = $(this).closest("div").clone();
					$copyArea.initZone("<%=cpath%>", true, "", true, false, false, true);
					$copyArea.find("#addArea").text("删除");
					$(this).closest("td").append($copyArea);

				} else {
					$(this).closest("div").remove();
				}
			});

			//区域初始化
			var activityScopeType = "${vpsAdPub.areaType}";
			console.log("===========>activityScopeType-" + activityScopeType);
			if (activityScopeType === "1") {
				$("div.area").initZone("<%=cpath%>", false, "", false, true, false, true);
			} else {
				var filterAreaCodeAry = "${vpsAdPub.areaCode}".split(",");
				console.log("===========>初始化省市县  filterAreaCodeAry:" + filterAreaCodeAry);
				$.each(filterAreaCodeAry, function (idx, val, ary) {
					console.log("===========>初始化省市县  val:" + val);
					if (val != '') {
						if (val == '000000') {
							$("td.area div:last-child").find("select").val(val);
						} else {
							if (idx > 0) $("#addArea").click();
							$("td.area div:last-child").initZone("<%=cpath%>", false, val, false, true, false, true);
						}
					}
				});
			}


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
                if ($(this).val() == '4' || $(this).val() == '5') {
                    $("tr.jumpurl").show();
                    $("tr.jumpurl").find("input[name='jumpUrl']").show();
                }
            });
            $("[name='jumpTyp'][value='${vpsAdPub.jumpTyp}']").attr("checked", "checked").change();
            
            // 初始化数据显示
            var jumpType = '${vpsAdPub.jumpTyp}';
            if (jumpType == "2") {
	            $("#picJumpUrl").attr("src",src='https://img.vjifen.com/images/vma/'+'${vpsAdPub.picJumpUrl}');
	            $("input[name= picJumpUrl]").val('${vpsAdPub.picJumpUrl}');
            } else if (jumpType == "1") {
            	$("tr.jumpurl").find("input[name='jumpUrl']").val('${vpsAdPub.jumpUrl}');
            } else if (jumpType == "3" || jumpType == "4" ) {
                $("tr.jumpurl").find("input[name='appid']").val('${vpsAdPub.appid}');
                $("tr.jumpurl").find("input[name='jumpUrl']").val('${vpsAdPub.jumpUrl}');
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

			//活动范围为热区时的校验
			var activityScopeType = $('input[name="activityScopeType"]:checked').val();
			$("input[name='areaType']").val(activityScopeType);
			if (activityScopeType === "1"){
				var selectedValue = $('select[name="hotAreaKey"]').find('option:selected').val();
				var selectedText = $('select[name="hotAreaKey"]').find('option:selected').text();
				console.log("===========>活动范围为热区时的校验:"+selectedValue+"-"+selectedText);
				if (selectedValue === "") {
					$.fn.alert("当前活动范围类型为热区，请选则一个热区作为活动范围");
					return false;
				}
				$("input[name='areaCode']").val(selectedValue);
				$("input[name='areaName']").val(selectedText);
			}else{
				// 组建筛选区域
				var areaCode = "";
				var areaName = "";
				$("td.area div.area").each(function (i) {
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
					$.fn.alert("当前区域类型为区域，请筛选行政区域作为活动范围");
					return false;
				}
			}



			if($('input[name="picUrl"]').val() == ""){
				alert("请上传图片");
				return false;
			}
			if(areaCode == ""){
				alert("请筛选区域！")
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
            	action="<%=cpath %>/adPub/adUpEdit.do">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>修改弹窗广告</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr style="display:none">
	                       		<td class="ab_left"><label class="adPubKey"><span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name=adPubKey tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="30" value = "${vpsAdPub.adPubKey}" />
	                       			</div>
	                       		</td>
	                       	</tr>
                			<tr>
	                       		<td class="ab_left"><label class="title">标题：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="title" tag="validate"
	                       					class="form-control required" autocomplete="off" maxlength="30" value = "${vpsAdPub.title}" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<%--<tr>
								<td class="ab_left"><label class="title">弹窗显示位置：<span class="required">*</span></label>
								</td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input type="radio" name="adLoc" value="1" class="tab-radio" checked="checked"
											   style="float:left;"/>
										<span class="blocker en-larger" style="margin-left: 2px;">开红包前</span>
										<input type="radio" name="adLoc" class="tab-radio" value="2"
											   style="float:left;"/>
										<span class="blocker en-larger" style="margin-left: 2px;">开红包后</span>
										<input type="radio" name="adLoc" value="3" class="tab-radio"
											   style="float:left;"/>
										<span class="blocker en-larger" style="margin-left: 2px;">提现后</span>
										<input type="radio" name="adLoc" value="4" class="tab-radio"
											   style="float:left;"/>
										<span class="blocker en-larger" style="margin-left: 2px;">小程序商城首页</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>--%>
							<tr>
								<td class="ab_left"><label class="title">弹窗显示位置：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input type="radio" name="adLoc" value="1"  class="tab-radio"  style="float:left;"<c:if test="${vpsAdPub.adLoc eq '1'}"> checked="checked"</c:if>>
										<span class="blocker en-larger"  style="margin-left: 2px;">拆开前</span>
										<input type="radio" name="adLoc" class="tab-radio" value="2" style="float:left;"<c:if test="${vpsAdPub.adLoc eq '2'}"> checked="checked"</c:if>>
										<span class="blocker en-larger" style="margin-left: 2px;">拆开后</span>
										<input type="radio" name="adLoc" class="tab-radio" value="3" style="float:left;"<c:if test="${vpsAdPub.adLoc eq '3'}"> checked="checked"</c:if>>
										<span class="blocker en-larger" style="margin-left: 2px;">兑付后</span>
										<input type="radio" name="adLoc" class="tab-radio" value="4" style="float:left;"<c:if test="${vpsAdPub.adLoc eq '4'}"> checked="checked"</c:if>>
										<span class="blocker en-larger" style="margin-left: 2px;">小程序商城首页</span>
                                        <c:if test="${currentUser.projectServerName ne 'qinghuafj' && currentUser.projectServerName ne 'laobaifj' && currentUser.projectServerName ne 'zhongliang' && currentUser.projectServerName ne 'hgquqi' && currentUser.projectServerName ne 'yuyuan' && currentUser.projectServerName ne 'zhongLCNY' && currentUser.projectServerName ne 'yinlu' && currentUser.projectServerName ne 'quechao' && currentUser.projectServerName ne 'zhongLJH' && currentUser.projectServerName ne 'mengniu' && currentUser.projectServerName ne 'mengniuzhi' && currentUser.projectServerName ne 'zhongLFQ' }">
                                            <input type="radio" name="adLoc" value="5" class="tab-radio" style="float:left;" <c:if test="${vpsAdPub.adLoc eq '5'}"> checked="checked"</c:if>/>
                                            <span class="blocker en-larger" style="margin-left: 2px;">120周年庆首页</span>
                                        </c:if>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
	                       	<tr>
                                <td class="ab_left"><label class="title">SKU：<span class="required">*</span></label></td>
                                <td class="ab_main sku" colspan="3">
                                    <div class="content sku promotionSku" style="display: flex; margin: 5px 0px; flex-direction: column;">
										<c:if test="${not empty(skuKeyList) }">
											<c:forEach items="${skuKeyList}" var="skuInfo" varStatus="idx">
												<div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
                                                    <c:choose>
                                                        <c:when test="${vpsAdPub.adLoc eq '4' or vpsAdPub.adLoc eq '5'}">
                                                            <select class="form-control input-width-larger required" id="skuKey" disabled name="skuKey" tag="validate">
                                                                <option value="">请选择SKU</option>
                                                                <c:if test="${!empty skuList}">
                                                                    <c:forEach items="${skuList}" var="sku">
                                                                        <option value="${sku.skuKey}"
                                                                                <c:if test="${sku.skuKey eq skuInfo}"> selected="selected" </c:if>>
                                                                                ${sku.skuName}
                                                                        </option>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </select>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <select class="form-control input-width-larger required" id="skuKey" name="skuKey" tag="validate">
                                                                <option value="">请选择SKU</option>
                                                                <c:if test="${!empty skuList}">
                                                                    <c:forEach items="${skuList}" var="sku">
                                                                        <option value="${sku.skuKey}"
                                                                                <c:if test="${sku.skuKey eq skuInfo}"> selected="selected" </c:if>>
                                                                                ${sku.skuName}
                                                                        </option>
                                                                    </c:forEach>
                                                                </c:if>
                                                            </select>
                                                        </c:otherwise>
                                                    </c:choose>


													<c:if test="${idx.index eq 0 }">
														<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">新增</label>
													</c:if>
													<c:if test="${idx.index > 0 }">
														<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">删除</label>
													</c:if>
													<label class="validate_tips"></label>
												</div>
											</c:forEach>
										</c:if>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>

							<%--活动范围--%>
							<tr id="activityScopeTrId">
								<input type="hidden" name="areaCode"/>
								<input type="hidden" name="areaName"/>
								<input type="hidden" name="areaType"/>
								<td class="ab_left"><label class="title">区域类型：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<%--默认选中区域--%>
									<c:if test="${empty(vpsAdPub.areaType) or vpsAdPub.areaType=='0'}">
										<div class="content">
											<input type="radio" class="radio" id="activityScopeType0" name="activityScopeType" value="0" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
											<span class="blocker en-larger">区域</span>
											<input type="radio" class="radio" id="activityScopeType1" name="activityScopeType" value="1" style="float:left; cursor: pointer; min-height: 33px;" />
											<span class="blocker en-larger">热区</span>
											<label class="validate_tips"></label>
										</div>
									</c:if>
									<c:if test="${not empty(vpsAdPub.areaType) and vpsAdPub.areaType=='1'}">
										<div class="content">
											<input type="radio" class="radio" id="activityScopeType0" name="activityScopeType" value="0" style="float:left; cursor: pointer; min-height: 33px;" />
											<span class="blocker en-larger">区域</span>
											<input type="radio" class="radio" id="activityScopeType1" name="activityScopeType" value="1" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
											<span class="blocker en-larger">热区</span>
											<label class="validate_tips"></label>
										</div>
									</c:if>
								</td>
							</tr>
							<!-- 公共行政区域tr -->
							<tr id="areaTrId" <c:if test="${not empty(vpsAdPub.areaType) and vpsAdPub.areaType=='1'}">style="display: none" </c:if>>
								<td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>
								<td class="ab_main area" colspan="3">
									<div class="area" style="display: flex; margin: 5px 0px;">
										<select name="provinceAry" class="zProvince form-control input-width-normal"></select>
										<select name="cityAry" class="zCity form-control input-width-normal"></select>
										<select name="countyAry" class="zDistrict form-control input-width-normal"></select>
										<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addArea">新增</label>
									</div>
								</td>
							</tr>
							<%--限定热区--%>
							<tr id="hotAreaTrId"<c:if test="${empty(vpsAdPub.areaType) or vpsAdPub.areaType=='0'}">style="display: none" </c:if>>
								<td class="ab_left"><label class="title mart5">限定热区：<span class="required">*</span></label></td>
								<td class="ab_main">
									<c:choose>
										<c:when test="${vpsAdPub.areaCode != null}">
											<div class="content hot">
												<select name="hotAreaKey" class="hotArea form-control" style="width: 480px;">
													<option value="">请选择</option>
													<c:if test="${not empty(hotAreaList)}">
														<c:forEach items="${hotAreaList}" var="hotArea">
															<option value="${hotArea.hotAreaKey }" <c:if test="${hotArea.hotAreaKey == vpsAdPub.areaCode }">selected="selected"</c:if>>${hotArea.hotAreaName}</option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</c:when>
										<c:otherwise>
											<div class="content hot">
												<select id="hotAreaKey" name="hotAreaKey" class="hotArea form-control" style="width: 352px;">
													<option value="">请选择</option>
													<c:if test="${not empty(hotAreaList)}">
														<c:forEach items="${hotAreaList}" var="hotArea">
															<option value="${hotArea.hotAreaKey }">${hotArea.hotAreaName}</option>
														</c:forEach>
													</c:if>
												</select>
											</div>
										</c:otherwise>
									</c:choose>
								</td>
							</tr>

<%--							<tr>--%>
<%--                                <input type="hidden" name="areaCode" />--%>
<%--                                <input type="hidden" name="areaName" />--%>

<%--                                <td class="ab_left"><label class="title">筛选区域：<span class="required">*</span></label></td>--%>
<%--                                <td class="ab_main area" colspan="3">--%>
<%--                                     <div class="content area" style="display: flex; margin: 5px 0px;">--%>
<%--                                         <select name="provinceAry" class="zProvince form-control input-width-normal required" tag="validate"></select>--%>
<%--                                         <select name="cityAry" class="zCity form-control input-width-normal required"></select>--%>
<%--                                         <select name="countyAry" class="zDistrict form-control input-width-normal required"></select>--%>
<%--										 <label class=" title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;display: block !important" id="addArea">新增</label>--%>
<%--									 </div>--%>
<%--								</td>--%>
<%--                            </tr>--%>
                			<tr>
	                       		<td class="ab_left"><label class="title">展示日期：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<span class="blocker">从</span>
                                        <input name="stGmt" id="startDate" class="form-control input-width-medium Wdate required preTime" value="${fn:substring(vpsAdPub.stGmt, 0, 19)}"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                        <span class="blocker en-larger">至</span>
                                        <input name="endGmt" id="endDate" class="form-control input-width-medium Wdate required sufTime" value="${fn:substring(vpsAdPub.endGmt, 0, 19)}"
                                            tag="validate" style="width: 180px !important;" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
								<input type="hidden" name="validTimeRange" />
								<td class="ab_left"><label class="title mart5">时间范围：<span class="required">*</span></label></td>
								<td class="ab_main time">
									<c:forEach items="${fn:split(vpsAdPub.beginTime,',')}" var="validTimeRange" varStatus="idx">
										<div class="content time" style="margin: 5px 0px; display: flex;">
											<input name="beginTime" id="beginTime${idx.index}" class="form-control input-width-medium required Wdate"  autocomplete="off"
												   tag="validate" onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'endTime${idx.index}\')}'})" value="${validTimeRange}"/>
											<span class="blocker en-larger">至</span>
											<input name="endTime" id="endTime${idx.index}" class="form-control input-width-medium required Wdate"  autocomplete="off"
												   tag="validate" onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'beginTime${idx.index}\')}'})" value="${fn:split(vpsAdPub.endTime,',')[idx.index]}"/>
											<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addTime"><c:choose><c:when test="${idx.index eq 0}">新增</c:when><c:otherwise>删除</c:otherwise></c:choose></label>
											<label class="validate_tips"></label>
										</div>
									</c:forEach>
								</td>
							</tr>
                            <tr>
                                <td class="ab_left"><label class="title">精准触达人群：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='0' <c:if test="${vpsAdPub.crowdLimitType eq '0'}">checked</c:if>/>不限制</label>
                                        <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='1' <c:if test="${vpsAdPub.crowdLimitType eq '1'}">checked</c:if>/>黑名单用户不可领取</label>
                                        <c:if test="${not empty(groupList)}">
                                            <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='2' <c:if test="${vpsAdPub.crowdLimitType eq '2'}">checked</c:if>/>指定群组参与</label>
                                            <label class="blocker en-larger" style="margin-left: 2px;"><input name="crowdLimitType" type="radio" value='3' <c:if test="${vpsAdPub.crowdLimitType eq '3'}">checked</c:if>/>指定群组不可参与</label>
                                        </c:if>
                                    </div>
                                </td>
                            </tr>
                            <c:if test="${not empty(groupList)}"> 
                                <tr id="userGroupTr" <c:if test="${vpsAdPub.crowdLimitType eq '0' or vpsAdPub.crowdLimitType eq '1'}"> style="display: none;" </c:if>>    
                                    <td class="ab_left"><label class="title"></label></td>
                                    <td class="ab_main" colspan="3">
                                        <div>
                                            <c:forEach items="${groupList }" var="group">
                                                <c:set var="checkedStr" value=""></c:set>
                                                <c:forEach items="${fn:split(vpsAdPub.userGroupIds, ',')}" var='groupId'>
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
                                <td class="ab_left"><label class="title">启用状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
									      <input name="pubStat" type="hidden" value="${vpsAdPub.pubStat}" />
                                        <input  id="pubStat" type="checkbox" <c:if test="${vpsAdPub.pubStat eq '1'}">checked</c:if> data-size="small" data-on-text="启用" data-off-text="停用" data-on-color="success" data-off-color="warning"/>
                                    </div>
                                </td>
                            </tr>
							<tr>
								<td class="ab_left"><label class="title">前置延时：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
                                       <c:choose>
										   <c:when test="${vpsAdPub.adLoc eq '1' }">
											   <input disabled name="frontDelayed"  id="frontDelayed" tag="validate"
													  class="form-control required number minValue maxValue" style="width: 500px;" autocomplete="off" maxlength="30" minVal="0.00" maxVal="5.00" value="${vpsAdPub.frontDelayed}"/>
											   <label class="validate_tips"></label>
										   </c:when>
										   <c:otherwise>
											   <input  name="frontDelayed"  id="frontDelayed" tag="validate"
													  class="form-control required number minValue maxValue" style="width: 500px;" autocomplete="off" maxlength="30" minVal="0.00" maxVal="5.00" value="${vpsAdPub.frontDelayed}"/>
											   <label class="validate_tips"></label>
										   </c:otherwise>
									   </c:choose>
									</div>
								</td>
							</tr>
                            <tr>
								<c:choose>
									<c:when test="${vpsAdPub.adLoc eq '4' or vpsAdPub.adLoc eq '5'}">
										<td id="popNumType" class="ab_left"><label class="title">每日弹出次数：<span class="white">*</span></label></td>
									</c:when>
									<c:otherwise>
										<td id="popNumType" class="ab_left"><label class="title">单人单天扫码弹出阶梯：<span class="white">*</span></label></td>
									</c:otherwise>
								</c:choose>

                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input tag="validate" name="popNum" tag="validate"
                                               class="form-control" style="width: 500px;" autocomplete="off" value="${vpsAdPub.popNum}"  maxlength="100" placeholder = "空表示不限制" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
							<tr>
								<td class="ab_left"><label class="title">广告顺序号：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input  tag="validate" name="sequenceno" tag="validate"
											   class="form-control input-width-small required" autocomplete="off" maxlength="2" value="${vpsAdPub.sequenceno}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">弹窗分组：</label></td>
								<td class="ab_main" colspan="3">
									<div  class="content">
										<input name="popGroupName"  value="${vpsAdPub.popGroupName}"
											   class="form-control " style="width: 500px;" autocomplete="off" maxlength="30" />
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">当前弹窗弹出次数上限：</label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="popNumLimit" value="${vpsAdPub.popNumLimit}"
											   class="form-control input-width-small " autocomplete="off" maxlength="9" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" />
										<span  class="blocker en-larger">(备注：分组内的弹窗轮播弹出)</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
	                       <tr>
                                <td class="ab_left"><label class="title">跳转：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="0" checked="checked"/>无</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="1" />URL链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="2" />图片链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="3" />第三方小程序链接</label>
                                        <label class="blocker en-larger" style="margin-left: 2px; float: left; display: block;"><input type="radio" name="jumpTyp" value="4" />小程序内部链接</label>
										<a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=12"  id="picJumpUrl1" target= "my_iframe" class="addImg"  onclick="clickImg('picJumpUrl');"
												<c:choose>
													<c:when test="${vpsAdPub.jumpTyp == '2'}">style="z-index: 3; opacity: 0;"</c:when>
													<c:otherwise>style="z-index: 3; opacity: 0; display: none"</c:otherwise>
												</c:choose>
										   style="z-index: 3; opacity: 0; display: none"></a>
										<img class="addImg"  id = "picJumpUrl"  src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20"  <c:if test= "${vpsAdPub.jumpTyp ne '2'}"> style="display:none" </c:if>>
										<input type="hidden" name="picJumpUrl" value="${vpsAdPub.picJumpUrl}">
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
								<td class="ab_left"><label class="title">弹窗关闭方式：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input type="radio" class="tab-radio" name="closeType" value="1" style="float:left;" <c:if test= "${vpsAdPub.closeType eq '1'}"> checked="checked" </c:if>>
										<span class="blocker en-larger" style="margin-left: 2px;">手动关闭</span>
										<input type="radio" class="tab-radio" name="closeType" value="2" style="float:left; margin-left: 10px !important;"  <c:if test= "${vpsAdPub.closeType eq '2'}"> checked="checked" </c:if>>
										<span class="blocker en-larger" style="margin-left: 2px;">自动关闭</span>
										<select name="closeTime" class="form-control required"  tag="validate"  autocomplete="off" style="width: 90px;"<c:if test= "${vpsAdPub.closeType eq '1'}"> disabled="disabled" </c:if>>
<%--										<option value=""<c:if test="${vpsAdPub.closeTime eq ''}"> selected="selected"</c:if>>请选择</option>--%>
	                                    <option value="3"<c:if test="${vpsAdPub.closeTime eq '3'}"> selected="selected"</c:if>>3秒(默认)</option>
										<option value="1"<c:if test="${vpsAdPub.closeTime eq '1'}"> selected="selected"</c:if>>1秒</option>
										<option value="2"<c:if test="${vpsAdPub.closeTime eq '2'}"> selected="selected"</c:if>>2秒</option>
										<option value="4"<c:if test="${vpsAdPub.closeTime eq '4'}"> selected="selected"</c:if>>4秒</option>
										<option value="5"<c:if test="${vpsAdPub.closeTime eq '5'}"> selected="selected"</c:if>>5秒</option>
										<label class="validate_tips"></label>
										</select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">重复扫码是否弹出广告：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<c:choose>
											<c:when test="${vpsAdPub.adLoc eq '4' or vpsAdPub.adLoc eq '5'}">
												<input disabled id="repeateCodeShow" type="radio" name="repeateCodeShow" value="1"  class="tab-radio"  style="float:left;"<c:if test="${vpsAdPub.repeateCodeShow eq '1'}"> checked="checked"</c:if>>
												<span class="blocker en-larger"  style="margin-left: 2px;">否</span>
												<input disabled id="repeateCodeShow1" type="radio" name="repeateCodeShow" class="tab-radio" value="2" style="float:left;"<c:if test="${vpsAdPub.repeateCodeShow eq '2'}"> checked="checked"</c:if>>
												<span class="blocker en-larger" style="margin-left: 2px;">是</span>
												<label class="validate_tips"></label>
											</c:when>
											<c:otherwise>
												<input id="repeateCodeShow"  type="radio" name="repeateCodeShow" value="1"  class="tab-radio"  style="float:left;"<c:if test="${vpsAdPub.repeateCodeShow eq '1'}"> checked="checked"</c:if>>
												<span class="blocker en-larger"  style="margin-left: 2px;">否</span>
												<input id="repeateCodeShow1" type="radio" name="repeateCodeShow" class="tab-radio" value="2" style="float:left;"<c:if test="${vpsAdPub.repeateCodeShow eq '2'}"> checked="checked"</c:if>>
												<span class="blocker en-larger" style="margin-left: 2px;">是</span>
												<label class="validate_tips"></label>
											</c:otherwise>
										</c:choose>

									</div>
								</td>
							</tr>
							<input type="hidden" name="btnName" id="btnName" value="">
							<tr>
								<td class="ab_left"><label class="title">选择素材：<span class="required">*</span>
									<c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="blocker en-larger" style="color: #b81900">尺寸建议600*790px</span></c:if>
								</label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<div class="item">
											<a href="<%=cpath %>/picLib/picLibList.do?vjfSessionId=${vjfSessionId}&group=8" target= "my_iframe" class="addImg1"  onclick="clickImg('picUrl');" style="z-index: 3;opacity: 0;"></a>
											<img class="addImg1"  id = "picUrl"  onclick="clickImg(this);" src="<%=cpath %>/inc/vpoints/img/a12.png" width="20" height="20" />
											<input type="hidden" name="picUrl">
										</div>
										<div style="clear: left;"></div>
									</div>
								</td>
							</tr>
							<tr>
								<input name="picWidth" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10" placeholder = "宽"  type="hidden" value = "${vpsAdPub.picWidth}"/>
								<input name="picHeight" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "高" type="hidden" value = "${vpsAdPub.picHeight}"/>
								<input name="picX" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10"  placeholder = "坐标x"type="hidden" value = "${vpsAdPub.picX}"/>
								<input name="picY" tag="validate"
									   class="form-control required" autocomplete="off" maxlength="10" placeholder = "坐标y" type="hidden" value = "${vpsAdPub.picY}"/>
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
<%--					    	<button class="btn btn-red btnSave" >预览</button>&nbsp;&nbsp;&nbsp;&nbsp;--%>
					    	<button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/adPub/showAdUpList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
