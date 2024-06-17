<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%
    String cpath = request.getContextPath(); 
	String allPath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+cpath;
    String filePath = PropertiesUtil.getPropertyValue("image_receipts_url");
    String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>添加商品</title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/htmlEdit/dist/summernote.css" rel="stylesheet" type="text/css"/>
<%-- 	<link href="<%=cpath%>/inc/htmlEdit/dist/bootstrap.css" rel="stylesheet" type="text/css"/> --%>
	<script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/summernote.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/htmlEdit/dist/lang/summernote-zh-CN.js"></script>
	
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUpForMany.js?v=1.1.6"></script>
<%-- 	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/showimg.js"></script> --%>

	<link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/demo.css" type="text/css">
	<link rel="stylesheet" href="<%=cpath%>/assets/css/ztree/zTreeStyle/zTreeStyle.css" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.core.js"></script>
	<script type="text/javascript" src="<%=cpath%>/assets/js/ztree/jquery.ztree.excheck.js"></script>

	<script>
	var basePath='<%=cpath%>';
	var allPath='<%=allPath%>';
	var imgSrc=[];
	var setting = {
		check: {
			enable: true,
			chkboxType: { "Y": "p", "N": "ps" }
		},
		data: {
			simpleData: {
				enable: true
			}
		}
	};

	var zNodes = eval('${areaJson}');
		
	$(document).ready(function () {
		// 发货区域初始化
		$.fn.zTree.init($("#tree"), setting, zNodes);
		
		// 初始化校验控件
		$.runtimeValidate($("#code_form"));
		
		initPage();
		$('.summernote').summernote({
	        height: 200,
	        tabsize: 1,
	        lang: 'zh-CN'
	    });
		
		// 一级品类change事件--异步加载级联的二级品类
		$("#categoryParent").change(function(){

    		var categoryParent=document.getElementById("categoryParent").value;
    		if(categoryParent){
    			$.ajax({
    				url:'<%=cpath%>/vpointsGoods/getCategoryByParent.do?parentId='+categoryParent,
    				type: 'POST',
    				dataType:'json',
    					beforeSend:appendVjfSessionId,
                    success: function(data){
    						document.getElementById("categoryType").options.length = 0;
    						$("#categoryType").append("<option value=''>请选择</option>");
    						for (var a = 0; a <data.length; a++) {
    							var obj=data[a];
    							$("#categoryType").append("<option value='"+obj.categoryType+"' data='"+obj.exchangeType+"'>"+obj.categoryName+"</option>");
    						}
    					},
    					error:function(data){
                   			alert('fail');
             			 }
    			});
    		}else{
    			document.getElementById("categoryType").options.length = 0;
				$("#categoryType").append("<option value=''>请选择</option>");
    		}
    	});

		//商品类型 设置为魔盒商品的时候不允许他 选择 礼品卡为是状态
		$("#brandId").change(function () {
			var isGiftCardVla = $("#isGiftCard").val();
			var brandIdVal = $("#brandId").val();
			if(brandIdVal === '28876dae-fde9-11e8-8180-224943c59dfs' && isGiftCardVla==='0'){
				$.fn.alert("魔盒商品不参与礼品卡兑换")
				$("#isGiftCard").val("1");
			}
		});
		$("#isGiftCard").change(function(){
			var isGiftCardVla = $("#isGiftCard").val();
			if(ids3.length==0){
				$.fn.alert("商品活动图片未填写（建议上传白底产品图片）");
				$("#isGiftCard").val("1");
				return;
			}
			//是礼品卡的时候，商品结束日期不能小于七天
			if(isGiftCardVla==='0'){
				var brandIdVal = $("#brandId").val();
				if(brandIdVal === '28876dae-fde9-11e8-8180-224943c59dfs' && isGiftCardVla==='0'){
					$.fn.alert("魔盒商品不参与礼品卡兑换")
					$("#isGiftCard").val("1");
					return
				}
				let goodsEndTime = $("#goodsEndTime").val();
				let goodsStartTime = $("#goodsStartTime").val();
				if(!goodsEndTime || !goodsStartTime){
					$.fn.alert("兑换时间不能为空")
					$("#isGiftCard").val("1");
					return;
				}
				let days = timeDifferenceForDay(new Date(), goodsEndTime,false);
				let startEndTime = timeDifferenceForDay(goodsStartTime, goodsEndTime,true);
				if(!days){
					$.fn.alert("兑换结束时间未填写");
					$("#isGiftCard").val("1");
				}else{
					if(days <= 7 || startEndTime <= 7){
						$.fn.alert("礼品卡活动兑换时间不能小于七天")
						$("#isGiftCard").val("1");
					}

				}

			}
		})
		// 计算两个时间相差的天数
		//type:start属性是否是字符
		function timeDifferenceForDay(start, end, type) {
			var startdate = start;
			if(type){
				start = start.replace(/-/g, "/");
				startdate = new Date(start);
			}
			end = end.replace(/-/g, "/");
			var enddate = new Date(end);
			var time = enddate.getTime() - startdate.getTime();
			var days = parseInt(time / (1000 * 60 * 60 * 24));
			return days;
		}
		// 一级品类change事件--异步加载级联的二级品类
		$("#categoryType").change(function(){
    		var categoryParent=document.getElementById("categoryParent").value;
    		var exchangeType=$("#categoryType").find("option:selected").attr("data");
    		$('#exchangeType').val(exchangeType);
    		if(exchangeType){
    			if(exchangeType==2){
            		$('#goodsValue').attr("disabled",true);
            		$('#goodsValue').val("");
            		$("#goodsValue").parents(".content").find(".validate_tips").text("");
    				$("#goodsValue").parents(".content").find(".validate_tips").removeClass("valid_fail_text");
        			$('#batchKey').attr("disabled",false);  
        			$.ajax({
        				url:'<%=cpath%>/vpointsGoods/getBatchList.do',
        				type: 'POST',
        				dataType:'json',
						beforeSend:appendVjfSessionId,
                    	success: function(data){
        						document.getElementById("batchKey").options.length = 0;
        						$("#batchKey").append("<option value=''>请选择</option>");
        						for (var a = 0; a <data.length; a++) {
        							var obj=data[a];
        							$("#batchKey").append("<option value='"+obj.batchKey+"'>"+obj.batchName+"</option>");
        						}
        					},
        					error:function(data){
                       			alert('fail');
                 			 }
        			});
        		}else if(exchangeType==3){
        			$('#goodsValue').attr("disabled",false); 
        			$('#batchKey').attr("disabled",true); 
    				$("#batchKey").parents(".content").find(".validate_tips").text("");
    				$("#batchKey").parents(".content").find(".validate_tips").removeClass("valid_fail_text");
        			document.getElementById("batchKey").options.length = 0;
    				$("#batchKey").append("<option value=''>请选择</option>"); 
        		}else{
        			$('#goodsValue').attr("disabled",true); 
        			$('#goodsValue').val("");
        			$("#goodsValue").parents(".content").find(".validate_tips").text("");
    				$("#goodsValue").parents(".content").find(".validate_tips").removeClass("valid_fail_text");
    				$('#batchKey').attr("disabled",true); 
    				$("#batchKey").parents(".content").find(".validate_tips").text("");
    				$("#batchKey").parents(".content").find(".validate_tips").removeClass("valid_fail_text");
        			document.getElementById("batchKey").options.length = 0;
    				$("#batchKey").append("<option value=''>请选择</option>");
        		}
    		}else{
    			$('#exchangeType').val("");
    			$('#goodsValue').attr("disabled",true); 
    			$('#goodsValue').val("");
    			$("#goodsValue").parents(".content").find(".validate_tips").text("");
				$("#goodsValue").parents(".content").find(".validate_tips").removeClass("valid_fail_text");
				$('#batchKey').attr("disabled",true); 
				$("#batchKey").parents(".content").find(".validate_tips").text("");
				$("#batchKey").parents(".content").find(".validate_tips").removeClass("valid_fail_text");
    			document.getElementById("batchKey").options.length = 0;
				$("#batchKey").append("<option value=''>请选择</option>");
    		}
    	});

		
		
		// 电子券批次
		$("#batchKey").change(function(){
			$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").text("");
			$("select[name='batchKey']").parents(".typeInfo").find(".ajax_tips").text("");
    		var batchKey=document.getElementById("batchKey").value;
    		if(batchKey){
    			$.ajax({
    				url:'<%=cpath%>/vpointsGoods/getUseCount.do?batchKey='+batchKey,
    				type: 'POST',
    					beforeSend:appendVjfSessionId,
                    success: function(data){
    						if(data<=0){
    							$("select[name='batchKey']").parents(".batchInfo").find(".ajax_tips").show();
    							$("select[name='batchKey']").parents(".batchInfo").find(".ajax_tips").text("此批次可用券码不足").css("color","#c54242");
    							$("#goodsNum").val(0);
    							$("#showNum").val(0);
    						}else{
    							$("select[name='batchKey']").parents(".batchInfo").find(".ajax_tips").show();
    							$("select[name='batchKey']").parents(".batchInfo").find(".ajax_tips").text("可用券码:"+data+"个").css("color","green");
    							$("#goodsNum").val(data);
    							$("#showNum").val(data);
    						}
    					},
    					error:function(data){
                   			alert('fail');
             			 }
    			});
    		}
    	});
		
		// 商品展示类型态切换
		$("[name='goodShowFlag']").on("change", function(){
			if($(this).val() == '') {
				$("[name='goodShowSequence']").val("").attr("disabled", true);
			} else {
                $("[name='goodShowSequence']").attr("disabled", false);
			}
			if($(this).val()=='5'){
				$("#hotRecommendTitle").removeAttr("hidden")
			}else{
				$("#hotRecommendTitle").attr("hidden","hidden")
			}

		});
		$("[name='isShowSales']").on("change", function(){
			if($(this).val() == '1') {
				$("[name='showSalesType']").attr("disabled", true);
				$("[name='showSalesBase']").attr("disabled",true);
			} else {
				$("[name='showSalesType']").attr("disabled", false);
				$("[name='showSalesBase']").attr("disabled",false);
			}
		});
		$("[name='showSalesType']").on("change", function(){
			if($(this).val() == '0') {
				$("[name='showSalesBase']").attr("disabled",true);
			} else {
				$("[name='showSalesBase']").attr("disabled",false);
			}
		});
 	});
		 
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			if ($("#isShowSales").val() == '0' && $("#showSalesType").val() == ''){
				alert("销量类型 不能为空");
				return false;
			}
            if ($("#showSalesType").val() == '1' && $("#showSalesBase").val() == ''){
                alert("展示销量 不能为空");
                return false;
            }

			var categoryType=$("#categoryType").find("option:selected").val();
			if(!categoryType){
				$.fn.alert("二级分类不能为空");
				return false;
			}
            if(categoryType=='SFJSD'){
				var goodsSpecification=$("#goodsSpecification").find("option:selected").val();
				var expressHotareaKey=$("#expressHotareaKey").find("option:selected").val();
				if(goodsSpecification=='1' || goodsSpecification=='其他'){
					$.fn.alert("极速达类型商品商品规格不能配置【1】【其他】");
					return false;
				}
                if(!expressHotareaKey){
                    $.fn.alert("极速达类型商品热区不能为空");
                    return false;
                }
			}

            var serverName = $(window.top.document).find("#projectServerName").val();
            // 河南特殊处理
            // if (serverName == "henanpz") {
            // 	if ($("[name='youPinFlag']").val() == '0'
            // 			&& Number($("input[name='goodsPayMoney']").val()) > 0) {
            // 		$.fn.alert("优品暂时仅支持纯积分商品");
            // 		return false;
            // 	}
            // }


// 			// 页面校验
// 			var pc=$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").val();
// 			if(pc&&pc.indexOf('个')>-1){
// 				$("select[name='batchKey']").parents(".typeInfo").find(".validate_tips").hidden();
// 			}
			// 页面校验
			var v_flag = true;
			$(".validate_tips:not(:hidden)").each(function(){
				if($(this).text() != ""){
					alert($(this).text());
					v_flag = false;
				}
			});
			if(!v_flag){
				return false;
			}
			var exchangeType=document.getElementById("exchangeType").value;
			var goodsValue=document.getElementById("goodsValue").value;
			if(exchangeType==3){
				if(!goodsValue){
					$("select[name='goodsValue']").parents(".content").find(".validate_tips").show();
					$("select[name='goodsValue']").parents(".content").find(".validate_tips").text("充值金额不能为空").css("color","#c54242");
					return false;
				}
			}
			
			var goodsNum=document.getElementById("goodsNum").value;
    		var batchKey=document.getElementById("batchKey").value;
    		var numReturn=true;
    		if(batchKey){
    			$.ajax({
    				url:'<%=cpath%>/vpointsGoods/getUseCount.do?batchKey='+batchKey,
    				async: false,
    				type: 'POST',
    					beforeSend:appendVjfSessionId,
                    success: function(data){
    						if(parseInt(data)<parseInt(goodsNum)){
    							$("input[name='goodsNum']").parents(".numInfo").find(".validate_tips").show();
    							$("input[name='goodsNum']").parents(".numInfo").find(".validate_tips").text("可用券码不足,最多可用数为"+data).css("color","#c54242");
    							$("input[name='goodsNum']").parents(".numInfo").find(".validate_tips").addClass("valid_fail_text");
    							$("#goodsNum").val(data);
    							$("#showNum").val(data);
    							numReturn=false;
    						}else if(parseInt(data)==0){
    							$("select[name='batchKey']").parents(".batchInfo").find(".ajax_tips").show();
    							$("select[name='batchKey']").parents(".batchInfo").find(".ajax_tips").text("此批次可用券码不足").css("color","#c54242");
    							$("#goodsNum").val(0);
    							$("#showNum").val(0);
    							numReturn=false;
    						}else{
    							$("input[name='goodsNum']").parents(".numInfo").find(".validate_tips").hide();
    						}
    					},
    					error:function(data){
                   			alert('fail');
             			 }
    			});
    		}
    		if(!numReturn){
    			return false;
    		}

            var sHTML = $('.summernote').summernote('code');
            $('#goodsContent').val(sHTML.trim());
            $('#goodsUrl').val(ids.toString());
            $('#goodsBigUrl').val(ids2.toString());
	    	$('#goodsActivityUrl').val(ids3.toString());
        //     $('#goodsUrl').val($("#imgUrl").val());
        //     $('#goodsBigUrl').val($("#bigImgUrl").val());
	    // $('#goodsActivityUrl').val($("#activityImgUrl").val());
            
            if(!sHTML){
            	alert("请输入说明");
            	return false;
            }
            /* if(imgSrc.length==0){
            	alert("请上传图片");
            	return false;
            } */
            $('#exchangeType').attr("disabled",false); 
            
         	// 获取发货地区
			var areaStr = "";
            var treeObj = $.fn.zTree.getZTreeObj("tree");
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes != ''){
				// 省-市-县;省-市;省;省-市-县...
				var lastChar;
				var province;
				var city;
				var idx = 0;
				for ( var i = 0; i <nodes.length; i++){
                    // 省
                    if(nodes[i].level == '0'){
                        province = nodes[i].name;
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-'){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                        }
                        areaStr += province + "-";
                        idx = 0;
                    }
                    
                    // 市
                    if(nodes[i].level == '1'){
                        lastChar = areaStr.substring(areaStr.length - 1);
                        if(lastChar == '-' && idx > 0){
                            areaStr = areaStr.substring(0, areaStr.length - 1) + ";";
                        }
                        if(idx != 0){
                            areaStr += province + "-";
                        }
                        city = nodes[i].name;
                        areaStr += city;
                        areaStr += "-";
                        idx = 1;
                    }
					// 区县
					if(nodes[i].level == '2'){
						if(idx == 2){
							areaStr += province + "-" + city + "-";
						}
						areaStr += nodes[i].name;
						areaStr += ";";
						idx = 2;
					}
				}	
				if(lastChar.length > 0 || areaStr.length > 0){
					lastChar = areaStr.substring(areaStr.length - 1);
					if(lastChar == ';' || lastChar == '-'){
						areaStr = areaStr.substring(0, areaStr.length - 1);
					}	
				}
                console.log(areaStr)
				$("#shipmentsArea").val(areaStr);
			}
			
			// 河南限定发货地区必填
			if("henanpz" == "${projectServerName}" && areaStr == "") {
                alert("请配置发货指定区域");
                return false;
			}
            
			return true;
		}
		
		function initPage() {
			// 底部按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				
				// 返回
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
					
				// 保存生效
				} else {
					var blacklistCogFlag = "0";
					$("input[mark=blacklist]:not([type=radio])").each(function(){
						if($(this).val() != ""){
							blacklistCogFlag = "1";
						}
					});
					$("input[name=blacklistFlag]").val(blacklistCogFlag);
					
					var flag = validForm();
					if(flag) {
						if(btnEvent == "2"){
							if(confirm("确认发布？")){
								return true;
							} else {
								return false;
							}
						} else {
							return true;
						}
					} else {
						return false;
					}
				}
			});
			$("[name='isGift']").on("change", function(){
				if ($(this).val() == '0') {
					$("tr.isGiftAsCoupons_model").show();
					$("tr.skuKeyAry_model").show();
				}else{
					$("tr.isGiftAsCoupons_model").css("display","none");
					$("tr.skuKeyAry_model").css("display","none");
				}
			});
			// 增加SKU
			$("form").on("click", "#addSku", function() {
				if ($(this).is("[disabled='disabled']")) return;
				if ($(this).text() == '新增') {
					var $copySku = $(this).closest("div").clone(true, false);
					$copySku.find("#addSku").text("删除");
					$(this).closest("td").append($copySku);

				} else {
					$(this).closest("div").remove();
				}
			});
			$("[name='exchangeChannel']").on("change", function(){
				$.ajax({
					url : '${basePath}/vpointsGoods/getGoodsAsGift.do?channelType='+$(this).val(),
					type : "POST",
					dataType : "json",
					async : false,
					beforeSend:appendVjfSessionId,
					success:  function(data){
						document.getElementById("promotionSkuKeyAry").options.length = 0;
						$("#promotionSkuKeyAry").append("<option value=''>请选择SKU</option>")
						for (var a = 0; a <data.length; a++) {
							var obj=data[a];
							$("#promotionSkuKeyAry").append("<option value='"+obj.goodsId+"'  data-img='"+obj.goodsUrl+"'>"+obj.goodsName+"</option>");
						}
					}
				});
			});
		}
		function uploadImages(files) {
		    var formData = new FormData();
		    for (f in files) {
		        formData.append("file", files[f]);
		    }
		    // XMLHttpRequest 对象
		    var xhr;
		    if (XMLHttpRequest) {
		    	xhr = new XMLHttpRequest();
	    	} else {
	    	   xhr = new ActiveXObject('Microsoft.XMLHTTP');
	    	}
		    xhr.open("post", basePath+"/skuInfo/imgUploadUrl.do?vjfSessionId=" + $("#vjfSessionId").val(), true);
		    xhr.onreadystatechange = function(){
		        if(xhr.readyState == XMLHttpRequest.DONE && xhr.status == 200){
// 		            console.info("上传完成");
		            var result = jQuery.parseJSON(xhr.responseText);
		            if(result["errMsg"]=="error"){
						alert("上传失败");
						return;
					}
		            var srcList=result["ipUrl"].split(",");
		            for(var i = 0;i<srcList.length;i++){
		            	$('.summernote').summernote('insertImage',srcList[i], srcList[i]);
		            }

		        }else{
// 		        	console.info("上传失败");
		        }
		    };
		    xhr.send(formData);
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
	</style>
  </head>
  
  <body>
  
	
	
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a> 积分商城</a></li>
        	<li class="current"><a title="">商品管理</a></li>
        	<li class="current"><a title="">添加商品</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath %>/vpointsGoods/addGoods.do">
            	<input type="hidden" name="goodsPay" id="goodsPay" value="0">
            	<input type="hidden" name="goodsContent" id="goodsContent" value="">
                <input type="hidden" name="goodsUrl" id="goodsUrl" value="">
                <input type="hidden" name="goodsBigUrl" id="goodsBigUrl" value="">
		<input type="hidden" name="goodsActivityUrl" id="goodsActivityUrl" value="">
                <input type="hidden" name="secKill" id="secKill" value="1">
            	<div class="widget box">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>商品信息</h4>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">商品名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content goodsName">
	                       				<input name="goodsName" tag="validate" 
	                       					class="form-control required" autocomplete="off" maxlength="60" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr style="height: 163px;">
                                <td class="ab_left" style="vertical-align:middle;"><label class="title">商品图片：<span class="white">*</span>
									<c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="blocker en-larger" style="color: #b81900">尺寸建议340*340px</span></c:if>
								</label></td>
                                <td class="ab_main" colspan="3">
									<div style="height: 163px;width: 800px;" >
										<form enctype="multipart/form-data">
											<input type="hidden" id="imagepath" name="">  <!-- 保存的图片id 用于表单提交 -->
											<input id="imgUrl" name="prizeImgAry" type="hidden" class='filevalue' />
										<!-- 上传按钮 -->
											<div id="upload_duixiang" class="scroll"><img src="<%=cpath %>/inc/vpoints/img/a11.png" style="width: 130px;height:120px"></div>
											<div class="show scroll"  >
											</div> <!-- 输出图片 -->
										</form>
									</div>

<%--                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">--%>
<%--                                        <section class="img-section" data-imgnum="4">--%>
<%--                                        	<input id="imgUrl" name="prizeImgAry" type="hidden" class='filevalue' />--%>
<%--                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">--%>
<%--                                                     <section class="z_file fl">--%>
<%--                                                        <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">--%>
<%--                                                        <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>--%>
<%--                                                     </section>--%>
<%--                                             </div>--%>
<%--                                         </section>--%>
<%--                                    </div>--%>
<%--                                    <aside style="display: none;" class="mask works-mask">--%>
<%--                                        <div class="mask-content">--%>
<%--                                            <p class="del-p ">您确定要删除图片吗？</p>--%>
<%--                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>--%>
<%--                                        </div>--%>
<%--                                    </aside>--%>
                                </td>
                            </tr>
                            <tr style="height: 163px;">
                                <td class="ab_left" style="vertical-align:middle;"><label class="title">首页商品大图：<span class="white">*</span>
									<c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="blocker en-larger" style="color: #b81900">尺寸建议340*340px</span></c:if>
								</label></td>
                                <td class="ab_main" colspan="3">
									<div style="height: 163px;width: 800px;" >
										<form enctype="multipart/form-data">
											<input  type="hidden" id="imagepath2" name="">  <!-- 保存的图片id 用于表单提交 -->
											<input id="bigImgUrl" name="prizeImgAry" type="hidden" class='filevalue' />
											<!-- 上传按钮 -->
											<div id="upload_duixiang2" class="scroll">
												<img src="<%=cpath %>/inc/vpoints/img/a11.png" style="width: 130px;height:120px">
											</div>
											<div class="show2 scroll"  >
											</div> <!-- 输出图片 -->
										</form>
									</div>

								<%--                                    <div style="height: 163px;width: 800px; float: left;" class="img-box full">--%>
<%--                                        <section class="img-section" data-imgnum="4">--%>
<%--                                        	<input id="bigImgUrl" name="prizeImgAry" type="hidden" class='filevalue' />--%>
<%--                                            <div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">--%>
<%--                                                     <section class="z_file fl">--%>
<%--                                                        <img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">--%>
<%--                                                        <input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>--%>
<%--                                                     </section>--%>
<%--                                             </div>--%>
<%--                                         </section>--%>
<%--                                    </div>--%>
<%--                                    <aside style="display: none;" class="mask works-mask">--%>
<%--                                        <div class="mask-content">--%>
<%--                                            <p class="del-p ">您确定要删除图片吗？</p>--%>
<%--                                            <p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>--%>
<%--                                        </div>--%>
<%--                                    </aside>--%>
                                </td>
                            </tr>
				<tr style="height: 163px;">
					<td class="ab_left" style="vertical-align:middle;"><label class="title">商品活动图片：<span class="white">*</span>
						<c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><br/><span class="blocker en-larger" style="color: #b81900">尺寸建议340*340px</span></c:if>
					</label></td>
					<td class="ab_main" colspan="3">
						<div style="height: 163px;width: 800px;" >
							<form enctype="multipart/form-data">
								<input type="hidden" id="imagepath3" name="">  <!-- 保存的图片id 用于表单提交 -->
								<input id="activityImgUrl" name="prizeImgAry" type="hidden" class='filevalue' />
								<!-- 上传按钮 -->
								<div id="upload_duixiang3" class="scroll">
									<img src="<%=cpath %>/inc/vpoints/img/a11.png" style="width: 130px;height:120px">
								</div>
								<div class="show3 scroll"  >
								</div> <!-- 输出图片 -->
							</form>
						</div>
<%--						<div style="height: 163px;width: 800px; float: left;" class="img-box full">--%>
<%--							<section class="img-section" data-imgnum="4">--%>
<%--								<input id="activityImgUrl" name="prizeImgAry" type="hidden" class='filevalue' />--%>
<%--								<div class="z_photo upimg-div clear" style="overflow: hidden;height: auto;" id="photoId">--%>
<%--									<section class="z_file fl">--%>
<%--										<img src="<%=cpath %>/inc/vpoints/img/a11.png" class="add-img">--%>
<%--										<input type="file" name="file" id="file" class="file" value="" accept="image/jpg,image/jpeg,image/png,image/bmp" multiple/>--%>
<%--									</section>--%>
<%--								</div>--%>
<%--							</section>--%>
<%--						</div>--%>
<%--						<aside style="display: none;" class="mask works-mask">--%>
<%--							<div class="mask-content">--%>
<%--								<p class="del-p ">您确定要删除图片吗？</p>--%>
<%--								<p class="check-p"><span class="del-com wsdel-ok">确定</span><span class="wsdel-no">取消</span></p>--%>
<%--							</div>--%>
<%--						</aside>--%>
					</td>
				</tr>
				<tr>
					<td class="ab_left" style="vertical-align:middle;"></td>
					<td class="ab_main" colspan="3">
						<div class="content">
						 <span class="red">首页爆款商品大图模式选择商品活动图片的第二张图片展示</span>
						</div>
					</td>
				</tr>
				<tr>
					<td class="ab_left"><label class="title">兑换渠道：<span class="required">*</span></label></td>
					<td class="ab_main" colspan="3">
						<div class="content channelInfo">
							<select name="exchangeChannel" id="exchangeChannel" tag="validate" class="form-control required input-width-large search" >
								<option value="">请选择</option>
								<c:if test="${projectServerName eq 'hebei'}">
									<option value="1">天津商城</option>
								</c:if>
								<c:if test="${projectServerName ne 'hebei' && !fn:startsWith(projectServerName, 'mengniu')}">
									<option value="1">畅享汇商城</option>
								</c:if>
								<c:if test="${fn:startsWith(projectServerName, 'mengniu')}">
									<option value="1">积分商城</option>
								</c:if>
								<option value="2">抽奖</option>
								<c:if test="${projectServerName eq 'hebei'}">
									<option value="6">畅享汇商城</option>
								</c:if>
								<c:if test="${projectServerName eq 'henanpz'}"><option value="9">竞价活动</option></c:if>
							</select>
							<label class="validate_tips"></label>
						</div>
					</td>
				</tr>
                		<tr>
	                       		<td class="ab_left"><label class="title">兑换时间：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input id="goodsStartTime" name="goodsStartTime" class="form-control input-width-medium required Wdate preTime" style="width: 160px !important;"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" />
                                       	<span class="blocker en-larger">至</span>
                                       	<input id="goodsEndTime" name="goodsEndTime" class="form-control input-width-medium required Wdate sufTime" style="width: 160px !important;"
                                       		tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss', minDate:'%y-%M-%d'})" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
		                       			<select name="brandId" id="brandId" tag="validate" class="form-control required input-width-large search" >
									    	<option value="">请选择</option>
	                                        <c:forEach items="${brandLst}" var="item">
	                                            <option value="${item.brandId}">${item.brandName}</option>
	                                        </c:forEach>
									    </select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品编号：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="goodsClientNo" tag="validate"  
                                            class="form-control required input-width-large rule" autocomplete="off" maxlength="36" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品简称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="goodsShortName" tag="validate" 
                                            class="form-control required input-width-large rule" autocomplete="off" maxlength="15" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品规格：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <select name="goodsSpecification" id="goodsSpecification" tag="validate" class="form-control required input-width-large search" >
									    	<option value="">请选择</option>
									    	<option value="218ml*24">218ml*24</option>
									    	<option value="258ml*24">258ml*24</option>
									    	<option value="296ml*6">296ml*6</option>
									    	<option value="296ml*24">296ml*24</option>
									    	<option value="316ml*24">316ml*24</option>
									    	<option value="330ml*6">330ml*6</option>
									    	<option value="330ml*6*4">330ml*6*4</option>
									    	<option value="330ml*12">330ml*12</option>
									    	<option value="355ml×6">355ml×6</option>
									    	<option value="355ml*12">355ml*12</option>
									    	<option value="500ml*6">500ml*6</option>
									    	<option value="500ml*12">500ml*12</option>
									    	<option value="500ml*18">500ml*18</option>
									    	<option value="815ml*1">815ml*1</option>
									    	<option value="815ml*6">815ml*6</option>
									    	<option value="1L*1">1L*1</option>
                                            <option value="1L*2">1L*2</option>
                                            <option value="1L*3">1L*3</option>
                                            <option value="1L*4">1L*4</option>
									    	<option value="1L*6">1L*6</option>
									    	<option value="1.5L*1">1.5L*1</option>
									    	<option value="5L*2">5L*2</option>
									    	<option value="1">1</option>
									    	<option value="其他">其他</option>
									    </select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品单位名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <input name="goodsUnitName" tag="validate" 
                                            class="form-control required input-width-large rule" autocomplete="off" maxlength="10" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">是否推荐：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
	                       			<select name="isCommend" id="isCommend" tag="validate" class="form-control required input-width-large search" >
								    	<option value="1">否</option>
								    	<option value="0">是</option>
								    </select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
				<tr class="ifGift_model serverhid">
					<td class="ab_left"><label class="title">是否赠品：<span class="required">*</span></label></td>
					<td class="ab_main" colspan="3">
						<div class="content commendInfo">
							<select name="isGift" id="isGift" tag="validate" class="form-control required input-width-large search" >
								<option  value="1">否</option>
								<option value="0">是</option>
							</select>
							<label class="validate_tips"></label>
						</div>
					</td>
				</tr>
<%--				<tr class="if_gift_card">--%>
<%--					<td class="ab_left"><label class="title">是否礼品卡：<span class="required">*</span></label></td>--%>
<%--					<td class="ab_main" colspan="3">--%>
<%--						<div class="content commendInfo">--%>
<%--							<select name="isGiftCard" id="isGiftCard" tag="validate" class="form-control required input-width-large search" >--%>
<%--								<option  value="1">否</option>--%>
<%--								<option value="0">是</option>--%>
<%--							</select>--%>
<%--							<label class="validate_tips"></label>--%>
<%--						</div>--%>
<%--					</td>--%>
<%--				</tr>--%>
				<tr class="isGiftAsCoupons_model" style="display: none">
					<td class="ab_left"><label class="title">是否与优惠券同享：<span class="required">*</span></label></td>
					<td class="ab_main" colspan="3">
						<div class="content commendInfo">
							<select name="isGiftAsCoupons" id="isGiftAsCoupons" tag="validate" class="form-control required input-width-large search" >
								<option  value="1">否</option>
								<option value="0">是</option>
							</select>
							<label class="validate_tips"></label>
						</div>
					</td>
				</tr>
				<tr class="skuKeyAry_model" style="display: none">
					<td class="ab_left"><label class="title">关联商品：<span class="required">*</span></label></td>
					<td class="ab_main sku" colspan="3">
						<div class="content sku promotionSku" style="display: flex; margin: 5px 0px;">
							<select class="form-control input-width-larger required" id="promotionSkuKeyAry" name="promotionSkuKeyAry" tag="validate">
								<option value="">请选择SKU</option>
							</select>
							<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addSku">新增</label>
							<label class="validate_tips"></label>
						</div>
					</td>
				</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品展示类型：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
	                       			<select name="goodShowFlag" class="form-control required input-width-large search" >
	                       				<option value="">请选择</option>
								    	<option value="1">首页商品大图</option>
								    	<option value="2">首页商品小图</option>
								    	<option value="3">积分好礼</option>
								    	<option value="4">活动专区</option>
								    	<option value="5">爆款推荐</option>
								    </select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr id="hotRecommendTitle" hidden>
								<td class="ab_left"><label class="title">爆款推荐标题：</label></td>
								<td class="ab_main" colspan="3">
									<div class="content commendInfo">
										<input name="hotRecommendTitle" tag="validate"
											   class="form-control required input-width-large" autocomplete="off" maxlength="30" />
									</div>
								</td>
							</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品展示排序：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
                                        <input name="goodShowSequence" tag="validate" disabled="disabled"
                                            class="form-control required number integer input-width-small rule" autocomplete="off" maxlength="5" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
								<td class="ab_left"><label class="title">自定义排序：<span class="white">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content commendInfo">
										<input name="goodsCustomSequence" tag="validate"
											   class="form-control number integer input-width-small rule" autocomplete="off" maxlength="5" />
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
	                       	<tr style="display: none">
	                       		<td class="ab_left"><label class="title">自定义排序：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
                                        <input name="saleNumSequence" tag="validate"
                                            class="form-control number integer input-width-small rule" autocomplete="off" maxlength="5" />
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	
	                       	<tr>
	                       		<td class="ab_left"><label class="title">一级品类：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content parentInfo">
										<select name="categoryParent" id="categoryParent" tag="validate" class="form-control required input-width-large search" >
											<option value="">请选择</option>
											<c:forEach items="${firstList}" var="item">
												<option value="${item.categoryType}">${item.categoryName}</option>
											</c:forEach>
										</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">二级品类：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content secondInfo">
	                       				<select name="categoryType" id="categoryType" tag="validate" class="form-control required input-width-large search">
                                        <option value="">请选择</option>
                                        <c:forEach items="${secondList}" var="item">
								    		<option value="${item.categoryType}">${item.categoryName}</option>
								    	</c:forEach>
								    	</select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品类型：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content typeInfo">
	                       			<select name="exchangeType" disabled id="exchangeType" tag="validate" class="form-control required input-width-large search" >
								    	<option value="">请选择</option>
								    	<option value="1">实物</option>
								    	<option value="2">电子券</option>
								    	<option value="3">话费</option>
								    </select>
                                       	<label class="validate_tips"></label>
                                       	<label class="ajax_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">电子券批次：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content batchInfo">
	                       			<select name="batchKey" id="batchKey" tag="validate" class="form-control required input-width-large search" disabled>
								    	<option value="">请选择</option>
								    </select>
                                       	<label class="validate_tips"></label>
                                       	<label class="ajax_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">充值金额：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content valueInfo">
	                       			<select name="goodsValue" id="goodsValue" tag="validate" class="form-control required input-width-large search" disabled >
	                       			<option value="">请选择</option>
								    	<option value="10" >10元</option>
								    	<option value="30" >30元</option>
								    	<option value="50" >50元</option>
	                       			</select>
                                    <label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">代采价格：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="daicaiMoney" tag="validate" 
	                       					class="form-control required money input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">元</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">兑换积分：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="goodsVpoints" tag="validate"  value="0"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">积分</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品参考价：</label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="goodsMoney"  value="0.00"
	                       					class="form-control required money input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">元<c:if test="${projectServerName eq 'hebei'}">，普通商品时划线价</c:if></span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品支付价格：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="goodsPayMoney" tag="validate"  value="0.00"
	                       					class="form-control required money input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">元<c:if test="${projectServerName eq 'hebei'}">，拼团、秒杀、折扣时划线价</c:if></span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品折扣：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="goodsDiscount" tag="validate"  value="100"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="3" />
	                       				<span class="blocker en-larger">%</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">是否优品/尊品：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <select name="youPinFlag" class="form-control required input-width-large search">
	                                        <option value="1">否</option>
	                                        <option value="0">是</option>
	                                    </select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品使用权益：<span class="white">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
                                        <select name="exchangeCardType" class="form-control required input-width-large search">
	                                       <option value="">请选择</option>
	                                       <c:forEach items="${exchangeCardTypeMap}" var="item">
	                                           <option value="${item.key}">${item.value}</option>
	                                       </c:forEach>
	                                    </select>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">商品个数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content numInfo">
	                       				<input name="goodsNum" id="goodsNum" tag="validate"  value="0"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">个</span>
	                       				<label class="validate_tips"></label>
	                       				<label class="ajax_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户兑换限制：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content numInfo">
	                       				<input name="goodsLimit" id="goodsLimit" tag="validate"  value="0"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">个(默认0时不限制)</span>
	                       				<label class="validate_tips"></label>
	                       				<label class="ajax_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">用户每天兑换限制：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content numInfo">
	                       				<input name="goodsUserDayLimit" id="goodsUserDayLimit" tag="validate"  value="0"
	                       					class="form-control required integer input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">个(默认0时不限制)</span>
	                       				<label class="validate_tips"></label>
	                       				<label class="ajax_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
							<tr>
								<td class="ab_left"><label class="title">是否展示销量：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<select name="isShowSales" id="isShowSales" tag="validate" class="form-control required input-width-large search" >
											<option value="">请选择</option>
											<option value="1">否</option>
											<option value="0">是</option>
										</select>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">展示销量类型：<span class="required">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<select name="showSalesType" id="showSalesType" tag="validate" class="form-control required input-width-large search" >
											<option value="">请选择</option>
											<option value="1">展示销量</option>
											<option value="0">真实销量</option>
										</select>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">销量：<span class="required">*近30天</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="showSalesBase" id="showSalesBase" tag="validate"  value="0"
											   class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" oninput='this.value=this.value.replace(/^[0]+[0-9]*$/gi,"")'/>
										<span class="blocker en-larger">件</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
							<tr>
								<td class="ab_left"><label class="title">显示个数：<span class="white">*</span></label></td>
								<td class="ab_main" colspan="3">
									<div class="content">
										<input name="showNum" id="showNum" tag="validate"  value="0"
											   class="form-control number positive input-width-small rule" autocomplete="off" maxlength="8" />
										<span class="blocker en-larger">个</span>
										<label class="validate_tips"></label>
									</div>
								</td>
							</tr>
                            <tr>
                                <td class="ab_left"><label class="title">商品状态：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content commendInfo">
                                    <select name="goodsStatus" id="goodsStatus" tag="validate" class="form-control required input-width-large search" >
                                        <option value="0">正常</option>
                                        <option value="1">暂停</option>
                                        <option value="2">下架</option>
                                    </select>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">暂停兑换提示语：<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content commendInfo">
                                        <input name="pauseExchangeTips" tag="validate" 
                                            class="form-control required input-width-larger rule" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title">发货时间提示语：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content commendInfo">
                                        <input name="expressTips"
                                            class="form-control required input-width-larger rule" autocomplete="off" maxlength="30" />
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left"><label class="title mart5">限定热区：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main">
                                    <div class="content hot">
                                        <select id="expressHotareaKey" name="expressHotareaKey" class="expressHotareaKey form-control" style="width: 352px;">
                                            <option value="">请选择</option>
                                            <c:if test="${not empty(hotAreaList) }">
                                                <c:forEach items="${hotAreaList}" var="hotArea">
                                                    <option value="${hotArea.hotAreaKey }" >${hotArea.hotAreaName}</option>
                                                </c:forEach>
                                            </c:if>
                                        </select>
										<span style="color: green; margin-left: 5px;">注意：极速达分类的商品必选热区</span>
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="display:table-cell; vertical-align:middle;"><label class="title">发货指定区域：<span class="required">${projectServerName eq 'henanpz' ? '*' : '' }</span></label></td>
                                <td class="ab_main" colspan="3">
                                	<input id="shipmentsArea" name="shipmentsArea" type="hidden">
                                    <div class="content_wrap">
										<div class="zTreeDemoBackground left" >
											<ul id="tree" class="ztree" style="background-color: white;"></ul>
										</div>
									</div>
                                </td>
                            </tr>
                            <tr>
                                <td class="ab_left" style="display:table-cell; vertical-align:middle;"><label class="title">运营者备注：<span class="required">&nbsp;</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
	                       				<textarea name="remarkInfo" rows="5"
	                       					class="form-control required" autocomplete="off" maxlength="500" ></textarea>
	                       				<label class="validate_tips"></label>
	                       			</div>
                                </td>
                            </tr>
                		</table>
                	</div>
                	<!-- <div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>商品拼团设置</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
	                       	<tr>
	                       		<td class="ab_left"><label class="title">是否拼团商品：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content commendInfo">
	                       			<select name="isGroupBuying" id="isGroupBuying" tag="validate" class="form-control required input-width-large search" >
								    	<option value="0">否</option>
								    	<option value="1">是</option>
								    </select>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">拼团商品积分：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="groupBuyingVpoints" tag="validate"  value="0"
	                       					class="form-control number intger input-width-small rule" autocomplete="off" maxlength="9" />
	                       				<span class="blocker en-larger">积分</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">限制开团数量：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="limitGroupNum" id="limitGroupNum" tag="validate"  value="0" minVal="2"
	                       					class="form-control number intger minValue input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">个</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
	                       		<td class="ab_left"><label class="title">成团人数：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="reachNum" id="reachNum" tag="validate"  value="0" minVal="2"
	                       					class="form-control number intger minValue input-width-small rule" autocomplete="off" maxlength="8" />
	                       				<span class="blocker en-larger">人</span>
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                		</table>
                	</div> -->
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>商品兑换说明</h4></div>
                		<div class="summernote">
                		<h2><b>商品简介</b></h2><p><b><br></b></p><p>&nbsp;&nbsp;&nbsp;&nbsp;在此输入商品简介……</p><p><b><br></b></p><p><b><br></b></p>
                		<h2><b>兑换流程</b></h2><p><b><br></b></p><p>&nbsp;&nbsp;&nbsp;&nbsp;在此输入兑换流程……</p><p><b><br></b></p><p>&nbsp;&nbsp;&nbsp;&nbsp;<b><br></b></p>
                		</div>
					<c:if test="${fn:startsWith(currentUser.projectServerName, 'mengniu')}"><div><span style="color: #b81900">尺寸建议图片宽750px</span></div></c:if>
					<div class="active_table_submit mart20">
			            <div class="button_place">
					    	<button class="btn btn-blue btnSave" data-event="1">保存生效</button>&nbsp;&nbsp;&nbsp;&nbsp;
					    	<button class="btn btnReturn btn-radius3" data-event="0" data-url="<%=cpath%>/vpointsGoods/getGoodsList.do">返 回</button>
			            </div>
	            	</div>
            	</div>
            </form>
        </div>
    </div>
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	  	<div class="modal-dialog">
		    <div class="modal-content" style="top:30%;">
	        	<div class="modal-header">
			        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			        <h4 class="modal-title" id="myModalLabel">提示消息</h4>
	      		</div>
      			<div class="modal-body">
      				<h6></h6>
      			</div>
	      		<div class="modal-footer">
			        <button type="button" class="btn btn-default" data-dismiss="modal">关 闭</button>
	      		</div>
		    </div>
	  	</div>
	</div>
    </div>
  </body>
  <style type="text/css">

	  body {
		  padding: 1rem;
	  }

	  pre {
		  background-color: #f7f7f9;
		  border: 1px solid #e1e1e8;
		  padding: 5px 10px;
		  margin: 0;
		  border-radius: 8px;
		  min-height: 20px;
	  }

	  xmp {
		  margin: 0;
		  padding: 0;
	  }

  </style>
  <link rel="stylesheet" type="text/css" href="<%=cpath%>/inc/goods/upload/FraUpload.css">
  <link rel="stylesheet" type="text/css" href="<%=cpath%>/inc/goods/upload/FraUpload2.css">
  <link rel="stylesheet" type="text/css" href="<%=cpath%>/inc/goods/upload/FraUpload3.css">
<%--  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/jquery-3.5.1.min.js"></script>--%>
  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/Sortable.js"></script>
  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/MD5.js"></script>
<%--  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/jquery-3.5.1.min.js"></script>--%>
  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/FraUpload.js"></script>
  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/FraUpload2.js"></script>
  <script type="text/javascript" src="<%=cpath%>/inc/goods/upload/FraUpload3.js"></script>
  <script onload="this" >
	  var ids = [];
	  // 商品图片上传
	  var a = $("#upload_duixiang").FraUpload({
		  view: ".show",      // 视图输出位置
		  url:  basePath+"/skuInfo/imgUploadUrl.do", // 上传接口
		  fetch: "img",   // 视图现在只支持img
		  debug: false,    // 是否开启调试模式
		  beforeSend:appendVjfSessionId,
		  /* 外部获得的回调接口 */
		  onLoad: function (e,viewId) { // 选择文件的回调方法
			  console.log("外部: 初始化完成...",e);
			  console.log("外部: 初始化完成...",viewId);
			  console.log(viewId[0].id);
		  },
		  breforePort: function (e) {         // 发送前触发
			  console.log("文件发送之前触发");
		  },
		  successPort: function (e) {         // 发送成功触发
			  console.log("文件发送成功");
			  onload_image()
		  },
		  errorPort: function (e) {       // 发送失败触发
			  console.log("文件发送失败");
			  onload_image()
		  },
		  deletePost: function (e) {    // 删除文件触发
			  console.log("删除文件");
			  alert('删除了' + e.filename)
			  onload_image()
		  },
		  sort: function (e) {      // 排序触发
			  console.log("排序");
			  onload_image()
			  console.log("ids", ids)
		  },
	  });
	  // 获取图片上传信息
	  function onload_image() {
		  var res = a.FraUpload.show()
		  ids = [];
		  for (let k in res) {
			  let this_val = res[k]
			  if (!empty(this_val['is_upload']) && !empty(this_val['ajax'])) {
				  let ajax_value = this_val['ajax'];
				  console.log("ajax_value", ajax_value);
				  ajax_value = eval("(" + ajax_value + ")");
				  console.log("ajax_value.path", ajax_value.path);
				  ids.push(ajax_value.path)
			  }
		  }
		  $("#imagepath").val(ids);
	  }
	  /**
	   * 判断变量是否为空
	   */
	  function empty(value) {
		  if (value == "" || value == undefined || value == null || value == false || value == [] || value == {}) {
			  return true;
		  } else {
			  return false;
		  }
	  }
	  var ids2 = [];
	  // 商品图片上传
	  var a2 = $("#upload_duixiang2").FraUpload2({
		  view: ".show2",      // 视图输出位置
		  url:  basePath+"/skuInfo/imgUploadUrl.do", // 上传接口
		  fetch: "img",   // 视图现在只支持img
		  debug: false,    // 是否开启调试模式
		  beforeSend:appendVjfSessionId,
		  /* 外部获得的回调接口 */
		  onLoad: function (e) {// 选择文件的回调方法
			  console.log("外部: 初始化完成...",e);

		  },
		  breforePort: function (e) {         // 发送前触发
			  console.log("文件发送之前触发");
		  },
		  successPort: function (e) {         // 发送成功触发
			  console.log("文件发送成功");
			  onload_image2()
		  },
		  errorPort: function (e) {       // 发送失败触发
			  console.log("文件发送失败");
			  onload_image2()
		  },
		  deletePost: function (e) {    // 删除文件触发
			  console.log("删除文件");
			  alert('删除了' + e.filename)
			  onload_image2()
		  },
		  sort: function (e) {      // 排序触发
			  console.log("排序");
			  onload_image2()
			  console.log("ids2", ids2)
		  },
	  });
	  // 获取图片上传信息
	  function onload_image2() {
		  var res = a2.FraUpload2.show()
		  ids2 = [];
		  for (let k in res) {
			  let this_val = res[k]
			  if (!empty(this_val['is_upload']) && !empty(this_val['ajax'])) {
				  let ajax_value = this_val['ajax'];
				  console.log("ajax_value2", ajax_value);
				  ajax_value = eval("(" + ajax_value + ")");
				  console.log("ajax_value.path2", ajax_value.path);
				  ids2.push(ajax_value.path)
			  }
		  }
		  $("#imagepath2").val(ids2);
	  }
	  var ids3 = [];
	  // 商品图片上传
	  var a3 = $("#upload_duixiang3").FraUpload3({
		  view: ".show3",      // 视图输出位置
		  url:  basePath+"/skuInfo/imgUploadUrl.do", // 上传接口
		  fetch: "img",   // 视图现在只支持img
		  debug: false,    // 是否开启调试模式
		  beforeSend:appendVjfSessionId,
		  /* 外部获得的回调接口 */
		  onLoad: function (e) {                    // 选择文件的回调方法
			  console.log("外部: 初始化完成...");
		  },
		  breforePort: function (e) {         // 发送前触发
			  console.log("文件发送之前触发");
		  },
		  successPort: function (e) {         // 发送成功触发
			  console.log("文件发送成功");
			  onload_image3()
		  },
		  errorPort: function (e) {       // 发送失败触发
			  console.log("文件发送失败");
			  onload_image3()
		  },
		  deletePost: function (e) {    // 删除文件触发
			  console.log("删除文件");
			  alert('删除了' + e.filename)
			  onload_image3()
		  },
		  sort: function (e) {      // 排序触发
			  console.log("排序");
			  onload_image3()
			  console.log("ids3", ids3)
		  },
	  });
	  // 获取图片上传信息
	  function onload_image3() {
		  var res = a3.FraUpload3.show()
		  ids3 = [];
		  for (let k in res) {
			  let this_val = res[k]
			  if (!empty(this_val['is_upload']) && !empty(this_val['ajax'])) {
				  let ajax_value = this_val['ajax'];
				  console.log("ajax_value3", ajax_value);
				  ajax_value = eval("(" + ajax_value + ")");
				  console.log("ajax_value.path3", ajax_value.path);
				  ids3.push(ajax_value.path)
			  }
		  }
		  $("#imagepath3").val(ids3);
	  }

  </script>
</html>
