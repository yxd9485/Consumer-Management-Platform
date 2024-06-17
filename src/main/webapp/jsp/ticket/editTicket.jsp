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
	String imageServerUrl = PropertiesUtil.getPropertyValue("image_server_url");
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title></title>
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<link href="<%=cpath%>/plugins/bootstrap-select-1.13.14/dist/css/bootstrap-select.min.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-select-1.13.14/dist/js/bootstrap-select.min.js"></script>
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-select-1.13.14/dist/js/i18n/defaults-zh_CN.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	<script>
	var basePath='<%=cpath%>';
	var allPath='<%=allPath%>';
	var imgSrc=[];
		$(function(){
			
			
			// 初始化校验控件
			$.runtimeValidate($("form"));
		
			// 初始化功能
			initPage();
			
		});
		
		function initPage() {
			if(${isInputFlag} == false){		
			 	$.fn.alert('${errMsg}', function(){
                 	 $("form").attr("action", '<%=cpath%>/smallticketAction/showTicketList.do');
                     $("form").attr("onsubmit", "return true;");
                     $("form").submit();
             	});
			}
			
			// 店内体系码
			$("#insideCodeType").selectpicker();
			
			// SPU商品下转入
			$(":input[name='skuKey']").selectpicker();
			
			 $("input[name='clearFlag']").val($('#insideCodeType').find("option:selected").text());
			
			// 初始商品列表显示
			$("#firstScanPrize #addPrizeItem").not(":first").text("删除");
			calculateTotalMoney();
			
			// 促销员门店双击代入
			$("#promotionTerminalName").on("dblclick", function(){
				var terminalName = $(this).data("terminalname");
				var terminalSystem = $("#promotionTerminalSystem").data("terminalsystem");
				if (terminalName != '') {
					$("#ticketTerminalName").val(terminalName).triggerHandler("blur");
					var insideCodeType = $("#insideCodeType option[txt='"+terminalSystem+"']").attr("value");
					$("#insideCodeType").selectpicker('val',insideCodeType);
                    $(":input[name='promotionEarnFlag'][value='1']").attr("checked", true).trigger("click");
				}
			});
         	
         	// 审核状态切换
			$("[name='ticketStatus']").on("change", function () {
				$(":input[name='promotionEarnFlag']").removeAttr("disabled");
				
				//不通过
                if ($(this).val() == '1') {   
                	$("#showdismissReason").show();
                	$("#showRemark").hide();
                	$("#showsubmintCheckReason").hide();
                	$("input[name='submintCheckReason']").val('');
                	$(":input[name='promotionEarnFlag'][value='0']").attr("checked", true).trigger("click");
                	$(":input[name='promotionEarnFlag']").attr("disabled", "disabled");
                	
                //通过	
                }else if($(this).val() == '2') {
                	$("#showRemark").show();
                	$("#showdismissReason").hide();
                	$("#showsubmintCheckReason").hide();
                	$("input[name='dismissReason']").val('');
                	$("input[name='submintCheckReason']").val('');
                 //送审		
                }else if($(this).val() == '3') {
                	$("#showsubmintCheckReason").show();
                	$("#showRemark").hide();
                	$("#showdismissReason").hide();
                	$("input[name='dismissReason']").val('');
                }
            });
		
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
                	
                	var tips = "确认操作吗？";
                	$("input[name$='ticketSkuUnitMoney']").each(function(i,obj){
                		var skuName = "";
                		if (Number($(this).val()) > 1000) {
                			if (skuName == "") {
                                skuName = $(this).closest("tr").find("input[name='ticketSkuName']").val();
                			} else {
                                skuName = skuName + "、" + $(this).closest("tr").find("input[name='ticketSkuName']").val();
                			}
                		}
                		if (skuName != "") {
                			tips = "以下商品单价大于1000元：" + skuName + "，" + tips;
                		}
                	});
                	
                    var url = $(this).attr("url");
                    $.fn.confirm(tips, function() {
                    	$(".btnSave").attr("disabled", "disabled");
                    	$("form").attr("action", url);
                   	 	$("form").attr("onsubmit", "return true;");
                   		 $("form").submit();
                    });
                }	
                return false;
            });
			
			// 动态增加奖项配置项
            $("form").on("click", "#addPrizeItem", function(){            		
            	if ($(this).closest("tr").find("#NO").text() == "1") {
            		var $cloneItem = $(this).closest("tr").clone(true, true);
            		$cloneItem.find("input[name$='skuNum']").data("oldval", "0").val("1");
            		$cloneItem.find("input[name$='ticketSkuUnitMoney']").data("oldval", "0.00").val("0.00");
            		$cloneItem.find("input[name$='ticketSkuMoney']").data("oldval", "0.00").val("0.00");
            		$cloneItem.find("input[name='terminalInsideCode'], input[name='ticketSkuName'], select[name='skuKey']").val("");
            		$cloneItem.find("#addPrizeItem").text("删除");
            		$skuKeyItem = $cloneItem.find(":input[name='skuKey']");
            		$cloneItem.find(".bootstrap-select").remove();
            		$cloneItem.find("td.skukey").append($skuKeyItem);
	                $(this).closest("tbody").find("tr:last-child").before($cloneItem);                    
            	} else {	   
                    $(this).closest("tr").remove();
                    calculateTotalMoney();
            	}	                
                $(this).closest("tbody").find("tr").each(function(i, obj){
                	$(this).find("#NO").text(i+1);
                	$cloneItem.find(":input[name='skuKey']").attr("id", "skuKey"+(i+1));
                });

                $(":input[name='skuKey']").selectpicker();
            });
			
         	// 输入数量、单价、合计
            $("input[name='skuNum'],input[name='ticketSkuUnitMoney'],input[name='ticketSkuMoney']").on("input", function(){
            	$tr = $(this).closest("tr");
            	var skuNum = $tr.find("input[name='skuNum']").val();
            	var skuUnitMoney = $tr.find("input[name='ticketSkuUnitMoney']").val();
                var SkuMoney = $tr.find("input[name='ticketSkuMoney']").val();
                
                if (skuNum == "") $tr.find("input[name='skuNum']").val("1");
                if (skuUnitMoney == "") $tr.find("input[name='ticketSkuUnitMoney']").val("0.00");
                if (SkuMoney == "") $tr.find("input[name='ticketSkuMoney']").val("0.00");
                
                if ($(this).attr("name") == "ticketSkuMoney") {
                	$tr.find("input[name='ticketSkuUnitMoney']").val((SkuMoney / skuNum).toFixed(2));
                } else {
                	$tr.find("input[name='ticketSkuMoney']").val((skuNum * skuUnitMoney).toFixed(2));
                }
            });
         	
         	// 变更数量、单价、合计
            $("form").on("change", "input[name='skuNum'],input[name='ticketSkuUnitMoney'],input[name='ticketSkuMoney']", function(){
            	calculateTotalMoney();
            });
         	
         	// 计算商品总个数、商品总价
         	function calculateTotalMoney() {
                var num = 0;
                var price = 0; 
         		$("#firstScanPrize tr").each(function(i,obj){
                    if($(this).find("[name='skuNum']").val()) {
                        num += Number($(this).find("[name='skuNum']").val());
                    } 
                    if($(this).find("[name='ticketSkuMoney']").val()) {
                        price += Number($(this).find("[name='ticketSkuMoney']").val());
                    } 
                });
                $("#firstScanPrize").find("#totalNum").text(num.toFixed(0))
                $("#firstScanPrize").find("#totalPrice").text(price.toFixed(2))
         	}
         	
         	
            	
         	 	//店内码模糊匹配  
    			 $('.alertChart').closest("tr").find("[id='terminalInsideCode']").keyup(function(){   
    				 var div = $(this).closest("tr").find("div.div_items");
    				 var item = $(this).closest("tr").find("div.div_item");
    				 div.css('display', 'block');
    				 //只要输入就显示列表框
                     if ($(this).val().length <= 0) {
                    	 item.css('display', 'block');//如果什么都没填，跳出，保持全部显示状态
                       return;
                     }
                
                    $(this).closest("tr").find("[id='skuKey']").val('')
                    $(this).closest("tr").find("[name='skuNum'],[id='ticketSkuUnitMoney'],[id='ticketSkuMoney']").val('')
    	     		$(this).closest("tr").find('.selectpicker').selectpicker('val',"");
                    var formData = new FormData();            
                    var myForm = new FormData(document.getElementById("code_form"));
     	            formData.append("insideCodeType", myForm.get("insideCodeType"));
     	            formData.append("ticketTerminalName", myForm.get("ticketTerminalName"));
     	            formData.append("terminalInsideCode", $(this).val());
     	            //清掉下拉框值
     	            item.remove(); 
                 	$.ajax({
                         type: "POST",
                         url : '<%=cpath %>/smallticketAction/searchLst.do',
                         async: false,
                         contentType : false,
                         processData: false,
                         data: formData,
                         dataType: "json",
                 		 beforeSend:appendVjfSessionId,
                 		 success: function(result){
                             if(result){
                                 $.each(result, function(i, item) {
                                	 div.append('<div class="div_item"  data-value='+item.terminalInsideCode  + "," + item.ticketSkuName+ "," +  item.skuKey+ "," + item.ticketSkuUnitMoney+ '>'+item.terminalInsideCode+'</div>')                            	
                                 });
                             }
                         },
                         error: function(data){
                             $.fn.alert("预览错误!请联系研发2");
                             return;
                         }
                     });
                 	 item.css('display', 'none');//如果填了，先将所有的选项隐藏
                     for (var i = 0; i < item.length; i++) {
                       //模糊匹配，将所有匹配项显示
                       if (item.eq(i).text().indexOf($(this).val())>=0 ) {
                    	   item.eq(i).css('display', 'block');
                       }
                     }
                     calculateTotalMoney();
                 });     			
    	
    			 //商品名称模糊匹配  
    			 $('.alertChart').closest("tr").find("[id='ticketSkuName']").keyup(function(){
    				 var div = $(this).closest("tr").find("div.div_ticket");
    				 var item = $(this).closest("tr").find("div.div_ticke");
    				 div.css('display', 'block');
                     if ($(this).val().length <= 0) {
                    	 item.css('display', 'block');
                         return;
                     }
    
                    $(this).closest("tr").find("[id='skuKey']").val('')
                    $(this).closest("tr").find("[name='skuNum'],[id='ticketSkuUnitMoney'],[id='ticketSkuMoney']").val('')
    	     		$(this).closest("tr").find('.selectpicker').selectpicker('val',"");
                    var formData = new FormData();            
                    var myForm = new FormData(document.getElementById("code_form"));
     	            formData.append("insideCodeType", myForm.get("insideCodeType"));
     	            formData.append("ticketTerminalName", myForm.get("ticketTerminalName"));
     	            formData.append("ticketSkuName", $(this).val());
     	            item.remove(); 
                 	$.ajax({
                         type: "POST",
                         url : '<%=cpath %>/smallticketAction/searchLst.do',
                         async: false,
                         contentType : false,
                         processData: false,
                         data: formData,
                         dataType: "json",
                 		 beforeSend:appendVjfSessionId,
                 		 success: function(result){
                             if(result){
                                 $.each(result, function(i, item) {
                                	 div.append('<div class="div_ticke"  data-value='+item.terminalInsideCode  + "," + item.ticketSkuName+ "," +  item.skuKey+ "," + item.ticketSkuUnitMoney+ '>'+item.ticketSkuName+'</div>')                                        	
                                 });
                             }
                         },error: function(data){
                             $.fn.alert("预览错误!请联系研发3");
                             return;
                         }
                     });
                 	 item.css('display', 'none');
                     for (var i = 0; i < item.length; i++) {
                       if (item.eq(i).text().indexOf($(this).val())>=0 ) {
                    	   item.eq(i).css('display', 'block');
                       }
                     }
                     calculateTotalMoney();
                 });
                
    	 		 //移出模糊查询框
    			 hide();
        	
    			//点击赋值
    			 $(document).on("click",".div_item,.div_ticke",function(){
    	     			var restrict = $(this).attr("data-value");
    	     			$(this).parents("tr").eq(0).children().eq(1).children().eq(0).val(restrict.split(",")[0]);  
    	     			$(this).parents("tr").eq(0).children().eq(2).children().eq(0).val(restrict.split(",")[1]);
//     	     			$(this).parents("tr").eq(0).children().eq(3).children().eq(0).find("option").prop("selected", false);
//     	     			 $($(this).parents("tr").eq(0).children().eq(3).children().eq(0).find("option")).each(function(){	   
//     	     				if($(this).val() == restrict.split(",")[2]){
//     	      				   $(this).prop("selected",true);
//     	      				 $('.selectpicker').selectpicker('val','mustard');
//     	     				} 
//     	     			});
    	     			$(this).parents("tr").eq(0).find('.selectpicker').selectpicker('val',restrict.split(",")[2]);
    	     			$(this).parents("tr").eq(0).children().eq(4).children().eq(0).val('1');
    	     			$(this).parents("tr").eq(0).children().eq(5).children().eq(0).val(restrict.split(",")[3]);
    	     			<!--重新计算 -->
    	     			$triggerObj = $(this).closest("tr").find("input[name='skuNum']");
    	                $triggerObj.trigger("input");	
    	                $triggerObj.trigger("change");	
    	        })
    			 
    			 //清除table
    			 $('#insideCodeType').change(function() {
    				 console.log($("input[name='clearFlag']").val())
    				 if(${!empty vpsTicketRecord.detailLst}){
    					if($("input[name='clearFlag']").val() != '请选择'){
    						 clearTable();
    	    				 <!--重新计算 -->
    	    	     		 $triggerObj = $(this).closest("tbody").find("input[name='skuNum']:first");
    	    	             $triggerObj.trigger("change");	
   	    				 }
   					 }	 
    				 $("input[name='clearFlag']").val($("#insideCodeType").find("option:selected").text());
    			});
    			 
    			
    			 //监听keyUp事件
    			 $(document).keyup(function(event){
    				 if(event.keyCode == 13){
    					 event.preventDefault();
    				 }
    			 })
         		//监听图片切换事件
         		 $("#myPic").click(function() {
         				if($("#pictd").is(':visible')== true){
         					$("#pictd").hide(); 
         					$("#texttd").show();
    			 		}else{
    			 			$("#pictd").show(); 
         					$("#texttd").hide();
    			 		}
    	       	});	
    			
    			// 增加不通过拉框 submint
    	          $("form").on("click", "#addreason", function() {
    	              var idx = $("td.activity div").index($(this).parent("div.activity"));
    	               if (idx == 0) {
    	                    var count = $("td.activity div").length;
    	                    if(count < 10){
    	                        var $activityCopy = $("div.activity").eq(0).clone(true);
    	                        $("td.activity").append($activityCopy);
    	                        $activityCopy.find("#addreason").text("删除");
    	                        var $activity = $("td.activity div.activity:last").find("input[name='reasonKyes']");
    	                        $activity.attr("id", "reasonKyes" + count).val("");
    	                    }
    	                } else {
    	                    $(this).parent("div.activity").remove();
    	                }
    	            });
    	          	$("form").on("click", "#addsubmint", function() {
    	              var idx = $("td.submint div").index($(this).parent("div.submint"));
    	               if (idx == 0) {
    	                    var count = $("td.submint div").length;
    	                    if(count < 10){
    	                        var $activityCopy = $("div.submint").eq(0).clone(true);
    	                        $("td.submint").append($activityCopy);
    	                        $activityCopy.find("#addsubmint").text("删除");
    	                        var $activity = $("td.submint div.submint:last").find("input[name='submintKyes']");
    	                        $activity.attr("id", "submintKyes" + count).val("");
    	                    }
    	                } else {
    	                    $(this).parent("div.submint").remove();
    	                }
    	            });
    	          									
    	          	<!--校验流水号以及门店名称 -->
    	          	$("#ticketTerminalName,#ticketNo").blur(function(){
    	          		var searchType =($("#ticketTerminalName").index(this) == '0')?'1':'2';
    	          	   	var formData = new FormData();            
                       	var myForm = new FormData(document.getElementById("code_form"));
        	            formData.append("ticketTerminalName", myForm.get("ticketTerminalName"));
        	            formData.append("terminalKey", myForm.get("terminalKey"));
        	            formData.append("ticketNo", myForm.get("ticketNo"));
        	            formData.append("searchType", searchType);
        	            formData.append("province", myForm.get("province"));
        	            formData.append("city", myForm.get("city"));
        	            formData.append("county", myForm.get("county"));
        	            formData.append("infoKey", myForm.get("infoKey"));
        	            $.ajax({
                            type: "POST",
                            url : '<%=cpath %>/smallticketAction/checkTicket.do',
                            async: false,
                            contentType : false,
                            processData: false,
                            data: formData,
                            dataType: "json",
                    		 beforeSend:appendVjfSessionId,                    		
                    		 success: function(result){
                             	<!--门店不存在 -->
                                console.log(result)
                                if(searchType=='1'){
                                	if(result == null){
//                                 		$.fn.alert("门店不存在");
                                    	$("input[name='terminalKey']").val('');
                                	}else{
                                		$("input[name='terminalKey']").val(result);
                                	}
                                <!--流水号重复驳回 -->	
                                }else if(searchType=='2' && result > 0){
                                 	$.fn.alert("流水号重复");  
                                 }
                            },error: function(data){
                                $.fn.alert("预览错误!请联系研发1");
                                return;
                            }
                        });
    	          	});
    	            // 小票单号不为空，校验是否重复
    	            if ("${vpsTicketRecord.ticketNo}" != '') {
    	                $("#ticketNo").triggerHandler("blur");
    	            }
				}	
		
	
		
		
		function hide(){
			$("body").click(function () {  
				 $('.div_items').css('display', 'none');  
				 $('.div_ticket').css('display', 'none'); 
	       });			
		}
		
		function clearTable(){
			$('.alertChart').each(function(i){  
			 if($(this).closest("tr").find("#NO").text() == "1"){
				 $(this).closest("tr").find("#terminalInsideCode").val('');
				 $(this).closest("tr").find("#ticketSkuName").val('');
				 $(this).closest("tr").find("#skuKey").val('');
				 $(this).closest("tr").find("#skuNum,#ticketSkuUnitMoney, #ticketSkuMoney").val('');
			 }else{
				 $(this).closest("tr").remove();
			 }   			            
		 	}) 					
		}	
		
		function isRepeat(arr){     
		    var obj = {};
		    for(var i in arr) { 
		        //存在重复值
		        if(obj[arr[i]])  return true; 
		        obj[arr[i]] = true; 
		    } 
		    //不重复
		    return false; 
		} 
		
		function validForm() {
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			var returnFlag = false;
			
			if($("input[name='ticketStatus']:checked").val() != '1'){
				if($("input[name='ticketTerminalName']").val() == ''){
					$.fn.alert("门店名称不能为空!");
					return false;	
				}
				if($("input[name='ticketNo']").val() == ''){
					$.fn.alert("流水号不能为空!");
					return false;	
				}
				
				if(Number($("#totalPrice").text()) <= 0) {
	                $.fn.alert("活动商品金额合计不能为小于等于0");
	                return false;;  
				}
				
				if ($(":input[name='promotionEarnFlag']:checked").size() == 0) {
	                $.fn.alert("请选择是否给促销员返利");
	                return false;;  
				}	
			}

			var ticketChannel = $("#ticketChannel option:selected").val();
			if(ticketChannel == ''){
				$.fn.alert("请选择上传渠道!");
				return false;	
			} 
			if (ticketChannel == '1' && $(":input[name='promotionEarnFlag']:checked").val() == "1") {
				$.fn.alert("餐饮渠道不参与促销员返利!");
				return false;	
			}
			
			$("input[name='goodsNum']").val($("#firstScanPrize").closest("tbody").find("#totalNum").text());
			$("input[name='goodsMoney']").val($("#firstScanPrize").closest("tbody").find("#totalPrice").text());
			
			// 组装驳回原因以及送审原因
			var activityAry = "";
			var submintAry = "";
			$("td.activity div.activity").each(function(i){
            	var $activity = $(this).find("select[name='reasonKyes']");
            	if($activity.val() != ""){
            	if(activityAry.indexOf($activity.val()) == -1){
	            	activityAry += $activity.val() + ",";
            	  }                    	
            	}
            });
			$("td.submint div.submint").each(function(i){
            	var $submint = $(this).find("select[name='submintKyes']");
            	if($submint.val() != ""){
            	if(submintAry.indexOf($submint.val()) == -1){
            		submintAry += $submint.val() + ",";
            	  }                    	
            	}
            });
			
			if($("input[name='ticketStatus']:checked").val() == '1'){
				if(activityAry != ""){
					$("input[name='dismissReason']").val(activityAry.substring(0, activityAry.length - 1));
				}else{
					returnFlag = true;
					$.fn.alert("请选择驳回原因!");	
				}
			};

			if($("input[name='ticketStatus']:checked").val() == '3'){
				if(submintAry != ""){
					$("input[name='submintCheckReason']").val(submintAry.substring(0, submintAry.length - 1));				
				}else{
					returnFlag = true;
					$.fn.alert("请选择送审原因!");				
				}
			};
			
			//检查商品列表
			var arr=[];
		if($("input[name='ticketStatus']:checked").val() == '2'){
			$('.alertChart').each(function(i){  
				var no = $(this).closest("tr").find("#NO").text();
				 if($(this).closest("tr").find("#ticketSkuName").val()==''){
					 $.fn.alert("第"+no+"行商品名称不能为空!");
					 returnFlag = true;;
				 }
				 if($(this).closest("tr").find("#skuKey").val()==''){
					 $.fn.alert("第"+no+"行活动产品名称不能为空!");
					 returnFlag = true;;
				 }
				 if($(this).closest("tr").find("#skuNum").val()==''){
					 $.fn.alert("第"+no+"行活动数量不能为空!");
					 returnFlag = true;;
				 }
				 if($(this).closest("tr").find("#ticketSkuUnitMoney").val()==''){
					 $.fn.alert("第"+no+"行商品价格不能为空!");
					 returnFlag = true;;
				 }
				 if($(this).closest("tr").find("#ticketSkuMoney").val()==''){
					 $.fn.alert("第"+no+"行商品价格合计不能为空!");
					 returnFlag = true;;
				 }
				 if($(this).closest("tr").find("#terminalInsideCode").val() != ''){
				 	arr.push($(this).closest("tr").find("#terminalInsideCode").val());						 
				 }
			});
			if(isRepeat(arr)) {
				$.fn.alert("商品条码重复,请重新填写!");
				returnFlag = true;;
			}
		}	
		//送审只校验名称
		if($("input[name='ticketStatus']:checked").val() == '3'){
			$('.alertChart').each(function(i){  
				var no = $(this).closest("tr").find("#NO").text();
				var value = $(this).closest("tr").find("#ticketSkuName").val();
				console.log(value)
				if(value==''|| value == undefined){
					$.fn.alert("第"+no+"行商品名称不能为空!");
					returnFlag = true;;
				}				
			});
		}
			if(returnFlag){
				return false;	
			}
				
			return true;
		}
	</script>
	
	<style>
	
	
	
	 #div_items {  
             position: relative;  
             height: 100px;  
             border: 1px solid #003300;  
             border-top: 0px;  
             overflow: auto;  
             display: none;  
         }  
        .div_item {  
              width: 100%;  
              height: 20px;  
              margin-top: 3px;  
              font-size: 13px;  
              text-align:left;  
        }  
      #div_ticket {  
             position: relative;  
             height: 100px;  
             border: 1px solid #003300;  
             border-top: 0px;  
             overflow: auto;  
             display: none;  
         } 
         .div_ticke {  
              width: 100%;  
              height: 20px;  
              margin-top: 3px;  
              font-size: 13px;  
              text-align:left;  
          }  
      
	
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
            <li class="current"><a> 首页</a></li>
            <li class="current"><a> 小票审核</a></li>
            <li class="current"><a> 审核列表</a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/smallticketAction/doTicketRecordEdit.do">
            	<div class="widget box activityinfo">
	            	<!-- 促销员信息 -->
	            	<c:if test="${not empty promotionUser}">
	                <div class="widget-header"><h4><i class="iconfont icon-daoru"></i>促销员信息</h4></div>
	                <div class="widget-content panel no-padding">
	                    <table class="active_board_table">
	                        <tr>
	                            <td class="ab_left"><label class="title">促销员姓名：<span class="required">*</span></label></td>
	                            <td class="ab_main" style="width: 25% !important;">
	                                <div class="content">
	                                    <span class="blocker en-larger">${promotionUser.userName}</span>
	                                </div>
	                            </td>
	                            <td class="ab_left"><label class="title">手机号：<span class="required">*</span></label></td>
	                            <td class="ab_main" >
	                                <div class="content">
	                                    <span class="blocker en-larger">${promotionUser.phoneNum}</span>
	                                </div>
	                            </td>
	                       </tr>
	                        <tr>
	                            <td class="ab_left"><label class="title">所属终端：<span class="required">*</span></label></td>
	                            <td class="ab_main"  style="width: 25% !important;">
	                                <div class="content">
	                                <c:choose>
	                                    <c:when test="${not empty(vpsTicketRecord.promotionTerminalName) && vpsTicketRecord.promotionTerminalName ne promotionUser.terminalName }">
	                                        <span id="" class="blocker en-larger" data-terminalname="${promotionUser.terminalName}">${promotionUser.terminalName}(${vpsTicketRecord.promotionTerminalName})</span>
	                                    </c:when>
	                                    <c:otherwise><span id="promotionTerminalName" class="blocker en-larger" data-terminalname="${promotionUser.terminalName}">${promotionUser.terminalName}</span></c:otherwise>
	                                </c:choose>
	                                </div>
	                            </td>
	                            <td class="ab_left"><label class="title">所属体系：<span class="required">*</span></label></td>
	                            <td class="ab_main" >
	                                <div class="content">
	                                    <span id="promotionTerminalSystem" class="blocker en-larger" data-terminalsystem="${promotionUser.terminalSystem}">${promotionUser.terminalSystem}</span>
	                                </div>
	                            </td>
	                       </tr>
	                        <tr>
	                            <td class="ab_left"><label class="title">门店地址：<span class="required">*</span></label></td>
	                            <td class="ab_main" colspan="3">
	                                <div class="content">
	                                    <span class="blocker en-larger">${promotionUser.province}${promotionUser.city}${promotionUser.county}${promotionUser.terminalAddress}</span>
	                                </div>
	                            </td>
	                       </tr>
	                    </table>
	                </div>
	                </c:if>
<!-- 	                小票信息 -->
               		<div class="widget-content panel no-padding">
                           <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                  id="dataTable_data" style="overflow: auto;">
                               <thead>
                               <tr>
                                   <div class="content" class="required">
                                       <th >小票照片 
                                       <i id="myPic" name="myPic" class="iconfont icon-jinggao" style="color: red; font-size: 20px;"></i>
                                       <th >小票信息
                                   </div>
                               </tr>
                               </thead>
                               <tbody>
                               <tr>
                              		<input type="hidden" name="province" value="${vpsTicketRecord.province}"/>
                              		<input type="hidden" name="city" value="${vpsTicketRecord.city}"/>
                              		<input type="hidden" name="county" value="${vpsTicketRecord.county}"/>
                              		<input type="hidden" name="infoKey" value="${vpsTicketRecord.infoKey}"/>
                              		<input type="hidden" name="terminalKey" value="${vpsTicketRecord.terminalKey}"/>
                              		<input type="hidden" name="createTime" value="${vpsTicketRecord.createTime}"/>
                              		<input type="hidden" name="createUser" value="${vpsTicketRecord.createUser}"/>
                              		<input type="hidden" name="projressName" value="${projressName}"/>
                              		<input type="hidden" name="warAreaName" value="${vpsTicketRecord.warAreaName}"/>
                              		<input type="hidden" name="userKey" value="${vpsTicketRecord.userKey}"/>
                              		<input type="hidden" name="promotionUserKey" value="${vpsTicketRecord.promotionUserKey}"/>
                              		<input type="hidden" name="goodsMoney"/>
                              		<input type="hidden" name="goodsNum"/>
                              		<input type="hidden" name="clearFlag"/>
                                   <td style="text-align:center;" id ="pictd" name = "pictd">
                                   	<a href="${vpsTicketRecord.ticketUrl}" target="_blank">
                                       <span><img src="${vpsTicketRecord.ticketUrl}" width=300 height=300 text-align:center;"></span>
                                       </a>
                                   </td>
                                   <td style="display:none" id ="texttd" name = "texttd">            
                                       <span id="text" name="text" style="width:300px;height:300px;display:block;overflow:auto;">${vpsTicketRecord.ocrContent}</span>
                                   </td>             	                      
                       			<td>	   
            			
                       				  <div class="row">
		            						<div class="col-md-12 ">
                           						<div class="form-group little_distance search" style="padding-bottom: 5px;">
		                        					<label class="control-label" style="width: 10%;" >门店名称：</label>
                               						<div class="search-ticket">
		                        								<input name="ticketTerminalName" id="ticketTerminalName" value= "${vpsTicketRecord.ticketTerminalName}"  class="form-control input-width-larger" autocomplete="off" maxlength="100"/>
                               						</div>               						                     
                                					<label class="control-label" style="width: 10%;">店内体系码：</label>
                               						<div class="search-ticket">
                                							<select style="width:29px" name="insideCodeType" id="insideCodeType" class="form-control input-width-larger selectpicker" autocomplete="off" data-live-search="true">
                                    						<option value="">请选择</option>
                                    							<c:if test="${!empty insideLst}">
                                               						<c:forEach items="${insideLst}" var="inside">
                                               							<option value="${inside.insideCodeType}" txt="${inside.insideCodeName}" <c:if test="${vpsTicketRecord.insideCode eq inside.insideCodeType}">selected</c:if>>${inside.insideCodeName}</option>
                                               						</c:forEach>
                                               					</c:if>
                                							</select>
                               						</div>
                          				 			</div>
		            						</div>
	            						</div>

                       				  <div class="row">
		            						<div class="col-md-12 ">
                           						<div class="form-group little_distance search" style="lpadding-bottom: 5px;">
		                        					<label class="control-label" style="width: 10%;">流&nbsp;水&nbsp;号&nbsp;&nbsp;：</label>
                               						<div class="search-ticket">
		                        						<input name="ticketNo" id="ticketNo"  value= "${vpsTicketRecord.ticketNo}" class="form-control input-width-larger" autocomplete="off" maxlength="100"/>
                               						</div> 
		                        					<label class="control-label" style="width: 10%;">地区：</label>
                               						<div class="search-ticket">
		                        						<label>${vpsTicketRecord.warAreaName}-${vpsTicketRecord.province}-${vpsTicketRecord.city}-${vpsTicketRecord.county}</label>
                               						</div>
                          				 			</div>
		            						</div>
	            						</div>
                       			
                       			 		<div class="row">
		            						<div class="col-md-12 ">
                           						<div class="form-group little_distance search" style="padding-bottom: 5px;">
		                        					<label class="control-label" style="width: 10%;">小票总金额：</label>
                               						<div class="search-ticket">
		                        							<input name="ticketMoney" value="${vpsTicketRecord.ticketMoney}" class="form-control  input-width-larger" autocomplete="off"  maxlength="10"/>
                               						</div> 
                                                       <label class="control-label" style="width: 10%;">上传渠道：</label>
                                                       <div class="content search-ticket">
															<select class="form-control input-width-larger" name="ticketChannel" id="ticketChannel" tag="validate">
																<option value="">请选择渠道</option>
																<option value="1" <c:if test="${vpsTicketRecord.ticketChannel eq '1'}">selected</c:if>>餐饮渠道</option>
																<option value="2" <c:if test="${vpsTicketRecord.ticketChannel eq '2'}">selected</c:if>>商超渠道</option>
															</select>
                                                       </div>
                          				 			</div>
		            						</div>
	            						</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                       					<div>	                       						
                                   		<table id="firstScanPrize" class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10" style="width: 95%; margin: 0 auto; text-align: center; overflow: auto !important">
                                       	<thead>
                                       	<tr>
                                           	<th style="width:5%; text-align: center;">序号</th>
                                           	<th style="width:6%; text-align: center;">商品条码</th>
                                           	<th style="width:6%; text-align: center;">商品名称</th>
                                           	<th style="width:6%; text-align: center;">活动产品名称</th>
                                           	<th style="width:11%; text-align: center;" class="cogItemVpoints">数量</th>
                                           	<th style="width:11%; text-align: center;" class="cogItemVpoints">单价</th>
                                           	<th style="width:11%; text-align: center;" class="cogItemVpoints">合计</th>
                                       	</tr>
                                       	</thead>
                                       	<tbody>

                                       	<c:choose>
                                           	<c:when test="${empty vpsTicketRecord.detailLst}">
                                               <tr class="alertChart">
                                                   <td>
                                                       <label id="NO" style="line-height: 30px;">1</label>
                                                   </td>
                                                   <td>
                                                       <input type="text" name="terminalInsideCode" id="terminalInsideCode"  class="form-control  input-width-wide"  autocomplete="off"
                                                            	   tag="validate"  maxlength="15" onkeyup="value=value.replace(/'/,'')" style="display: initial;">	
                                                       <!--模糊匹配窗口-->  
              							 				<div id="div_items" name ="div_items" class="div_items";autocomplete="off" >             						 						                 										 
               						 				</div> 
                                                   </td>
                                                   <td>
                                                       <input type="text" name="ticketSkuName" id="ticketSkuName" class="form-control input-width-larger"  autocomplete="off"
                                                                  tag="validate" maxlength="50" onkeyup="value=value.replace(/'/,'')" style="display: initial;">	
                                                       <!--模糊匹配窗口-->  
              							 				<div id="div_ticket" name ="div_ticket" class="div_ticket";autocomplete="off" >  		
               						 				</div>
                                                   </td>
                                                   <td class="skukey">                                                       
                                                       <select id="skuKey" name="skuKey" class="selectpicker form-control input-width-larger" data-live-search="true">
                                                       	<option value="">请选择</option>
                                                           <c:if test="${!empty skuLst}">
                                               				<c:forEach items="${skuLst}" var="sku">
                                               					<option value="${sku.skuKey}">${sku.skuName}</option>
                                               				</c:forEach>
                                               			</c:if>
                                                       </select>
                                                   </td>
                                                  <td class="cogItemVpoints">
                                                       	<input id= "skuNum" name="skuNum" class="form-control input-width-small number integer maxValue" value = "1"
                                                       		autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="3" style="display: initial; width: 50px !important;">	
                                                   </td>
                                                   <td style="position: relative;">
                                                           <input type="text" id="ticketSkuUnitMoney" name="ticketSkuUnitMoney" class="form-control input-width-small number maxValue"
                                                                  autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="999999" maxlength="7" style="display: initial; ">
                                                   </td>
                                                   <td style="position: relative;">
                                                           <input type="text" id="ticketSkuMoney" name="ticketSkuMoney" class="form-control input-width-small number maxValue"
                                                                  autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="999999" maxlength="7" style="display: initial; ">
                                                          <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                   </td>
                                               </tr>
                                           </c:when>
                                           <c:otherwise>
                                               <c:forEach items="${vpsTicketRecord.detailLst}" var="detail" varStatus="idx">
                                                   <tr class="alertChart">
                                                    <td>
                                                       <label id="NO" style="line-height: 30px;">${idx.count}</label>
                                                   </td>
                                                   <td>
                                                       <input type="text" name="terminalInsideCode"  id="terminalInsideCode" value="${detail.terminalInsideCode}"  class="form-control  input-width-wide"  autocomplete="off"
                                                            	   tag="validate"  maxlength="15" onkeyup="value=value.replace(/'/,'')" style="display: initial;">	
                                                       <!--模糊匹配窗口-->  
              							 				<div id="div_items" name ="div_items" class="div_items"  ;autocomplete="off" >             						 						                 										 
               						 				</div> 
                                                   </td>
                                                   <td>
                                                       <input type="text" name="ticketSkuName" id="ticketSkuName" value="${detail.ticketSkuName}" class="form-control input-width-larger"  autocomplete="off"
                                                                  tag="validate" maxlength="50" onkeyup="value=value.replace(/'/,'')" style="display: initial;">	
                                                       <!--模糊匹配窗口-->  
              							 				<div id="div_ticket" name ="div_ticket" class="div_ticket" ;autocomplete="off" >  		
               						 				</div>
                                                   </td>
                                                   <td class="skukey">                                                       
                                                       <select id="skuKey${idx.count}" name="skuKey" class="selectpicker form-control input-width-larger" data-live-search="true">
                                                       	<option value="">请选择</option>
                                                           <c:if test="${!empty skuLst}">
                                               				<c:forEach items="${skuLst}" var="sku">
                                               					<option value="${sku.skuKey}" <c:if test="${detail.skuKey eq sku.skuKey}">selected</c:if>>${sku.skuName}</option>
                                               				</c:forEach>
                                               			</c:if>
                                                       </select>
                                                   </td>
                                                  <td class="cogItemVpoints">
                                                       	<input id= "skuNum" name="skuNum" value="${detail.skuNum}"  class="form-control input-width-small number integer maxValue" value = "1"
                                                       		autocomplete="off" tag="validate" data-oldval="0" value="0" maxVal="999999" maxlength="3" style="display: initial; width: 50px !important;">	
                                                   </td>
                                                   <td style="position: relative;">
                                                           <input type="text" id="ticketSkuUnitMoney" value="${detail.ticketSkuUnitMoney}" name="ticketSkuUnitMoney" class="form-control input-width-small number maxValue money"
                                                                  autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="999999" maxlength="7" style="display: initial; ">
                                                   </td>
                                                   <td style="position: relative;">
                                                           <input type="text" id="ticketSkuMoney" name="ticketSkuMoney" value="${detail.ticketSkuMoney}" class="form-control input-width-small number maxValue money"
                                                                  autocomplete="off" tag="validate" data-oldval="0.00" value="0.00" maxVal="999999" maxlength="7" style="display: initial; ">
                                                          <label id="addPrizeItem" class="btn-txt-add-red" style="position: absolute; right: -28px; line-height: 30px;">新增</label>
                                                   </td>
                                                   
                                               </tr>
                                               </c:forEach>
                                           </c:otherwise>
                                       </c:choose>
                                       <tr>
                                           <td>合计</td>
                                           <td>--</td>
                                           <td>--</td>
                                           <td>--</td>
                                           <td><span id="totalNum" name="totalNum" data-currval="0" style="font-weight: bold;">0</span></td>
                                           <td>--</td>
                                           <td><span id="totalPrice" name="totalPrice " data-currval="0" style="font-weight: bold;">0</span></td>
                                       </tr>
                                       </tbody>
                                   </table>
								</div>
                       			</td>
                               </tr>
                               </tbody>
                           </table>
                       </div>
                		
                	<!-- 审核信息 -->
                	<div class="widget-header"><h4><i class="iconfont icon-daoru"></i>审核信息</h4></div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
                                <td class="ab_left"><label class="title">审核结果<span class="required">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div class="content">
                                        <input type="radio" class="radio" name="ticketStatus" id="ticketStatus2" value="2" checked="checked" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">通过</span>
                                        <input type="radio" class="radio" name="ticketStatus" id="ticketStatus1" value="1" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">不通过</span>
                                        <input type="radio" class="radio" name="ticketStatus" id="ticketStatus3" value="3" style="float:left; cursor: pointer; min-height: 33px;" />
                                        <span class="blocker en-larger">转发给客户审核</span>
                                        <label class="validate_tips"></label>
                                    </div>
                                </td>
                          </tr>
                		  <tr>
	                          <td class="ab_left"><label class="title">是否给促销员返利：<span class="required">*</span></label></td>
	                          <td class="ab_main" colspan="3">
	                              <div class="content">
	                                  <input type="radio" class="radio required" name="promotionEarnFlag" value="1" tag="validate" <c:if test="${empty promotionUser}"> disabled</c:if> style="float:left; cursor: pointer; min-height: 33px;" />
	                                  <span class="blocker en-larger">返利</span>
	                                  <input type="radio" class="radio required" name="promotionEarnFlag" value="0" tag="validate" <c:if test="${empty promotionUser}"> checked</c:if> style="float:left; cursor: pointer; min-height: 33px;" />
	                                  <span class="blocker en-larger">不返利</span>
	                                  <label class="validate_tips"></label>
	                              </div>
	                          </td>
                           </tr>
                           <!-- remark -->
                           	<tr name= "showRemark" id= "showRemark">
                                 <td class="ab_left"><label class="title">备注：</label></td>
                                		<td>
                                    	<div class="content">
                                        	<input class="form-control" maxlength="200" tag="validate" autocomplete="off" name="remark"></input>
                                        	<label class="validate_tips"></label>
                                    	</div>
                                		</td>
                            </tr>
                            <!-- 驳回原因 -->
                            <tr name="showdismissReason" id="showdismissReason" style="display: none">
	                       		<td class="ab_left"><label class="title">驳回原因： </label></td>
	                       		<td class="ab_main activity">
	                       			<input type="hidden" name="dismissReason" />
	                       			<div class="content activity" style="margin: 5px 0px; display: flex;">
	                       				<select class="form-control input-width-larger" id="reasonKyes" name="reasonKyes">
	                       					<option value="">请选择</option>
	                       					<option value="照片模糊">照片模糊</option>
	                       					<option value="人工涂改">人工涂改</option>
	                       					<option value="重复上传">重复上传</option>
	                       					<option value="无活动商品">无活动商品</option>
	                       					<option value="非活动期间小票">非活动期间小票</option>
	                       					<option value="缺少小票票头">缺少小票票头</option>
	                       					<option value="小票有遮挡">小票有遮挡</option>
	                       					<option value="上传渠道错误，请选择商超入口">上传渠道错误，请选择商超入口</option>
	                       					<option value="上传渠道错误，请选择餐饮入口">上传渠道错误，请选择餐饮入口</option>
	                       					<option value="请联系订单所属电商客服获取权益">请联系订单所属电商客服获取权益</option>
	                       					<option value="活动商品个数未达标">活动商品个数未达标</option>
	                       					<option value="小票不完整">小票不完整</option>
	                       					<option value="请上传购买单据">请上传购买单据</option>
	                       					<option value="请上传单张小票">请上传单张小票</option>
	                       					<option value="其他">其他</option>
	                       				</select>
	                       				<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addreason">新增</label>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>  	
	                       	 <!-- 转发给客户审核 -->
	                       	  <tr name="showsubmintCheckReason" id="showsubmintCheckReason" style="display: none">
	                       		<td class="ab_left"><label class="title">送审原因： </label></td>
	                       		<td class="ab_main submint">
	                       			<input type="hidden" name="submintCheckReason" />
	                       			<div class="content submint" style="margin: 5px 0px; display: flex;">
	                       				<select class="form-control input-width-larger" id="submintKyes" name="submintKyes">
	                       				    <option value="">请选择</option>
	                       					<option value="商品名称不符">商品名称不符</option>
	                       					<option value="门店名称不符">门店名称不符</option>
	                       				</select>
	                       				<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addsubmint">新增</label>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr> 
                		</table>
                	</div>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <button class="btn btn-blue btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;
                            <button type="button" class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/smallticketAction/showTicketList.do">返 回</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
    
    
    
<script>
</script>
  </body>
</html>
