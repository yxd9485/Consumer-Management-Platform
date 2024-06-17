<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="com.dbt.framework.util.PropertiesUtil"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="java.util.List" %>
<%
    String cpath = request.getContextPath();
	String imagePathPrx = PropertiesUtil.getPropertyValue("image_path_prx");
	String pathPrefix = cpath + "/" + imagePathPrx;
%>
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <title>首页信息</title>

    <jsp:include page="/inc/Main.jsp"></jsp:include>
    <script type="text/javascript" src="<%=cpath%>/assets/js/tableFormValidate.js?v=2"></script>
	<script type="text/javascript" src="<%=cpath%>/inc/My97DatePicker/WdatePicker.js"></script>

	<script>
		$(function(){
			// 初始化校验控件
			$.runtimeValidate($("#code_form"));
			// 初始化功能
			initPage();

            btnChange(${popssInfo.isExtractLimit})
            
            // 修改父页面提醒消息
             if("${currentUser.roleKey ne '1' and currentUser.roleKey ne '2'}"){
            	 window.parent.$("#msgRemind").parents("a").css("display","none");
             }else{
            	 window.parent.$("#msgRemind").html("消息提醒:"+${totals});
             }
            
		});

		function toExpireRemind(){
           var url= "<%=cpath%>/expireRemindAction/expireRemindListCondition.do";

            $("form").attr("action", url);
            $("form").submit();
		}

		function initPage() {
			$("tr.doubt-rule").find("input.money").prop("disabled", true);
			$("tr.doubt-rule").find("input.number").prop("disabled", true);
			// 可疑用户
			$("tr.doubt-rule").delegate("input[type=radio]", "click", function(){
				var currentVal = $(this).val();
				if(currentVal == "1"){
					$("tr.doubt-rule:eq(0)").find("input.number").prop("disabled", false);
					$("tr.doubt-rule:eq(1)").find("input.money").prop("disabled", true);
					$("tr.doubt-rule:eq(1)").find("input.money").val("");
				} else {
					$("tr.doubt-rule:eq(1)").find("input.money").prop("disabled", false);
					$("tr.doubt-rule:eq(0)").find("input.number").prop("disabled", true);
					$("tr.doubt-rule:eq(0)").find("input.number").val("");
				}
			});


			$("select[name='skuKey']").change(function(){
				$(this).parents(".skuInfo").find(".validate_tips").text("");
				$(this).parents(".skuInfo").find(".validate_tips").removeClass("valid_fail_text");
			});


			// 按钮事件
			$(".button_place").find("button").click(function(){
				var btnEvent = $(this).data("event");
				if(btnEvent == "0"){
					var url = $(this).data("url");
					$("form").attr("onsubmit", "");
					$("form").attr("action", url);
					$("form").submit();
				} else {
				/*	var blacklistCogFlag = "0";
					$("input[mark=blacklist]:not([type=radio])").each(function(){
						if($(this).val() != ""){
							blacklistCogFlag = "1";
						}
					});
					$("input[name=blacklistFlag]").val(blacklistCogFlag);*/

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
		}

        function btnChange(values){
		    if(values ==0){
                $("input[name='firstCash']").attr("disabled","disabled");
                $("#firstTip").html("");
                $("input[name='secondCash']").attr("disabled","disabled");
                $("#secondTip").html("");
                $("input[name='firstCash']").val("")
                $("input[name='secondCash']").val("")
			}else{
                $("input[name='firstCash']").removeAttr("disabled");
                $("#firstTip").html("");
                $("input[name='secondCash']").removeAttr("disabled");
                $("#secondTip").html("");
			}
		}
		/*	$("#producteInfo").on("click", function () {
			var url ="skuInfo/showSkuInfoList.do";
			$("form").attr("action", url);
			$("form").submit();
			});*/
		function turnMenu(menuUrl,infoKey){
		    if("1"==infoKey){
		        
            }else{
                $.ajax({
                    type: "POST",
                    url: "<%=cpath%>/expireRemindAction/updateStatusForRead.do",
                    async: false,
                    data: {"keys":infoKey,"status":1},
                    dataType: "text",
                    beforeSend:appendVjfSessionId,
                    success:  function(result){

                    },
                });
            }
		    
		    //  活动操作步骤 start
		    if("producte" == menuUrl){
            window.parent.openMenu("skuInfoshowSkuInfoListdo");
            var url ="<%=cpath%>/skuInfo/showSkuInfoList.do";
            $("form").attr("action", url);
            $("form").submit();
            }
            if("qrCode"==menuUrl){
                window.parent.openMenu("vpsQrcodeOrdershowQrcodeOrderListdo");
                var url ="<%=cpath%>/qrcodeBatchInfo/showBatchInfoList.do";
                $("form").attr("action", url);
                $("form").submit();
            }
            if("activity"==menuUrl){
                window.parent.openMenu("vcodeActivityshowVcodeActivityListdo");
                var url ="<%=cpath%>/vcodeActivity/showVcodeActivityList.do?activityType=1&menuType=activity";
                $("form").attr("action", url);
                $("form").submit();
            }
            if("batchImport"==menuUrl){
                window.parent.openMenu("qrcodeBatchInfoshowBatchImportListdo");
                var url ="<%=cpath%>/qrcodeBatchInfo/showBatchImportList.do";
                $("form").attr("action", url);
                $("form").submit();
            }
            //  活动操作步骤 end
            //  提醒消息菜单栏跳转 start
              // 扫码活动
              if("0"==menuUrl){
                    window.parent.openMenu("vcodeActivityshowVcodeActivityListdo");
                }
                  // 一码双奖
                if("6"==menuUrl){
                    window.parent.openMenu("doubleprizeshowDoublePrizeListdo");
                }
                // 捆绑促销
                if("5"==menuUrl){
                    window.parent.openMenu("promotionshowBindPromotionListdo");
                }
                // 万能签到
                if("4"==menuUrl){
                    window.parent.openMenu("vcodeSigninshowVcodeSigninListdo");
                   
                }
            //  提醒消息菜单栏跳转 end
        }
	</script>

	<style>
		.form-horizontal .form-group {
			margin-right: 0px;
			margin-left: 18px;
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
			border-top: 5px solid rgb(240,240,240);
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
       .tdd {
            word-break: break-all;
            word-wrap: break-word;
        }
        .active_board_table .ab_left {
            width: 20%;
            text-align: right;
            margin: 6px;
            padding: 6px;
        }
		.piece-pop {
			float: left;
			margin: 1%;
			width: 18%;
			height: 130px;
			text-align: center;
			border-radius: 5px;
		}
		.piece-pop>label:nth-child(1) {
			height: 30px;
			width: 100%;
			padding: 20px;
			color: white;
			text-align: center;
			font-size: 18px !important;
			font-family: Arial,Verdana,Sans-serif;
		}
		.piece-pop>label:nth-child(2) {
			width: 100%;
			padding: 10px;
			color: white;
			text-align: bottom;
			font-size: 20px !important;
			font-family: Arial,Verdana,Sans-serif;
			font-weight: lighter;
			height: 36px;
		}

		.widget.box .widget-content.panell {
			margin-top: -3px;
			border-radius: 0px 0px 3px 3px;
			margin-bottom: 10px;
			padding: 10px !important;
		}
	</style>
  </head>

  <body>
    <div class="container">
    <div class="crumbs">
        <ul id="breadcrumbs" class="breadcrumb">
        	<li class="current"><a><%-- POPSS活动基础信息--%></a></li>
        </ul>
    </div>
    <div class="row">
        <div class="col-md-12 tabbable tabbable-custom">
            <form method="post" class="form-horizontal row-border validate_form" id="code_form"
            	action="<%=cpath%>/vpsWechatMovementActivityBaseInfo/savePopssActivityInfo.do">
            	<input type="hidden" name="infoKey" value="${popssInfo.infoKey}">
            	<div class="widget box">
            		<%--<div class="widget-header">
            			<h4><i class="iconfont icon-xinxi"></i>基础设置</h4>
            		</div>--%>
					<div class="row">
						<div class="piece-pop totalVpoints"style="background-color: #05D8B5">
							<label  >累计消耗积分数
							</label>
							<label >
								<c:if test="${!empty dayInfo.totalVpoints}">${dayInfo.totalVpoints}</c:if>
								<c:if test="${empty dayInfo.totalVpoints}">0</c:if>
							</label>
							<c:if test="${!empty dayInfo.signVpoints && dayInfo.signVpoints!='0'}">(+${dayInfo.signVpoints})</c:if>
						</div>
						<div class="piece-pop lastVpoints" style="background-color: #1BCCC2">
							<label  >省区剩余积分数</label>
							<label  >
								<c:if test="${!empty dayInfo.lastVpoints}">${dayInfo.lastVpoints}</c:if>
								<c:if test="${empty dayInfo.lastVpoints}">0</c:if>
							</label>
						</div>
						<div class="piece-pop totalParticipated" style=" background-color: #10BDCD;">
							<label  >${currentUser.projectServerName eq 'mengniu' ? '终端用户人数' : '消费者人数' }</label>
							<label  >
								<c:if test="${!empty dayInfo.totalUsers}">${dayInfo.totalUsers}</c:if>
								<c:if test="${empty dayInfo.totalUsers}">0</c:if>
							</label>
						</div>
						<div class="piece-pop totalReduceVpoints" style="background-color: #00A0DA;">
							<label  >${currentUser.projectServerName eq 'mengniu' ? '终端用户参与频次' : '消费者参与频次' }</label>
							<label  >
								<c:if test="${!empty dayInfo.totalScans}">${dayInfo.totalScans}</c:if>
								<c:if test="${empty dayInfo.totalScans}">0</c:if>
							</label>
						</div>
						<div class="piece-pop totalUsers" style="background-color: #127CEA;">
							<label  >省区剩余可用天数</label>
							<label  >
								<c:if test="${!empty dayInfo.lastDays}">${dayInfo.lastDays}</c:if>
								<c:if test="${empty dayInfo.lastDays}">0</c:if>
							</label>
						</div>
					</div>
                	<div class="widget-header top-only"><h4><i class="iconfont icon-saoma"></i>轻松完成四步</h4></div>
<%--                	<div class="widget-content panell no-padding">--%>
						<div class="row">
						<%--	<a href="<%=cpath%>/skuInfo/showSkuInfoList.do">--%>
							<div class="piece-pop totalVpoints"style="background-color: #05D8B5; margin-left: 5%;" onclick="turnMenu('producte','1')" id="producteInfo" >
                                <label  >第一步：产品配置</label>
							</div>
						<%--	</a>--%>
							<div class="piece-pop lastVpoints" style="background-color: #1BCCC2;margin-left: 5%;" onclick="turnMenu('qrCode','1')" id="qrCode">
								<label  >第二步：生成码源</label>

							</div>
							<div class="piece-pop totalParticipated" style=" background-color: #10BDCD;margin-left: 5%;" onclick="turnMenu('activity','1')" id="activity">
								<label  >第三步：活动配置</label>

							</div>
							<div class="piece-pop totalReduceVpoints" style="background-color: #00A0DA;margin-left: 5%;" onclick="turnMenu('batchImport','1')" id="batchImport">
								<label  >第四步：码源活动关联</label>

							</div>

						</div>
<%--                	</div>--%>

					<div class="widget-header top-only"><h4><i class="iconfont icon-jinggao"></i>最新提醒</h4></div>

					<div class="row">
						<div class="col-md-12">
							<div class="widget box">
								<div class="widget-content">
									<div role="grid" class="dataTables_wrapper form-inline" id="DataTables_Table_0_wrapper">
										<div class="row">
											<div class="dataTables_header clearfix">
												<form class="form-horizontal marl30 listForm" method="post"
													  action="<%=cpath%>/homeData/homeDataPage.do">
													<input type="hidden" class="tableTotalCount" value="${showCount}" />
													<input type="hidden" class="tableStartIndex" value="${startIndex}" />
													<input type="hidden" class="tablePerPage" value="${countPerPage}" />
													<input type="hidden" class="tableCurPage" value="${currentPage}" />
													<input type="hidden" name="pageParam" />
													<input type="hidden" name="queryParam" />
													<c:if test="${currentUser.roleKey eq '1' and  currentUser.roleKey eq '2'}">
														<div class="form-group text-left" style="border-top: 0px solid #ececec ; padding-top: 0px ; padding-bottom: 0px ; margin-bottom: 0 ;width: 70%;">
															<button type="button"  class="btn btn-res btn-radius3">到期提醒 ${totals}</button>
														</div>
														<div class="form-group text-right" style="border-top: 0px solid #ececec ; padding-top: 0px ; padding-bottom: 0px ; width: 20%;">
															<button type="button"  class="btn btn-res btn-radius3"  onclick="toExpireRemind()">查看更多</button>
														</div>
													</c:if>
												</form>
											</div>
										</div>
										<table class="table table-checkable table-striped table-hover table-bordered plus_end plus_top dt_table mart20"
											   id="dataTable_data">
                                            <input type="hidden" id="screenWidth1366" 
                                                   value="4,7,10,22,14,12,12,10,10" />
											<thead>
											<tr>
												<th style="width:4%; text-align: center;"><input type="checkbox" name="allInfoKey" id="allInfoKey" /></th>
												<th style="width:7%; text-align: center;">活动编号</th>
												<th style="width:10%; text-align: center;">活动类型</th>
												<th style="width:22%; text-align: center;">活动名称</th>
												<th style="width:14%; text-align: center;">活动时间</th>
												<th style="width:12%; text-align: center;">提醒类型</th>
												<th style="width:12%; text-align: center;">通知备注</th>
												<th style="width:10%; text-align: center;">收件时间</th>
												<c:if test="${transStatus != 2 and (currentUser.roleKey eq '1' or currentUser.roleKey eq '2')}">
													<th style="width:10%; text-align: center;">操作</th>
												</c:if>
											</tr>
											</thead>
											<tbody>
                                            
											<c:choose>
												<c:when test="${fn:length(msgList) gt 0}">
                                                
													<c:forEach items="${msgList}" var="expireRemind" varStatus="idx">
														<tr style="<c:if test="${expireRemind.readFlag == 0}">font-weight:bold;</c:if>">
															<td style="text-align: center;"><span><input type="checkbox" name="allInfoKey" id="allInfoKey" /></span></td>
															<td style="text-align: center;"><span>${expireRemind.activityNo}</span></td>
															<td style="text-align: center;">
															<span>
																<c:if test="${expireRemind.activityType == 0}">扫码活动</c:if>
																<c:if test="${expireRemind.activityType == 4}">万能签到活动</c:if>
																<c:if test="${expireRemind.activityType == 5}">捆绑升级活动</c:if>
																<c:if test="${expireRemind.activityType == 6}">一码双奖</c:if>
															</span>
															</td>
															<td style="text-align: center;"><span>${expireRemind.activityName}</span></td>
															<td style="text-align: center;">
																<span data-ref="remoney" data-value="${expireRemind.activityStartDate}-${recharge.activityEndDate}">${expireRemind.activityStartDate}至${expireRemind.activityEndDate}</span>
															</td>

															<td style="text-align: center;">
															<span>
																<c:if test="${expireRemind.msgType == 0}">规则到期</c:if>
																<c:if test="${expireRemind.msgType == 1}">剩余钱数</c:if>
																<c:if test="${expireRemind.msgType == 2}">剩余积分</c:if>
																<c:if test="${expireRemind.msgType == 3}">红包个数</c:if>
																<c:if test="${expireRemind.msgType == 4}">批次结束</c:if>
															</span>
															</td>
															<td style="text-align: center;"><span>${expireRemind.msgContent}</span></td>
															<td style="text-align: center;"><span>${expireRemind.updateTime}</span></td>
															<c:if test="${currentUser.roleKey eq '1' or currentUser.roleKey eq '2'}">
																<td data-key="${expireRemind.infoKey}" style="text-align: center;">
                                                                    <c:if test="${expireRemind.msgType eq 0}">
                                                                        <c:if test="${expireRemind.activityType eq 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进 入</a>
                                                                        </c:if>
                                                                        <c:if test="${expireRemind.activityType ne 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进 入</a>
                                                                        </c:if>

                                                                    </c:if>
                                                                    <c:if test="${expireRemind.msgType eq 1}">
                                                                        <c:if test="${expireRemind.activityType eq 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进 入</a>
                                                                        </c:if>
                                                                        <c:if test="${expireRemind.activityType ne 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进 入</a>
                                                                        </c:if>
                                                                    </c:if>
                                                                    <c:if test="${expireRemind.msgType eq 2}">
                                                                        <c:if test="${expireRemind.activityType eq 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进 入</a>
                                                                        </c:if>
                                                                        <c:if test="${expireRemind.activityType ne 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进 入</a>
                                                                        </c:if>
                                                                    </c:if>
                                                                    <c:if test="${expireRemind.msgType eq 3}">
                                                                        <c:if test="${expireRemind.activityType eq 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?vjfSessionId=${vjfSessionId}&childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                                                        </c:if>
                                                                        <c:if test="${expireRemind.activityType ne 0}">
                                                                            <a class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/vcodeActivityRebateRule/showRebateRuleListMain.do?vjfSessionId=${vjfSessionId}&tabsFlag=1&vcodeActivityKey=${expireRemind.vcodeActivityKey}&activityType=${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a>
                                                                        </c:if>
                                                                    </c:if>
                                                                    <c:if test="${expireRemind.msgType eq 4}"><a
                                                                            class="btn btn-xs enter btn-orange" onclick="turnMenu('${expireRemind.activityType}','${expireRemind.infoKey}')" href="<%=cpath%>/qrcodeBatchInfo/showBatchForActivityUsedList.do?vjfSessionId=${vjfSessionId}&infoKey=${expireRemind.vcodeActivityKey}&queryParam=,,,,,,,,,4" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>

                                                                <%--  <c:if test="${expireRemind.msgType == 0}"><a
                                                                        class="btn btn-xs enter btn-orange"href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType= ${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>
                                                                    <c:if test="${expireRemind.msgType == 1}"><a
                                                                            class="btn btn-xs enter btn-orange"href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType= ${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>
                                                                    <c:if test="${expireRemind.msgType == 2}"><a
                                                                            class="btn btn-xs enter btn-orange"href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType= ${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>
                                                                    <c:if test="${expireRemind.msgType == 3}"><a
                                                                            class="btn btn-xs enter btn-orange"href="<%=cpath%>/vcodeActivity/showVcodeActivityEdit.do?childTab=3&infoKey=${expireRemind.vcodeActivityKey}&activityType= ${expireRemind.activityType}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>
                                                                    <c:if test="${expireRemind.msgType == 4}"><a
                                                                            class="btn btn-xs enter btn-orange"href="<%=cpath%>/qrcodeBatchInfo/showBatchForActivityUsedList.do?infoKey=${expireRemind.vcodeActivityKey}" ><i class="iconfont icon-xiugai"></i>&nbsp;进入</a></c:if>
--%>
                                                                </td>
															</c:if>

													</tr>
												</c:forEach>
											</c:when>
											<c:otherwise>
												<tr>
														<td colspan="${transStatus != 2 ? 9: 7}"><span>查无数据！</span></td>
													</tr>
												</c:otherwise>
											</c:choose>
											</tbody>
										</table>
										<table id="pagination"></table>
									</div>
								</div>
							</div>
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
</html>
