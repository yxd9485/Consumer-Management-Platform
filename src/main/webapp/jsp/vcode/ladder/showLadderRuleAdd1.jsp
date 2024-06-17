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
    <title></title>
    
    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=8"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	
	<link href="<%=cpath%>/inc/vpoints/css/index.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/inc/vpoints/css/common.css" rel="stylesheet" type="text/css"/>
	<link href="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/css/bootstrap3/bootstrap-switch.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="<%=cpath%>/plugins/bootstrap-switch-3.3.4/dist/js/bootstrap-switch.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/vpoints/jquery-form.js"></script>
      <script type="text/javascript" src="<%=cpath %>/assets/js/plugins/zonesheets.js?v=3"></script>
      <script type="text/javascript" src="<%=cpath%>/inc/vpoints/imgUp.js?v=1.1.4"></script>
	
	<script>
        var basePath='<%=cpath%>';
        var allPath='<%=allPath%>';
        var imgSrc=[];
        var ruleKey;
        // 本界面上传图片要求
        var customerDefaults = {
            fileType         : ["jpg","png","bmp","jpeg"],   // 上传文件的类型
            fileSize         : 1024 * 200 // 上传文件的大小 200K
        };
        $(function(){
			// 初始化校验控件
			$.runtimeValidate($("form"));
			
			$("[data-toggle='popover']").popover();
			$('body').on('click', function(event) { 
				if ($("div.popover").size() > 0 
						&& $(event.target).closest("[data-toggle]").size() == 0 
						&& !($(event.target).is(".popover") || $(event.target).closest("div.popover").size() == 1)) {
		            $("[data-toggle='popover']").popover('toggle');
				}
            });
            // 初始化校验控件
            $.runtimeValidate($("#addBatchDialog"));
            // 新增
            $("#addItem").click(function(){
                var url = "<%=cpath%>/ladderRule/showLadderUIAdd.do?ruleKey="+ruleKey;
                $("form").attr("action", url);
                $("form").submit();
            });

            // 编辑
            $("a.edit").off();
            $("a.edit").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/ladderRule/showLadderRuleEdit.do?infoKey="+key;
                $("form").attr("action", url);
                $("form").submit();
            });



            // 删除
            $("a.del").off();
            $("a.del").on("click", function(){
                var key = $(this).parents("td").data("key");
                var url = "<%=cpath%>/qrcodeBatchInfo/doBatchInfoDelete.do?batchKey="+key;
                $.fn.confirm("确认删除吗？", function(){
                    $("form").attr("action", url);
                    $("form").submit();
                });
            });
			
			// 初始化功能
			initPage();
			
			// 增加活动下拉框
            $("form").on("click", "#addActivity", function() {
            	var idx = $("td.activity div").index($(this).parent("div.activity"));
                if (idx == 0) {
                    var count = $("td.activity div").length;
                    if(count < 10){
                        var $activityCopy = $("div.activity").eq(0).clone(true);
                        $("td.activity").append($activityCopy);
                        $activityCopy.find("#addActivity").text("删除");
                        var $activity = $("td.activity div.activity:last").find("input[name='vcodeActivityKey']");
                        $activity.attr("id", "vcodeActivityKey" + count).val("");
                    }
                } else {
                    $(this).parent("div.activity").remove();
                }
            });
            
            // 检验名称是否重复
            $("input[name='morescanName']").on("blur",function(){
            	var morescanName = $("input[name='morescanName']").val();
            	if(morescanName == "") return;
            	checkName(morescanName);
            });
            
            // 阶梯规则
            $("[name='ruleFlag']").on("change", function(){
            	if($(this).val() == '1'){
            		$(".ladderTimeTr").css("display","");
            		$(".ladderTimeTr input").attr("tag","validate");
            	}else if($(this).val() == '2'){
            		$(".ladderTimeTr").css("display","none");
            		$(".ladderTimeTr input").removeAttr("tag");
            	}
            });
            $("[name='ladderRuleFlag']").trigger("change");
            
		});
		
		function initPage() {
            // Tab切换
            $("div.tab-group a").on("click", function(){
                // 导航
                var tabIndex = $("div.tab-group a").index($(this));
                if (tabIndex != 0 && $(":hidden[name='infoKey']").val() == '') {
                    $.fn.alert("请先创建阶梯规则");
                } else if (tabIndex == 2 && $("div.tab-group a:eq(1)").data("saved") != "true") {
                    $.fn.alert("请先配置并保存风控规则！");
                } else {
                    $("ul.breadcrumb #currTab").text($(this).text());
                    $("ul.breadcrumb #currTab").data("tabindex", tabIndex);
                    $("div.activityinfo").css("display", tabIndex == 2 ? "none" : "");

                    // 按钮状态
                    $("div.tab-group a").removeClass("btn-red");
                    $(this).addClass("btn-red");

                    // 显示内容
                    $("div.tab-content").css("display", "none");
                    $("div.tab-content").eq(tabIndex).css("display", "");
                }

            });

            // 返回
			$(".btnReturn").click(function(){
				$("form").attr("action", $(this).data("url"));
				$("form").attr("onsubmit", "return true;");
				$("form").submit();
			});
            
            // // 保存
			// $(".btnSave").click(function(){
			// 	var flag = validForm();
			// 	if (flag) {
			// 		var url = $(this).attr("url");
			// 		$("form").attr("action", url);
			// 		$("form").attr("onsubmit", "return true;");
			// 		$("form").submit();
			// 	}
			// 	return false;
			// });
            // 保存活动信息及风控规则
            $(".btnSave").on("click", function(){
                // 输入元素校验
                $validContent = $(this).closest("div.tab-content");
                var validateResult = $.submitValidate($validContent);
                if(!validateResult){
                    return false;
                }

                // 识别次数校验
                // if ($validContent.find("input[name='sameMinuteRestrict']").size() > 0) {
                //     if (!checkDoubtCog()) {
                //         $.fn.alert("四种识别次数中至少一个要大于0");
                //         return false;
                //     }
                // }
                //
                // if ($validContent.find("input:radio[name='doubtfulTimeLimitType']:checked").size() > 0) {
                //     if(!checkDoubtLimit){
                //         $.fn.alert("可疑时间限制必须选择其中一个");
                //         return false;
                //     }}
                // 当前Tab
                var tabIndex = $("ul.breadcrumb #currTab").data("tabindex");

                // JSON
                var paramJson = {};
                $(this).closest("div.tab-content").find(":input:enabled[type!=hidden][name]").each(function(){
                    if($(this).attr("type") == "radio") {
                        if ($(this).is(":checked")) {
                            paramJson[$(this).attr("name")] = $(this).val();
                        }
                    } else {
                        paramJson[$(this).attr("name")] = $(this).val();
                    }
                });

                // 提交表单
                // var ruleKey = 1;
                //  ruleKey = $(":hidden[name='infoKey']").val();
                var url = "<%=cpath%>/ladderRule/doLadderRuleAdd.do";
                <%--if (vcodeActivityKey != "") {--%>
                <%--    url = "<%=cpath%>/vcodeActivity/doVcodeActivityEdit.do";--%>
                <%--    paramJson["vcodeActivityKey"] = vcodeActivityKey;--%>
                <%--}--%>
                $.ajax({
                    type: "POST",
                    url: url,
                    data: paramJson,
                    dataType: "json",
                    async: false,
                    success: function(data) {
                        if (data['errMsg'] != "保存成功") {
                            $.fn.alert(data['errMsg'], function(){
                                $("button.btn-primary").trigger("click");
                            });
                        } else {
                            // 活动添加成功后活动主键
                            if (data["infoKey"]) {
                                $(":hidden[name='infoKey']").val(data["infoKey"]);
                                $("#ruleKey").attr("value",data.infoKey);
                                $("#ruleKey").val("11111111")
                                $(" input[ name='ruleKey' ] ").val(data.infoKey) ;
                                ruleKey = data.infoKey;
                                document.getElementsByName('ruleKey').value = data.infoKey;
                                // $("#ruleFrame").attr("src", $("#ruleFrame").attr("src") + data["vcodeActivityKey"]);
                            }
                            // else {
                            //     $("#ruleFrame").attr("src", $("#ruleFrame").attr("src"));
                            // }
                            // 保存过风控规则
                            if (tabIndex == 1) {
                                $("div.tab-group a:eq(1)").data("saved", "true");
                            }
                            // 切换Tab
                            $("div.tab-group a").eq(tabIndex + 1).trigger("click");
                        }
                    }
                });

            });
		}
		
		// 检验名称是否重复
		var flagStatus = false;
		function checkName(morescanName){
			$.ajax({
				url : "${basePath}/ladderRule/checkBussionName.do",
			    data:{
			    		"infoKey":"",
			    		"bussionName":morescanName
			    	},
	            type : "POST",
	            dataType : "json",
	            async : false,
	            success : function(data){
	        	   if(data=="1"){
	        		   $.fn.alert("规则名称已存在，请重新输入");
						flagStatus = false;
					}else if(data=="0"){
						flagStatus = true;
					}
	            }
		  	});
		}

		function validForm() {
			var returnFlag = false;
			var validateResult = $.submitValidate($("form"));
			if(!validateResult){
				return false;
			}
			
			// 检验名称是否重复
			var morescanName = $("input[name='morescanName']").val();
        	if(morescanName == "") return false;
        	checkName(morescanName);
        	if(!flagStatus){
        		return false;
        	}
			
			// 组装活动KEY
			var activityAry = "";
			$("td.activity div.activity").each(function(i){
            	var $activity = $(this).find("select[name='vcodeActivityKey']");
            	if($activity.val() == ""){
            		$.fn.alert("指定活动不能为空!");
            		returnFlag = true;
            		return false; // 相当于break
            	}
            	if(activityAry.indexOf($activity.val()) == -1){
	            	activityAry += $activity.val() + ",";
            	}
            });

			if(returnFlag){
				return false;	
			}
			
			if(activityAry != ""){
				// 判断活动是否可配置
				var activityKeys = activityAry.substring(0, activityAry.length - 1);
				$.ajax({
					url : "${basePath}/ladderRule/checkActivityForLadder.do",
				    data:{
				    		"activityKeys":activityKeys,
				    		"ladderInfoKey":""
				    	},
		            type : "POST",
		            dataType : "json",
		            async : false,
		            success : function(data){
		        	   if(data!='0'){
		        		   $.fn.alert(data + "：活动已存在阶梯，请修改");
		        		   returnFlag = true;
						}else{
							returnFlag = false;
							$("input[name='vcodeActivityKeys']").val(activityKeys);
						}
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
            <li class="current"><a> 活动管理</a></li>
            <li class="current"><a> 阶梯规则</a></li>
            <li class="current"><a id="currTab" data-tabindex="0">新增阶梯规则</a></li>
        </ul>
    </div>
        <div class="row">
            <div class="col-md-12 tabbable tab-group">
                <a class="btn tab btn-red">新增阶梯规则</a>
                <a class="btn tab">前端样式</a>
            </div>
        </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form">
                <input type="hidden" name="queryParam" value="${queryParam}" />
                <input type="hidden" name="pageParam" value="${pageParam}" />
            	<div class="widget box">
                    <div class="tab-content">
            		<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>阶梯规则</h4>
                        <span class="marl10" title="温馨提示" data-html="true" 
                                data-container="body" data-toggle="popover" data-placement="auto" 
                                data-content="
                                <b>1</b>.阶梯规则影响本活动下所有阶梯配置规则，阶梯类型如果选择“无”，则阶梯配置表中有阶梯奖项，也不会中出。</br>
                                <b>2</b>.阶梯类型选择“每日”，可以选择每日的时间段，阶梯活动会在每日的该时间段累计瓶数，第二天重新计算。</br>
                                <b>3</b>.阶梯类型选择“时间段”，可以选择最长七天时间执行阶梯规则，在该时间段内累计瓶数。</br>
                                <b>4</b>.配置阶梯活动时，完全按阶梯活动时间创建规则，避免系统中遗留阶梯配置规则重复执行。</br>
                                &nbsp;&nbsp;&nbsp;例如：10月1日至7日做阶梯活动，创建一条单独规则设置阶梯奖项,“每日”和“时间段”都按此执行。</br>
                                <b>5</b>.阶梯活动结束时，请选择阶梯类型为“无”，以避免系统资源浪费。</br>
                                <b>6</b>.修改阶梯类型时，瓶数重新计算;不修改阶梯类型，仅修改该类型下时间/日期，瓶数不重新计算。">
                            <i class="iconfont icon-tixing" style="color: red; font-size: 20px;"></i>
                        </span>
            		</div>
                	<div class="widget-content panel no-padding">
                		<table class="active_board_table">
                			<tr>
	                       		<td class="ab_left"><label class="title">规则名称：<span class="required">*</span></label></td>
	                       		<td class="ab_main" colspan="3">
	                       			<div class="content">
	                       				<input name="ruleName" tag="validate" class="form-control required"
	                       					style="width: 330px;" autocomplete="off" maxlength="30" />
	                       				<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
	                       	<tr>
                        		<td class="ab_left"><label class="title mart5">日期范围：<span class="required">*</span></label></td>
                        		<td class="ab_main date">
                        			<div class="content date" style="margin: 5px 0px; display: flex;">
                                        <input name="startDate" id="startDate" class="Wdate form-control input-width-medium required preTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', maxDate:'#F{$dp.$D(\'endDate\')}'})"/>
                                        <span class="blocker en-larger">至</span>
                                        <input name="endDate" id="endDate" class="Wdate form-control input-width-medium required sufTime"
                                            tag="validate" autocomplete="off" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd', minDate:'#F{$dp.$D(\'startDate\')}'})"/>
		                                <label class="validate_tips"></label>
                        			</div>
                        		</td>
                        	</tr>


                			<tr>
                               <td class="ab_left"><label class="title">阶梯类型：<span class="required">*</span></label></td>
                               <td class="ab_main" colspan="3">
                                   <div class="content">
                                       <select name="ruleFlag" tag="validate" class="form-control input-width-larger required">
                                           <option value="1">每日</option>
                                           <option value="2">时间段</option>
                                       </select>
                                       <label class="validate_tips"></label>
                                   </div>
                               </td>
                           </tr>
                           <tr class="ladderTimeTr">
                               <td class="ab_left"><label class="title">阶梯时间：<span class="required">*</span></label></td>
                               <td class="ab_main" colspan="3">
                    			<div class="content">
                                    <input name="ladderStartTime" id="ladderStartTime" class="form-control input-width-medium Wdate preTime"
                                    onfocus="WdatePicker({dateFmt:'HH:mm:ss', maxDate:'#F{$dp.$D(\'ladderEndTime\')}'})" value="00:00:00" autocomplete="off"/>
                                    <span class="blocker en-larger">至</span>
                                    <input name="ladderEndTime" id="ladderEndTime" class="form-control input-width-medium Wdate sufTime"
                                    onfocus="WdatePicker({dateFmt:'HH:mm:ss', minDate:'#F{$dp.$D(\'ladderStartTime\')}'})" value="23:59:59" autocomplete="off"/>
                                    <label class="validate_tips"></label>
                    			</div>
                    		</td>
                           </tr>
                            <tr>
	                       		<td class="ab_left"><label class="title">联接活动：<span class="required">*</span></label></td>
	                       		<td class="ab_main activity">
	                       			<input type="hidden" name="vcodeActivityKeys" />
	                       			<div class="content activity" style="margin: 5px 0px; display: flex;">
	                       				<select class="form-control input-width-larger" id="vcodeActivityKey" name="vcodeActivityKey">
	                       					<option value="">请选择</option>
	                       					<c:forEach items="${activityList }" var="activity">
	                       						<option value="${activity.vcodeActivityKey }">${activity.vcodeActivityName }</option>
	                       					</c:forEach>
	                       				</select>
	                       				<label class="title mart5 btn-txt-add-red" style="font-weight: normal; margin-left: 5px;" id="addActivity">新增</label>
                                       	<label class="validate_tips"></label>
	                       			</div>
	                       		</td>
	                       	</tr>
                            <tr>
                                <td class="ab_left"><label class="title">阶梯红包与翻倍卡是否互斥：<span class="white">*</span></label></td>
                                <td class="ab_main" colspan="3">
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="allowanceaExclusiveFlag" value="0" style="float: left; height:20px; cursor: pointer;" checked="checked">&nbsp;不互斥
                                    </div>
                                    <div style="float: left; width: 80px; line-height: 25px;">
                                        <input type="radio" name="allowanceaExclusiveFlag" value="1" style="float: left; height:20px; cursor: pointer;">&nbsp;互斥
                                    </div>
                                    <label class="validate_tips"></label>
                                </td>
                            </tr>

                		</table>
                	</div>
<%--                    <div class="active_table_submit mart20">--%>
<%--                        <div class="button_place">--%>
<%--                            <button class="btn btn-red btnSave marr20" >保 存</button>&nbsp;&nbsp;&nbsp;&nbsp;--%>
<%--                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/ladderRule/showLadderRuleList.do">返 回</button>--%>
<%--                        </div>--%>
<%--                    </div>--%>
                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <a class="btn btn-blue btnSave">保存并下一步</a>
                            <button class="btn btnReturn btn-radius3"  data-url="<%=cpath%>/ladderRule/showLadderRuleList.do">返 回</button>
                        </div>
                    </div>
                    </div>

                <div class="tab-content" style="display: none;">
                    <div class="row">
                        <div class="col-md-2"><span style="font-size: 15px; color: white; font-weight: bold;">阶梯中奖 </span></div>
                        <div class="col-md-10 text-right">
                                <a id="addItem" class="btn btn-blue">
                                    <i class="iconfont icon-tianjiajiahaowubiankuang" style="font-size: 12px;"></i>&nbsp; 新增ui
                                </a>
                        </div>
                    </div>
                        <div class="widget-content form-inline">
                            <form class="listForm" method="post"
                                  action="<%=cpath%>/ladderRule/showLadderRuleAdd.do">
                                <input type="hidden" name="infoKey" />
                                <input type="hidden" name="activityType" value="${activityType}" />
                                <input type="hidden" class="tableTotalCount" value="${showCount}" />
                                <input type="hidden" class="tableStartIndex" value="${startIndex}" />
                                <input type="hidden" class="tablePerPage" value="${countPerPage}" />
                                <input type="hidden" class="tableCurPage" value="${currentPage}" />
                                <input type="hidden" class="tableOrderCol" value="${orderCol}" />
                                <input type="hidden" class="tableOrderType" value="${orderType}" />
                                <input type="hidden" name="queryParam" value="${queryParam}" />
                                <input type="hidden" name="pageParam" value="${pageParam}" />
                                <input type="hidden" name="tabsFlag" value="${tabsFlag }"/>
                            </form>
                        </div>
                    <div class="widget-content">
                        <div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
                            <table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart10"
                                   id="dataTable_data">
                                <thead>
                                <tr>
                                    <th style="width:4%;">序号</th>
                                    <th style="width:8%;" data-ordercol="rule_no">阶梯级数</th>
                                    <th style="width:18%;" data-ordercol="rule_name">中奖图片</th>
                                    <th style="width:12%;" data-ordercol="start_date">弹窗提示语</th>
                                    <th style="width:10%;" data-ordercol="rule_flag">创建时间</th>
                                    <th style="width:15%;">操作</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:choose>
                                    <c:when test="${fn:length(ladderUIList) gt 0}">
                                        <c:forEach items="${ladderUIList}" var="item" varStatus="idx">
                                            <tr>
                                                <td style="text-align:center; ">
                                                    <span>${idx.count}</span>
                                                </td>
                                                <td style="text-align:center;">
                                                    <span>${item.ladderNum}</span>
                                                </td>
                                                <td>
                                                    <span>${item.ladderPic}</span>
                                                </td>
                                                <td>
                                                    <span>${item.ladderContent}</span>
                                                </td>

                                                <td style="text-align:center;">
                                                    <span>${item.createTime}</span>
                                                </td>
                                                <td data-key="${item.infoKey}" style="text-align: center;">
                                                    <c:choose>
                                                        <c:when test="${currentUser.roleKey eq '4'}">
                                                            <a class="btn btn-xs view btn-red"><i class="iconfont icon-xinxi" style="font-size: 14px;"></i>&nbsp;查 看</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <a class="btn btn-xs edit btn-orange"><i class="iconfont icon-xiugai"></i>&nbsp;修 改</a>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <a class="btn btn-xs del btn-red"><i class="iconfont icon-lajixiang"></i>&nbsp;删 除</a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <tr>
                                            <td colspan="9"><span>查无数据！</span></td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                                </tbody>
                            </table>
                            <table id="pagination"></table>
                        </div>
                    </div>

                    <div class="active_table_submit mart20">
                        <div class="button_place">
                            <a class="btn btn-blue btnSave">预发布</a>
                        </div>
                    </div>
                </div>
                </div>
            </form>
        </div>
    </div>
    </div>
  </body>
</html>
